package com.huong.mycinema.ui.editMovie;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.huong.mycinema.CinemaApp;
import com.huong.mycinema.R;
import com.huong.mycinema.models.MovieListData;
import com.huong.mycinema.networking.ApiUtils;
import com.huong.mycinema.util.KeyboardUtils;
import com.huong.mycinema.util.SharedPreference;
import com.huong.mycinema.util.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditMovieActivity extends CinemaApp {
    private MovieListData data;
    @BindView(R.id.imvCinemaEdit)
    ImageView imvCinemaEdit;
    @BindView(R.id.btnSPEdit)
    Button btnSPEdit;
    @BindView(R.id.edtMovieEdit)
    EditText edtMovieEdit;
    @BindView(R.id.spGenreMovieEdit)
    Spinner spGenreMovieEdit;
    @BindView(R.id.edtDayEdit)
    EditText edtDayEdit;
    @BindView(R.id.edtDesEdit)
    EditText edtDesEdit;
    @BindView(R.id.btnCMovieEdit)
    Button btnCMovieEdit;
    @BindView(R.id.rootEditMovie)
    LinearLayout root;

    String idCreator = null;
    String idMovie = null;
    String apiToken = null;
    File fileCurrent = null;
    private static final String IMAGE_DIRECTORY = "/echo";
    private int GALLERY = 1, CAMERA = 2;
    Calendar myCalendar;

    public static final String EXTRA_DATA = "EXTRA";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movie);
        getDeps().inject(this);
        ButterKnife.bind(this);
        getDataIntent();
        openDataPicker();
        apiToken = SharedPreference.getString(getApplication(), "ACCESS_TOKEN");

        btnCMovieEdit.setOnClickListener(view -> validateAndEditMovie());

        btnSPEdit.setOnClickListener(view -> openDialog());

        setTouchActivity();
    }

    private void setTouchActivity(){
        root.setOnTouchListener((view, motionEvent) -> {
            KeyboardUtils.hideKeyboard(EditMovieActivity.this);
            return true;
        });
    }

    private void validateAndEditMovie(){
        if (edtMovieEdit.getText().toString().trim().equals("")) {
            edtMovieEdit.setError("Chỗ này không được bỏ trống");
        } else if (edtDesEdit.getText().toString().trim().equals("")) {
            edtDesEdit.setError("Chỗ này không được bỏ trống");
        } else {
            editMovie();
        }
    }

    private void getDataIntent(){
        Intent i = getIntent();
        if (i != null) {
            Bundle b = i.getBundleExtra("BUNDLE");
            if (b != null) {
                data = (MovieListData) b.getParcelable("DATA");
                updateView(data);
            }
        }
    }

    private void updateView(MovieListData data) {
        if (data != null){
            edtDayEdit.setText(Utils.getConvertedTime(data.getReleaseDate()));
            edtDesEdit.setText(data.getContent());
            edtMovieEdit.setText(data.getName());
            setSpinText(spGenreMovieEdit, data.getGenre());
            idCreator = data.getCreatorId();
            idMovie = data.getId();
            String images = "https://cinema-hatin.herokuapp.com" + data.getPosterURL();
            Glide.with(this).load(images).into(imvCinemaEdit);
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            edtDayEdit.setText(dateFormat.format(data.getReleaseDate()));
        }
    }

    private void setSpinText(Spinner spin, String text) {
        for(int i= 0; i < spin.getAdapter().getCount(); i++) {
            if(spin.getAdapter().getItem(i).toString().contains(text)) {
                spin.setSelection(i);
            }
        }
    }

    private void editMovie(){
        final String sName = edtMovieEdit.getText().toString();
        final String sGenre = spGenreMovieEdit.getSelectedItem().toString();
        final String sDate = Utils.convertLongTime(edtDayEdit.getText().toString());
        final String sContent = edtDesEdit.getText().toString();

        RequestBody file;
        MultipartBody.Part fbody;
        if (fileCurrent != null){
            file= RequestBody.create(MediaType.parse("image/*"), fileCurrent);
            fbody= MultipartBody.Part.createFormData("file", fileCurrent.getName(), file);
        }else {
            fbody = MultipartBody.Part.createFormData("file","");
        }
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), sName);
        RequestBody creatorId = RequestBody.create(MediaType.parse("text/plain"), idCreator);
        RequestBody genre = RequestBody.create(MediaType.parse("text/plain"), sGenre);
        RequestBody releaseDate = RequestBody.create(MediaType.parse("text/plain"), sDate);
        RequestBody content = RequestBody.create(MediaType.parse("text/plain"), sContent);
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), idMovie);
        HashMap<String, RequestBody> meMap = new HashMap<String, RequestBody>();
        meMap.put("name", name);
        meMap.put("genre", genre);
        meMap.put("releaseDate", releaseDate);
        meMap.put("content", content);
        meMap.put("creatorId", creatorId);
        meMap.put("id", id);
        Call<JsonObject> call = ApiUtils.getService().editMovie(apiToken, meMap, fbody);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("Apitiny", response.toString());
                Intent data = new Intent();
                data.putExtra(EXTRA_DATA, idMovie);
                setResult(Activity.RESULT_OK, data);
                finish();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }

        });
    }

    private void openDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(EditMovieActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
                return;
            }
        }
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA);
        } else {
            //Request camera permission
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA },
                    2);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
//                    String path = saveImage(bitmap);
                    String pathToImage = contentURI.getPath()+".jpg";

                    String path = getPath(contentURI);
                    File file = new File(path);
                    fileCurrent = file;
                    imvCinemaEdit.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(EditMovieActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imvCinemaEdit.setImageBitmap(thumbnail);
            saveImage(thumbnail);
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            fileCurrent = f;
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());
            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        Integer column_index = cursor
                .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        String imagePath = cursor.getString(column_index);

        return cursor.getString(column_index);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(EditMovieActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case 2: {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA);
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }

    }

    private void openDataPicker() {
        myCalendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        edtDayEdit.setText(dateFormat.format(myCalendar.getTime()));

        final DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "MM/dd/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                edtDayEdit.setText(sdf.format(myCalendar.getTime()));
            }
        };

        edtDayEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final DatePickerDialog dp = new DatePickerDialog(EditMovieActivity.this, R.style.DialogTheme, dateListener,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dp.show();
            }
        });
    }
}

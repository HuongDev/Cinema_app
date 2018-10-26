package com.huong.mycinema.ui.createmovie;

import android.Manifest;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.huong.mycinema.CinemaApp;
import com.huong.mycinema.R;
import com.huong.mycinema.networking.ApiUtils;
import com.huong.mycinema.networking.Service;
import com.huong.mycinema.util.DialogUtils;
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

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateMovieActivity extends CinemaApp {

    //region Injection
    @Inject
    public Service service;

    //region BindView
    @BindView(R.id.edtDes)
    EditText edtDes;
    @BindView(R.id.edtMovie)
    EditText edtMovie;
    @BindView(R.id.edtOpeningDay)
    EditText edtOpeningDay;
    @BindView(R.id.spGenreMovie)
    Spinner spGenreMovie;
    @BindView(R.id.btnCreateMovie)
    Button btnCreateMovie;
    @BindView(R.id.btnSelectPhoto)
    Button btnSelectPhoto;
    @BindView(R.id.imvCinema)
    ImageView imvMovie;
    @BindView(R.id.rootCreateMovie)
    LinearLayout root;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    //endregion

    //region Variable
    MoviePresenter presenter;
    String IMAGE_PATH;
    private Uri mImageCaptureUri;

    File fileCurrent = null;
    Calendar myCalendar;

    private static final String IMAGE_DIRECTORY = "/echo";
    private int GALLERY = 1, CAMERA = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_movie);
        ButterKnife.bind(this);
        getDeps().inject(this);

        openDataPicker();
        setTouchActivity();

        btnCreateMovie.setOnClickListener((view) -> createMovie());

        btnSelectPhoto.setOnClickListener(view -> openDialog());
    }

    private void setTouchActivity() {
        root.setOnTouchListener((view, motionEvent) -> {
            KeyboardUtils.hideKeyboard(CreateMovieActivity.this);
            return true;
        });
    }

    private void createMovie(){
        final SweetAlertDialog pDialog = DialogUtils.showProgress(CreateMovieActivity.this);

        String nameMovie = edtMovie.getText().toString().trim();
        String description = edtDes.getText().toString();

        if (fileCurrent == null) {
            Toast.makeText(CreateMovieActivity.this, "Bạn phải chọn hình", Toast.LENGTH_SHORT).show();
            return;
        }
        if (nameMovie.trim().equals("")) {
            edtMovie.setError("Chỗ này không được bỏ trống");
        } else if (description.trim().equals("")) {
            edtDes.setError("Chỗ này không được bỏ trống");
        } else {

            //Loading Progress Bar
            pDialog.show();

            // create RequestBody instance from file
            String idCreator = SharedPreference.getString(getApplication(), "ID");
            RequestBody file = RequestBody.create(MediaType.parse("image/*"), fileCurrent);
            MultipartBody.Part fbody = MultipartBody.Part.createFormData("file", fileCurrent.getName(), file);
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), nameMovie);
            RequestBody creatorId = RequestBody.create(MediaType.parse("text/plain"), idCreator);
            RequestBody genre = RequestBody.create(MediaType.parse("text/plain"), spGenreMovie.getSelectedItem().toString());
            RequestBody releaseDate = RequestBody.create(MediaType.parse("text/plain"), Utils.convertLongTime(edtOpeningDay.getText().toString()));
            RequestBody content = RequestBody.create(MediaType.parse("text/plain"), edtDes.getText().toString());
            HashMap<String, RequestBody> meMap = new HashMap<String, RequestBody>();
            meMap.put("name", name);
            meMap.put("genre", genre);
            meMap.put("releaseDate", releaseDate);
            meMap.put("content", content);
            meMap.put("creatorId", creatorId);
            Call<ResponseBody> call = ApiUtils.getService().uploadMovie(meMap, fbody);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.d("Apitiny", response.toString());
                    pDialog.cancel();
                    Toast.makeText(CreateMovieActivity.this, "Đã upload thành công phim", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    pDialog.cancel();
                    Toast.makeText(CreateMovieActivity.this, "upload Lỗi", Toast.LENGTH_SHORT).show();
                }

            });
        }
    }

    private void openDataPicker() {
        myCalendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        edtOpeningDay.setText(dateFormat.format(myCalendar.getTime()));

        final DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "MM/dd/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                edtOpeningDay.setText(sdf.format(myCalendar.getTime()));
            }
        };

        edtOpeningDay.setOnClickListener(v -> {
            final DatePickerDialog dp = new DatePickerDialog(CreateMovieActivity.this, R.style.DialogTheme, dateListener,
                    myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
            dp.show();
        });
    }

    private void openDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
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
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CreateMovieActivity.this,
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
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                    mImageCaptureUri);
            startActivityForResult(intent, CAMERA);
        } else {
            //Request camera permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
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
                    String path = getPath(contentURI);
                    File file = new File(path);
                    fileCurrent = file;
                    imvMovie.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(CreateMovieActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imvMovie.setImageBitmap(thumbnail);
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
            IMAGE_PATH = f.getAbsolutePath();
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
                    Toast.makeText(CreateMovieActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case 2: {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA);
                return;
            }

        }

    }
}

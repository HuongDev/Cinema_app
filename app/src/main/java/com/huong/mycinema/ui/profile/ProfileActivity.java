package com.huong.mycinema.ui.profile;

import android.Manifest;
import android.app.ProgressDialog;
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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonObject;
import com.huong.mycinema.CinemaApp;
import com.huong.mycinema.ui.createmovie.CreateMovieActivity;
import com.huong.mycinema.R;
import com.huong.mycinema.ui.login.LoginActivity;
import com.huong.mycinema.models.MovieListData;
import com.huong.mycinema.models.response.MovieListResponse;
import com.huong.mycinema.networking.ApiUtils;
import com.huong.mycinema.models.response.ResponseData;
import com.huong.mycinema.util.SharedPreference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends CinemaApp {

    @BindView(R.id.btnChangePass)
    Button btnChangePass;
    @BindView(R.id.btnLogout)
    Button btnLogout;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.rcvMyMovie)
    RecyclerView rcvMyMovie;
    @BindView(R.id.imvAvatar)
    ImageView imvAvatar;
    @BindView(R.id.SRLProfile)
    SwipeRefreshLayout SRLProfile;


    //My post

    ArrayList<MovieListData> listData;
    String apiToken;

    private static final String IMAGE_DIRECTORY = "/echo";
    private int GALLERY = 1, CAMERA = 2;
    File fileCurrent = null;

    ProfileAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        getDeps().inject(this);
        requestApiMovie();

        apiToken = SharedPreference.getString(getApplication(), "ACCESS_TOKEN");

        SRLProfile.setOnRefreshListener(() -> {
            updateUserInfo();
            requestApiMovie();
        });
    }

    @OnClick(R.id.btnLogout)
    public void logoutApp(){
        checkLogoutUser();
    }

    @OnClick(R.id.tvName)
    public void clickName(){
        changeName(tvName.getText().toString());
    }

    @OnClick(R.id.imvAvatar)
    public void clickAvatar(){
        changeAvatar();
    }

    private void checkLogoutUser(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        SharedPreference.deleteString(getApplication(), "ACCESS_TOKEN");
                        Intent i = new Intent(getApplication(), LoginActivity.class);
                        startActivity(i);
                        finishAffinity();
                        finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.cancel();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setMessage("Bạn có muốn đăng xuất ?").setPositiveButton("Đồng ý", dialogClickListener)
                .setNegativeButton("Hủy", dialogClickListener).show();
    }

    private void changeName(final String oldName){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProfileActivity.this);
        alertDialog.setTitle("Thay đổi tên");
        alertDialog.setMessage("Nhập tên cần đổi vào ô trống");

        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int which) {
                        final String name = input.getText().toString();
                        if (name == "" || name.equals("")){
                            input.setError("Không được để trống");
                            return;
                        }
                        if (name.compareTo("") != 0) {
                            if (name.equals(oldName)) {
                                Toast.makeText(getApplicationContext(),
                                        "Đổi tên khác với tên ban đầu", Toast.LENGTH_SHORT).show();
                                return;

                            } else {
                                JsonObject body = new JsonObject();
                                body.addProperty("name", name);
                                Call<JsonObject> call = ApiUtils.getService().updateName(apiToken, body);
                                call.enqueue(new Callback<JsonObject>() {
                                    @Override
                                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                        if (response.isSuccessful()) {
                                            Log.d("Apitiny", response.toString());
                                            Toast.makeText(getApplicationContext(),
                                                    "Đổi tên thành công", Toast.LENGTH_SHORT).show();
                                            dialog.cancel();
                                            tvName.setText(name);
                                        }
                                        else {
                                            Toast.makeText(ProfileActivity.this, "Đổi tên thất bại", Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<JsonObject> call, Throwable t) {
                                        Toast.makeText(ProfileActivity.this, "Đổi tên thất bại", Toast.LENGTH_SHORT).show();
                                    }

                                });
                            }
                        }
                    }
                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    private void changeAvatar(){
        openDialog();
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
                ActivityCompat.requestPermissions(ProfileActivity.this,
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
                    imvAvatar.setImageBitmap(bitmap);
                    String path = getPath(contentURI);
                    File file = new File(path);
                    fileCurrent = file;
                    postImage();

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(ProfileActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imvAvatar.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            postImage();
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

        return cursor.getString(column_index);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
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
                    Toast.makeText(ProfileActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
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

    private void requestApiMovie(){
//        final SweetAlertDialog pDialog = DialogUtils.showProgress(ProfileActivity.this);
        Call<MovieListResponse> call = ApiUtils.getService().getMyMovie();
        call.enqueue(new Callback<MovieListResponse>() {
            @Override
            public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {
                Log.d("Apitiny", response.toString());
                MovieListResponse data = response.body();
                if (data != null){
                    adapter = new ProfileAdapter(ProfileActivity.this, myPostMovie(data.getData()));
                    LinearLayoutManager layoutManager = new LinearLayoutManager(ProfileActivity.this);
                    layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    rcvMyMovie.setLayoutManager(layoutManager);
                    rcvMyMovie.setAdapter(adapter);
                    SRLProfile.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<MovieListResponse> call, Throwable t) {
            }

        });
    }

    @OnClick(R.id.imvBack)
    public void back(){
        finish();
    }

    @OnClick(R.id.imvCreateMovie)
    public void createMovie(){
        startActivity(new Intent(this, CreateMovieActivity.class));
    }

    @OnClick(R.id.btnChangePass)
    public void btnChangePass(){
        //open Alert Dialog New Password

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_change_password, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();

        final EditText edtConfirmPass = (EditText) dialogView.findViewById(R.id.edtConfirmPassword);
        final EditText edtNewPass = (EditText) dialogView.findViewById(R.id.edtNewPassword);
        final EditText edtOldPass = (EditText) dialogView.findViewById(R.id.edtOldPassword);
        Button btChangePass = (Button) dialogView.findViewById(R.id.btnChange);
        Button btLogout = (Button) dialogView.findViewById(R.id.btnCancel);

        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        btChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String newPass = edtNewPass.getText().toString();
                final String oldPass = edtOldPass.getText().toString();
                final String confirmPass = edtConfirmPass.getText().toString();
                if (oldPass.matches("")){
                    edtOldPass.setError("Không thể trống");
                }else if (newPass.matches("")){
                    edtNewPass.setError("Không thể trống");
                } else if (confirmPass.matches("")){
                    edtConfirmPass.setError("Không thể trống");
                }
                else if (!newPass.equals(confirmPass)){
                    edtConfirmPass.setError("Mật khẩu không khớp");
                }else {
                    final ProgressDialog progress = new ProgressDialog(ProfileActivity.this);
                    progress.setTitle("Đổi mật khẩu");
                    progress.setMessage("Đang thay đổi...");
                    progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                    progress.show();

                    JsonObject body = new JsonObject();
                    body.addProperty("oldPassword", oldPass);
                    body.addProperty("newPassword", newPass);
                    Call<ResponseData> call = ApiUtils.getService().getChangePassword(apiToken, body);
                    call.enqueue(new Callback<ResponseData>() {
                        @Override
                        public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

                            if (response.body().getStatus() == 200) {
                                Log.d("Apitiny", response.toString());
                                progress.dismiss();
                                alertDialog.cancel();
                                Toast.makeText(ProfileActivity.this, "Thay đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                            }
                            else if (response.body().getError().getStatus() == 400){
                                progress.dismiss();
                                Toast.makeText(ProfileActivity.this, "Mật khẩu cũ sai", Toast.LENGTH_SHORT).show();
                            }else {
                                progress.dismiss();
                                Toast.makeText(ProfileActivity.this, "Thay đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseData> call, Throwable t) {
                            Toast.makeText(ProfileActivity.this, "Thay đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                        }

                    });
                }
            }
        });

        alertDialog.show();
    }

    private void updateUserInfo(){
        JsonObject body = new JsonObject();
        body.addProperty("token", apiToken);
        Call<UserInfoResponse> call = ApiUtils.getService().getUserInfo(body);
        call.enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                if (response.isSuccessful()){
                    String images = "https://cinema-hatin.herokuapp.com" + response.body().getAvatarURL();
                    tvName.setText(response.body().getName());
                    tvEmail.setText(response.body().getEmail());
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.ic_picture)
                            .error(R.drawable.ic_picture)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .priority(Priority.HIGH);
                    Glide.with(ProfileActivity.this)
                            .load(images)
                            .apply(options)
                            .into(imvAvatar);
                }
                SRLProfile.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUserInfo();
        requestApiMovie();
    }

    private List<MovieListData> myPostMovie(List<MovieListData> dataFromApi){
        String userId = SharedPreference.getString(getApplication(), "ID");
        ArrayList<MovieListData> listMyPost = new ArrayList<MovieListData>();
        for (MovieListData data : dataFromApi)
        {
            if (data.getCreatorId() == null || data.getCreatorId().equals("")){

            }else {
                if (data.getCreatorId().equals(userId)) {
                    listMyPost.add(data);
                }
            }
        }
        return listMyPost;
    }

    private void postImage(){
        RequestBody file = RequestBody.create(MediaType.parse("image/*"), fileCurrent);
        MultipartBody.Part fbody = MultipartBody.Part.createFormData("file", fileCurrent.getName(), file);
        Call<JsonObject> call = ApiUtils.getService().changeAvatar(apiToken, fbody);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.d("Apitiny", response.toString());
                    updateUserInfo();
                }
                else {
                    Toast.makeText(ProfileActivity.this, "Update avatar fail", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Ảnh quá lớn", Toast.LENGTH_SHORT).show();
            }

        });
    }
}

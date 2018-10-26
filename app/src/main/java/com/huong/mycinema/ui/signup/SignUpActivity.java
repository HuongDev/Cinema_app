package com.huong.mycinema.ui.signup;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.huong.mycinema.CinemaApp;
import com.huong.mycinema.R;
import com.huong.mycinema.ui.login.LoginActivity;
import com.huong.mycinema.networking.ApiUtils;
import com.huong.mycinema.ui.movielist.MovieListActivity;
import com.huong.mycinema.util.KeyboardUtils;
import com.huong.mycinema.util.SharedPreference;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends CinemaApp {

    @BindView(R.id.edtEmailSU)
    EditText edtEmail;
    @BindView(R.id.edtConfirmPasswordSU)
    EditText edtCPassword;
    @BindView(R.id.edtPasswordSU)
    EditText edtPassword;
    @BindView(R.id.edtNameSU)
    EditText edtName;
    @BindView(R.id.btnSignUp)
    Button btnSignUp;
    @BindView(R.id.btnISignIn)
    Button btnISignIn;
    @BindView(R.id.signUp)
    ConstraintLayout signUp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        getDeps().inject(this);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkSignUp();
            }
        });

        btnISignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });

        signUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
//                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                KeyboardUtils.hideKeyboard(SignUpActivity.this);
                return true;
            }
        });
    }

    private void registerUser(){

        Call<SignUpResponse> call = ApiUtils.getService().postSignUp(toJsonObject());
        call.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("Apitiny", response.toString());
                    Gson gson = new Gson();
                    String json = gson.toJson(response.body().getUser());
                    SharedPreference.saveString(getApplication(),"USER", json);
                    SharedPreference.saveString(getApplication(),"ACCESS_TOKEN", response.body().getToken());
                    SharedPreference.saveString(getApplication(),"ID", response.body().getUser().getId());
                    Toast.makeText(SignUpActivity.this, "Đã tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUpActivity.this, MovieListActivity.class));
                    finish();
                }
                else {
                    // error case
                    switch (response.code()) {
                        case 404:
                            Toast.makeText(SignUpActivity.this, "Tài khoản đã có người sử dụng", Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(SignUpActivity.this, "Tài khoản đã có người sử dụng", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(SignUpActivity.this, "Tài khoản đã có người sử dụng", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "Tài khoản đã được sử dụng", Toast.LENGTH_SHORT).show();
            }

        });
    }

    public JsonObject toJsonObject() {
        JsonObject body = new JsonObject();
        body.addProperty("email", edtEmail.getText().toString());
        body.addProperty("name", edtName.getText().toString());
        body.addProperty("password", edtPassword.getText().toString());
        return body;
    }

    private void checkSignUp(){
        String email = edtEmail.getText().toString();
        String pass = edtPassword.getText().toString();
        String name = edtName.getText().toString();
        String confirmPass = edtCPassword.getText().toString();
        if (email.equals("") || pass.equals("") || name.equals("") || confirmPass.equals("")){
            if (name.trim().equals("")) {
                edtName.setError("Chỗ này không được bỏ trống");
            }else if (email.trim().equals("")){
                edtEmail.setError("Chỗ này không được bỏ trống");
            }else if (pass.trim().equals("")){
                edtPassword.setError("Chỗ này không được bỏ trống");
            }else if (confirmPass.trim().equals("")){
                edtCPassword.setError("Chỗ này không được bỏ trống");
            }
        }else {
            if (pass.equals(confirmPass)){
                if (email.length() > 2 || pass.length() > 2 || name.length() > 2 || confirmPass.length() > 2){
                    if (EMAIL_PATTERN.matcher(email).matches()){
                        registerUser();
                    }else {
                        edtEmail.setError("Email sai định dạng");
                        return;
                    }
                }else {
                    Toast.makeText(SignUpActivity.this, "Nhập lại các thông tin trên dài hơn ", Toast.LENGTH_SHORT).show();
                    return;
                }
            }else {
                edtCPassword.setError("không trùng khớp");
                Toast.makeText(SignUpActivity.this, "Mật khẩu nhập vào không trùng khớp !!!", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    public static final Pattern EMAIL_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );
}

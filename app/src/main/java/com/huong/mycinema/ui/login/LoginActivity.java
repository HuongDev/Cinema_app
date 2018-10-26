package com.huong.mycinema.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.text.Html;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huong.mycinema.CinemaApp;
import com.huong.mycinema.R;
import com.huong.mycinema.networking.ApiUtils;
import com.huong.mycinema.ui.changepass.ChangePasswordActivity;
import com.huong.mycinema.ui.movielist.MovieListActivity;
import com.huong.mycinema.ui.signup.SignUpActivity;
import com.huong.mycinema.util.DialogUtils;
import com.huong.mycinema.util.KeyboardUtils;
import com.huong.mycinema.util.SharedPreference;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends CinemaApp {

    @BindView(R.id.tvResetPassword)
    TextView tvResetPassword;
    @BindView(R.id.edtEmail)
    EditText edtEmail;
    @BindView(R.id.edtPassword)
    EditText edtPassword;
    @BindView(R.id.btnISignUp)
    Button btnSignUp;
    @BindView(R.id.btnSignIn)
    Button btnSignIn;
    @BindView(R.id.login)
    ConstraintLayout login;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        getDeps().inject(this);

        tvResetPassword.setText(Html.fromHtml("<u>Đặt lại mật khẩu</u>"));
        tvResetPassword.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, ChangePasswordActivity.class)));

        btnSignUp.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            finish();
        });

        btnSignIn.setOnClickListener(view -> checkLogin());

        setTouchActivity();
    }

    private void setTouchActivity() {
        login.setOnTouchListener((view, motionEvent) -> {
            KeyboardUtils.hideKeyboard(LoginActivity.this);
            return false;
        });
    }

    private void checkLogin() {

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(R.color.colorPrimaryDark);
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);

        String email = edtEmail.getText().toString();
        String pass = edtPassword.getText().toString();

        if (isValidEmaillId(edtEmail.getText().toString().trim())) {
            if (pass.length() > 2) {
                pDialog.show();
                Call<LoginResponse> call = ApiUtils.getService().postSignIn(new LoginRequest(email, pass).toJsonObject());
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        Log.d("Apitiny", response.toString());
                        if (response.isSuccessful()) {
                            pDialog.cancel();
                            Gson gson = new Gson();
                            String json = gson.toJson(response.body().getUser());
                            SharedPreference.saveString(getApplication(), "USER", json);
                            SharedPreference.saveString(getApplication(), "ACCESS_TOKEN", response.body().getToken());
                            SharedPreference.saveString(getApplication(), "ID", response.body().getUser().getId());
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(LoginActivity.this, MovieListActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            // error case
                            pDialog.cancel();
                            DialogUtils.showDialogError(LoginActivity.this, "Không đúng tài khoản hoặc mật khẩu");
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        pDialog.cancel();
                    }

                });
            } else {
                DialogUtils.showDialogError(LoginActivity.this, "Password lớn hơn 2 kí tự.");
                return;
            }

        } else {
            DialogUtils.showDialogError(LoginActivity.this, "Email không đúng định dạng");
            return;
        }


    }

    private boolean isValidEmaillId(String email) {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

}

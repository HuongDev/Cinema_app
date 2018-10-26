package com.huong.mycinema.ui.changepass;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.huong.mycinema.CinemaApp;
import com.huong.mycinema.R;
import com.huong.mycinema.networking.ApiUtils;
import com.huong.mycinema.util.KeyboardUtils;
import com.huong.mycinema.util.SharedPreference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends CinemaApp {
    @BindView(R.id.edtMail)
    EditText edtMail;
    @BindView(R.id.btnChangePass)
    Button btnChangePass;
    @BindView(R.id.btnLogout)
    Button btnLogout;
    @BindView(R.id.rootPassword)
    ConstraintLayout rootPassword;
    String apiToken;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getDeps().inject(this);
        ButterKnife.bind(this);
        setTouchActivity();
    }

    private void setTouchActivity() {
        rootPassword.setOnTouchListener((view, motionEvent) -> {
            KeyboardUtils.hideKeyboard(ChangePasswordActivity.this);
            return true;
        });
    }

    private void resetPassword(){
        // call api reset password
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Thay đổi mật khẩu");
        progress.setMessage("Chờ 1 xíu...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        String email = edtMail.getText().toString().trim();
        apiToken = SharedPreference.getString(getApplication(), "token");
        JsonObject body = new JsonObject();
        body.addProperty("email", email);
        Call<JsonObject> call = ApiUtils.getService().resetPassword(apiToken, body);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // To dismiss the dialog
                progress.dismiss();
                if (response.isSuccessful() && response.code() == 200){
                    Toast.makeText(ChangePasswordActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(ChangePasswordActivity.this, "Không tìm thấy tài khoản. Vui lòng kiểm tra lại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(ChangePasswordActivity.this, "Không tìm thấy tài khoản. Vui lòng kiểm tra lại", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @OnClick(R.id.btnLogout)
    public void openLogin(){
        finish();
    }

    @OnClick(R.id.btnChangePass)
    public void resetNewPassword(){
        resetPassword();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

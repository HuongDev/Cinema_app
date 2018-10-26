package com.huong.mycinema.ui.detail;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.huong.mycinema.CinemaApp;
import com.huong.mycinema.R;
import com.huong.mycinema.models.MovieListData;
import com.huong.mycinema.networking.ApiUtils;
import com.huong.mycinema.ui.editMovie.EditMovieActivity;
import com.huong.mycinema.ui.signup.User;
import com.huong.mycinema.util.KeyboardUtils;
import com.huong.mycinema.util.SharedPreference;
import com.huong.mycinema.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailMovieActivity extends CinemaApp {

    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.imvMovieDetail)
    ImageView imvMovieDetail;
    @BindView(R.id.tvMovieName)
    TextView tvMovieName;
    @BindView(R.id.tvMovieGenre)
    TextView tvMovieGenre;
    @BindView(R.id.tvMovieRelease)
    TextView tvMovieRelease;
    @BindView(R.id.tvMovieDescription)
    TextView tvMovieDescription;
    @BindView(R.id.btnDeleteMovie)
    Button btnDeleteMovie;
    @BindView(R.id.btnEditMovie)
    Button btnEditMovie;
    @BindView(R.id.rootDetail)
    ConstraintLayout rootDetail;

    //region Variable
    String apiToken;
    String idMovie = null;
    private MovieListData movieObject;
    private String idCreator = null;

    private static final int REQUEST_CODE_ACTIVITY = 72;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        ButterKnife.bind(this);
        getDeps().inject(this);

        supportToolbar();
        checkLogin();

        apiToken = SharedPreference.getString(getApplication(), "ACCESS_TOKEN");

        btnDeleteMovie.setOnClickListener(view -> dialogRemoveMovie());

        btnEditMovie.setOnClickListener(view -> passingDataIntent());

        setTouchActivity();
    }

    private void setTouchActivity(){
        rootDetail.setOnTouchListener((view, motionEvent) -> {
            KeyboardUtils.hideKeyboard(DetailMovieActivity.this);
            return false;
        });
    }

    private void getMovieDetail(String id){

        Call<DetailMovieResponse> call = ApiUtils.getService().getDetailMovie(id);
        call.enqueue(new Callback<DetailMovieResponse>() {
            @Override
            public void onResponse(Call<DetailMovieResponse> call, Response<DetailMovieResponse> response) {
                Log.d("Apitiny", response.toString());
                DetailMovieResponse data = response.body();
                movieObject = data.getData();
                if (data != null){
                    String images = "https://cinema-hatin.herokuapp.com" + data.getData().getPosterURL();
                    toolbar_title.setText(data.getData().getName());
                    tvMovieGenre.setText(data.getData().getGenre());
                    tvMovieRelease.setText(Utils.getConvertedTime(data.getData().getReleaseDate()));
                    tvMovieDescription.setText(data.getData().getContent());
                    Glide.with(DetailMovieActivity.this)
                            .load(images)
                            .into(imvMovieDetail);
                }else {
                    String empty = "empty";
                    toolbar_title.setText(empty);
                    tvMovieGenre.setText(empty);
                    tvMovieDescription.setText(empty);
                    Glide.with(DetailMovieActivity.this)
                        .load(R.drawable.ic_picture)
                        .into(imvMovieDetail);
                }
            }

            @Override
            public void onFailure(Call<DetailMovieResponse> call, Throwable t) {

            }

        });
    }

    private void passingDataIntent(){
        Intent i = new Intent(DetailMovieActivity.this, EditMovieActivity.class);

        Bundle b = new Bundle();
        b.putParcelable("DATA", (MovieListData) movieObject);
        i.putExtra("BUNDLE", b);

        startActivityForResult(i, REQUEST_CODE_ACTIVITY);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK) {

                final String resultId = data.getStringExtra(EditMovieActivity.EXTRA_DATA);
                getMovieDetail(resultId);

            } else {

            }
        }
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }

    private void dialogRemoveMovie(){
        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:

                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("_id", idMovie);
                        Call<JsonObject> call = ApiUtils.getService().deleteMovie(apiToken, jsonObject);
                        call.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                Log.d("Apitiny", response.toString());
                                if (response.isSuccessful() && response.code() == 200){
                                    dialog.cancel();
                                    Toast.makeText(DetailMovieActivity.this, "Xóa phim thành công", Toast.LENGTH_SHORT).show();
                                    finish();
                                }else {
                                    dialog.cancel();
                                    Toast.makeText(DetailMovieActivity.this, "Xóa phim thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {

                            }
                        });
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.cancel();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(DetailMovieActivity.this);
        builder.setMessage("Bạn có muốn xóa phim này không?").setPositiveButton("Đồng ý", dialogClickListener)
                .setNegativeButton("Hủy", dialogClickListener).show();
    }

    private void supportToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDetail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void checkLogin(){
        if (getIntent().hasExtra("id")) {
            idMovie = getIntent().getStringExtra("id");
            getMovieDetail(idMovie);
            idCreator = getIntent().getStringExtra("idCreator");
            Gson gson = new Gson();
            String json = SharedPreference.getString(getApplication(), "USER");
            User obj = gson.fromJson(json, User.class);
            if (idCreator.equals(obj.getId())){
                btnEditMovie.setVisibility(View.VISIBLE);
                btnDeleteMovie.setVisibility(View.VISIBLE);
            }
        }
    }
}

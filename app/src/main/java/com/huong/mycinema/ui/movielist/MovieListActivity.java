package com.huong.mycinema.ui.movielist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.huong.mycinema.CinemaApp;
import com.huong.mycinema.R;
import com.huong.mycinema.models.MovieListData;
import com.huong.mycinema.models.response.MovieListResponse;
import com.huong.mycinema.networking.Service;
import com.huong.mycinema.ui.createmovie.CreateMovieActivity;
import com.huong.mycinema.ui.detail.DetailMovieActivity;
import com.huong.mycinema.ui.login.LoginActivity;
import com.huong.mycinema.ui.profile.ProfileActivity;
import com.huong.mycinema.util.SharedPreference;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovieListActivity extends CinemaApp implements SearchView.OnQueryTextListener, MovieListView {

    @Inject
    public Service service;

    @BindView(R.id.rcvMovieList)
    RecyclerView rcvMovieList;
    @BindView(R.id.myFabCreateMovie)
    FloatingActionButton fabCreateMovie;
    @BindView(R.id.SRLMovie)
    SwipeRefreshLayout SRLMovie;
    @BindView(R.id.myFabUser)
    FloatingActionButton fabUser;

    //region Variable
    private List<MovieListData> listData;
    private List<MovieListData> listFilteredData = new ArrayList<>();
    private MovieListPresenter presenter;
    private MovieListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        getDeps().inject(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvMovieList.setLayoutManager(layoutManager);

        initPresenter();

        SRLMovie.setOnRefreshListener(() -> presenter.getMovieList());

        String login = SharedPreference.getString(getApplication(),"ACCESS_TOKEN");
        if (login != null){
            fabUser.setVisibility(View.VISIBLE);
        }

    }

    private void initPresenter(){
        presenter = new MovieListPresenter(service, this);
        presenter.getMovieList();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search Movie");
        searchView.setOnQueryTextListener(this);
        searchView.setIconified(false);

        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(R.color.white));

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        filter(newText);
        return false;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        listFilteredData.clear();
        if (charText.length() == 0) {
            listFilteredData.addAll(listData);
        } else {
            for (MovieListData wp : listData) {
                if (wp.getName() == null || wp.getName().equals("")){

                }else {
                    if (wp.getName().toLowerCase().contains(charText.toLowerCase()) || wp.getName().contains(charText)) {
                        listFilteredData.add(wp);
                    }
                }
            }
        }
        adapter.swapData(listFilteredData);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showWait() {

    }

    @Override
    public void removeWait() {

    }

    @Override
    public void onFailure(String appErrorMessage) {

    }

    @Override
    public void getMovieListSuccess(MovieListResponse movieListResponse) {
        listData = movieListResponse.getData();
        adapter = new MovieListAdapter(getApplicationContext(), movieListResponse.getData(),
                new MovieListAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(MovieListData Item) {
                        String idMovie = Item.getId();
                        Intent i = new Intent(MovieListActivity.this, DetailMovieActivity.class);
                        i.putExtra("id", idMovie);
                        i.putExtra("idCreator", Item.getCreatorId());
                        startActivity(i);

                    }
                });

        rcvMovieList.setAdapter(adapter);
        SRLMovie.setRefreshing(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.getMovieList();
    }

    @OnClick(R.id.myFabUser)
    public void showProfile(){
        Intent i = new Intent(MovieListActivity.this, ProfileActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.myFabCreateMovie)
    public void createMovie(){
        checkLogin();
    }

    private void checkLogin(){
        String login = SharedPreference.getString(getApplication(),"ACCESS_TOKEN");
        if (login == null){
            askLogin();
        }else {
            startActivity(new Intent(MovieListActivity.this, CreateMovieActivity.class));
        }
    }

    private void askLogin(){
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                //set title
                .setTitle("Bạn phải dăng nhập trước khi tạo phim")
                //set positive button
                .setPositiveButton("Đăng nhập", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(MovieListActivity.this, LoginActivity.class));
                    }
                })
                //set negative button
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what should happen when negative button is clicked
                        dialogInterface.cancel();
                    }
                }).show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
    }

}

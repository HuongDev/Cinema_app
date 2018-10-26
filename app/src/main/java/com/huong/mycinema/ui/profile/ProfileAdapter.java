package com.huong.mycinema.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huong.mycinema.R;
import com.huong.mycinema.models.MovieListData;
import com.huong.mycinema.ui.detail.DetailMovieActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HuongPN on 10/22/2018.
 */
public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    private List<MovieListData> data = new ArrayList<>();
    private Context context;

    public ProfileAdapter(Context context, List<MovieListData> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public ProfileAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_movie, null);
        return new ProfileAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProfileAdapter.ViewHolder holder, final int position) {

        holder.tvName.setText(data.get(position).getName());

        String images = "https://cinema-hatin.herokuapp.com" + data.get(position).getPosterURL();

        if (data.get(position).getPosterURL() == null || data.get(position).getPosterURL() == ""){
            Glide.with(context)
                    .load(R.drawable.ic_picture)
                    .into(holder.imvMovie);
        }else {
            Glide.with(context)
                    .load(images)
                    .into(holder.imvMovie);
        }

        holder.lnMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = data.get(position).getId();
                String idCreator = data.get(position).getCreatorId();
                Intent i = new Intent(context, DetailMovieActivity.class);
                i.putExtra("id", id);
                i.putExtra("idCreator", idCreator);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(i);
            }
        });
    }

    public void swapData(List<MovieListData> list){
        data = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView imvMovie;
        LinearLayout lnMovie;

        public ViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvMyName);
            imvMovie = itemView.findViewById(R.id.imvMyMovie);
            lnMovie = itemView.findViewById(R.id.lnMovie);
        }

    }
}

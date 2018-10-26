package com.huong.mycinema.ui.movielist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.huong.mycinema.R;
import com.huong.mycinema.models.MovieListData;
import com.huong.mycinema.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HuongPN on 10/17/2018.
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {
    private OnItemClickListener listener = null;
    private List<MovieListData> data = new ArrayList<>();

    private Context context;

    public MovieListAdapter(Context context, List<MovieListData> data, OnItemClickListener listener) {
        this.data = data;
        this.listener = listener;
        this.context = context;
    }


    @Override
    public MovieListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.click(data.get(position), listener);
        holder.tvTitle.setText(data.get(position).getName());
        holder.tvGenre.setText(data.get(position).getGenre());
        holder.tvDate.setText(Utils.getConvertedTime(data.get(position).getReleaseDate()));
        if (data.get(position).getUser() == null){
            holder.tvInfo.setText("undefined");
        }else {
            holder.tvInfo.setText(data.get(position).getUser().getName());
        }
        String images = "https://cinema-hatin.herokuapp.com" + data.get(position).getPosterURL();

        if (data.get(position).getPosterURL() == null || data.get(position).getPosterURL() == ""){
            Glide.with(context)
                    .load(R.drawable.ic_picture)
                    .apply(new RequestOptions().override(300, 100))
                    .into(holder.imvMovie);
        }else {
            Glide.with(context)
                    .load(images)
                    .apply(new RequestOptions().override(300, 100))
                    .into(holder.imvMovie);
        }
    }

    public void swapData(List<MovieListData> list){
        data = list;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    public interface OnItemClickListener {
        void onClick(MovieListData Item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvGenre, tvDate, tvInfo;
        ImageView imvMovie;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvGenre = itemView.findViewById(R.id.tvGenre);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvInfo = itemView.findViewById(R.id.tvInfo);
            imvMovie = itemView.findViewById(R.id.imvMovie);

        }


        public void click(final MovieListData movieListData, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(movieListData);
                }
            });
        }
    }
}
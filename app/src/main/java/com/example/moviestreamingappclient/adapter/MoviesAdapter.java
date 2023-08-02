package com.example.moviestreamingappclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviestreamingappclient.R;
import com.example.moviestreamingappclient.interfaces.MovieClickListener;
import com.example.moviestreamingappclient.model.MoviesModel;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private Context context;
    private List<MoviesModel> moviesModelList;

    public MoviesAdapter(Context context, List<MoviesModel> moviesModelList, MovieClickListener movieClickListener) {
        this.context = context;
        this.moviesModelList = moviesModelList;
        this.movieClickListener = movieClickListener;
    }

    private MovieClickListener movieClickListener;


    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.movies_item,parent,false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        MoviesModel moviesModel = moviesModelList.get(position);
        holder.textView.setText(moviesModel.getVideo_name());
        Glide.with(context).load(moviesModel.getVideo_thumb()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return moviesModelList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        ConstraintLayout container;
        public MovieViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_movie_img);
            textView = itemView.findViewById(R.id.item_movie_title);
            container = itemView.findViewById(R.id.container_item);

            itemView.setOnClickListener(view -> {
                movieClickListener.onMovieClick(moviesModelList.get(getAdapterPosition()),imageView);
            });
        }
    }
}

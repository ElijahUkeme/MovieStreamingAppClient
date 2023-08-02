package com.example.moviestreamingappclient.interfaces;

import android.widget.ImageView;

import com.example.moviestreamingappclient.model.MoviesModel;

public interface MovieClickListener {

    void onMovieClick(MoviesModel moviesModel, ImageView imageView);
}

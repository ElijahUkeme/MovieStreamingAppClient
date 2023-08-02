package com.example.moviestreamingappclient.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.moviestreamingappclient.MainActivity;
import com.example.moviestreamingappclient.R;
import com.example.moviestreamingappclient.adapter.MoviesAdapter;
import com.example.moviestreamingappclient.interfaces.MovieClickListener;
import com.example.moviestreamingappclient.model.MoviesModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity implements MovieClickListener {

    private ImageView thumbNailImage,coverImage;
    private TextView titleTv, descriptionTv;
    private MoviesAdapter moviesAdapter;
    private RecyclerView rvCast, rvSimilar;
    private DatabaseReference databaseReference;
    private FloatingActionButton play_fab;
    private List<MoviesModel> uploads,latestMovies,popularMovies,
            romanticMovies,comedy,sports,actionMovies,adventures,keyboardMovies;
    String current_video_url,current_video_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        initViews();
        initSimilarMovies();
        initRecyclerView();
        play_fab.setOnClickListener(view -> {
            Intent intent = new Intent(DetailsActivity.this,PlayMovieActivity.class);
            intent.putExtra("movieUrl",current_video_url);
            startActivity(intent);
        });
    }

    private void initRecyclerView(){
        if (current_video_category.equalsIgnoreCase("Action")){
            moviesAdapter = new MoviesAdapter(this,actionMovies,this);
            rvSimilar.setAdapter(moviesAdapter);
            rvSimilar.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
            moviesAdapter.notifyDataSetChanged();
        }
        if (current_video_category.equalsIgnoreCase("Sport")){
            moviesAdapter = new MoviesAdapter(this,sports,this);
            rvSimilar.setAdapter(moviesAdapter);
            rvSimilar.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
            moviesAdapter.notifyDataSetChanged();
        }
        if (current_video_category.equalsIgnoreCase("Comedy")){
            moviesAdapter = new MoviesAdapter(this,comedy,this);
            rvSimilar.setAdapter(moviesAdapter);
            rvSimilar.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
            moviesAdapter.notifyDataSetChanged();
        }
        if (current_video_category.equalsIgnoreCase("Adventure")){
            moviesAdapter = new MoviesAdapter(this,adventures,this);
            rvSimilar.setAdapter(moviesAdapter);
            rvSimilar.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
            moviesAdapter.notifyDataSetChanged();
        }
        if (current_video_category.equalsIgnoreCase("Keyboard Training")){
            moviesAdapter = new MoviesAdapter(this,keyboardMovies,this);
            rvSimilar.setAdapter(moviesAdapter);
            rvSimilar.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
            moviesAdapter.notifyDataSetChanged();
        }
        if (current_video_category.equalsIgnoreCase("Romantic")){
            moviesAdapter = new MoviesAdapter(this,romanticMovies,this);
            rvSimilar.setAdapter(moviesAdapter);
            rvSimilar.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
            moviesAdapter.notifyDataSetChanged();
        }
    }

    private void initSimilarMovies(){

        uploads = new ArrayList<>();
        latestMovies = new ArrayList<>();
        popularMovies = new ArrayList<>();
        romanticMovies = new ArrayList<>();
        comedy = new ArrayList<>();
        sports = new ArrayList<>();
        actionMovies = new ArrayList<>();
        adventures = new ArrayList<>();
        keyboardMovies = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Videos");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    MoviesModel moviesModel = dataSnapshot.getValue(MoviesModel.class);
                    if (moviesModel.getVideo_category().equalsIgnoreCase("Latest Movies")){
                        latestMovies.add(moviesModel);
                    }
                    if (moviesModel.getVideo_type().equalsIgnoreCase("Popular Movies")){
                        popularMovies.add(moviesModel);
                    }
                    if (moviesModel.getVideo_category().equalsIgnoreCase("Romantic")){
                        romanticMovies.add(moviesModel);
                    }
                    if (moviesModel.getVideo_category().equalsIgnoreCase("Comedy")){
                        comedy.add(moviesModel);
                    }
                    if (moviesModel.getVideo_category().equalsIgnoreCase("Sport")){
                        sports.add(moviesModel);
                    }
                    if (moviesModel.getVideo_category().equalsIgnoreCase("Action")){
                        actionMovies.add(moviesModel);
                    }
                    if (moviesModel.getVideo_category().equalsIgnoreCase("Adventure")){
                        adventures.add(moviesModel);
                    }
                    if (moviesModel.getVideo_category().equalsIgnoreCase("Keyboarding Training")){
                        keyboardMovies.add(moviesModel);
                    }
                    uploads.add(moviesModel);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void initViews(){
        play_fab = findViewById(R.id.fab_play);
        titleTv = findViewById(R.id.details_movie_title);
        descriptionTv = findViewById(R.id.details_movie_desc);
        thumbNailImage = findViewById(R.id.details_movie_img);
        rvSimilar = findViewById(R.id.recyclerview_similar_movies);
        coverImage = findViewById(R.id.detail_image_cover);
        String title = getIntent().getStringExtra("title");
        String imageUrl = getIntent().getStringExtra("imageUrl");
        String imageCover = getIntent().getStringExtra("imageCover");
        String movieUrl = getIntent().getStringExtra("movieUrl");
        String movieDesc = getIntent().getStringExtra("movieDescription");
        current_video_category = getIntent().getStringExtra("movieCategory");
        current_video_url = movieUrl;
        Glide.with(this).load(imageUrl).into(thumbNailImage);
        Glide.with(this).load(imageCover).into(coverImage);
        titleTv.setText(title);
        descriptionTv.setText(movieDesc);
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onMovieClick(MoviesModel moviesModel, ImageView imageView) {
        titleTv.setText(moviesModel.getVideo_name());
        getSupportActionBar().setTitle(moviesModel.getVideo_name());
        Glide.with(this).load(moviesModel.getVideo_thumb()).into(thumbNailImage);
        Glide.with(this).load(moviesModel.getVideo_thumb()).into(coverImage);
        descriptionTv.setText(moviesModel.getVideo_description());
        current_video_category = moviesModel.getVideo_category();
        current_video_url = moviesModel.getVideo_url();
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(DetailsActivity.this,imageView,"sharedName");
        options.toBundle();
    }
}
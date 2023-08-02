package com.example.moviestreamingappclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.moviestreamingappclient.activity.DetailsActivity;
import com.example.moviestreamingappclient.adapter.MoviesAdapter;
import com.example.moviestreamingappclient.adapter.SliderPageAdapter;
import com.example.moviestreamingappclient.interfaces.MovieClickListener;
import com.example.moviestreamingappclient.model.MoviesModel;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements MovieClickListener {

    private MoviesAdapter moviesAdapter;
    DatabaseReference databaseReference;
    private List<MoviesModel> uploads,latestMovies,popularMovies,
            romanticMovies,comedy,sports,actionMovies,adventures,keyboardMovies;
    private ViewPager sliderPager;
    private List<MoviesModel> uploadSlider;
    private TabLayout indicator,tabActionMovies;
    private RecyclerView moviesRv, tab, moviesRvWeek;
    private ProgressDialog progressDialog;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        progressDialog = new ProgressDialog(this);

        initViews();
        addAllMovies();
        initPopularMovies();
        initWeekMovies();
        initMoviesViewTab();
    }

    private void addAllMovies(){
        uploads = new ArrayList<>();
        latestMovies = new ArrayList<>();
        popularMovies = new ArrayList<>();
        romanticMovies = new ArrayList<>();
        comedy = new ArrayList<>();
        sports = new ArrayList<>();
        actionMovies = new ArrayList<>();
        adventures = new ArrayList<>();
        keyboardMovies = new ArrayList<>();
        uploadSlider = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Videos");
        progressDialog.setMessage("Loading...");
        progressDialog.show();
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
                    if (moviesModel.getVideo_slide().equalsIgnoreCase("Slider Movies")){
                        uploadSlider.add(moviesModel);
                    }
                    uploads.add(moviesModel);
                }
                initSlider();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void initSlider(){

        SliderPageAdapter adapter = new SliderPageAdapter(this,uploadSlider);
        sliderPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //set up timer
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(),4000,6000);
        indicator.setupWithViewPager(sliderPager,true);

    }

    private void initWeekMovies(){
        moviesAdapter = new MoviesAdapter(this,latestMovies,this);
        moviesRvWeek.setAdapter(moviesAdapter);
        moviesRvWeek.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        moviesAdapter.notifyDataSetChanged();
    }

    private void initPopularMovies(){
        moviesAdapter = new MoviesAdapter(this,popularMovies,this);
        moviesRv.setAdapter(moviesAdapter);
        moviesRv.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        moviesAdapter.notifyDataSetChanged();
    }

    private void initMoviesViewTab(){
        getActionMovies();
        tabActionMovies.addTab(tabActionMovies.newTab().setText("Action"));
        tabActionMovies.addTab(tabActionMovies.newTab().setText("Adventures"));
        tabActionMovies.addTab(tabActionMovies.newTab().setText("Comedy"));
        tabActionMovies.addTab(tabActionMovies.newTab().setText("Keyboarding"));
        tabActionMovies.addTab(tabActionMovies.newTab().setText("Sport"));
        tabActionMovies.addTab(tabActionMovies.newTab().setText("Romantic"));
        tabActionMovies.setTabGravity(TabLayout.GRAVITY_FILL);
        tabActionMovies.setTabTextColors(ColorStateList.valueOf(Color.WHITE));

        tabActionMovies.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        getActionMovies();
                        break;
                    case 1:
                        getAdventureMovies();
                        break;
                    case 2:
                        getComedyMovies();
                        break;
                    case 3:
                        getKeyboardingMovies();
                        break;
                    case 4:
                        getSportMovies();
                        break;
                    case 5:
                        getRomanticMovies();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initViews(){
        moviesRv = findViewById(R.id.recycler_view_movies);
        tab = findViewById(R.id.tabRecyclerView);
        moviesRvWeek = findViewById(R.id.recycler_view_movies_week);
        indicator = findViewById(R.id.indicator);
        tabActionMovies = findViewById(R.id.tabActionMovies);
        sliderPager = findViewById(R.id.slider_pager);
    }

    @Override
    public void onMovieClick(MoviesModel moviesModel, ImageView imageView) {

        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("title",moviesModel.getVideo_name());
        intent.putExtra("imgUrl",moviesModel.getVideo_thumb());
        intent.putExtra("imgCover",moviesModel.getVideo_thumb());
        intent.putExtra("movieUrl",moviesModel.getVideo_url());
        intent.putExtra("movieCategory",moviesModel.getVideo_category());
        intent.putExtra("movieDescription",moviesModel.getVideo_description());
        ActivityOptions  options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,imageView,"sharedName");
        startActivity(intent,options.toBundle());

    }

    public class SliderTimer extends TimerTask {

        @Override
        public void run() {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (sliderPager.getCurrentItem()<uploadSlider.size() -1){
                        sliderPager.setCurrentItem(sliderPager.getCurrentItem()+1);
                    }else {
                        sliderPager.setCurrentItem(0);
                    }
                }
            });
        }
    }

    private void getActionMovies(){
        moviesAdapter = new MoviesAdapter(this,actionMovies,this);
        tab.setAdapter(moviesAdapter);
        tab.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        moviesAdapter.notifyDataSetChanged();
    }

    private void getRomanticMovies(){
        moviesAdapter = new MoviesAdapter(this,romanticMovies,this);
        tab.setAdapter(moviesAdapter);
        tab.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        moviesAdapter.notifyDataSetChanged();
    }
    private void getAdventureMovies(){
        moviesAdapter = new MoviesAdapter(this,adventures,this);
        tab.setAdapter(moviesAdapter);
        tab.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        moviesAdapter.notifyDataSetChanged();
    }

    private void getComedyMovies(){
        moviesAdapter = new MoviesAdapter(this,comedy,this);
        tab.setAdapter(moviesAdapter);
        tab.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        moviesAdapter.notifyDataSetChanged();
    }
    private void getSportMovies(){
        moviesAdapter = new MoviesAdapter(this,sports,this);
        tab.setAdapter(moviesAdapter);
        tab.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        moviesAdapter.notifyDataSetChanged();
    }
    private void getKeyboardingMovies(){
        moviesAdapter = new MoviesAdapter(this,keyboardMovies,this);
        tab.setAdapter(moviesAdapter);
        tab.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        moviesAdapter.notifyDataSetChanged();
    }
}
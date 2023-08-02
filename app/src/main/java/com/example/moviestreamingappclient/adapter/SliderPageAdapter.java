package com.example.moviestreamingappclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.moviestreamingappclient.R;
import com.example.moviestreamingappclient.model.MoviesModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class SliderPageAdapter extends PagerAdapter {

    private Context context;

    public SliderPageAdapter(Context context, List<MoviesModel> moviesModelList) {
        this.context = context;
        this.moviesModelList = moviesModelList;
    }

    private List<MoviesModel> moviesModelList;
    @Override
    public int getCount() {
        return moviesModelList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View sliderLayout = inflater.inflate(R.layout.slider_item,null);
        ImageView sliderImage = sliderLayout.findViewById(R.id.slide_img);
        TextView sliderTitle = sliderLayout.findViewById(R.id.slider_title);
        FloatingActionButton floatingActionButton = sliderLayout.findViewById(R.id.floatingActionButton);

        Glide.with(context).load(moviesModelList.get(position).getVideo_thumb()).into(sliderImage);
        sliderTitle.setText(moviesModelList.get(position).getVideo_name()+"\n"+moviesModelList.get(position).getVideo_description());
        floatingActionButton.setOnClickListener(view -> {

        });
        container.addView(sliderLayout);
        return sliderLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}

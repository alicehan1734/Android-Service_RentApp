package org.techtown.android_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import java.util.ArrayList;
import java.util.List;

public class Close_image extends AppCompatActivity {

    ImageView image,cancelimage;
    List<String> arrayList;
    int postion;
    ViewPager Viewpager;
    viewadapter_close viewAdapter;
    List<String> imagearray = new ArrayList<>(4);
    private int position;
    SpringDotsIndicator dot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close_image);
        image = findViewById(R.id.image);
        cancelimage = findViewById(R.id.cancelimage);
        Viewpager = findViewById(R.id.Viewpager_image);
        dot = findViewById(R.id.DotsIndicator);

        Intent intent = getIntent();
        imagearray = intent.getStringArrayListExtra("image");
        postion = intent.getIntExtra("image postion",position);
        
        viewAdapter = new viewadapter_close(Close_image.this, imagearray);
        Viewpager.setAdapter(viewAdapter);
        Viewpager.setCurrentItem(postion);
        dot.setViewPager(Viewpager);

        cancelimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
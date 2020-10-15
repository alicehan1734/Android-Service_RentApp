package org.techtown.android_project;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class viewadapter_close extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    //private Integer[] images = {R.drawable.logo_korean_small, R.drawable.logo_korean_small, R.drawable.logo_korean_small, R.drawable.logo_korean_small};
    List<String> arrayList;

    viewadapter_close(Context context, List<String> arrayList) {
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {

        if(arrayList != null){
            return arrayList.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View view = layoutInflater.inflate(R.layout.activity_item_image, container,false);
        ImageView imageView = view.findViewById(R.id.image_view);
        Glide.with(context).
                load(arrayList.get(position)).into(imageView);
        container.addView(view);
//        imageView.setImageResource(images[position]);
//        ViewPager viewPager = (ViewPager) container;
//        viewPager.addView(view, 0);

        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position,  Object object) {

        container.removeView((ConstraintLayout)object);
    }

}

package org.techtown.android_project.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.techtown.android_project.R;
import org.techtown.android_project.models.Post;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {


    private List<Post> datas;
    private Context context_1;

    public PostAdapter(List<Post> datas,Context mcontext) {

        this.datas = datas;
        context_1 = mcontext;

    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


      return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
    Post data = datas.get(position); // 위에서 부터 아래까지의 포지션에
    holder.nickname.setText("작성자 : " + data.getNickname());
    holder.title.setText(data.getTitle()); // 포지션 1에 타이틀 각각을 넣는것. (Post)에 있는 get을 불러왔다.
    holder.rentfee.setText("렌트비(1일) : " + data.getRentfee() +" 원");
    holder.deposit.setText("보증금          : " + data.getDeposit() +" 원");
    holder.date.setText("올린 날짜 : " + data.getCurrentdate());
    holder.location.setText("거래장소 : " + data.getLocation());

    Glide.with(this.context_1).load(data.getImage()).override(300,300).into(holder.ImageView_postimage);

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        private TextView date;
        private TextView nickname;
        private TextView title;
        private TextView rentfee;
        private TextView deposit;
        private TextView location;
        private ImageView ImageView_postimage;


        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            nickname = itemView.findViewById(R.id.item_post_nickname);
            title = itemView.findViewById(R.id.item_post_title);
            rentfee = itemView.findViewById(R.id.item_post_rentfee);
            deposit = itemView.findViewById(R.id.item_post_deposit);
            date = itemView.findViewById(R.id.dateNow);
            location = itemView.findViewById(R.id.location);
            ImageView_postimage = itemView.findViewById(R.id.ImageView_postimage);
        }

    }
}

package org.techtown.android_project.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.techtown.android_project.R;
import org.techtown.android_project.models.Post;

import java.util.List;

public class SimplePostAdapter extends RecyclerView.Adapter<SimplePostAdapter.PostViewHolder> {


private List<Post> datas;
private Context context_1;

public SimplePostAdapter(List<Post> datas,Context mcontext) {

        this.datas = datas;
        context_1 = mcontext;

        }

@NonNull
@Override
public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.useritem,parent,false));

        }

@Override
public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post data = datas.get(position); // 위에서 부터 아래까지의 포지션에
        holder.rentfee.setText("렌트 " + data.getRentfee() +" 원");
        holder.title.setText(data.getTitle());

        Glide.with(this.context_1).load(data.getImage()).override(300,300).into(holder.ImageView_postimage);

        }

@Override
public int getItemCount() {
        return datas.size();
        }

class PostViewHolder extends RecyclerView.ViewHolder {

    private TextView rentfee, title;
    private ImageView ImageView_postimage;



    public PostViewHolder(@NonNull View itemView) {
        super(itemView);

        rentfee = itemView.findViewById(R.id.rentfee);
        ImageView_postimage = itemView.findViewById(R.id.imageview_post);
        title = itemView.findViewById(R.id.title_simple);

    }

}
}

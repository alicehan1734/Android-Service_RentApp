package org.techtown.android_project;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.Collections;

public class ImageviewAdapter extends RecyclerView.Adapter<ImageviewAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    ArrayList<Uri> uris;
    Context context;

    @Override
    public boolean onItemMove(int fromPos, int targetPos) {
        Log.d("onItemMove", "z");
        if (fromPos < targetPos) {
            for (int i = fromPos; i < targetPos; i++) {
                Collections.swap(uris, i, i + 1);
            }
        } else {
            for (int i = fromPos; i > targetPos; i--) {
                Collections.swap(uris, i, i - 1);
            }
        }
        notifyItemMoved(fromPos, targetPos);

        return false;
    }

    public interface OnStartDragListener {
        void onStartDrag(ImageviewAdapter.ViewHolder holder);
        void onDelete();
    }
    private final Context mcontext;
    private final OnStartDragListener mStartDragListener;

    public ImageviewAdapter(ArrayList<Uri> arrayList, Context context_, Context mcontext, OnStartDragListener mStartDragListener) {

        this.uris = arrayList;
        this.context = context_;

        this.mcontext = mcontext;
        this.mStartDragListener = mStartDragListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder { //스크롤이 넘길때마다 내가 원하는 데이터가 바껴지도록

        private ImageView image;


        public ViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.ImageView_selected_image);

        }

        public ImageView getImage() {
            return this.image;
        }

    }


    @Override
    public ImageviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { //뷰홀더를 만들어주는곳 (아까 내가 만들어낸 객체들을 생성해주는것이다.)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_image_recyclerview, parent, false);
        view.setLayoutParams(new RecyclerView.LayoutParams(300, 300)); //파라미터 정의인데 다시 공부해야함.
        ViewHolder holder = new ViewHolder(view);

        // return new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) //데이터 묶어줄 작업.
    {
        Glide.with(this.context)
                .load(uris.get(position).toString())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.getImage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setMessage("선택하신 이미지를 삭제하시겠습니까?");
                Log.e("삭제 물어보기! ", ".");

               dialog.setPositiveButton("삭제하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                      remove(holder.getAdapterPosition());
                      Log.e("uri 삭제후 남은 이미지 숫자", String.valueOf(uris));
                      mStartDragListener.onDelete();
                   }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "취소 되었습니다.", Toast.LENGTH_SHORT).show();

                    }
                });
                dialog.setTitle(" 이미지 삭제 ");
                dialog.show();
            }
        });


       holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) { //드래그 시작 확인
                    mStartDragListener.onStartDrag(holder);
                }
                Log.e("드래그 후 ", String.valueOf(uris));
                return false;
            }
        });
    }


    @Override
    public int getItemCount() { //우리가 뿌려줄 데이터의 전체길이를 리턴하면 된다. 더 깊게 알아볼 필요가 없음.
        return uris.size();
    }

    public void remove(int position) { //이미지 삭제하는 법
        try {
            Log.e("uri 삭제완료!! ", uris.toString());
            uris.remove(position);
            notifyItemRemoved(position); //새로고침을 해야 사라진걸 확인할수있다.
        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }

    }
}



package org.techtown.android_project;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewItemClickListener implements RecyclerView.OnItemTouchListener { // 에니메이션을 만들수있다. 밀었다가 내렸다가 할수있다.

    private OnItemClickListener listener;    //2. 리스너 생성하기
    private GestureDetector gestureDetector;  //3. 리스너 변수 생성하기 , 손가락의 움직임을 생성해주는것

    public interface OnItemClickListener {      //1. 리스너를 우선 만들기, 내가 필요한 리스너를 만들수있다.
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public RecyclerViewItemClickListener(Context context, final RecyclerView recyclerView, final OnItemClickListener listener) { //4. 생성자를 받기
        this.listener = listener;
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){ //SimpleOnGestureListener <- 원하는것만 받을 수 있다.
            @Override
            public boolean onSingleTapUp(MotionEvent e){ //한번 탭하는것
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {//길게 누르는것
                View v = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if ( v != null && listener != null) {
                    listener.onItemLongClick(v, recyclerView.getChildAdapterPosition(v));
                }
            }

        });

    }
    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) { //인터페이스
        View view = rv.findChildViewUnder(e.getX(), e.getY());
        if(view != null && gestureDetector.onTouchEvent(e)) {
            listener.onItemClick(view, rv.getChildAdapterPosition(view));
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}

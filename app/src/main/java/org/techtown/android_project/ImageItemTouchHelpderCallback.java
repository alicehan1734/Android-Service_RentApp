package org.techtown.android_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ImageItemTouchHelpderCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter mAdapter;
    public ImageItemTouchHelpderCallback(ItemTouchHelperAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    //해당 클래스를 생성할 때 ItemTouchHelperListener를 입력받아 멤버 인스턴스로 지정하고 ItemTouchHelper.Callback이 지원하는 여러 메소드를 오버라이딩 하여 필요에 따라 사용한 후  결과값들을 반환

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) { // 어느방향으로 움직일 것에 따라 flag 받는것 정의

        int dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT; //ItemTouchHelper.UP | ItemTouchHelper.DOWN;

        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public boolean onMove( RecyclerView recyclerView,  RecyclerView.ViewHolder viewHolder,  RecyclerView.ViewHolder target) {//  data 를 refresh 하기 위한 함수
        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }
}

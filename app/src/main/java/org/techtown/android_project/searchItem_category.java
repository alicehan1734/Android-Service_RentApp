package org.techtown.android_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.techtown.android_project.adapters.SimplePostAdapter;
import org.techtown.android_project.models.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class searchItem_category extends AppCompatActivity {
    private List<Post> mDatas;

    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    TextView post2_title,textview_nosign;
    String categoryname;
    private RecyclerView recyclerview;
    RecyclerView.LayoutManager layoutManager;
    private SimplePostAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item_category);
        post2_title = findViewById(R.id.post2_title);
        recyclerview = findViewById(R.id.recyclerview);
        textview_nosign = findViewById(R.id.textview_nosign);
        layoutManager = new GridLayoutManager(this,2);
        recyclerview.setLayoutManager(layoutManager);
        Intent intent = getIntent();
        categoryname = getIntent().getStringExtra("category");
        post2_title.setText(categoryname);


        recyclerview.addOnItemTouchListener(new RecyclerViewItemClickListener(this, recyclerview, new RecyclerViewItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {      // 아이템을 그냥 클릭했을 때의 상황
                Intent intent = new Intent(searchItem_category.this,Post2.class);
                intent.putExtra(FirebaseID.documentID, mDatas.get(position).getDocumentId());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));


    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatas = new ArrayList<>();

                    mStore.collection(FirebaseID.post)
                            .orderBy(FirebaseID.timestamp, Query.Direction.DESCENDING) //시간상으로 오름차순으로 포스팅을 정렬하게 해준다.
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                    if (queryDocumentSnapshots != null) {

                                        mDatas.clear();  //중복이 되므로, 한번 그전에 초기화를 한번 해주기 .

                                        for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()) {

                                            Map<String, Object> shot = snap.getData();
                                            String documentId = String.valueOf(shot.get(FirebaseID.documentID));
                                            String nickname = String.valueOf(shot.get(FirebaseID.nickname));
                                            String title = String.valueOf(snap.get(FirebaseID.title));
                                            //String contents = String.valueOf(shot.get(FirebaseID.contents));
                                            String date = String.valueOf(shot.get(FirebaseID.date));
                                            String rentfee = String.valueOf(shot.get(FirebaseID.rentfee));
                                            String deposit = String.valueOf(shot.get(FirebaseID.deposit));
                                            String location = String.valueOf(shot.get(FirebaseID.location));
                                            String UserID = String.valueOf(shot.get(FirebaseID.document_userID));
                                            String mainimage = String.valueOf(shot.get(FirebaseID.image));
                                            String category = String.valueOf(shot.get(FirebaseID.category));

                                            if (categoryname.equals(category)) {
                                                Log.i("문서내의 아이디값은", UserID);
                                                Post data = new Post(documentId, nickname, title, date, deposit, rentfee, location, mainimage);
                                                textview_nosign.setVisibility(View.GONE);

                                                mDatas.add(data);
                                            }
                                        }

                                        mAdapter = new SimplePostAdapter(mDatas,searchItem_category.this);
                                        recyclerview.setAdapter(mAdapter);




                                    }
                                }
                            });
                }



    }

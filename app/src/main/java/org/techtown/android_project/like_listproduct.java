package org.techtown.android_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.techtown.android_project.adapters.PostAdapter;
import org.techtown.android_project.adapters.SimplePostAdapter;
import org.techtown.android_project.models.Post;
import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class like_listproduct extends AppCompatActivity {

    RecyclerView recyclerview;
    private List<Post> mDatas;
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    String id;
    private PostAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_listproduct);

        id = getIntent().getStringExtra(FirebaseID.document_userID);

        recyclerview = findViewById(R.id.recyclerview);


    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatas = new ArrayList<>();
        Log.d("아이디값", id);
        mDatas.clear();  //중복이 되므로, 한번 그전에 초기화를 한번 해주기 .

        mStore.collection(FirebaseID.post).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (final DocumentSnapshot document2 : task.getResult()) {
                        Log.d("정보", document2.getId() + "=>" + document2.getData());

                        mStore.collection(FirebaseID.post).document(document2.getId()).collection("like").whereEqualTo(id, true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d("like 가 있다 !! ", document2.getId() + "=>" + document.getData());
                                        final String dg = document2.getId();
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



//                                                                if (dg.equals(documentId)) {
//                                                                    Post data = new Post(documentId, nickname, title, date, deposit, rentfee, location, mainimage);
//                                                                    mDatas.add(data);
//                                                                    Log.d("like 가 있다 !! 22", dg);
//                                                                    Log.d("like 가 있다 !! 33", documentId);
//                                                                    Log.d("like 가 있다 !! 33", String.valueOf(mDatas));
//                                                                    Log.d("like 가 있다 !! 99", String.valueOf(mDatas));
//                                                                    mAdapter = new PostAdapter(mDatas,like_listproduct.this);
//                                                                    recyclerview.setAdapter(mAdapter);
//                                                                }
                                                            }


                                                        }
                                                    }
                                                });
                                    }

                                }

                            }
                        });

                    }
                }

            }

        });
    }
}
package org.techtown.android_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import org.techtown.android_project.adapters.PostAdapter;
import org.techtown.android_project.adapters.RequestAdapter;
import org.techtown.android_project.adapters.SimplePostAdapter;
import org.techtown.android_project.models.Post;
import org.techtown.android_project.models.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Store_Information extends AppCompatActivity {
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance(); //사용자 가져오기
    TextView TextView_nickname, TextView_product_size, morerequest_button,revisestore;
    CircleImageView image_person;
    private RecyclerView recyclerview, recyclerview_request;
    private SimplePostAdapter mAdapter;
    private RequestAdapter RAdapter;

    private ArrayList<Post> mDatas;
    private ArrayList<Request> mRequest;

    Button click_request_button;
    EditText EditText_request;
    String id; //현재 이 상점의 아이디
    String currentUser = mAuth.getCurrentUser().getUid();
    String postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store__information);

        revisestore = findViewById(R.id.revisestore);
        id = getIntent().getStringExtra(FirebaseID.document_userID);
        TextView_nickname = findViewById(R.id.TextView_nickname);
        image_person = findViewById(R.id.image_person);
        recyclerview = findViewById(R.id.recyclerview);
        TextView_product_size = findViewById(R.id.TextView_product_size);
        click_request_button = findViewById(R.id.click_request_button);
        EditText_request = findViewById(R.id.EditText_request);
        recyclerview_request = findViewById(R.id.recyclerview_request);
        morerequest_button = findViewById(R.id.morerequest_button);

        mStore.collection(FirebaseID.user).document(id)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Map<String, Object> snap = task.getResult().getData();
                String name = String.valueOf(snap.get(FirebaseID.nickname));
                String profile = String.valueOf(snap.get(FirebaseID.profilephoto));

                TextView_nickname.setText(name);

                if(profile.equals("null")){
                }else {
                    Glide.with(Store_Information.this).
                            load(profile).into(image_person);
                }
            }
        });

        click_request_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Map<String, Object> tags = new HashMap<>();
                tags.put(FirebaseID.mainrequestID, currentUser);
                tags.put(FirebaseID.mainrequestID_message, EditText_request.getText().toString()); //글쓴 날짜
                tags.put(FirebaseID.TimeMillis,System.currentTimeMillis()); //글쓴 날짜
                tags.put(FirebaseID.timestamp, FieldValue.serverTimestamp()); //글쓴 날짜

                mStore.collection(FirebaseID.user).document(currentUser).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String mainphoto = String.valueOf(documentSnapshot.getString(FirebaseID.profilephoto));
                        postId = mStore.collection(FirebaseID.user).document().getId();
                        tags.put(FirebaseID.profilephoto,mainphoto);
                        mStore.collection(FirebaseID.user).document(id).collection(FirebaseID.request).document(postId).set(tags, SetOptions.merge());

                    }
                });

            }
        });

        morerequest_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Store_Information.this, Request_answer.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        if(id == currentUser){
            revisestore.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        mDatas = new ArrayList<>();
        mRequest = new ArrayList<>();

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

                                if (id.equals(UserID)) {
                                    Log.i("문서내의 아이디값은", UserID);
                                    Post data = new Post(documentId, nickname, title, date, deposit, rentfee, location, mainimage);

                                    mDatas.add(data);
                                }
                            }
                            Log.d("mDatas", String.valueOf(mDatas.size()));
                            TextView_product_size.setText(String.valueOf(mDatas.size()));

                            mAdapter = new SimplePostAdapter(mDatas,Store_Information.this);
                            recyclerview.setAdapter(mAdapter);


                        }
                    }
                });
        mStore.collection(FirebaseID.user).document(id).collection(FirebaseID.request).orderBy(FirebaseID.timestamp, Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value != null) {
                            mRequest.clear();  //중복이 되므로, 한번 그전에 초기화를 한번 해주기 .

                            for (DocumentSnapshot snap : value.getDocuments()) {
                                Map<String, Object> shot = snap.getData();
                                String mainrequestID = String.valueOf(shot.get(FirebaseID.mainrequestID));
                                String mainrequestID_message = String.valueOf(shot.get(FirebaseID.mainrequestID_message));
                                String TimeMillis = String.valueOf(shot.get(FirebaseID.TimeMillis));

                                long time = Long.parseLong(TimeMillis);
                                String profile = String.valueOf(shot.get(FirebaseID.profilephoto));

                                Request data = new Request(time,mainrequestID,mainrequestID_message,profile);
                                mRequest.add(data);
                            }

                            RAdapter = new RequestAdapter(mRequest,Store_Information.this);
                            recyclerview_request.setAdapter(RAdapter);
                        }
                    }
                });


    }
}
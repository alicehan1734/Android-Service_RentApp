package org.techtown.android_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.like.LikeButton;
import com.like.OnAnimationEndListener;
import com.like.OnLikeListener;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import org.techtown.android_project.adapters.PostAdapter;
import org.techtown.android_project.adapters.SimplePostAdapter;
import org.techtown.android_project.models.Post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Post2 extends AppCompatActivity implements OnLikeListener {

    private List<Post> mDatas;

    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private TextView mTitleText, mContentsText, mNameText, mdeposit, mrentfee, mlocation, mcategory, nickname_front;
    private String id;
    private String title_2, nickname_2;
    ImageView ImageView_back, viewPagerItem_image1;
    Button Button_checkprice, Button_chatting, Button_revise, Button_cancel;
    final Context c = this;
    ViewPager Viewpager;
    ViewAdapter viewAdapter;
    SpringDotsIndicator dot;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance(); //사용자 가져오기
    private LikeButton heart;
    ArrayList<String> imagearray = new ArrayList<>(4);
    CircleImageView image_person;
    private RecyclerView recyclerview;
    RecyclerView.LayoutManager layoutManager;
    private SimplePostAdapter mAdapter;
    private String postid;
    LinearLayout LinearLayout_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post2);

        heart = findViewById(R.id.heart_button);
        Viewpager = findViewById(R.id.Viewpager_image);
        dot = findViewById(R.id.DotsIndicator);
        Button_revise = findViewById(R.id.Button_revise);
        Button_cancel = findViewById(R.id.Button_cancel);
        recyclerview = findViewById(R.id.recyclerview);
        layoutManager = new GridLayoutManager(this,3);
        recyclerview.setLayoutManager(layoutManager);
        LinearLayout_id = findViewById(R.id.LinearLayout_id);

        //recyclerview.setNestedScrollingEnabled(false);

        mTitleText = findViewById(R.id.post2_title);
        mContentsText = findViewById(R.id.post2_contents);
        mNameText = findViewById(R.id.post2_name);
        mdeposit = findViewById(R.id.deposit);
        mrentfee = findViewById(R.id.rentfee);
        mlocation = findViewById(R.id.location);
        mcategory = findViewById(R.id.category);
        image_person = findViewById(R.id.image_person);
        nickname_front = findViewById(R.id.nickname_front);
        id = getIntent().getStringExtra(FirebaseID.documentID);
        Log.e("ITEM DOCUMENT ID : ", id);

        LinearLayout_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Post2.this, Store_Information.class);
                intent.putExtra(FirebaseID.document_userID, postid);
                startActivity(intent);
            }
        });
        mStore.collection(FirebaseID.post).document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                if (task.getResult() != null) {

                                    Map<String, Object> snap = task.getResult().getData();
                                    String title = String.valueOf(snap.get(FirebaseID.title));
                                    String contents = String.valueOf(snap.get(FirebaseID.contents));
                                    String name = String.valueOf(snap.get(FirebaseID.nickname));
                                    String deposit = String.valueOf(snap.get(FirebaseID.deposit));
                                    String rentfee = String.valueOf(snap.get(FirebaseID.rentfee));
                                    String location = String.valueOf(snap.get(FirebaseID.location));
                                    String UserID = String.valueOf(snap.get(FirebaseID.document_userID));
                                    String category = String.valueOf(snap.get(FirebaseID.category));

                                    mStore.collection(FirebaseID.user).document(UserID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            Map<String, Object> sna = task.getResult().getData();
                                            String nickname2 = String.valueOf(sna.get(FirebaseID.nickname));
                                            nickname_front.setText(nickname2);

                                        }
                                    });

                                    String currentUser = mAuth.getCurrentUser().getUid();
                                    postid = UserID;

                                    if (snap.get(FirebaseID.image) != null) {
                                        imagearray.add(String.valueOf(snap.get(FirebaseID.image)));
                                    }
                                    if (snap.get(FirebaseID.image_2) != null) {
                                        imagearray.add(String.valueOf(snap.get(FirebaseID.image_2)));
                                    }
                                    if (snap.get(FirebaseID.image_3) != null) {
                                        imagearray.add(String.valueOf(snap.get(FirebaseID.image_3)));
                                    }
                                    if (snap.get(FirebaseID.image_4) != null ) {
                                        imagearray.add(String.valueOf(snap.get(FirebaseID.image_4)));
                                        Log.e("사이즈",String.valueOf(snap.get(FirebaseID.image_4)));
                                    }


                                    viewAdapter = new ViewAdapter(Post2.this, imagearray);
                                    Viewpager.setAdapter(viewAdapter);

                                    dot.setViewPager(Viewpager);
                                    title_2 = title;
                                    nickname_2 = name;

                                    mTitleText.setText(title);
                                    mContentsText.setText(contents);
                                    mNameText.setText(name+" 님의 다른 렌트상품");
                                    mdeposit.setText("보증금 " + deposit + " 원");
                                    mrentfee.setText(rentfee + "원");
                                    mlocation.setText(location);
                                    mcategory.setText("카테고리 : " + category);



                                    if (UserID.equals(currentUser)) { //현재유저와 게시물 유저가 같다면..
                                        Button_revise.setVisibility(View.VISIBLE);
                                        Button_cancel.setVisibility(View.VISIBLE);
                                    } else { //현재유저와 게시물 유저가 다르다면..
                                        Button_checkprice.setVisibility(View.VISIBLE);
                                        Button_chatting.setVisibility(View.VISIBLE);

                                    }

                                    mStore.collection(FirebaseID.user).document(postid)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    Map<String, Object> sna = task.getResult().getData();
                                                    String imageurl = String.valueOf(sna.get(FirebaseID.profilephoto));

                                                    if(!imageurl.equals(null)){
                                                    Glide.with(Post2.this).
                                                            load(imageurl).into(image_person);}
                                                }
                                            });
                                }
                            } else {
                                Toast.makeText(Post2.this, "삭제된 문서입니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

        ImageView_back = findViewById(R.id.ImageView_back);
        ImageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        Button_revise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Post2.this, activity_revise_main_creat.class); //수정해야될 사항 가져가기
                intent.putExtra(FirebaseID.documentID, id);
                startActivity(intent);
            }
        });

        Button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(Post2.this);
                dialog.setMessage("삭제하시겠습니까?");
                dialog.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        id = getIntent().getStringExtra(FirebaseID.documentID);
                        mStore.collection(FirebaseID.post).document(id).delete();
                        Toast.makeText(Post2.this, "삭제 되었습니다. 자동으로 뒤로 돌아갑니다. ", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(Post2.this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.setTitle(" 삭제 알림 ");
                dialog.show();
            }
        });
        Button_checkprice = findViewById(R.id.Button_checkprice);
        Button_checkprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder ad = new AlertDialog.Builder(Post2.this);

                ad.setTitle("시세 검색");
                ad.setMessage("주요 키워드를 입력해 주세요. \n네이버 쇼핑 검색결과를 제공합니다.");


                final EditText et = new EditText(Post2.this);
                et.setHint(title_2);
                ad.setView(et);

                ad.setPositiveButton("검색", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Text 값 받아서 로그 남기기
                        String value = et.getText().toString();
                        if (value.equals("")) {
                            Log.i("value is null", value);
                            Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse("https://msearch.shopping.naver.com/search/all?query=" + title_2));
                            startActivity(browser);
                        } else {
                            Log.i("value is not null", value);

                            Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse("https://msearch.shopping.naver.com/search/all?query=" + value));
                            startActivity(browser);
                        }
                        dialog.dismiss();


                    }
                });


                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });
                ad.show();

            }
        });
        Button_chatting = findViewById(R.id.Button_chatting);

        Button_chatting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Post2.this, Chatroom.class);
                intent.putExtra("nickname", nickname_2);
                startActivity(intent);
            }
        });

        heart.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                Toast.makeText(Post2.this, "liked", Toast.LENGTH_SHORT).show();
                Map<String, Boolean> tags = new HashMap<>();
                tags.put(mAuth.getCurrentUser().getUid(), true);

                mStore.collection(FirebaseID.post).document(id).collection("like").document(mAuth.getCurrentUser().getUid()).set(tags, SetOptions.merge());

            }

            @Override
            public void unLiked(LikeButton likeButton) {
                Toast.makeText(Post2.this, "unliked", Toast.LENGTH_SHORT).show();

                Map<String, Boolean> tags = new HashMap<>();
                tags.put(mAuth.getCurrentUser().getUid(), false);

                mStore.collection(FirebaseID.post).document(id).collection("like").document(mAuth.getCurrentUser().getUid()).set(tags, SetOptions.merge());
            }
        });
        recyclerview.addOnItemTouchListener(new RecyclerViewItemClickListener(this, recyclerview, new RecyclerViewItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {      // 아이템을 그냥 클릭했을 때의 상황
                Intent intent = new Intent(Post2.this,Post2.class);
                intent.putExtra(FirebaseID.documentID, mDatas.get(position).getDocumentId());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        mStore.collection(FirebaseID.post).document(id).collection("like").whereEqualTo(mAuth.getCurrentUser().getUid(), true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        heart.setLiked(true);

                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatas = new ArrayList<>();
        mStore.collection(FirebaseID.post).document(id)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()){
                    Map<String, Object> snap = task.getResult().getData();
                    final String UserID2 = String.valueOf(snap.get(FirebaseID.document_userID));

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
                                            Log.d("id", id);

                                            Log.d("nickname", UserID2);
                                            Log.d("nickname2", String.valueOf(UserID));

                                            if (UserID2.equals(UserID)) {
                                                Log.i("문서내의 아이디값은", UserID);
                                                Post data = new Post(documentId, nickname, title, date, deposit, rentfee, location, mainimage);
                                                mDatas.add(data);
                                            }
                                        }

                                        mAdapter = new SimplePostAdapter(mDatas,Post2.this);
                                        recyclerview.setAdapter(mAdapter);
                                    }
                                }
                            });
                }
            }
        });


    }

    @Override
    public void liked(LikeButton likeButton) {

    }

    @Override
    public void unLiked(LikeButton likeButton) {

    }
}
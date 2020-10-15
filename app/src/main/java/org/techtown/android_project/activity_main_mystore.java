package org.techtown.android_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.techtown.android_project.adapters.PostAdapter;
import org.techtown.android_project.models.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class activity_main_mystore extends AppCompatActivity {
//    Button Button_reviseinformation;
    private List<Post> mDatas;
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private PostAdapter mAdapter;
    private RecyclerView mPostRecyclerView;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance(); //사용자 가져오기
    TextView TextView_nickname;
    CircleImageView image_person;
    String nickname;
    String photouri;
    LinearLayout rent, like;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_mystore);
        mPostRecyclerView = findViewById(R.id.main_recyclerview);
        TextView_nickname = findViewById(R.id.TextView_nickname);
        image_person = findViewById(R.id.image_person);
        rent = findViewById(R.id.rent);
        like = findViewById(R.id.like);

        ((View) image_person).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_main_mystore.this, Revise_nickname.class);
                intent.putExtra("nickname",String.valueOf(nickname));
                intent.putExtra("photo",photouri);
                startActivity(intent);
            }
        });

        ImageView button1 = findViewById(R.id.ImageView_setting) ;
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_main_mystore.this, activity_main_setting.class); // 여기에서 activity_main_point 로 인텐트가 이동한다.
                startActivity(intent);

            }
        });

        rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_main_mystore.this, Store_Information.class);
                intent.putExtra(FirebaseID.document_userID, mAuth.getCurrentUser().getUid());
                startActivity(intent);
            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_main_mystore.this, like_listproduct.class);
                intent.putExtra(FirebaseID.document_userID, mAuth.getCurrentUser().getUid());
                startActivity(intent);
            }
        });


        //초기와하기
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //홈화면 선택하기
        bottomNavigationView.setSelectedItemId(R.id.nav_account);

        //itemselectedListener 불러일으키기
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.nav_home:

                        startActivity(new Intent(getApplicationContext(), activity_main_point.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.nav_chat:
                        startActivity(new Intent(getApplicationContext(), activity_main_favorite.class));
                        overridePendingTransition(0,0);
                        return  true;

                    case R.id.nav_search:
                        startActivity(new Intent(getApplicationContext(), activity_main_creat.class));
                        overridePendingTransition(0,0);
                        return  true;

                    case R.id.nav_account:

                        return  true;
                }
                return false;
            }
        });

        mPostRecyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(this, mPostRecyclerView, new RecyclerViewItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {      // 아이템을 그냥 클릭했을 때의 상황
                Intent intent = new Intent(activity_main_mystore.this,Post2.class);
                intent.putExtra(FirebaseID.documentID, mDatas.get(position).getDocumentId());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, final int position) {  // 아이템을 길게 클릭했을때의 상황


                AlertDialog.Builder dialog = new AlertDialog.Builder(activity_main_mystore.this);
                dialog.setMessage("삭제하시겠습니까?");
                dialog.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mStore.collection(FirebaseID.post).document(mDatas.get(position).getDocumentId()).delete();
                        Toast.makeText(activity_main_mystore.this, "삭제 되었습니다. ", Toast.LENGTH_SHORT).show();

                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(activity_main_mystore.this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.setTitle(" 삭제 알림 ");
                dialog.show();
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
                                Log.i("현재나의 아이디값은",mAuth.getCurrentUser().getUid());
                                String currentUser = mAuth.getCurrentUser().getUid();

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

                                    if(currentUser.equals(UserID)) {
                                        Log.i("문서내의 아이디값은",UserID);
                                        Post data = new Post(documentId, nickname, title, date, deposit, rentfee, location,mainimage);
                                    mDatas.add(data);

                                }
                            }

                            mAdapter = new PostAdapter(mDatas,activity_main_mystore.this);
                            mPostRecyclerView.setAdapter(mAdapter);
                        }
                    }
                });
        String id = getIntent().getStringExtra(mAuth.getCurrentUser().getUid());

        mStore.collection(FirebaseID.user).document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Map<String, Object> sna = task.getResult().getData();
                        nickname = String.valueOf(sna.get(FirebaseID.nickname));
                        Log.i("문서내의 아이디값은",nickname);

                        TextView_nickname.setText(nickname);

                        photouri = String.valueOf(sna.get(FirebaseID.profilephoto));
                        if(photouri.equals("null")){
                    }else{
                            Glide.with(activity_main_mystore.this).
                                    load(photouri).into(image_person);}
                    }
                });
    }


}
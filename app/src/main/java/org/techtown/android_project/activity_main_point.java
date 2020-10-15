package org.techtown.android_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.techtown.android_project.adapters.PostAdapter;
import org.techtown.android_project.models.Post;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.techtown.android_project.FirebaseID.image;

public class activity_main_point extends AppCompatActivity {
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    String address;
    private RecyclerView mPostRecyclerView;
    TextView TextView_address;
    private PostAdapter mAdapter;
    private List<Post> mDatas;
    ImageView image_Search;
    TextView EditText_findstuff;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_point);
        TextView_address = findViewById(R.id.TextView_address);
        mPostRecyclerView = findViewById(R.id.main_recyclerview);
        image_Search = findViewById(R.id.image_Search);
        EditText_findstuff = findViewById(R.id.EditText_findstuff);

        TextView_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(activity_main_point.this, "삭제된 문서입니다.", Toast.LENGTH_SHORT).show(); // 확인해야되는요소
                if (address == null) {
                    Toast.makeText(activity_main_point.this, "선택하신 주소가 없으므로 자동으로 지도화면으로 이동합니다. ", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(activity_main_point.this, mainpoint_map.class);
                    startActivityForResult(intent,1);
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(activity_main_point.this);
                    dialog.setMessage("거래 주소를 바꾸시겠습니까?");
                    dialog.setPositiveButton("바꾸기", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(activity_main_point.this, mainpoint_map.class);
                            startActivityForResult(intent,1);

                        }
                    }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(activity_main_point.this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();

                        }
                    });
                    dialog.setTitle(" 주소 변경 ");
                    dialog.show();
                }


            }
        });

        EditText_findstuff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_main_point.this,Search_item.class);
                startActivity(intent);
            }
        });
        Intent intent = getIntent();
        address = intent.getStringExtra("address");
        TextView_address.setText(address); // 다시 주소 찾았서 넘겼던 값을 돌려 받아 화면에 띄우기.

        mPostRecyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(this, mPostRecyclerView, new RecyclerViewItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {      // 아이템을 그냥 클릭했을 때의 상황
                Intent intent = new Intent(activity_main_point.this,Post2.class);
                intent.putExtra(FirebaseID.documentID, mDatas.get(position).getDocumentId());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, final int position) {  // 아이템을 길게 클릭했을때의 상황


//               AlertDialog.Builder dialog = new AlertDialog.Builder(activity_main_point.this);
//             dialog.setMessage("삭제하시겠습니까?");
//             dialog.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
//                 @Override
//                 public void onClick(DialogInterface dialog, int which) {
//                    mStore.collection(FirebaseID.post).document(mDatas.get(position).getDocumentId()).delete();
//                    Toast.makeText(activity_main_point.this, "삭제 되었습니다. ", Toast.LENGTH_SHORT).show();
//
//                 }
//             }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
//                 @Override
//                 public void onClick(DialogInterface dialog, int which) {
//                    Toast.makeText(activity_main_point.this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
//                 }
//             });
//             dialog.setTitle(" 삭제 알림 ");
//             dialog.show();
            }
        }));


        image_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // 이부분에서 지도 다시 생성해서 해야하기.
            }
        });

        //초기화하기
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //홈화면 선택하기
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        //itemselectedListener 불러일으키기
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        return true;

                    case R.id.nav_chat:
                        startActivity(new Intent(getApplicationContext(), activity_main_favorite.class));
                        overridePendingTransition(0,0);
                        return  true;

                    case R.id.nav_search:
                        Intent intent = new Intent(getApplicationContext(), activity_main_creat.class );
                        intent.putExtra("address", address);
                        startActivity(intent);
                        //startActivity(new Intent(getApplicationContext(), activity_main_creat.class));
                        overridePendingTransition(0,0);
                        return  true;

                    case R.id.nav_account:
                        startActivity(new Intent(getApplicationContext(), activity_main_mystore.class));
                        overridePendingTransition(0,0);
                        return  true;
                }
                return false;
            }
        });
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
                                String mainimage = String.valueOf(shot.get(image));

                                Post data = new Post(documentId,nickname, title,date,deposit,rentfee,location,mainimage);
                                mDatas.add(data);
                            }

                            mAdapter = new PostAdapter(mDatas,activity_main_point.this);
                            mPostRecyclerView.setAdapter(mAdapter);
                        }
                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) { //이미지 실행 구간
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK){
                address = intent.getStringExtra("address");
                TextView_address.setText(address);
            }

        }}

}
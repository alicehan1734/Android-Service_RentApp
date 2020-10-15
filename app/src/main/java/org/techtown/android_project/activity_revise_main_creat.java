package org.techtown.android_project;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpicker.Config;
import com.gun0912.tedpicker.ImagePickerActivity;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class activity_revise_main_creat extends AppCompatActivity implements ImageviewAdapter.OnStartDragListener{
    private static final int REQUEST_READ_PHONE_STATE = 100;
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    TextView EditText_rentlocation;
    EditText post_title_edit, EditText_rentfee, EditText_deposit, post_contents_edit;
    TextView mcategory,manyphoto;
    private String id;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance(); //사용자 가져오기
    RelativeLayout RelativeLayout_category;
    RecyclerView recyclerview;
    ImageviewAdapter adapter;
    ArrayList<Uri> image_uris = new ArrayList<>();
    ItemTouchHelper mItemTouchHelper;
    ImageView ImageView_photo;
    private static final int PERMISSIONS_REQUEST_CODE = 100; //퍼미션리퀘스트 코드 확정 설정하는 부분
    // 앱을 실행하기 위해 필요한 퍼미션을 정의합니다.
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};  // 외부 저장소
    private View mLayout;  // Snackbar 사용하기 위해서는 View가 필요합니다.
    Button post_save_button;
    String nickname;
    private StorageReference mStorageRef;
    public Map<String, Object> newsnap;
    private int upload_count = 0 ;
    final List<String> imagearray = new ArrayList<>(4);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revise_main_creat);
        post_save_button = findViewById(R.id.post_save_button);
        EditText_rentlocation = findViewById(R.id.EditText_rentlocation);
        post_title_edit = findViewById(R.id.post_title_edit);
        EditText_rentfee = findViewById(R.id.EditText_rentfee);
        EditText_deposit = findViewById(R.id.EditText_deposit);
        post_contents_edit = findViewById(R.id.post_contents_edit);
        mcategory = findViewById(R.id.category);
        RelativeLayout_category = findViewById(R.id.RelativeLayout_category);
        recyclerview = findViewById(R.id.recyclerview);
        manyphoto = findViewById(R.id.manyphoto);
        mLayout = findViewById(R.id.layout_main); // 사진 퍼미션을 위한 스낵바를 위해 쓰여짐
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads"); //저장소에 관해 정의 해주기

        adapter = new ImageviewAdapter(image_uris, this, this, this);
        ItemTouchHelper.Callback mCallback = new ImageItemTouchHelpderCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(mCallback);
        mItemTouchHelper.attachToRecyclerView(recyclerview);
        recyclerview.setAdapter(adapter); // 어뎁터 작동시작

        RelativeLayout_category.setOnClickListener(new View.OnClickListener() { //카테고리 홈으로 가기
            @Override
            public void onClick(View v) { //카테고리 선택한 부분
                Intent intent = new Intent(activity_revise_main_creat.this, Creat_category.class);
                //  intent.putExtra("category",category_1);
                startActivityForResult(intent, 1);
            }
        });

        EditText_rentlocation.setOnClickListener(new View.OnClickListener() { //지도상의 렌트 주소로 가기
            @Override
            public void onClick(View v) { //주소라인 부분설정.  (startActivityForResult) 로 바꾸기.

                if (EditText_rentlocation == null) {
                    Toast.makeText(activity_revise_main_creat.this, "선택하신 주소가 없으므로 자동으로 지도화면으로 이동합니다. ", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(activity_revise_main_creat.this, mainpoint_map.class);
                    startActivityForResult(intent,1);
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(activity_revise_main_creat.this);
                    dialog.setMessage("거래 주소를 바꾸시겠습니까?");
                    dialog.setPositiveButton("바꾸기", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(activity_revise_main_creat.this, mainpoint_map.class);
                            startActivityForResult(intent,1);

                        }
                    }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(activity_revise_main_creat.this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();

                        }
                    });
                    dialog.setTitle(" 주소 변경 ");
                    dialog.show();
                }
            }
        });

        ImageView_photo = findViewById(R.id.ImageView_photo); //카메라사진을 누르면 사진을 가져올수있도록 하는 이모티콘 설정

        Config config = new Config(); //오픈소스 커스텀 설정
        config.setSelectionMin(1); //최소 한개는 선정해야한다.
        config.setSelectionLimit(4); //최대 5개의 이미지 선정가능.
        config.setSelectedBottomColor(R.color.colorBlue);
        config.setTabSelectionIndicatorColor(R.color.colorBlue);
        ImagePickerActivity.setConfig(config);


        ImageView_photo.setOnClickListener(new View.OnClickListener() {     // 사진퍼미션 받기
            @Override
            public void onClick(View v) {

                int permissionCheckforCamera = ContextCompat.checkSelfPermission(activity_revise_main_creat.this, android.Manifest.permission.CAMERA);
                int permissionCheckforstorage = ContextCompat.checkSelfPermission(activity_revise_main_creat.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                int permissionCheckforcamerastorage = ContextCompat.checkSelfPermission(activity_revise_main_creat.this, Manifest.permission.READ_EXTERNAL_STORAGE);

                if (permissionCheckforCamera == PackageManager.PERMISSION_GRANTED && permissionCheckforstorage == PackageManager.PERMISSION_GRANTED && permissionCheckforcamerastorage == PackageManager.PERMISSION_GRANTED) {
                    // 카메라 다 허용이 되면, 실행할수있다.

                    Intent intent = new Intent(activity_revise_main_creat.this, ImagePickerActivity.class);
                    startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);

                } else {
                    // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity_revise_main_creat.this, REQUIRED_PERMISSIONS[0])) {

                        // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                        Snackbar.make(mLayout, "이 앱을 실행하려면 카메라 (저장소 포함) 접근 권한이 필요합니다.",
                                Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {

                                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                                ActivityCompat.requestPermissions(activity_revise_main_creat.this, REQUIRED_PERMISSIONS,
                                        PERMISSIONS_REQUEST_CODE);
                            }
                        }).show();


                    } else {
                        // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                        // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                        ActivityCompat.requestPermissions(activity_revise_main_creat.this, REQUIRED_PERMISSIONS,
                                PERMISSIONS_REQUEST_CODE);
                    }

                    ActivityCompat.requestPermissions(activity_revise_main_creat.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_READ_PHONE_STATE);
                }
                // startActivityForResult(intent, 1); - 사진 시작하려는 부분

            }
        });

        id = getIntent().getStringExtra(FirebaseID.documentID);
        Log.e("ITEM DOCUMENT ID : ", id);
        mStore.collection(FirebaseID.post).document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists() ) {
                                if (task.getResult() != null) {
                                    Log.e("들어감","enter");

                                    Map<String, Object> snap = task.getResult().getData();
                                    String title = String.valueOf(snap.get(FirebaseID.title));
                                    String contents = String.valueOf(snap.get(FirebaseID.contents));
                                    //String name = String.valueOf(snap.get(FirebaseID.nickname));
                                    String deposit = String.valueOf(snap.get(FirebaseID.deposit));
                                    String rentfee = String.valueOf(snap.get(FirebaseID.rentfee));
                                    String location = String.valueOf(snap.get(FirebaseID.location));
                                    String category = String.valueOf(snap.get(FirebaseID.category));
                                    //String UserID = String.valueOf(snap.get(FirebaseID.document_userID));
                                    //String currentUser = mAuth.getCurrentUser().getUid();

                                    image_uris.clear();

                                    if (snap.get(FirebaseID.image) != null) {
                                        Uri image = Uri.parse(String.valueOf(snap.get(FirebaseID.image)));
                                        image_uris.add(image);

                                    }
                                    if (snap.get(FirebaseID.image_2) != null) {
                                        Uri image_2 = Uri.parse(String.valueOf(snap.get(FirebaseID.image_2)));
                                        image_uris.add(image_2);

                                    }
                                    if (snap.get(FirebaseID.image_3) != null) {
                                        Uri image_3 = Uri.parse(String.valueOf(snap.get(FirebaseID.image_3)));
                                        image_uris.add(image_3);

                                    }
                                    if (snap.get(FirebaseID.image_4) != null ) {
                                        Uri image_4 = Uri.parse(String.valueOf(snap.get(FirebaseID.image_4)));
                                        image_uris.add(image_4);

                                    }

                                    manyphoto.setText(String.valueOf(image_uris.size())); // 다시 주소 찾았서 넘겼던 값을 돌려 받아 화면에 띄우기.
                                    adapter = new ImageviewAdapter(image_uris, activity_revise_main_creat.this, activity_revise_main_creat.this, activity_revise_main_creat.this);
                                    ItemTouchHelper.Callback mCallback = new ImageItemTouchHelpderCallback(adapter);
                                    mItemTouchHelper = new ItemTouchHelper(mCallback);
                                    mItemTouchHelper.attachToRecyclerView(recyclerview);
                                    recyclerview.setAdapter(adapter); // 어뎁터 작동시작
                                    Log.e("title",title);

                                    post_title_edit.setText(title);
                                    post_contents_edit.setText(contents);
                                    EditText_deposit.setText(deposit);
                                    EditText_rentfee.setText(rentfee);
                                    EditText_rentlocation.setText(location);
                                    mcategory.setText(category);
                                }
                            } else {
                                Toast.makeText(activity_revise_main_creat.this, "삭제된 문서입니다.", Toast.LENGTH_SHORT).show();
                            }}
                    }
                });

        if (mAuth.getCurrentUser() != null) { //현재 유저가 없을 일은 없지만, 좀 정리되게 하고싶은마음에 씀.
            mStore.collection(FirebaseID.user).document(mAuth.getCurrentUser().getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.getResult() != null) {
                                nickname = (String) task.getResult().getData().get(FirebaseID.nickname);
                            }
                        }
                    });
        }

        post_save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newsnap = new HashMap<>();
                newsnap.put(FirebaseID.documentID, id); //mAuth.getCurrentUser().getUid() - 사용자에 관한 아이디 값 //실제 문서의 아이디값
                newsnap.put(FirebaseID.document_userID, mAuth.getCurrentUser().getUid()); //글쓴이의 아이디
                newsnap.put(FirebaseID.nickname, nickname); //닉네임
                newsnap.put(FirebaseID.title, post_title_edit.getText().toString()); //타이틀
                newsnap.put(FirebaseID.contents, post_contents_edit.getText().toString()); //자세한 내용
                newsnap.put(FirebaseID.deposit, EditText_deposit.getText().toString()); //보증금
                newsnap.put(FirebaseID.rentfee, EditText_rentfee.getText().toString()); //렌트비 (1일)
                newsnap.put(FirebaseID.location, EditText_rentlocation.getText().toString()); //거래 지역
                newsnap.put(FirebaseID.timestamp, FieldValue.serverTimestamp()); //글쓴 날짜
                newsnap.put(FirebaseID.timecalculate, System.currentTimeMillis());
                newsnap.put(FirebaseID.category, mcategory.getText().toString());

                if (image_uris != null ) {
                    // StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(image_uris));
                    for(upload_count = 0 ; upload_count < image_uris.size() ; upload_count++) {

                        Uri IndividualImage = Uri.fromFile(new File(String.valueOf(image_uris.get(upload_count))));
                        final StorageReference ImageName = mStorageRef.child("Image" + IndividualImage.getLastPathSegment());
                        String url = String.valueOf(IndividualImage);
                        Log.i("uploadcount", String.valueOf(upload_count));


                        ImageName.putFile(IndividualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Log.i("uploadcount", String.valueOf(upload_count));

                                ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        String url = String.valueOf(uri);
                                        imagearray.add(url);

                                        Log.i("uploadcount", String.valueOf(imagearray));
                                        Log.i("uploadcount", String.valueOf(upload_count));

                                    }
                                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {

                                        if(imagearray.size() == upload_count) {
                                            newsnap.put(FirebaseID.image, imagearray.get(0));
                                            if(upload_count>=2) {
                                                newsnap.put(FirebaseID.image_2, imagearray.get(1));
                                            }
                                            if(upload_count>=3){
                                                newsnap.put(FirebaseID.image_3, imagearray.get(2));}
                                            if(upload_count>=4) {
                                                newsnap.put(FirebaseID.image_4, imagearray.get(3));
                                            }
                                            Log.i("마지막!", String.valueOf(imagearray));
                                            mStore.collection(FirebaseID.post).document(id).set(newsnap, SetOptions.merge());

                                        }

                                    }
                                });
                            }
                        });
                    }
                }else {
                    Toast.makeText(activity_revise_main_creat.this, " No file selected ", Toast.LENGTH_SHORT).show();
                }


                mStore.collection(FirebaseID.post).document(id)
                        .set(newsnap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully written!");
//                        mStore.collection(FirebaseID.post).document(id).set(newsnap, SetOptions.merge());
                        Toast.makeText(activity_revise_main_creat.this, "정상적으로 수정되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(activity_revise_main_creat.this, activity_main_point.class);
                        startActivity(intent);

                        finish();


                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("TAG", "Error writing document", e);
                            }
                        });
            }
        });
}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) { //카테고리,이미지, 주소 실행 구간
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == 1) { //카테고리에 관한 영역
            if (resultCode == RESULT_FIRST_USER) {
                String category_1 = intent.getStringExtra("category");
                mcategory.setText(category_1);
            }
            if (resultCode == RESULT_OK){
                String address = intent.getStringExtra("address");
                EditText_rentlocation.setText(address);
            }

        }
        if (requestCode == INTENT_REQUEST_GET_IMAGES && resultCode == Activity.RESULT_OK) { //이미지에 관한 영역
            image_uris.clear();
            image_uris.addAll(intent.<Uri>getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS)); //이미지 Uri 에 오픈소스에 받았던 정보를 넣어놈

            manyphoto.setText(String.valueOf(image_uris.size())); // 다시 주소 찾았서 넘겼던 값을 돌려 받아 화면에 띄우기.
            adapter = new ImageviewAdapter(image_uris, activity_revise_main_creat.this, activity_revise_main_creat.this, activity_revise_main_creat.this);
            ItemTouchHelper.Callback mCallback = new ImageItemTouchHelpderCallback(adapter);
            mItemTouchHelper = new ItemTouchHelper(mCallback);
            mItemTouchHelper.attachToRecyclerView(recyclerview);
            recyclerview.setAdapter(adapter); // 어뎁터 작동시작

            if (image_uris.size() != 0) { //선택한 이미지의 사이즈가 있는경우
                manyphoto.setText(String.valueOf(image_uris.size())); // 다시 주소 찾았서 넘겼던 값을 돌려 받아 화면에 띄우기.
                adapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    public void onStartDrag(ImageviewAdapter.ViewHolder holder) {
        mItemTouchHelper.startDrag(holder);
    }

    @Override
    public void onDelete() {
        manyphoto.setText(String.valueOf(image_uris.size()));
    }

}

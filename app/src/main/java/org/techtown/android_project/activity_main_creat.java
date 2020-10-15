package org.techtown.android_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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


import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class activity_main_creat extends AppCompatActivity implements ImageviewAdapter.OnStartDragListener{
    private static final int REQUEST_READ_PHONE_STATE = 100;
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    // final int PICTURE_REQUEST_CODE = 100;
    private StorageReference mStorageRef;
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance(); //사용자 가져오기
    private EditText mTitle, mContents, mRentfee, mdeposit;
    private TextView location;
    private String nickname;
    ImageView ImageView_photo;
    Button Button_clendar;
    String address, category_1, Imagesize;
    private TextView EditText_rentlocation, category, manyphoto;
    ImageView ImageView_cancel;
    RelativeLayout RelativeLayout_category;
    RecyclerView recyclerview;
    ArrayList<Uri> image_uris = new ArrayList<>();
    private int upload_count = 0 ;
    public Map<String, Object> data;
    ItemTouchHelper mItemTouchHelper;
    ImageviewAdapter adapter;
    public Map<String, String> image;
    String postId;
    String image_1,image_2,image_3,image_4;
    final List<String> imagearray = new ArrayList<>(4);
    private DecimalFormat decimalFormat = new DecimalFormat("#,###");
    private String result="";

    private View mLayout;  // Snackbar 사용하기 위해서는 View가 필요합니다.
    // onRequestPermissionsResult에서 수신된 결과에서 ActivityCompat.requestPermissions를 사용한 퍼미션 요청을 구별하기 위해 사용됩니다.
    private static final int PERMISSIONS_REQUEST_CODE = 100; //퍼미션리퀘스트 코드 확정 설정하는 부분

    // 앱을 실행하기 위해 필요한 퍼미션을 정의합니다.
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};  // 외부 저장소

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_creat);
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads"); //저장소에 관해 정의 해주기

        EditText_rentlocation = findViewById(R.id.EditText_rentlocation);
        ImageView_cancel = findViewById(R.id.ImageView_cancel);
        RelativeLayout_category = findViewById(R.id.RelativeLayout_category);
        category = findViewById(R.id.category);
        recyclerview = findViewById(R.id.recyclerview);
        manyphoto = findViewById(R.id.manyphoto);

        adapter = new ImageviewAdapter(image_uris, this, this, this);
        ItemTouchHelper.Callback mCallback = new ImageItemTouchHelpderCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(mCallback);
        mItemTouchHelper.attachToRecyclerView(recyclerview);
        recyclerview.setAdapter(adapter); // 어뎁터 작동시작

        // category_1 = intent.getStringExtra("category");
        // category.setText(category_1); // 카테고리에 넘겼던 값을돌려 받아 화면에 띄우기.
     //   Intent intent = getIntent();
       // address = intent.getStringExtra("address"); //받은 주소 부분, 추후 다시 바껴야될듯. (startActivityForResult)
        EditText_rentlocation.setText(address); // 다시 주소 찾았서 넘겼던 값을 돌려 받아 화면에 띄우기.

        RelativeLayout_category.setOnClickListener(new View.OnClickListener() { //카테고리 홈으로 가기
            @Override
            public void onClick(View v) { //카테고리 선택한 부분
                Intent intent = new Intent(activity_main_creat.this, Creat_category.class);
                //  intent.putExtra("category",category_1);
                startActivityForResult(intent, 1);

            }
        });
        ImageView_cancel.setOnClickListener(new View.OnClickListener() {   //뒤로 넘어가는 씬 (메인 홈으로 돌아가도록 했음.)
            @Override
            public void onClick(View v) { //뒤로 돌아가는 부분. (인텐트로 넘기기, finish() 설정했지만, 오류가 남.)
                startActivity(new Intent(getApplicationContext(), activity_main_point.class));
                overridePendingTransition(0, 0); //전환되는 색깔 액티비티를 해지해주는 구간. 굳이 이 화면에는 없어도됨. (혹시몰라 해놓음, 추후 테스트 후에 없애기)
            }
        });
        EditText_rentlocation.setOnClickListener(new View.OnClickListener() { //지도상의 렌트 주소로 가기
            @Override
            public void onClick(View v) { //주소라인 부분설정.  (startActivityForResult) 로 바꾸기.

                if (address == null) {
                    Toast.makeText(activity_main_creat.this, "선택하신 주소가 없으므로 자동으로 지도화면으로 이동합니다. ", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(activity_main_creat.this, mainpoint_map.class);
                    startActivityForResult(intent,1);
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(activity_main_creat.this);
                    dialog.setMessage("거래 주소를 바꾸시겠습니까?");
                    dialog.setPositiveButton("바꾸기", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(activity_main_creat.this, mainpoint_map.class);
                            startActivityForResult(intent,1);

                        }
                    }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(activity_main_creat.this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();

                        }
                    });
                    dialog.setTitle(" 주소 변경 ");
                    dialog.show();
                }
            }
        });

        mLayout = findViewById(R.id.layout_main); // 사진 퍼미션을 위한 스낵바를 위해 쓰여짐

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

                int permissionCheckforCamera = ContextCompat.checkSelfPermission(activity_main_creat.this, android.Manifest.permission.CAMERA);
                int permissionCheckforstorage = ContextCompat.checkSelfPermission(activity_main_creat.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                int permissionCheckforcamerastorage = ContextCompat.checkSelfPermission(activity_main_creat.this, Manifest.permission.READ_EXTERNAL_STORAGE);

                if (permissionCheckforCamera == PackageManager.PERMISSION_GRANTED && permissionCheckforstorage == PackageManager.PERMISSION_GRANTED && permissionCheckforcamerastorage == PackageManager.PERMISSION_GRANTED) {
                    // 카메라 다 허용이 되면, 실행할수있다.

                    Intent intent = new Intent(activity_main_creat.this, ImagePickerActivity.class);
                    startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);

                } else {
                    // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity_main_creat.this, REQUIRED_PERMISSIONS[0])) {

                        // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                        Snackbar.make(mLayout, "이 앱을 실행하려면 카메라 (저장소 포함) 접근 권한이 필요합니다.",
                                Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {

                                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                                ActivityCompat.requestPermissions(activity_main_creat.this, REQUIRED_PERMISSIONS,
                                        PERMISSIONS_REQUEST_CODE);
                            }
                        }).show();


                    } else {
                        // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                        // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                        ActivityCompat.requestPermissions(activity_main_creat.this, REQUIRED_PERMISSIONS,
                                PERMISSIONS_REQUEST_CODE);
                    }

                    ActivityCompat.requestPermissions(activity_main_creat.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_READ_PHONE_STATE);
                }
                // startActivityForResult(intent, 1); - 사진 시작하려는 부분

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
        mTitle = findViewById(R.id.post_title_edit);
        mContents = findViewById(R.id.post_contents_edit);
        mdeposit = findViewById(R.id.EditText_deposit);
        mRentfee = findViewById(R.id.EditText_rentfee);
        location = findViewById(R.id.EditText_rentlocation);

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence.toString()) && !charSequence.toString().equals(result)){
                    result = decimalFormat.format(Double.parseDouble(charSequence.toString().replaceAll(",","")));
                    mRentfee.setText(result);
                    mRentfee.setSelection(result.length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        TextWatcher watcher2 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence.toString()) && !charSequence.toString().equals(result)){
                    result = decimalFormat.format(Double.parseDouble(charSequence.toString().replaceAll(",","")));
                    mdeposit.setText(result);
                    mdeposit.setSelection(result.length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        mRentfee.addTextChangedListener(watcher);
        mdeposit.addTextChangedListener(watcher2);
        findViewById(R.id.post_save_button).setOnClickListener(new View.OnClickListener() { //최종 저장라인
            @Override
            public void onClick(View v) {
                if (mAuth.getCurrentUser() != null) {
                    postId = mStore.collection(FirebaseID.post).document().getId();

                    data = new HashMap<>(); //                    Map<String, Object> data = new HashMap<>(); <= 기존엔 public 으로 안함
                    data.put(FirebaseID.documentID, postId); //mAuth.getCurrentUser().getUid() - 사용자에 관한 아이디 값 //실제 문서의 아이디값
                    data.put(FirebaseID.document_userID, mAuth.getCurrentUser().getUid()); //글쓴이의 아이디
                    data.put(FirebaseID.nickname, nickname); //닉네임
                    data.put(FirebaseID.title, mTitle.getText().toString()); //타이틀
                    data.put(FirebaseID.contents, mContents.getText().toString()); //자세한 내용
                    data.put(FirebaseID.deposit, mdeposit.getText().toString()); //보증금
                    data.put(FirebaseID.rentfee, mRentfee.getText().toString()); //렌트비 (1일)
                    data.put(FirebaseID.location, location.getText().toString()); //거래 지역
                    data.put(FirebaseID.timestamp, FieldValue.serverTimestamp()); //글쓴 날짜
                    data.put(FirebaseID.timecalculate, System.currentTimeMillis());
                    data.put(FirebaseID.category, category.getText().toString());

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
                                                data.put(FirebaseID.image, imagearray.get(0));
                                                if(upload_count>=2) {
                                                    data.put(FirebaseID.image_2, imagearray.get(1));
                                                }
                                                if(upload_count>=3){
                                                data.put(FirebaseID.image_3, imagearray.get(2));}
                                                if(upload_count>=4) {
                                                    data.put(FirebaseID.image_4, imagearray.get(3));
                                                }
                                                Log.i("마지막!", String.valueOf(imagearray));
                                                mStore.collection(FirebaseID.post).document(postId).set(data, SetOptions.merge());

                                            }

                                        }
                                    });
                                }
                            });
                        }
                    }else {
                        Toast.makeText(activity_main_creat.this, " No file selected ", Toast.LENGTH_SHORT).show();
                    }


                    //data.put(FirebaseID.imagelink,image);


                    mStore.collection(FirebaseID.post).document(postId).set(data, SetOptions.merge());

                    Intent intent = new Intent(activity_main_creat.this, activity_main_point.class); // 이미지 저장이 안되어서 새로 추가함 (10.6)
                    intent.putExtra("address", address);
                    startActivity(intent); // 이미지 저장이 안되어서 새로 추가함 (10.6)

                    // startActivity(new Intent(getApplicationContext(), activity_main_point.class)); //기존에 이것만 호출함.

                    overridePendingTransition(0, 0);
                }
            }
        });


        Button_clendar = findViewById(R.id.Button_clendar); //칼렌더라인, 곧 업어질 예정
        Button_clendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_main_creat.this, CalendarView.class);
                startActivity(intent);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) { //카테고리,이미지, 주소 실행 구간
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == 1) { //카테고리에 관한 영역
            if (resultCode == RESULT_FIRST_USER) {
                category_1 = intent.getStringExtra("category");
                category.setText(category_1);
            }
            if (resultCode == RESULT_OK){
                address = intent.getStringExtra("address");
                EditText_rentlocation.setText(address);
            }

        }

        if (requestCode == INTENT_REQUEST_GET_IMAGES && resultCode == Activity.RESULT_OK) { //이미지에 관한 영역
            image_uris.clear();
            image_uris.addAll(intent.<Uri>getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS)); //이미지 Uri 에 오픈소스에 받았던 정보를 넣어놈

            if (image_uris.size() != 0) { //선택한 이미지의 사이즈가 있는경우
                manyphoto.setText(String.valueOf(image_uris.size())); // 다시 주소 찾았서 넘겼던 값을 돌려 받아 화면에 띄우기.
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) { //사진퍼미션 받는 부분이다.
        if (requestCode == PERMISSIONS_REQUEST_CODE && grantResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if (check_result) {

                // 퍼미션을 허용했다면 위치 업데이트를 시작합니다.
                Intent intent = new Intent(activity_main_creat.this, ImagePickerActivity.class);
                startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);

            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {


                    // 사용자가 거부만 선택한 경우에는 앱을 다시 실행하여 허용을 선택하면 앱을 사용할 수 있습니다.
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            finish();
                        }
                    }).show();

                } else {


                    // "다시 묻지 않음"을 사용자가 체크하고 거부를 선택한 경우에는 설정(앱 정보)에서 퍼미션을 허용해야 앱을 사용할 수 있습니다.
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            finish();
                        }
                    }).show();
                }
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

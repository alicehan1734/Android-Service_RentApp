package org.techtown.android_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import io.grpc.Context;

public class Revise_nickname extends AppCompatActivity {
    CircleImageView image_person;
    EditText nickname_2;
    public Uri imageUri;
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance(); //사용자 가져오기
    private StorageReference mStorageRef;
    String id,photouri;
    TextView completebutton, textsize;
    Button commonbutton;
    String nickname;
    int check,checkimage = 0;
    String url_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revise_nickname);

        completebutton = findViewById(R.id.completebutton);
        image_person = findViewById(R.id.image_person);
        nickname_2 = findViewById(R.id.EditText_nickname);
        id = mAuth.getCurrentUser().getUid();
        textsize= findViewById(R.id.textsize);
        commonbutton = findViewById(R.id.commonbutton);

        commonbutton.setSelected(true);

        Intent intent = getIntent();
        nickname = intent.getStringExtra("nickname");
        nickname_2.setText(nickname);
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads"); //저장소에 관해 정의 해주기
        String photouri = intent.getStringExtra("photo");

        textsize.setText(nickname_2.length() + " /7");
        if (photouri.equals("null")) {
        } else {
            Glide.with(Revise_nickname.this).
                    load(photouri).into(image_person);
        }

        image_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });

        completebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check > 0 ){
                mStore.collection(FirebaseID.user).document(id).update("nickname",nickname_2.getText().toString());
                if(checkimage>0) {
                    uploadPicture();
                }else{
                    finish();
                }

                }
                else{
                    Toast.makeText(getApplicationContext(), "닉네임 중복확인을 해주세요. ", Toast.LENGTH_LONG).show();

                }
            }
        });

        nickname_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = nickname_2.getText().toString();
                textsize.setText(input.length()+" /7");
                commonbutton.setText("중복확인");

                if(input.length()<=7){
                    commonbutton.setEnabled(true);
                    if(input.equals(nickname))
                    {
                        commonbutton.setSelected(false);
                    }
                }else{
                        commonbutton.setEnabled(false);
                    }
                }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        commonbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStore.collection(FirebaseID.user).whereEqualTo("nickname",nickname_2.getText().toString()).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            int i = 0;

                            for(DocumentSnapshot document : task.getResult()){
                                i++;
                                Log.d("정보", document.getId() + "=>" +document.getData());
                                if(document.getId().equals(id)){
                                    Snackbar.make(findViewById(R.id.EditText_nickname), "현재사용하고 계신 닉네임입니다. ", Snackbar.LENGTH_LONG).show();
                                    i = 0;
                                }
                            }

                            Log.d("숫자값", String.valueOf(i));

                            if(i==0) {
                                Snackbar.make(findViewById(R.id.EditText_nickname), "중복확인 되셨습니다. ", Snackbar.LENGTH_LONG).show();
                                commonbutton.setText("확인완료");
                                commonbutton.setEnabled(false);
                                check ++;

                            }else{
                                Snackbar.make(findViewById(R.id.EditText_nickname), "현재 다른사용자가 사용하고 계십니다. ", Snackbar.LENGTH_LONG).show();

                            }


                        }else{

                        }
                    }
                });
            }
        });


    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode==RESULT_OK && data!=null && data.getData()!=null) {
            imageUri = data.getData();
            image_person.setImageURI(imageUri);
            checkimage ++;

        }else{
        }
    }

    private void uploadPicture(){

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("업로드 중입니다...");
        pd.show();

        final String randomkey = UUID.randomUUID().toString();
        final StorageReference ImageName = mStorageRef.child("profile" + randomkey);

        ImageName.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();
                Snackbar.make(findViewById(R.id.EditText_nickname), "업로드 되었습니다. ",Snackbar.LENGTH_LONG).show();

                ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = String.valueOf(uri);

                        Map<String, Object> data = new HashMap<>();
                        data.put(FirebaseID.profilephoto,url);
                        mStore.collection(FirebaseID.user).document(id).set(data, SetOptions.merge());
                        finish();
                    }
                });
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();

                        Toast.makeText(getApplicationContext(), "업로드 실패했습니다.", Toast.LENGTH_LONG).show();
                    }
                })

                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        pd.setMessage("업로드중입니다.");
                    }
                });

    }
}
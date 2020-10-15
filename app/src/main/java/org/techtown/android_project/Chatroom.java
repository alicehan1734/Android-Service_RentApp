package org.techtown.android_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class Chatroom extends AppCompatActivity {

    TextView TextView_nickname;
    ImageView ImageView_back;
    ImageView ImageView_chat;
    ImageView btn_send;
    EditText text_send;

    FirebaseUser fuser;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance(); //사용자 가져오기
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);
        ImageView_back = findViewById(R.id.ImageView_back);
        ImageView_chat = findViewById(R.id.ImageView_chat);
        TextView_nickname = findViewById(R.id.TextView_nickname);
        text_send= findViewById(R.id.EditText_chatbar);
        btn_send = findViewById(R.id.send);

        Intent intent = getIntent();
        TextView_nickname.setText(intent.getStringExtra("nickname"));
        ImageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImageView_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Chatroom.this, activity_main_favorite.class);
                startActivity(intent);

                finish();
            }
        });

        mStorageRef = FirebaseStorage.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid());

    }

    private void sendMessage(String sender, String receiver, String message) {

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads"); //저장소에 관해 정의 해주기


        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);

//        mStorageRef.child("chats").push().setValue(hashMap);


    }


}
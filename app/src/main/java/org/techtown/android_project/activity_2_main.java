package org.techtown.android_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static org.techtown.android_project.FirebaseID.email;

public class activity_2_main extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    TextInputEditText TextInputEditText_email, TextInputEditText_password;
    Button Button_login;
    Button Button_signup;
    Switch Switch_auto_login;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_main);


       TextInputEditText_email = findViewById(R.id.TextInputEditText_email); //컴포넌트를 찾아라
        TextInputEditText_password = findViewById(R.id.TextInputEditText_password);
        Switch_auto_login = findViewById(R.id.Switch_auto_login);

        //1.값을 가져온다.
        //2. 클릭을 감지한다.
        //3. 1번의 값을 다음 액티비티로 넘긴다.

        Button_login = findViewById(R.id.Button_login);

        Button_login.setClickable(true); //다시한번더, 로그인버튼이 눌러줄수있다는것 세팅
        Button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //다음액티비티로 넘어가는것 실행하기
                mAuth.signInWithEmailAndPassword(TextInputEditText_email.getText().toString(), TextInputEditText_password.getText().toString())
                        .addOnCompleteListener(activity_2_main.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) { // 로그인 성공
                                   FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {

                                        Switch_auto_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {     //자동 로그인 툴부터 만들기
                                                if (isChecked == true){
                                                    onStart();    // 자동로그인 메소드 불러오기
                                                }else{

                                                }
                                            }
                                        });

                                        Toast.makeText(activity_2_main.this," 로그인 성공되었습니다. ", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(activity_2_main.this,activity_main_point.class));

                                        Intent intent = new Intent(activity_2_main.this, activity_6_checkdistance.class);
                                        startActivity(intent);
                                    }
                                } else {
                                    Toast.makeText(activity_2_main.this, "로그인정보가 없습니다. 회원가입을 해주세요.", Toast.LENGTH_SHORT).show();
                                }
                            }

                        });
            }
        });


        Button_signup = findViewById(R.id.Button_signup);

        Button_signup.setClickable(true); //다시한번더, 로그인버튼이 눌러줄수있다는것 세팅
        Button_signup.setOnClickListener(new View.OnClickListener() {   //회원가입 버튼
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_2_main.this, activity_3_signup_page.class);
                startActivity(intent);
            }
        });


        Intent intent = getIntent();

        email = intent.getStringExtra("email");
        TextInputEditText_email.setText(email); // 다시 주소 찾았서 넘겼던 값을 돌려 받아 화면에 띄우기.
    }



//protected void onStart() { //자동 로그인 메소드 만들기
//
//    super.onStart();
//    FirebaseUser user = mAuth.getCurrentUser();
//    if (user != null){
//        Toast.makeText(this,"자동 로그인이 되셨습니다. "+ user.getUid(), Toast.LENGTH_SHORT).show();
//        startActivity(new Intent(this,activity_6_checkdistance.class));
//    }
//}
}
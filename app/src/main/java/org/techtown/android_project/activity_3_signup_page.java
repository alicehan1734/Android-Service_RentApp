package org.techtown.android_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class activity_3_signup_page extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                   "(?=.*[0-9])" +         //at least 1 digit
                  //  "(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                        //    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                      //      "(?=\\S+$)" +           //no white spaces
                            ".{4,12}" +               //at least 4 characters
                            "$");

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();

    private TextInputLayout mNickname, EditText_email, EditText_password;
    private String emailInput, usernameInput, validatePassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3_signup_page);
        EditText_email = findViewById(R.id.EditText_email);
        EditText_password = findViewById(R.id.EditText_password);
        mNickname = findViewById(R.id.sign_nickname);


        findViewById(R.id.Button_nextstep).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //버튼 클릭후 그 후 행동
                if(!validateEmail() | !validatePassword()  | !validateUsername()){

                    return; }
                mAuth.createUserWithEmailAndPassword(emailInput, validatePassword)
                        .addOnCompleteListener(activity_3_signup_page.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {                                     //회원가입성공
                                        Map<String, Object> userMap = new HashMap<>();
                                        userMap.put(FirebaseID.documentID, user.getUid()); //관리하기가 쉽게 되기위해서, FirebaseID 에서 따로 불러서 바꾸면 된다.
                                        userMap.put(FirebaseID.nickname, usernameInput);
                                        userMap.put(FirebaseID.email, emailInput);
                                        userMap.put(FirebaseID.password,validatePassword);
                                        mStore.collection(FirebaseID.user).document(user.getUid()).set(userMap, SetOptions.merge()); // 덮어쓰기를 이용한다.
                                        Toast.makeText(activity_3_signup_page.this, "축하합니다. 정상적으로 회원가입이 되셨습니다.",
                                                Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(activity_3_signup_page.this, activity_2_main.class);
                                        intent.putExtra("email", emailInput);
                                        startActivity(intent);

                                                finish();
                                    } else {
                                        Toast.makeText(activity_3_signup_page.this, "회원가입이 실패되었습니다.",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                } else {

                                    Toast.makeText(activity_3_signup_page.this, "회원가입이 실패되었습니다. 해당 규격에 맞게 작성해주세요.",
                                            Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                    String Input = "이메일: " + EditText_email.getEditText().getText().toString();
                    Input += "\n";
                    Input += "닉네임: " + mNickname.getEditText().getText().toString();
                    Input += "\n";
                    Input += "비밀번호: " + EditText_password.getEditText().getText().toString();

                    Toast.makeText(activity_3_signup_page.this, Input,Toast.LENGTH_LONG).show();

            }
        });

    }

    private boolean validateEmail() {
        emailInput = EditText_email.getEditText().getText().toString().trim();
        if (emailInput.isEmpty()) {
            EditText_email.setError("이메일을 입력해주세요.");
            return false;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
            EditText_email.setError("유효한 이메일을 입력해주세요.");
            return false;
        }
        else {
            EditText_email.setError(null);

            return true;
        }
    }

    private boolean validateUsername() {
        usernameInput = mNickname.getEditText().getText().toString().trim();

        if (usernameInput.isEmpty()) {
            mNickname.setError("닉네임을 입력해주세요.");
            return false;

        }else if (usernameInput.length() > 10) {
            mNickname.setError("닉네임이 너무 길어요.");
            return false;

        }else {
            mNickname.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        validatePassword = EditText_password.getEditText().getText().toString().trim();
        if (validatePassword.isEmpty()) {
            EditText_password.setError("비밀번호를 입력해주세요.");
            return false;
        } else if(!PASSWORD_PATTERN.matcher(validatePassword).matches()){

            EditText_password.setError("* 영문, 숫자 모두 포함(4 ~ 12자) ");
            return false;

        }else {
            EditText_password.setError(null);
            return true;
        }
    }


}
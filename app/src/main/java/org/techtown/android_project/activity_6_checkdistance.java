package org.techtown.android_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class activity_6_checkdistance extends AppCompatActivity {
    String address;

    TextView TextView_findposition;
    ImageView ImageView_cancel;
    Button Button_checksave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_6_checkdistance);

        TextView_findposition = findViewById(R.id.TextView_findposition);
        TextView_findposition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_6_checkdistance.this, activity_6_checkdistance_sub.class);
                startActivity(intent);
            }
        });

        ImageView_cancel = findViewById(R.id.ImageView_cancel);
        ImageView_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_6_checkdistance.this, activity_main_point.class);
                startActivity(intent);
            }
        });
        Intent intent = getIntent();

        address = intent.getStringExtra("address");
        TextView_findposition.setText(address); // 다시 주소 찾았서 넘겼던 값을 돌려 받아 화면에 띄우기.

        Button_checksave = findViewById(R.id.Button_checksave);
        Button_checksave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_6_checkdistance.this, activity_main_point.class);
                intent.putExtra("address", address); // 인텐트를 통해 주소 옮기기
                startActivity(intent);
            }
        });
    }
}
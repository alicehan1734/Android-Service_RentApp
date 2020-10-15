package org.techtown.android_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class activity_6_checkdistance_sub extends AppCompatActivity {
    TextView TextView_findposition, textView2;
    String address;
    Button Button_savedistance;
   ImageView ImageView_out;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_6_checkdistance_sub);
        TextView_findposition = findViewById(R.id.TextView_findposition);
        Button_savedistance = findViewById(R.id.Button_savedistance);
        ImageView_out = findViewById(R.id.ImageView_out);
        textView2 = findViewById(R.id.textView2);

        TextView_findposition.setOnClickListener(new View.OnClickListener() {  // 주소찾기 바를 클릭했을시
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_6_checkdistance_sub.this, map.class);
                startActivity(intent);
            }
        });

        Button_savedistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity_6_checkdistance_sub.this, activity_6_checkdistance.class);
                intent.putExtra("address", textView2.getText().toString()); // 인텐트를 통해 주소 옮기기
                startActivity(intent);

            }
        });

        ImageView_out.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_6_checkdistance_sub.this, activity_6_checkdistance.class);
                startActivity(intent);
            }
        });


        Intent intent = getIntent();

        address = intent.getStringExtra("address");
        textView2.setText(address); // 다시 주소 찾았서 넘겼던 값을 돌려 받아 화면에 띄우기.

    }}
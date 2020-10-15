package org.techtown.android_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class activity_main_setting extends AppCompatActivity {


    Button Button_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_setting);

        Button_logout = findViewById(R.id.Button_logout);

        Button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        Button button1 = (Button) findViewById(R.id.version) ;
        button1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeView(0) ;
            }
        });

        Button button2 = (Button) findViewById(R.id.alarm) ;
        button2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeView(1) ;
            }
        });

        Button button3 = (Button) findViewById(R.id.statement) ;
        button3.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeView(2) ;
            }
        });

        changeView(0) ;

    }


    private void changeView(int index) {

        // 자식(Children) 뷰들에 대한 참조 획득.
        ConstraintLayout ConstraintLayout_version = (ConstraintLayout) findViewById(R.id.ConstraintLayout_version) ;
        ConstraintLayout ConstraintLayout_alarm = (ConstraintLayout) findViewById(R.id.ConstraintLayout_alarm) ;
        ScrollView ScrollView_statement = (ScrollView) findViewById(R.id.ScrollView_statement) ;

        // index에 따라 자식(Children) 뷰 들의 visibility 설정.
        switch (index) {
            case 0 :
                ConstraintLayout_version.setVisibility(View.VISIBLE) ;
                ConstraintLayout_alarm.setVisibility(View.INVISIBLE) ;
                ScrollView_statement.setVisibility(View.INVISIBLE) ;
                break ;
            case 1 :
                ConstraintLayout_version.setVisibility(View.INVISIBLE) ;
                ConstraintLayout_alarm.setVisibility(View.VISIBLE) ;
                ScrollView_statement.setVisibility(View.INVISIBLE) ;
                break ;
            case 2 :
                ConstraintLayout_version.setVisibility(View.INVISIBLE) ;
                ConstraintLayout_alarm.setVisibility(View.INVISIBLE) ;
                ScrollView_statement.setVisibility(View.VISIBLE) ;
                break ;
        }
    }
}






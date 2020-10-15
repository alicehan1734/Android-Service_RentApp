package org.techtown.android_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class activity_main_favorite extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);

        //초기와하기
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //홈화면 선택하기
        bottomNavigationView.setSelectedItemId(R.id.nav_chat);

        //itemselectedListener 불러일으키기
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.nav_home:

                        startActivity(new Intent(getApplicationContext(), activity_main_point.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.nav_chat:

                        return  true;

                    case R.id.nav_search:
                        startActivity(new Intent(getApplicationContext(), activity_main_creat.class));
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

}
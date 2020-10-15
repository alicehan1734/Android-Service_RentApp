package org.techtown.android_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Creat_category extends AppCompatActivity {

    ListView listView;
    ImageView ImageView_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_category);

        listView = (ListView)findViewById(R.id.listview);
        ImageView_cancel = findViewById(R.id.ImageView_cancel);

        ImageView_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final ArrayList<String> arraylist = new ArrayList<>();

        arraylist.add("디지털/가전");
        arraylist.add("가구/인테리어");
        arraylist.add("유아동/유아도서");
        arraylist.add("생활/가공식품");
        arraylist.add("스포츠/레저");
        arraylist.add("여성잡화");
        arraylist.add("여성의류");
        arraylist.add("남성패션/잡화");
        arraylist.add("게임/취미");
        arraylist.add("반려동물용품");
        arraylist.add("도서/티켓/음반");
        arraylist.add("기타");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arraylist);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Creat_category.this, "선택한 카테고리는 " +arraylist.get(position),Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Creat_category.this, activity_main_creat.class);
                intent.putExtra("category", arraylist.get(position)); // 인텐트를 통해 주소 옮기기
                setResult(RESULT_FIRST_USER, intent);

                Intent intentx = new Intent(Creat_category.this, activity_revise_main_creat.class);
                intentx.putExtra("category", arraylist.get(position)); // 인텐트를 통해 주소 옮기기
                setResult(RESULT_FIRST_USER, intentx);

                finish();
            }
        });

    }
}
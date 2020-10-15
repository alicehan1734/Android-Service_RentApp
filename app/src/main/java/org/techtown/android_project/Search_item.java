package org.techtown.android_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Search_item extends AppCompatActivity {
    ImageView back;
    List<DataItem> dataItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item);
        dataItems = new ArrayList<>();

        dataItems.add(new DataItem(R.drawable.categoryhome_appliance,"디지털/가전"));
        dataItems.add(new DataItem(R.drawable.furniture,"가구/인테리어"));
        dataItems.add(new DataItem(R.drawable.infant,"유아동/유아도서"));
        dataItems.add(new DataItem(R.drawable.living,"생활/가공식품"));
        dataItems.add(new DataItem(R.drawable.sports,"스포츠/레저"));
        dataItems.add(new DataItem(R.drawable.ladybag,"여성잡화"));
        dataItems.add(new DataItem(R.drawable.dress,"여성의류"));
        dataItems.add(new DataItem(R.drawable.manfashion,"남성패션/잡화"));
        dataItems.add(new DataItem(R.drawable.game,"게임/취미"));
        dataItems.add(new DataItem(R.drawable.dog,"반려동물용품"));
        dataItems.add(new DataItem(R.drawable.book,"도서/티켓/음반"));
        dataItems.add(new DataItem(R.drawable.etc,"기타"));


        ListView listView = (ListView)findViewById(R.id.listView);

        CategoryAdapter adapter = new CategoryAdapter(this,R.layout.itemrow,dataItems);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //다른 일 할것이다.
                Intent intent = new Intent(Search_item.this,searchItem_category.class);
               intent.putExtra("category",dataItems.get(position).categoryname);

               startActivity(intent);

            }
        });

    back = findViewById(R.id.back);

    back.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    });
    }




}
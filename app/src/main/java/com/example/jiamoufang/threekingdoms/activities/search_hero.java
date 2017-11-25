package com.example.jiamoufang.threekingdoms.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.jiamoufang.threekingdoms.R;
import com.example.jiamoufang.threekingdoms.heros.LocalHero;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class search_hero extends AppCompatActivity {
    private ListView searchList;
    private List<LocalHero> searchItems = new ArrayList<LocalHero>();   //用于装搜索结果的list
    private List<Map<String, Object>> headerlist = new ArrayList<>();   //视图上的list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_hero);

        searchList = (ListView)findViewById(R.id.search_items);
        final SimpleAdapter simpleAdapter = new SimpleAdapter(search_hero.this, headerlist, R.layout.search_item,
                new String[]{"image","name", "detail"}, new int[]{R.id.image,R.id.name, R.id.detail});
        searchList.setAdapter(simpleAdapter);


        Map<String, Object> temp = new LinkedHashMap<>();
        temp.put("image", "drawable/guanyu");
        temp.put("name", "关羽");
        headerlist.add(temp);

        //实际上要根据searchItems更新视图

        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });



    }
}

package com.example.jiamoufang.threekingdoms.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jiamoufang.threekingdoms.MainActivity;
import com.example.jiamoufang.threekingdoms.MyMusic;
import com.example.jiamoufang.threekingdoms.R;
import com.example.jiamoufang.threekingdoms.heros.LocalHero;

import static com.example.jiamoufang.threekingdoms.MainActivity.Herolist;

/**
 * Created by jiamoufang on 2017/11/5.
 */

public class HeroDetailsActivity extends AppCompatActivity {
    public static final String HERO_NAME = "heroName";
    public static final String HERO_IMAGE_ID = "heroImageId";
    private MyMusic myMusic;
    private Toolbar toolbar;
    private String heroName;
    private int sig = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hero_details);

        Intent intent = getIntent();
        heroName = intent.getStringExtra(HERO_NAME);
        int heroImageId = intent.getIntExtra(HERO_IMAGE_ID,0);
        String introduction = intent.getStringExtra("introduction");

        toolbar = (Toolbar)findViewById(R.id.toolbar_heroDetails);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collaspsing_heroDetails);
        CoordinatorLayout coordinatorlayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);
        ImageView heroImage = (ImageView)findViewById(R.id.iamge_hero_details);
        TextView heroTextContent = (TextView)findViewById(R.id.textView_heroDetails);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();


        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        collapsingToolbarLayout.setTitle(heroName);
        Glide.with(this).load(heroImageId).into(heroImage);
        coordinatorlayout.setBackgroundResource(heroImageId);
        heroTextContent.setText(introduction);

        myMusic = new MyMusic(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!MainActivity.isMute) {
            myMusic.initMusic(R.raw.herodetails_music);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!MainActivity.isMute)
            myMusic.releaseMusic();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //加载菜单文件
        getMenuInflater().inflate(R.menu.pop_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.detail_edit:
                Intent intent = new Intent(HeroDetailsActivity.this, AddHero.class);
                intent.putExtra("editHero", heroName);
                startActivityForResult(intent, 3);
                break;
            case R.id.detail_delete:
                AlertDialog.Builder message = new AlertDialog.Builder(this);
                message.setTitle("");
                message.setMessage("确定删除英雄" + heroName + "?");
                message.setNegativeButton("取消",  new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                message.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sig = 1;
                        for(int j = 0; j < Herolist.size(); j++) {
                            if(heroName.equals(Herolist.get(j).getName())) {
                                Herolist.remove(j);
                                break;
                            }
                        }
                        finish();
                    }
                });
                message.create().show();
                break;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

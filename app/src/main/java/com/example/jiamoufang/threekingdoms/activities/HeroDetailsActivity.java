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
    //public static final String HERO_IMAGE_BYTES = "heroImageBytes";
    private MyMusic myMusic;
    private Toolbar toolbar;
    private String heroName;
    private  CollapsingToolbarLayout collapsingToolbarLayout;
    private  CoordinatorLayout coordinatorlayout;
    private  ImageView heroImage;
    private TextView heroTextContent;
    private TextView info_heroDetails;
    private int sig = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hero_details);

        Intent intent = getIntent();
        heroName = intent.getStringExtra(HERO_NAME);
        int heroImageId = intent.getIntExtra(HERO_IMAGE_ID,0);
        String introduction = intent.getStringExtra("introduction");
        String birth = intent.getStringExtra("birth");
        String address = intent.getStringExtra("address");
        String sex = intent.getStringExtra("sex");
        String belong = intent.getStringExtra("belong");


        toolbar = (Toolbar)findViewById(R.id.toolbar_heroDetails);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collaspsing_heroDetails);
        coordinatorlayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);
        heroImage = (ImageView)findViewById(R.id.iamge_hero_details);
        heroTextContent = (TextView)findViewById(R.id.textView_heroDetails);
        info_heroDetails = (TextView) findViewById(R.id.info_heroDetails);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();


        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        collapsingToolbarLayout.setTitle(heroName);
        Glide.with(this).load(heroImageId).into(heroImage);
       coordinatorlayout.setBackgroundResource(heroImageId);
        heroTextContent.setText(introduction);
        info_heroDetails.setText(sex + ", 生卒：" + birth +", 籍贯：" +  address + ", 主校势力：" + belong);

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
                        /*
                        * 只有finish是不行的，自己再想想
                        * */
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 3:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String name = bundle.getString("name");
                    int index = -1;
                    for (int i = 0; i < Herolist.size(); i++) {
                        if (Herolist.get(i).getName().equals(name)) {
                            index = 0;
                            break;
                        }
                    }
                    /*修改数据*/
                    LocalHero localHero = Herolist.get(index);
                    localHero.setName(name);
                    localHero.setSex(bundle.getString("sex"));
                    localHero.setDate(bundle.getString("birth"));
                    localHero.setPlace(bundle.getString("address"));
                    localHero.setState(bundle.getString("belong"));
                    localHero.setForce(Integer.parseInt(bundle.getString("attack")));
                    localHero.setIntelligence(Integer.parseInt(bundle.getString("intelligence")));
                    localHero.setLeadership(Integer.parseInt(bundle.getString("leadership")));
                    localHero.setForage(Integer.parseInt(bundle.getString("food")));
                    localHero.setArmy();
                    localHero.setIntroduction(bundle.getString("introduction"));
                    localHero.setHeroImageId(bundle.getInt("imageId"));
                    Herolist.remove(index);
                    Herolist.add(index,localHero);
                    /*重新渲染*/
                    heroName = name;
                    collapsingToolbarLayout.setTitle(heroName);
                    Glide.with(this).load(bundle.getInt("imageId")).into(heroImage);
                    coordinatorlayout.setBackgroundResource(bundle.getInt("imageId"));
                    heroTextContent.setText(bundle.getString("introduction"));
                    info_heroDetails.setText(bundle.getString("sex")+ ", 生卒：" + bundle.getString("birth") + ", 籍贯：" + bundle.getString("address")
                            + ", 主校势力：" + bundle.getString("belong"));

                }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

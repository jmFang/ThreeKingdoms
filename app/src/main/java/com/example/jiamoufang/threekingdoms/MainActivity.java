package com.example.jiamoufang.threekingdoms;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.jiamoufang.threekingdoms.activities.AddHero;
import com.example.jiamoufang.threekingdoms.entities.Person;
import com.example.jiamoufang.threekingdoms.fragment.HerosListFragment;
import com.example.jiamoufang.threekingdoms.fragment.HerosPKFragment;
import com.example.jiamoufang.threekingdoms.fragment.HitHeroFragment;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends FragmentActivity implements View.OnClickListener{
    private DrawerLayout mDrawerLayout;

    private LinearLayout mTabHitHero;
    private LinearLayout mTabHeroPk;
    private LinearLayout mTabHeroList;

    /*
    * 底部 Tabs
    * */
    private ImageButton mImgHeroHit;
    private ImageButton mImgHeroPk;
    private ImageButton mImgHeroList;

    /*
    * Fragments
    * */
    private Fragment mTab01;
    private Fragment mTab02;
    private Fragment mTab03;

    /*
    * 滑动抽屉菜单的点击NavigationView
    * */
    private NavigationView navigationView;
    private View nav_headerView;
    private CircleImageView nav_headerImg;

    /*
    * 播放音樂MediaPlayer
    * */
    private List<MediaPlayer> mediaPlayerList = new ArrayList<>();
    private int currentFragmentIndex = -1;
    public static boolean isMute = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationView = (NavigationView)findViewById(R.id.nav_view);
        nav_headerView = navigationView.getHeaderView(0);
        nav_headerImg = nav_headerView.findViewById(R.id.nav_icon_image);

        /*
        * 初始化云端
        * */
        Bmob.initialize(this,"885d634d2f139989576fd66a85664c55");

        /*
        * 云端测试
        * */
        Person p2 = new Person();
        p2.setName("lucky girl ddd");
        p2.setAddress("北京海淀");
        p2.save(new SaveListener<String>() {
            @Override
            public void done(String objectId,BmobException e) {
                if(e==null){
                    Toast.makeText(MainActivity.this, "添加数据成功，返回objectId为："+objectId, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "创建数据失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        BmobQuery<Person> bmomquery = new BmobQuery<>();
        bmomquery.getObject("3f611ed547", new QueryListener<Person>() {
            @Override
            public void done(Person person, BmobException e) {
                if(e == null) {
                    Toast.makeText(MainActivity.this, "查询成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*
        * navigationView的选择事件
        * */
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.my_hero:
                        Toast.makeText(MainActivity.this, "you select 我的英雄", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.add_hero:
                        Intent toAddHero = new Intent(MainActivity.this, AddHero.class);
                        startActivity(toAddHero);
                        //Toast.makeText(MainActivity.this, "you select 添加英雄", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.del_hero:
                        Toast.makeText(MainActivity.this, "you select 删除英雄", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.invite_hero:
                        Toast.makeText(MainActivity.this, "you select 邀请好友", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.settings:
                        Toast.makeText(MainActivity.this, "you select 设置", Toast.LENGTH_SHORT).show();
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("背景音樂")
                                .setMessage("是否調為靜音")
                                .setPositiveButton("一刻寧靜", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (isMute != true) {
                                            isMute = true;
                                            findTargetMusic();
                                        }
                                    }
                                })
                                .setNegativeButton("給我噪起來", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (isMute != false) {
                                            isMute = false;
                                            findTargetMusic();
                                        }
                                    }
                                }).show();

                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        initView();
        initEvent();
    }

    private void setSelect(final int i) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);

        switch(i) {
            case 0:
                if (mTab01 == null) {
                    mTab01 = new HitHeroFragment();
                    transaction.add(R.id.id_content, mTab01, "main");
                } else {
                    transaction.show(mTab01);
                }
                break;
            case 1:
                if (mTab02 == null) {
                    mTab02 = new HerosPKFragment();
                   transaction.add(R.id.id_content, mTab02, "battle");
                } else {
                    //transaction.add(R.id.id_content, mTab02);
                    transaction.show(mTab02);
                }
                break;
            case 2:
                if (mTab03 == null) {
                    mTab03 = new HerosListFragment();
                    transaction.add(R.id.id_content, mTab03, "list");
                } else {
                    //transaction.add(R.id.id_content, mTab03);
                    transaction.show(mTab03);
                }
                break;
            default:
                break;
        }
        transaction.commit();

        changAndStartMusic(i);
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (mTab01 != null) {
            transaction.hide(mTab01);
        }
        if (mTab02 != null) {
            transaction.hide(mTab02);
        }
        if (mTab03 != null) {
            transaction.hide(mTab03);
        }
    }

    private void initEvent() {
        mTabHitHero.setOnClickListener(this);
        mTabHeroPk.setOnClickListener(this);
        mTabHeroList.setOnClickListener(this);
        nav_headerImg.setOnClickListener(this);
    }

    private void initView() {
        mTabHitHero = (LinearLayout)findViewById(R.id.id_tab_hit_hero);
        mTabHeroPk = (LinearLayout)findViewById(R.id.id_tab_heros_pk);
        mTabHeroList = (LinearLayout)findViewById(R.id.id_tab_herolist);

        mImgHeroHit = (ImageButton) findViewById(R.id.id_tab_hit_hero_img);
        mImgHeroPk = (ImageButton) findViewById(R.id.id_tab_heros_pk_img);
        mImgHeroList = (ImageButton) findViewById(R.id.id_tab_herolist_img);

        //mTab01 = new HitHeroFragment();
       // mTab02 = new HerosPKFragment();
        //mTab03 = new HerosListFragment();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.id_tab_hit_hero:
                setSelect(0);
                break;
            case R.id.id_tab_heros_pk:
                setSelect(1);
                break;
            case R.id.id_tab_herolist:
                setSelect(2);
                break;
            case R.id.nav_icon_image:
                Toast.makeText(this, "你点击了导航的头像", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

    }

    public void initMediaPlayer() {
        mediaPlayerList.add(MediaPlayer.create(this, R.raw.main_music));
        mediaPlayerList.add(MediaPlayer.create(this, R.raw.battle_music));
        mediaPlayerList.add(MediaPlayer.create(this, R.raw.herolist_music));

        for (int i = 0; i < mediaPlayerList.size(); i++) {
            final MediaPlayer mediaPlayer = mediaPlayerList.get(i);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
        }
    }

    public void changAndStartMusic(int i) {
        for (int t = 0; t < mediaPlayerList.size(); t++) {
            if (mediaPlayerList.get(t).isPlaying()) {
                mediaPlayerList.get(t).pause();
            }
        }
        mediaPlayerList.get(i).seekTo(0);
        if (!isMute) {
            mediaPlayerList.get(i).start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        findTargetMusic();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (getSupportFragmentManager().findFragmentByTag("main") != null && getSupportFragmentManager().findFragmentByTag("main").isVisible()) {
            currentFragmentIndex = 0;
        } else if (getSupportFragmentManager().findFragmentByTag("battle") != null && getSupportFragmentManager().findFragmentByTag("battle").isVisible()) {
            currentFragmentIndex = 1;
        } else {
            currentFragmentIndex = 2;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        for (int i = 0; i < mediaPlayerList.size(); i++) {
            if (mediaPlayerList.get(i) != null) {
                mediaPlayerList.get(i).release();
            }
        }
        mediaPlayerList.clear();
    }

    public void findTargetMusic() {
        initMediaPlayer();
        switch (currentFragmentIndex) {
            case -1:
                setSelect(0);
                break;
            case 0:
                setSelect(0);
                break;
            case 1:
                setSelect(1);
                break;
            default:
                setSelect(2);
                break;
        }
    }
}
package com.example.jiamoufang.threekingdoms;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ListView;
import android.widget.Toast;

import com.example.jiamoufang.threekingdoms.activities.AddHero;
import com.example.jiamoufang.threekingdoms.adapter.SelectHeroToEditorAdapter;
import com.example.jiamoufang.threekingdoms.api.ApiOfDatabase;
import com.example.jiamoufang.threekingdoms.entities.NonEditedHero;
import com.example.jiamoufang.threekingdoms.entities.PkRecords;
import com.example.jiamoufang.threekingdoms.fragment.HerosListFragment;
import com.example.jiamoufang.threekingdoms.fragment.HerosPKFragment;
import com.example.jiamoufang.threekingdoms.fragment.HitHeroFragment;
import com.example.jiamoufang.threekingdoms.heros.LocalHero;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.http.bean.Api;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends FragmentActivity implements View.OnClickListener{
    /*
    * 装所有Hero示例的list
    * 有几个activity都需要访问这个变量，通过设置为static的方式方便访问
    * 唯一标识，设置变量的时候注意一下应该不会出错
    * */
    public static List<LocalHero> Herolist = new ArrayList<>();

    /*尚未编辑的英雄，在添加界面可供选择编辑添加*/
    public static List<NonEditedHero> NonEditedHeroList = new ArrayList<>();

    /*英雄对决记录，在群英会界面使用，每次对决添加*/
    public static List<PkRecords> pkRecordsList = new ArrayList<>();

    /*我喜欢的英雄*/
    public static List<LocalHero> MylovedHeros = new ArrayList<>();

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

        /*创建本地数据库*/
        LitePal.getDatabase();

        /*
        * 实例化几个Localhero类
        * */
        InitAddHeros();

        /*
        * 实例化尚未编辑的英雄
        * */
        InitNonEditedHeros();

        /*
        * 初始化云端
        * */
        Bmob.initialize(this,"885d634d2f139989576fd66a85664c55");

        /*
        * 云端测试
        * */
       /* Person p2 = new Person();
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
        });*/

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

        mImgHeroHit.setImageResource(R.mipmap.tab_weixin_pressed);
        mImgHeroPk.setImageResource(R.mipmap.tab_find_frd_normal);
        mImgHeroList.setImageResource(R.mipmap.tab_settings_normal);
        //mTab01 = new HitHeroFragment();
       // mTab02 = new HerosPKFragment();
        //mTab03 = new HerosListFragment();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.id_tab_hit_hero:
                mImgHeroHit.setImageResource(R.mipmap.tab_weixin_pressed);
                mImgHeroPk.setImageResource(R.mipmap.tab_find_frd_normal);
                mImgHeroList.setImageResource(R.mipmap.tab_settings_normal);
                setSelect(0);
                break;
            case R.id.id_tab_heros_pk:
                mImgHeroHit.setImageResource(R.mipmap.tab_weixin_normal);
                mImgHeroPk.setImageResource(R.mipmap.tab_find_frd_pressed);
                mImgHeroList.setImageResource(R.mipmap.tab_settings_normal);
                setSelect(1);
                break;
            case R.id.id_tab_herolist:
                mImgHeroHit.setImageResource(R.mipmap.tab_weixin_normal);
                mImgHeroPk.setImageResource(R.mipmap.tab_find_frd_normal);
                mImgHeroList.setImageResource(R.mipmap.tab_settings_pressed);
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

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }


    public void InitAddHeros() {

        List<LocalHero> tmp = DataSupport.findAll(LocalHero.class);
        if (tmp.size() == 0) {
            Herolist.add(new LocalHero("关羽",R.drawable.guanyu,"男","????", "出生地","蜀","历史简介：\n 前将军。本字长生，亡命奔涿郡。与张飞追随刘备征战，当刘备为平原相时，他们俩为别部司马。二人与刘备寝则同床，恩若兄弟。稠人广坐则侍立终日。随同曹操和刘备讨吕布于下邳，事后为朝廷封为中郎将。当刘备袭杀曹操的徐州刺史车冑后，以关羽镇下邳太守。曹操东征破刘备，关羽被俘，被拜为偏将军，对他礼遇很优厚。白马之战时关羽万军中刺敌主帅颜良，被封为汉寿亭侯，报了曹操之恩后便告辞，寻找刘备。长阪之战刘备败北，抄近路赴汉津与关羽的数百只船汇合至江夏。赤壁之战孙刘联军胜利后，关羽在其后的江陵之战中绝北道，阻隔曹操的援军，为周瑜能攻下江陵创造有利的条件。事后遙领襄阳太守、拜为荡寇将军。诸葛亮等人入蜀增援刘备，关羽便镇荊州。刘备自立为汉中王，被拜为前将军，假节钺。关羽乘汉水暴涨之机出兵襄樊，更在于禁七军为水所淹乘船进攻，梁、郏、陆浑各县的盗贼有些远远地领受了关羽的官印封号，威震华夏；曹操一度想迁都避其锋芒。后因孙权背盟投曹，后方为吕蒙所破，关羽便退兵，但终为孙权军所擒杀。"
                    ,99, 80, 85, 900));
            Herolist.add(new LocalHero("姜维", R.drawable.jiangwei,"男", "生卒", "籍贯","蜀","历史简介：\n 大家好我是姜维",85, 95, 95, 800));
            Herolist.add(new LocalHero("刘备", R.drawable.liubei,"男", "生卒", "籍贯","蜀","刘备 历史简介 \n 蜀汉的开国皇帝，相传是汉景帝之子中山靖王刘胜的后代。刘备少年丧父，与母亲贩鞋织草席为生。黄巾起义时，刘备组织义兵，随政府军剿除黄巾，有功，任安喜县尉，不久因鞭打督邮弃官。后诸侯割据，刘备势力弱小，经常寄人篱下，先后投靠过公孙瓒、曹操、袁绍、刘表等人，几经波折，却仍无自己的地盘。赤壁之战之际，刘备联吴抗曹，取得胜利，从东吴处“借”到荆州，迅速发展起来，吞并益州，占领汉中，建立蜀汉政权。后关羽战死，荆州被孙权夺取，刘备于称帝后伐吴，在夷陵之战中被陆逊击败，病逝于白帝城，临终托孤于诸葛亮。"
                    ,80, 85, 90, 1000));
            Herolist.add(new LocalHero("吕布", R.drawable.lvbu,"男","????", "出生地","蜀","历史简介：\n 前将军。本字长生，亡命奔涿郡。与张飞追随刘备征战，当刘备为平原相时，他们俩为别部司马。二人与刘备寝则同床，恩若兄弟。稠人广坐则侍立终日。随同曹操和刘备讨吕布于下邳，事后为朝廷封为中郎将。当刘备袭杀曹操的徐州刺史车冑后，以关羽镇下邳太守。曹操东征破刘备，关羽被俘，被拜为偏将军，对他礼遇很优厚。白马之战时关羽万军中刺敌主帅颜良，被封为汉寿亭侯，报了曹操之恩后便告辞，寻找刘备。长阪之战刘备败北，抄近路赴汉津与关羽的数百只船汇合至江夏。赤壁之战孙刘联军胜利后，关羽在其后的江陵之战中绝北道，阻隔曹操的援军，为周瑜能攻下江陵创造有利的条件。事后遙领襄阳太守、拜为荡寇将军。诸葛亮等人入蜀增援刘备，关羽便镇荊州。刘备自立为汉中王，被拜为前将军，假节钺。关羽乘汉水暴涨之机出兵襄樊，更在于禁七军为水所淹乘船进攻，梁、郏、陆浑各县的盗贼有些远远地领受了关羽的官印封号，威震华夏；曹操一度想迁都避其锋芒。后因孙权背盟投曹，后方为吕蒙所破，关羽便退兵，但终为孙权军所擒杀。"
                    ,100, 60, 70, 900));
            Herolist.add(new LocalHero("许褚", R.drawable.xuchu,"男","????", "出生地","蜀","历史简介：\n 前将军。本字长生，亡命奔涿郡。与张飞追随刘备征战，当刘备为平原相时，他们俩为别部司马。二人与刘备寝则同床，恩若兄弟。稠人广坐则侍立终日。随同曹操和刘备讨吕布于下邳，事后为朝廷封为中郎将。当刘备袭杀曹操的徐州刺史车冑后，以关羽镇下邳太守。曹操东征破刘备，关羽被俘，被拜为偏将军，对他礼遇很优厚。白马之战时关羽万军中刺敌主帅颜良，被封为汉寿亭侯，报了曹操之恩后便告辞，寻找刘备。长阪之战刘备败北，抄近路赴汉津与关羽的数百只船汇合至江夏。赤壁之战孙刘联军胜利后，关羽在其后的江陵之战中绝北道，阻隔曹操的援军，为周瑜能攻下江陵创造有利的条件。事后遙领襄阳太守、拜为荡寇将军。诸葛亮等人入蜀增援刘备，关羽便镇荊州。刘备自立为汉中王，被拜为前将军，假节钺。关羽乘汉水暴涨之机出兵襄樊，更在于禁七军为水所淹乘船进攻，梁、郏、陆浑各县的盗贼有些远远地领受了关羽的官印封号，威震华夏；曹操一度想迁都避其锋芒。后因孙权背盟投曹，后方为吕蒙所破，关羽便退兵，但终为孙权军所擒杀。"
                    ,95, 70, 75, 900));
            Herolist.add(new LocalHero("颜良",  R.drawable.yanliang,"男","????", "出生地","蜀","历史简介：\n 前将军。本字长生，亡命奔涿郡。与张飞追随刘备征战，当刘备为平原相时，他们俩为别部司马。二人与刘备寝则同床，恩若兄弟。稠人广坐则侍立终日。随同曹操和刘备讨吕布于下邳，事后为朝廷封为中郎将。当刘备袭杀曹操的徐州刺史车冑后，以关羽镇下邳太守。曹操东征破刘备，关羽被俘，被拜为偏将军，对他礼遇很优厚。白马之战时关羽万军中刺敌主帅颜良，被封为汉寿亭侯，报了曹操之恩后便告辞，寻找刘备。长阪之战刘备败北，抄近路赴汉津与关羽的数百只船汇合至江夏。赤壁之战孙刘联军胜利后，关羽在其后的江陵之战中绝北道，阻隔曹操的援军，为周瑜能攻下江陵创造有利的条件。事后遙领襄阳太守、拜为荡寇将军。诸葛亮等人入蜀增援刘备，关羽便镇荊州。刘备自立为汉中王，被拜为前将军，假节钺。关羽乘汉水暴涨之机出兵襄樊，更在于禁七军为水所淹乘船进攻，梁、郏、陆浑各县的盗贼有些远远地领受了关羽的官印封号，威震华夏；曹操一度想迁都避其锋芒。后因孙权背盟投曹，后方为吕蒙所破，关羽便退兵，但终为孙权军所擒杀。"
                    ,97, 80, 80, 900));
            Herolist.add(new LocalHero("张飞", R.drawable.zhangfei,"男","????", "出生地","蜀","历史简介：\n 前将军。本字长生，亡命奔涿郡。与张飞追随刘备征战，当刘备为平原相时，他们俩为别部司马。二人与刘备寝则同床，恩若兄弟。稠人广坐则侍立终日。随同曹操和刘备讨吕布于下邳，事后为朝廷封为中郎将。当刘备袭杀曹操的徐州刺史车冑后，以关羽镇下邳太守。曹操东征破刘备，关羽被俘，被拜为偏将军，对他礼遇很优厚。白马之战时关羽万军中刺敌主帅颜良，被封为汉寿亭侯，报了曹操之恩后便告辞，寻找刘备。长阪之战刘备败北，抄近路赴汉津与关羽的数百只船汇合至江夏。赤壁之战孙刘联军胜利后，关羽在其后的江陵之战中绝北道，阻隔曹操的援军，为周瑜能攻下江陵创造有利的条件。事后遙领襄阳太守、拜为荡寇将军。诸葛亮等人入蜀增援刘备，关羽便镇荊州。刘备自立为汉中王，被拜为前将军，假节钺。关羽乘汉水暴涨之机出兵襄樊，更在于禁七军为水所淹乘船进攻，梁、郏、陆浑各县的盗贼有些远远地领受了关羽的官印封号，威震华夏；曹操一度想迁都避其锋芒。后因孙权背盟投曹，后方为吕蒙所破，关羽便退兵，但终为孙权军所擒杀。"
                    ,97, 75, 85, 900));
            Herolist.add(new LocalHero("周瑜",R.drawable.zhouyu,"男","????", "出生地","蜀","历史简介：\n 前将军。本字长生，亡命奔涿郡。与张飞追随刘备征战，当刘备为平原相时，他们俩为别部司马。二人与刘备寝则同床，恩若兄弟。稠人广坐则侍立终日。随同曹操和刘备讨吕布于下邳，事后为朝廷封为中郎将。当刘备袭杀曹操的徐州刺史车冑后，以关羽镇下邳太守。曹操东征破刘备，关羽被俘，被拜为偏将军，对他礼遇很优厚。白马之战时关羽万军中刺敌主帅颜良，被封为汉寿亭侯，报了曹操之恩后便告辞，寻找刘备。长阪之战刘备败北，抄近路赴汉津与关羽的数百只船汇合至江夏。赤壁之战孙刘联军胜利后，关羽在其后的江陵之战中绝北道，阻隔曹操的援军，为周瑜能攻下江陵创造有利的条件。事后遙领襄阳太守、拜为荡寇将军。诸葛亮等人入蜀增援刘备，关羽便镇荊州。刘备自立为汉中王，被拜为前将军，假节钺。关羽乘汉水暴涨之机出兵襄樊，更在于禁七军为水所淹乘船进攻，梁、郏、陆浑各县的盗贼有些远远地领受了关羽的官印封号，威震华夏；曹操一度想迁都避其锋芒。后因孙权背盟投曹，后方为吕蒙所破，关羽便退兵，但终为孙权军所擒杀。"
                    ,80, 90, 90, 900));
        } else {
            Herolist.clear();
            for (int i = 0; i < tmp.size(); i++) {
                /*只显示8条
                * 其它用于搜索
                * */
                if (i < 8 ) {
                    Herolist.add(tmp.get(i));
                }
            }
        }

        /*
        * 写入数据库
        * 注意第二次开启APP不重写，以后只会使用增删查改单步操作
        * */
        if (tmp.size() == 0) {
            ApiOfDatabase apiOfDatabase = new ApiOfDatabase();
            apiOfDatabase.WriteLocalHeroToDatabase(Herolist);
        }
    }

    private void InitNonEditedHeros() {

        List<NonEditedHero> tmp = DataSupport.findAll(NonEditedHero.class);
        if (tmp.size() == 0) {
            NonEditedHeroList.add(new NonEditedHero("左慈",R.drawable.zuoci));
            NonEditedHeroList.add(new NonEditedHero("祖茂",R.drawable.zumao));
            NonEditedHeroList.add(new NonEditedHero("诸葛瑾",R.drawable.zhugejin));
            NonEditedHeroList.add(new NonEditedHero("诸葛均",R.drawable.zhugejun));
            NonEditedHeroList.add(new NonEditedHero("朱灵",R.drawable.zhuling));
            NonEditedHeroList.add(new NonEditedHero("周仓",R.drawable.zhoucang));
            NonEditedHeroList.add(new NonEditedHero("钟繇",R.drawable.zhongyao));
            NonEditedHeroList.add(new NonEditedHero("曹植",R.drawable.caozhi));
            NonEditedHeroList.add(new NonEditedHero("曹丕",R.drawable.caopi));
            NonEditedHeroList.add(new NonEditedHero("鲁肃",R.drawable.lusu));
            NonEditedHeroList.add(new NonEditedHero("陆逊",R.drawable.luxun));
            NonEditedHeroList.add(new NonEditedHero("吕蒙",R.drawable.lvmeng));
            NonEditedHeroList.add(new NonEditedHero("张辽",R.drawable.zhangliao));
            NonEditedHeroList.add(new NonEditedHero("华雄",R.drawable.huaxiong));
            NonEditedHeroList.add(new NonEditedHero("孙坚",R.drawable.sunjian));
            NonEditedHeroList.add(new NonEditedHero("孙权",R.drawable.sunquan));
        } else {
            NonEditedHeroList.clear();
            NonEditedHeroList = tmp;
        }

        /*
        * 写入数据库,注意第二次开启APP不重写
        * 以后只会使用增删查改单步操作
        * */

        if (tmp.size() == 0) {
            ApiOfDatabase apiOfDatabase = new ApiOfDatabase();
            apiOfDatabase.WriteNonEditedHeroToDatabase(NonEditedHeroList);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
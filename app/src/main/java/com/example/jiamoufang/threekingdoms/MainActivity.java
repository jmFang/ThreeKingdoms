package com.example.jiamoufang.threekingdoms;

import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.jiamoufang.threekingdoms.fragment.HerosListFragment;
import com.example.jiamoufang.threekingdoms.fragment.HerosPKFragment;
import com.example.jiamoufang.threekingdoms.fragment.HitHeroFragment;

public class MainActivity extends FragmentActivity implements View.OnClickListener{
    private DrawerLayout mDrawerLayout;

    private LinearLayout mTabHitHero;
    private LinearLayout mTabHeroPk;
    private LinearLayout mTabHeroList;

    private ImageButton mImgHeroHit;
    private ImageButton mImgHeroPk;
    private ImageButton mImgHeroList;

    private Fragment mTab01;
    private Fragment mTab02;
    private Fragment mTab03;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEvent();
        setSelect(0);
    }

    private void setSelect(int i) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);

        switch(i) {
            case 0:
                if (mTab01 == null) {
                    mTab01 = new HitHeroFragment();
                    transaction.add(R.id.id_content,mTab01);
                } else {
                    transaction.show(mTab01);
                }
                break;
            case 1:
                if (mTab02 == null) {
                    mTab02 = new HerosPKFragment();
                   transaction.add(R.id.id_content, mTab02);
                } else {
                    //transaction.add(R.id.id_content, mTab02);
                    transaction.show(mTab02);
                }
                break;
            case 2:
                if (mTab03 == null) {
                    mTab03 = new HerosListFragment();
                    transaction.add(R.id.id_content, mTab03);
                } else {
                    //transaction.add(R.id.id_content, mTab03);
                    transaction.show(mTab03);
                }
                break;
            default:
                break;
        }
        transaction.commit();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.settings:
                Toast.makeText(this, "you click settings", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
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
            default:
                break;
        }

    }
}

package com.example.jiamoufang.threekingdoms.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.jiamoufang.threekingdoms.R;
import com.example.jiamoufang.threekingdoms.adapter.HeroAdapter;
import com.example.jiamoufang.threekingdoms.heros.Hero;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiamoufang on 2017/11/5.
 */

public class HerosListFragment extends Fragment {

    private Hero[] heros = {
            new Hero("关羽",R.drawable.guanyu), new Hero("姜维", R.drawable.jiangwei),
            new Hero("刘备", R.drawable.liubei), new Hero("吕布", R.drawable.lvbu), new Hero("许褚", R.drawable.xuchu),
            new Hero("颜良", R.drawable.yanliang), new Hero("张飞", R.drawable.zhangfei), new Hero("周瑜", R.drawable.zhouyu),
            new Hero("诸葛亮", R.drawable.zhugeliang)
    };
    private List<Hero>  mHerosList = new ArrayList<>();
    private HeroAdapter heroAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.tab03, container, false);
        initHeros();
        RecyclerView recyclerview = (RecyclerView)view.findViewById(R.id.tab03_recyclerview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 2);
        recyclerview.setLayoutManager(gridLayoutManager);
        heroAdapter = new HeroAdapter(mHerosList);
        recyclerview.setAdapter(heroAdapter);

        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    private void initHeros() {
        mHerosList.clear();
        for (int i = 0; i < heros.length; i++) {
            mHerosList.add(heros[i]);
        }
    }
}

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
            new Hero("关羽",R.drawable.guanyu,"关羽 历史简介：\n 前将军。本字长生，亡命奔涿郡。与张飞追随刘备征战，当刘备为平原相时，他们俩为别部司马。二人与刘备寝则同床，恩若兄弟。稠人广坐则侍立终日。随同曹操和刘备讨吕布于下邳，事后为朝廷封为中郎将。当刘备袭杀曹操的徐州刺史车冑后，以关羽镇下邳太守。曹操东征破刘备，关羽被俘，被拜为偏将军，对他礼遇很优厚。白马之战时关羽万军中刺敌主帅颜良，被封为汉寿亭侯，报了曹操之恩后便告辞，寻找刘备。长阪之战刘备败北，抄近路赴汉津与关羽的数百只船汇合至江夏。赤壁之战孙刘联军胜利后，关羽在其后的江陵之战中绝北道，阻隔曹操的援军，为周瑜能攻下江陵创造有利的条件。事后遙领襄阳太守、拜为荡寇将军。诸葛亮等人入蜀增援刘备，关羽便镇荊州。刘备自立为汉中王，被拜为前将军，假节钺。关羽乘汉水暴涨之机出兵襄樊，更在于禁七军为水所淹乘船进攻，梁、郏、陆浑各县的盗贼有些远远地领受了关羽的官印封号，威震华夏；曹操一度想迁都避其锋芒。后因孙权背盟投曹，后方为吕蒙所破，关羽便退兵，但终为孙权军所擒杀。"),
            new Hero("姜维", R.drawable.jiangwei,"姜维 历史简介:\n" +
                    "幼年丧父和母亲生活，喜爱郑玄的经学，曾为魏中郎将，参天水郡军事。诸葛亮第一次北伐后投蜀汉，因他忠勤时事、思虑精密、敏于军事、即有胆义，又兼心存汉室，故深得诸葛亮的器重。诸葛亮于五丈原病逝后，姜维令杨仪反旗鸣鼓，导致尾追的司马懿退兵。姜维继诸葛亮之略，伺图中原，恢复汉室。又因其熟悉西方风俗，欲以羌胡为羽翼断陇西为蜀汉所有。蒋琬和费祎在位时实行保境安民，姜维每次出兵不过万人，但费祎被刺杀后能实行自己的志向，于是伺机累次兵伐中原，降李简、斩徐质、大破王经，一时挫魏国之威。但也有麹城被夺、段谷及侯和之败。后请刘禅杀专权的宦人黄皓不果，以屯田之名避禍沓中。司马昭大举伐蜀姜维上表请朝廷增援，但黄皓并不理会。姜维为邓艾军所缠，后用计令诸葛绪误以他将袭雍州而得脫，于剑阁拒守钟会十余万大军。奈先有江由守将马邈投降，再有诸葛瞻不听黄崇抢占涪，更战死于绵竹，蜀汉震恐后从投降派谯周之议，后主投降，并敕令姜维也降，将士得知后奋怒斩石。姜维乃佯降于钟会，看出他阴有异志策应他造反，图谋杀会后重扶汉室，乃事败，姜维及妻子皆伏诛。\n"),
            new Hero("刘备", R.drawable.liubei,"刘备 历史简介 \n 蜀汉的开国皇帝，相传是汉景帝之子中山靖王刘胜的后代。刘备少年丧父，与母亲贩鞋织草席为生。黄巾起义时，刘备组织义兵，随政府军剿除黄巾，有功，任安喜县尉，不久因鞭打督邮弃官。后诸侯割据，刘备势力弱小，经常寄人篱下，先后投靠过公孙瓒、曹操、袁绍、刘表等人，几经波折，却仍无自己的地盘。赤壁之战之际，刘备联吴抗曹，取得胜利，从东吴处“借”到荆州，迅速发展起来，吞并益州，占领汉中，建立蜀汉政权。后关羽战死，荆州被孙权夺取，刘备于称帝后伐吴，在夷陵之战中被陆逊击败，病逝于白帝城，临终托孤于诸葛亮。"),
            new Hero("吕布", R.drawable.lvbu,"吕布"),
            new Hero("许褚", R.drawable.xuchu,"许褚"),
            new Hero("颜良", R.drawable.yanliang,"颜良"),
            new Hero("张飞", R.drawable.zhangfei,"张飞"),
            new Hero("周瑜", R.drawable.zhouyu,"周瑜"),
            new Hero("诸葛亮", R.drawable.zhugeliang,"诸葛亮")
    };
    private List<Hero>  mHerosList = new ArrayList<>();
    private HeroAdapter heroAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.tab03, container, false);
        initHeros();
        //这里有点坑,注意是用 view 来拿到 recyclerView，否则拿不到就成了NullPointer，后面就崩了
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

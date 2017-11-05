package com.example.jiamoufang.threekingdoms.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.jiamoufang.threekingdoms.R;
import com.oragee.banners.BannerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiamoufang on 2017/11/5.
 */

public class HitHeroFragment extends Fragment {

    private BannerView bannerView;
    private int[] imgIds = {
            R.drawable.guanyu,
            R.drawable.zhangfei,
            R.drawable.liubei
    };
    private List<View> viewsList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.tab01, container, false);

        bannerView = view.findViewById(R.id.banner_tab01);
        viewsList = new ArrayList<>();
        for (int i = 0; i < imgIds.length; i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(imgIds[i]);
            viewsList.add(imageView);
        }
        bannerView.startLoop(true);

        bannerView.setViewList(viewsList);
        return view;
    }
}

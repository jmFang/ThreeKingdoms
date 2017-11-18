package com.example.jiamoufang.threekingdoms.entities;

import org.litepal.crud.DataSupport;

/**
 * Created by jiamoufang on 2017/11/18.
 */

public class MyLovedHero extends DataSupport {
    private int ImageId;
    private String heroName;

    public void setImageId(int imageId) {
        ImageId = imageId;
    }

    public void setHeroName(String heroName) {
        this.heroName = heroName;
    }

    public String getHeroName() {
        return heroName;
    }

    public int getImageId() {
        return ImageId;
    }
}

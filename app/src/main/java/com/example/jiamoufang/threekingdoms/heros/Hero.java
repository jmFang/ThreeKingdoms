package com.example.jiamoufang.threekingdoms.heros;

/**
 * Created by jiamoufang on 2017/11/5.
 */

public class Hero {
    private String name;
    private int imageId;

    public Hero(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public int getImageId() {
        return imageId;
    }

    public String getName() {
        return name;
    }
}

package com.example.jiamoufang.threekingdoms.heros;

/**
 * Created by jiamoufang on 2017/11/5.
 */

public class Hero {
    private String name;
    private int imageId;
    private String introduction;

    public Hero(String name, int imageId,String introduction) {
        this.name = name;
        this.imageId = imageId;
        this.introduction = introduction;
    }

    public int getImageId() {
        return imageId;
    }

    public String getName() {
        return name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void setName(String name) {
        this.name = name;
    }
}

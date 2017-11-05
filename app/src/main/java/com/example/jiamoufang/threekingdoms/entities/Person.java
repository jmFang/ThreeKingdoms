package com.example.jiamoufang.threekingdoms.entities;

import cn.bmob.v3.BmobObject;

/**
 * Created by jiamoufang on 2017/11/6.
 */

public class Person extends BmobObject {
    private String name;
    private String address;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}
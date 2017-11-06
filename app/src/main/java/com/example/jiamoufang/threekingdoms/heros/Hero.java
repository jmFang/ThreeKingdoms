
package com.example.jiamoufang.threekingdoms.heros;

/**
 * Created by jiamoufang on 2017/11/5.
 */

public class Hero {
    private String name;   //姓名
    private int imageId;   //头像
    private String sex;     //性别
    private String date;    //出生日期
    private String place;   //籍贯
    private String state;   //主校势力
    private String introduction;    //简介

    private Integer force;  //武力值(0~100)
    private Integer inteligence;  //智力值(0~100)
    private Integer leadship;   //统率值(0~100)
    private Integer army;   //军队数量:有统率值决定
    private Integer forage; // 粮草数量(0~1000)


    public Hero(String name, int imageId, String sex, String date, String place, String state, String introduction,
                Integer force, Integer inteligence, Integer leadship, Integer forage) {
        this.name = name;
        this.imageId = imageId;
        this.sex = sex;
        this.date = date;
        this.place = place;
        this.state = state;
        this.introduction = introduction;
        this.force = force;
        this.inteligence = inteligence;
        this.leadship = leadship;
        this.army = leadship * 1000;
        this.forage = forage;
    }

    public int getImageId() {
        return imageId;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public String getDate() {
        return date;
    }

    public String getPlace() {
        return place;
    }

    public String getState() {
        return state;
    }

    public String getIntroduction() {
        return introduction;
    }

    public Integer getForce() {
        return force;
    }

    public Integer getInteligence() {
        return inteligence;
    }

    public Integer getLeadship() {
        return leadship;
    }

    public Integer getArmy() {
        return army;
    }

    public Integer getForage() {
        return forage;
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

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPlace(String place) {
        this.place  = place;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setForce(Integer force) {
        this.force = force;
    }

    public void setInteligence(Integer intel) {
        this.inteligence = intel;
    }

    public void setLeadship(Integer leadship) {
        this.leadship = leadship;
        this.army = leadship * 1000;
    }

    public void setArmy(Integer army) {
        this.army = army;
    }

    public void setForage() {
        this.forage = forage;
    }

    //计算绝对战斗力，比较大小
    public Integer getEffectiveness() {
        Integer attr = (int)(force * 0.3 + inteligence * 0.3 + leadship * 0.4);
        int tarmy = army;
        if(tarmy > forage * 100) {
            double ratio = ((double) forage * 100) / army;
            tarmy = (int) (tarmy * ratio);
        }
        return attr * 1000 + tarmy;
    }
}

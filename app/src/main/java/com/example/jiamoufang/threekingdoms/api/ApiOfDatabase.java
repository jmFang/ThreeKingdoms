package com.example.jiamoufang.threekingdoms.api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ArrayAdapter;

import com.example.jiamoufang.threekingdoms.entities.NonEditedHero;
import com.example.jiamoufang.threekingdoms.entities.PkRecords;
import com.example.jiamoufang.threekingdoms.heros.LocalHero;
import com.example.jiamoufang.threekingdoms.MainActivity.*;

import org.litepal.crud.DataSupport;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static com.example.jiamoufang.threekingdoms.MainActivity.Herolist;

/**
 * Created by jiamoufang on 2017/11/17.
 */

public class ApiOfDatabase {

    /*
     * 将所有的PKRecords写回数据库
     * */
    public boolean WritoRecordsToDatabase(List<PkRecords> PKList) {
        for (int i = 0; i < PKList.size(); i++) {
            PKList.get(i).save();
        }
        return true;
    }
    /*
     * 将所有的LocalHero写回数据库
     * */
    public boolean WriteLocalHeroToDatabase(List<LocalHero> localHerosLists) {
        for (int i = 0; i < Herolist.size(); i++) {
            /*检查是否重复写入*/
            List<LocalHero> tmp = DataSupport.where("name = ?",Herolist.get(i).getName()).find(LocalHero.class);
            if (tmp.size() == 0) {
                Herolist.get(i).save();
            }
        }
        return true;
    }
    /*
    * 将所有的NonEditedHero写回数据库
    * */
    public boolean WriteNonEditedHeroToDatabase(List<NonEditedHero> nonEditedHerosList) {
        for (int i = 0; i < nonEditedHerosList.size(); i++) {

            nonEditedHerosList.get(i).save();
        }
        return true;
    }
    /*
    * 将所有的MyLovedHero写回数据库
    * */
    public boolean WriteMyLovedHerosToDatabase(List<LocalHero> myLovedHerosList) {
        for (int i = 0; i < myLovedHerosList.size(); i++) {
            myLovedHerosList.get(i).save();
        }
        return true;
    }
    /*
    * 向LocalHeros表添加一条数据
    * */
    public boolean addToLocalHeros(LocalHero hero) {
        List<LocalHero> tmp = DataSupport.where("name = ?", hero.getName()).find(LocalHero.class);
        if (tmp.size() == 0) {   //检查重复
            return hero.save();
        }
       return false;
    }
    /*
    * 按英雄名字删除一条本地英雄记录
    * false : 没有这个英雄
    * true : 删除成功
    * */
    public boolean deleteFromLocalHeros(String heroName) {
        return DataSupport.deleteAll(LocalHero.class,"name = ?", heroName) > 0;
    }
    /*
    * 向PKRecords表添加一条数据
    * */
    public boolean addToPKRecords(PkRecords pkRecords) {
        return pkRecords.save();
    }
    /*
    * 当未编辑的英雄被添加后，从数据库里删除
    * 按英雄名字删除
    * false : 没有这个英雄
    * true : 删除成功
    * */
    public boolean deleteFromNonEditedHeros(String heroName) {
       return DataSupport.deleteAll(NonEditedHero.class,"heroName = ?",heroName) > 0;
    }
}
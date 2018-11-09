package com.fangzuo.assist.Beans;

/**
 * Created by NB on 2017/7/28.
 */

public class SettingList {
    public int ImageResourse;
    public String tv;
    public String tag;
    public SettingList(){}
    public SettingList(String tv,int Image){
        this.ImageResourse = Image;
        this.tv=tv;
    }
    public SettingList(String tv,String tag,int Image){
        this.ImageResourse = Image;
        this.tv=tv;
        this.tag = tag;
    }
}

package com.fangzuo.assist.Dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by NB on 2017/7/26.
 */
@Entity
public class YuandanType {
    public String FID;
    public String FName_CHS;
    public String getFName_CHS() {
        return this.FName_CHS;
    }
    public void setFName_CHS(String FName_CHS) {
        this.FName_CHS = FName_CHS;
    }
    public String getFID() {
        return this.FID;
    }
    public void setFID(String FID) {
        this.FID = FID;
    }
    @Generated(hash = 1445046635)
    public YuandanType(String FID, String FName_CHS) {
        this.FID = FID;
        this.FName_CHS = FName_CHS;
    }
    @Generated(hash = 1253390434)
    public YuandanType() {
    }
}

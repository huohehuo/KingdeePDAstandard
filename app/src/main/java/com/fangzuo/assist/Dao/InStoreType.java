package com.fangzuo.assist.Dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by NB on 2017/8/15.
 */
@Entity
public class InStoreType {
    public String FID;
    public String FName;
    public String getFName() {
        return this.FName;
    }
    public void setFName(String FName) {
        this.FName = FName;
    }
    public String getFID() {
        return this.FID;
    }
    public void setFID(String FID) {
        this.FID = FID;
    }
    @Generated(hash = 2040180131)
    public InStoreType(String FID, String FName) {
        this.FID = FID;
        this.FName = FName;
    }
    @Generated(hash = 181106428)
    public InStoreType() {
    }
}

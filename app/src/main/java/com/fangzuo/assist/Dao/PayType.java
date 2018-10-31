package com.fangzuo.assist.Dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by NB on 2017/7/26.
 */
@Entity
public class PayType {
    public String FItemID;
    public String FName;
    public String FNumber;
    public String getFNumber() {
        return this.FNumber;
    }
    public void setFNumber(String FNumber) {
        this.FNumber = FNumber;
    }
    public String getFName() {
        return this.FName;
    }
    public void setFName(String FName) {
        this.FName = FName;
    }
    public String getFItemID() {
        return this.FItemID;
    }
    public void setFItemID(String FItemID) {
        this.FItemID = FItemID;
    }
    @Generated(hash = 479679086)
    public PayType(String FItemID, String FName, String FNumber) {
        this.FItemID = FItemID;
        this.FName = FName;
        this.FNumber = FNumber;
    }
    @Generated(hash = 822612439)
    public PayType() {
    }

    @Override
    public String toString() {
        return "PayType{" +
                "FItemID='" + FItemID + '\'' +
                ", FName='" + FName + '\'' +
                ", FNumber='" + FNumber + '\'' +
                '}';
    }
}

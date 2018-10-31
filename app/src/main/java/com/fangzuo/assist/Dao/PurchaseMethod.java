package com.fangzuo.assist.Dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by NB on 2017/7/26.
 */
@Entity
public class PurchaseMethod {
    public String FTypeID;
    public String FItemID;
    public String FNumber;
    public String FName;
    public String getFName() {
        return this.FName;
    }
    public void setFName(String FName) {
        this.FName = FName;
    }
    public String getFNumber() {
        return this.FNumber;
    }
    public void setFNumber(String FNumber) {
        this.FNumber = FNumber;
    }
    public String getFItemID() {
        return this.FItemID;
    }
    public void setFItemID(String FItemID) {
        this.FItemID = FItemID;
    }
    public String getFTypeID() {
        return this.FTypeID;
    }
    public void setFTypeID(String FTypeID) {
        this.FTypeID = FTypeID;
    }
    @Generated(hash = 220182701)
    public PurchaseMethod(String FTypeID, String FItemID, String FNumber,
            String FName) {
        this.FTypeID = FTypeID;
        this.FItemID = FItemID;
        this.FNumber = FNumber;
        this.FName = FName;
    }
    @Generated(hash = 535577305)
    public PurchaseMethod() {
    }

    @Override
    public String toString() {
        return "PurchaseMethod{" +
                "FTypeID='" + FTypeID + '\'' +
                ", FItemID='" + FItemID + '\'' +
                ", FNumber='" + FNumber + '\'' +
                ", FName='" + FName + '\'' +
                '}';
    }
}

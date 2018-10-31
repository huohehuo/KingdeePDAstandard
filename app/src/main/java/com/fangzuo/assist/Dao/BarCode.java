package com.fangzuo.assist.Dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by NB on 2017/7/26.
 */
@Entity
public class BarCode {
    public String FName;
    public String FTypeID;
    public String FItemID;
    public String FBarCode;
    public String FNumber;
    public String FUnitID;
    public String getFUnitID() {
        return this.FUnitID;
    }
    public void setFUnitID(String FUnitID) {
        this.FUnitID = FUnitID;
    }
    public String getFNumber() {
        return this.FNumber;
    }
    public void setFNumber(String FNumber) {
        this.FNumber = FNumber;
    }
    public String getFBarCode() {
        return this.FBarCode;
    }
    public void setFBarCode(String FBarCode) {
        this.FBarCode = FBarCode;
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
    public String getFName() {
        return this.FName;
    }
    public void setFName(String FName) {
        this.FName = FName;
    }
    @Generated(hash = 1919893496)
    public BarCode(String FName, String FTypeID, String FItemID, String FBarCode,
            String FNumber, String FUnitID) {
        this.FName = FName;
        this.FTypeID = FTypeID;
        this.FItemID = FItemID;
        this.FBarCode = FBarCode;
        this.FNumber = FNumber;
        this.FUnitID = FUnitID;
    }
    @Generated(hash = 303441476)
    public BarCode() {
    }
}

package com.fangzuo.assist.Dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by NB on 2017/7/26.
 */
@Entity
public class Client {
    public String FItemID ;
    public String FItemClassID;
    public String FNumber;
    public String FParentID;
    public String FLevel;
    public String FDetail;
    public String FName;
    public String FAddress;
    public String FPhone;
    public String FEmail;
    public String FTypeID;
    public String getFTypeID() {
        return this.FTypeID;
    }
    public void setFTypeID(String FTypeID) {
        this.FTypeID = FTypeID;
    }
    public String getFEmail() {
        return this.FEmail;
    }
    public void setFEmail(String FEmail) {
        this.FEmail = FEmail;
    }
    public String getFPhone() {
        return this.FPhone;
    }
    public void setFPhone(String FPhone) {
        this.FPhone = FPhone;
    }
    public String getFAddress() {
        return this.FAddress;
    }
    public void setFAddress(String FAddress) {
        this.FAddress = FAddress;
    }
    public String getFName() {
        return this.FName;
    }
    public void setFName(String FName) {
        this.FName = FName;
    }
    public String getFDetail() {
        return this.FDetail;
    }
    public void setFDetail(String FDetail) {
        this.FDetail = FDetail;
    }
    public String getFLevel() {
        return this.FLevel;
    }
    public void setFLevel(String FLevel) {
        this.FLevel = FLevel;
    }
    public String getFParentID() {
        return this.FParentID;
    }
    public void setFParentID(String FParentID) {
        this.FParentID = FParentID;
    }
    public String getFNumber() {
        return this.FNumber;
    }
    public void setFNumber(String FNumber) {
        this.FNumber = FNumber;
    }
    public String getFItemClassID() {
        return this.FItemClassID;
    }
    public void setFItemClassID(String FItemClassID) {
        this.FItemClassID = FItemClassID;
    }
    public String getFItemID() {
        return this.FItemID;
    }
    public void setFItemID(String FItemID) {
        this.FItemID = FItemID;
    }
    @Generated(hash = 1528206654)
    public Client(String FItemID, String FItemClassID, String FNumber,
            String FParentID, String FLevel, String FDetail, String FName,
            String FAddress, String FPhone, String FEmail, String FTypeID) {
        this.FItemID = FItemID;
        this.FItemClassID = FItemClassID;
        this.FNumber = FNumber;
        this.FParentID = FParentID;
        this.FLevel = FLevel;
        this.FDetail = FDetail;
        this.FName = FName;
        this.FAddress = FAddress;
        this.FPhone = FPhone;
        this.FEmail = FEmail;
        this.FTypeID = FTypeID;
    }
    @Generated(hash = 1485887936)
    public Client() {
    }
}

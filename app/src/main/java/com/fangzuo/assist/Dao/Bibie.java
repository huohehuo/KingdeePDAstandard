package com.fangzuo.assist.Dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by NB on 2017/7/26.
 */
@Entity
public class Bibie {
    private String FCurrencyID;
    public String FNumber;
    public String FName;
    private String FExChangeRate;
    private String fClassTypeId;
    public String getFClassTypeId() {
        return this.fClassTypeId;
    }
    public void setFClassTypeId(String fClassTypeId) {
        this.fClassTypeId = fClassTypeId;
    }
    public String getFExChangeRate() {
        return this.FExChangeRate;
    }
    public void setFExChangeRate(String FExChangeRate) {
        this.FExChangeRate = FExChangeRate;
    }
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
    public String getFCurrencyID() {
        return this.FCurrencyID;
    }
    public void setFCurrencyID(String FCurrencyID) {
        this.FCurrencyID = FCurrencyID;
    }
    @Generated(hash = 886038905)
    public Bibie(String FCurrencyID, String FNumber, String FName,
            String FExChangeRate, String fClassTypeId) {
        this.FCurrencyID = FCurrencyID;
        this.FNumber = FNumber;
        this.FName = FName;
        this.FExChangeRate = FExChangeRate;
        this.fClassTypeId = fClassTypeId;
    }
    @Generated(hash = 1936228239)
    public Bibie() {
    }
}

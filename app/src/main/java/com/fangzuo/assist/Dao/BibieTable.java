package com.fangzuo.assist.Dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by NB on 2017/7/26.
 */
@Entity
public class BibieTable {
    @Id
    private long id;
    private String FCurrencyID;
    private String FNumber;
    private String FName;
    private String FExChangeRate;
    private String fClassTypeId;
    @Transient
    private int temp;
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
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    @Generated(hash = 478267468)
    public BibieTable(long id, String FCurrencyID, String FNumber, String FName,
            String FExChangeRate, String fClassTypeId) {
        this.id = id;
        this.FCurrencyID = FCurrencyID;
        this.FNumber = FNumber;
        this.FName = FName;
        this.FExChangeRate = FExChangeRate;
        this.fClassTypeId = fClassTypeId;
    }
    @Generated(hash = 1702430705)
    public BibieTable() {
    }
}

package com.fangzuo.assist.Dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by NB on 2017/7/26.
 */
@Entity
public class GetGoodsDepartment {
    public String FItemID ;
    public String FDeleted ;
    public String FNumber;
    public String FName;
    public String FDetail;
    public String getFDetail() {
        return this.FDetail;
    }
    public void setFDetail(String FDetail) {
        this.FDetail = FDetail;
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
    public String getFDeleted() {
        return this.FDeleted;
    }
    public void setFDeleted(String FDeleted) {
        this.FDeleted = FDeleted;
    }
    public String getFItemID() {
        return this.FItemID;
    }
    public void setFItemID(String FItemID) {
        this.FItemID = FItemID;
    }
    @Generated(hash = 1560024641)
    public GetGoodsDepartment(String FItemID, String FDeleted, String FNumber,
            String FName, String FDetail) {
        this.FItemID = FItemID;
        this.FDeleted = FDeleted;
        this.FNumber = FNumber;
        this.FName = FName;
        this.FDetail = FDetail;
    }
    @Generated(hash = 1493624061)
    public GetGoodsDepartment() {
    }
}

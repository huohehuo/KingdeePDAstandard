package com.fangzuo.assist.Dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by NB on 2017/7/26.
 */
@Entity
public class NoticBean {
    @Id(autoincrement = true)
    public Long id;
    public String FNoticeId;
    public String FBillNo;
    public String FNumAll;
    public String FType;
    public String FActivityType;
    public String FTime;
    public String FRemark;
    @Generated(hash = 433136535)
    public NoticBean(Long id, String FNoticeId, String FBillNo, String FNumAll,
            String FType, String FActivityType, String FTime, String FRemark) {
        this.id = id;
        this.FNoticeId = FNoticeId;
        this.FBillNo = FBillNo;
        this.FNumAll = FNumAll;
        this.FType = FType;
        this.FActivityType = FActivityType;
        this.FTime = FTime;
        this.FRemark = FRemark;
    }
    @Generated(hash = 1800097068)
    public NoticBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getFBillNo() {
        return this.FBillNo;
    }
    public void setFBillNo(String FBillNo) {
        this.FBillNo = FBillNo;
    }
    public String getFNumAll() {
        return this.FNumAll;
    }
    public void setFNumAll(String FNumAll) {
        this.FNumAll = FNumAll;
    }
    public String getFType() {
        return this.FType;
    }
    public void setFType(String FType) {
        this.FType = FType;
    }
    public String getFActivityType() {
        return this.FActivityType;
    }
    public void setFActivityType(String FActivityType) {
        this.FActivityType = FActivityType;
    }
    public String getFTime() {
        return this.FTime;
    }
    public void setFTime(String FTime) {
        this.FTime = FTime;
    }
    public String getFRemark() {
        return this.FRemark;
    }
    public void setFRemark(String FRemark) {
        this.FRemark = FRemark;
    }
    public String getFNoticeId() {
        return this.FNoticeId;
    }
    public void setFNoticeId(String FNoticeId) {
        this.FNoticeId = FNoticeId;
    }

}

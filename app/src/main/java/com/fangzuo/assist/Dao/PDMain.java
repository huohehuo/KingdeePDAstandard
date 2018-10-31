package com.fangzuo.assist.Dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by NB on 2017/8/18.
 */
@Entity
public class PDMain {
    @Id(autoincrement = true)
    public Long id;
    public String FID;
    public String FProcessId;
    public String FRemark;
    public String FDate;
    public String FUserName;
    public String getFUserName() {
        return this.FUserName;
    }
    public void setFUserName(String FUserName) {
        this.FUserName = FUserName;
    }
    public String getFDate() {
        return this.FDate;
    }
    public void setFDate(String FDate) {
        this.FDate = FDate;
    }
    public String getFRemark() {
        return this.FRemark;
    }
    public void setFRemark(String FRemark) {
        this.FRemark = FRemark;
    }
    public String getFProcessId() {
        return this.FProcessId;
    }
    public void setFProcessId(String FProcessId) {
        this.FProcessId = FProcessId;
    }
    public String getFID() {
        return this.FID;
    }
    public void setFID(String FID) {
        this.FID = FID;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 563415013)
    public PDMain(Long id, String FID, String FProcessId, String FRemark,
            String FDate, String FUserName) {
        this.id = id;
        this.FID = FID;
        this.FProcessId = FProcessId;
        this.FRemark = FRemark;
        this.FDate = FDate;
        this.FUserName = FUserName;
    }
    @Generated(hash = 172423400)
    public PDMain() {
    }
}

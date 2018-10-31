package com.fangzuo.assist.Dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by NB on 2017/8/1.
 */
@Entity
public class WaveHouse {
    public String FSPID;
    public String FSPGroupID;
    public String FNumber;
    public String FName;
    public String FFullName;
    public String FLevel;
    public String FDetail;
    public String FParentID;
    public String FClassTypeID;
    public String FDefaultSPID;
    public String getFDefaultSPID() {
        return this.FDefaultSPID;
    }
    public void setFDefaultSPID(String FDefaultSPID) {
        this.FDefaultSPID = FDefaultSPID;
    }
    public String getFClassTypeID() {
        return this.FClassTypeID;
    }
    public void setFClassTypeID(String FClassTypeID) {
        this.FClassTypeID = FClassTypeID;
    }
    public String getFParentID() {
        return this.FParentID;
    }
    public void setFParentID(String FParentID) {
        this.FParentID = FParentID;
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
    public String getFFullName() {
        return this.FFullName;
    }
    public void setFFullName(String FFullName) {
        this.FFullName = FFullName;
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
    public String getFSPGroupID() {
        return this.FSPGroupID;
    }
    public void setFSPGroupID(String FSPGroupID) {
        this.FSPGroupID = FSPGroupID;
    }
    public String getFSPID() {
        return this.FSPID;
    }
    public void setFSPID(String FSPID) {
        this.FSPID = FSPID;
    }
    @Generated(hash = 1429297750)
    public WaveHouse(String FSPID, String FSPGroupID, String FNumber, String FName,
            String FFullName, String FLevel, String FDetail, String FParentID,
            String FClassTypeID, String FDefaultSPID) {
        this.FSPID = FSPID;
        this.FSPGroupID = FSPGroupID;
        this.FNumber = FNumber;
        this.FName = FName;
        this.FFullName = FFullName;
        this.FLevel = FLevel;
        this.FDetail = FDetail;
        this.FParentID = FParentID;
        this.FClassTypeID = FClassTypeID;
        this.FDefaultSPID = FDefaultSPID;
    }
    @Generated(hash = 365743171)
    public WaveHouse() {
    }

    @Override
    public String toString() {
        return "WaveHouse{" +
                "FSPID='" + FSPID + '\'' +
                ", FSPGroupID='" + FSPGroupID + '\'' +
                ", FNumber='" + FNumber + '\'' +
                ", FName='" + FName + '\'' +
                ", FFullName='" + FFullName + '\'' +
                ", FLevel='" + FLevel + '\'' +
                ", FDetail='" + FDetail + '\'' +
                ", FParentID='" + FParentID + '\'' +
                ", FClassTypeID='" + FClassTypeID + '\'' +
                ", FDefaultSPID='" + FDefaultSPID + '\'' +
                '}';
    }
}

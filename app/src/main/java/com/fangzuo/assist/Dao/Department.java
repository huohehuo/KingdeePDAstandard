package com.fangzuo.assist.Dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by NB on 2017/7/26.
 */
@Entity
public class Department {
    public String  FItemID ;
    public String FNumber ;
    public String FName ;
    public String FparentID ;
    public String getFparentID() {
        return this.FparentID;
    }
    public void setFparentID(String FparentID) {
        this.FparentID = FparentID;
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
    public String getFItemID() {
        return this.FItemID;
    }
    public void setFItemID(String FItemID) {
        this.FItemID = FItemID;
    }
    @Generated(hash = 1464744388)
    public Department(String FItemID, String FNumber, String FName, String FparentID) {
        this.FItemID = FItemID;
        this.FNumber = FNumber;
        this.FName = FName;
        this.FparentID = FparentID;
    }
    @Generated(hash = 355406289)
    public Department() {
    }

    @Override
    public String toString() {
        return "Department{" +
                "FItemID='" + FItemID + '\'' +
                ", FNumber='" + FNumber + '\'' +
                ", FName='" + FName + '\'' +
                ", FparentID='" + FparentID + '\'' +
                '}';
    }
}

package com.fangzuo.assist.Dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by NB on 2017/7/26.
 */
@Entity
public class Unit {
    public String FMeasureUnitID;
    public String FUnitGroupID;
    public String FNumber;
    public String FName;
    public String FCoefficient;
    public String getFCoefficient() {
        return this.FCoefficient;
    }
    public void setFCoefficient(String FCoefficient) {
        this.FCoefficient = FCoefficient;
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
    public String getFUnitGroupID() {
        return this.FUnitGroupID;
    }
    public void setFUnitGroupID(String FUnitGroupID) {
        this.FUnitGroupID = FUnitGroupID;
    }
    public String getFMeasureUnitID() {
        return this.FMeasureUnitID;
    }
    public void setFMeasureUnitID(String FMeasureUnitID) {
        this.FMeasureUnitID = FMeasureUnitID;
    }
    @Generated(hash = 1654359466)
    public Unit(String FMeasureUnitID, String FUnitGroupID, String FNumber,
            String FName, String FCoefficient) {
        this.FMeasureUnitID = FMeasureUnitID;
        this.FUnitGroupID = FUnitGroupID;
        this.FNumber = FNumber;
        this.FName = FName;
        this.FCoefficient = FCoefficient;
    }
    @Generated(hash = 1236212320)
    public Unit() {
    }

    @Override
    public String toString() {
        return "Unit{" +
                "FMeasureUnitID='" + FMeasureUnitID + '\'' +
                ", FUnitGroupID='" + FUnitGroupID + '\'' +
                ", FNumber='" + FNumber + '\'' +
                ", FName='" + FName + '\'' +
                ", FCoefficient='" + FCoefficient + '\'' +
                '}';
    }
}

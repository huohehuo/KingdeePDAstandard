package com.fangzuo.assist.Dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by NB on 2017/7/26.
 */
@Entity
public class Storage {
    public String FItemID;
    public String FEmpID;
    public String FName;
    public String FNumber;
    public String FTypeID;
    public String FSPGroupID;
    public String FGroupID;
    public String FStockGroupID;
    public String FIsStockMgr;
    public String FUnderStock;
    public String getFUnderStock() {
        return this.FUnderStock;
    }
    public void setFUnderStock(String FUnderStock) {
        this.FUnderStock = FUnderStock;
    }
    public String getFIsStockMgr() {
        return this.FIsStockMgr;
    }
    public void setFIsStockMgr(String FIsStockMgr) {
        this.FIsStockMgr = FIsStockMgr;
    }
    public String getFStockGroupID() {
        return this.FStockGroupID;
    }
    public void setFStockGroupID(String FStockGroupID) {
        this.FStockGroupID = FStockGroupID;
    }
    public String getFGroupID() {
        return this.FGroupID;
    }
    public void setFGroupID(String FGroupID) {
        this.FGroupID = FGroupID;
    }
    public String getFSPGroupID() {
        return this.FSPGroupID;
    }
    public void setFSPGroupID(String FSPGroupID) {
        this.FSPGroupID = FSPGroupID;
    }
    public String getFTypeID() {
        return this.FTypeID;
    }
    public void setFTypeID(String FTypeID) {
        this.FTypeID = FTypeID;
    }
    public String getFNumber() {
        return this.FNumber;
    }
    public void setFNumber(String FNumber) {
        this.FNumber = FNumber;
    }
    public String getFName() {
        return this.FName;
    }
    public void setFName(String FName) {
        this.FName = FName;
    }
    public String getFEmpID() {
        return this.FEmpID;
    }
    public void setFEmpID(String FEmpID) {
        this.FEmpID = FEmpID;
    }
    public String getFItemID() {
        return this.FItemID;
    }
    public void setFItemID(String FItemID) {
        this.FItemID = FItemID;
    }
    @Generated(hash = 911618242)
    public Storage(String FItemID, String FEmpID, String FName, String FNumber,
            String FTypeID, String FSPGroupID, String FGroupID,
            String FStockGroupID, String FIsStockMgr, String FUnderStock) {
        this.FItemID = FItemID;
        this.FEmpID = FEmpID;
        this.FName = FName;
        this.FNumber = FNumber;
        this.FTypeID = FTypeID;
        this.FSPGroupID = FSPGroupID;
        this.FGroupID = FGroupID;
        this.FStockGroupID = FStockGroupID;
        this.FIsStockMgr = FIsStockMgr;
        this.FUnderStock = FUnderStock;
    }
    @Generated(hash = 2114225574)
    public Storage() {
    }

    @Override
    public String toString() {
        return "Storage{" +
                "FItemID='" + FItemID + '\'' +
                ", FEmpID='" + FEmpID + '\'' +
                ", FName='" + FName + '\'' +
                ", FNumber='" + FNumber + '\'' +
                ", FTypeID='" + FTypeID + '\'' +
                ", FSPGroupID='" + FSPGroupID + '\'' +
                ", FGroupID='" + FGroupID + '\'' +
                ", FStockGroupID='" + FStockGroupID + '\'' +
                ", FIsStockMgr='" + FIsStockMgr + '\'' +
                ", FUnderStock='" + FUnderStock + '\'' +
                '}';
    }
}

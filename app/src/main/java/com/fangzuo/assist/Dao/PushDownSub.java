package com.fangzuo.assist.Dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by NB on 2017/8/24.
 *      订单详情
 */
@Entity
public class PushDownSub {
    @Id(autoincrement = true)
    public Long id;
    public String FName;
    public String FNumber;          //物料名
    public String FModel;
    public String FBillNo;          //单据编号
    public String FInterID;
    public String FEntryID;
    public String FItemID;
    public String FUnitID;
    public String FAuxQty;          //订单数量
    public String FAuxPrice;
    public String FQtying;          //已验收数量
    public String FDCStockID;
    public String FDCSPID;
    public String FBatchNo;
    public String FDCSTOCK_ID;
    public String getFQtying() {
        return this.FQtying;
    }
    public void setFQtying(String FQtying) {
        this.FQtying = FQtying;
    }
    public String getFAuxPrice() {
        return this.FAuxPrice;
    }
    public void setFAuxPrice(String FAuxPrice) {
        this.FAuxPrice = FAuxPrice;
    }
    public String getFAuxQty() {
        return this.FAuxQty;
    }
    public void setFAuxQty(String FAuxQty) {
        this.FAuxQty = FAuxQty;
    }
    public String getFUnitID() {
        return this.FUnitID;
    }
    public void setFUnitID(String FUnitID) {
        this.FUnitID = FUnitID;
    }
    public String getFItemID() {
        return this.FItemID;
    }
    public void setFItemID(String FItemID) {
        this.FItemID = FItemID;
    }
    public String getFEntryID() {
        return this.FEntryID;
    }
    public void setFEntryID(String FEntryID) {
        this.FEntryID = FEntryID;
    }
    public String getFInterID() {
        return this.FInterID;
    }
    public void setFInterID(String FInterID) {
        this.FInterID = FInterID;
    }
    public String getFBillNo() {
        return this.FBillNo;
    }
    public void setFBillNo(String FBillNo) {
        this.FBillNo = FBillNo;
    }
    public String getFModel() {
        return this.FModel;
    }
    public void setFModel(String FModel) {
        this.FModel = FModel;
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
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getFDCStockID() {
        return this.FDCStockID;
    }
    public void setFDCStockID(String FDCStockID) {
        this.FDCStockID = FDCStockID;
    }
    public String getFDCSPID() {
        return this.FDCSPID;
    }
    public void setFDCSPID(String FDCSPID) {
        this.FDCSPID = FDCSPID;
    }
    public String getFBatchNo() {
        return this.FBatchNo;
    }
    public void setFBatchNo(String FBatchNo) {
        this.FBatchNo = FBatchNo;
    }
    public String getFDCSTOCK_ID() {
        return this.FDCSTOCK_ID;
    }
    public void setFDCSTOCK_ID(String FDCSTOCK_ID) {
        this.FDCSTOCK_ID = FDCSTOCK_ID;
    }
    @Generated(hash = 385272229)
    public PushDownSub(Long id, String FName, String FNumber, String FModel,
            String FBillNo, String FInterID, String FEntryID, String FItemID,
            String FUnitID, String FAuxQty, String FAuxPrice, String FQtying,
            String FDCStockID, String FDCSPID, String FBatchNo,
            String FDCSTOCK_ID) {
        this.id = id;
        this.FName = FName;
        this.FNumber = FNumber;
        this.FModel = FModel;
        this.FBillNo = FBillNo;
        this.FInterID = FInterID;
        this.FEntryID = FEntryID;
        this.FItemID = FItemID;
        this.FUnitID = FUnitID;
        this.FAuxQty = FAuxQty;
        this.FAuxPrice = FAuxPrice;
        this.FQtying = FQtying;
        this.FDCStockID = FDCStockID;
        this.FDCSPID = FDCSPID;
        this.FBatchNo = FBatchNo;
        this.FDCSTOCK_ID = FDCSTOCK_ID;
    }
    @Generated(hash = 2008125598)
    public PushDownSub() {
    }


    @Override
    public String toString() {
        return "PushDownSub{" +
                "id=" + id +
                ", FName='" + FName + '\'' +
                ", FNumber='" + FNumber + '\'' +
                ", FModel='" + FModel + '\'' +
                ", FBillNo='" + FBillNo + '\'' +
                ", FInterID='" + FInterID + '\'' +
                ", FEntryID='" + FEntryID + '\'' +
                ", FItemID='" + FItemID + '\'' +
                ", FUnitID='" + FUnitID + '\'' +
                ", FAuxQty='" + FAuxQty + '\'' +
                ", FAuxPrice='" + FAuxPrice + '\'' +
                ", FQtying='" + FQtying + '\'' +
                ", FDCStockID='" + FDCStockID + '\'' +
                ", FDCSPID='" + FDCSPID + '\'' +
                ", FBatchNo='" + FBatchNo + '\'' +
                ", FDCSTOCK_ID='" + FDCSTOCK_ID + '\'' +
                '}';
    }
}

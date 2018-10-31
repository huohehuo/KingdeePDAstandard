package com.fangzuo.assist.Dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by NB on 2017/8/18.
 */
@Entity
public class PDSub {
    @Id(autoincrement = true)
    public Long id;
    public String FID;
    public String FStockID;
    public String FItemID;
    public String FSPName;
    public String FNumber;
    public String FName;
    public String FModel;
    public String FStockPlaceID;
    public String FUnitName;
    public String FQty;//z账存数量
    public String FQtyAct1;
    public String FQtyAct;//实存数量
    public String FCheckQty1;//一盘数量
    public String FCheckQty;//本次盘点数量
    public String FAdjQty1;//本次调整数量
    public String FAdjQty;//已调整数量
    public String FRemark;
    public String FUnitID;
    public String FUnitGroupID;
    public String FBatchNo;
    public String getFBatchNo() {
        return this.FBatchNo;
    }
    public void setFBatchNo(String FBatchNo) {
        this.FBatchNo = FBatchNo;
    }
    public String getFUnitGroupID() {
        return this.FUnitGroupID;
    }
    public void setFUnitGroupID(String FUnitGroupID) {
        this.FUnitGroupID = FUnitGroupID;
    }
    public String getFUnitID() {
        return this.FUnitID;
    }
    public void setFUnitID(String FUnitID) {
        this.FUnitID = FUnitID;
    }
    public String getFRemark() {
        return this.FRemark;
    }
    public void setFRemark(String FRemark) {
        this.FRemark = FRemark;
    }
    public String getFAdjQty() {
        return this.FAdjQty;
    }
    public void setFAdjQty(String FAdjQty) {
        this.FAdjQty = FAdjQty;
    }
    public String getFAdjQty1() {
        return this.FAdjQty1;
    }
    public void setFAdjQty1(String FAdjQty1) {
        this.FAdjQty1 = FAdjQty1;
    }
    public String getFCheckQty() {
        return this.FCheckQty;
    }
    public void setFCheckQty(String FCheckQty) {
        this.FCheckQty = FCheckQty;
    }
    public String getFCheckQty1() {
        return this.FCheckQty1;
    }
    public void setFCheckQty1(String FCheckQty1) {
        this.FCheckQty1 = FCheckQty1;
    }
    public String getFQtyAct() {
        return this.FQtyAct;
    }
    public void setFQtyAct(String FQtyAct) {
        this.FQtyAct = FQtyAct;
    }
    public String getFQtyAct1() {
        return this.FQtyAct1;
    }
    public void setFQtyAct1(String FQtyAct1) {
        this.FQtyAct1 = FQtyAct1;
    }
    public String getFQty() {
        return this.FQty;
    }
    public void setFQty(String FQty) {
        this.FQty = FQty;
    }
    public String getFUnitName() {
        return this.FUnitName;
    }
    public void setFUnitName(String FUnitName) {
        this.FUnitName = FUnitName;
    }
    public String getFStockPlaceID() {
        return this.FStockPlaceID;
    }
    public void setFStockPlaceID(String FStockPlaceID) {
        this.FStockPlaceID = FStockPlaceID;
    }
    public String getFModel() {
        return this.FModel;
    }
    public void setFModel(String FModel) {
        this.FModel = FModel;
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
    public String getFSPName() {
        return this.FSPName;
    }
    public void setFSPName(String FSPName) {
        this.FSPName = FSPName;
    }
    public String getFItemID() {
        return this.FItemID;
    }
    public void setFItemID(String FItemID) {
        this.FItemID = FItemID;
    }
    public String getFStockID() {
        return this.FStockID;
    }
    public void setFStockID(String FStockID) {
        this.FStockID = FStockID;
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
    @Generated(hash = 1835714022)
    public PDSub(Long id, String FID, String FStockID, String FItemID,
            String FSPName, String FNumber, String FName, String FModel,
            String FStockPlaceID, String FUnitName, String FQty, String FQtyAct1,
            String FQtyAct, String FCheckQty1, String FCheckQty, String FAdjQty1,
            String FAdjQty, String FRemark, String FUnitID, String FUnitGroupID,
            String FBatchNo) {
        this.id = id;
        this.FID = FID;
        this.FStockID = FStockID;
        this.FItemID = FItemID;
        this.FSPName = FSPName;
        this.FNumber = FNumber;
        this.FName = FName;
        this.FModel = FModel;
        this.FStockPlaceID = FStockPlaceID;
        this.FUnitName = FUnitName;
        this.FQty = FQty;
        this.FQtyAct1 = FQtyAct1;
        this.FQtyAct = FQtyAct;
        this.FCheckQty1 = FCheckQty1;
        this.FCheckQty = FCheckQty;
        this.FAdjQty1 = FAdjQty1;
        this.FAdjQty = FAdjQty;
        this.FRemark = FRemark;
        this.FUnitID = FUnitID;
        this.FUnitGroupID = FUnitGroupID;
        this.FBatchNo = FBatchNo;
    }
    @Generated(hash = 974003275)
    public PDSub() {
    }

    @Override
    public String toString() {
        return "PDSub{" +
                "id=" + id +
                ", FID='" + FID + '\'' +
                ", FStockID='" + FStockID + '\'' +
                ", FItemID='" + FItemID + '\'' +
                ", FSPName='" + FSPName + '\'' +
                ", FNumber='" + FNumber + '\'' +
                ", FName='" + FName + '\'' +
                ", FModel='" + FModel + '\'' +
                ", FStockPlaceID='" + FStockPlaceID + '\'' +
                ", FUnitName='" + FUnitName + '\'' +
                ", FQty='" + FQty + '\'' +
                ", FQtyAct1='" + FQtyAct1 + '\'' +
                ", FQtyAct='" + FQtyAct + '\'' +
                ", FCheckQty1='" + FCheckQty1 + '\'' +
                ", FCheckQty='" + FCheckQty + '\'' +
                ", FAdjQty1='" + FAdjQty1 + '\'' +
                ", FAdjQty='" + FAdjQty + '\'' +
                ", FRemark='" + FRemark + '\'' +
                ", FUnitID='" + FUnitID + '\'' +
                ", FUnitGroupID='" + FUnitGroupID + '\'' +
                ", FBatchNo='" + FBatchNo + '\'' +
                '}';
    }
}

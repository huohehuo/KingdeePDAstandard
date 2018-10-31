package com.fangzuo.assist.Dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by NB on 2017/7/26.
 */
@Entity
public class InStorageNum {
    @Id(autoincrement = true)
    private Long _id;
    public String  FItemID ;
    public String  FStockID ;
    public String  FQty;
    public String  FBal ;
    public String  FStockPlaceID ;
    public String  FKFPeriod ;
    public String  FKFDate ;
    public String  FBatchNo ;
    public String getFBatchNo() {
        return this.FBatchNo;
    }
    public void setFBatchNo(String FBatchNo) {
        this.FBatchNo = FBatchNo;
    }
    public String getFKFDate() {
        return this.FKFDate;
    }
    public void setFKFDate(String FKFDate) {
        this.FKFDate = FKFDate;
    }
    public String getFKFPeriod() {
        return this.FKFPeriod;
    }
    public void setFKFPeriod(String FKFPeriod) {
        this.FKFPeriod = FKFPeriod;
    }
    public String getFStockPlaceID() {
        return this.FStockPlaceID;
    }
    public void setFStockPlaceID(String FStockPlaceID) {
        this.FStockPlaceID = FStockPlaceID;
    }
    public String getFBal() {
        return this.FBal;
    }
    public void setFBal(String FBal) {
        this.FBal = FBal;
    }
    public String getFQty() {
        return this.FQty;
    }
    public void setFQty(String FQty) {
        this.FQty = FQty;
    }
    public String getFStockID() {
        return this.FStockID;
    }
    public void setFStockID(String FStockID) {
        this.FStockID = FStockID;
    }
    public String getFItemID() {
        return this.FItemID;
    }
    public void setFItemID(String FItemID) {
        this.FItemID = FItemID;
    }
    public Long get_id() {
        return this._id;
    }
    public void set_id(Long _id) {
        this._id = _id;
    }
    @Generated(hash = 1027405859)
    public InStorageNum(Long _id, String FItemID, String FStockID, String FQty, String FBal,
            String FStockPlaceID, String FKFPeriod, String FKFDate, String FBatchNo) {
        this._id = _id;
        this.FItemID = FItemID;
        this.FStockID = FStockID;
        this.FQty = FQty;
        this.FBal = FBal;
        this.FStockPlaceID = FStockPlaceID;
        this.FKFPeriod = FKFPeriod;
        this.FKFDate = FKFDate;
        this.FBatchNo = FBatchNo;
    }
    @Generated(hash = 471196027)
    public InStorageNum() {
    }

    @Override
    public String toString() {
        return "InStorageNum{" +
                "_id=" + _id +
                ", FItemID='" + FItemID + '\'' +
                ", FStockID='" + FStockID + '\'' +
                ", FQty='" + FQty + '\'' +
                ", FBal='" + FBal + '\'' +
                ", FStockPlaceID='" + FStockPlaceID + '\'' +
                ", FKFPeriod='" + FKFPeriod + '\'' +
                ", FKFDate='" + FKFDate + '\'' +
                ", FBatchNo='" + FBatchNo + '\'' +
                '}';
    }
}

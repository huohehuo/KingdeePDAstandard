package com.fangzuo.assist.Dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by NB on 2017/8/24.
 *
 *              单据信息
 */
@Entity
public class PushDownMain {
    @Id(autoincrement = true)
    public Long id;
    public String FBillNo;
    public String FName;
    public String FDate;
    public String FSupplyID;
    public String FDeptID;
    public String FManagerID;
    public String FEmpID;
    public String FInterID;
    public int tag;
    public String getFInterID() {
        return this.FInterID;
    }
    public void setFInterID(String FInterID) {
        this.FInterID = FInterID;
    }
    public String getFEmpID() {
        return this.FEmpID;
    }
    public void setFEmpID(String FEmpID) {
        this.FEmpID = FEmpID;
    }
    public String getFManagerID() {
        return this.FManagerID;
    }
    public void setFManagerID(String FManagerID) {
        this.FManagerID = FManagerID;
    }
    public String getFDeptID() {
        return this.FDeptID;
    }
    public void setFDeptID(String FDeptID) {
        this.FDeptID = FDeptID;
    }
    public String getFSupplyID() {
        return this.FSupplyID;
    }
    public void setFSupplyID(String FSupplyID) {
        this.FSupplyID = FSupplyID;
    }
    public String getFDate() {
        return this.FDate;
    }
    public void setFDate(String FDate) {
        this.FDate = FDate;
    }
    public String getFName() {
        return this.FName;
    }
    public void setFName(String FName) {
        this.FName = FName;
    }
    public String getFBillNo() {
        return this.FBillNo;
    }
    public void setFBillNo(String FBillNo) {
        this.FBillNo = FBillNo;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getTag() {
        return this.tag;
    }
    public void setTag(int tag) {
        this.tag = tag;
    }
    @Generated(hash = 667568497)
    public PushDownMain(Long id, String FBillNo, String FName, String FDate,
            String FSupplyID, String FDeptID, String FManagerID, String FEmpID,
            String FInterID, int tag) {
        this.id = id;
        this.FBillNo = FBillNo;
        this.FName = FName;
        this.FDate = FDate;
        this.FSupplyID = FSupplyID;
        this.FDeptID = FDeptID;
        this.FManagerID = FManagerID;
        this.FEmpID = FEmpID;
        this.FInterID = FInterID;
        this.tag = tag;
    }
    @Generated(hash = 92092905)
    public PushDownMain() {
    }
}

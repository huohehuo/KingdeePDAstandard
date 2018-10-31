package com.fangzuo.assist.Dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by NB on 2017/7/26.
 */
@Entity
public class Employee {
    public String  FItemID ;
    public String FNumber ;
    public String FName;
    public String FDpartmentID ;
    public String FEmpGroup ;
    public String FEmpGroupID ;
    public String getFEmpGroupID() {
        return this.FEmpGroupID;
    }
    public void setFEmpGroupID(String FEmpGroupID) {
        this.FEmpGroupID = FEmpGroupID;
    }
    public String getFEmpGroup() {
        return this.FEmpGroup;
    }
    public void setFEmpGroup(String FEmpGroup) {
        this.FEmpGroup = FEmpGroup;
    }
    public String getFDpartmentID() {
        return this.FDpartmentID;
    }
    public void setFDpartmentID(String FDpartmentID) {
        this.FDpartmentID = FDpartmentID;
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
    @Generated(hash = 873873653)
    public Employee(String FItemID, String FNumber, String FName,
            String FDpartmentID, String FEmpGroup, String FEmpGroupID) {
        this.FItemID = FItemID;
        this.FNumber = FNumber;
        this.FName = FName;
        this.FDpartmentID = FDpartmentID;
        this.FEmpGroup = FEmpGroup;
        this.FEmpGroupID = FEmpGroupID;
    }
    @Generated(hash = 202356944)
    public Employee() {
    }

    @Override
    public String toString() {
        return "Employee{" +
                "FItemID='" + FItemID + '\'' +
                ", FNumber='" + FNumber + '\'' +
                ", FName='" + FName + '\'' +
                ", FDpartmentID='" + FDpartmentID + '\'' +
                ", FEmpGroup='" + FEmpGroup + '\'' +
                ", FEmpGroupID='" + FEmpGroupID + '\'' +
                '}';
    }
}

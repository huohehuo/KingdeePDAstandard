package com.fangzuo.assist.Dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by NB on 2017/7/26.
 */
@Entity
public class User {
    public String FUserID;
    public String FName;
    public String FPassWord;
    public String FEmpID;
    public String FGroupName;
    public String FPermit;
    public String getFPermit() {
        return this.FPermit;
    }
    public void setFPermit(String FPermit) {
        this.FPermit = FPermit;
    }
    public String getFGroupName() {
        return this.FGroupName;
    }
    public void setFGroupName(String FGroupName) {
        this.FGroupName = FGroupName;
    }
    public String getFEmpID() {
        return this.FEmpID;
    }
    public void setFEmpID(String FEmpID) {
        this.FEmpID = FEmpID;
    }
    public String getFPassWord() {
        return this.FPassWord;
    }
    public void setFPassWord(String FPassWord) {
        this.FPassWord = FPassWord;
    }
    public String getFName() {
        return this.FName;
    }
    public void setFName(String FName) {
        this.FName = FName;
    }
    public String getFUserID() {
        return this.FUserID;
    }
    public void setFUserID(String FUserID) {
        this.FUserID = FUserID;
    }
    @Generated(hash = 2357555)
    public User(String FUserID, String FName, String FPassWord, String FEmpID,
            String FGroupName, String FPermit) {
        this.FUserID = FUserID;
        this.FName = FName;
        this.FPassWord = FPassWord;
        this.FEmpID = FEmpID;
        this.FGroupName = FGroupName;
        this.FPermit = FPermit;
    }
    @Generated(hash = 586692638)
    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "FUserID='" + FUserID + '\'' +
                ", FName='" + FName + '\'' +
                ", FPassWord='" + FPassWord + '\'' +
                ", FEmpID='" + FEmpID + '\'' +
                ", FGroupName='" + FGroupName + '\'' +
                ", FPermit='" + FPermit + '\'' +
                '}';
    }
}

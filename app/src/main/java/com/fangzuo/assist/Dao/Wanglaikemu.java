package com.fangzuo.assist.Dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by NB on 2017/7/26.
 */
@Entity
public class Wanglaikemu {
    public String FAccountID;
    public String FNumber;
    public String FFullName;
    public String getFFullName() {
        return this.FFullName;
    }
    public void setFFullName(String FFullName) {
        this.FFullName = FFullName;
    }
    public String getFNumber() {
        return this.FNumber;
    }
    public void setFNumber(String FNumber) {
        this.FNumber = FNumber;
    }
    public String getFAccountID() {
        return this.FAccountID;
    }
    public void setFAccountID(String FAccountID) {
        this.FAccountID = FAccountID;
    }
    @Generated(hash = 1262802224)
    public Wanglaikemu(String FAccountID, String FNumber, String FFullName) {
        this.FAccountID = FAccountID;
        this.FNumber = FNumber;
        this.FFullName = FFullName;
    }
    @Generated(hash = 2085777475)
    public Wanglaikemu() {
    }
}

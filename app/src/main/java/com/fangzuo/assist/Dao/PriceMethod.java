package com.fangzuo.assist.Dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by NB on 2017/7/26.
 */
@Entity
public class PriceMethod {
    public String FInterID;
    public String FEntryID;
    public String FPri;
    public String FPrice;
    public String FName;
    public String FItemID;
    public String FUnitID;
    public String FRelatedID;
    public String FBegQty;
    public String FEndQty;
    public String FBegDate;
    public String FEndDate;
    public String getFEndDate() {
        return this.FEndDate;
    }
    public void setFEndDate(String FEndDate) {
        this.FEndDate = FEndDate;
    }
    public String getFBegDate() {
        return this.FBegDate;
    }
    public void setFBegDate(String FBegDate) {
        this.FBegDate = FBegDate;
    }
    public String getFEndQty() {
        return this.FEndQty;
    }
    public void setFEndQty(String FEndQty) {
        this.FEndQty = FEndQty;
    }
    public String getFBegQty() {
        return this.FBegQty;
    }
    public void setFBegQty(String FBegQty) {
        this.FBegQty = FBegQty;
    }
    public String getFRelatedID() {
        return this.FRelatedID;
    }
    public void setFRelatedID(String FRelatedID) {
        this.FRelatedID = FRelatedID;
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
    public String getFName() {
        return this.FName;
    }
    public void setFName(String FName) {
        this.FName = FName;
    }
    public String getFPrice() {
        return this.FPrice;
    }
    public void setFPrice(String FPrice) {
        this.FPrice = FPrice;
    }
    public String getFPri() {
        return this.FPri;
    }
    public void setFPri(String FPri) {
        this.FPri = FPri;
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
    @Generated(hash = 775725898)
    public PriceMethod(String FInterID, String FEntryID, String FPri,
            String FPrice, String FName, String FItemID, String FUnitID,
            String FRelatedID, String FBegQty, String FEndQty, String FBegDate,
            String FEndDate) {
        this.FInterID = FInterID;
        this.FEntryID = FEntryID;
        this.FPri = FPri;
        this.FPrice = FPrice;
        this.FName = FName;
        this.FItemID = FItemID;
        this.FUnitID = FUnitID;
        this.FRelatedID = FRelatedID;
        this.FBegQty = FBegQty;
        this.FEndQty = FEndQty;
        this.FBegDate = FBegDate;
        this.FEndDate = FEndDate;
    }
    @Generated(hash = 1181802731)
    public PriceMethod() {
    }
}

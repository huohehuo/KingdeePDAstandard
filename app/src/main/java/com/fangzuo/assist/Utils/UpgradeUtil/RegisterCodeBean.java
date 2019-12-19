package com.fangzuo.assist.Utils.UpgradeUtil;

//尽量不变动字段，以新增的形式添加
public class RegisterCodeBean {
    public int rid;//公司名称
    public String register_code;//公司名称
    public String CompanyName;//公司名称
    public String phone;//更新日期
    public String address;//更新提示
    public String note;//更新提示
    public String imie;//更新提示
    public String register_time;//更新提示
    public String AppID;//更新提示

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getRegister_code() {
        return register_code;
    }

    public void setRegister_code(String register_code) {
        this.register_code = register_code;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public String getTime() {
        return register_time;
    }

    public void setTime(String time) {
        this.register_time = time;
    }

    public String getAppID() {
        return AppID;
    }

    public void setAppID(String appID) {
        AppID = appID;
    }
}

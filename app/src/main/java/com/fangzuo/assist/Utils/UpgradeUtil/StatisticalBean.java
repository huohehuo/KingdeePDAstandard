package com.fangzuo.assist.Utils.UpgradeUtil;

//尽量不变动字段，以新增的形式添加
public class StatisticalBean {
    public int sid;//公司名称
    public String CompanyName;//公司名称
    public String AppVersion;//app版本号
    public String AppID;//程序id
    public String imie;//更新提示
    public String realTime;//更新提示
    public String num;//更新提示
    public String onActivity;//更新日期
    public String phone;//更新日期




    public StatisticalBean() {
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getAppVersion() {
        return AppVersion;
    }

    public void setAppVersion(String appVersion) {
        AppVersion = appVersion;
    }

    public String getAppID() {
        return AppID;
    }

    public void setAppID(String appID) {
        AppID = appID;
    }

    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public String getRealTime() {
        return realTime;
    }

    public void setRealTime(String realTime) {
        this.realTime = realTime;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getOnActivity() {
        return onActivity;
    }

    public void setOnActivity(String onActivity) {
        this.onActivity = onActivity;
    }
}

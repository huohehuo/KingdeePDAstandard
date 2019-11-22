package com.fangzuo.assist.Beans;

import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Utils.BasicShareUtil;

public class PostBean {
    public String json;
    public String sqlip;
    public String sqlport;
    public String sqluser;
    public String sqlpass;
    public String sqlname;
    public String version;
    public PostBean(String json) {
        BasicShareUtil share = BasicShareUtil.getInstance(App.getContext());
        this.json = json;
        this.sqlip   = share.getDatabaseIp();
        this.sqlport = share.getDatabasePort();
        this.sqluser = share.getDataBaseUser();
        this.sqlpass = share.getDataBasePass();
        this.sqlname = share.getDataBase();
        this.version = share.getVersion();
    }
}

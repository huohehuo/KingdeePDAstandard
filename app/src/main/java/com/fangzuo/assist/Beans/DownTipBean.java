package com.fangzuo.assist.Beans;

import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Utils.BasicShareUtil;

public class DownTipBean {
    public String FName;
    public String FTip;
    public String tag;

    public DownTipBean(String FName, int tag) {
        this.FName = FName;
        this.tag = tag+"";
    }
}

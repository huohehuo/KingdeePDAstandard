package com.fangzuo.assist.Utils;

import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.greendao.gen.DaoSession;

public class LocDataUtil {

    //删除盘点方案
    public static void deletePd(String fid){
        if (null==fid || "".equals(fid))return;
        DaoSession daoSession = GreenDaoManager.getmInstance(App.getContext()).getDaoSession();
        String sql="DELETE  FROM PDSUB WHERE FID = ?";
        daoSession.getDatabase().execSQL(sql,new String[]{fid});
    }
}

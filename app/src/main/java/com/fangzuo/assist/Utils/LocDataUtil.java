package com.fangzuo.assist.Utils;

import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Dao.T_Detail;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.T_DetailDao;

import java.util.List;

public class LocDataUtil {

    //删除盘点方案
    public static void deletePd(String fid){
        if (null==fid || "".equals(fid))return;
        DaoSession daoSession = GreenDaoManager.getmInstance(App.getContext()).getDaoSession();
        String sql="DELETE  FROM PDSUB WHERE FID = ?";
        daoSession.getDatabase().execSQL(sql,new String[]{fid});
    }



    //该单据存在的查看数据
    public static String getLocDetail(int activity){
        T_DetailDao unitDao = GreenDaoManager.getmInstance(App.getContext()).getDaoSession().getT_DetailDao();
        List<T_Detail> units = unitDao.queryBuilder().where(
                T_DetailDao.Properties.Activity.eq(activity)
        ).build().list();
        if (units.size()>0){
            return units.size()+"";
        }else{
            return "";
        }
    }

}

package com.fangzuo.assist.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.fangzuo.greendao.gen.DaoMaster;
import com.fangzuo.greendao.gen.DaoSession;


/**
 * Created by NB on 2017/7/27.
 */

public class GreenDaoManager {
    private static GreenDaoManager mInstance;
    private final DaoMaster daoMaster;
    private final DaoSession daoSession;

    private GreenDaoManager(Context context) {
        //通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper
        //注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, "db_localInfo",null);
        //注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        SQLiteDatabase sqLiteDatabase = devOpenHelper.getWritableDatabase();

        daoMaster = new DaoMaster(sqLiteDatabase);
        daoSession = daoMaster.newSession();
    }

    public static GreenDaoManager getmInstance(Context context){
        if (mInstance == null){
            synchronized (GreenDaoManager.class){
                if (mInstance == null){
                    mInstance = new GreenDaoManager(context);
                }
            }
        }
        return mInstance;
    }
    public DaoSession getDaoSession(){
        return  daoSession;
    }
    public DaoMaster getDaoMaster(){
        return daoMaster;
    }
}

package com.fangzuo.assist.Utils;

import com.google.gson.Gson;

import com.fangzuo.assist.Beans.ConnectSQLBean;

import java.util.ArrayList;

/**
 * Created by NB on 2017/7/24.
 */

public class JsonCreater {
    public static Gson gson = new Gson();
    public static String ConnectSQL(String ip,String port,String username,String password,String dataBase){
        ConnectSQLBean cBean = new ConnectSQLBean(ip,port,username,password,dataBase);
        return gson.toJson(cBean);
    }

    public static String DownLoadData(String ip, String port, String username, String password, String dataBase, String version, ArrayList<Integer> choose){
        ConnectSQLBean cBean = new ConnectSQLBean(ip,port,username,password,dataBase,version,choose);
        return gson.toJson(cBean);
    }

    public static String DownLoadDataWithIp(BasicShareUtil share, ArrayList<Integer> choose){
        ConnectSQLBean cBean = new ConnectSQLBean(share.getDatabaseIp(),
                share.getDatabasePort(), share.getDataBaseUser(), share.getDataBasePass(),
                share.getDataBase(), share.getVersion(),choose);
        return gson.toJson(cBean);
    }
}

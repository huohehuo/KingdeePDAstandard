package com.fangzuo.assist.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by wly on 2016/11/26.
 */
public class BasicShareUtil {
    private static final String FILE_NAME = "ASSIST_SQL";
    private static BasicShareUtil mInstance;
    private Context mContext=null;
    private final SharedPreferences shared;
    private final SharedPreferences.Editor editor;


    public synchronized static BasicShareUtil getInstance(Context mContext){
        if(mInstance == null)
            mInstance = new BasicShareUtil(mContext);
        return mInstance;
    }

    public BasicShareUtil(Context context) {
        this.mContext = context;
        shared = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = shared.edit();
    }
    public void setIMIE(String imie){
        editor.putString("imie",imie).apply();
    }

    public String getIMIE(){
        return shared.getString("imie","");
    }
    public void setIP(String Ip){
        editor.putString("serverIp",Ip);
        editor.apply();
    }

    public void setPort(String Port){
        editor.putString("serverPort",Port);
        editor.apply();
    }

    public String getIP(){
        return shared.getString("serverIp","");
    }

    public String getPort(){
        return shared.getString("serverPort","");
    }

    public String getBaseURL(){
        return "http://"+ shared.getString("serverIp","192.168.0.0")+":"+shared.getString("serverPort","8080")+"/Assist/";
    }

    public void setRegisterState(boolean b){
        editor.putBoolean("RegisterState",b);
        editor.apply();
    }

    public void setMAC(String mac){
        editor.putString("MAC",mac).apply();
    }

    public String getMAC(){
        return shared.getString("MAC","");
    }
    public boolean getRegisterState(){
        return shared.getBoolean("RegisterState",false);
    }

    public void setDatabaseIp(String ip){
        editor.putString("databaseIp",ip);
        editor.apply();
    }
    public String getDatabaseIp(){
        return shared.getString("databaseIp","");
    }


    public void setDatabasePort(String port){
        editor.putString("DatabasePort",port);
        editor.apply();
    }
    public String getDatabasePort(){
        return shared.getString("DatabasePort","");
    }


    public void setDataBaseUser(String DataBaseUser) {
        editor.putString("DataBaseUser", DataBaseUser);
        editor.apply();
    }
    public String getDataBaseUser(){
        return shared.getString("DataBaseUser","");
    }


    public void setDataBasePass(String DataBasePass){
        editor.putString("DataBasePass",DataBasePass);
        editor.apply();
    }
    public String getDataBasePass(){
        return shared.getString("DataBasePass","");
    }

    public void setDataBase(String DataBase){
        editor.putString("DataBase",DataBase);
        editor.apply();
    }
    public String getDataBase(){
        return shared.getString("DataBase","");
    }

    public void setVersion(String version){
        editor.putString("version",version);
        editor.apply();
    }
    public String getVersion(){
       return shared.getString("version","");
    }

    public void setTryState(int state){
        editor.putInt("TryState",state).apply();
    }
    public int getTryState(){
        return shared.getInt("TryState",0);
    }

    public void setTryTime(long time){
        editor.putLong("trytime",time).apply();
    }

    public long getTryTime(){
        return shared.getLong("trytime",0L);
    }

    public void setIsOL(boolean isOL){
        editor.putBoolean("isOL",isOL).apply();
    }

    public boolean getIsOL(){
        return shared.getBoolean("isOL",false);
    }

    public void  clear(){
        editor.clear().apply();
    }






}

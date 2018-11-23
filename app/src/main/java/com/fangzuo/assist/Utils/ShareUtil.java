package com.fangzuo.assist.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wly on 2016/11/26.
 */
public class ShareUtil {
    private static final String FILE_NAME = "ASSIST";
    public static ShareUtil mInstance;
    private Context mContext=null;
    private final SharedPreferences shared;
    private final SharedPreferences.Editor editor;


    public synchronized static ShareUtil getInstance(Context mContext){
        if(mInstance == null)
            mInstance = new ShareUtil(mContext);
        return mInstance;
    }

    public ShareUtil(Context context) {
        this.mContext = context;
        shared = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = shared.edit();
    }
    //获取单据的单据编号：key为activity的类名字
    public void setOrderCode(Activity activity, long orderCode){
        editor.putLong(activity.getClass().getSimpleName(),orderCode).apply();
    }
    public long getOrderCode(Activity activity){
        return shared.getLong(activity.getClass().getSimpleName(),0);
    }

    public void setBooleam(String key,boolean val){
        editor.putBoolean(key,val);
        editor.apply();
    }
    public boolean getBoolean(String key){
        return shared.getBoolean(key,false);
    }

    public void setPOD(String ip){
        editor.putString("POD",ip);
        editor.apply();
    }
    public String getPOD(){
        return shared.getString("POD","");
    }


    public void setDatabaseIp(String ip){
        editor.putString("databaseIp",ip);
        editor.apply();
    }
    public String getDatabaseIp(){
        return shared.getString("databaseIp","");
    }
    public void setPROGOrderCode(long PISOrderCode){
        editor.putLong("getPROGOrderCode",PISOrderCode).apply();
    }

    public long getPROGOrderCode(){
        return shared.getLong("getPROGOrderCode",0);
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


    public void setUserName(String UserName){
        editor.putString("UserName",UserName);
        editor.apply();
    }
    public String getUserName(){
        return shared.getString("UserName","");
    }

    public void setUserID(String setUserID){
        editor.putString("setUserID",setUserID);
        editor.apply();
    }
    public String getsetUserID(){
        return shared.getString("setUserID","");
    }

    public void setRegisterState(boolean b){
        editor.putBoolean("RegisterState",b);
        editor.apply();
    }
    public boolean getRegisterState(){
        return shared.getBoolean("RegisterState",false);
    }

    public void setServerIP(String ip){
        editor.putString("serverip",ip).apply();
    }

    public String getServerIP(){
        return shared.getString("serverip","");
    }

    public void setServerPort(String port){
        editor.putString("serverport",port).apply();
    }

    public String getServerPort(){
        return shared.getString("serverport","");
    }

    public String getServerUrl(){
        return "http://"+getServerIP()+":"+getServerPort()+"/Assist/";
    }

    public void setMAC(String mac){
        editor.putString("MAC",mac).apply();
    }

    public String getMAC(){
        return shared.getString("MAC","");
    }

    public void setPISpayMethod(int position){
        editor.putInt("pispm",position).apply();
    }
    public int getPISpayMethod(){
        return shared.getInt("pispm",0);
    }

    public void setPISkeepperson(int position){
        editor.putInt("piskp",position).apply();
    }
    public int getPISkeepperson(){
        return shared.getInt("piskp",0);
    }

    public void setPISyanshou(int position){
        editor.putInt("pisys",position).apply();
    }
    public int getPISyanshou(){
        return shared.getInt("pisys",0);
    }

    public void setPISdepartment(int position){
        editor.putInt("PISdepartment",position).apply();
    }
    public int getPISdepartment(){
        return shared.getInt("PISdepartment",0);
    }

    public void setPISemployee(int position){
        editor.putInt("PISemployee",position).apply();
    }
    public int getPISemployee(){
        return shared.getInt("PISemployee",0);
    }

    public void setPISManager(int position){
        editor.putInt("PISManager",position).apply();
    }
    public int getPISManager(){
        return shared.getInt("PISManager",0);
    }

    public void setPISwanglaikemu(int position){
        editor.putInt("PISwanglaikemu",position).apply();
    }
    public int getPISwanglaikemu(){
        return shared.getInt("PISwanglaikemu",0);
    }

    public void setPISdate(String date){
        editor.putString("PISdate",date).apply();
    }
    public String getPISdate(){
        return shared.getString("PISdate",getTime());
    }

    public void setPISdatepay(String PISdatepay){
        editor.putString("PISdatepay",PISdatepay).apply();
    }
    public String getPISdatepay(){
        return shared.getString("PISdatepay",getTime());
    }

    public void setPIScbstorage(boolean PIScbstorage){
        editor.putBoolean("PIScbstorage",PIScbstorage).apply();
    }
    public boolean getPIScbstorage(){
        return shared.getBoolean("PIScbstorage",false);
    }


    public void setPISOrderCode(long PISOrderCode){
        editor.putLong("PISOrderCode",PISOrderCode).apply();
    }
    public long getPISOrderCode(){
        return shared.getLong("PISOrderCode",0);
    }

    public void  clear(){
        editor.clear().apply();
    }

    public String getTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date();
        String str = format.format(curDate);
        return str;
    }

    public void setPROISOrderCode(long PISOrderCode){
        editor.putLong("PROISOrderCode",PISOrderCode).apply();
    }
    public long getPROISOrderCode(){
        return shared.getLong("PROISOrderCode",0);
    }

    public void setPROISCapture(int position){
        editor.putInt("PROISCapture",position).apply();
    }
    public int getPROISCapture(){
        return shared.getInt("PROISCapture",0);
    }

    public void setPROISyanshou(int position){
        editor.putInt("PROISyanshou",position).apply();
    }
    public int getPROISyanshou(){
        return shared.getInt("PROISyanshou",0);
    }

    public void setPROISdate(String date){
        editor.putString("PROISdate",date).apply();
    }
    public String getPROISdate(){
        return shared.getString("PROISdate",getTime());
    }
    public void setPOdate(String date){
        editor.putString("POdate",date).apply();
    }
    public String getPOdate(){
        return shared.getString("POdate",getTime());
    }
    public void setPOpaydate(String date){
        editor.putString("POpaydate",date).apply();
    }
    public String getPOpaydate(){
        return shared.getString("POpaydate",getTime());
    }

    public void setPODepartment(int position){
        editor.putInt("PODepartment",position).apply();
    }
    public int getPODepartment(){
        return shared.getInt("PODepartment",0);
    }

    public void setPOPurchaseMethod(int position){
        editor.putInt("POpayMethod",position).apply();
    }
    public int getPOPurchaseMethod(){
        return shared.getInt("POpayMethod",0);
    }
    public void setPOPurchaseRange(int position){
        editor.putInt("POPurchaseRange",position).apply();
    }
    public int getPOPurchaseRange(){
        return shared.getInt("POPurchaseRange",0);
    }

    public void setPOYuandan(int position){
        editor.putInt("POYuandan",position).apply();
    }
    public int getPOYuandan(){
        return shared.getInt("POYuandan",0);
    }

    public void setPOPayMethod(int position){
        editor.putInt("POPayMethod",position).apply();
    }
    public int getPOPayMethod(){
        return shared.getInt("POPayMethod",0);
    }

    public void setPOEmployee(int position){
        editor.putInt("POEmployee",position).apply();
    }
    public int getPOEmployee(){
        return shared.getInt("POEmployee",0);
    }

    public void setPOManager(int position){
        editor.putInt("POManager",position).apply();
    }
    public int getPOManager(){
        return shared.getInt("POManager",0);
    }

    public void setSOSendMethod(int position){
        editor.putInt("SOSendMethod",position).apply();
    }
    public int getSOSendMethod(){
        return shared.getInt("SOSendMethod",0);
    }

    public void setPDMTCapture(int position){
        editor.putInt("setPDMTCapture",position).apply();
    }
    public int getPDMTCapture(){
        return shared.getInt("setPDMTCapture",0);
    }

    public void setXSDDPDFLTZDCapture(int position){
        editor.putInt("setXSDDPDFLTZDCapture",position).apply();
    }
    public int getXSDDPDFLTZDCapture(){
        return shared.getInt("setXSDDPDFLTZDCapture",0);
    }

    public void setPDPOCapture(int position){
        editor.putInt("setPDPOCapture",position).apply();
    }
    public int getPDPOCapture(){
        return shared.getInt("setPDPOCapture",0);
    }

    public void setPPISCapture(int position){
        editor.putInt("setPPISCapture",position).apply();
    }
    public int getPPISCapture(){
        return shared.getInt("setPPISCapture",0);
    }

    public void setPPISEmployee(int position){
        editor.putInt("setPPISEmployee",position).apply();
    }
    public int getPPISEmployee(){
        return shared.getInt("setPPISEmployee",0);
    }


    public void setSLTZCapture(int position){
        editor.putInt("setSLTZCapture",position).apply();
    }
    public int getSLTZCapture(){
        return shared.getInt("setSLTZCapture",0);
    }


    public void setPDSNCapture(int position){
        editor.putInt("setPDSNCapture",position).apply();
    }
    public int getPDSNCapture(){
        return shared.getInt("setPDSNCapture",0);
    }

    public void setPDSNSendman(int position){
        editor.putInt("setPDSNSendman",position).apply();
    }
    public int getPDSNSendman(){
        return shared.getInt("setPDSNSendman",0);
    }

    public void setPDMTSendman(int position){
        editor.putInt("setPDMTSendman",position).apply();
    }
    public int getPDMTSendman(){
        return shared.getInt("setPDMTSendman",0);
    }

    public void setHBDPDCPRKCapture(int position){
        editor.putInt("setnHBDPDCPRKCapture",position).apply();
    }
    public int getHBDPDCPRKCapture(){
        return shared.getInt("setnHBDPDCPRKCapture",0);
    }


    public void setPOOrderCode(long PISOrderCode){
        editor.putLong("POOrderCode",PISOrderCode).apply();
    }
    public long getPOOrderCode(){
        return shared.getLong("POOrderCode",0);
    }

    public void setSOOrderCode(long PISOrderCode){
        editor.putLong("SOOrderCode",PISOrderCode).apply();
    }
    public long getSOOrderCode(){
        return shared.getLong("SOOrderCode",0);
    }

    public void setSODepartment(int position){
        editor.putInt("SODepartment",position).apply();
    }
    public int getSODepartment(){
        return shared.getInt("SODepartment",0);
    }
    public void setSOSaleMethod(int position){
        editor.putInt("SOPurchaseMethod",position).apply();
    }
    public int getSOSaleMethod(){
        return shared.getInt("SOPurchaseMethod",0);
    }

    public void setSOSaleRange(int position){
        editor.putInt("SOSaleRange",position).apply();
    }
    public int getSOSaleRange(){
        return shared.getInt("SOSaleRange",0);
    }

    public void setSOYuandan(int position){
        editor.putInt("SOYuandan",position).apply();
    }
    public int getSOYuandan(){
        return shared.getInt("SOYuandan",0);
    }

    public void setSOPayMethod(int position){
        editor.putInt("SOPayMethod",position).apply();
    }
    public int getSOPayMethod(){
        return shared.getInt("SOPayMethod",0);
    }

    public void setSOEmployee(int position){
        editor.putInt("setSOEmployee",position).apply();
    }
    public int getSOEmployee(){
        return shared.getInt("setSOEmployee",0);
    }

    public void setSOManager(int position){
        editor.putInt("setSOManager",position).apply();
    }
    public int getSOManager(){
        return shared.getInt("setSOManager",0);
    }

    public void setSOUTOrderCode(long PISOrderCode){
        editor.putLong("SOUTOrderCode",PISOrderCode).apply();
    }
    public long getSOUTOrderCode(){
        return shared.getLong("SOUTOrderCode",0);
    }

    public void setSOUTDepartment(int position){
        editor.putInt("SOUTDepartment",position).apply();
    }
    public int getSOUTDepartment(){
        return shared.getInt("SOUTDepartment",0);
    }

    public int getSOUTsaleMethod(){
        return shared.getInt("SOUTsaleMethod",0);
    }
    public void setSOUTsaleMethod(int position){
        editor.putInt("SOUTsaleMethod",position).apply();
    }

    public void setSoutSaleRange(int position){
        editor.putInt("POPurchaseRange",position).apply();
    }
    public int getSoutSaleRange(){
        return shared.getInt("POPurchaseRange",0);
    }

    public void setSOUTYuandan(int position){
        editor.putInt("SOUTYuandan",position).apply();
    }
    public int getSOUTYuandan(){
        return shared.getInt("SOUTYuandan",0);
    }

    public void setSOUTPayMethod(int position){
        editor.putInt("SOUTPayMethod",position).apply();
    }
    public int getSOUTPayMethod(){
        return shared.getInt("SOUTPayMethod",0);
    }

    public void setSOUTEmployee(int position){
        editor.putInt("SOUTEmployee",position).apply();
    }
    public int getSOUTEmployee(){
        return shared.getInt("SOUTEmployee",0);
    }

    public void setSOUTManager(int position){
        editor.putInt("SOUTManager",position).apply();
    }
    public int getSOUTManager(){
        return shared.getInt("SOUTManager",0);
    }


    public void setSOUTsendmethod(int position){
        editor.putInt("SOUTsendmethod",position).apply();
    }
    public int getSOUTsendmethod(){
        return shared.getInt("SOUTsendmethod",0);
    }

    public void setSOUTdate(String date){
        editor.putString("SOUTdate",date).apply();
    }


    public String getSOUTdate(){
        return shared.getString("SOUTdate",getTime());
    }

    public void setSOUTdatepay(String PISdatepay){
        editor.putString("SOUTdatepay",PISdatepay).apply();
    }
    public String getSOUTdatepay(){
        return shared.getString("SOUTdatepay",getTime());
    }

    public void setOISOrderCode(long PISOrderCode){
        editor.putLong("OISOrderCode",PISOrderCode).apply();
    }
    public long getOISOrderCode(){
        return shared.getLong("OISOrderCode",0);
    }

    public void setOISkeepperson(int position){
        editor.putInt("setOISkeepperson",position).apply();
    }
    public int getOISkeepperson(){
        return shared.getInt("setOISkeepperson",0);
    }

    public void setOISyanshou(int position){
        editor.putInt("setOISyanshou",position).apply();
    }
    public int getOISyanshou(){
        return shared.getInt("setOISyanshou",0);
    }

    public void setOISManager(int position){
        editor.putInt("setOISManager",position).apply();
    }
    public int getOISManager(){
        return shared.getInt("setOISManager",0);
    }

    public void setOISemployee(int position){
        editor.putInt("setOISemployee",position).apply();
    }
    public int getOISemployee(){
        return shared.getInt("setOISemployee",0);
    }


    public void setOISdepartment(int position){
        editor.putInt("setOISdepartment",position).apply();
    }
    public int getOISdepartment(){
        return shared.getInt("setOISdepartment",0);
    }

    public void setOISInstoreType(int position){
        editor.putInt("setOISInstoreType",position).apply();
    }
    public int getOISInstoreType(){
        return shared.getInt("setOISInstoreType",0);
    }
    public void setOISdate(String date){
        editor.putString("setOISdate",date).apply();
    }
    public String getOISdate(){
        return shared.getString("setOISdate",getTime());
    }

    public void setDBOrderCode(long PISOrderCode){
        editor.putLong("setDBOrderCode",PISOrderCode).apply();
    }
    public long getDBOrderCode(){
        return shared.getLong("setDBOrderCode",0);
    }

    public void setpushsoorderCode(long PISOrderCode){
        editor.putLong("setpushsoorderCode",PISOrderCode).apply();
    }
    public long getpushsoorderCode(){
        return shared.getLong("setpushsoorderCode",0);
    }

    public void setProISYuanDan(int position){
        editor.putInt("setProISYuanDan",position).apply();
    }
    public int getProISYuanDan(){
        return shared.getInt("setProISYuanDan",0);
    }

    public void setPDMTSaleMethod(int position){
        editor.putInt("setPDMTSaleMethod",position).apply();
    }

    public int getPDMTSaleMethod(){
        return shared.getInt("setPDMTSaleMethod",0);
    }

    public void setPDMTSaleScop(int position){
        editor.putInt("setPDMTSaleScop",position).apply();
    }

    public int getPDMTSaleScop(){
        return shared.getInt("setPDMTSaleScop",0);
    }

    public void setPDMTYuanDan(int position){
        editor.putInt("setPDMTYuanDan",position).apply();
    }

    public int getPDMTYuanDan(){
        return shared.getInt("setPDMTYuanDan",0);
    }
    public void setPDMTPayMethod(int position){
        editor.putInt("setPDMTPayMethod",position).apply();
    }

    public int getPDMTPayMethod(){
        return shared.getInt("setPDMTPayMethod",0);
    }

    public void setPDMTManager(int position){
        editor.putInt("setPDMTManager",position).apply();
    }

    public int getPDMTManager(){
        return shared.getInt("setPDMTManager",0);
    }
    public void setPDMTsendMethod(int position){
        editor.putInt("setPDMTsendMethod",position).apply();
    }

    public int getPDMTsendMethod(){
        return shared.getInt("setPDMTManager",0);
    }
    public void setPDPOPurchaseMehtod(int position){
        editor.putInt("setPDPOPurchaseMehtod",position).apply();
    }

    public int getPDPOPurchaseMehtod(){
        return shared.getInt("setPDPOPurchaseMehtod",0);
    }
    public void setPDPOPurchaseScop(int position){
        editor.putInt("setPDPOPurchaseScop",position).apply();
    }

    public int getPDPOPurchaseScop(){
        return shared.getInt("setPDPOPurchaseScop",0);
    }
    public void setPDPOYuandan(int position){
        editor.putInt("setPDPOYuandan",position).apply();
    }

    public int getPDPOYuandan(){
        return shared.getInt("setPDPOYuandan",0);
    }

    public void setPDPOPayMethod(int position){
        editor.putInt("setPDPOPayMethod",position).apply();
    }

    public int getPDPOPayMethod(){
        return shared.getInt("setPDPOPayMethod",0);
    }

    public void setPDPOManager(int position){
        editor.putInt("setPDPOManager",position).apply();
    }

    public int getPDPOManager(){
        return shared.getInt("setPDPOManager",0);
    }

    public void setPDSNSaleMethod(int position){
        editor.putInt("setPDSNSaleMethod",position).apply();
    }

    public int getPDSNSaleMethod(){
        return shared.getInt("setPDSNSaleMethod",0);
    }

    public void setPDSNSaleScop(int position){
        editor.putInt("setPDSNSaleScop",position).apply();
    }

    public int getPDSNSaleScop(){
        return shared.getInt("setPDSNSaleScop",0);
    }

    public void setPDSNYuandan(int position){
        editor.putInt("setPDSNYuandan",position).apply();
    }

    public int getPDSNYuandan(){
        return shared.getInt("setPDSNYuandan",0);
    }

    public void setPDSNPayMethod(int position){
        editor.putInt("setPDSNPayMethod",position).apply();
    }

    public int getPDSNPayMethod(){
        return shared.getInt("setPDSNPayMethod",0);
    }
    public void setPDSNManager(int position){
        editor.putInt("setPDSNManager",position).apply();
    }

    public int getPDSNManager(){
        return shared.getInt("setPDSNManager",0);
    }

    public void setPDSNSendMethod(int position){
        editor.putInt("setPDSNSendMethod",position).apply();
    }

    public int getPDSNSendMethod(){
        return shared.getInt("setPDSNSendMethod",0);
    }

    public void setOOSOrderCode(long PISOrderCode){
        editor.putLong("OOSOrderCode",PISOrderCode).apply();
    }
    public long getOOSOrderCode(){
        return shared.getLong("OOSOrderCode",0);
    }

    public void setOOSkeepperson(int position){
        editor.putInt("setOOSkeepperson",position).apply();
    }
    public int getOOSkeepperson(){
        return shared.getInt("setOOSkeepperson",0);
    }

    public void setOOSyanshou(int position){
        editor.putInt("setOOSyanshou",position).apply();
    }
    public int getOOSyanshou(){
        return shared.getInt("setOOSyanshou",0);
    }

    public void setOOSManager(int position){
        editor.putInt("setOOSManager",position).apply();
    }
    public int getOOSManager(){
        return shared.getInt("setOOSManager",0);
    }

    public void setOOSemployee(int position){
        editor.putInt("setOOSemployee",position).apply();
    }
    public int getOOSemployee(){
        return shared.getInt("setOOSemployee",0);
    }


    public void setOOSdepartment(int position){
        editor.putInt("setOOSdepartment",position).apply();
    }
    public int getOOSdepartment(){
        return shared.getInt("setOOSdepartment",0);
    }

    public void setOOSInstoreType(int position){
        editor.putInt("setOOSInstoreType",position).apply();
    }
    public int getOOSInstoreType(){
        return shared.getInt("setOOSInstoreType",0);
    }
    public void setOOSdate(String date){
        editor.putString("setOOSdate",date).apply();
    }
    public String getOOSdate(){
        return shared.getString("setOOSdate",getTime());
    }

    public void setDBSignPerson(int position){
        editor.putInt("setDBSignPerson",position).apply();
    }
    public int getDBSignPerson(){
        return shared.getInt("setDBSignPerson",0);
    }

    public void setDBDepartment(int position){
        editor.putInt("setDBDepartment",position).apply();
    }
    public int getDBDepartment(){
        return shared.getInt("setDBSignPerson",0);
    }

    public void setDBcapturePerson(int position){
        editor.putInt("setDBcapturePerson",position).apply();
    }
    public int getDBcapturePerson(){
        return shared.getInt("setDBcapturePerson",0);
    }

    public void setDBEmployee(int position){
        editor.putInt("setDBEmployee",position).apply();
    }
    public int getDBEmployee(){
        return shared.getInt("setDBEmployee",0);
    }

    public void  setOISisAuto(boolean isAuto){
        editor.putBoolean("setOISisAuto",isAuto).apply();
    }

    public boolean getOISisAuto(){
        return shared.getBoolean("setOISisAuto",false);
    }
    public void  setOOSisAuto(boolean isAuto){
        editor.putBoolean("setOOSisAuto",isAuto).apply();
    }

    public boolean getOOSisAuto(){
        return shared.getBoolean("setOOSisAuto",false);
    }

    public void  setPGisAuto(boolean isAuto){
        editor.putBoolean("setPGisAuto",isAuto).apply();
    }

    public boolean getPGisAuto(){
        return shared.getBoolean("setPGisAuto",false);
    }

    public void  setPISisAuto(boolean isAuto){
        editor.putBoolean("setPISisAuto",isAuto).apply();
    }

    public boolean getPISisAuto(){
        return shared.getBoolean("setPISisAuto",false);
    }

    public void  setPUISisAuto(boolean isAuto){
        editor.putBoolean("setPUISisAuto",isAuto).apply();
    }

    public boolean getPUISisAuto(){
        return shared.getBoolean("setPUISisAuto",false);
    }

    public void  setPOisAuto(boolean isAuto){
        editor.putBoolean("setPOisAuto",isAuto).apply();
    }

    public boolean getPOisAuto(){
        return shared.getBoolean("setPOisAuto",false);
    }

    public void  setPDMTisAuto(boolean isAuto){
        editor.putBoolean("setPDMTisAuto",isAuto).apply();
    }

    public boolean getPDMTisAuto(){
        return shared.getBoolean("setPDMTisAuto",false);
    }

    public void  setPDPOisAuto(boolean isAuto){
        editor.putBoolean("setPDPOisAuto",isAuto).apply();
    }

    public boolean getPDPOisAuto(){
        return shared.getBoolean("setPDPOisAuto",false);
    }

    public void  setPDSNisAuto(boolean isAuto){
        editor.putBoolean("setPDSNisAuto",isAuto).apply();
    }

    public boolean getPDSNisAuto(){
        return shared.getBoolean("setPDSNisAuto",false);
    }

    public void  setSOisAuto(boolean isAuto){
        editor.putBoolean("setSOisAuto",isAuto).apply();
    }

    public boolean getSOisAuto(){
        return shared.getBoolean("setSOisAuto",false);
    }

    public void  setSLTZisAuto(boolean isAuto){
        editor.putBoolean("setSLTZisAuto",isAuto).apply();
    }

    public boolean getSLTZisAuto(){
        return shared.getBoolean("setSLTZisAuto",false);
    }

    public void  setSOUTisAuto(boolean isAuto){
        editor.putBoolean("setSOUTisAuto",isAuto).apply();
    }

    public boolean getSOUTisAuto(){
        return shared.getBoolean("setSOUTisAuto",false);
    }

    public void setSLTZpurchaseMethod(int position){
        editor.putInt("setSLTZpurchaseMethod",position).apply();
    }
    public int getSLTZpurchaseMethod(){
        return shared.getInt("setSLTZpurchaseMethod",0);
    }

    public void setSLTZcapturePerson(int position){
        editor.putInt("setSLTZcapturePerson",position).apply();
    }
    public int getSLTZcapturePerson(){
        return shared.getInt("setSLTZcapturePerson",0);
    }

    public void setSLTZSignPerson(int position){
        editor.putInt("setSLTZSignPerson",position).apply();
    }
    public int getSLTZSignPerson(){
        return shared.getInt("setSLTZSignPerson",0);
    }

    public void setSLTZwlkm(int position){
        editor.putInt("setSLTZwlkm",position).apply();
    }
    public int getSLTZZwlkm(){
        return shared.getInt("setSLTZwlkm",0);
    }

    public void setSLTZManager(int position){
        editor.putInt("setSLTZManager",position).apply();
    }
    public int getSLTZManager(){
        return shared.getInt("setSLTZManager",0);
    }

    public void setPGDepartment(int position){
        editor.putInt("setPGDepartment",position).apply();
    }
    public int getPGDepartment(){
        return shared.getInt("setPGDepartment",0);
    }

    public void setPGGetMan(int position){
        editor.putInt("setPGGetMan",position).apply();
    }
    public int getPGGetMan(){
        return shared.getInt("setPGGetMan",0);
    }

    public void setPGGetType(int position){
        editor.putInt("setPGGetType",position).apply();
    }
    public int getPGGetType(){
        return shared.getInt("setPGGetType",0);
    }
    public void setPGsendMan(int position){
        editor.putInt("setPGsendMan",position).apply();
    }
    public int getPGsendMan(){
        return shared.getInt("setPGsendMan",0);
    }

    public void setPO1Department(int position){
        editor.putInt("PO1Department",position).apply();
    }
    public int getPO1Department(){
        return shared.getInt("PODepartment",0);
    }

    public void setPO1PurchaseMethod(int position){
        editor.putInt("PO1payMethod",position).apply();
    }
    public int getPO1PurchaseMethod(){
        return shared.getInt("PO1payMethod",0);
    }
    public void setPO1PurchaseRange(int position){
        editor.putInt("PO1PurchaseRange",position).apply();
    }
    public int getPO1PurchaseRange(){
        return shared.getInt("PO1PurchaseRange",0);
    }

    public void setPO1Yuandan(int position){
        editor.putInt("PO1Yuandan",position).apply();
    }
    public int getPO1Yuandan(){
        return shared.getInt("PO1Yuandan",0);
    }

    public void setPO1PayMethod(int position){
        editor.putInt("PO1PayMethod",position).apply();
    }
    public int getPO1PayMethod(){
        return shared.getInt("PO1PayMethod",0);
    }

    public void setPO1Employee(int position){
        editor.putInt("PO1Employee",position).apply();
    }
    public int getPO1Employee(){
        return shared.getInt("PO1Employee",0);
    }
    public void setPO1Manager(int position){
        editor.putInt("PO1Manager",position).apply();
    }
    public int getPO1Manager(){
        return shared.getInt("PO1Manager",0);
    }

    public void  setPDisAuto(boolean isAuto){
        editor.putBoolean("setPDisAuto",isAuto).apply();
    }

    public boolean getPDisAuto(){
        return shared.getBoolean("setPDisAuto",false);
    }

    public void  setDBisAuto(boolean isAuto){
        editor.putBoolean("setDBisAuto",isAuto).apply();
    }

    public boolean getDBisAuto(){
        return shared.getBoolean("setDBisAuto",false);
    }


    public int getCGSLTZDPurchaseMehtod(){
        return shared.getInt("CGSLTZDPurchaseMehtod",0);
    }
    public void setCGSLTZDPurchaseMehtod(int position){
        editor.putInt("CGSLTZDPurchaseMehtod",position).apply();
    }

    public int getCGSLTZDManager(){
        return shared.getInt("getCGSLTZDManager",0);
    }
    public void setCGSLTZDManager(int position){
        editor.putInt("getCGSLTZDManager",position).apply();
    }


    public void setFHDBEmployee(int position){
        editor.putInt("FHDBEmployee",position).apply();
    }
    public int getFHDBEmployee(){
        return shared.getInt("FHDBEmployee",0);
    }
    public void setFHDBCapture(int position){
        editor.putInt("FHDBCapture",position).apply();
    }
    public int getFHDBCapture(){
        return shared.getInt("FHDBCapture",0);
    }
    public void setFHDBsign(int position){
        editor.putInt("FHDBsign",position).apply();
    }
    public int getFHDBsign(){
        return shared.getInt("FHDBsign",0);
    }
    public void setFHDBDepartment(int position){
        editor.putInt("FHDBDepartment",position).apply();
    }
    public int getFHDBDepartment(){
        return shared.getInt("FHDBDepartment",0);
    }

    public void setPISOperator(int position){
        editor.putInt("setPISOperator",position).apply();
    }
    public int getPISOperator(){
        return shared.getInt("setPISOperator",0);
    }

    public void setPPISOperator(int position){
        editor.putInt("setPISOperator",position).apply();
    }
    public int getPPISOperator(){
        return shared.getInt("setPISOperator",0);
    }
}

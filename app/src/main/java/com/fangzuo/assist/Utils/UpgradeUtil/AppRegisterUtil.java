package com.fangzuo.assist.Utils.UpgradeUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Activity.SplashActivity;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Utils.Asynchttp;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.Info;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.Toast;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AppRegisterUtil {
    public static String BaseUrl = "http://148.70.108.65:8080/LogAssist/UploadRegisterUser";
//    public static String BaseUrl = "http://192.168.0.103:8085/Assist/UploadRegisterUser";
    //数据统计操纵
    public static void upDataStatis(final Context context,String code,String note) {
        RegisterCodeBean bean = new RegisterCodeBean();
        bean.imie = Build.MODEL+"--"+ SplashActivity.getNewMac();//手机IMIE码
//        bean.imie = Build.MODEL+"--"+ getTimeLong(false);//手机IMIE码
        bean.AppID=context.getPackageName();//appid
        bean.register_code= code;//版本信息
        bean.note=note;//所在页面
        bean.register_time=getTime(false);//当前时间
        bean.phone="10086";//手机号
        bean.CompanyName= Config.Company;//手机号
        bean.address="10086";//手机号
        Lg.e("数据统计",bean);
        Asynchttp.post(App.getContext(), BaseUrl, new Gson().toJson(bean), new Asynchttp.Response() {
            @Override
            public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                Toast.showText("请求注册成功,请与实施联系");
//                Lg.e("统计成功,无返回东西");
//                Lg.e("得到版本信息",cBean.returnJson);
//                showDlg(context,cBean.returnJson);
            }

            @Override
            public void onFailed(String Msg, AsyncHttpClient client) {
                Toast.showText("请求注册失败,"+Msg);
//                Lg.e("统计失败");
//                Lg.e("查询版本信息错误");
            }
        });
    }
    public static String getTime(boolean b){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat(b?"yyyy-MM-dd":"yyyyMMdd");
        Date curDate = new Date();
        Log.e("date",curDate.toString());
        String str = format.format(curDate);
        return str;
    }
    public static String getTimeLong(boolean b) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat(b ? "yyyy-MM-dd-HH-mm-ss" : "yyyyMMddHHmmss");
        Date curDate = new Date();
        Log.e("date", curDate.toString());
        String str = format.format(curDate);
        return str;
    }


}

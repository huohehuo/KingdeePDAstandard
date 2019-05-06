package com.fangzuo.assist.Activity.Crash;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.fangzuo.assist.RxSerivce.RService;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.Config;
import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 //  ┏┓　　　┏┓
 //┏┛┻━━━┛┻┓
 //┃　　　　　　　┃
 //┃　　　━　　　┃
 //┃　┳┛　┗┳　┃
 //┃　　　　　　　┃
 //┃　　　┻　　　┃
 //┃　　　　　　　┃
 //┗━┓　　　┏━┛
 //    ┃　　　┃   神兽保佑
 //    ┃　　　┃   代码无BUG！
 //    ┃　　　┗━━━┓
 //    ┃　　　　　　　┣┓
 //    ┃　　　　　　　┏┛
 //    ┗┓┓┏━┳┓┏┛
 //      ┃┫┫　┃┫┫
 //      ┗┻┛　┗┻┛
 */


public class App extends MultiDexApplication {
    public static boolean isDebug=true;
    public static String JsonFile="";

    private static Context mContext;
//    private String mCurDev = "";
//    private boolean isIsDebug =true;
//    static App instance = null;

    private static OkHttpClient           okHttpClient;
    private static okhttp3.logging.HttpLoggingInterceptor interceptor;
//    private static Gson gson;

    private static Retrofit retrofit;
    public static String NowUrl;
    public static boolean isChangeIp=false;
    private static RService mService;//本地retrofit方法

    public static int PDA_Choose;//{" 1 G02A设备","2 8000设备","3 5000设备"4 M60,"5手机端，6 h100};

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
        mContext = this;

        Hawk.init(mContext).build();
        PDA_Choose=Hawk.get(Config.PDA,1);
        NowUrl = BasicShareUtil.getInstance(mContext).getBaseURL();
        //retrofit的基本初始化相关
//        gson = new Gson();
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.connectTimeout(5000, TimeUnit.SECONDS);
//        builder.readTimeout(20, TimeUnit.SECONDS);
//        builder.writeTimeout(20, TimeUnit.SECONDS);
//        builder.retryOnConnectionFailure(true);
        interceptor = new okhttp3.logging.HttpLoggingInterceptor();
        interceptor.setLevel(okhttp3.logging.HttpLoggingInterceptor.Level.BODY);
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(5000, TimeUnit.SECONDS)
                .readTimeout(200, TimeUnit.SECONDS)
                .writeTimeout(200, TimeUnit.SECONDS)
                .build();

        //这里的baseurl,注意要有实际格式的链接，不然会崩
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BasicShareUtil.getInstance(mContext).getBaseURL())
                .build();
        mService = new RService();

    }

    public static Context getContext(){
        return mContext;
    }


    //获取Service对象，当ip发生变化时，更换Serivce对象
    public static RService getRService() {
        if (TextUtils.equals(BasicShareUtil.getInstance(App.getContext()).getBaseURL(),App.NowUrl)){
            isChangeIp=false;
            return mService;
        }else{
            isChangeIp=true;
            RService mSerivce = new RService();
            setRService(mSerivce);
            String changeUrl=BasicShareUtil.getInstance(App.getContext()).getBaseURL();
            App.NowUrl=changeUrl;
            return mSerivce;
        }
    }

    public static void setRService(RService mService) {
        App.mService = mService;
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    public static void setRetrofit(Retrofit retrofit) {
        App.retrofit = retrofit;
    }
    public static OkHttpClient getOkHttpClient(){
        return okHttpClient;
    }
}

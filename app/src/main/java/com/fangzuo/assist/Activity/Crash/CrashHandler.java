package com.fangzuo.assist.Activity.Crash;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.fangzuo.assist.Utils.Config;
import com.orhanobut.hawk.Hawk;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by pbids903 on 2017/12/7.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";
    private static final boolean DEBUG = true;
    //文件路径
    private static final String PATH = "C:\\Program Files\\Apache Software Foundation\\Tomcat 8.5\\webapps\\PdaLog";
    private static final String FILE_NAME = "crash";
    private static final String FILE_NAME_SUFEIX = ".txt";
    private static Thread.UncaughtExceptionHandler mDefaultCrashHandler;
    private static CrashHandler mCrashHandler = new CrashHandler();
    private Context mContext;

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return mCrashHandler;
    }

    public void init(Context context) {
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        mContext = context.getApplicationContext();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            //将文件写入sd卡
            writeToSDcard(ex);
            //写入后在这里可以进行上传操作
            PackageManager pm = mContext.getPackageManager();
            long currenttime = System.currentTimeMillis();
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(currenttime));
            StringBuilder stringBuilder=new StringBuilder()
                    .append(time+"---")
                    .append(ex.toString()+"\n")
                    .append(Hawk.get(Config.Text_Log,"")+"\n")
                    ;
            Hawk.put(Config.Text_Log,stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        ex.printStackTrace();
        //如果系统提供了默认异常处理就交给系统进行处理，否则自己进行处理。
        if (mDefaultCrashHandler != null) {
            mDefaultCrashHandler.uncaughtException(thread, ex);
        } else {
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    //将异常写入文件
    private void writeToSDcard(Throwable ex) throws IOException, PackageManager.NameNotFoundException {
        //如果没有SD卡，直接返回
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return;
        }
        File filedir = new File(PATH);
        if (!filedir.exists()) {
            filedir.mkdirs();
        }
        long currenttime = System.currentTimeMillis();
        String time = new SimpleDateFormat("yyyy-MM-ddHHmmss").format(new Date(currenttime));

        File exfile = new File(PATH +File.separator+FILE_NAME+time + FILE_NAME_SUFEIX);
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(exfile)));
        Log.e("错误日志文件路径",""+exfile.getAbsolutePath());
        pw.println(time);
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        //当前版本号
        pw.println("App Version:" + pi.versionName + "_" + pi.versionCode);
        //当前系统
        pw.println("OS version:" + Build.VERSION.RELEASE + "_" + Build.VERSION.SDK_INT);
        //制造商
        pw.println("Vendor:" + Build.MANUFACTURER);
        //手机型号
        pw.println("Model:" + Build.MODEL);
        //CPU架构
        pw.println("CPU ABI:" + Build.CPU_ABI);

        ex.printStackTrace(pw);
        //------------------------------崩溃时，上传数据到云端
        StringBuilder builder = new StringBuilder();
        builder.append(ex.toString()+"\n");
        StackTraceElement[] stes = ex.getStackTrace();
        for (int i = 0; i < stes.length; i ++) {
            builder.append(i + "->"  + stes[i].getClassName() + "-->" + stes[i].getMethodName() + "-->" + stes[i].getFileName()+"\n");
        }
        com.fangzuo.assist.Utils.Asynchttp.post(mContext, Config.Error_Url, Config.Company+"^"+"Crash"+"^"+builder.toString(), new com.fangzuo.assist.Utils.Asynchttp.Response() {
            @Override
            public void onSucceed(com.fangzuo.assist.Beans.CommonResponse cBean, com.loopj.android.http.AsyncHttpClient client) {

            }

            @Override
            public void onFailed(String Msg, com.loopj.android.http.AsyncHttpClient client) {
            }
        });//------------------------------------
        pw.close();

    }
}

package com.fangzuo.assist.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Utils.Asynchttp;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.WebApi;
import com.loopj.android.http.AsyncHttpClient;

/**
 * Created by 王璐阳 on 2017/9/11.
 */

public class SetLastUseTime extends Service {
    private Service mContext;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mContext = this;
        TimeThread timeThread = new TimeThread();
        timeThread.start();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
    private class TimeThread extends Thread{
        @Override
        public void run() {
            while(true){

                try {
                    Thread.sleep(50000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

    }
    public String getBaseUrl(){
        return BasicShareUtil.getInstance(this).getBaseURL();
    }

}

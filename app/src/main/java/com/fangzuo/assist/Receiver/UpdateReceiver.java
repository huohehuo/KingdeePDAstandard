package com.fangzuo.assist.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.fangzuo.assist.Activity.SplashActivity;
import com.fangzuo.assist.Utils.Lg;

/**
 * PDA更新程序时，自动启动程序
 */

public class UpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.PACKAGE_REPLACED")){
//            Lg.e("得到包名",intent.getDataString());
//            Lg.e("得到包名2",context.getPackageName());
            if (intent.getDataString().equals("package:"+context.getPackageName())){
                Lg.e("TAG","收到广播,包名一致");
                Intent intent2 = new Intent(context, SplashActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent2);
            }else {
                Lg.e("TAG","收到广播,包名不一致");
            }

        }
    }
}

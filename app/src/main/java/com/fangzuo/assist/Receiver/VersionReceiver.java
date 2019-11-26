package com.fangzuo.assist.Receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;


import com.fangzuo.assist.Activity.NoticeActivity;
import com.fangzuo.assist.Activity.PDActivity;
import com.fangzuo.assist.Activity.SplashActivity;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.CommonUtil;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.MathUtil;

import static android.content.Context.NOTIFICATION_SERVICE;

public class VersionReceiver extends BroadcastReceiver {
//    private String id="com.fangzuo.version";
    private String name="升级通知";
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Intent resultIntent = new Intent(context, NoticeActivity.class);
//        intent.putExtra("goto","NoticeActivity");
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = null;
        String rec = intent.getStringExtra("noticID");
        String billNo = intent.getStringExtra("billNo");
        String num = intent.getStringExtra("num");
        Lg.e("通知"+rec+billNo);
        Uri path = Uri.parse("android.resource://com.fangzuo.assist/"+R.raw.beep);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Lg.e("通知1111");
            NotificationChannel channel = new NotificationChannel(Config.VersionReceiver,name,NotificationManager.IMPORTANCE_LOW);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            mNotifyMgr.createNotificationChannel(channel);
            notification = new Notification.Builder(context)
                    .setChannelId(Config.VersionReceiver)
                    .setContentTitle("推送提醒")
                    .setSound(path)
                    .setTimeoutAfter(3600000)
                    .setAutoCancel(true)                           //设置点击后取消Notification
                    .setContentText("推送单据:"+billNo+",总数："+num)
                    .setContentIntent(resultPendingIntent)
                    .setSmallIcon(R.mipmap.logo).build();
        }else{
        Lg.e("通知2222");
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.mipmap.logo)
                            .setContentTitle("推送提醒")
                            .setTimeoutAfter(3600000)
                            .setSound(path)
                            .setAutoCancel(true)                           //设置点击后取消Notification
                            .setContentIntent(resultPendingIntent)
                            .setContentText("推送单据:"+billNo+",总数："+num);
//            mBuilder.setContentIntent(resultPendingIntent);
            notification = mBuilder.build();
        }

        mNotifyMgr.notify(MathUtil.toInt(rec), notification);
//        Toast.showText(context,"广播接收到信息"+intent.getStringExtra("version"));
    }
}

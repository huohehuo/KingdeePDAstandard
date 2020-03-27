package com.fangzuo.assist.Utils;

import android.annotation.SuppressLint;
import android.util.Log;

import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.UseTimeBean;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.widget.LoadingUtil;
import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Administrator on 2019/11/26.
 * 控制类
 */

public class ControlUtil {


    public static final String GetUseTime = "GetUseTime";
    public static final String ServiceVersion = "ServiceVersion";
    public static String SaveTime="SaveTime";//用于保存使用截止日期
    public static String Key="01235679";//用于保存使用截止日期（需要web端的key与之相同,并且不能倒序，只能递增的数字）

    //获取配置文件中的时间数据
    public static void DownLoadUseTime() {
        App.getRService().doIOAction(GetUseTime, "获取时间", new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                super.onNext(commonResponse);
                LoadingUtil.dismiss();
                if (!commonResponse.state) return;
                UseTimeBean bean = new Gson().fromJson(commonResponse.returnJson, UseTimeBean.class);
                if (Integer.parseInt(CommonUtil.getTime(false)) < Integer.parseInt(bean.nowTime)) {
                    Toast.showText(App.getContext(), "PDA本地时间与服务器时间有误，请调整好时间");
                    Hawk.put(SaveTime, bean);
                    return;
                } else {
                    if (Integer.parseInt(CommonUtil.getTime(false)) > Integer.parseInt(dealTime(bean.endTime))) {
                        Toast.showText(App.getContext(), "软件已过期，请联系供应商提供服务");
                        Hawk.put(SaveTime, bean);
                        return;
                    } else {
                        Lg.e("获取起止时间：" + commonResponse.returnJson);
                        Hawk.put(SaveTime, bean);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
//                super.onError(e);
//                LoadingUtil.dismiss();
//                    Hawk.put(Config.SaveTime,null);
//                Toast.showText(App.getContext(), e.toString());
                Lg.e("错误：" + e.toString());
            }
        });
    }

    //检测是否符合时间要求（本地，如果不符，则取请求网络）
    public static boolean checkTime() {
        if (null == Hawk.get(SaveTime, null)) {
            LoadingUtil.showDialog(App.getContext(), "正在获取配置信息...");
            ControlUtil.DownLoadUseTime();
            return false;
        } else {
            UseTimeBean bean = Hawk.get(SaveTime);
            if (Integer.parseInt(getTime(false)) < Integer.parseInt(bean.nowTime)) {
                Toast.showText(App.getContext(), "PDA本地时间与服务器时间有误，请调整好时间");
                return false;
            } else {
                if (Integer.parseInt(getTime(false)) > Integer.parseInt(dealTime(bean.endTime))) {
                    Toast.showText(App.getContext(), "软件已过期，请联系供应商提供服务");
                    return false;
                } else {
                    return true;
                }
            }
        }
    }
    public static String getTime(boolean b) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat(b ? "yyyy-MM-dd" : "yyyyMMdd");
        Date curDate = new Date();
        Log.e("date", curDate.toString());
        String str = format.format(curDate);
        return str;
    }
    //解密加密的时间
    public static String dealTime(String timemd){
//        Lg.e("加密的日期："+timemd);
        StringBuffer buffer = new StringBuffer()
                .append(timemd.charAt(Integer.parseInt(Key.charAt(0)+"")+1))
                .append(timemd.charAt(Integer.parseInt(Key.charAt(1)+"")+2))
                .append(timemd.charAt(Integer.parseInt(Key.charAt(2)+"")+3))
                .append(timemd.charAt(Integer.parseInt(Key.charAt(3)+"")+4))
                .append(timemd.charAt(Integer.parseInt(Key.charAt(4)+"")+5))
                .append(timemd.charAt(Integer.parseInt(Key.charAt(5)+"")+6))
                .append(timemd.charAt(Integer.parseInt(Key.charAt(6)+"")+7))
                .append(timemd.charAt(Integer.parseInt(Key.charAt(7)+"")+8));
//        Lg.e("解析日期："+buffer.toString());
        return buffer.toString();
    }
}

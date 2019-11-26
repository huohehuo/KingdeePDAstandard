package com.fangzuo.assist.Utils;

import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.UseTimeBean;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.widget.LoadingUtil;
import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;

import static com.fangzuo.assist.Utils.CommonUtil.dealTime;

/**
 * Created by Administrator on 2019/11/26.
 * 控制类
 */

public class ControlUtil {



    //获取配置文件中的时间数据
    public static void DownLoadUseTime() {
        App.getRService().doIOAction(WebApi.GetUseTime, "获取时间", new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                super.onNext(commonResponse);
                LoadingUtil.dismiss();
                if (!commonResponse.state) return;
                UseTimeBean bean = new Gson().fromJson(commonResponse.returnJson, UseTimeBean.class);
                if (Integer.parseInt(CommonUtil.getTime(false)) < Integer.parseInt(bean.nowTime)) {
                    Toast.showText(App.getContext(), "PDA本地时间与服务器时间有误，请调整好时间");
                    Hawk.put(Config.SaveTime, bean);
                    return;
                } else {
                    if (Integer.parseInt(CommonUtil.getTime(false)) > Integer.parseInt(dealTime(bean.endTime))) {
                        Toast.showText(App.getContext(), "软件已过期，请联系供应商提供服务");
                        Hawk.put(Config.SaveTime, bean);
                        return;
                    } else {
                        Lg.e("获取起止时间：" + commonResponse.returnJson);
                        Hawk.put(Config.SaveTime, bean);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                LoadingUtil.dismiss();
//                    Hawk.put(Config.SaveTime,null);
                Toast.showText(App.getContext(), e.toString());
                Lg.e("错误：" + e.toString());
            }
        });
    }



}

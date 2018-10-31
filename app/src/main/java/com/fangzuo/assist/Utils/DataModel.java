package com.fangzuo.assist.Utils;

import android.content.Context;

import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.greendao.gen.T_DetailDao;
import com.loopj.android.http.AsyncHttpClient;

public class DataModel {
    //检测点击回单时是否存在单据
    public static boolean checkHasDetail(Context mContext,int activity){
        return GreenDaoManager.getmInstance(mContext).getDaoSession().getT_DetailDao().queryBuilder().where(
                T_DetailDao.Properties.Activity.eq(activity)).build().list().size()>0;
    }

    public static void upload(Context context,String url,String json){
        Asynchttp.post(context, url, json, new Asynchttp.Response() {
            @Override
            public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Upload_OK,""));
            }

            @Override
            public void onFailed(String Msg, AsyncHttpClient client) {
                EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Upload_Error,Msg));
            }
        });
    }

}

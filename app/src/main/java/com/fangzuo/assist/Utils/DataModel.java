package com.fangzuo.assist.Utils;

import android.content.Context;
import android.database.Cursor;
import android.widget.TextView;

import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.ConnectResponseBean;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.Beans.InStoreNumBean;
import com.fangzuo.assist.Dao.InStorageNum;
import com.fangzuo.assist.Dao.Product;
import com.fangzuo.assist.Dao.Storage;
import com.fangzuo.assist.Dao.T_Detail;
import com.fangzuo.assist.Dao.T_main;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.widget.LoadingUtil;
import com.fangzuo.greendao.gen.T_DetailDao;
import com.fangzuo.greendao.gen.T_mainDao;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;

import java.util.ArrayList;
import java.util.List;

public class DataModel {
    //检测点击回单时是否存在单据
    public static boolean checkHasDetail(Context mContext,int activity){
        return GreenDaoManager.getmInstance(mContext).getDaoSession().getT_DetailDao().queryBuilder().where(
                T_DetailDao.Properties.Activity.eq(activity)).build().list().size()>0;
    }

    //统一回单数据请求
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

    //统一回单数据请求
    public static void upload(String url,String json){
        App.getRService().doIOAction(url, json, new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                super.onNext(commonResponse);
                if (commonResponse.state){
                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Upload_OK,""));
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Upload_Error,e.toString()));
            }
        });
    }




    //下推时，统一回单数据请求
    public static long findOrderCode(Context context, int activity, ArrayList<String> fidcontainer){
        String con="";
        for (String str:fidcontainer) {
            con= con+str+",";
        }
        if (con.length() > 0) {
            con = con.subSequence(0, con.length() - 1).toString();
        }
        ArrayList<T_main> mainTips = new ArrayList<>();
        String SQL = "SELECT ORDER_ID,FINDEX,FDELIVERY_TYPE FROM T_MAIN WHERE ACTIVITY=? AND FDELIVERY_TYPE IN ("+con+")";
        Lg.e("SQL:"+SQL);
        Cursor cursor = GreenDaoManager.getmInstance(context).getDaoSession().getDatabase().rawQuery(SQL, new String[]{activity + ""});
        while (cursor.moveToNext()) {
            T_main f = new T_main();
            f.FIndex = cursor.getString(cursor.getColumnIndex("FINDEX"));
            f.orderId = cursor.getLong(cursor.getColumnIndex("ORDER_ID"));
            f.FDeliveryType = cursor.getString(cursor.getColumnIndex("FDELIVERY_TYPE"));
            mainTips.add(f);
        }
        if (mainTips.size()>0){
            if (mainTips.size()==1){
                return mainTips.get(0).orderId;
            }else{
                long ordercode = System.currentTimeMillis();
                for (int i = 0; i < mainTips.size(); i++) {
                    //重新查找并更新，不适用上面的查找数据，不然会被清空，无法更新
                    List<T_main> mains = GreenDaoManager.getmInstance(context).getDaoSession().getT_mainDao().queryBuilder().where(
                            T_mainDao.Properties.Activity.eq(activity),
                            T_mainDao.Properties.OrderId.eq(mainTips.get(i).orderId)
                    ).build().list();
                    List<T_Detail> t_details = GreenDaoManager.getmInstance(context).getDaoSession().getT_DetailDao().queryBuilder().where(
                            T_DetailDao.Properties.FOrderId.eq(mainTips.get(i).orderId)
                    ).build().list();
                    for (T_Detail bean:t_details) {
                        bean.FOrderId = ordercode;
                        GreenDaoManager.getmInstance(context).getDaoSession().getT_DetailDao().update(bean);
                    }
                    for (T_main bean:mains) {
                        bean.orderId = ordercode;
                        GreenDaoManager.getmInstance(context).getDaoSession().getT_mainDao().update(bean);
                        Lg.e("更新main："+bean.toString());
                    }

                }
                return ordercode;
            }
        }else{
            return System.currentTimeMillis();
        }

    }


    //下载配置的连接
    public static void SetConnectSQL(String json){
        App.getRService().connectToSQL(json, new MySubscribe<CommonResponse>() {
                    @Override
                    public void onNext(CommonResponse commonResponse) {
                        super.onNext(commonResponse);
                        EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Connect_OK,commonResponse));
                    }

                    @Override
                    public void onError(Throwable e) {
                        EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Connect_Error,e.toString()));
                    }
                });
    }
    //下载配置的配置
    public static void SetProp(String json){
        App.getRService().SetProp(json, new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Prop_OK,commonResponse));
            }

            @Override
            public void onError(Throwable e) {
                EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Prop_Error,e.toString()));
            }
        });
    }

    //获取库存
    public static void getStoreNum(Product product, Storage storage, String wavehouse,String batch, Context mContext, final TextView textView){
        if (product == null || storage == null){
            return;
        }
        if (BasicShareUtil.getInstance(mContext).getIsOL()) {
            InStoreNumBean storageNum = new InStoreNumBean(product.FItemID,storage.FItemID,wavehouse,batch);
            App.getRService().doIOAction(WebApi.GETINSTORENUM, new Gson().toJson(storageNum), new MySubscribe<CommonResponse>() {
                @Override
                public void onNext(CommonResponse commonResponse) {
                    super.onNext(commonResponse);
                    if (!commonResponse.state)return;
                    textView.setText(commonResponse.returnJson);
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    textView.setText("0");
                }
            });
        }else{
            List<InStorageNum> container = new ArrayList<>();
            String con="";
            if (!"".equals(storage.FItemID)){
                con+=" and FSTOCK_ID='"+storage.FItemID+"'";
            }
            if (!"".equals(product.FItemID)){
                con+=" and FITEM_ID='"+product.FItemID+"'";
            }
            if (!"".equals(batch)){
                con+=" and FBATCH_NO='"+batch+"'";
            }
            String SQL = "SELECT * FROM IN_STORAGE_NUM WHERE 1=1 "+con;
            Lg.e("库存查询SQL:"+SQL);
            Cursor cursor = GreenDaoManager.getmInstance(mContext).getDaoSession().getDatabase().rawQuery(SQL, null);
            while (cursor.moveToNext()) {
                InStorageNum f = new InStorageNum();
                f.FQty = cursor.getString(cursor.getColumnIndex("FQTY"));
                Lg.e("库存查询存在FQty："+f.FQty);
                container.add(f);
            }
            if (container.size() > 0) {
                textView.setText(container.get(0).FQty);
            } else {
                textView.setText("0");
            }
        }
    }


}

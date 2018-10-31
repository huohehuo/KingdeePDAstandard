package com.fangzuo.assist.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.fangzuo.assist.Adapter.PiciSpAdapter;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Beans.GetBatchNoBean;
import com.fangzuo.assist.Dao.InStorageNum;
import com.fangzuo.assist.Dao.Product;
import com.fangzuo.assist.Dao.Storage;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.Asynchttp;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.InStorageNumDao;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;

import java.util.ArrayList;
import java.util.List;


public class SpinnerPici extends RelativeLayout {
    // 返回按钮控件
      private Spinner mSp;
      // 标题Tv
      private TextView mTitleTv;
    private SpinnerAdapter adapter;
    private PiciSpAdapter piciSpAdapter;
    private Context mContext;;
    private List<InStorageNum> inStorageNumList;
    private String autoString;//用于联网时，再次去自动设置值
    private DaoSession daoSession;
    private static BasicShareUtil share;

    public SpinnerPici(Context context, AttributeSet attributeSet) {
        super(context,attributeSet);
        mContext=context;
        LayoutInflater.from(context).inflate(R.layout.view_my_people_spinner,this);
        inStorageNumList = new ArrayList<>();
        daoSession = GreenDaoManager.getmInstance(context).getDaoSession();
        share = BasicShareUtil.getInstance(context);

        piciSpAdapter = new PiciSpAdapter(mContext, inStorageNumList);
        // 获取控件
        mSp = (Spinner) findViewById(R.id.sp);
        mSp.setAdapter(piciSpAdapter);
    }

    // 为左侧返回按钮添加自定义点击事件
    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener listener) {
        mSp.setOnItemSelectedListener(listener);
    }
    public void setAdapter(SpinnerAdapter adapter){
        mSp.setAdapter(adapter);
        this.adapter = adapter;
    }
    public void setSelection(int i){
        mSp.setSelection(i);
    }
    public PiciSpAdapter getAdapter(){
        return piciSpAdapter;
    }
    public void setEnabled(boolean enabled){
        mSp.setEnabled(enabled);
    }

    //自动设置保存的值（非下推的方法）
    public void setAuto(Storage storage, String wavehouse, Product product, String autoStr) {
        Lg.e("pici:自动设置："+autoStr);
        autoString = autoStr;
        inStorageNumList.clear();
        if (storage==null || product==null){
            Lg.e("pici:仓库或物料为空");
            return;
        }
        if (share.getIsOL()){
            GetBatchNoBean bean = new GetBatchNoBean();
            bean.ProductID=product.FItemID;
            bean.StorageID=storage.FItemID;
            bean.WaveHouseID=wavehouse==null?"0":wavehouse;
            String json = new Gson().toJson(bean);
            Asynchttp.post(mContext, share.getBaseURL() + WebApi.GETPICI, json, new Asynchttp.Response() {
                @Override
                public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                    DownloadReturnBean dBean = new Gson().fromJson(cBean.returnJson, DownloadReturnBean.class);
                    if(dBean.InstorageNum!=null){
                        for (int i = 0; i < dBean.InstorageNum.size(); i++) {
                            if (dBean.InstorageNum.get(i).FQty != null
                                    && Double.parseDouble(dBean.InstorageNum.get(i).FQty) > 0) {
                                inStorageNumList.add(dBean.InstorageNum.get(i));
                            }
                        }
                    }
                    piciSpAdapter.notifyDataSetChanged();
                    for (int j = 0; j < piciSpAdapter.getCount(); j++) {
                        if (((InStorageNum) piciSpAdapter.getItem(j)).FBatchNo.equals(autoString)) {
                            mSp.setSelection(j);
                            break;
                        }
                    }
                }

                @Override
                public void onFailed(String Msg, AsyncHttpClient client) {
//                    Toast.showText(mContext, "批次："+Msg);
                }
            });
        }else{
            InStorageNumDao inStorageNumDao = daoSession.getInStorageNumDao();
            List<InStorageNum> list = inStorageNumDao.queryBuilder().where(
                    InStorageNumDao.Properties.FStockPlaceID.eq(wavehouse==null?"0":wavehouse),
                    InStorageNumDao.Properties.FStockID.eq(storage.FItemID),
                    InStorageNumDao.Properties.FItemID.eq(product.FItemID),
                    InStorageNumDao.Properties.FQty.gt(0)
            ).build().list();
            for (int i = 0;i<list.size();i++){
                if(list.get(i).FQty!= null&&Double.parseDouble(list.get(i).FQty)>0){
                    inStorageNumList.add(list.get(i));
                }
            }
            piciSpAdapter.notifyDataSetChanged();
            for (int j = 0; j < piciSpAdapter.getCount(); j++) {
                if (((InStorageNum) piciSpAdapter.getItem(j)).FBatchNo.equals(autoString)) {
                    mSp.setSelection(j);
                    break;
                }
            }
        }

    }


    //自动设置保存的值(下推时的方法)
    public void setAuto(String storageId, String wavehouse, String subId, String autoStr) {
        Lg.e("pici:自动设置："+autoStr);
        autoString = autoStr;
        inStorageNumList.clear();
        if (share.getIsOL()){
            GetBatchNoBean bean = new GetBatchNoBean();
            bean.ProductID=subId;
            bean.StorageID=storageId;
            bean.WaveHouseID=wavehouse==null?"0":wavehouse;
            String json = new Gson().toJson(bean);
            Asynchttp.post(mContext, share.getBaseURL() + WebApi.GETPICI, json, new Asynchttp.Response() {
                @Override
                public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                    DownloadReturnBean dBean = new Gson().fromJson(cBean.returnJson, DownloadReturnBean.class);
                    if(dBean.InstorageNum!=null){
                        for (int i = 0; i < dBean.InstorageNum.size(); i++) {
                            if (dBean.InstorageNum.get(i).FQty != null
                                    && Double.parseDouble(dBean.InstorageNum.get(i).FQty) > 0) {
                                inStorageNumList.add(dBean.InstorageNum.get(i));
                            }
                        }
                    }
                    piciSpAdapter.notifyDataSetChanged();
                    for (int j = 0; j < piciSpAdapter.getCount(); j++) {
                        if (((InStorageNum) piciSpAdapter.getItem(j)).FBatchNo.equals(autoString)) {
                            mSp.setSelection(j);
                            break;
                        }
                    }
                }

                @Override
                public void onFailed(String Msg, AsyncHttpClient client) {
                    Toast.showText(mContext, Msg);
                }
            });
        }else{
            InStorageNumDao inStorageNumDao = daoSession.getInStorageNumDao();
            List<InStorageNum> inStorageNa = inStorageNumDao.queryBuilder().where(
                    InStorageNumDao.Properties.FStockID.eq(storageId),
                    InStorageNumDao.Properties.FStockPlaceID.eq(wavehouse==null?"0":wavehouse),
                    InStorageNumDao.Properties.FItemID.eq(subId)
            ).build().list();
            if (inStorageNa.size() > 0) {
                inStorageNumList.addAll(inStorageNa);
                piciSpAdapter.notifyDataSetChanged();
                for (int j = 0; j < piciSpAdapter.getCount(); j++) {
                    if (((InStorageNum) piciSpAdapter.getItem(j)).FBatchNo.equals(autoString)) {
                        mSp.setSelection(j);
                        break;
                    }
                }
            }
        }

    }



    public void clear(){
        inStorageNumList.clear();
        piciSpAdapter.notifyDataSetChanged();
    }

}

package com.fangzuo.assist.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Adapter.WaveHouseSpAdapter;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.Dao.Storage;
import com.fangzuo.assist.Dao.WaveHouse;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Utils.Asynchttp;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.EventBusUtil;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.JsonCreater;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.WaveHouseDao;
import com.loopj.android.http.AsyncHttpClient;

import java.util.ArrayList;
import java.util.List;


public class SpinnerWaveHouse extends RelativeLayout {
    public static String Number="number";
    public static String Name="name";
    public static String ID="id";
    // 返回按钮控件
    private Spinner mSp;
    // 标题Tv
    private TextView mTitleTv;
    private static BasicShareUtil share;
    private String autoString;//用于联网时，再次去自动设置值
    private WaveHouseSpAdapter waveHouseSpAdapter;
    private ArrayList<WaveHouse> waveHouses;
    private DaoSession daoSession;
    private String waveHouseId = "";
    private String waveHouseName = "";
    private String waveHouseNumber = "";

    public SpinnerWaveHouse(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        LayoutInflater.from(context).inflate(R.layout.view_my_wave_spinner, this);
        daoSession = GreenDaoManager.getmInstance(context).getDaoSession();
        waveHouses = new ArrayList<>();
        waveHouseSpAdapter = new WaveHouseSpAdapter(context, waveHouses);
        share = BasicShareUtil.getInstance(context);
        // 获取控件
        mSp = (Spinner) findViewById(R.id.sp);
        mTitleTv = (TextView) findViewById(R.id.tv);
        TypedArray attrArray = context.obtainStyledAttributes(attributeSet, R.styleable.SpinnerWaveHouse);
        int count = attrArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attrName = attrArray.getIndex(i);
            switch (attrName) {
                case R.styleable.SpinnerWaveHouse_wspinner_name:
                    mTitleTv.setText(attrArray.getString(R.styleable.SpinnerWaveHouse_wspinner_name));
                    break;
                case R.styleable.SpinnerWaveHouse_wspinner_focusable:
                    mSp.setEnabled(attrArray.getBoolean(R.styleable.SpinnerWaveHouse_wspinner_focusable, true));
                    break;
                case R.styleable.SpinnerWaveHouse_wspinner_name_size:
                    mTitleTv.setText(attrArray.getString(R.styleable.SpinnerWaveHouse_wspinner_name));
                    mTitleTv.setTextSize(attrArray.getDimension(R.styleable.SpinnerWaveHouse_wspinner_name_size, 10));
                    break;
            }
        }
        attrArray.recycle();

        mSp.setAdapter(waveHouseSpAdapter);

//        mSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                WaveHouse waveHouse = (WaveHouse) waveHouseSpAdapter.getItem(i);
//                wavehouseID = waveHouse.FSPID;
//                wavehouseName = waveHouse.FName;
//                Log.e("wavehouseName", wavehouseName);
//                getInstorageNum(product);
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

    }

    // 为左侧返回按钮添加自定义点击事件
    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener listener) {
        mSp.setOnItemSelectedListener(listener);
    }

    public void setAdapter(WaveHouseSpAdapter adapter) {
        mSp.setAdapter(adapter);
        this.waveHouseSpAdapter = adapter;
    }

    public void setEnabled(boolean b) {
        mSp.setEnabled(b);
    }

    public String getWaveHouseId() {
        return waveHouseId;
    }
    public void clear() {
        waveHouseId = "0";
        waveHouseName = "";
        waveHouseNumber = "";
        waveHouses.clear();
        waveHouseSpAdapter.notifyDataSetChanged();
    }
    public String getWaveHouseName() {
        return waveHouseName;
    }

    public String getWaveHouseNumber() {
        return waveHouseNumber;
    }

    public WaveHouseSpAdapter getAdapter() {
        return waveHouseSpAdapter;
    }

    public void setSelection(int i) {
        mSp.setSelection(i);
    }

    // 设置标题的方法
    public void setTitleText(String title) {
        mTitleTv.setText(title);
    }

    //自动设置保存的值
    //type: 根据什么字段定位：number，id，name
    public void setAuto(final Context context, final Storage storage, String autoStr, final String type) {
        waveHouseId = "0";
        waveHouseName = "";
        waveHouseNumber = "";
        if (storage == null) {
            return;
        }
        Lg.e("setAuto:" + autoStr);
//        LoadingUtil.dismiss();
//        LoadingUtil.show(context, "请稍等...");
        autoString = autoStr;
        List<WaveHouse> list = getLocData(storage);
        dealAuto(list,storage, type,false);

//        waveHouses.clear();
//        if ("".equals(spid)||"0".equals(spid)){
//            WaveHouseDao wavehousedao = daoSession.getWaveHouseDao();
//            List<WaveHouse> waveHouseList = wavehousedao.queryBuilder().where(
//                    WaveHouseDao.Properties.FSPGroupID.eq(storage.FSPGroupID)
//            ).build().list();
////            waveHouseList.add(new WaveHouse());
//            if (waveHouseList.size()>0){
//                waveHouses.addAll(waveHouseList);
////                Log.e("CommonMethod:","获取到本地数据waveHouse:"+waveHouseList.toString());
//                waveHouseSpAdapter.notifyDataSetChanged();
//                for (int j = 0; j < waveHouseSpAdapter.getCount(); j++) {
//                    if (((WaveHouse) waveHouseSpAdapter.getItem(j)).FSPID.equals(autoString)) {
//                        waveHouseId=autoString;
//                        waveHouseName=((WaveHouse) waveHouseSpAdapter.getItem(j)).FName;
//                        mSp.setSelection(j);
//                        break;
//                    }
//                }
//                EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Ok_WaveHouse,""));
//            }else{
//                EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Ok_WaveHouse,""));
//                waveHouseSpAdapter.notifyDataSetChanged();
////                Log.e("CommonMethod:","无数据：waveHouse:");
//            }
//            LoadingUtil.dismiss();
//            return;
//        }
        if (share.getIsOL()) {
            Log.e("CommonMethod:", "getWaveHouseAdapter联网");
            ArrayList<Integer> choose = new ArrayList<>();
            choose.add(4);
            String json = JsonCreater.DownLoadData(
                    share.getDatabaseIp(),
                    share.getDatabasePort(),
                    share.getDataBaseUser(),
                    share.getDataBasePass(),
                    share.getDataBase(),
                    share.getVersion(),
                    choose
            );
            App.getRService().doIOAction(WebApi.DOWNLOADDATA, json, new MySubscribe<CommonResponse>() {
                @Override
                public void onNext(CommonResponse cBean) {
                    super.onNext(cBean);
//                    Log.e("CommonMethod:","getWaveHouseAdapter获得数据：\n"+cBean.returnJson);
                    DownloadReturnBean dBean = JsonCreater.gson.fromJson(cBean.returnJson, DownloadReturnBean.class);
                    WaveHouseDao wavehouseDao = daoSession.getWaveHouseDao();
                    wavehouseDao.deleteAll();
//                    for (int i = 0; i < dBean.wavehouse.size(); i++) {
//                        if (storage.FSPGroupID.equals(dBean.wavehouse.get(i).FSPGroupID)){
//                            waveHouses.add(dBean.wavehouse.get(i));
//                        }
//                    }
//                    waveHouseSpAdapter.notifyDataSetChanged();
                    wavehouseDao.insertOrReplaceInTx(dBean.wavehouse);
                    wavehouseDao.detachAll();
                    if (dBean.wavehouse.size()>0 && waveHouses.size() <= 0) {
                        waveHouses.addAll(dBean.wavehouse);
                        dealAuto(waveHouses,storage, type,true);
                    }
                }

                @Override
                public void onError(Throwable e) {
//                    super.onError(e);
                }
            });
//            Asynchttp.post(context, share.getBaseURL() + WebApi.DOWNLOADDATA, json, new Asynchttp.Response() {
//                @Override
//                public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
//
//
////                    if ("".equals(autoString)||"0".equals(autoString)){
//////                        waveHouses.addAll(dBean.wavehouse);
//////                        waveHouses.clear();
////                        waveHouseSpAdapter.notifyDataSetChanged();
////                        EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.check_Storage_waveHouse,autoString));
////
////                    }else{
////                        if ("number".equals(type)){
////                            for (int j = 0; j < waveHouseSpAdapter.getCount(); j++) {
////                                if (((WaveHouse) waveHouseSpAdapter.getItem(j)).FNumber.equals(autoString)) {
////                                    Lg.e("仓位定位（自定义控件number："+autoString);
//////                                    waveHouseId=autoString;
////                                    waveHouseName=((WaveHouse) waveHouseSpAdapter.getItem(j)).FName;
////                                    waveHouseNumber=((WaveHouse) waveHouseSpAdapter.getItem(j)).FNumber;
////                                    mSp.setSelection(j);
////                                    break;
////                                }
////                            }
////                            EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.check_Storage_waveHouse,""));
////                        }else if ("id".equals(type)){
////                            for (int j = 0; j < waveHouseSpAdapter.getCount(); j++) {
////                                if (((WaveHouse) waveHouseSpAdapter.getItem(j)).FSPID.equals(autoString)) {
////                                    Lg.e("仓位定位（自定义控件id："+autoString);
////                                    waveHouseId=autoString;
////                                    waveHouseName=((WaveHouse) waveHouseSpAdapter.getItem(j)).FName;
////                                    waveHouseNumber=((WaveHouse) waveHouseSpAdapter.getItem(j)).FNumber;
////                                    mSp.setSelection(j);
////                                    break;
////                                }
////                            }
////
////                        }else if ("name".equals(type)){
////                            for (int j = 0; j < waveHouseSpAdapter.getCount(); j++) {
////                                if (((WaveHouse) waveHouseSpAdapter.getItem(j)).FName.equals(autoString)) {
////                                    Lg.e("仓位定位（自定义控件name："+autoString);
//////                                    waveHouseId=autoString;
////                                    waveHouseName=((WaveHouse) waveHouseSpAdapter.getItem(j)).FName;
////                                    mSp.setSelection(j);
////                                    break;
////                                }
////                            }
////                        }
////
////                    }
////
////                    LoadingUtil.dismiss();
//                }
//
//                @Override
//                public void onFailed(String Msg, AsyncHttpClient client) {
//                    Lg.e("仓位下载失败");
////                    LoadingUtil.dismiss();
////                    waveHouseSpAdapter.notifyDataSetChanged();
////                    Toast.showText(context, Msg);
//                }
//            });
        }
//        else{
//            Log.e("CommonMethod:","getWaveHouseAdapter-不-联网");
//            WaveHouseDao wavehousedao = daoSession.getWaveHouseDao();
//            List<WaveHouse> waveHouseList = wavehousedao.queryBuilder().where(
//                    WaveHouseDao.Properties.FSPGroupID.eq(storage.FSPGroupID)
//            ).build().list();
//            waveHouseList.add(new WaveHouse());
//            if (waveHouseList.size()>0){
//                waveHouses.addAll(waveHouseList);
//                if ("".equals(autoString)||"0".equals(autoString)){
////                        waveHouses.addAll(dBean.wavehouse);
////                        waveHouses.clear();
//                        waveHouseSpAdapter.notifyDataSetChanged();
//                        EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.check_Storage_waveHouse,autoString));
//                    }else{
//                        if ("number".equals(type)){
//                            for (int j = 0; j < waveHouseSpAdapter.getCount(); j++) {
//                                if (((WaveHouse) waveHouseSpAdapter.getItem(j)).FNumber.equals(autoString)) {
//                                    Lg.e("仓位定位（自定义控件number："+autoString);
////                                    waveHouseId=autoString;
//                                    waveHouseName=((WaveHouse) waveHouseSpAdapter.getItem(j)).FName;
//                                    waveHouseNumber=((WaveHouse) waveHouseSpAdapter.getItem(j)).FNumber;
//                                    mSp.setSelection(j);
//                                    break;
//                                }
//                            }
//                            EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.check_Storage_waveHouse,""));
//                        }else if ("id".equals(type)){
//                            for (int j = 0; j < waveHouseSpAdapter.getCount(); j++) {
//                                if (((WaveHouse) waveHouseSpAdapter.getItem(j)).FSPID.equals(autoString)) {
//                                    Lg.e("仓位定位（自定义控件id："+autoString);
//                                    waveHouseId=autoString;
//                                    waveHouseName=((WaveHouse) waveHouseSpAdapter.getItem(j)).FName;
//                                    waveHouseNumber=((WaveHouse) waveHouseSpAdapter.getItem(j)).FNumber;
//                                    mSp.setSelection(j);
//                                    break;
//                                }
//                            }
//
//                        }else if ("name".equals(type)){
//                            for (int j = 0; j < waveHouseSpAdapter.getCount(); j++) {
//                                if (((WaveHouse) waveHouseSpAdapter.getItem(j)).FName.equals(autoString)) {
//                                    Lg.e("仓位定位（自定义控件name："+autoString);
////                                    waveHouseId=autoString;
//                                    waveHouseName=((WaveHouse) waveHouseSpAdapter.getItem(j)).FName;
//                                    mSp.setSelection(j);
//                                    break;
//                                }
//                            }
//                        }
//
//                    }
//
//                waveHouseSpAdapter.notifyDataSetChanged();
//                LoadingUtil.dismiss();
//                for (int j = 0; j < waveHouseSpAdapter.getCount(); j++) {
//                    if (((WaveHouse) waveHouseSpAdapter.getItem(j)).FNumber.equals(autoString)) {
//                        mSp.setSelection(j);
//                        break;
//                    }
//                }
//            }else{
//                waveHouseSpAdapter.notifyDataSetChanged();
//
////                Log.e("CommonMethod:","无数据：waveHouse:");
//            }
//        }

    }

    //根据仓库，过滤出仓位
    private List<WaveHouse> getLocData(Storage storage) {
        WaveHouseDao wavehousedao = daoSession.getWaveHouseDao();
        return wavehousedao.queryBuilder().where(
                WaveHouseDao.Properties.FSPGroupID.eq(storage.FSPGroupID)
        ).build().list();
    }

    //匹配自动值
    private void dealAuto(List<WaveHouse> list, Storage storage, final String type, boolean check) {
        waveHouses.clear();
        if (check) {//针对网络获取时，过滤
            for (int i = 0; i < list.size(); i++) {
                if (storage.FSPGroupID.equals(list.get(i).FSPGroupID)) {
                    waveHouses.add(list.get(i));
                }
            }
        }else{
            waveHouses.addAll(list);
        }
        if (waveHouses.size() > 0) {
                mSp.setAdapter(waveHouseSpAdapter);
                waveHouseSpAdapter.notifyDataSetChanged();
            if (null==autoString || "".equals(autoString) || "0".equals(autoString)) {
                if (list.size()>0){
                    waveHouseId = list.get(0).FSPID;
                    waveHouseName = list.get(0).FName;
                    waveHouseNumber = list.get(0).FNumber;
                }
                LoadingUtil.dismiss();
//                EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.check_Storage_waveHouse, autoString));
            } else {
                if ("number".equals(type)) {
                    for (int j = 0; j < waveHouses.size(); j++) {
                        if (waveHouses.get(j).FNumber.equals(autoString)) {
                            Lg.e("仓位定位（自定义控件number：" + autoString);
                            waveHouseId=waveHouses.get(j).FSPID;
                            waveHouseName = waveHouses.get(j).FName;
                            waveHouseNumber = waveHouses.get(j).FNumber;
                            mSp.setSelection(j);
                            break;
                        }
                    }
//                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.check_Storage_waveHouse, ""));
                } else if ("id".equals(type)) {
                    for (int j = 0; j < waveHouses.size(); j++) {
                        if (waveHouses.get(j).FSPID.equals(autoString)) {
                            Lg.e("仓位定位（自定义控件id：" + autoString);
                            waveHouseId = autoString;
                            waveHouseName = waveHouses.get(j).FName;
                            waveHouseNumber = waveHouses.get(j).FNumber;
                            mSp.setSelection(j);
                            break;
                        }
                    }

                } else if ("name".equals(type)) {
                    for (int j = 0; j < waveHouses.size(); j++) {
                        if (waveHouses.get(j).FName.equals(autoString)) {
                            Lg.e("仓位定位（自定义控件name：" + autoString);
                            waveHouseId=waveHouses.get(j).FSPID;
                            waveHouseName = waveHouses.get(j).FName;
                            waveHouseNumber = waveHouses.get(j).FNumber;
                            mSp.setSelection(j);
                            break;
                        }
                    }
                }
            }
            LoadingUtil.dismiss();
            waveHouseSpAdapter.notifyDataSetChanged();
        } else {
            LoadingUtil.dismiss();
            waveHouseSpAdapter.notifyDataSetChanged();
        }
    }

}

package com.fangzuo.assist.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.EmployeeDao;
import com.fangzuo.greendao.gen.WaveHouseDao;
import com.loopj.android.http.AsyncHttpClient;

import java.util.ArrayList;
import java.util.List;

/*<declare-styleable name="MyWaveSpinner">
        <attr name="spinner_name" format="string"/>
        <attr name="spinner_name_size" format="dimension"/>
    </declare-styleable>

     Activity activity= (Activity) mContext;
                    activity.finish();


    */
public class MyWaveHouseSpinner extends RelativeLayout {
    // 返回按钮控件
      private Spinner mSp;
      // 标题Tv
      private TextView mTitleTv;
    private static BasicShareUtil share;
    private String autoString;//用于联网时，再次去自动设置值
    private String waveHouseId="0";//用于联网时，再次去自动设置值
    private String waveHouse="";//用于联网时，再次去自动设置值
    private WaveHouseSpAdapter waveHouseSpAdapter;
    private ArrayList<WaveHouse> waveHouses;
    private DaoSession daoSession;
    private Storage storage;
    private Context mContext;

    public MyWaveHouseSpinner(Context context, AttributeSet attributeSet) {
        super(context,attributeSet);

        LayoutInflater.from(context).inflate(R.layout.view_my_wave_spinner,this);
        daoSession = GreenDaoManager.getmInstance(context).getDaoSession();
        waveHouses = new ArrayList<>();
        waveHouseSpAdapter = new WaveHouseSpAdapter(context, waveHouses);
        share = BasicShareUtil.getInstance(context);
        // 获取控件
        mSp = (Spinner) findViewById(R.id.sp);
        mTitleTv = (TextView) findViewById(R.id.tv);
        TypedArray attrArray = context.obtainStyledAttributes(attributeSet, R.styleable.MyWaveSpinner);
        int count = attrArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attrName = attrArray.getIndex(i);
            switch (attrName) {
                case R.styleable.MyWaveSpinner_spinner_name:
                    mTitleTv.setText(attrArray.getString(R.styleable.MyWaveSpinner_spinner_name));
                    break;
                case R.styleable.MyWaveSpinner_spinner_name_size:
                    mTitleTv.setText(attrArray.getString(R.styleable.MyWaveSpinner_spinner_name));
                    mTitleTv.setTextSize(attrArray.getDimension(R.styleable.MyWaveSpinner_spinner_name_size,10));
                    break;
            }
        }
        attrArray.recycle();

        mSp.setAdapter(waveHouseSpAdapter);
        if(share.getIsOL()){
            Log.e("CommonMethod:","getWaveHouseAdapter联网");
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
                public void onNext(CommonResponse commonResponse) {
                    super.onNext(commonResponse);
                    //                    Log.e("CommonMethod:","getWaveHouseAdapter获得数据：\n"+cBean.returnJson);
                    DownloadReturnBean dBean = JsonCreater.gson.fromJson(commonResponse.returnJson, DownloadReturnBean.class);
                    WaveHouseDao wavehouseDao = daoSession.getWaveHouseDao();
                    wavehouseDao.deleteAll();
                    wavehouseDao.insertOrReplaceInTx(dBean.wavehouse);
                    wavehouseDao.detachAll();
//                    if (waveHouses.size()<=0){
//                        waveHouses.addAll(dBean.wavehouse);
//                        waveHouseSpAdapter.notifyDataSetChanged();
//                        setAuto(mContext,storage,autoString);
//                    }
//                    waveHouses.clear();
//                    for (int i = 0; i < dBean.wavehouse.size(); i++) {
//                        if (storage.FSPGroupID.equals(dBean.wavehouse.get(i).FSPGroupID)){
//                            waveHouses.add(dBean.wavehouse.get(i));
//                        }
//                    }
////                    waveHouses.addAll(dBean.wavehouse);
//                    waveHouseSpAdapter.notifyDataSetChanged();
//                    for (int j = 0; j < waveHouseSpAdapter.getCount(); j++) {
//                        if (((WaveHouse) waveHouseSpAdapter.getItem(j)).FSPID.equals(autoString)) {
//                            mSp.setSelection(j);
//                            break;
//                        }
//                    }
                }

                @Override
                public void onError(Throwable e) {
//                    super.onError(e);
                }
            });
        }

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
    public void setAdapter(WaveHouseSpAdapter adapter){
        mSp.setAdapter(adapter);
        this.waveHouseSpAdapter = adapter;
    }
    public WaveHouseSpAdapter getAdapter(){
        return waveHouseSpAdapter;
    }
    public void setSelection(int i){
        mSp.setSelection(i);
    }
    // 设置标题的方法
    public void setTitleText(String title) {
        mTitleTv.setText(title);
    }

    public String getWaveHouseId(){
        return waveHouseId;
    }
    public String getWaveHouse(){
        return waveHouse;
    }

    //自动设置保存的值
    public void setAuto(final Context context, final Storage storage, String spid) {
            autoString = spid;
            this.storage = storage;
            if (null==storage){
                return;
            }
        Lg.e("setAuto:"+spid);
//        if(share.getIsOL()){
//            Log.e("CommonMethod:","getWaveHouseAdapter联网");
//            ArrayList<Integer> choose = new ArrayList<>();
//            choose.add(4);
//            String json = JsonCreater.DownLoadData(
//                    share.getDatabaseIp(),
//                    share.getDatabasePort(),
//                    share.getDataBaseUser(),
//                    share.getDataBasePass(),
//                    share.getDataBase(),
//                    share.getVersion(),
//                    choose
//            );
//            Asynchttp.post(context,share.getBaseURL()+ WebApi.DOWNLOADDATA, json, new Asynchttp.Response() {
//                @Override
//                public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
////                    Log.e("CommonMethod:","getWaveHouseAdapter获得数据：\n"+cBean.returnJson);
//                    DownloadReturnBean dBean = JsonCreater.gson.fromJson(cBean.returnJson, DownloadReturnBean.class);
//                    WaveHouseDao wavehouseDao = daoSession.getWaveHouseDao();
//                    wavehouseDao.deleteAll();
//                    wavehouseDao.insertOrReplaceInTx(dBean.wavehouse);
//                    wavehouseDao.detachAll();
////                    if (waveHouses.size()<=0){
////                        waveHouses.addAll(dBean.wavehouse);
////                        waveHouseSpAdapter.notifyDataSetChanged();
////                        setAuto(mContext,storage,autoString);
////                    }
////                    waveHouses.clear();
////                    for (int i = 0; i < dBean.wavehouse.size(); i++) {
////                        if (storage.FSPGroupID.equals(dBean.wavehouse.get(i).FSPGroupID)){
////                            waveHouses.add(dBean.wavehouse.get(i));
////                        }
////                    }
//////                    waveHouses.addAll(dBean.wavehouse);
////                    waveHouseSpAdapter.notifyDataSetChanged();
////                    for (int j = 0; j < waveHouseSpAdapter.getCount(); j++) {
////                        if (((WaveHouse) waveHouseSpAdapter.getItem(j)).FSPID.equals(autoString)) {
////                            mSp.setSelection(j);
////                            break;
////                        }
////                    }
//                }
//
//                @Override
//                public void onFailed(String Msg, AsyncHttpClient client) {
//                    Toast.showText(context, Msg);
//                }
//            });
//        }
//        else{
//            Log.e("CommonMethod:","getWaveHouseAdapter-不-联网");


            WaveHouseDao wavehousedao = daoSession.getWaveHouseDao();
            List<WaveHouse> waveHouseList = wavehousedao.queryBuilder().where(
                    WaveHouseDao.Properties.FSPGroupID.eq(storage.FSPGroupID)
            ).build().list();
//            waveHouseList.add(new WaveHouse());
            if (waveHouseList.size()>0){
                waveHouses.clear();
                Lg.e("过滤仓位");
                waveHouses.addAll(waveHouseList);
                mSp.setAdapter(waveHouseSpAdapter);
//                Log.e("CommonMethod:","获取到本地数据waveHouse:"+waveHouseList.toString());
                waveHouseSpAdapter.notifyDataSetChanged();
                for (int j = 0; j < waveHouses.size(); j++) {
                    if (((WaveHouse) waveHouses.get(j)).FSPID.equals(autoString)) {
                        Lg.e("过滤出仓位："+autoString);
                        waveHouseId = ((WaveHouse) waveHouses.get(j)).FSPID;
                        waveHouse = ((WaveHouse) waveHouses.get(j)).FName;
                        mSp.setSelection(j);
                        break;
                    }
                }
                Lg.e("过滤完毕");
            }else{
                waveHouseId="0";
                waveHouse="";
                Lg.e("过滤出仓位--空");
                waveHouses.clear();
                waveHouseSpAdapter.notifyDataSetChanged();

//                Log.e("CommonMethod:","无数据：waveHouse:");
            }
//        }

    }

}

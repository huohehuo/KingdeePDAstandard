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

import com.fangzuo.assist.Adapter.WaveHouseSpAdapter;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Dao.Storage;
import com.fangzuo.assist.Dao.WaveHouse;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.Asynchttp;
import com.fangzuo.assist.Utils.BasicShareUtil;
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
    private SpinnerAdapter adapter;
    private static BasicShareUtil share;
    private String autoString;//用于联网时，再次去自动设置值
    private WaveHouseSpAdapter waveHouseSpAdapter;
    private ArrayList<WaveHouse> waveHouses;
    private DaoSession daoSession;
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
    public void setAdapter(SpinnerAdapter adapter){
        mSp.setAdapter(adapter);
        this.adapter = adapter;
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

    //自动设置保存的值
    public void setAuto(final Context context, final Storage storage, String spid) {
            autoString = spid;
        Lg.e("setAuto:"+spid);
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
            Asynchttp.post(context,share.getBaseURL()+ WebApi.DOWNLOADDATA, json, new Asynchttp.Response() {
                @Override
                public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
//                    Log.e("CommonMethod:","getWaveHouseAdapter获得数据：\n"+cBean.returnJson);
                    DownloadReturnBean dBean = JsonCreater.gson.fromJson(cBean.returnJson, DownloadReturnBean.class);
                    WaveHouseDao wavehouseDao = daoSession.getWaveHouseDao();
                    wavehouseDao.deleteAll();
                    waveHouses.clear();
                    for (int i = 0; i < dBean.wavehouse.size(); i++) {
                        if (storage.FSPGroupID.equals(dBean.wavehouse.get(i).FSPGroupID)){
                            waveHouses.add(dBean.wavehouse.get(i));
                        }
                    }
//                    waveHouses.addAll(dBean.wavehouse);
                    waveHouseSpAdapter.notifyDataSetChanged();
                    wavehouseDao.insertOrReplaceInTx(dBean.wavehouse);
                    wavehouseDao.detachAll();
                    for (int j = 0; j < waveHouseSpAdapter.getCount(); j++) {
                        if (((WaveHouse) waveHouseSpAdapter.getItem(j)).FSPID.equals(autoString)) {
                            mSp.setSelection(j);
                            break;
                        }
                    }
                }

                @Override
                public void onFailed(String Msg, AsyncHttpClient client) {
                    Toast.showText(context, Msg);
                }
            });
        }else{
//            Log.e("CommonMethod:","getWaveHouseAdapter-不-联网");
            WaveHouseDao wavehousedao = daoSession.getWaveHouseDao();
            List<WaveHouse> waveHouseList = wavehousedao.queryBuilder().where(
                    WaveHouseDao.Properties.FSPGroupID.eq(storage.FSPGroupID)
            ).build().list();
//            waveHouseList.add(new WaveHouse());
            if (waveHouseList.size()>0){
                waveHouses.clear();
                waveHouses.addAll(waveHouseList);
//                Log.e("CommonMethod:","获取到本地数据waveHouse:"+waveHouseList.toString());
                waveHouseSpAdapter.notifyDataSetChanged();
                for (int j = 0; j < waveHouseSpAdapter.getCount(); j++) {
                    if (((WaveHouse) waveHouseSpAdapter.getItem(j)).FSPID.equals(autoString)) {
                        mSp.setSelection(j);
                        break;
                    }
                }
            }else{
                waveHouses.clear();
                waveHouseSpAdapter.notifyDataSetChanged();

//                Log.e("CommonMethod:","无数据：waveHouse:");
            }
        }

    }

}

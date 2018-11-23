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

import com.fangzuo.assist.Adapter.StorageSpAdapter;
import com.fangzuo.assist.Adapter.UnitSpAdapter;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Dao.Storage;
import com.fangzuo.assist.Dao.Unit;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.Asynchttp;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.JsonCreater;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.StorageDao;
import com.fangzuo.greendao.gen.UnitDao;
import com.loopj.android.http.AsyncHttpClient;

import java.util.ArrayList;
import java.util.List;


public class SpinnerUnit extends RelativeLayout {
    // 返回按钮控件
    private Spinner mSp;
    // 标题Tv
    private TextView mTitleTv;
    private static BasicShareUtil share;
    private String autoString;//用于联网时，再次去自动设置值
    private UnitSpAdapter adapter;
    private ArrayList<Unit> list;
    private DaoSession daoSession;
    private String unitId = "";
    private String unitName = "";
    public static final String Name = "name";
    public static final String Id = "id";
    public static final String Number = "number";
    public static final String TGP = "Unit+";     //7

    public SpinnerUnit(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        LayoutInflater.from(context).inflate(R.layout.view_my_unit_spinner, this);
        daoSession = GreenDaoManager.getmInstance(context).getDaoSession();
        list = new ArrayList<>();
        adapter = new UnitSpAdapter(context, list);
        share = BasicShareUtil.getInstance(context);
        // 获取控件
        mSp = (Spinner) findViewById(R.id.sp);
        mTitleTv = (TextView) findViewById(R.id.tv);
        TypedArray attrArray = context.obtainStyledAttributes(attributeSet, R.styleable.Style_Spinner_Unit);
        int count = attrArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attrName = attrArray.getIndex(i);
            switch (attrName) {
                case R.styleable.Style_Spinner_Unit_Uspinner_name:
                    mTitleTv.setText(attrArray.getString(R.styleable.Style_Spinner_Unit_Uspinner_name));
                    break;
                case R.styleable.Style_Spinner_Unit_Uspinner_focusable:
                    mSp.setEnabled(attrArray.getBoolean(R.styleable.Style_Spinner_Unit_Uspinner_focusable, true));
                    break;
                case R.styleable.Style_Spinner_Unit_Uspinner_name_size:
                    mTitleTv.setText(attrArray.getString(R.styleable.Style_Spinner_Unit_Uspinner_name));
                    mTitleTv.setTextSize(attrArray.getDimension(R.styleable.Style_Spinner_Unit_Uspinner_name_size, 10));
                    break;
            }
        }
        attrArray.recycle();

        mSp.setAdapter(adapter);
    }

    // 为左侧返回按钮添加自定义点击事件
    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener listener) {
        mSp.setOnItemSelectedListener(listener);
    }

    public void setAdapter(UnitSpAdapter adapter) {
        mSp.setAdapter(adapter);
        this.adapter = adapter;
    }

    public void setEnabled(boolean b) {
        mSp.setEnabled(b);
    }

    public String getDataId() {
        return unitId;
    }

    public String getDataName() {
        return unitName;
    }

    public UnitSpAdapter getAdapter() {
        return adapter;
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
    public void setAuto(final Context context, final String unitGroupIDTemp, String autoStr, final String type) {
        unitId = "";
        unitName = "";
        final String UnitGroupID;
        Lg.e(TGP+"setAuto:" + autoStr);
        if (null==unitGroupIDTemp || "".equals(unitGroupIDTemp)){
            UnitGroupID = "";
            Lg.e("单位组不存在");
        }else{
            UnitGroupID=unitGroupIDTemp;
        }
//        LoadingUtil.dismiss();
//        LoadingUtil.show(context,"正在调整单位...");
        autoString = autoStr;
        final List<Unit> listTemp =getLocData(UnitGroupID);
        dealAuto(listTemp,UnitGroupID,type,false);

        if (share.getIsOL()) {
            ArrayList<Integer> choose = new ArrayList<>();
            choose.add(7);
            String json = JsonCreater.DownLoadData(
                    share.getDatabaseIp(),
                    share.getDatabasePort(),
                    share.getDataBaseUser(),
                    share.getDataBasePass(),
                    share.getDataBase(),
                    share.getVersion(),
                    choose
            );
            Asynchttp.post(context, share.getBaseURL() + WebApi.DOWNLOADDATA, json, new Asynchttp.Response() {
                @Override
                public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {

                    Log.e("CommonMethod:","getWaveHouseAdapter获得数据：\n"+cBean.returnJson);
                    DownloadReturnBean dBean = JsonCreater.gson.fromJson(cBean.returnJson, DownloadReturnBean.class);
                    if (dBean != null && dBean.units != null && dBean.units.size() > 0) {
                        UnitDao unitDao = daoSession.getUnitDao();
                        unitDao.deleteAll();
                        unitDao.insertOrReplaceInTx(dBean.units);
                        unitDao.detachAll();
                        if (list.size()<=0){
                            dealAuto(dBean.units,UnitGroupID,type,true);
                        }

                    }
//                    LoadingUtil.dismiss();
                }

                @Override
                public void onFailed(String Msg, AsyncHttpClient client) {

//                    LoadingUtil.dismiss();
//                    adapter.notifyDataSetChanged();
//                    Toast.showText(context, Msg);
                }
            });
        }
// else {
////            Log.e("CommonMethod:","getWaveHouseAdapter-不-联网");
//            UnitDao unitDao = daoSession.getUnitDao();
//            List<Unit> units = unitDao.queryBuilder().where(
//                    UnitDao.Properties.FUnitGroupID.eq(UnitGroupID)
//            ).build().list();
//
//            if (units.size() > 0) {
//                list.addAll(units);
////                Log.e("CommonMethod:","获取到本地数据waveHouse:"+waveHouseList.toString());
//                adapter.notifyDataSetChanged();
//                if (Number.equals(type)) {
//                    for (int j = 0; j < adapter.getCount(); j++) {
//                        if (((Unit) adapter.getItem(j)).FNumber.equals(autoString)) {
//                            Lg.e("单位定位（自定义控件：" + autoString);
//                            unitId = autoString;
//                            unitName = ((Unit) adapter.getItem(j)).FName;
//                            mSp.setSelection(j);
//                            break;
//                        }
//                    }
//                } else if (Name.equals(type)) {
//                    for (int j = 0; j < adapter.getCount(); j++) {
//                        if (((Unit) adapter.getItem(j)).FName.equals(autoString)) {
//                            Lg.e("单位定位（自定义控件：" + autoString);
//                            unitId = autoString;
//                            unitName = ((Unit) adapter.getItem(j)).FName;
//                            mSp.setSelection(j);
//                            break;
//                        }
//                    }
//                } else if (Id.equals(type)) {
//                    for (int j = 0; j < adapter.getCount(); j++) {
//                        if (((Unit) adapter.getItem(j)).FMeasureUnitID.equals(autoString)) {
//                            Lg.e("单位定位（自定义控件：" + autoString);
//                            unitId = autoString;
//                            unitName = ((Unit) adapter.getItem(j)).FName;
//                            mSp.setSelection(j);
//                            break;
//                        }
//                    }
//                }
//
//            } else {
//                adapter.notifyDataSetChanged();
////                Log.e("CommonMethod:","无数据：waveHouse:");
//            }
//        }

    }


    private List<Unit> getLocData(String unitGroupID){
        UnitDao unitDao = daoSession.getUnitDao();
        return unitDao.queryBuilder().where(
                UnitDao.Properties.FUnitGroupID.eq(unitGroupID)
        ).build().list();
    }

    private void dealAuto(List<Unit> listData, String unitGroupID,final String type,boolean check){
        list.clear();
        if (check){
            for (int i = 0; i < listData.size(); i++) {
                if (listData.get(i).FUnitGroupID.equals(unitGroupID)){
                    list.add(listData.get(i));
                }
            }
        }else{
            list.addAll(listData);
        }
        if (null==autoString || "".equals(autoString) || "0".equals(autoString)) {
            adapter.notifyDataSetChanged();
        } else {
            if (Number.equals(type)) {
                for (int j = 0; j < adapter.getCount(); j++) {
                    if (((Unit) adapter.getItem(j)).FNumber.equals(autoString)) {
                        Lg.e("单位定位（自定义控件：" + autoString);
                        unitId = autoString;
                        unitName = ((Unit) adapter.getItem(j)).FName;
                        mSp.setSelection(j);
                        break;
                    }
                }
            } else if (Name.equals(type)) {
                for (int j = 0; j < adapter.getCount(); j++) {
                    if (((Unit) adapter.getItem(j)).FName.equals(autoString)) {
                        Lg.e("单位定位（自定义控件：" + autoString);
                        unitId = autoString;
                        unitName = ((Unit) adapter.getItem(j)).FName;
                        mSp.setSelection(j);
                        break;
                    }
                }
            } else if (Id.equals(type)) {
                for (int j = 0; j < adapter.getCount(); j++) {
                    if (((Unit) adapter.getItem(j)).FMeasureUnitID.equals(autoString)) {
                        Lg.e("单位定位（自定义控件：" + autoString);
                        Lg.e("单位定位（自定义控件：" + ((Unit) adapter.getItem(j)).toString());
                        unitId = autoString;
                        unitName = ((Unit) adapter.getItem(j)).FName;
                        mSp.setSelection(j);
                        break;
                    }
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

}

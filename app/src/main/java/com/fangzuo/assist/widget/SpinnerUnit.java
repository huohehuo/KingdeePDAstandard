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
import com.fangzuo.assist.Adapter.StorageSpAdapter;
import com.fangzuo.assist.Adapter.UnitSpAdapter;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Dao.Storage;
import com.fangzuo.assist.Dao.Unit;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Utils.Asynchttp;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.JsonCreater;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.MathUtil;
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
    private double unitrate = 0d;
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
        mSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Unit unit = (Unit) adapter.getItem(i);
                unitId = unit.FMeasureUnitID;
                unitName = unit.FName;
                unitrate = MathUtil.toD(unit.FCoefficient);
                Lg.e("单位选择：",unit);
//                    Log.e("unitId", unitId + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
    public Double getDataUnitrate() {
        return unitrate;
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
        unitrate = 0d;
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
            App.getRService().doIOAction(WebApi.DOWNLOADDATA, json, new MySubscribe<CommonResponse>() {
                @Override
                public void onNext(CommonResponse cBean) {
                    super.onNext(cBean);
                    Log.e("CommonMethod:","getWaveHouseAdapter获得数据：\n"+cBean.returnJson);
                    DownloadReturnBean dBean = JsonCreater.gson.fromJson(cBean.returnJson, DownloadReturnBean.class);
                    if (dBean != null && dBean.units != null && dBean.units.size() > 0) {
                        UnitDao unitDao = daoSession.getUnitDao();
                        unitDao.deleteAll();
                        unitDao.insertOrReplaceInTx(dBean.units);
                        unitDao.detachAll();
                        if (dBean.units.size()>0 && list.size()<=0){
                            dealAuto(dBean.units,UnitGroupID,type,true);
                        }

                    }
                }

                @Override
                public void onError(Throwable e) {
//                    super.onError(e);
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
        if (list.size()>0){
                mSp.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            if (list.size()==1){//当只有一个的时候，重新适配器，为了spinner的监听能响应
                unitId = list.get(0).FMeasureUnitID;
                unitName=list.get(0).FName;
                unitrate= MathUtil.toD(list.get(0).FCoefficient);
            }else{//过滤设定的值
                if (null==autoString || "".equals(autoString) || "0".equals(autoString)) {
                    unitId = list.get(0).FMeasureUnitID;
                    unitName=list.get(0).FName;
                    unitrate= MathUtil.toD(list.get(0).FCoefficient);
                } else {
                    if (Number.equals(type)) {
                        for (int j = 0; j < list.size(); j++) {
                            if (list.get(j).FNumber.equals(autoString)) {
                                Lg.e("单位定位（自定义控件：" + autoString);
                                unitId = list.get(j).FMeasureUnitID;
                                unitName = list.get(j).FName;
                                unitrate= MathUtil.toD(list.get(j).FCoefficient);
                                mSp.setSelection(j);
                                break;
                            }
                        }
                    } else if (Name.equals(type)) {
                        for (int j = 0; j < list.size(); j++) {
                            if (list.get(j).FName.equals(autoString)) {
                                Lg.e("单位定位（自定义控件：" + autoString);
                                unitId = list.get(j).FMeasureUnitID;
                                unitName = list.get(j).FName;
                                unitrate= MathUtil.toD(list.get(j).FCoefficient);
                                mSp.setSelection(j);
                                break;
                            }
                        }
                    } else if (Id.equals(type)) {
                        for (int j = 0; j < list.size(); j++) {
                            if (list.get(j).FMeasureUnitID.equals(autoString)) {
                                Lg.e("单位定位（自定义控件：" + autoString);
                                Lg.e("单位定位（自定义控件：" + list.get(j).toString());
                                unitId = autoString;
                                unitName = list.get(j).FName;
                                unitrate= MathUtil.toD(list.get(j).FCoefficient);
                                mSp.setSelection(j);
                                break;
                            }
                        }
                    }
                    if ("".equals(unitId) && "".equals(unitName)){
                        unitId = list.get(0).FMeasureUnitID;
                        unitName=list.get(0).FName;
                        unitrate= MathUtil.toD(list.get(0).FCoefficient);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

        }else{
            adapter.notifyDataSetChanged();
        }

    }

}

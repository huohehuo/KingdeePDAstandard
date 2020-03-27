package com.fangzuo.assist.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Adapter.StorageSpAdapter;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Dao.Storage;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.JsonCreater;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.StorageDao;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

public class SpinnerStorageDlg extends RelativeLayout {
    // 返回按钮控件
    private Spinner mSp;
    // 标题Tv
    private AppCompatTextView mTitleTv;
    private ImageView ivFind;
    private boolean showEd = false;
    //    private SpinnerAdapter adapter;
    private DaoSession daoSession;
    private ArrayList<String> autoList;
    private BasicShareUtil share;
    private ArrayList<Storage> container;
    private ArrayList<Storage> containerTemp;
    private StorageSpAdapter adapter;
    private StorageSpAdapter adapterDlg;
    private String autoString="";//用于联网时，再次去自动设置值
//    private String autoOrg="";//用于联网时，再次去自动设置值
    private String saveKeyString="";//用于保存数据的key
    private String stringOfEt="";//用于保存数据的key
    private String Id="";
    private String Name="";
    private String T="仓库：";     //19


    public SpinnerStorageDlg(final Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        LayoutInflater.from(context).inflate(R.layout.view_storage_spinner, this);
        daoSession = GreenDaoManager.getmInstance(context).getDaoSession();

        autoList = new ArrayList<>();
        share = BasicShareUtil.getInstance(context);
        container = new ArrayList<>();
        containerTemp = new ArrayList<>();
        // 获取控件
        mSp = (Spinner) findViewById(R.id.sp);
        mTitleTv = (AppCompatTextView) findViewById(R.id.tv);
        ivFind = (ImageView) findViewById(R.id.iv_find);
        adapter = new StorageSpAdapter(context, container);
        adapterDlg = new StorageSpAdapter(context, container);
        mSp.setAdapter(adapter);

//        if (share.getIsOL()) {
//            ArrayList<Integer> choose = new ArrayList<>();
//            choose.add(6);
//            String json = JsonCreater.DownLoadData(
//                    share.getDatabaseIp(),
//                    share.getDatabasePort(),
//                    share.getDataBaseUser(),
//                    share.getDataBasePass(),
//                    share.getDataBase(),
//                    share.getVersion(),
//                    choose
//            );
//            Asynchttp.post(context, share.getBaseURL() + WebApi.DOWNLOADDATA, json, new Asynchttp.Response() {
//                @Override
//                public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
//                    DownloadReturnBean dBean = JsonCreater.gson.fromJson(cBean.returnJson, DownloadReturnBean.class);
//                    StorageDao yuandanTypeDao = daoSession.getStorageDao();
//                    yuandanTypeDao.deleteAll();
//                    yuandanTypeDao.insertOrReplaceInTx(dBean.storage);
//                    yuandanTypeDao.detachAll();
//                    if (dBean.storage.size() > 0 && container.size()<=0){
//                        container.addAll(dBean.storage);
//                        adapter.notifyDataSetChanged();
//                        setAutoSelection(saveKeyString,autoString);
//                    }
//
//                }
//
//                @Override
//                public void onFailed(String Msg, AsyncHttpClient client) {
////                    Toast.showText(context, Msg);
//                }
//            });
//        }
////        else {
//            StorageDao storageDao = daoSession.getStorageDao();
//            List<Storage> storages = storageDao.loadAll();
//            container.addAll(storages);
//            adapter.notifyDataSetChanged();
//            if (autoString != null) {
//                setAutoSelection(saveKeyString,autoString);
//            }
//            Log.e("CommonMethod", "获取到本地数据：\n" + container.toString());
//        }


//        mSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                InStoreType employee = (InStoreType) adapter.getItem(i);
//                Id = employee.FID;
//                Name = employee.FName;
//                Lg.e("选中"+T+employee.toString());
//                Hawk.put(saveKeyString,employee.FName);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        /*//点击文字时，触发spinner点击*/
        mTitleTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDlg(context);
//                mSp.performClick();
            }
        });
        ivFind.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDlg(context);
            }
        });
    }
    AlertDialog alertDialog;
    private void showDlg(Context context){
        AlertDialog.Builder ab = new AlertDialog.Builder(context);
        ab.setTitle("仓库过滤");
        View v = LayoutInflater.from(context).inflate(R.layout.show_storage, null);
        final EditText etSearch     = v.findViewById(R.id.et_search);
        final ListView listView     = v.findViewById(R.id.lv_storage);
        etSearch.setText(stringOfEt);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Lg.e("变化前....");
                containerTemp.clear();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Lg.e("变化中....");
            }

            @Override
            public void afterTextChanged(Editable s) {
                Lg.e("变化后....");
                stringOfEt = etSearch.getText().toString();
                if ("".equals(etSearch.getText().toString().trim())){
                    adapterDlg.clear();
                    container.addAll(getLocData());
                    listView.setAdapter(adapterDlg);
                    adapterDlg.notifyDataSetChanged();
                }else{
                    container.clear();
                    container.addAll(getLocData());
                    for (int i = 0; i < container.size(); i++) {
                        if (container.get(i).FName.contains(etSearch.getText().toString())
                                ||container.get(i).FNumber.contains(etSearch.getText().toString())){
                            containerTemp.add(container.get(i));
                        }
                    }
                    adapterDlg.addData(containerTemp);
                    listView.setAdapter(adapterDlg);
                }
//                if (adapter.getCount()<=0){
//                    employeeId = "";
//                    employeeName = "";
//                    employeeNumber = "";
//                    Lg.e("重置");
//                }
            }
        });
        listView.setAdapter(adapterDlg);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Storage storage = (Storage) adapterDlg.getItem(i);
                for (int j = 0; j < adapter.getCount(); j++) {
                    if (storage.FName.equals(((Storage)adapter.getItem(j)).FName)){
                        mSp.setSelection(j);
                    }
                }
                alertDialog.dismiss();
            }
        });
        ab.setView(v);
        ab.setPositiveButton("返回", null);

        alertDialog = ab.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    // 为左侧返回按钮添加自定义点击事件
    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener listener) {
        mSp.setOnItemSelectedListener(listener);
    }
//    public void setAdapter(SpinnerAdapter adapter){
//        this.adapter = adapter;
//        mSp.setAdapter(adapter);
//    }
    public void setSelection(int i){
        mSp.setSelection(i);
    }



//    public String getDataId() {
//        return Id == null ? "" : Id;
//    }
//
//    public String getDataName() {
//        return Name == null ? "" : Name;
//    }

    public void setAutoSelection(String string) {
        autoString = string;
        Lg.e("自动"+T+autoString);
        for (int j = 0; j < adapter.getCount(); j++) {
            if (((Storage) adapter.getItem(j)).FName.equals(autoString)
                    || ((Storage) adapter.getItem(j)).FItemID.equals(autoString)) {
                mSp.setSelection(j);
//                autoString = null;
                break;
            }
        }
    }

    public void initData(String autoStr) {
        Id="";
        Name="";
        autoString = autoStr;

    }
    public void setAuto(String autoStr) {
        Id="";
        Name="";
        autoString = autoStr;
        Lg.e("带出仓库："+autoStr);
//        autoOrg = org==null?"":org.FOrgID;
        mTitleTv.setText("");
        final List<Storage> listTemp = getLocData();
        dealAuto(listTemp, false);

        if (share.getIsOL()){//当在线模式时才去更新数据
            ArrayList<Integer> choose = new ArrayList<>();
            choose.add(6);
            String json = JsonCreater.DownLoadData(
                    share.getDatabaseIp(),
                    share.getDatabasePort(),
                    share.getDataBaseUser(),
                    share.getDataBasePass(),
                    share.getDataBase(),
                    share.getVersion(),
                    choose
            );
            App.getRService().downloadData(json, new MySubscribe<CommonResponse>() {
                @Override
                public void onNext(CommonResponse commonResponse) {
                    DownloadReturnBean dBean = JsonCreater.gson.fromJson(commonResponse.returnJson, DownloadReturnBean.class);
                    StorageDao yuandanTypeDao = daoSession.getStorageDao();
                    yuandanTypeDao.deleteAll();
                    yuandanTypeDao.insertOrReplaceInTx(dBean.storage);
                    yuandanTypeDao.detachAll();
                    if (dBean.storage.size() > 0 && container.size()<=0){
                        dealAuto(dBean.storage,true);
//                    container.addAll(dBean.storage);
//                    adapter.notifyDataSetChanged();
//                    setAutoSelection(saveKeyString,autoString);
                    }
                }

                @Override
                public void onError(Throwable e) {
//                    LoadingUtil.dismiss();
//                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Updata_Error,e.toString()));
                }
            });
        }


    }

    private List<Storage> getLocData() {
        StorageDao employeeDao = daoSession.getStorageDao();
//        List<Storage> employees = employeeDao.queryBuilder().where(StorageDao.Properties.FOrg.eq(org)).build().list();
        List<Storage> employees = employeeDao.loadAll();
        return employees;
    }

    private void dealAuto(List<Storage> listData, boolean check) {
        container.clear();
//        if (check) {
//            for (int i = 0; i < listData.size(); i++) {
//                if (listData.get(i).FOrg.equals(autoOrg)) {
//                    container.add(listData.get(i));
//                }
//            }
//        } else {
            container.addAll(listData);
//        }
        if (container.size() > 0) {
            mSp.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            for (int j = 0; j < container.size(); j++) {
                if (container.get(j).FName.equals(autoString)
                        || container.get(j).FItemID.equals(autoString)) {
                    mSp.setSelection(j);
                    break;
                }
            }
        } else {
            adapter.notifyDataSetChanged();
        }

    }





    public StorageSpAdapter getAdapter() {
        return adapter;
    }

    //清空
    private void clear() {
        container.clear();
    }
//     设置标题的方法
    public void setTitleText(String title) {
        mTitleTv.setText(title);
    }

}

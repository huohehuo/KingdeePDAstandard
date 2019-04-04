package com.fangzuo.assist.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Adapter.DepartmentSpAdapter;
import com.fangzuo.assist.Adapter.WLKMSpAdapter;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Dao.Department;
import com.fangzuo.assist.Dao.Wanglaikemu;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Utils.Asynchttp;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.JsonCreater;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.DepartmentDao;
import com.fangzuo.greendao.gen.WanglaikemuDao;
import com.loopj.android.http.AsyncHttpClient;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

public class SpinnerWlkm extends RelativeLayout {
    // 返回按钮控件
    private Spinner mSp;
    // 标题Tv
    private TextView mTitleTv;
    private boolean showEd = false;
    //    private SpinnerAdapter adapter;
    private DaoSession daoSession;
    private ArrayList<String> autoList;
    private BasicShareUtil share;
    private ArrayList<Wanglaikemu> container;
    private WLKMSpAdapter adapter;
    private String autoString="";//用于联网时，再次去自动设置值
    private String saveKeyString="";//用于保存数据的key
    private String Id="";
    private String Name="";
    private String T="往来科目：";     //17


    public SpinnerWlkm(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        LayoutInflater.from(context).inflate(R.layout.view_my_people_spinner, this);
        daoSession = GreenDaoManager.getmInstance(context).getDaoSession();

        autoList = new ArrayList<>();
        share = BasicShareUtil.getInstance(context);
        container = new ArrayList<>();
        // 获取控件
        mSp = (Spinner) findViewById(R.id.sp);
//        mTitleTv = (TextView) findViewById(R.id.tv);
//        TypedArray attrArray = context.obtainStyledAttributes(attributeSet, R.styleable.MySpinner);
//        int count = attrArray.getIndexCount();
//        for (int i = 0; i < count; i++) {
//            int attrName = attrArray.getIndex(i);
//            switch (attrName) {
//                case R.styleable.MySpinner_spinner_name:
//                    mTitleTv.setText(attrArray.getString(R.styleable.MySpinner_spinner_name));
//                    break;
//                case R.styleable.MySpinner_spinner_name_size:
//                    mTitleTv.setText(attrArray.getString(R.styleable.MySpinner_spinner_name));
//                    mTitleTv.setTextSize(attrArray.getDimension(R.styleable.MySpinner_spinner_name_size,15));
//                    break;
//            }
//        }
//        attrArray.recycle();
        adapter = new WLKMSpAdapter(context, container);
        mSp.setAdapter(adapter);
        if (share.getIsOL()) {
            ArrayList<Integer> choose = new ArrayList<>();
            choose.add(17);
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
                    DownloadReturnBean dBean = JsonCreater.gson.fromJson(cBean.returnJson, DownloadReturnBean.class);
                    WanglaikemuDao yuandanTypeDao = daoSession.getWanglaikemuDao();
                    yuandanTypeDao.deleteAll();
                    yuandanTypeDao.insertOrReplaceInTx(dBean.wanglaikemu);
                    yuandanTypeDao.detachAll();
                    if (dBean.wanglaikemu.size()>0 && container.size()<=0){
                        container.addAll(dBean.wanglaikemu);
                        adapter.notifyDataSetChanged();
                        setAutoSelection(saveKeyString,autoString);
                    }
                }

                @Override
                public void onError(Throwable e) {
//                    super.onError(e);
                }
            });
        }
//        else {
            WanglaikemuDao employeeDao = daoSession.getWanglaikemuDao();
            List<Wanglaikemu> employees = employeeDao.loadAll();
            container.addAll(employees);
            adapter.notifyDataSetChanged();
            setAutoSelection(saveKeyString,autoString);

//            Log.e("CommonMethod", "获取到本地数据：\n" + container.toString());
//        }


        mSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Wanglaikemu employee = (Wanglaikemu) adapter.getItem(i);
                Id = employee.FAccountID;
                Name = employee.FFullName;
                Lg.e("选中"+T+employee.toString());
                Hawk.put(saveKeyString,employee.FFullName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    //自动设置保存的值

//
//    public EmployeeSpAdapter getEmployeeSpAdapter() {
//        if (adapter.getCount() < 0) {
//            Lg.e("adapter初始化失败，重新更新adapter");
//            EmployeeDao employeeDao = daoSession.getEmployeeDao();
//            List<Employee> employees = employeeDao.loadAll();
//            container.addAll(employees);
//            adapter.notifyDataSetChanged();
//            return adapter;
//        } else {
//            return adapter;
//        }
//    }

    // 为左侧返回按钮添加自定义点击事件
    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener listener) {
        mSp.setOnItemSelectedListener(listener);
    }
//    public void setAdapter(SpinnerAdapter adapter){
//        this.adapter = adapter;
//        mSp.setAdapter(adapter);
//    }
//    public void setSelection(int i){
//        mSp.setSelection(i);
//    }


    public String getDataId() {
        return Id == null ? "" : Id;
    }

    public String getDataName() {
        return Name == null ? "" : Name;
    }

    /**
     *
     * @param saveKeyStr        用于保存的key
     * @param string            自动设置的z值
     * */
    public void setAutoSelection(String saveKeyStr,String string) {
        saveKeyString =saveKeyStr;
        autoString = string;
        Lg.e("自动"+T+autoString);
        if ("".equals(string)){
            autoString = Hawk.get(saveKeyString,"");
        }
        for (int j = 0; j < adapter.getCount(); j++) {
            if (((Wanglaikemu) adapter.getItem(j)).FFullName.equals(autoString)
                    || ((Wanglaikemu) adapter.getItem(j)).FAccountID.equals(autoString)) {
                mSp.setSelection(j);
//                autoString = null;
                break;
            }
        }
    }

    public WLKMSpAdapter getAdapter() {
        return adapter;
    }

    //清空
    private void clear() {
        container.clear();
    }
//     设置标题的方法
//    public void setTitleText(String title) {
//        mTitleTv.setText(title);
//    }

}

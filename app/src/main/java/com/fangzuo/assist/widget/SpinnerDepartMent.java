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
import com.fangzuo.assist.Adapter.EmployeeSpAdapter;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Dao.Department;
import com.fangzuo.assist.Dao.Employee;
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
import com.fangzuo.greendao.gen.EmployeeDao;
import com.fangzuo.greendao.gen.YuandanTypeDao;
import com.loopj.android.http.AsyncHttpClient;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

public class SpinnerDepartMent extends RelativeLayout {
    // 返回按钮控件
    private Spinner mSp;
    // 标题Tv
    private TextView mTitleTv;
    private boolean showEd = false;
    //    private SpinnerAdapter adapter;
    private DaoSession daoSession;
    private ArrayList<String> autoList;
    private BasicShareUtil share;
    private ArrayList<Department> container;
    private DepartmentSpAdapter adapter;
    private String autoString="";//用于联网时，再次去自动设置值
    private String saveKeyString="";//用于保存数据的key
    private String Id="";
    private String Name="";
    private String T="部门：";     //2


    public SpinnerDepartMent(Context context, AttributeSet attributeSet) {
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
        adapter = new DepartmentSpAdapter(context, container);
        mSp.setAdapter(adapter);
        if (share.getIsOL()) {
            ArrayList<Integer> choose = new ArrayList<>();
            choose.add(2);
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
                    DownloadReturnBean dBean = JsonCreater.gson.fromJson(commonResponse.returnJson, DownloadReturnBean.class);
                    DepartmentDao yuandanTypeDao = daoSession.getDepartmentDao();
                    yuandanTypeDao.deleteAll();
                    yuandanTypeDao.insertOrReplaceInTx(dBean.department);
                    yuandanTypeDao.detachAll();
                    if (dBean.department.size()>0 && container.size()<=0){
                        container.addAll(dBean.department);
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
            DepartmentDao employeeDao = daoSession.getDepartmentDao();
            List<Department> employees = employeeDao.loadAll();
            container.addAll(employees);
            adapter.notifyDataSetChanged();
//            if (autoString != null) {
                setAutoSelection(saveKeyString,autoString);
//            }
//            Log.e("CommonMethod", "获取到本地数据：\n" + container.toString());
//        }


        mSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Department employee = (Department) adapter.getItem(i);
                Id = employee.FItemID;
                Name = employee.FName;
                Lg.e("选中"+T+employee.toString());
                Hawk.put(saveKeyString,employee.FName);
//                share.setPOEmployee(i);
//                if (isFirst6){
//                    share.setPOEmployee(i);
//                    spEmployee.setSelection(i);
//                }
//                else{
//                    spEmployee.setSelection(share.getPOEmployee());
//                    isFirst6=true;
//                }
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
            if (((Department) adapter.getItem(j)).FName.equals(autoString)
                    || ((Department) adapter.getItem(j)).FItemID.equals(autoString)) {
                mSp.setSelection(j);
//                autoString = null;
                break;
            }
        }
    }

    public DepartmentSpAdapter getAdapter() {
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

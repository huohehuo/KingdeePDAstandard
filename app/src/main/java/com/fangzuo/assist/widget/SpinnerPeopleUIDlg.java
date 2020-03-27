package com.fangzuo.assist.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Adapter.EmployeeSpAdapter;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Dao.Employee;
import com.fangzuo.assist.Dao.Storage;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.JsonCreater;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.EmployeeDao;
import com.fangzuo.greendao.gen.StorageDao;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

public class SpinnerPeopleUIDlg extends RelativeLayout {
    // 返回按钮控件
    private Spinner mSp;
    // 标题Tv
    private AppCompatTextView mTitleTv;//不受setSize控制，只能在xml里设置最大值
    private AppCompatTextView mText;
    private LinearLayout llClick;
    private DaoSession daoSession;
    private BasicShareUtil share;
    private ArrayList<Employee> container;
    private ArrayList<Employee> containerTemp;
    private EmployeeSpAdapter adapter;
    private EmployeeSpAdapter adapterDlg;
    private String autoString="";//用于联网时，再次去自动设置值
    private String saveKeyString="";//用于保存数据的key
    private String employeeId="";
    private String employeeName="";
    private String T="人物：";     //3
    private boolean isAddnull=false;     //3
    private String stringOfEt="";//用于保存数据的key


    public SpinnerPeopleUIDlg(final Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        LayoutInflater.from(context).inflate(R.layout.view_ui_spinner, this);
        daoSession = GreenDaoManager.getmInstance(context).getDaoSession();

        share = BasicShareUtil.getInstance(context);
        container = new ArrayList<>();
        containerTemp = new ArrayList<>();
        // 获取控件
        mSp = (Spinner) findViewById(R.id.sp);
        mText = (AppCompatTextView) findViewById(R.id.tv);
        mTitleTv = (AppCompatTextView) findViewById(R.id.tv_title);
        llClick = (LinearLayout) findViewById(R.id.ll_click);
        TypedArray attrArray = context.obtainStyledAttributes(attributeSet, R.styleable.ManSpinner);
        int count = attrArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attrName = attrArray.getIndex(i);
            switch (attrName) {
                case R.styleable.ManSpinner_manspinner_name:
                    mText.setText(attrArray.getString(R.styleable.ManSpinner_manspinner_name));//控件名称
                    break;
                case R.styleable.ManSpinner_manspinner_title:
                    mSp.setPrompt(attrArray.getString(R.styleable.ManSpinner_manspinner_title));
                    break;
                case R.styleable.ManSpinner_manspinner_size:
//                    mTitleTv.setText(attrArray.getString(R.styleable.ManSpinner_manspinner_name));
//                    mTitleTv.setTextSize(attrArray.getDimension(R.styleable.ManSpinner_manspinner_size, 18));
//                    mText.setTextSize(attrArray.getDimension(R.styleable.ManSpinner_manspinner_size, 18));
                    break;
            }
        }
        attrArray.recycle();
        mSp.setPrompt("请选择"+mText.getText().toString());
        adapter = new EmployeeSpAdapter(context, container);
        adapterDlg = new EmployeeSpAdapter(context, container);
        mSp.setAdapter(adapter);



        mSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Employee employee = (Employee) adapter.getItem(i);
                employeeId = employee.FItemID;
                employeeName = employee.FName;
                setTitleText(employeeName);
                Lg.e("选中"+mText.getText().toString(),employee);
                Hawk.put(saveKeyString,employee.FName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
                /*//点击文字时，触发spinner点击*/
        mTitleTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDlg(context);
//                mSp.performClick();
            }
        });
        llClick.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDlg(context);
            }
        });
    }

    AlertDialog alertDialog;
    private void showDlg(Context context){
        AlertDialog.Builder ab = new AlertDialog.Builder(context);
        ab.setTitle(mText.getText().toString()+"过滤");
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
                Employee storage = (Employee) adapterDlg.getItem(i);
                for (int j = 0; j < adapter.getCount(); j++) {
                    if (storage.FName.equals(((Employee)adapter.getItem(j)).FName)){
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

    private List<Employee> getLocData() {
        EmployeeDao employeeDao = daoSession.getEmployeeDao();
//        List<Storage> employees = employeeDao.queryBuilder().where(StorageDao.Properties.FOrg.eq(org)).build().list();
        List<Employee> employees = employeeDao.loadAll();
        return employees;
    }


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


    public String getEmployeeId() {
        return employeeId == null ? "" : employeeId;
    }

    public String getEmployeeName() {
        return employeeName == null ? "" : employeeName;
    }

    /**
     *
     * @param saveKeyStr        用于保存的key
//     * @param string            自动设置的z值
     * */
    public void setAutoSelection(String saveKeyStr,String string,boolean isnull) {
        isAddnull= isnull;//是否添加一个空的数据
        saveKeyString = saveKeyStr;
        autoString = string;
        Lg.e("带出人物："+autoString);
//        autoOrg = org==null?"":org.FOrgID;
        mTitleTv.setText("");
        final List<Employee> listTemp = getLocData();
        dealAuto(listTemp, false);
        if (share.getIsOL()) {
            ArrayList<Integer> choose = new ArrayList<>();
            choose.add(3);
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
                    EmployeeDao payTypeDao = daoSession.getEmployeeDao();
                    payTypeDao.deleteAll();
                    payTypeDao.insertOrReplaceInTx(dBean.employee);
                    payTypeDao.detachAll();
                    if (dBean.employee.size()>0 && container.size()<=0){
                        dealAuto(dBean.employee,false);
                    }
                }

                @Override
                public void onError(Throwable e) {
//                    super.onError(e);
                }
            });
        }

    }
    private void dealAuto(List<Employee> listData, boolean check) {
        container.clear();
//        if (check) {
//            for (int i = 0; i < listData.size(); i++) {
//                if (listData.get(i).FOrg.equals(autoOrg)) {
//                    container.add(listData.get(i));
//                }
//            }
//        } else {
        if (isAddnull){
            container.add(new Employee("","","","","",""));
            container.addAll(listData);
        }else{
            container.addAll(listData);
        }
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

    public EmployeeSpAdapter getAdapter() {
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

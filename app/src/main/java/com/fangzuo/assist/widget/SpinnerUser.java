package com.fangzuo.assist.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Adapter.EmployeeSpAdapter;
import com.fangzuo.assist.Adapter.LoginSpAdapter;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Dao.Employee;
import com.fangzuo.assist.Dao.User;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.JsonCreater;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.EmployeeDao;
import com.fangzuo.greendao.gen.UserDao;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

public class SpinnerUser extends RelativeLayout {
    // 返回按钮控件
    private Spinner mSp;
    // 标题Tv
    private TextView mTitleTv;
    private boolean showEd = false;
    //    private SpinnerAdapter adapter;
    private DaoSession daoSession;
    private ArrayList<String> autoList;
    private Context mContext;
    private BasicShareUtil share;
    private ArrayList<User> container;
    private LoginSpAdapter adapter;
    private String autoString="";//用于联网时，再次去自动设置值
    private String saveKeyString="";//用于保存数据的key
    private String employeeId="";
    private String employeeName="";
    private String T="人物：";     //3


    public SpinnerUser(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        LayoutInflater.from(context).inflate(R.layout.view_user_spinner, this);
        daoSession = GreenDaoManager.getmInstance(context).getDaoSession();
        mContext = context;
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
        adapter = new LoginSpAdapter(context, container);
        mSp.setAdapter(adapter);
        if (share.getIsOL()) {
//            LoadingUtil.show(mContext,"更新用户数据...");
            ArrayList<Integer> choose = new ArrayList<>();
            choose.add(12);
            if (share.getDatabaseIp().equals("") || share.getDataBase().equals("")){
//                Toast.showText(mContext,"不存在服务器数据");
                return;
            }
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
                    UserDao payTypeDao = daoSession.getUserDao();
                    payTypeDao.deleteAll();
                    payTypeDao.insertOrReplaceInTx(dBean.User);
                    payTypeDao.detachAll();
                    if (dBean.User.size()>0){
                        container.clear();
                        container.addAll(dBean.User);
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
            UserDao employeeDao = daoSession.getUserDao();
            List<User> employees = employeeDao.loadAll();
            container.addAll(employees);
            adapter.notifyDataSetChanged();
            setAutoSelection(saveKeyString,autoString);

//            Log.e("CommonMethod", "获取到本地数据：\n" + container.toString());
//        }


//        mSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Employee employee = (Employee) adapter.getItem(i);
//                employeeId = employee.FItemID;
//                employeeName = employee.FName;
//                Lg.e("选中业务员："+employee.toString());
//                Hawk.put(saveKeyString,employee.FName);
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
//    public void setAdapter(SpinnerAdapter adapter){
//        this.adapter = adapter;
//        mSp.setAdapter(adapter);
//    }
//    public void setSelection(int i){
//        mSp.setSelection(i);
//    }

    public void LoadUser(){
            ArrayList<Integer> choose = new ArrayList<>();
            choose.add(12);
        if (share.getDatabaseIp().equals("") || share.getDataBase().equals("")){
//            Toast.showText(mContext,"不存在服务器数据");
            return;
        }
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
//                    Lg.e("用户：",cBean.returnJson);
//                    Lg.e("用户：",cBean);
                    DownloadReturnBean dBean = JsonCreater.gson.fromJson(cBean.returnJson, DownloadReturnBean.class);
                    UserDao payTypeDao = daoSession.getUserDao();
                    payTypeDao.deleteAll();
                    payTypeDao.insertOrReplaceInTx(dBean.User);
                    payTypeDao.detachAll();
                    if (dBean.User.size()>0){
                        container.clear();
                        container.addAll(dBean.User);
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

    public String getEmployeeId() {
        return employeeId == null ? "" : employeeId;
    }

    public String getEmployeeName() {
        return employeeName == null ? "" : employeeName;
    }

    /**
     *
     * @param saveKeyStr        用于保存的key
     * @param string            自动设置的z值
     * */
    public void setAutoSelection(String saveKeyStr,String string) {
        saveKeyString =saveKeyStr;
        autoString = string;
        if ("".equals(string)&&!"".equals(saveKeyStr)){
            autoString = Hawk.get(saveKeyString,"");
        }
        for (int j = 0; j < adapter.getCount(); j++) {
            if (((User) adapter.getItem(j)).FName.equals(autoString)
                    || ((User) adapter.getItem(j)).FUserID.equals(autoString)) {
                mSp.setSelection(j);
//                autoString = null;
                break;
            }
        }
    }

    public LoginSpAdapter getAdapter() {
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

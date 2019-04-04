package com.fangzuo.assist.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Adapter.ClientSpAdapter;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Dao.Client;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Utils.Asynchttp;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.JsonCreater;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.greendao.gen.ClientDao;
import com.fangzuo.greendao.gen.DaoSession;
import com.loopj.android.http.AsyncHttpClient;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

public class SpinnerClient extends RelativeLayout {
    // 返回按钮控件
    private Spinner mSp;
    // 标题Tv
    private TextView mTitleTv;
    private EditText editText;
    private boolean showEd = false;
    //    private SpinnerAdapter adapter;
    private DaoSession daoSession;
    private ArrayList<String> autoList;
    private BasicShareUtil share;
    private ArrayList<Client> container;
    private ArrayList<Client> containerTemp;
    private ClientSpAdapter adapter;
    private String autoString="";//用于联网时，再次去自动设置值
    private String saveKeyString="";//用于保存数据的key
    private String employeeId="";
    private String employeeName="";
    private String employeeNumber="";
    private String T="客户：";     //3


    public SpinnerClient(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        LayoutInflater.from(context).inflate(R.layout.view_client_spinner, this);
        daoSession = GreenDaoManager.getmInstance(context).getDaoSession();

        autoList = new ArrayList<>();
        share = BasicShareUtil.getInstance(context);
        container = new ArrayList<>();
        containerTemp = new ArrayList<>();
        // 获取控件
        mSp = (Spinner) findViewById(R.id.sp);
        mTitleTv = (TextView) findViewById(R.id.tv);
        editText=findViewById(R.id.ed);
        TypedArray attrArray = context.obtainStyledAttributes(attributeSet, R.styleable.ClientSpinner);
        int count = attrArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attrName = attrArray.getIndex(i);
            switch (attrName) {
                case R.styleable.ClientSpinner_clientspinner_name:
                    mTitleTv.setText(attrArray.getString(R.styleable.ClientSpinner_clientspinner_name));
                    break;
                case R.styleable.ClientSpinner_clientspinner_size:
                    mTitleTv.setText(attrArray.getString(R.styleable.ClientSpinner_clientspinner_name));
                    mTitleTv.setTextSize(attrArray.getDimension(R.styleable.ClientSpinner_clientspinner_size,10));
                    break;
                case R.styleable.ClientSpinner_clientspinner_hint:
                    editText.setHint(attrArray.getString(R.styleable.ClientSpinner_clientspinner_hint));
                    break;
            }
        }
        attrArray.recycle();
        adapter = new ClientSpAdapter(context, container);
        mSp.setAdapter(adapter);
        if (share.getIsOL()) {
            ArrayList<Integer> choose = new ArrayList<>();
            choose.add(13);
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
                    Lg.e("得到Client："+commonResponse.returnJson);
                    DownloadReturnBean dBean = JsonCreater.gson.fromJson(commonResponse.returnJson, DownloadReturnBean.class);
//                    Lg.e("得到Client：",dBean.clients.get(0));
                    ClientDao payTypeDao = daoSession.getClientDao();
                    payTypeDao.deleteAll();
                    payTypeDao.insertOrReplaceInTx(dBean.clients);
                    payTypeDao.detachAll();
                    if (dBean.clients.size() > 0 && container.size()<=1){
                        container.add(new Client("","","","","","","","","","",""));
                        container.addAll(dBean.clients);
                        adapter.notifyDataSetChanged();
//                        setAutoSelection(saveKeyString,autoString);
                    }
                }

                @Override
                public void onError(Throwable e) {
//                    super.onError(e);
                }
            });
        }

        ClientDao inStoreTypeDao = daoSession.getClientDao();
        List<Client> inStoreTypes = inStoreTypeDao.loadAll();
        container.add(new Client("","","","","","","","","","",""));
        container.addAll(inStoreTypes);
        adapter.notifyDataSetChanged();
//        setAutoSelection(saveKeyString,autoString);

        mSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Client employee = (Client) adapter.getItem(i);
                employeeId = employee.FItemID;
                employeeName = employee.FName;
                employeeNumber = employee.FNumber;
                Lg.e("选中"+T,employee);
                Hawk.put(saveKeyString,employee.FName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //根据搜索框，过滤相似信息
        editText.addTextChangedListener(new TextWatcher() {
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
                if ("".equals(editText.getText().toString().trim())){
                    adapter.clear();
                    ClientDao inStoreTypeDao = daoSession.getClientDao();
                    List<Client> inStoreTypes = inStoreTypeDao.loadAll();
                    container.add(new Client("","","","","","","","","","",""));
                    container.addAll(inStoreTypes);
                    mSp.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }else{
                    container.clear();
                    List<Client> inStoreTypes = daoSession.getClientDao().loadAll();
                    container.addAll(inStoreTypes);
                    for (int i = 0; i < container.size(); i++) {
                        if (container.get(i).FName.contains(editText.getText().toString())){
                            containerTemp.add(container.get(i));
                        }
                    }
                    adapter.addData(containerTemp);
                    mSp.setAdapter(adapter);
                }
                if (adapter.getCount()<=0){
                    employeeId = "";
                    employeeName = "";
                    employeeNumber = "";
                    Lg.e("重置");
                }
            }
        });

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


    public String getDataId() {
        return employeeId == null ? "" : employeeId;
    }

    public String getDataName() {
        return employeeName == null ? "" : employeeName;
    }
    public String getDataNumber() {
        return employeeNumber == null ? "" : employeeNumber;
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
            if (((Client) adapter.getItem(j)).FNumber.equals(autoString)
                    || ((Client) adapter.getItem(j)).FName.equals(autoString)) {
                mSp.setSelection(j);
//                autoString = null;
                break;
            }
        }
    }

    public ClientSpAdapter getAdapter() {
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

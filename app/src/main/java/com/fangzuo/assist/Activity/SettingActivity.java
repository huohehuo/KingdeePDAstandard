package com.fangzuo.assist.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.ConnectResponseBean;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Server.WebAPI;
import com.fangzuo.assist.Service.DataService;
import com.fangzuo.assist.Utils.Asynchttp;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.CallBack;
import com.fangzuo.assist.Utils.DataBaseAdapter;
import com.fangzuo.assist.Utils.DownLoadData;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.Info;
import com.fangzuo.assist.Utils.JsonCreater;
import com.fangzuo.assist.Utils.RetrofitUtil;
import com.fangzuo.assist.Utils.ShareUtil;
import com.fangzuo.assist.Utils.SnackBarUtil;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.assist.widget.LoadingUtil;
import com.fangzuo.greendao.gen.DaoMaster;
import com.fangzuo.greendao.gen.DaoSession;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SettingActivity extends BaseActivity {

    @BindView(R.id.btn_back)
    RelativeLayout btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private DataBaseAdapter adapter;
    private ListView mLvDataBase;
    private EditText mEtUserName;
    private EditText mEtPassword;
    private EditText mEtServerIP;
    private EditText mEtServerPort;
    private Button mBtnConn;
    private Button mBtnProp;
    private Button mBtnDownload;
    private SettingActivity mContext;
    private CommonListener commonListener;
    private Gson gson;
//    private ProgressDialog pg;
    private ArrayList<ConnectResponseBean.DataBaseList> container;
    private BasicShareUtil share;
    private String chooseDatabase;
    private long nowTime;
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private DaoSession session;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    long nowTime = (long) msg.obj;
                    int size = msg.arg1;
                    long endTime = System.currentTimeMillis();
                    AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                    ab.setTitle("下载完成");
                    ab.setMessage("耗时:" + (endTime - nowTime) + "ms" + ",共插入" + size + "条数据");
                    ab.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startNewActivity(LoginActivity.class, R.anim.activity_slide_left_in, R.anim.activity_slide_left_out, true, null);
                        }
                    });
                    ab.create().show();
                    break;
            }
        }
    };
    private int size;
    private int flag = 1;
    private CoordinatorLayout containerView;

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    public void onEventBusCome(ClassEvent event) {
        switch (event.Msg) {
            case EventBusInfoCode.DownData_OK:
                String result = (String) event.postEvent;
                LoadingUtil.dismiss();
                if ("0".equals(result)) {
                    long nowTime = Long.parseLong(event.Msg2);
                    int size = Integer.parseInt(event.Msg3);
                    long endTime = System.currentTimeMillis();
                    AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                    ab.setTitle("下载完成");
                    ab.setMessage("耗时:" + (endTime - nowTime) + "ms" + ",共插入" + size + "条数据");
                    ab.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startNewActivity(LoginActivity.class, R.anim.activity_slide_left_in, R.anim.activity_slide_left_out, true, null);
                        }
                    });
                    ab.create().show();
                } else {
                    AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                    ab.setTitle("下载错误");
                    ab.setPositiveButton("确认", null);
                    ab.create().show();
                }
                break;
        }
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        tvTitle.setText("下载配置");
        containerView = findViewById(R.id.container);
        mLvDataBase = findViewById(R.id.lv_database);
        mEtUserName = findViewById(R.id.ed_username);
        mEtPassword = findViewById(R.id.ed_pass);
        mEtServerIP = findViewById(R.id.ed_serverip);
        mEtServerPort = findViewById(R.id.ed_port);
        mBtnConn = findViewById(R.id.btn_connect);
        mBtnProp = findViewById(R.id.btn_prop);
        mBtnDownload = findViewById(R.id.btn_download);

    }

    @Override
    public void initData() {
        mContext = this;
        gson = new Gson();
        container = new ArrayList<>();
        commonListener = new CommonListener();
//
//        pg = new ProgressDialog(mContext);
//        pg.setMessage("请稍后...");
//        pg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        share = BasicShareUtil.getInstance(mContext);

        //为了测试
        if (!share.getDatabaseIp().equals("")) {
            mEtServerIP.setText(share.getDatabaseIp());
            mEtServerPort.setText(share.getDatabasePort());
            mEtUserName.setText(share.getDataBaseUser());
            mEtPassword.setText(share.getDataBasePass());
        }

        session = GreenDaoManager.getmInstance(mContext).getDaoSession();
    }

    @Override
    public void initListener() {
        mBtnConn.setOnClickListener(commonListener);
        mBtnProp.setOnClickListener(commonListener);
        mBtnDownload.setOnClickListener(commonListener);
        mLvDataBase.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    adapter.setIsCheck(i);
                    adapter.notifyDataSetChanged();
                }
                chooseDatabase = container.get(i).dataBaseName;
                Toast.showText(mContext, chooseDatabase);

            }
        });

    }

    @Override
    protected void OnReceive(String code) {

    }


    @OnClick({R.id.btn_back, R.id.tv_title})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_title:
                break;
        }
    }

    private class CommonListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_connect:
                    connectToSQL();
                    break;
                case R.id.btn_prop:
                    prop();
                    break;
                case R.id.btn_download:
                    DownLoadData.getInstance(mContext, containerView, handler).alertToChoose();
                    break;
            }
        }
    }


    private void prop() {
        AlertDialog.Builder ab1 = new AlertDialog.Builder(mContext);
        ab1.setTitle("是否配置");
        ab1.setMessage("配置将会清空所有数据（包括已做单据）");
        ab1.setPositiveButton("清空", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setprop(true);
            }
        });
        ab1.setNeutralButton("不清空", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setprop(false);
            }
        });
        ab1.setNegativeButton("取消", null);

        ab1.create().show();

    }

    private void setprop(final boolean isClear) {
        LoadingUtil.show(mContext,"正在配置...");
        final AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
        String json = JsonCreater.ConnectSQL(share.getDatabaseIp(), share.getDatabasePort(), share.getDataBaseUser(), share.getDataBasePass(), chooseDatabase);
        RetrofitUtil.getInstance(mContext).createReq(WebAPI.class).SetProp(RetrofitUtil.getParams(mContext, json))
                .enqueue(new CallBack() {
                    @Override
                    public void onSucceed(CommonResponse cBean) {
                        if (isClear) {
                            DataService.deleteAll(mContext);
                            deleteData();
                        }
                        LoadingUtil.dismiss();
                        ab.setTitle("配置结果");
                        ab.setMessage("配置成功，请继续下一步操作");
                        ab.setPositiveButton("确认", null);
                        ab.create().show();
                        share.setVersion(cBean.returnJson);
                        share.setDataBase(chooseDatabase);
                    }

                    @Override
                    public void OnFail(String Msg) {
                        LoadingUtil.dismiss();
                        ab.setTitle("配置结果");
                        ab.setMessage(Msg);
                        ab.setPositiveButton("确认", null);
                        ab.setNegativeButton("重试", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                prop();
                            }
                        });
                        ab.create().show();
                    }
                });
    }

    private void deleteData() {
//        session.getBibieDao().deleteAll();
//        session.getBarCodeDao().deleteAll();
//        session.getT_DetailDao().deleteAll();
//        session.getT_mainDao().deleteAll();
//        session.getClientDao().deleteAll();
//        session.getDepartmentDao().deleteAll();
//        session.getEmployeeDao().deleteAll();
//        session.getGetGoodsDepartmentDao().deleteAll();
//        session.getInStorageNumDao().deleteAll();
//        session.getInStoreTypeDao().deleteAll();
//        session.getPayTypeDao().deleteAll();
//        session.getPDMainDao().deleteAll();
//        session.getPDSubDao().deleteAll();
//        session.getPriceMethodDao().deleteAll();
//        session.getProductDao().deleteAll();
//        session.getPurchaseMethodDao().deleteAll();
//        session.getPushDownMainDao().deleteAll();
//        session.getPushDownSubDao().deleteAll();
//        session.getStorageDao().deleteAll();
//        session.getSuppliersDao().deleteAll();
//        session.getUnitDao().deleteAll();
//        session.getUserDao().deleteAll();
//        session.getWanglaikemuDao().deleteAll();
//        session.getWaveHouseDao().deleteAll();
//        session.getYuandanTypeDao().deleteAll();

//        share.clear();
        ShareUtil share2 = ShareUtil.getInstance(mContext);
        share2.clear();
    }

    private void connectToSQL() {
        LoadingUtil.show(mContext,"正在连接...");
        String json = JsonCreater.ConnectSQL(mEtServerIP.getText().toString(), mEtServerPort.getText().toString(),
                mEtUserName.getText().toString(), mEtPassword.getText().toString(), Info.DATABASESETTING);
        Asynchttp.post(mContext, getBaseUrl() + WebApi.CONNECTSQL, json, new Asynchttp.Response() {


            @Override
            public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                share.setDatabaseIp(mEtServerIP.getText().toString());
                share.setDatabasePort(mEtServerPort.getText().toString());
                share.setDataBaseUser(mEtUserName.getText().toString());
                share.setDataBasePass(mEtPassword.getText().toString());
                LoadingUtil.dismiss();
                ConnectResponseBean connectBean = gson.fromJson(cBean.returnJson, ConnectResponseBean.class);
                container.clear();
                ConnectResponseBean connectResponseBean = new ConnectResponseBean();
                ConnectResponseBean.DataBaseList dBean = connectResponseBean.new DataBaseList();
                dBean.name = "账套";
                dBean.dataBaseName = "数据库";
                container.add(dBean);
                container.addAll(connectBean.DataBaseList);
                adapter = new DataBaseAdapter(mContext, container);
                mLvDataBase.setAdapter(adapter);
                Toast.showText(mContext, "获取了" + connectBean.DataBaseList.size() + "条数据");
            }

            @Override
            public void onFailed(String Msg, AsyncHttpClient client) {
                LoadingUtil.dismiss();
                Toast.showText(mContext,"连接失败，请重试");
//                SnackBarUtil.LongSnackbar(containerView, Msg, SnackBarUtil.Alert).setAction("重试", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        connectToSQL();
//                    }
//                }).show();


            }
        });
    }


}

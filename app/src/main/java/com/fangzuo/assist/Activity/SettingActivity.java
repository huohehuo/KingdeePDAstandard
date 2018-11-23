package com.fangzuo.assist.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Adapter.DataSearchRyAdapter;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.ConnectResponseBean;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Service.DataService;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.DataBaseAdapter;
import com.fangzuo.assist.Utils.DataModel;
import com.fangzuo.assist.Utils.DownLoadData;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.Info;
import com.fangzuo.assist.Utils.JsonCreater;
import com.fangzuo.assist.Utils.ShareUtil;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.widget.LoadingUtil;
import com.fangzuo.greendao.gen.DaoMaster;
import com.fangzuo.greendao.gen.DaoSession;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SettingActivity extends BaseActivity implements DataSearchRyAdapter.OnItemClickListener {

    @BindView(R.id.btn_back)
    RelativeLayout btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ry_data_search)
    RecyclerView ryDataSearch;
    @BindView(R.id.btn_connect)
    Button btnConnect;
    @BindView(R.id.btn_prop)
    Button btnProp;
    @BindView(R.id.btn_download)
    Button btnDownload;
    @BindView(R.id.ed_serverip)
    EditText edServerip;
    @BindView(R.id.ed_port)
    EditText edPort;
    @BindView(R.id.ed_username)
    EditText edUsername;
    @BindView(R.id.ed_pass)
    EditText edPass;
    @BindView(R.id.lv_database)
    ListView lvDatabase;
    @BindView(R.id.container)
    CoordinatorLayout containerView;
    private DataBaseAdapter adapter;
    private SettingActivity mContext;
    private ArrayList<ConnectResponseBean.DataBaseList> container;
    private BasicShareUtil share;
    private String chooseDatabase;
    private long nowTime;
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private DataSearchRyAdapter dataSearchRyAdapter;

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

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    public void onEventBusCome(ClassEvent event) {
        switch (event.Msg) {
            case EventBusInfoCode.Connect_OK:
                CommonResponse commonResponse = (CommonResponse) event.postEvent;
                share.setDatabaseIp(edServerip.getText().toString());
                share.setDatabasePort(edPort.getText().toString());
                share.setDataBaseUser(edUsername.getText().toString());
                share.setDataBasePass(edPass.getText().toString());
                LoadingUtil.dismiss();
                ConnectResponseBean connectBean = gson.fromJson(commonResponse.returnJson, ConnectResponseBean.class);
                dataSearchRyAdapter.addAll(connectBean.DataBaseList);
                adapter = new DataBaseAdapter(mContext, container);
                lvDatabase.setAdapter(adapter);
                Toast.showText(mContext, "获取了" + connectBean.DataBaseList.size() + "条数据");
                break;
            case EventBusInfoCode.Connect_Error:
                String str = (String) event.postEvent;
                LoadingUtil.dismiss();
                Toast.showText(mContext, "连接错误:" + str);
                break;
            case EventBusInfoCode.Prop_OK:
                CommonResponse prop = (CommonResponse) event.postEvent;
                final AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                if (prop.state) {
                    if (isClearAll) {
                        DataService.deleteAll(mContext);
                        ShareUtil.getInstance(mContext).clear();
                    }
                    LoadingUtil.dismiss();
                    ab.setTitle("配置结果");
                    ab.setMessage("配置成功，请继续下一步操作");
                    ab.setPositiveButton("确认", null);
                    ab.create().show();
                    share.setVersion(prop.returnJson);
                    share.setDataBase(chooseDatabase);
                } else {
                    LoadingUtil.dismiss();
                    ab.setTitle("配置结果");
                    ab.setMessage(prop.returnJson);
                    ab.setPositiveButton("确认", null);
                    ab.setNegativeButton("重试", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            prop();
                        }
                    });
                    ab.create().show();
                }
                isClearAll = false;
                break;
            case EventBusInfoCode.Prop_Error:
                String str2 = (String) event.postEvent;
                final AlertDialog.Builder ab2 = new AlertDialog.Builder(mContext);
                LoadingUtil.dismiss();
                ab2.setTitle("配置结果");
                ab2.setMessage(str2);
                ab2.setPositiveButton("确认", null);
                ab2.setNegativeButton("重试", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        prop();
                    }
                });
                ab2.create().show();
                break;
        }
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        tvTitle.setText("下载配置");
    }

    @Override
    public void initData() {
        mContext = this;
        container = new ArrayList<>();
        share = BasicShareUtil.getInstance(mContext);

        //为了测试
        if (!share.getDatabaseIp().equals("")) {
            edServerip.setText(share.getDatabaseIp());
            edPort.setText(share.getDatabasePort());
            edUsername.setText(share.getDataBaseUser());
            edPass.setText(share.getDataBasePass());
        }

        dataSearchRyAdapter = new DataSearchRyAdapter(mContext, container);
        ryDataSearch.setAdapter(dataSearchRyAdapter);
        ryDataSearch.setItemAnimator(new DefaultItemAnimator());
        ryDataSearch.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        dataSearchRyAdapter.setOnItemClickListener(this);
        dataSearchRyAdapter.notifyDataSetChanged();
    }

    @Override
    public void initListener() {


//        lvDatabase.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                if (i != 0) {
//                    adapter.setIsCheck(i);
//                    adapter.notifyDataSetChanged();
//                }
//                chooseDatabase = container.get(i).dataBaseName;
//                Toast.showText(mContext, chooseDatabase);
//            }
//        });

    }

    @Override
    protected void OnReceive(String code) {

    }


    @Override
    public void onItemClick(View view, int position) {

//        adapter.setIsCheck(position);
//        adapter.notifyDataSetChanged();
        dataSearchRyAdapter.setIsCheck(position);
        chooseDatabase = container.get(position).dataBaseName;
        Toast.showText(mContext, chooseDatabase);
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    @OnClick({R.id.btn_back, R.id.btn_connect, R.id.btn_prop, R.id.btn_download})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
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

    private boolean isClearAll = false;

    private void setprop(final boolean isClear) {
        if (null == chooseDatabase) {
            Toast.showText(mContext, "请选择账套");
            return;
        }
        isClearAll = isClear;
        LoadingUtil.show(mContext, "正在配置...");
        DataModel.SetProp(JsonCreater.ConnectSQL(
                share.getDatabaseIp(),
                share.getDatabasePort(),
                share.getDataBaseUser(),
                share.getDataBasePass(),
                chooseDatabase));
//        String json = JsonCreater.ConnectSQL(share.getDatabaseIp(), share.getDatabasePort(), share.getDataBaseUser(), share.getDataBasePass(), chooseDatabase);
//        RetrofitUtil.getInstance(mContext).createReq(WebAPI.class).SetProp(RetrofitUtil.getParams(mContext, json))
//                .enqueue(new CallBack() {
//                    @Override
//                    public void onSucceed(CommonResponse cBean) {
//                        if (isClear) {
//                            DataService.deleteAll(mContext);
//                            deleteData();
//                        }
//                        LoadingUtil.dismiss();
//                        ab.setTitle("配置结果");
//                        ab.setMessage("配置成功，请继续下一步操作");
//                        ab.setPositiveButton("确认", null);
//                        ab.create().show();
//                        share.setVersion(cBean.returnJson);
//                        share.setDataBase(chooseDatabase);
//                    }
//
//                    @Override
//                    public void OnFail(String Msg) {
//                        LoadingUtil.dismiss();
//                        ab.setTitle("配置结果");
//                        ab.setMessage(Msg);
//                        ab.setPositiveButton("确认", null);
//                        ab.setNegativeButton("重试", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                prop();
//                            }
//                        });
//                        ab.create().show();
//                    }
//                });
    }

    private void connectToSQL() {
        LoadingUtil.showDialog(mContext, "正在连接...");
        DataModel.SetConnectSQL(JsonCreater.ConnectSQL(
                edServerip.getText().toString(),
                edPort.getText().toString(),
                edUsername.getText().toString(),
                edPass.getText().toString(),
                Info.DATABASESETTING));
//        String json = JsonCreater.ConnectSQL(edServerip.getText().toString(), edPort.getText().toString(),
//                edUsername.getText().toString(), edPass.getText().toString(), Info.DATABASESETTING);
//        Asynchttp.post(mContext, getBaseUrl() + WebApi.CONNECTSQL, json, new Asynchttp.Response() {
//
//
//            @Override
//            public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
//                share.setDatabaseIp(edServerip.getText().toString());
//                share.setDatabasePort(edPort.getText().toString());
//                share.setDataBaseUser(edUsername.getText().toString());
//                share.setDataBasePass(edPass.getText().toString());
//                LoadingUtil.dismiss();
//                ConnectResponseBean connectBean = gson.fromJson(cBean.returnJson, ConnectResponseBean.class);
//                container.clear();
//                ConnectResponseBean connectResponseBean = new ConnectResponseBean();
//                ConnectResponseBean.DataBaseList dBean = connectResponseBean.new DataBaseList();
//                dBean.name = "账套";
//                dBean.dataBaseName = "数据库";
//                container.add(dBean);
//                container.addAll(connectBean.DataBaseList);
//                adapter = new DataBaseAdapter(mContext, container);
//                lvDatabase.setAdapter(adapter);
//                Toast.showText(mContext, "获取了" + connectBean.DataBaseList.size() + "条数据");
//            }
//
//            @Override
//            public void onFailed(String Msg, AsyncHttpClient client) {
//                LoadingUtil.dismiss();
//                Toast.showText(mContext,"连接失败，请重试");
////                SnackBarUtil.LongSnackbar(containerView, Msg, SnackBarUtil.Alert).setAction("重试", new View.OnClickListener() {
////                    @Override
////                    public void onClick(View view) {
////                        connectToSQL();
////                    }
////                }).show();
//
//
//            }
//        });
    }


}

package com.fangzuo.assist.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Adapter.DataSearchRyAdapter;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.ConnectResponseBean;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Service.DataService;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.CommonUtil;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.DataBaseAdapter;
import com.fangzuo.assist.Utils.DataModel;
import com.fangzuo.assist.Utils.DownLoadData;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.Info;
import com.fangzuo.assist.Utils.JsonCreater;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.ShareUtil;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.assist.widget.LoadingUtil;
import com.fangzuo.greendao.gen.BarCodeDao;
import com.fangzuo.greendao.gen.BibieDao;
import com.fangzuo.greendao.gen.ClientDao;
import com.fangzuo.greendao.gen.DaoMaster;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.DepartmentDao;
import com.fangzuo.greendao.gen.EmployeeDao;
import com.fangzuo.greendao.gen.GetGoodsDepartmentDao;
import com.fangzuo.greendao.gen.InStorageNumDao;
import com.fangzuo.greendao.gen.InStoreTypeDao;
import com.fangzuo.greendao.gen.PayTypeDao;
import com.fangzuo.greendao.gen.PriceMethodDao;
import com.fangzuo.greendao.gen.ProductDao;
import com.fangzuo.greendao.gen.PurchaseMethodDao;
import com.fangzuo.greendao.gen.StorageDao;
import com.fangzuo.greendao.gen.SuppliersDao;
import com.fangzuo.greendao.gen.UnitDao;
import com.fangzuo.greendao.gen.UserDao;
import com.fangzuo.greendao.gen.WanglaikemuDao;
import com.fangzuo.greendao.gen.WaveHouseDao;
import com.fangzuo.greendao.gen.YuandanTypeDao;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.greenrobot.greendao.async.AsyncSession;

import java.io.File;
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
    private ProgressDialog pDialog;
    private DaoSession session;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    LoadingUtil.dismiss();
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
//                String str = (String) event.postEvent;
//                LoadingUtil.dismiss();
//                Toast.showText(mContext, "连接错误:" + str);
                final AlertDialog.Builder adCError = new AlertDialog.Builder(mContext);
                adCError.setTitle("连接错误");
                adCError.setMessage((String)event.postEvent);
                adCError.setPositiveButton("确认", null);
                LoadingUtil.dismiss();
                adCError.create().show();
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
        session = GreenDaoManager.getmInstance(mContext).getDaoSession();

        if (App.PDA_Choose==5){
            try{
                //设置新大陆的扫描模式为广播模式
                Intent intent = new Intent ("ACTION_BAR_SCANCFG");
                intent.putExtra("EXTRA_SCAN_MODE", 3);
                mContext.sendBroadcast(intent);
            }catch (Exception e){}
        }
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
        btnDownload.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder ab1 = new AlertDialog.Builder(mContext);
                ab1.setTitle("是否以文件包形式下载数据");
                ab1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        downForFile();
                    }
                });
                ab1.setNeutralButton("取消", null);
                ab1.create().show();
                return true;
            }
        });

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

    //让服务端下载好数据
    private void downForFile(){
        LoadingUtil.showDialog(mContext,"服务器正在准备数据...");
        String json = JsonCreater.DownLoadData(share.getDatabaseIp(),
                share.getDatabasePort(), share.getDataBaseUser(), share.getDataBasePass(),
                share.getDataBase(), share.getVersion(), new ArrayList<Integer>());
        nowTime = System.currentTimeMillis();
        App.getRService().doIOAction(WebApi.DownloadInfoForFile,json, new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                LoadingUtil.dismiss();
                if (commonResponse.state){
                    DownLoad(getBaseUrl()+ Config.Data_Url);
                }else{
                    Toast.showText(mContext,commonResponse.returnJson);
                }
            }
            @Override
            public void onError(Throwable e) {
                LoadingUtil.dismiss();
                Toast.showText(mContext,"下载错误:"+e.toString());
            }
        });
    }
    //进行下载
    private void DownLoad(String downLoadURL) {
        Lg.e("数据下载链接："+downLoadURL);
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {

            pDialog = new ProgressDialog(mContext);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setTitle("下载中");
            pDialog.show();
            final String target = Environment.getExternalStorageDirectory()
                    + "/AllData.txt";
            HttpUtils utils = new HttpUtils();
            utils.download(downLoadURL, target, new RequestCallBack<File>() {
                @Override
                public void onLoading(long total, long current,
                                      boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                    System.out.println("下载进度:" + current + "/" + total);
                    pDialog.setProgress((int) (current*100/total));
                }

                @Override
                public void onSuccess(ResponseInfo<File> arg0) {
                    pDialog.dismiss();
                    Lg.e("下载完成：");
                    Toast.showText(mContext,"下载数据包完成");
                    doData(target);
                }

                @Override
                public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
                    pDialog.dismiss();
                    Toast.showText(mContext, "下载失败");
                }


            });
        } else {
            pDialog.dismiss();
            Toast.showText(mContext, "正在安装");

        }
    }
    //解析
    private void doData(final String name){
        LoadingUtil.showDialog(mContext,"正在解析...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                DownloadReturnBean dBean = new Gson().fromJson(CommonUtil.getString(name), DownloadReturnBean.class);
                Lg.e("获得对象并解析：",dBean);
                insert(dBean);
            }
        }).start();
    }
    private void insert(final DownloadReturnBean dBean) {
        AsyncSession asyncSession = session.startAsyncSession();
        AsyncSession asyncSession2 = session.startAsyncSession();
        asyncSession.runInTx(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                boolean b = insertLocalSQLite(dBean);
                Log.e("result", b + "");
                if (b) {
                    message.what = 1;
                    message.obj = nowTime;
                    message.arg1 = dBean.size;
                }
                handler.sendMessage(message);
            }
        });
        asyncSession2.runInTx(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                boolean b = insertLocalSQLite2(dBean);
                Log.e("result", b + "");

            }

        });
        try{
            LoadingUtil.dismiss();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    private boolean insertLocalSQLite2(DownloadReturnBean dBean) {
        if (dBean.bibiezhongs != null && dBean.bibiezhongs.size() > 0) {
            BibieDao bibieDao = session.getBibieDao();
            bibieDao.deleteAll();
            bibieDao.insertOrReplaceInTx(dBean.bibiezhongs);
            bibieDao.detachAll();
        }

        if (dBean.department != null && dBean.department.size() > 0) {
            DepartmentDao departmentDao = session.getDepartmentDao();
            departmentDao.deleteAll();
            departmentDao.insertOrReplaceInTx(dBean.department);
            departmentDao.detachAll();
        }
        if (dBean.employee != null && dBean.employee.size() > 0) {
            EmployeeDao employeeDao = session.getEmployeeDao();
            employeeDao.deleteAll();
            employeeDao.insertOrReplaceInTx(dBean.employee);
            employeeDao.detachAll();
        }
        if (dBean.wavehouse != null && dBean.wavehouse.size() > 0) {
            WaveHouseDao wavehouseDao = session.getWaveHouseDao();
            wavehouseDao.deleteAll();
            wavehouseDao.insertOrReplaceInTx(dBean.wavehouse);
            wavehouseDao.detachAll();
        }
        if (dBean.InstorageNum != null && dBean.InstorageNum.size() > 0) {
            InStorageNumDao storageNumDao = session.getInStorageNumDao();
            storageNumDao.deleteAll();
            storageNumDao.insertOrReplaceInTx(dBean.InstorageNum);
            storageNumDao.detachAll();
        }
        if (dBean.storage != null && dBean.storage.size() > 0) {
            StorageDao storageDao = session.getStorageDao();
            storageDao.deleteAll();
            storageDao.insertOrReplaceInTx(dBean.storage);
            storageDao.detachAll();
        }
        if (dBean.units != null && dBean.units.size() > 0) {
            UnitDao unitDao = session.getUnitDao();
            unitDao.deleteAll();
            unitDao.insertOrReplaceInTx(dBean.units);
            unitDao.detachAll();
        }

        if (dBean.suppliers != null && dBean.suppliers.size() > 0) {
            SuppliersDao suppliersDao = session.getSuppliersDao();
            suppliersDao.deleteAll();
            suppliersDao.insertOrReplaceInTx(dBean.suppliers);
            suppliersDao.detachAll();
        }
        if (dBean.payTypes != null && dBean.payTypes.size() > 0) {
            PayTypeDao payTypeDao = session.getPayTypeDao();
            payTypeDao.deleteAll();
            payTypeDao.insertOrReplaceInTx(dBean.payTypes);
            payTypeDao.detachAll();
        }
        if (dBean.products != null && dBean.products.size() > 0) {
            ProductDao productDao = session.getProductDao();
            productDao.deleteAll();
            productDao.insertOrReplaceInTx(dBean.products);
            productDao.detachAll();
        }
        return true;
    }

    private boolean insertLocalSQLite(DownloadReturnBean dBean) {
        if (dBean.BarCode != null && dBean.BarCode.size() > 0) {
            Lg.e("添加BarCode");
            BarCodeDao barCodeDao = session.getBarCodeDao();
            barCodeDao.deleteAll();
            barCodeDao.insertOrReplaceInTx(dBean.BarCode);
            barCodeDao.detachAll();
        }
        if (dBean.User != null && dBean.User.size() > 0) {
            Lg.e("添加User");

            UserDao userDao = session.getUserDao();
            userDao.deleteAll();
            userDao.insertOrReplaceInTx(dBean.User);
            userDao.detachAll();
        }
        if (dBean.clients != null && dBean.clients.size() > 0) {
            ClientDao clientDao = session.getClientDao();
            clientDao.deleteAll();
            clientDao.insertOrReplaceInTx(dBean.clients);
            clientDao.detachAll();
        }
        if (dBean.getGoodsDepartments != null && dBean.getGoodsDepartments.size() > 0) {
            GetGoodsDepartmentDao getGoodsDepartmentDao = session.getGetGoodsDepartmentDao();
            getGoodsDepartmentDao.deleteAll();
            getGoodsDepartmentDao.insertOrReplaceInTx(dBean.getGoodsDepartments);
            getGoodsDepartmentDao.detachAll();
        }
        if (dBean.purchaseMethod != null && dBean.purchaseMethod.size() > 0) {
            PurchaseMethodDao purchaseMethodDao = session.getPurchaseMethodDao();
            purchaseMethodDao.deleteAll();
            purchaseMethodDao.insertOrReplaceInTx(dBean.purchaseMethod);
            purchaseMethodDao.detachAll();
        }
        if (dBean.yuandanTypes != null && dBean.yuandanTypes.size() > 0) {
            YuandanTypeDao yuandanTypeDao = session.getYuandanTypeDao();
            yuandanTypeDao.deleteAll();
            yuandanTypeDao.insertOrReplaceInTx(dBean.yuandanTypes);
            yuandanTypeDao.detachAll();
        }
        if (dBean.wanglaikemu != null && dBean.wanglaikemu.size() > 0) {
            WanglaikemuDao wanglaikemuDao = session.getWanglaikemuDao();
            wanglaikemuDao.deleteAll();
            wanglaikemuDao.insertOrReplaceInTx(dBean.wanglaikemu);
            wanglaikemuDao.detachAll();
        }
        if (dBean.priceMethods != null && dBean.priceMethods.size() > 0) {
            PriceMethodDao priceMethodDao = session.getPriceMethodDao();
            priceMethodDao.deleteAll();
            priceMethodDao.insertOrReplaceInTx(dBean.priceMethods);
            priceMethodDao.detachAll();
        }
        if (dBean.inStorageTypes != null && dBean.inStorageTypes.size() > 0) {
            InStoreTypeDao inStoreTypeDao = session.getInStoreTypeDao();
            inStoreTypeDao.deleteAll();
            inStoreTypeDao.insertOrReplaceInTx(dBean.inStorageTypes);
            inStoreTypeDao.detachAll();
        }
        return true;
    }
}

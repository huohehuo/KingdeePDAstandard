package com.fangzuo.assist.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Adapter.AlertLvAdapter;
import com.fangzuo.assist.Adapter.PDListAdapter;
import com.fangzuo.assist.Adapter.PDMainSpAdapter;
import com.fangzuo.assist.Adapter.PiciSpAdapter;
import com.fangzuo.assist.Adapter.PiciSpForSubAdapter;
import com.fangzuo.assist.Adapter.ProductselectAdapter;
import com.fangzuo.assist.Adapter.ProductselectAdapter1;
import com.fangzuo.assist.Adapter.StorageSpAdapter;
import com.fangzuo.assist.Adapter.UnitSpAdapter;
import com.fangzuo.assist.Adapter.WaveHouseSpAdapter;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.Beans.GetBatchNoBean;
import com.fangzuo.assist.Beans.PDListReturnBean;
import com.fangzuo.assist.Beans.PDSubRequestBean;
import com.fangzuo.assist.Beans.PDsubReturnBean;
import com.fangzuo.assist.Beans.PurchaseInStoreUploadBean;
import com.fangzuo.assist.Dao.BarCode;
import com.fangzuo.assist.Dao.InStorageNum;
import com.fangzuo.assist.Dao.PDMain;
import com.fangzuo.assist.Dao.PDSub;
import com.fangzuo.assist.Dao.Product;
import com.fangzuo.assist.Dao.Storage;
import com.fangzuo.assist.Dao.T_Detail;
import com.fangzuo.assist.Dao.Unit;
import com.fangzuo.assist.Dao.WaveHouse;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Service.DataService;
import com.fangzuo.assist.Utils.Asynchttp;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.CommonMethod;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.DataModel;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.Info;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.MathUtil;
import com.fangzuo.assist.Utils.MediaPlayer;
import com.fangzuo.assist.Utils.ShareUtil;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.assist.widget.LoadingUtil;
import com.fangzuo.assist.widget.MyWaveHouseSpinner;
import com.fangzuo.assist.widget.SpinnerStorage;
import com.fangzuo.assist.widget.SpinnerUnit;
import com.fangzuo.assist.zxing.CustomCaptureActivity;
import com.fangzuo.assist.zxing.activity.CaptureActivity;
import com.fangzuo.greendao.gen.BarCodeDao;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.PDMainDao;
import com.fangzuo.greendao.gen.PDSubDao;
import com.fangzuo.greendao.gen.ProductDao;
import com.fangzuo.greendao.gen.T_DetailDao;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.loopj.android.http.AsyncHttpClient;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.fangzuo.assist.R.id.isAutoAdd;

public class PDActivity extends BaseActivity {


    @BindView(R.id.btn_delete)
    RelativeLayout btnDelete;
    @BindView(R.id.sp_pdplan)
    Spinner spPdplan;
    @BindView(R.id.sp_storage)
    SpinnerStorage spStorage;
    @BindView(R.id.sp_wavehouse)
    MyWaveHouseSpinner spWavehouse;
    @BindView(R.id.scanbyCamera)
    RelativeLayout scanbyCamera;
    @BindView(R.id.ed_code)
    EditText edCode;
    @BindView(R.id.search)
    RelativeLayout search;
    @BindView(R.id.tv_goodName)
    TextView tvGoodName;
    @BindView(R.id.tv_num_onServer)
    TextView tvNumOnServer;
    @BindView(R.id.tv_ypnum)
    TextView tvYtnum;
    @BindView(R.id.ed_pdnum)
    EditText edPdnum;
    @BindView(R.id.btn_add)
    Button btnAdd;
    @BindView(R.id.btn_backorder)
    Button btnBackorder;
    @BindView(R.id.btn_checkorder)
    Button btnCheckorder;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.btn_downloadall)
    Button btnDownloadall;
    @BindView(R.id.btn_downloadchoosen)
    Button btnDownloadchoosen;
    @BindView(R.id.cb_isClear)
    CheckBox cbIsClear;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.lv_pdlist)
    ListView lvPdlist;
    @BindView(R.id.mDrawer)
    DrawerLayout mDrawer;
    @BindView(R.id.sp_unit)
    SpinnerUnit spUnit;
    //    @BindView(R.id.ed_pihao)
//    EditText edPihao;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    @BindView(isAutoAdd)
    CheckBox autoAdd;
    @BindView(R.id.ishebing)
    CheckBox cbHebing;
    @BindView(R.id.sp_pihao)
    Spinner spPihao;
//    private Gson gson;
    private List<PDMain> mainContainer;
    private List<Boolean> isCheck;
    private PDListAdapter pdListAdapter;
    private ArrayList<String> choice;
//    private DaoSession daoSession;
    private ProgressDialog pg;
    private CommonMethod method;
    private PDMainSpAdapter pdMainSpAdapter;
    private String fid;
    private List<Product> products;
    private UnitSpAdapter unitAdapter;
    private String fprocessID;
//    private StorageSpAdapter storageAdapter;
    private PDSub pdsubChoice;
    private Storage storage;
    private String storageId;
    private String storageName;
    private WaveHouseSpAdapter waveHouseAdapter;
    private String wavehouseID = "0";
    private String wavehouseName;
//    private String unitId;
//    private String unitName;
//    private double unitrate;
    private PDSubDao pdSubDao;
    private T_DetailDao t_detailDao;
    private ProductselectAdapter productselectAdapter;
    private ProductselectAdapter1 productselectAdapter1;
    private Product product;
    private String default_unitID;
    private boolean fBatchManager;
    private String FInStoreDate;
    private String FSpplierID;
    private String FInStoreOrderID;
    private boolean isClear = false;
    private PDMain pdMain;
    private List<PDMain> choiceContainer;
    private boolean isHebing = true;
    private boolean isAuto;

    private String pihao = "";
    private PiciSpForSubAdapter piciSpAdapter;
    private PDSub pdSub;
    private String wavehouseAutoString = "";
    private int activity = Config.PDActivity;
    private PDActivity mContext;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_pd);
        mContext = this;
        ButterKnife.bind(this);
        initDrawer(mDrawer);
        pdSubDao = daoSession.getPDSubDao();
        isAuto = share.getPDisAuto();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(ClassEvent event) {
        switch (event.Msg) {
            case EventBusInfoCode.ScanResult://
                BarcodeResult res = (BarcodeResult) event.postEvent;
                OnReceive(res.getResult().getText());
                break;
            case EventBusInfoCode.PRODUCTRETURN:
                product = (Product) event.postEvent;
                getSubQty();
                setDATA("", true);
                break;
        }
    }

    @Override
    protected void initData() {
        cbHebing.setChecked(isHebing);
        autoAdd.setChecked(share.getPDisAuto());
        method = CommonMethod.getMethod(mContext);
        getBasicData();
        mainContainer = new ArrayList<>();
        isCheck = new ArrayList<>();
        choice = new ArrayList<>();
        choiceContainer = new ArrayList<>();
        pdListAdapter = new PDListAdapter(mContext, mainContainer, isCheck);
        lvPdlist.setAdapter(pdListAdapter);
        getList();

    }

    private void getBasicData() {
//        storageAdapter = method.getStorageSpinner(spStorage);
        spStorage.setAutoSelection(getString(R.string.spStorage_pd), "");

        pdMainSpAdapter = method.getpdmain(spPdplan);
    }

    @Override
    protected void initListener() {
        btnBackorder.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                if (DataModel.checkHasDetail(mContext, activity)) {
                    btnBackorder.setClickable(false);
                    LoadingUtil.show(mContext, "正在回单...");
                    upload();
                } else {
                    Toast.showText(mContext, "无单据信息");
                }
            }
        });
        cbHebing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isHebing = b;
            }
        });
        autoAdd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isAuto = b;
                share.setPDisAuto(b);
            }
        });
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh.setRefreshing(true);
                getList();
                refresh.setRefreshing(false);
                refresh.setRefreshing(false);
            }
        });
        cbIsClear.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isClear = b;
            }
        });
        edCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == 0 && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    setDATA(edCode.getText().toString(), false);
                    setfocus(edCode);
                }
                return true;
            }
        });
//        edPihao.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                if (i == 0 && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
//                    setfocus(edPdnum);
//                }
//                return true;
//            }
//        });

        spPihao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                PDSub pdSub = (PDSub) piciSpAdapter.getItem(i);
                pihao = pdSub.FBatchNo;
                Lg.e("批次：" + pihao);
//                getInstorageNum(product);
                getSubQty();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        spUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Unit unit = (Unit) spUnit.getAdapter().getItem(i);
//                unitId = unit.FMeasureUnitID;
//                unitName = unit.FName;
//                unitrate = MathUtil.toD(unit.FCoefficient);
//                Log.e("点击单位", unit.toString());
//
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        lvPdlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (isCheck.get(i)) {
                    isCheck.set(i, false);
                    Log.e("isCheck", isCheck.get(i) + "");
                    for (int j = 0; j < choice.size(); j++) {
                        if (choice.get(j).equals(mainContainer.get(i).FID)) {
                            choice.remove(j);
                            choiceContainer.remove(j);
                        }
                    }
                } else {
                    isCheck.set(i, true);
                    Log.e("isCheck", isCheck.get(i) + "");
                    choice.add(mainContainer.get(i).FID);
                    choiceContainer.add(mainContainer.get(i));

                }
                getSubQty();
                pdListAdapter.notifyDataSetChanged();
            }
        });

        spPdplan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pdMain = (PDMain) pdMainSpAdapter.getItem(i);
                fid = pdMain.FID;
                fprocessID = pdMain.FProcessId;
//                if (product != null && fid != null) {
//                    final List<PDSub> pdSubs = pdSubDao.queryBuilder().where(
//                            PDSubDao.Properties.FID.eq(fid),
//                            PDSubDao.Properties.FItemID.eq(product.FItemID)
//                    ).build().list();
//                    tvYtnum.setText(pdSubs.get(0).FCheckQty);
//                }
                Log.e("fprocessID", fprocessID);
                getSubQty();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spStorage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                storage = (Storage) spStorage.getAdapter().getItem(i);
                Hawk.put(getString(R.string.spStorage_pd),storage.FName);
                wavehouseID = "0";
//                waveHouseAdapter = CommonMethod.getMethod(mContext).getWaveHouseAdapter(storage, spWavehouse);
                spWavehouse.setAuto(mContext, storage, wavehouseAutoString);

                storageId = storage.FItemID;
                storageName = storage.FName;
                getSubQty();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spWavehouse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                WaveHouse waveHouse = (WaveHouse) spWavehouse.getAdapter().getItem(i);
                wavehouseID = waveHouse.FSPID;
                wavehouseName = waveHouse.FName;
                Log.e("wavehouseName", wavehouseName);
                getSubQty();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void getSubQty() {
        if (fid == null || product == null || storageId == null || wavehouseID == null || pihao == null) {
            Lg.e("getSubQty:查询数据不完整");
            return;
        }
        final List<PDSub> pdSubs = pdSubDao.queryBuilder().where(
                PDSubDao.Properties.FID.eq(fid),
                PDSubDao.Properties.FItemID.eq(product.FItemID),
                PDSubDao.Properties.FStockID.eq(storageId),
                PDSubDao.Properties.FStockPlaceID.eq(wavehouseID),
                PDSubDao.Properties.FBatchNo.eq(pihao)
        ).build().list();
        Lg.e("getSubQty:",pdSubs);
        if (pdSubs.size() > 0) {
            pdsubChoice = pdSubs.get(0);
            tvYtnum.setText(pdSubs.get(0).FCheckQty);
            tvNumOnServer.setText(pdSubs.get(0).FQty);
        } else {
            tvYtnum.setText("0");
            tvNumOnServer.setText("0");
        }

    }

    //获取批次,根据调出仓库
    private void getPici() {
        List<PDSub> container1 = new ArrayList<>();
        piciSpAdapter = new PiciSpForSubAdapter(mContext, container1);
        spPihao.setAdapter(piciSpAdapter);
        if (product == null) {
            return;
        }
        if (fBatchManager) {
            Log.e("查找pici：", "in...");
            spPihao.setEnabled(true);
            piciSpAdapter = CommonMethod.getMethod(mContext).getPici(pdsubChoice, spPihao);
            piciSpAdapter.notifyDataSetChanged();
//            if (!BasicShareUtil.getInstance(mContext).getIsOL()) {
//                piciSpAdapter = CommonMethod.getMethod(mContext).getPici(storage, wavehouseID, product, spPihao);
//            } else {
//                final List<InStorageNum> container = new ArrayList<>();
//                GetBatchNoBean bean = new GetBatchNoBean();
//                bean.ProductID=product.FItemID;
//                bean.StorageID=storageId;
//                bean.WaveHouseID=wavehouseID;
//                String json = new Gson().toJson(bean);
//                Log.e(TAG, "getPici批次提交："+json);
//                Asynchttp.post(mContext, getBaseUrl() + WebApi.GETPICI, json, new Asynchttp.Response() {
//                    @Override
//                    public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
//                        Log.e(TAG,"getPici获取数据："+cBean.returnJson);
//                        DownloadReturnBean dBean = new Gson().fromJson(cBean.returnJson, DownloadReturnBean.class);
//                        if(dBean.InstorageNum!=null){
//                            for (int i = 0; i < dBean.InstorageNum.size(); i++) {
//                                if (dBean.InstorageNum.get(i).FQty != null
//                                        && MathUtil.toD(dBean.InstorageNum.get(i).FQty) > 0) {
//                                    Log.e(TAG,"有库存的批次："+dBean.InstorageNum.get(i).toString());
//                                    container.add(dBean.InstorageNum.get(i));
//                                }
//                            }
//                            piciSpAdapter = new PiciSpAdapter(mContext, container);
//                            spPihao.setAdapter(piciSpAdapter);
//                        }
//                        piciSpAdapter.notifyDataSetChanged();
//                    }
//
//                    @Override
//                    public void onFailed(String Msg, AsyncHttpClient client) {
//                        Log.e(TAG,"getPici获取数据错误："+Msg);
//                        Toast.showText(mContext, Msg);
//                    }
//                });
//            }
        } else {
            spPihao.setEnabled(false);
        }
    }

    @Override
    protected void OnReceive(String code) {
        Log.e("CODE", code + ":获得的code");
        FInStoreDate = "";
        FInStoreOrderID = "";
        Log.e("code", code);
//            if (edPihao.hasFocus()) {
//                edPihao.setText(code);
//                setfocus(edPdnum);
//            } else {
//                String[] split = code.split("/");
//                if (split.length == 5) {
//                    FSpplierID = split[1];
//                    FInStoreDate = split[2];
//                    FInStoreOrderID = split[3];
//                    setDATA(split[0], false);
//                } else {
//                    edCode.setText(code);
//                    setDATA(code, false);
//                }
//            }

        String[] split = code.split("/");
        if (split.length == 5) {
            FSpplierID = split[1];
            FInStoreDate = split[2];
            FInStoreOrderID = split[3];
            setDATA(split[0], false);
        } else {
            edCode.setText(code);
            setDATA(code, false);
        }


    }


    //获取盘点方案list
    private void getList() {
        Asynchttp.post(mContext, getBaseUrl() + WebApi.PDMAINLIST, "", new Asynchttp.Response() {
            @Override
            public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                mainContainer.clear();
                isCheck.clear();
                choice.clear();
                PDListReturnBean pBean = gson.fromJson(cBean.returnJson, PDListReturnBean.class);
                if (pBean.items != null) {
                    mainContainer.addAll(pBean.items);
                    for (int i = 0; i < mainContainer.size(); i++) {
                        isCheck.add(false);
                        Log.e("isCheck", isCheck.size() + "");
                    }
                }
                pdListAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailed(String Msg, AsyncHttpClient client) {
                Toast.showText(mContext, Msg);
            }
        });
    }


    @OnClick({R.id.btn_delete, R.id.scanbyCamera, R.id.search, R.id.btn_add,R.id.btn_checkorder, R.id.btn_downloadall, R.id.btn_downloadchoosen})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_delete:
                final PDMainDao pdMainDao = daoSession.getPDMainDao();
                if (fid != null) {
                    final PDMain pdMains = pdMainDao.queryBuilder().where(PDMainDao.Properties.FID.eq(fid)).build().unique();
                    AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                    ab.setTitle("删除此计划么");
                    ab.setMessage("确认删除盘点计划?");
                    ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            pdSubDao = daoSession.getPDSubDao();
                            Log.e("deleteFID", pdMain.FID);
                            List<PDSub> list = pdSubDao.queryBuilder().where(
                                    PDSubDao.Properties.FID.eq(pdMain.FID)
                            ).build().list();
                            for (int j = 0; j < list.size(); j++) {
                                pdSubDao.delete(list.get(j));
                            }
                            if (pdMains != null) {
                                pdMainDao.delete(pdMains);
                            }
                            method.getpdmain(spPdplan);
                            Toast.showText(mContext, "删除成功");
//                            startNewActivity(MiddleActivity.class, R.anim.activity_fade_in, R.anim.activity_fade_out, true, null);
                        }
                    }).setNegativeButton("取消", null).create().show();
                }

                break;
            case R.id.scanbyCamera:
                IntentIntegrator intentIntegrator = new IntentIntegrator(mContext);
                // 设置自定义扫描Activity
                intentIntegrator.setCaptureActivity(CustomCaptureActivity.class);
                intentIntegrator.initiateScan();
//                Intent in = new Intent(mContext, CaptureActivity.class);
//                startActivityForResult(in, 0);
                break;
            case R.id.search:
                Log.e("search", "onclick");
                Bundle b1 = new Bundle();
                b1.putString("search", edCode.getText().toString());
                b1.putInt("where", Info.SEARCHPRODUCT);
                startNewActivityForResult(ProductSearchActivity.class, R.anim.activity_open, 0, Info.SEARCHFORRESULT, b1);
                break;
            case R.id.btn_add:
                AddOrder();
                break;
            case R.id.btn_checkorder:
                Bundle b2 = new Bundle();
                b2.putInt("activity", activity);
                startNewActivity(Table2Activity.class, R.anim.activity_fade_in, R.anim.activity_fade_out, false, b2);
                break;
            case R.id.btn_downloadall:
                PDSubRequestBean pdSubRequestBean = new PDSubRequestBean();
                pdSubRequestBean.isClear = isClear;
                pdSubRequestBean.Fid = new ArrayList<>();
                download(gson.toJson(pdSubRequestBean), true);
                break;
            case R.id.btn_downloadchoosen:
                if (choice.size() < 1) {
                    Toast.showText(mContext, "没有选择盘点方案");
                } else {
                    PDSubRequestBean pdSubRequestBean1 = new PDSubRequestBean();
                    pdSubRequestBean1.Fid = choice;
                    pdSubRequestBean1.isClear = isClear;
                    download(gson.toJson(pdSubRequestBean1), false);
                }
                break;
        }
    }

    private void AddOrder() {
        try {
            if (product==null) {
                Toast.showText(mContext, "请选择物料");
                MediaPlayer.getInstance(mContext).error();
                return;
            }
            String num = edPdnum.getText().toString();
            if (edCode.getText().toString().equals("")) {
                Toast.showText(mContext, "请输入物料编号");
            } else if (edPdnum.getText().toString().equals("0") || edPdnum.getText().toString().equals("")) {
                Toast.showText(mContext, "请输入盘点数量");
            } else {
                T_DetailDao t_detailDao = daoSession.getT_DetailDao();
                Lg.e("AddOrder:" ,pdsubChoice);
                if (isHebing) {
                    List<T_Detail> detailhebing = t_detailDao.queryBuilder().where(
                            T_DetailDao.Properties.Activity.eq(activity),
                            T_DetailDao.Properties.FProductId.eq(product.FItemID),
                            T_DetailDao.Properties.FBatch.eq(pihao == null ? "" : pihao),
                            T_DetailDao.Properties.FUnitId.eq(spUnit.getDataId()),
                            T_DetailDao.Properties.FStorageId.eq(storageId),
                            T_DetailDao.Properties.FPositionId.eq(wavehouseID == null ? "0" : wavehouseID)).build().list();
                    if (detailhebing.size() > 0) {
                        for (int i = 0; i < detailhebing.size(); i++) {
                            num = (MathUtil.toD(edPdnum.getText().toString()) + MathUtil.toD(detailhebing.get(i).FQuantity)) + "";
                            t_detailDao.delete(detailhebing.get(i));
                        }
                    }
                }
                T_Detail t_detail = new T_Detail();
                t_detail.FBatch = pihao == null ? "" : pihao;
                t_detail.FProductCode = edCode.getText().toString();
                t_detail.FProductId = product.FItemID;
                t_detail.model = product.FModel;
                t_detail.FProductName = product.FName;
                t_detail.FUnitId = spUnit.getDataId();
                t_detail.FUnit = spUnit.getDataName();
                t_detail.FStorage = storageName == null ? "" : storageName;
                t_detail.FStorageId = storageId == null ? "" : storageId;
                t_detail.FInterID = fid == null ? "" : fid;
                t_detail.FPosition = wavehouseName == null ? "" : wavehouseName;
                t_detail.FPositionId = wavehouseID == null ? "" : wavehouseID;
                t_detail.activity = activity;
                t_detail.unitrate = spUnit.getDataUnitrate();
                t_detail.FIndex = getTimesecond();
                t_detail.FIdentity = "0";
                t_detail.FQuantity = num == null ? "1" : num;
                long insert = t_detailDao.insert(t_detail);

                if (insert > 0) {
                    MediaPlayer.getInstance(mContext).ok();
                    Toast.showText(mContext, "添加成功");
                    if (pdsubChoice != null) {
                        pdsubChoice.FCheckQty = (MathUtil.toD(pdsubChoice.FCheckQty) + MathUtil.toD(edPdnum.getText().toString())) + "";
                        pdSubDao.update(pdsubChoice);
                        ResetAll();
                    } else {
                        PDSub pdSub = new PDSub();
                        pdSub.FAdjQty = "0";
                        pdSub.FCheckQty = edPdnum.getText().toString();
                        pdSub.FItemID = product.FItemID;
                        pdSub.FStockPlaceID = wavehouseID == null ? "" : wavehouseID;
                        pdSub.FBatchNo = pihao == null ? "" : pihao;
                        pdSub.FID = fid;
                        pdSub.FStockID = storageId;
                        pdSub.FName = product.FName;
                        pdSubDao.insert(pdSub);
                        ResetAll();
                    }
                } else {
                    MediaPlayer.getInstance(mContext).error();
                    Toast.showText(mContext, "添加失败");
                }
            }
        } catch (Exception e) {
            DataService.pushError(mContext, this.getClass().getSimpleName(), e);
        }
    }

    private void upload() {
        String main = "";
        ArrayList<String> container = new ArrayList<>();
        t_detailDao = daoSession.getT_DetailDao();
        ArrayList<PurchaseInStoreUploadBean.purchaseInStore> data = new ArrayList<>();
        List<T_Detail> details = t_detailDao.queryBuilder().where(
                T_DetailDao.Properties.Activity.eq(activity)
        ).build().list();

        for (int i = 0; i < details.size(); i++) {
            if (i != 0 && (i + 1) % 50 == 0) {
                T_Detail t_main = details.get(i);
                main += t_main.FInterID + "|" +
                        t_main.FProductId + "|" +
                        t_main.FUnitId + "|" +
                        t_main.FStorageId + "|" +
                        t_main.FPositionId + "|" +
                        t_main.FQuantity + "|" +
                        t_main.FIdentity + "|" +
                        t_main.FBatch + "|";
                container.add(main);
                main = "";
            } else {
                T_Detail t_main = details.get(i);
                main += t_main.FInterID + "|" +
                        t_main.FProductId + "|" +
                        t_main.FUnitId + "|" +
                        t_main.FStorageId + "|" +
                        t_main.FPositionId + "|" +
                        t_main.FQuantity + "|" +
                        t_main.FIdentity + "|" +
                        t_main.FBatch + "|";
            }

        }
        if (main.length() > 0) {
            main = main.subSequence(0, main.length() - 1).toString();
        }
        container.add(main);
        postToServer(container);
    }

    private void postToServer(ArrayList<String> data) {
        uploadBean pBean = new uploadBean();
        pBean.items = data;
        Gson gson = new Gson();
        Asynchttp.post(mContext, getBaseUrl() + WebApi.PDUPLOAD, gson.toJson(pBean), new Asynchttp.Response() {
            @Override
            public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                MediaPlayer.getInstance(mContext).ok();
                Toast.showText(mContext, "上传成功");
                List<T_Detail> list = t_detailDao.queryBuilder().where(T_DetailDao.Properties.Activity.eq(activity)).build().list();
                for (int i = 0; i < list.size(); i++) {
                    t_detailDao.delete(list.get(i));
                }
                btnBackorder.setClickable(true);
                LoadingUtil.dismiss();
            }

            @Override
            public void onFailed(String Msg, AsyncHttpClient client) {
                MediaPlayer.getInstance(mContext).error();
                btnBackorder.setClickable(true);
                Toast.showText(mContext, Msg);
                LoadingUtil.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("code", requestCode + "" + "    " + resultCode);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Bundle b = data.getExtras();
                String message = b.getString("result");
                OnReceive(message);
//                edCode.setText(message);
//                Toast.showText(mContext, message);
//                edCode.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
            }
        }
    }

    private void setDATA(String fnumber, boolean flag) {
//        Log.e(TAG,"setDATA--product:"+product.toString()+" flag:"+flag);
        default_unitID = null;
//        edPihao.setText("");
        edCode.setText(fnumber);
        if (flag) {
            default_unitID = product.FUnitID;
            tvorIsAuto(product);
        } else {
            if (BasicShareUtil.getInstance(mContext).getIsOL()) {
                Asynchttp.post(mContext, getBaseUrl() + WebApi.SEARCHPRODUCTS, fnumber, new Asynchttp.Response() {
                    @Override
                    public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                        final DownloadReturnBean dBean = new Gson().fromJson(cBean.returnJson, DownloadReturnBean.class);
                        if (dBean.products.size() == 1) {
                            getProductOL(dBean, 0);
                            default_unitID = dBean.products.get(0).FUnitID;
//                            chooseUnit(default_unitID);
                        } else if (dBean.products.size() > 1) {
                            AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                            ab.setTitle("请选择物料");
                            View v = LayoutInflater.from(mContext).inflate(R.layout.pd_alert, null);
                            ListView lv = v.findViewById(R.id.lv_alert);
                            productselectAdapter1 = new ProductselectAdapter1(mContext, dBean.products);
                            lv.setAdapter(productselectAdapter);
                            ab.setView(v);
                            final AlertDialog alertDialog = ab.create();
                            alertDialog.show();
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    getProductOL(dBean, i);
                                    default_unitID = dBean.products.get(i).FUnitID;
//                                    chooseUnit(default_unitID);
                                    alertDialog.dismiss();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailed(String Msg, AsyncHttpClient client) {
                        Toast.showText(mContext, Msg);
                    }
                });
            } else {
                final ProductDao productDao = daoSession.getProductDao();
                BarCodeDao barCodeDao = daoSession.getBarCodeDao();
                final List<BarCode> barCodes = barCodeDao.queryBuilder().where(BarCodeDao.Properties.FBarCode.eq(fnumber)).build().list();
                if (barCodes.size() > 0) {
                    if (barCodes.size() == 1) {
                        products = productDao.queryBuilder().where(ProductDao.Properties.FItemID.eq(barCodes.get(0).FItemID)).build().list();
                        default_unitID = barCodes.get(0).FUnitID;
                        product = products.get(0);
                        tvorIsAuto(product);

                    } else {
                        AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                        ab.setTitle("请选择物料");
                        View v = LayoutInflater.from(mContext).inflate(R.layout.pd_alert, null);
                        ListView lv = v.findViewById(R.id.lv_alert);
                        productselectAdapter = new ProductselectAdapter(mContext, barCodes);
                        lv.setAdapter(productselectAdapter);
                        ab.setView(v);
                        final AlertDialog alertDialog = ab.create();
                        alertDialog.show();
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                BarCode barCode = (BarCode) productselectAdapter.getItem(i);
                                products = productDao.queryBuilder().where(
                                        ProductDao.Properties.FItemID.eq(barCode.FItemID)
                                ).build().list();
                                default_unitID = barCode.FUnitID;
//                                chooseUnit(default_unitID);
                                product = products.get(0);
                                tvorIsAuto(product);
                                alertDialog.dismiss();
                            }
                        });

                    }
                } else {
                    Toast.showText(mContext, "未找到物料");
                    MediaPlayer.getInstance(mContext).error();
                    edCode.setText("");
                }
            }

        }

    }
//    //定位到指定单位
//    private void chooseUnit(final String unitId){
//        if (unitId != null && !"".equals(unitId)) {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    for (int i = 0; i < unitAdapter.getCount(); i++) {
//                        if (unitId.equals(((Unit) unitAdapter.getItem(i)).FMeasureUnitID)) {
//                            spUnit.setSelection(i);
//                            Log.e(TAG,"定位了单位："+((Unit) unitAdapter.getItem(i)).toString());
//                        }
//                    }
//                }
//            }, 100);
//        }
//    }


    private void tvorIsAuto(final Product product) {
        try {
            edCode.setText(product.FNumber);
            tvGoodName.setText(product.FName);
            pdSubDao = daoSession.getPDSubDao();
            wavehouseAutoString = product.FSPID;
            if ((product.FBatchManager) != null && (product.FBatchManager).equals("1")) {
                fBatchManager = true;
//            setfocus(edPihao);
//            setfocus(edPihao);
//            edPihao.setEnabled(true);
            } else {
//            edPihao.setEnabled(false);
                fBatchManager = false;
            }

            spUnit.setAuto(mContext, product.FUnitGroupID, default_unitID, SpinnerUnit.Id);
//        unitAdapter = CommonMethod.getMethod(mContext).getUnitAdapter(product.FUnitGroupID, spUnit);
//        chooseUnit(default_unitID);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < unitAdapter.getCount(); i++) {
//                    if (default_unitID.equals(((Unit) unitAdapter.getItem(i)).FMeasureUnitID)) {
//                        spUnit.setSelection(i);
//                    }
//                }
//            }
//        }, 100);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (fid != null) {
                        final List<PDSub> pdSubs = pdSubDao.queryBuilder().where(
                                PDSubDao.Properties.FID.eq(fid),
                                PDSubDao.Properties.FItemID.eq(product.FItemID),
                                PDSubDao.Properties.FStockID.eq(storageId)
                        ).build().list();
                        if (pdSubs.size() > 0) {
//                        if (pdSubs.size() == 1) {
                            pdsubChoice = pdSubs.get(0);
                            getPici();
//                            edPihao.setText(pdsubChoice.FBatchNo);
                            tvYtnum.setText(pdsubChoice.FCheckQty);
                            tvNumOnServer.setText(pdsubChoice.FQty);
                            if (isAuto) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        edPdnum.setText("1.0");
                                        AddOrder();
                                    }
                                }, 100);

                            }
//                        } else {
//
//                            ////////////////////////////////////////////////////////////////
//                            AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
//                            ab.setTitle("请选择盘点明细");
//                            View v = LayoutInflater.from(mContext).inflate(R.layout.pd_alert, null);
//                            ListView lv = v.findViewById(R.id.lv_alert);
//                            AlertLvAdapter alertLvAdapter = new AlertLvAdapter(mContext, pdSubs);
//                            lv.setAdapter(alertLvAdapter);
//                            ab.setView(v);
//                            final AlertDialog alertDialog = ab.create();
//                            alertDialog.show();
//                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                                    pdsubChoice = pdSubs.get(i);
////                                    edPihao.setText(pdsubChoice.FBatchNo);
//                                    for (int j = 0; j < storageAdapter.getCount(); j++) {
//                                        Storage storage = (Storage) storageAdapter.getItem(j);
//                                        if (storage.FItemID.equals(pdsubChoice.FStockID)) {
//                                            spStorage.setSelection(j);
//                                        }
//                                    }
//                                    if (!pdsubChoice.FStockPlaceID.equals("0")) {
//                                        for (int j = 0; j < waveHouseAdapter.getCount(); j++) {
//                                            WaveHouse waveHouse = (WaveHouse) waveHouseAdapter.getItem(j);
//                                            if (waveHouse.FSPID.equals(pdsubChoice.FStockPlaceID)) {
//                                                spWavehouse.setSelection(j);
//                                            }
//                                        }
//                                    }
//                                    tvYtnum.setText(pdsubChoice.FCheckQty);
//                                    tvNumOnServer.setText(pdsubChoice.FQty);
//                                    if (isAuto) {
//                                        edPdnum.setText("1.0");
//                                        AddOrder();
//                                    }
//                                    alertDialog.dismiss();
//                                }
//                            });
//                        }


                        } else {
                            Toast.showText(mContext, "未找到盘点明细");
//                        if (isAuto && fid != null) {
//                            edPdnum.setText("1.0");
//                            AddOrder();
//                        }
                        }
                    } else {
                        Toast.showText(mContext, "请下载盘点方案");
                    }

                }
            }, 100);
        } catch (Exception e) {
            DataService.pushError(mContext, this.getClass().getSimpleName(), e);
        }
    }

    private void getProductOL(DownloadReturnBean dBean, int j) {
        product = dBean.products.get(j);
        tvorIsAuto(product);
        boolean isAuto = false;
        if (isAuto) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    edPdnum.setText("1.0");
                    AddOrder();
                }
            }, 150);

        }
    }

    private void download(final String Json, final boolean downloadall) {
        pg = new ProgressDialog(mContext);
        pg.setMessage("请稍后");
        pg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pg.show();
        Asynchttp.post(mContext, getBaseUrl() + WebApi.PDSUBLIST, Json, new Asynchttp.Response() {
            @Override
            public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                PDsubReturnBean pBean = gson.fromJson(cBean.returnJson, PDsubReturnBean.class);
                PDSubDao pdSubDao = daoSession.getPDSubDao();
                for (int i = 0; i < pBean.items.size(); i++) {
                    List<PDSub> list = pdSubDao.queryBuilder().where(
                            PDSubDao.Properties.FID.eq(pBean.items.get(i).FID),
                            PDSubDao.Properties.FStockPlaceID.eq(pBean.items.get(i).FStockPlaceID),
                            PDSubDao.Properties.FStockID.eq(pBean.items.get(i).FStockID),
                            PDSubDao.Properties.FBatchNo.eq(pBean.items.get(i).FBatchNo),
                            PDSubDao.Properties.FItemID.eq(pBean.items.get(i).FItemID)
                    ).build().list();
                    if (list.size() == 0) {
                        pdSubDao.deleteInTx(list);
                        long insert = pdSubDao.insert(pBean.items.get(i));
                    } else {
                        pdSubDao.deleteInTx(list);
                        long insert = pdSubDao.insert(pBean.items.get(i));
                    }


                }
                PDMainDao pdMainDao = daoSession.getPDMainDao();
                if (downloadall) {
                    for (int i = 0; i < mainContainer.size(); i++) {
                        PDMain p = pdMainDao.queryBuilder().where(PDMainDao.Properties.FID.eq(mainContainer.get(i).FID)).build().unique();
                        if (p != null) {
                            pdMainDao.delete(p);
                        }
                        try {
                            pdMainDao.insert(mainContainer.get(i));
                        } catch (SQLiteConstraintException e) {
                            continue;
                        }
                    }
                } else {
                    for (int i = 0; i < mainContainer.size(); i++) {
                        for (int j = 0; j < choice.size(); j++) {
                            if (choice.get(j).equals(mainContainer.get(i).FID)) {
                                PDMain p = pdMainDao.queryBuilder().where(PDMainDao.Properties.FID.eq(mainContainer.get(i).FID)).build().unique();
                                if (p != null) {
                                    pdMainDao.delete(p);
                                }
                                try {
                                    pdMainDao.insert(mainContainer.get(i));
                                } catch (SQLiteConstraintException e) {
                                    continue;
                                }


                            }
                        }
                    }
                }

                Toast.showText(mContext, "下载完成");
                pdMainSpAdapter = method.getpdmain(spPdplan);
                pg.dismiss();

            }

            @Override
            public void onFailed(String Msg, AsyncHttpClient client) {
                Toast.showText(mContext, Msg);
                pg.dismiss();
            }
        });
    }

    private void ResetAll() {
        tvGoodName.setText("");
        tvNumOnServer.setText("");
        tvYtnum.setText("");
//        edPihao.setText("");
        List<PDSub> container = new ArrayList<>();
        piciSpAdapter = new PiciSpForSubAdapter(mContext, container);
        spPihao.setAdapter(piciSpAdapter);
        edCode.setText("");
        edPdnum.setText("");
        product=null;
    }


    private class uploadBean {
        public ArrayList<String> items;
    }

    //用于adpater首次更新时，不存入默认值，而是选中之前的选项
    private boolean isFirst = false;
    private boolean isFirst2 = false;
    private boolean isFirst3 = false;
    private boolean isFirst4 = false;
    private boolean isFirst5 = false;
    private boolean isFirst6 = false;
    private boolean isFirst7 = false;
}

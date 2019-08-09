package com.fangzuo.assist.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Adapter.DepartmentSpAdapter;
import com.fangzuo.assist.Adapter.EmployeeSpAdapter;
import com.fangzuo.assist.Adapter.PayMethodSpAdapter;
import com.fangzuo.assist.Adapter.PiciSpAdapter;
import com.fangzuo.assist.Adapter.ProductselectAdapter;
import com.fangzuo.assist.Adapter.ProductselectAdapter1;
import com.fangzuo.assist.Adapter.StorageSpAdapter;
import com.fangzuo.assist.Adapter.UnitSpAdapter;
import com.fangzuo.assist.Adapter.WaveHouseSpAdapter;
import com.fangzuo.assist.Adapter.YuandanSpAdapter;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.Beans.GetBatchNoBean;
import com.fangzuo.assist.Beans.InStoreNumBean;
import com.fangzuo.assist.Beans.PurchaseInStoreUploadBean;
import com.fangzuo.assist.Dao.BarCode;
import com.fangzuo.assist.Dao.Department;
import com.fangzuo.assist.Dao.Employee;
import com.fangzuo.assist.Dao.InStorageNum;
import com.fangzuo.assist.Dao.Product;
import com.fangzuo.assist.Dao.PurchaseMethod;
import com.fangzuo.assist.Dao.Storage;
import com.fangzuo.assist.Dao.T_Detail;
import com.fangzuo.assist.Dao.T_main;
import com.fangzuo.assist.Dao.Unit;
import com.fangzuo.assist.Dao.WaveHouse;
import com.fangzuo.assist.Dao.YuandanType;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Service.DataService;
import com.fangzuo.assist.Utils.Asynchttp;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.CommonMethod;
import com.fangzuo.assist.Utils.CommonUtil;
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
import com.fangzuo.assist.widget.SpinnerDepartMent;
import com.fangzuo.assist.widget.SpinnerLingliaoType;
import com.fangzuo.assist.widget.SpinnerPeople;
import com.fangzuo.assist.widget.SpinnerStorage;
import com.fangzuo.assist.widget.SpinnerUnit;
import com.fangzuo.assist.zxing.CustomCaptureActivity;
import com.fangzuo.assist.zxing.activity.CaptureActivity;
import com.fangzuo.greendao.gen.BarCodeDao;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.InStorageNumDao;
import com.fangzuo.greendao.gen.ProductDao;
import com.fangzuo.greendao.gen.T_DetailDao;
import com.fangzuo.greendao.gen.T_mainDao;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.loopj.android.http.AsyncHttpClient;
import com.orhanobut.hawk.Hawk;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//                生产领料
public class ProduceAndGetActivity extends BaseActivity {


    @BindView(R.id.ishebing)
    CheckBox cbHebing;
    @BindView(R.id.isAutoAdd)
    CheckBox autoAdd;
    @BindView(R.id.sp_department)
    SpinnerDepartMent spDepartment;
    @BindView(R.id.sp_which_storage)
    SpinnerStorage spWhichStorage;
    @BindView(R.id.scanbyCamera)
    RelativeLayout scanbyCamera;
    @BindView(R.id.ed_code)
    EditText edCode;
    @BindView(R.id.search)
    RelativeLayout search;
    @BindView(R.id.tv_goodName)
    TextView tvGoodName;
    @BindView(R.id.tv_model)
    TextView tvModel;
    @BindView(R.id.tv_numinstorage)
    TextView tvNuminstorage;
    @BindView(R.id.ed_pihao)
    EditText edPihao;
    @BindView(R.id.sp_wavehouse)
    MyWaveHouseSpinner spWavehouse;
    @BindView(R.id.ed_num)
    EditText edNum;
    @BindView(R.id.sp_unit)
    SpinnerUnit spUnit;
    @BindView(R.id.ed_pricesingle)
    EditText edPricesingle;
    @BindView(R.id.btn_add)
    Button btnAdd;
    @BindView(R.id.btn_finishorder)
    Button btnFinishorder;
    @BindView(R.id.btn_backorder)
    Button btnBackorder;
    @BindView(R.id.btn_checkorder)
    Button btnCheckorder;
    @BindView(R.id.cb_isStorage)
    CheckBox cbIsStorage;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.sp_getType)
    SpinnerLingliaoType spGetType;
    @BindView(R.id.sp_getman)
    SpinnerPeople spGetman;
    @BindView(R.id.sp_sendman)
    SpinnerPeople spSendman;
    @BindView(R.id.mDrawer)
    DrawerLayout mDrawer;
    boolean isHebing = true;
    @BindView(R.id.blue)
    RadioButton blue;
    @BindView(R.id.red)
    RadioButton red;
    @BindView(R.id.redorBlue)
    RadioGroup redorBlue;
    @BindView(R.id.sp_pihao)
    Spinner spPihao;
    private boolean isAuto;
    private DecimalFormat df;
//    private DaoSession daoSession;
    private CommonMethod method;
    private long ordercode;
//    private StorageSpAdapter storageAdapter;
    private PayMethodSpAdapter produceTypeSpAdapter;
    private EmployeeSpAdapter getManAdapter;
    private DepartmentSpAdapter departmentAdapter;
    private List<Product> products;
    private boolean fBatchManager;
    private UnitSpAdapter unitAdapter;
    private String default_unitID;
    private ProductselectAdapter productselectAdapter;
    private Product product;
    private ProductselectAdapter1 productselectAdapter1;
//    private T_mainDao t_mainDao;
//    private T_DetailDao t_detailDao;
    private String wavehouseID;
    private String pihao;
    private boolean isGetDefaultStorage;
    //    private String departmentId;
//    private String departmentName;
//    private String unitId;
//    private String unitName;
//    private double unitrate;
    private Storage storage;
    private WaveHouseSpAdapter waveHouseAdapter;
    private String storageId;
    private String storageName;
    private String wavehouseName;
    private double storenum;
    //    private String getmanId;
//    private String getmanName;
//    private String getTypeId;
//    private String getTypeName;
//    private String SendmanId;
//    private String SendmanName;
    private String date;
    private boolean isRed = false;
    private String redblue = "蓝字";
    private PiciSpAdapter piciSpAdapter;
    List<InStorageNum> piciContainer;
    private boolean checkStorage = false;  // 0不允许负库存false  1允许负库存出库true
    private String wavehouseAutoString = "";
    private int activity = Config.ProduceAndGetActivity;
    private ProduceAndGetActivity mContext;

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
                setDATA("", true);
                break;
//            case EventBusInfoCode.Upload_OK://回单成功
//                t_detailDao.deleteInTx(t_detailDao.queryBuilder().where(
//                        T_DetailDao.Properties.Activity.eq(activity)
//                ).build().list());
//                t_mainDao.deleteInTx(t_mainDao.queryBuilder().where(
//                        T_mainDao.Properties.Activity.eq(activity)
//                ).build().list());
//                btnBackorder.setClickable(true);
//                LoadingUtil.dismiss();
//                MediaPlayer.getInstance(mContext).ok();
//                break;
//            case EventBusInfoCode.Upload_Error://回单失败
//                String error = (String) event.postEvent;
//                Toast.showText(mContext, error);
//                btnBackorder.setClickable(true);
//                LoadingUtil.dismiss();
//                MediaPlayer.getInstance(mContext).error();
//                break;
        }
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_produce_and_get);
        mContext = this;
        ButterKnife.bind(ProduceAndGetActivity.this);
        share = ShareUtil.getInstance(mContext);
        df = new DecimalFormat("######0.00");
        initDrawer(mDrawer);
        edPihao.setEnabled(false);
        cbHebing.setChecked(isHebing);
        autoAdd.setChecked(share.getPGisAuto());
        isAuto = share.getPGisAuto();
        isGetDefaultStorage = share.getBoolean(Info.Storage + activity);
        cbIsStorage.setChecked(isGetDefaultStorage);
    }

    @Override
    protected void initData() {
        method = CommonMethod.getMethod(mContext);
//        if (share.getPROGOrderCode() == 0) {
//            ordercode = Long.parseLong(getTime(false) + "001");
//            Log.e("ordercode", ordercode + "");
//            share.setPROGOrderCode(ordercode);
//        } else {
//            ordercode = share.getPROGOrderCode();
//            Log.e("ordercode", ordercode + "");
//        }
        ordercode = CommonUtil.createOrderCode(this);
        LoadBasicData();
    }

    private void LoadBasicData() {
        tvDate.setText(share.getPROISdate());
//        storageAdapter = method.getStorageSpinner(spWhichStorage);
        spWhichStorage.setAutoSelection(getString(R.string.spStorage_pag), "");
        spDepartment.setAutoSelection(getString(R.string.spDepartment_pag), "");
        spGetType.setAutoSelection(getString(R.string.spGetType_pag), "");
        spGetman.setAutoSelection(getString(R.string.spGetman_pag), "");
        spSendman.setAutoSelection(getString(R.string.spSendman_pag), "");
//        departmentAdapter = method.getDepartMentAdapter(spDepartment);
//        produceTypeSpAdapter = method.getSaleMethodSpinner(spGetType);
//        getManAdapter = method.getEmployeeAdapter(spGetman);
//        method.getEmployeeAdapter(spSendman);
//
//        spDepartment.setSelection(share.getPGDepartment());
//        spGetman.setSelection(share.getPGGetMan());
//        spGetType.setSelection(share.getPGGetType());
//        spSendman.setSelection(share.getPGsendMan());
    }

    @Override
    protected void initListener() {
        btnBackorder.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                if (DataModel.checkHasDetail(mContext, activity)) {
//                    btnBackorder.setClickable(false);
//                    LoadingUtil.show(mContext, "正在回单...");
//                    upload();
                    UpLoadActivity.start(mContext,activity);
                } else {
                    Toast.showText(mContext, "无单据信息");
                }
            }
        });
        redorBlue.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = findViewById(i);
                isRed = radioButton.getText().toString().equals("红字");
                redblue = isRed ? "红字" : "蓝字";

            }
        });
        cbIsStorage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isGetDefaultStorage = b;
                share.setBooleam(Info.Storage + activity, b);
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
                share.setPGisAuto(b);
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
        spPihao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                InStorageNum inStorageNum = (InStorageNum) piciSpAdapter.getItem(i);
                Lg.e("点击库存：" + inStorageNum.toString());
                pihao = inStorageNum.FBatchNo;
                getInstorageNum(product);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


//        spDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Department department = (Department) departmentAdapter.getItem(i);
//                departmentId = department.FItemID;
//                departmentName = department.FName;
////                share.setPGDepartment(i);
//                if (isFirst){
//                    share.setPGDepartment(i);
//                    spDepartment.setSelection(i);
//                }
//                else{
//                    spDepartment.setSelection(share.getPGDepartment());
//                    isFirst=true;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

//        spGetman.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Employee getMan = (Employee) getManAdapter.getItem(i);
//                getmanId = getMan.FItemID;
//                getmanName = getMan.FName;
////                share.setPGGetMan(i);
//                if (isFirst2){
//                    share.setPGGetMan(i);
//                    spGetman.setSelection(i);
//                }
//                else{
//                    spGetman.setSelection(share.getPGGetMan());
//                    isFirst2=true;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

//        spGetType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                PurchaseMethod yuandanType = (PurchaseMethod) produceTypeSpAdapter.getItem(i);
//                getTypeId = yuandanType.FItemID;
//                getTypeName = yuandanType.FName;
////                share.setPGGetType(i);
//                if (isFirst3){
//                    share.setPGGetType(i);
//                    spGetType.setSelection(i);
//                }
//                else{
//                    spGetType.setSelection(share.getPGGetType());
//                    isFirst3=true;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//        spSendman.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Employee getMan = (Employee) getManAdapter.getItem(i);
//                SendmanId = getMan.FItemID;
//                SendmanName = getMan.FName;
////                share.setPGsendMan(i);
//                if (isFirst4){
//                    share.setPGsendMan(i);
//                    spSendman.setSelection(i);
//                }
//                else{
//                    spSendman.setSelection(share.getPGsendMan());
//                    isFirst4=true;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        spUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Unit unit = (Unit) spUnit.getAdapter().getItem(i);
                Lg.e("页面单位：",unit);
//                if (unit != null) {
//                    unitId = unit.FMeasureUnitID;
//                    unitName = unit.FName;
//                    unitrate = MathUtil.toD(unit.FCoefficient);
//                    Log.e("1111", unitrate + "");
//                }
                getInstorageNum(product);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spWhichStorage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                storage = (Storage) spWhichStorage.getAdapter().getItem(i);
                Hawk.put(getString(R.string.spStorage_pag),storage.FName);
                if ("1".equals(storage.FUnderStock)) {
                    checkStorage = true;
                } else {
                    checkStorage = false;
                }
                wavehouseID = "0";
                wavehouseName = "";
//                waveHouseAdapter = CommonMethod.getMethod(mContext).getWaveHouseAdapter(storage, spWavehouse);
                spWavehouse.setAuto(mContext, storage, wavehouseAutoString);

                storageId = storage.FItemID;
                storageName = storage.FName;
                Log.e("storageId", storageId);
                Log.e("storageName", storageName);
                getpici();
//                getInstorageNum(product);

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
                getInstorageNum(product);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    @Override
    protected void OnReceive(String code) {
        Lg.e("扫码：" + code);
        try {
            String[] split = code.split("\\^");
            Lg.e("截取条码：" + split);
            if (split.length > 2) {
                edPihao.setText(split[1] + "");
                edNum.setText(split[2]);
                setDATA(split[0], false);
            } else {
                if (edPihao.hasFocus()) {
                    edPihao.setText(code);
                    if (isAuto) {
                        Addorder();
                    } else if (edNum.getText().toString().equals("")) {
                        setfocus(edNum);
                    }
                } else {
                    edCode.setText(code);
                    setDATA(code, false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Lg.e("截取条码出错：" + e.toString());
            Toast.showText(mContext, "条码有误");
        }

    }

    //获取批次
    private void getpici() {
        piciContainer = new ArrayList<>();
        piciSpAdapter = new PiciSpAdapter(mContext, piciContainer);
        spPihao.setAdapter(piciSpAdapter);
        if (product == null) {
            return;
        }
        if (fBatchManager) {
            Log.e(TAG, "开启批次");
            spPihao.setEnabled(true);

            if (!BasicShareUtil.getInstance(mContext).getIsOL()) {
                piciSpAdapter = CommonMethod.getMethod(mContext).getPici(storage, spWavehouse.getWaveHouseId(), product, spPihao);
                getInstorageNum(product);
            } else {
//                final List<InStorageNum> container = new ArrayList<>();
//                piciSpAdapter = new PiciSpAdapter(mContext, container);
//                spPihao.setAdapter(piciSpAdapter);
                GetBatchNoBean bean = new GetBatchNoBean();
                bean.ProductID = product.FItemID;
                bean.StorageID = storageId;
                bean.WaveHouseID = spWavehouse.getWaveHouseId();
                String json = new Gson().toJson(bean);
                Log.e(TAG, "getPici批次请求：" + json);
                Asynchttp.post(mContext, getBaseUrl() + WebApi.GETPICI, json, new Asynchttp.Response() {
                    @Override
                    public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                        Log.e(TAG, "getPici获取数据：" + cBean.returnJson);
                        DownloadReturnBean dBean = new Gson().fromJson(cBean.returnJson, DownloadReturnBean.class);
                        if (dBean.InstorageNum != null) {
                            for (int i = 0; i < dBean.InstorageNum.size(); i++) {
                                if (dBean.InstorageNum.get(i).FQty != null
                                        && MathUtil.toD(dBean.InstorageNum.get(i).FQty) > 0) {
                                    Log.e(TAG, "有库存的批次：" + dBean.InstorageNum.get(i).toString());
                                    piciContainer.add(dBean.InstorageNum.get(i));

                                }
                            }
                            piciSpAdapter.notifyDataSetChanged();
                            getInstorageNum(product);
                        }
                    }

                    @Override
                    public void onFailed(String Msg, AsyncHttpClient client) {
                        Log.e(TAG, "getPici获取数据错误：" + Msg);
                        Toast.showText(mContext, Msg);
                    }
                });
            }
        } else {
            spPihao.setEnabled(false);
            getInstorageNum(product);
        }
    }


    @OnClick({R.id.scanbyCamera, R.id.search, R.id.btn_add, R.id.btn_finishorder,R.id.btn_checkorder, R.id.tv_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
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
                Addorder();
                break;
            case R.id.btn_finishorder:
                finishOrder();
                break;
            case R.id.btn_checkorder:
                Bundle b2 = new Bundle();
                b2.putInt("activity", activity);
                startNewActivity(TableActivity.class, R.anim.activity_fade_in, R.anim.activity_fade_out, false, b2);
                break;
            case R.id.tv_date:
                datePicker(tvDate);
                break;
        }
    }

    public void finishOrder() {
        AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
        ab.setTitle("确认使用完单");
        ab.setMessage("确认？");
        ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                red.setClickable(true);
                blue.setClickable(true);
                red.setBackgroundColor(Color.WHITE);
                blue.setBackgroundColor(Color.WHITE);
                ordercode++;
                Log.e("ordercode", ordercode + "");
                share.setOrderCode(ProduceAndGetActivity.this, ordercode);
            }
        });
        ab.setNegativeButton("取消", null);
        ab.create().show();

    }

    //自动添加
    private void tvorisAuto(final Product product) {
        try {
            edCode.setText(product.FNumber);
            tvModel.setText(product.FModel);
            wavehouseAutoString = product.FSPID;
            edPricesingle.setText(df.format(MathUtil.toD(product.FSalePrice)));
            tvGoodName.setText(product.FName);
            if ((product.FBatchManager) != null && (product.FBatchManager).equals("1")) {
                fBatchManager = true;
                setfocus(edPihao);
                edPihao.setEnabled(true);
            } else {
                edPihao.setEnabled(false);
                fBatchManager = false;
            }
            if (isGetDefaultStorage) {
                spWhichStorage.setAutoSelection(getString(R.string.spStorage_pag), product.FDefaultLoc);
//
//                for (int j = 0; j < storageAdapter.getCount(); j++) {
//                    if (((Storage) storageAdapter.getItem(j)).FItemID.equals(product.FDefaultLoc)) {
//                        spWhichStorage.setSelection(j);
//                        break;
//                    }
//                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        spWavehouse.setAuto(mContext, storage, wavehouseAutoString);
//                    for (int j = 0; j < waveHouseAdapter.getCount(); j++) {
//                        if (((WaveHouse) waveHouseAdapter.getItem(j)).FSPID.equals(product.FSPID)) {
//                            spWavehouse.setSelection(j);
//                            break;
//                        }
//                    }
                    }
                }, 50);
            }

            getpici();
            spUnit.setAuto(mContext, product.FUnitGroupID, default_unitID, SpinnerUnit.Id);
//        unitAdapter = CommonMethod.getMethod(mContext).getUnitAdapter(product.FUnitGroupID, spUnit);
//        chooseUnit(default_unitID);
//        if (default_unitID != null) {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    for (int i = 0; i < unitAdapter.getCount(); i++) {
//                        if (default_unitID.equals(((Unit) unitAdapter.getItem(i)).FMeasureUnitID)) {
//                            spUnit.setSelection(i);
//                        }
//                    }
//                }
//            }, 100);
//        }


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getInstorageNum(product);
                }
            }, 100);
            if (isAuto) {
                edNum.setText("1.0");
            }

//        if ((isAuto && !fBatchManager) || (isAuto && fBatchManager && !edPihao.getText().toString().equals(""))) {
            if ((isAuto && !fBatchManager) || (isAuto && fBatchManager && !"".equals(pihao))) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Addorder();
                    }
                }, 150);
            }
        } catch (Exception e) {
            DataService.pushError(mContext, this.getClass().getSimpleName(), e);
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


    private void getInstorageNum(Product product) {
        if (product == null) {
            return;
        }
//        Log.e(TAG,"getInstorageNum获取product："+product.toString());
//        pihao = edPihao.getText().toString();
        if (fBatchManager) {
            if (pihao == null || pihao.equals("")) {
                pihao = "";
            }
        } else {
            pihao = "";
        }
//        if (wavehouseID == null) {
//            wavehouseID = "0";
//        }
        if (BasicShareUtil.getInstance(mContext).getIsOL()) {
            InStoreNumBean iBean = new InStoreNumBean();
            iBean.FStockPlaceID = spWavehouse.getWaveHouseId();
            iBean.FBatchNo = pihao;
            iBean.FStockID = storage.FItemID;
            iBean.FItemID = product.FItemID;
            String json = new Gson().toJson(iBean);
            Log.e("inStorenum", json);
            Asynchttp.post(mContext, getBaseUrl() + WebApi.GETINSTORENUM, json, new Asynchttp.Response() {
                @Override
                public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                    storenum = MathUtil.toD(cBean.returnJson);
//                    tvNuminstorage.setText((storenum / unitrate) + "");
                    tvNuminstorage.setText(dealStoreNumForOut(storenum / spUnit.getDataUnitrate() + "") + "");
                    storenum = MathUtil.toD(dealStoreNumForOut(storenum + ""));

                }

                @Override
                public void onFailed(String Msg, AsyncHttpClient client) {
                    Toast.showText(mContext, Msg);
                    tvNuminstorage.setText("0");
                }
            });
        } else {
            InStorageNumDao inStorageNumDao = daoSession.getInStorageNumDao();
            List<InStorageNum> list1 = inStorageNumDao.queryBuilder().where(
                    InStorageNumDao.Properties.FItemID.eq(product.FItemID),
                    InStorageNumDao.Properties.FStockID.eq(storage.FItemID),
                    InStorageNumDao.Properties.FStockPlaceID.eq(spWavehouse.getWaveHouseId()),
                    InStorageNumDao.Properties.FBatchNo.eq(pihao)).build().list();
            if (list1.size() > 0) {
                Log.e("FQty", list1.get(0).FQty);
                Double qty = MathUtil.toD(list1.get(0).FQty);
                Log.e("qty", qty + "");
                if (qty != null) {
                    tvNuminstorage.setText((qty / spUnit.getDataUnitrate()) + "");
                }

            } else {
                tvNuminstorage.setText("0");
            }

        }
    }

    //处理网络库存与已添加的本地库存数量问题
    private String dealStoreNumForOut(String num) {
        if (null==product)return num;
        if (null==storage)return num;
        List<T_Detail> list1 = t_detailDao.queryBuilder().where(
                T_DetailDao.Properties.FProductId.eq(product.FItemID),
                T_DetailDao.Properties.FStorageId.eq(storage.FItemID)
        ).build().list();
        List<T_Detail> list = new ArrayList<>();
        list.addAll(list1);
        if (!"".equals(pihao)) {
            for (T_Detail bean : list) {
                if (!pihao.equals(bean.FBatch)) {
                    list1.remove(bean);
                }
            }
        }
        if (!"".equals(spWavehouse.getWaveHouseId())) {
            for (T_Detail bean : list) {
                if (!spWavehouse.getWaveHouseId().equals(bean.FPositionId)) {
                    list1.remove(bean);
                }
            }
        }
        if (list1.size() > 0) {
            double qty = 0;
            for (int i = 0; i < list1.size(); i++) {
                qty += MathUtil.toD(list1.get(i).FQuantity);
            }
            Lg.e("本地：FQty:" + qty);
//            double qty = MathUtil.toD(list1.get(0).FQuantity);
            return MathUtil.toD(num) - qty + "";
        } else {
            return num;
        }
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

    private void getProductOL(DownloadReturnBean dBean, int j) {
        product = dBean.products.get(j);
        tvorisAuto(product);
    }


    private void Addorder() {
        try {
            String num = "";
//        pihao = edPihao.getText().toString();
            String discount = "";
            if (isRed) {
                num = "-" + edNum.getText().toString();
            } else {
                num = edNum.getText().toString();
            }
            if (product==null) {
                Toast.showText(mContext, "请选择物料");
                MediaPlayer.getInstance(mContext).error();
                return;
            }

            if ("".equals(edCode.getText().toString())) {
                Toast.showText(mContext, "请输入物料编号");
                MediaPlayer.getInstance(mContext).error();
                return;
            }
            if ("".equals(edPricesingle.getText().toString())) {
                Toast.showText(mContext, "请输入单价");
                MediaPlayer.getInstance(mContext).error();
                return;
            }
            if ("".equals(edNum.getText().toString())) {
                Toast.showText(mContext, "请输入数量");
                MediaPlayer.getInstance(mContext).error();
                return;
            }
            InStorageNumDao inStorageNumDao = daoSession.getInStorageNumDao();
            List<InStorageNum> innum = inStorageNumDao.queryBuilder().where(
                    InStorageNumDao.Properties.FItemID.eq(product.FItemID),
                    InStorageNumDao.Properties.FStockID.eq(storageId),
                    InStorageNumDao.Properties.FStockPlaceID.eq(spWavehouse.getWaveHouseId()),
                    InStorageNumDao.Properties.FBatchNo.eq(pihao == null ? "" : pihao)
            ).build().list();
            //是否开启库存管理 true，开启允许负库存
            if (!checkStorage && !isRed) {
                if (!BasicShareUtil.getInstance(mContext).getIsOL()
                        && innum.size() > 0
                        && (MathUtil.toD(innum.get(0).FQty) - (MathUtil.toD(edNum.getText().toString()) * spUnit.getDataUnitrate())) < 0) {
                    MediaPlayer.getInstance(mContext).error();
                    Toast.showText(mContext, "大兄弟，库存不够了");
                    return;
//            } else if (BasicShareUtil.getInstance(mContext).getIsOL() && MathUtil.toD(edNum.getText().toString()) > storenum) {
                } else if (BasicShareUtil.getInstance(mContext).getIsOL() && MathUtil.toD(edNum.getText().toString()) > storenum) {
                    MediaPlayer.getInstance(mContext).error();
                    Toast.showText(mContext, "大兄弟，库存不够了");
                    return;
                } else if (!BasicShareUtil.getInstance(mContext).getIsOL() && innum.size() < 1) {
                    MediaPlayer.getInstance(mContext).error();
                    Toast.showText(mContext, "未找到库存");
                    return;
                }
            }

            if (isHebing) {
                List<T_Detail> detailhebing = t_detailDao.queryBuilder().where(
                        T_DetailDao.Properties.Activity.eq(activity),
                        T_DetailDao.Properties.FOrderId.eq(ordercode),
                        T_DetailDao.Properties.FProductId.eq(product.FItemID),
                        T_DetailDao.Properties.FBatch.eq(pihao == null ? "" : pihao),
                        T_DetailDao.Properties.FUnitId.eq(spUnit.getDataId()),
                        T_DetailDao.Properties.FStorageId.eq(storageId),
                        T_DetailDao.Properties.FDiscount.eq(discount),
                        T_DetailDao.Properties.FPositionId.eq(spWavehouse.getWaveHouseId()),
                        T_DetailDao.Properties.FDiscount.eq(discount)
                ).build().list();
                if (detailhebing.size() > 0) {
                    for (int i = 0; i < detailhebing.size(); i++) {
                        num = (MathUtil.toD(num) + MathUtil.toD(detailhebing.get(i).FQuantity)) + "";
                        t_detailDao.delete(detailhebing.get(i));
                    }
                }
            }
            List<T_main> dewlete = t_mainDao.queryBuilder().where(T_mainDao.Properties.OrderId.eq(ordercode)).build().list();
            t_mainDao.deleteInTx(dewlete);
            String second = getTimesecond();
            T_main t_main = new T_main();
            t_main.FDepartment = spDepartment.getDataName();
            t_main.FDepartmentId = spDepartment.getDataId();
            t_main.FIndex = second;
            t_main.FPaymentDate = "";
            t_main.orderId = ordercode;
            t_main.orderDate = tvDate.getText().toString();
            t_main.FPurchaseUnit = spUnit.getDataName();
            t_main.FSalesMan = spGetman.getEmployeeName();
            t_main.FSalesManId = spGetman.getEmployeeId();
            t_main.FMaker = share.getUserName();
            t_main.FMakerId = share.getsetUserID();
            t_main.FDirector = spSendman.getEmployeeName();
            t_main.FDirectorId = spSendman.getEmployeeId();
            t_main.saleWay = "";
            t_main.FDeliveryAddress = "";
            t_main.FRemark = "";
            t_main.saleWayId = "";
            t_main.FCustody = spGetType.getDataName();
            t_main.FCustodyId = spGetType.getDataId();
            t_main.FAcount = "";
            t_main.FAcountID = "";
            t_main.FRedBlue = redblue;
            t_main.Rem = "";
            t_main.supplier = "";
            t_main.supplierId = "";
            t_main.FSendOutId = "";
            t_main.activity = activity;
            t_main.sourceOrderTypeId = "";


            T_Detail t_detail = new T_Detail();
            t_detail.FRedBlue = redblue;
            t_detail.FBatch = pihao == null ? "" : pihao;
            t_detail.FOrderId = ordercode;
            t_detail.FProductCode = edCode.getText().toString();
            t_detail.FProductId = product.FItemID;
            t_detail.model = product.FModel;
            t_detail.FProductName = product.FName;
            t_detail.FIndex = second;
            t_detail.FUnitId = spUnit.getDataId();
            t_detail.FUnit = spUnit.getDataName();
            t_detail.FStorage = storageName == null ? "" : storageName;
            t_detail.FStorageId = storageId == null ? "" : storageId;
            t_detail.FPosition = spWavehouse.getWaveHouse();
            t_detail.FPositionId = spWavehouse.getWaveHouseId();
            t_detail.activity = activity;
            t_detail.FDiscount = discount;
            t_detail.FQuantity = num;
            t_detail.unitrate = spUnit.getDataUnitrate();
            t_detail.FTaxUnitPrice = edPricesingle.getText().toString();
//            if (wavehouseID == null) {
//                wavehouseID = "0";
//            }
            if (!BasicShareUtil.getInstance(mContext).getIsOL()) {
                long insert = t_detailDao.insert(t_detail);
                long insert1 = t_mainDao.insert(t_main);
                if (insert > 0 && insert1 > 0) {
                    Toast.showText(mContext, "添加成功");
                    MediaPlayer.getInstance(mContext).ok();
                    Log.e("qty_insert", MathUtil.toD(innum.get(0).FQty) + "");
                    Log.e("qty_insert", (MathUtil.toD(edNum.getText().toString()) * spUnit.getDataUnitrate()) + "");
                    Log.e("qty_insert", (spUnit.getDataUnitrate()) + "");
//                        if (isRed) {
//                            innum.get(0).FQty = String.valueOf(((MathUtil.toD(innum.get(0).FQty) + (MathUtil.toD(edNum.getText().toString()) * unitrate))));
//                        } else {
                    innum.get(0).FQty = String.valueOf(((MathUtil.toD(innum.get(0).FQty) - (MathUtil.toD(edNum.getText().toString()) * spUnit.getDataUnitrate()))));
//                        }
                    inStorageNumDao.update(innum.get(0));
                    resetAll();
                } else {
                    Toast.showText(mContext, "添加失败");
                    MediaPlayer.getInstance(mContext).error();
                }
            } else {
                long insert = t_detailDao.insert(t_detail);
                long insert1 = t_mainDao.insert(t_main);
                resetAll();
                if (insert > 0 && insert1 > 0) {
                    Toast.showText(mContext, "添加成功");
                    MediaPlayer.getInstance(mContext).ok();
                }
            }
        } catch (Exception e) {
            DataService.pushError(mContext, this.getClass().getSimpleName(), e);
        }

    }

    private void setDATA(String fnumber, boolean flag) {
        default_unitID = null;
        products = null;
//        edPihao.setText("");
        if (flag) {
            default_unitID = product.FUnitID;
            tvorisAuto(product);
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
                                default_unitID = barCode.FUnitID;
                                products = productDao.queryBuilder().where(ProductDao.Properties.FItemID.eq(barCode.FItemID)).build().list();
                                alertDialog.dismiss();
                            }
                        });
                    }
                } else {
                    MediaPlayer.getInstance(mContext).error();
                    Toast.showText(mContext, "未找到条码");
                }
                if (products != null && products.size() > 0) {
                    product = products.get(0);
                    tvorisAuto(product);
                } else {
                    Toast.showText(mContext, "未找到物料");
                    edPihao.setEnabled(false);
                    edCode.setText("");
                }
            }
        }


    }

    private void resetAll() {
        red.setClickable(false);
        blue.setClickable(false);
        red.setBackgroundColor(Color.GRAY);
        blue.setBackgroundColor(Color.GRAY);
        edNum.setText("");
        edPricesingle.setText("");
        edCode.setText("");
        tvNuminstorage.setText("");
        tvGoodName.setText("");
        tvModel.setText("");
        List<InStorageNum> container = new ArrayList<>();
        piciSpAdapter = new PiciSpAdapter(mContext, container);
        spPihao.setAdapter(piciSpAdapter);
        setfocus(edCode);
        product=null;
    }

    private void upload() {
        PurchaseInStoreUploadBean pBean = new PurchaseInStoreUploadBean();
        PurchaseInStoreUploadBean.purchaseInStore puBean = pBean.new purchaseInStore();
        t_mainDao = daoSession.getT_mainDao();
        t_detailDao = daoSession.getT_DetailDao();
        ArrayList<String> detailContainer = new ArrayList<>();
        ArrayList<PurchaseInStoreUploadBean.purchaseInStore> data = new ArrayList<>();
        List<T_main> mains = t_mainDao.queryBuilder().where(T_mainDao.Properties.Activity.eq(activity)).build().list();
        for (int i = 0; i < mains.size(); i++) {
            if (i > 0 && mains.get(i).orderId == mains.get(i - 1).orderId) {

            } else {
                detailContainer = new ArrayList<>();
                puBean = pBean.new purchaseInStore();
                String main;
                String detail = "";
                T_main t_main = mains.get(i);
                main = t_main.FMakerId + "|" +
                        t_main.orderDate + "|" +
                        t_main.FDepartmentId + "|"
                        + t_main.FDirectorId + "|" +
                        t_main.FSalesManId + "|" +
                        t_main.FRedBlue + "|" +
                        t_main.FCustodyId + "|" +
                        "" + "|";
                puBean.main = main;
                List<T_Detail> details = t_detailDao.queryBuilder().where(
                        T_DetailDao.Properties.FOrderId.eq(t_main.orderId),
                        T_DetailDao.Properties.Activity.eq(activity)
                ).build().list();
                for (int j = 0; j < details.size(); j++) {
                    if (j != 0 && j % 49 == 0) {
                        Log.e("j%49", j % 49 + "");
                        T_Detail t_detail = details.get(j);
                        detail = detail +
                                t_detail.FProductId + "|" +
                                t_detail.FUnitId + "|" +
                                t_detail.FTaxUnitPrice + "|" +
                                t_detail.FQuantity + "|" +
                                t_detail.FDiscount + "|" +
                                t_detail.FStorageId + "|" +
                                t_detail.FBatch + "|" +
                                t_detail.FPositionId + "|";
                        detail = detail.subSequence(0, detail.length() - 1).toString();
                        detailContainer.add(detail);
                        detail = "";
                    } else {
                        Log.e("j", j + "");
                        Log.e("details.size()", details.size() + "");
                        T_Detail t_detail = details.get(j);
                        detail = detail +
                                t_detail.FProductId + "|" +
                                t_detail.FUnitId + "|" +
                                t_detail.FTaxUnitPrice + "|" +
                                t_detail.FQuantity + "|" +
                                t_detail.FDiscount + "|" +
                                t_detail.FStorageId + "|" +
                                t_detail.FBatch + "|" +
                                t_detail.FPositionId + "|";
                        Log.e("detail1", detail);
                    }

                }
                if (detail.length() > 0) {
                    detail = detail.subSequence(0, detail.length() - 1).toString();
                }

                Log.e("detail", detail);
                detailContainer.add(detail);
                puBean.detail = detailContainer;
                data.add(puBean);
            }

        }
        pBean.list = data;
        DataModel.upload(WebApi.PRODUCEANDGET, gson.toJson(pBean));
//        postToServer(data);
    }

    private void postToServer(ArrayList<PurchaseInStoreUploadBean.purchaseInStore> data) {
        PurchaseInStoreUploadBean pBean = new PurchaseInStoreUploadBean();
        pBean.list = data;
        Gson gson = new Gson();
        Asynchttp.post(mContext, getBaseUrl() + WebApi.PRODUCEANDGET, gson.toJson(pBean), new Asynchttp.Response() {
            @Override
            public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                MediaPlayer.getInstance(mContext).ok();
                Toast.showText(mContext, "上传成功");
                List<T_Detail> list = t_detailDao.queryBuilder().where(
                        T_DetailDao.Properties.Activity.eq(activity)
                ).build().list();
                List<T_main> list1 = t_mainDao.queryBuilder().where(
                        T_mainDao.Properties.Activity.eq(activity)
                ).build().list();
                for (int i = 0; i < list.size(); i++) {
                    t_detailDao.delete(list.get(i));
                }
                for (int i = 0; i < list1.size(); i++) {
                    t_mainDao.delete(list1.get(i));
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
    public void onBackPressed() {
        AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
        ab.setTitle("确认退出");
        ab.setMessage("退出会自动执行完单,是否退出?");
        ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ordercode++;
                Log.e("ordercode", ordercode + "");
                share.setOrderCode(ProduceAndGetActivity.this, ordercode);
                finish();
            }
        });
        ab.setNegativeButton("取消", null);
        ab.create().show();
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

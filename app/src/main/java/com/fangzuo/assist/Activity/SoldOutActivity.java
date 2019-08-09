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
import com.fangzuo.assist.Adapter.PayTypeSpAdapter;
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
import com.fangzuo.assist.Dao.PayType;
import com.fangzuo.assist.Dao.Product;
import com.fangzuo.assist.Dao.PurchaseMethod;
import com.fangzuo.assist.Dao.Storage;
import com.fangzuo.assist.Dao.T_Detail;
import com.fangzuo.assist.Dao.T_main;
import com.fangzuo.assist.Dao.Unit;
import com.fangzuo.assist.Dao.WaveHouse;
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
import com.fangzuo.assist.widget.SpinnerGoodsType;
import com.fangzuo.assist.widget.SpinnerPayType;
import com.fangzuo.assist.widget.SpinnerPeople;
import com.fangzuo.assist.widget.SpinnerSaleMethod;
import com.fangzuo.assist.widget.SpinnerSaleScope;
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


//                  销售出库
public class SoldOutActivity extends BaseActivity {


    @BindView(R.id.drawer)
    DrawerLayout mDrawer;
    @BindView(R.id.ishebing)
    CheckBox cbHebing;
    @BindView(R.id.sp_send_storage)
    SpinnerStorage spSendStorage;              //发货仓库
    @BindView(R.id.ed_client)
    EditText edClient;                  //客户输入框
    @BindView(R.id.search_supplier)
    RelativeLayout searchSupplier;
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
    TextView tvNuminstorage;            //库存
    @BindView(R.id.sp_wavehouse)
    MyWaveHouseSpinner spWavehouse;
    @BindView(R.id.ed_onsale)
    EditText edOnsale;                  //折扣率
    @BindView(R.id.ed_num)
    EditText edNum;
    @BindView(R.id.sp_unit)
    SpinnerUnit spUnit;
    @BindView(R.id.ed_pricesingle)
    EditText edPricesingle;
    @BindView(R.id.ed_zhaiyao)
    EditText edZhaiyao;
    @BindView(R.id.btn_add)
    Button btnAdd;
    @BindView(R.id.btn_finishorder)
    Button btnFinishorder;
    @BindView(R.id.btn_backorder)
    Button btnBackorder;
    @BindView(R.id.btn_checkorder)
    Button btnCheckorder;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_date_pay)
    TextView tvDatePay;
    @BindView(R.id.sp_sale_scope)
    SpinnerSaleScope spSaleScope;             //销售范围
    @BindView(R.id.sp_saleMethod)
    SpinnerSaleMethod spSaleMethod;           //销售方式
    @BindView(R.id.sp_yuandan)
    SpinnerPeople spYuandan;              //保管
    @BindView(R.id.sp_sendMethod)
    SpinnerGoodsType spSendMethod;           //交货方式
    @BindView(R.id.sp_payMethod)
    SpinnerPayType spPayMethod;            //结算方式
    @BindView(R.id.sp_sendplace)
    Spinner spSendplace;            //交货地点
    @BindView(R.id.sp_department)
    SpinnerDepartMent spDepartment;           //部门
    @BindView(R.id.sp_employee)
    SpinnerPeople spEmployee;             //业务员
    @BindView(R.id.sp_manager)
    SpinnerPeople spManager;              //主管
    @BindView(R.id.isAutoAdd)
    CheckBox autoAdd;
    @BindView(R.id.cb_isStorage)
    CheckBox cbIsStorage;           //是否带出默认仓库
    @BindView(R.id.sp_pihao)
    Spinner spPihao;
    @BindView(R.id.blue)
    RadioButton blue;
    @BindView(R.id.red)
    RadioButton red;
    @BindView(R.id.redorBlue)
    RadioGroup redorBlue;
    private SoldOutActivity mContext;
    private DecimalFormat df;
    private long ordercode;         //获取当前时间段
//    private StorageSpAdapter storageSpAdapter;
    private CommonMethod method;
    private PayMethodSpAdapter slaesRange;
    private PayMethodSpAdapter saleMethodSpAdapter;
    private EmployeeSpAdapter employeeAdapter;
    private YuandanSpAdapter yuandanSpAdapter;
    private PayTypeSpAdapter payTypeSpAdapter;
    private DepartmentSpAdapter departMentAdapter;
    private PayMethodSpAdapter getGoodsType;
    //    private String departmentId;
//    private String departmentName;
//    private String SaleMethodId;
//    private String SaleMethodName;
//    private String saleRangeId;
//    private String saleRangeName;
//    private String yuandanID;
//    private String yuandanName;
//    private String payTypeId;
//    private String payTypeName;
//    private String employeeId;
//    private String employeeName;
//    private String ManagerId;
//    private String ManagerName;
//    private String sendMethodId;
//    private String sendMethodName;
    private List<Product> products;
    private UnitSpAdapter unitAdapter;
//    private String unitId;
//    private String unitName;
//    private double unitrate;
    private boolean fBatchManager;
    private String pihao;
    private Storage storage;
    private WaveHouseSpAdapter waveHouseAdapter;
    private String storageId;
    private String storageName;
    private String wavehouseID = "0";
    private String wavehouseName;
    private String clientId;
    private String clientName;
    private String date;
    private String datePay;
    private boolean isHebing = true;
    private Double qty;
    private boolean isAuto;
    private boolean isGetStorage;
    private Product product;
    private ProductselectAdapter productselectAdapter;
    private ProductselectAdapter1 productselectAdapter1;
    private PiciSpAdapter pici;
    private boolean isRed = false;
    private String redblue = "蓝字";
    private String default_unitID;
    private Double storenum;
    private boolean checkStorage = false;  // 0不允许负库存false  1允许负库存出库true
    private String wavehouseAutoString = "";
    private int activity = Config.SoldOutActivity;

    @Override
    public void initView() {
        setContentView(R.layout.activity_sold_out);
        mContext = this;
        ButterKnife.bind(mContext);
        share = ShareUtil.getInstance(mContext);
        initDrawer(mDrawer);
        df = new DecimalFormat("######0.00");
        initDrawer(mDrawer);
        cbHebing.setChecked(true);
        edOnsale.setText("0");
        autoAdd.setChecked(share.getSOUTisAuto());
        isAuto = share.getSOUTisAuto();
        isGetStorage = share.getBoolean(Info.Storage + activity);
        cbIsStorage.setChecked(isGetStorage);
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
    public void initData() {
        method = CommonMethod.getMethod(mContext);
        //获取当前时间值
//        if (share.getSOUTOrderCode() == 0) {
//            ordercode = Long.parseLong(getTime(false) + "001");
//            Log.e("ordercode", ordercode + "");
//            share.setSOUTOrderCode(ordercode);
//        } else {
//            ordercode = share.getSOUTOrderCode();
//            Log.e("ordercode", ordercode + "");
//        }
        ordercode = CommonUtil.createOrderCode(this);
        //初始化各种spinner
        LoadBasicData();
    }

    private void LoadBasicData() {

//        storageSpAdapter = method.getStorageSpinner(spSendStorage);
        spSendStorage.setAutoSelection(getString(R.string.spStorage_soldo), "");
        spYuandan.setAutoSelection(getString(R.string.spYuandan_soldo), "");
        spManager.setAutoSelection(getString(R.string.spManager_soldo), "");
        spEmployee.setAutoSelection(getString(R.string.spEmployee_soldo), "");
        spDepartment.setAutoSelection(getString(R.string.spDepartment_soldo), "");
        spSendMethod.setAutoSelection(getString(R.string.spSendMethod_soldo), "");
        spSaleScope.setAutoSelection(getString(R.string.spSaleScope_soldo), "");
        spSaleMethod.setAutoSelection(getString(R.string.spSaleMethod_soldo), "");
        spPayMethod.setAutoSelection(getString(R.string.spPayMethod_soldo), "");
//        slaesRange = method.getPurchaseRange(spSaleScope);
//        saleMethodSpAdapter = method.getSaleMethodSpinner(spSaleMethod);
//        payTypeSpAdapter = method.getpayType(spPayMethod);
//        departMentAdapter = method.getDepartMentAdapter(spDepartment);
//        employeeAdapter = method.getEmployeeAdapter(spEmployee);
//        method.getEmployeeAdapter(spManager);
//        getGoodsType = method.getGoodsTypes(spSendMethod);


//        spManager.setAdapter(employeeAdapter);
//        spEmployee.setAdapter(employeeAdapter);
//        spYuandan.setAdapter(employeeAdapter);

//        spDepartment.setSelection(share.getSOUTDepartment());
//        spSaleMethod.setSelection(share.getSOUTsaleMethod());
//        spSaleScope.setSelection(share.getSoutSaleRange());
//        spYuandan.setSelection(share.getSOUTYuandan());
//        spPayMethod.setSelection(share.getSOUTPayMethod());
//        spEmployee.setSelection(share.getSOUTEmployee());
//        spManager.setSelection(share.getSOUTManager());
//        spSendMethod.setSelection(share.getSOUTsendmethod());


        tvDate.setText(share.getSOUTdate());
        tvDatePay.setText(share.getSOUTdatepay());
    }

    @Override
    public void initListener() {
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
                isGetStorage = b;
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
                share.setSOUTisAuto(b);
            }
        });

        //批号
        spPihao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                InStorageNum inStorageNum = (InStorageNum) pici.getItem(i);
                pihao = inStorageNum.FBatchNo;
                getInstorageNum(product);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //扫码输入框
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

        //部门
//        spDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Department department = (Department) departMentAdapter.getItem(i);
//                departmentId = department.FItemID;
//                departmentName = department.FName;
////                share.setSOUTDepartment(i);
//                if (isFirst){
//                    share.setSOUTDepartment(i);
//                    spDepartment.setSelection(i);
//                }
//                else{
//                    spDepartment.setSelection(share.getSOUTDepartment());
//                    isFirst=true;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

//        //销售方式
//        spSaleMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                PurchaseMethod item = (PurchaseMethod) saleMethodSpAdapter.getItem(i);
//                SaleMethodId = item.FItemID;
//                SaleMethodName = item.FName;
////                share.setSOUTsaleMethod(i);
//                if (isFirst2){
//                    share.setSOUTsaleMethod(i);
//                    spSaleMethod.setSelection(i);
//                }
//                else{
//                    spSaleMethod.setSelection(share.getSOUTsaleMethod());
//                    isFirst2=true;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

//        //销售范围
//        spSaleScope.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                PurchaseMethod purchaseMethod = (PurchaseMethod) slaesRange.getItem(i);
//                saleRangeId = purchaseMethod.FItemID;
//                saleRangeName = purchaseMethod.FName;
////                share.setSoutSaleRange(i);
//                if (isFirst3){
//                    share.setSoutSaleRange(i);
//                    spSaleScope.setSelection(i);
//                }
//                else{
//                    spSaleScope.setSelection(share.getSoutSaleRange());
//                    isFirst3=true;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        //保管
//        spYuandan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Employee employee = (Employee) employeeAdapter.getItem(i);
//                yuandanID = employee.FItemID;
//                yuandanName = employee.FName;
////                share.setSOUTYuandan(i);
//                if (isFirst4){
//                    share.setSOUTYuandan(i);
//                    spYuandan.setSelection(i);
//                }
//                else{
//                    spYuandan.setSelection(share.getSOUTYuandan());
//                    isFirst4=true;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

//        //结算方式
//        spPayMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                PayType payType = (PayType) payTypeSpAdapter.getItem(i);
//                payTypeId = payType.FItemID;
//                payTypeName = payType.FName;
////                share.setSOUTPayMethod(i);
//                if (isFirst5){
//                    share.setSOUTPayMethod(i);
//                    spPayMethod.setSelection(i);
//                }
//                else{
//                    spPayMethod.setSelection(share.getSOUTPayMethod());
//                    isFirst5=true;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        //业务员
//        spEmployee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Employee employee = (Employee) employeeAdapter.getItem(i);
//                employeeId = employee.FItemID;
//                employeeName = employee.FName;
////                share.setSOUTEmployee(i);
//                if (isFirst6){
//                    share.setSOUTEmployee(i);
//                    spEmployee.setSelection(i);
//                }
//                else{
//                    spEmployee.setSelection(share.getSOUTEmployee());
//                    isFirst6=true;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        //主管
//        spManager.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Employee employee = (Employee) employeeAdapter.getItem(i);
//                ManagerId = employee.FItemID;
//                ManagerName = employee.FName;
////                share.setSOUTManager(i);
//                if (isFirst7){
//                    share.setSOUTManager(i);
//                    spManager.setSelection(i);
//                }
//                else{
//                    spManager.setSelection(share.getSOUTManager());
//                    isFirst7=true;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        //交货方式
//        spSendMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                PurchaseMethod purchaseMethod = (PurchaseMethod) getGoodsType.getItem(i);
//                sendMethodId = purchaseMethod.FItemID;
//                sendMethodName = purchaseMethod.FName;
////                share.setSOUTsendmethod(i);
//                if (isFirst8){
//                    share.setSOUTsendmethod(i);
//                    spSendMethod.setSelection(i);
//                }
//                else{
//                    spSendMethod.setSelection(share.getSOUTsendmethod());
//                    isFirst8=true;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        //发货仓库
        spSendStorage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                storage = (Storage) spSendStorage.getAdapter().getItem(i);
                Hawk.put(getString(R.string.spStorage_soldo),storage.FName);
                Lg.e("仓库：" + storage.toString());
                if ("1".equals(storage.FUnderStock)) {
                    checkStorage = true;
                } else {
                    checkStorage = false;
                }
//                waveHouseAdapter = CommonMethod.getMethod(mContext).getWaveHouseAdapter(storage, spWavehouse);
                wavehouseID = "0";
                spWavehouse.setAuto(mContext, storage, wavehouseAutoString);
                storageId = storage.FItemID;
                storageName = storage.FName;
                getPici();
                Log.e("点击仓库storageId", storageId);
                Log.e("点击仓库storageName", storageName);

                //获取仓库对应的仓位
                getInstorageNum(product);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //单位
        spUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Unit unit = (Unit) spUnit.getAdapter().getItem(i);
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
        //仓位
        spWavehouse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                WaveHouse waveHouse = (WaveHouse) spWavehouse.getAdapter().getItem(i);
                wavehouseID = waveHouse.FSPID;
                wavehouseName = waveHouse.FName;
                Log.e("点击仓位wavehouse：", wavehouseName);
                getPici();

                getInstorageNum(product);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    protected void OnReceive(String code) {
        setDATA(code, false);
    }

    private void getProductOL(DownloadReturnBean dBean, int j) {
        product = dBean.products.get(j);
        tvorisAuto(product);
    }

    //设置或查找product
    private void setDATA(String fnumber, boolean flag) {
        default_unitID = null;
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
                            Log.e(TAG, "setDATA联网获取product数据:" + dBean.products.get(0));
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
                        Toast.showText(Msg);
                    }
                });
            } else {
                final ProductDao productDao = daoSession.getProductDao();
                BarCodeDao barCodeDao = daoSession.getBarCodeDao();
                final List<BarCode> barCodes = barCodeDao.queryBuilder().where(
                        BarCodeDao.Properties.FBarCode.eq(fnumber)
                ).build().list();

                if (barCodes.size() > 0) {
                    if (barCodes.size() == 1) {
                        products = productDao.queryBuilder().where(
                                ProductDao.Properties.FItemID.eq(barCodes.get(0).FItemID)
                        ).build().list();
                        default_unitID = barCodes.get(0).FUnitID;
                        Log.e(TAG, "setDATA联网获取product数据:" + products.get(0));
                        getProductOFL(products);
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
                                products = productDao.queryBuilder().where(ProductDao.Properties.FItemID.eq(barCode.FItemID)).build().list();
                                default_unitID = barCode.FUnitID;
                                getProductOFL(products);
                                alertDialog.dismiss();
                            }
                        });
                    }
                } else {
                    MediaPlayer.getInstance(mContext).error();
                    Toast.showText("未找到条码");
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


    private void getProductOFL(List<Product> list) {
        if (list != null && list.size() > 0) {
            product = list.get(0);
            tvorisAuto(product);
        } else {
            Toast.showText(mContext, "未找到物料");
            edCode.setText("");
            setfocus(edCode);
        }
    }

    //获取批次
    private void getPici() {
        List<InStorageNum> container1 = new ArrayList<>();
        pici = new PiciSpAdapter(mContext, container1);
        spPihao.setAdapter(pici);
        if (fBatchManager) {
            spPihao.setEnabled(true);
            if (!BasicShareUtil.getInstance(mContext).getIsOL()) {
                pici = CommonMethod.getMethod(mContext).getPici(storage, spWavehouse.getWaveHouseId(), product, spPihao);
            } else {
                final List<InStorageNum> container = new ArrayList<>();
//                pici = new PiciSpAdapter(mContext, container);
//                spPihao.setAdapter(pici);

                GetBatchNoBean bean = new GetBatchNoBean();
                bean.ProductID = product.FItemID;
                bean.StorageID = storageId;
                bean.WaveHouseID = spWavehouse.getWaveHouseId();
                String json = new Gson().toJson(bean);
                Log.e(TAG, "getPici批次提交：" + json);
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
                                    container.add(dBean.InstorageNum.get(i));
                                }
                            }
                            pici = new PiciSpAdapter(mContext, container);
                            spPihao.setAdapter(pici);
                        }
                        pici.notifyDataSetChanged();
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
        }
    }

    private void tvorisAuto(final Product product) {
        edCode.setText(product.FNumber);
        tvModel.setText(product.FModel);
        wavehouseAutoString = product.FSPID;
        edPricesingle.setText(df.format(MathUtil.toD(product.FSalePrice)));
        tvGoodName.setText(product.FName);
        fBatchManager = (product.FBatchManager) != null && (product.FBatchManager).equals("1");
//        if (wavehouseID == null) {
//            wavehouseID = "0";
//        }
        if (isGetStorage) {
            spSendStorage.setAutoSelection(getString(R.string.spStorage_soldo), product.FDefaultLoc);
//
//            for (int j = 0; j < storageSpAdapter.getCount(); j++) {
//                if (((Storage) storageSpAdapter.getItem(j)).FItemID.equals(product.FDefaultLoc)) {
//                    spSendStorage.setSelection(j);
//                    break;
//                }
//            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    spWavehouse.setAuto(mContext, storage, wavehouseAutoString);
//                    for (int j = 0; j < waveHouseAdapter.getCount(); j++) {
//                        if (((WaveHouse)waveHouseAdapter.getItem(j)).FSPID.equals(product.FSPID)) {
//                            spWavehouse.setSelection(j);
//                            break;
//                        }
//                    }
                }
            }, 50);
        }
        getPici();

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
        getInstorageNum(product);

        if (isAuto) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    edNum.setText("1.0");
                    edOnsale.setText("0");
                    Addorder();
                }
            }, 150);

        }
    }


    //获取库存
    private void getInstorageNum(Product product) {
        if (product == null || storage == null) {
            return;
        }
        Log.e(TAG, "getInstorageNum");
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
            iBean.FStockPlaceID = spWavehouse.getWaveHouseId();      //仓位ID
            iBean.FBatchNo = pihao;         //批次
            iBean.FStockID = storage.FItemID;       //
            iBean.FItemID = product.FItemID;
            String json = new Gson().toJson(iBean);
            Log.e(TAG, "getInstorageNum库存提交：" + json);
            Asynchttp.post(mContext, getBaseUrl() + WebApi.GETINSTORENUM, json, new Asynchttp.Response() {
                @Override
                public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                    Log.e(TAG, "库存返回：" + cBean.returnJson);
                    storenum = MathUtil.toD(cBean.returnJson);
//                    tvNuminstorage.setText((storenum / unitrate) + "");
                    tvNuminstorage.setText(dealStoreNumForOut(storenum / spUnit.getDataUnitrate() + "") + "");

                }

                @Override
                public void onFailed(String Msg, AsyncHttpClient client) {
                    Log.e(TAG, "库存错误返回：" + Msg);
                    Toast.showText(mContext, Msg);
                    tvNuminstorage.setText("0");
                    storenum = 0.0;
                }
            });
        } else {
            InStorageNumDao inStorageNumDao = daoSession.getInStorageNumDao();
            List<InStorageNum> list1 = inStorageNumDao.queryBuilder().
                    where(InStorageNumDao.Properties.FItemID.eq(product.FItemID),
                            InStorageNumDao.Properties.FStockID.eq(storage.FItemID),
                            InStorageNumDao.Properties.FStockPlaceID.eq(spWavehouse.getWaveHouseId()),
                            InStorageNumDao.Properties.FBatchNo.eq(pihao)).build().list();
            Log.e("SoldOutActivity", "list1.size():" + list1.size());
            Log.e("SoldOutActivity", product.FItemID);
            Log.e("SoldOutActivity", spWavehouse.getWaveHouseId());
            Log.e("SoldOutActivity", "pici:" + pihao);
            Log.e("SoldOutActivity", storage.FItemID);
            if (list1.size() > 0) {
                Log.e("FQty", list1.get(0).FQty);
                storenum = MathUtil.toD(list1.get(0).FQty);
                Log.e("qty", storenum + "");
                if (storenum != null) {
                    tvNuminstorage.setText((storenum / spUnit.getDataUnitrate()) + "");
                }

            } else {
                storenum = 0.0;
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
            return MathUtil.toD(num) - qty + "";
        } else {
            return num;
        }
    }

    @OnClick({R.id.search_supplier, R.id.scanbyCamera, R.id.search, R.id.btn_add, R.id.btn_finishorder,R.id.btn_checkorder, R.id.tv_date, R.id.tv_date_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //选择客户
            case R.id.search_supplier:
                Bundle b = new Bundle();
                b.putString("search", edClient.getText().toString());
                b.putInt("where", Info.SEARCHCLIENT);
                startNewActivityForResult(ProductSearchActivity.class, R.anim.activity_open, 0, Info.SEARCHFORRESULTCLIRNT, b);
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
            case R.id.tv_date_pay:
                datePicker(tvDatePay);
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
                share.setOrderCode(SoldOutActivity.this, ordercode);
            }
        });
        ab.setNegativeButton("取消", null);
        ab.create().show();

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
        } else if (requestCode == Info.SEARCHFORRESULTCLIRNT) {
            if (resultCode == Info.SEARCHFORRESULTCLIRNT) {
                Bundle b = data.getExtras();
                clientId = b.getString("001");
                clientName = b.getString("002");
                edClient.setText(clientName);
                setfocus(edCode);
            }
        }
    }

    private void Addorder() {
        try {
//            if (wavehouseID == null) {
//                wavehouseID = "0";
//            }
            String discount = edOnsale.getText().toString();
            String num = edNum.getText().toString();


            if (isRed) {
                num = "-" + edNum.getText().toString();
            } else {
                num = edNum.getText().toString();
            }
            if (clientId == null || "".equals(edClient.getText().toString())) {
                Toast.showText(mContext, "请选择客户");
                MediaPlayer.getInstance(mContext).error();
                return;
            }
            if (edCode.getText().toString().equals("")) {
                Toast.showText(mContext, "请输入物料编号");
                MediaPlayer.getInstance(mContext).error();
                return;
            }

            if (edPricesingle.getText().toString().equals("")) {
                Toast.showText(mContext, "请输入单价");
                MediaPlayer.getInstance(mContext).error();
                return;
            }
            if (edNum.getText().toString().equals("")) {
                Toast.showText(mContext, "请输入数量");
                MediaPlayer.getInstance(mContext).error();
                return;
            }
            if (edOnsale.getText().toString().equals("")) {
                Toast.showText(mContext, "请输入折扣");
                MediaPlayer.getInstance(mContext).error();
                return;
            }

            //是否开启库存管理 true，开启允许负库存
            if (!checkStorage && !isRed) {
//            if ((storenum / unitrate) < MathUtil.toD(num)) {
                if (MathUtil.toD(tvNuminstorage.getText().toString().trim()) < MathUtil.toD(num)) {
                    MediaPlayer.getInstance(mContext).error();
                    Toast.showText(mContext, "大兄弟，库存不够了");
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
            t_main.FPaymentDate = tvDatePay.getText().toString();
            t_main.orderId = ordercode;
            t_main.orderDate = tvDate.getText().toString();
            t_main.FPurchaseUnit = spUnit.getDataName();
            t_main.FSalesMan = spEmployee.getEmployeeName();
            t_main.FSalesManId = spEmployee.getEmployeeId();
            t_main.FMaker = share.getUserName();
            t_main.FMakerId = share.getsetUserID();
            t_main.FDirector = spManager.getEmployeeName();
            t_main.FDirectorId = spManager.getEmployeeId();
            t_main.saleWay = spSaleMethod.getDataName();
            t_main.FDeliveryAddress = "";
            t_main.FRedBlue = redblue;
            t_main.FRemark = edZhaiyao.getText().toString();
            t_main.saleWayId = spSaleMethod.getDataId();
            t_main.FCustody = spSaleScope.getDataName();
            t_main.FCustodyId = spSaleScope.getDataId();
            t_main.FAcount = spSendMethod.getDataName();
            t_main.FAcountID = spSendMethod.getDataId();
            t_main.Rem = edZhaiyao.getText().toString();
            t_main.supplier = clientName == null ? "" : clientName;
            t_main.supplierId = clientId == null ? "" : clientId;
            t_main.FSendOutId = spPayMethod.getDataId();
            t_main.activity = activity;
            t_main.sourceOrderTypeId = spYuandan.getEmployeeId();


            T_Detail t_detail = new T_Detail();
            t_detail.FBatch = pihao == null ? "" : pihao;
            t_detail.FOrderId = ordercode;
            t_detail.FRedBlue = redblue;
            t_detail.FProductCode = edCode.getText().toString();
            t_detail.FProductId = product.FItemID;
            t_detail.model = product.FModel;
            t_detail.FProductName = product.FName;
            t_detail.FIndex = second;
            t_detail.FUnitId = spUnit.getDataId();
            t_detail.FUnit = spUnit.getDataName();
            t_detail.FStorage = storageName == null ? "" : storageName;
            t_detail.FStorageId = storageId == null ? "0" : storageId;
            t_detail.FPosition = spWavehouse.getWaveHouse();
            t_detail.FPositionId = spWavehouse.getWaveHouseId();
            t_detail.activity = activity;
            t_detail.FDiscount = discount;
            t_detail.FQuantity = num;
            t_detail.unitrate = spUnit.getDataUnitrate();
            t_detail.FTaxUnitPrice = edPricesingle.getText().toString();

            if (!BasicShareUtil.getInstance(mContext).getIsOL()) {
                long insert = t_detailDao.insert(t_detail);
                long insert1 = t_mainDao.insert(t_main);
                if (insert > 0 && insert1 > 0) {
                    InStorageNumDao inStorageNumDao = daoSession.getInStorageNumDao();
                    List<InStorageNum> innum = inStorageNumDao.queryBuilder().where(
                            InStorageNumDao.Properties.FItemID.eq(product.FItemID),
                            InStorageNumDao.Properties.FStockID.eq(storageId),
                            InStorageNumDao.Properties.FStockPlaceID.eq(spWavehouse.getWaveHouseId()),
                            InStorageNumDao.Properties.FBatchNo.eq(pihao == null ? "" : pihao)
                    ).build().list();
                    Toast.showText(mContext, "添加成功");
                    MediaPlayer.getInstance(mContext).ok();
//                Log.e("qty_insert", MathUtil.toD(innum.get(0).FQty) + "");
//                Log.e("qty_insert", (MathUtil.toD(edNum.getText().toString()) * unitrate) + "");
//                Log.e("qty_insert", (unitrate) + "");
                    if (isRed) {
                        if (innum.size() == 0) {
                            InStorageNum inStorageNum = new InStorageNum();
                            inStorageNum.FItemID = product.FItemID;
                            inStorageNum.FBatchNo = pihao == null ? "" : pihao;
                            inStorageNum.FStockPlaceID = spWavehouse.getWaveHouseId();
                            inStorageNum.FStockID = storageId;
                            inStorageNum.FQty = (MathUtil.toD(edNum.getText().toString()) * spUnit.getDataUnitrate()) + "";
                            inStorageNumDao.insert(inStorageNum);
                        } else {
                            innum.get(0).FQty = String.valueOf(((MathUtil.toD(innum.get(0).FQty) + (MathUtil.toD(edNum.getText().toString()) * spUnit.getDataUnitrate()))));
                        }

                    } else {
                        innum.get(0).FQty = String.valueOf(((MathUtil.toD(innum.get(0).FQty) - (MathUtil.toD(edNum.getText().toString()) * spUnit.getDataUnitrate()))));
                    }
                    if (innum.size() != 0) {
                        inStorageNumDao.update(innum.get(0));
                    }
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

    private void resetAll() {
        red.setClickable(false);
        blue.setClickable(false);
        red.setBackgroundColor(Color.GRAY);
        blue.setBackgroundColor(Color.GRAY);
        edNum.setText("");
        edPricesingle.setText("");
        edZhaiyao.setText("");
        edCode.setText("");
        tvNuminstorage.setText("");
        tvGoodName.setText("");
        tvModel.setText("");
        edOnsale.setText("0");
        List<InStorageNum> container = new ArrayList<>();
        pici = new PiciSpAdapter(mContext, container);
        spPihao.setAdapter(pici);
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
                        t_main.FPaymentDate + "|" +
                        t_main.saleWayId + "|" +
                        t_main.FDeliveryAddress + "|" +
                        t_main.FDepartmentId + "|" +
                        t_main.FSalesManId + "|" +
                        t_main.FDirectorId + "|" +
                        t_main.supplierId + "|" +
                        t_main.Rem + "|" +
                        t_main.FSalesManId + "|" +
                        t_main.sourceOrderTypeId + "|" +
                        t_main.FRedBlue + "|" +
                        t_main.FCustodyId + "|" +
                        t_main.sourceOrderTypeId + "|";
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
        DataModel.upload(WebApi.UPLOADSOUT, gson.toJson(pBean));
//        postToServer(data);
    }

    private void postToServer(ArrayList<PurchaseInStoreUploadBean.purchaseInStore> data) {
        PurchaseInStoreUploadBean pBean = new PurchaseInStoreUploadBean();
        pBean.list = data;
        Gson gson = new Gson();
        Asynchttp.post(mContext, getBaseUrl() + WebApi.UPLOADSOUT, gson.toJson(pBean), new Asynchttp.Response() {
            @Override
            public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                MediaPlayer.getInstance(mContext).ok();
                Toast.showText(mContext, "上传成功");
                List<T_Detail> list = t_detailDao.queryBuilder().where(T_DetailDao.Properties.Activity.eq(activity)).build().list();
                List<T_main> list1 = t_mainDao.queryBuilder().where(T_mainDao.Properties.Activity.eq(activity)).build().list();
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
}

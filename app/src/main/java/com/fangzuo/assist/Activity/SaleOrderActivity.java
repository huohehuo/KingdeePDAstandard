package com.fangzuo.assist.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Adapter.DepartmentSpAdapter;
import com.fangzuo.assist.Adapter.EmployeeSpAdapter;
import com.fangzuo.assist.Adapter.PayMethodSpAdapter;
import com.fangzuo.assist.Adapter.PayTypeSpAdapter;
import com.fangzuo.assist.Adapter.ProductselectAdapter;
import com.fangzuo.assist.Adapter.ProductselectAdapter1;
import com.fangzuo.assist.Adapter.UnitSpAdapter;
import com.fangzuo.assist.Adapter.YuandanSpAdapter;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.Beans.PurchaseInStoreUploadBean;
import com.fangzuo.assist.Dao.BarCode;
import com.fangzuo.assist.Dao.Department;
import com.fangzuo.assist.Dao.Employee;
import com.fangzuo.assist.Dao.PayType;
import com.fangzuo.assist.Dao.Product;
import com.fangzuo.assist.Dao.PurchaseMethod;
import com.fangzuo.assist.Dao.T_Detail;
import com.fangzuo.assist.Dao.T_main;
import com.fangzuo.assist.Dao.Unit;
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
import com.fangzuo.assist.Utils.MathUtil;
import com.fangzuo.assist.Utils.MediaPlayer;
import com.fangzuo.assist.Utils.ShareUtil;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.assist.widget.LoadingUtil;
import com.fangzuo.assist.widget.SpinnerDepartMent;
import com.fangzuo.assist.widget.SpinnerGoodsType;
import com.fangzuo.assist.widget.SpinnerPayType;
import com.fangzuo.assist.widget.SpinnerPeople;
import com.fangzuo.assist.widget.SpinnerSaleMethod;
import com.fangzuo.assist.widget.SpinnerSaleMethodForSaleOrder;
import com.fangzuo.assist.widget.SpinnerSaleScope;
import com.fangzuo.assist.widget.SpinnerUnit;
import com.fangzuo.assist.widget.SpinnerYuanDan;
import com.fangzuo.assist.zxing.CustomCaptureActivity;
import com.fangzuo.assist.zxing.activity.CaptureActivity;
import com.fangzuo.greendao.gen.BarCodeDao;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.ProductDao;
import com.fangzuo.greendao.gen.T_DetailDao;
import com.fangzuo.greendao.gen.T_mainDao;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.loopj.android.http.AsyncHttpClient;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//销售订单
public class SaleOrderActivity extends BaseActivity {

    @BindView(R.id.drawer)
    DrawerLayout mDrawer;
    @BindView(R.id.ishebing)
    CheckBox cbHebing;
    @BindView(R.id.tv_date_arrive)
    TextView tvDateArrive;
    @BindView(R.id.ed_supplier)
    EditText edClient;
    @BindView(R.id.search_supplier)
    RelativeLayout searchClient;
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
    @BindView(R.id.sp_unit)
    SpinnerUnit spUnit;
    @BindView(R.id.ed_onsale)
    EditText edOnsale;
    @BindView(R.id.ed_num)
    EditText edNum;
    @BindView(R.id.ed_zhaiyao)
    EditText edZhaiyao;
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
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_date_pay)
    TextView tvDatePay;
    @BindView(R.id.sp_sale_scope)
    SpinnerSaleScope spSaleScope;
    @BindView(R.id.sp_saleMethod)
    SpinnerSaleMethodForSaleOrder spSaleMethod;
    @BindView(R.id.sp_yuandan)
    SpinnerYuanDan spYuandan;
    @BindView(R.id.sp_sendMethod)
    SpinnerGoodsType spSendMethod;
    @BindView(R.id.sp_payMethod)
    SpinnerPayType spPayMethod;
    @BindView(R.id.sp_department)
    SpinnerDepartMent spDepartment;
    @BindView(R.id.sp_employee)
    SpinnerPeople spEmployee;
    @BindView(R.id.sp_manager)
    SpinnerPeople spManager;
    @BindView(R.id.cb_isStorage)
    CheckBox cbIsStorage;
    @BindView(R.id.isAutoAdd)
    CheckBox autoAdd;
    private SaleOrderActivity mContext;
    private DecimalFormat df;
//    private DaoSession daoSession;
    private CommonMethod method;
    private long ordercode;
    private PayMethodSpAdapter slaesRange;
    private PayMethodSpAdapter payMethodSpinner;
    private YuandanSpAdapter yuandanSpAdapter;
    private PayTypeSpAdapter payTypeSpAdapter;
    private DepartmentSpAdapter departMentAdapter;
    private EmployeeSpAdapter employeeAdapter;
    private PayMethodSpAdapter getGoodsType;
    private String dateArrive;
    private String clientName;
    private String clientId;
    private List<Product> products;
    private UnitSpAdapter unitAdapter;
    private String datePay;
    private String date;
    //    private String sendMethodId;
//    private String sendMethodName;
//    private String unitId;
//    private String unitName;
//    private double unitrate;
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
    private boolean isHebing = true;
    private boolean isAuto;
    private boolean isGetStorage;
    private Product product;
    private ProductselectAdapter productselectAdapter;
    private ProductselectAdapter1 productselectAdapter1;
    private String default_unitID;
    private int activity = Config.SaleOrderActivity;

    @Override
    public void initView() {
        setContentView(R.layout.activity_sale_order);
        mContext = this;
        ButterKnife.bind(mContext);
        share = ShareUtil.getInstance(mContext);
        initDrawer(mDrawer);
        df = new DecimalFormat("######0.00");
        cbHebing.setChecked(isHebing);
        edOnsale.setText("0");
        autoAdd.setChecked(share.getSOisAuto());
        isAuto = share.getSOisAuto();
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
//        if (share.getSOOrderCode() == 0) {
//            ordercode = Long.parseLong(getTime(false) + "001");
//            Log.e("ordercode", ordercode + "");
//            share.setSOOrderCode(ordercode);
//        } else {
//            ordercode = share.getSOOrderCode();
//            Log.e("ordercode", ordercode + "");
//        }
        ordercode = CommonUtil.createOrderCode(this);
        loadBasicData();
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
                share.setSOisAuto(b);
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
//        spDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Department department = (Department) departMentAdapter.getItem(i);
//                departmentId = department.FItemID;
//                departmentName = department.FName;
////                share.setSODepartment(i);
//                if (isFirst){
//                    share.setSODepartment(i);
//                    spDepartment.setSelection(i);
//                }
//                else{
//                    spDepartment.setSelection(share.getSODepartment());
//                    isFirst=true;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

//        spSaleMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                PurchaseMethod item = (PurchaseMethod) payMethodSpinner.getItem(i);
//                SaleMethodId = item.FItemID;
//                SaleMethodName = item.FName;
////                share.setSOSaleMethod(i);
//                if (isFirst2){
//                    share.setSOSaleMethod(i);
//                    spSaleMethod.setSelection(i);
//                }
//                else{
//                    spSaleMethod.setSelection(share.getSOSaleMethod());
//                    isFirst2=true;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

//        spSaleScope.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                PurchaseMethod purchaseMethod = (PurchaseMethod) slaesRange.getItem(i);
//                saleRangeId = purchaseMethod.FItemID;
//                saleRangeName = purchaseMethod.FName;
////                share.setSOSaleRange(i);
//                if (isFirst3){
//                    share.setSOSaleRange(i);
//                    spSaleScope.setSelection(i);
//                }
//                else{
//                    spSaleScope.setSelection(share.getSOSaleRange());
//                    isFirst3=true;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//        spYuandan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                YuandanType yuandanType = (YuandanType) yuandanSpAdapter.getItem(i);
//                yuandanID = yuandanType.FID;
//                yuandanName = yuandanType.FName_CHS;
////                share.setSOYuandan(i);
//                if (isFirst4){
//                    share.setSOYuandan(i);
//                    spYuandan.setSelection(i);
//                }
//                else{
//                    spYuandan.setSelection(share.getSOYuandan());
//                    isFirst4=true;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

//        spPayMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                PayType payType = (PayType) payTypeSpAdapter.getItem(i);
//                payTypeId = payType.FItemID;
//                payTypeName = payType.FName;
////                share.setSOPayMethod(i);
//                if (isFirst5){
//                    share.setSOPayMethod(i);
//                    spPayMethod.setSelection(i);
//                }
//                else{
//                    spPayMethod.setSelection(share.getSOPayMethod());
//                    isFirst5=true;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//        spEmployee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Employee employee = (Employee) employeeAdapter.getItem(i);
//                employeeId = employee.FItemID;
//                employeeName = employee.FName;
////                share.setPOEmployee(i);
//                if (isFirst6){
//                    share.setPOEmployee(i);
//                    spEmployee.setSelection(i);
//                }
//                else{
//                    spEmployee.setSelection(share.getPOEmployee());
//                    isFirst6=true;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

//        spManager.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Employee employee = (Employee) employeeAdapter.getItem(i);
//                ManagerId = employee.FItemID;
//                ManagerName = employee.FName;
////                share.setPOManager(i);
//                if (isFirst7){
//                    share.setPOManager(i);
//                    spManager.setSelection(i);
//                }
//                else{
//                    spManager.setSelection(share.getPOManager());
//                    isFirst7=true;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

//        spSendMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                PurchaseMethod purchaseMethod = (PurchaseMethod) getGoodsType.getItem(i);
//                sendMethodId = purchaseMethod.FItemID;
//                sendMethodName = purchaseMethod.FName;
//                if (isFirst8){
//                    share.setSOSendMethod(i);
//                    spSendMethod.setSelection(i);
//                }
//                else{
//                    spSendMethod.setSelection(share.getSOSendMethod());
//                    isFirst8=true;
//                }
//
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

//        spUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Unit unit = (Unit) spUnit.getAdapter().getItem(i);
//                if (unit != null) {
//                    unitId = unit.FMeasureUnitID;
//                    unitName = unit.FName;
//                    unitrate = MathUtil.toD(unit.FCoefficient);
//                    Log.e("1111", unitrate + "");
//                }
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
    }

    @Override
    protected void OnReceive(String code) {
        setDATA(code, false);
    }


    @OnClick({R.id.tv_date_arrive, R.id.search_supplier, R.id.scanbyCamera, R.id.search, R.id.btn_add, R.id.btn_finishorder, R.id.btn_checkorder, R.id.tv_date, R.id.tv_date_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_date_arrive:
                datePicker(tvDateArrive);
                break;
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

    private void loadBasicData() {
        tvDateArrive.setText(getTime(true));
        tvDate.setText(share.getPOdate());
        tvDatePay.setText(share.getPOpaydate());

//        slaesRange = method.getPurchaseRange(spSaleScope);
//        payMethodSpinner = method.getPayMethodSpinner(spSaleMethod);

        spYuandan.setAutoSelection(getString(R.string.spYuandan_so), "");
        spPayMethod.setAutoSelection(getString(R.string.spPayMethod_so), "");
        spDepartment.setAutoSelection(getString(R.string.spDepartment_so), "");
        spEmployee.setAutoSelection(getString(R.string.spEmployee_so), "");
        spManager.setAutoSelection(getString(R.string.spManager_so), "");
        spSaleScope.setAutoSelection(getString(R.string.spSaleScope_so), "");
        spSaleMethod.setAutoSelection(getString(R.string.spSaleMethod_so), "");
        spSendMethod.setAutoSelection(getString(R.string.spSendMethod_so), "");

//        yuandanSpAdapter = method.getyuandanSp(spYuandan);
//        payTypeSpAdapter = method.getpayType(spPayMethod);
//        departMentAdapter = method.getDepartMentAdapter(spDepartment);
//        employeeAdapter = method.getEmployeeAdapter(spEmployee);
//        method.getEmployeeAdapter(spManager);
//        getGoodsType = method.getGoodsTypes(spSendMethod);

//        spDepartment.setSelection(share.getPODepartment());
//        spSaleMethod.setSelection(share.getPOPurchaseMethod());
//        spSaleScope.setSelection(share.getPOPurchaseRange());
//        spYuandan.setSelection(share.getPOYuandan());
//        spPayMethod.setSelection(share.getPOPayMethod());
//        spEmployee.setSelection(share.getPOEmployee());
//        spManager.setSelection(share.getPOManager());
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
                Log.d("clientId", clientId == null ? "null" : clientId);
                clientName = b.getString("002");
                edClient.setText(clientName);
                setfocus(edCode);
            }
        }
    }

    private void tvorisAuto(Product product) {
        try {
            edCode.setText(product.FNumber);
            tvModel.setText(product.FModel);
            edPricesingle.setText(df.format(MathUtil.toD(product.FSalePrice)));
            tvGoodName.setText(product.FName);

            spUnit.setAuto(mContext, product.FUnitGroupID, default_unitID, SpinnerUnit.Id);
//        unitAdapter = CommonMethod.getMethod(mContext).getUnitAdapter(product.FUnitGroupID, spUnit);

//        chooseUnit(default_unitID);
//        if(default_unitID!=null){
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    for(int i = 0;i<unitAdapter.getCount();i++){
//                        if(default_unitID.equals(((Unit)unitAdapter.getItem(i)).FMeasureUnitID)){
//                            spUnit.setSelection(i);
//                        }
//                    }
//                }
//            },100);
//        }

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
                                default_unitID = barCode.FUnitID;
                                products = productDao.queryBuilder().where(ProductDao.Properties.FItemID.eq(barCode.FItemID)).build().list();
                                getProductOFL(products);
                                alertDialog.dismiss();
                            }
                        });
                    }
                } else {
                    MediaPlayer.getInstance(mContext).error();
                    Toast.showText(mContext, "未找到条码");
                }

            }
        }


    }

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

    private void getProductOL(DownloadReturnBean dBean, int j) {
        product = dBean.products.get(j);
        tvorisAuto(product);
    }

    private void resetAll() {
        edNum.setText("");
        edPricesingle.setText("");
        edOnsale.setText("");
        edZhaiyao.setText("");
        edCode.setText("");
        tvGoodName.setText("");
        tvModel.setText("");
        setfocus(edCode);
        product=null;
    }

    public void finishOrder() {
        AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
        ab.setTitle("确认使用完单");
        ab.setMessage("确认？");
        ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ordercode++;
                Log.e("ordercode", ordercode + "");
                share.setOrderCode(SaleOrderActivity.this, ordercode);
            }
        });
        ab.setNegativeButton("取消", null);
        ab.create().show();

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
                share.setOrderCode(SaleOrderActivity.this, ordercode);
                finish();
            }
        });
        ab.setNegativeButton("取消", null);
        ab.create().show();
    }

    private void Addorder() {
        try {
            String discount = edOnsale.getText().toString();
            String num = edNum.getText().toString();
            if (product == null) {
                Toast.showText(mContext, "请选择物料");
                MediaPlayer.getInstance(mContext).error();
                return;
            }
            if (clientId == null || "".equals(edClient.getText().toString())) {
                Toast.showText(mContext, "请选择客户");
                MediaPlayer.getInstance(mContext).error();
                return;
            }

            if (edOnsale.getText().toString().equals("") || edCode.getText().toString().equals("") || edPricesingle.getText().toString().equals("") || edNum.getText().toString().equals("")) {
                MediaPlayer.getInstance(mContext).error();
                if (edCode.getText().toString().equals("")) {
                    Toast.showText(mContext, "请输入物料编号");
                } else if (edPricesingle.getText().toString().equals("")) {
                    Toast.showText(mContext, "请输入单价");
                } else if (edNum.getText().toString().equals("")) {
                    Toast.showText(mContext, "请输入数量");
                } else if (edOnsale.getText().toString().equals("")) {
                    Toast.showText(mContext, "请输入折扣率");
                }
            } else {
                if (isHebing) {
                    List<T_Detail> detailhebing = t_detailDao.queryBuilder().where(
                            T_DetailDao.Properties.Activity.eq(activity),
                            T_DetailDao.Properties.FOrderId.eq(ordercode),
                            T_DetailDao.Properties.FProductId.eq(product.FItemID),
                            T_DetailDao.Properties.FUnitId.eq(spUnit.getDataId()),
                            T_DetailDao.Properties.FTaxUnitPrice.eq(edPricesingle.getText().toString()),
                            T_DetailDao.Properties.FDiscount.eq(discount)).build().list();
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
                t_main.FPurchaseUnit = clientId == null ? "" : clientId;
                t_main.FSalesMan = spEmployee.getEmployeeName();
                t_main.FSalesManId = spEmployee.getEmployeeId();
                t_main.FMaker = share.getUserName();
                t_main.FMakerId = share.getsetUserID();
                t_main.FDirector = spManager.getEmployeeName();
                t_main.FDirectorId = spManager.getEmployeeId();
                t_main.FPaymentType = spPayMethod.getDataName();
                t_main.FPaymentTypeId = spPayMethod.getDataId();
                t_main.saleWayId = spSaleMethod.getDataId();
                t_main.saleWay = spSaleScope.getDataName();
                t_main.FDeliveryAddress = "";
                t_main.FRemark = edZhaiyao.getText().toString();
                t_main.FCustody = spSaleScope.getDataName();
                t_main.FCustodyId = spSaleScope.getDataId();
                t_main.FAcount = spSendMethod.getDataId();
                t_main.FAcountID = tvDateArrive.getText().toString();
                t_main.Rem = edZhaiyao.getText().toString();
                t_main.supplier = clientName == null ? "" : clientName;
                t_main.supplierId = clientId == null ? "" : clientId;
                t_main.FSendOutId = "";
                t_main.FDeliveryType = "";
                t_main.activity = activity;
                t_main.sourceOrderTypeId = spYuandan.getDataId();
                long insert1 = t_mainDao.insert(t_main);

                T_Detail t_detail = new T_Detail();
                t_detail.FBatch = "";
                t_detail.FOrderId = ordercode;
                t_detail.FProductCode = edCode.getText().toString();
                t_detail.FProductId = product.FItemID;
                t_detail.model = product.FModel;
                t_detail.FProductName = product.FName;
                t_detail.FIndex = second;
                t_detail.FUnitId = spUnit.getDataId();
                t_detail.FUnit = spUnit.getDataName();
                t_detail.FDateDelivery = tvDateArrive.getText().toString();
                Log.e("date", tvDateArrive.getText().toString());
                t_detail.FStorage = "";
                t_detail.FStorageId = "";
                t_detail.FPosition = "";
                t_detail.FPositionId = "";
                t_detail.activity = activity;
                t_detail.FDiscount = discount;
                t_detail.FQuantity = num;
                t_detail.unitrate = spUnit.getDataUnitrate();
                t_detail.FTaxUnitPrice = edPricesingle.getText().toString();

                long insert = t_detailDao.insert(t_detail);

                if (insert1 > 0 && insert > 0) {
                    Toast.showText(mContext, "添加成功");
                    MediaPlayer.getInstance(mContext).ok();
                    resetAll();
                } else {
                    Toast.showText(mContext, "添加失败，请重试");
                    MediaPlayer.getInstance(mContext).error();
                }
            }
        } catch (Exception e) {
            DataService.pushError(mContext, this.getClass().getSimpleName(), e);
        }
    }

    private void upload() {
        PurchaseInStoreUploadBean pBean = new PurchaseInStoreUploadBean();
        PurchaseInStoreUploadBean.purchaseInStore puBean = pBean.new purchaseInStore();
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
                        t_main.saleWayId + "|"
                        + "1" + "|" +
                        t_main.FDepartmentId + "|" +
                        t_main.FSalesManId + "|" +
                        t_main.FDirectorId + "|" +
                        t_main.supplierId + "|" +
                        t_main.FRemark + "|" +
                        t_main.FCustodyId + "|" +
                        t_main.FPaymentTypeId + "|" +
                        t_main.FAcount + "|" +
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
                                t_detail.FDateDelivery + "|";
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
                                t_detail.FDateDelivery + "|";
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
        DataModel.upload(WebApi.UPLOADSO, gson.toJson(pBean));
//        postToServer(data);
    }

    private void postToServer(ArrayList<PurchaseInStoreUploadBean.purchaseInStore> data) {
        PurchaseInStoreUploadBean pBean = new PurchaseInStoreUploadBean();
        pBean.list = data;
        Gson gson = new Gson();
        Asynchttp.post(mContext, getBaseUrl() + WebApi.UPLOADSO, gson.toJson(pBean), new Asynchttp.Response() {
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

    //用于adpater首次更新时，不存入默认值，而是选中之前的选项
    private boolean isFirst = false;
    private boolean isFirst2 = false;
    private boolean isFirst3 = false;
    private boolean isFirst4 = false;
    private boolean isFirst5 = false;
    private boolean isFirst6 = false;
    private boolean isFirst7 = false;
    private boolean isFirst8 = false;
}

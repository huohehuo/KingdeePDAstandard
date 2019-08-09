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
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Adapter.AutoTVAdapter;
import com.fangzuo.assist.Adapter.DepartmentSpAdapter;
import com.fangzuo.assist.Adapter.EmployeeSpAdapter;
import com.fangzuo.assist.Adapter.PayMethodSpAdapter;
import com.fangzuo.assist.Adapter.ProductselectAdapter;
import com.fangzuo.assist.Adapter.ProductselectAdapter1;
import com.fangzuo.assist.Adapter.StorageSpAdapter;
import com.fangzuo.assist.Adapter.SupplierSpAdapter;
import com.fangzuo.assist.Adapter.UnitSpAdapter;
import com.fangzuo.assist.Adapter.WaveHouseSpAdapter;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.Beans.InStoreNumBean;
import com.fangzuo.assist.Beans.PurchaseInStoreUploadBean;
import com.fangzuo.assist.Dao.BarCode;
import com.fangzuo.assist.Dao.Department;
import com.fangzuo.assist.Dao.Employee;
import com.fangzuo.assist.Dao.InStorageNum;
import com.fangzuo.assist.Dao.Product;
import com.fangzuo.assist.Dao.PurchaseMethod;
import com.fangzuo.assist.Dao.Storage;
import com.fangzuo.assist.Dao.Suppliers;
import com.fangzuo.assist.Dao.T_Detail;
import com.fangzuo.assist.Dao.T_main;
import com.fangzuo.assist.Dao.Unit;
import com.fangzuo.assist.Dao.Wanglaikemu;
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
import com.fangzuo.assist.widget.SpinnerPeople;
import com.fangzuo.assist.widget.SpinnerPurchaseMethod;
import com.fangzuo.assist.widget.SpinnerStorage;
import com.fangzuo.assist.widget.SpinnerUnit;
import com.fangzuo.assist.widget.SpinnerWlkm;
import com.fangzuo.assist.zxing.CustomCaptureActivity;
import com.fangzuo.assist.zxing.activity.CaptureActivity;
import com.fangzuo.greendao.gen.BarCodeDao;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.InStorageNumDao;
import com.fangzuo.greendao.gen.ProductDao;
import com.fangzuo.greendao.gen.T_DetailDao;
import com.fangzuo.greendao.gen.T_mainDao;
import com.fangzuo.greendao.gen.WaveHouseDao;
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
import butterknife.Unbinder;

//                  外购入库
public class PurchaseInStorageActivity extends BaseActivity {

    @BindView(R.id.drawer)
    DrawerLayout mDrawer;
    @BindView(R.id.scanbyCamera)
    RelativeLayout scanbyCamera;
    @BindView(R.id.ed_code)
    EditText edCode;
    @BindView(R.id.ishebing)
    CheckBox cbHebing;
    @BindView(R.id.ed_supplier)
    EditText edSupplier;
    @BindView(R.id.sp_which_storage)
    SpinnerStorage spWhichStorage;
    @BindView(R.id.tv_goodName)
    TextView tvGoodName;
    @BindView(R.id.tv_model)
    TextView tvModel;
    @BindView(R.id.sp_unit)
    SpinnerUnit spUnit;
    @BindView(R.id.ed_pihao)
    EditText edPihao;
    @BindView(R.id.sp_wavehouse)
    MyWaveHouseSpinner spWavehouse;
    @BindView(R.id.tv_numinstorage)
    TextView tvNuminstorage;            //库存
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
    @BindView(R.id.sp_purchaseMethod)
    SpinnerPurchaseMethod spPurchaseMethod;
    @BindView(R.id.sp_capture_person)
    SpinnerPeople spCapturePerson;
    @BindView(R.id.sp_sign_person)
    SpinnerPeople spSignPerson;
    @BindView(R.id.sp_department)
    SpinnerDepartMent spDepartment;
    @BindView(R.id.sp_employee)
    SpinnerPeople spEmployee;
    @BindView(R.id.sp_manager)
    SpinnerPeople spManager;
    @BindView(R.id.sp_wanglaikemu)
    SpinnerWlkm spWanglaikemu;
    @BindView(R.id.search)
    RelativeLayout search;
    @BindView(R.id.search_supplier)
    RelativeLayout searchSupplier;
    @BindView(R.id.isAutoAdd)
    CheckBox autoAdd;
    @BindView(R.id.cb_isStorage)
    CheckBox cbIsStorage;
    @BindView(R.id.redorBlue)
    RadioGroup redorBlue;
    private PurchaseInStorageActivity mContext;
    private Unbinder bind;
    private ProductDao productDao;
    private ArrayList<String> container1;
    private ArrayList<String> container;
    private AutoTVAdapter items;
    private List<Product> resultAll;
    private PopupWindow popupWindow;
//    private DaoSession daoSession;
    private StorageSpAdapter storageSpAdapter;
    private SupplierSpAdapter supplierSpAdapter;
    private Storage storage;
    private List<WaveHouse> waveHouses;
    private boolean isGetDefaultStorage = false;
    private boolean isSpStorageDefault = true;
    private List<Suppliers> supplierses;
    private WaveHouseDao wavehousedao;
    private boolean fBatchManager;
    private String wavehouseID = "0";
    private List<Product> list;
    private String dateAdd;
    private String datePay;
    //    private ShareUtil share;
    private long ordercode;
    private String storageId;
    private String storageName;
    public String wavehouseName;
    private List<PurchaseMethod> purchaseMethods;
    private List<Employee> employees;
    private List<Department> departments;
    private List<Wanglaikemu> wanglaikemus;
    //    private String PurchaseMethodId;
//    private String purchaseMethodName;
//    private String capturePersonId;
//    private String capyurePersonName;
//    private String signPersonId;
//    private String signPersonName;
//    private String departmentId;
//    private String departmentName;
//    private String PersonId;
//    private String PersonName;
//    private String managerId;
//    private String managerName;
//    private String wanglaikemuId = "";
//    private String wanglaikemuName;
    private List<Unit> units;
//    private String unitId;
//    private String unitName;
//    private Double unitrate;
    private String supplierid = "";
    private String supplierName = "";
    private boolean isHebing = true;
    private PayMethodSpAdapter payMethodSpAdapter;
    private EmployeeSpAdapter employeeSpAdapter;
    private DepartmentSpAdapter departMentAdapter;
    private WaveHouseSpAdapter waveHouseAdapter;
    private List<Wanglaikemu> wanglaikemuList;
    private UnitSpAdapter unitAdapter;
    private DecimalFormat df;
    private String date;
    private Double qty;
    private boolean isAuto = false;
    private Product product;
    private ProductselectAdapter productselectAdapter;
    private ProductselectAdapter1 productselectAdapter1;


    private String default_unitID;
    private boolean isRed;
    private String redblue = "蓝字";
    private String BatchNo;
    private String wavehouseAutoString = "";
    private int activity = Config.PurchaseInStorageActivity;
    @Override
    public void initView() {
        setContentView(R.layout.activity_purchase_in_storage);
        mContext = this;
        df = new DecimalFormat("######0.00");
        bind = ButterKnife.bind(mContext);
//        share = ShareUtil.getInstance(mContext);
        edPihao.setEnabled(false);
        initDrawer(mDrawer);
        cbHebing.setChecked(true);
        autoAdd.setChecked(share.getPUISisAuto());
        isGetDefaultStorage = share.getBoolean(Info.Storage + activity);
        cbIsStorage.setChecked(isGetDefaultStorage);
        isAuto = share.getPUISisAuto();
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
//        if (share.getPISOrderCode() == 0) {
//            ordercode = Long.parseLong(getTime(false) + "001");
//            Log.e("ordercode", ordercode + "");
//            share.setPISOrderCode(ordercode);
//        } else {
//            ordercode = share.getPISOrderCode();
//            Log.e("ordercode", ordercode + "");
//        }
        ordercode = CommonUtil.createOrderCode(this);
        LoadBasicData();
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
                RadioButton radioButton = (RadioButton) findViewById(i);
                isRed = radioButton.getText().toString().equals("红字");
                redblue = isRed ? "红字" : "蓝字";
                Log.e("isred", isRed + "");

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

        //批号输入监听
        edPihao.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                Log.e(TAG, "批号监听。。。");
                if (i == 0 && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    getInstorageNum(product);
                    setfocus(edPihao);
                }
                return true;
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

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("search", "onclick");
                Bundle b = new Bundle();
                b.putString("search", edCode.getText().toString());
                b.putInt("where", Info.SEARCHPRODUCT);
                startNewActivityForResult(ProductSearchActivity.class, R.anim.activity_open, 0, Info.SEARCHFORRESULT, b);
            }
        });

        spWhichStorage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                isSpStorageDefault = false;
                storage = (Storage) spWhichStorage.getAdapter().getItem(i);
                wavehouseID = "0";
//                waveHouseAdapter = CommonMethod.getMethod(mContext).getWaveHouseAdapter(storage, spWavehouse);
                spWavehouse.setAuto(mContext, storage, wavehouseAutoString);
                storageId = storage.FItemID;
                storageName = storage.FName;
                Log.e("storageId", storageId);
                Log.e("storageName", storageName);
                getInstorageNum(product);
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

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker(tvDate);
            }
        });

        tvDatePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker(tvDatePay);

            }
        });

//        spPurchaseMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                PurchaseMethod item = payMethodSpAdapter.getChild(i);
//                PurchaseMethodId = item.FItemID;
//                purchaseMethodName = item.FName;
////                share.setPISpayMethod(i);
//                if (isFirst){
//                    share.setPISpayMethod(i);
//                    spPurchaseMethod.setSelection(i);
//                }
//                else{
//                    spPurchaseMethod.setSelection(share.getPISpayMethod());
//                    isFirst=true;
//                }
//
//                Log.e("purchaseMethodName", purchaseMethodName);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

//        spCapturePerson.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Employee employee = (Employee) employeeSpAdapter.getItem(i);
//                capturePersonId = employee.FItemID;
//                capyurePersonName = employee.FName;
////                share.setPISkeepperson(i);
//                if (isFirst2){
//                    share.setPISkeepperson(i);
//                    spCapturePerson.setSelection(i);
//                }
//                else{
//                    spCapturePerson.setSelection(share.getPISkeepperson());
//                    isFirst2=true;
//                }
//                Log.e("capyurePersonName", capyurePersonName);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

//        spSignPerson.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Employee employee = (Employee) employeeSpAdapter.getItem(i);
//                signPersonId = employee.FItemID;
//                signPersonName = employee.FName;
////                share.setPISyanshou(i);
//                if (isFirst3){
//                    share.setPISyanshou(i);
//                    spSignPerson.setSelection(i);
//                }
//                else{
//                    spSignPerson.setSelection(share.getPISyanshou());
//                    isFirst3=true;
//                }
//                Log.e("signPersonName", signPersonName);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

//        spDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Department department = (Department) departMentAdapter.getItem(i);
//                departmentId = department.FItemID;
//                departmentName = department.FName;
////                share.setPISdepartment(i);
//                if (isFirst4){
//                    share.setPISdepartment(i);
//                    spDepartment.setSelection(i);
//                }
//                else{
//                    spDepartment.setSelection(share.getPISdepartment());
//                    isFirst4=true;
//                }
//                Log.e("departmentName", departmentName);
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
//                Employee employee = (Employee) employeeSpAdapter.getItem(i);
//                PersonId = employee.FItemID;
//                PersonName = employee.FName;
////                share.setPISemployee(i);
//                if (isFirst5){
//                    share.setPISemployee(i);
//                    spEmployee.setSelection(i);
//                }
//                else{
//                    spEmployee.setSelection(share.getPISemployee());
//                    isFirst5=true;
//                }
//                Log.e("1111", PersonName);
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
//                Employee employee = (Employee) employeeSpAdapter.getItem(i);
//                managerId = employee.FItemID;
//                managerName = employee.FName;
////                share.setPISManager(i);
//                if (isFirst6){
//                    share.setPISManager(i);
//                    spManager.setSelection(i);
//                }
//                else{
//                    spManager.setSelection(share.getPISManager());
//                    isFirst6=true;
//                }
//                Log.e("1111", managerName);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

//        spWanglaikemu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                if (wanglaikemuList.size() > 0) {
//                    wanglaikemuId = wanglaikemuList.get(i).FAccountID;
//                    wanglaikemuName = wanglaikemuList.get(i).FFullName;
//                } else {
//                    wanglaikemuId = "";
//                    wanglaikemuName = "";
//                }
////                share.setPISwanglaikemu(i);
//                if (isFirst7){
//                    share.setPISwanglaikemu(i);
//                    spWanglaikemu.setSelection(i);
//                }
//                else{
//                    spWanglaikemu.setSelection(share.getPISwanglaikemu());
//                    isFirst7=true;
//                }
//
//                Log.e("1111", unitName + "");
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
        cbHebing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isHebing = b;
                Log.e("ishebing", isHebing + "");
            }
        });

        spUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Unit unit = (Unit) spUnit.getAdapter().getItem(i);
//                if (unit != null) {
//                    unitId = unit.FMeasureUnitID;
//                    unitName = unit.FName;
//                    unitrate = MathUtil.toD(unit.FCoefficient);
//                    Log.e("unitId", unitId + "");
//                }

                getInstorageNum(product);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        edSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edSupplier.getText().toString().equals("")) {
                    edSupplier.selectAll();
                }
            }
        });

        autoAdd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isAuto = b;
                share.setPUISisAuto(b);
            }
        });
    }

    @Override
    protected void OnReceive(String code) {
        if (edPihao.hasFocus()) {
            edPihao.setText(code);
            getInstorageNum(product);
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

    private void LoadBasicData() {

//        storageSpAdapter = CommonMethod.getMethod(mContext).getStorageSpinner(spWhichStorage);
//        payMethodSpAdapter = CommonMethod.getMethod(mContext).getPayMethodSpinner(spPurchaseMethod);
//        employeeSpAdapter = CommonMethod.getMethod(mContext).getEmployeeAdapter(spCapturePerson);
//        departMentAdapter = CommonMethod.getMethod(mContext).getDepartMentAdapter(spDepartment);
//        wanglaikemuList = CommonMethod.getMethod(mContext).getwlkmAdapter(spWanglaikemu);

//        spSignPerson.setAdapter(employeeSpAdapter);
//        spManager.setAdapter(employeeSpAdapter);
//        spEmployee.setAdapter(employeeSpAdapter);
//        employeeSpAdapter.notifyDataSetChanged();

//        spPurchaseMethod.setSelection(share.getPISpayMethod());
//        spCapturePerson.setSelection(share.getPISkeepperson());
//        spSignPerson.setSelection(share.getPISyanshou());
//        spManager.setSelection(share.getPISManager());
//        spEmployee.setSelection(share.getPISemployee());
//        spDepartment.setSelection(share.getPISdepartment());
//        spWanglaikemu.setSelection(share.getPISwanglaikemu());

        tvDate.setText(share.getPISdate());
        tvDatePay.setText(share.getPISdatepay());
        spWhichStorage.setAutoSelection(getString(R.string.spStorage_pis), "");
        spPurchaseMethod.setAutoSelection(getString(R.string.spPurchaseMethod_pis), "");
        spDepartment.setAutoSelection(getString(R.string.spDepartment_pis), "");
        spCapturePerson.setAutoSelection(getString(R.string.spCapturePerson_pis), "");
        spSignPerson.setAutoSelection(getString(R.string.spSignPerson_pis), "");
        spManager.setAutoSelection(getString(R.string.spManager_pis), "");
        spEmployee.setAutoSelection(getString(R.string.spEmployee_pis), "");
        spWanglaikemu.setAutoSelection(getString(R.string.spWanglaikemu_pis), "");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
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
        } else if (requestCode == Info.SEARCHFORRESULTPRODUCT) {
            if (resultCode == Info.SEARCHFORRESULTPRODUCT) {
                Bundle b = data.getExtras();
                supplierid = b.getString("001");
                supplierName = b.getString("002");
                edSupplier.setText(supplierName);
                setfocus(edCode);
            }
        }
    }

    private void setDATA(String fnumber, boolean flag) {
        default_unitID = null;
        list = null;
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
                            Lg.e("products:" + dBean.products.toString());
                            default_unitID = dBean.products.get(0).FUnitID;
                            getProductOL(dBean, 0);
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
                                    default_unitID = dBean.products.get(i).FUnitID;
                                    getProductOL(dBean, i);
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
                final List<BarCode> barCodes = barCodeDao.queryBuilder().where(
                        BarCodeDao.Properties.FBarCode.eq(fnumber)
                ).build().list();
                if (barCodes.size() > 0) {
                    if (barCodes.size() == 1) {
                        list = productDao.queryBuilder().where(
                                ProductDao.Properties.FItemID.eq(barCodes.get(0).FItemID)
                        ).build().list();
                        default_unitID = barCodes.get(0).FUnitID;
//                        chooseUnit(default_unitID);
                        getProductOFL(list);
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
//                                chooseUnit(default_unitID);
                                list = productDao.queryBuilder().where(ProductDao.Properties.FItemID.eq(barCode.FItemID)).build().list();
                                getProductOFL(list);
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
            edPihao.setEnabled(false);
        }
    }

    private void getProductOL(DownloadReturnBean dBean, int j) {
        product = dBean.products.get(j);
        tvorisAuto(product);
    }

    //库存
    private void getInstorageNum(Product product) {
        if (product == null) {
            return;
        }
        String pihao = "";
        if (fBatchManager) {
            pihao = edPihao.getText().toString();
            if (pihao.equals("")) {
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
                    double num = MathUtil.toD(cBean.returnJson);
                    tvNuminstorage.setText((num / spUnit.getDataUnitrate()) + "");
                }

                @Override
                public void onFailed(String Msg, AsyncHttpClient client) {
                    tvNuminstorage.setText("0");
                }
            });
        } else {
            InStorageNumDao inStorageNumDao = daoSession.getInStorageNumDao();
            List<InStorageNum> list1 = inStorageNumDao.queryBuilder().where(
                    InStorageNumDao.Properties.FItemID.eq(product.FItemID),
                    InStorageNumDao.Properties.FStockID.eq(storage.FItemID),
                    InStorageNumDao.Properties.FStockPlaceID.eq(spWavehouse.getWaveHouseId()),
                    InStorageNumDao.Properties.FBatchNo.eq(pihao)
            ).build().list();
            if (list1.size() > 0) {
                Log.e("FQty", list1.get(0).FQty);
                qty = MathUtil.toD(list1.get(0).FQty);
                Log.e("qty", qty + "");
                if (qty != null) {
                    if (spUnit.getDataUnitrate() != null) {
                        tvNuminstorage.setText((qty / spUnit.getDataUnitrate()) + "");
                    } else {
                        tvNuminstorage.setText("0");
                        Lg.e("unitrate为空");
                    }
                }
            } else {
                tvNuminstorage.setText("0");
            }

        }


    }

    private void tvorisAuto(final Product product) {
        try {
            edCode.setText(product.FNumber);
            tvModel.setText(product.FModel);
            wavehouseAutoString = product.FSPID;
            edPricesingle.setText(df.format(MathUtil.toD(product.FSalePrice)));
            tvGoodName.setText(product.FName);
            if ((product.FBatchManager) != null && (product.FBatchManager).equals("1")) {
                setfocus(edPihao);
                fBatchManager = true;
                edPihao.setEnabled(true);
            } else {
                edPihao.setText("");
                edPihao.setEnabled(false);
                fBatchManager = false;
            }
            if (isGetDefaultStorage) {
                spWhichStorage.setAutoSelection(getString(R.string.spStorage_pis), product.FDefaultLoc);
//            for (int j = 0; j < storageSpAdapter.getCount(); j++) {
//                if (((Storage) storageSpAdapter.getItem(j)).FItemID.equals(product.FDefaultLoc)) {
//                    spWhichStorage.setSelection(j);
//                    break;
//                }
//            }
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
            Lg.e("默认单位：" + default_unitID);
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

            if ((isAuto && !fBatchManager) || (isAuto && fBatchManager && !edPihao.getText().toString().equals(""))) {
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

    @OnClick({R.id.btn_add, R.id.btn_finishorder, R.id.btn_checkorder, R.id.search_supplier,R.id.scanbyCamera})
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
            case R.id.btn_add:
                Addorder();
                break;
            case R.id.btn_finishorder:
                finishOrder();
                break;
            case R.id.btn_checkorder:
                Bundle b = new Bundle();
                b.putInt("activity", activity);
                startNewActivity(TableActivity.class, R.anim.activity_fade_in, R.anim.activity_fade_out, false, b);
                break;
            case R.id.search_supplier:
                SearchSupplier();
                break;
        }
    }

    private void upload() {
        PurchaseInStoreUploadBean pBean = new PurchaseInStoreUploadBean();
        PurchaseInStoreUploadBean.purchaseInStore puBean = pBean.new purchaseInStore();
        ArrayList<String> detailContainer = new ArrayList<>();
        ArrayList<PurchaseInStoreUploadBean.purchaseInStore> data = new ArrayList<>();
        List<T_main> mains = t_mainDao.queryBuilder().where(
                T_mainDao.Properties.Activity.eq(activity)
        ).build().list();
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
                        t_main.FSendOutId + "|" +
                        t_main.FCustodyId + "|" +
                        t_main.FRedBlue + "|" +
                        "" + "|" +
                        t_main.sourceOrderTypeId + "|" +
                        t_main.FAcountID + "|" +
                        t_main.orderId + "|";
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
        DataModel.upload(WebApi.UPLOADPIS, gson.toJson(pBean));
//        postToServer(data);
    }

    private void postToServer(ArrayList<PurchaseInStoreUploadBean.purchaseInStore> data) {
        PurchaseInStoreUploadBean pBean = new PurchaseInStoreUploadBean();
        pBean.list = data;
        Gson gson = new Gson();
        Asynchttp.post(mContext, getBaseUrl() + WebApi.UPLOADPIS, gson.toJson(pBean), new Asynchttp.Response() {
            @Override
            public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
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
                MediaPlayer.getInstance(mContext).ok();
            }

            @Override
            public void onFailed(String Msg, AsyncHttpClient client) {
                Toast.showText(mContext, Msg);
                btnBackorder.setClickable(true);
                LoadingUtil.dismiss();
                MediaPlayer.getInstance(mContext).error();
            }
        });
    }

    private void SearchSupplier() {
        Bundle b = new Bundle();
        b.putString("search", edSupplier.getText().toString());
        b.putInt("where", Info.SEARCHSUPPLIER);
        startNewActivityForResult(ProductSearchActivity.class, R.anim.activity_open, 0, Info.SEARCHFORRESULTPRODUCT, b);

    }

    private void Addorder() {
        try {
            String discount = "0";
            String num = edNum.getText().toString();
            if (isRed) {
                num = "-" + edNum.getText().toString();
            } else {
                num = edNum.getText().toString();
            }
            if (product==null){
                MediaPlayer.getInstance(mContext).error();
                Toast.showText(mContext, "请选择物料");
                return;
            }
            if (supplierid==null){
                MediaPlayer.getInstance(mContext).error();
                Toast.showText(mContext, "请选择供应商");
                return;
            }
            if (edSupplier.getText().toString().equals("") || edCode.getText().toString().equals("") || edPricesingle.getText().toString().equals("") || edNum.getText().toString().equals("")) {
                MediaPlayer.getInstance(mContext).error();
                if (edCode.getText().toString().equals("")) {
                    setfocus(edCode);
                    Toast.showText(mContext, "请输入物料编号");
                } else if (edPricesingle.getText().toString().equals("")) {
                    setfocus(edPricesingle);
                    Toast.showText(mContext, "请输入单价");
                } else if (edNum.getText().toString().equals("")) {
                    setfocus(edNum);
                    Toast.showText(mContext, "请输入数量");
                } else if (edSupplier.getText().toString().equals("") || supplierid.equals("")) {
                    setfocus(edSupplier);
                    Toast.showText(mContext, "请输入供应商");
                }
            } else if (fBatchManager && edPihao.getText().toString().equals("")) {
                setfocus(edPihao);
                Toast.showText(mContext, "请输入批次号");
            } else if (isRed && (Math.abs(MathUtil.toD(num)) > (qty / spUnit.getDataUnitrate()))) {
                Toast.showText(mContext, "库存不足");
            } else {
                if (isHebing) {
                    Log.e("wavehouseID", spWavehouse.getWaveHouseId());
//                    Log.e("unitId", unitId);
                    List<T_Detail> detailhebing = t_detailDao.queryBuilder().where(
                            T_DetailDao.Properties.Activity.eq(activity),
                            T_DetailDao.Properties.FOrderId.eq(ordercode),
                            T_DetailDao.Properties.FProductId.eq(product.FItemID),
                            T_DetailDao.Properties.FBatch.eq(edPihao.getText().toString()),
                            T_DetailDao.Properties.FUnitId.eq(spUnit.getDataId()),
                            T_DetailDao.Properties.FStorageId.eq(storageId),
                            T_DetailDao.Properties.FPositionId.eq(spWavehouse.getWaveHouseId())).build().list();
                    if (detailhebing.size() > 0) {
                        for (int i = 0; i < detailhebing.size(); i++) {
                            num = (MathUtil.toD(num) + MathUtil.toD(detailhebing.get(i).FQuantity)) + "";
                            List<T_main> t_mainList = t_mainDao.queryBuilder().where(
                                    T_mainDao.Properties.FIndex.eq(detailhebing.get(i).FIndex)
                            ).build().list();
                            t_detailDao.delete(detailhebing.get(i));
                        }
                    }
                }
                List<T_main> delete = t_mainDao.queryBuilder().where(
                        T_mainDao.Properties.OrderId.eq(ordercode)
                ).build().list();
                t_mainDao.deleteInTx(delete);
                String second = getTimesecond();
                T_main t_main = new T_main();
                t_main.FDepartment = spDepartment.getDataName();
                t_main.FDepartmentId = spDepartment.getDataId();
                t_main.FIndex = second;
                t_main.FPaymentDate = tvDatePay.getText().toString();
                t_main.orderId = ordercode;
                t_main.orderDate = share.getPISdate();
                t_main.FPurchaseUnit = spUnit.getDataName();
                t_main.FSalesMan = spEmployee.getEmployeeName();
                t_main.FSalesManId = spEmployee.getEmployeeId();
                t_main.FMaker = share.getUserName();
                t_main.FMakerId = share.getsetUserID();
                t_main.FDirector = spManager.getEmployeeName();
                t_main.FDirectorId = spManager.getEmployeeId();
                t_main.saleWay = spPurchaseMethod.getDataName();
                t_main.FDeliveryAddress = "";
                t_main.FRemark = edZhaiyao.getText().toString();
                t_main.saleWayId = spPurchaseMethod.getDataId();
                t_main.FCustody = spCapturePerson.getEmployeeName();
                t_main.FCustodyId = spCapturePerson.getEmployeeId();
                t_main.FAcount = spWanglaikemu.getDataName();
                t_main.FAcountID = spWanglaikemu.getDataId();
                t_main.Rem = edZhaiyao.getText().toString();
                t_main.supplier = supplierName == null ? "" : supplierName;
                t_main.supplierId = supplierid == null ? "" : supplierid;
                t_main.FSendOutId = spSignPerson.getEmployeeId();
                t_main.FRedBlue = redblue;
                t_main.activity = activity;
                t_main.sourceOrderTypeId = "";
                long insert1 = t_mainDao.insert(t_main);

                T_Detail t_detail = new T_Detail();
                t_detail.FBatch = edPihao.getText().toString();
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

                long insert = t_detailDao.insert(t_detail);

                if (insert1 > 0 && insert > 0) {
                    Lg.e("添加成功"+t_detail.toString());
                    Toast.showText(mContext, "添加成功");
                    MediaPlayer.getInstance(mContext).ok();
                    if (!BasicShareUtil.getInstance(mContext).getIsOL()) {
                        InStorageNumDao inStorageNumDao = daoSession.getInStorageNumDao();
                        List<InStorageNum> innum = inStorageNumDao.queryBuilder().where(
                                InStorageNumDao.Properties.FBatchNo.eq(edPihao.getText().toString()),
                                InStorageNumDao.Properties.FStockID.eq(storageId),
                                InStorageNumDao.Properties.FStockPlaceID.eq(spWavehouse.getWaveHouseId()),
                                InStorageNumDao.Properties.FItemID.eq(product.FItemID)
                        ).build().list();
                        if (innum.size() > 0) {
                            innum.get(0).FQty = (MathUtil.toD(innum.get(0).FQty) + (MathUtil.toD(edNum.getText().toString()) * spUnit.getDataUnitrate())) + "";
                            inStorageNumDao.update(innum.get(0));
                        } else {
                            InStorageNum i = new InStorageNum();
                            i.FQty = (MathUtil.toD(edNum.getText().toString()) * spUnit.getDataUnitrate()) + "";
                            i.FItemID = product.FItemID;
                            i.FBatchNo = edPihao.getText().toString();
                            i.FStockID = storageId;
                            i.FStockPlaceID = spWavehouse.getWaveHouseId();
                            inStorageNumDao.insert(i);
                        }
                    }

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

    private void resetAll() {
        redorBlue.setVisibility(View.GONE);
        edNum.setText("");
        edPricesingle.setText("");
        edPihao.setText("");
        edZhaiyao.setText("");
        edCode.setText("");
        tvNuminstorage.setText("");
        tvGoodName.setText("");
        tvModel.setText("");
        setfocus(edCode);
        product=null;
    }

    public void finishOrder() {
        redorBlue.setVisibility(View.VISIBLE);
        AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
        ab.setTitle("确认使用完单");
        ab.setMessage("确认？");
        ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ordercode++;
                Log.e("ordercode", ordercode + "");
                share.setOrderCode(PurchaseInStorageActivity.this, ordercode);
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
                share.setOrderCode(PurchaseInStorageActivity.this, ordercode);
                finish();
            }
        });
        ab.setNegativeButton("取消", null);
        ab.create().show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e("按键", keyCode + "");
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}

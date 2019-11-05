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
import com.fangzuo.assist.Adapter.InStoreTypeSpAdapter;
import com.fangzuo.assist.Adapter.PiciSpAdapter;
import com.fangzuo.assist.Adapter.ProductselectAdapter;
import com.fangzuo.assist.Adapter.ProductselectAdapter1;
import com.fangzuo.assist.Adapter.StorageSpAdapter;
import com.fangzuo.assist.Adapter.UnitSpAdapter;
import com.fangzuo.assist.Adapter.WaveHouseSpAdapter;
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
import com.fangzuo.assist.Dao.InStoreType;
import com.fangzuo.assist.Dao.Product;
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
import com.fangzuo.assist.widget.SpinnerPeople;
import com.fangzuo.assist.widget.SpinnerStorage;
import com.fangzuo.assist.widget.SpinnerStoreType;
import com.fangzuo.assist.widget.SpinnerUnit;
import com.fangzuo.assist.widget.SpinnerWaveHouse;
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
import butterknife.Unbinder;

//其他出库
public class OtherOutStoreActivity extends BaseActivity {


    @BindView(R.id.ishebing)
    CheckBox cbHebing;
    @BindView(R.id.ed_supplier)
    EditText edSupplier;
    @BindView(R.id.search_supplier)
    RelativeLayout searchSupplier;
    @BindView(R.id.sp_which_storage)
    SpinnerStorage spWhichStorage;
    @BindView(R.id.sp_wavehouse)
    SpinnerWaveHouse spWavehouse;
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
    @BindView(R.id.ed_pricesingle)
    EditText edPricesingle;
    @BindView(R.id.sp_unit)
    SpinnerUnit spUnit;
    @BindView(R.id.ed_num)
    EditText edNum;
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
    @BindView(R.id.sp_inStoreType)
    SpinnerStoreType spInStoreType;
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
    @BindView(R.id.ed_zhaiyao)
    EditText edZhaiyao;
    @BindView(R.id.drawer)
    DrawerLayout drawer;
    @BindView(R.id.isAutoAdd)
    CheckBox autoAdd;
    @BindView(R.id.sp_pihao)
    Spinner spPihao;


    private DecimalFormat df;
    private long ordercode;
    private EmployeeSpAdapter employeeSpAdapter;
//    private StorageSpAdapter storageSpAdapter;
    private DepartmentSpAdapter departMentAdapter;
    private InStoreTypeSpAdapter inStoreType;
    private String supplierid;
    private String supplierName;
    private List<Product> Products;
    private boolean isGetDefaultStorage;
    private boolean fBatchManager;
    private boolean isSpStorageDefault;
    private Storage storage;
    private WaveHouseSpAdapter waveHouseAdapter;
    private String storageId;
    private String storageName;
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
    private String wavehouseID;
    private String wavehouseName;
    private UnitSpAdapter unitAdapter;
    private String pihao;
//    private String unitId;
//    private String unitName;
//    private double unitrate;
    private String date;
    //    private String inStoreTypeId;
//    private String inStoreTypeName;
    private boolean isHebing = true;
    private boolean isAuto;


    @BindView(R.id.cb_isStorage)
    CheckBox cbIsStorage;
    private Product product;
    private ProductselectAdapter productselectAdapter;
    private ProductselectAdapter1 productselectAdapter1;
    private double qty = 0.0;
    private PiciSpAdapter piciSpAdapter;
    private String default_unitID;
    private ArrayList<String> detailContainer;
    private boolean checkStorage = false;  // 0不允许负库存false  1允许负库存出库true
    private String wavehouseAutoString = "";
    private int activity = Config.OtherOutStoreActivity;
    private OtherOutStoreActivity mContext;
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
        setContentView(R.layout.activity_other_out_store);
        mContext = this;
        Unbinder bind = ButterKnife.bind(this);
        share = ShareUtil.getInstance(mContext);
        df = new DecimalFormat("######0.00");
        initDrawer(drawer);
        cbHebing.setChecked(isHebing);
        autoAdd.setChecked(share.getOISisAuto());
        isAuto = share.getOISisAuto();
        isGetDefaultStorage = share.getBoolean(Info.Storage + activity);
        cbIsStorage.setChecked(isGetDefaultStorage);
    }

    @Override
    protected void initData() {
//        if (share.getOISOrderCode() == 0) {
//            ordercode = Long.parseLong(getTime(false) + "001");
//            Log.e("ordercode", ordercode + "");
//            share.setOISOrderCode(ordercode);
//        } else {
//            ordercode = share.getOISOrderCode();
//            Log.e("ordercode", ordercode + "");
//        }
        ordercode = CommonUtil.createOrderCode(this);
        LoadBasicData();
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
                share.setOISisAuto(b);
            }
        });
        spPihao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                InStorageNum inStorageNum = (InStorageNum) piciSpAdapter.getItem(i);
                pihao = inStorageNum.FBatchNo;
                getInstorageNum(product);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
//        spCapturePerson.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Employee employee = (Employee) employeeSpAdapter.getItem(i);
//                capturePersonId = employee.FItemID;
//                capyurePersonName = employee.FName;
////                share.setOOSkeepperson(i);
//                if (isFirst){
//                    share.setOOSkeepperson(i);
//                    spCapturePerson.setSelection(i);
//                }
//                else{
//                    spCapturePerson.setSelection(share.getOOSkeepperson());
//                    isFirst=true;
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
////                share.setOOSyanshou(i);
//                if (isFirst2){
//                    share.setOOSyanshou(i);
//                    spSignPerson.setSelection(i);
//                }
//                else{
//                    spSignPerson.setSelection(share.getOOSyanshou());
//                    isFirst2=true;
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
////                share.setOOSdepartment(i);
//                if (isFirst3){
//                    share.setOOSdepartment(i);
//                    spDepartment.setSelection(i);
//                }
//                else{
//                    spDepartment.setSelection(share.getOOSdepartment());
//                    isFirst3=true;
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
////                share.setOOSemployee(i);
//                if (isFirst4){
//                    share.setOOSemployee(i);
//                    spEmployee.setSelection(i);
//                }
//                else{
//                    spEmployee.setSelection(share.getOOSemployee());
//                    isFirst4=true;
//                }
//                Log.e("1111", PersonName);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

//        spInStoreType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                InStoreType inStoreType1 = (InStoreType) inStoreType.getItem(i);
//                inStoreTypeId = inStoreType1.FID;
//                inStoreTypeName = inStoreType1.FName;
////                share.setOOSInstoreType(i);
//                if (isFirst5){
//                    share.setOOSInstoreType(i);
//                    spInStoreType.setSelection(i);
//                }
//                else{
//                    spInStoreType.setSelection(share.getOOSInstoreType());
//                    isFirst5=true;
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
//                Employee employee = (Employee) employeeSpAdapter.getItem(i);
//                managerId = employee.FItemID;
//                managerName = employee.FName;
////                share.setOOSManager(i);
//                if (isFirst6){
//                    share.setOOSManager(i);
//                    spManager.setSelection(i);
//                }
//                else{
//                    spManager.setSelection(share.getOOSManager());
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

        spWhichStorage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                isSpStorageDefault = false;
                storage = (Storage) spWhichStorage.getAdapter().getItem(i);
                Hawk.put(getString(R.string.spStorage_oos),storage.FName);

                Lg.e("仓库：、、、、、" + storage.toString());
                if ("1".equals(storage.FUnderStock)) {
                    checkStorage = true;
                } else {
                    checkStorage = false;
                }
                wavehouseID = "0";
//                waveHouseAdapter = CommonMethod.getMethod(mContext).getWaveHouseAdapter(storage, spWavehouse);
                spWavehouse.setAuto(mContext, storage, wavehouseAutoString);

                storageId = storage.FItemID;
                storageName = storage.FName;
                Log.e("storageId", storageId);
                Log.e("storageName", storageName);
                getpici();

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
        edSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edSupplier.getText().toString().equals("")) {
                    edSupplier.selectAll();
                }
            }
        });
    }

    @Override
    protected void OnReceive(String code) {
        edCode.setText(code);
        setDATA(code, false);
    }

    private void LoadBasicData() {
//        storageSpAdapter = CommonMethod.getMethod(mContext).getStorageSpinner(spWhichStorage);
        spWhichStorage.setAutoSelection(getString(R.string.spStorage_oos), "");
        spCapturePerson.setAutoSelection(getString(R.string.spCapturePerson_oos), "");
        spDepartment.setAutoSelection(getString(R.string.spDepartment_oos), "");
        spInStoreType.setAutoSelection(getString(R.string.spInStoreType_oos), "");
        spSignPerson.setAutoSelection(getString(R.string.spSignPerson_oos), "");
        spManager.setAutoSelection(getString(R.string.spManager_oos), "");
        spEmployee.setAutoSelection(getString(R.string.spEmployee_oos), "");

//        employeeSpAdapter = CommonMethod.getMethod(mContext).getEmployeeAdapter(spCapturePerson);
//        departMentAdapter = CommonMethod.getMethod(mContext).getDepartMentAdapter(spDepartment);
//        spSignPerson.setAdapter(employeeSpAdapter);
//        spManager.setAdapter(employeeSpAdapter);
//        spEmployee.setAdapter(employeeSpAdapter);
//        inStoreType = CommonMethod.getMethod(mContext).getInStoreType(spInStoreType);

//        spCapturePerson.setSelection(share.getOOSkeepperson());
//        spSignPerson.setSelection(share.getOOSyanshou());
//        spManager.setSelection(share.getOOSManager());
//        spEmployee.setSelection(share.getOOSemployee());
//        spDepartment.setSelection(share.getOOSdepartment());
//        spInStoreType.setSelection(share.getOOSInstoreType());

        tvDate.setText(share.getOOSdate());
    }

    @OnClick({R.id.search_supplier, R.id.scanbyCamera, R.id.search, R.id.btn_add, R.id.btn_finishorder,R.id.btn_checkorder, R.id.tv_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.search_supplier:
                Bundle b = new Bundle();
                b.putString("search", edSupplier.getText().toString());
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
        } else if (requestCode == Info.SEARCHFORRESULTCLIRNT) {
            if (resultCode == Info.SEARCHFORRESULTCLIRNT) {
                Bundle b = data.getExtras();
                supplierid = b.getString("001");
                supplierName = b.getString("002");
                edSupplier.setText(supplierName);
                setfocus(edCode);
            }


        }
    }

    //获取批次
    private void getpici() {
        List<InStorageNum> container1 = new ArrayList<>();
        piciSpAdapter = new PiciSpAdapter(mContext, container1);
        spPihao.setAdapter(piciSpAdapter);
        if (product == null) {
            return;
        }
        if (fBatchManager) {
            Log.e(TAG, "进入获取批次");
            spPihao.setEnabled(true);
            if (!BasicShareUtil.getInstance(mContext).getIsOL()) {
                piciSpAdapter = CommonMethod.getMethod(mContext).getPici(storage, spWavehouse.getWaveHouseId(), product, spPihao);
            } else {
                final List<InStorageNum> container = new ArrayList<>();
                piciSpAdapter = new PiciSpAdapter(mContext, container);
                spPihao.setAdapter(piciSpAdapter);
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
                        }
                        piciSpAdapter.notifyDataSetChanged();
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
        try {
            Log.e(TAG, "tvorisAuto获取product：" + product.toString());
            edCode.setText(product.FNumber);
            tvModel.setText(product.FModel);
            wavehouseAutoString = product.FSPID;
            edPricesingle.setText(df.format(MathUtil.toD(product.FSalePrice)));
            tvGoodName.setText(product.FName);
            fBatchManager = (product.FBatchManager) != null && (product.FBatchManager).equals("1");
            if (isGetDefaultStorage) {
                spWhichStorage.setAutoSelection(getString(R.string.spStorage_oos), product.FDefaultLoc);
//
//                for (int j = 0; j < storageSpAdapter.getCount(); j++) {
//                    if (((Storage) storageSpAdapter.getItem(j)).FItemID.equals(product.FDefaultLoc)) {
//                        spWhichStorage.setSelection(j);
//                        break;
//                    }
//                }
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

            getpici();
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

            getInstorageNum(product);

            if (isAuto) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        edNum.setText("1.0");
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
        Lg.e("setDate",fnumber);
        Products = null;
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
                            lv.setAdapter(productselectAdapter1);
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
                        Products = productDao.queryBuilder().where(ProductDao.Properties.FItemID.eq(barCodes.get(0).FItemID)).build().list();
                        default_unitID = barCodes.get(0).FUnitID;
                        getProductOFL(Products);
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
                                Products = productDao.queryBuilder().where(ProductDao.Properties.FItemID.eq(barCode.FItemID)).build().list();
                                default_unitID = barCode.FUnitID;
                                getProductOFL(Products);
                                alertDialog.dismiss();
                            }
                        });
                    }
                } else {
                    MediaPlayer.getInstance(mContext).error();
                    Toast.showText(mContext, "未找到条码");
                }
//                if (Products != null && Products.size() > 0) {
////                    product = Products.get(0);
////                    tvorisAuto(product);
//                } else {
//                    Toast.showText(mContext, "未找到物料");
//                    MediaPlayer.getInstance(mContext).error();
//                }
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

    private void getInstorageNum(Product product) {
        if (product == null) {
            return;
        }
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
            Log.e("getInstorageNum库存提交", json);
            Asynchttp.post(mContext, getBaseUrl() + WebApi.GETINSTORENUM, json, new Asynchttp.Response() {
                @Override
                public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                    Log.e(TAG, "库存返回：" + cBean.returnJson);
                    qty = MathUtil.toD(cBean.returnJson);
//                    tvNuminstorage.setText((qty/unitrate) + "");
                    tvNuminstorage.setText(dealStoreNumForOut(qty / spUnit.getDataUnitrate() + "") + "");

                }

                @Override
                public void onFailed(String Msg, AsyncHttpClient client) {
                    Log.e(TAG, "库存错误返回：" + Msg);
                    Toast.showText(mContext, Msg);
                    tvNuminstorage.setText("0");
                    qty = 0.0;
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
                tvNuminstorage.setText((qty / spUnit.getDataUnitrate()) + "");


            } else {
                tvNuminstorage.setText("0");
                qty = 0.0;
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

    private void Addorder() {
        try {
//            if (wavehouseID == null) {
//                wavehouseID = "0";
//            }
            if (supplierid==null || "".equals(edSupplier.getText().toString())) {
                Toast.showText(mContext, "请选择客户");
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
            String discount = "0";
            String num = edNum.getText().toString();

            //是否开启库存管理 true，开启允许负库存
            if (!checkStorage) {
//            if ((qty/unitrate)<MathUtil.toD(num)) {
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
            t_main.orderDate = share.getOISdate();
            t_main.FPurchaseUnit = spUnit.getDataName();
            t_main.FSalesMan = spEmployee.getEmployeeName();
            t_main.FSalesManId = spEmployee.getEmployeeId();
            t_main.FMaker = share.getUserName();
            t_main.FMakerId = share.getsetUserID();
            t_main.FDirector = spManager.getEmployeeName();
            t_main.FDirectorId = spManager.getEmployeeId();
            t_main.saleWay = "";
            t_main.FDeliveryAddress = "";
            t_main.FRemark = edZhaiyao.getText().toString();
            t_main.saleWayId = "";
            t_main.FCustody = spCapturePerson.getEmployeeName();
            t_main.FCustodyId = spCapturePerson.getEmployeeId();
            t_main.FAcount = spInStoreType.getDataName();
            t_main.FAcountID = spInStoreType.getDataId();
            t_main.Rem = edZhaiyao.getText().toString();
            t_main.supplier = supplierName == null ? "" : supplierName;
            t_main.supplierId = supplierid == null ? "" : supplierid;
            t_main.FSendOutId = spSignPerson.getEmployeeId();

            t_main.activity = activity;
            t_main.sourceOrderTypeId = "";


            T_Detail t_detail = new T_Detail();
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
            if (!BasicShareUtil.getInstance(mContext).getIsOL()) {
                long insert = t_detailDao.insert(t_detail);
                long insert1 = t_mainDao.insert(t_main);
                if (insert1 > 0 && insert > 0) {
                    InStorageNumDao inStorageNumDao = daoSession.getInStorageNumDao();
                    List<InStorageNum> innum = inStorageNumDao.queryBuilder().where(
                            InStorageNumDao.Properties.FBatchNo.eq(pihao == null ? "" : pihao),
                            InStorageNumDao.Properties.FStockID.eq(storageId),
                            InStorageNumDao.Properties.FStockPlaceID.eq(spWavehouse.getWaveHouseId()),
                            InStorageNumDao.Properties.FItemID.eq(product.FItemID)
                    ).build().list();
                    if (innum.size() == 0) {
                        InStorageNum inStorageNum = new InStorageNum();
                        inStorageNum.FItemID = product.FItemID;
                        inStorageNum.FBatchNo = pihao == null ? "" : pihao;
                        inStorageNum.FStockPlaceID = spWavehouse.getWaveHouseId();
                        inStorageNum.FStockID = storageId;
                        inStorageNum.FQty = "-" + (MathUtil.toD(edNum.getText().toString()) * spUnit.getDataUnitrate());
                        inStorageNumDao.insert(inStorageNum);
                    } else {
                        if (checkStorage) {
                            innum.get(0).FQty = String.valueOf(((MathUtil.toD(innum.get(0).FQty) + (MathUtil.toD("-" + edNum.getText().toString()) * spUnit.getDataUnitrate()))));
                        } else {
                            innum.get(0).FQty = (MathUtil.toD(innum.get(0).FQty) - (MathUtil.toD(edNum.getText().toString()) * spUnit.getDataUnitrate())) + "";
                        }
                    }
                    if (innum.size() != 0) {
                        Toast.showText(mContext, "添加成功");
                        MediaPlayer.getInstance(mContext).ok();
                        inStorageNumDao.update(innum.get(0));
                    }

//                        innum.get(0).FQty = (MathUtil.toD(innum.get(0).FQty) - (MathUtil.toD(edNum.getText().toString()) * unitrate)) + "";
//                        inStorageNumDao.update(innum.get(0));
                    resetAll();
                } else {
                    MediaPlayer.getInstance(mContext).error();
                    Toast.showText(mContext, "添加失败，请重试");
                }
            } else {
                long insert = t_detailDao.insert(t_detail);
                long insert1 = t_mainDao.insert(t_main);
                if (insert1 > 0 && insert > 0) {
                    Toast.showText(mContext, "添加成功");
                    MediaPlayer.getInstance(mContext).ok();
                    resetAll();
                } else {
                    Toast.showText(mContext, "添加失败");
                    MediaPlayer.getInstance(mContext).error();
                }

            }
        } catch (Exception e) {
            DataService.pushError(mContext, this.getClass().getSimpleName(), e);
        }
    }

    private void upload() {
        PurchaseInStoreUploadBean pBean = new PurchaseInStoreUploadBean();

        t_mainDao = daoSession.getT_mainDao();
        t_detailDao = daoSession.getT_DetailDao();
        detailContainer = new ArrayList<>();
        ArrayList<PurchaseInStoreUploadBean.purchaseInStore> data = new ArrayList<>();
        List<T_main> mains = t_mainDao.queryBuilder().where(T_mainDao.Properties.Activity.eq(activity)).build().list();
        for (int i = 0; i < mains.size(); i++) {
            if (i > 0 && mains.get(i).orderId == mains.get(i - 1).orderId) {

            } else {
                PurchaseInStoreUploadBean.purchaseInStore puBean = pBean.new purchaseInStore();
                detailContainer = new ArrayList<>();
                String main;
                String detail = "";
                T_main t_main = mains.get(i);
                main = t_main.FMakerId + "|" +
                        t_main.orderDate + "|" +
                        t_main.FDepartmentId + "|" +
                        t_main.FSalesManId + "|" +
                        t_main.FDirectorId + "|" +
                        t_main.supplierId + "|" +
                        t_main.Rem + "|" +
                        t_main.FSendOutId + "|" +
                        t_main.FCustodyId + "|" +
                        t_main.FRedBlue + "|" +
                        t_main.FAcountID + "|" +
                        t_main.sourceOrderTypeId + "|";
                puBean.main = main;
                List<T_Detail> details = t_detailDao.queryBuilder().where(T_DetailDao.Properties.FOrderId.eq(t_main.orderId), T_DetailDao.Properties.Activity.eq(activity)).build().list();
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
        DataModel.upload(WebApi.OTHEROUTSTORE, gson.toJson(pBean));
//        postToServer(data);
    }

    private void postToServer(ArrayList<PurchaseInStoreUploadBean.purchaseInStore> data) {
        PurchaseInStoreUploadBean pBean = new PurchaseInStoreUploadBean();
        pBean.list = data;
        Gson gson = new Gson();
        Asynchttp.post(mContext, getBaseUrl() + WebApi.OTHEROUTSTORE, gson.toJson(pBean), new Asynchttp.Response() {
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

    private void resetAll() {
        edNum.setText("");
        edPricesingle.setText("");
        edZhaiyao.setText("");
        edCode.setText("");
        tvNuminstorage.setText("");
        tvGoodName.setText("");
        tvModel.setText("");
        List<InStorageNum> container = new ArrayList<>();
        piciSpAdapter = new PiciSpAdapter(mContext, container);
        spPihao.setAdapter(piciSpAdapter);
        setfocus(edCode);
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
                share.setOrderCode(OtherOutStoreActivity.this, ordercode);
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
                share.setOrderCode(OtherOutStoreActivity.this, ordercode);
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



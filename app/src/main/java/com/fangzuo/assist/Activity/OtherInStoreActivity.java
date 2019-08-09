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

//          其他入库
public class OtherInStoreActivity extends BaseActivity {


    @BindView(R.id.ishebing)
    CheckBox cbHebing;
    @BindView(R.id.ed_supplier)
    EditText edSupplier;
    @BindView(R.id.search_supplier)
    RelativeLayout searchSupplier;
    @BindView(R.id.sp_which_storage)
    SpinnerStorage spWhichStorage;
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
    @BindView(R.id.tv_model)
    TextView tvModel;
    @BindView(R.id.tv_numinstorage)
    TextView tvNuminstorage;
    @BindView(R.id.ed_pihao)
    EditText edPihao;
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

    @BindView(R.id.cb_isStorage)
    CheckBox cbIsStorage;

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
//    private String unitId;
//    private String unitName;
//    private double unitrate;
    private String date;
    //    private String inStoreTypeId;
//    private String inStoreTypeName;
    private boolean isHebing = true;
    private boolean isAuto;
    private ProductselectAdapter productselectAdapter;
    private Product product;
    private List<Product> Products;
    private ProductselectAdapter1 productselectAdapter1;
    private String default_unitID;
    private String pihao;
    private PiciSpAdapter piciSpAdapter;
    private String wavehouseAutoString = "";
    private int activity = Config.OtherInStoreActivity;
    private OtherInStoreActivity mContext;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_other_in_store);
        mContext = this;
        Unbinder bind = ButterKnife.bind(this);
        df = new DecimalFormat("######0.00");
        initDrawer(drawer);
        edPihao.setEnabled(false);
        cbHebing.setChecked(isHebing);
        autoAdd.setChecked(share.getOOSisAuto());
        isAuto = share.getOOSisAuto();
        isGetDefaultStorage = share.getBoolean(Info.Storage + activity);
        cbIsStorage.setChecked(isGetDefaultStorage);
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
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                LoadBasicData();
//            }
//        },1000);
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
                share.setOOSisAuto(b);
            }
        });
        edPihao.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == 0 && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    getInstorageNum(product);
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

//        spPihao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                InStorageNum inStorageNum = (InStorageNum) piciSpAdapter.getItem(i);
//                pihao = inStorageNum.FBatchNo;
//                getInstorageNum(product);
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
////                share.setOISkeepperson(i);
//                if (isFirst){
//                    share.setOISkeepperson(i);
//                    spCapturePerson.setSelection(i);
//                }
//                else{
//                    spCapturePerson.setSelection(share.getOISkeepperson());
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
////                share.setOISyanshou(i);
//                if (isFirst2){
//                    share.setOISyanshou(i);
//                    spSignPerson.setSelection(i);
//                }
//                else{
//                    spSignPerson.setSelection(share.getOISyanshou());
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
////                share.setOISdepartment(i);
//                if (isFirst3){
//                    share.setOISdepartment(i);
//                    spDepartment.setSelection(i);
//                }
//                else{
//                    spDepartment.setSelection(share.getOISdepartment());
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
////                share.setOISemployee(i);
//                if (isFirst4){
//                    share.setOISemployee(i);
//                    spEmployee.setSelection(i);
//                }
//                else{
//                    spEmployee.setSelection(share.getOISemployee());
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
////                share.setOISInstoreType(i);
//                if (isFirst5){
//                    share.setOISInstoreType(i);
//                    spInStoreType.setSelection(i);
//                }
//                else{
//                    spInStoreType.setSelection(share.getOISInstoreType());
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
////                share.setOISManager(i);
//                if (isFirst6){
//                    share.setOISManager(i);
//                    spManager.setSelection(i);
//                }
//                else{
//                    spManager.setSelection(share.getOISManager());
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
                Hawk.put(getString(R.string.spStorage_ois),storage.FName);

                wavehouseID = "0";
//                waveHouseAdapter = CommonMethod.getMethod(mContext).getWaveHouseAdapter(storage, spWavehouse);
                spWavehouse.setAuto(mContext, storage, wavehouseAutoString);
                storageId = storage.FItemID;
                storageName = storage.FName;
                Log.e("storageId", storageId);
                Log.e("storageName", storageName);
//                getpici();
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

    //获取批次
    private void getpici() {
        List<InStorageNum> container1 = new ArrayList<>();
        piciSpAdapter = new PiciSpAdapter(mContext, container1);
        spPihao.setAdapter(piciSpAdapter);
        if (product == null) {
            return;
        }
        if (fBatchManager) {
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


    @Override
    protected void OnReceive(String code) {
        try {
            String[] split = code.split("\\^");
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
            Toast.showText(mContext, "条码有误");
        }

    }


    private void LoadBasicData() {
        tvDate.setText(share.getOISdate());
        CommonMethod.getMethod(mContext).getGoodsDepartmentSpAdapter();
//        storageSpAdapter = CommonMethod.getMethod(mContext).getStorageSpinner(spWhichStorage);
        spWhichStorage.setAutoSelection(getString(R.string.spStorage_ois), "");

        spInStoreType.setAutoSelection(getString(R.string.spInStoreType_ois), "");
        spCapturePerson.setAutoSelection(getString(R.string.spCapturePerson_ois), "");
        spEmployee.setAutoSelection(getString(R.string.spEmployee_ois), "");
        spManager.setAutoSelection(getString(R.string.spManager_ois), "");
        spSignPerson.setAutoSelection(getString(R.string.spSignPerson_ois), "");
        spDepartment.setAutoSelection(getString(R.string.spDepartment_ois), "");

//        inStoreType = CommonMethod.getMethod(mContext).getInStoreType(spInStoreType);
//        employeeSpAdapter = CommonMethod.getMethod(mContext).getEmployeeAdapter(spCapturePerson);
//        departMentAdapter = CommonMethod.getMethod(mContext).getDepartMentAdapter(spDepartment);
//        spSignPerson.setAdapter(employeeSpAdapter);
//        spManager.setAdapter(employeeSpAdapter);
//        spEmployee.setAdapter(employeeSpAdapter);
//        spCapturePerson.setSelection(share.getOISkeepperson());
//        spSignPerson.setSelection(share.getOISyanshou());
//        spManager.setSelection(share.getOISManager());
//        spEmployee.setSelection(share.getOISemployee());
//        spDepartment.setSelection(share.getOISdepartment());
//        spInStoreType.setSelection(share.getOISInstoreType());
    }

    @OnClick({R.id.search_supplier, R.id.scanbyCamera, R.id.search, R.id.btn_add, R.id.btn_finishorder, R.id.btn_checkorder, R.id.tv_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.search_supplier:
                Bundle b = new Bundle();
                b.putString("search", edSupplier.getText().toString());
                b.putInt("where", Info.SEARCHSUPPLIER);
                startNewActivityForResult(ProductSearchActivity.class, R.anim.activity_open, 0, Info.SEARCHFORRESULTPRODUCT, b);
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

    private void getProductOL(DownloadReturnBean dBean, int j) {
        product = dBean.products.get(j);
        tvorisAuto(product);
    }

    private void setDATA(String fnumber, boolean flag) {
        default_unitID = null;
        Products = null;
//        edPihao.setText("");
        final ProductDao productDao = daoSession.getProductDao();
        BarCodeDao barCodeDao = daoSession.getBarCodeDao();
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
                                default_unitID = barCode.FUnitID;
                                Products = productDao.queryBuilder().where(ProductDao.Properties.FItemID.eq(barCode.FItemID)).build().list();
                                getProductOFL(Products);
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
                pihao = "";
                edPihao.setText("");
                edPihao.setEnabled(false);
                fBatchManager = false;
            }
            if (isGetDefaultStorage) {
                spWhichStorage.setAutoSelection(getString(R.string.spStorage_ois), product.FDefaultLoc);
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
//        getpici();
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

    private void getInstorageNum(Product product) {
        if (product == null) {
            return;
        }
        String pihao;
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
                    Log.e(TAG, "库存返回：" + cBean.returnJson);
                    double num = MathUtil.toD(cBean.returnJson);
                    tvNuminstorage.setText((num * spUnit.getDataUnitrate()) + "");
                }

                @Override
                public void onFailed(String Msg, AsyncHttpClient client) {
                    Log.e(TAG, "库存错误返回：" + Msg);
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

    private void Addorder() {
        try {
//            if (wavehouseID == null) {
//                wavehouseID = "0";
//            }
            String discount = "0";
            String num = edNum.getText().toString();
            if (supplierid==null || edSupplier.getText().toString().equals("")){
                Toast.showText(mContext, "请选择供应商");
                MediaPlayer.getInstance(mContext).error();
                return;
            }
            if (edPricesingle.getText().toString().equals("")){
                Toast.showText(mContext, "请输入单价");
                MediaPlayer.getInstance(mContext).error();
                return;
            }
            if (edNum.getText().toString().equals("")){
                Toast.showText(mContext, "请输入数量");
                MediaPlayer.getInstance(mContext).error();
                return;
            }
            if (product== null || edCode.getText().toString().equals("")){
                Toast.showText(mContext, "请选择物料编号");
                MediaPlayer.getInstance(mContext).error();
                return;
            }
            if (fBatchManager && edPihao.getText().toString().equals("")){
                Toast.showText(mContext, "请输入批次号");
                MediaPlayer.getInstance(mContext).error();
                return;
            }

                if (isHebing) {
                    List<T_Detail> detailhebing = t_detailDao.queryBuilder().where(
                            T_DetailDao.Properties.Activity.eq(activity),
                            T_DetailDao.Properties.FOrderId.eq(ordercode),
                            T_DetailDao.Properties.FProductId.eq(product.FItemID),
                            T_DetailDao.Properties.FBatch.eq(edPihao.getText().toString()),
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
                Log.e("Rem", edZhaiyao.getText().toString());
                t_main.supplier = supplierName == null ? "" : supplierName;
                t_main.supplierId = supplierid == null ? "" : supplierid;
                t_main.FSendOutId = spSignPerson.getEmployeeId();
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
                    MediaPlayer.getInstance(mContext).ok();
                    Toast.showText(mContext, "添加成功");
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
        DataModel.upload(WebApi.OTHERINSTORE, gson.toJson(pBean));
//        postToServer(data);
    }

    private void postToServer(ArrayList<PurchaseInStoreUploadBean.purchaseInStore> data) {
        PurchaseInStoreUploadBean pBean = new PurchaseInStoreUploadBean();
        pBean.list = data;
        Gson gson = new Gson();
        Asynchttp.post(mContext, getBaseUrl() + WebApi.OTHERINSTORE, gson.toJson(pBean), new Asynchttp.Response() {
            @Override
            public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                Toast.showText(mContext, "上传成功");
                MediaPlayer.getInstance(mContext).ok();
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
                Toast.showText(mContext, Msg);
                btnBackorder.setClickable(true);
                MediaPlayer.getInstance(mContext).error();
                LoadingUtil.dismiss();
            }
        });
    }

    private void resetAll() {
        edNum.setText("");
        edPricesingle.setText("");
        edPihao.setText("");
        edZhaiyao.setText("");
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

    public void finishOrder() {
        AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
        ab.setTitle("确认使用完单");
        ab.setMessage("确认？");
        ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ordercode++;
                Log.e("ordercode", ordercode + "");
                share.setOrderCode(OtherInStoreActivity.this, ordercode);
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
                share.setOrderCode(OtherInStoreActivity.this, ordercode);
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

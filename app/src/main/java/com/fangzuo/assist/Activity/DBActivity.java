package com.fangzuo.assist.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.fangzuo.assist.Dao.Product;
import com.fangzuo.assist.Dao.Storage;
import com.fangzuo.assist.Dao.T_Detail;
import com.fangzuo.assist.Dao.T_main;
import com.fangzuo.assist.Dao.Unit;
import com.fangzuo.assist.Dao.WaveHouse;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.Asynchttp;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.CommonMethod;
import com.fangzuo.assist.Utils.DataModel;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.Info;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.MediaPlayer;
import com.fangzuo.assist.Utils.ShareUtil;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.assist.widget.LoadingUtil;
import com.fangzuo.assist.widget.MyWaveHouseSpinner;
import com.fangzuo.assist.widget.SpinnerDepartMent;
import com.fangzuo.assist.widget.SpinnerPeople;
import com.fangzuo.assist.widget.SpinnerUnit;
import com.fangzuo.assist.zxing.activity.CaptureActivity;
import com.fangzuo.greendao.gen.BarCodeDao;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.InStorageNumDao;
import com.fangzuo.greendao.gen.ProductDao;
import com.fangzuo.greendao.gen.T_DetailDao;
import com.fangzuo.greendao.gen.T_mainDao;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DBActivity extends BaseActivity {

    @BindView(R.id.sp_storage_out)
    Spinner spStorageOut;
    @BindView(R.id.sp_wavehouse_out)
    MyWaveHouseSpinner spWavehouseOut;
    @BindView(R.id.sp_storage_in)
    Spinner spStorageIn;
    @BindView(R.id.sp_wavehouse_in)
    Spinner spWavehouseIn;
    @BindView(R.id.scanbyCamera)
    RelativeLayout scanbyCamera;
    @BindView(R.id.ed_code)
    EditText edCode;
    @BindView(R.id.search)
    RelativeLayout search;
    @BindView(R.id.tv_goodName)
    TextView tvGoodName;
    @BindView(R.id.tv_numoutStore)
    TextView tvNumoutStore;
    @BindView(R.id.tv_numinStore)
    TextView tvNuminStore;
    @BindView(R.id.tv_model)
    TextView tvModel;
    @BindView(R.id.ed_pihao)
    EditText edPihao;
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
    @BindView(R.id.sp_department)
    SpinnerDepartMent spDepartment;
    @BindView(R.id.sp_employee)
    SpinnerPeople spEmployee;
    @BindView(R.id.sp_sign_person)
    SpinnerPeople spSignPerson;
    @BindView(R.id.sp_capture_person)
    SpinnerPeople spCapturePerson;
    @BindView(R.id.cb_isStorage)
    CheckBox cbIsStorage;
    @BindView(R.id.ishebing)
    CheckBox ishebing;
    @BindView(R.id.isAutoAdd)
    CheckBox isAutoAdd;
    @BindView(R.id.sp_pihao)
    Spinner spPihao;
    private ShareUtil share;
    private CommonMethod method;
    private long ordercode;
    private EmployeeSpAdapter employeeAdapter;
    private StorageSpAdapter storageSpinner;
    private DaoSession daoSession;
    private int year;
    private int month;
    private int day;
    private List<Product> products;
    private boolean fBatchManager = false;
    private UnitSpAdapter unitAdapter;
//    private PiciSpAdapter piciSpAdapter;
    private String pihao;
    private String unitId;
    private String unitName;
    private double unitrate;
    private String instorageName;
    private String instorageId;
    private WaveHouseSpAdapter inwavehouseAdapter;
    private String outstorageName;
    private String outstorageId;
    private WaveHouseSpAdapter outwavehouseAdapter;
    private String inwavehouseID;
    private String inwavehouseName;
    private String outwavehouseID;
    private String outwavehouseName;
    private String date;
//    private String captureName;
//    private String captureId;
//    private String yanshouName;
//    private String yanshouId;
    private DepartmentSpAdapter departMentAdapter;
    private boolean isHebing = true;
//    private String departmentId;
//    private String departmentName;
    public static final int DB = 12385;
//    private String employeename;
//    private String employeeId;
    private T_mainDao t_mainDao;
    private T_DetailDao t_detailDao;
    private boolean isGetDefaultStorage;
    private ProductselectAdapter1 productselectAdapter1;
    private Product product;
    private ProductselectAdapter productselectAdapter;
    private DecimalFormat df;
    private boolean isAuto;
    private String default_unitID;
    private double qty;
    private Storage storageIn;
    private Storage storageOut;
    private PiciSpAdapter piciSpAdapter;
    private boolean checkStorage=false;  // 0不允许负库存false 1允许负库存出库true
    private String wavehouseAutoString="";

    @Override
    protected void initView() {
        setContentView(R.layout.activity_db);
        mContext = this;
        ButterKnife.bind(this);
        edPihao.setEnabled(false);
        ishebing.setChecked(isHebing);
        df = new DecimalFormat("######0.00");
        daoSession = GreenDaoManager.getmInstance(mContext).getDaoSession();
        t_detailDao = daoSession.getT_DetailDao();
        t_mainDao = daoSession.getT_mainDao();
        year = Calendar.getInstance().get(Calendar.YEAR);
        month = Calendar.getInstance().get(Calendar.MONTH);
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        share = ShareUtil.getInstance(mContext);
        isAuto = share.getDBisAuto();
        isGetDefaultStorage = share.getBoolean(Info.Storage+DB);
        cbIsStorage.setChecked(isGetDefaultStorage);
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(ClassEvent event) {
        switch (event.Msg){
            case EventBusInfoCode.PRODUCTRETURN:
                product = (Product)event.postEvent;
                setDATA("", true);
                break;
        }
    }

    @Override
    protected void initData() {
        isAutoAdd.setChecked(share.getDBisAuto());
        tvDate.setText(getTime(true));
        method = CommonMethod.getMethod(mContext);
        if (share.getDBOrderCode() == 0) {
            ordercode = Long.parseLong(getTime(false) + "001");
            Log.e("ordercode", ordercode + "");
            share.setDBOrderCode(ordercode);
        } else {
            ordercode = share.getDBOrderCode();
            Log.e("ordercode", ordercode + "");
        }
        loadBasicData();
    }


    @Override
    protected void initListener() {
        ishebing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isHebing = b;
            }
        });
        isAutoAdd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isAuto = b;
                share.setDBisAuto(b);
            }
        });
        cbIsStorage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isGetDefaultStorage = b;
                share.setBooleam(Info.Storage+DB,b);
            }
        });

//        edPihao.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                if (i == 0 && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
//                        getInstorageNum(product);
//                        getOutstorageNum(product);
//
//                }
//                return true;
//            }
//        });
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
        spStorageIn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                storageIn = (Storage) storageSpinner.getItem(i);

                instorageName = storageIn.FName;
                instorageId = storageIn.FItemID;
                inwavehouseID = "0";
                Log.e("仓库入", storageIn.toString());
                inwavehouseAdapter = method.getWaveHouseAdapter(storageIn, spWavehouseIn);

                getInstorageNum(product);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spStorageOut.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                storageOut = (Storage) storageSpinner.getItem(i);
                Log.e("仓库出", storageOut.toString());
                if ("1".equals(storageOut.FUnderStock)){
                    checkStorage=true;
                }else{
                    checkStorage=false;
                }
                outstorageName = storageOut.FName;
                outstorageId = storageOut.FItemID;
                outwavehouseID = "0";
                getPici();

                getOutstorageNum(product);


//                outwavehouseAdapter = method.getWaveHouseAdapter(storageOut, spWavehouseOut);
                spWavehouseOut.setAuto(mContext,storageOut,wavehouseAutoString);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spWavehouseIn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                WaveHouse waveHouse = (WaveHouse) inwavehouseAdapter.getItem(i);
                inwavehouseID = waveHouse.FSPID;
                inwavehouseName = waveHouse.FName;
                Log.e("wavehouseName", inwavehouseName);

                getInstorageNum(product);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spWavehouseOut.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                WaveHouse waveHouse = (WaveHouse) spWavehouseOut.getAdapter().getItem(i);
                outwavehouseID = waveHouse.FSPID;
                outwavehouseName = waveHouse.FName;
                Log.e("wavehouseName", outwavehouseName);

                getOutstorageNum(product);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Unit unit = (Unit) spUnit.getAdapter().getItem(i);
                if (unit != null) {
                    unitId = unit.FMeasureUnitID;
                    unitName = unit.FName;
                    unitrate = Double.parseDouble(unit.FCoefficient);
                    Log.e("1111", unitrate + "");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        spSignPerson.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Employee employee = (Employee) employeeAdapter.getItem(i);
//                yanshouName = employee.FName;
//                yanshouId = employee.FItemID;
////                share.setDBSignPerson(i);
//                if (isFirst5){
//                    share.setDBSignPerson(i);
//                    spSignPerson.setSelection(i);
//                }
//                else{
//                    spSignPerson.setSelection(share.getDBSignPerson());
//                    isFirst5=true;
//                }
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
////                share.setDBDepartment(i);
//                if (isFirst){
//                    share.setDBDepartment(i);
//                    spDepartment.setSelection(i);
//                }
//                else{
//                    spDepartment.setSelection(share.getDBDepartment());
//                    isFirst=true;
//                }
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
//                Employee employee = (Employee) employeeAdapter.getItem(i);
//                captureName = employee.FName;
//                captureId = employee.FItemID;
////                share.setDBcapturePerson(i);
//                if (isFirst2){
//                    share.setDBcapturePerson(i);
//                    spCapturePerson.setSelection(i);
//                }
//                else{
//                    spCapturePerson.setSelection(share.getDBcapturePerson());
//                    isFirst2=true;
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
//                employeename = employee.FName;
//                employeeId = employee.FItemID;
////                share.setDBEmployee(i);
//                if (isFirst3){
//                    share.setDBEmployee(i);
//                    spEmployee.setSelection(i);
//                }
//                else{
//                    spEmployee.setSelection(share.getDBEmployee());
//                    isFirst3=true;
//                }
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
//        if (edNum.getText().toString().equals("")){
//            setfocus(edNum);
//            return;
//        }
//        if (isAuto){
//            Addorder();
//        }else{
//            edCode.setText(code);
//            setDATA(code, false);
//        }
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


    private void loadBasicData() {
        storageSpinner = method.getStorageSpinner(spStorageOut);
        method.getStorageSpinner(spStorageIn);
        spCapturePerson.setAutoSelection(getString(R.string.spCapturePerson_db),"");
        spEmployee.setAutoSelection(getString(R.string.spEmployee_db),"");
        spSignPerson.setAutoSelection(getString(R.string.spSignPerson_db),"");
        spDepartment.setAutoSelection(getString(R.string.spDepartment_db),"");

//        employeeAdapter = method.getEmployeeAdapter(spCapturePerson);
//        method.getEmployeeAdapter(spEmployee);
//        method.getEmployeeAdapter(spSignPerson);
//        departMentAdapter = method.getDepartMentAdapter(spDepartment);

//        spSignPerson.setSelection(share.getDBSignPerson());
//        spDepartment.setSelection(share.getDBDepartment());
//        spEmployee.setSelection(share.getDBEmployee());

    }

    @OnClick({R.id.scanbyCamera, R.id.search, R.id.btn_add, R.id.btn_finishorder, R.id.btn_backorder, R.id.btn_checkorder, R.id.tv_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.scanbyCamera:
                Intent in = new Intent(mContext, CaptureActivity.class);
                startActivityForResult(in, 0);
                break;
            case R.id.search:
                Log.e("search", "onclick");
                Bundle b = new Bundle();
                b.putString("search", edCode.getText().toString());
                b.putInt("where", Info.SEARCHPRODUCT);
                startNewActivityForResult(ProductSearchActivity.class, R.anim.activity_open, 0, Info.SEARCHFORRESULT, b);
                break;
            case R.id.btn_add:
                Addorder();
                break;
            case R.id.btn_finishorder:
                finishOrder();
                break;
            case R.id.btn_backorder:
                if (DataModel.checkHasDetail(mContext,DB)){
                    btnBackorder.setClickable(false);
                    LoadingUtil.show(mContext,"正在回单...");
                    upload();
                }else{
                    Toast.showText(mContext,"无单据信息");
                }
                break;
            case R.id.btn_checkorder:
                Bundle b1 = new Bundle();
                b1.putInt("activity", DB);
                startNewActivity(TableActivity.class, R.anim.activity_fade_in, R.anim.activity_fade_out, false, b1);
                break;
            case R.id.tv_date:
                getdate();
                break;
        }


    }

    private void getdate() {
        final DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            }
        }, year, month, day);

        datePickerDialog.setButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int year = datePickerDialog.getDatePicker().getYear();
                int month = datePickerDialog.getDatePicker().getMonth();
                int day = datePickerDialog.getDatePicker().getDayOfMonth();
                date = year + "-" + ((month < 10) ? "0" + (month + 1) : (month + 1)) + "-" + ((day < 10) ? "0" + day : day);
                tvDate.setText(date);
                Toast.showText(mContext, date);
                datePickerDialog.dismiss();

            }
        });
        datePickerDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("code", requestCode + "" + "    " + resultCode);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Bundle b = data.getExtras();
                String message = b.getString("result");
                edCode.setText(message);
                Toast.showText(mContext, message);
                edCode.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
            }
        }
    }

    private void setDATA(String fnumber, boolean flag) {
        default_unitID = null;
        edPihao.setText("");
        getPici();
        if (flag) {
            default_unitID = product.FUnitID;
            tvorisAuto(product);
        } else {
            BarCodeDao barCodeDao = daoSession.getBarCodeDao();
            final ProductDao productDao = daoSession.getProductDao();
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
                                products = productDao.queryBuilder().where(ProductDao.Properties.FItemID.eq(barCode.FItemID)).build().list();
                                default_unitID = barCode.FUnitID;
                                getProductOFL(products);
                                alertDialog.dismiss();
                            }
                        });
                    }
                }else{
                    MediaPlayer.getInstance(mContext).error();
                    Toast.showText(mContext,"未找到条码" );
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


    private void getProductOFL(List<Product> list){
        if (list != null && list.size() > 0) {
            product = list.get(0);
            Lg.e("物料offline："+product.toString());
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
        Lg.e("物料online："+product.toString());
        tvorisAuto(product);
    }

    private void tvorisAuto(final Product product) {
        Lg.e("物料："+product.toString());

        edCode.setText(product.FNumber);
        tvModel.setText(product.FModel);
        tvGoodName.setText(product.FName);
        wavehouseAutoString=product.FSPID;
        if ((product.FBatchManager) != null && (product.FBatchManager).equals("1")) {
            fBatchManager = true;
            setfocus(edPihao);
            edPihao.setEnabled(true);
        } else {
            edPihao.setEnabled(false);
            fBatchManager = false;
        }
        if (isGetDefaultStorage) {
            for (int j = 0; j < storageSpinner.getCount(); j++) {
                if (((Storage) storageSpinner.getItem(j)).FItemID.equals(product.FDefaultLoc)) {
                    spStorageOut.setSelection(j);
                    break;
                }
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    spWavehouseOut.setAuto(mContext,storageOut,wavehouseAutoString);
//                    for (int j = 0; j < waveHouseAdapter.getCount(); j++) {
//                        if (((WaveHouse) waveHouseAdapter.getItem(j)).FSPID.equals(product.FSPID)) {
//                            spWavehouse.setSelection(j);
//                            break;
//                        }
//                    }
                }
            }, 50);
        }
        spUnit.setAuto(mContext,product.FUnitGroupID,default_unitID,SpinnerUnit.Id);
//        unitAdapter = CommonMethod.getMethod(mContext).getUnitAdapter(product.FUnitGroupID, spUnit);
//        chooseUnit(default_unitID);
//        if (default_unitID != null) {
//            for (int i = 0; i < unitAdapter.getCount(); i++) {
//                if (default_unitID.equals(((Unit) unitAdapter.getItem(i)).FMeasureUnitID)) {
//                    spUnit.setSelection(i);
//                }
//            }
//        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getOutstorageNum(product);
                getInstorageNum(product);
            }
        }, 100);
        if (isAuto) {
            edNum.setText("1.0");
        }

        if ((isAuto && !fBatchManager) || (isAuto && fBatchManager && !edPihao.getText().toString().equals(""))) {
//        if ((isAuto && !fBatchManager) || (isAuto && fBatchManager)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Addorder();
                }
            }, 150);
        } else {
            edNum.setText("1.0");
            setfocus(edPihao);
        }

    }


    private void getInstorageNum(Product product) {
        if (product != null) {
            if (fBatchManager) {
//                pihao = edPihao.getText().toString();
                if (pihao.equals("")) {
                    pihao = "";
                }
            } else {
                pihao = "";
            }
            if (inwavehouseID == null) {
                inwavehouseID = "0";
            }
            if (BasicShareUtil.getInstance(mContext).getIsOL()) {
                InStoreNumBean iBean = new InStoreNumBean();
                iBean.FStockPlaceID = inwavehouseID;
                iBean.FBatchNo = pihao;
                iBean.FStockID = instorageId;
                iBean.FItemID = product.FItemID;
                String json = new Gson().toJson(iBean);
                Log.e("inStorenum", json);
                Asynchttp.post(mContext, getBaseUrl() + WebApi.GETINSTORENUM, json, new Asynchttp.Response() {
                    @Override
                    public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                        double inQty = Double.parseDouble(cBean.returnJson);
                        tvNuminStore.setText((inQty / unitrate) + "");
                        Log.e("inQty", inQty + "   " + unitrate);
                    }

                    @Override
                    public void onFailed(String Msg, AsyncHttpClient client) {
                        tvNuminStore.setText("0");
                    }
                });
            } else {
                InStorageNumDao inStorageNumDao = daoSession.getInStorageNumDao();
                List<InStorageNum> list1 = inStorageNumDao.queryBuilder().where(
                        InStorageNumDao.Properties.FItemID.eq(product.FItemID),
                        InStorageNumDao.Properties.FStockID.eq(instorageId),
                        InStorageNumDao.Properties.FStockPlaceID.eq(inwavehouseID),
                        InStorageNumDao.Properties.FBatchNo.eq(pihao)
                ).build().list();
                if (list1.size() > 0) {
                    Log.e("FQty", list1.get(0).FQty);
                    Double inQty = Double.parseDouble(list1.get(0).FQty);
                    Log.e("qty", qty + "");
                    if (inQty > 0) {
                        tvNuminStore.setText((inQty / unitrate) + "");
                    } else {
                        tvNuminStore.setText((0.0) + "");
                    }

                } else {
                    tvNuminStore.setText("0");
                }

            }
        }

    }

    private void getOutstorageNum(Product product) {
        if (product == null) {
            return;
        }
            if (fBatchManager) {
//                pihao = edPihao.getText().toString();
                if (pihao==null||pihao.equals("")) {
                    pihao = "";
                }
            } else {
                pihao = "";
            }
            if (outwavehouseID == null) {
                outwavehouseID = "0";
            }
            if (BasicShareUtil.getInstance(mContext).getIsOL()) {
                InStoreNumBean iBean = new InStoreNumBean();
                iBean.FStockPlaceID = outwavehouseID;
                iBean.FBatchNo = pihao;
                iBean.FStockID = outstorageId;
                iBean.FItemID = product.FItemID;
                String json = new Gson().toJson(iBean);
                Log.e("inStorenum", json);
                Asynchttp.post(mContext, getBaseUrl() + WebApi.GETINSTORENUM, json, new Asynchttp.Response() {
                    @Override
                    public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                        qty = Double.parseDouble(cBean.returnJson);
                        Log.e("QTY", qty + "   " + unitrate);
//                        tvNumoutStore.setText((qty / unitrate) + "");
                        tvNumoutStore.setText(dealStoreNumForOut((qty / unitrate) + ""));
                        qty = Double.parseDouble(dealStoreNumForOut(qty+""));

                    }

                    @Override
                    public void onFailed(String Msg, AsyncHttpClient client) {
                        tvNumoutStore.setText("0");
                    }
                });
            } else {
                InStorageNumDao inStorageNumDao = daoSession.getInStorageNumDao();
                List<InStorageNum> list1 = inStorageNumDao.queryBuilder().where(
                        InStorageNumDao.Properties.FItemID.eq(product.FItemID),
                        InStorageNumDao.Properties.FStockID.eq(outstorageId),
                                InStorageNumDao.Properties.FStockPlaceID.eq(outwavehouseID),
                        InStorageNumDao.Properties.FBatchNo.eq(pihao)
                ).build().list();
                if (list1.size() > 0) {
                    Log.e("FQty", list1.get(0).FQty);
                    qty = Double.parseDouble(list1.get(0).FQty);
                    Log.e("qty", qty + "");
                    if (qty > 0) {
                        tvNumoutStore.setText((qty / unitrate) + "");
                    } else {
                        tvNuminStore.setText((0.0) + "");
                    }

                } else {
                    tvNumoutStore.setText("0");
                }

            }

    }
    //处理网络库存与已添加的本地库存数量问题
    private String dealStoreNumForOut(String num) {
        List<T_Detail> list1 = t_detailDao.queryBuilder().where(
                T_DetailDao.Properties.FProductId.eq(product.FItemID),
                T_DetailDao.Properties.FoutStorageid.eq(outstorageId)
        ).build().list();
        List<T_Detail> list=new ArrayList<>();
        list.addAll(list1);
        if (!"".equals(pihao)){
            for (T_Detail bean:list) {
                if (!pihao.equals(bean.FBatch)){
                    list1.remove(bean);
                }
            }
        }
        if (!"".equals(outwavehouseID)){
            for (T_Detail bean:list) {
                if (!outwavehouseID.equals(bean.Foutwavehouseid)){
                    list1.remove(bean);
                }
            }
        }
        if (list1.size() > 0) {
            Log.e("本地：FQty", list1.get(0).FQuantity);
            double qty = Double.parseDouble(list1.get(0).FQuantity);
            return Double.parseDouble(num) - qty + "";
        } else {
            return num;
        }
    }

    //获取批次,根据调出仓库
    private void getPici() {
        List<InStorageNum> container1 = new ArrayList<>();
        piciSpAdapter = new PiciSpAdapter(mContext, container1);
        spPihao.setAdapter(piciSpAdapter);
        if (product==null){
            return;
        }
        if (fBatchManager) {
            spPihao.setEnabled(true);
            if (!BasicShareUtil.getInstance(mContext).getIsOL()) {
                piciSpAdapter = CommonMethod.getMethod(mContext).getPici(storageOut, outwavehouseID, product, spPihao);
            } else {
                final List<InStorageNum> container = new ArrayList<>();
                GetBatchNoBean bean = new GetBatchNoBean();
                bean.ProductID=product.FItemID;
                bean.StorageID=outstorageId;
                bean.WaveHouseID=outwavehouseID;
                String json = new Gson().toJson(bean);
                Log.e(TAG, "getPici批次提交："+json);
                Asynchttp.post(mContext, getBaseUrl() + WebApi.GETPICI, json, new Asynchttp.Response() {
                    @Override
                    public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                        Log.e(TAG,"getPici获取数据："+cBean.returnJson);
                        DownloadReturnBean dBean = new Gson().fromJson(cBean.returnJson, DownloadReturnBean.class);
                        if(dBean.InstorageNum!=null){
                            for (int i = 0; i < dBean.InstorageNum.size(); i++) {
                                if (dBean.InstorageNum.get(i).FQty != null
                                        && Double.parseDouble(dBean.InstorageNum.get(i).FQty) > 0) {
                                    Log.e(TAG,"有库存的批次："+dBean.InstorageNum.get(i).toString());
                                    container.add(dBean.InstorageNum.get(i));
                                }
                            }
                            piciSpAdapter = new PiciSpAdapter(mContext, container);
                            spPihao.setAdapter(piciSpAdapter);
                        }
                        piciSpAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailed(String Msg, AsyncHttpClient client) {
                        Log.e(TAG,"getPici获取数据错误："+Msg);
                        Toast.showText(mContext, Msg);
                    }
                });
            }
        } else {
            spPihao.setEnabled(false);
        }
    }

    private void Addorder() {

        String discount = "0";
        if (inwavehouseID == null) {
            inwavehouseID = "0";
        }
        if (outwavehouseID == null) {
            outwavehouseID = "0";
        }
        String num = edNum.getText().toString();
        String num1 = edNum.getText().toString();
        if ("".equals(edCode.getText().toString())){
            Toast.showText(mContext, "请输入物料编号");
            MediaPlayer.getInstance(mContext).error();
            return;
        }
        if ("".equals(edNum.getText().toString())){
            Toast.showText(mContext, "请输入数量");
            MediaPlayer.getInstance(mContext).error();
            return;
        }
        if (fBatchManager && pihao.equals("")) {
            Toast.showText(mContext, "请输入批次号");
            MediaPlayer.getInstance(mContext).error();
            return;
        }

        if (outstorageId.equals(instorageId)) {
            Toast.showText(mContext, "大兄弟，你太闲了，搬进去搬出来不累么");
            MediaPlayer.getInstance(mContext).error();
            return;
        }

        //是否开启库存管理 true，开启允许负库存
        if (!checkStorage) {
//            if (qty < Double.parseDouble(num)) {
            if (Double.parseDouble(tvNumoutStore.getText().toString()) < Double.parseDouble(num)) {
                MediaPlayer.getInstance(mContext).error();
                Toast.showText(mContext, "大兄弟 库存不够了");
                return;
            }
        }
                if (isHebing) {
                    List<T_Detail> detailhebing = t_detailDao.queryBuilder().where(
                            T_DetailDao.Properties.Activity.eq(DB),
                            T_DetailDao.Properties.FOrderId.eq(ordercode),
                            T_DetailDao.Properties.FProductId.eq(product.FItemID),
//                            T_DetailDao.Properties.FBatch.eq(edPihao.getText().toString()),
                            T_DetailDao.Properties.FBatch.eq(pihao),
                            T_DetailDao.Properties.FUnitId.eq(unitId),
                            T_DetailDao.Properties.FStorageId.eq(instorageId),
                            T_DetailDao.Properties.FPositionId.eq(inwavehouseID),
                            T_DetailDao.Properties.FDiscount.eq(discount),
                            T_DetailDao.Properties.FoutStorageid.eq(outstorageId),
                            T_DetailDao.Properties.Foutwavehouseid.eq(outwavehouseID)
                    ).build().list();
                    if (detailhebing.size() > 0) {
                        for (int i = 0; i < detailhebing.size(); i++) {
                            num = (Double.parseDouble(num) + Double.parseDouble(detailhebing.get(i).FQuantity)) + "";
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
                t_main.orderDate = share.getPROISdate();
                t_main.FPurchaseUnit = unitName == null ? "" : unitName;
                t_main.FMaker = share.getUserName();
                t_main.FMakerId = share.getsetUserID();
                t_main.FDirector = spSignPerson.getEmployeeName();
                t_main.FDirectorId = spSignPerson.getEmployeeId();
                t_main.saleWay = "";
                t_main.FDeliveryAddress = "";
                t_main.FRemark = "";
                t_main.saleWayId = "";
                t_main.FCustody =spCapturePerson.getEmployeeName();
                t_main.FCustodyId = spCapturePerson.getEmployeeId();
                t_main.FAcount = spEmployee.getEmployeeName();
                t_main.FAcountID = spEmployee.getEmployeeId();
                t_main.FSalesMan = spEmployee.getEmployeeName();
                t_main.FSalesManId = spEmployee.getEmployeeId();
                t_main.Rem = "";
                t_main.FRedBlue = "0";
                t_main.supplier = "";
                t_main.supplierId = "";
                t_main.FSendOutId = "";
                t_main.sourceOrderTypeId = "";
                t_main.activity = DB;


                T_Detail t_detail = new T_Detail();
//                t_detail.FBatch = edPihao.getText().toString();
                t_detail.FBatch = pihao;
                t_detail.FOrderId = ordercode;
                t_detail.FProductCode = edCode.getText().toString();
                t_detail.FProductId = product.FItemID;
                t_detail.model = product.FModel;
                t_detail.FProductName = product.FName;
                t_detail.FIndex = second;
                t_detail.FUnitId = unitId == null ? "0" : unitId;
                t_detail.FUnit = unitName == null ? "" : unitName;
                t_detail.FStorage = instorageName == null ? "" : instorageName;
                t_detail.FStorageId = instorageId == null ? "0" : instorageId;
                t_detail.FPosition = inwavehouseName == null ? "" : inwavehouseName;
                t_detail.FPositionId = inwavehouseID == null ? "0" : inwavehouseID;
                t_detail.FoutStorageid = outstorageId == null ? "0" : outstorageId;
                t_detail.FoutStoragename = outstorageName == null ? "" : outstorageName;
                t_detail.Foutwavehouseid = outwavehouseID == null ? "0" : outwavehouseID;
                t_detail.Foutwavehousename = outwavehouseName == null ? "" : outwavehouseName;
                t_detail.activity = DB;
                t_detail.FDiscount = discount;
                t_detail.FQuantity = num;
                t_detail.unitrate = unitrate;
                t_detail.FTaxUnitPrice = "";

                if (!BasicShareUtil.getInstance(mContext).getIsOL()) {
                    long insert1 = t_mainDao.insert(t_main);
                    long insert = t_detailDao.insert(t_detail);
                    if (insert1 > 0 && insert > 0) {
                        MediaPlayer.getInstance(mContext).ok();
                        Toast.showText(mContext, "添加成功");
                        InStorageNumDao inStorageNumDao = daoSession.getInStorageNumDao();
                        List<InStorageNum> outnum = inStorageNumDao.queryBuilder().where(
//                                InStorageNumDao.Properties.FBatchNo.eq(edPihao.getText().toString()),
                                InStorageNumDao.Properties.FBatchNo.eq(pihao),
                                InStorageNumDao.Properties.FStockID.eq(outstorageId),
                                InStorageNumDao.Properties.FStockPlaceID.eq(outwavehouseID == null ? "0" : outwavehouseID),
                                InStorageNumDao.Properties.FItemID.eq(product.FItemID)).build().list();
                        if (outnum.size() > 0) {
                            if (checkStorage){
                                outnum.get(0).FQty = String.valueOf(((Double.parseDouble(outnum.get(0).FQty) + (Double.parseDouble("-"+edNum.getText().toString())))));
                            }else{
                                Log.e("innum", outnum.get(0).FQty);
                                Log.e("innum", outnum.size() + "");
                                outnum.get(0).FQty = (Double.parseDouble(outnum.get(0).FQty) - Double.parseDouble(edNum.getText().toString())) + "";

//                                outnum.get(0).FQty = (Double.parseDouble(outnum.get(0).FQty) + (Double.parseDouble(edNum.getText().toString()) / unitrate)) + "";
                            }
                            inStorageNumDao.update(outnum.get(0));

                        } else {
                                InStorageNum inStorageNum = new InStorageNum();
                                inStorageNum.FItemID = product.FItemID;
                                inStorageNum.FBatchNo = edPihao.getText().toString();
                                inStorageNum.FStockPlaceID = outwavehouseID == null ? "0" : outwavehouseID;
                                inStorageNum.FStockID = outstorageId == null ? "0" : outstorageId;
                                inStorageNum.FQty = (Double.parseDouble("-"+edNum.getText().toString()) * unitrate)+"";
                                inStorageNumDao.insert(inStorageNum);
                        }
//                        outnum.get(0).FQty = (Double.parseDouble(outnum.get(0).FQty) - Double.parseDouble(edNum.getText().toString())) + "";

                        inStorageNumDao.update(outnum.get(0));
                        List<InStorageNum> innum = inStorageNumDao.queryBuilder().where(
//                                InStorageNumDao.Properties.FBatchNo.eq(edPihao.getText().toString()),
                                InStorageNumDao.Properties.FBatchNo.eq(pihao),
                                InStorageNumDao.Properties.FStockID.eq(instorageId == null ? "0" : instorageId),
                                InStorageNumDao.Properties.FStockPlaceID.eq(inwavehouseID == null ? "0" : inwavehouseID),
                                InStorageNumDao.Properties.FItemID.eq(product.FItemID)
                        ).build().list();
                        if (innum.size() > 0) {
                            Log.e("innum", innum.get(0).FQty);
                            Log.e("innum", innum.size() + "");
                            innum.get(0).FQty = (Double.parseDouble(innum.get(0).FQty) + (Double.parseDouble(edNum.getText().toString()) / unitrate)) + "";
                            inStorageNumDao.update(innum.get(0));

                        } else {
                            Log.e("InStorageNum", num1);
                            InStorageNum i = new InStorageNum();
                            i.FQty = edNum.getText().toString();
                            i.FItemID = product.FItemID;
//                            i.FBatchNo = edPihao.getText().toString();
                            i.FBatchNo = pihao;
                            i.FStockID = instorageId;
                            i.FStockPlaceID = inwavehouseID == null ? "0" : inwavehouseID;
                            inStorageNumDao.insert(i);
                        }
                        resetAll();
                    } else {
                        Toast.showText(mContext, "添加失败，请重试");
                        MediaPlayer.getInstance(mContext).error();
                    }

                } else {
                    MediaPlayer.getInstance(mContext).ok();
                    long insert1 = t_mainDao.insert(t_main);
                    long insert = t_detailDao.insert(t_detail);
                    Toast.showText(mContext, "添加成功");
                    resetAll();
                }

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
                share.setPISOrderCode(ordercode);
                finish();
            }
        });
        ab.setNegativeButton("取消", null);
        ab.create().show();
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
                share.setPROISOrderCode(ordercode);
            }
        });
        ab.setNegativeButton("取消", null);
        ab.create().show();

    }

    private void upload() {
        PurchaseInStoreUploadBean pBean = new PurchaseInStoreUploadBean();
        PurchaseInStoreUploadBean.purchaseInStore puBean = pBean.new purchaseInStore();
        t_mainDao = daoSession.getT_mainDao();
        t_detailDao = daoSession.getT_DetailDao();
        ArrayList<String> detailContainer = new ArrayList<>();
        ArrayList<PurchaseInStoreUploadBean.purchaseInStore> data = new ArrayList<>();
        List<T_main> mains = t_mainDao.queryBuilder().where(T_mainDao.Properties.Activity.eq(DB)).build().list();
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
                        t_main.FCustodyId + "|" +
                        t_main.FDirectorId + "|";
                puBean.main = main;
                List<T_Detail> details = t_detailDao.queryBuilder().where(
                        T_DetailDao.Properties.FOrderId.eq(t_main.orderId),
                        T_DetailDao.Properties.Activity.eq(DB)
                ).build().list();
                for (int j = 0; j < details.size(); j++) {
                    if (j != 0 && j % 49 == 0) {
                        Log.e("j%49", j % 49 + "");
                        T_Detail t_detail = details.get(j);
                        detail = detail +
                                t_detail.FProductId + "|" +
                                t_detail.FUnitId + "|" +
                                t_detail.FQuantity + "|" +
                                t_detail.FStorageId + "|" +
                                t_detail.FoutStorageid + "|" +
                                t_detail.FPositionId + "|" +
                                t_detail.Foutwavehouseid + "|" +
                                t_detail.FBatch + "|"
                        ;
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
                                t_detail.FQuantity + "|" +
                                t_detail.FStorageId + "|" +
                                t_detail.FoutStorageid + "|" +
                                t_detail.FPositionId + "|" +
                                t_detail.Foutwavehouseid + "|" +
                                t_detail.FBatch + "|";
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
        postToServer(data);
    }

    private void postToServer(ArrayList<PurchaseInStoreUploadBean.purchaseInStore> data) {
        PurchaseInStoreUploadBean pBean = new PurchaseInStoreUploadBean();
        pBean.list = data;
        Gson gson = new Gson();
        Asynchttp.post(mContext, getBaseUrl() + WebApi.DBUPLOAD, gson.toJson(pBean), new Asynchttp.Response() {
            @Override
            public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                Toast.showText(mContext, "上传成功");
                MediaPlayer.getInstance(mContext).ok();
                List<T_Detail> list = t_detailDao.queryBuilder().where(
                        T_DetailDao.Properties.Activity.eq(DB)
                ).build().list();
                List<T_main> list1 = t_mainDao.queryBuilder().where(
                        T_mainDao.Properties.Activity.eq(DB)
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
                Toast.showText(mContext, Msg);
                btnBackorder.setClickable(true);
                MediaPlayer.getInstance(mContext).ok();
                LoadingUtil.dismiss();
            }
        });
    }

    private void resetAll() {
        qty=0.0;
        edNum.setText("");
        edPihao.setText("");
        edCode.setText("");
        tvNumoutStore.setText("");
        tvNuminStore.setText("");
        tvGoodName.setText("");
        tvModel.setText("");
        List<InStorageNum> container = new ArrayList<>();
        piciSpAdapter = new PiciSpAdapter(mContext,container);
        spPihao.setAdapter(piciSpAdapter);
        setfocus(edCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
    //用于adpater首次更新时，不存入默认值，而是选中之前的选项
    private boolean isFirst=false;
    private boolean isFirst2=false;
    private boolean isFirst3=false;
    private boolean isFirst4=false;
    private boolean isFirst5=false;
    private boolean isFirst6=false;
    private boolean isFirst7=false;
}


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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Adapter.EmployeeSpAdapter;
import com.fangzuo.assist.Adapter.GetGoodsDepartmentSpAdapter;
import com.fangzuo.assist.Adapter.ProductselectAdapter;
import com.fangzuo.assist.Adapter.ProductselectAdapter1;
import com.fangzuo.assist.Adapter.StorageSpAdapter;
import com.fangzuo.assist.Adapter.UnitSpAdapter;
import com.fangzuo.assist.Adapter.WaveHouseSpAdapter;
import com.fangzuo.assist.Adapter.YuandanSpAdapter;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.Beans.InStoreNumBean;
import com.fangzuo.assist.Beans.PurchaseInStoreUploadBean;
import com.fangzuo.assist.Dao.BarCode;
import com.fangzuo.assist.Dao.Employee;
import com.fangzuo.assist.Dao.InStorageNum;
import com.fangzuo.assist.Dao.Product;
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
import com.fangzuo.assist.widget.SpinnerPeople;
import com.fangzuo.assist.widget.SpinnerStorage;
import com.fangzuo.assist.widget.SpinnerUnit;
import com.fangzuo.assist.widget.SpinnerYuanDan;
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

//产品入库
public class ProductInStorageActivity extends BaseActivity {

    @BindView(R.id.mDrawer)
    DrawerLayout mDrawer;
    @BindView(R.id.ishebing)
    CheckBox cbHebing;
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
    @BindView(R.id.ed_pihao)
    EditText edPihao;
    @BindView(R.id.sp_wavehouse)
    MyWaveHouseSpinner spWavehouse;
    @BindView(R.id.tv_numinstorage)
    TextView tvNuminstorage;
    @BindView(R.id.ed_num)
    EditText edNum;
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
    @BindView(R.id.sp_yuandan)
    SpinnerYuanDan spYuandan;
    @BindView(R.id.sp_yanshou)
    SpinnerPeople spYanshou;
    @BindView(R.id.sp_capture)
    SpinnerPeople spCapture;
    @BindView(R.id.isAutoAdd)
    CheckBox autoAdd;
    @BindView(R.id.sp_which_storage)
    SpinnerStorage spWhichStorage;
    @BindView(R.id.cb_isStorage)
    CheckBox cbIsStorage;
    @BindView(R.id.edghunit)
    EditText edghunit;
    @BindView(R.id.search_ghunit)
    ImageView searchGhunit;
    private ProductInStorageActivity mContext;
    private DecimalFormat df;
//    private DaoSession daoSession;
    private long ordercode;
    private GetGoodsDepartmentSpAdapter goodsDepartmentSpAdapter;
//    private StorageSpAdapter storageAdapter;
    private YuandanSpAdapter yuandanSpAdapter;
    private CommonMethod method;
    private EmployeeSpAdapter yanshouAdapter;
    //    private String captureName;
//    private String captureId;
    private List<Product> products;
    private boolean isGetDefaultStorage;
    private boolean fBatchManager;
    private UnitSpAdapter unitAdapter;
    private String storageName;
    private Storage storage;
    private String storageId;
    private WaveHouseSpAdapter wavehouseAdapter;
    private String wavehouseID = "0";
    private String wavehouseName;
//    private String unitId;
//    private String unitName;
//    private double unitrate;
    //    private String yanshouName;
//    private String yanshouId;
    private String jiaohuoName;
    private String jiaohuoId;
    boolean isHebing = true;
    private String dateAdd;
    //    private String yuandanId;
    private boolean isAuto;
    private Product product;
    private ProductselectAdapter productselectAdapter;
    private ProductselectAdapter1 productselectAdapter1;
    private String default_unitID;
    private String wavehouseAutoString = "";
    private int activity = Config.ProductInStorageActivity;

    @Override
    public void initView() {
        setContentView(R.layout.activity_product_in_storage);
        mContext = this;
        ButterKnife.bind(this);
        share = ShareUtil.getInstance(mContext);
        df = new DecimalFormat("######0.00");
        initDrawer(mDrawer);
        edPihao.setEnabled(false);
        cbHebing.setChecked(isHebing);
        autoAdd.setChecked(share.getPISisAuto());
        isGetDefaultStorage = share.getBoolean(Info.Storage + activity);
        cbIsStorage.setChecked(isGetDefaultStorage);
        isAuto = share.getPISisAuto();
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
//        if (share.getPROISOrderCode() == 0) {
//            ordercode = Long.parseLong(getTime(false) + "001");
//            Log.e("ordercode", ordercode + "");
//            share.setPROISOrderCode(ordercode);
//        } else {
//            ordercode = share.getPROISOrderCode();
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
        searchGhunit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("search", "onclick");
                Bundle b = new Bundle();
                b.putString("search", edghunit.getText().toString());
                b.putInt("where", Info.SEARCHJH);
                startNewActivityForResult(ProductSearchActivity.class, R.anim.activity_open, 0, Info.SEARCHJH, b);
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
                share.setPISisAuto(b);
            }
        });


        edPihao.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == 0 && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    getInstorageNum();
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


//        spYanshou.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Employee employee = (Employee) yanshouAdapter.getItem(i);
//                yanshouName = employee.FName;
//                yanshouId = employee.FItemID;
////                share.setPROISyanshou(i);
//                if (isFirst){
//                    share.setPROISyanshou(i);
//                    spYanshou.setSelection(i);
//                }
//                else{
//                    spYanshou.setSelection(share.getPROISyanshou());
//                    isFirst=true;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//        spCapture.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Employee employee = (Employee) yanshouAdapter.getItem(i);
//                captureName = employee.FName;
//                captureId = employee.FItemID;
////                share.setPROISCapture(i);
//                if (isFirst2){
//                    share.setPROISCapture(i);
//                    spCapture.setSelection(i);
//                }
//                else{
//                    spCapture.setSelection(share.getPROISCapture());
//                    isFirst2=true;
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
//                yuandanId = yuandanType.FID;
////                share.setProISYuanDan(i);
//                if (isFirst3){
//                    share.setProISYuanDan(i);
//                    spYuandan.setSelection(i);
//                }
//                else{
//                    spYuandan.setSelection(share.getProISYuanDan());
//                    isFirst3=true;
//                }
//
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
                storage = (Storage) spWhichStorage.getAdapter().getItem(i);
                Hawk.put(getString(R.string.spStorage_pris),storage.FName);
                wavehouseID = "0";
                storageName = storage.FName;
                storageId = storage.FItemID;
//                wavehouseAdapter = method.getWaveHouseAdapter(storage, spWavehouse);
                spWavehouse.setAuto(mContext, storage, wavehouseAutoString);
                getInstorageNum();
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

                getInstorageNum();
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

                getInstorageNum();
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
    }

    @Override
    protected void OnReceive(String code) {
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

    private void LoadBasicData() {
        tvDate.setText(share.getPROISdate());
//        storageAdapter = method.getStorageSpinner(spWhichStorage);
        spWhichStorage.setAutoSelection(getString(R.string.spStorage_pris), "");
        spYuandan.setAutoSelection(getString(R.string.spYuandan_pris), "");
        spYanshou.setAutoSelection(getString(R.string.spYanshou_pris), "");
        spCapture.setAutoSelection(getString(R.string.spCapture_pris), "");

//        yuandanSpAdapter = method.getyuandanSp(spYuandan);
//        yanshouAdapter = method.getEmployeeAdapter(spYanshou);
//        method.getEmployeeAdapter(spCapture);
//        spYuandan.setSelection(ShareUtil.getInstance(mContext).getPROISyanshou());
//        spYanshou.setSelection(ShareUtil.getInstance(mContext).getPROISyanshou());
//        spCapture.setSelection(ShareUtil.getInstance(mContext).getPROISCapture());

    }


    @OnClick({R.id.scanbyCamera, R.id.search, R.id.btn_add, R.id.btn_finishorder,R.id.btn_checkorder})
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
                searchproduct();
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
                        t_main.FDirectorId + "|" +
                        t_main.FCustodyId + "|" +
                        t_main.FRedBlue + "|" +
                        t_main.sourceOrderTypeId + "|";
                puBean.main = main;
                List<T_Detail> details = t_detailDao.queryBuilder().where(
                        T_DetailDao.Properties.FOrderId.eq(t_main.orderId),
                        T_DetailDao.Properties.Activity.eq(activity)
                ).build().list();
                for (int j = 0; j < details.size(); j++) {
                    if (j != 0 && (j + 1) % 50 == 0) {
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
        DataModel.upload(WebApi.UPLOADPROIS, gson.toJson(pBean));
//        postToServer(data);
    }

    private void postToServer(ArrayList<PurchaseInStoreUploadBean.purchaseInStore> data) {
        PurchaseInStoreUploadBean pBean = new PurchaseInStoreUploadBean();
        pBean.list = data;
        Gson gson = new Gson();
        Asynchttp.post(mContext, getBaseUrl() + WebApi.UPLOADPROIS, gson.toJson(pBean), new Asynchttp.Response() {
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

    public void finishOrder() {
        AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
        ab.setTitle("确认使用完单");
        ab.setMessage("确认？");
        ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ordercode++;
                Log.e("ordercode", ordercode + "");
                share.setOrderCode(ProductInStorageActivity.this, ordercode);
            }
        });
        ab.setNegativeButton("取消", null);
        ab.create().show();

    }

    private void searchproduct() {
        Log.e("search", "onclick");
        Bundle b = new Bundle();
        b.putString("search", edCode.getText().toString());
        b.putInt("where", Info.SEARCHPRODUCT);
        startNewActivityForResult(ProductSearchActivity.class, R.anim.activity_open, 0, Info.SEARCHFORRESULT, b);
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
        } else if (requestCode == Info.SEARCHJH) {
            if (resultCode == Info.SEARCHFORRESULTJH) {
                Bundle b = data.getExtras();
                jiaohuoId = b.getString("001");
                jiaohuoName = b.getString("002");
                edghunit.setText(jiaohuoName);
            }
        }
    }

    private void getProductOL(DownloadReturnBean dBean, int j) {
        product = dBean.products.get(j);
        tvorisAuto(product);
    }

    private void tvorisAuto(final Product product) {
        try {
            Lg.e("product:" + product.toString());
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
                edPihao.setText("");
                edPihao.setEnabled(false);
                fBatchManager = false;
            }
            if (isGetDefaultStorage) {
                spWhichStorage.setAutoSelection(getString(R.string.spStorage_pris), product.FDefaultLoc);
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
//                    for (int j = 0; j < wavehouseAdapter.getCount(); j++) {
//                        if (((WaveHouse)wavehouseAdapter.getItem(j)).FSPID.equals(product.FSPID)) {
//                            spWavehouse.setSelection(j);
//                            break;
//                        }
//                    }
                    }
                }, 50);
            }
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


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getInstorageNum();
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
        products = null;
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
            edPihao.setEnabled(false);
        }
    }

    private void getInstorageNum() {
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
            iBean.FStockID = storageId;
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
                    InStorageNumDao.Properties.FStockID.eq(storageId),
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
            String discount = "0";
            String num = edNum.getText().toString();
            T_DetailDao t_detailDao = daoSession.getT_DetailDao();
            T_mainDao t_mainDao = daoSession.getT_mainDao();
            if (product==null){
                MediaPlayer.getInstance(mContext).error();
                Toast.showText(mContext, "请选择物料");
                return;
            }
            if (jiaohuoId==null){
                MediaPlayer.getInstance(mContext).error();
                Toast.showText(mContext, "请选择交货单位");
                return;
            }
            if ((edghunit.getText().toString().equals("")) || edCode.getText().toString().equals("") || edPricesingle.getText().toString().equals("") || edNum.getText().toString().equals("")) {
                MediaPlayer.getInstance(mContext).error();
                if (edCode.getText().toString().equals("")) {
                    Toast.showText(mContext, "请输入物料编号");
                } else if (edPricesingle.getText().toString().equals("")) {
                    Toast.showText(mContext, "请输入单价");
                } else if (edNum.getText().toString().equals("")) {
                    Toast.showText(mContext, "请输入数量");
                } else if (edghunit.getText().toString().equals("")) {
                    Toast.showText(mContext, "请输入交货单位");
                }
            } else if (fBatchManager && edPihao.getText().toString().equals("")) {
                Toast.showText(mContext, "请输入批次号");
            } else {
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
                List<T_main> dewlete = t_mainDao.queryBuilder().where(
                        T_mainDao.Properties.OrderId.eq(ordercode)
                ).build().list();
                t_mainDao.deleteInTx(dewlete);
                String second = getTimesecond();
                T_main t_main = new T_main();
                t_main.FDepartment = jiaohuoName == null ? "" : jiaohuoName;
                t_main.FDepartmentId = jiaohuoId == null ? "" : jiaohuoId;
                t_main.FIndex = second;
                t_main.FPaymentDate = "";
                t_main.orderId = ordercode;
                t_main.orderDate = share.getPROISdate();
                t_main.FPurchaseUnit = spUnit.getDataName();
                t_main.FSalesMan = "";
                t_main.FSalesManId = "";
                t_main.FMaker = share.getUserName();
                t_main.FMakerId = share.getsetUserID();
                t_main.FDirector = spYanshou.getEmployeeName();
                t_main.FDirectorId = spYanshou.getEmployeeId();
                t_main.saleWay = "";
                t_main.FDeliveryAddress = "";
                t_main.FRemark = "";
                t_main.saleWayId = "";
                t_main.FCustody = spCapture.getEmployeeName();
                t_main.FCustodyId = spCapture.getEmployeeId();
                t_main.FAcount = "";
                t_main.FAcountID = "";
                t_main.Rem = "";
                t_main.FRedBlue = "0";
                t_main.supplier = "";
                t_main.supplierId = "";
                t_main.FSendOutId = "";
                t_main.sourceOrderTypeId = spYuandan.getDataId();
                Log.e("yuandan", spYuandan.getDataId());
                t_main.activity = activity;
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
                    Lg.e("添加成功："+t_detail.toString());
                    Toast.showText(mContext, "添加成功");
                    if (!BasicShareUtil.getInstance(mContext).getIsOL()) {
                        InStorageNumDao inStorageNumDao = daoSession.getInStorageNumDao();
                        List<InStorageNum> innum = inStorageNumDao.queryBuilder().where(
                                InStorageNumDao.Properties.FBatchNo.eq(edPihao.getText().toString()),
                                InStorageNumDao.Properties.FStockID.eq(storageId)
                                , InStorageNumDao.Properties.FStockPlaceID.eq(spWavehouse.getWaveHouseId()),
                                InStorageNumDao.Properties.FItemID.eq(product.FItemID)
                        ).build().list();
                        if (innum.size() > 0) {
                            innum.get(0).FQty = (MathUtil.toD(innum.get(0).FQty) + (MathUtil.toD(edNum.getText().toString()) * spUnit.getDataUnitrate())) + "";
                            Log.e("QTY", innum.get(0).FQty);
                            Log.e("QTY", spUnit.getDataUnitrate() + "");
                            Log.e("QTY", num + "");
                            inStorageNumDao.update(innum.get(0));
                        } else {
                            InStorageNum i = new InStorageNum();
                            i.FQty = num;
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
        edNum.setText("");
        edPricesingle.setText("");
        edPihao.setText("");
        edCode.setText("");
        tvNuminstorage.setText("");
        tvGoodName.setText("");
        tvModel.setText("");
        setfocus(edCode);
        product=null;
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
                share.setOrderCode(ProductInStorageActivity.this, ordercode);
                finish();
            }
        });
        ab.setNegativeButton("取消", null);
        ab.create().show();
    }
}

package com.fangzuo.assist.Activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Adapter.ProductselectAdapter1;
import com.fangzuo.assist.Adapter.StorageCheckRyAdapter;
import com.fangzuo.assist.Adapter.StorageSpAdapter;
import com.fangzuo.assist.Adapter.UnitSpAdapter;
import com.fangzuo.assist.Adapter.WaveHouseSpAdapter;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.Beans.InStorageNumListBean;
import com.fangzuo.assist.Beans.InStoreNumBean;
import com.fangzuo.assist.Dao.Product;
import com.fangzuo.assist.Dao.Storage;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Utils.Asynchttp;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.Info;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.assist.widget.LoadingUtil;
import com.fangzuo.greendao.gen.DaoSession;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StorageCheckActivity extends BaseActivity {


    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.check)
    Button check;
    @BindView(R.id.ry_storage_check)
    RecyclerView ryStorageCheck;
    @BindView(R.id.btn_clear)
    Button btnClear;
    @BindView(R.id.tv_storage_num)
    TextView tvStorageNum;
    @BindView(R.id.tv_all)
    TextView tvAll;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_check_by)
    Button btnCheckBy;
    private String fkfDate;
    private String batchNo;
    private StorageSpAdapter storageSpAdapter;
    private Storage storage;
    private WaveHouseSpAdapter waveHouseAdapter;
    private String storageId;
    private String wavehouseID;
    private double qty;
    private Product product;
    private String default_unitID;
    private ProductselectAdapter1 productselectAdapter1;
    private boolean isPeriod;
    private DaoSession daoSession;
    private UnitSpAdapter unitAdapter;
    private String unitId;
    private double unitrate;
    private StorageCheckRyAdapter adapter;
    private DecimalFormat df;
    private ArrayList<InStorageNumListBean.inStoreList> container;
//    private CodeCheckBackDataBean codeCheckBackDataBean;
    @Override
    public void initView() {
        setContentView(R.layout.activity_storage_check);
        mContext = this;
        ButterKnife.bind(this);
        daoSession = GreenDaoManager.getmInstance(mContext).getDaoSession();
        tvTitle.setText("库存查询");
        df = new DecimalFormat("######0.00");
        //已在xml中设置
//        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        container = new ArrayList<>();
        adapter = new StorageCheckRyAdapter(mContext, container);
//        ryAcountNew.setLayoutManager(mLinearLayoutManager);
        ryStorageCheck.setAdapter(adapter);
        ryStorageCheck.setItemAnimator(new DefaultItemAnimator());
        ryStorageCheck.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
//        adapter.setInnerClickListener(this);
        adapter.notifyDataSetChanged();
        setfocus(btnCheckBy);
    }

    @Override
    public void initData() {
//        storageSpAdapter = CommonMethod.getMethod(mContext).getStorageSpinner2(spWhichStorage);

    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(ClassEvent event) {
        switch (event.Msg) {
            case EventBusInfoCode.PRODUCTRETURN:
                product = (Product) event.postEvent;
                Lg.e(product.toString());
                getProductOL(product);
                break;
//            case EventBusInfoCode.CodeCheck_OK://检测条码成功
//                codeCheckBackDataBean = (CodeCheckBackDataBean)event.postEvent;
//                Lg.e("条码检查："+codeCheckBackDataBean.toString());
////                edPihao.setText(codeCheckBackDataBean.FBatchNo);
////                edPdnum.setText(codeCheckBackDataBean.FQty);
//                LoadingUtil.dismiss();
//                getDate(codeCheckBackDataBean.FItemID);
////                setDATA(codeCheckBackDataBean.FItemID,false);
//                break;
//            case EventBusInfoCode.CodeCheck_Error://检测条码失败
//                LoadingUtil.dismiss();
//                codeCheckBackDataBean = (CodeCheckBackDataBean)event.postEvent;
//                Toast.showText(mContext, codeCheckBackDataBean.FTip);
//                break;
//            case EventBusInfoCode.CodeInsert_OK://写入条码唯一表成功
//                AddOrder();
//                break;
//            case EventBusInfoCode.CodeInsert_Error://写入条码唯一表失败
//                codeCheckBackDataBean = (CodeCheckBackDataBean)event.postEvent;
//                Toast.showText(mContext, codeCheckBackDataBean.FTip);
//                break;
//            case EventBusInfoCode.Upload_OK://回单成功
//                t_detailDao.deleteInTx(t_detailDao.queryBuilder().where(
//                        T_DetailDao.Properties.Activity.eq(activity)
//                ).build().list());
////                t_mainDao.deleteInTx(t_mainDao.queryBuilder().where(
////                        T_mainDao.Properties.Activity.eq(activity)
////                ).build().list());
//                btnBackorder.setClickable(true);
//                LoadingUtil.dismiss();
//                MediaPlayer.getInstance(mContext).ok();
//                break;
//            case EventBusInfoCode.Upload_Error://回单失败
//                String error = (String)event.postEvent;
//                Toast.showText(mContext, error);
//                btnBackorder.setClickable(true);
//                LoadingUtil.dismiss();
//                MediaPlayer.getInstance(mContext).error();
//                break;
        }
    }

    private boolean isFirst=true;
    @Override
    public void initListener() {
//        ryStorage.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                adapter.removeAll();
//                ryStorage.setRefreshing(true);
//                getInstorageNumList(false);
//            }
//        });
//        spWhichStorage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                storage = (Storage) storageSpAdapter.getItem(i);
////                waveHouseAdapter = CommonMethod.getMethod(mContext).getWaveHouseAdapter(storage, spWavehouse);
//                spWavehouse.setAuto(mContext,storage,"");
//
//                storageId = storage.FItemID;
//                Log.e("storageId", storageId);
////                etCode.setText("");
////                if (!isFirst){
////                    getInstorageNumList(false);
////                }
////                    isFirst=false;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//
//        spWavehouse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//                if (spWavehouse.getAdapter().getCount()>0){
//                    WaveHouse waveHouse = (WaveHouse) spWavehouse.getAdapter().getItem(i);
//                    wavehouseID = waveHouse.FSPID;
////                    wavehouseName = waveHouse.FName;
//                }else{
//                    spWavehouse.setAuto(mContext,storage,"");
//                }
////                if (!isFirst){
////                    getInstorageNumList(false);
////                }
////                    isFirst=false;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//
//        spUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Unit unit = (Unit) unitAdapter.getItem(i);
//                if (unit != null) {
//                    unitId = unit.FMeasureUnitID;
//                    unitrate = Double.parseDouble(unit.FCoefficient);
//                    Log.e("unitId", unitId + "");
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

    private String barcode = "";
    @Override
    public void OnReceive(String code) {
        barcode = code;
//        LoadingUtil.showDialog(mContext,"正在查找...");
//        查询条码唯一表
//        CodeCheckBean bean = new CodeCheckBean(barcode);
//        DataModel.codeCheckForPD(gson.toJson(bean));


//        try {
//            //条码规则:条码^批次^数量^单据编号
////            StringBuilder logString = new StringBuilder();
//            String[] split = code.split("\\^", 4);
//            Log.e("Split222", split.length + "");
////            for (int i = 0; i < split.length; i++) {
////                logString.append(split[i] + "|");
////            }
////            Log.e("Split", logString.toString());
//            if (split.length >= 4) {
////                onlyCode = split[4];
//                String productCode = split[0];
//                etCode.setText(split[1]);
//                getDate(productCode);
//            } else {
//                Toast.showText(mContext, "条码有误");
//                MediaPlayer.getInstance(mContext).error();
//            }
//        } catch (NullPointerException e) {
//            Toast.showText(mContext, "条码数据缺失");
//            MediaPlayer.getInstance(mContext).error();
//        }
//        // 普通条码和特定条码规则:条形码^批次
//        if (code.contains("^")){
//            try {
//                String[] split = code.split("\\^", 2);
//                Log.e("Split", split.length + "");
//                if (split.length == 2) {
//                    String productCode = split[0];
////                    edPihao.setText(split[1]);
//                    etCode.setText(split[1]);
//                    getDate(productCode);
//                } else {
//                    Toast.showText(mContext, "条码有误");
//                    MediaPlayer.getInstance(mContext).error();
//                }
//            } catch (NullPointerException e) {
//                Toast.showText(mContext, "条码数据缺失");
//                MediaPlayer.getInstance(mContext).error();
//            }
//        }else{
//            etCode.setText(code);
//            getDate(code);
//        }
//        try {
//            StringBuilder logString = new StringBuilder();
//            String[] split = code.split("\\^");
//            Log.e("Split", "^");
//            Log.e("Split", split.length + "");
//            for (int i = 0; i < split.length; i++) {
//                logString.append(split[i] + "|||");
//            }
//            Log.e("Split", logString.toString());
//            if (split.length > 4) {
//                String productCode = split[0];
//                fkfDate = split[2];
//                batchNo = split[1];
//                tvDateProduce.setText(fkfDate);
//                etBatchNo.setText(batchNo);
//                etCode.setText(productCode);
//                getDate(productCode);
//            } else {
//                getDate(code);
//            }
//
//        } catch (NullPointerException e) {
//            Toast.showText(mContext, "条码数据缺失");
//            MediaPlayer.getInstance(mContext).error();
//        }
    }

    private void getDate(String productID) {
        App.getRService().doIOAction("",productID, new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                LoadingUtil.dismiss();
                final DownloadReturnBean dBean = new Gson().fromJson(commonResponse.returnJson, DownloadReturnBean.class);
                if (dBean.products.size() > 0) {
                    product = dBean.products.get(0);
                    Lg.e("获得物料："+product.toString());
                    getProductOL(dBean, 0);
                } else {
                    Toast.showText(mContext, "未找到物料信息");
                }
            }

            @Override
            public void onError(Throwable e) {
                LoadingUtil.dismiss();
                Toast.showText(mContext, "物料信息获取失败" + e.toString());
            }
        });

//        Asynchttp.post(mContext, getBaseUrl() + WebApi.SEARCHPRODUCTS, productCode, new Asynchttp.Response() {
//            @Override
//            public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
//                final DownloadReturnBean dBean = new Gson().fromJson(cBean.returnJson, DownloadReturnBean.class);
//                if (dBean.products.size() == 1) {
//                    getProductOL(dBean, 0);
//                    default_unitID = dBean.products.get(0).FUnitID;
//                } else if (dBean.products.size() > 1) {
//                    AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
//                    ab.setTitle("请选择物料");
//                    View v = LayoutInflater.from(mContext).inflate(R.layout.pd_alert, null);
//                    ListView lv = v.findViewById(R.id.lv_alert);
//                    productselectAdapter1 = new ProductselectAdapter1(mContext, dBean.products);
//                    lv.setAdapter(productselectAdapter1);
//                    ab.setView(v);
//                    final AlertDialog alertDialog = ab.create();
//                    alertDialog.show();
//                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                            getProductOL(dBean, i);
//                            default_unitID = dBean.products.get(i).FUnitID;
//                            alertDialog.dismiss();
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onFailed(String Msg, AsyncHttpClient client) {
//                Toast.showText(mContext, Msg);
//            }
//        });
    }

    private void getProductOL(DownloadReturnBean dBean, int j) {
        product = dBean.products.get(j);
        etCode.setText(product.FNumber);
//        tvProductName.setText(product.FName);
//        tvProductCode.setText(product.FNumber);

//        isPeriod = (product.FISKFPeriod) != null && (product.FISKFPeriod).equals("1");
//        if (isPeriod) {
//            tvPeriod.setText(product.FKFPeriod + "");
//        }
//        unitAdapter = CommonMethod.getMethod(mContext).getUnitAdapter(product.FUnitGroupID, spUnit);

        getInstorageNumList(false);

    }

    private void getProductOL(Product product) {
        Log.e("asdf", "asdf" + product.toString());
        etCode.setText(product.FName);
//        tvProductName.setText(product.FName);
//        tvProductCode.setText(product.FNumber);

//        isPeriod = (product.FISKFPeriod) != null && (product.FISKFPeriod).equals("1");
//        if (isPeriod) {
//            tvPeriod.setText(product.FKFPeriod + "");
//        }
//        unitAdapter = CommonMethod.getMethod(mContext).getUnitAdapter(product.FUnitGroupID, spUnit);

        getInstorageNumList(false);

    }

    private String AllG="";
    private String AllM="";
    int allG;
    double allM;
    private void getInstorageNumList(boolean type) {
        Lg.e("getInstorageNumList", "进入");
        LoadingUtil.show(mContext,"正在查找...",true);
        String json;
        String productId="";
        if (type){
            json=new Gson().toJson(new InStoreNumBean("","","",""));
        }else{
            if (product == null) {
                if ("".equals(etCode.getText().toString().trim())){
                    productId="";
                }else{
                    productId=etCode.getText().toString().trim();
                }
            }else{
                productId = product.FItemID;
            }

            if (null ==storageId ||"".equals(storageId)){
                storageId="";
            }
            if (null ==wavehouseID ||"".equals(wavehouseID)){
                wavehouseID="";
            }

            InStoreNumBean iBean = new InStoreNumBean();
            iBean.FItemID = productId;
            iBean.FStockID = storageId;
            iBean.FStockPlaceID = wavehouseID;
            json = new Gson().toJson(iBean);
        }


        Log.e("获取库存明细-json：", json);
        Asynchttp.post(mContext, getBaseUrl() + WebApi.GetInStorageNumList, json, new Asynchttp.Response() {
            @Override
            public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                Lg.e("getInstorageNumList", "获取库存明细数据：" + cBean.returnJson);
                InStorageNumListBean iBean = new Gson().fromJson(cBean.returnJson, InStorageNumListBean.class);
                adapter.clear();
                for (InStorageNumListBean.inStoreList bean:iBean.list) {
                    if (!"".equals(bean.FQty) && Double.parseDouble(bean.FQty)>0){
                        adapter.addNewItem(bean);
                    }
                }
                LoadingUtil.dismiss();
//                adapter.addAll(iBean.list);
                tvStorageNum.setText("共有"+adapter.getAllData().size()+"条商品");
//                allG=0;
                allM=0;
                for (int i=0;i<adapter.getAllData().size();i++){
//                    allG=allG+(int)Math.ceil(Double.parseDouble(adapter.getAllData().get(i).FSecQty));
                    allM=allM+Double.parseDouble(adapter.getAllData().get(i).FQty);
                }
                tvAll.setText("汇总："+df.format(allM));
            }

            @Override
            public void onFailed(String Msg, AsyncHttpClient client) {
                LoadingUtil.dismiss();
                Lg.e("getInstorageNumList：获取库存明细数据失败：");
                Toast.showText(mContext, Msg);
                tvStorageNum.setText("无商品信息");
//                tvAll.setText("无数据");
                adapter.clear();
            }
        });
    }

    private void checkStorage(){

        InStoreNumBean iBean = new InStoreNumBean();
        iBean.FStockPlaceID = wavehouseID;
        iBean.FStockID = storageId;
        iBean.FItemID = product.FItemID;
        String json = new Gson().toJson(iBean);
        Log.e("获取库存-json：", json);
        Asynchttp.post(mContext, getBaseUrl() + WebApi.GETINSTORENUM, json, new Asynchttp.Response() {
            @Override
            public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                Lg.e("库存："+cBean.returnJson);
                qty = Double.parseDouble(cBean.returnJson);
            }

            @Override
            public void onFailed(String Msg, AsyncHttpClient client) {
                Lg.e("获取库存失败。。。");
                qty = 0.0;
            }
        });
    }

//    private void getInstorageNum() {
////        batchNo = etBatchNo.getText().toString();
////        if (batchNo == null || batchNo.equals("")) {
////            batchNo = "0";
////        } else {
////            batchNo = "";
////        }
////        if (wavehouseID == null) {
////            wavehouseID = "0";
////        }
////        if (product ==null ){
////            return;
////        }
////        InStoreNumBean iBean = new InStoreNumBean();
////        iBean.FStockPlaceID = wavehouseID;
////        iBean.FBatchNo = batchNo;
////        iBean.FStockID = storageId;
////        iBean.FItemID = product.FItemID;
////        iBean.FKFDate = fkfDate;
////        String json = new Gson().toJson(iBean);
////        Log.e("获取库存-json：", json);
////        Asynchttp.post(mContext, getBaseUrl() + WebApi.GETINSTORENUM, json, new Asynchttp.Response() {
////            @Override
////            public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
////                Lg.e("库存："+cBean.returnJson);
////                qty = Double.parseDouble(cBean.returnJson);
////            }
////
////            @Override
////            public void onFailed(String Msg, AsyncHttpClient client) {
////                Lg.e("获取库存失败。。。");
////                qty = 0.0;
////            }
////        });
////
////
////        tvTotalNum.setText(qty / unitrate + "");
//
//    }

    @OnClick(R.id.check)
    public void onViewClicked() {
        Log.e("search", "onclick");
        Bundle b1 = new Bundle();
        b1.putString("search", etCode.getText().toString());
        b1.putInt("where", Info.SEARCHPRODUCT);
        startNewActivityForResult(ProductSearchActivity.class, R.anim.activity_open, 0, Info.SEARCHFORRESULT, b1);

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.e("code", requestCode + "" + "    " + resultCode);
//        if (requestCode == Info.SEARCHFORRESULT) {
//            if (resultCode == 9998) {
////                Bundle b = data.getExtras();
////                product = (Product) b.getSerializable("001");
//                product = Hawk.get(Config.For_Back_Data_InStorageNum, null);
//                Lg.e("返回Product：" + product.toString());
//                etCode.setText(product.FNumber);
////                getProductOL(product);
////                getInstorageNumList(false);
//            }
//        }
//    }


    @OnClick({R.id.btn_clear, R.id.btn_check_by, R.id.btn_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_clear:
                etCode.setText("");
                product=null;
                break;
            case R.id.btn_check_by:
                getInstorageNumList(false);
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }

}
package com.fangzuo.assist.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Adapter.BatchNoSpAdapter;
import com.fangzuo.assist.Adapter.PushDownSubListAdapter;
import com.fangzuo.assist.Adapter.StorageSpAdapter;
import com.fangzuo.assist.Adapter.UnitSpAdapter;
import com.fangzuo.assist.Adapter.WaveHouseSpAdapter;
import com.fangzuo.assist.Dao.Product;
import com.fangzuo.assist.Dao.PushDownSub;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.CommonMethod;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.PushDownMainDao;
import com.fangzuo.greendao.gen.PushDownSubDao;
import com.fangzuo.greendao.gen.T_DetailDao;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class InCheckGoodsActivity extends BaseActivity {

    @BindView(R.id.ishebing)
    CheckBox ishebing;
    @BindView(R.id.isAutoAdd)
    CheckBox isAutoAdd;
    @BindView(R.id.lv_pushsub)
    ListView lvPushsub;
    @BindView(R.id.sp_storage)
    Spinner spStorage;
    @BindView(R.id.sp_wavehouse)
    Spinner spWavehouse;
    @BindView(R.id.productName)
    TextView productName;
    @BindView(R.id.sp_unit)
    Spinner spUnit;
    @BindView(R.id.ed_batchNo)
    EditText edBatchNo;
    @BindView(R.id.ed_num)
    EditText edNum;
    @BindView(R.id.btn_add)
    Button btnAdd;
    @BindView(R.id.btn_backorder)
    Button btnBackorder;
    @BindView(R.id.btn_checkorder)
    Button btnCheckorder;
    private DaoSession daoSession;
    boolean isAuto = true;
    private CommonMethod method;
    private StorageSpAdapter storageSpinner;
    private BatchNoSpAdapter batchNoSpAdapter;
    private WaveHouseSpAdapter waveHouseAdapter;
    private String storageID;
    private String storageName;
    private String waveHouseID;
    private String waveHouseName;
    private ArrayList<PushDownSub> container;           //用于存储订单详情信息
    private ArrayList<String> fidcontainer;
    private PushDownSubDao pushDownSubDao;
    private PushDownMainDao pushDownMainDao;
    private PushDownSubListAdapter pushDownSubListAdapter;
    private Product product;
    private PushDownSub pushDownSub;
    private List<Product> products;
    private String batchNo;
    private String productID;
    private String fid;
    private String fentryid;
    private String fprice;
    private UnitSpAdapter unitAdapter;
    private String unitId;
    private String unitName;
    private double unitrate;
    private T_DetailDao t_detailDao;
    private String billNo;
    public static final int OUCHECK = 95411;
    private ArrayList<String> fidc;
    private ArrayList<String> fidno;
    private String FstorageID;
    private String FwaveHouseID;
    private String Batch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_in_check_goods);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void OnReceive(String code) {

    }

    @OnClick({R.id.btn_add, R.id.btn_backorder, R.id.btn_checkorder})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                break;
            case R.id.btn_backorder:
                break;
            case R.id.btn_checkorder:
                break;
        }
    }
}

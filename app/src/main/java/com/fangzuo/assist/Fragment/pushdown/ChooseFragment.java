package com.fangzuo.assist.Fragment.pushdown;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.ABase.BaseFragment;
import com.fangzuo.assist.Activity.CGDDPDSLTZDActivity;
import com.fangzuo.assist.Activity.FHTZDDBActivity;
import com.fangzuo.assist.Activity.HBDPDCPRKActivity;
import com.fangzuo.assist.Activity.InCheckGoodsActivity;
import com.fangzuo.assist.Activity.OutCheckGoodsActivity;
import com.fangzuo.assist.Activity.OutsourcingOrdersISActivity;
import com.fangzuo.assist.Activity.OutsourcingOrdersOSActivity;
import com.fangzuo.assist.Activity.ProducePushInStoreActivity;
import com.fangzuo.assist.Activity.PushDownMTActivity;
import com.fangzuo.assist.Activity.PushDownPOActivity;
import com.fangzuo.assist.Activity.PushDownPagerActivity;
import com.fangzuo.assist.Activity.PushDownSNActivity;
import com.fangzuo.assist.Activity.SCRWDPDSCHBDActivity;
import com.fangzuo.assist.Activity.ShengchanrenwudanxiatuilingliaoActivity;
import com.fangzuo.assist.Activity.ShouLiaoTongZhiActivity;
import com.fangzuo.assist.Activity.XSDDPDFLTZDActivity;
import com.fangzuo.assist.Adapter.ClientSpAdapter;
import com.fangzuo.assist.Adapter.PushDownListAdapter;
import com.fangzuo.assist.Adapter.SupplierSpAdapter;
import com.fangzuo.assist.Dao.Client;
import com.fangzuo.assist.Dao.PushDownMain;
import com.fangzuo.assist.Dao.PushDownSub;
import com.fangzuo.assist.Dao.Suppliers;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Service.DataService;
import com.fangzuo.assist.Utils.CommonMethod;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.widget.SpinnerClient;
import com.fangzuo.assist.widget.SpinnerSupplier;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.PushDownMainDao;
import com.fangzuo.greendao.gen.PushDownSubDao;
import com.fangzuo.greendao.gen.T_DetailDao;
import com.fangzuo.greendao.gen.T_mainDao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


//选择单据信息Fragment（所属：PushDownPagerActivity);
public class ChooseFragment extends BaseFragment {


    @BindView(R.id.ed_code)
    EditText edCode;
    @BindView(R.id.start_date)
    TextView startDate;
    @BindView(R.id.end_date)
    TextView endDate;
    @BindView(R.id.btn_delete)
    Button btnDelete;
    @BindView(R.id.btn_search)
    Button btnSearch;
    @BindView(R.id.lv_pushdown_download)
    ListView lvPush;
    Unbinder unbinder;
    @BindView(R.id.btn_getpush)
    Button btnGetpush;
    @BindView(R.id.rl1)
    LinearLayout rl1;
    @BindView(R.id.v1)
    View v1;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    @BindView(R.id.sp_client)
    SpinnerClient spClient;
    @BindView(R.id.sp_supplier)
    SpinnerSupplier spSupplier;
    private FragmentActivity mContext;
    private DaoSession daosession;
    private ArrayList<Boolean> isCheck;
    private PushDownMainDao pushDownMainDao;
    private int tag;
//    private SupplierSpAdapter supplierAdapter;
//    private ClientSpAdapter clientSpAdapter;
    private String supplierID;
    private boolean defaultsp = false;
    private List<PushDownMain> container;               //单据信息，用于存储查找到的单据数据
    private ArrayList<PushDownMain> downloadIDs;        //单据信息，用于存储选中的单据数据
    private PushDownListAdapter pushDownListAdapter;
    private Intent intent;

    public ChooseFragment() {
        // Required empty public constructor
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        tag = ((PushDownPagerActivity) activity).getTitles();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_choose, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        return view;
    }


    private void push() {
        boolean flag = true;
        ArrayList<String> container = new ArrayList<>();
        if (downloadIDs.size() == 0) {
            Toast.showText(mContext, "请选择单据");
            return;
        }
        for (int i = 0; i < downloadIDs.size(); i++) {
            container.add(downloadIDs.get(i).FInterID);
            if (i > 0 && !downloadIDs.get(i).FSupplyID.equals(downloadIDs.get(i - 1).FSupplyID)) {
                flag = false;
                break;
            }
        }

        if (flag && downloadIDs.size() > 0) {
            Bundle b = new Bundle();
            b.putStringArrayList("fid", container);
            Lg.e("ChooseFragment跳转数据", container);
            switch (tag) {
                case 1://销售订单下推销售出库
                    intent = new Intent(mContext, PushDownMTActivity.class);
                    break;
                case 2://采购订单下推外购入库
                    intent = new Intent(mContext, PushDownPOActivity.class);
                    break;
                case 3://发货通知下推销售出库
                    intent = new Intent(mContext, PushDownSNActivity.class);
                    break;
                case 4://收料通知下推外购入库
                    intent = new Intent(mContext, ShouLiaoTongZhiActivity.class);
                    break;
                case 11://委外订单下推委外入库
                    intent = new Intent(mContext, OutsourcingOrdersISActivity.class);
                    break;
                case 12://委外订单下推委外出库
                    intent = new Intent(mContext, OutsourcingOrdersOSActivity.class);
                    break;
                case 9://生产任务单下推产品入库
                    intent = new Intent(mContext, ProducePushInStoreActivity.class);
                    break;
                case 13://生产任务单下推生产领料  出
                    intent = new Intent(mContext, ShengchanrenwudanxiatuilingliaoActivity.class);
                    break;
                case 14://采购订单下推收料通知单
                    intent = new Intent(mContext, CGDDPDSLTZDActivity.class);
                    break;
                case 15://销售订单下推发料通知单
                    intent = new Intent(mContext, XSDDPDFLTZDActivity.class);
                    break;
                case 16://生产任务单下推生生产汇报单
                    intent = new Intent(mContext, SCRWDPDSCHBDActivity.class);
                    break;
                case 18://汇报单下推产品入库
                    intent = new Intent(mContext, HBDPDCPRKActivity.class);
                    break;
                case 7://销售出库单验货
                    intent = new Intent(mContext, OutCheckGoodsActivity.class);
                    break;
                case 20://发货通知生成调拨单     出
                    intent = new Intent(mContext, FHTZDDBActivity.class);
                    break;
//                        case 22://产品入库验货
//                            intent = new Intent(mContext, InCheckGoodsActivity.class);
//                            break;
            }


            intent.putExtras(b);
            startActivity(intent);
            getActivity().finish();
        } else {
            if (!flag) Toast.showText(mContext, "供应商不一致");
            else if (downloadIDs.size() < 0) Toast.showText(mContext, "未选择下推单据");
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //相当于Fragment的onResume
            initList();
        } else {
            //相当于Fragment的onPause
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initList();
    }

    @Override
    protected void initView() {
        isCheck = new ArrayList<>();
        downloadIDs = new ArrayList<PushDownMain>();
        if (tag == 1 || tag == 3) {
            //初始化客户数据
            spClient.setVisibility(View.VISIBLE);
            spSupplier.setVisibility(View.GONE);
        } else {
            //初始化往来单位数据
            spClient.setVisibility(View.GONE);
            spSupplier.setVisibility(View.VISIBLE);
        }
        container = new ArrayList<>();
        daosession = GreenDaoManager.getmInstance(mContext).getDaoSession();
        initList();
    }

    @Override
    protected void initData() {

    }

    private void initList() {
        isCheck.clear();
        container.clear();
        downloadIDs.clear();
        pushDownMainDao = daosession.getPushDownMainDao();
        //根据 tag 查找相应的单据
        List<PushDownMain> pushDownMains = pushDownMainDao.queryBuilder().where(
                PushDownMainDao.Properties.Tag.eq(tag)
        ).build().list();
        container.addAll(pushDownMains);
        for (int i = 0; i < pushDownMains.size(); i++) {
            isCheck.add(false);
        }
        pushDownListAdapter = new PushDownListAdapter(mContext, container, isCheck);
        lvPush.setAdapter(pushDownListAdapter);
        pushDownListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initListener() {
        //刷新
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initList();
                refresh.setRefreshing(false);
            }
        });

        //列表选中监听
        lvPush.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PushDownMain pushDownListReturnBean = (PushDownMain) pushDownListAdapter.getItem(i);
                if (isCheck.get(i)) {
                    Lg.e("choose", "不--选中");
                    isCheck.set(i, false);
                    for (int j = 0; j < downloadIDs.size(); j++) {
                        if (downloadIDs.get(j).FInterID.equals(pushDownListReturnBean.FInterID)) {
                            downloadIDs.remove(j);
                        }
                    }
                } else {
                    Lg.e("choose", "选中");
                    isCheck.set(i, true);
                    downloadIDs.add(pushDownListReturnBean);
                }
                pushDownListAdapter.notifyDataSetChanged();
            }
        });
        startDate.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startDate.setText("");
                return true;
            }
        });
        endDate.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                endDate.setText("");
                return true;
            }
        });
    }

    @Override
    protected void OnReceive(String code) {

    }

    public String getTime(boolean b) {
        SimpleDateFormat format = new SimpleDateFormat(b ? "yyyy-MM-dd" : "yyyyMMdd");
        Date curDate = new Date();
        String str = format.format(curDate);
        return str;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_delete, R.id.btn_search, R.id.btn_getpush,R.id.start_date,R.id.end_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_delete:
                delete();
                break;
            case R.id.btn_search:
                Search();
                break;
            case R.id.btn_getpush:
                push();
                break;
            case R.id.start_date:
                datePicker(startDate);
                break;
            case R.id.end_date:
                datePicker(endDate);
                break;
        }
    }


    //删除本地数据
    private void delete() {
        try {
            PushDownSubDao pushDownSubDao = daosession.getPushDownSubDao();
            for (int i = 0; i < downloadIDs.size(); i++) {
                List<PushDownSub> pushDownSubs = pushDownSubDao.queryBuilder().where(PushDownSubDao.Properties.FInterID.eq(downloadIDs.get(i).FInterID)).build().list();
                for (int j = 0; j < pushDownSubs.size(); j++) {
                    pushDownSubDao.delete(pushDownSubs.get(j));
                }
                //删掉与该单据相关的明细
                daosession.getT_DetailDao().deleteInTx(daosession.getT_DetailDao().queryBuilder().where(
                        T_DetailDao.Properties.FInterID.eq(downloadIDs.get(i).FInterID)).build().list());
                daosession.getT_mainDao().deleteInTx(daosession.getT_mainDao().queryBuilder().where(
                        T_mainDao.Properties.FDeliveryType.eq(downloadIDs.get(i).FInterID)).build().list());
                pushDownMainDao.delete(downloadIDs.get(i));
                Toast.showText(mContext, "删除成功");
            }

            initList();
            Search();
        } catch (Exception e) {
            DataService.pushError(mContext, this.getClass().getSimpleName(), e);
        }
    }

    //查找本地数据
    private void Search() {
        try {
            if (tag == 1 || tag == 3) {
                //客户信息绑定
                supplierID=spClient.getDataId();
            } else {
                //供应商信息绑定
                supplierID=spSupplier.getDataId();
            }
            container.clear();
            isCheck.clear();
            String con="";
            if (!"".equals(endDate.getText().toString())&& !"".equals(startDate.getText().toString())) {
                con += " and  FDATE between " + "\'" + startDate.getText().toString()+" 00:00:00.0" + "\'" + "and" + "\'" + endDate.getText().toString()+" 00:00:00.0" + "\'";
            }
            if (!"".equals(supplierID)){
                con+=" and FSUPPLY_ID='"+supplierID+"'";
            }
            if (!"".equals(edCode.getText().toString())){
                con+=" and FBILL_NO='"+edCode.getText().toString()+"'";
            }
            String SQL = "SELECT * FROM PUSH_DOWN_MAIN WHERE 1=1 "+con+" and TAG="+tag;
            Lg.e("SQL:"+SQL);
            Cursor cursor = GreenDaoManager.getmInstance(mContext).getDaoSession().getDatabase().rawQuery(SQL, null);
            while (cursor.moveToNext()) {
                PushDownMain f = new PushDownMain();
                f.FBillNo = cursor.getString(cursor.getColumnIndex("FBILL_NO"));
                f.FName = cursor.getString(cursor.getColumnIndex("FNAME"));
                f.FDate = cursor.getString(cursor.getColumnIndex("FDATE"));
                f.FSupplyID = cursor.getString(cursor.getColumnIndex("FSUPPLY_ID"));
                f.FInterID = cursor.getString(cursor.getColumnIndex("FINTER_ID"));
                f.FManagerID = cursor.getString(cursor.getColumnIndex("FMANAGER_ID"));
                f.FEmpID = cursor.getString(cursor.getColumnIndex("FEMP_ID"));
                f.FDeptID = cursor.getString(cursor.getColumnIndex("FDEPT_ID"));
                f.tag = cursor.getInt(cursor.getColumnIndex("TAG"));
                container.add(f);
            }
            if (container.size()==0){
                Toast.showText(mContext,"未查询到数据");
            }else{
                for (int i = 0; i < container.size(); i++) {
                    isCheck.add(false);
                }
            }
            pushDownListAdapter.notifyDataSetChanged();




//
//
//            if (list.size() > 0) {
//                container.clear();
//                container.addAll(list);
//                isCheck.clear();
//                for (int i = 0; i < container.size(); i++) {
//                    isCheck.add(false);
//                }
//                pushDownListAdapter = new PushDownListAdapter(mContext, container, isCheck);
//                lvPush.setAdapter(pushDownListAdapter);
//            } else {
//                Toast.showText(mContext, "未查询到数据");
//            }
//
//            pushDownListAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            DataService.pushError(mContext, this.getClass().getSimpleName(), e);
        }
    }


}

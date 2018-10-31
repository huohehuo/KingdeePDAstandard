package com.fangzuo.assist.Fragment.pushdown;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.fangzuo.assist.Utils.CommonMethod;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.PushDownMainDao;
import com.fangzuo.greendao.gen.PushDownSubDao;

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
public class ChooseFragment extends Fragment {
    @BindView(R.id.sp_wlunit)
    Spinner spWlunit;
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
    private FragmentActivity mContext;
    private DaoSession daosession;
    private ArrayList<Boolean> isCheck;
    private int year;
    private int month;
    private int day;
    private String enddate;
    private String startdate;
    private PushDownMainDao pushDownMainDao;
    private int tag;
    private SupplierSpAdapter supplierAdapter;
    private ClientSpAdapter clientSpAdapter;
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        isCheck = new ArrayList<>();
        year = Calendar.getInstance().get(Calendar.YEAR);
        month = Calendar.getInstance().get(Calendar.MONTH);
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        downloadIDs = new ArrayList<PushDownMain>();
        if (tag == 1 || tag == 3) {
            //初始化客户数据
            clientSpAdapter = CommonMethod.getMethod(mContext).getCilent(spWlunit);
        } else {
            //初始化往来单位数据
            supplierAdapter = CommonMethod.getMethod(mContext).getSupplier(spWlunit);
        }
        container = new ArrayList<>();
        daosession = GreenDaoManager.getmInstance(mContext).getDaoSession();
        initList();
        initListener();
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
            Log.e("ChooseFragment", "跳转数据：" + container.toString());
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

    private void initListener() {
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
                    Log.e("choose", "不--选中");
                    isCheck.set(i, false);
                    for (int j = 0; j < downloadIDs.size(); j++) {
                        if (downloadIDs.get(j).FInterID.equals(pushDownListReturnBean.FInterID)) {
                            downloadIDs.remove(j);
                        }
                    }
                } else {
                    Log.e("choose", "选中");
                    isCheck.set(i, true);
                    downloadIDs.add(pushDownListReturnBean);
                }
                pushDownListAdapter.notifyDataSetChanged();
            }
        });
        spWlunit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!defaultsp) {
                    supplierID = "";
                } else {
                    if (tag == 1 || tag == 3) {
                        if (clientSpAdapter != null) {
                            Client client = (Client) clientSpAdapter.getItem(i);
                            supplierID = client.FItemID;
                        }
                    } else {
                        if (supplierAdapter != null) {
                            Suppliers supplier = (Suppliers) supplierAdapter.getItem(i);
                            supplierID = supplier.FItemID;
                        }
                    }
                }
                defaultsp = true;
                Log.e("supplier", supplierID);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    }
                }, year, month, day);
                datePickerDialog.show();
                datePickerDialog.setButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int year = datePickerDialog.getDatePicker().getYear();
                        int month = datePickerDialog.getDatePicker().getMonth();
                        int day = datePickerDialog.getDatePicker().getDayOfMonth();
                        enddate = year + "-" + ((month < 10) ? "0" + (month + 1) : (month + 1)) + "-" + ((day < 10) ? "0" + day : day);
                        endDate.setText(enddate);
                        Toast.showText(mContext, enddate);
                        datePickerDialog.dismiss();
                    }
                });
            }
        });

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    }
                }, year, month, day);
                datePickerDialog.show();
                datePickerDialog.setButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int year = datePickerDialog.getDatePicker().getYear();
                        int month = datePickerDialog.getDatePicker().getMonth();
                        int day = datePickerDialog.getDatePicker().getDayOfMonth();
                        startdate = year + "-" + ((month < 10) ? "0" + (month + 1) : (month + 1)) + "-" + ((day < 10) ? "0" + day : day);
                        startDate.setText(startdate);
                        Toast.showText(mContext, startdate);
                        datePickerDialog.dismiss();
                    }
                });
            }
        });
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

    @OnClick({R.id.btn_delete, R.id.btn_search, R.id.btn_getpush})
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
        }
    }


    //删除本地数据
    private void delete() {
        PushDownSubDao pushDownSubDao = daosession.getPushDownSubDao();
        for (int i = 0; i < downloadIDs.size(); i++) {
            List<PushDownSub> pushDownSubs = pushDownSubDao.queryBuilder().where(PushDownSubDao.Properties.FInterID.eq(downloadIDs.get(i).FInterID)).build().list();
            for (int j = 0; j < pushDownSubs.size(); j++) {
                pushDownSubDao.delete(pushDownSubs.get(j));
            }
            pushDownMainDao.delete(downloadIDs.get(i));
            Toast.showText(mContext, "删除成功");
        }

        initList();
        Search();
    }

    //查找本地数据
    private void Search() {
        String code = edCode.getText().toString();
        String endtime = endDate.getText().toString();
        String startTime = startDate.getText().toString();
        List<PushDownMain> list = pushDownMainDao.queryBuilder().where(
                PushDownMainDao.Properties.FBillNo.like("%" + code + "%"),
                PushDownMainDao.Properties.FSupplyID.like("%" + supplierID + "%"),
                PushDownMainDao.Properties.FDate.between(startTime, endtime)
        ).build().list();
        if (list.size() > 0) {
            container.clear();
            container.addAll(list);

        } else {
            Toast.showText(mContext, "未查询到数据");
        }

        pushDownListAdapter.notifyDataSetChanged();
    }


}

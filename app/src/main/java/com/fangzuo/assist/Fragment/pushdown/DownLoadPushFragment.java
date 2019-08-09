package com.fangzuo.assist.Fragment.pushdown;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.device.ScanDevice;
import android.os.Bundle;
import android.os.Handler;
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
import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Activity.FHTZDDBActivity;
import com.fangzuo.assist.Activity.HBDPDCPRKActivity;
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
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownLoadSubListBean;
import com.fangzuo.assist.Beans.PushDownDLBean;
import com.fangzuo.assist.Beans.PushDownListRequestBean;
import com.fangzuo.assist.Beans.PushDownListReturnBean;
import com.fangzuo.assist.Beans.ScanDLReturnBean;
import com.fangzuo.assist.Dao.Client;
import com.fangzuo.assist.Dao.PushDownMain;
import com.fangzuo.assist.Dao.PushDownSub;
import com.fangzuo.assist.Dao.Suppliers;
import com.fangzuo.assist.ABase.BaseFragment;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Service.DataService;
import com.fangzuo.assist.Utils.Asynchttp;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.CommonMethod;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.assist.widget.LoadingUtil;
import com.fangzuo.assist.widget.SpinnerClient;
import com.fangzuo.assist.widget.SpinnerSupplier;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.PushDownMainDao;
import com.fangzuo.greendao.gen.PushDownSubDao;
import com.fangzuo.greendao.gen.T_DetailDao;
import com.fangzuo.greendao.gen.T_mainDao;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * <p>
 * //下载单据信息Fragment（所属：PushDownPagerActivity);
 */
public class DownLoadPushFragment extends BaseFragment {


    @BindView(R.id.ed_code)
    EditText edCode;
    @BindView(R.id.start_date)
    TextView startDate;
    @BindView(R.id.end_date)
    TextView endDate;
    @BindView(R.id.btn_download)
    Button btnDownload;
    @BindView(R.id.btn_search)
    Button btnSearch;
    @BindView(R.id.rl1)
    LinearLayout rl1;
    @BindView(R.id.lv_pushdown_download)
    ListView lvPushdownDownload;
    Unbinder unbinder;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    @BindView(R.id.sp_client)
    SpinnerClient spClient;
    @BindView(R.id.sp_supplier)
    SpinnerSupplier spSupplier;
    private int tag;
    private FragmentActivity mContext;
//    private SupplierSpAdapter supplierAdapter;
//    private ClientSpAdapter clientSpAdapter;
//    private String clientID;
//    private String supplierID;
    private boolean defaultsp = false;
    private ArrayList<Boolean> isCheck;
    private PushDownListAdapter pushDownListAdapter;
    private ArrayList<PushDownMain> downloadIDs;            //用于listview选择时，添加临时对象
    private PushDownListReturnBean puBean;
    private DaoSession daosession;
    private String enddate;
    private String startdate;
    private ArrayList<PushDownMain> container;
    private ScanDevice sm;
    private Intent intent;
    private String TAG = "DownLoadPushFragment";

    @Override
    protected void initView() {
        isCheck = new ArrayList<>();
        downloadIDs = new ArrayList<>();

        daosession = GreenDaoManager.getmInstance(mContext).getDaoSession();

        if (tag == 1 || tag == 3) {
            //客户信息绑定
            spClient.setVisibility(View.VISIBLE);
            spSupplier.setVisibility(View.GONE);
        } else {
            //供应商信息绑定
            spClient.setVisibility(View.GONE);
            spSupplier.setVisibility(View.VISIBLE);

        }
    }

    @Override
    protected void OnReceive(String barCode) {
        Lg.e("Fragment-code:", barCode);

        LoadingUtil.show(mContext,"正在下载...");
        Toast.showText(mContext, barCode + "下载中...");
        PushDownListRequestBean pBean = new PushDownListRequestBean();
        pBean.id = tag;
        pBean.code = barCode;
        App.getRService().doIOAction(WebApi.SCANTODLPDLIST, new Gson().toJson(pBean), new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                super.onNext(commonResponse);
                if (!commonResponse.state)return;
                LoadingUtil.dismiss();
                Lg.e("OnReceive-获取数据:" ,commonResponse.returnJson);
                ScanDLReturnBean sBean = new Gson().fromJson(commonResponse.returnJson, ScanDLReturnBean.class);
                PushDownMainDao pushDownMainDao = daosession.getPushDownMainDao();
                PushDownSubDao pushDownSubDao = daosession.getPushDownSubDao();
                List<PushDownMain> list = pushDownMainDao.queryBuilder().where(PushDownMainDao.Properties.FInterID.eq(sBean.list1.get(0).FInterID)).build().list();
                if (list.size() > 0) {
                    pushDownMainDao.deleteInTx(list);
                    List<PushDownSub> pushDownSubs = pushDownSubDao.queryBuilder().where(PushDownSubDao.Properties.FInterID.eq(sBean.list1.get(0).FInterID)).build().list();
                    if (pushDownSubs.size() > 0) {
                        pushDownSubDao.deleteInTx(pushDownSubs);
                    }
                    T_mainDao t_mainDao = daosession.getT_mainDao();
                    T_DetailDao t_detailDao = daosession.getT_DetailDao();
                    t_mainDao.deleteInTx(t_mainDao.queryBuilder().where(T_mainDao.Properties.FDeliveryType.eq(sBean.list1.get(0).FInterID)).build().list());
                    t_detailDao.deleteInTx(t_detailDao.queryBuilder().where(T_DetailDao.Properties.FInterID.eq(sBean.list1.get(0).FInterID)).build().list());
                }
                pushDownMainDao.insert(sBean.list1.get(0));
                pushDownSubDao.insertInTx(sBean.list);
                final ArrayList<String> container = new ArrayList<>();
                container.add(sBean.list1.get(0).FInterID);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Bundle b = new Bundle();
                        b.putStringArrayList("fid", container);
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
                            case 13://生产任务单下推生产领料
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
                            case 20://发货通知生成调拨单
                                intent = new Intent(mContext, FHTZDDBActivity.class);
                                break;
//                            case 8:
//                                intent = new Intent(mContext, GetGoodsCheckActivity.class);
//                                break;
//                            case 10:
//                                intent = new Intent(mContext, PDProduceReportPROISActivity.class);
//                                break;
                        }


                        intent.putExtras(b);
                        startActivity(intent);
                        getActivity().finish();
                    }
                }, 200);
            }

            @Override
            public void onError(Throwable e) {
//                super.onError(e);
                LoadingUtil.dismiss();
                Toast.showText(mContext, e.getMessage());
            }
        });
//        Asynchttp.post(
//                mContext,
//                BasicShareUtil.getInstance(mContext).getBaseURL() + WebApi.SCANTODLPDLIST,
//                new Gson().toJson(pBean),
//                new Asynchttp.Response() {
//                    @Override
//                    public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
//                        LoadingUtil.dismiss();
//                        Lg.e("OnReceive-获取数据:" ,cBean.returnJson);
//                        ScanDLReturnBean sBean = new Gson().fromJson(cBean.returnJson, ScanDLReturnBean.class);
//                        PushDownMainDao pushDownMainDao = daosession.getPushDownMainDao();
//                        PushDownSubDao pushDownSubDao = daosession.getPushDownSubDao();
//                        List<PushDownMain> list = pushDownMainDao.queryBuilder().where(PushDownMainDao.Properties.FInterID.eq(sBean.list1.get(0).FInterID)).build().list();
//                        if (list.size() > 0) {
//                            pushDownMainDao.deleteInTx(list);
//                            List<PushDownSub> pushDownSubs = pushDownSubDao.queryBuilder().where(PushDownSubDao.Properties.FInterID.eq(sBean.list1.get(0).FInterID)).build().list();
//                            if (pushDownSubs.size() > 0) {
//                                pushDownSubDao.deleteInTx(pushDownSubs);
//                            }
//                            T_mainDao t_mainDao = daosession.getT_mainDao();
//                            T_DetailDao t_detailDao = daosession.getT_DetailDao();
//                            t_mainDao.deleteInTx(t_mainDao.queryBuilder().where(T_mainDao.Properties.FDeliveryType.eq(sBean.list1.get(0).FInterID)).build().list());
//                            t_detailDao.deleteInTx(t_detailDao.queryBuilder().where(T_DetailDao.Properties.FInterID.eq(sBean.list1.get(0).FInterID)).build().list());
//                        }
//                        pushDownMainDao.insert(sBean.list1.get(0));
//                        pushDownSubDao.insertInTx(sBean.list);
//                        final ArrayList<String> container = new ArrayList<>();
//                        container.add(sBean.list1.get(0).FInterID);
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                Bundle b = new Bundle();
//                                b.putStringArrayList("fid", container);
//                                switch (tag) {
//                                    case 1://销售订单下推销售出库
//                                        intent = new Intent(mContext, PushDownMTActivity.class);
//                                        break;
//                                    case 2://采购订单下推外购入库
//                                        intent = new Intent(mContext, PushDownPOActivity.class);
//                                        break;
//                                    case 3://发货通知下推销售出库
//                                        intent = new Intent(mContext, PushDownSNActivity.class);
//                                        break;
//                                    case 4://收料通知下推外购入库
//                                        intent = new Intent(mContext, ShouLiaoTongZhiActivity.class);
//                                        break;
//                                    case 11://委外订单下推委外入库
//                                        intent = new Intent(mContext, OutsourcingOrdersISActivity.class);
//                                        break;
//                                    case 12://委外订单下推委外出库
//                                        intent = new Intent(mContext, OutsourcingOrdersOSActivity.class);
//                                        break;
//                                    case 9://生产任务单下推产品入库
//                                        intent = new Intent(mContext, ProducePushInStoreActivity.class);
//                                        break;
//                                    case 13://生产任务单下推生产领料
//                                        intent = new Intent(mContext, ShengchanrenwudanxiatuilingliaoActivity.class);
//                                        break;
//                                    case 14://采购订单下推收料通知单
//                                        intent = new Intent(mContext, CGDDPDSLTZDActivity.class);
//                                        break;
//                                    case 15://销售订单下推发料通知单
//                                        intent = new Intent(mContext, XSDDPDFLTZDActivity.class);
//                                        break;
//                                    case 16://生产任务单下推生生产汇报单
//                                        intent = new Intent(mContext, SCRWDPDSCHBDActivity.class);
//                                        break;
//                                    case 18://汇报单下推产品入库
//                                        intent = new Intent(mContext, HBDPDCPRKActivity.class);
//                                        break;
//                                    case 7://销售出库单验货
//                                        intent = new Intent(mContext, OutCheckGoodsActivity.class);
//                                        break;
//                                    case 20://发货通知生成调拨单
//                                        intent = new Intent(mContext, FHTZDDBActivity.class);
//                                        break;
////                            case 8:
////                                intent = new Intent(mContext, GetGoodsCheckActivity.class);
////                                break;
////                            case 10:
////                                intent = new Intent(mContext, PDProduceReportPROISActivity.class);
////                                break;
//                                }
//
//
//                                intent.putExtras(b);
//                                startActivity(intent);
//                                getActivity().finish();
//                            }
//                        }, 200);
//
//                    }
//
//                    @Override
//                    public void onFailed(String Msg, AsyncHttpClient client) {
//                        LoadingUtil.dismiss();
//                        Toast.showText(mContext, Msg);
//                    }
//                });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        //下拉刷新
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                searchList();
                refresh.setRefreshing(false);
            }
        });

        //列表下载的选择处理
        lvPushdownDownload.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PushDownMain pushDownListReturnBean = (PushDownMain) pushDownListAdapter.getItem(i);
                if (isCheck.get(i)) {
                    isCheck.set(i, false);
                    for (int j = 0; j < downloadIDs.size(); j++) {
                        if (downloadIDs.get(j).FInterID.equals(pushDownListReturnBean.FInterID)) {
                            downloadIDs.remove(j);
                        }
                    }
                } else {
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


    public DownLoadPushFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        tag = ((PushDownPagerActivity) activity).getTitles();
        Lg.e("获取到--tag--", tag + "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_down_load_push, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    //点击事件
    @OnClick({R.id.btn_download, R.id.btn_search,R.id.start_date,R.id.end_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_download:
                if (downloadIDs.size() > 0) {
                    download(downloadIDs);
                } else {
                    Toast.showText(mContext, "未选择单号");
                }
                break;
            case R.id.btn_search:
                searchList();
                break;
            case R.id.start_date:
                datePicker(startDate);
                break;
            case R.id.end_date:
                datePicker(endDate);
                break;
        }
    }

    //下载数据
    private void download(final ArrayList<PushDownMain> downloadIDs) {
        try {
            LoadingUtil.show(mContext, "下载中...");
            for (int i = 0; i < downloadIDs.size(); i++) {
                final PushDownMain pushDownMain = downloadIDs.get(i);
                final int finalI = i;
                Lg.e("finterid", i + "");
                Lg.e("finterid2", finalI + "");
                DownLoadSubListBean dBean = new DownLoadSubListBean();
                dBean.interID = downloadIDs.get(i).FInterID;
                dBean.tag = downloadIDs.get(i).tag;
                //获取下推订单信息
                App.getRService().doIOAction(WebApi.PUSHDOWNDLLIST, new Gson().toJson(dBean), new MySubscribe<CommonResponse>() {
                    @Override
                    public void onNext(CommonResponse commonResponse) {
                        super.onNext(commonResponse);
                        if (!commonResponse.state)return;
                        //查找本地的单据信息，若和选择的单据id相同，则删除本地相对应的单据信息，
                        PushDownMainDao pushDownMainDao = daosession.getPushDownMainDao();
                        List<PushDownMain> pushDownMains = pushDownMainDao.loadAll();
                        Lg.e("download-获取数据:" ,commonResponse.returnJson);
                        PushDownDLBean pBean = new Gson().fromJson(commonResponse.returnJson, PushDownDLBean.class);
                        PushDownSubDao pushDownSubDao = daosession.getPushDownSubDao();

                        for (int j = 0; j < pushDownMains.size(); j++) {
                            if (pushDownMains.get(j).FInterID.equals(downloadIDs.get(finalI).FInterID)) {
                                pushDownMainDao.delete(pushDownMains.get(j));
                                //查找本地的下推订单信息，若和选择的下推订单id相同，则删除本地相对应的下推订单信息，
                                List<PushDownSub> pushDownSubs = pushDownSubDao.loadAll();
                                for (int k = 0; k < pushDownSubs.size(); k++) {
                                    if (pushDownMains.get(j).FInterID.equals(pushDownSubs.get(k).FInterID)) {
                                        pushDownSubDao.delete(pushDownSubs.get(k));
                                    }
                                }
                            }
                        }
                        //异步添加下推订单信息
                        for (int j = 0; j < pBean.list.size(); j++) {
                            pushDownSubDao.insertOrReplaceInTx(pBean.list.get(j));
                        }

                        T_mainDao t_mainDao = daosession.getT_mainDao();
                        T_DetailDao t_detailDao = daosession.getT_DetailDao();
                        //删除本地指定数据
                        t_mainDao.deleteInTx(t_mainDao.queryBuilder().where(
                                T_mainDao.Properties.FDeliveryType.eq(
                                        downloadIDs.get(finalI).FInterID)
                        ).build().list());
                        //删除本地指定数据
                        t_detailDao.deleteInTx(t_detailDao.queryBuilder().where(
                                T_DetailDao.Properties.FInterID.eq(
                                        downloadIDs.get(finalI).FInterID)
                        ).build().list());
                        //异步添加单据信息
                        pushDownMainDao.insertOrReplaceInTx(pushDownMain);
                        Toast.showText(mContext, "下载成功");
                        LoadingUtil.dismiss();

                    }

                    @Override
                    public void onError(Throwable e) {
//                        super.onError(e);
                        Toast.showText(mContext, e.getMessage());
                        LoadingUtil.dismiss();
                    }
                });

//                Asynchttp.post(mContext,
//                        BasicShareUtil.getInstance(mContext).getBaseURL() + WebApi.PUSHDOWNDLLIST,
//                        new Gson().toJson(dBean), new Asynchttp.Response() {
//                            @Override
//                            public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
//                                //查找本地的单据信息，若和选择的单据id相同，则删除本地相对应的单据信息，
//                                PushDownMainDao pushDownMainDao = daosession.getPushDownMainDao();
//                                List<PushDownMain> pushDownMains = pushDownMainDao.loadAll();
//                                Log.e(TAG, "download-获取数据:" + cBean.returnJson);
//                                PushDownDLBean pBean = new Gson().fromJson(cBean.returnJson, PushDownDLBean.class);
//                                PushDownSubDao pushDownSubDao = daosession.getPushDownSubDao();
//
//                                for (int j = 0; j < pushDownMains.size(); j++) {
//                                    if (pushDownMains.get(j).FInterID.equals(downloadIDs.get(finalI).FInterID)) {
//                                        pushDownMainDao.delete(pushDownMains.get(j));
//                                        //查找本地的下推订单信息，若和选择的下推订单id相同，则删除本地相对应的下推订单信息，
//                                        List<PushDownSub> pushDownSubs = pushDownSubDao.loadAll();
//                                        for (int k = 0; k < pushDownSubs.size(); k++) {
//                                            if (pushDownMains.get(j).FInterID.equals(pushDownSubs.get(k).FInterID)) {
//                                                pushDownSubDao.delete(pushDownSubs.get(k));
//                                            }
//                                        }
//                                    }
//                                }
//                                //异步添加下推订单信息
//                                for (int j = 0; j < pBean.list.size(); j++) {
//                                    pushDownSubDao.insertOrReplaceInTx(pBean.list.get(j));
//                                }
//
//                                T_mainDao t_mainDao = daosession.getT_mainDao();
//                                T_DetailDao t_detailDao = daosession.getT_DetailDao();
//                                //删除本地指定数据
//                                t_mainDao.deleteInTx(t_mainDao.queryBuilder().where(
//                                        T_mainDao.Properties.FDeliveryType.eq(
//                                                downloadIDs.get(finalI).FInterID)
//                                ).build().list());
//                                //删除本地指定数据
//                                t_detailDao.deleteInTx(t_detailDao.queryBuilder().where(
//                                        T_DetailDao.Properties.FInterID.eq(
//                                                downloadIDs.get(finalI).FInterID)
//                                ).build().list());
//                                //异步添加单据信息
//                                pushDownMainDao.insertOrReplaceInTx(pushDownMain);
//                                Toast.showText(mContext, "下载成功");
//                                LoadingUtil.dismiss();
//                            }
//
//                            @Override
//                            public void onFailed(String Msg, AsyncHttpClient client) {
//                                Toast.showText(mContext, Msg);
//                                LoadingUtil.dismiss();
//                            }
//                        });
            }
        } catch (Exception e) {
            DataService.pushError(mContext, this.getClass().getSimpleName(), e);
        }
    }

    //获取数据
    private void searchList() {
        try {
            LoadingUtil.showDialog(mContext, "正在加载...");
            container = new ArrayList<>();
            isCheck = new ArrayList<>();
            String code = edCode.getText().toString();
            String endtime = endDate.getText().toString();
            String startTime = startDate.getText().toString();

            PushDownListRequestBean pBean = new PushDownListRequestBean();
            pBean.id = tag;
            pBean.code = code;
            pBean.StartTime = startTime;
            pBean.endTime = endtime;
            if (tag == 1 || tag == 3) {
                pBean.FWLUnitID = spClient.getDataId();
            } else {
                pBean.FWLUnitID = spSupplier.getDataId();
            }
            String Json = new Gson().toJson(pBean);
            //获取单据信息
            App.getRService().doIOAction(WebApi.PUSHDOWNLIST, Json, new MySubscribe<CommonResponse>() {
                @Override
                public void onNext(CommonResponse commonResponse) {
                    super.onNext(commonResponse);
                    if (!commonResponse.state)return;
                    LoadingUtil.dismiss();
                    puBean = new Gson().fromJson(commonResponse.returnJson, PushDownListReturnBean.class);
                    Lg.e("获取单据信息数：", puBean.list.size());
                    isCheck.clear();
                    for (int i = 0; i < puBean.list.size(); i++) {
                        isCheck.add(false);
                    }
                    container.addAll(puBean.list);
                    pushDownListAdapter = new PushDownListAdapter(mContext, container, isCheck);
                    if (lvPushdownDownload != null) {//可防止还没刷新出数据就退出页面后的崩溃问题
                        lvPushdownDownload.setAdapter(pushDownListAdapter);
                        pushDownListAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onError(Throwable e) {
//                    super.onError(e);
                    LoadingUtil.dismiss();
                    Toast.showText(mContext, e.getMessage());
                    pushDownListAdapter = new PushDownListAdapter(mContext, container, isCheck);
                    if (lvPushdownDownload != null) {
                        lvPushdownDownload.setAdapter(pushDownListAdapter);
                    }
                    pushDownListAdapter.notifyDataSetChanged();
                }
            });
//            Asynchttp.post(
//                    mContext,
//                    BasicShareUtil.getInstance(mContext).getBaseURL() + WebApi.PUSHDOWNLIST,
//                    Json,
//                    new Asynchttp.Response() {
//                        @Override
//                        public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
//                            LoadingUtil.dismiss();
//                            puBean = new Gson().fromJson(cBean.returnJson, PushDownListReturnBean.class);
//                            Log.e("获取单据信息数：", puBean.list.size() + "");
//                            isCheck.clear();
//                            for (int i = 0; i < puBean.list.size(); i++) {
//                                isCheck.add(false);
//                            }
//                            container.addAll(puBean.list);
//                            pushDownListAdapter = new PushDownListAdapter(mContext, container, isCheck);
//                            if (lvPushdownDownload != null) {//可防止还没刷新出数据就退出页面后的崩溃问题
//                                lvPushdownDownload.setAdapter(pushDownListAdapter);
//                                pushDownListAdapter.notifyDataSetChanged();
//                            }
//                        }
//
//                        @Override
//                        public void onFailed(String Msg, AsyncHttpClient client) {
//                            LoadingUtil.dismiss();
//                            Toast.showText(mContext, Msg);
//                            pushDownListAdapter = new PushDownListAdapter(mContext, container, isCheck);
//                            if (lvPushdownDownload != null) {
//                                lvPushdownDownload.setAdapter(pushDownListAdapter);
//                            }
//                            pushDownListAdapter.notifyDataSetChanged();
//
//                        }
//                    });
        } catch (Exception e) {
            DataService.pushError(mContext, this.getClass().getSimpleName(), e);
        }
    }
}

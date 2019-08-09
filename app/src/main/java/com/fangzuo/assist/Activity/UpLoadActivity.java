package com.fangzuo.assist.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.Beans.PurchaseInStoreUploadBean;
import com.fangzuo.assist.Dao.Product;
import com.fangzuo.assist.Dao.T_Detail;
import com.fangzuo.assist.Dao.T_main;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.DataModel;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.EventBusUtil;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.MediaPlayer;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.assist.widget.LoadingUtil;
import com.fangzuo.greendao.gen.PushDownMainDao;
import com.fangzuo.greendao.gen.PushDownSubDao;
import com.fangzuo.greendao.gen.T_DetailDao;
import com.fangzuo.greendao.gen.T_mainDao;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpLoadActivity extends BaseActivity {

    @BindView(R.id.view_cancle)
    View viewCancle;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.pg_top)
    ProgressBar pgTop;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.tv_success_num)
    TextView tvSuccessNum;

    private PushDownSubDao pushDownSubDao;
    private PushDownMainDao pushDownMainDao;
    private PurchaseInStoreUploadBean pBean;
    private PurchaseInStoreUploadBean.purchaseInStore puBean;
    private ArrayList<String> detailContainer;
    private ArrayList<PurchaseInStoreUploadBean.purchaseInStore> data;
    private String io;
    private int isPushDown;
    private int activity;
    private int upLoadNum = 0;//用于递增请求数
    private int okUpLoad = 0;//用于显示成功数
    private List<T_main> mains;

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(ClassEvent event) {
        switch (event.Msg) {
            case EventBusInfoCode.Upload_OK://回单成功
//                long orderid = (long) event.postEvent;
                if (isPushDown != 0) {
                    pushDownSubDao.deleteInTx(pushDownSubDao.queryBuilder().where(
                            PushDownSubDao.Properties.FInterID.eq(mains.get(upLoadNum).FDeliveryType)).build().list());
                    pushDownMainDao.deleteInTx(pushDownMainDao.queryBuilder().where(
                            PushDownMainDao.Properties.FInterID.eq(mains.get(upLoadNum).FDeliveryType)).build().list());
                }
                t_detailDao.deleteInTx(t_detailDao.queryBuilder().where(
                        T_DetailDao.Properties.Activity.eq(activity),
                        T_DetailDao.Properties.FOrderId.eq(mains.get(upLoadNum).orderId)
                ).build().list());
                t_mainDao.deleteInTx(t_mainDao.queryBuilder().where(
                        T_mainDao.Properties.Activity.eq(activity),
                        T_mainDao.Properties.OrderId.eq(mains.get(upLoadNum).orderId)
                ).build().list());
                okUpLoad++;
                if (upLoadNum == mains.size() - 1) {
                    tvSuccessNum.setText(okUpLoad + "");
                    pgTop.setVisibility(View.GONE);
                    btnBack.setVisibility(View.VISIBLE);
                    MediaPlayer.getInstance(mContext).ok();
                    title.setText("执行完毕");
                    Toast.showText(mContext, "已上传完");
//                    finish();
                } else {
                    tvSuccessNum.setText(upLoadNum + "");
                    upLoadNum++;
                    upLoad(upLoadNum);
                }
                break;
            case EventBusInfoCode.Upload_Error://回单失败
                String error = (String) event.postEvent;
                Toast.showText(mContext, error);
                MediaPlayer.getInstance(mContext).error();
                if (upLoadNum == mains.size() - 1) {
                    tvSuccessNum.setText(okUpLoad + "");
                    pgTop.setVisibility(View.GONE);
                    btnBack.setVisibility(View.VISIBLE);
                    title.setText("执行完毕");
                    Toast.showText(mContext, "已上传完，关闭");
//                    finish();
                } else {
                    upLoadNum++;
                    upLoad(upLoadNum);
                }
                break;
        }
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_up_load);
        ButterKnife.bind(this);
        Intent in = getIntent();
        Bundle b = in.getExtras();
//        String searchString = b.getString("search", "");
        isPushDown = b.getInt("isPushDown");
        activity = b.getInt("activity");
        Lg.e("获得数据：", "" + isPushDown);
        Lg.e("获得数据：", "" + activity);
        pushDownSubDao = daoSession.getPushDownSubDao();
        pushDownMainDao = daoSession.getPushDownMainDao();
    }

    @Override
    protected void initData() {
        mains = t_mainDao.queryBuilder().where(T_mainDao.Properties.Activity.eq(activity)).build().list();
        tvNum.setText(mains.size() + "");
        upLoad(upLoadNum);
    }

    //根据表头进行遍历，拼接出表头和明细的字符串
    private void upLoad(int i) {
        pBean = new PurchaseInStoreUploadBean();
        puBean = pBean.new purchaseInStore();
        detailContainer = new ArrayList<>();
        data = new ArrayList<>();

        detailContainer = new ArrayList<>();
        puBean = pBean.new purchaseInStore();
        String main;
        String detail = "";
        T_main t_main = mains.get(i);
        main = getMainString(t_main);
        puBean.main = main;
        List<T_Detail> details = t_detailDao.queryBuilder().where(
                T_DetailDao.Properties.Activity.eq(activity),
                T_DetailDao.Properties.FOrderId.eq(t_main.orderId)
        ).build().list();
        Lg.e("请求表头：" + mains.get(upLoadNum));
        Lg.e("请求明细：" + details.size());
        if (details.size() <= 0) {
            EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Upload_OK, ""));
            return;
        }
        for (int j = 0; j < details.size(); j++) {
            if (j != 0 && j % 49 == 0) {
                Log.e("j%49", j % 49 + "");
                T_Detail t_detail = details.get(j);
                detail = detail + getDetailString(t_detail);
                detail = detail.subSequence(0, detail.length() - 1).toString();
                detailContainer.add(detail);
                detail = "";
            } else {
                Log.e("j", j + "");
                Log.e("details.size()", details.size() + "");
                T_Detail t_detail = details.get(j);
                detail = detail + getDetailString(t_detail);
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
        pBean.list = data;
        App.getRService().doIOAction(io, gson.toJson(pBean), new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                super.onNext(commonResponse);
                if (commonResponse.state) {
                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Upload_OK, ""));
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Upload_Error, e.toString()));
            }
        });
    }


    //拼接表头数据
    private String getMainString(T_main t_main) {
        String string = "";
        switch (activity) {
            case Config.PurchaseOrderActivity:
                io = WebApi.UPLOADPO;
                string = t_main.FMakerId + "|" +
                        t_main.orderDate + "|" +
                        t_main.FPaymentDate + "|" +
                        t_main.saleWayId + "|" +
                        t_main.FAcount + "|" +
                        t_main.FDepartmentId + "|" +
                        t_main.FSalesManId + "|" +
                        t_main.FDirectorId + "|" +
                        t_main.supplierId + "|" +
                        t_main.Rem + "|" +
                        t_main.FCustodyId + "|" +
                        t_main.FPaymentTypeId + "|" +
                        t_main.sourceOrderTypeId + "|";
                break;
            case Config.PurchaseInStorageActivity:
                io = WebApi.UPLOADPIS;
                string = t_main.FMakerId + "|" +
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
                break;
            case Config.ProductInStorageActivity:
                io = WebApi.UPLOADPROIS;
                string = t_main.FMakerId + "|" +
                        t_main.orderDate + "|" +
                        t_main.FDepartmentId + "|" +
                        t_main.FDirectorId + "|" +
                        t_main.FCustodyId + "|" +
                        t_main.FRedBlue + "|" +
                        t_main.sourceOrderTypeId + "|";
                break;
            case Config.SaleOrderActivity:
                io = WebApi.UPLOADSO;
                string = t_main.FMakerId + "|" +
                        t_main.orderDate + "|" +
                        t_main.FPaymentDate + "|" +
                        t_main.saleWayId + "|"
                        + "1" + "|" +
                        t_main.FDepartmentId + "|" +
                        t_main.FSalesManId + "|" +
                        t_main.FDirectorId + "|" +
                        t_main.supplierId + "|" +
                        t_main.FRemark + "|" +
                        t_main.FCustodyId + "|" +
                        t_main.FPaymentTypeId + "|" +
                        t_main.FAcount + "|" +
                        t_main.sourceOrderTypeId + "|";
                break;
            case Config.SoldOutActivity:
                io = WebApi.UPLOADSOUT;
                string = t_main.FMakerId + "|" +
                        t_main.orderDate + "|" +
                        t_main.FPaymentDate + "|" +
                        t_main.saleWayId + "|" +
                        t_main.FDeliveryAddress + "|" +
                        t_main.FDepartmentId + "|" +
                        t_main.FSalesManId + "|" +
                        t_main.FDirectorId + "|" +
                        t_main.supplierId + "|" +
                        t_main.Rem + "|" +
                        t_main.FSalesManId + "|" +
                        t_main.sourceOrderTypeId + "|" +
                        t_main.FRedBlue + "|" +
                        t_main.FCustodyId + "|" +
                        t_main.sourceOrderTypeId + "|";
                break;
            case Config.ProduceAndGetActivity:
                io = WebApi.PRODUCEANDGET;
                string = t_main.FMakerId + "|" +
                        t_main.orderDate + "|" +
                        t_main.FDepartmentId + "|"
                        + t_main.FDirectorId + "|" +
                        t_main.FSalesManId + "|" +
                        t_main.FRedBlue + "|" +
                        t_main.FCustodyId + "|" +
                        "" + "|";
                break;
            case Config.OtherOutStoreActivity:
                io = WebApi.OTHEROUTSTORE;
                string = t_main.FMakerId + "|" +
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
                break;
            case Config.OtherInStoreActivity:
                io = WebApi.OTHERINSTORE;
                string = t_main.FMakerId + "|" +
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
                break;
            case Config.DBActivity:
                io = WebApi.DBUPLOAD;
                string = t_main.FMakerId + "|" +
                        t_main.orderDate + "|" +
                        t_main.FDepartmentId + "|" +
                        t_main.FSalesManId + "|" +
                        t_main.FCustodyId + "|" +
                        t_main.FDirectorId + "|";
                break;
/*
                //下推单据---------------------------------------------------------------------
*/
            case Config.FHTZDDBActivity://发货通知生成调拨单     出
                io = "PushDownFHDBUpload";
                string = t_main.FMakerId + "|" +
                        t_main.orderDate + "|" +
                        t_main.FPaymentDate + "|" +
                        t_main.supplierId + "|" +
                        "0" + "|" +
                        t_main.FDepartmentId + "|" +
                        t_main.FSalesManId + "|" +
                        "" + "|" +
                        t_main.saleWayId + "|" +
                        t_main.saleWay + "|";
                break;
            case Config.HBDPDCPRKActivity://汇报单下推产品入库
                io = WebApi.HBDPDCPRKUpload;
                string = t_main.FMakerId + "|" +
                        t_main.orderDate + "|" + "|" +
                        t_main.FDepartmentId + "|" +
                        "" + "|" +
                        t_main.saleWayId + "|" +
                        t_main.FSalesManId + "|" +
                        t_main.FCustody + "|" +
                        t_main.FAcount + "|" +
                        t_main.supplier + "|";
                break;
            case Config.SCRWDPDSCHBDActivity://生产任务单下推生生产汇报单
                io = WebApi.SCRWDPDSCHBDUPLOAD;
                string =t_main.FMakerId + "|" + t_main.orderDate + "|" + t_main.Rem + "|";
                break;
            case Config.XSDDPDFLTZDActivity://销售订单下推发料通知单
                io = WebApi.XSDDPDFLTZUPLOAD;
                string =t_main.FMakerId + "|" + t_main.orderDate + "|" + t_main.FPaymentDate + "|" + t_main.supplierId + "|" + t_main.saleWayId + "|"
                        + "1" + "|" + t_main.FDepartmentId + "|" +
                        t_main.FSalesManId + "|" + t_main.FDirectorId + "|" + "0" + "|" + t_main.supplier + "|";
                break;
            case Config.CGDDPDSLTZDActivity://采购订单下推收料通知单
                io = WebApi.CGDDPDSLTZDUpload;
                string = t_main.FMakerId + "|" +
                        t_main.orderDate + "|" +
                        t_main.FPaymentDate + "|" +
                        t_main.supplierId + "|" +
                        t_main.FPaymentTypeId + "|"
                        + "|" +
                        t_main.FDepartmentId + "|" +
                        t_main.FSalesManId + "|" +
                        t_main.Rem + "|" +
                        t_main.FDirectorId + "|";
                break;
            case Config.ShengchanrenwudanxiatuilingliaoActivity://生产任务单下推生产领料  出
                io = WebApi.SCRWSCLLUpload;
                string = t_main.FMakerId + "|" +
                        t_main.orderDate + "|" +
                        "" + "|" +
                        t_main.FDepartmentId + "|" +
                        t_main.saleWayId + "|" +
                        "" + "|" +
                        t_main.FDirectorId + "|" +
                        "" + "|" +
                        t_main.FCustody + "|" +
                        t_main.FCustodyId + "|";
                break;
            case Config.ProducePushInStoreActivity://生产任务单下推产品入库
                io = WebApi.PUSHDOWNPPISUPLOAD;
                string = t_main.FMakerId + "|" +
                        t_main.orderDate + "|" +
                        "" + "|" +
                        t_main.FDepartmentId + "|" +
                        t_main.FPaymentTypeId + "|" +
                        "" + "|" +
                        t_main.FSalesManId + "|" +
                        t_main.FDirectorId + "|" +
                        t_main.FPaymentTypeId + "|" +
                        t_main.supplier + "|";
                break;
            case Config.OutsourcingOrdersOSActivity://委外订单下推委外出库
                io = WebApi.PushDownOCOSUpload;
                string = t_main.FMakerId + "|" +
                        t_main.orderDate + "|" +
                        "1" + "|" +
                        t_main.saleWayId + "|" +
                        t_main.FPaymentTypeId + "|" +
                        "" + "|" +
                        t_main.supplier + "|" +
                        "" + "|";
                break;
            case Config.OutsourcingOrdersISActivity://委外订单下推委外入库
                io = WebApi.PushDownOCISUpload;
                string = t_main.FMakerId + "|" +
                        t_main.orderDate + "|" +
                        "1" + "|" +
                        t_main.saleWayId + "|" +
                        t_main.FPaymentTypeId + "|" +
                        "" + "|" +
                        t_main.supplier + "|" +
                        "" + "|";
                break;
            case Config.ShouLiaoTongZhiActivity://收料通知下推外购入库
                io = WebApi.PUSHDOWNSLUPLOAD;
                string = t_main.FMakerId + "|" +
                        t_main.orderDate + "|" +
                        t_main.FPaymentDate + "|" +
                        t_main.supplierId + "|" +
                        t_main.FRemark + "|" +
                        t_main.FDepartmentId + "|" +
                        t_main.FSalesManId + "|" +
                        t_main.FDirectorId + "|" +
                        t_main.FPaymentTypeId + "|" +
                        t_main.supplier + "|";
                break;
            case Config.PushDownSNActivity://发货通知下推销售出库
                io = WebApi.PUSHDOWNSNUPLOAD;
                string = t_main.FMakerId + "|" +
                        t_main.orderDate + "|" +
                        t_main.FPaymentDate + "|" +
                        t_main.supplierId + "|" +
                        t_main.saleWayId + "|" +
                        t_main.FDepartmentId + "|" +
                        t_main.FSalesManId + "|" +
                        t_main.FDirectorId + "|" +
                        t_main.FCustody + "|" +
                        t_main.supplier + "|";
                break;
            case Config.PushDownMTActivity://销售订单下推销售出库
                io = WebApi.PUSHDOWNSOUPLOAD;
                string = t_main.FMakerId + "|" +
                        t_main.orderDate + "|" +
                        t_main.FPaymentDate + "|" +
                        t_main.supplierId + "|" +
                        t_main.saleWayId + "|" +
                        t_main.FDepartmentId + "|" +
                        t_main.FSalesManId + "|" +
                        t_main.FDirectorId + "|" +
                        t_main.FCustody + "|" +
                        t_main.supplier + "|";
                break;
            case Config.PushDownPOActivity://采购订单下推外购入库
                io = WebApi.PUSHDOWNPOUPLOAD;
                string = t_main.FMakerId + "|" +
                        t_main.orderDate + "|" +
                        t_main.FPaymentDate + "|" +
                        t_main.supplierId + "|" +
                        t_main.saleWayId + "|" +
                        "1" + "|" +
                        t_main.FDepartmentId + "|" +
                        t_main.FSalesManId + "|" +
                        t_main.FDirectorId + "|" +
                        "0" + "|" +
                        t_main.supplier + "|";
                break;
        }

        return string;
    }
    //拼接明细的字符串
    private String getDetailString(T_Detail t_detail) {
        String string = "";
        switch (activity) {
            case Config.PurchaseOrderActivity:
                string = t_detail.FProductId + "|" + t_detail.FUnitId + "|" + t_detail.FTaxUnitPrice + "|" +
                        t_detail.FQuantity + "|" + t_detail.FDiscount + "|" + t_detail.FDateDelivery + "|";
                break;
            case Config.PurchaseInStorageActivity:
                string = t_detail.FProductId + "|" + t_detail.FUnitId + "|" + t_detail.FTaxUnitPrice + "|" +
                        t_detail.FQuantity + "|" + t_detail.FDiscount + "|" + t_detail.FStorageId + "|" +
                        t_detail.FBatch + "|" + t_detail.FPositionId + "|";
                break;
            case Config.ProductInStorageActivity:
                string = t_detail.FProductId + "|" + t_detail.FUnitId + "|" + t_detail.FTaxUnitPrice + "|" +
                        t_detail.FQuantity + "|" + t_detail.FDiscount + "|" + t_detail.FStorageId + "|" +
                        t_detail.FBatch + "|" + t_detail.FPositionId + "|";
                break;
            case Config.SaleOrderActivity:
                string = t_detail.FProductId + "|" + t_detail.FUnitId + "|" + t_detail.FTaxUnitPrice + "|" +
                        t_detail.FQuantity + "|" + t_detail.FDiscount + "|" + t_detail.FDateDelivery + "|";
                break;

            case Config.SoldOutActivity:
                string = t_detail.FProductId + "|" + t_detail.FUnitId + "|" + t_detail.FTaxUnitPrice + "|" +
                        t_detail.FQuantity + "|" + t_detail.FDiscount + "|" + t_detail.FStorageId + "|" +
                        t_detail.FBatch + "|" + t_detail.FPositionId + "|";
                break;
            case Config.ProduceAndGetActivity:
                string = t_detail.FProductId + "|" + t_detail.FUnitId + "|" + t_detail.FTaxUnitPrice + "|" +
                        t_detail.FQuantity + "|" + t_detail.FDiscount + "|" + t_detail.FStorageId + "|" +
                        t_detail.FBatch + "|" + t_detail.FPositionId + "|";
                break;
            case Config.OtherOutStoreActivity:
                string = t_detail.FProductId + "|" + t_detail.FUnitId + "|" + t_detail.FTaxUnitPrice + "|" +
                        t_detail.FQuantity + "|" + t_detail.FDiscount + "|" + t_detail.FStorageId + "|" +
                        t_detail.FBatch + "|" + t_detail.FPositionId + "|";
                break;
            case Config.OtherInStoreActivity:
                string = t_detail.FProductId + "|" + t_detail.FUnitId + "|" + t_detail.FTaxUnitPrice + "|" +
                        t_detail.FQuantity + "|" + t_detail.FDiscount + "|" + t_detail.FStorageId + "|" +
                        t_detail.FBatch + "|" + t_detail.FPositionId + "|";
                break;
            case Config.DBActivity:
                string = t_detail.FProductId + "|" + t_detail.FUnitId + "|" + t_detail.FQuantity + "|" +
                        t_detail.FStorageId + "|" + t_detail.FoutStorageid + "|" + t_detail.FPositionId + "|" +
                        t_detail.Foutwavehouseid + "|" + t_detail.FBatch + "|";
                break;
/*
                //下推单据---------------------------------------------------------------------
*/
            case Config.FHTZDDBActivity://发货通知生成调拨单     出
                string = t_detail.FProductId + "|" +
                        t_detail.FUnitId + "|" +
                        t_detail.FTaxUnitPrice + "|" +
                        t_detail.FQuantity + "|" +
                        t_detail.FStorageId + "|" +
                        t_detail.FPositionId + "|" +
                        t_detail.FEntryID + "|" +
                        t_detail.FInterID + "|" +
                        t_detail.FBatch + "|" +
                        t_detail.FoutStorageid + "|" +
                        t_detail.Foutwavehouseid + "|";
                break;
            case Config.HBDPDCPRKActivity://汇报单下推产品入库
                string = t_detail.FProductId + "|" +
                        t_detail.FUnitId + "|" +
                        t_detail.FTaxUnitPrice + "|" +
                        t_detail.FQuantity + "|" +
                        t_detail.FStorageId + "|" +
                        t_detail.FPositionId + "|" +
                        t_detail.FEntryID + "|" +
                        t_detail.FInterID + "|" +
                        t_detail.FBatch + "|";
                break;
            case Config.SCRWDPDSCHBDActivity://生产任务单下推生生产汇报单
                string = t_detail.FProductId + "|" + t_detail.FUnitId + "|" + t_detail.FTaxUnitPrice + "|" +
                        t_detail.FQuantity + "|" + t_detail.FStorageId + "|" + t_detail.FPositionId + "|" + t_detail.FEntryID +
                        "|" + t_detail.FInterID + "|" + t_detail.FBatch + "|";
                break;
            case Config.XSDDPDFLTZDActivity://销售订单下推发料通知单
                string = t_detail.FProductId + "|" + t_detail.FUnitId + "|" + t_detail.FTaxUnitPrice + "|" +
                        t_detail.FQuantity + "|" + t_detail.FStorageId + "|" + t_detail.FPositionId + "|" + t_detail.FEntryID +
                        "|" + t_detail.FInterID + "|" + t_detail.FBatch + "|";
                break;
            case Config.CGDDPDSLTZDActivity://采购订单下推收料通知单
                string = t_detail.FProductId + "|" +
                        t_detail.FUnitId + "|" +
                        t_detail.FTaxUnitPrice + "|" +
                        t_detail.FQuantity + "|" +
                        t_detail.FStorageId + "|" +
                        t_detail.FPositionId + "|" +
                        t_detail.FEntryID + "|" +
                        t_detail.FInterID + "|" +
                        t_detail.FBatch + "|";
                break;
            case Config.ShengchanrenwudanxiatuilingliaoActivity://生产任务单下推生产领料  出
                string = t_detail.FProductId + "|" +
                        t_detail.FUnitId + "|" +
                        t_detail.FTaxUnitPrice + "|" +
                        t_detail.FQuantity + "|" +
                        t_detail.FStorageId + "|" +
                        t_detail.FPositionId + "|" +
                        t_detail.FEntryID + "|" +
                        t_detail.FInterID + "|" +
                        t_detail.FBatch + "|";
                break;
            case Config.ProducePushInStoreActivity://生产任务单下推产品入库
                string = t_detail.FProductId + "|" +
                        t_detail.FUnitId + "|" +
                        t_detail.FTaxUnitPrice + "|" +
                        t_detail.FQuantity + "|" +
                        t_detail.FStorageId + "|" +
                        t_detail.FPositionId + "|" +
                        t_detail.FEntryID + "|" +
                        t_detail.FInterID + "|" +
                        t_detail.FBatch + "|";
                break;
            case Config.OutsourcingOrdersOSActivity://委外订单下推委外出库
                string = t_detail.FProductId + "|" +
                        t_detail.FUnitId + "|" +
                        t_detail.FTaxUnitPrice + "|" +
                        t_detail.FQuantity + "|" +
                        t_detail.FStorageId + "|" +
                        t_detail.FPositionId + "|" +
                        t_detail.FEntryID + "|" +
                        t_detail.FInterID + "|" +
                        t_detail.FBatch + "|";
                break;
            case Config.OutsourcingOrdersISActivity://委外订单下推委外入库
                string = t_detail.FProductId + "|" +
                        t_detail.FUnitId + "|" +
                        t_detail.FTaxUnitPrice + "|" +
                        t_detail.FQuantity + "|" +
                        t_detail.FStorageId + "|" +
                        t_detail.FPositionId + "|" +
                        t_detail.FEntryID + "|" +
                        t_detail.FInterID + "|" +
                        t_detail.FBatch + "|";
                break;

            case Config.ShouLiaoTongZhiActivity://收料通知下推外购入库
                string = t_detail.FProductId + "|" +
                        t_detail.FUnitId + "|" +
                        t_detail.FTaxUnitPrice + "|" +
                        t_detail.FQuantity + "|" +
                        t_detail.FStorageId + "|" +
                        t_detail.FPositionId + "|" +
                        t_detail.FEntryID + "|" +
                        t_detail.FInterID + "|" +
                        t_detail.FBatch + "|";
                break;
            case Config.PushDownSNActivity://发货通知下推销售出库
                string = t_detail.FProductId + "|" +
                        t_detail.FUnitId + "|" +
                        t_detail.FTaxUnitPrice + "|" +
                        t_detail.FQuantity + "|" +
                        t_detail.FStorageId + "|" +
                        t_detail.FPositionId + "|" +
                        t_detail.FEntryID + "|" +
                        t_detail.FInterID + "|" +
                        t_detail.FBatch + "|";
                break;
            case Config.PushDownMTActivity://销售订单下推销售出库
                string = t_detail.FProductId + "|" + t_detail.FUnitId + "|" + t_detail.FTaxUnitPrice + "|" +
                        t_detail.FQuantity + "|" + t_detail.FStorageId + "|" + t_detail.FPositionId + "|" +
                        t_detail.FEntryID + "|" + t_detail.FInterID + "|" + t_detail.FBatch + "|";
                break;
            case Config.PushDownPOActivity://采购订单下推外购入库
                string = t_detail.FProductId + "|" + t_detail.FUnitId + "|" + t_detail.FTaxUnitPrice + "|" +
                        t_detail.FQuantity + "|" + t_detail.FStorageId + "|" + t_detail.FPositionId + "|" +
                        t_detail.FEntryID + "|" + t_detail.FInterID + "|" + t_detail.FBatch + "|";
                break;


        }

        return string;
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void OnReceive(String code) {

    }

    //非下推单据
    public static void start(Context context, int activity) {
        Intent intent = new Intent(context, UpLoadActivity.class);
        intent.putExtra("activity", activity);
//        intent.putStringArrayListExtra("fid", fid);
        context.startActivity(intent);
    }

    //下推时
    public static void start(Context context, int isPushDown, int activity) {
        Intent intent = new Intent(context, UpLoadActivity.class);
        intent.putExtra("isPushDown", isPushDown);
        intent.putExtra("activity", activity);
//        intent.putStringArrayListExtra("fid", fid);
        context.startActivity(intent);
    }

    @OnClick({R.id.view_cancle, R.id.btn_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.view_cancle:
                break;
            case R.id.btn_back:
                if (isPushDown != 0) {
                    finish();
                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Close_Activity, ""));
                } else {
                    finish();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isPushDown != 0) {
            finish();
            EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Close_Activity, ""));
        } else {
            finish();
        }
    }
}

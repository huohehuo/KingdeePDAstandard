package com.fangzuo.assist.MVContract.presenter;

import android.content.Context;

import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
//import com.fangzuo.assist.Dao.Company;
import com.fangzuo.assist.Dao.Department;
import com.fangzuo.assist.Dao.Employee;
import com.fangzuo.assist.Dao.PayType;
//import com.fangzuo.assist.Dao.ProductLongType;
import com.fangzuo.assist.Dao.PurchaseMethod;
import com.fangzuo.assist.Dao.Storage;
import com.fangzuo.assist.MVContract.base.IPresenter;
import com.fangzuo.assist.MVContract.view.DataView;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.JsonCreater;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.greendao.gen.DaoSession;
import com.google.gson.Gson;

import java.util.ArrayList;


public class DataPresenter implements IPresenter {

    private Context mContext;
    private DataView view;
    private Context context;
    private ArrayList<Integer> choose;
    private static BasicShareUtil share;
    private DaoSession daoSession;
    private Gson gson;
    private DownloadReturnBean downloadReturnBean;

    public DataPresenter(Context mContext, DataView view){
        this.view = view;
        this.context = mContext;
        choose = new ArrayList<>();
        share = BasicShareUtil.getInstance(context);
        daoSession = GreenDaoManager.getmInstance(context).getDaoSession();
        gson = new Gson();
        downloadReturnBean = new DownloadReturnBean();
    }
    @Override
    public void onCreate() {

    }

    //根据type类型查找相应的表数据
    public void getData(final int type){
        choose.clear();
        if(share.getIsOL()){
            choose.add(type);
            String json = JsonCreater.DownLoadDataWithIp(share,choose);
            App.getRService().downloadData(json, new MySubscribe<CommonResponse>() {
                @Override
                public void onNext(CommonResponse commonResponse) {
                    super.onNext(commonResponse);
                    view.getBackData(commonResponse, type);
                }
                @Override
                public void onError(Throwable e) {
                    view.showError(e.toString(),type);
                }
            });
        }else{
            dealLocationData(type);
        }
    }

    //根据类型，查找出本地数据(当前为查找所有，若要筛选，请移步activity中)
    private void dealLocationData(int type){
        Lg.e("本地数据");
        if (type == Config.Data_Storage){//仓库
            CommonResponse commonResponse = new CommonResponse();
            ArrayList<Storage> arrayList = new ArrayList<>();
            arrayList.addAll(daoSession.getStorageDao().loadAll());
            downloadReturnBean.storage=arrayList;
            //此处为了让返回的数据类型无论是在线或离线，activity端收到的类型统一
            commonResponse.returnJson = gson.toJson(downloadReturnBean);
            view.getBackData(commonResponse, Config.Data_Storage);
        }else if (type== Config.Data_PurchaseMethod){//销售/采购方式表
            CommonResponse commonResponse = new CommonResponse();
            ArrayList<PurchaseMethod> arrayList = new ArrayList<>();
            arrayList.addAll(daoSession.getPurchaseMethodDao().loadAll());
            downloadReturnBean.purchaseMethod=arrayList;
            commonResponse.returnJson = gson.toJson(downloadReturnBean);
            view.getBackData(commonResponse, Config.Data_PurchaseMethod);
        }else if (type== Config.Data_Company){//公司信息表
//            CommonResponse commonResponse = new CommonResponse();
//            ArrayList<Company> arrayList = new ArrayList<>();
//            arrayList.addAll(daoSession.getCompanyDao().loadAll());
//            downloadReturnBean.companies=arrayList;
//            commonResponse.returnJson = gson.toJson(downloadReturnBean);
//            view.getBackData(commonResponse, Config.Data_Company);
        }else if (type== Config.Data_PayTypes){//结算方式表
            CommonResponse commonResponse = new CommonResponse();
            ArrayList<PayType> arrayList = new ArrayList<>();
            arrayList.addAll(daoSession.getPayTypeDao().loadAll());
            downloadReturnBean.payTypes=arrayList;
            commonResponse.returnJson = gson.toJson(downloadReturnBean);
            view.getBackData(commonResponse, Config.Data_PayTypes);
        }else if (type== Config.Data_Department){//部门表
            CommonResponse commonResponse = new CommonResponse();
            ArrayList<Department> arrayList = new ArrayList<>();
            arrayList.addAll(daoSession.getDepartmentDao().loadAll());
            downloadReturnBean.department=arrayList;
            commonResponse.returnJson = gson.toJson(downloadReturnBean);
            view.getBackData(commonResponse, Config.Data_Department);
        }else if (type== Config.Data_Employee){//职员表
            CommonResponse commonResponse = new CommonResponse();
            ArrayList<Employee> arrayList = new ArrayList<>();
            arrayList.addAll(daoSession.getEmployeeDao().loadAll());
            downloadReturnBean.employee=arrayList;
            commonResponse.returnJson = gson.toJson(downloadReturnBean);
            view.getBackData(commonResponse, Config.Data_Employee);
        }else if (type== Config.Data_Product_Type){//长材物料类别表
//            CommonResponse commonResponse = new CommonResponse();
//            ArrayList<ProductLongType> arrayList = new ArrayList<>();
//            arrayList.addAll(daoSession.getProductLongTypeDao().loadAll());
//            downloadReturnBean.productLongTypes=arrayList;
//            commonResponse.returnJson = gson.toJson(downloadReturnBean);
//            view.getBackData(commonResponse, Config.Data_Product_Type);
        }
    }



}

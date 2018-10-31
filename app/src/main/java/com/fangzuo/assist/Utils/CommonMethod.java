package com.fangzuo.assist.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.Adapter;
import android.widget.Spinner;

import com.fangzuo.assist.Adapter.ClientSpAdapter;
import com.fangzuo.assist.Adapter.DepartmentSpAdapter;
import com.fangzuo.assist.Adapter.EmployeeSpAdapter;
import com.fangzuo.assist.Adapter.InStoreTypeSpAdapter;
import com.fangzuo.assist.Adapter.PDMainSpAdapter;
import com.fangzuo.assist.Adapter.PayMethodSpAdapter;
import com.fangzuo.assist.Adapter.PayTypeSpAdapter;
import com.fangzuo.assist.Adapter.PiciSpAdapter;
import com.fangzuo.assist.Adapter.PiciSpForSubAdapter;
import com.fangzuo.assist.Adapter.PurchaseMethodSpAdapter;
import com.fangzuo.assist.Adapter.PurchaseScopeSpAdapter;
import com.fangzuo.assist.Adapter.StorageSpAdapter;
import com.fangzuo.assist.Adapter.SupplierSpAdapter;
import com.fangzuo.assist.Adapter.UnitSpAdapter;
import com.fangzuo.assist.Adapter.WLKMSpAdapter;
import com.fangzuo.assist.Adapter.WaveHouseSpAdapter;
import com.fangzuo.assist.Adapter.YuandanSpAdapter;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Dao.Client;
import com.fangzuo.assist.Dao.Department;
import com.fangzuo.assist.Dao.Employee;
import com.fangzuo.assist.Dao.InStorageNum;
import com.fangzuo.assist.Dao.InStoreType;
import com.fangzuo.assist.Dao.PDMain;
import com.fangzuo.assist.Dao.PDSub;
import com.fangzuo.assist.Dao.PayType;
import com.fangzuo.assist.Dao.Product;
import com.fangzuo.assist.Dao.PurchaseMethod;
import com.fangzuo.assist.Dao.Storage;
import com.fangzuo.assist.Dao.Suppliers;
import com.fangzuo.assist.Dao.Unit;
import com.fangzuo.assist.Dao.Wanglaikemu;
import com.fangzuo.assist.Dao.WaveHouse;
import com.fangzuo.assist.Dao.YuandanType;
import com.fangzuo.assist.Server.WebAPI;
import com.fangzuo.greendao.gen.ClientDao;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.DepartmentDao;
import com.fangzuo.greendao.gen.EmployeeDao;
import com.fangzuo.greendao.gen.GetGoodsDepartmentDao;
import com.fangzuo.greendao.gen.InStorageNumDao;
import com.fangzuo.greendao.gen.InStoreTypeDao;
import com.fangzuo.greendao.gen.PDMainDao;
import com.fangzuo.greendao.gen.PDSubDao;
import com.fangzuo.greendao.gen.PayTypeDao;
import com.fangzuo.greendao.gen.PurchaseMethodDao;
import com.fangzuo.greendao.gen.StorageDao;
import com.fangzuo.greendao.gen.SuppliersDao;
import com.fangzuo.greendao.gen.UnitDao;
import com.fangzuo.greendao.gen.WanglaikemuDao;
import com.fangzuo.greendao.gen.WaveHouseDao;
import com.fangzuo.greendao.gen.YuandanTypeDao;
import com.loopj.android.http.AsyncHttpClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NB on 2017/8/8.
 */

public class CommonMethod {

    public static CommonMethod commonMethod=null;
    Context context;
    private final DaoSession daoSession;
    private static BasicShareUtil share;

    public static CommonMethod getMethod(Context context){
        if(commonMethod==null){
            commonMethod = new CommonMethod(context);
        }
        return commonMethod;
    }
    public CommonMethod(Context context) {
        this.context = context;
        share = BasicShareUtil.getInstance(context);
        daoSession = GreenDaoManager.getmInstance(context).getDaoSession();
    }

    //仓库Spinner
    public StorageSpAdapter getStorageSpinner(Spinner sp){
        final ArrayList<Storage> container = new ArrayList<>();
        final StorageSpAdapter storageSpAdapter = new StorageSpAdapter(context, container);
        sp.setAdapter(storageSpAdapter);
        if(share.getIsOL()){
            Log.e("CommonMethod:","getStorageSpinner联网");
            ArrayList<Integer> choose = new ArrayList<>();
            choose.add(6);
            String json = JsonCreater.DownLoadData(
                    share.getDatabaseIp(),
                    share.getDatabasePort(),
                    share.getDataBaseUser(),
                    share.getDataBasePass(),
                    share.getDataBase(),
                    share.getVersion(),
                    choose
            );
            Asynchttp.post(context,share.getBaseURL()+WebApi.DOWNLOADDATA, json, new Asynchttp.Response() {
                @Override
                public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                    Log.e("CommonMethod:","getStorageSpinner获得联网数据：\n"+cBean.returnJson);
                    DownloadReturnBean dBean = JsonCreater.gson.fromJson(cBean.returnJson, DownloadReturnBean.class);
                    container.addAll(dBean.storage);
                    storageSpAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailed(String Msg, AsyncHttpClient client) {
                    Toast.showText(context, Msg);
                }
            });
//            RetrofitUtil.getInstance(context).createReq(WebAPI.class).
//                    downloadData(RetrofitUtil.getParams(context,json)).enqueue(new CallBack() {
//                @Override
//                public void onSucceed(CommonResponse cBean) {
//                    Log.e("CommonMethod:","getStorageSpinner获得数据：\n"+cBean.returnJson);
//                    DownloadReturnBean dBean = JsonCreater.gson.fromJson(cBean.returnJson, DownloadReturnBean.class);
//                    container.addAll(dBean.storage);
//                    storageSpAdapter.notifyDataSetChanged();
//                }
//
//                @Override
//                public void OnFail(String Msg) {
//                    Toast.showText(context, Msg);
//                }
//            });
        }else{
            Log.e("CommonMethod:","getStorageSpinner不-联网");
            StorageDao storageDao = daoSession.getStorageDao();
            List<Storage> storages = storageDao.loadAll();
            container.addAll(storages);
            Log.e("CommonMethod","获取到本地数据：\n"+container.toString());
            storageSpAdapter.notifyDataSetChanged();
        }
        return storageSpAdapter;
    }

    public PDMainSpAdapter getpdmain(Spinner sp){
        PDMainDao pdMainDao = daoSession.getPDMainDao();
        List<PDMain> mains = pdMainDao.loadAll();
        Log.e("PDMain",mains.size()+"");
        PDMainSpAdapter pdMainSpAdapter = new PDMainSpAdapter(context,mains);
        sp.setAdapter(pdMainSpAdapter);
        pdMainSpAdapter.notifyDataSetChanged();
        return pdMainSpAdapter;
    }

    //
    public PayMethodSpAdapter getSaleMethodSpinner(Spinner sp){
        final ArrayList<PurchaseMethod> container = new ArrayList<>();
        final PayMethodSpAdapter payMethodSpAdapter = new PayMethodSpAdapter(context, container);
        sp.setAdapter(payMethodSpAdapter);
        if(share.getIsOL()){
            ArrayList<Integer> choose = new ArrayList<>();
            choose.add(15);
            String json = JsonCreater.DownLoadData(
                    share.getDatabaseIp(),
                    share.getDatabasePort(),
                    share.getDataBaseUser(),
                    share.getDataBasePass(),
                    share.getDataBase(),
                    share.getVersion(),
                    choose
            );
            Asynchttp.post(context,share.getBaseURL()+WebApi.DOWNLOADDATA, json, new Asynchttp.Response() {
                @Override
                public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                    Log.e("CommonMethod:","getSaleMethodSpinner获得联网数据：\n"+cBean.returnJson);
                    DownloadReturnBean dBean = JsonCreater.gson.fromJson(cBean.returnJson, DownloadReturnBean.class);
                    PurchaseMethodDao purchaseMethodDao = daoSession.getPurchaseMethodDao();
                    purchaseMethodDao.deleteAll();
                    purchaseMethodDao.insertOrReplaceInTx(dBean.purchaseMethod);
                    purchaseMethodDao.detachAll();
                    //过滤指定参数
                    for (int i=0;i<dBean.purchaseMethod.size();i++){
                        if (dBean.purchaseMethod.get(i).FTypeID.contains("101")
                                && !dBean.purchaseMethod.get(i).FNumber.equals("02")){
                            container.add(dBean.purchaseMethod.get(i));
                        }
                    }
                    payMethodSpAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailed(String Msg, AsyncHttpClient client) {
                    Toast.showText(context, Msg);
                }
            });
        }else{
            PurchaseMethodDao purchaseMethodDao = daoSession.getPurchaseMethodDao();
            List<PurchaseMethod> purchaseMethods = purchaseMethodDao.queryBuilder().
                    where(PurchaseMethodDao.Properties.FTypeID.eq("101"),
                            PurchaseMethodDao.Properties.FNumber.notEq("02"))
//                    .orderAsc(PurchaseMethodDao.Properties.FNumber)
                    .build().list();
            container.addAll(purchaseMethods);
            payMethodSpAdapter.notifyDataSetChanged();
        }

        return payMethodSpAdapter;
    }
    //采购方式
    public PayMethodSpAdapter getPayMethodSpinner(Spinner sp){
        final ArrayList<PurchaseMethod> container = new ArrayList<>();
        final PayMethodSpAdapter payMethodSpAdapter = new PayMethodSpAdapter(context, container);
        sp.setAdapter(payMethodSpAdapter);
        if(share.getIsOL()){
            ArrayList<Integer> choose = new ArrayList<>();
            choose.add(15);
            String json = JsonCreater.DownLoadData(
                    share.getDatabaseIp(),
                    share.getDatabasePort(),
                    share.getDataBaseUser(),
                    share.getDataBasePass(),
                    share.getDataBase(),
                    share.getVersion(),choose);
            Asynchttp.post(context,share.getBaseURL()+WebApi.DOWNLOADDATA, json, new Asynchttp.Response() {
                @Override
                public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                    DownloadReturnBean dBean = JsonCreater.gson.fromJson(cBean.returnJson, DownloadReturnBean.class);
                    //过滤指定参数
                    for (int i=0;i<dBean.purchaseMethod.size();i++){
                        if (dBean.purchaseMethod.get(i).FTypeID.contains("162")
                                && !dBean.purchaseMethod.get(i).FNumber.equals("02")){
                            container.add(dBean.purchaseMethod.get(i));
                        }
                    }
//                    container.addAll(dBean.purchaseMethod);
                    payMethodSpAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailed(String Msg, AsyncHttpClient client) {
                    Toast.showText(context, Msg);
                }
            });
        }else{
            PurchaseMethodDao purchaseMethodDao = daoSession.getPurchaseMethodDao();
            List<PurchaseMethod> purchaseMethods = purchaseMethodDao.queryBuilder().
                    where(PurchaseMethodDao.Properties.FTypeID.eq("162"),
                            PurchaseMethodDao.Properties.FNumber.notEq("02"))
//                    .orderAsc(PurchaseMethodDao.Properties.FNumber)
                    .build().list();
            container.addAll(purchaseMethods);
            payMethodSpAdapter.notifyDataSetChanged();
        }
        return payMethodSpAdapter;
    }

    //采购方式
    public PurchaseMethodSpAdapter getPurchaseMethodSpinner(Spinner sp){
        final ArrayList<PurchaseMethod> container = new ArrayList<>();
        final PurchaseMethodSpAdapter payMethodSpAdapter = new PurchaseMethodSpAdapter(context, container);
        sp.setAdapter(payMethodSpAdapter);
        if(share.getIsOL()){
            ArrayList<Integer> choose = new ArrayList<>();
            choose.add(15);
            String json = JsonCreater.DownLoadData(share.getDatabaseIp(),
                    share.getDatabasePort(), share.getDataBaseUser(), share.getDataBasePass(),
                    share.getDataBase(), share.getVersion(),choose);
            Asynchttp.post(context,share.getBaseURL()+WebApi.DOWNLOADDATA, json, new Asynchttp.Response() {
                @Override
                public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                    DownloadReturnBean dBean = JsonCreater.gson.fromJson(cBean.returnJson, DownloadReturnBean.class);

                    //过滤指定参数
                    for (int i=0;i<dBean.purchaseMethod.size();i++){
                        if (dBean.purchaseMethod.get(i).FTypeID.contains("162")
                                && !dBean.purchaseMethod.get(i).FNumber.equals("02")){
                            container.add(dBean.purchaseMethod.get(i));
                        }
                    }
//                    container.addAll(dBean.purchaseMethod);
                    Log.e("eeeee",container.toString());
                    payMethodSpAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailed(String Msg, AsyncHttpClient client) {
                    Toast.showText(context, Msg);
                }
            });
        }else{
            PurchaseMethodDao purchaseMethodDao = daoSession.getPurchaseMethodDao();
            List<PurchaseMethod> purchaseMethods = purchaseMethodDao.queryBuilder().
                    where(PurchaseMethodDao.Properties.FTypeID.eq("162"),
                            PurchaseMethodDao.Properties.FNumber.notEq("02"))
//                    .orderAsc(PurchaseMethodDao.Properties.FNumber)
                    .build().list();
            Log.e("eeeee",purchaseMethods.toString());
            container.addAll(purchaseMethods);
            payMethodSpAdapter.notifyDataSetChanged();
        }
        return payMethodSpAdapter;
    }

    public PayMethodSpAdapter getweiwaiSpinner(Spinner sp){
        final ArrayList<PurchaseMethod> container = new ArrayList<>();
        final PayMethodSpAdapter payMethodSpAdapter = new PayMethodSpAdapter(context, container);
        sp.setAdapter(payMethodSpAdapter);
        if(share.getIsOL()){
            ArrayList<Integer> choose = new ArrayList<>();
            choose.add(15);
            String json = JsonCreater.DownLoadData(share.getDatabaseIp(),
                    share.getDatabasePort(), share.getDataBaseUser(), share.getDataBasePass(),
                    share.getDataBase(), share.getVersion(),choose);
            Asynchttp.post(context,share.getBaseURL()+WebApi.DOWNLOADDATA, json, new Asynchttp.Response() {
                @Override
                public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                    DownloadReturnBean dBean = JsonCreater.gson.fromJson(cBean.returnJson, DownloadReturnBean.class);
                    container.addAll(dBean.purchaseMethod);
                    payMethodSpAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailed(String Msg, AsyncHttpClient client) {
                    Toast.showText(context, Msg);
                }
            });
        }else{
            PurchaseMethodDao purchaseMethodDao = daoSession.getPurchaseMethodDao();
            List<PurchaseMethod> purchaseMethods = purchaseMethodDao.queryBuilder().
                    where(PurchaseMethodDao.Properties.FTypeID.eq("632")).orderAsc(PurchaseMethodDao.Properties.FNumber)
                    .build().list();
            container.addAll(purchaseMethods);
            payMethodSpAdapter.notifyDataSetChanged();
        }
        return payMethodSpAdapter;
    }


    public EmployeeSpAdapter getEmployeeAdapter(Spinner sp){
        final ArrayList<Employee> container = new ArrayList<>();
        final EmployeeSpAdapter employeeSpAdapter = new EmployeeSpAdapter(context, container);
        sp.setAdapter(employeeSpAdapter);
        if(share.getIsOL()){
            ArrayList<Integer> choose = new ArrayList<>();
            choose.add(3);
            String json = JsonCreater.DownLoadData(
                    share.getDatabaseIp(),
                    share.getDatabasePort(),
                    share.getDataBaseUser(),
                    share.getDataBasePass(),
                    share.getDataBase(),
                    share.getVersion(),
                    choose
            );
            Asynchttp.post(context,share.getBaseURL()+WebApi.DOWNLOADDATA, json, new Asynchttp.Response() {
                @Override
                public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                    DownloadReturnBean dBean = JsonCreater.gson.fromJson(cBean.returnJson, DownloadReturnBean.class);
                    EmployeeDao employeeDao = daoSession.getEmployeeDao();
                    container.addAll(dBean.employee);
                    employeeSpAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailed(String Msg, AsyncHttpClient client) {
                    Toast.showText(context, Msg);
                }
            });
        }else{
            EmployeeDao employeeDao = daoSession.getEmployeeDao();
            List<Employee> employees = employeeDao.loadAll();
            container.addAll(employees);
            employeeSpAdapter.notifyDataSetChanged();
        }
        return employeeSpAdapter;
    }

    public DepartmentSpAdapter getDepartMentAdapter(Spinner sp){
        final ArrayList<Department> container = new ArrayList<>();
        final DepartmentSpAdapter departmentSpAdapter = new DepartmentSpAdapter(context, container);
        sp.setAdapter(departmentSpAdapter);
        if(share.getIsOL()){
            ArrayList<Integer> choose = new ArrayList<>();
            choose.add(2);
            String json = JsonCreater.DownLoadData(share.getDatabaseIp(),
                    share.getDatabasePort(), share.getDataBaseUser(), share.getDataBasePass(),
                    share.getDataBase(), share.getVersion(),choose);
            Asynchttp.post(context,share.getBaseURL()+WebApi.DOWNLOADDATA, json, new Asynchttp.Response() {
                @Override
                public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                    DownloadReturnBean dBean = JsonCreater.gson.fromJson(cBean.returnJson, DownloadReturnBean.class);
                    container.addAll(dBean.department);
                    departmentSpAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailed(String Msg, AsyncHttpClient client) {
                    Toast.showText(context, Msg);
                }
            });
        }else{
            DepartmentDao departmentDao = daoSession.getDepartmentDao();
            List<Department> departments = departmentDao.loadAll();
            Log.e("departmentsize",departments.size()+"");
            container.addAll(departments);
            departmentSpAdapter.notifyDataSetChanged();
        }
        return departmentSpAdapter;
    }

    public List<Wanglaikemu> getwlkmAdapter(Spinner sp){
        final ArrayList<Wanglaikemu> container = new ArrayList<>();
        final WLKMSpAdapter wlkmSpAdapter = new WLKMSpAdapter(context, container);
        sp.setAdapter(wlkmSpAdapter);
        if(share.getIsOL()){
            ArrayList<Integer> choose = new ArrayList<>();
            choose.add(17);
            String json = JsonCreater.DownLoadData(share.getDatabaseIp(),
                    share.getDatabasePort(), share.getDataBaseUser(), share.getDataBasePass(),
                    share.getDataBase(), share.getVersion(),choose);
            Asynchttp.post(context,share.getBaseURL()+WebApi.DOWNLOADDATA, json, new Asynchttp.Response() {
                @Override
                public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                    DownloadReturnBean dBean = JsonCreater.gson.fromJson(cBean.returnJson, DownloadReturnBean.class);
                    container.addAll(dBean.wanglaikemu);
                    wlkmSpAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailed(String Msg, AsyncHttpClient client) {
                    Toast.showText(context, Msg);
                }
            });
        }else{
            WanglaikemuDao wanglaikemuDao = daoSession.getWanglaikemuDao();
            List<Wanglaikemu> wanglaikemus = wanglaikemuDao.loadAll();
            container.addAll(wanglaikemus);
            wlkmSpAdapter.notifyDataSetChanged();
        }

        return container;
    }

    //设置仓位信息（根据仓位）
    public WaveHouseSpAdapter getWaveHouseAdapter(final Storage storage, Spinner sp){
        final ArrayList<WaveHouse> waveHouses = new ArrayList<>();
        final WaveHouseSpAdapter waveHouseSpAdapter = new WaveHouseSpAdapter(context, waveHouses);
        sp.setAdapter(waveHouseSpAdapter);
        if(share.getIsOL()){
            Log.e("CommonMethod:","getWaveHouseAdapter联网");
            ArrayList<Integer> choose = new ArrayList<>();
            choose.add(4);
            String json = JsonCreater.DownLoadData(
                    share.getDatabaseIp(),
                    share.getDatabasePort(),
                    share.getDataBaseUser(),
                    share.getDataBasePass(),
                    share.getDataBase(),
                    share.getVersion(),
                    choose
            );
            Asynchttp.post(context,share.getBaseURL()+WebApi.DOWNLOADDATA, json, new Asynchttp.Response() {
                @Override
                public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                    Log.e("CommonMethod:","getWaveHouseAdapter获得数据：\n"+cBean.returnJson);

                    DownloadReturnBean dBean = JsonCreater.gson.fromJson(cBean.returnJson, DownloadReturnBean.class);
                    WaveHouseDao wavehouseDao = daoSession.getWaveHouseDao();
                    wavehouseDao.deleteAll();
                    for (int i = 0; i < dBean.wavehouse.size(); i++) {
                        if (storage.FSPGroupID.equals(dBean.wavehouse.get(i).FSPGroupID)){
                            waveHouses.add(dBean.wavehouse.get(i));
                        }
                    }
//                    waveHouses.addAll(dBean.wavehouse);
                    waveHouseSpAdapter.notifyDataSetChanged();
                    wavehouseDao.insertOrReplaceInTx(dBean.wavehouse);
                    wavehouseDao.detachAll();
                }

                @Override
                public void onFailed(String Msg, AsyncHttpClient client) {
                    Toast.showText(context, Msg);
                }
            });
        }else{
            Log.e("CommonMethod:","getWaveHouseAdapter-不-联网");
            WaveHouseDao wavehousedao = daoSession.getWaveHouseDao();
            List<WaveHouse> waveHouseList = wavehousedao.queryBuilder().where(
                WaveHouseDao.Properties.FSPGroupID.eq(storage.FSPGroupID)
            ).build().list();
//            waveHouseList.add(new WaveHouse());
            if (waveHouseList.size()>0){
                waveHouses.addAll(waveHouseList);
               Log.e("CommonMethod:","获取到本地数据waveHouse:"+waveHouseList.toString());
                waveHouseSpAdapter.notifyDataSetChanged();
            }else{
                Log.e("CommonMethod:","无数据：waveHouse:");

            }
        }
        return waveHouseSpAdapter;
    }

    public UnitSpAdapter getUnitAdapter(String groupid,Spinner sp){
        Log.e("groupid",groupid);
        if(share.getIsOL()){
            ArrayList<Integer> choose = new ArrayList<>();
            choose.add(7);
            String json = JsonCreater.DownLoadData(
                    share.getDatabaseIp(),
                    share.getDatabasePort(),
                    share.getDataBaseUser(),
                    share.getDataBasePass(),
                    share.getDataBase(),
                    share.getVersion(),
                    choose
            );
            Asynchttp.post(context,share.getBaseURL()+WebApi.DOWNLOADDATA, json, new Asynchttp.Response() {
                @Override
                public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                    Log.e("CommonMethod:","联网获取单位："+cBean.returnJson);
                    DownloadReturnBean dBean = JsonCreater.gson.fromJson(cBean.returnJson, DownloadReturnBean.class);
                    if(dBean!=null&&dBean.units!=null&&dBean.units.size()>0){
                        UnitDao unitDao = daoSession.getUnitDao();
                        unitDao.deleteAll();
                        unitDao.insertOrReplaceInTx(dBean.units);
                        unitDao.detachAll();
                    }

                }

                @Override
                public void onFailed(String Msg, AsyncHttpClient client) {
                    Toast.showText(context, Msg);
                }
            });
        }
        UnitDao unitDao = daoSession.getUnitDao();
        List<Unit> units = unitDao.queryBuilder().where(
                UnitDao.Properties.FUnitGroupID.eq(groupid)
        ).build().list();
        Log.e("CommonMethod:","本地获取单位："+units.toString());
        UnitSpAdapter unitSpAdapter = new UnitSpAdapter(context, units);
        sp.setAdapter(unitSpAdapter);
        unitSpAdapter.notifyDataSetChanged();
        return unitSpAdapter;
    }

    public void getGoodsDepartmentSpAdapter(){
        if(share.getIsOL()){
            ArrayList<Integer> choose = new ArrayList<>();
            choose.add(14);
            String json = JsonCreater.DownLoadData(share.getDatabaseIp(),
                    share.getDatabasePort(), share.getDataBaseUser(), share.getDataBasePass(),
                    share.getDataBase(), share.getVersion(),choose);
            Asynchttp.post(context,share.getBaseURL()+WebApi.DOWNLOADDATA, json, new Asynchttp.Response() {
                @Override
                public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                    DownloadReturnBean dBean = JsonCreater.gson.fromJson(cBean.returnJson, DownloadReturnBean.class);
                    GetGoodsDepartmentDao getGoodsDepartmentDao = daoSession.getGetGoodsDepartmentDao();
                    getGoodsDepartmentDao.deleteAll();
                    getGoodsDepartmentDao.insertOrReplaceInTx(dBean.getGoodsDepartments);
                    getGoodsDepartmentDao.detachAll();
                }

                @Override
                public void onFailed(String Msg, AsyncHttpClient client) {
                    Toast.showText(context, Msg);
                }
            });
        }

    }

    public YuandanSpAdapter getyuandanSp(Spinner sp){
        if(share.getIsOL()){
            ArrayList<Integer> choose = new ArrayList<>();
            choose.add(16);
            String json = JsonCreater.DownLoadData(share.getDatabaseIp(),
                    share.getDatabasePort(), share.getDataBaseUser(), share.getDataBasePass(),
                    share.getDataBase(), share.getVersion(),choose);
            Asynchttp.post(context,share.getBaseURL()+WebApi.DOWNLOADDATA, json, new Asynchttp.Response() {
                @Override
                public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                    DownloadReturnBean dBean = JsonCreater.gson.fromJson(cBean.returnJson, DownloadReturnBean.class);
                    YuandanTypeDao yuandanTypeDao = daoSession.getYuandanTypeDao();
                    yuandanTypeDao.deleteAll();
                    yuandanTypeDao.insertOrReplaceInTx(dBean.yuandanTypes);
                    yuandanTypeDao.detachAll();
                }

                @Override
                public void onFailed(String Msg, AsyncHttpClient client) {
                    Toast.showText(context, Msg);
                }
            });
        }
        YuandanTypeDao yuandanTypeDao = daoSession.getYuandanTypeDao();
        List<YuandanType> yuandanTypes = yuandanTypeDao.loadAll();
        YuandanSpAdapter yuandanSpAdapter = new YuandanSpAdapter(context,yuandanTypes);
        sp.setAdapter(yuandanSpAdapter);
        yuandanSpAdapter.notifyDataSetChanged();
        return  yuandanSpAdapter;
    }

   public PayMethodSpAdapter getPurchaseRange(Spinner sp){
       if(share.getIsOL()){
           ArrayList<Integer> choose = new ArrayList<>();
           choose.add(15);
           String json = JsonCreater.DownLoadData(share.getDatabaseIp(),
                   share.getDatabasePort(), share.getDataBaseUser(), share.getDataBasePass(),
                   share.getDataBase(), share.getVersion(),choose);
           Asynchttp.post(context,share.getBaseURL()+WebApi.DOWNLOADDATA, json, new Asynchttp.Response() {
               @Override
               public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                   DownloadReturnBean dBean = JsonCreater.gson.fromJson(cBean.returnJson, DownloadReturnBean.class);
                   PurchaseMethodDao purchaseMethodDao = daoSession.getPurchaseMethodDao();
                   purchaseMethodDao.deleteAll();
                   purchaseMethodDao.insertOrReplaceInTx(dBean.purchaseMethod);
                   purchaseMethodDao.detachAll();
               }

               @Override
               public void onFailed(String Msg, AsyncHttpClient client) {
                   Toast.showText(context, Msg);
               }
           });
       }
       PurchaseMethodDao purchaseMethodDao = daoSession.getPurchaseMethodDao();
       List<PurchaseMethod> purchaseMethods = purchaseMethodDao.queryBuilder().
               where(PurchaseMethodDao.Properties.FTypeID.eq("997")).orderAsc(PurchaseMethodDao.Properties.FNumber)
               .build().list();
       PayMethodSpAdapter payMethodSpAdapter = new PayMethodSpAdapter(context, purchaseMethods);
       sp.setAdapter(payMethodSpAdapter);
       payMethodSpAdapter.notifyDataSetChanged();
       return payMethodSpAdapter;
   }

   //采购范围
    public PurchaseScopeSpAdapter getPurchaseScope(Spinner sp){
        final ArrayList<PurchaseMethod> container = new ArrayList<>();
        final PurchaseScopeSpAdapter payMethodSpAdapter = new PurchaseScopeSpAdapter(context, container);
        sp.setAdapter(payMethodSpAdapter);
        if(share.getIsOL()){
            ArrayList<Integer> choose = new ArrayList<>();
            choose.add(15);
            String json = JsonCreater.DownLoadData(share.getDatabaseIp(),
                    share.getDatabasePort(), share.getDataBaseUser(), share.getDataBasePass(),
                    share.getDataBase(), share.getVersion(),choose);
            Asynchttp.post(context,share.getBaseURL()+WebApi.DOWNLOADDATA, json, new Asynchttp.Response() {
                @Override
                public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                    DownloadReturnBean dBean = JsonCreater.gson.fromJson(cBean.returnJson, DownloadReturnBean.class);
                    //保存所有
                    PurchaseMethodDao purchaseMethodDao = daoSession.getPurchaseMethodDao();
                    purchaseMethodDao.deleteAll();
                    purchaseMethodDao.insertOrReplaceInTx(dBean.purchaseMethod);
                    purchaseMethodDao.detachAll();
                    //过滤指定参数
                    for (int i=0;i<dBean.purchaseMethod.size();i++){
                        if (dBean.purchaseMethod.get(i).FTypeID.contains("997")){
                            container.add(dBean.purchaseMethod.get(i));
                        }
                    }
//                    container.addAll(dBean.purchaseMethod);
                    payMethodSpAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailed(String Msg, AsyncHttpClient client) {
                    Toast.showText(context, Msg);
                }
            });
        }else{
            PurchaseMethodDao purchaseMethodDao = daoSession.getPurchaseMethodDao();
            List<PurchaseMethod> purchaseMethods = purchaseMethodDao.queryBuilder().
                    where(PurchaseMethodDao.Properties.FTypeID.eq("997"))
//                    .orderAsc(PurchaseMethodDao.Properties.FNumber)
                    .build().list();
            container.addAll(purchaseMethods);
            payMethodSpAdapter.notifyDataSetChanged();
        }

        return payMethodSpAdapter;
    }


   public PayTypeSpAdapter getpayType(Spinner sp){
       if(share.getIsOL()){
           ArrayList<Integer> choose = new ArrayList<>();
           choose.add(10);
           String json = JsonCreater.DownLoadData(share.getDatabaseIp(),
                   share.getDatabasePort(), share.getDataBaseUser(), share.getDataBasePass(),
                   share.getDataBase(), share.getVersion(),choose);
           Asynchttp.post(context,share.getBaseURL()+WebApi.DOWNLOADDATA, json, new Asynchttp.Response() {
               @Override
               public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                   DownloadReturnBean dBean = JsonCreater.gson.fromJson(cBean.returnJson, DownloadReturnBean.class);
                   PayTypeDao payTypeDao = daoSession.getPayTypeDao();
                   payTypeDao.deleteAll();
                   payTypeDao.insertOrReplaceInTx(dBean.payTypes);
                   payTypeDao.detachAll();
               }

               @Override
               public void onFailed(String Msg, AsyncHttpClient client) {
                   Toast.showText(context, Msg);
               }
           });
       }
       PayTypeDao payTypeDao = daoSession.getPayTypeDao();
       List<PayType> payTypes = payTypeDao.loadAll();
       PayTypeSpAdapter payTypeSpAdapter = new PayTypeSpAdapter(context,payTypes);
       sp.setAdapter(payTypeSpAdapter);
       payTypeSpAdapter.notifyDataSetChanged();
       return payTypeSpAdapter;
   }

   public PayMethodSpAdapter getGoodsTypes(Spinner sp){
       if(share.getIsOL()){
           ArrayList<Integer> choose = new ArrayList<>();
           choose.add(15);
           String json = JsonCreater.DownLoadData(share.getDatabaseIp(),
                   share.getDatabasePort(), share.getDataBaseUser(), share.getDataBasePass(),
                   share.getDataBase(), share.getVersion(),choose);
           Asynchttp.post(context,share.getBaseURL()+WebApi.DOWNLOADDATA, json, new Asynchttp.Response() {
               @Override
               public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                   DownloadReturnBean dBean = JsonCreater.gson.fromJson(cBean.returnJson, DownloadReturnBean.class);
                   PurchaseMethodDao purchaseMethodDao = daoSession.getPurchaseMethodDao();
                   purchaseMethodDao.deleteAll();
                   purchaseMethodDao.insertOrReplaceInTx(dBean.purchaseMethod);
                   purchaseMethodDao.detachAll();
               }

               @Override
               public void onFailed(String Msg, AsyncHttpClient client) {
                   Toast.showText(context, Msg);
               }
           });
       }
       PurchaseMethodDao purchaseMethodDao = daoSession.getPurchaseMethodDao();
       List<PurchaseMethod> purchaseMethods = purchaseMethodDao.queryBuilder().
               where(PurchaseMethodDao.Properties.FTypeID.eq("32")).orderAsc(PurchaseMethodDao.Properties.FNumber)
               .build().list();
       PayMethodSpAdapter payMethodSpAdapter = new PayMethodSpAdapter(context, purchaseMethods);
       sp.setAdapter(payMethodSpAdapter);
       payMethodSpAdapter.notifyDataSetChanged();
       return payMethodSpAdapter;
   }

    public InStoreTypeSpAdapter getInStoreType(Spinner sp){
        if(share.getIsOL()){
            ArrayList<Integer> choose = new ArrayList<>();
            choose.add(19);
            String json = JsonCreater.DownLoadData(share.getDatabaseIp(),
                    share.getDatabasePort(), share.getDataBaseUser(), share.getDataBasePass(),
                    share.getDataBase(), share.getVersion(),choose);
            Asynchttp.post(context,share.getBaseURL()+WebApi.DOWNLOADDATA, json, new Asynchttp.Response() {
                @Override
                public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                    DownloadReturnBean dBean = JsonCreater.gson.fromJson(cBean.returnJson, DownloadReturnBean.class);
                    InStoreTypeDao inStoreTypeDao = daoSession.getInStoreTypeDao();
                    inStoreTypeDao.deleteAll();
                    inStoreTypeDao.insertOrReplaceInTx(dBean.inStorageTypes);
                    inStoreTypeDao.detachAll();
                }

                @Override
                public void onFailed(String Msg, AsyncHttpClient client) {
                    Toast.showText(context, Msg);
                }
            });
        }
        InStoreTypeDao inStoreTypeDao = daoSession.getInStoreTypeDao();
        List<InStoreType> inStoreTypes = inStoreTypeDao.loadAll();
        InStoreTypeSpAdapter inStoreTypeSpAdapter = new InStoreTypeSpAdapter(context,inStoreTypes);
        sp.setAdapter(inStoreTypeSpAdapter);
        inStoreTypeSpAdapter.notifyDataSetChanged();
        return inStoreTypeSpAdapter;
    }

    public void updateSupplier(){
        ArrayList<Integer> choose = new ArrayList<>();
        choose.add(9);
        String json = JsonCreater.DownLoadData(share.getDatabaseIp(),
                share.getDatabasePort(), share.getDataBaseUser(), share.getDataBasePass(),
                share.getDataBase(), share.getVersion(),choose);
        Asynchttp.post(context,share.getBaseURL()+WebApi.DOWNLOADDATA, json, new Asynchttp.Response() {
            @Override
            public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                DownloadReturnBean dBean = JsonCreater.gson.fromJson(cBean.returnJson, DownloadReturnBean.class);
                SuppliersDao suppliersDao = daoSession.getSuppliersDao();
                suppliersDao.deleteAll();
                suppliersDao.insertOrReplaceInTx(dBean.suppliers);
                suppliersDao.detachAll();
            }

            @Override
            public void onFailed(String Msg, AsyncHttpClient client) {
                Toast.showText(context, Msg);
            }
        });

    }

    public void updateCilent(){
        ArrayList<Integer> choose = new ArrayList<>();
        choose.add(13);
        String json = JsonCreater.DownLoadData(share.getDatabaseIp(),
                share.getDatabasePort(), share.getDataBaseUser(), share.getDataBasePass(),
                share.getDataBase(), share.getVersion(),choose);
        Asynchttp.post(context,share.getBaseURL()+WebApi.DOWNLOADDATA, json, new Asynchttp.Response() {
            @Override
            public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                DownloadReturnBean dBean = JsonCreater.gson.fromJson(cBean.returnJson, DownloadReturnBean.class);
                ClientDao clientDao = daoSession.getClientDao();
                clientDao.deleteAll();
                clientDao.insertOrReplaceInTx(dBean.clients);
                clientDao.detachAll();
            }

            @Override
            public void onFailed(String Msg, AsyncHttpClient client) {
                Toast.showText(context, Msg);
            }
        });

    }

    public SupplierSpAdapter getSupplier(Spinner sp){
        SuppliersDao inStoreTypeDao = daoSession.getSuppliersDao();
        List<Suppliers> inStoreTypes = inStoreTypeDao.loadAll();
        inStoreTypes.add(0,new Suppliers("","","","","","","","","",""));
        SupplierSpAdapter inStoreTypeSpAdapter = new SupplierSpAdapter(context,inStoreTypes);
        sp.setAdapter(inStoreTypeSpAdapter);
        inStoreTypeSpAdapter.notifyDataSetChanged();
        return inStoreTypeSpAdapter;
    }

    public ClientSpAdapter getCilent(Spinner sp){
        ClientDao inStoreTypeDao = daoSession.getClientDao();
        List<Client> inStoreTypes = inStoreTypeDao.loadAll();
        inStoreTypes.add(0,new Client("","","","","","","","","","",""));
        ClientSpAdapter inStoreTypeSpAdapter = new ClientSpAdapter(context,inStoreTypes);
        sp.setAdapter(inStoreTypeSpAdapter);
        inStoreTypeSpAdapter.notifyDataSetChanged();
        return inStoreTypeSpAdapter;
    }

    public PiciSpAdapter getPici(Storage storage,String wavehouse,Product product,Spinner spinner){
        ArrayList<InStorageNum> container = new ArrayList<>();
        InStorageNumDao inStorageNumDao = daoSession.getInStorageNumDao();
        List<InStorageNum> list = inStorageNumDao.queryBuilder().where(
                InStorageNumDao.Properties.FStockPlaceID.eq(wavehouse==null?"0":wavehouse),
                InStorageNumDao.Properties.FStockID.eq(storage.FItemID),
                InStorageNumDao.Properties.FItemID.eq(product.FItemID),
                InStorageNumDao.Properties.FQty.gt(0)
        ).build().list();
        for (int i = 0;i<list.size();i++){
            if(list.get(i).FQty!= null&&Double.parseDouble(list.get(i).FQty)>0){
                container.add(list.get(i));
            }
        }
        PiciSpAdapter piciSpAdapter = new PiciSpAdapter(context,container);
        spinner.setAdapter(piciSpAdapter);
        piciSpAdapter.notifyDataSetChanged();
        return piciSpAdapter;
    }

    public PiciSpForSubAdapter getPici(PDSub pdSub,Spinner spinner){
        Log.e("查找批次：sub：",pdSub.toString());
        ArrayList<PDSub> container = new ArrayList<>();
        PDSubDao pdSubDao = daoSession.getPDSubDao();
        List<PDSub> list = pdSubDao.queryBuilder().where(
                PDSubDao.Properties.FID.eq(pdSub.FID),
                PDSubDao.Properties.FItemID.eq(pdSub.FItemID),
                PDSubDao.Properties.FStockID.eq(pdSub.FStockID),
                PDSubDao.Properties.FStockPlaceID.eq(pdSub.FStockPlaceID)
        ).build().list();
        Log.e("查找批次：",list.toString());
        for (int i = 0;i<list.size();i++){
            if(list.get(i).FQty!= null&&Double.parseDouble(list.get(i).FQty)>0){
                container.add(list.get(i));
            }
        }
        PiciSpForSubAdapter piciSpAdapter = new PiciSpForSubAdapter(context,container);
        spinner.setAdapter(piciSpAdapter);
        piciSpAdapter.notifyDataSetChanged();
        return piciSpAdapter;
    }
}

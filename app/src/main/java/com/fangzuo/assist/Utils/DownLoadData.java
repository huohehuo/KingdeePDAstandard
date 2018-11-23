package com.fangzuo.assist.Utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Server.WebAPI;
import com.fangzuo.assist.Service.DataService;
import com.fangzuo.assist.widget.LoadingUtil;
import com.fangzuo.greendao.gen.BarCodeDao;
import com.fangzuo.greendao.gen.BibieDao;
import com.fangzuo.greendao.gen.ClientDao;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.DepartmentDao;
import com.fangzuo.greendao.gen.EmployeeDao;
import com.fangzuo.greendao.gen.GetGoodsDepartmentDao;
import com.fangzuo.greendao.gen.InStorageNumDao;
import com.fangzuo.greendao.gen.InStoreTypeDao;
import com.fangzuo.greendao.gen.PayTypeDao;
import com.fangzuo.greendao.gen.PriceMethodDao;
import com.fangzuo.greendao.gen.ProductDao;
import com.fangzuo.greendao.gen.PurchaseMethodDao;
import com.fangzuo.greendao.gen.StorageDao;
import com.fangzuo.greendao.gen.SuppliersDao;
import com.fangzuo.greendao.gen.UnitDao;
import com.fangzuo.greendao.gen.UserDao;
import com.fangzuo.greendao.gen.WanglaikemuDao;
import com.fangzuo.greendao.gen.WaveHouseDao;
import com.fangzuo.greendao.gen.YuandanTypeDao;
import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;

import org.greenrobot.greendao.async.AsyncSession;

import java.util.ArrayList;

/**
 * Created by 王璐阳 on 2018/1/2.
 */

public class DownLoadData {
//    private  ProgressDialog pg;
    private  BasicShareUtil share;
    private Context mContext;
    private Handler handler;
    private DaoSession session;
    private View container;
    private long nowTime;
    private int size;

    public DownLoadData(Context mContext, View v, Handler handler) {
        this.container  = v;
        this.mContext = mContext;
        this.handler = handler;
        share = BasicShareUtil.getInstance(mContext);
        session = GreenDaoManager.getmInstance(mContext).getDaoSession();
    }

    public static DownLoadData getInstance(Context context,View v, Handler handler){
        return new DownLoadData(context,v,handler);
    }
    public  void alertToChoose() {
        AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
        ab.setTitle("请选择要下载的内容");
        View v = LayoutInflater.from(mContext).inflate(R.layout.selectdownload, null);
        final ArrayList<CheckBox> cbList = new ArrayList<>();
        cbList.add((CheckBox)v.findViewById(R.id.cb1));
        cbList.add((CheckBox)v.findViewById(R.id.cb2));
        cbList.add((CheckBox)v.findViewById(R.id.cb3));
        cbList.add((CheckBox)v.findViewById(R.id.cb4));
        cbList.add((CheckBox)v.findViewById(R.id.cb5));
        cbList.add((CheckBox)v.findViewById(R.id.cb6));
        cbList.add((CheckBox)v.findViewById(R.id.cb7));
        cbList.add((CheckBox)v.findViewById(R.id.cb8));
        cbList.add((CheckBox)v.findViewById(R.id.cb9));
        cbList.add((CheckBox)v.findViewById(R.id.cb10));
        cbList.add((CheckBox)v.findViewById(R.id.cb11));
        cbList.add((CheckBox)v.findViewById(R.id.cb12));
        cbList.add((CheckBox)v.findViewById(R.id.cb13));
        cbList.add((CheckBox)v.findViewById(R.id.cb14));
        cbList.add((CheckBox)v.findViewById(R.id.cb15));
        cbList.add((CheckBox)v.findViewById(R.id.cb16));
        cbList.add((CheckBox)v.findViewById(R.id.cb17));
        cbList.add((CheckBox)v.findViewById(R.id.cb18));
        cbList.add((CheckBox)v.findViewById(R.id.cb19));
        ab.setView(v);
        ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ArrayList<Integer> choose = new ArrayList<>();
                for (CheckBox checkbox : cbList) {
                    if (checkbox.isChecked()) {
                        Log.e("选择的是", checkbox.getText().toString());
                        switch (checkbox.getText().toString()) {
                            case "币别表":
                                choose.add(1);
                                break;
                            case "部门表":
                                choose.add(2);
                                break;
                            case "职员表":
                                choose.add(3);
                                break;
                            case "仓位表":
                                choose.add(4);
                                break;
                            case "库存表":
                                choose.add(5);
                                break;
                            case "仓库表":
                                choose.add(6);
                                break;
                            case "单位表":
                                choose.add(7);
                                break;
                            case "条码表":
                                choose.add(8);
                                break;
                            case "供应商表":
                                choose.add(9);
                                break;
                            case "结算方式表":
                                choose.add(10);
                                break;
                            case "商品资料表":
                                choose.add(11);
                                break;
                            case "用户信息表":
                                choose.add(12);
                                break;
                            case "客户信息表":
                                choose.add(13);
                                break;
                            case "交货单位":
                                choose.add(14);
                                break;
                            case "销售/采购方式表":
                                choose.add(15);
                                break;
                            case "源单类型":
                                choose.add(16);
                                break;
                            case "往来科目":
                                choose.add(17);
                                break;
                            case "价格政策":
                                choose.add(18);
                                break;
                            case "入库类型":
                                choose.add(19);
                                break;
                        }
                    }
                }
                downloadData(choose);
            }
        });
        ab.setNeutralButton("下载全部", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ArrayList<Integer> chooseAll = new ArrayList<>();
                //币别表1   部门表2   职员表3   仓位表4   库存表5  仓库表6
                //单位表7   条码表8   供应商表9    结算方式表10      商品资料表11
                //用户信息表12    客户信息表13    交货单位14     销售/采购方式表15
                //源单类型16   往来科目17  价格政策18  入库类型19
                for (int j = 1; j <= cbList.size(); j++) {
                    chooseAll.add(j);
                }
                downloadData(chooseAll);
            }
        });
        ab.setNegativeButton("取消", null);
        ab.create().show();
    }
    private void downloadData(final ArrayList<Integer> choose) {
        LoadingUtil.show(mContext,"正在下载...");
        String json = JsonCreater.DownLoadData(share.getDatabaseIp(),
                share.getDatabasePort(), share.getDataBaseUser(), share.getDataBasePass(),
                share.getDataBase(), share.getVersion(), choose);
        nowTime = System.currentTimeMillis();
        App.getRService().downloadData(json, new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                DownloadReturnBean dBean = new Gson().fromJson(commonResponse.returnJson, DownloadReturnBean.class);
                insert(dBean);
            }

            @Override
            public void onError(Throwable e) {
                LoadingUtil.dismiss();
                Toast.showText(mContext,"下载错误:"+e.toString());
            }
        });
//        RetrofitUtil.getInstance(mContext).createReq(WebAPI.class).
//                downloadData(RetrofitUtil.getParams(mContext,json)).enqueue(new CallBack() {
//            @Override
//            public void onSucceed(CommonResponse cBean) {
//                DownloadReturnBean dBean = new Gson().fromJson(cBean.returnJson, DownloadReturnBean.class);
//                insert(dBean);
//            }
//
//            @Override
//            public void OnFail(String Msg) {
//                LoadingUtil.dismiss();
//                SnackBarUtil.LongSnackbar(container, Msg, SnackBarUtil.Alert).setAction("重试", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        downloadData(choose);
//                    }
//                }).show();
//            }
//        });
    }

    private void insert(final DownloadReturnBean dBean) {
        AsyncSession asyncSession = session.startAsyncSession();
        AsyncSession asyncSession2 = session.startAsyncSession();
        asyncSession.runInTx(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                boolean b = insertLocalSQLite(dBean);
                Log.e("result", b + "");
                if (b) {
                    message.what = 1;
                    message.obj = nowTime;
                    message.arg1 = dBean.size;
                }
                handler.sendMessage(message);
            }
        });
        asyncSession2.runInTx(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                boolean b = insertLocalSQLite2(dBean);
                Log.e("result", b + "");

            }

        });
        try{
            LoadingUtil.dismiss();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    private boolean insertLocalSQLite2(DownloadReturnBean dBean) {
        if (dBean.bibiezhongs != null && dBean.bibiezhongs.size() > 0) {
            BibieDao bibieDao = session.getBibieDao();
            bibieDao.deleteAll();
            bibieDao.insertOrReplaceInTx(dBean.bibiezhongs);
            bibieDao.detachAll();
        }

        if (dBean.department != null && dBean.department.size() > 0) {
            DepartmentDao departmentDao = session.getDepartmentDao();
            departmentDao.deleteAll();
            departmentDao.insertOrReplaceInTx(dBean.department);
            departmentDao.detachAll();
        }
        if (dBean.employee != null && dBean.employee.size() > 0) {
            EmployeeDao employeeDao = session.getEmployeeDao();
            employeeDao.deleteAll();
            employeeDao.insertOrReplaceInTx(dBean.employee);
            employeeDao.detachAll();
        }
        if (dBean.wavehouse != null && dBean.wavehouse.size() > 0) {
            WaveHouseDao wavehouseDao = session.getWaveHouseDao();
            wavehouseDao.deleteAll();
            wavehouseDao.insertOrReplaceInTx(dBean.wavehouse);
            wavehouseDao.detachAll();
        }
        if (dBean.InstorageNum != null && dBean.InstorageNum.size() > 0) {
            InStorageNumDao storageNumDao = session.getInStorageNumDao();
            storageNumDao.deleteAll();
            storageNumDao.insertOrReplaceInTx(dBean.InstorageNum);
            storageNumDao.detachAll();
        }
        if (dBean.storage != null && dBean.storage.size() > 0) {
            StorageDao storageDao = session.getStorageDao();
            storageDao.deleteAll();
            storageDao.insertOrReplaceInTx(dBean.storage);
            storageDao.detachAll();
        }
        if (dBean.units != null && dBean.units.size() > 0) {
            UnitDao unitDao = session.getUnitDao();
            unitDao.deleteAll();
            unitDao.insertOrReplaceInTx(dBean.units);
            unitDao.detachAll();
        }

        if (dBean.suppliers != null && dBean.suppliers.size() > 0) {
            SuppliersDao suppliersDao = session.getSuppliersDao();
            suppliersDao.deleteAll();
            suppliersDao.insertOrReplaceInTx(dBean.suppliers);
            suppliersDao.detachAll();
        }
        if (dBean.payTypes != null && dBean.payTypes.size() > 0) {
            PayTypeDao payTypeDao = session.getPayTypeDao();
            payTypeDao.deleteAll();
            payTypeDao.insertOrReplaceInTx(dBean.payTypes);
            payTypeDao.detachAll();
        }
        if (dBean.products != null && dBean.products.size() > 0) {
            ProductDao productDao = session.getProductDao();
            productDao.deleteAll();
            productDao.insertOrReplaceInTx(dBean.products);
            productDao.detachAll();
        }
        return true;
    }

    private boolean insertLocalSQLite(DownloadReturnBean dBean) {
        if (dBean.BarCode != null && dBean.BarCode.size() > 0) {
            BarCodeDao barCodeDao = session.getBarCodeDao();
            barCodeDao.deleteAll();
            barCodeDao.insertOrReplaceInTx(dBean.BarCode);
            barCodeDao.detachAll();
        }
        if (dBean.User != null && dBean.User.size() > 0) {
            UserDao userDao = session.getUserDao();
            userDao.deleteAll();
            userDao.insertOrReplaceInTx(dBean.User);
            userDao.detachAll();
        }
        if (dBean.clients != null && dBean.clients.size() > 0) {
            ClientDao clientDao = session.getClientDao();
            clientDao.deleteAll();
            clientDao.insertOrReplaceInTx(dBean.clients);
            clientDao.detachAll();
        }
        if (dBean.getGoodsDepartments != null && dBean.getGoodsDepartments.size() > 0) {
            GetGoodsDepartmentDao getGoodsDepartmentDao = session.getGetGoodsDepartmentDao();
            getGoodsDepartmentDao.deleteAll();
            getGoodsDepartmentDao.insertOrReplaceInTx(dBean.getGoodsDepartments);
            getGoodsDepartmentDao.detachAll();
        }
        if (dBean.purchaseMethod != null && dBean.purchaseMethod.size() > 0) {
            PurchaseMethodDao purchaseMethodDao = session.getPurchaseMethodDao();
            purchaseMethodDao.deleteAll();
            purchaseMethodDao.insertOrReplaceInTx(dBean.purchaseMethod);
            purchaseMethodDao.detachAll();
        }
        if (dBean.yuandanTypes != null && dBean.yuandanTypes.size() > 0) {
            YuandanTypeDao yuandanTypeDao = session.getYuandanTypeDao();
            yuandanTypeDao.deleteAll();
            yuandanTypeDao.insertOrReplaceInTx(dBean.yuandanTypes);
            yuandanTypeDao.detachAll();
        }
        if (dBean.wanglaikemu != null && dBean.wanglaikemu.size() > 0) {
            WanglaikemuDao wanglaikemuDao = session.getWanglaikemuDao();
            wanglaikemuDao.deleteAll();
            wanglaikemuDao.insertOrReplaceInTx(dBean.wanglaikemu);
            wanglaikemuDao.detachAll();
        }
        if (dBean.priceMethods != null && dBean.priceMethods.size() > 0) {
            PriceMethodDao priceMethodDao = session.getPriceMethodDao();
            priceMethodDao.deleteAll();
            priceMethodDao.insertOrReplaceInTx(dBean.priceMethods);
            priceMethodDao.detachAll();
        }
        if (dBean.inStorageTypes != null && dBean.inStorageTypes.size() > 0) {
            InStoreTypeDao inStoreTypeDao = session.getInStoreTypeDao();
            inStoreTypeDao.deleteAll();
            inStoreTypeDao.insertOrReplaceInTx(dBean.inStorageTypes);
            inStoreTypeDao.detachAll();
        }
        return true;
    }
}

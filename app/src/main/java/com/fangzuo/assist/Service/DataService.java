package com.fangzuo.assist.Service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Utils.GreenDaoManager;
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

import org.greenrobot.greendao.async.AsyncSession;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DataService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.fangzuo.assist.Service.action.FOO";
    private static final String ACTION_BAZ = "com.fangzuo.assist.Service.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.fangzuo.assist.Service.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.fangzuo.assist.Service.extra.PARAM2";

    public DataService() {
        super("DataService");
    }

    private DaoSession session;
    @Override
    public void onCreate() {
        super.onCreate();
        session = GreenDaoManager.getmInstance(App.getContext()).getDaoSession();
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void deleteAll(Context context) {
        Intent intent = new Intent(context, DataService.class);
        intent.setAction(ACTION_FOO);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
//    // TODO: Customize helper method
//    public static void downData(Context context, String param1) {
//        Intent intent = new Intent(context, DataService.class);
//        intent.setAction(ACTION_BAZ);
//        intent.putExtra(EXTRA_PARAM1, param1);
//        context.startService(intent);
//    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                handleActionFoo();
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
//                handleActionBaz(param1);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo() {
        session.getBibieDao().deleteAll();
        session.getBarCodeDao().deleteAll();
        session.getT_DetailDao().deleteAll();
        session.getT_mainDao().deleteAll();
        session.getClientDao().deleteAll();
        session.getDepartmentDao().deleteAll();
        session.getEmployeeDao().deleteAll();
        session.getGetGoodsDepartmentDao().deleteAll();
        session.getInStorageNumDao().deleteAll();
        session.getInStoreTypeDao().deleteAll();
        session.getPayTypeDao().deleteAll();
        session.getPDMainDao().deleteAll();
        session.getPDSubDao().deleteAll();
        session.getPriceMethodDao().deleteAll();
        session.getProductDao().deleteAll();
        session.getPurchaseMethodDao().deleteAll();
        session.getPushDownMainDao().deleteAll();
        session.getPushDownSubDao().deleteAll();
        session.getStorageDao().deleteAll();
        session.getSuppliersDao().deleteAll();
        session.getUnitDao().deleteAll();
        session.getUserDao().deleteAll();
        session.getWanglaikemuDao().deleteAll();
        session.getWaveHouseDao().deleteAll();
        session.getYuandanTypeDao().deleteAll();
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
//    DownloadReturnBean dBean;
//    private void handleActionBaz(String param1) {
//        dBean = new Gson().fromJson(param1, DownloadReturnBean.class);
//
//        AsyncSession asyncSession = session.startAsyncSession();
//        asyncSession.runInTx(new Runnable() {
//            @Override
//            public void run() {
//                boolean b = insertLocalSQLite(dBean);
//                Log.e("result", b + "");
//                if (b) {
//                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.DownData_OK,System.currentTimeMillis()+"",dBean.size+"","0"));
//                }else{
//                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.DownData_ERROR,""));
//
//                }
//            }
//        });
//    }


//    private boolean insertLocalSQLite(DownloadReturnBean dBean) {
//        if (dBean.bibiezhongs != null && dBean.bibiezhongs.size() > 0) {
//            BibieDao bibieDao = session.getBibieDao();
//            bibieDao.deleteAll();
//            bibieDao.insertOrReplaceInTx(dBean.bibiezhongs);
//            bibieDao.detachAll();
//        }
//
//        if (dBean.department != null && dBean.department.size() > 0) {
//            DepartmentDao departmentDao = session.getDepartmentDao();
//            departmentDao.deleteAll();
//            departmentDao.insertOrReplaceInTx(dBean.department);
//            departmentDao.detachAll();
//        }
//        if (dBean.employee != null && dBean.employee.size() > 0) {
//            EmployeeDao employeeDao = session.getEmployeeDao();
//            employeeDao.deleteAll();
//            employeeDao.insertOrReplaceInTx(dBean.employee);
//            employeeDao.detachAll();
//        }
//        if (dBean.wavehouse != null && dBean.wavehouse.size() > 0) {
//            WaveHouseDao wavehouseDao = session.getWaveHouseDao();
//            wavehouseDao.deleteAll();
//            wavehouseDao.insertOrReplaceInTx(dBean.wavehouse);
//            wavehouseDao.detachAll();
//        }
//        if (dBean.InstorageNum != null && dBean.InstorageNum.size() > 0) {
//            InStorageNumDao storageNumDao = session.getInStorageNumDao();
//            storageNumDao.deleteAll();
//            storageNumDao.insertOrReplaceInTx(dBean.InstorageNum);
//            storageNumDao.detachAll();
//        }
//        if (dBean.storage != null && dBean.storage.size() > 0) {
//            StorageDao storageDao = session.getStorageDao();
//            storageDao.deleteAll();
//            storageDao.insertOrReplaceInTx(dBean.storage);
//            storageDao.detachAll();
//        }
//        if (dBean.units != null && dBean.units.size() > 0) {
//            UnitDao unitDao = session.getUnitDao();
//            unitDao.deleteAll();
//            unitDao.insertOrReplaceInTx(dBean.units);
//            unitDao.detachAll();
//        }
//
//        if (dBean.suppliers != null && dBean.suppliers.size() > 0) {
//            SuppliersDao suppliersDao = session.getSuppliersDao();
//            suppliersDao.deleteAll();
//            suppliersDao.insertOrReplaceInTx(dBean.suppliers);
//            suppliersDao.detachAll();
//        }
//        if (dBean.payTypes != null && dBean.payTypes.size() > 0) {
//            PayTypeDao payTypeDao = session.getPayTypeDao();
//            payTypeDao.deleteAll();
//            payTypeDao.insertOrReplaceInTx(dBean.payTypes);
//            payTypeDao.detachAll();
//        }
//        if (dBean.products != null && dBean.products.size() > 0) {
//            ProductDao productDao = session.getProductDao();
//            productDao.deleteAll();
//            productDao.insertOrReplaceInTx(dBean.products);
//            productDao.detachAll();
//        }
//        if (dBean.BarCode != null && dBean.BarCode.size() > 0) {
//            BarCodeDao barCodeDao = session.getBarCodeDao();
//            barCodeDao.deleteAll();
//            barCodeDao.insertOrReplaceInTx(dBean.BarCode);
//            barCodeDao.detachAll();
//        }
//        if (dBean.User != null && dBean.User.size() > 0) {
//            UserDao userDao = session.getUserDao();
//            userDao.deleteAll();
//            userDao.insertOrReplaceInTx(dBean.User);
//            userDao.detachAll();
//        }
//        if (dBean.clients != null && dBean.clients.size() > 0) {
//            ClientDao clientDao = session.getClientDao();
//            clientDao.deleteAll();
//            clientDao.insertOrReplaceInTx(dBean.clients);
//            clientDao.detachAll();
//        }
//        if (dBean.getGoodsDepartments != null && dBean.getGoodsDepartments.size() > 0) {
//            GetGoodsDepartmentDao getGoodsDepartmentDao = session.getGetGoodsDepartmentDao();
//            getGoodsDepartmentDao.deleteAll();
//            getGoodsDepartmentDao.insertOrReplaceInTx(dBean.getGoodsDepartments);
//            getGoodsDepartmentDao.detachAll();
//        }
//        if (dBean.purchaseMethod != null && dBean.purchaseMethod.size() > 0) {
//            PurchaseMethodDao purchaseMethodDao = session.getPurchaseMethodDao();
//            purchaseMethodDao.deleteAll();
//            purchaseMethodDao.insertOrReplaceInTx(dBean.purchaseMethod);
//            purchaseMethodDao.detachAll();
//        }
//        if (dBean.yuandanTypes != null && dBean.yuandanTypes.size() > 0) {
//            YuandanTypeDao yuandanTypeDao = session.getYuandanTypeDao();
//            yuandanTypeDao.deleteAll();
//            yuandanTypeDao.insertOrReplaceInTx(dBean.yuandanTypes);
//            yuandanTypeDao.detachAll();
//        }
//        if (dBean.wanglaikemu != null && dBean.wanglaikemu.size() > 0) {
//            WanglaikemuDao wanglaikemuDao = session.getWanglaikemuDao();
//            wanglaikemuDao.deleteAll();
//            wanglaikemuDao.insertOrReplaceInTx(dBean.wanglaikemu);
//            wanglaikemuDao.detachAll();
//        }
//        if (dBean.priceMethods != null && dBean.priceMethods.size() > 0) {
//            PriceMethodDao priceMethodDao = session.getPriceMethodDao();
//            priceMethodDao.deleteAll();
//            priceMethodDao.insertOrReplaceInTx(dBean.priceMethods);
//            priceMethodDao.detachAll();
//        }
//        if (dBean.inStorageTypes != null && dBean.inStorageTypes.size() > 0) {
//            InStoreTypeDao inStoreTypeDao = session.getInStoreTypeDao();
//            inStoreTypeDao.deleteAll();
//            inStoreTypeDao.insertOrReplaceInTx(dBean.inStorageTypes);
//            inStoreTypeDao.detachAll();
//        }
//        return true;
//    }
}

package com.fangzuo.assist.Service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Utils.Asynchttp;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.EventBusUtil;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.WebApi;
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
import com.loopj.android.http.AsyncHttpClient;

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
    private static final String UpdateTime = "com.fangzuo.assist.Service.action.UpdateTime";

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
    //更新服务器中的当前时间
    public static void updateTime(Context context) {
        Intent intent = new Intent(context, DataService.class);
        intent.setAction(UpdateTime);
        context.startService(intent);
    }
    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
//    // TODO: Customize helper method
    public static void pushError(Context context, String txtName,Throwable ex) {
        Toast.showText(App.getContext(),"发生内部错误，请重试..."+ex.toString());
        Intent intent = new Intent(context, DataService.class);
        StackTraceElement[] stes = ex.getStackTrace();
        StringBuilder builder = new StringBuilder();
        builder.append(ex.toString()+"\n");
        for (int i = 0; i < stes.length; i ++) {
            builder.append(i + "->"  + stes[i].getClassName() + "-->" + stes[i].getMethodName() + "-->" + stes[i].getFileName()+"\n");
        }
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, txtName);
        intent.putExtra(EXTRA_PARAM2, builder.toString());
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                handleActionFoo();
            } else if (ACTION_BAZ.equals(action)) {
                final String txtNa = intent.getStringExtra(EXTRA_PARAM1);
                final String err = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(txtNa,err);
            }else if (UpdateTime.equals(action)) {
                handleActionUpdateTime();
            }
        }
    }
    private void handleActionUpdateTime(){
        App.getRService().doIOAction(WebApi.SetUseTime, "更新时间", new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
//                super.onNext(commonResponse);
            }

            @Override
            public void onError(Throwable e) {
//                super.onError(e);
            }
        });
    }
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
    private void handleActionBaz(String txtN,String param1) {

        Asynchttp.post(App.getContext(), Config.Error_Url, Config.Company+"^"+txtN+"^"+param1, new Asynchttp.Response() {
            @Override
            public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {

            }
            @Override
            public void onFailed(String Msg, AsyncHttpClient client) {
            }
        });
    }

}

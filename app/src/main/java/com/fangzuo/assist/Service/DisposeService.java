package com.fangzuo.assist.Service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.greendao.gen.DaoSession;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DisposeService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.fangzuo.assist.Service.action.FOO";
    private static final String ACTION_BAZ = "com.fangzuo.assist.Service.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.fangzuo.assist.Service.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.fangzuo.assist.Service.extra.PARAM2";
    private DaoSession session;

    public DisposeService() {
        super("DisposeService");
    }

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
        Intent intent = new Intent(context, DisposeService.class);
        intent.setAction(ACTION_FOO);
//        intent.putExtra(EXTRA_PARAM1, param1);
//        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, DisposeService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
//                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
//                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo();
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
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
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

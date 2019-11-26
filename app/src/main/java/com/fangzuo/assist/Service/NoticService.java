package com.fangzuo.assist.Service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.Beans.SearchBean;
import com.fangzuo.assist.Dao.NoticBean;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.EventBusUtil;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.greendao.gen.NoticBeanDao;
import com.google.gson.Gson;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class NoticService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.fangzuo.assist.Service.action.FOO";
    private static final String ACTION_BAZ = "com.fangzuo.assist.Service.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.fangzuo.assist.Service.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.fangzuo.assist.Service.extra.PARAM2";

    public NoticService() {
        super("NoticService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startNotic(Context context, String userid, String time) {
        Intent intent = new Intent(context, NoticService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, userid);
        intent.putExtra(EXTRA_PARAM2, time);
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
        Intent intent = new Intent(context, NoticService.class);
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
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
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
    private void handleActionFoo(String userid, String time) {
        SearchBean bean = new SearchBean();
        bean.val1 = userid;bean.val2 = time;
        Lg.e("请求推送"+userid+time);
        App.getRService().doIOAction("GetNoticData", new Gson().toJson(bean), new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                super.onNext(commonResponse);
                if (!commonResponse.state)return;
                DownloadReturnBean sBean = new Gson().fromJson(commonResponse.returnJson, DownloadReturnBean.class);
                if (null!=sBean.noticBeans && sBean.noticBeans.size()>0){
                    NoticBeanDao noticBean = GreenDaoManager.getmInstance(App.getContext()).getDaoSession().getNoticBeanDao();
                    for (int i = 0; i < sBean.noticBeans.size(); i++) {
                        //当本地不存在该billno，则添加
                        if (noticBean.queryBuilder().where(NoticBeanDao.Properties.FBillNo.eq(sBean.noticBeans)).build().list().size()<=0){
                            noticBean.insertInTx(sBean.noticBeans.get(i));
                            EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Upload_Notice, ""));
                        }
                    }
                    for (int i = 0; i < sBean.noticBeans.size(); i++) {
                        //                //发送更新提示广播
                        Intent intent = new Intent(Config.VersionReceiver);
                        intent.putExtra("noticID",sBean.noticBeans.get(i).FNoticeId);
                        intent.putExtra("billNo",sBean.noticBeans.get(i).FBillNo);
                        intent.putExtra("num",sBean.noticBeans.get(i).FNumAll);
                        intent.setPackage(getPackageName());
                        sendBroadcast(intent);
                    }
                }

            }

            @Override
            public void onError(Throwable e) {
//                super.onError(e);
            }
        });

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

package com.fangzuo.assist.ABase;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.device.ScanDevice;
import android.device.ScanManager;
import android.device.scanner.configuration.PropertyID;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Service.DataService;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.EventBusUtil;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.ShareUtil;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.T_DetailDao;
import com.fangzuo.greendao.gen.T_mainDao;
import com.google.gson.Gson;
import com.nineoldandroids.view.ViewHelper;
import com.orhanobut.hawk.Hawk;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by NB on 2017/8/1.
 */

public abstract class BaseActivity extends FragmentActivity {

    public Context mContext;
    public ShareUtil share;
    private IntentFilter scanDataIntentFilter;
    public String barcodeStr;
    public String TAG = getClass().getSimpleName();
    public Gson gson;
    public T_mainDao t_mainDao;
    public T_DetailDao t_detailDao;
    public DaoSession daoSession;

    //u8000
    private ScanDevice sm;
    private BroadcastReceiver mScanDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            byte[] barocode = intent.getByteArrayExtra("barocode");
            int barocodelen = intent.getIntExtra("length", 0);
            byte temp = intent.getByteExtra("barcodeType", (byte) 0);
            Log.i("debug", "----codetype--" + temp);
            barcodeStr = new String(barocode, 0, barocodelen);
            OnReceive(barcodeStr);
        }
    };

    //c5000
    private BroadcastReceiver mScanDataReceiverFor5000 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("com.android.scanservice.scancontext")) {
                barcodeStr = intent.getStringExtra("Scan_context");
                OnReceive(barcodeStr);
            }
        }
    };
    //G02A
    private static final String ACTION_DISPLAY_SCAN_RESULT = "techain.intent.action.DISPLAY_SCAN_RESULT";
    private BroadcastReceiver mScanDataReceiverForG02A = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_DISPLAY_SCAN_RESULT)) {
                barcodeStr = intent.getStringExtra("decode_data");
                OnReceive(barcodeStr);
            }
        }
    };
    //  M60
    private static final String ACTION_M60 = "com.mobilead.tools.action.scan_result";
    private BroadcastReceiver mScanDataReceiverForM60 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_M60)) {
                barcodeStr = intent.getStringExtra("decode_rslt");
                OnReceive(barcodeStr);
            }
        }
    };
    //新大陆
    private static final String ACTION_XDL_SCAN_RESULT = "nlscan.action.SCANNER_RESULT";
    private BroadcastReceiver mScanDataReceiverForXDL = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_XDL_SCAN_RESULT)) {
                barcodeStr = intent.getStringExtra("SCAN_BARCODE1");
                OnReceive(barcodeStr);
            }
        }
    };

//    public Barcode2DWithSoft.ScanCallback mScanCallback = new Barcode2DWithSoft.ScanCallback() {
//        @Override
//        public void onScanComplete(int i, int length, byte[] data) {
//            Log.i("ErDSoftScanFragment", "onScanComplete() i=" + i);
//            barcodeStr = new String(data);
//            OnReceive(barcodeStr);
//        }
//    };


//    //UBX
//    private ScanManager mScanManager;
//    private BroadcastReceiver mScanDataReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            byte[] barcode = intent.getByteArrayExtra(ScanManager.DECODE_DATA_TAG);
//            int barcodelen = intent.getIntExtra(ScanManager.BARCODE_LENGTH_TAG, 0);
//            byte temp = intent.getByteExtra(ScanManager.BARCODE_TYPE_TAG, (byte) 0);
//            Log.i("debug", "----codetype--" + temp);
//            barcodeStr = new String(barcode, 0, barcodelen);
//            OnReceive(barcodeStr);
//
//        }
//    };
//    private void initScan() {
//        // TODO Auto-generated method stub
//        try{
//            mScanManager = new ScanManager();
//            mScanManager.openScanner();
//            mScanManager.switchOutputMode(0);
//            SoundPool soundpool = new SoundPool(1, AudioManager.STREAM_NOTIFICATION, 100);
//            int soundid = soundpool.load("/etc/Scan_new. ", 1);
//            Log.e("OnResume","OnResume");
//            IntentFilter filter = new IntentFilter();
//            int[] idbuf = new int[]{PropertyID.WEDGE_INTENT_ACTION_NAME, PropertyID.WEDGE_INTENT_DATA_STRING_TAG};
//            String[] value_buf = mScanManager.getParameterString(idbuf);
//            if(value_buf != null && value_buf[0] != null && !value_buf[0].equals("")) {
//                filter.addAction(value_buf[0]);
//            } else {
//                filter.addAction(ScanManager.ACTION_DECODE);
//            }
//
//            registerReceiver(mScanDataReceiver, filter);
//        }catch (RuntimeException stub){
//            Lg.e("初始化扫描器失败");
//        }
//
//    }



//    public Barcode2DWithSoft mReader;
    private String date;
    private int year;
    private int month;
    private int day;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        if (isRegisterEventBus()) {
            EventBusUtil.register(this);
        }
        share = ShareUtil.getInstance(mContext);
        year = Calendar.getInstance().get(Calendar.YEAR);
        month = Calendar.getInstance().get(Calendar.MONTH);
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        gson = new Gson();
        daoSession = GreenDaoManager.getmInstance(mContext).getDaoSession();
        t_mainDao = daoSession.getT_mainDao();
        t_detailDao = daoSession.getT_DetailDao();
        //UBX
//        initScan();

        if (App.PDA_Choose==6){
//            try {
//                mReader = Barcode2DWithSoft.getInstance();
//            } catch (Exception ex) {
//                Toast.showText(mContext,"初始化H100设备错误"+ex.toString());
//            }
        }



        initView();
        initData();
        initListener();
    }
//    public BroadcastReceiver getBroadcastReceiver(){
//        return mScanDataReceiver;
//    }
@Override
protected void onResume() {
    // TODO Auto-generated method stub
    super.onResume();
//手机4：不需要注册
//        if (App.PDA_Choose!=4){
    if (App.PDA_Choose == 1) {
        //G02A
        IntentFilter scanDataIntentFilter = new IntentFilter();
        scanDataIntentFilter.addAction(ACTION_DISPLAY_SCAN_RESULT);
        registerReceiver(mScanDataReceiverForG02A, scanDataIntentFilter);
    } else if (App.PDA_Choose==2){
        //u8000
        sm = new ScanDevice();
        IntentFilter filter = new IntentFilter();
        filter.addAction("scan.rcv.message");
        registerReceiver(mScanDataReceiver, filter);
    }else if (App.PDA_Choose==3){
        //5000
        IntentFilter filter = new IntentFilter();
        filter.addAction("scan.rcv.message");
        filter.addAction("com.android.scanservice.scancontext");
        registerReceiver(mScanDataReceiverFor5000, filter);
    }else if (App.PDA_Choose==4){
        //M60
        IntentFilter scanDataIntentFilter = new IntentFilter();
        scanDataIntentFilter.addAction(ACTION_M60);
        registerReceiver(mScanDataReceiverForM60, scanDataIntentFilter);
    }else if (App.PDA_Choose==5){
        //新大陆注册");
        IntentFilter xdlFilter = new IntentFilter();
        xdlFilter.addAction(ACTION_XDL_SCAN_RESULT);
        registerReceiver(mScanDataReceiverForXDL, xdlFilter);
//        if (mReader != null) {
////            new InitTask().execute();
//            boolean result = mReader.open(mContext);
//            if (result) {
//                mReader.setParameter(324, 1);
//                mReader.setParameter(300, 0); // Snapshot Aiming
//                mReader.setParameter(361, 0); // Image Capture Illumination
//                mReader.setScanCallback(mScanCallback);
//            }else{
//                Toast.showText(mContext,"H100设备启动出错");
//            }
//        }
    }
//        }
}
    @Override
    protected void onPause() {
        super.onPause();
//        unregisterReceiver(mScanDataReceiver);
        //        if (App.PDA_Choose!=4){
        try{
            if (mScanDataReceiver != null ||
                    mScanDataReceiverForG02A != null||
                    mScanDataReceiverFor5000 != null||
                    mScanDataReceiverForXDL != null||
                    mScanDataReceiverForM60 != null
                    ) {
                if (App.PDA_Choose == 1) {
                    unregisterReceiver(mScanDataReceiverForG02A);
                } else if (App.PDA_Choose==2){
                    unregisterReceiver(mScanDataReceiver);
                }else if (App.PDA_Choose == 3){
                    unregisterReceiver(mScanDataReceiverFor5000);
                }else if (App.PDA_Choose == 4){
                    unregisterReceiver(mScanDataReceiverForM60);
                }else if (App.PDA_Choose == 5){
                    unregisterReceiver(mScanDataReceiverForXDL);
//                Toast.showText(mContext,"关闭H100设备");
//                if (mReader != null) {
//                    mReader.close();
//                }
                }
            }
        }catch (Exception e){
            DataService.pushError(mContext, this.getClass().getSimpleName(), e);
        }

//        }
    }

    /**
     * 设备上电异步类
     *
     * @author liuruifeng
     */


//    public class InitTask extends AsyncTask<String, Integer, Boolean> {
//        ProgressDialog mypDialog;
//
//        @Override
//        protected Boolean doInBackground(String... params) {
//
//            boolean result = false;
//
////            if (mReader != null) {
////                result = mReader.open(mContext);
////                Lg.e("打开检测："+result);
////                if (result) {
////                    mReader.setParameter(324, 1);
////                    mReader.setParameter(300, 0); // Snapshot Aiming
////                    mReader.setParameter(361, 0); // Image Capture Illumination
////                }
////            }
//
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean result) {
//            super.onPostExecute(result);
//
//            mypDialog.cancel();
//
//            if (!result) {
//
//                Toast.showText(mContext, "init fail");
//            }
//
////            if (mReader != null) {
////
////                mReader.setScanCallback(mScanCallback);
////            }
//        }
//
//        @Override
//        protected void onPreExecute() {
//            // TODO Auto-generated method stub
//            super.onPreExecute();
//
//            mypDialog = new ProgressDialog(mContext);
//            mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            mypDialog.setMessage("init...");
//            mypDialog.setCanceledOnTouchOutside(false);
//            mypDialog.show();
//        }
//
//    }
//

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (isRegisterEventBus()) {
            EventBusUtil.unregister(this);
        }
    }
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCome(ClassEvent event) {
        if (event != null) {
            receiveEvent(event);
        }
    }

    protected void receiveEvent(ClassEvent event) {

    }

    public String datePicker(final TextView v) {
        final DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            }
        }, year, month, day);
        datePickerDialog.setButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int year = datePickerDialog.getDatePicker().getYear();
                int month = datePickerDialog.getDatePicker().getMonth();
                int day = datePickerDialog.getDatePicker().getDayOfMonth();
                date = year + "-" + ((month < 10) ? "0" + (month + 1) : (month + 1)) + "-" + ((day < 10) ? "0" + day : day);
                Toast.showText(mContext, date);
                v.setText(date);
                datePickerDialog.dismiss();

            }
        });
        datePickerDialog.show();
        return date;
    }





    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initListener();

    protected abstract void OnReceive(String code);

    // 检查Service是否运行
    private boolean isServiceRunning(String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(100);
        if (!(serviceList.size() > 0)) {
            return false;
        }

        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    //androidmanifest中获取版本名称
    public String getVersionName() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    getPackageName(), 0);

            int versionCode = packageInfo.versionCode;
            String versionName = packageInfo.versionName;

            System.out.println("versionName=" + versionName + ";versionCode="
                    + versionCode);

            return versionName;
        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();
        }

        return "";
    }

    //androidmanifest中获取版本名称
    public int getVersionCode() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    getPackageName(), 0);

            int versionCode = packageInfo.versionCode;
            String versionName = packageInfo.versionName;

            System.out.println("versionName=" + versionName + ";versionCode="
                    + versionCode);

            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();
        }

        return 0;
    }

    public final void startNewActivity(Class<? extends Activity> target,
                                       int enterAnim, int exitAnim, boolean isFinish, Bundle mBundle) {
        Intent mIntent = new Intent(this, target);
        if (mBundle != null) {
            mIntent.putExtras(mBundle);
        }
        startActivity(mIntent);
        overridePendingTransition(enterAnim, exitAnim);
        if (isFinish) {
            finish();
        }
    }


    protected final void startNewActivityForResult(Class<? extends Activity> target, int enterAnim, int exitAnim, int requestCode, Bundle mBundle) {
        Intent mIntent = new Intent(this, target);
        if (mBundle != null) {
            mIntent.putExtras(mBundle);
        }
        startActivityForResult(mIntent, requestCode);
        overridePendingTransition(enterAnim, exitAnim);
    }

    public void initDrawer(final DrawerLayout mDrawer) {
        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.LEFT);
        mDrawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                View mContent = mDrawer.getChildAt(0);
                View mMenu = drawerView;
                float scale = 1 - slideOffset;
                float right = 0.8f + (1 - slideOffset) * 0.2f;


                if (drawerView.getTag().equals("LEFT")) {
                    float leftScale = 1 - scale;
                    ViewHelper.setScaleX(mMenu, leftScale);//drawer
                    ViewHelper.setScaleY(mMenu, leftScale);//drawer
                    ViewHelper.setAlpha(mMenu, 0.8f + 0.2f * (leftScale));
                    ViewHelper.setTranslationX(mContent, (1 - scale));
                    ViewHelper.setPivotX(mContent, 0);
                    ViewHelper.setPivotY(mContent, mContent.getMeasuredHeight() / 2);
                    mContent.invalidate();
                    ViewHelper.setScaleX(mContent, right);
                    ViewHelper.setScaleY(mContent, right);
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.LEFT);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    public void setfocus(View v) {
        v.setFocusable(true);
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.findFocus();
    }

    public String getTime(boolean b) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat(b ? "yyyy-MM-dd" : "yyyyMMdd");
        Date curDate = new Date();
        Log.e("date", curDate.toString());
        String str = format.format(curDate);
        return str;
    }

    public String getTimesecond() {
        Date curDate = new Date();
        Long time = curDate.getTime();
        return time + "";
    }

    public String getBaseUrl() {
        return BasicShareUtil.getInstance(mContext).getBaseURL();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Log.e("base","侧滑");
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }

    //侧滑监听
    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }


    //使状态栏透明并沉浸到activity
    protected void initBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            //获取顶级窗口
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN      //全屏标志,布局侵入
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION    //标志布局会侵入到导航栏下
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;            //保持稳定
            decorView.setSystemUiVisibility(option);                //设置系统UI可见属性

            getWindow().setStatusBarColor(Color.TRANSPARENT);       //设置状态栏颜色透明
            getWindow().setNavigationBarColor(Color.TRANSPARENT);   //设置导航栏颜色透明

            //设置状态栏为半透明
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //设置导航栏为半透明
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |
                    localLayoutParams.flags);
        }

    }
    //防止点击过快的替换类Button
    public abstract  class NoDoubleClickListener implements View.OnClickListener{
        public static final int MIN_CLICK_DELAY_TIME = 1500;//间隔多少秒
        private long lastClickTime = 0;

        @Override
        public void onClick(View v) {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                lastClickTime = currentTime;
                Lg.e("点击OK");
                onNoDoubleClick(v);
            }else{
                Toast.showText(mContext,"别点太快");
                Lg.e("太快了");
            }
        }
        protected abstract void onNoDoubleClick(View view);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == 139) {
//            if (event.getRepeatCount() == 0) {
//                mReader.scan();
//            }
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        if (keyCode == 139) {
//            mReader.stopScan();
//        }
//
//        return super.onKeyUp(keyCode, event);
//    }
}

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
import android.os.Handler;
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
import com.fangzuo.assist.Service.NoticService;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.EventBusInfoCode;
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
import com.symbol.scanning.BarcodeManager;
import com.symbol.scanning.ScanDataCollection;
import com.symbol.scanning.Scanner;
import com.symbol.scanning.ScannerException;
import com.symbol.scanning.ScannerInfo;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    public boolean canScan=true;//用于限制pda扫码，BaseActivity时也会初始化为true
    public void lockScan(int lock){//0:解锁，1：锁住
        if (lock==0){
            Lg.e("解锁");
            canScan=true;
        }else{
            Lg.e("锁定");
            canScan=false;
        }
    }

    //u8000
    private ScanDevice sm;
    private BroadcastReceiver mScanDataReceiver;
//    = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            byte[] barocode = intent.getByteArrayExtra("barocode");
//            int barocodelen = intent.getIntExtra("length", 0);
//            byte temp = intent.getByteExtra("barcodeType", (byte) 0);
//            Log.i("debug", "----codetype--" + temp);
//            barcodeStr = new String(barocode, 0, barocodelen);
//            OnReceive(barcodeStr);
//        }
//    };

    //c5000
    private BroadcastReceiver mScanDataReceiverFor5000;
//    = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (action.equals("com.android.scanservice.scancontext")) {
//                barcodeStr = intent.getStringExtra("Scan_context");
//                OnReceive(barcodeStr);
//            }
//        }
//    };

    //G02A
    private static final String ACTION_DISPLAY_SCAN_RESULT = "techain.intent.action.DISPLAY_SCAN_RESULT";
    private BroadcastReceiver mScanDataReceiverForG02A;
//            = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (action.equals(ACTION_DISPLAY_SCAN_RESULT)) {
//                barcodeStr = intent.getStringExtra("decode_data");
//                OnReceive(barcodeStr);
//            }
//        }
//    };

    //  M60
    private static final String ACTION_M60 = "com.mobilead.tools.action.scan_result";
    private BroadcastReceiver mScanDataReceiverForM60;
//    = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (action.equals(ACTION_M60)) {
//                barcodeStr = intent.getStringExtra("decode_rslt");
//                OnReceive(barcodeStr);
//            }
//        }
//    };

    //新大陆
    private static final String ACTION_XDL_SCAN_RESULT = "nlscan.action.SCANNER_RESULT";
    private BroadcastReceiver mScanDataReceiverForXDL;
// = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (action.equals(ACTION_XDL_SCAN_RESULT)) {
//                barcodeStr = intent.getStringExtra("SCAN_BARCODE1");
//                OnReceive(barcodeStr);
//            }
//        }
//    };

//    public Barcode2DWithSoft.ScanCallback mScanCallback = new Barcode2DWithSoft.ScanCallback() {
//        @Override
//        public void onScanComplete(int i, int length, byte[] data) {
//            Log.i("ErDSoftScanFragment", "onScanComplete() i=" + i);
//            barcodeStr = new String(data);
//            OnReceive(barcodeStr);
//        }
//    };


//    //UBX
    private ScanManager mScanManager;
    private BroadcastReceiver mScanDataReceiverUBX;
    private void initUBXScan() {
        // TODO Auto-generated method stub
        try{
            mScanManager = new ScanManager();
            mScanManager.openScanner();
            mScanManager.switchOutputMode(0);
            SoundPool soundpool = new SoundPool(1, AudioManager.STREAM_NOTIFICATION, 100);
            int soundid = soundpool.load("/etc/Scan_new. ", 1);
            Log.e("OnResume","OnResume");
            IntentFilter filter = new IntentFilter();
            int[] idbuf = new int[]{PropertyID.WEDGE_INTENT_ACTION_NAME, PropertyID.WEDGE_INTENT_DATA_STRING_TAG};
            String[] value_buf = mScanManager.getParameterString(idbuf);
            if(value_buf != null && value_buf[0] != null && !value_buf[0].equals("")) {
                filter.addAction(value_buf[0]);
            } else {
                filter.addAction(ScanManager.ACTION_DECODE);
            }

            registerReceiver(mScanDataReceiverUBX, filter);
        }catch (RuntimeException stub){
            Lg.e("初始化扫描器失败");
        }

    }

    //M80s
    public static final String ACTION_START_DECODE = "com.mobilead.tools.action.scan_start";
    public static final String SCAN_RESULT_BROADCAST = "com.mobilead.tools.action.scan_result";
    // extra data
    public static final String EXTRA_BARCODE_STRING = "decode_rslt";
    public static final String EXTRA_BARCODE_TYPE = "decode_type";
    private BroadcastReceiver mScanDataReceiverForM80s;

    //肖邦
    private BroadcastReceiver mScanDataReceiverForXB;
    public static final String SCN_CUST_EX_SCODE = "scannerdata";
    public static final String SCN_CUST_ACTION_SCODE = "com.android.server.scannerservice.broadcast";

    //M71
    // Constants for Broadcast Receiver defined below.
    public static final String ACTION_BROADCAST_RECEIVER = "com.android.decodewedge.decode_action";
    public static final String CATEGORY_BROADCAST_RECEIVER = "com.android.decodewedge.decode_category";
    // extra data
    public static final String EXTRA_BARCODE_DATA = "com.android.decode.intentwedge.barcode_data";
    public static final String EXTRA_BARCODE_STRING_M71 = "com.android.decode.intentwedge.barcode_string";
    //    public static final String EXTRA_BARCODE_TYPE = "com.android.decode.intentwedge.barcode_type";
//    public static final String ACTION_START_DECODE = "com.datalogic.decode.action.START_DECODE";
//    public static final String ACTION_STOP_DECODE = "com.datalogic.decode.action.STOP_DECODE";
    private BroadcastReceiver mScanDataReceiverForM71 = null;

    //M82
    public static final String EXTRA_BARCODE_STRING_M82 = "com.android.decode.intentwedge.barcode_string";
    public static final String ACTION_BROADCAST_RECEIVER_M82 = "com.android.decodewedge.decode_action";
    public static final String CATEGORY_BROADCAST_RECEIVER_M82 = "com.android.decodewedge.decode_category";
    public static final String ACTION_START_DECODE_M82 = "com.datalogic.decode.action.START_DECODE";
    private BroadcastReceiver mScanDataReceiverForM82 = null;


    private String date;
    private int year;
    private int month;
    private int day;
//    private Handler handlerForNotice;

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
//        handlerForNotice = new Handler();
        //UBX
//        initScan();

        initView();
        initData();
        initListener();
    }
    //用于下推时判断是否为手机，显示扫码按钮
    public boolean isPhoneScan(){
        if (App.PDA_Choose==9){
            return true;
        }
        return false;
    }

//    public void startRunGetNotice(){
//        App.gettingNotice = true;
//        handlerForNotice.postDelayed(runGetNotice(),5000);
//    }
//
//    private Runnable runnable;
//    private Runnable runGetNotice(){
//        return runnable = new Runnable() {
//            @Override
//            public void run() {
//                NoticService.startNotic(mContext,ShareUtil.getInstance(mContext).getsetUserID(),getTime(true));
//                handlerForNotice.postDelayed(runGetNotice(),5000);
//            }
//        };
//    }
@Override
protected void onResume() {
    // TODO Auto-generated method stub
    super.onResume();
    Lg.e("onResume");
//    if (App.gettingNotice){
//        handlerForNotice.postDelayed(runGetNotice(),5000);
//    }
    try{
        switch (App.PDA_Choose){
            case 1://G02A
                if (null==mScanDataReceiverForG02A){
                    mScanDataReceiverForG02A = new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            String action = intent.getAction();
                            if (action.equals(ACTION_DISPLAY_SCAN_RESULT)) {
                                barcodeStr = intent.getStringExtra("decode_data");
                                OnReceive(barcodeStr);
                            }
                        }
                    };
                }
                IntentFilter scanDataIntentFilter = new IntentFilter();
                scanDataIntentFilter.addAction(ACTION_DISPLAY_SCAN_RESULT);
                registerReceiver(mScanDataReceiverForG02A, scanDataIntentFilter);
                break;
            case 2://u8000
                if (null==mScanDataReceiver){
                    mScanDataReceiver = new BroadcastReceiver() {
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
                }
                sm = new ScanDevice();
                IntentFilter filter = new IntentFilter();
                filter.addAction("scan.rcv.message");
                registerReceiver(mScanDataReceiver, filter);
                break;
            case 3://5000
                if (null==mScanDataReceiverFor5000){
                    mScanDataReceiverFor5000 = new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            String action = intent.getAction();
                            if (action.equals("com.android.scanservice.scancontext")) {
                                barcodeStr = intent.getStringExtra("Scan_context");
                                OnReceive(barcodeStr);
                            }
                        }
                    };
                }
                IntentFilter filter5000 = new IntentFilter();
                filter5000.addAction("scan.rcv.message");
                filter5000.addAction("com.android.scanservice.scancontext");
                registerReceiver(mScanDataReceiverFor5000, filter5000);
                break;
            case 4://M60
                if (null==mScanDataReceiverForM60){
                    mScanDataReceiverForM60 = new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            String action = intent.getAction();
                            if (action.equals(ACTION_M60)) {
                                barcodeStr = intent.getStringExtra("decode_rslt");
                                OnReceive(barcodeStr);
                            }
                        }
                    };
                }
                IntentFilter filter60 = new IntentFilter();
                filter60.addAction(ACTION_M60);
                registerReceiver(mScanDataReceiverForM60, filter60);
                break;
            case 5://新大陆注册");
                if (null==mScanDataReceiverForXDL){
                    mScanDataReceiverForXDL = new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            String action = intent.getAction();
                            if (action.equals(ACTION_XDL_SCAN_RESULT)) {
                                barcodeStr = intent.getStringExtra("SCAN_BARCODE1");
                                OnReceive(barcodeStr);
                            }
                        }
                    };
                }
                IntentFilter xdlFilter = new IntentFilter();
                xdlFilter.addAction(ACTION_XDL_SCAN_RESULT);
                registerReceiver(mScanDataReceiverForXDL, xdlFilter);
                break;
            case 6://M36:注意：不能再该设备的Scan Config程序中添加本程序，不然会导致无法识别崩溃
                if (null== mBarcodeManager)mBarcodeManager = new BarcodeManager();
                if (null == mInfo)mInfo =new ScannerInfo("se4710_cam_builtin", "DECODER_2D");
                if (null == mScanner)mScanner = mBarcodeManager.getDevice(mInfo);
                try
                {
                    mScanner.enable();
                    setDecodeListener();
                }
                catch(ScannerException se)
                {
                    se.printStackTrace();
                }
                break;
            case 7://M80s");
                if (null==mScanDataReceiverForM80s){
                    mScanDataReceiverForM80s = new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            String action = intent.getAction();
                            if (action.equals(SCAN_RESULT_BROADCAST)) {
                                // Read content of result intent.
                                barcodeStr = intent.getStringExtra(EXTRA_BARCODE_STRING);
                                OnReceive(barcodeStr);
//                            barcodeType = intent.getStringExtra(EXTRA_BARCODE_TYPE);

                            }
                        }
                    };
                }
                IntentFilter FilterM80s = new IntentFilter();
                FilterM80s.addAction(SCAN_RESULT_BROADCAST);
                registerReceiver(mScanDataReceiverForM80s, FilterM80s);
                break;
            case 8://肖邦");
                if (null==mScanDataReceiverForXB){
                    mScanDataReceiverForXB = new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            if (intent.getAction().equals(SCN_CUST_ACTION_SCODE)) {
                                try {
                                    barcodeStr = intent.getStringExtra(SCN_CUST_EX_SCODE).toString();
                                    OnReceive(barcodeStr);
                                } catch (Exception e) {
                                    // TODO: handle exception
                                    e.printStackTrace();
                                    Log.e("in", e.toString());
                                }
                            }
                        }
                    };
                }
                IntentFilter FilterXB = new IntentFilter();
                FilterXB.addAction(SCN_CUST_ACTION_SCODE);
                registerReceiver(mScanDataReceiverForXB, FilterXB);
                break;
            case 10://优博讯");
                if (null==mScanDataReceiverUBX){
                    mScanDataReceiverUBX = new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            byte[] barcode = intent.getByteArrayExtra(ScanManager.DECODE_DATA_TAG);
                            int barcodelen = intent.getIntExtra(ScanManager.BARCODE_LENGTH_TAG, 0);
                            byte temp = intent.getByteExtra(ScanManager.BARCODE_TYPE_TAG, (byte) 0);
                            Log.i("debug", "----codetype--" + temp);
                            barcodeStr = new String(barcode, 0, barcodelen);
                            OnReceive(barcodeStr);

                        }
                    };
                }
                initUBXScan();
                break;
            case 11://M71");
                if (null==mScanDataReceiverForM71){
                    mScanDataReceiverForM71 = new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            if (intent.getAction().equals(ACTION_BROADCAST_RECEIVER)) {
                                try {
                                    barcodeStr = intent.getStringExtra(EXTRA_BARCODE_STRING_M71);
//                                    barcodeType = intent.getStringExtra(EXTRA_BARCODE_TYPE);
                                    if (barcodeStr != null) {
                                        OnReceive(barcodeStr);
                                    }
                                } catch (Exception e) {
                                    // TODO: handle exception
                                    e.printStackTrace();
                                    Log.e("in", e.toString());
                                }
                            }
                        }
                    };
                }
                IntentFilter FilterM71 = new IntentFilter();
                FilterM71.addAction(ACTION_BROADCAST_RECEIVER);
                FilterM71.addCategory(CATEGORY_BROADCAST_RECEIVER);
                registerReceiver(mScanDataReceiverForM71, FilterM71);
                break;
            case 12://M82");
                if (null==mScanDataReceiverForM82){
                    mScanDataReceiverForM82 = new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            if (intent.getAction().equals(ACTION_BROADCAST_RECEIVER_M82)) {
                                try {
                                    barcodeStr = intent.getStringExtra(EXTRA_BARCODE_STRING_M82);
                                    if (barcodeStr != null) {
                                        OnReceive(barcodeStr);
                                    }
                                } catch (Exception e) {
                                    // TODO: handle exception
                                    e.printStackTrace();
                                    Log.e("in", e.toString());
                                }
                            }
                        }
                    };
                }

                IntentFilter FilterM82 = new IntentFilter();
                FilterM82.addAction(ACTION_BROADCAST_RECEIVER_M82);
                FilterM82.addCategory(CATEGORY_BROADCAST_RECEIVER_M82);
                registerReceiver(mScanDataReceiverForM82, FilterM82);
                break;
        }
    }catch (Exception e){
        DataService.pushError(mContext, this.getClass().getSimpleName(), e);
    }
}
    @Override
    protected void onPause() {
        super.onPause();
        Lg.e("opPause");
//        if (null!=handlerForNotice && null !=runnable){
//            handlerForNotice.removeCallbacks(runnable);
//        }
        try{
            if (App.PDA_Choose==6){
                try
                {
                    if(!canDecode){
                        mScanner.cancelRead();
                    }
                    mScanner.disable();
                }
                catch(ScannerException se)
                {
                    se.printStackTrace();
                }
                finally
                {
                    mScanner.removeDataListener(mDataListener);
                    canDecode = true;
                }
            }

            if (App.PDA_Choose==1 && null!=mScanDataReceiverForG02A)unregisterReceiver(mScanDataReceiverForG02A);
            if (App.PDA_Choose==2 && null!=mScanDataReceiver)unregisterReceiver(mScanDataReceiver);
            if (App.PDA_Choose==3 && null!=mScanDataReceiverFor5000)unregisterReceiver(mScanDataReceiverFor5000);
            if (App.PDA_Choose==4 && null!=mScanDataReceiverForM60)unregisterReceiver(mScanDataReceiverForM60);
            if (App.PDA_Choose==5 && null!=mScanDataReceiverForXDL)unregisterReceiver(mScanDataReceiverForXDL);
            if (App.PDA_Choose==7 && null!=mScanDataReceiverForM80s)unregisterReceiver(mScanDataReceiverForM80s);
            if (App.PDA_Choose==8 && null!=mScanDataReceiverForXB)unregisterReceiver(mScanDataReceiverForXB);
            if (App.PDA_Choose==10 && null!=mScanDataReceiverUBX)unregisterReceiver(mScanDataReceiverUBX);
            if (App.PDA_Choose==11 && null!=mScanDataReceiverForM71)unregisterReceiver(mScanDataReceiverForM71);
            if (App.PDA_Choose==12 && null!=mScanDataReceiverForM82)unregisterReceiver(mScanDataReceiverForM82);

        }catch (Exception e){
            DataService.pushError(mContext, this.getClass().getSimpleName(), e);
        }

//        }
    }

    //M36-------------------------------------------------------------------------------------------------------
    private BarcodeManager mBarcodeManager;
    private ScannerInfo mInfo;
    private Scanner mScanner;
    //    private List<ScannerInfo> scanInfoList = mBarcodeManager.getSupportedDevicesInfo();
    private Scanner.DataListener mDataListener;
    private boolean canDecode = true;
    public void setDecodeListener()
    {
        mDataListener =  new Scanner.DataListener()
        {
            public void onData(ScanDataCollection scanDataCollection)
            {
                String data = "";
                ArrayList<ScanDataCollection.ScanData> scanDataList = scanDataCollection.getScanData();
                for(ScanDataCollection.ScanData scanData :scanDataList)
                {
                    data = scanData.getData();
                }
                final String finalData = data;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        OnReceive(finalData);
                    }
                });
                canDecode = true;
            }
        };

        mScanner.addDataListener(mDataListener);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if((keyCode == KeyEvent.KEYCODE_BUTTON_L1)
                || (keyCode == KeyEvent.KEYCODE_BUTTON_R1)
                || (keyCode == KeyEvent.KEYCODE_BUTTON_L2))
        {
//            Log.i("ScanApp", "onKeyDown");
            if((App.PDA_Choose==6 && canDecode && event.getRepeatCount() == 0))
            {
                canDecode = false;
                try
                {
                    mScanner.read();
                }
                catch (ScannerException se)
                {
                    se.printStackTrace();
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        if((keyCode == KeyEvent.KEYCODE_BUTTON_L1)
                || (keyCode == KeyEvent.KEYCODE_BUTTON_R1)
                || (keyCode == KeyEvent.KEYCODE_BUTTON_L2))
        {
//            Log.i("ScanApp", "onKeyUp");
            if(App.PDA_Choose==6 && !canDecode)
            {
                try
                {
                    mScanner.cancelRead();
                }
                catch (ScannerException se)
                {
                    se.printStackTrace();
                }
                canDecode = true;
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }
////------------------------------------------------------------------------------------------------

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

    //隐藏键盘
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 隐藏软键盘(可用于Activity，Fragment)
     */
    public static void hideSoftKeyboard(Context context, List<View> viewList) {
        if (viewList == null) return;

        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);

        for (View v : viewList) {
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
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

}

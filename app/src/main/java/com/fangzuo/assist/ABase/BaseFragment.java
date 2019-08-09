package com.fangzuo.assist.ABase;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanDevice;
import android.device.ScanManager;
import android.device.scanner.configuration.PropertyID;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Service.DataService;
import com.fangzuo.assist.Utils.EventBusUtil;
import com.fangzuo.assist.Utils.Toast;

import java.util.Calendar;

/**
 * Created by NB on 2017/7/28.
 */

public abstract class BaseFragment extends Fragment {
    private static final String ACTION_DISPLAY_SCAN_RESULT = "techain.intent.action.DISPLAY_SCAN_RESULT";
    private FragmentActivity mContext;
    private String barcodeStr;

    public void onCreate(Bundle savedInstanceState) {
        FragmentActivity mContext = getActivity();
        year = Calendar.getInstance().get(Calendar.YEAR);
        month = Calendar.getInstance().get(Calendar.MONTH);
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        super.onCreate(savedInstanceState);
//        registerBroadCast(mScanDataReceiver);
    }
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

    //M80s
    public static final String ACTION_START_DECODE = "com.mobilead.tools.action.scan_start";
    public static final String SCAN_RESULT_BROADCAST = "com.mobilead.tools.action.scan_result";
    // extra data
    public static final String EXTRA_BARCODE_STRING = "decode_rslt";
    public static final String EXTRA_BARCODE_TYPE = "decode_type";
    private BroadcastReceiver mScanDataReceiverForM80s  = new BroadcastReceiver() {
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

    //肖邦
    public static final String SCN_CUST_EX_SCODE = "scannerdata";
    public static final String SCN_CUST_ACTION_SCODE = "com.android.server.scannerservice.broadcast";
    private BroadcastReceiver mScanDataReceiverForXB = new BroadcastReceiver() {
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
//            getActivity().registerReceiver(mScanDataReceiver, filter);
//        }catch (RuntimeException stub){
//            stub.printStackTrace();
//        }
//
//    }


//        //u8000
//    private ScanDevice sm;
//    private BroadcastReceiver mScanDataReceiver = new BroadcastReceiver() {
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
//
//    //c50002
//    private BroadcastReceiver mScanDataReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (action.equals("com.android.scanservice.scancontext")) {
//                String str = intent.getStringExtra("Scan_context");
//                OnReceive(str);
//            }
//        }
//    };

//    public void registerBroadCast(BroadcastReceiver mScanDataReceiver){
//        IntentFilter scanDataIntentFilter = new IntentFilter();
//        scanDataIntentFilter.addAction(ACTION_DISPLAY_SCAN_RESULT);
//        getActivity().registerReceiver(mScanDataReceiver, scanDataIntentFilter);
//
//    }
public void registerBroadCast(BroadcastReceiver mScanDataReceiver) {
    IntentFilter scanDataIntentFilter = new IntentFilter();
    scanDataIntentFilter.addAction("com.android.scanservice.scancontext");
    getActivity().registerReceiver(mScanDataReceiver, scanDataIntentFilter);

}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);

    }
//    public void registerBroadCast(BroadcastReceiver mScanDataReceiver){
//        IntentFilter scanDataIntentFilter = new IntentFilter();
//        scanDataIntentFilter.addAction(ACTION_DISPLAY_SCAN_RESULT);
//        mContext.registerReceiver(mScanDataReceiver, scanDataIntentFilter);
//
//    }
    @Override
    public void onResume() {
        super.onResume();




//        //UBX
//        initScan();

//        //u8000
//        sm = new ScanDevice();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction("scan.rcv.message");
//        mContext.registerReceiver(mScanDataReceiver, filter);

//        //5000
//        IntentFilter filter = new IntentFilter();
//        filter.addAction("scan.rcv.message");
//        filter.addAction("com.android.scanservice.scancontext");
//        mContext.registerReceiver(mScanDataReceiver, filter);


//        //G02A
//        IntentFilter scanDataIntentFilter = new IntentFilter();
//        scanDataIntentFilter.addAction(ACTION_DISPLAY_SCAN_RESULT);
//        getActivity().registerReceiver(mScanDataReceiver, scanDataIntentFilter);

//        if (App.PDA_Choose!=4){
            if (App.PDA_Choose == 1) {
                //G02A
                IntentFilter scanDataIntentFilter = new IntentFilter();
                scanDataIntentFilter.addAction(ACTION_DISPLAY_SCAN_RESULT);
                getActivity().registerReceiver(mScanDataReceiverForG02A, scanDataIntentFilter);
            } else if (App.PDA_Choose==2){
                //u8000
                sm = new ScanDevice();
                IntentFilter filter = new IntentFilter();
                filter.addAction("scan.rcv.message");
                getActivity().registerReceiver(mScanDataReceiver, filter);
            }else if (App.PDA_Choose==3){
                //5000
                IntentFilter filter = new IntentFilter();
                filter.addAction("scan.rcv.message");
                filter.addAction("com.android.scanservice.scancontext");
                getActivity().registerReceiver(mScanDataReceiverFor5000, filter);
            }else if (App.PDA_Choose==4){
                //M60
                IntentFilter scanDataIntentFilter = new IntentFilter();
                scanDataIntentFilter.addAction(ACTION_M60);
                getActivity().registerReceiver(mScanDataReceiverForM60, scanDataIntentFilter);
            }else if (App.PDA_Choose==5) {
                //新大陆注册");
                IntentFilter xdlFilter = new IntentFilter();
                xdlFilter.addAction(ACTION_XDL_SCAN_RESULT);
                getActivity().registerReceiver(mScanDataReceiverForXDL, xdlFilter);
            }else if (App.PDA_Choose==7) {
                IntentFilter FilterM80s = new IntentFilter();
                FilterM80s.addAction(SCAN_RESULT_BROADCAST);
                getActivity().registerReceiver(mScanDataReceiverForM80s, FilterM80s);
            }else if (App.PDA_Choose==8) {
                IntentFilter FilterXB = new IntentFilter();
                FilterXB.addAction(SCN_CUST_ACTION_SCODE);
                getActivity().registerReceiver(mScanDataReceiverForXB, FilterXB);
            }
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        mContext.unregisterReceiver(mScanDataReceiver);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();
//        registerBroadCast(mScanDataReceiver);
        initView();
        initData();
        initListener();
    }
    protected abstract void initView();
    protected abstract void OnReceive(String barCode);
    protected abstract void initData();
    protected abstract void initListener();

    public final void startNewActivity(Class<? extends Activity> target,
                                       Bundle mBundle) {
        Intent mIntent = new Intent(getActivity(), target);
        if (mBundle != null) {
            mIntent.putExtras(mBundle);
        }
        startActivity(mIntent);
    }
    public int year;
    public int month;
    public int day;
    private String date;
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
    @Override
    public void onDestroy() {
        super.onDestroy();
        try{
//            if (App.PDA_Choose!=4){
                if (mScanDataReceiver != null ||
                        mScanDataReceiverForG02A != null||
                        mScanDataReceiverFor5000 != null||
                        mScanDataReceiverForXDL != null||
                        mScanDataReceiverForM60 != null) {
                    if (App.PDA_Choose == 1) {
                        getActivity().unregisterReceiver(mScanDataReceiverForG02A);
                    } else if (App.PDA_Choose==2){
                        getActivity().unregisterReceiver(mScanDataReceiver);
                    }else if (App.PDA_Choose == 3){
                        getActivity().unregisterReceiver(mScanDataReceiverFor5000);
                    }else if (App.PDA_Choose == 4){
                        getActivity().unregisterReceiver(mScanDataReceiverForM60);
                    }else if (App.PDA_Choose == 5){
                        getActivity().unregisterReceiver(mScanDataReceiverForXDL);
//                Toast.showText(mContext,"关闭H100设备");
//                if (mReader != null) {
//                    mReader.close();
//                }
                    }else if (App.PDA_Choose == 7){
                        getActivity().unregisterReceiver(mScanDataReceiverForM80s);
                    }else if (App.PDA_Choose == 8){
                        getActivity().unregisterReceiver(mScanDataReceiverForXB);
                    }

                }
//            }
        }catch (Exception e){
            DataService.pushError(mContext, this.getClass().getSimpleName(), e);
        }

    }
}

package com.fangzuo.assist.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.TimeBean;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Utils.Asynchttp;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.MD5;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.assist.databinding.ActivitySplashBinding;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.orhanobut.hawk.Hawk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class SplashActivity extends AppCompatActivity  implements EasyPermissions.PermissionCallbacks {

    private SplashActivity mContext;
    private BasicShareUtil instance;
    private long serverTime;
    private String register_code;
    private String newRegister;
    private String lastRegister;
    private ActivitySplashBinding binding;
    ArrayAdapter<String> adapter;
    private String string;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        mContext = this;
        getPermisssion();
        if (getNewMac() != null && !getNewMac().equals("")) {
            binding.tvCode.setText("注册码：" + MD5.getMD5(getNewMac()));
            Lg.e("注册码："+MD5.getMD5(getNewMac()));
            register_code = MD5.getMD5(getNewMac()) + "fzkj601";
            newRegister = MD5.getMD5(register_code);
            lastRegister = MD5.getMD5(newRegister);
            Hawk.put(Config.PDA_IMIE, lastRegister);
            Hawk.put(Config.PDA_RegisterCode, MD5.getMD5(getNewMac()));
        } else {
            Toast.showText(mContext, "请链接WIFI");
        }
        initData();
        initListener();
    }


    private void RegisterState() {
        App.getRService().doIOAction(WebApi.REGISTER, lastRegister, new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
//                super.onNext(commonResponse);
                if (!commonResponse.state)return;
                instance.setRegisterState(true);
                startNewActivity(LoginActivity.class, R.anim.activity_slide_left_in, R.anim.activity_slide_left_out, true, null);
            }

            @Override
            public void onError(Throwable e) {
                if (e.getMessage().equals("1")) {
                    instance.setRegisterState(false);
                } else {
                    startNewActivity(LoginActivity.class, R.anim.activity_slide_left_in, R.anim.activity_slide_left_out, true, null);
                }
            }
        });
//        Asynchttp.post(mContext, getBaseUrl() + WebApi.REGISTER, lastRegister, new Asynchttp.Response() {
//            @Override
//            public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
//                instance.setRegisterState(true);
//                startNewActivity(LoginActivity.class, R.anim.activity_slide_left_in, R.anim.activity_slide_left_out, true, null);
//            }
//
//            @Override
//            public void onFailed(String Msg, AsyncHttpClient client) {
//                if (Msg.equals("1")) {
//                    instance.setRegisterState(false);
//                } else {
//                    startNewActivity(LoginActivity.class, R.anim.activity_slide_left_in, R.anim.activity_slide_left_out, true, null);
//                }
//            }
//        });
    }


    public void initData() {
        //创建ArrayAdapter对象
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Config.PDA_Type);
        binding.spPda.setAdapter(adapter);
        instance = BasicShareUtil.getInstance(mContext);
        CheckServer();
        getServerTime();
        if (serverTime > instance.getTryTime()) {
            instance.setTryState(2);
        }
        if (instance.getRegisterState()) {
            RegisterState();
        }
        if (instance.getRegisterState()) {
            Log.e("RegisterState", instance.getRegisterState() + "");
            startNewActivity(LoginActivity.class, R.anim.activity_slide_left_in, R.anim.activity_slide_left_out, true, null);
        } else if (instance.getTryState() == 1) {
            Log.e("TryState", instance.getTryState() + "");
            startNewActivity(LoginActivity.class, R.anim.activity_slide_left_in, R.anim.activity_slide_left_out, true, null);
        }

    }


    public void initListener() {
        binding.ipchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewActivity(IpPortActivity.class, 0, 0, false, null);
            }
        });
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("请选择设备型号".equals(string)){
                    Toast.showText(mContext, "请选择设备型号");
                }else{
                    instance.setMAC(lastRegister);
                    Asynchttp.post(mContext, getBaseUrl() + WebApi.REGISTER, lastRegister, new Asynchttp.Response() {
                        @Override
                        public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                            instance.setRegisterState(true);
                            Toast.showText(mContext, "注册成功");
                            startNewActivity(LoginActivity.class, R.anim.activity_slide_left_in, R.anim.activity_slide_left_out, true, null);
                        }

                        @Override
                        public void onFailed(String Msg, AsyncHttpClient client) {
                            Toast.showText(mContext, Msg);
                            if (Msg.equals("1")) {
                                Toast.showText(mContext, "请联系软件供应商注册");
                            }
                        }
                    });
                }

            }
        });
        binding.btnTry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("请选择设备型号".equals(string)){
                    Toast.showText(mContext, "请选择设备型号");
                }else{
                    if (instance.getTryState() == 0) {
                        getServerTime();
                        if (getFileFromSD().equals("")) {
                            long tryTime = serverTime + 2592000000L;
                            writeSdCard("1");
                            instance.setTryTime(tryTime);
                            instance.setTryState(1);
                            startNewActivity(LoginActivity.class, R.anim.activity_slide_left_in, R.anim.activity_slide_left_out, true, null);
                        } else if (getFileFromSD().equals("1")) {
                            Toast.showText(mContext, "每台机器只能申请一次试用");
                        }
                    } else if (instance.getTryState() == 2) {
                        Toast.showText(mContext, "每台机器只能申请一次试用");
                    }
                }

            }
        });
        binding.spPda.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                string = adapter.getItem(i);
//                private String[] arr={"G02A设备","8000设备","5000设备"手机端};
                if ("G02A设备".equals(string)) {
                    Hawk.put(Config.PDA,1);
                    App.PDA_Choose =1;
                    Toast.showText(mContext,"选择了G02A设备"+App.PDA_Choose);
                } else if ("8000设备".equals(string)) {
                    Hawk.put(Config.PDA,2);
                    App.PDA_Choose =2;
                    Toast.showText(mContext,"选择了8000设备"+App.PDA_Choose);
                } else if ("5000设备".equals(string)) {
                    Hawk.put(Config.PDA,3);
                    App.PDA_Choose =3;
                    Toast.showText(mContext,"选择了5000设备"+App.PDA_Choose);
                }else if ("M60".equals(string)){
                    Hawk.put(Config.PDA,4);
                    App.PDA_Choose =4;
                    Toast.showText(mContext,"选择了M60"+App.PDA_Choose);
                }else if ("新大陆".equals(string)){
                    Hawk.put(Config.PDA,5);
                    App.PDA_Choose =5;
                    Toast.showText(mContext,"选择了新大陆"+App.PDA_Choose);
                } else if ("M36".equals(string)) {
                    Hawk.put(Config.PDA,6);
                    App.PDA_Choose =6;
                    Toast.showText(mContext,"选择了M36"+App.PDA_Choose);
                } else if ("M80s".equals(string)) {
                    Hawk.put(Config.PDA,7);
                    App.PDA_Choose =7;
                    Toast.showText(mContext,"M80s"+App.PDA_Choose);
                }else if ("肖邦".equals(string)) {
                    Hawk.put(Config.PDA, 8);
                    App.PDA_Choose =8;
                    Toast.showText(mContext, "选择了肖邦" + App.PDA_Choose);
                }else if ("手机端".equals(string)) {
                    Hawk.put(Config.PDA, 9);
                    App.PDA_Choose =9;
                    Toast.showText(mContext, "选择了手机端" + App.PDA_Choose);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void getServerTime() {
        App.getRService().doIOAction("GetServerTime", "", new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
//                super.onNext(commonResponse);
                if (!commonResponse.state)return;
                TimeBean tBean = new Gson().fromJson(commonResponse.returnJson, TimeBean.class);
                serverTime = tBean.time;
            }

            @Override
            public void onError(Throwable e) {
//                super.onError(e);
                Toast.showText(mContext, e.getMessage());

            }
        });
//        Asynchttp.post(mContext, instance.getBaseURL() + "GetServerTime", "", new Asynchttp.Response() {
//            @Override
//            public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
//                TimeBean tBean = new Gson().fromJson(cBean.returnJson, TimeBean.class);
//                serverTime = tBean.time;
//            }
//
//            @Override
//            public void onFailed(String Msg, AsyncHttpClient client) {
//                Toast.showText(mContext, Msg);
//            }
//        });
    }

    private static String getNewMac() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    private void CheckServer() {
        if ((instance.getIP()).equals("") || (instance.getPort()).equals("")) {
            AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
            ab.setTitle("未获取到服务器,请输入服务器地址");
            View v = LayoutInflater.from(mContext).inflate(R.layout.ipport, null);
            final EditText mEdIp = v.findViewById(R.id.ed_ip);
            final EditText mEdPort = v.findViewById(R.id.ed_port);

            mEdIp.setText("192.168.0.19");
            mEdPort.setText("8080");

            ab.setView(v);
            ab.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (!(mEdIp.getText().toString()).equals("") && !(mEdPort.getText().toString()).equals("")) {
                        instance.setIP(mEdIp.getText().toString());
                        instance.setPort(mEdPort.getText().toString());
                    } else {
                        System.exit(0);
                    }
                }
            });
            ab.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    System.exit(0);
                }
            });
            ab.setCancelable(false);
            ab.create().show();
        }
    }


    private void writeSdCard(String person) {
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "json"
                + File.separator + "0x8b69a33.txt");
        // 文件夹不存在的话，就创建文件夹
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        // 写入内存卡
        PrintStream outputStream = null;
        try {
            outputStream = new PrintStream(new FileOutputStream(file));
            outputStream.print(person);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    private String getFileFromSD() {
        String result = "";
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "json"
                + File.separator + "0x8b69a33.txt");
        if (file.exists()) {
            try {
                FileInputStream f = new FileInputStream(file);
                BufferedReader bis = new BufferedReader(new InputStreamReader(f));
                String line = "";
                while ((line = bis.readLine()) != null) {
                    result += line;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;

    }


//    @OnClick({R.id.btn_try, R.id.btn_register})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.btn_try:
//                if (instance.getTryState() == 0) {
//                    getServerTime();
//                    if (getFileFromSD().equals("")) {
//                        long tryTime = serverTime + 2592000000L;
//                        writeSdCard("1");
//                        instance.setTryTime(tryTime);
//                        instance.setTryState(1);
//                        startNewActivity(LoginActivity.class, R.anim.activity_slide_left_in, R.anim.activity_slide_left_out, true, null);
//                    } else if (getFileFromSD().equals("1")) {
//                        Toast.showText(mContext, "每台机器只能申请一次试用");
//                    }
//                } else if (instance.getTryState() == 2) {
//                    Toast.showText(mContext, "每台机器只能申请一次试用");
//                }
//                break;
//            case R.id.btn_register:
//                instance.setMAC(lastRegister);
//                Asynchttp.post(mContext, getBaseUrl() + WebApi.REGISTER, lastRegister, new Asynchttp.Response() {
//                    @Override
//                    public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
//                        instance.setRegisterState(true);
//                        Toast.showText(mContext, "注册成功");
//                        startNewActivity(LoginActivity.class, R.anim.activity_slide_left_in, R.anim.activity_slide_left_out, true, null);
//                    }
//
//                    @Override
//                    public void onFailed(String Msg, AsyncHttpClient client) {
//                        Toast.showText(mContext, Msg);
//                        if (Msg.equals("1")) {
//                            Toast.showText(mContext, "请联系软件供应商注册");
//                        }
//                    }
//                });
//                break;
//        }
//    }

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

    public String getBaseUrl() {
        return BasicShareUtil.getInstance(mContext).getBaseURL();
    }



    //权限获取-------------------------------------------------------------
    private void getPermisssion() {
        String[] perm = {
                Manifest.permission.CAMERA,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(mContext, perm)) {
            EasyPermissions.requestPermissions(this, "必要的权限", 0, perm);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //把申请权限的回调交由EasyPermissions处理
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.i("permisssion", "获取成功的权限" + perms);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.i("permisssion", "获取失败的权限" + perms);
    }
}

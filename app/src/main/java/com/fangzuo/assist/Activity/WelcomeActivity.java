package com.fangzuo.assist.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.EventBusUtil;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.MD5;
import com.fangzuo.assist.Utils.RegisterUtil;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.widget.LoadingUtil;
import com.orhanobut.hawk.Hawk;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.EasyPermissions;

public class WelcomeActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    @BindView(R.id.pg)
    ProgressBar pg;
    @BindView(R.id.tv_try)
    TextView tvTry;
    @BindView(R.id.tv_mac)
    TextView tvMac;
    private String lastRegister = "";

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveEvent(ClassEvent event) {
        switch (event.Msg) {
            case EventBusInfoCode.Register_Result://注册信息反馈
                String result = (String) event.postEvent;
                if ("OK".equals(result)) {
                    try {
                        EventBusUtil.unregister(this);
                    } catch (Exception e) {
                    }
                    BasicShareUtil.getInstance(App.getContext()).setRegisterState(true);
                    startNewActivity(LoginActivity.class, R.anim.activity_slide_left_in, R.anim.activity_slide_left_out, true, null);
                } else {
                    tvTry.setVisibility(View.VISIBLE);
                    LoadingUtil.showAlter(WelcomeActivity.this, "提示", result);
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        getPermisssion();
        // 避免从桌面启动程序后，会重新实例化入口类的activity----------------------------
        //也就是说Home键再点app图标时，不会重新从登陆界面进
        if (!this.isTaskRoot()) {
            Intent intent = getIntent();
            if (intent != null) {
                String action = intent.getAction();
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                    finish();
                    return;
                }
            }
        }
        //当注册过之后，直接跳过并进入登陆界面（登陆界面也有检测是否存在注册码)
        if (!BasicShareUtil.getInstance(App.getContext()).getRegisterState()) {
            EventBusUtil.register(this);
            //Mac地址第一次MD5为注册码
            String mac = RegisterUtil.getNewMac();
            if (!"".equals(mac)) {
//            binding.tvCode.setText("注册码：" + MD5.getMD5(mac));
//            Lg.e("注册码:"+MD5.getMD5(mac));
                String register_code = MD5.getMD5(mac) + "fzkj601";
                String newRegister = MD5.getMD5(register_code);
                lastRegister = MD5.getMD5(newRegister);
                Hawk.put(Config.PDA_IMIE, lastRegister);
                Hawk.put(Config.PDA_RegisterCode, MD5.getMD5(mac));
                tvMac.setText("用户码:"+lastRegister);
                checkDlg();
            } else {
                Toast.showText(App.getContext(), "请链接WIFI");
            }
        } else {
            startNewActivity(LoginActivity.class, R.anim.activity_slide_left_in, R.anim.activity_slide_left_out, true, null);
        }
//-----------------------------------

        //重试按钮
        tvTry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDlg();
            }
        });
    }

    private void checkDlg() {
        AlertDialog.Builder ab = new AlertDialog.Builder(WelcomeActivity.this);
        ab.setTitle("请输入服务器地址");
        View v = LayoutInflater.from(WelcomeActivity.this).inflate(R.layout.ipport, null);
        final EditText mEdIp = v.findViewById(R.id.ed_ip);
        final EditText mEdPort = v.findViewById(R.id.ed_port);
        mEdIp.setText(BasicShareUtil.getInstance(App.getContext()).getIP().equals("") ? "192.168.0.19" : BasicShareUtil.getInstance(App.getContext()).getIP());
        mEdPort.setText(BasicShareUtil.getInstance(App.getContext()).getPort().equals("") ? "8080" : BasicShareUtil.getInstance(App.getContext()).getPort());

        ab.setView(v);
        ab.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!(mEdIp.getText().toString()).equals("") && !(mEdPort.getText().toString()).equals("")) {
                    BasicShareUtil.getInstance(App.getContext()).setIP(mEdIp.getText().toString());
                    BasicShareUtil.getInstance(App.getContext()).setPort(mEdPort.getText().toString());
                    RegisterUtil.getRegiterMaxNum(lastRegister);
//                        checkRegister();
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


    @Override
    protected void onPause() {
        super.onPause();
        Lg.e("welcome onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Lg.e("welcome Destroy");

    }

    //权限获取-------------------------------------------------------------
    private void getPermisssion() {
        String[] perm = {
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(WelcomeActivity.this, perm)) {
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

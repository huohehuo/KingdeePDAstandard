package com.fangzuo.assist.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.Beans.UseTimeBean;
import com.fangzuo.assist.Dao.User;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Service.DataService;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.CommonUtil;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.Info;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.RegisterUtil;
import com.fangzuo.assist.Utils.ShareUtil;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.assist.widget.LoadingUtil;
import com.fangzuo.assist.widget.SpinnerUser;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.UserDao;
import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;
import com.symbol.scanning.BarcodeManager;
import com.symbol.scanning.ScanDataCollection;
import com.symbol.scanning.Scanner;
import com.symbol.scanning.ScannerException;
import com.symbol.scanning.ScannerInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

import static com.fangzuo.assist.Utils.CommonUtil.dealTime;

public class LoginActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {


    @BindView(R.id.isRemPass)
    CheckBox isRemPass;
    @BindView(R.id.ver)
    TextView ver;
    @BindView(R.id.ed_pass)
    EditText mEtPassword;
    @BindView(R.id.isOL)
    CheckBox mCbisOL;
    @BindView(R.id.btn_setting)
    Button mBtnSetting;
    @BindView(R.id.btn_login)
    Button btnLogin;
    //    private Button mBtnLogin;
//    private Button mBtnSetting;
    private LoginActivity mContext;
    @BindView(R.id.sp_login)
    SpinnerUser spinner;
    private DaoSession session;
    private String userName = "";
    private String userID = "";
    private List<User> users;
    private BasicShareUtil share;
    private boolean isOL;
    private String userPass;
    private UserDao userDao;

    @Override
    public void initView() {
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mContext = this;
        initBar();
        share = BasicShareUtil.getInstance(mContext);
        session = GreenDaoManager.getmInstance(mContext).getDaoSession();
        userDao = session.getUserDao();
        getPermisssion();
        ver.setText("标准版 Ver:" + getVersionName());
        Lg.e("PDA：" + App.PDA_Choose);
        isRemPass.setChecked(Hawk.get(Info.IsRemanber, false));
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint({"HardwareIds", "MissingPermission"}) String deviceId = tm.getDeviceId();
        Log.e("IMIE", deviceId);
        share.setIMIE(deviceId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        spinner.LoadUser();
        DataService.updateTime(mContext);
        DownLoadUseTime();
        //检查是否存在注册码
//        RegisterUtil.checkHasRegister();
    }

    //获取配置文件中的时间数据
    private void DownLoadUseTime() {
        App.getRService().doIOAction(WebApi.GetUseTime, "获取时间", new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                super.onNext(commonResponse);
                LoadingUtil.dismiss();
                if (!commonResponse.state) return;
                UseTimeBean bean = gson.fromJson(commonResponse.returnJson, UseTimeBean.class);
                if (Integer.parseInt(getTime(false)) < Integer.parseInt(bean.nowTime)) {
                    Toast.showText(mContext, "PDA本地时间与服务器时间有误，请调整好时间");
                    Hawk.put(Config.SaveTime, bean);
                    return;
                } else {
                    if (Integer.parseInt(getTime(false)) > Integer.parseInt(dealTime(bean.endTime))) {
                        Toast.showText(mContext, "软件已过期，请联系供应商提供服务");
                        Hawk.put(Config.SaveTime, bean);
                        return;
                    } else {
                        Lg.e("获取起止时间：" + commonResponse.returnJson);
                        Hawk.put(Config.SaveTime, bean);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                LoadingUtil.dismiss();
//                    Hawk.put(Config.SaveTime,null);
                Toast.showText(mContext, e.toString());
                Lg.e("错误：" + e.toString());
            }
        });


//        Lg.e("本地配置数据；",Hawk.get(Config.SettingData,new DownloadReturnBean().new SetFile()));
//
//        Asynchttp.post(mContext,Config.Setting_Url, "sdfa", new Asynchttp.Response() {
//            @Override
//            public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
//                Lg.e("配置数据：",cBean);
//                Lg.e("配置数据：",cBean.returnJson);
//                DownloadReturnBean dBean = JsonCreater.gson.fromJson(cBean.returnJson, DownloadReturnBean.class);
//                Lg.e("配置解析：",dBean);
//                Lg.e("配置解析：",dBean.serverTime);
//                for (int i = 0; i < dBean.setFiles.size(); i++) {
//                    if (getApplication().getPackageName().equals(dBean.setFiles.get(i).AppID)){
//                        Lg.e("存在App：",dBean.setFiles.get(i));
//                        Hawk.put(Config.SettingData,dBean.setFiles.get(i));
//                    }
//                }
//            }
//
//            @Override
//            public void onFailed(String Msg, AsyncHttpClient client) {
//                Lg.e("配置解析错误："+Msg);
//            }
//        });
    }

    //检测是否符合时间要求
    private boolean checkTime() {
        if (null == Hawk.get(Config.SaveTime, null)) {
            LoadingUtil.showDialog(mContext, "正在获取配置信息...");
            DownLoadUseTime();
            return false;
        } else {
            UseTimeBean bean = Hawk.get(Config.SaveTime);
            if (Integer.parseInt(getTime(false)) < Integer.parseInt(bean.nowTime)) {
                Toast.showText(mContext, "PDA本地时间与服务器时间有误，请调整好时间");
                return false;
            } else {
                if (Integer.parseInt(getTime(false)) > Integer.parseInt(dealTime(bean.endTime))) {
                    Toast.showText(mContext, "软件已过期，请联系供应商提供服务");
                    return false;
                } else {
                    return true;
                }
            }
        }
    }


    @Override
    public void initData() {
//        getUserInfo();
        mCbisOL.setChecked(BasicShareUtil.getInstance(mContext).getIsOL());
        //自动设置上次的用户
        spinner.setAutoSelection(Info.AutoLogin, "");


    }

    @Override
    public void initListener() {
        isRemPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Hawk.put(Info.IsRemanber, b);
            }
        });
        mCbisOL.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isOL = b;
                BasicShareUtil.getInstance(mContext).setIsOL(b);
//                users = userDao.loadAll();
//                LoginSpAdapter ada = new LoginSpAdapter(mContext, users);
//                spinner.setAdapter(ada);
//                ada.notifyDataSetChanged();
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Lg.e("选中用户：" + users.get(i).toString());
                User user = (User) spinner.getAdapter().getItem(i);
                Lg.e("用户：", user);
                userName = user.FName;
                userID = user.FUserID;
                userPass = user.FPassWord;
                //设置下次默认选择的用户
                Hawk.put(Info.AutoLogin, userName);
                //设置该用户密码
                mEtPassword.setText(Hawk.get(userName, ""));
                ShareUtil.getInstance(mContext).setUserName(userName);
                ShareUtil.getInstance(mContext).setUserID(userID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected void OnReceive(String code) {
        Toast.showText(mContext, "测试扫码:" + code);
        Log.e("CODE", code + ":获得的code");
    }

    @OnClick({R.id.btn_login, R.id.btn_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                Login();
                break;
            case R.id.btn_setting:
                Bundle b = new Bundle();
                b.putInt("flag", 0);
                startNewActivity(SettingMenuActivity.class, R.anim.activity_slide_left_in, R.anim.activity_slide_left_out, false, null);
                break;
        }
    }

    private void Login() {
        if (!checkTime()) {
            DownLoadUseTime();
//            Toast.showText(mContext,"验证信息失败");
            return;
        }
        if (!userID.equals("") && !userName.equals("")) {
            if (mEtPassword.getText().toString().equals(userPass)) {
                ShareUtil.getInstance(mContext).setUserName(userName);
                ShareUtil.getInstance(mContext).setUserID(userID);
                if (isRemPass.isChecked()) {
                    //保存该用户的密码
                    Hawk.put(userName, mEtPassword.getText().toString());
                } else {
                    Hawk.put(userName, "");
                }
                startNewActivity(MenuActivity.class, R.anim.activity_slide_left_in, R.anim.activity_slide_left_out, false, null);
            } else {
                Toast.showText(mContext, "请输入正确的登录信息");
            }

        } else {
            Toast.showText(mContext, "请下载用户配置信息");
        }
    }


    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(ClassEvent event) {
        switch (event.Msg) {
            case EventBusInfoCode.Down_json_file://下载配置文件结果
                String str = (String) event.postEvent;
                if ("OK".equals(str)) {
                    String string = CommonUtil.getString(App.JsonFile);
                    Lg.e("数据：" + string);
                    DownloadReturnBean dBean = new Gson().fromJson(string, DownloadReturnBean.class);
                    Lg.e("解析：", dBean);

                } else {
                    Toast.showText(mContext, "下载失败");
                }

                break;
            case EventBusInfoCode.Register_Result://注册信息反馈
                String result = (String) event.postEvent;
                if ("OK".equals(result)) {
                    btnLogin.setClickable(true);
                    btnLogin.setText("登陆");
                    Lg.e("成功注册");
//                    BasicShareUtil.getInstance(App.getContext()).setRegisterState(true);
//                    startNewActivity(LoginActivity.class, R.anim.activity_slide_left_in, R.anim.activity_slide_left_out, true, null);
                } else {
                    Lg.e("生成dlg000000");
                    btnLogin.setClickable(false);
                    btnLogin.setText("未注册");
                    AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                    ab.setTitle("提示");
                    ab.setMessage(result);
                    ab.setPositiveButton("注册", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            RegisterUtil.getRegiterMaxNum(Hawk.get(Config.PDA_IMIE, ""));

                        }
                    });
                    ab.setNegativeButton("取消", null);
                    ab.setNeutralButton("关闭程序", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    });
                    final AlertDialog alertDialog = ab.create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();
//                    LoadingUtil.showAlter(LoginActivity.this, "提示", result);
                }
                break;

        }
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

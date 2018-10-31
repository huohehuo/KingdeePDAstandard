package com.fangzuo.assist.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Adapter.LoginSpAdapter;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Dao.User;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.Asynchttp;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.Info;
import com.fangzuo.assist.Utils.JsonCreater;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.ShareUtil;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.assist.widget.LoadingUtil;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.UserDao;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.EasyPermissions;

public class LoginActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {


    @BindView(R.id.isRemPass)
    CheckBox isRemPass;
    private Button mBtnLogin;
    private Button mBtnSetting;
    private LoginActivity mContext;
    private CommonListener commonListener;
    @BindView(R.id.sp_login)
    Spinner spinner;
    private DaoSession session;
    private String userName = "";
    private String userID = "";
    private List<User> users;
    private CheckBox mCbisOL;
    private BasicShareUtil share;
    private boolean isOL;
    private String userPass;
    private EditText edPass;
    private EditText mEtPassword;
    private TextView mTvVersion;

    @Override
    public void initView() {
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mContext = this;
        initBar();
        share = BasicShareUtil.getInstance(mContext);
        mEtPassword = findViewById(R.id.ed_pass);
        mBtnLogin = findViewById(R.id.btn_login);
        mBtnSetting = findViewById(R.id.btn_setting);
        mCbisOL = findViewById(R.id.isOL);
        mTvVersion = findViewById(R.id.ver);
        getPermisssion();
        Log.e("123", ShareUtil.getInstance(mContext).getPISpayMethod() + "");
        mTvVersion.setText("标准版 Ver:" + getVersionName());
        Lg.e("PDA：" + App.PDA_Choose);
        isRemPass.setChecked(Hawk.get(Info.IsRemanber,false));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getListUser();

    }

    //获取用户数据
    private void getListUser() {
        Lg.e("getListUser");
        final Gson gson = new Gson();
        ArrayList<Integer> chooseAll = new ArrayList<>();
        chooseAll.add(12);
        LoadingUtil.show(mContext, "正在预加载...请稍后...", true);
        String json = JsonCreater.DownLoadData(share.getDatabaseIp(),
                share.getDatabasePort(), share.getDataBaseUser(), share.getDataBasePass(),
                share.getDataBase(), share.getVersion(), chooseAll);
        Asynchttp.post(mContext, getBaseUrl() + WebApi.DOWNLOADDATA, json, new Asynchttp.Response() {
            @Override
            public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                Log.e(TAG, "获取用户数据：" + cBean.returnJson);
                DownloadReturnBean dBean = gson.fromJson(cBean.returnJson, DownloadReturnBean.class);
                UserDao userDao = session.getUserDao();
                userDao.deleteAll();
                userDao.detachAll();
                userDao.insertInTx(dBean.User);
                LoadingUtil.dismiss();
                getUserInfo();
            }

            @Override
            public void onFailed(String Msg, AsyncHttpClient client) {
                LoadingUtil.dismiss();
            }
        });
    }


    @Override
    public void initData() {
        commonListener = new CommonListener();
        getUserInfo();
        mCbisOL.setChecked(BasicShareUtil.getInstance(mContext).getIsOL());

    }

    private void getUserInfo() {
        session = GreenDaoManager.getmInstance(mContext).getDaoSession();
        UserDao userDao = session.getUserDao();
        users = userDao.loadAll();
        LoginSpAdapter ada = new LoginSpAdapter(mContext, users);
        spinner.setAdapter(ada);
        ada.notifyDataSetChanged();
        if (users.size() > 0) {
            userName = users.get(0).FName;
            userID = users.get(0).FUserID;
            ShareUtil.getInstance(mContext).setUserName(userName);
            ShareUtil.getInstance(mContext).setUserID(userID);
        }
        //自动设置上次的用户
        for (int i = 0; i < ada.getCount(); i++) {
            if (((User)ada.getItem(i)).FName.equals(Hawk.get(Info.AutoLogin,""))){
                spinner.setSelection(i);
            }
        }

    }

    @Override
    public void initListener() {
        isRemPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Hawk.put(Info.IsRemanber,b);
            }
        });
        mCbisOL.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isOL = b;
                BasicShareUtil.getInstance(mContext).setIsOL(b);
                session = GreenDaoManager.getmInstance(mContext).getDaoSession();
                UserDao userDao = session.getUserDao();
                users = userDao.loadAll();
                LoginSpAdapter ada = new LoginSpAdapter(mContext, users);
                spinner.setAdapter(ada);
                ada.notifyDataSetChanged();
            }
        });
        mBtnLogin.setOnClickListener(commonListener);
        mBtnSetting.setOnClickListener(commonListener);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Lg.e("选中用户："+users.get(i).toString());
                userName = users.get(i).FName;
                userID = users.get(i).FUserID;
                userPass = users.get(i).FPassWord;
                //设置下次默认选择的用户
                Hawk.put(Info.AutoLogin,userName);
                //设置该用户密码
                mEtPassword.setText(Hawk.get(userName, ""));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected void OnReceive(String code) {
        Log.e("CODE", code + ":获得的code");
    }


    private class CommonListener implements View.OnClickListener {

        @Override
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
    }

    private void Login() {
        if (!userID.equals("") && !userName.equals("")) {
            if (mEtPassword.getText().toString().equals(userPass)) {
                ShareUtil.getInstance(mContext).setUserName(userName);
                ShareUtil.getInstance(mContext).setUserID(userID);
                if (isRemPass.isChecked()){
                    //保存该用户的密码
                    Hawk.put(userName,mEtPassword.getText().toString());
                }else{
                    Hawk.put(userName,"");
                }
                startNewActivity(MenuActivity.class, R.anim.activity_slide_left_in, R.anim.activity_slide_left_out, false, null);
            } else {
                Toast.showText(mContext, "请输入正确的登录信息");
            }

        } else {
            Toast.showText(mContext, "请下载用户配置信息");
        }
    }


    //权限获取-------------------------------------------------------------
    private void getPermisssion() {
        String[] perm = {
                Manifest.permission.CAMERA,
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

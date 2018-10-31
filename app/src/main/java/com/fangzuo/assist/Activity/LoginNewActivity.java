package com.fangzuo.assist.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Adapter.LoginSpAdapter;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Dao.User;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.AnimUtil;
import com.fangzuo.assist.Utils.Asynchttp;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.JsonCreater;
import com.fangzuo.assist.Utils.ShareUtil;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.assist.widget.LoadingUtil;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.UserDao;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class LoginNewActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {


    @BindView(R.id.cv_login)
    CardView cvLogin;
    @BindView(R.id.cv_setting)
    CardView cvSetting;
    @BindView(R.id.btn_link)
    Button btnLink;
    @BindView(R.id.ver)
    TextView mTvVersion;
    @BindView(R.id.ed_pass)
    EditText mEtPassword;
    @BindView(R.id.isOL)
    CheckBox mCbisOL;
    @BindView(R.id.tv_ip_port)
    TextView tvIpPort;

    private LoginNewActivity mContext;
    @BindView(R.id.sp_login)
    Spinner spinner;
    private DaoSession session;
    private String userName = "";
    private String userID = "";
    private List<User> users;
    //    private CheckBox mCbisOL;
    private BasicShareUtil share;
    private boolean isOL;
    private String userPass;


    @Override
    public void initView() {
        setContentView(R.layout.activity_login_new);
        ButterKnife.bind(this);
        mContext = this;
        share = BasicShareUtil.getInstance(mContext);
        getPermisssion();
        Log.e("123", ShareUtil.getInstance(mContext).getPISpayMethod() + "");
        getListUser();
        mTvVersion.setText("标准版 Ver:" + getVersionName());
    }

    //获取用户数据
    private void getListUser() {
        final Gson gson = new Gson();
        LoadingUtil.show(mContext,"正在预加载...",true);
        ArrayList<Integer> chooseAll = new ArrayList<>();
        chooseAll.add(12);
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
            }

            @Override
            public void onFailed(String Msg, AsyncHttpClient client) {
                LoadingUtil.dismiss();
            }
        });
    }


    @Override
    public void initData() {
        getUserInfo();
        mCbisOL.setChecked(BasicShareUtil.getInstance(mContext).getIsOL());
        tvIpPort.setText(BasicShareUtil.getInstance(mContext).getIP()+"\n"+BasicShareUtil.getInstance(mContext).getPort());
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

    }

    @Override
    public void initListener() {
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
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                userName = users.get(i).FName;
                userID = users.get(i).FUserID;
                userPass = users.get(i).FPassWord;
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

    @OnClick({R.id.tv_go_login, R.id.tv_go_set, R.id.btn_login, R.id.btn_setting, R.id.btn_changeip})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_go_login:
                if (cvLogin.getVisibility() == View.GONE) {
                    AnimUtil.FlipAnimatorXViewShow(cvSetting, cvLogin, 200);
                }
                break;
            case R.id.tv_go_set:
                if (cvSetting.getVisibility() == View.GONE) {
                    AnimUtil.FlipAnimatorXViewShow(cvLogin, cvSetting, 200);
                }
                break;
            case R.id.btn_login:
                Login();
                break;
            case R.id.btn_changeip:
                AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                ab.setTitle("请输入服务器地址");
                View v = LayoutInflater.from(mContext).inflate(R.layout.ipport, null);
                final EditText mEdIp = v.findViewById(R.id.ed_ip);
                final EditText mEdPort = v.findViewById(R.id.ed_port);
                mEdIp.setText(BasicShareUtil.getInstance(mContext).getIP());
                mEdPort.setText(BasicShareUtil.getInstance(mContext).getPort());
                ab.setView(v);
                ab.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!(mEdIp.getText().toString()).equals("") && !(mEdPort.getText().toString()).equals("")) {
                            BasicShareUtil.getInstance(mContext).setIP(mEdIp.getText().toString());
                            BasicShareUtil.getInstance(mContext).setPort(mEdPort.getText().toString());
                        }
                    }
                });
                ab.setNegativeButton("取消", null);
                ab.setCancelable(false);
                ab.create().show();
                break;
            case R.id.btn_setting:
                Bundle b = new Bundle();
                b.putInt("flag", 0);
                startNewActivity(SettingMenuActivity.class, R.anim.activity_slide_left_in, R.anim.activity_slide_left_out, true, null);
                break;
        }
    }


    private void Login() {
        if (!userID.equals("") && !userName.equals("")) {
            if (mEtPassword.getText().toString().equals(userPass)) {
                ShareUtil.getInstance(mContext).setUserName(userName);
                ShareUtil.getInstance(mContext).setUserID(userID);
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

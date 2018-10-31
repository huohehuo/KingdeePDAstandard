package com.fangzuo.assist.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Adapter.SettingListAdapter;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.NewVersionBean;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.Asynchttp;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.WebApi;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.loopj.android.http.AsyncHttpClient;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.fangzuo.assist.Utils.GetSettingList.getList;

//静态调用了fragment
public class SettingMenuActivity extends BaseActivity {


//    @BindView(R.id.lv_setting)
//    ListView lvSetting;
//    private ProgressDialog pDialog;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_setting_menu);
        mContext = this;
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
//        SettingListAdapter ada = new SettingListAdapter(mContext,getList());
//        lvSetting.setAdapter(ada);
//        ada.notifyDataSetChanged();
    }

    @Override
    protected void initListener() {
//        lvSetting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                switch (i){
//                    case 0:
//                        startNewActivity(SettingActivity.class,0,0,false,null);
//                        break;
//                    case 1:
//                        startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
//                        break;
//                    case 2:
//                        startActivity(new Intent(Settings.ACTION_SOUND_SETTINGS));
//                        break;
//                    case 3:
//                        checkNewVersion();
//                        break;
//                    case 4:
//                        startActivity(new Intent(mContext,IpPortActivity.class));
//                        break;
//                    case 5:
//                        startActivity(new Intent(mContext,TestActivity.class));
//                        break;
//                }
//
//            }
//        });
    }

    @Override
    protected void OnReceive(String code) {

    }

//    private void checkNewVersion() {
//        Asynchttp.post(mContext, getBaseUrl() + WebApi.GETNEWVERSION, "", new Asynchttp.Response() {
//            @Override
//            public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
//                final NewVersionBean nBean = new Gson().fromJson(cBean.returnJson,NewVersionBean.class);
//                if(nBean.Version>getVersionCode()){
//                    AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
//                    ab.setTitle("发现新版本");
//                    ab.setMessage(nBean.Rem);
//                    ab.setPositiveButton("更新", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            DownLoad(nBean.downLoadURL);
//                        }
//                    });
//                    ab.setNegativeButton("取消",null);
//                    ab.create().show();
//                }else{
//                    Toast.showText(mContext,"无新版本");
//                }
//            }
//
//            @Override
//            public void onFailed(String Msg, AsyncHttpClient client) {
//                Toast.showText(mContext,Msg);
//            }
//        });
//    }

//    private void DownLoad(String downLoadURL) {
//        if (Environment.getExternalStorageState().equals(
//                Environment.MEDIA_MOUNTED)) {
//
//            pDialog = new ProgressDialog(mContext);
//            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            pDialog.setTitle("下载中");
//            pDialog.show();
//            String target = Environment.getExternalStorageDirectory()
//                    + "/ScanAssist.apk";
//            HttpUtils utils = new HttpUtils();
//
//            utils.download(downLoadURL, target, new RequestCallBack<File>() {
//
//                @Override
//                public void onLoading(long total, long current,
//                                      boolean isUploading) {
//                    super.onLoading(total, current, isUploading);
//                    System.out.println("下载进度:" + current + "/" + total);
//                    pDialog.setProgress((int) (current*100/total));
//                }
//
//                @Override
//                public void onSuccess(ResponseInfo<File> arg0) {
//                    pDialog.dismiss();
//                    System.out.println("下载完成");
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.addCategory(Intent.CATEGORY_DEFAULT);
//                    intent.setDataAndType(Uri.fromFile(arg0.result),
//                            "application/vnd.android.package-archive");
//                    startActivityForResult(intent, 0);
//                }
//
//                @Override
//                public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
//                    pDialog.dismiss();
//                    Toast.showText(mContext, "下载失败");
//                }
//
//
//            });
//        } else {
//            pDialog.dismiss();
//            Toast.showText(mContext, "正在安装");
//
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
//        startNewActivity(LoginActivity.class,0,0,true,null);
    }
}

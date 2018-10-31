package com.fangzuo.assist.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fangzuo.assist.ABase.BaseFragment;
import com.fangzuo.assist.Activity.IpPortActivity;
import com.fangzuo.assist.Activity.SettingActivity;
import com.fangzuo.assist.Activity.TestActivity;
import com.fangzuo.assist.Adapter.SettingListAdapter;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.NewVersionBean;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.Asynchttp;
import com.fangzuo.assist.Utils.BasicShareUtil;
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
import butterknife.Unbinder;

import static com.fangzuo.assist.Utils.GetSettingList.getList;


public class SettingFragment extends BaseFragment {
    @BindView(R.id.lv)
    ListView lv;
    private Unbinder bind;
    private FragmentActivity mContext;
    private ProgressDialog pDialog;
    public SettingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_setting, container, false);
        bind = ButterKnife.bind(this, v);
        return v;
    }


    @Override
    public void initView() {
        mContext = getActivity();

    }

    @Override
    protected void OnReceive(String barCode) {

    }

    @Override
    protected void initData() {
        SettingListAdapter ada = new SettingListAdapter(mContext,getList());
        lv.setAdapter(ada);
        ada.notifyDataSetChanged();
    }

    @Override
    protected void initListener() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Bundle b= new Bundle();
                        b.putInt("flag",100);
                        startNewActivity(SettingActivity.class,b);
                        break;
                    case 1:
                        startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
                        break;
                    case 2:
                        startActivity(new Intent(Settings.ACTION_SOUND_SETTINGS));
                        break;
                    case 3:
                        checkNewVersion();
                        break;
                    case 4:
                        startActivity(new Intent(mContext, IpPortActivity.class));
                        break;
                    case 5:
                        startActivity(new Intent(mContext, TestActivity.class));
                        break;
                }

            }
        });
    }

    public String getBaseUrl(){
        return BasicShareUtil.getInstance(mContext).getBaseURL();
    }
    public int getVersionCode() {
        //androidmanifest中获取版本名称
        PackageManager packageManager = mContext.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    mContext.getPackageName(), 0);

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
    }
    private void checkNewVersion() {
        Asynchttp.post(mContext, getBaseUrl() + WebApi.GETNEWVERSION, "", new Asynchttp.Response() {
            @Override
            public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                final NewVersionBean nBean = new Gson().fromJson(cBean.returnJson,NewVersionBean.class);
                if(nBean.Version>getVersionCode()){
                    AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                    ab.setTitle("发现新版本");
                    ab.setMessage(nBean.Rem);
                    ab.setPositiveButton("更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DownLoad(nBean.downLoadURL);
                        }
                    });
                    ab.setNegativeButton("取消",null);
                    ab.create().show();
                }else{
                    Toast.showText(mContext,"无新版本");
                }
            }

            @Override
            public void onFailed(String Msg, AsyncHttpClient client) {
                Toast.showText(mContext,Msg);
            }
        });
    }

    private void DownLoad(String downLoadURL) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {

            pDialog = new ProgressDialog(mContext);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setTitle("下载中");
            pDialog.show();
            String target = Environment.getExternalStorageDirectory()
                    + "/ScanAssist.apk";
            HttpUtils utils = new HttpUtils();

            utils.download(downLoadURL, target, new RequestCallBack<File>() {

                @Override
                public void onLoading(long total, long current,
                                      boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                    System.out.println("下载进度:" + current + "/" + total);
                    pDialog.setProgress((int) (current*100/total));
                }

                @Override
                public void onSuccess(ResponseInfo<File> arg0) {
                    pDialog.dismiss();
                    System.out.println("下载完成");
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setDataAndType(Uri.fromFile(arg0.result),
                            "application/vnd.android.package-archive");
                    startActivityForResult(intent, 0);
                }

                @Override
                public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
                    pDialog.dismiss();
                    Toast.showText(mContext, "下载失败");
                }


            });
        } else {
            pDialog.dismiss();
            Toast.showText(mContext, "正在安装");

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}

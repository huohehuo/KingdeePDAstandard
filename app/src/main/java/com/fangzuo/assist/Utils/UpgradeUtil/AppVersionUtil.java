package com.fangzuo.assist.Utils.UpgradeUtil;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;

import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Utils.Asynchttp;
import com.fangzuo.assist.Utils.CommonUtil;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.Info;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.widget.LoadingUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.loopj.android.http.AsyncHttpClient;
import com.orhanobut.hawk.Hawk;

import java.io.File;

public class AppVersionUtil {
    public static String BaseUrl = "http://148.70.108.65:8080/LogAssist/getApkVersion";
//    public static String BaseUrl = "http://192.168.0.136:8084/Assist/getApkVersion";
    public static void CheckVersion(final Context context) {
        String pack = context.getPackageName();
        Lg.e("本地包名",pack);
        Asynchttp.post(App.getContext(), BaseUrl, pack, new Asynchttp.Response() {
            @Override
            public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                Lg.e("得到版本信息",cBean.returnJson);
                showDlg(context,cBean.returnJson);
            }

            @Override
            public void onFailed(String Msg, AsyncHttpClient client) {
                Lg.e("查询版本信息错误");
            }
        });
    }

    //处理并展示更新提示信息
    private static UpgradeBean upgradeBean;
    private static void showDlg(final Context context, String json){
        try {
            upgradeBean = new Gson().fromJson(json, UpgradeBean.class);
        }catch (Exception e){
            return;
        }
        if (null != upgradeBean){
            //检测本地版本是否最新
            if (!checkLocVersion(upgradeBean, Info.getAppNo()))return;
            String versionLog;
            //处理多版本信息时，用于区分本地与最新的版本号作比较
//            if ("1".equals(Hawk.get(Config.PDA_Project_Type,"1"))){
                versionLog=upgradeBean.UpgradeLog;
//            }else{
//                versionLog=upgradeBean.UpgradeLog2;
//            }
            AlertDialog.Builder ab = new AlertDialog.Builder(context);
            ab.setTitle("更新提示");
            ab.setMessage(versionLog+"");
            ab.setPositiveButton("下载并更新", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Lg.e("更新");
//                    DownLoad(context,"http://148.70.108.65:8080/AppFile/GZWS/app-debug.apk");
                    DownLoad(context,upgradeBean);
                }
            });
            ab.setNeutralButton("取消",null);
            final AlertDialog alertDialog = ab.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }
    }

    /**
     *     对比本地版本号和服务器最新版本号
     * @param upgradeBean   版本信息对象
     * @param version       本地版本号
     * @return
     */
    private static boolean checkLocVersion(UpgradeBean upgradeBean,String version){
        try{
            String versionType;
            //处理多版本信息时，用于区分本地与最新的版本号作比较
//            if ("1".equals(Hawk.get(Config.PDA_Project_Type,"1"))){
                versionType=upgradeBean.AppVersion;
//            }else{
//                versionType=upgradeBean.AppVersion2;
//            }
            if (null!=version && !"".equals(version)){
                String verNew;
                String ver;
                Lg.e("最初："+version+"--"+versionType);
                if (version.contains(".")){
                    ver = version.replaceAll("\\.","");
                }else{
                    ver = version;
                }
                if (versionType.contains(".")){
                    verNew = versionType.replaceAll("\\.","");
                }else{
                    verNew = versionType;
                }
                Lg.e(verNew);
                Lg.e(ver);
                if (toInt(verNew)>toInt(ver)){
                    return true;
                }else{
                    Lg.e("版本无升级");
                    return false;
                }
            }else{
                Lg.e("本地版本信息有误");
                return false;
            }
        }catch (Exception e){
            return false;
        }

    }
    //防止强转时崩溃操作
    public static int toInt(String string) {
        if (null == string) {
            return 0;
        } else if (string.equals("")) {
            return 0;
        } else {
            try{
                return Integer.parseInt(string);
            }catch (Exception e){
                return 0;
            }
        }
    }
    //下载apk
    private static void DownLoad(final Context mContext, UpgradeBean upgradeBean) {
        LoadingUtil.dismiss();
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {

            final ProgressDialog pDialog;
            pDialog = new ProgressDialog(mContext);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setTitle("下载中");
            pDialog.show();
            String target = Environment.getExternalStorageDirectory()
                    + "/NewApp"+upgradeBean.UpgradeTime+".apk";
            HttpUtils utils = new HttpUtils();
            //处理多版本信息时，用于区分本地与最新的版本号作比较
            String versionUrl;
//            if ("1".equals(Hawk.get(Config.PDA_Project_Type,"1"))){
                versionUrl=upgradeBean.UpgradeUrl;
//            }else{
//                versionUrl=upgradeBean.UpgradeUrl2;
//            }
            Lg.e("更新地址"+versionUrl);
            utils.download(versionUrl, target, new RequestCallBack<File>() {

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
                    try{
                        CommonUtil.installApk(mContext,arg0.result+"");
//                        Intent intent = new Intent(Intent.ACTION_VIEW);
//                        intent.addCategory(Intent.CATEGORY_DEFAULT);
//                        intent.setDataAndType(Uri.fromFile(arg0.result),
//                                "application/vnd.android.package-archive");
//                        startActivityForResult(intent, 0);
                    }catch (Exception e){
                        try{
                            StringBuilder builder = new StringBuilder();
                            builder.append("请先退出本软件\n");
                            builder.append("进入PDA软件主页\n");
                            builder.append("选择文件管理器\n");
                            builder.append("找到文件NewApp\n");
                            builder.append("长按变色点击右下角重命名\n删去后面的数字\n");
                            builder.append("变成文件名：NewApp.apk\n");
                            builder.append("点击安装\n");
                            AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                            ab.setTitle("下载成功!\n请按操作重新安装APK");
                            ab.setMessage(builder.toString());
                            ab.setPositiveButton("确定", null);
                            ab.create().show();
                        }catch (Exception e1){

                        }
                    }
                }

                @Override
                public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
                    pDialog.dismiss();
                    Toast.showText(mContext, "下载失败");
                }


            });
        }
//        else {
//            pDialog.dismiss();
//            Toast.showText(mContext, "正在安装");
//
//        }
    }
}

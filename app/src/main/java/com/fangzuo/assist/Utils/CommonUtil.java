package com.fangzuo.assist.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.widget.LoadingUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CommonUtil {
    /*条码规则：
    物料条码.16位数

    批次条码:大于22位
                批次和备注  11位到空格是批次 后三位备注
    批次条码:大于16小于20位
               批次和备注  11位到空格是批次*/

    /*if (CommonUtil.ScanBack(code).size()>0){
            List<String> list = CommonUtil.ScanBack(code);
            edNum.setText(list.get(1));
            ScanBarCode(list.get(0));
        }*/
    public static List<String> ScanBack(String code) {
        List<String> list = new ArrayList<>();
        if (code.contains("/")) {
            String[] split = code.split("/", 3);
            Log.e("code:", split.length + "");
            if (split.length == 3) {
                String fcode = split[0];
                if (fcode.length() > 12) {
                    try {
                        String barcode = fcode.substring(0, 12);
                        list.add(barcode);
                        String num = fcode.substring(12, fcode.length());
                        list.add(num);
                        list.add(split[1]);
                        return list;
                    } catch (Exception e) {
                        Toast.showText(App.getContext(), "条码有误");
                        return new ArrayList<>();
                    }
                } else {
                    Toast.showText(App.getContext(), "条码有误");
                    return new ArrayList<>();
                }
            } else {
                Toast.showText(App.getContext(), "条码有误");
                return new ArrayList<>();
            }
        } else {
            Toast.showText(App.getContext(), "条码有误");
            return new ArrayList<>();
        }

//        List<String> list = new ArrayList<>();
//        if (code.length()>22){
//            String barcode = code.substring(0, 12);
//            list.add(barcode);
//            return list;
//        }else if (code.length()>16 && code.length()<20){
//            String barcode = code.substring(0, 12);
//            list.add(barcode);
//            return list;
//        }else{
//            return new ArrayList<>();
//        }

        // 大于12位的条码  前面12是条形码 后面为数量
        //角标以 0 未开始获取
//        if (code.length()>12){
//            try {
//                String barcode = code.substring(0, 12);
//                list.add(barcode);
//                String num = code.substring(12, code.length());
//                list.add(num);
//                return list;
//            } catch (Exception e) {
//                Toast.showText(App.getContext(), "条码有误");
//                return new ArrayList<>();
//            }
//        }else{
//            Toast.showText(App.getContext(), "条码有误");
//            return new ArrayList<>();
//        }
    }


    //生成单据编号
    public static long createOrderCode(Activity activity) {
        Long ordercode = 0l;
        ShareUtil share = ShareUtil.getInstance(activity.getApplicationContext());
        if (share.getOrderCode(activity) == 0) {
            ordercode = Long.parseLong(getTimeLong(false) + "001");
            share.setOrderCode(activity, ordercode);
        } else {
            //当不是当天时，生成新的单据，重新计算
            if (String.valueOf(share.getOrderCode(activity)).contains(getTime(false))) {
                ordercode = share.getOrderCode(activity);
            } else {
                ordercode = Long.parseLong(getTimeLong(false) + "001");
                share.setOrderCode(activity, ordercode);
            }
        }
        Log.e("生成新的单据:", ordercode + "");
        return ordercode;
    }

    public static String getTime(boolean b) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat(b ? "yyyy-MM-dd" : "yyyyMMdd");
        Date curDate = new Date();
        Log.e("date", curDate.toString());
        String str = format.format(curDate);
        return str;
    }

    public static String getTimeLong(boolean b) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat(b ? "yyyy-MM-dd-HH-mm-ss" : "yyyyMMddHHmmss");
        Date curDate = new Date();
        Log.e("date", curDate.toString());
        String str = format.format(curDate);
        return str;
    }

    //获取条码中的年份和周数确定日期
    public static String getDateFromScan(String strDate) {
        if (strDate.length() == 4) {
            String year = "20" + strDate.substring(0, 2);
            String week = strDate.substring(2, 4);
            int day = (Integer.parseInt(week) - 1) * 7 + 3;
            Lg.e("year：" + year);
            Lg.e("week：" + week);
            Lg.e("天数：" + day);
            Calendar cld = Calendar.getInstance();
            cld.set(Calendar.YEAR, Integer.parseInt(year));
            cld.set(Calendar.MONTH, 0);
            cld.set(Calendar.DATE, 0);
            //调用Calendar类中的add()，增加时间量
            cld.add(Calendar.DATE, day);
            Date date = cld.getTime();
            SimpleDateFormat format = new SimpleDateFormat(true ? "yyyy-MM-dd" : "yyyyMMdd");
            Log.e("date", date.toString());
            String str = format.format(date);
            Lg.e("date:" + str);
            return str;
        }

/*//           System.out.println("增加100天的日期为："+cld.get(Calendar.YEAR)+"年"+cld.get(Calendar.MONTH)+"月"+cld.get(Calendar.DATE)+"日");
  //知道年份，第几周，周几，计算日期
Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2018); // 2016年
        cal.set(Calendar.WEEK_OF_YEAR, 53); // 设置为2016年的第10周
        cal.set(Calendar.DAY_OF_WEEK, 4); // 1表示周日，2表示周一，7表示周六
        Date date = cal.getTime();
        SimpleDateFormat format = new SimpleDateFormat(true ? "yyyy-MM-dd" : "yyyyMMdd");
        Log.e("date", date.toString());
        String str = format.format(date);//确定2019年第二周的周一的日期
        Lg.e("date:"+str);*/

        return getTime(true);
    }

    //读取本地下载好的txt数据包，解析
    public static String getString(String txtName) {
        String lineTxt = null;
        StringBuilder builder = new StringBuilder();
        try {

            File file = new File(txtName);
            if (file.isFile() && file.exists()) {
//                InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
                InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "GBK");
                BufferedReader br = new BufferedReader(isr);
                while ((lineTxt = br.readLine()) != null) {
                    lineTxt+=br.readLine();
                    Lg.e("读取txt:"+lineTxt);
                    if (!"".equals(lineTxt)){
                        builder.append(lineTxt);
//                        Lg.e("读取txt2:"+builder.toString());
                    }
                }
                br.close();
                return builder.toString().substring(1,builder.toString().length()-4);
            } else {
                System.out.println("文件不存在!");
            }

//            File f = new File(txtName);
//            //以防有中文名路径，中文路径里面的空格会被"%20"代替
//            txtName = java.net.URLDecoder.decode(txtName, "utf-8");
//
//            FileInputStream redis = new FileInputStream(f);
////            br = new BufferedReader(new InputStreamReader(redis));
//
//            InputStream inputStream = App.getContext().getResources().getAssets().open(txtName);
//            byte[] arrayOfByte = new byte[inputStream.available()];
//            inputStream.read(arrayOfByte);
//            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lineTxt;
    }

    //更新软件
    public static void installApk(Context context, String apkPath) {
        if (context == null || TextUtils.isEmpty(apkPath)) {
            return;
        }
        Lg.e("获得文件路径："+apkPath);

        File file = new File(apkPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);

        //判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= 24) {
            Lg.e(">=24时");
//            Log.v(TAG,"7.0以上，正在安装apk...");
            //provider authorities
            Uri apkUri = FileProvider.getUriForFile(context,
                    context.getPackageName()+".new.provider",
//                    BuildConfig.APPLICATION_ID + ".provider",
                    file);
//            Uri apkUri = FileProvider.getUriForFile(context, "com.fangzuo.assist.fileprovider", file);
            //Granting Temporary Permissions to a URI
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            Lg.e("<24时");
//            Log.v(TAG,"7.0以下，正在安装apk...");
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }


    public static void  DownLoadJson(String downLoadURL) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            final String target = Environment.getExternalStorageDirectory()
                    + "/checkforuser"+getTimeLong(false)+".txt";
            App.JsonFile = target;
            HttpUtils utils = new HttpUtils();
            utils.download(downLoadURL, target, new RequestCallBack<File>() {
                @Override
                public void onLoading(long total, long current,
                                      boolean isUploading) {
                    super.onLoading(total, current, isUploading);
//                    System.out.println("下载进度:" + current + "/" + total);
//                    pDialog.setProgress((int) (current*100/total));
                }

                @Override
                public void onSuccess(ResponseInfo<File> arg0) {
                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Down_json_file,"OK"));
                }

                @Override
                public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Down_json_file,""));
                }


            });
        } else {

        }
    }
    //解密加密的时间
    public static String dealTime(String timemd){
//        Lg.e("加密的日期："+timemd);
        StringBuffer buffer = new StringBuffer()
                .append(timemd.charAt(Integer.parseInt(Config.Key.charAt(0)+"")+1))
                .append(timemd.charAt(Integer.parseInt(Config.Key.charAt(1)+"")+2))
                .append(timemd.charAt(Integer.parseInt(Config.Key.charAt(2)+"")+3))
                .append(timemd.charAt(Integer.parseInt(Config.Key.charAt(3)+"")+4))
                .append(timemd.charAt(Integer.parseInt(Config.Key.charAt(4)+"")+5))
                .append(timemd.charAt(Integer.parseInt(Config.Key.charAt(5)+"")+6))
                .append(timemd.charAt(Integer.parseInt(Config.Key.charAt(6)+"")+7))
                .append(timemd.charAt(Integer.parseInt(Config.Key.charAt(7)+"")+8));
//        Lg.e("解析日期："+buffer.toString());
        return buffer.toString();
    }
}



/*
String str = "abc,12,3yy98,0";
String[]  strs=str.split(",");//以，截取   或者\\^
for(int i=0,len=strs.length;i<len;i++){
    System.out.println(strs[i].toString());



 sb.substring(2);//索引号 2 到结尾

String barcode = code.substring(0, 12);//第一位到第十一位

3.通过StringUtils提供的方法
StringUtils.substringBefore(“dskeabcee”, “e”);
/结果是：dsk/
这里是以第一个”e”，为标准。

StringUtils.substringBeforeLast(“dskeabcee”, “e”)
结果为：dskeabce
这里以最后一个“e”为准。
* */
package com.fangzuo.assist.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;

import com.fangzuo.assist.Activity.Crash.App;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
            String[] split = code.split("/",3);
            Log.e("code:",split.length+"");
            if (split.length==3){
                String fcode = split[0];
                if (fcode.length()>12){
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
                }else{
                    Toast.showText(App.getContext(), "条码有误");
                    return new ArrayList<>();
                }
            }else{
                Toast.showText(App.getContext(), "条码有误");
                return new ArrayList<>();
            }
        }else{
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
    public static long createOrderCode(Activity activity){
        Long ordercode=0l;
        ShareUtil share =ShareUtil.getInstance(activity.getApplicationContext());
        if (share.getOrderCode(activity) == 0) {
            ordercode = Long.parseLong(getTimeLong(false) + "001");
            share.setOrderCode(activity,ordercode);
        } else {
            //当不是当天时，生成新的单据，重新计算
            if (String.valueOf(share.getOrderCode(activity)).contains(getTime(false))){
                ordercode =share.getOrderCode(activity);
            }else{
                ordercode = Long.parseLong(getTimeLong(false) + "001");
                share.setOrderCode(activity,ordercode);
            }
        }
        Log.e("生成新的单据:", ordercode + "");
        return ordercode;
    }
    public static String getTime(boolean b){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat(b?"yyyy-MM-dd":"yyyyMMdd");
        Date curDate = new Date();
        Log.e("date",curDate.toString());
        String str = format.format(curDate);
        return str;
    }
    public static String getTimeLong(boolean b){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat(b?"yyyy-MM-dd-HH-mm-ss":"yyyyMMddHHmmss");
        Date curDate = new Date();
        Log.e("date",curDate.toString());
        String str = format.format(curDate);
        return str;
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
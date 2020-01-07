package com.fangzuo.assist.Utils;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MathUtil {


    //防止强转时崩溃操作
    public static double toD(String string) {
        if (null == string) {
            return 0;
        } else if (string.equals("")) {
            return 0;
        } else {
            try {
                return Double.parseDouble(string);
            }catch (Exception e){
                return 0;
            }
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

    //解决 1.1-1.0=0.10000009的情况
    //double相减
    public static double doubleSub(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.subtract(b2).doubleValue();
    }
    //是否为数字
    public static boolean isNumeric(String str) {
        //Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+");//这个有问题，一位的整数不能通过
        Pattern pattern = Pattern.compile("^(\\-|\\+)?\\d+(\\.\\d+)?$");//这个是对的
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
    //去掉小数点以后的数值
    public static String Cut0(String value) {
        if (value==null||"".equals(value)){
            return "0";
        }
        if (value.contains(".")){
            String str=Math.rint(Double.parseDouble(value))+"";
            return str.substring(0,value.lastIndexOf("."));
        }else{
            return value;
        }

//       Lg.e("TEST",DoubleUtil.Cut0("0.50"));0
//       Lg.e("TEST",DoubleUtil.Cut0("1.50"));2
//       Lg.e("TEST",DoubleUtil.Cut0("1.40"));1
//       Lg.e("TEST",DoubleUtil.Cut0("0.45"));0
    }
    //解决 1.1+1.0=2.09999999的情况
    //double相加
    public static double sum(String d1,String d2){
        if ("".equals(d1))d1="0";
        if ("".equals(d2))d2="0";
        BigDecimal bd1 = new BigDecimal(d1);
        BigDecimal bd2 = new BigDecimal(d2);
        return bd1.add(bd2).doubleValue();
    }
    /*Math.ceil()执行向上舍入，即它总是将数值向上舍入为最接近的整数；
Math.floor()执行向下舍入，即它总是将数值向下舍入为最接近的整数；
Math.round()执行标准舍入，即它总是将数值四舍五入为最接近的整数。*/
//    //四舍五入取整
//    public static double D2saveInt(Double d){
//        return Math.round(d);
//    }
    public static String DoubleKeepInt(Double d){
        if (null == d)d=0.0;
        return Cut0(Math.round(d)+"");
    }

    //保留两位小数（四舍五入
    public static double DoubleKeep(Double d,int num){
        if (null == d)d=0.0;
        String keep = "%."+num+"f";
        return Double.parseDouble(String.format(keep, d));
    }
}

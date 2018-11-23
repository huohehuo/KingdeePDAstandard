package com.fangzuo.assist.Utils;

public class MathUtil {

    //防止强转时崩溃操作
    public static double toD(String string) {
        if (null == string) {
            return 0;
        } else if (string.equals("")) {
            return 0;
        } else {
            return Double.parseDouble(string);
        }
    }



}

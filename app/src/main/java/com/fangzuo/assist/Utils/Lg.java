package com.fangzuo.assist.Utils;

import android.util.Log;

import com.fangzuo.assist.Activity.Crash.App;

public class Lg {

    public static void e(String string){
        if (App.isDebug){
            if (string!=null){
                Log.e("TEST","\n"+string);
            }
        }
    }
    public static void e(String tag,String string){
        if (App.isDebug){
            if (string!=null){
                Log.e(tag,"\n"+string);
            }
        }
    }
}

package com.fangzuo.assist.Utils;

import android.content.Context;
import android.os.Handler;

import com.fangzuo.assist.Activity.Crash.App;

/**
 * Created by NB on 2017/7/24.
 */

public class Toast {

    public static void showText(Context context,String text){
        final android.widget.Toast toast = android.widget.Toast.makeText(context, text, android.widget.Toast.LENGTH_SHORT);
        toast.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        },600);
    }
    public static void showText(String text){
        final android.widget.Toast toast = android.widget.Toast.makeText(App.getContext(), text, android.widget.Toast.LENGTH_SHORT);
        toast.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        },600);
    }
}

package com.fangzuo.assist.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fangzuo.assist.R;

public class LoadingUtil {

    private static ProgressDialog mDialog;
    private static Dialog loadingDialog;

    private LoadingUtil() {}

    /**
     * 显示
     * @param title
     * @param msg
     */
    public static void show(Context context, String title, String msg) {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
        if (null !=context){
            mDialog = new ProgressDialog(context);
            // 点击back键和点击屏幕不隐藏
            mDialog.setCancelable(false);
            mDialog.setTitle(title);
            mDialog.setMessage(msg);
            if (!mDialog.isShowing()) {
                mDialog.show();
            }
        }
    }
    public static void show(Context context, String title, String msg,boolean cancelable) {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
        if (null !=context){
            mDialog = new ProgressDialog(context);
            // 点击back键和点击屏幕不隐藏
            mDialog.setCancelable(cancelable);
            mDialog.setTitle(title);
            mDialog.setMessage(msg);
            if (!mDialog.isShowing()) {
                mDialog.show();
            }
        }

    }

    //STYLE_HORIZONTAL = 1;
    //STYLE_SPINNER = 0;
    public static void show(Context context, String title, String msg,boolean cancelable,int style) {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
        if (null !=context){
            mDialog = new ProgressDialog(context);
            // 点击back键和点击屏幕不隐藏
            mDialog.setCancelable(cancelable);
            mDialog.setProgressStyle(style);
            mDialog.setTitle(title);
            mDialog.setMessage(msg);
            if (!mDialog.isShowing()) {
                mDialog.show();
            }
        }

    }

    /**
     * 显示
     * @param context
     * @param msg
     */
    public static void show(Context context, String msg) {
        show(context, "", msg);
    }
    public static void show(Context context, String msg,boolean cancelable) {
        show(context, "", msg,cancelable);
    }
    /**
     * 隐藏
     */
    public static void dismiss() {
        try{
            if (loadingDialog != null && loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }
            if (mDialog !=null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
        }catch (Exception e){

        }

    }


    public static void showDialog(Context context, String msg) {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
            loadingDialog=null;
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v
                .findViewById(R.id.dialog_loading_view);// 加载布局
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        tipTextView.setText(msg);// 设置加载信息

        loadingDialog = new Dialog(context, R.style.LoadingDialogStyle);// 创建自定义样式dialog
        loadingDialog.setCancelable(true); // 是否可以按“返回键”消失
        loadingDialog.setCanceledOnTouchOutside(false); // 点击加载框以外的区域
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        /**
         *将显示Dialog的方法封装在这里面
         */
        Window window = loadingDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.PopWindowAnimStyle);
        loadingDialog.show();

    }
    //检测版本号窗口的弹窗提示
//    public static Dialog createCheckDialog(Context context) {
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View v = inflater.inflate(R.layout.dialog_updata, null);// 得到加载view
//        LinearLayout layout = (LinearLayout) v
//                .findViewById(R.id.dialog_updata_view);// 加载布局
//        TextView updata = (TextView) v.findViewById(R.id.tv_dialog_btn_updata);// 提示文字
//        TextView no = (TextView) v.findViewById(R.id.tv_dialog_btn_no);// 提示文字
//
//        Dialog dialog = new Dialog(context, R.style.MyDialogStyle);// 创建自定义样式dialog
//        dialog.setCancelable(true); // 是否可以按“返回键”消失
//        dialog.setCanceledOnTouchOutside(false); // 点击加载框以外的区域
//        dialog.setContentView(layout, new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
//
//        updata.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Uri uri = Uri.parse("http://vpn.tubebook.net");
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                context.startActivity(intent);
//            }
//        });
//        no.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//        /**
//         *将显示Dialog的方法封装在这里面
//         */
//        Window window = dialog.getWindow();
//        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        window.setGravity(Gravity.CENTER);
//        window.setAttributes(lp);
//        window.setWindowAnimations(R.style.PopWindowAnimStyle);
//        dialog.show();
//
//        return dialog;
//    }
    /**
     * 关闭dialog
     *
     * http://blog.csdn.net/qq_21376985
     *
     * @param
     */
    public static void closeDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }
    //弹窗提示
    public static void showAlter(Context mContext,String title,String msg){
        new AlertDialog.Builder(mContext)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("确认",null)
                .create().show();
    }
    //弹窗提示,是否可以点击外部取消
    public static void showAlter(Context mContext,String title,String msg,boolean cancele){
        AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
        ab.setTitle(title);
        ab.setMessage(msg);
        ab.setPositiveButton("确认",null);
        final AlertDialog alertDialog = ab.create();
        alertDialog.setCanceledOnTouchOutside(cancele);
        alertDialog.show();
    }

}

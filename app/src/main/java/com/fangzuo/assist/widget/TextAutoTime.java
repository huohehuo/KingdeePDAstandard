package com.fangzuo.assist.widget;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Adapter.UnitSpAdapter;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Dao.Unit;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.JsonCreater;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.MathUtil;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.UnitDao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class TextAutoTime extends RelativeLayout {
    // 返回按钮控件
    // 标题Tv
    private AppCompatTextView mTitleTv;
    private AppCompatTextView mTime;
    private String date;
    private int year;
    private int month;
    private int day;
    public TextAutoTime(final Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        LayoutInflater.from(context).inflate(R.layout.view_my_time, this);
        year = Calendar.getInstance().get(Calendar.YEAR);
        month = Calendar.getInstance().get(Calendar.MONTH);
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        // 获取控件
        mTitleTv = (AppCompatTextView) findViewById(R.id.tv);
        mTime = (AppCompatTextView) findViewById(R.id.time);

        TypedArray attrArray = context.obtainStyledAttributes(attributeSet, R.styleable.Style_Time_Choose);
        int count = attrArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attrName = attrArray.getIndex(i);
            switch (attrName) {
                case R.styleable.Style_Time_Choose_Time_titile:
                    mTitleTv.setText(attrArray.getString(R.styleable.Style_Time_Choose_Time_titile));
                    break;
                case R.styleable.Style_Time_Choose_Time_titile_size:
//                    mTitleTv.setTextSize(attrArray.getDimension(R.styleable.Style_Time_Choose_Time_titile_size, 10));
//                    mTime.setTextSize(attrArray.getDimension(R.styleable.Style_Time_Choose_Time_titile_size, 10));
                    break;
            }
        }
        attrArray.recycle();

        mTime.setText(getTime(true));
        mTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerWithData(mTime,context,mTime.getText().toString());
            }
        });
    }

    public String datePicker(final TextView v) {
        final DatePickerDialog datePickerDialog = new DatePickerDialog(App.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            }
        }, year, month, day);
        datePickerDialog.setButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int year = datePickerDialog.getDatePicker().getYear();
                int month = datePickerDialog.getDatePicker().getMonth();
                int day = datePickerDialog.getDatePicker().getDayOfMonth();
                date = year + "-" + ((month < 9) ? "0" + (month + 1) : (month + 1)) + "-" + ((day < 10) ? "0" + day : day);
                v.setText(date);
                datePickerDialog.dismiss();
            }
        });
        datePickerDialog.show();
        return date;
    }

    public String datePickerWithData(final TextView v,Context context,String time) {
        if (time.length()==10){
            year = MathUtil.toInt(time.substring(0,4));
            month = MathUtil.toInt(time.substring(5,7))-1;
            day = MathUtil.toInt(time.substring(8,time.length()));
        }
        Lg.e("获取时间1"+year);
        Lg.e("获取时间2"+month);
        Lg.e("获取时间3"+day);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            }
        }, year, month, day);
        datePickerDialog.setButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int year = datePickerDialog.getDatePicker().getYear();
                int month = datePickerDialog.getDatePicker().getMonth();
                int day = datePickerDialog.getDatePicker().getDayOfMonth();
                date = year + "-" + ((month < 9) ? "0" + (month + 1) : (month + 1)) + "-" + ((day < 10) ? "0" + day : day);
                v.setText(date);
                datePickerDialog.dismiss();

            }
        });
        datePickerDialog.show();
        return date;
    }

    private String getTime(boolean b) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat(b ? "yyyy-MM-dd" : "yyyyMMdd");
        return format.format(new Date());
    }

//    // 为左侧返回按钮添加自定义点击事件
//    public void setOnClickListener(OnClickListener listener) {
//        mTime.setOnClickListener(listener);
//    }
////

    public void setText(String time){
        mTime.setText(time);
    }
    public String getText(){
        return mTime.getText().toString();
    }



}

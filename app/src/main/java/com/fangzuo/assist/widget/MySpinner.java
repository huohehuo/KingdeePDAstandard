package com.fangzuo.assist.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.fangzuo.assist.R;

/*
* <declare-styleable name="MySpinner">
        <attr name="myspinner_name" format="string"/>
        <attr name="myspinner_name_size" format="dimension"/>
    </declare-styleable>
* */
public class MySpinner extends RelativeLayout {
    // 返回按钮控件
      private Spinner mSp;
      // 标题Tv
      private TextView mTitleTv;
    private SpinnerAdapter adapter;
    public MySpinner(Context context, AttributeSet attributeSet) {
        super(context,attributeSet);

        LayoutInflater.from(context).inflate(R.layout.view_myspinner,this);

        // 获取控件
        mSp = (Spinner) findViewById(R.id.sp);
        mTitleTv = (TextView) findViewById(R.id.tv);
        TypedArray attrArray = context.obtainStyledAttributes(attributeSet, R.styleable.MySpinner);
        int count = attrArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attrName = attrArray.getIndex(i);
            switch (attrName) {
                case R.styleable.MySpinner_myspinner_name:
                    mTitleTv.setText(attrArray.getString(R.styleable.MySpinner_myspinner_name));
                    break;
                case R.styleable.MySpinner_myspinner_name_size:
                    mTitleTv.setText(attrArray.getString(R.styleable.MySpinner_myspinner_name));
                    mTitleTv.setTextSize(attrArray.getDimension(R.styleable.MySpinner_myspinner_name_size,10));
                    break;
            }
        }
        attrArray.recycle();
    }

    // 为左侧返回按钮添加自定义点击事件
    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener listener) {
        mSp.setOnItemSelectedListener(listener);
    }
    public void setAdapter(SpinnerAdapter adapter){
        mSp.setAdapter(adapter);
        this.adapter = adapter;
    }
    public void setSelection(int i){
        mSp.setSelection(i);
    }
    // 设置标题的方法
    public void setTitleText(String title) {
        mTitleTv.setText(title);
    }



}

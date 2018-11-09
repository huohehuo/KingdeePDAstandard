package com.fangzuo.assist.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fangzuo.assist.Adapter.UnitSpAdapter;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Dao.Unit;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.Asynchttp;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.JsonCreater;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.UnitDao;
import com.loopj.android.http.AsyncHttpClient;

import java.util.ArrayList;
import java.util.List;


public class TextViewCard extends RelativeLayout {
    // 返回按钮控件
    // 标题Tv
    private TextView title;
    private TextView world;
    private static BasicShareUtil share;
    private String autoString;//用于联网时，再次去自动设置值
    private ArrayList<Unit> list;
    private DaoSession daoSession;
    private String unitId = "";
    private String unitName = "";
    public static final String Name = "name";
    public static final String Id = "id";
    public static final String Number = "number";
    public static final String TGP = "Unit+";

    public TextViewCard(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        LayoutInflater.from(context).inflate(R.layout.view_my_textview, this);
        daoSession = GreenDaoManager.getmInstance(context).getDaoSession();
        list = new ArrayList<>();
        share = BasicShareUtil.getInstance(context);
        // 获取控件
        title = (TextView) findViewById(R.id.tv_cardtitle);
        world = (TextView) findViewById(R.id.tv_cardworld);

        TypedArray attrArray = context.obtainStyledAttributes(attributeSet, R.styleable.Style_TextView);
        int count = attrArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attrName = attrArray.getIndex(i);
            switch (attrName) {
                case R.styleable.Style_TextView_textcardtext:
                    title.setText(attrArray.getString(R.styleable.Style_TextView_textcardtext));
//                    world.setText(attrArray.getString(R.styleable.Style_TextView_textcardtext));
                    break;
//                case R.styleable.Style_Spinner_Unit_Uspinner_focusable:
//                    mSp.setEnabled(attrArray.getBoolean(R.styleable.Style_Spinner_Unit_Uspinner_focusable, true));
//                    break;
                case R.styleable.Style_TextView_textcardsize:
                    title.setTextSize(attrArray.getDimension(R.styleable.Style_TextView_textcardsize, 10));
//                    world.setTextSize(attrArray.getDimension(R.styleable.Style_TextView_textcardsize, 10));
                    break;
            }
        }
        attrArray.recycle();

    }
    public void setText(String string){
        world.setText(string);
    }

    public String getString(){
        return world.getText().toString().trim();
    }

}

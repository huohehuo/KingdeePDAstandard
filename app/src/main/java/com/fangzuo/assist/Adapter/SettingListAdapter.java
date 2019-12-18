package com.fangzuo.assist.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fangzuo.assist.Beans.SettingList;
import com.fangzuo.assist.R;

import java.util.ArrayList;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by NB on 2017/7/28.
 */

public class SettingListAdapter extends BaseAdapter {
    Context context;
    ArrayList<SettingList> items;
    private ViewHolder viewHolder;

    public SettingListAdapter(Context context, ArrayList<SettingList> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.setting_lv_items, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.iv.setImageResource(items.get(i).ImageResourse);
        viewHolder.tv.setText(items.get(i).tv);
        //设置非通用单据为灰色
        if ("16".equals(items.get(i).tag) || "17".equals(items.get(i).tag) || "20".equals(items.get(i).tag)
                || "21".equals(items.get(i).tag) || "22".equals(items.get(i).tag) || "23".equals(items.get(i).tag)
                || "24".equals(items.get(i).tag) || "25".equals(items.get(i).tag)
                ){
            viewHolder.tv.setTextColor(viewHolder.gray);
        }else{
            viewHolder.tv.setTextColor(viewHolder.black);
        }
        return view;
    }


     class ViewHolder {
        @BindView(R.id.iv)
        ImageView iv;
        @BindView(R.id.tv)
        TextView tv;
        @BindColor(R.color.pd_text_gray)int gray;
        @BindColor(R.color.pd_text_black)int black;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

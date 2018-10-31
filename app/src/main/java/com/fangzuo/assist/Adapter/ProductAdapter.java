package com.fangzuo.assist.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fangzuo.assist.Dao.Product;
import com.fangzuo.assist.R;

import java.util.ArrayList;

/**
 * Created by NB on 2017/8/1.
 */

public class ProductAdapter extends BaseAdapter {
    Context context;
    ArrayList<Product> items;
    private ViewHolder viewHolder;

    public ProductAdapter(Context context, ArrayList<Product> items) {
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
        if(view==null){
            view = LayoutInflater.from(context).inflate(R.layout.autolayout,null);
            viewHolder = new ViewHolder();
            viewHolder.fid = view.findViewById(R.id.fID);
            viewHolder.fname = view.findViewById(R.id.fname);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.fid.setText(items.get(i).FNumber);
        viewHolder.fname.setText(items.get(i).FName);
        return view;
    }

    class ViewHolder{
        TextView fid;
        TextView fname;
    }
}

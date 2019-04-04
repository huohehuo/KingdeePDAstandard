package com.fangzuo.assist.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fangzuo.assist.Dao.Client;
import com.fangzuo.assist.Dao.Suppliers;
import com.fangzuo.assist.R;

import java.util.List;

/**
 * Created by NB on 2017/7/27.
 */

public class SupplierSpAdapter extends BaseAdapter {
    Context context;
    List<Suppliers> items;
    private ViewHolder viewHolder;

    public SupplierSpAdapter(Context context, List<Suppliers> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void addData(List<Suppliers> clients) {
        items.clear();
        items.addAll(clients);
        notifyDataSetChanged();
    }
    public void clear() {
        items.clear();
        notifyDataSetChanged();
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
            view = LayoutInflater.from(context).inflate(R.layout.itemtext,null);
            viewHolder = new ViewHolder();
            viewHolder.tv = view.findViewById(R.id.textView);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tv.setText(items.get(i).FName);
        return view;
    }


    class ViewHolder{
        TextView tv ;
    }
}

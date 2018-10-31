package com.fangzuo.assist.Utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.fangzuo.assist.R;

import java.util.ArrayList;

import com.fangzuo.assist.Beans.ConnectResponseBean;

/**
 * Created by NB on 2017/7/24.
 */

public class DataBaseAdapter extends BaseAdapter{
//    public  HashMap<Integer, Boolean> isSelected;
    Context context;
    ArrayList<ConnectResponseBean.DataBaseList> items;
    private  ViewHolder viewHolder;
    int checkPosition;

    public DataBaseAdapter(Context context, ArrayList<ConnectResponseBean.DataBaseList> items) {
        this.context = context;
        this.items = items;
//        isSelected = new HashMap<>();
//        initDate();
    }

//    public void initDate(){
//        for(int i=0; i<items.size();i++) {
//            getIsSelected().put(i,false);
//        }
//    }
//    public void allCheck(){
//        Log.e("items",items.size()+"");Log.e("items",(items.size()+1)+"");
//        for(int i=0; i<items.size();i++) {
//            getIsSelected().put(i,true);
//        }
//    }


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
            view = LayoutInflater.from(context).inflate(R.layout.lv_database_items,null);
            viewHolder = new ViewHolder();
            viewHolder.cb_ischeck = view.findViewById(R.id.cb_ischeck);
            viewHolder.zhangtao = view.findViewById(R.id.zhangtao);
            viewHolder.database = view.findViewById(R.id.database);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        if(i==0){
            view.setBackgroundColor(view.getResources().getColor(R.color.dividerLine));
            view.setEnabled(false);
            Log.e("checkPosition",checkPosition+"");
            viewHolder.cb_ischeck.setChecked(false);
        }


            viewHolder.zhangtao.setText(items.get(i).name);
            viewHolder.database.setText(items.get(i).dataBaseName);
        if(i==checkPosition&&i!=0){
            Log.e("checkPosition",checkPosition+"");
            viewHolder.cb_ischeck.setChecked(true);
        }else{
            Log.e("checkPosition",checkPosition+"");
            viewHolder.cb_ischeck.setChecked(false);
        }





        return view;

    }
//    public  HashMap<Integer,Boolean> getIsSelected() {
//        return isSelected;
//    }
//
//    public void setIsSelected(HashMap<Integer,Boolean> isSelected) {
//        this.isSelected = isSelected;
//    }

    public void setIsCheck(int checkPosition){
        Log.e("checkPosition",checkPosition+"");
        this.checkPosition = checkPosition;
    }

    class ViewHolder{
        CheckBox cb_ischeck;
        TextView zhangtao;
        TextView database;
    }
}

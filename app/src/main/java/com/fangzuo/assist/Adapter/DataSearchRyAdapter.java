package com.fangzuo.assist.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.fangzuo.assist.Beans.ConnectResponseBean;
import com.fangzuo.assist.Dao.Product;
import com.fangzuo.assist.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王璐阳 on 2018/3/27.
 */
//代码，名称，规格，总库存，单位
public class DataSearchRyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    public ArrayList<ConnectResponseBean.DataBaseList> items;
    private RvInnerClickListener mListener;
    //1、定义一个集合，用来记录选中
    Context context;
    private OnItemClickListener onItemClickListener;
    private int checkPosition=-1;
    public DataSearchRyAdapter(Context context, ArrayList<ConnectResponseBean.DataBaseList> items) {
        this.items = items;
        this.context = context;

    }

    public void addAll(List<ConnectResponseBean.DataBaseList> itemss) {
        items.clear();
        items.addAll(itemss);
        notifyDataSetChanged();
    }
    public void clear(){
        items.clear();
        notifyDataSetChanged();
    }
    public ArrayList<ConnectResponseBean.DataBaseList> getAllData(){
        return items;
    }

//    @Override
//    public int getItemViewType(int position) {
//        return Integer.valueOf(items.get(position).getBackDateType());
//
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        RecyclerView.ViewHolder holder = null;
//        if(1 == viewType){
            View v = mInflater.inflate(R.layout.item_data_search,parent,false);
            holder = new MarkHolder(v);
//        }else if (2 == viewType){
//            View v = mInflater.inflate(R.layout.item_account_check_b,parent,false);
//            holder = new MainHolderTwo(v);
//        }else{
//            View v = mInflater.inflate(R.layout.item_account_check_c,parent,false);
//            holder = new MainHolderThree(v);
//        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        int pos;
            ((MarkHolder) holder).name.setText(   items.get(position).name);
            ((MarkHolder) holder).dataBase.setText(   items.get(position).dataBaseName);

//            if (Double.parseDouble(items.get(position).getFLastMoney())>300000.00){
//                ((MainHolderThree) holder).FLastMoney.setTextColor(Color.RED);
//            }else{
//                ((MainHolderThree) holder).FLastMoney.setTextColor(Color.BLACK);
//            }
//        ((MarkHolder) holder).cb_ischeck.setChecked(false);
            ((MarkHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = ((MarkHolder) holder).getLayoutPosition();
//                    if (pos==position){
//                        ((MarkHolder) holder).name.setTextColor(Color.BLUE);
//                        ((MarkHolder) holder).cb_ischeck.setChecked(true);
//
//                    }else{
//                        ((MarkHolder) holder).name.setTextColor(Color.BLACK);
//                        ((MarkHolder) holder).cb_ischeck.setChecked(false);
//                    }
                    onItemClickListener.onItemClick(((MarkHolder) holder).itemView, pos);
                }
            });
        if(position==checkPosition){
            ((MarkHolder) holder).name.setTextColor(Color.BLUE);
            Log.e("checkPosition",checkPosition+"");
            ((MarkHolder) holder).cb_ischeck.setChecked(true);
        }else{
            ((MarkHolder) holder).name.setTextColor(Color.BLACK);
            Log.e("checkPosition",checkPosition+"");
            ((MarkHolder) holder).cb_ischeck.setChecked(false);
       }


    }

    public ConnectResponseBean.DataBaseList getItems(int position) {
        return items.get(position);
    }
    public void setIsCheck(int checkPosition){
        Log.e("checkPosition",checkPosition+"");
        this.checkPosition = checkPosition;
        notifyDataSetChanged();
    }
    public void clearCheck(){
        this.checkPosition=-1;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return items.size();
    }


    @Override
    public void onClick(View view) {
        mListener.InnerItemOnClick(view);
    }

    public interface RvInnerClickListener {
        void InnerItemOnClick(View v);
    }

    public void setInnerClickListener(RvInnerClickListener mListener) {
        this.mListener = mListener;
    }

    public void addNewItem(ConnectResponseBean.DataBaseList temp) {
        if (items != null) {
            items.add(temp);
            notifyItemInserted(items.size() + 1);
        }
    }

    public void deleteItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public void setOnItemClickListener(DataSearchRyAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    class MarkHolder  extends RecyclerView.ViewHolder {
        private CheckBox cb_ischeck;
        private TextView name;
        private TextView dataBase;
        private TextView number;
        private TextView unit;
        private TextView num;
        private TextView helpcode;
//        private TextView pici;

        public MarkHolder(View itemView) {
            super(itemView);
            name        = (TextView) itemView.findViewById(R.id.tv_name);
            dataBase        = (TextView) itemView.findViewById(R.id.tv_database);
            cb_ischeck        = (CheckBox) itemView.findViewById(R.id.cb_ischeck);


//            pici        = (TextView) itemView.findViewById(R.id.tv_pici);

        }
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }
}

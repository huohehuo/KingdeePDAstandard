package com.fangzuo.assist.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fangzuo.assist.Beans.InStorageNumListBean;
import com.fangzuo.assist.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王璐阳 on 2018/3/27.
 */

public class StorageCheckRyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    public ArrayList<InStorageNumListBean.inStoreList> items;
    private RvInnerClickListener mListener;
    //1、定义一个集合，用来记录选中
    Context context;
    private OnItemClickListener onItemClickListener;

    public StorageCheckRyAdapter(Context context, ArrayList<InStorageNumListBean.inStoreList> items) {
        this.items = items;
        this.context = context;

    }

    public void addAll(List<InStorageNumListBean.inStoreList> itemss) {
        items.clear();
        items.addAll(itemss);
        notifyDataSetChanged();
    }
    public void clear(){
        items.clear();
        notifyDataSetChanged();
    }
    public ArrayList<InStorageNumListBean.inStoreList> getAllData(){
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
            View v = mInflater.inflate(R.layout.item_storage_check,parent,false);
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

            ((MarkHolder) holder).name.setText(   items.get(position).getFName());
            ((MarkHolder) holder).model.setText(   items.get(position).getFModel());
            ((MarkHolder) holder).number.setText(   items.get(position).getFNumber());
            ((MarkHolder) holder).unit.setText(    items.get(position).getFUnit());
            ((MarkHolder) holder).num.setText(    items.get(position).getFQty());
            ((MarkHolder) holder).pici.setText(items.get(position).getFBatchNo());
            ((MarkHolder) holder).storage.setText(items.get(position).getFStockID());
            ((MarkHolder) holder).wavehouse.setText(items.get(position).getFStockPlaceID());

//            if (Double.parseDouble(items.get(position).getFLastMoney())>300000.00){
//                ((MainHolderThree) holder).FLastMoney.setTextColor(Color.RED);
//            }else{
//                ((MainHolderThree) holder).FLastMoney.setTextColor(Color.BLACK);
//            }
//            ((MarkHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int pos = ((MarkHolder) holder).getLayoutPosition();
//                    onItemClickListener.onItemClick(((MarkHolder) holder).itemView, pos);
//                }
//            });




    }

    public InStorageNumListBean.inStoreList getItems(int position) {
        return items.get(position);
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

    public void addNewItem(InStorageNumListBean.inStoreList temp) {
        if (items != null) {
            items.add(temp);
            notifyItemInserted(items.size() + 1);
        }
    }

    public void deleteItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public void setOnItemClickListener(StorageCheckRyAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    class MarkHolder  extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView model;
        private TextView number;
        private TextView unit;
        private TextView num;
        private TextView pici;
        private TextView storage;
        private TextView wavehouse;

        public MarkHolder(View itemView) {
            super(itemView);
            name        = (TextView) itemView.findViewById(R.id.tv_name);
            model        = (TextView) itemView.findViewById(R.id.tv_model);
            number        = (TextView) itemView.findViewById(R.id.tv_number);
            unit        = (TextView) itemView.findViewById(R.id.tv_unit);
            num        = (TextView) itemView.findViewById(R.id.tv_num);
            pici        = (TextView) itemView.findViewById(R.id.tv_pici);
            wavehouse        = (TextView) itemView.findViewById(R.id.tv_wavehouse);
            storage        = (TextView) itemView.findViewById(R.id.tv_storage);

        }
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }
}

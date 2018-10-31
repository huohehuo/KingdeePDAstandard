package com.fangzuo.assist.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fangzuo.assist.Dao.T_Detail;
import com.fangzuo.assist.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by NB on 2017/8/3.
 */

public class TableAdapter1 extends BaseAdapter implements View.OnClickListener {
    Context context;
    List<T_Detail> detail;
    private ViewHolder viewHolder;
    InnerClickListener mListener;
    List<Boolean> isCheck;

    public TableAdapter1(Context context,  List<T_Detail> detail, List<Boolean> isCheck) {
        this.context = context;
        this.detail = detail;
        this.isCheck = isCheck;
    }

    @Override
    public int getCount() {
        return detail.size();
    }

    @Override
    public Object getItem(int i) {
        return detail.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_layout, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

            viewHolder.productname.setText("盘点ID:" + detail.get(i).FIndex);
            viewHolder.productId.setText("商品编码:" + detail.get(i).FProductCode);
            viewHolder.productxh.setText("型号:" + detail.get(i).model);
            viewHolder.num.setText("本次盘点:" + detail.get(i).FQuantity);
            viewHolder.price.setText("本次调整:" + detail.get(i).FIdentity);
            viewHolder.storage.setText("仓库:" + detail.get(i).FStorage);


        viewHolder.delete.setOnClickListener(this);
        viewHolder.delete.setTag(i);
        viewHolder.fix.setOnClickListener(this);
        viewHolder.fix.setTag(i);
        viewHolder.cbIscheck.setChecked(isCheck.get(i));
        return view;
    }

    @Override
    public void onClick(View view) {
        mListener.InOnClick(view);
    }

    public void setInnerListener(InnerClickListener mListener) {
        this.mListener = mListener;
    }

    public interface InnerClickListener {
        void InOnClick(View v);
    }




    static class ViewHolder {
        @BindView(R.id.delete)
        Button delete;
        @BindView(R.id.productname)
        TextView productname;
        @BindView(R.id.productId)
        TextView productId;
        @BindView(R.id.productxh)
        TextView productxh;
        @BindView(R.id.storage)
        TextView storage;
        @BindView(R.id.num)
        TextView num;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.fix)
        Button fix;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.cb_ischeck)
        CheckBox cbIscheck;
        @BindView(R.id.wavehousein)
        TextView wavehousein;
        @BindView(R.id.wavehouseout)
        TextView wavehouseout;
        @BindView(R.id.ll_wavehosue)
        LinearLayout llWavehosue;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }



}

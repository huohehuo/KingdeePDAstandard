package com.fangzuo.assist.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fangzuo.assist.Dao.Product;
import com.fangzuo.assist.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by NB on 2017/9/6.
 */

public class ProductselectAdapter1 extends BaseAdapter {
    Context context;
    List<Product> data;
    private ViewHolder viewHolder;

    public ProductselectAdapter1(Context context, List<Product> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.producselect, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tvUnit.setText(data.get(0).FTaxRate);
        viewHolder.tvProductName.setText(data.get(i).FName);
        viewHolder.tvProductNumber.setText(data.get(i).FNumber);
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.tv_productName)
        TextView tvProductName;
        @BindView(R.id.tv_productNumber)
        TextView tvProductNumber;
        @BindView(R.id.tv_unit)
        TextView tvUnit;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

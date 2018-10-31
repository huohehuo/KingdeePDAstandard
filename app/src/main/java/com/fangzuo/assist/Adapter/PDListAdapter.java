package com.fangzuo.assist.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.fangzuo.assist.Dao.PDMain;
import com.fangzuo.assist.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by NB on 2017/8/21.
 */

public class PDListAdapter extends BaseAdapter {
    Context context;
    List<PDMain> items;
    List<Boolean> isCheck;
    private ViewHolder viewHolder;

    public PDListAdapter(Context context, List<PDMain> items, List<Boolean> isCheck) {
        this.context = context;
        this.items = items;
        this.isCheck = isCheck;
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
            view = LayoutInflater.from(context).inflate(R.layout.items_pdlist, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.cbIscheck.setChecked(isCheck.get(i));
        viewHolder.tvDate.setText(items.get(i).FDate);
        viewHolder.tvPdid.setText(items.get(i).FID);
        viewHolder.tvRemark.setText(items.get(i).FRemark);
        viewHolder.tvPdname.setText(items.get(i).FProcessId);
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.cb_ischeck)
        CheckBox cbIscheck;
        @BindView(R.id.tv_pdname)
        TextView tvPdname;
        @BindView(R.id.tv_pdid)
        TextView tvPdid;
        @BindView(R.id.tv_remark)
        TextView tvRemark;
        @BindView(R.id.tv_date)
        TextView tvDate;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

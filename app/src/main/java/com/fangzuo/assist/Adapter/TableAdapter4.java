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

import com.fangzuo.assist.Activity.DBActivity;
import com.fangzuo.assist.Dao.T_Detail;
import com.fangzuo.assist.Dao.T_main;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.T_mainDao;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by NB on 2017/8/3.
 */

public class TableAdapter4 extends BaseAdapter implements View.OnClickListener {
    Context context;
    List<T_main> main;
    List<T_Detail> detail;
    private ViewHolder viewHolder;
    InnerClickListener mListener;
    List<Boolean> isCheck;

    public TableAdapter4(Context context, List<T_Detail> detail, List<Boolean> isCheck) {
        this.context = context;
        this.main = main;
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
        DaoSession daoSession = GreenDaoManager.getmInstance(context).getDaoSession();
        T_mainDao t_mainDao = daoSession.getT_mainDao();
        List<T_main> t_mains = t_mainDao.queryBuilder().where(T_mainDao.Properties.OrderId.eq(detail.get(i).FOrderId),T_mainDao.Properties.Activity.eq(detail.get(i).activity)).build().list();
        if(t_mains.size()>=0){
//            if(t_mains.get(0).activity== DBActivity.DB){
//                viewHolder.llWavehosue.setVisibility(View.VISIBLE);
//                viewHolder.productname.setText("单据编号:" + t_mains.get(0).orderId);
//                viewHolder.productId.setText("物料编码:" + detail.get(i).FProductCode);
//                viewHolder.productxh.setText("调入仓库:" + detail.get(i).FStorage);
//                viewHolder.num.setText("数量:" + detail.get(i).FQuantity+detail.get(i).FUnit);
//                viewHolder.price.setText("价格:" + detail.get(i).FTaxUnitPrice);
//                viewHolder.storage.setText("调出仓库:" + detail.get(i).FoutStoragename);
//                viewHolder.date.setText("物料名:" + detail.get(i).FProductName);
//                viewHolder.wavehousein.setText("调入仓位"+detail.get(i).FPosition);
//                viewHolder.wavehouseout.setText("调出仓位"+detail.get(i).Foutwavehousename);
//
//            }else {
                viewHolder.productname.setText("单据编号:" + detail.get(i).FBillNo);
                viewHolder.productId.setText("物料编码:" + detail.get(i).FProductCode);
                viewHolder.productxh.setText("物料名:" + detail.get(i).FProductName);
                viewHolder.num.setText("数量:" + detail.get(i).FQuantity+detail.get(i).FUnit);
                viewHolder.price.setText("价格:" + detail.get(i).FTaxUnitPrice);
                viewHolder.storage.setText("仓库:" + detail.get(i).FStorage);
//                viewHolder.date.setText("下单日期:" + t_mains.get(0).orderDate);
//            }

            viewHolder.delete.setOnClickListener(this);
            viewHolder.delete.setTag(i);
            viewHolder.fix.setOnClickListener(this);
            viewHolder.fix.setTag(i);
            viewHolder.cbIscheck.setChecked(isCheck.get(i));
        }

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

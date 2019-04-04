package com.fangzuo.assist.Fragment;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.fangzuo.assist.ABase.BaseFragment;
import com.fangzuo.assist.Adapter.GridViewAdapter;
import com.fangzuo.assist.Beans.SettingList;
import com.fangzuo.assist.Utils.GetSettingList;
import com.fangzuo.assist.Activity.ProductInStorageActivity;
import com.fangzuo.assist.Activity.PurchaseInStorageActivity;
import com.fangzuo.assist.Activity.PurchaseOrderActivity;
import com.fangzuo.assist.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class PurchaseFragment extends BaseFragment {
    @BindView(R.id.gv)
    GridView gv;
    Unbinder unbinder;
    private FragmentActivity mContext;
    public PurchaseFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_purchase, container, false);
        unbinder = ButterKnife.bind(this, v);
        mContext = getActivity();
        return v;
    }
    @Override
    protected void initData() {
//        String getPermit=share.getString(ShareInfo.USER_PERMIT);
//        String[] arylist = getPermit.split("\\-"); // 这样才能得到正确的结果
        GridViewAdapter ada = new GridViewAdapter(mContext, GetSettingList.getPurchaseList());
        gv.setAdapter(ada);
        ada.notifyDataSetChanged();
    }

    @Override
    protected void initListener() {
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                SettingList tv= (SettingList) ada.getItem(i);
//                Log.e("listitem",tv.tv);
//                switch (tv.tag){
                switch (i) {
                    case 0://采购订单
                        startNewActivity(PurchaseOrderActivity.class, null);
                        break;
                    case 1://外购入库
                        startNewActivity(PurchaseInStorageActivity.class, null);
                        break;
                    case 2://产品入库
                        startNewActivity(ProductInStorageActivity.class, null);
                        break;
                }
            }
        });
    }
    @Override
    public void initView() {
    }

    @Override
    protected void OnReceive(String barCode) {

    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

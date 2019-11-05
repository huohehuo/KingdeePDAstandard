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
import com.fangzuo.assist.Activity.ProduceAndGetActivity;
import com.fangzuo.assist.Adapter.GridViewAdapter;
import com.fangzuo.assist.Beans.SettingList;
import com.fangzuo.assist.Utils.GetSettingList;
import com.fangzuo.assist.Activity.PushDownActivity;
import com.fangzuo.assist.Activity.SaleOrderActivity;
import com.fangzuo.assist.Activity.SoldOutActivity;
import com.fangzuo.assist.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

//销售fragment
public class SaleFragment extends BaseFragment {
    @BindView(R.id.gv)
    GridView gv;
    Unbinder unbinder;
    private FragmentActivity mContext;
    public SaleFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sale, container, false);
        unbinder = ButterKnife.bind(this, v);
        mContext = getActivity();
        return v;
    }
    GridViewAdapter ada;
    @Override
    protected void initData() {
        //        String getPermit=share.getString(ShareInfo.USER_PERMIT);
//        String[] arylist = getPermit.split("\\-"); // 这样才能得到正确的结果
        ada = new GridViewAdapter(mContext, GetSettingList.getSaleList());
        gv.setAdapter(ada);
        ada.notifyDataSetChanged();
    }

    @Override
    protected void initListener() {
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SettingList tv= (SettingList) ada.getItem(i);
                Log.e("listitem",tv.tv);
                switch (tv.tag){
//                switch (i){
                    case "4"://销售订单
                        startNewActivity(SaleOrderActivity.class,null);
                        break;
                    case "5"://销售出库
                        startNewActivity(SoldOutActivity.class,null);
                        break;
                    case "6"://单据下推
                        startNewActivity(PushDownActivity.class,null);
                        break;
                    case "7"://生产领料
                        startNewActivity(ProduceAndGetActivity.class,null);
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

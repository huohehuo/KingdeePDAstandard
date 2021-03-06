package com.fangzuo.assist.Fragment;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.fangzuo.assist.ABase.BaseFragment;
import com.fangzuo.assist.Adapter.GridViewAdapter;
import com.fangzuo.assist.Adapter.Page1Adapter;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.Beans.SettingList;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.EventBusUtil;
import com.fangzuo.assist.Utils.GetSettingList;
import com.fangzuo.assist.Activity.ProductInStorageActivity;
import com.fangzuo.assist.Activity.PurchaseInStorageActivity;
import com.fangzuo.assist.Activity.PurchaseOrderActivity;
import com.fangzuo.assist.R;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class PurchaseFragment extends BaseFragment {
    @BindView(R.id.ry_data)
    EasyRecyclerView ryData;
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
//    GridViewAdapter ada;
    Page1Adapter adapter;
    @Override
    protected void initData() {
//        String getPermit=share.getString(ShareInfo.USER_PERMIT);
//        String[] arylist = getPermit.split("\\-"); // 这样才能得到正确的结果
//        ada = new GridViewAdapter(mContext, GetSettingList.getPurchaseList());
//        gv.setAdapter(ada);
//        ada.notifyDataSetChanged();
        ryData.setAdapter(adapter = new Page1Adapter(mContext));
        ryData.setLayoutManager(new GridLayoutManager(getActivity(), 3));
//        ryData.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter.addAll(GetSettingList.getPurchaseList());
    }

    @Override
    protected void initListener() {
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                SettingList tv = (SettingList) adapter.getAllData().get(position);
                Log.e("listitem", tv.tv);
                EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Click_Order,tv.tag));
            }
        });
//        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                SettingList tv= (SettingList) ada.getItem(i);
//                Log.e("listitem",tv.tv);
//                EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Click_Order,tv.tag));
//            }
//        });
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

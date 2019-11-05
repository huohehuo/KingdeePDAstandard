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
import com.fangzuo.assist.Activity.DBActivity;
import com.fangzuo.assist.Activity.OtherInStoreActivity;
import com.fangzuo.assist.Activity.OtherOutStoreActivity;
import com.fangzuo.assist.Activity.PDActivity;
import com.fangzuo.assist.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class StorageFragment extends BaseFragment {
    @BindView(R.id.gv)
    GridView gv;
    Unbinder unbinder;
    private FragmentActivity mContext;

    public StorageFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_storage, container, false);
        unbinder = ButterKnife.bind(this, v);
        return v;
    }
    GridViewAdapter ada;
    @Override
    protected void initData() {
        //        String getPermit=share.getString(ShareInfo.USER_PERMIT);
//        String[] arylist = getPermit.split("\\-"); // 这样才能得到正确的结果
        ada= new GridViewAdapter(mContext, GetSettingList.getStorageList());
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
                    case "8"://盘点
                        startNewActivity(PDActivity.class,null);
                        break;
                    case "9"://调拨
                        startNewActivity(DBActivity.class,null);
                        break;
                    case "10"://其他入库
                        startNewActivity(OtherInStoreActivity.class,null);
                        break;
                    case "11"://其他出库
                        startNewActivity(OtherOutStoreActivity.class,null);
                        break;
                }
            }
        });
    }

    @Override
    public void initView() {
        mContext = getActivity();
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

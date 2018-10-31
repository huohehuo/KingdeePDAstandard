package com.fangzuo.assist.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fangzuo.assist.Adapter.SearchAdapter;
import com.fangzuo.assist.Dao.Product;
import com.fangzuo.assist.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王璐阳 on 2018/1/4.
 */

public class PopUtil {
    private final SearchAdapter ada;
    Context mContext;
    View Edittext;
    private final ListView lv;
    private final PopupWindow popupWindow;
    private final ArrayList<Product> container;

    public PopUtil(Context mContext, View edittext, List<Product> products) {
        container = new ArrayList<>();
        this.mContext = mContext;
        Edittext = edittext;
        View v = LayoutInflater.from(mContext).inflate(R.layout.popview,null);
        lv = v.findViewById(R.id.lv_result);
        TextView tv_model = v.findViewById(R.id.model);
        TextView tv_name = v.findViewById(R.id.name);
        tv_model.setText("名称");
        tv_name.setText("型号");
        ada = new SearchAdapter(mContext, container);
        container.addAll(products);
        ada.notifyDataSetChanged();
        lv.setAdapter(ada);
        popupWindow = new PopupWindow(edittext.getWidth(),300);
    }

    public void setItemClickListener(AdapterView.OnItemClickListener listener){
        lv.setOnItemClickListener(listener);
    }

    public void showPop(){
        popupWindow.showAsDropDown(Edittext);
    }

    public void dismiss(){
        popupWindow.dismiss();
    }

    public void refreshList(List<Product> products){
        container.clear();
        container.addAll(products);
        ada.notifyDataSetChanged();
    }
}

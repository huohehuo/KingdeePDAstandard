package com.fangzuo.assist.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.fangzuo.assist.Dao.Product;
import com.fangzuo.assist.R;

import java.util.List;

/**
 * Created by NB on 2017/8/1.
 */

public class Popwindow{
    private static PopupWindow popupWindow;
    private static ListView lvPop;

    public static void showPop(Context context, List<Product> items, View v, final Itemclick itemclick) {
        View popView = LayoutInflater.from(context).inflate(R.layout.poplist, null);
        lvPop = popView.findViewById(R.id.lv);
        popView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWindow = new PopupWindow(popView, WindowManager.LayoutParams.MATCH_PARENT, 150);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(v);
        lvPop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    itemclick.itemClick(adapterView,view,i,l);
                }
            });

    }


    public interface Itemclick{
        void itemClick(AdapterView<?> adapterView, View view, int i, long l);
    }
}

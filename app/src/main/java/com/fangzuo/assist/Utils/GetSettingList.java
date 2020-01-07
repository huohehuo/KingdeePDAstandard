package com.fangzuo.assist.Utils;

import android.text.TextUtils;
import android.util.Log;

import com.fangzuo.assist.Beans.SettingList;
import com.fangzuo.assist.R;

import java.util.ArrayList;

/**
 * Created by NB on 2017/7/28.
 */

public class GetSettingList {
    public static ArrayList<SettingList> getPurchaseList() {//String[] ary
        ArrayList<SettingList> items = new ArrayList<>();
        //最终返回的选项
        ArrayList<SettingList> backItems = new ArrayList<>();
        items.add(new SettingList("1","采购订单",R.mipmap.purchaseorder));
        items.add(new SettingList("2","外购入库",R.mipmap.purchaseorback));
        items.add(new SettingList("3","产品入库",R.mipmap.purchaseinstorage));
//        for (int i=0; i<items.size();i++){
//            Log.e("test","定位ary:"+ary[i]);
//            Log.e("test","定位items:"+items.get(i).tag);
            //根据ary的值，遍历list符合的item并添加
//            for (int j=0;j<ary.length;j++){
//                if (TextUtils.equals(items.get(i).tag,ary[j])){
//                    Log.e("test","222加入："+items.get(i).toString());
//                    backItems.add(items.get(i));
//                }
//            }
//        }
        return items;
    }
    public static ArrayList<SettingList> getSaleList() {
        ArrayList<SettingList> items = new ArrayList<>();
        items.add(new SettingList("4","销售订单",R.mipmap.saleorder));
        items.add(new SettingList("5","销售出库",R.mipmap.sellinout));
        items.add(new SettingList("6","单据下推",R.mipmap.sellout));
        items.add(new SettingList("7","生产领料",R.mipmap.chuku));
        return items;
    }
    public static ArrayList<SettingList> getStorageList() {
        ArrayList<SettingList> items = new ArrayList<>();
        items.add(new SettingList("8","盘点",R.mipmap.pandian));
        items.add(new SettingList("9","调拨",R.mipmap.diaobo));
        items.add(new SettingList("10","其他入库",R.mipmap.ruku));
        items.add(new SettingList("11","其他出库",R.mipmap.chuku));
        return items;
    }

    public static ArrayList<SettingList> GetPushDownList() {
        ArrayList<SettingList> items = new ArrayList<>();
        items.add(new SettingList("12","销售订单下推销售出库",R.mipmap.pandian));//1
        items.add(new SettingList("13","采购订单下推外购入库",R.mipmap.diaobo));//2
        items.add(new SettingList("18","生产任务单下推产品入库",R.mipmap.ruku));//9
        items.add(new SettingList("19","生产任务单下推生产领料",R.mipmap.pandian));//13
        items.add(new SettingList("14","发货通知下推销售出库",R.mipmap.ruku));//3
        items.add(new SettingList("15","收料通知下推外购入库",R.mipmap.chuku));//4
        //---------------非通用单据---------------------------
        items.add(new SettingList("16","委外订单下推委外入库",R.mipmap.pandian));//11
        items.add(new SettingList("17","委外订单下推委外出库",R.mipmap.diaobo));//12
        items.add(new SettingList("20","采购订单下推收料通知单",R.mipmap.pandian));//14
        items.add(new SettingList("21","销售订单下推发料通知单",R.mipmap.pandian));//15
        items.add(new SettingList("22","生产任务单下推生产汇报单",R.mipmap.pandian));//16
        items.add(new SettingList("23","汇报单下推产品入库",R.mipmap.pandian));//18
        items.add(new SettingList("24","销售出库单验货",R.mipmap.pandian));//7
        items.add(new SettingList("25","发货通知生成调拨单",R.mipmap.pandian));//20
//        items.add(new SettingList("产品入库验货",R.mipmap.pandian));
        return items;
    }
    public static ArrayList<SettingList> getList() {
        ArrayList<SettingList> items = new ArrayList<>();
        items.add(new SettingList("下载配置",R.mipmap.download));
        items.add(new SettingList("wifi连接",R.mipmap.wifi));
        items.add(new SettingList("声音设置",R.mipmap.sound));
        items.add(new SettingList("更新版本",R.mipmap.getnewversion));
        items.add(new SettingList("服务器设置",R.mipmap.tomcat));
        items.add(new SettingList("网络测试",R.mipmap.test));
        return items;
    }
}

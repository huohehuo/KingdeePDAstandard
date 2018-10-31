package com.fangzuo.assist.MVContract;

import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Dao.PurchaseMethod;

import java.util.ArrayList;
import java.util.List;

public class ScreenList {

    public static List<PurchaseMethod> GoodsType(DownloadReturnBean dBean){
        //采购方式,交货方式
        List<PurchaseMethod> listGoodsType = new ArrayList<>();
        for (PurchaseMethod data : dBean.purchaseMethod) {
            if (data.getFTypeID().equals("32")) {
                listGoodsType.add(data);
            }
        }
        return listGoodsType;
    }
    public static List<PurchaseMethod> SaleMethod(DownloadReturnBean dBean){
        //销售方式
        List<PurchaseMethod> listSaleMethod = new ArrayList<>();
        for (PurchaseMethod data : dBean.purchaseMethod) {
            if (data.getFTypeID().equals("101") && !data.FNumber.equals("02")) {
                listSaleMethod.add(data);
            }
        }
        return listSaleMethod;
    }

    public static List<PurchaseMethod> SaleScope(DownloadReturnBean dBean){
        //销售范围
        List<PurchaseMethod> list = new ArrayList<>();
        for (PurchaseMethod data : dBean.purchaseMethod) {
            if (data.getFTypeID().equals("997")) {
                list.add(data);
            }
        }
        return list;
    }

}

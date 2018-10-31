package com.fangzuo.assist.Beans;

import java.util.ArrayList;

/**
 * Created by NB on 2017/8/4.
 */

public class PurchaseInStoreUploadBean {
    public ArrayList<purchaseInStore> list;
    public class purchaseInStore{
        public String main;
        public ArrayList<String> detail;
    }
}

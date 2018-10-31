package com.fangzuo.assist.Beans;

import java.util.List;

/**
 * Created by NB on 2017/7/24.
 */

public class ProductReturnBean {
    public List<productBean> returnList;
    public class productBean{
        public String FName;
        public String FTypeID;
        public String FItemID;
        public String FBarCode;
        public String FNumber;
        public String FUnitID;
        public String FModel;
        public String FUnitGroupID;
        public String FDefaultLoc;
        public String FBatchManager;
        public String FSalePrice;
        public String FSPID;
    }
}

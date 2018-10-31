package com.fangzuo.assist.Beans;

import java.util.ArrayList;

/**
 * Created by NB on 2017/7/24.
 */

public class ConnectResponseBean {
    public ArrayList<DataBaseList> DataBaseList;
    public class DataBaseList{
        public String name;
        public String dataBaseName;
    }
}

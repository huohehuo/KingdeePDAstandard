package com.fangzuo.assist.Utils;

public class Config {
    //    public static final String Error_Url = "http://192.168.0.115:8083/Assist/GetLogMessage";
    public static final String Error_Url = "http://148.70.108.65:8080/LogAssist/GetLogMessage";
//    public static final String Setting_Url = "http://148.70.108.65:8080/LogAssist/GetSettingData";//配置文件地址
    public static final String Setting_Url = "http://192.168.0.136:8083/Assist/GetSettingData";//配置文件地址
//    public static final String Apk_Url = "http://148.70.108.65:8080/AppFile/Standerd/app-debug.apk";
    public static final String Apk_Url = "https://slbserver-10034414.cos.ap-shanghai.myqcloud.com/LB0323.apk";
    public static final String Json_Url = "http://148.70.108.65:8080/JsonFile/setting.txt";
    public static final String Data_Url = "DownData/AllData.txt";
    public static String Company="通用版";
    public static String User_Permit="User_Permit";
    public static String PDA_IMIE="PDA_IMIE";//用于保存注册码
    public static String PDA_RegisterCode="PDA_RegisterCode";//用于保存注册码
    public static String PDA_RegisterMaxNum="PDA_RegisterMaxNum";//用于保存注册码
    public static String VersionReceiver="com.fangzuo.assist.version";//用于保存注册码
    public static String CheckAutoLogin="CheckAutoLogin";//是否自动登录
    public static String AutoLoginName="AutoLoginName";//是否自动登录
    public static String AutoLoginPwd="AutoLoginPwd";//是否自动登录

    public static final String DATABASESETTING = "master";
    public static final String PDA = "PDA";
    public static final String[] PDA_Type = {"请选择设备型号","G02A设备", "8000设备", "5000设备","M60","新大陆","M36","M80s","肖邦","手机端","优博讯","M71","M82"};

    //用于接口回调的判断------------------------------------------
    public static final String IO_type_Test="IO_type_Test";
    public static final String IO_type_TestDataBase="IO_type_TestDataBase";
    public static final String IO_type_SetProp="IO_type_SetProp";
    public static final String IO_type_connectToSQL="IO_type_connectToSQL";


    //用于Presenter下载表的type区分
    public static final int Data_Bibie           =1;    //币别表
    public static final int Data_Department      =2;    //部门表
    public static final int Data_Employee        =3;    //职员表
    public static final int Data_WaveHouse       =4;    //仓位表
    public static final int Data_InstorageNums   =5;    //库存表
    public static final int Data_Storage         =6;    //仓库表
    public static final int Data_Unit            =7;    //单位表
    public static final int Data_BarCodes        =8;    //条码表":
    public static final int Data_Suppliers       =9;    //供应商表"
    public static final int Data_PayTypes        =10;   //结算方式表
    public static final int Data_Product         =11;   //商品资料表
    public static final int Data_User            =12;   //用户信息表
    public static final int Data_Client          =13;   //客户信息表
    public static final int Data_GoodsDepartments=14;   //交货单位"
    public static final int Data_PurchaseMethod  =15;   //销售/采购方式表
    public static final int Data_yuandanType     =16;   //源单类型"
    public static final int Data_Wanglaikemu     =17;   //往来科目"
    public static final int Data_PriceMethods    =18;   //价格政策"
    public static final int Data_InStoreType     =19;   //入库类型"
    public static final int Data_Company         =20;   //公司信息表
    public static final int Data_Product_Type    =21;   //物料类别


    public static final String Text_Log    ="Text_Log1";   //物料类别

    public static final int PushDownMTActivity             =10001;
    public static final int PushDownPOActivity             =10002;
    public static final int PushDownSNActivity             =10003;
    public static final int ShouLiaoTongZhiActivity        =10004;
    public static final int OutsourcingOrdersISActivity    =10005;
    public static final int OutsourcingOrdersOSActivity    =10006;
    public static final int ProducePushInStoreActivity     =10007;
    public static final int ShengchanrenwudanxiatuilingliaoActivity =10008;
    public static final int CGDDPDSLTZDActivity                     =10009;
    public static final int XSDDPDFLTZDActivity                     =10010;
    public static final int SCRWDPDSCHBDActivity                    =10011;
    public static final int HBDPDCPRKActivity                       =10012;
    public static final int OutCheckGoodsActivity                   =10013;
    public static final int FHTZDDBActivity                         =10014;
    public static final int PurchaseInStorageActivity               =10016;
    public static final int ProductInStorageActivity                =10017;
    public static final int OtherInStoreActivity                    =10018;
    public static final int SoldOutActivity                         =10019;
    public static final int ProduceAndGetActivity                   =10020;
    public static final int OtherOutStoreActivity                   =10021;
    public static final int PurchaseOrderActivity                   =10022;
    public static final int SaleOrderActivity                       =10023;
    public static final int PDActivity                              =10024;
    public static final int DBActivity                              =10025;

}

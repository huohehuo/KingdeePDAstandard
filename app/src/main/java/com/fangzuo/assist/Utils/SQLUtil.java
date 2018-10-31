package com.fangzuo.assist.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by NB on 2017/7/26.
 */

public class SQLUtil extends SQLiteOpenHelper {
    //1
    public static final String BIBIETABLE = "bibie";
    public static final String FCurrencyID = "FCurrencyID";
    public static final String FNumber = "FNumber";
    public static final String FName = "FName";
    public static final String FExChangeRate = "FExChangeRate";
    public static final String FClassTypeId = "FClassTypeId";
    //2
    public static final String DEPARTMENTTABLE = "DEPARTMENTTABLE";
    public static final String FItemID = "FItemID";
    public static final String FparentID = "FparentID";
    //3
    public static final String EMPLOYEETABLE = "EMPLOYEETABLE";
    public static final String FDpartmentID = "FDpartmentID";
    public static final String FEmpGroup = "FEmpGroup";
    public static final String FEmpGroupID = "FEmpGroupID";
    //4
    public static final String WAVEHOUSETABLE = "wavehouse";
    public static final String FSPID = "FSPID";
    public static final String FFullName = "FFullName";
    public static final String FDetail = "FDetail";
    public static final String FParentID = "FParentID";
    public static final String FClassTypeID = "FClassTypeID";
    public static final String FDefaultSPID = "FDefaultSPID";
    //5
    public static final String InstorageNum = "InstorageNum";
    public static final String FStockID = "FStockID";
    public static final String FQty = "FQty";
    public static final String FBal = "FBal";
    public static final String FStockPlaceID = "FStockPlaceID";
    public static final String FKFPeriod = "FKFPeriod";
    public static final String FKFDate = "FKFDate";
    public static final String FBatchNo = "FBatchNo";
    //6
    public static final String storage = "storage";
    public static final String FEmpID = "FEmpID";
    public static final String FTypeID = "FTypeID";
    public static final String FSPGroupID = "FSPGroupID";
    public static final String FGroupID = "FGroupID";
    public static final String FStockGroupID = "FStockGroupID";
    public static final String FIsStockMgr = "FIsStockMgr";
    public static final String FUnderStock = "FUnderStock";
    //7
    public static final String Unit = "Unit";
    public static final String FMeasureUnitID = "FMeasureUnitID";
    public static final String FUnitGroupID = "FUnitGroupID";
    public static final String FCoefficient = "FCoefficient";
    //8
    public static final String BarCode = "BarCode";
    public static final String FBarCode = "FBarCode";
    public static final String FUnitID = "FUnitID";
    //9
    public static final String suppliers = "suppliers";
    public static final String FItemClassID = "FItemClassID";
    public static final String FLevel = "FLevel";
    public static final String FAddress = "FAddress";
    public static final String FPhone = "FPhone";
    public static final String FEmail = "FEmail";
    //10
    public static final String payType = "payType";
    //11
    public static final String product = "product";
    public static final String FModel = "FModel";
    public static final String FTaxRate = "FTaxRate";
    public static final String FDefaultLoc = "FDefaultLoc";
    public static final String FProfitRate = "FProfitRate";
    public static final String FOrderPrice = "FOrderPrice";
    public static final String FSalePrice = "FSalePrice";
    public static final String FPlanPrice = "FPlanPrice";
    public static final String FBatchManager = "FBatchManager";
    //12
    public static final String User = "User";
    public static final String FUserID = "FBatchManager";
    public static final String FPassWord = "FPassWord";
    public static final String FPermit = "FPermit";
    public static final String FGroupName = "FGroupName";
    //13
    public static final String Client = "Client";
    //14
    public static final String GetGoodsDepartment = "GetGoodsDepartment";
    public static final String FDeleted = "FDeleted";
    //15
    public static final String purchaseMethod = "purchaseMethod";
    //16
    public static final String yuandanType = "yuandanType";
    public static final String FID = "FID";
    public static final String FName_CHS = "FName_CHS";
    //17
    public static final String wanglaikemu = "wanglaikemu";
    public static final String FAccountID = "FAccountID";
    //18
    public static final String PriceMethod = "PriceMethod";
    public static final String FInterID = "FInterID";
    public static final String FEntryID = "FEntryID";
    public static final String FPri = "FPri";
    public static final String FRelatedID = "FRelatedID";
    public static final String FBegQty = "FBegQty";
    public static final String FEndQty = "FEndQty";
    public static final String FBegDate = "FBegDate";
    public static final String FEndDate = "FEndDate";
    public static final String FPrice = "FPrice";



    public SQLUtil(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "info", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

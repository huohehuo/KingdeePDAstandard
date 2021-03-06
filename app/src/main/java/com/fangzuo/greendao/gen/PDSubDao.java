package com.fangzuo.greendao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.fangzuo.assist.Dao.PDSub;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "PDSUB".
*/
public class PDSubDao extends AbstractDao<PDSub, Long> {

    public static final String TABLENAME = "PDSUB";

    /**
     * Properties of entity PDSub.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property FID = new Property(1, String.class, "FID", false, "FID");
        public final static Property FStockID = new Property(2, String.class, "FStockID", false, "FSTOCK_ID");
        public final static Property FItemID = new Property(3, String.class, "FItemID", false, "FITEM_ID");
        public final static Property FSPName = new Property(4, String.class, "FSPName", false, "FSPNAME");
        public final static Property FNumber = new Property(5, String.class, "FNumber", false, "FNUMBER");
        public final static Property FName = new Property(6, String.class, "FName", false, "FNAME");
        public final static Property FModel = new Property(7, String.class, "FModel", false, "FMODEL");
        public final static Property FStockPlaceID = new Property(8, String.class, "FStockPlaceID", false, "FSTOCK_PLACE_ID");
        public final static Property FUnitName = new Property(9, String.class, "FUnitName", false, "FUNIT_NAME");
        public final static Property FQty = new Property(10, String.class, "FQty", false, "FQTY");
        public final static Property FQtyAct1 = new Property(11, String.class, "FQtyAct1", false, "FQTY_ACT1");
        public final static Property FQtyAct = new Property(12, String.class, "FQtyAct", false, "FQTY_ACT");
        public final static Property FCheckQty1 = new Property(13, String.class, "FCheckQty1", false, "FCHECK_QTY1");
        public final static Property FCheckQty = new Property(14, String.class, "FCheckQty", false, "FCHECK_QTY");
        public final static Property FAdjQty1 = new Property(15, String.class, "FAdjQty1", false, "FADJ_QTY1");
        public final static Property FAdjQty = new Property(16, String.class, "FAdjQty", false, "FADJ_QTY");
        public final static Property FRemark = new Property(17, String.class, "FRemark", false, "FREMARK");
        public final static Property FUnitID = new Property(18, String.class, "FUnitID", false, "FUNIT_ID");
        public final static Property FUnitGroupID = new Property(19, String.class, "FUnitGroupID", false, "FUNIT_GROUP_ID");
        public final static Property FBatchNo = new Property(20, String.class, "FBatchNo", false, "FBATCH_NO");
    }


    public PDSubDao(DaoConfig config) {
        super(config);
    }
    
    public PDSubDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"PDSUB\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"FID\" TEXT," + // 1: FID
                "\"FSTOCK_ID\" TEXT," + // 2: FStockID
                "\"FITEM_ID\" TEXT," + // 3: FItemID
                "\"FSPNAME\" TEXT," + // 4: FSPName
                "\"FNUMBER\" TEXT," + // 5: FNumber
                "\"FNAME\" TEXT," + // 6: FName
                "\"FMODEL\" TEXT," + // 7: FModel
                "\"FSTOCK_PLACE_ID\" TEXT," + // 8: FStockPlaceID
                "\"FUNIT_NAME\" TEXT," + // 9: FUnitName
                "\"FQTY\" TEXT," + // 10: FQty
                "\"FQTY_ACT1\" TEXT," + // 11: FQtyAct1
                "\"FQTY_ACT\" TEXT," + // 12: FQtyAct
                "\"FCHECK_QTY1\" TEXT," + // 13: FCheckQty1
                "\"FCHECK_QTY\" TEXT," + // 14: FCheckQty
                "\"FADJ_QTY1\" TEXT," + // 15: FAdjQty1
                "\"FADJ_QTY\" TEXT," + // 16: FAdjQty
                "\"FREMARK\" TEXT," + // 17: FRemark
                "\"FUNIT_ID\" TEXT," + // 18: FUnitID
                "\"FUNIT_GROUP_ID\" TEXT," + // 19: FUnitGroupID
                "\"FBATCH_NO\" TEXT);"); // 20: FBatchNo
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"PDSUB\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, PDSub entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String FID = entity.getFID();
        if (FID != null) {
            stmt.bindString(2, FID);
        }
 
        String FStockID = entity.getFStockID();
        if (FStockID != null) {
            stmt.bindString(3, FStockID);
        }
 
        String FItemID = entity.getFItemID();
        if (FItemID != null) {
            stmt.bindString(4, FItemID);
        }
 
        String FSPName = entity.getFSPName();
        if (FSPName != null) {
            stmt.bindString(5, FSPName);
        }
 
        String FNumber = entity.getFNumber();
        if (FNumber != null) {
            stmt.bindString(6, FNumber);
        }
 
        String FName = entity.getFName();
        if (FName != null) {
            stmt.bindString(7, FName);
        }
 
        String FModel = entity.getFModel();
        if (FModel != null) {
            stmt.bindString(8, FModel);
        }
 
        String FStockPlaceID = entity.getFStockPlaceID();
        if (FStockPlaceID != null) {
            stmt.bindString(9, FStockPlaceID);
        }
 
        String FUnitName = entity.getFUnitName();
        if (FUnitName != null) {
            stmt.bindString(10, FUnitName);
        }
 
        String FQty = entity.getFQty();
        if (FQty != null) {
            stmt.bindString(11, FQty);
        }
 
        String FQtyAct1 = entity.getFQtyAct1();
        if (FQtyAct1 != null) {
            stmt.bindString(12, FQtyAct1);
        }
 
        String FQtyAct = entity.getFQtyAct();
        if (FQtyAct != null) {
            stmt.bindString(13, FQtyAct);
        }
 
        String FCheckQty1 = entity.getFCheckQty1();
        if (FCheckQty1 != null) {
            stmt.bindString(14, FCheckQty1);
        }
 
        String FCheckQty = entity.getFCheckQty();
        if (FCheckQty != null) {
            stmt.bindString(15, FCheckQty);
        }
 
        String FAdjQty1 = entity.getFAdjQty1();
        if (FAdjQty1 != null) {
            stmt.bindString(16, FAdjQty1);
        }
 
        String FAdjQty = entity.getFAdjQty();
        if (FAdjQty != null) {
            stmt.bindString(17, FAdjQty);
        }
 
        String FRemark = entity.getFRemark();
        if (FRemark != null) {
            stmt.bindString(18, FRemark);
        }
 
        String FUnitID = entity.getFUnitID();
        if (FUnitID != null) {
            stmt.bindString(19, FUnitID);
        }
 
        String FUnitGroupID = entity.getFUnitGroupID();
        if (FUnitGroupID != null) {
            stmt.bindString(20, FUnitGroupID);
        }
 
        String FBatchNo = entity.getFBatchNo();
        if (FBatchNo != null) {
            stmt.bindString(21, FBatchNo);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, PDSub entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String FID = entity.getFID();
        if (FID != null) {
            stmt.bindString(2, FID);
        }
 
        String FStockID = entity.getFStockID();
        if (FStockID != null) {
            stmt.bindString(3, FStockID);
        }
 
        String FItemID = entity.getFItemID();
        if (FItemID != null) {
            stmt.bindString(4, FItemID);
        }
 
        String FSPName = entity.getFSPName();
        if (FSPName != null) {
            stmt.bindString(5, FSPName);
        }
 
        String FNumber = entity.getFNumber();
        if (FNumber != null) {
            stmt.bindString(6, FNumber);
        }
 
        String FName = entity.getFName();
        if (FName != null) {
            stmt.bindString(7, FName);
        }
 
        String FModel = entity.getFModel();
        if (FModel != null) {
            stmt.bindString(8, FModel);
        }
 
        String FStockPlaceID = entity.getFStockPlaceID();
        if (FStockPlaceID != null) {
            stmt.bindString(9, FStockPlaceID);
        }
 
        String FUnitName = entity.getFUnitName();
        if (FUnitName != null) {
            stmt.bindString(10, FUnitName);
        }
 
        String FQty = entity.getFQty();
        if (FQty != null) {
            stmt.bindString(11, FQty);
        }
 
        String FQtyAct1 = entity.getFQtyAct1();
        if (FQtyAct1 != null) {
            stmt.bindString(12, FQtyAct1);
        }
 
        String FQtyAct = entity.getFQtyAct();
        if (FQtyAct != null) {
            stmt.bindString(13, FQtyAct);
        }
 
        String FCheckQty1 = entity.getFCheckQty1();
        if (FCheckQty1 != null) {
            stmt.bindString(14, FCheckQty1);
        }
 
        String FCheckQty = entity.getFCheckQty();
        if (FCheckQty != null) {
            stmt.bindString(15, FCheckQty);
        }
 
        String FAdjQty1 = entity.getFAdjQty1();
        if (FAdjQty1 != null) {
            stmt.bindString(16, FAdjQty1);
        }
 
        String FAdjQty = entity.getFAdjQty();
        if (FAdjQty != null) {
            stmt.bindString(17, FAdjQty);
        }
 
        String FRemark = entity.getFRemark();
        if (FRemark != null) {
            stmt.bindString(18, FRemark);
        }
 
        String FUnitID = entity.getFUnitID();
        if (FUnitID != null) {
            stmt.bindString(19, FUnitID);
        }
 
        String FUnitGroupID = entity.getFUnitGroupID();
        if (FUnitGroupID != null) {
            stmt.bindString(20, FUnitGroupID);
        }
 
        String FBatchNo = entity.getFBatchNo();
        if (FBatchNo != null) {
            stmt.bindString(21, FBatchNo);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public PDSub readEntity(Cursor cursor, int offset) {
        PDSub entity = new PDSub( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // FID
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // FStockID
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // FItemID
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // FSPName
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // FNumber
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // FName
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // FModel
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // FStockPlaceID
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // FUnitName
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // FQty
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // FQtyAct1
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // FQtyAct
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // FCheckQty1
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // FCheckQty
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // FAdjQty1
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // FAdjQty
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // FRemark
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // FUnitID
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19), // FUnitGroupID
            cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20) // FBatchNo
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, PDSub entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setFID(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setFStockID(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setFItemID(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setFSPName(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setFNumber(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setFName(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setFModel(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setFStockPlaceID(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setFUnitName(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setFQty(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setFQtyAct1(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setFQtyAct(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setFCheckQty1(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setFCheckQty(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setFAdjQty1(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setFAdjQty(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setFRemark(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setFUnitID(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setFUnitGroupID(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setFBatchNo(cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(PDSub entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(PDSub entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(PDSub entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}

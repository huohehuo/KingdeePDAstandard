package com.fangzuo.assist.Utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.fangzuo.greendao.gen.BarCodeDao;
import com.fangzuo.greendao.gen.BibieDao;
import com.fangzuo.greendao.gen.BibieTableDao;
import com.fangzuo.greendao.gen.ClientDao;
import com.fangzuo.greendao.gen.DaoMaster;
import com.fangzuo.greendao.gen.DepartmentDao;
import com.fangzuo.greendao.gen.EmployeeDao;
import com.fangzuo.greendao.gen.GetGoodsDepartmentDao;
import com.fangzuo.greendao.gen.InStorageNumDao;
import com.fangzuo.greendao.gen.InStoreTypeDao;
import com.fangzuo.greendao.gen.NoticBeanDao;
import com.fangzuo.greendao.gen.PDMainDao;
import com.fangzuo.greendao.gen.PDSubDao;
import com.fangzuo.greendao.gen.PayTypeDao;
import com.fangzuo.greendao.gen.PriceMethodDao;
import com.fangzuo.greendao.gen.ProductDao;
import com.fangzuo.greendao.gen.PurchaseMethodDao;
import com.fangzuo.greendao.gen.PushDownMainDao;
import com.fangzuo.greendao.gen.PushDownSubDao;
import com.fangzuo.greendao.gen.StorageDao;
import com.fangzuo.greendao.gen.SuppliersDao;
import com.fangzuo.greendao.gen.T_DetailDao;
import com.fangzuo.greendao.gen.T_mainDao;
import com.fangzuo.greendao.gen.UnitDao;
import com.fangzuo.greendao.gen.UserDao;
import com.fangzuo.greendao.gen.WanglaikemuDao;
import com.fangzuo.greendao.gen.WaveHouseDao;
import com.fangzuo.greendao.gen.YuandanTypeDao;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.internal.DaoConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2019/11/15.
 */

public class DbUpgradeHelper extends DaoMaster.DevOpenHelper {
    private static final String CONVERSION_CLASS_NOT_FOUND_EXCEPTION =
            "MIGRATION HELPER - CLASS DOESN'T MATCH WITH THE CURRENT PARAMETERS";

    public DbUpgradeHelper(Context context, String name) {
        super(context, name);
    }

    public DbUpgradeHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
       /*此处不用super，因为父类中包含了
       dropAllTables(db, true);
        onCreate(db);
        需要自己定制升级
        */

       /*
                注意，新加的表，若是下个班就进行添加到下面进行数据备份，将会崩溃，
                理应下个版本都更新完后，已经存在了新表，才添加到下面进行处理备份

                */

       migrate(db,
               BarCodeDao.class,
               BibieDao.class,
               BibieTableDao.class,
               ClientDao.class,
               DepartmentDao.class,
               EmployeeDao.class,
               GetGoodsDepartmentDao.class,
               InStorageNumDao.class,
               InStoreTypeDao.class,
               PayTypeDao.class,
               PDMainDao.class,
               PDSubDao.class,
               PriceMethodDao.class,
               ProductDao.class,
               PurchaseMethodDao.class,
               PushDownMainDao.class,
               PushDownSubDao.class,
               StorageDao.class,
               SuppliersDao.class,
               T_DetailDao.class,
               T_mainDao.class,
               UnitDao.class,
               UserDao.class,
               WanglaikemuDao.class,
               WaveHouseDao.class,
               YuandanTypeDao.class,
               NoticBeanDao.class
       );
    }

    /**
     * 删除原表重新再建立一个表
     * @param db
     */
    public void dropAndCreate(Database db){
        DaoMaster.dropAllTables(db, true);
        DaoMaster.createAllTables(db, false);
    }

    /**
     * 备份还原
     * @param db
     * @param daoClasses
     */
    public void migrate(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        generateTempTables(db, daoClasses);
        DaoMaster.dropAllTables(db, true);
        DaoMaster.createAllTables(db, false);
        restoreData(db, daoClasses);
    }
    /**
     * 数据库备份
     * @param db
     * @param daoClasses
     */
    private void generateTempTables(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        for (int i = 0; i < daoClasses.length; i++) {
            try {
                DaoConfig daoConfig = new DaoConfig(db, daoClasses[i]);

                String divider = "";
                String tableName = daoConfig.tablename;
                String tempTableName = daoConfig.tablename.concat("_TEMP");
                ArrayList<String> properties = new ArrayList<>();

                StringBuilder createTableStringBuilder = new StringBuilder();

                createTableStringBuilder.append("CREATE TABLE ").append(tempTableName).append(" (");

                for (int j = 0; j < daoConfig.properties.length; j++) {
                    String columnName = daoConfig.properties[j].columnName;

                    if (getColumns(db, tableName).contains(columnName)) {
                        properties.add(columnName);

                        String type = null;

                        try {
                            type = getTypeByClass(daoConfig.properties[j].type);
                        } catch (Exception exception) {
                        }

                        createTableStringBuilder.append(divider).append(columnName).append(" ").append(type);

                        if (daoConfig.properties[j].primaryKey) {
                            createTableStringBuilder.append(" PRIMARY KEY");
                        }

                        divider = ",";
                    }
                }
                createTableStringBuilder.append(");");

                db.execSQL(createTableStringBuilder.toString());

                StringBuilder insertTableStringBuilder = new StringBuilder();

                insertTableStringBuilder.append("INSERT INTO ").append(tempTableName).append(" (");
                insertTableStringBuilder.append(TextUtils.join(",", properties));
                insertTableStringBuilder.append(") SELECT ");
                insertTableStringBuilder.append(TextUtils.join(",", properties));
                insertTableStringBuilder.append(" FROM ").append(tableName).append(";");

                db.execSQL(insertTableStringBuilder.toString());
            }catch (Exception e){
                Lg.e("新表备份会报错");
//                continue;
            }

        }
    }
    /**
     * 数据库恢复
     * @param db
     * @param daoClasses
     */
    private void restoreData(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        for (int i = 0; i < daoClasses.length; i++) {
            DaoConfig daoConfig = new DaoConfig(db, daoClasses[i]);

            String tableName = daoConfig.tablename;
            String tempTableName = daoConfig.tablename.concat("_TEMP");
            ArrayList<String> properties = new ArrayList();

            for (int j = 0; j < daoConfig.properties.length; j++) {
                String columnName = daoConfig.properties[j].columnName;

                if (getColumns(db, tempTableName).contains(columnName)) {
                    properties.add(columnName);
                }
            }

            StringBuilder insertTableStringBuilder = new StringBuilder();

            insertTableStringBuilder.append("INSERT INTO ").append(tableName).append(" (");
            insertTableStringBuilder.append(TextUtils.join(",", properties));
            insertTableStringBuilder.append(") SELECT ");
            insertTableStringBuilder.append(TextUtils.join(",", properties));
            insertTableStringBuilder.append(" FROM ").append(tempTableName).append(";");

            StringBuilder dropTableStringBuilder = new StringBuilder();

            dropTableStringBuilder.append("DROP TABLE ").append(tempTableName);

            db.execSQL(insertTableStringBuilder.toString());
            db.execSQL(dropTableStringBuilder.toString());
        }
    }
    private String getTypeByClass(Class<?> type) throws Exception {
        if (type.equals(String.class)) {
            return "TEXT";
        }
        if (type.equals(Long.class) || type.equals(Integer.class) || type.equals(long.class)) {
            return "INTEGER";
        }
        if (type.equals(Boolean.class)) {
            return "BOOLEAN";
        }

        Exception exception =
                new Exception(CONVERSION_CLASS_NOT_FOUND_EXCEPTION.concat(" - Class: ").concat(type.toString()));
        throw exception;
    }

    private static List<String> getColumns(Database db, String tableName) {
        List<String> columns = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + tableName + " limit 1", null);
            if (cursor != null) {
                columns = new ArrayList<>(Arrays.asList(cursor.getColumnNames()));
            }
        } catch (Exception e) {
            Log.v(tableName, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        return columns;
    }
    /**
     * 对比差异，在原表中直接添加column,赞不做删除操作
     * @param db
     * @param daoClasses
     */
    public void contrastDiff(Database db,ArrayList<String>properties, Class<? extends AbstractDao<?, ?>>... daoClasses){
        for(int i=0;i<daoClasses.length;i++){
            DaoConfig daoConfig = new DaoConfig(db, daoClasses[i]);
            String tableName=daoConfig.tablename;
            if(properties!=null&&properties.size()>0){
                ArrayList<String>tem=new ArrayList<>();
                StringBuilder sqlBuilder=new StringBuilder();
                for(int j=0;j<properties.size();j++){
                    if(getColumns(db,tableName).contains(properties.get(j))){
                        continue;
                    }
                    tem.add(properties.get(j));
                }
                sqlBuilder.append("INSERT INTO ").append(tableName).append(" (");
                sqlBuilder.append(TextUtils.join(",", tem));
                sqlBuilder.append(") SELECT ");
                sqlBuilder.append(TextUtils.join(",", tem));
                sqlBuilder.append(" FROM ").append(tableName).append(";");
                db.execSQL(sqlBuilder.toString());
            }
        }
    }


}
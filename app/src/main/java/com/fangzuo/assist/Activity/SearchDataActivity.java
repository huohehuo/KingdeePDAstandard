package com.fangzuo.assist.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Adapter.SearchAdapter;
import com.fangzuo.assist.Adapter.SearchManAdapter;
import com.fangzuo.assist.Adapter.SearchStorageAdapter;
import com.fangzuo.assist.Adapter.SearchWaveHouseAdapter;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.Beans.SearchBean;
import com.fangzuo.assist.Dao.Client;
import com.fangzuo.assist.Dao.Employee;
import com.fangzuo.assist.Dao.GetGoodsDepartment;
import com.fangzuo.assist.Dao.Product;
import com.fangzuo.assist.Dao.Storage;
import com.fangzuo.assist.Dao.Suppliers;
import com.fangzuo.assist.Dao.WaveHouse;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.EventBusUtil;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.greendao.gen.EmployeeDao;
import com.fangzuo.greendao.gen.StorageDao;
import com.fangzuo.greendao.gen.WaveHouseDao;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchDataActivity extends BaseActivity {


    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.lv_result)
    ListView lvResult;
    @BindView(R.id.cancle)
    View cancle;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.model)
    TextView model;
    @BindView(R.id.name)
    TextView name;
    private String searchString;
    private String searchString4wavehouse;
    private String backBus;
    private SearchDataActivity mContext;
    private SearchAdapter ada;
    private SearchStorageAdapter searchStorageAdapter;
    private SearchWaveHouseAdapter searchWaveHouseAdapter;
    private List<Product> items1;
    private List<Product> items;
    private List<Employee> employeeList;
    private List<Product> itemAll;
    //    private int where;
    private List<Suppliers> itemAllSupplier;
    private List<Suppliers> suppliersList;
    private List<Storage> storageList;
    private List<WaveHouse> waveHouseList;
    private List<Client> itemAllClient;
    private List<Client> itemClient;
    private List<GetGoodsDepartment> goodsDepartmentList;
    private List<GetGoodsDepartment> goodsDepartmentAllList;

    @Override
    public void initView() {
        setContentView(R.layout.activity_search_data);
        ButterKnife.bind(this);
        mContext = this;

        //接收
        Intent intent = getIntent();
        if (intent != null) {
            searchString = intent.getStringExtra("search");
            searchString4wavehouse = intent.getStringExtra("search_storage");
            backBus = intent.getStringExtra("backBus");
//            where = intent.getIntExtra("where",0);
//            fidcontainer = intent.getStringArrayListExtra("fid");
//            Lg.e("Intent:"+fidcontainer.toString());
        }
//        if (where == Info.Search_Man) title.setText("查询结果(员工)");
//        if (where == Info.SEARCHPRODUCT) title.setText("查询结果(物料)");
//        if (where == Info.SEARCHSUPPLIER) title.setText("查询结果(供应商)");
//        if (where == Info.SEARCHCLIENT) title.setText("查询结果(客户)");
//        if (where == Info.SEARCHJH) title.setText("查询结果(交货单位)");
        if (EventBusInfoCode.Storage.equals(backBus)) title.setText("查询结果(仓库)");
        if (EventBusInfoCode.Storage2.equals(backBus)) title.setText("查询结果(调入仓库)");
        if (EventBusInfoCode.WaveHouse.equals(backBus)) title.setText("查询结果(仓位)");
        if (EventBusInfoCode.WaveHouse2.equals(backBus)) title.setText("查询结果(调入仓位)");
        if (EventBusInfoCode.Man.equals(backBus)) title.setText("查询结果(员工)");
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(ClassEvent event) {
        super.receiveEvent(event);
    }

    @Override
    public void initData() {
        if (EventBusInfoCode.Storage.equals(backBus)||EventBusInfoCode.Storage2.equals(backBus)) {
            model.setText("编码");
            name.setText("名称");
            if (BasicShareUtil.getInstance(mContext).getIsOL()) {
                App.getRService().doIOAction("StorageSearchLike", searchString, new MySubscribe<CommonResponse>() {
                    @Override
                    public void onNext(CommonResponse commonResponse) {
                        super.onNext(commonResponse);
                        DownloadReturnBean dBean = new Gson().fromJson(commonResponse.returnJson, DownloadReturnBean.class);
                        storageList = dBean.storage;
                        if (storageList.size() > 0) {
                            searchStorageAdapter = new SearchStorageAdapter(mContext, storageList);
                            lvResult.setAdapter(searchStorageAdapter);
                            searchStorageAdapter.notifyDataSetChanged();
                        } else {
                            Toast.showText(mContext, "无数据");
                            setResult(-9998, null);
                            onBackPressed();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Toast.showText(mContext, e.getMessage());
                    }
                });
            }else{
                StorageDao storageDao = daoSession.getStorageDao();
                List<Storage> storages = storageDao.queryBuilder().whereOr(
                        StorageDao.Properties.FItemID.eq(searchString),
                        StorageDao.Properties.FNumber.eq(searchString)
                ).build().list();
                storageList = storages;
                if (storageList.size() > 0) {
                    searchStorageAdapter = new SearchStorageAdapter(mContext, storageList);
                    lvResult.setAdapter(searchStorageAdapter);
                    searchStorageAdapter.notifyDataSetChanged();
                } else {
                    Toast.showText(mContext, "无数据");
                    setResult(-9998, null);
                    onBackPressed();
                }
            }
//            else {
//                model.setText("编码");
//                name.setText("名称");
//                ProductDao productDao = GreenDaoManager.getmInstance(mContext).getDaoSession().getProductDao();
//                items = productDao.queryBuilder().whereOr(
//                        ProductDao.Properties.FNumber.like("%" + searchString + "%"),
//                        ProductDao.Properties.FBarcode.like("%" + searchString + "%"),
//                        ProductDao.Properties.FName.like("%" + searchString + "%")).
//                        orderAsc(ProductDao.Properties.FNumber).limit(50).orderAsc(ProductDao.Properties.FNumber).build().list();
//                itemAll = new ArrayList<>();
//                itemAll.addAll(items);
//                if (itemAll.size() > 0) {
//                    ada = new SearchAdapter(mContext, itemAll);
//                    lvResult.setAdapter(ada);
//                    ada.notifyDataSetChanged();
//                } else {
//                    Toast.showText(mContext, "无数据");
//                    setResult(-9998, null);
//                    onBackPressed();
//                }
//            }
        }else if (EventBusInfoCode.WaveHouse.equals(backBus) || EventBusInfoCode.WaveHouse2.equals(backBus) ) {
            model.setText("编号");
            name.setText("名称");
            if (BasicShareUtil.getInstance(mContext).getIsOL()) {
                SearchBean searchBean = new SearchBean();
                searchBean.val1 = searchString;searchBean.val2 = searchString4wavehouse;
                App.getRService().doIOAction("WaveHouseSearchLike", gson.toJson(searchBean), new MySubscribe<CommonResponse>() {
                    @Override
                    public void onNext(CommonResponse commonResponse) {
                        super.onNext(commonResponse);
                        DownloadReturnBean dBean = new Gson().fromJson(commonResponse.returnJson, DownloadReturnBean.class);
                        waveHouseList = dBean.wavehouse;
                        if (waveHouseList.size() > 0) {
                            SearchWaveHouseAdapter ada1 = new SearchWaveHouseAdapter(mContext, waveHouseList);
                            lvResult.setAdapter(ada1);
                            ada1.notifyDataSetChanged();
                        } else {
                            Toast.showText(mContext, "无数据");
                            setResult(-9998, null);
                            onBackPressed();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Toast.showText(mContext, e.getMessage());
                    }
                });
            }else{
                WaveHouseDao storageDao = daoSession.getWaveHouseDao();
                List<WaveHouse> storages = storageDao.queryBuilder().where(
                        WaveHouseDao.Properties.FSPGroupID.eq(searchString4wavehouse)
                ).whereOr(WaveHouseDao.Properties.FSPID.eq(searchString),WaveHouseDao.Properties.FNumber.eq(searchString)).build().list();
                waveHouseList = storages;
                if (waveHouseList.size() > 0) {
                    SearchWaveHouseAdapter ada1 = new SearchWaveHouseAdapter(mContext, waveHouseList);
                    lvResult.setAdapter(ada1);
                    ada1.notifyDataSetChanged();
                } else {
                    Toast.showText(mContext, "无数据");
                    setResult(-9998, null);
                    onBackPressed();
                }
            }
        }else if (EventBusInfoCode.Man.equals(backBus) || EventBusInfoCode.Man2.equals(backBus)|| EventBusInfoCode.Man3.equals(backBus) || EventBusInfoCode.Man4.equals(backBus) ) {
            model.setText("编号");
            name.setText("名称");
            if (BasicShareUtil.getInstance(mContext).getIsOL()) {
                App.getRService().doIOAction("ManSearchLike", searchString, new MySubscribe<CommonResponse>() {
                    @Override
                    public void onNext(CommonResponse commonResponse) {
                        super.onNext(commonResponse);
                        DownloadReturnBean dBean = new Gson().fromJson(commonResponse.returnJson, DownloadReturnBean.class);
                        employeeList = dBean.employee;
                        if (employeeList.size() > 0) {
                            SearchManAdapter ada1 = new SearchManAdapter(mContext, employeeList);
                            lvResult.setAdapter(ada1);
                            ada1.notifyDataSetChanged();
                        } else {
                            Toast.showText(mContext, "无数据");
                            setResult(-9998, null);
                            onBackPressed();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Toast.showText(mContext, e.getMessage());
                    }
                });
            }else{
                EmployeeDao suppliersDao = GreenDaoManager.getmInstance(mContext).getDaoSession().getEmployeeDao();
                List<Employee> list = suppliersDao.queryBuilder().whereOr(
                        EmployeeDao.Properties.FName.like("%" + searchString + "%"),
                        EmployeeDao.Properties.FNumber.like("%" + searchString + "%")
                ).orderAsc(StorageDao.Properties.FItemID).limit(50).build().list();
                employeeList.addAll(list);
                if (employeeList.size() > 0) {
                    SearchManAdapter ada1 = new SearchManAdapter(mContext, employeeList);
                    lvResult.setAdapter(ada1);
                    ada1.notifyDataSetChanged();
                } else {
                    Toast.showText(mContext, "未查询到数据");
                    setResult(-9998, null);
                    onBackPressed();
                }
            }
        }
//        //物料
//        if (where == Info.SEARCHPRODUCT) {
//            model.setText("编码");
//            name.setText("名称");
//            if (BasicShareUtil.getInstance(mContext).getIsOL()) {
//                Asynchttp.post(mContext, getBaseUrl() + WebApi.PRODUCTSEARCHLIKE, searchString, new Asynchttp.Response() {
//                    @Override
//                    public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
//                        DownloadReturnBean dBean = new Gson().fromJson(cBean.returnJson, DownloadReturnBean.class);
//                        items = dBean.products;
//                        Log.e("getProduct:",items.toString());
//                        itemAll = new ArrayList<>();
//                        itemAll.addAll(items);
//                        if (itemAll.size() > 0) {
//                            ada = new SearchAdapter(mContext, itemAll);
//                            lvResult.setAdapter(ada);
//                            ada.notifyDataSetChanged();
//                        } else {
//                            Toast.showText(mContext, "无数据");
//                            setResult(-9998, null);
//                            onBackPressed();
//                        }
//                    }
//
//                    @Override
//                    public void onFailed(String Msg, AsyncHttpClient client) {
//                        Toast.showText(mContext, Msg);
//                    }
//                });
//            } else {
//                model.setText("编码");
//                name.setText("名称");
//                ProductDao productDao = GreenDaoManager.getmInstance(mContext).getDaoSession().getProductDao();
//                items = productDao.queryBuilder().whereOr(
//                        ProductDao.Properties.FNumber.like("%" + searchString + "%"),
//                        ProductDao.Properties.FBarcode.like("%" + searchString + "%"),
//                        ProductDao.Properties.FName.like("%" + searchString + "%")).
//                        orderAsc(ProductDao.Properties.FNumber).limit(50).orderAsc(ProductDao.Properties.FNumber).build().list();
//                itemAll = new ArrayList<>();
//                itemAll.addAll(items);
//                if (itemAll.size() > 0) {
//                    ada = new SearchAdapter(mContext, itemAll);
//                    lvResult.setAdapter(ada);
//                    ada.notifyDataSetChanged();
//                } else {
//                    Toast.showText(mContext, "无数据");
//                    setResult(-9998, null);
//                    onBackPressed();
//                }
//            }
//
//            //供应商
//        }
//        else if (where == Info.SEARCHSUPPLIER) {
//            model.setText("编号");
//            name.setText("名称");
//            if (BasicShareUtil.getInstance(mContext).getIsOL()) {
//                Asynchttp.post(mContext, getBaseUrl() + WebApi.SUPPLIERSEARCHLIKE, searchString, new Asynchttp.Response() {
//                    @Override
//                    public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
//                        DownloadReturnBean dBean = new Gson().fromJson(cBean.returnJson, DownloadReturnBean.class);
//                        suppliersList = dBean.suppliers;
//                        itemAllSupplier = new ArrayList<>();
//                        itemAllSupplier.addAll(suppliersList);
//                        if (itemAllSupplier.size() > 0) {
//                            SearchSupplierAdapter ada1 = new SearchSupplierAdapter(mContext, itemAllSupplier);
//                            lvResult.setAdapter(ada1);
//                            ada1.notifyDataSetChanged();
//                        } else {
//                            Toast.showText(mContext, "无数据");
//                            setResult(-9998, null);
//                            onBackPressed();
//                        }
//                    }
//
//                    @Override
//                    public void onFailed(String Msg, AsyncHttpClient client) {
//                        Toast.showText(mContext, Msg);
//                    }
//                });
//            }else{
//                SuppliersDao suppliersDao = GreenDaoManager.getmInstance(mContext).getDaoSession().getSuppliersDao();
//                List<Suppliers> list = suppliersDao.queryBuilder().whereOr(
//                        SuppliersDao.Properties.FName.like("%" + searchString + "%"),
//                        SuppliersDao.Properties.FItemID.like("%" + searchString + "%")
//                ).orderAsc(SuppliersDao.Properties.FItemID).limit(50).build().list();
//                itemAllSupplier = new ArrayList<>();
//                itemAllSupplier.addAll(list);
//                if (itemAllSupplier.size() > 0) {
//                    SearchSupplierAdapter ada1 = new SearchSupplierAdapter(mContext, itemAllSupplier);
//                    lvResult.setAdapter(ada1);
//                    ada1.notifyDataSetChanged();
//                } else {
//                    Toast.showText(mContext, "未查询到数据");
//                    setResult(-9998, null);
//                    onBackPressed();
//                }
//
//            }
//            //客户
//        } else if (where == Info.SEARCHCLIENT) {
//            model.setText("编号");
//            name.setText("名称");
//            if (BasicShareUtil.getInstance(mContext).getIsOL()) {
//                Asynchttp.post(mContext, getBaseUrl() + WebApi.CLIENTSEARCHLIKE, searchString, new Asynchttp.Response() {
//                    @Override
//                    public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
//                        DownloadReturnBean dBean = new Gson().fromJson(cBean.returnJson, DownloadReturnBean.class);
//                        itemClient = dBean.clients;
//                        itemAllClient = new ArrayList<>();
//                        itemAllClient.addAll(itemClient);
//                        if (itemAllClient.size() > 0) {
//                            SearchClientAdapter ada2 = new SearchClientAdapter(mContext, itemAllClient);
//                            lvResult.setAdapter(ada2);
//                            ada2.notifyDataSetChanged();
//                        } else {
//                            Toast.showText(mContext, "无数据");
//                            setResult(-9998, null);
//                            onBackPressed();
//                        }
//                    }
//
//                    @Override
//                    public void onFailed(String Msg, AsyncHttpClient client) {
//                        Toast.showText(mContext, Msg);
//                    }
//                });
//            }else{
//                ClientDao clientDao = GreenDaoManager.getmInstance(mContext).getDaoSession().getClientDao();
//                List<Client> clients = clientDao.queryBuilder().whereOr(ClientDao.Properties.FName.like("%" + searchString + "%"), ClientDao.Properties.FItemID.like("%" + searchString + "%")).orderAsc(ClientDao.Properties.FItemID).build().list();
//                itemAllClient = new ArrayList<>();
//                itemAllClient.addAll(clients);
//                if (itemAllClient.size() > 0) {
//                    SearchClientAdapter ada2 = new SearchClientAdapter(mContext, itemAllClient);
//                    lvResult.setAdapter(ada2);
//                    ada2.notifyDataSetChanged();
//                } else {
//                    Toast.showText(mContext, "未查询到数据");
//                    setResult(-9998, null);
//                    onBackPressed();
//                }
//            }
//            //交货单位
//        } else if (where == Info.SEARCHJH) {
//            model.setText("编号");
//            name.setText("名称");
//            if (BasicShareUtil.getInstance(mContext).getIsOL()) {
//                Asynchttp.post(mContext, getBaseUrl() + WebApi.SEARCHJHSEARCHLIKE, searchString, new Asynchttp.Response() {
//                    @Override
//                    public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
//                        DownloadReturnBean dBean = new Gson().fromJson(cBean.returnJson, DownloadReturnBean.class);
//                        goodsDepartmentList = dBean.getGoodsDepartments;
//                        goodsDepartmentAllList = new ArrayList<>();
//                        goodsDepartmentAllList.addAll(goodsDepartmentList);
//                        if (goodsDepartmentAllList.size() > 0) {
//                            SearchDepartmentAdapter ada1 = new SearchDepartmentAdapter(mContext, goodsDepartmentAllList);
//                            lvResult.setAdapter(ada1);
//                            ada1.notifyDataSetChanged();
//                        } else {
//                            Toast.showText(mContext, "无数据");
//                            setResult(-9998, null);
//                            onBackPressed();
//                        }
//                    }
//
//                    @Override
//                    public void onFailed(String Msg, AsyncHttpClient client) {
//                        Toast.showText(mContext, Msg);
//                    }
//                });
//            }else{
//                GetGoodsDepartmentDao getGoodsDepartmentDao = GreenDaoManager.getmInstance(mContext).getDaoSession().getGetGoodsDepartmentDao();
//                List<GetGoodsDepartment> list = getGoodsDepartmentDao.queryBuilder().whereOr(
//                        GetGoodsDepartmentDao.Properties.FNumber.like("%" + searchString + "%"),
//                        GetGoodsDepartmentDao.Properties.FName.like("%" + searchString + "%")
//                ).build().list();
//                goodsDepartmentList = new ArrayList<>();
//                goodsDepartmentList.addAll(list);
//                if (goodsDepartmentList.size() > 0) {
//                    SearchDepartmentAdapter ada3 = new SearchDepartmentAdapter(mContext, goodsDepartmentList);
//                    lvResult.setAdapter(ada3);
//                    ada3.notifyDataSetChanged();
//                } else {
//                    Toast.showText(mContext, "未查询到数据");
//                    setResult(-9998, null);
//                    onBackPressed();
//                }
//            }
//
//        }else if (where == Info.Search_DbType) {
//            model.setText("编号");
//            name.setText("名称");
//            if (BasicShareUtil.getInstance(mContext).getIsOL()) {
//                Asynchttp.post(mContext, getBaseUrl() + WebApi.SearchDbType, searchString, new Asynchttp.Response() {
//                    @Override
//                    public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
//                        DownloadReturnBean dBean = new Gson().fromJson(cBean.returnJson, DownloadReturnBean.class);
//                        dbTypeList = dBean.dbTypes;
////                        itemAllSupplier = new ArrayList<>();
////                        itemAllSupplier.addAll(suppliersList);
//                        if (dbTypeList.size() > 0) {
//                            SearchDbTypeAdapter ada1 = new SearchDbTypeAdapter(mContext, dbTypeList);
//                            lvResult.setAdapter(ada1);
//                            ada1.notifyDataSetChanged();
//                        } else {
//                            Toast.showText(mContext, "无数据");
//                            setResult(-9998, null);
//                            onBackPressed();
//                        }
//                    }
//
//                    @Override
//                    public void onFailed(String Msg, AsyncHttpClient client) {
//                        Toast.showText(mContext, Msg);
//                    }
//                });
//            }else{
//                SuppliersDao suppliersDao = GreenDaoManager.getmInstance(mContext).getDaoSession().getSuppliersDao();
//                List<Suppliers> list = suppliersDao.queryBuilder().whereOr(
//                        SuppliersDao.Properties.FName.like("%" + searchString + "%"),
//                        SuppliersDao.Properties.FItemID.like("%" + searchString + "%")
//                ).orderAsc(SuppliersDao.Properties.FItemID).limit(50).build().list();
//                itemAllSupplier = new ArrayList<>();
//                itemAllSupplier.addAll(list);
//                if (itemAllSupplier.size() > 0) {
//                    SearchSupplierAdapter ada1 = new SearchSupplierAdapter(mContext, itemAllSupplier);
//                    lvResult.setAdapter(ada1);
//                    ada1.notifyDataSetChanged();
//                } else {
//                    Toast.showText(mContext, "未查询到数据");
//                    setResult(-9998, null);
//                    onBackPressed();
//                }
//
//            }
//            //客户
//        }else if (where == Info.Search_Storage) {
//            model.setText("编号");
//            name.setText("名称");
//            if (BasicShareUtil.getInstance(mContext).getIsOL()) {
//                Asynchttp.post(mContext, getBaseUrl() + WebApi.SearchStorage, searchString, new Asynchttp.Response() {
//                    @Override
//                    public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
//                        DownloadReturnBean dBean = new Gson().fromJson(cBean.returnJson, DownloadReturnBean.class);
//                        storageList = dBean.storage;
//                        if (storageList.size() > 0) {
//                            SearchStorageAdapter ada1 = new SearchStorageAdapter(mContext, storageList);
//                            lvResult.setAdapter(ada1);
//                            ada1.notifyDataSetChanged();
//                        } else {
//                            Toast.showText(mContext, "无数据");
//                            setResult(-9998, null);
//                            onBackPressed();
//                        }
//                    }
//
//                    @Override
//                    public void onFailed(String Msg, AsyncHttpClient client) {
//                        Toast.showText(mContext, Msg);
//                    }
//                });
//            }else{
//                StorageDao suppliersDao = GreenDaoManager.getmInstance(mContext).getDaoSession().getStorageDao();
//                List<Storage> list = suppliersDao.queryBuilder().whereOr(
//                        StorageDao.Properties.FName.like("%" + searchString + "%"),
//                        StorageDao.Properties.FItemID.like("%" + searchString + "%")
//                ).orderAsc(StorageDao.Properties.FItemID).limit(50).build().list();
//                storageList = new ArrayList<>();
//                storageList.addAll(list);
//                if (storageList.size() > 0) {
//                    SearchStorageAdapter ada1 = new SearchStorageAdapter(mContext, storageList);
//                    lvResult.setAdapter(ada1);
//                    ada1.notifyDataSetChanged();
//                } else {
//                    Toast.showText(mContext, "未查询到数据");
//                    setResult(-9998, null);
//                    onBackPressed();
//                }
//
//            }
//        }


    }

    @Override
    public void initListener() {
        lvResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent mIntent = new Intent();
//                Bundle b = new Bundle();
                if (EventBusInfoCode.Storage.equals(backBus)) {
                    EventBusUtil.sendEvent(new ClassEvent(backBus,storageList.get(i)));
                    onBackPressed();
                }else if (EventBusInfoCode.WaveHouse.equals(backBus)) {
                    EventBusUtil.sendEvent(new ClassEvent(backBus,waveHouseList.get(i)));
                    onBackPressed();
                }else if (EventBusInfoCode.Man.equals(backBus) || EventBusInfoCode.Man2.equals(backBus)|| EventBusInfoCode.Man3.equals(backBus) || EventBusInfoCode.Man4.equals(backBus) ) {
                    EventBusUtil.sendEvent(new ClassEvent(backBus,employeeList.get(i)));
                    onBackPressed();
                }
//                if (where == Info.SEARCHPRODUCT) {
//                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.PRODUCTRETURN,itemAll.get(i)));
//                    setResult(Info.SEARCHFORRESULT, mIntent);
//                    onBackPressed();
//                } else if (where == Info.SEARCHSUPPLIER) {
//                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Search_Supplier,itemAllSupplier.get(i)));
//                    b.putString("001", itemAllSupplier.get(i).FItemID);
//                    b.putString("002", itemAllSupplier.get(i).FName);
//                    mIntent.putExtras(b);
//                    setResult(Info.SEARCHFORRESULTPRODUCT, mIntent);
//                    onBackPressed();
//                } else if (where == Info.SEARCHCLIENT) {
//                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Search_client,itemAllClient.get(i)));
//                    b.putString("001", itemAllClient.get(i).FItemID);
//                    b.putString("002", itemAllClient.get(i).FName);
//                    mIntent.putExtras(b);
//                    setResult(Info.SEARCHFORRESULTCLIRNT, mIntent);
//                    onBackPressed();
//                } else if (where == Info.SEARCHJH) {
//                    b.putString("001", goodsDepartmentList.get(i).FItemID);
//                    b.putString("002", goodsDepartmentList.get(i).FName);
//                    mIntent.putExtras(b);
//                    setResult(Info.SEARCHFORRESULTJH, mIntent);
//                    onBackPressed();
//                } else if (where == Info.Search_DbType) {
//                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Search_DbType,dbTypeList.get(i)));
//                    onBackPressed();
//                } else if (where == Info.Search_Storage) {
//                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Search_Storage,storageList.get(i)));
//                    onBackPressed();
//                }

            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void OnReceive(String code) {

    }
    public static void start(Context context, String search, String backBus) {
        Intent starter = new Intent(context, SearchDataActivity.class);
        starter.putExtra("search", search);
        starter.putExtra("backBus", backBus);
//        starter.putStringArrayListExtra("fid", fid);
        context.startActivity(starter);
    }
    public static void start(Context context, String search,String search_storage, String backBus) {
        Intent starter = new Intent(context, SearchDataActivity.class);
        starter.putExtra("search", search);
        starter.putExtra("search_storage", search_storage);
        starter.putExtra("backBus", backBus);
//        starter.putStringArrayListExtra("fid", fid);
        context.startActivity(starter);
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        finish();
        this.overridePendingTransition(R.anim.bottom_end, 0);
    }

    @Override
    public void onBackPressed() {
        finish();
        this.overridePendingTransition(0, R.anim.bottom_end);
    }



}
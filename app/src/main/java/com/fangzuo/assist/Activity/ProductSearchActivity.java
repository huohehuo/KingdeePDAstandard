package com.fangzuo.assist.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Adapter.SearchAdapter;
import com.fangzuo.assist.Adapter.SearchClientAdapter;
import com.fangzuo.assist.Adapter.SearchDepartmentAdapter;
import com.fangzuo.assist.Adapter.SearchSupplierAdapter;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.Dao.Client;
import com.fangzuo.assist.Dao.GetGoodsDepartment;
import com.fangzuo.assist.Dao.Product;
import com.fangzuo.assist.Dao.Suppliers;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.Asynchttp;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.EventBusUtil;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.Info;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.greendao.gen.ClientDao;
import com.fangzuo.greendao.gen.GetGoodsDepartmentDao;
import com.fangzuo.greendao.gen.ProductDao;
import com.fangzuo.greendao.gen.SuppliersDao;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductSearchActivity extends BaseActivity {


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
    @BindView(R.id.pg)
    ProgressBar pg;
    private String searchString;
    private ProductSearchActivity mContext;
    private SearchAdapter ada;
    private List<Product> items1;
    private List<Product> items;
    private List<Product> itemAll;
    private int where;
    private List<Suppliers> itemAllSupplier;
    private List<Suppliers> suppliersList;
    private List<Client> itemAllClient;
    private List<Client> itemClient;
    private List<GetGoodsDepartment> goodsDepartmentList;
    private List<GetGoodsDepartment> goodsDepartmentAllList;

    @Override
    public void initView() {
        setContentView(R.layout.activity_product_search);
        ButterKnife.bind(this);
        mContext = this;
        Intent in = getIntent();
        Bundle b = in.getExtras();
        searchString = b.getString("search", "");
        where = b.getInt("where");
        Log.e("searchString", searchString);
        if (where == Info.SEARCHPRODUCT) title.setText("查询结果(物料)");
        if (where == Info.SEARCHSUPPLIER) title.setText("查询结果(供应商)");
        if (where == Info.SEARCHCLIENT) title.setText("查询结果(客户)");
        if (where == Info.SEARCHJH) title.setText("查询结果(交货单位)");
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
        //物料
        if (where == Info.SEARCHPRODUCT) {
            model.setText("名称");
            name.setText("型号");
            if (BasicShareUtil.getInstance(mContext).getIsOL()) {
                Asynchttp.post(mContext, getBaseUrl() + WebApi.PRODUCTSEARCHLIKE, searchString, new Asynchttp.Response() {
                    @Override
                    public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                        pg.setVisibility(View.GONE);
                        DownloadReturnBean dBean = new Gson().fromJson(cBean.returnJson, DownloadReturnBean.class);
                        items = dBean.products;
                        Log.e("getProduct:", items.toString());
                        itemAll = new ArrayList<>();
                        itemAll.addAll(items);
                        if (itemAll.size() > 0) {
                            ada = new SearchAdapter(mContext, itemAll);
                            lvResult.setAdapter(ada);
                            ada.notifyDataSetChanged();
                        } else {
                            Toast.showText(mContext, "无数据");
                            setResult(-9998, null);
                            onBackPressed();
                        }
                    }

                    @Override
                    public void onFailed(String Msg, AsyncHttpClient client) {
                        Toast.showText(mContext, Msg);
                    }
                });
            } else {
                model.setText("名称");
                name.setText("型号");
                ProductDao productDao = GreenDaoManager.getmInstance(mContext).getDaoSession().getProductDao();
                items = productDao.queryBuilder().whereOr(
                        ProductDao.Properties.FNumber.like("%" + searchString + "%"),
                        ProductDao.Properties.FBarcode.like("%" + searchString + "%"),
                        ProductDao.Properties.FName.like("%" + searchString + "%")).
                        orderAsc(ProductDao.Properties.FNumber).limit(50).orderAsc(ProductDao.Properties.FNumber).build().list();
                itemAll = new ArrayList<>();
                itemAll.addAll(items);
                pg.setVisibility(View.GONE);
                if (itemAll.size() > 0) {
                    ada = new SearchAdapter(mContext, itemAll);
                    lvResult.setAdapter(ada);
                    ada.notifyDataSetChanged();
                } else {
                    Toast.showText(mContext, "无数据");
                    setResult(-9998, null);
                    onBackPressed();
                }
            }

            //供应商
        } else if (where == Info.SEARCHSUPPLIER) {
            model.setText("编号");
            name.setText("名称");
            if (BasicShareUtil.getInstance(mContext).getIsOL()) {
                Asynchttp.post(mContext, getBaseUrl() + WebApi.SUPPLIERSEARCHLIKE, searchString, new Asynchttp.Response() {
                    @Override
                    public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                        pg.setVisibility(View.GONE);
                        DownloadReturnBean dBean = new Gson().fromJson(cBean.returnJson, DownloadReturnBean.class);
                        suppliersList = dBean.suppliers;
                        itemAllSupplier = new ArrayList<>();
                        itemAllSupplier.addAll(suppliersList);
                        if (itemAllSupplier.size() > 0) {
                            SearchSupplierAdapter ada1 = new SearchSupplierAdapter(mContext, itemAllSupplier);
                            lvResult.setAdapter(ada1);
                            ada1.notifyDataSetChanged();
                        } else {
                            Toast.showText(mContext, "无数据");
                            setResult(-9998, null);
                            onBackPressed();
                        }
                    }

                    @Override
                    public void onFailed(String Msg, AsyncHttpClient client) {
                        Toast.showText(mContext, Msg);
                    }
                });
            } else {
                SuppliersDao suppliersDao = GreenDaoManager.getmInstance(mContext).getDaoSession().getSuppliersDao();
                List<Suppliers> list = suppliersDao.queryBuilder().whereOr(
                        SuppliersDao.Properties.FName.like("%" + searchString + "%"),
                        SuppliersDao.Properties.FItemID.like("%" + searchString + "%")
                ).orderAsc(SuppliersDao.Properties.FItemID).limit(50).build().list();
                itemAllSupplier = new ArrayList<>();
                itemAllSupplier.addAll(list);
                pg.setVisibility(View.GONE);
                if (itemAllSupplier.size() > 0) {
                    SearchSupplierAdapter ada1 = new SearchSupplierAdapter(mContext, itemAllSupplier);
                    lvResult.setAdapter(ada1);
                    ada1.notifyDataSetChanged();
                } else {
                    Toast.showText(mContext, "未查询到数据");
                    setResult(-9998, null);
                    onBackPressed();
                }

            }
            //客户
        } else if (where == Info.SEARCHCLIENT) {
            model.setText("编号");
            name.setText("名称");
            if (BasicShareUtil.getInstance(mContext).getIsOL()) {
                Asynchttp.post(mContext, getBaseUrl() + WebApi.CLIENTSEARCHLIKE, searchString, new Asynchttp.Response() {
                    @Override
                    public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                        pg.setVisibility(View.GONE);
                        DownloadReturnBean dBean = new Gson().fromJson(cBean.returnJson, DownloadReturnBean.class);
                        itemClient = dBean.clients;
                        itemAllClient = new ArrayList<>();
                        itemAllClient.addAll(itemClient);
                        if (itemAllClient.size() > 0) {
                            SearchClientAdapter ada2 = new SearchClientAdapter(mContext, itemAllClient);
                            lvResult.setAdapter(ada2);
                            ada2.notifyDataSetChanged();
                        } else {
                            Toast.showText(mContext, "无数据");
                            setResult(-9998, null);
                            onBackPressed();
                        }
                    }

                    @Override
                    public void onFailed(String Msg, AsyncHttpClient client) {
                        Toast.showText(mContext, Msg);
                    }
                });
            } else {
                ClientDao clientDao = GreenDaoManager.getmInstance(mContext).getDaoSession().getClientDao();
                List<Client> clients = clientDao.queryBuilder().whereOr(ClientDao.Properties.FName.like("%" + searchString + "%"), ClientDao.Properties.FItemID.like("%" + searchString + "%")).orderAsc(ClientDao.Properties.FItemID).build().list();
                itemAllClient = new ArrayList<>();
                itemAllClient.addAll(clients);
                pg.setVisibility(View.GONE);
                if (itemAllClient.size() > 0) {
                    SearchClientAdapter ada2 = new SearchClientAdapter(mContext, itemAllClient);
                    lvResult.setAdapter(ada2);
                    ada2.notifyDataSetChanged();
                } else {
                    Toast.showText(mContext, "未查询到数据");
                    setResult(-9998, null);
                    onBackPressed();
                }
            }
            //交货单位
        } else if (where == Info.SEARCHJH) {
            model.setText("编号");
            name.setText("名称");
            if (BasicShareUtil.getInstance(mContext).getIsOL()) {
                Asynchttp.post(mContext, getBaseUrl() + WebApi.SEARCHJHSEARCHLIKE, searchString, new Asynchttp.Response() {
                    @Override
                    public void onSucceed(CommonResponse cBean, AsyncHttpClient client) {
                        pg.setVisibility(View.GONE);
                        DownloadReturnBean dBean = new Gson().fromJson(cBean.returnJson, DownloadReturnBean.class);
                        goodsDepartmentList = dBean.getGoodsDepartments;
                        goodsDepartmentAllList = new ArrayList<>();
                        goodsDepartmentAllList.addAll(goodsDepartmentList);
                        if (goodsDepartmentAllList.size() > 0) {
                            SearchDepartmentAdapter ada1 = new SearchDepartmentAdapter(mContext, goodsDepartmentAllList);
                            lvResult.setAdapter(ada1);
                            ada1.notifyDataSetChanged();
                        } else {
                            Toast.showText(mContext, "无数据");
                            setResult(-9998, null);
                            onBackPressed();
                        }
                    }

                    @Override
                    public void onFailed(String Msg, AsyncHttpClient client) {
                        Toast.showText(mContext, Msg);
                    }
                });
            } else {
                GetGoodsDepartmentDao getGoodsDepartmentDao = GreenDaoManager.getmInstance(mContext).getDaoSession().getGetGoodsDepartmentDao();
                List<GetGoodsDepartment> list = getGoodsDepartmentDao.queryBuilder().whereOr(
                        GetGoodsDepartmentDao.Properties.FNumber.like("%" + searchString + "%"),
                        GetGoodsDepartmentDao.Properties.FName.like("%" + searchString + "%")
                ).build().list();
                goodsDepartmentList = new ArrayList<>();
                goodsDepartmentList.addAll(list);
                pg.setVisibility(View.GONE);
                if (goodsDepartmentList.size() > 0) {
                    SearchDepartmentAdapter ada3 = new SearchDepartmentAdapter(mContext, goodsDepartmentList);
                    lvResult.setAdapter(ada3);
                    ada3.notifyDataSetChanged();
                } else {
                    Toast.showText(mContext, "未查询到数据");
                    setResult(-9998, null);
                    onBackPressed();
                }
            }

        }


    }

    @Override
    public void initListener() {
        lvResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent mIntent = new Intent();
                Bundle b = new Bundle();
                if (where == Info.SEARCHPRODUCT) {
                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.PRODUCTRETURN, itemAll.get(i)));
                    setResult(Info.SEARCHFORRESULT, mIntent);
                    onBackPressed();
                } else if (where == Info.SEARCHSUPPLIER) {
                    b.putString("001", itemAllSupplier.get(i).FItemID);
                    b.putString("002", itemAllSupplier.get(i).FName);
                    mIntent.putExtras(b);
                    setResult(Info.SEARCHFORRESULTPRODUCT, mIntent);
                    onBackPressed();
                } else if (where == Info.SEARCHCLIENT) {
                    b.putString("001", itemAllClient.get(i).FItemID);
                    b.putString("002", itemAllClient.get(i).FName);
                    mIntent.putExtras(b);
                    setResult(Info.SEARCHFORRESULTCLIRNT, mIntent);
                    onBackPressed();
                } else if (where == Info.SEARCHJH) {
                    b.putString("001", goodsDepartmentList.get(i).FItemID);
                    b.putString("002", goodsDepartmentList.get(i).FName);
                    mIntent.putExtras(b);
                    setResult(Info.SEARCHFORRESULTJH, mIntent);
                    onBackPressed();
                }

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }


}

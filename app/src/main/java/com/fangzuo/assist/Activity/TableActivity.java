package com.fangzuo.assist.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Adapter.TableAdapter;
import com.fangzuo.assist.Adapter.TableAdapter1;
import com.fangzuo.assist.Dao.InStorageNum;
import com.fangzuo.assist.Dao.T_Detail;
import com.fangzuo.assist.Dao.T_main;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.MathUtil;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.InStorageNumDao;
import com.fangzuo.greendao.gen.PDSubDao;
import com.fangzuo.greendao.gen.T_DetailDao;
import com.fangzuo.greendao.gen.T_mainDao;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TableActivity extends BaseActivity implements TableAdapter.InnerClickListener {
    @BindView(R.id.btn_back)
    RelativeLayout btnBack;
    @BindView(R.id.lv_result)
    ListView lvResult;
    @BindView(R.id.btn_delete)
    RelativeLayout btnDelete;
    @BindView(R.id.productcategory)
    TextView productcategory;
    @BindView(R.id.productnum)
    TextView productnum;
    @BindView(R.id.delete_all)
    TextView deleteAll;
    private DaoSession daoSession;
    private List<T_Detail> list;
    private List<T_main> list1;
    private TableAdapter tableAdapter;
    private T_DetailDao t_detailDao;
    private T_mainDao t_mainDao;
    private List<Boolean> isCheck;
    private int activity;
    private TableAdapter1 tableAdapter1;
    private PDSubDao pdSubDao;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_table);
        mContext = this;
        ButterKnife.bind(this);
        daoSession = GreenDaoManager.getmInstance(mContext).getDaoSession();
        t_mainDao = daoSession.getT_mainDao();
        t_detailDao = daoSession.getT_DetailDao();

    }

    @Override
    protected void initData() {
        Intent in = getIntent();
        Bundle extras = in.getExtras();
        activity = extras.getInt("activity");


        initList();
    }

    private void initList() {
        double num = 0;
        isCheck = new ArrayList<>();
        list = new ArrayList<>();
        list1 = new ArrayList<>();
        list1.clear();
        list.clear();
        list = t_detailDao.queryBuilder().where(T_DetailDao.Properties.Activity.eq(activity)).build().list();
        list1 = t_mainDao.queryBuilder().where(T_mainDao.Properties.Activity.eq(activity)).build().list();
        if (list.size() > 0 && list1.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                isCheck.add(false);
            }

        }
        Lg.e("表头："+list1.size(),list1);
        Lg.e("明细："+list.size(),list);
        tableAdapter = new TableAdapter(mContext,list, isCheck);
        lvResult.setAdapter(tableAdapter);
        tableAdapter.setInnerListener(this);
        tableAdapter.notifyDataSetChanged();
        List<String> products = new ArrayList<>();
        products.clear();
        if (list.size() > 0) {
            if (products.size() == 0) {
                products.add(list.get(0).FProductId);
            }
            for (int i = 0; i < list.size(); i++) {
                    if (!products.contains(list.get(i).FProductId)) {
                        products.add(list.get(i).FProductId);
                    }
                }


            for (int i = 0; i < list.size(); i++) {
                num += MathUtil.toD(list.get(i).FQuantity);
            }
            productcategory.setText("物料类别数:" + products.size() + "个");
            productnum.setText("物料总数为:" + num + "");


        } else {
            productcategory.setText("物料类别数:" + 0 + "个");
            productnum.setText("物料总数为:" + 0 + "");
        }
        if(list1.size()>0){
            for(int i = 0;i<list1.size();i++){
                List<T_Detail> details = t_detailDao.queryBuilder().where(T_DetailDao.Properties.FOrderId.eq(list1.get(i).orderId)).build().list();
                if(details.size()==0||details==null){
                    t_mainDao.deleteInTx(list1.get(i));
                }
            }
        }
    }

    @Override
    protected void initListener() {
        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                ab.setTitle("确认删除");
                ab.setMessage("确认删除所有么？");
                ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        T_DetailDao t_detailDao = daoSession.getT_DetailDao();
                        T_mainDao t_mainDao = daoSession.getT_mainDao();
                        for(int j = 0;j<list.size();j++){
                            T_Detail t_detail = t_detailDao.queryBuilder().where(T_DetailDao.Properties.FIndex.eq(list.get(j).FIndex)).build().unique();
                            if (activity != Config.PurchaseOrderActivity && activity != Config.SaleOrderActivity) {
                                InStorageNumDao inStorageNumDao = daoSession.getInStorageNumDao();
                                List<InStorageNum> innum = inStorageNumDao.queryBuilder().where(InStorageNumDao.Properties.FBatchNo.eq(t_detail.FBatch), InStorageNumDao.Properties.FStockID.eq(t_detail.FStorageId)
                                        , InStorageNumDao.Properties.FStockPlaceID.eq(t_detail.FPositionId), InStorageNumDao.Properties.FItemID.eq(t_detail.FProductId)).build().list();
                                if (innum.size() > 0) {
                                    if (activity == Config.PurchaseInStorageActivity || activity == Config.ProductInStorageActivity) {
                                        innum.get(0).FQty = (Double.parseDouble(innum.get(0).FQty) - ((Double.parseDouble(t_detail.FQuantity) * t_detail.unitrate))) + "";
                                        inStorageNumDao.update(innum.get(0));
                                    } else if (activity == Config.DBActivity) {
                                        innum.get(0).FQty = (Double.parseDouble(innum.get(0).FQty) - ((Double.parseDouble(t_detail.FQuantity) * t_detail.unitrate))) + "";
                                        inStorageNumDao.update(innum.get(0));
                                        InStorageNumDao inStorageNumDao1 = daoSession.getInStorageNumDao();
                                        List<InStorageNum> outnum = inStorageNumDao1.queryBuilder().where(InStorageNumDao.Properties.FBatchNo.eq(t_detail.FBatch), InStorageNumDao.Properties.FStockID.eq(t_detail.FoutStorageid)
                                                , InStorageNumDao.Properties.FStockPlaceID.eq(t_detail.Foutwavehouseid), InStorageNumDao.Properties.FItemID.eq(t_detail.FProductId)).build().list();
                                        if (outnum.size() > 0) {
                                            outnum.get(0).FQty = (Double.parseDouble(outnum.get(0).FQty) + (Double.parseDouble(t_detail.FQuantity) * t_detail.unitrate)) + "";
                                            inStorageNumDao1.update(outnum.get(0));
                                        }

                                    } else {
                                        innum.get(0).FQty = (Double.parseDouble(innum.get(0).FQty) + (Double.parseDouble(t_detail.FQuantity) * t_detail.unitrate)) + "";
                                        inStorageNumDao.update(innum.get(0));
                                    }
                                }


                            }
                            t_detailDao.delete(t_detail);
                            Toast.showText(mContext, "删除成功");
                        }




                        initList();
                    }
                });
                ab.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                ab.create().show();

            }
        });
        lvResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (isCheck.get(i)) {
                    isCheck.set(i, false);
                } else {
                    isCheck.set(i, true);
                }

                Log.e("ischeck", isCheck.get(i) + "");
                tableAdapter.notifyDataSetChanged();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pdSubDao = daoSession.getPDSubDao();
                if (list.size() > 0) {
                    AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                    ab.setTitle("确认删除");
                    ab.setMessage("选中数据会被删除，确定？");
                    ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            for (int j = 0; j < isCheck.size(); j++) {
                                if (isCheck.get(j)) {
                                    Log.e(i + "", isCheck.get(j) + "");
                                    T_Detail t_detail = t_detailDao.queryBuilder().where(T_DetailDao.Properties.FIndex.eq(list.get(j).FIndex)).build().unique();
                                    if (activity != Config.PurchaseOrderActivity && activity != Config.SaleOrderActivity) {
                                        InStorageNumDao inStorageNumDao = daoSession.getInStorageNumDao();
                                        List<InStorageNum> innum = inStorageNumDao.queryBuilder().where(InStorageNumDao.Properties.FBatchNo.eq(t_detail.FBatch), InStorageNumDao.Properties.FStockID.eq(t_detail.FStorageId)
                                                , InStorageNumDao.Properties.FStockPlaceID.eq(t_detail.FPositionId), InStorageNumDao.Properties.FItemID.eq(t_detail.FProductId)).build().list();
                                        if (innum.size() > 0) {
                                            if (activity == Config.OtherInStoreActivity ||activity == Config.PurchaseInStorageActivity || activity == Config.ProductInStorageActivity) {
                                                innum.get(0).FQty = (Double.parseDouble(innum.get(0).FQty) - (Double.parseDouble(t_detail.FQuantity) * t_detail.unitrate)) + "";
                                                inStorageNumDao.update(innum.get(0));
                                            } else if (activity == Config.DBActivity) {
                                                innum.get(0).FQty = (Double.parseDouble(innum.get(0).FQty) - (Double.parseDouble(t_detail.FQuantity) * t_detail.unitrate)) + "";
                                                inStorageNumDao.update(innum.get(0));
                                                InStorageNumDao inStorageNumDao1 = daoSession.getInStorageNumDao();
                                                List<InStorageNum> outnum = inStorageNumDao1.queryBuilder().where(InStorageNumDao.Properties.FBatchNo.eq(t_detail.FBatch), InStorageNumDao.Properties.FStockID.eq(t_detail.FoutStorageid)
                                                        , InStorageNumDao.Properties.FStockPlaceID.eq(t_detail.Foutwavehouseid), InStorageNumDao.Properties.FItemID.eq(t_detail.FProductId)).build().list();
                                                if (outnum.size() > 0) {
                                                    outnum.get(0).FQty = (Double.parseDouble(outnum.get(0).FQty) + (Double.parseDouble(t_detail.FQuantity) * t_detail.unitrate)) + "";
                                                    inStorageNumDao1.update(outnum.get(0));
                                                }
                                            } else {
                                                innum.get(0).FQty = (Double.parseDouble(innum.get(0).FQty) + (Double.parseDouble(t_detail.FQuantity) * t_detail.unitrate)) + "";
                                                inStorageNumDao.update(innum.get(0));
                                            }
                                        }


                                    }
                                    t_detailDao.delete(t_detail);
                                    Toast.showText(mContext, "删除成功");
                                }
                            }
                            initList();
                            tableAdapter.notifyDataSetChanged();
                        }
                    });
                    ab.setNegativeButton("取消", null);
                    ab.create().show();
                } else {
                    Toast.showText(mContext, "没有数据");
                }

            }
        });
    }

    @Override
    protected void OnReceive(String code) {

    }


    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        onBackPressed();
    }

    @Override
    public void InOnClick(View v) {
        final int position = (int) v.getTag();
        switch (v.getId()) {
            case R.id.delete:
                AlertDialog.Builder ab1 = new AlertDialog.Builder(mContext);
                ab1.setTitle("确认");
                ab1.setMessage("确认删除么?");
                ab1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        T_Detail t_detail = t_detailDao.queryBuilder().where(T_DetailDao.Properties.FIndex.eq(list.get(position).FIndex)).build().unique();
                        if (activity != Config.PurchaseOrderActivity&& activity != Config.SaleOrderActivity) {
                            InStorageNumDao inStorageNumDao = daoSession.getInStorageNumDao();
                            List<InStorageNum> innum = inStorageNumDao.queryBuilder().where(InStorageNumDao.Properties.FBatchNo.eq(t_detail.FBatch), InStorageNumDao.Properties.FStockID.eq(t_detail.FStorageId)
                                    , InStorageNumDao.Properties.FStockPlaceID.eq(t_detail.FPositionId), InStorageNumDao.Properties.FItemID.eq(t_detail.FProductId)).build().list();
                            if (innum.size() > 0) {
                                if (activity == Config.OtherInStoreActivity ||activity == Config.PurchaseInStorageActivity || activity == Config.ProductInStorageActivity || activity == Config.OtherInStoreActivity) {
                                    innum.get(0).FQty = (Double.parseDouble(innum.get(0).FQty) - (Double.parseDouble(t_detail.FQuantity) * t_detail.unitrate)) + "";
                                    inStorageNumDao.update(innum.get(0));
                                } else if (activity ==Config.DBActivity) {
                                    innum.get(0).FQty = (Double.parseDouble(innum.get(0).FQty) - (Double.parseDouble(t_detail.FQuantity) * t_detail.unitrate)) + "";
                                    inStorageNumDao.update(innum.get(0));
                                    InStorageNumDao inStorageNumDao1 = daoSession.getInStorageNumDao();
                                    List<InStorageNum> outnum = inStorageNumDao1.queryBuilder().where(InStorageNumDao.Properties.FBatchNo.eq(t_detail.FBatch), InStorageNumDao.Properties.FStockID.eq(t_detail.FoutStorageid)
                                            , InStorageNumDao.Properties.FStockPlaceID.eq(t_detail.Foutwavehouseid), InStorageNumDao.Properties.FItemID.eq(t_detail.FProductId)).build().list();
                                    if (outnum.size() > 0) {
                                        outnum.get(0).FQty = (Double.parseDouble(innum.get(0).FQty) + (Double.parseDouble(t_detail.FQuantity) * t_detail.unitrate)) + "";
                                        inStorageNumDao1.update(outnum.get(0));
                                    }
                                } else {
                                    innum.get(0).FQty = (Double.parseDouble(innum.get(0).FQty) + (Double.parseDouble(t_detail.FQuantity) * t_detail.unitrate)) + "";
                                    inStorageNumDao.update(innum.get(0));
                                }
                            }
                            t_detailDao.delete(t_detail);
                        } else {
                            t_detailDao.delete(t_detail);
                        }
                        Toast.showText(mContext, "删除成功");
                        initList();
                        tableAdapter.notifyDataSetChanged();
                        Log.e("list", list.size() + "");
                        Log.e("list1", list1.size() + "");

                    }
                });
                ab1.setNegativeButton("取消", null);
                ab1.create().show();
                break;
            case R.id.fix:
                AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                ab.setTitle("修改数据");
                View view = LayoutInflater.from(mContext).inflate(R.layout.fixalertitems, null);
                final EditText mEtNum = view.findViewById(R.id.ed_num);
                final EditText mEtPrice = view.findViewById(R.id.ed_price);
                mEtNum.setText(list.get(position).FQuantity);
                mEtPrice.setText(list.get(position).FTaxUnitPrice);
                ab.setView(view);
                ab.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        T_Detail t_detail = t_detailDao.queryBuilder().where(T_DetailDao.Properties.FIndex.eq(list.get(position).FIndex)).build().unique();
                        String old = t_detail.FQuantity;
                        String newnum = mEtNum.getText().toString();
                        double getnum = Double.parseDouble(old) - Double.parseDouble(newnum);
                        Log.e("getnum", getnum + "");
                        if (!mEtPrice.getText().toString().equals(""))
                            t_detail.FTaxUnitPrice = mEtPrice.getText().toString();
                        if (!mEtNum.getText().toString().equals("")) {
                            if (activity != Config.PurchaseOrderActivity && activity != Config.SaleOrderActivity) {
                                InStorageNumDao inStorageNumDao = daoSession.getInStorageNumDao();
                                List<InStorageNum> innum = inStorageNumDao.queryBuilder().where(InStorageNumDao.Properties.FBatchNo.eq(t_detail.FBatch), InStorageNumDao.Properties.FStockID.eq(t_detail.FStorageId)
                                        , InStorageNumDao.Properties.FStockPlaceID.eq(t_detail.FPositionId), InStorageNumDao.Properties.FItemID.eq(t_detail.FProductId)).build().list();
                                if (innum.size() > 0) {
                                    if (activity == Config.PurchaseInStorageActivity || activity == Config.ProductInStorageActivity || activity == Config.OtherInStoreActivity) {
                                        innum.get(0).FQty = (Double.parseDouble(innum.get(0).FQty) - (getnum * t_detail.unitrate)) + "";
                                        inStorageNumDao.update(innum.get(0));
                                        Toast.showText(mContext, "修改成功");
                                        t_detail.FQuantity = newnum;
                                        t_detailDao.update(t_detail);
                                    } else if (activity == Config.DBActivity) {
                                        List<InStorageNum> outnum = inStorageNumDao.queryBuilder().where(InStorageNumDao.Properties.FBatchNo.eq(t_detail.FBatch), InStorageNumDao.Properties.FStockID.eq(t_detail.FoutStorageid)
                                                , InStorageNumDao.Properties.FStockPlaceID.eq(t_detail.Foutwavehouseid), InStorageNumDao.Properties.FItemID.eq(t_detail.FProductId)).build().list();
                                        if (outnum.size() > 0) {
                                            if (getnum < 0 && Double.parseDouble(outnum.get(0).FQty) > -getnum) {
                                                Log.e("qtyout", outnum.get(0).FQty);
                                                innum.get(0).FQty = (Double.parseDouble(innum.get(0).FQty) - (getnum * t_detail.unitrate)) + "";
                                                inStorageNumDao.update(innum.get(0));
                                                outnum.get(0).FQty = (Double.parseDouble(innum.get(0).FQty) + (getnum * t_detail.unitrate)) + "";
                                                inStorageNumDao.update(outnum.get(0));
                                                Toast.showText(mContext, "修改成功");
                                                t_detail.FQuantity = newnum;
                                                t_detailDao.update(t_detail);
                                            } else if (getnum > 0) {
                                                Log.e("getnum > 0", getnum + "");
                                                innum.get(0).FQty = (Double.parseDouble(innum.get(0).FQty) - (getnum * t_detail.unitrate)) + "";
                                                inStorageNumDao.update(innum.get(0));
                                                outnum.get(0).FQty = (Double.parseDouble(innum.get(0).FQty) + (getnum * t_detail.unitrate)) + "";
                                                inStorageNumDao.update(outnum.get(0));
                                                Toast.showText(mContext, "修改成功");
                                                t_detail.FQuantity = newnum;
                                                t_detailDao.update(t_detail);
                                            } else {
                                                Toast.showText(mContext, "库存不足");
                                            }
                                        }
                                    } else {
                                        innum.get(0).FQty = (Double.parseDouble(innum.get(0).FQty) + (getnum * t_detail.unitrate)) + "";
                                        inStorageNumDao.update(innum.get(0));
                                        t_detail.FQuantity = newnum;
                                        Toast.showText(mContext, "修改成功");
                                        t_detailDao.update(t_detail);
                                    }


                                }

                            } else {
                                t_detail.FQuantity = newnum;
                                Toast.showText(mContext, "修改成功");
                                t_detailDao.update(t_detail);
                            }
                        }
                        initData();

                    }
                });
                ab.setNegativeButton("取消", null);
                ab.create().show();
                break;
        }
    }


}

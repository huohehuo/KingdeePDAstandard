package com.fangzuo.assist.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Adapter.DownTipRyAdapter;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.DownTipBean;
import com.fangzuo.assist.Beans.DownloadReturnBean;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.EventBusUtil;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.JsonCreater;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.MathUtil;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.greendao.gen.BarCodeDao;
import com.fangzuo.greendao.gen.BibieDao;
import com.fangzuo.greendao.gen.ClientDao;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.DepartmentDao;
import com.fangzuo.greendao.gen.EmployeeDao;
import com.fangzuo.greendao.gen.GetGoodsDepartmentDao;
import com.fangzuo.greendao.gen.InStorageNumDao;
import com.fangzuo.greendao.gen.InStoreTypeDao;
import com.fangzuo.greendao.gen.PayTypeDao;
import com.fangzuo.greendao.gen.PriceMethodDao;
import com.fangzuo.greendao.gen.ProductDao;
import com.fangzuo.greendao.gen.PurchaseMethodDao;
import com.fangzuo.greendao.gen.StorageDao;
import com.fangzuo.greendao.gen.SuppliersDao;
import com.fangzuo.greendao.gen.UnitDao;
import com.fangzuo.greendao.gen.UserDao;
import com.fangzuo.greendao.gen.WanglaikemuDao;
import com.fangzuo.greendao.gen.WaveHouseDao;
import com.fangzuo.greendao.gen.YuandanTypeDao;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.async.AsyncSession;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DownDataActivity extends BaseActivity {

    @BindView(R.id.ry_down_tip)
    EasyRecyclerView ryDownTip;
    @BindView(R.id.tv_tip)
    TextView tvTip;
    @BindView(R.id.pg_loading)
    ProgressBar pgLoading;
    @BindView(R.id.btn_down)
    Button btnDown;
    @BindView(R.id.btn_downall)
    Button btnDownall;
    private DownTipRyAdapter adapter;
    private ArrayList<String> chooseAllS;
    private List<DownTipBean> listData;
    private Context mContext;
    private BasicShareUtil share;
    private DaoSession session;


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCome(ClassEvent event) {
        if (event != null) {
            switch (event.Msg) {
                case EventBusInfoCode.AutoDown:
                    Lg.e("nowTag" + nowTag);
                    nowTipAdd += MathUtil.toInt(backSize);
                    chooseAllS.remove(nowTag);
                    adapter.notifyDataSetChanged();
                    if (chooseAllS.size() > 0) {
                        startDown();
                    } else {
                        chooseAllS = new ArrayList<>();
                        pgLoading.setVisibility(View.INVISIBLE);
                        tvTip.setText("下载完成,共：" + nowTipAdd);
                        nowTipAdd = 0;
                        adapter.setDownList(chooseAllS);
                        btnDown.setText("下载");
                        btnDownall.setText("下载全部");
                    }
                    break;
            }
        }
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_down_data);
        ButterKnife.bind(this);
        EventBusUtil.register(this);
        mContext = this;
        share = BasicShareUtil.getInstance(mContext);
        session = GreenDaoManager.getmInstance(mContext).getDaoSession();
//        tvTitle.setText("下载数据");
        chooseAllS = new ArrayList<>();
//        chooseAll = new ArrayList<>();

        ryDownTip.setAdapter(adapter = new DownTipRyAdapter(this));
//        ryDownTip.setLayoutManager(new LinearLayoutManager(this));
        ryDownTip.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

    }

    @Override
    protected void initData() {
        adapter.clear();
        listData = new ArrayList<>();
        listData.add(new DownTipBean("币别表", 1));
        listData.add(new DownTipBean("部门表", 2));
        listData.add(new DownTipBean("职员表", 3));
        listData.add(new DownTipBean("仓位表", 4));
        listData.add(new DownTipBean("库存表", 5));
        listData.add(new DownTipBean("仓库表", 6));
        listData.add(new DownTipBean("单位表", 7));
        listData.add(new DownTipBean("条码表", 8));
        listData.add(new DownTipBean("供应商表", 9));
        listData.add(new DownTipBean("结算方式表", 10));
        listData.add(new DownTipBean("商品资料表", 11));
        listData.add(new DownTipBean("用户信息表", 12));
        listData.add(new DownTipBean("客户信息表", 13));
        listData.add(new DownTipBean("交货单位", 14));
        listData.add(new DownTipBean("销售/采购方式表", 15));
        listData.add(new DownTipBean("源单类型", 16));
        listData.add(new DownTipBean("往来科目", 17));
        listData.add(new DownTipBean("价格政策", 18));
        listData.add(new DownTipBean("入库类型", 19));
        adapter.addAll(listData);

    }


    @Override
    protected void initListener() {
//列表点击事件
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                DownTipBean noticBean = adapter.getAllData().get(position);
                Lg.e("点击", noticBean);
                if (!chooseAllS.contains(noticBean.tag)) {
                    chooseAllS.add(noticBean.tag);
                } else {
                    chooseAllS.remove(noticBean.tag);
                }
                adapter.setDownList(chooseAllS);
            }
        });
    }


    @OnClick({R.id.btn_back, R.id.btn_down, R.id.btn_downall, R.id.cancle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancle:
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_down:
                Lg.e("得到下载list", chooseAllS);
                if (null == chooseAllS || chooseAllS.size() <= 0) {
                    Toast.showText(mContext, "请选择需要下载的表");
                    return;
                }
                btnDown.setText("正在下载...");
                startDown();
                break;
            case R.id.btn_downall:
                chooseAllS = new ArrayList<>();
                for (int i = 0; i < listData.size(); i++) {
                    chooseAllS.add(listData.get(i).tag);
                }
                initData();
                btnDownall.setText("正在下载");
                startDown();
                break;
        }
    }

    private String nowTag = "";
    private int nowTipAdd;

    private void startDown() {
        if (null == chooseAllS || chooseAllS.size() <= 0) return;
        ArrayList<Integer> choose = new ArrayList<>();
        nowTag = chooseAllS.get(0);
        choose.add(MathUtil.toInt(chooseAllS.get(0)));
        pgLoading.setVisibility(View.VISIBLE);
        tvTip.setText("正在下载,请稍等...");
        String json = JsonCreater.DownLoadData(share.getDatabaseIp(),
                share.getDatabasePort(), share.getDataBaseUser(), share.getDataBasePass(),
                share.getDataBase(), share.getVersion(), choose);
        App.getRService().downloadData(json, new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                DownloadReturnBean dBean = new Gson().fromJson(commonResponse.returnJson, DownloadReturnBean.class);
                insert(dBean);
            }

            @Override
            public void onError(Throwable e) {
//                LoadingUtil.dismiss();
                Toast.showText(mContext, "下载错误:" + e.toString());
                checkSaveOK("下载错误");
            }
        });
    }


    private void insert(final DownloadReturnBean dBean) {
        AsyncSession asyncSession = session.startAsyncSession();
        asyncSession.runInTx(new Runnable() {
            @Override
            public void run() {
                boolean b = insertLocalSQLite(dBean);
                if (b) {
                    checkSaveOK(backSize);
                } else {
                    Toast.showText(mContext, nowTag + ":存入本地失败");
                    checkSaveOK("保存错误");
                }

            }
        });
    }
    //判断是否完成下载返回，并修改列表状态
    private void checkSaveOK(String save) {
        if (MathUtil.isNumeric(save) && MathUtil.toD(save) <= 0) {
            save = "无数据";
        }
        for (int i = 0; i < listData.size(); i++) {
            if (listData.get(i).tag.equals(nowTag)) {
                DownTipBean bean = listData.get(i);
                bean.FTip = save;
                listData.set(i, bean);
                break;
            }
        }
        EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.AutoDown, ""));
    }


    private String backSize;

    private boolean insertLocalSQLite(DownloadReturnBean dBean) {
        Lg.e("保存");
        backSize = "0";
        if (dBean.BarCode != null && dBean.BarCode.size() > 0) {
            BarCodeDao barCodeDao = session.getBarCodeDao();
            barCodeDao.deleteAll();
            barCodeDao.insertOrReplaceInTx(dBean.BarCode);
            backSize = dBean.BarCode.size() + "";
            barCodeDao.detachAll();
        }
        if (dBean.User != null && dBean.User.size() > 0) {
            UserDao userDao = session.getUserDao();
            userDao.deleteAll();
            userDao.insertOrReplaceInTx(dBean.User);
            backSize = dBean.User.size() + "";
            userDao.detachAll();
        }
        if (dBean.clients != null && dBean.clients.size() > 0) {
            ClientDao clientDao = session.getClientDao();
            clientDao.deleteAll();
            clientDao.insertOrReplaceInTx(dBean.clients);
            backSize = dBean.clients.size() + "";
            clientDao.detachAll();
        }
        if (dBean.getGoodsDepartments != null && dBean.getGoodsDepartments.size() > 0) {
            GetGoodsDepartmentDao getGoodsDepartmentDao = session.getGetGoodsDepartmentDao();
            getGoodsDepartmentDao.deleteAll();
            getGoodsDepartmentDao.insertOrReplaceInTx(dBean.getGoodsDepartments);
            backSize = dBean.getGoodsDepartments.size() + "";
            getGoodsDepartmentDao.detachAll();
        }
        if (dBean.purchaseMethod != null && dBean.purchaseMethod.size() > 0) {
            PurchaseMethodDao purchaseMethodDao = session.getPurchaseMethodDao();
            purchaseMethodDao.deleteAll();
            purchaseMethodDao.insertOrReplaceInTx(dBean.purchaseMethod);
            backSize = dBean.purchaseMethod.size() + "";
            purchaseMethodDao.detachAll();
        }
        if (dBean.yuandanTypes != null && dBean.yuandanTypes.size() > 0) {
            YuandanTypeDao yuandanTypeDao = session.getYuandanTypeDao();
            yuandanTypeDao.deleteAll();
            yuandanTypeDao.insertOrReplaceInTx(dBean.yuandanTypes);
            backSize = dBean.yuandanTypes.size() + "";
            yuandanTypeDao.detachAll();
        }
        if (dBean.wanglaikemu != null && dBean.wanglaikemu.size() > 0) {
            WanglaikemuDao wanglaikemuDao = session.getWanglaikemuDao();
            wanglaikemuDao.deleteAll();
            wanglaikemuDao.insertOrReplaceInTx(dBean.wanglaikemu);
            backSize = dBean.wanglaikemu.size() + "";
            wanglaikemuDao.detachAll();
        }
        if (dBean.priceMethods != null && dBean.priceMethods.size() > 0) {
            PriceMethodDao priceMethodDao = session.getPriceMethodDao();
            priceMethodDao.deleteAll();
            priceMethodDao.insertOrReplaceInTx(dBean.priceMethods);
            backSize = dBean.priceMethods.size() + "";
            priceMethodDao.detachAll();
        }
        if (dBean.inStorageTypes != null && dBean.inStorageTypes.size() > 0) {
            InStoreTypeDao inStoreTypeDao = session.getInStoreTypeDao();
            inStoreTypeDao.deleteAll();
            inStoreTypeDao.insertOrReplaceInTx(dBean.inStorageTypes);
            backSize = dBean.inStorageTypes.size() + "";
            inStoreTypeDao.detachAll();
        }
        if (dBean.bibiezhongs != null && dBean.bibiezhongs.size() > 0) {
            BibieDao bibieDao = session.getBibieDao();
            bibieDao.deleteAll();
            bibieDao.insertOrReplaceInTx(dBean.bibiezhongs);
            backSize = dBean.bibiezhongs.size() + "";
            bibieDao.detachAll();
        }

        if (dBean.department != null && dBean.department.size() > 0) {
            DepartmentDao departmentDao = session.getDepartmentDao();
            departmentDao.deleteAll();
            departmentDao.insertOrReplaceInTx(dBean.department);
            backSize = dBean.department.size() + "";
            departmentDao.detachAll();
        }
        if (dBean.employee != null && dBean.employee.size() > 0) {
            EmployeeDao employeeDao = session.getEmployeeDao();
            employeeDao.deleteAll();
            employeeDao.insertOrReplaceInTx(dBean.employee);
            backSize = dBean.employee.size() + "";
            employeeDao.detachAll();
        }
        if (dBean.wavehouse != null && dBean.wavehouse.size() > 0) {
            WaveHouseDao wavehouseDao = session.getWaveHouseDao();
            wavehouseDao.deleteAll();
            wavehouseDao.insertOrReplaceInTx(dBean.wavehouse);
            backSize = dBean.wavehouse.size() + "";
            wavehouseDao.detachAll();
        }
        if (dBean.InstorageNum != null && dBean.InstorageNum.size() > 0) {
            InStorageNumDao storageNumDao = session.getInStorageNumDao();
            storageNumDao.deleteAll();
            storageNumDao.insertOrReplaceInTx(dBean.InstorageNum);
            backSize = dBean.InstorageNum.size() + "";
            storageNumDao.detachAll();
        }
        if (dBean.storage != null && dBean.storage.size() > 0) {
            StorageDao storageDao = session.getStorageDao();
            storageDao.deleteAll();
            storageDao.insertOrReplaceInTx(dBean.storage);
            backSize = dBean.storage.size() + "";
            storageDao.detachAll();
        }
        if (dBean.units != null && dBean.units.size() > 0) {
            UnitDao unitDao = session.getUnitDao();
            unitDao.deleteAll();
            unitDao.insertOrReplaceInTx(dBean.units);
            backSize = dBean.units.size() + "";
            unitDao.detachAll();
        }

        if (dBean.suppliers != null && dBean.suppliers.size() > 0) {
            SuppliersDao suppliersDao = session.getSuppliersDao();
            suppliersDao.deleteAll();
            suppliersDao.insertOrReplaceInTx(dBean.suppliers);
            backSize = dBean.suppliers.size() + "";
            suppliersDao.detachAll();
        }
        if (dBean.payTypes != null && dBean.payTypes.size() > 0) {
            PayTypeDao payTypeDao = session.getPayTypeDao();
            payTypeDao.deleteAll();
            payTypeDao.insertOrReplaceInTx(dBean.payTypes);
            backSize = dBean.payTypes.size() + "";
            payTypeDao.detachAll();
        }
        if (dBean.products != null && dBean.products.size() > 0) {
            ProductDao productDao = session.getProductDao();
            productDao.deleteAll();
            productDao.insertOrReplaceInTx(dBean.products);
            backSize = dBean.products.size() + "";
            productDao.detachAll();
        }
        return true;
    }


    public static void start(Context context) {
        Intent intent = new Intent(context, DownDataActivity.class);
//        intent.putExtra("activity", activity);
//        intent.putStringArrayListExtra("fid", fid);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            EventBusUtil.unregister(this);
        } catch (Exception e) {

        }
    }

    @Override
    protected void OnReceive(String code) {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}

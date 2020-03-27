package com.fangzuo.assist.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Adapter.MenuFragmentAdapter;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.Beans.UseTimeBean;
import com.fangzuo.assist.Dao.Product;
import com.fangzuo.assist.Fragment.PurchaseFragment;
import com.fangzuo.assist.Fragment.SaleFragment;
import com.fangzuo.assist.Fragment.SettingFragment;
import com.fangzuo.assist.Fragment.StorageFragment;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Utils.Asynchttp;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.ControlUtil;
import com.fangzuo.assist.Utils.DownLoadData;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.assist.Utils.ShareUtil;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.UpgradeUtil.AppVersionUtil;
import com.fangzuo.assist.Utils.WebApi;
import com.fangzuo.assist.widget.LoadingUtil;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.NoticBeanDao;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.loopj.android.http.AsyncHttpClient;
import com.orhanobut.hawk.Hawk;

import org.greenrobot.greendao.async.AsyncSession;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MenuActivity extends BaseActivity {


    @BindView(R.id.btn_back)
    RelativeLayout btnBack;
    @BindView(R.id.tv_user)
    TextView tvUser;
    @BindView(R.id.iv_purchase)
    ImageView ivPurchase;
    @BindView(R.id.tv_purchase)
    TextView tvPurchase;
    @BindView(R.id.bottom_btn_purchase)
    LinearLayout bottomBtnPurchase;
    @BindView(R.id.iv_sale)
    ImageView ivSale;
    @BindView(R.id.tv_sale)
    TextView tvSale;
    @BindView(R.id.bottom_btn_sale)
    LinearLayout bottomBtnSale;
    @BindView(R.id.iv_storage)
    ImageView ivStorage;
    @BindView(R.id.tv_storage)
    TextView tvStorage;
    @BindView(R.id.bottom_btn_storage)
    LinearLayout bottomBtnStorage;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.tv_setting)
    TextView tvSetting;
    @BindView(R.id.bottom_btn_setting)
    LinearLayout bottomBtnSetting;
    @BindColor(R.color.bottombartv)
    int tvcolor;
    @BindColor(R.color.fragment_text)
    int tvColorUnClick;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.update_data)
    TextView updateData;
    @BindView(R.id.container)
    CoordinatorLayout container;
    @BindView(R.id.tv_notic)
    TextView tvNotic;
    @BindView(R.id.iv_notic)
    ImageView ivNotic;
    private MenuActivity mContext;
    private FragmentTransaction ft;
    private Fragment curFragment;
    private AsyncSession asyncSession;
    private AsyncSession asyncSession2;
    private long nowTime;
    private int size;
    private ProgressDialog pg;
    NoticBeanDao noticBeanDao;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    long nowTime = (long) msg.obj;
                    int size = msg.arg1;
                    long endTime = System.currentTimeMillis();
                    AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                    ab.setTitle("下载完成");
                    ab.setMessage("耗时:" + (endTime - nowTime) + "ms" + ",共插入" + size + "条数据");
                    ab.setPositiveButton("确认", null);
                    ab.create().show();
                    break;
                case 2:
                    Toast.showText(mContext, "线程2完成");
                    Log.e("线程2回调", "线程2完成");
                    break;
            }
        }
    };


    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(ClassEvent event) {
        switch (event.Msg) {
            case EventBusInfoCode.Upload_Notice://
                tvNotic.setText(noticBeanDao.loadAll().size()+"");
                break;
            case EventBusInfoCode.Click_Order://回单失败
                String tag = (String)event.postEvent;
                clickOrder(tag);
        }
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_menu);
        mContext = this;
        ButterKnife.bind(mContext);
        noticBeanDao = daoSession.getNoticBeanDao();
        initFragments();
        //登录成功，开启循环请求推送信息
//        App.startRunGetNotice();
        //更新时间控制日期
        ControlUtil.DownLoadUseTime();

    }
    @Override
    public void onBackPressed() {
//        //若是没勾选自动登录，推出后停止获取推送信息
        if (!"OK".equals(Hawk.get(Config.CheckAutoLogin,""))){
//            App.stopRunGetNotice();
            startNewActivity(LoginActivity.class, R.anim.activity_slide_left_in, R.anim.activity_slide_left_out, true, null);
        }else{
            super.onBackPressed();
        }
    }
    @Override
    public void initData() {
        tvUser.setText("当前用户:" + ShareUtil.getInstance(mContext).getUserName());
        ivPurchase.setImageResource(R.mipmap.purchase);
        tvPurchase.setTextColor(tvcolor);
        tvNotic.setText(noticBeanDao.loadAll().size()+"");
        items_pushdown= new ArrayList<>();
//        String getPermit= Hawk.get(Config.User_Permit,"");
//        String[] aa = getPermit.split("\\-"); // 这样才能得到正确的结果
//        dealPushDownMenu(null);
    }


    @Override
    public void initListener() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        resetBottomView();
                        ivPurchase.setImageResource(R.mipmap.purchase);
                        tvPurchase.setTextColor(tvcolor);
                        break;
                    case 1:
                        resetBottomView();
                        ivSale.setImageResource(R.mipmap.sale);
                        tvSale.setTextColor(tvcolor);
                        break;
                    case 2:
                        resetBottomView();
                        ivStorage.setImageResource(R.mipmap.storage);
                        tvStorage.setTextColor(tvcolor);
                        break;
                    case 3:
                        resetBottomView();
                        ivSetting.setImageResource(R.mipmap.setting_focus);
                        tvSetting.setTextColor(tvcolor);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }



    //初始化Fragment
    private void initFragments() {
        FragmentManager fm = getSupportFragmentManager();
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new PurchaseFragment());
        fragments.add(new SaleFragment());
        fragments.add(new StorageFragment());
        fragments.add(new SettingFragment());
        MenuFragmentAdapter menuFragmentAdapter = new MenuFragmentAdapter(fm, fragments);
        viewPager.setAdapter(menuFragmentAdapter);
        viewPager.setCurrentItem(0);
    }

    @OnClick({R.id.btn_back, R.id.iv_notic, R.id.update_data, R.id.bottom_btn_purchase, R.id.bottom_btn_sale, R.id.bottom_btn_storage, R.id.bottom_btn_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                new AlertDialog.Builder(mContext)
                        .setTitle("是否重新登录")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Hawk.put(Config.CheckAutoLogin,"noOK");
                                App.stopRunGetNotice();//停止获取推送信息
                                startNewActivity(LoginActivity.class, R.anim.activity_slide_left_in, R.anim.activity_slide_left_out, true, null);
                                finish();

                            }
                        })
                        .create().show();
                break;
            case R.id.iv_notic:
                NoticeActivity.start(mContext);
                break;
            case R.id.update_data:
                DownLoadData.getInstance(mContext, container, handler).alertToChoose();
                break;
            case R.id.bottom_btn_purchase:
                viewPager.setCurrentItem(0, true);
                resetBottomView();
                ivPurchase.setImageResource(R.mipmap.purchase);
                tvPurchase.setTextColor(tvcolor);
                break;
            case R.id.bottom_btn_sale:
                viewPager.setCurrentItem(1, true);
                resetBottomView();
                ivSale.setImageResource(R.mipmap.sale);
                tvSale.setTextColor(tvcolor);
                break;
            case R.id.bottom_btn_storage:
                viewPager.setCurrentItem(2, true);
                resetBottomView();
                ivStorage.setImageResource(R.mipmap.storage);
                tvStorage.setTextColor(tvcolor);
                break;
            case R.id.bottom_btn_setting:
                viewPager.setCurrentItem(3, true);
                resetBottomView();
                ivSetting.setImageResource(R.mipmap.setting_focus);
                tvSetting.setTextColor(tvcolor);
                break;
        }
    }

    private AlertDialog.Builder builder;
    private Bundle b;
    List<String> items_pushdown  ;   /*= new String[]{"原单", "销售订单下推销售出库单","VMI销售订单下推销售出库单","退货通知单下推销售退货单"};*/
    //统一点击单据时跳转
    public void clickOrder(String tag) {
        switch (tag) {
            case "1"://采购订单
                startNewActivity(PurchaseOrderActivity.class,R.anim.activity_slide_left_in, R.anim.activity_slide_left_out,false, null);
                break;
            case "2"://外购入库
                startNewActivity(PurchaseInStorageActivity.class,R.anim.activity_slide_left_in, R.anim.activity_slide_left_out,false,  null);
                break;
            case "3"://产品入库
                startNewActivity(ProductInStorageActivity.class,R.anim.activity_slide_left_in, R.anim.activity_slide_left_out,false,  null);
                break;
            case "4"://销售订单
                startNewActivity(SaleOrderActivity.class,R.anim.activity_slide_left_in, R.anim.activity_slide_left_out,false,  null);
                break;
            case "5"://销售出库
                startNewActivity(SoldOutActivity.class,R.anim.activity_slide_left_in, R.anim.activity_slide_left_out,false,  null);
                break;
            case "7"://生产领料
                startNewActivity(ProduceAndGetActivity.class,R.anim.activity_slide_left_in, R.anim.activity_slide_left_out,false,  null);
                break;
            case "8"://盘点
                startNewActivity(PDActivity.class,R.anim.activity_slide_left_in, R.anim.activity_slide_left_out,false,  null);
                break;
            case "9"://调拨
                startNewActivity(DBActivity.class,R.anim.activity_slide_left_in, R.anim.activity_slide_left_out,false,  null);
                break;
            case "10"://其他入库
                startNewActivity(OtherInStoreActivity.class,R.anim.activity_slide_left_in, R.anim.activity_slide_left_out,false,  null);
                break;
            case "11"://其他出库
                startNewActivity(OtherOutStoreActivity.class,R.anim.activity_slide_left_in, R.anim.activity_slide_left_out,false,  null);
                break;
            case "26"://库存查询
                startNewActivity(StorageCheckActivity.class,R.anim.activity_slide_left_in, R.anim.activity_slide_left_out,false,  null);
                break;
            case "6"://单据下推
//                // 创建对话框构建器
//                b = new Bundle();
//                builder = new AlertDialog.Builder(this);
//                // 设置参数
//                builder.setAdapter(
//                        new ArrayAdapter<String>(this,
//                                R.layout.item_choose, R.id.textView, items_pushdown),
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog,
//                                                int which) {
//                                switch (items_pushdown.get(which)) {
//                                    case "销售订单下推销售出库":b.clear();b.putInt("123", 1);break;
//                                    case "采购订单下推外购入库":b.clear();b.putInt("123", 2);break;
//                                    case "发货通知下推销售出库":b.clear();b.putInt("123", 3);break;
//                                    case "收料通知下推外购入库":b.clear();b.putInt("123", 4);break;
//                                    case "委外订单下推委外入库":b.clear();b.putInt("123", 11);break;
//                                    case "委外订单下推委外出库":b.clear();b.putInt("123", 12);break;
//                                    case "生产任务单下推产品入库":b.clear();b.putInt("123", 9);break;
//                                    case "生产任务单下推生产领料":b.clear();b.putInt("123", 13);break;
//                                    case "采购订单下推收料通知单":b.clear();b.putInt("123", 14);break;
//                                    case "销售订单下推发料通知单":b.clear();b.putInt("123", 15);break;
//                                    case "生产任务单下推生产汇报单":b.clear();b.putInt("123", 16);break;
//                                    case "汇报单下推产品入库":b.clear();b.putInt("123", 18);break;
//                                    case "销售出库单验货":b.clear();b.putInt("123", 7);break;
//                                    case "发货通知生成调拨单":b.clear();b.putInt("123", 20);break;
//                                }
//                                startNewActivity(PushDownPagerActivity.class, R.anim.activity_fade_in, R.anim.activity_fade_out, false, b);
//                            }
//                        });
//                builder.create().show();
                startNewActivity(PushDownActivity.class,R.anim.activity_slide_left_in, R.anim.activity_slide_left_out,false,  null);
                break;

        }
    }

    //处理二级菜单权限数据
    private void dealPushDownMenu(String[] arys){
        Lg.e("赛选：",arys);
        String[] ary= new String[]{"12","13","14","15","16","17","18","19","20","21","22","23","24","25"};
        for (int i=0; i<ary.length;i++){
            switch (ary[i]){
                //成品外购入库
                case "12":
                    items_pushdown.add("销售订单下推销售出库");
                    break;
                case "13":
                    items_pushdown.add("采购订单下推外购入库");
                    break;
                case "14":
                    items_pushdown.add("发货通知下推销售出库");
                    break;
                case "15":
                    items_pushdown.add("收料通知下推外购入库");
                    break;
                case "16":
                    items_pushdown.add("委外订单下推委外入库");
                    break;
                case "17":
                    items_pushdown.add("委外订单下推委外出库");
                    break;
                case "18":
                    items_pushdown.add("生产任务单下推产品入库");
                    break;
                case "19":
                    items_pushdown.add("生产任务单下推生产领料");
                    break;
                case "20":
                    items_pushdown.add("采购订单下推收料通知单");
                    break;
                case "21":
                    items_pushdown.add("销售订单下推发料通知单");
                    break;
                case "22":
                    items_pushdown.add("生产任务单下推生产汇报单");
                    break;
                case "23":
                    items_pushdown.add("汇报单下推产品入库");
                    break;
                case "24":
                    items_pushdown.add("销售出库单验货");
                    break;
                case "25":
                    items_pushdown.add("发货通知生成调拨单");
                    break;

            }
//            Log.e("test","定位ary:"+ary[i]);
//            Log.e("test","定位items:"+items.get(i).tag);
            //根据ary的值，遍历list符合的item并添加
//            for (int j=0;j<ary.length;j++){
//                if (TextUtils.equals(items.get(i).tag,ary[j])){
//                    Lg.e("加入单据",items.get(i));
//                    backItems.add(items.get(i));
//                }
//            }
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        //检测版本更新
        AppVersionUtil.CheckVersion(mContext);
    }

    //重置所有按钮未未选中状态
    private void resetBottomView() {
        ivPurchase.setImageResource(R.mipmap.unpurchase);
        ivSale.setImageResource(R.mipmap.unsale);
        ivStorage.setImageResource(R.mipmap.unstorage);
        ivSetting.setImageResource(R.mipmap.unsetting);
        tvPurchase.setTextColor(tvColorUnClick);
        tvSale.setTextColor(tvColorUnClick);
        tvSetting.setTextColor(tvColorUnClick);
        tvStorage.setTextColor(tvColorUnClick);
    }




    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }


    @Override
    protected void OnReceive(String code) {

    }


}

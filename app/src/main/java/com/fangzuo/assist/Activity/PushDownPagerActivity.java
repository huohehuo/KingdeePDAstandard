package com.fangzuo.assist.Activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Adapter.StripAdapter;
import com.fangzuo.assist.Fragment.pushdown.ChooseFragment;
import com.fangzuo.assist.Fragment.pushdown.DownLoadPushFragment;
import com.fangzuo.assist.Utils.PagerSlidingTabStrip;
import com.fangzuo.assist.R;

import java.util.ArrayList;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;


//          下载，选择单据页面（包含：下载单据Fragment，选择单据Fragment）
public class PushDownPagerActivity extends BaseActivity {
    @BindView(R.id.tabstrip)
    PagerSlidingTabStrip tabstrip;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindColor(R.color.cpb_blue)
    int cpb_blue;
    public int tag;
    @BindView(R.id.tv_pdname)
    TextView tvPdname;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_push_down_pager);
        ButterKnife.bind(this);
        tag = getIntent().getExtras().getInt("123");
        Log.e("获取到--tag--", tag +"");
        setPDTitle(tag);//设置页面标题
    }

    @Override
    protected void initData() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        fragments.add(new DownLoadPushFragment());
        fragments.add(new ChooseFragment());
        titles.add("下载单据");
        titles.add("选择单据");
        StripAdapter stripAdapter = new StripAdapter(getSupportFragmentManager(), fragments, titles);
        Log.e("stripAdapter", stripAdapter + "");
        viewpager.setAdapter(stripAdapter);
        tabstrip.setShouldExpand(true);
        tabstrip.setViewPager(viewpager);
        tabstrip.setDividerColor(cpb_blue);
        tabstrip.setUnderlineHeight(3);
        tabstrip.setIndicatorHeight(6);
        tabstrip.setIndicatorColor(cpb_blue);

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void OnReceive(String code) {
        Log.e("code:","PushDownPagerActivity-"+code);
    }

    public int getTitles(){
        return tag;
    }
    //根据tag设置头部标题
    private void setPDTitle(int tag) {
        String string = "";
        switch (tag) {
            case 1:
                string = "销售订单下推销售出库";
                break;
            case 2:
                string = "采购订单下推外购入库";
                break;
            case 3:
                string = "发货通知下推销售出库";
                break;
            case 4:
                string = "收料通知下推外购入库";
                break;
            case 11:
                string = "委外订单下推委外入库";
                break;
            case 12:
                string = "委外订单下推委外出库";
                break;
            case 9:
                string = "生产任务单下推产品入库";
                break;
            case 13:
                string = "生产任务单下推生产领料";
                break;
            case 14:
                string = "采购订单下推收料通知单";
                break;
            case 15:
                string = "销售订单下推发料通知单";
                break;
            case 16:
                string = "生产任务单下推生产汇报单";
                break;
            case 18:
                string = "汇报单下推产品入库";
                break;
            case 7:
                string = "销售出库单验货";
                break;
            case 20:
                string = "发货通知生成调拨单";
                break;
            case 22:
                string = "产品入库验货";
                break;



        }
        if (!"".equals(string)){
            tvPdname.setText(string);
        }else{
            tvPdname.setVisibility(View.GONE);
        }
    }
}

package com.fangzuo.assist.Activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;

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


    @Override
    protected void initView() {
        setContentView(R.layout.activity_push_down_pager);
        ButterKnife.bind(this);
        tag = getIntent().getExtras().getInt("123");
        Log.e("获取到--tag--", tag +"");
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
}

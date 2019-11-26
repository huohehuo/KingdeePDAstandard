package com.fangzuo.assist.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Adapter.NoticRyAdapter;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.Dao.NoticBean;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.EventBusInfoCode;
import com.fangzuo.assist.Utils.EventBusUtil;
import com.fangzuo.assist.Utils.Lg;
import com.fangzuo.greendao.gen.NoticBeanDao;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoticeActivity extends BaseActivity {

    @BindView(R.id.btn_back)
    RelativeLayout btnBack;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.ry_notic)
    EasyRecyclerView ryNotic;
    private NoticRyAdapter adapter;
    NoticBeanDao noticBeanDao;
    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(ClassEvent event) {
        switch (event.Msg) {
            case EventBusInfoCode.Upload_Notice://
                initData();
                break;
        }
    }


    @Override
    protected void initView() {
        setContentView(R.layout.activity_notice);
        ButterKnife.bind(this);
        noticBeanDao = daoSession.getNoticBeanDao();
        tvTitle.setText("推送信息");
        ryNotic.setAdapter(adapter = new NoticRyAdapter(this));
        ryNotic.setLayoutManager(new LinearLayoutManager(this));
//        ryNotic.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
    }

    @Override
    protected void initData() {
        adapter.removeAll();
        adapter.addAll(noticBeanDao.loadAll());
    }

    @Override
    protected void initListener() {
//列表点击事件
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                NoticBean noticBean = adapter.getAllData().get(position);
                Lg.e("点击",noticBean);
                Bundle b = new Bundle();
                b.putInt("123", 3);
                b.putString("billNO", noticBean.FBillNo);
                startNewActivity(PushDownPagerActivity.class, 0, 0, true, b);


            }
        });
        adapter.setOnItemLongClickListener(new RecyclerArrayAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(int position) {
                noticBeanDao.delete(adapter.getAllData().get(position));
                initData();
                return true;
            }
        });
    }

    @Override
    protected void OnReceive(String code) {

    }

    @OnClick({R.id.btn_back, R.id.tv_title})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_title:
                break;
        }
    }
    public static void start(Context context){
        Intent intent = new Intent(context,NoticeActivity.class);
//        intent.putExtra("activity", activity);
//        intent.putStringArrayListExtra("fid", fid);
        context.startActivity(intent);
    }
}

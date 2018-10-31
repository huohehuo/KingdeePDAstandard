package com.fangzuo.assist.Activity;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.R;

public class MiddleActivity extends BaseActivity {



    @Override
    protected void initView() {
        setContentView(R.layout.activity_middle);
        startNewActivity(PDActivity.class,R.anim.activity_fade_in,R.anim.activity_fade_out,true,null);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void OnReceive(String code) {

    }
}

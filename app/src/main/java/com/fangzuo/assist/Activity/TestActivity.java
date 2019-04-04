package com.fangzuo.assist.Activity;

import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Server.WebAPI;
import com.fangzuo.assist.Utils.CallBack;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.RetrofitUtil;
import com.fangzuo.assist.widget.LoadingUtil;
import com.orhanobut.hawk.Hawk;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestActivity extends BaseActivity {


    @BindView(R.id.test_tomcat)
    Button testTomcat;
    @BindView(R.id.test_database)
    Button testDatabase;
    @BindView(R.id.tv_result)
    TextView tvResult;
    @BindView(R.id.btn_back)
    RelativeLayout btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private TestActivity mContext;
    @BindColor(R.color.green)
    int green;
    @BindColor(R.color.red)
    int red;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_test2);
        ButterKnife.bind(this);
        mContext = this;
        tvTitle.setText("网络测试");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        testDatabase.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                tvResult.setText(Hawk.get(Config.Text_Log, ""));
                return false;
            }
        });
    }

    @Override
    protected void OnReceive(String code) {

    }

    private void testTomcat() {
        LoadingUtil.showDialog(mContext, "测试中...");
        final long nowtime = System.currentTimeMillis();
        App.getRService().getTest("", new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                LoadingUtil.dismiss();
                long endTime = System.currentTimeMillis();
                tvResult.setTextColor(green);
                tvResult.setTextSize(18);
//                tvResult.setText("结果:获取150kb数据所需时间" + (endTime - nowtime) + "获取数据:" + commonResponse.returnJson);
                tvResult.setText("结果:连接TomCat服务器成功~ \n获取150kb数据所需时间" + (endTime - nowtime) );
            }

            @Override
            public void onError(Throwable e) {
                LoadingUtil.dismiss();
                tvResult.setTextColor(red);
                tvResult.setText(e.toString());
            }
        });

//
//        RetrofitUtil.getInstance(mContext).createReq(WebAPI.class).
//                TestTomcat(RetrofitUtil.getParams(mContext,"")).enqueue(new CallBack() {
//            @Override
//            public void onSucceed(CommonResponse cBean) {
//                long endTime = System.currentTimeMillis();
//                tvResult.setTextColor(green);
//                tvResult.setText("结果:获取150kb数据所需时间"+(endTime-nowtime)+"获取数据:"+cBean.returnJson);
//
//            }
//
//            @Override
//            public void OnFail(String Msg) {
//                tvResult.setTextColor(red);
//                tvResult.setText(Msg);
//            }
//        });
    }

    private void testDataBase() {
        LoadingUtil.showDialog(mContext, "测试中...");
        RetrofitUtil.getInstance(mContext).createReq(WebAPI.class).TestDataBase(RetrofitUtil.getParams(mContext, ""))
                .enqueue(new CallBack() {
                    @Override
                    public void onSucceed(CommonResponse cBean) {
                        LoadingUtil.dismiss();
                        tvResult.setText("服务器测试结果:\r\n" + cBean.returnJson);
                    }

                    @Override
                    public void OnFail(String Msg) {
                        LoadingUtil.dismiss();
                        tvResult.setText("服务器测试结果:" + Msg);
                    }
                });
    }

    @OnClick({R.id.test_tomcat, R.id.test_database,R.id.btn_back, R.id.tv_title})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.test_tomcat:
                testTomcat();
                break;
            case R.id.test_database:
                testDataBase();
                break;
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_title:
                break;
        }
    }

}

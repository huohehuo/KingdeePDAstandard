package com.fangzuo.assist.Activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Server.WebAPI;
import com.fangzuo.assist.Utils.CallBack;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.RetrofitUtil;
import com.fangzuo.assist.databinding.ActivityTestingBinding;
import com.fangzuo.assist.widget.LoadingUtil;
import com.orhanobut.hawk.Hawk;

public class TestingActivity extends AppCompatActivity {

    private ActivityTestingBinding binding;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_testing);
        mContext = this;
        binding.top.tvTitle.setText("网络测试");
        actionListener();
    }

    private void actionListener(){
        binding.top.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.testDatabase.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                binding.tvResult.setText(Hawk.get(Config.Text_Log, ""));
                return false;
            }
        });
        binding.testDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadingUtil.showDialog(mContext, "测试中...");
                App.getRService().getTestDataBase("", new MySubscribe<CommonResponse>() {
                    @Override
                    public void onNext(CommonResponse commonResponse) {
                        LoadingUtil.dismiss();
                        binding.tvResult.setText("服务器测试结果:\r\n" + commonResponse.returnJson);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LoadingUtil.dismiss();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            binding.tvResult.setTextColor(getColor(R.color.red));
                        }
                        binding.tvResult.setText("服务器测试结果:" + e.toString());
                    }
                });
            }
        });
        binding.testTomcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadingUtil.showDialog(mContext, "测试中...");
                final long nowtime = System.currentTimeMillis();
                App.getRService().getTest("", new MySubscribe<CommonResponse>() {
                    @Override
                    public void onNext(CommonResponse commonResponse) {
                        LoadingUtil.dismiss();
                        long endTime = System.currentTimeMillis();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            binding.tvResult.setTextColor(getColor(R.color.green));
                        }
//                        binding.tvResult.setText("结果:获取150kb数据所需时间" + (endTime - nowtime) + "获取数据:" + commonResponse.returnJson);
                        binding.tvResult.setTextSize(18);
                        binding.tvResult.setText("      结果:\n\n      连接TomCat服务器成功~\n      获取150kb数据所需时间" + (endTime - nowtime)+"ms");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LoadingUtil.dismiss();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            binding.tvResult.setTextColor(getColor(R.color.red));
                        }
                        binding.tvResult.setText(e.toString());
                    }
                });
            }
        });
    }


}

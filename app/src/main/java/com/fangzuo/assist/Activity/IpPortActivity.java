package com.fangzuo.assist.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fangzuo.assist.ABase.BaseActivity;
import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.UseTimeBean;
import com.fangzuo.assist.R;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.fangzuo.assist.Utils.BasicShareUtil;
import com.fangzuo.assist.Utils.Config;
import com.fangzuo.assist.Utils.Toast;
import com.fangzuo.assist.Utils.WebApi;
import com.orhanobut.hawk.Hawk;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.fangzuo.assist.Utils.CommonUtil.dealTime;

public class IpPortActivity extends BaseActivity {


    @BindView(R.id.ed_ip)
    EditText edIp;
    @BindView(R.id.ed_port)
    EditText edPort;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.sp_pda)
    Spinner spPda;
    @BindView(R.id.btn_back)
    RelativeLayout btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_endtime)
    TextView tvEndtime;
    private BasicShareUtil share;
    ArrayAdapter<String> adapter;
    private String string;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_ip_port);
        mContext = this;
        ButterKnife.bind(this);
        tvTitle.setText("服务器设置");
        share = BasicShareUtil.getInstance(mContext);
        if (!share.getIP().equals("")) {
            edIp.setText(share.getIP());
        }

        if (!share.getPort().equals("")) {
            edPort.setText(share.getPort());
        }
    }

    @Override
    protected void initData() {
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Config.PDA_Type);
        spPda.setAdapter(adapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                spPda.setSelection(App.PDA_Choose);
            }
        }, 100);
        if (null != Hawk.get(Config.SaveTime, null)) {
            UseTimeBean bean=Hawk.get(Config.SaveTime);
            tvEndtime.setText("有效期：" + dealTime(bean.endTime) + "   用户码：" + Hawk.get(Config.PDA_IMIE, "获取失败")+"  注册码："+Hawk.get(Config.PDA_RegisterCode,"获取失败"));
        } else {
            tvEndtime.setText("获取时间失效" + "   用户码：" + Hawk.get(Config.PDA_IMIE, "获取失败")+"  注册码："+Hawk.get(Config.PDA_RegisterCode,"获取失败"));
        }
    }

    @Override
    protected void initListener() {
        spPda.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                string = adapter.getItem(i);
//                private String[] arr={"G02A设备","8000设备","5000设备"手机端};
                if ("G02A设备".equals(string)) {
                    Hawk.put(Config.PDA, 1);
                    App.PDA_Choose =1;
                    Toast.showText(mContext, "选择了G02A设备" + App.PDA_Choose);
                } else if ("8000设备".equals(string)) {
                    Hawk.put(Config.PDA, 2);
                    App.PDA_Choose =2;
                    Toast.showText(mContext, "选择了8000设备" + App.PDA_Choose);
                } else if ("5000设备".equals(string)) {
                    Hawk.put(Config.PDA, 3);
                    App.PDA_Choose =3;
                    Toast.showText(mContext, "选择了5000设备" + App.PDA_Choose);
                } else if ("M60".equals(string)) {
                    Hawk.put(Config.PDA, 4);
                    App.PDA_Choose =4;
                    Toast.showText(mContext, "选择了M60" + App.PDA_Choose);
                } else if ("新大陆".equals(string)) {
                    Hawk.put(Config.PDA, 5);
                    App.PDA_Choose =5;
                    Toast.showText(mContext, "选择了新大陆" + App.PDA_Choose);
                } else if ("M36".equals(string)) {
                    Hawk.put(Config.PDA, 6);
                    App.PDA_Choose =6;
                    Toast.showText(mContext, "选择了M36" + App.PDA_Choose);
                } else if ("M80s".equals(string)) {
                    Hawk.put(Config.PDA,7);
                    App.PDA_Choose =7;
                    Toast.showText(mContext,"M80s"+App.PDA_Choose);
                }else if ("肖邦".equals(string)) {
                    Hawk.put(Config.PDA, 8);
                    App.PDA_Choose =8;
                    Toast.showText(mContext, "选择了肖邦" + App.PDA_Choose);
                }else if ("手机端".equals(string)) {
                    Hawk.put(Config.PDA, 9);
                    App.PDA_Choose =9;
                    Toast.showText(mContext, "选择了手机端" + App.PDA_Choose);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected void OnReceive(String code) {
        Toast.showText(mContext, code);
    }


    @Override
    protected void onDestroy() {
        App.PDA_Choose = Hawk.get(Config.PDA, 2);
        super.onDestroy();
    }

    @OnClick({R.id.btn_save,R.id.btn_back, R.id.tv_title, R.id.btn_loginout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_title:
                break;
            case R.id.btn_save:
                if (!edPort.getText().toString().equals("") && !edIp.getText().toString().equals("")) {
                    share.setIP(edIp.getText().toString());
                    share.setPort(edPort.getText().toString());
                    finish();
                }
                break;
            case R.id.btn_loginout:
                AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                ab.setTitle("是否登出");
                ab.setMessage("登出后，需重新注册才能使用");
                ab.setPositiveButton("登出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        App.getRService().doIOAction(WebApi.RegisterDelete, Hawk.get(Config.PDA_IMIE,""), new MySubscribe<CommonResponse>() {
                            @Override
                            public void onNext(CommonResponse commonResponse) {
                                super.onNext(commonResponse);
                                if (!commonResponse.state)return;
                                Toast.showText(mContext,"用户已登出");
                            }

                            @Override
                            public void onError(Throwable e) {
//                        super.onError(e);
                                Toast.showText(mContext,"用户登出失败");
                            }
                        });
                    }
                });
                ab.setNegativeButton("取消", null);
                final AlertDialog alertDialog = ab.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
                break;
        }
    }
}

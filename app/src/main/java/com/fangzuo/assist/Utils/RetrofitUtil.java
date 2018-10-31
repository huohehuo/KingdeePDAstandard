package com.fangzuo.assist.Utils;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 王璐阳 on 2017/12/25.
 */

public class RetrofitUtil {

    private static RetrofitUtil mRetrofitUtil;
    private Retrofit mRetrofit;

    private Context mContext;

    private RetrofitUtil(Context context){
        this.mContext = context;
        initRetrofit();
    }

    public static synchronized RetrofitUtil getInstance(Context context){
//        if (mRetrofitUtil == null){
            mRetrofitUtil = new RetrofitUtil(context);
//        }
        return mRetrofitUtil;
    }

    private void initRetrofit() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.retryOnConnectionFailure(true);
        OkHttpClient client = builder.build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BasicShareUtil.getInstance(mContext).getBaseURL())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }


    public <T> T createReq(Class<T> reqServer){
        return mRetrofit.create(reqServer);
    }



    public static Map<String,String> getParams(Context context, String json){
        BasicShareUtil share = BasicShareUtil.getInstance(context);
        Map<String,String> params = new HashMap<>();
        params.put("json",json);
        params.put("sqlip",share.getDatabaseIp());
        params.put("sqlport",share.getDatabasePort());
        params.put("sqluser",share.getDataBaseUser());
        params.put("sqlpass",share.getDataBasePass());
        params.put("sqlname",share.getDataBase());
        params.put("version",share.getVersion());
        return params;
    }


}

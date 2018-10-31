package com.fangzuo.assist.Utils;

import android.content.Context;
import android.util.Log;

import com.fangzuo.assist.Beans.CommonResponse;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

/**
 * Created by syan on 2017/7/21.
 */

public class Asynchttp {

    private static CommonResponse cBean;

    public static void post(Context context,String url, String json, final Response response){
        Log.e("Asynchttp-提交数据：",json+" "+url);
        final AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(10000);
        BasicShareUtil share = BasicShareUtil.getInstance(context);
        RequestParams params = new RequestParams();
        params.add("json",json);
        params.add("sqlip",share.getDatabaseIp());
        params.add("sqlport",share.getDatabasePort());
        params.add("sqluser",share.getDataBaseUser());
        params.add("sqlpass",share.getDataBasePass());
        params.add("sqlname",share.getDataBase());
        params.add("version",share.getVersion());
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                if(!new String(bytes).equals("")&&new String(bytes)!=null){
//                    Log.e("Asynchttp--得到数据：",new String(bytes));

                        Gson gson = new Gson();
                        cBean = gson.fromJson(new String(bytes), CommonResponse.class);
//                        Log.e("cBean.state",cBean.state+"");
                        if(cBean.state){
                            response.onSucceed(cBean,client);
                        }else{
                            response.onFailed(cBean.returnJson,client);
                        }

                }else{
                    response.onFailed("服务器未响应",client);
                }


            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                response.onFailed("网络错误",client);
            }
        });

    }

    public interface Response{
        void onSucceed(CommonResponse cBean, AsyncHttpClient client);
        void onFailed(String Msg, AsyncHttpClient client);
    }
}

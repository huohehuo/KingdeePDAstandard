package com.fangzuo.assist.Utils;

import com.fangzuo.assist.Beans.CommonResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 王璐阳 on 2017/12/27.
 */

public abstract class CallBack implements Callback<CommonResponse> {
    @Override
    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
        if(response.body().state){
            onSucceed(response.body());
        }else{
            OnFail(response.body().returnJson);
        }
    }

    @Override
    public void onFailure(Call<CommonResponse> call, Throwable t) {
        OnFail("网络错误");
    }

    public abstract void onSucceed(CommonResponse cBean);
    public abstract void OnFail(String Msg);
}

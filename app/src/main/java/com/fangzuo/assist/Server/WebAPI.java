package com.fangzuo.assist.Server;

import com.fangzuo.assist.Beans.CommonResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by 王璐阳 on 2017/12/27.
 */

public interface WebAPI {
    @FormUrlEncoded
    @POST("TestServlet")
    Call<CommonResponse> TestTomcat(@FieldMap Map<String,String> params);

    @FormUrlEncoded
    @POST("TestDataBase")
    Call<CommonResponse> TestDataBase(@FieldMap Map<String,String> params);

    @FormUrlEncoded
    @POST("SetPropties")
    Call<CommonResponse> SetProp(@FieldMap Map<String,String> params);

    @FormUrlEncoded
    @POST("DownloadInfo")
    Call<CommonResponse> downloadData(@FieldMap Map<String,String> params);

}

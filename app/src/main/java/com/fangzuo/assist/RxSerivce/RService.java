package com.fangzuo.assist.RxSerivce;


import android.util.Log;

import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.BuildConfig;
import com.fangzuo.assist.Utils.BasicShareUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by aid on 11/10/16.
 */

public class RService {

    private ServiceRequest request;
    private Retrofit retrofit;
    public RService() {
        //当ip地址发生变化时，替换掉原有对象
        if (!App.isChangeIp){
            request = App.getRetrofit().create(ServiceRequest.class);
        }else{
            retrofit= new Retrofit.Builder()
                    .client(App.getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl(BasicShareUtil.getInstance(App.getContext()).getBaseURL())
                    .build();
            request=retrofit.create(ServiceRequest.class);
            App.setRetrofit(retrofit);
        }
    }

    public void getTest(String json, MySubscribe<CommonResponse> mySubscribe) {
        toSubscribe(request.TestTomcat(getParams(json)), mySubscribe);
    }
    public void getTestDataBase(String json, MySubscribe<CommonResponse> mySubscribe) {
        toSubscribe(request.TestDataBase(getParams(json)), mySubscribe);
    }
    public void SetProp(String json, MySubscribe<CommonResponse> mySubscribe) {
        toSubscribe(request.SetProp(getParams(json)), mySubscribe);
    }
    public void connectToSQL(String json, MySubscribe<CommonResponse> mySubscribe) {
        toSubscribe(request.connectToSQL(getParams(json)), mySubscribe);
    }

    public void downloadData(String json, MySubscribe<CommonResponse> mySubscribe) {
        toSubscribe(request.downloadData(getParams(json)), mySubscribe);
    }
    //执行接口
    public void doIOAction(String io, String data,MySubscribe<CommonResponse> mySubscribe) {
        toSubscribe(request.actionIO(io, getParams(data)), mySubscribe);
    }




//    /**
//     * 获取可购买的所有礼物
//     */
//    public void getAllGift(int page, MySubscribe<GetAllGiftResponse> subscribe) {
//        Observable<GetAllGiftResponse> observable = request.getAllGift(User.getSocial(), User.getSocialId(), page);
//        observable = observable.filter(new Func1<GetAllGiftResponse, Boolean>() {
//            @Override
//            public Boolean call(GetAllGiftResponse response) {
//                if ("0".equals(response.getErrno())) {
//                    Collections.sort(response.getData(), new Comparator<GiftBean>() {
//                        @Override
//                        public int compare(GiftBean o1, GiftBean o2) {
//                            return Integer.valueOf(o1.getPrice()).intValue() - Integer.valueOf(o2.getPrice()).intValue();
//                        }
//                    });
//                }
//                return true;
//            }
//        });
//        toSubscribe(observable, subscribe);
//    }
//
//    public void sendGift(String giftId, String roomId, MySubscribe<SendGiftResponse> subscribe) {
//        Observable<SendGiftResponse> observable = request.sendGift(User.getSocial(), User.getSocialId(), giftId, roomId);
//        toSubscribe(observable, subscribe);
//    }
//
//    public void follow(MySubscribe<SimpleResponse> subscribe, String user_id) {
//        Observable<SimpleResponse> observable = request.follow(User.getSocial(), User.getSocialId(), user_id);
//        toSubscribe(observable, subscribe);
//    }
//
//    public void unfollow(MySubscribe<SimpleResponse> subscribe, String user_id) {
//        Observable<SimpleResponse> observable = request.unfollow(User.getSocial(), User.getSocialId(), user_id);
//        toSubscribe(observable, subscribe);
//    }
//
//    public void getUserInfo(String user_id, String page, MySubscribe<UserInfoBean> subscribe) {
//        Observable<UserInfoBean> observable = request.getUserInfo(User.getSocial(), User.getSocialId(), user_id, page);
//        toSubscribe(observable, subscribe);
//    }
//
//    public void withdrawsCash(MySubscribe<SimpleResponse> subscribe, String gift_list, String type, String account) {
//        Observable<SimpleResponse> observable = request.withdraws(User.getSocial(), User.getSocialId(), gift_list, type, account);
//        toSubscribe(observable, subscribe);
//    }
//
//    public void getOrderNum(MySubscribe<OrderInfoBean> subscribe, String product_id) {
//        Observable<OrderInfoBean> observable = request.getOrderNum(User.getSocial(), User.getSocialId(), User.getId(), product_id);
//        toSubscribe(observable, subscribe);
//    }
//
//    public void getVXOrderInfoBean(MySubscribe<WeChatOrderInfoBean> subscribe, String product_id) {
//        Observable<WeChatOrderInfoBean> observable = request.getVXOrderInfoBean(User.getSocial(), User.getSocialId(), User.getId(), product_id);
//        toSubscribe(observable, subscribe);
//
//    }
//
//    public void bindingJPush(MySubscribe<SimpleResponse> subscribe, String jiguangid) {
//        Observable<SimpleResponse> observable = request.bindingJPush(User.getSocial(), User.getSocialId(), jiguangid);
//        toSubscribe(observable, subscribe);
//    }
//
//    public GetRoomPushStatusResponse getRoomPushStatus(String room_id) {
//        try {
//            return request.getRoomPushStatus(User.getSocial(), User.getSocialId(), room_id, User.getId()).execute().body();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public GetRoomPushStatusResponse getRoomPushStatusV2(String room_id) {
//        try {
//            return request.getRoomPushStatusV2(User.getSocial(), User.getSocialId(), room_id, User.getId()).execute().body();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public void closeLiveRoom(String room_id, MySubscribe<CloseRoomBean> mySubscribe) {
//        toSubscribe(request.closeLiveRoom(User.getSocial(), User.getSocialId(), room_id), mySubscribe);
//    }
//
//    public CreateResponse createLiveRoom(String title, String picUrl, String room_id) {
//        try {
//            return request.createLiveRoom(User.getSocial(), User.getSocialId(), title, picUrl, room_id).execute().body();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public CreateResponse createLiveRoomV2(String title, String picUrl) {
//        try {
//            return request.createLiveRoomV2(User.getSocial(), User.getSocialId(), title, picUrl).execute().body();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public void getRankingList(String page, String type, String is_day_rank, MySubscribe<AnchorRankingResponse> mySubscribe) {
//        toSubscribe(request.getRankingList(User.getSocial(), User.getSocialId(), page, type, is_day_rank), mySubscribe);
//    }


    /**
     * retrofit 线程管理
     */
    private static <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

//    public static OkHttpClient getOkHttpClient() {
//        return OkHttpClientHolder.client;
//    }
//
//    private static class OkHttpClientHolder {
//        private static final OkHttpClient client;
//
//        static {
//            OkHttpClient.Builder builder = new OkHttpClient.Builder();
//            if (BuildConfig.DEBUG) {
//                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//                builder.addInterceptor(interceptor);
//            }
//            client = builder.addNetworkInterceptor(new HttpLoggingInterceptor())
//                    .connectTimeout(5, TimeUnit.SECONDS)
//                    .build();
//        }
//
//    }

    private static Map<String,String> getParams(String json){
        BasicShareUtil share = BasicShareUtil.getInstance(App.getContext());
        Map<String,String> params = new HashMap<>();
        params.put("json",json);
        params.put("sqlip",share.getDatabaseIp());
        params.put("sqlport",share.getDatabasePort());
        params.put("sqluser",share.getDataBaseUser());
        params.put("sqlpass",share.getDataBasePass());
        params.put("sqlname",share.getDataBase());
        params.put("version",share.getVersion());
        Log.e("请求：","网络数据："+params.toString());
        return params;
    }
}

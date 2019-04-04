package com.fangzuo.assist.RxSerivce;


import com.fangzuo.assist.Beans.CommonResponse;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by aid on 11/10/16.
 */

interface ServiceRequest {

    @FormUrlEncoded
    @POST("TestServlet")
    Observable<CommonResponse> TestTomcat(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("TestDataBase")
    Observable<CommonResponse> TestDataBase(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("SetPropties")
    Observable<CommonResponse> SetProp(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("ConnectDataBase")
    Observable<CommonResponse> connectToSQL(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("DownloadInfo")
    Observable<CommonResponse> downloadData(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("{actionio}")
    Observable<CommonResponse> actionIO(@Path("actionio") String io , @FieldMap Map<String, String> params);









//    @GET("api/gift/all")
//    Observable<GetAllGiftResponse> getAllGift(
//            @Query("social") String social,
//            @Query("id") String id,
//            @Query("page") int page);
//
//
//    @FormUrlEncoded
//    @POST("api/gift/send")
//    Observable<SendGiftResponse> sendGift(
//            @Field("social") String social,
//            @Field("id") String id,
//            @Field("gift_id") String giftId,
//            @Field("room_id") String roomId);
//
//    @FormUrlEncoded
//    @POST("/api/me/follow")
//    Observable<SimpleResponse> follow(
//            @Field("social") String social,
//            @Field("id") String id,
//            @Field("user_id") String giftId);
//
//    @FormUrlEncoded
//    @POST("/api/me/unfollow")
//    Observable<SimpleResponse> unfollow(
//            @Field("social") String social,
//            @Field("id") String id,
//            @Field("user_id") String giftId);
//
//    @GET("api/user/info")
//    Observable<UserInfoBean> getUserInfo(
//            @Query("social") String social,
//            @Query("id") String id,
//            @Query("user_id") String user_id,
//            @Query("page") String page);
//
//    @FormUrlEncoded
//    @POST("api/user/withdraws-cash")
//    Observable<SimpleResponse> withdraws(
//            @Field("social") String social,
//            @Field("id") String id,
//            @Field("gift_list") String gift_list,
//            @Field("type") String type,
//            @Field("account") String account
//    );
//
//    @FormUrlEncoded
//    @POST("api/user/alicharge")
//    Observable<OrderInfoBean> getOrderNum(
//            @Query("social") String social,
//            @Query("id") String id,
//            @Field("user_id") String user_id,
//            @Field("product_id") String product_id
//    );
//
//    @GET("api/wechat/charge")
//    Observable<WeChatOrderInfoBean> getVXOrderInfoBean(
//            @Query("social") String social,
//            @Query("id") String id,
//            @Query("user_id") String user_id,
//            @Query("product_id") String product_id
//    );
//
//
//    @FormUrlEncoded
//    @POST("api/me/jiguangid")
//    Observable<SimpleResponse> bindingJPush(
//            @Field("social") String social,
//            @Field("id") String id,
//            @Field("jiguangid") String jiguangid
//    );
//
//    @GET("api/room/push-status")
//    Call<GetRoomPushStatusResponse> getRoomPushStatus(
//            @Query("social") String social,
//            @Query("id") String id,
//            @Query("room_id") String room_id,
//            @Query("caster_id") String caster_id
//    );
//
//    // TODO V2
//    @GET("api/v2/room/push-status")
//    Call<GetRoomPushStatusResponse> getRoomPushStatusV2(
//            @Query("social") String social,
//            @Query("id") String id,
//            @Query("room_id") String room_id,
//            @Query("caster_id") String caster_id
//    );
//
//    @FormUrlEncoded
//    @POST("api/room/stop")
//    Observable<CloseRoomBean> closeLiveRoom(
//            @Field("social") String social,
//            @Field("id") String id,
//            @Field("room_id") String room_id
//    );
//
//    @FormUrlEncoded
//    @POST("api/room/start")
//    Call<CreateResponse> createLiveRoom(
//            @Field("social") String social,
//            @Field("id") String id,
//            @Field("title") String title,
//            @Field("preview_pic") String preview_pic,
//            @Field("room_id") String room_id
//    );
//
//    @FormUrlEncoded
//    @POST("api/v2/room/start")
//    Call<CreateResponse> createLiveRoomV2(
//            @Field("social") String social,
//            @Field("id") String id,
//            @Field("title") String title,
//            @Field("preview_pic") String preview_pic
//    );
//
//    @GET("api/room/ranking")
//    Observable<AnchorRankingResponse> getRankingList(
//            @Query("social") String social,
//            @Query("id") String id,
//            @Query("page") String page,
//            @Query("type") String type,
//            @Query("is_day_rank") String is_day_rank
//    );
//
//    @GET("api/gift/recv")
//    Observable<MyGiftBean> getMygifts(
//            @Query("social") String social,
//            @Query("id") String id,
//            @Query("user_id") String user_id,
//            @Query("page") String page,
//            @Query("type") String type
//    );
//
//    @GET("/api/gift/send")
//    Observable<GetMyGiftsResponse> getMySendGifts(
//            @Query("social") String social,
//            @Query("id") String id,
//            @Query("gift_id") String user_id,
//            @Query("room_id") String page
//    );
//
//    @GET("api/room/random_list")
//    Observable<RandomListResponse> getRoomList(
//            @Query("social") String social,
//            @Query("id") String id,
//            @Query("type") String type
//    );
//
//    @GET("api/room/status")
//    Observable<RoomRealTimeStatusResponse> getRoomStatus(
//            @Query("social") String social,
//            @Query("id") String id,
//            @Query("room_id") String room_id
//    );
//
//    @GET("api/me")
//    Observable<MeResponseBean> getMyInfo(
//            @Query("social") String social,
//            @Query("id") String id
//    );
//
//    @GET("api/room/join")
//    Observable<JoinLiveRoomResponse> joinRoom(
//            @Query("social") String social,
//            @Query("id") String id,
//            @Query("room_id") String room_id
//    );
//
//    @GET("api/v2/room/join")
//    Observable<JoinLiveRoomResponse> joinRoomV2(
//            @Query("social") String social,
//            @Query("id") String id,
//            @Query("room_id") String room_id
//    );
//
//    @FormUrlEncoded
//    @POST("api/room/leave")
//    Observable<SimpleResponse> leaveRoom(
//            @Field("social") String social,
//            @Field("id") String id,
//            @Field("room_id") String room_id
//    );
//
//    @FormUrlEncoded
//    @POST("api/room/keep-alive")
//    Observable<SimpleResponse> keepAlive(
//            @Field("social") String social,
//            @Field("id") String id,
//            @Field("room_id") String room_id,
//            @Field("type") String type
//    );
//
//    @FormUrlEncoded
//    @POST("api/room/preview")
//    Observable<SimpleResponse> sendPreviewPic(
//            @Field("social") String social,
//            @Field("id") String id,
//            @Field("room_id") String room_id,
//            @Field("preview_pic") String preview_pic
//    );
//
//    @FormUrlEncoded
//    @POST("api/login")
//    Observable<UserLoginResponse> login(
//            @Field("social") String social,
//            @Field("id") String id,
//            @Field("id_str") String id_str,
//            @Field("openid") String openid,
//            @Field("nickname") String nickname,
//            @Field("name") String name,
//            @Field("screen_name") String screen_name,
//            @Field("avatar") String avatar,
//            @Field("profile_image_url") String profile_image_url,
//            @Field("headimgurl") String headimgurl,
//            @Field("location") String location
//    );
//
//    @FormUrlEncoded
//    @POST("api/login")
//    Observable<UserLoginResponse> signIn(
//            @Field("social") String social,
//            @Field("username") String username,
//            @Field("password") String password,
//            @Field("phone") String phone,
//            @Field("avatar") String avatar,
//            @Field("headimgurl") String headimgurl,
//            @Field("profile_image_url") String profile_image_url,
//            @Field("is_register") String is_register,
//            @Field("location") String location
//    );
//
//    @GET("api/carousel")
//    Observable<CarouselBean> getCarouselImage(
//    );
//
//    @GET("/api/comment")
//    Observable<VideoCommentBean> getVideoComment(
//            @Query("social") String social,
//            @Query("id") String id,
//            @Query("type") String type,
//            @Query("target_id") String target_id
//    );
//
//    @FormUrlEncoded
//    @POST("/api/comment")
//    Observable<SimpleResponse> postComment(
//            @Field("social") String social,
//            @Field("id") String id,
//            @Field("type") String type,
//            @Field("target_id") String target_id,
//            @Field("body") String body,
//            @Field("reply_id") String reply_id
//    );
//
//    @FormUrlEncoded
//    @POST("/api/room/start-and")
//    Observable<PushUrlBean> getPushUrl(
//            @Field("social") String social,
//            @Field("id") String id
//    );
//
//    @GET("/api/search/result")
//    Observable<HomeSearchResultResponse> homeSearch(
//            @Query("social") String social,
//            @Query("id") String id,
//            @Query("keyword") String keyword
//    );
//
//    @GET("/api/search/recommend")
//    Observable<HomeSearchRecommendResponse> getRecommend(
//            @Query("social") String social,
//            @Query("id") String id
//    );
//
//    @GET("/api/circle/friends")
//    Observable<FriendsBean> getFriends(
//            @Query("social") String social,
//            @Query("id") String id
//    );
//
//    @GET("/api/college/class")
//    Observable<CollegeBean> getCollege(
//            @Query("social") String social,
//            @Query("id") String id,
//            @Query("type") String type
//    );
//
//    @POST("/api/me/edit")
//    Observable<SimpleResponse> postEdit(
//            @Query("social") String social,
//            @Query("id") String id,
//            @Query("name") String name,
//            @Query("sex") String sex,
//            @Query("location") String location,
//            @Query("motto") String motto,
//            @Query("avatar") String avatar,
//            @Query("constellation") String constellation
//    );
//
//    @GET("/api/me/get_editable")
//    Observable<EditableBean> getEditable(
//            @Query("social") String social,
//            @Query("id") String id
//    );
//
//    @GET("/api/circle/zone")
//    Observable<MomentBean> getMoments(
//            @Query("social") String social,
//            @Query("id") String id
//    );
//
//    @GET("/api/college/classified")
//    Observable<CollegeTypeBean> getCollegeType(
//            @Query("social") String social,
//            @Query("id") String id
//    );
//
//    @GET("/api/college/search")
//    Observable<CollegeSearchBean> getCollegeSearch(
//            @Query("social") String social,
//            @Query("id") String id,
//            @Query("keyword") String keyword,
//            @Query("type") String type
//    );
//
//    @FormUrlEncoded
//    @POST("/api/circle/post_moment")
//    Observable<SimpleResponse> publicMoment(
//            @Field("social") String social,
//            @Field("id") String id,
//            @Field("text") String text,
//            @Field("images") String images
//    );
//
//    @POST("api/certify/person")
//    Observable<SimpleResponse> personCertify(
//            @Query("social") String social,
//            @Query("id") String id,
//            @Query("realname") String realname,
//            @Query("phonenum") String phonenum,
//            @Query("identitytab") String identitytab,
//            @Query("industrytype") String industrytype,
//            @Query("idcardwithperson") String idcardwithperson,
//            @Query("idcard") String idcard,
//            @Query("businesscard") String businesscard
//    );
//
//    @POST("api/certify/company")
//    Observable<SimpleResponse> companyCertify(
//            @Query("social") String social,
//            @Query("id") String id,
//            @Query("companyName") String companyName,
//            @Query("companyAddress") String companyAddress,
//            @Query("companyUrl") String companyUrl,
//            @Query("companyTerritory") String companyTerritory,
//            @Query("managerName") String managerName,
//            @Query("companyPhone") String companyPhone,
//            @Query("photoFront") String photoFront,
//            @Query("backFront") String backFront
//    );
//
//    @GET("api/circle/comment")
//    Observable<SimpleResponse> momentComment(
//            @Query("social") String social,
//            @Query("id") String id,
//            @Query("zone_id") String zone_id,
//            @Query("content") String content
//    );
//
//    @GET("/api/circle/thumbs")
//    Observable<SimpleResponse> momentThumbs(
//            @Query("social") String social,
//            @Query("id") String id,
//            @Query("zone_id") String zone_id
//    );
//
//    @GET("api/certify/person/info")
//    Observable<PersonCertifyBean> getPersonCertifyInfo(
//            @Query("social") String social,
//            @Query("id") String socialid
//    );
//
//    @GET("api/certify/company/info")
//    Observable<CompanyCertifyBean> getCompanyCertifyInfo(
//            @Query("social") String social,
//            @Query("id") String socialid
//    );
//
//    @GET("/api/me/release")
//    Observable<MomentBean> getMyRelease(
//            @Query("social") String social,
//            @Query("id") String socialid
//    );
//
//    @GET("/api/me/chargerecord")
//    Observable<ChargeRecordBean> getChargerecord(
//            @Query("social") String social,
//            @Query("id") String socialid,
//            @Query("page") String page,
//            @Query("type") String type
//    );
//
//    @GET("/api/me/balance")
//    Observable<BalanceBean> getBalance(
//            @Query("social") String social,
//            @Query("id") String socialid
//    );
//
//    @POST("/api/me/exchange")
//    Observable<BalanceBean> exchanage(
//            @Query("social") String social,
//            @Query("id") String socialid,
//            @Query("num_d") String numd
//    );
//
//    @POST("/api/me/withdraw")
//    Observable<SimpleResponse> withdraw(
//            @Query("social") String social,
//            @Query("id") String socialid,
//            @Query("num_d") String numd,
//            @Query("account") String account,
//            @Query("name") String name
//    );
//
//    @GET("/api/me/black_list")
//    Observable<BlackListBean> getBlackList(
//            @Query("social") String social,
//            @Query("id") String socialid
//    );
//
//    @GET("/api/me/withdrawrecord")
//    Observable<WithDrawRecordBean> getWithdrawRecord(
//            @Query("social") String social,
//            @Query("id") String socialid
//    );
//
//    @GET("/api/room/recommend")
//    Observable<RecommendBean> getLiveRecommend(
//            @Query("social") String social,
//            @Query("id") String socialId,
//            @Query("room_id") String roomId
//    );
//
//    @FormUrlEncoded
//    @POST("/api/room/black_user ")
//    Observable<SimpleResponse> addToBlackList(
//            @Field("social") String social,
//            @Field("id") String id,
//            @Field("user_id") String user_id
//    );
//
//    @FormUrlEncoded
//    @POST("/api/room/report_user")
//    Observable<SimpleResponse> reportUser(
//            @Field("social") String social,
//            @Field("id") String id,
//            @Field("user_id") String user_id
//    );
//
//    @GET("/api/college/video_detail")
//    Observable<VideoDetailBean> getVideoDetail(
//            @Query("social") String social,
//            @Query("id") String socialId,
//            @Query("roomId") String roomId
//    );
//
//    @FormUrlEncoded
//    @POST("/api/comment/praise")
//    Observable<SimpleResponse> videoCommentThumbs(
//            @Field("social") String social,
//            @Field("id") String socialId,
//            @Field("comment_id") String comment_id
//    );
//
//    @GET("/api/me/withdrawrate")
//    Observable<WithdrawRateBean> getWithdrawRate(
//            @Query("social") String social,
//            @Query("id") String socialId
//    );
//
//    @GET("/api/me/remove_black_list")
//    Observable<SimpleResponse> removeBlackList(
//            @Query("social") String social,
//            @Query("id") String id,
//            @Query("bid") String bid
//    );
//
//    @FormUrlEncoded
//    @POST("/api/room/chat")
//    Observable<SimpleResponse> chatCount(
//            @Field("social") String social,
//            @Field("id") String socialId,
//            @Field("room_id") String room_id
//    );
//
//    @GET("/api/front/room/status")
//    Observable<ShowStatusBean> getShowStatus(
//            @Query("social") String social,
//            @Query("id") String id,
//            @Query("room_id") String roomId
//    );
//
//    @GET("/api/certify/state")
//    Observable<CertifyBean> getCertifyState(
//            @Query("social") String social,
//            @Query("id") String id
//    );
//
//    @FormUrlEncoded
//    @POST("/api/user/forgetpwd")
//    Observable<SimpleResponse> forgetPwd(
//            @Field("phone") String phone,
//            @Field("password") String password
//    );
}


package com.fangzuo.assist.Utils;

import android.content.Context;

import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.Beans.EventBusEvent.ClassEvent;
import com.fangzuo.assist.RxSerivce.MySubscribe;
import com.orhanobut.hawk.Hawk;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

/**
 * 用于管理注册逻辑类
 */
public class RegisterUtil {

    //获取本机Mac地址
    public static String getNewMac(){
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
        return "";
    }

    //获取服务器最大注册数
    public static void getRegiterMaxNum(final String lastRegister){
        App.getRService().doIOAction(WebApi.RegisterGetNum, "", new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                super.onNext(commonResponse);
                if (!commonResponse.state)return;
                Hawk.put(Config.PDA_RegisterMaxNum,commonResponse.returnJson);
                doRegisterCheck(lastRegister);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Register_Result,"获取软件使用数量失败"));
            }
        });
    }
    //执行注册逻辑
    //1:检查是否超过用户数
    //2：检查是否存在用户
    //3：不存在进行注册
    public static void doRegisterCheck(final String lastRegister){
        //查询出注册表包含的用户数量
        App.getRService().doIOAction(WebApi.RegisterNumber, "获取用户数number", new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
                super.onNext(commonResponse);
                if (!commonResponse.state) return;
                Lg.e("注册信息数量：", commonResponse.returnJson);
                if (Integer.parseInt(commonResponse.returnJson) <=  Integer.parseInt(Hawk.get(Config.PDA_RegisterMaxNum,"1"))) {
                    Lg.e("符合用户注册最低数量");
                    App.getRService().doIOAction(WebApi.RegisterCheck, lastRegister, new MySubscribe<CommonResponse>() {
                        @Override
                        public void onNext(CommonResponse commonResponse) {
                            super.onNext(commonResponse);
                            if (!commonResponse.state)return;
                            if (commonResponse.returnJson.equals("OK")){
                                Lg.e("存在注册码");
                                //存在注册码
                                EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Register_Result,"OK"));
                            }else{
                                //不存在注册码，进行注册
                                Lg.e("不存在注册码");
                                App.getRService().doIOAction(WebApi.RegisterCode, lastRegister, new MySubscribe<CommonResponse>() {
                                    @Override
                                    public void onNext(CommonResponse commonResponse) {
                                        super.onNext(commonResponse);
                                        if (!commonResponse.state) return;//注册成功
                                        EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Register_Result,"OK"));
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        super.onError(e);
                                        EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Register_Result,"注册失败"));
//                                        LoadingUtil.showAlter(WelcomeActivity.this, "提示", "注册失败");
                                    }
                                });
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
//                            super.onError(e);
                            EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Register_Result,"查询用户错误"+e.getMessage()));
                        }
                    });

                } else {
                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Register_Result,"软件使用数量已达上限："+Hawk.get(Config.PDA_RegisterMaxNum,"1")));
//                    LoadingUtil.showAlter(WelcomeActivity.this, "提示", "软件使用数量已达上限");
                }
            }

            @Override
            public void onError(Throwable e) {
//                super.onError(e);
                EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Register_Result,"查询用户数错误："+e.getMessage()));
//                Toast.showText(WelcomeActivity.this,"查询用户数错误："+e.getMessage());
            }
        });
    }

    //检查dbother.db文件是否存在注册码
    public static void checkHasRegister(){
        //检查是否存在注册码
        if ("".equals(Hawk.get(Config.PDA_IMIE,"")))return;
        App.getRService().doIOAction(WebApi.RegisterCheck,  Hawk.get(Config.PDA_IMIE,""), new MySubscribe<CommonResponse>() {
            @Override
            public void onNext(CommonResponse commonResponse) {
//                super.onNext(commonResponse);
                if (commonResponse.returnJson.equals("OK")){
                    Lg.e("存在注册码");
                    //存在注册码
                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Register_Result,"OK"));
                }else{
                    Lg.e("不存在注册码");
                    EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Register_Result,"软件未注册,是否重新注册"));
                }
            }

            @Override
            public void onError(Throwable e) {
//                super.onError(e);
                EventBusUtil.sendEvent(new ClassEvent(EventBusInfoCode.Register_Result,"查询注册信息错误"));

            }
        });
    }


}




















/*
String str = "abc,12,3yy98,0";
String[]  strs=str.split(",");//以，截取   或者\\^
for(int i=0,len=strs.length;i<len;i++){
    System.out.println(strs[i].toString());



 sb.substring(2);//索引号 2 到结尾

String barcode = code.substring(0, 12);//第一位到第十一位

3.通过StringUtils提供的方法
StringUtils.substringBefore(“dskeabcee”, “e”);
/结果是：dsk/
这里是以第一个”e”，为标准。

StringUtils.substringBeforeLast(“dskeabcee”, “e”)
结果为：dskeabce
这里以最后一个“e”为准。
* */
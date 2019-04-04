package com.fangzuo.assist.RxSerivce;

import android.widget.Toast;

import com.fangzuo.assist.Activity.Crash.App;
import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.widget.LoadingUtil;

import java.net.ConnectException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;


/**
 * Created by Hmei on 16/11/17.
 */

public abstract class MySubscribe<T extends CommonResponse> extends Subscriber<T> {

    @Override
    public void onNext(T t) {
        if (!t.state){
            onError(new Throwable(t.returnJson));
            LoadingUtil.dismiss();
        }
//        if ("1".equals(t.getErrno())) {
//            onError(new Throwable(t.getMsg()));
//        }
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof ConnectException | e instanceof HttpException) {
            Toast.makeText(App.getContext(), "网络异常，请检查网络后重试", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(App.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//            Logger.e("MySubscribe Error" + "++++" + e.toString());
        }
    }
}

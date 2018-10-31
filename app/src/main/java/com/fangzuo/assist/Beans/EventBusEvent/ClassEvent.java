package com.fangzuo.assist.Beans.EventBusEvent;

/**
 * Created by 王璐阳 on 2017/11/30.
 */

public class ClassEvent {
    public final String Msg;
    public final String Msg2;
    public final String Msg3;
    public final Object postEvent;

    public ClassEvent(String msg,String msg2,String msg3, Object postEvent) {
        Msg = msg;
        Msg2 = msg2;
        Msg3 = msg3;
        this.postEvent = postEvent;
    }
    public ClassEvent(String msg,String msg2 ,Object postEvent) {
        Msg = msg;
        Msg2 = msg2;
        Msg3 = "";
        this.postEvent = postEvent;
    }

    public ClassEvent(String msg ,Object postEvent) {
        Msg = msg;
        Msg2 = "";
        Msg3 = "";
        this.postEvent = postEvent;
    }
}

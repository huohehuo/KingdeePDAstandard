package com.fangzuo.assist.MVContract.view;

import com.fangzuo.assist.Beans.CommonResponse;
import com.fangzuo.assist.MVContract.base.IView;

public interface TestView extends IView {

    void getBackData(CommonResponse commonResponse, String type);
    void showError(String error, String type);
}

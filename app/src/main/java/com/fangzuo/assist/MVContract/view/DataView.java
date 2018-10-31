package com.fangzuo.assist.MVContract.view;

import com.fangzuo.assist.MVContract.base.IView;

public interface DataView extends IView {

    void getBackData(Object object, int type);
    void showError(String error, int type);
}

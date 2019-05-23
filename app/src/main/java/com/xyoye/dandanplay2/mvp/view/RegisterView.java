package com.xyoye.dandanplay2.mvp.view;

import android.content.Context;

import com.xyoye.dandanplay2.utils.interf.view.BaseMvpView;
import com.xyoye.dandanplay2.utils.interf.view.LoadDataView;

/**
 * Created by YE on 2018/8/5.
 */


public interface RegisterView extends BaseMvpView, LoadDataView {
    Context getRegisterContext();

    void registerSuccess();
}
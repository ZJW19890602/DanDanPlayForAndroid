package com.xyoye.dandanplay.mvp.impl;

import android.os.Bundle;

import com.xyoye.dandanplay.base.BaseMvpPresenterImpl;
import com.xyoye.dandanplay.mvp.presenter.PersonalInfoPresenter;
import com.xyoye.dandanplay.mvp.view.PersonalInfoView;
import com.xyoye.dandanplay.utils.Lifeful;

/**
 * Created by YE on 2018/7/23.
 */


public class PersonalInfoPresenterImpl extends BaseMvpPresenterImpl<PersonalInfoView> implements PersonalInfoPresenter {

    public PersonalInfoPresenterImpl(PersonalInfoView view, Lifeful lifeful) {
        super(view, lifeful);
    }

    @Override
    public void init() {

    }

    @Override
    public void process(Bundle savedInstanceState) {

    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }
}

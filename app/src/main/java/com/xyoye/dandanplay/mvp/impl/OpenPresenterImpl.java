package com.xyoye.dandanplay.mvp.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.blankj.utilcode.util.ToastUtils;
import com.xyoye.core.base.BaseMvpPresenter;
import com.xyoye.core.db.DataBaseInfo;
import com.xyoye.core.db.DataBaseManager;
import com.xyoye.core.rx.Lifeful;
import com.xyoye.core.utils.KeyUtil;
import com.xyoye.core.utils.TLog;
import com.xyoye.dandanplay.bean.BannerBeans;
import com.xyoye.dandanplay.bean.PersonalBean;
import com.xyoye.dandanplay.bean.params.LoginParam;
import com.xyoye.dandanplay.mvp.presenter.OpenPresenter;
import com.xyoye.dandanplay.mvp.view.OpenView;
import com.xyoye.dandanplay.net.CommJsonObserver;
import com.xyoye.dandanplay.net.NetworkConsumer;
import com.xyoye.dandanplay.utils.TokenShare;
import com.xyoye.dandanplay.utils.UserInfoShare;

import java.util.List;

/**
 * Created by YE on 2018/7/15.
 */

public class OpenPresenterImpl extends BaseMvpPresenter<OpenView> implements OpenPresenter {

    public OpenPresenterImpl(OpenView view, Lifeful lifeful) {
        super(view, lifeful);
    }

    @Override
    public void init() {
        getView().setLastLogin(UserInfoShare.getInstance().isLogin());
        getData();
        reToken();
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

    private void getData(){
        BannerBeans.getBanner(new CommJsonObserver<BannerBeans>() {
            @Override
            public void onSuccess(BannerBeans bannerBean) {
                List<BannerBeans.BannersBean> beans = bannerBean.getBanners();
                SQLiteDatabase sqLiteDatabase = DataBaseManager.getInstance().getSQLiteDatabase();
                sqLiteDatabase.delete(DataBaseInfo.getTableNames()[3],"", new String[]{});
                for(BannerBeans.BannersBean bean : beans ){
                    ContentValues values=new ContentValues();
                    values.put(DataBaseInfo.getFieldNames()[3][1], bean.getTitle());
                    values.put(DataBaseInfo.getFieldNames()[3][2], bean.getDescription());
                    values.put(DataBaseInfo.getFieldNames()[3][3], bean.getUrl());
                    values.put(DataBaseInfo.getFieldNames()[3][4], bean.getImageUrl());
                    sqLiteDatabase.insert(DataBaseInfo.getTableNames()[3],null,values);
                }
            }

            @Override
            public void onError(int errorCode, String message) {
                TLog.e(message);
                ToastUtils.showShort(message);
            }
        }, new NetworkConsumer());
    }

    private void reToken(){
        PersonalBean.reToken(new CommJsonObserver<PersonalBean>() {
            @Override
            public void onSuccess(PersonalBean personalBean) {
                UserInfoShare.getInstance().setLogin(true);
                UserInfoShare.getInstance().saveUserScreenName(personalBean.getScreenName());
                UserInfoShare.getInstance().saveUserImage(personalBean.getProfileImage());
                TokenShare.getInstance().saveToken(personalBean.getToken());
                getView().loginSuccess();
            }

            @Override
            public void onError(int errorCode, String message) {
                UserInfoShare.getInstance().setLogin(false);
                UserInfoShare.getInstance().saveUserName("");
                UserInfoShare.getInstance().saveUserImage("");
                UserInfoShare.getInstance().saveUserScreenName("");
                TokenShare.getInstance().saveToken("");
                TLog.e(message);
            }
        }, new NetworkConsumer());
    }
}

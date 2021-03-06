package com.player.commom.listener;

import android.content.res.Configuration;

import com.player.commom.bean.SubtitleBean;

import java.util.List;

/**
 * Created by xyoye on 2019/4/26.
 */

public interface PlayerViewListener {

    void onResume();

    void onPause();

    void onDestroy();

    boolean handleVolumeKey(int keyCode);

    boolean onBackPressed();

    void configurationChanged(Configuration configuration);

    void setBatteryChanged(int status, int progress);

    void setSubtitlePath(String subtitlePath);

    void onSubtitleQuery(int size);

    void onScreenLocked();
}

package com.player.commom.utils;

import android.content.res.Configuration;

/**
 * Created by xyoye on 2019/4/26.
 */

public interface PlayerViewListener {

    void onResume();

    void onPause();

    long onDestroy();

    boolean handleVolumeKey(int keyCode);

    boolean onBackPressed();

    void configurationChanged(Configuration configuration);

    void setBatteryChanged(int status, int progress);

    void onScreenLocked();
}

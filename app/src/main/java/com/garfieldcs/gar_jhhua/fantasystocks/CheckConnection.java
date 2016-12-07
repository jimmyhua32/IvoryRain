package com.garfieldcs.gar_jhhua.fantasystocks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by gar_jhhua on 12/7/2016.
 */
public class CheckConnection {
    private ConnectivityManager cManager;
    private NetworkInfo networkInfo;

    public CheckConnection(Context c) {
        cManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = cManager.getActiveNetworkInfo();
    }

    public boolean isConnected() {
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}

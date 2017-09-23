package com.furq.popularmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by syedasarafurqan on 23/09/2017.
 */

public class Network {

    /**
     * Get the network info
     *
     * @param context
     * @return
     */
    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo();
    }

    /**
     * Check if there is any internet connection
     * @param context
     * @return true/false
     */
    public static boolean isConnected(Context context) {
        NetworkInfo info = Network.getNetworkInfo(context);
        return (info != null && info.isConnected());
    }
}

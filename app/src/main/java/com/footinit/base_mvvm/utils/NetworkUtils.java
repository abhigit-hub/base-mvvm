package com.footinit.base_mvvm.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.footinit.base_mvvm.di.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class NetworkUtils {

    private Context context;

    public NetworkUtils(@ApplicationContext Context context) {
        this.context = context;
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}

package com.v2soft.apsf.shared;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetWorkAvailable {

    private static NetWorkAvailable instance = null;

    public static Boolean bOnline;

    static Context m_context;

    public static NetWorkAvailable getInstance(Context context) {
        if (instance == null) {
            if (context == null) {
                throw new IllegalStateException(NetWorkAvailable.class.getSimpleName() +
                        " is not initialized, call getInstance(Context) with a VALID Context first.");
            }
            m_context = context;
            instance = new NetWorkAvailable(context.getApplicationContext());
        }
        return instance;
    }

    public NetWorkAvailable(Context parent) {
        m_context = parent;
        bOnline = isConnectingToInternet();
    }

    public static boolean getNetWorkStatus() {
        return isConnectingToInternet();
    }

    public static boolean isConnectingToInternet() {
        if (m_context != null) {
            ConnectivityManager connectivity = (ConnectivityManager) m_context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
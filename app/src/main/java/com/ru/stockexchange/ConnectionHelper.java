package com.ru.stockexchange;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionHelper {
    private static ConnectionHelper instance;
    private ConnectivityManager connectivityManager;
    public boolean isConnected;

    public static ConnectionHelper getInstance(Context context){
        if (instance == null) {
            instance = new ConnectionHelper(context);
        }
        return instance;
    }
    private ConnectionHelper(Context context){
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    };
    public boolean isConnected() {
        NetworkInfo wifiConnection = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConnection = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return wifiConnection != null && wifiConnection.isConnected() ||
                mobileConnection != null && mobileConnection.isConnected();
    }
}

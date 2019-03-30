package io.comet.Activity;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import io.comet.Listener.NetworkBroadcast;
import io.comet.Listener.UserBroadcast;
import io.comet.Utils.Singleton;

public class BaseActivity extends AppCompatActivity {
    BaseActivity instance = null;
    Context mContext;

    private IntentFilter networkFilter = null;
    private NetworkBroadcast networkReceiver = null;
    private IntentFilter userBroadcastFilter = null;
    private UserBroadcast userBroadcastReceiver = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mContext = this;
        instance = this;

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        networkFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        networkReceiver = new NetworkBroadcast(this);
        userBroadcastFilter = new IntentFilter(Singleton.USER_BROADCAST_ACTION);
        userBroadcastReceiver = new UserBroadcast(this);

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        registerReceiver(networkReceiver, networkFilter);
        registerReceiver(userBroadcastReceiver, userBroadcastFilter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkReceiver);
        unregisterReceiver(userBroadcastReceiver);
        super.onStop();
    }
}

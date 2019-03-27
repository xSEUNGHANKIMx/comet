package io.comet.Activity;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import io.comet.Listener.NetworkBroadcast;

abstract  public class BaseActivity extends AppCompatActivity {
    int viewId;
    int toolbarId;
    abstract  void onCreate();

    BaseActivity instance = null;
    Context mContext;

    private IntentFilter networkFilter = null;
    private NetworkBroadcast networkReceiver = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mContext = this;
        instance = this;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(viewId);
        if (toolbarId != 0) {
            Toolbar toolbar = findViewById(toolbarId);
            setSupportActionBar(toolbar);
        }
        onCreate();
        networkFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        networkReceiver = new NetworkBroadcast(this);

        super.onCreate(savedInstanceState);
    }
}

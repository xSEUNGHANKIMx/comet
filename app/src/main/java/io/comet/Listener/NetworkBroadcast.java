package io.comet.Listener;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import io.comet.R;
import io.comet.Utils.AlertConfirmExecute;
import io.comet.Utils.NetworkUtil;
import io.comet.Utils.Util;

public class NetworkBroadcast extends BroadcastReceiver {
    private Activity activity;

    public NetworkBroadcast(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (action == ConnectivityManager.CONNECTIVITY_ACTION) {
                if (context != null) {
                    connectDialog(context);
                }
            }
        }
    }

    private void connectDialog(Context context) {
        if (!NetworkUtil.isNetConnected(context)) {
                Util.alert(context, R.string.err_connection, R.string.err_connection, new AlertConfirmExecute() {
                    @Override
                    public void execute() {

                    }
                });
        }
    }
}

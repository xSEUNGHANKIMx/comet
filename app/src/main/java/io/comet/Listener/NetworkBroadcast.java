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
        String title, message;

        if (NetworkUtil.isNetConnected(context)) {
            title = context.getString(R.string.all_success);
            message = context.getString(R.string.network_connected);
        } else {
            title = context.getString(R.string.all_sorry);
            message = context.getString(R.string.network_not_connected);
        }

        Util.alert(context, title, message, new AlertConfirmExecute() {
            @Override
            public void execute() {

            }
        });
    }
}

package io.comet.Listener;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import io.comet.R;
import io.comet.Utils.AlertConfirmExecute;
import io.comet.Utils.Singleton;
import io.comet.Utils.Util;

public class UserBroadcast extends BroadcastReceiver {
    private Activity activity;

    public UserBroadcast(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction() == Singleton.USER_BROADCAST_ACTION) {
            if(context != null) {
                String title = context.getString(R.string.all_success);
                String desc = context.getString(R.string.user_broadcast_received);
                Util.alert(context, title, desc, new AlertConfirmExecute() {
                    @Override
                    public void execute() {
                    }
                });
            }
        }
    }
}

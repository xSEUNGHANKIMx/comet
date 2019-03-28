package io.comet.Utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import com.afollestad.materialdialogs.MaterialDialog;

import io.comet.Activity.MainActivity;
import io.comet.R;

public class Util {
    private static MaterialDialog mDlg;

    public static void alert(Context context, int title, int message ) {
        try {
            MainActivity activity = (MainActivity) context;
            if(activity.getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
        }

        if(mDlg != null && mDlg.isShowing()) return;
        mDlg = new MaterialDialog.Builder(context)
                .title(context.getString(title))
                .content(context.getString(message))
                .positiveText(context.getString(R.string.all_confirm))
                .negativeText(null)
                .show();
    }
}

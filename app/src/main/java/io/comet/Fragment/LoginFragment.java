package io.comet.Fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.comet.Activity.MainActivity;
import io.comet.Model.AccessToken;
import io.comet.R;
import io.comet.Utils.AlertConfirmExecute;
import io.comet.Utils.Singleton;
import io.comet.Utils.Util;
import retrofit2.Response;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;

public class LoginFragment extends Fragment {
    private Context mContext;
    private MainActivity mActivity;
    private Button loginBtn;
    private String email, password;
    private ExtendedEditText emailEdit, pwdEdit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        mActivity = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        emailEdit = view.findViewById(R.id.emailEdt);
        pwdEdit = view.findViewById(R.id.pwdEdt);
        loginBtn = view.findViewById(R.id.loginBtn);

        emailEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                email = s.toString();
                loginBtn.setEnabled(isAvailable());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        pwdEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                password = s.toString();
                loginBtn.setEnabled(isAvailable());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DoInBackground().execute("");
            }
        });

        return view;
    }

    private boolean checkEmail(String email) {
        if (null != email) {
            String regex = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        }
        return false;
    }

    private boolean checkPassword(String pwd) {
        if (null != pwd) {
            return pwd.length() >= 8;
        }
        return false;
    }

    private boolean isAvailable() {
        return checkEmail(email) && checkPassword(password);
    }

    private void goToLogoutFragment() {
        mActivity.replaceFragment(mActivity.FRAGMENT_ID_LOGOUT);
    }

    private class DoInBackground extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... strings) {
            int ret = 0;
            try {
                Response<AccessToken> response = Singleton.getInstance().loginRetrofit().login(email, password).execute();
                if (response != null) {
                    ret = response.code();
                    if (ret == 200) {
                        AccessToken token = response.body();
                        Singleton.getInstance().setToken(token.aToken, token.rToken);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ret;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            final int rspCode = integer;
            String title, message;

            switch (rspCode) {
                case 200: // OK
                    title = mContext.getString(R.string.all_success);
                    message = mContext.getString(R.string.login_success_msg);
                    break;
                case 500: // Already linked with account
                    title = mContext.getString(R.string.all_sorry);
                    message = mContext.getString(R.string.login_fail_already_logged_in);
                    break;
                case 501: // Wrong auth code
                    title = mContext.getString(R.string.all_sorry);
                    message = mContext.getString(R.string.login_fail_wrong_auth_code);
                    break;
                case 502: // email or password is wrong
                case 503:
                case 504:
                case 505:
                case 506:
                    title = mContext.getString(R.string.all_sorry);
                    message = mContext.getString(R.string.login_fail_check_id_pwd);
                    break;
                case 507: // Not found token
                    title = mContext.getString(R.string.all_sorry);
                    message = mContext.getString(R.string.login_fail_not_found_token);
                    break;
                default:
                    title = mContext.getString(R.string.all_sorry);
                    message = mContext.getString(R.string.login_fail_unknown);
                    break;
            }

            Util.alert(mContext, title, message, new AlertConfirmExecute() {
                @Override
                public void execute() {
                    if (rspCode == 200 || rspCode == 500) {
                        goToLogoutFragment();
                    }
                }
            });
            super.onPostExecute(integer);
        }
    }
}

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

import io.comet.Activity.MainActivity;
import io.comet.Model.AccessToken;
import io.comet.R;
import io.comet.Utils.Singleton;
import retrofit2.Response;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;

import java.io.IOException;
import java.util.regex.*;

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

    private class DoInBackground extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                Response<AccessToken> response = Singleton.retrofit().login(email, password).execute();
                AccessToken token = response.body();
                Singleton.getInstance().setToken(token.aToken, token.rToken);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

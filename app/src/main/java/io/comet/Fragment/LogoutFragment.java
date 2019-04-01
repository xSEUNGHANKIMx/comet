package io.comet.Fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.comet.Activity.MainActivity;
import io.comet.Model.SimpleRes;
import io.comet.R;
import io.comet.Utils.Singleton;
import retrofit2.Response;

public class LogoutFragment extends Fragment {
    private Context mContext;
    private MainActivity mActivity;

    @BindView(R.id.logoutBtn)
    Button logoutBtn;
    @BindView(R.id.tvToken)
    TextView tvToken;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        mActivity = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_logout, container, false);
        ButterKnife.bind(this, view);

        tvToken.setText(Singleton.getInstance().getToken().aToken);
        logoutBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new LogoutFragment.DoInBackground().execute("");
            }
        });

        return view;
    }

    private void goToStartupFragment() {
        mActivity.replaceFragment(mActivity.FRAGMENT_ID_STARTUP);
    }

    private class DoInBackground extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... strings) {
            int ret = 0;
            try {
                Response<SimpleRes> response = Singleton.getInstance().loginRetrofit().logout("ANDROID").execute();
                if (response != null) {
                    Singleton.getInstance().setToken("", "");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ret;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            goToStartupFragment();
            super.onPostExecute(integer);
        }
    }
}

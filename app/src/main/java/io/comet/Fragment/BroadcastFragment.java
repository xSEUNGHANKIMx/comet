package io.comet.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.IOException;

import io.comet.Activity.MainActivity;
import io.comet.Model.SimpleRes;
import io.comet.R;
import io.comet.Utils.Singleton;
import retrofit2.Response;

public class BroadcastFragment extends Fragment {
    private Context mContext;
    private MainActivity mActivity;
    private Button broadcastBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        mActivity = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_broadcast, container, false);
        broadcastBtn = view.findViewById(R.id.broadcastBtn);

        broadcastBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Singleton.USER_BROADCAST_ACTION);
                mActivity.sendBroadcast(intent);
            }
        });

        return view;
    }
}

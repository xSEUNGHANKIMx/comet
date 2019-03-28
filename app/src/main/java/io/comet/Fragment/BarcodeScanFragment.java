package io.comet.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.comet.Activity.BaseActivity;

public class BarcodeScanFragment extends Fragment {
    private Context mContext;
    private BaseActivity mActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        mActivity = (BaseActivity) getActivity();

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
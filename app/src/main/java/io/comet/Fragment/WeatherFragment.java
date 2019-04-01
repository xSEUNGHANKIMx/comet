package io.comet.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.comet.Activity.MainActivity;
import io.comet.Activity.WeatherDetailActivity;
import io.comet.Model.CurrentlyWeatherModel;
import io.comet.Model.WeatherDataModel;
import io.comet.Model.WeatherModel;
import io.comet.R;
import io.comet.Utils.APIService;
import io.comet.Utils.SeparatorDeco;
import io.comet.Utils.Singleton;
import io.comet.Utils.TimeUtils;
import io.comet.Utils.WeatherIcons;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static butterknife.internal.Utils.arrayOf;

@RuntimePermissions
public class WeatherFragment extends Fragment {
    private Context mContext;
    private MainActivity mActivity;
    private double mLatitude = 32.715736;
    private double mLongitude = -117.161087;
    private Location mCurrLocation = null;
    private LayoutInflater mInflater;
    private List<WeatherDataModel> mDailyWeatherDataModel = new ArrayList<>();
    private DailyReportAdapter mDailyReportAdapter = new DailyReportAdapter();
    private Animation mUpdateAnim;

    @BindView(R.id.tv_city_name)
    TextView mTvCityName;
    @BindView(R.id.icTodayWeather)
    ImageView mIconTodayWeather;
    @BindView(R.id.tvTemperature)
    TextView mTvTemperature;
    @BindView(R.id.tvSummary)
    TextView mTvSummary;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_button)
    ImageView mBtnRefresh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        mActivity = (MainActivity) getActivity();
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        ButterKnife.bind(this, view);

        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.INTERNET}, 0);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setAdapter(mDailyReportAdapter);
        mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setLayoutManager(layoutManager);
        SeparatorDeco decoration = new SeparatorDeco(mActivity, Color.GRAY, 1.0f);
        mRecyclerView.addItemDecoration(decoration);

        mBtnRefresh.setOnClickListener(mRefreshBtnClickListener);
        mUpdateAnim = AnimationUtils.loadAnimation(mActivity, R.anim.anim_refresh_rotate);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        checkPermissions();
    }

    private View.OnClickListener mRefreshBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getWeather();
        }
    };

    @NeedsPermission(Manifest.permission.INTERNET)
    public void getWeather() {
        mTvCityName.setText(mActivity.getString(R.string.all_wait));
        mBtnRefresh.startAnimation(mUpdateAnim);

        mCurrLocation = getCurrLocation();
        if(mCurrLocation != null) {
            mLatitude = Double.valueOf(mCurrLocation.getLatitude());
            mLongitude = Double.valueOf(mCurrLocation.getLongitude());
        } else {
            // San Diego
            mLatitude = 32.715736;
            mLongitude = -117.161087;
        }

        Singleton.getInstance().weatherRetrofit().create(APIService.class)
                .getWeather(mLatitude, mLongitude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<WeatherModel>>() {

                    @Override
                    public void onNext(Response<WeatherModel> response) {
                        unsubscribe();
                        if (response.isSuccessful()) {
                            WeatherModel weather = response.body();
                            if (weather != null) {
                                CurrentlyWeatherModel currently = weather.getCurrently();
                                Geocoder geocoder = new Geocoder(mActivity, Locale.getDefault());
                                List<Address> addresses = null;
                                try {
                                    addresses = geocoder.getFromLocation(mLatitude, mLongitude, 1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                String cityName = addresses.get(0).getLocality();
                                if(cityName.isEmpty()) {
                                    mTvCityName.setText("San Diego");
                                } else {
                                    mTvCityName.setText(cityName);
                                }

                                mTvTemperature.setText(String.format("%.1f", currently.getTemperature()) + "\u00b0F");
                                mTvSummary.setText(currently.getSummary());

                                mDailyWeatherDataModel = weather.getDaily().getData();
                                mDailyReportAdapter.notifyDataSetChanged();

                                Integer iconResource = WeatherIcons.getIconResource(currently.getIcon());
                                mIconTodayWeather.setImageResource(iconResource);
                            }
                            mBtnRefresh.clearAnimation();
                        }
                    }

                    @Override
                    public void onCompleted() {
                        mBtnRefresh.clearAnimation();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mBtnRefresh.clearAnimation();
                    }
                });
    }

    private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION), Singleton.PERMISSION_CALLBACK_CONSTANT);
            } else {
                //just request the permission
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION), Singleton.PERMISSION_CALLBACK_CONSTANT);
            }
        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission();
        }
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Singleton.PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                proceedAfterPermission();
            } else {
                mActivity.replaceFragment(mActivity.FRAGMENT_ID_STARTUP);
            }
        }
    }

    private void proceedAfterPermission() {
        mBtnRefresh.startAnimation(mUpdateAnim);
        getWeather();
    }

    private Location getCurrLocation() {
        LocationManager lm = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
        Location fineLocation = null;
        Location coarseLocation = null;
        Location currLocation = null;
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                fineLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            } else if(lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                fineLocation = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

        }

        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            coarseLocation =  lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        if((fineLocation != null) && (coarseLocation != null)) {
            if(fineLocation.getAccuracy() < coarseLocation.getAccuracy()) {
                currLocation = fineLocation;
            } else {
                currLocation = coarseLocation;
            }
        } else if ((fineLocation != null) && (coarseLocation == null)) {
            currLocation = fineLocation;
        } else if ((fineLocation == null) && (coarseLocation != null)) {
            currLocation = coarseLocation;
        }

        return currLocation;
    }

    class DailyReportAdapter extends RecyclerView.Adapter<DailyReportViewHolder> {
        @Override
        public DailyReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.item_daily_weather, parent, false);
            return new DailyReportViewHolder(view);
        }

        @Override
        public void onBindViewHolder(DailyReportViewHolder holder, final int position) {
            WeatherDataModel dailyData = mDailyWeatherDataModel.get(position);

            String day = TimeUtils.getDayOfWeek(dailyData.getTime());
            holder.setWeekday(day);
            String date = TimeUtils.getDate(dailyData.getTime());
            holder.setDate(date);
            holder.setIcon(dailyData.getIcon());
            Double tempHigh = dailyData.getTemperatureHigh();
            holder.setTempHigh(String.format("%.1f", tempHigh));
            Double tempLow = dailyData.getTemperatureLow();
            holder.setTempLow(String.format("%.1f", tempLow));
            holder.setSummary(dailyData.getSummary());

            // add click listener
            holder.holder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity.getApplicationContext(), WeatherDetailActivity.class);
                    Gson gson = new Gson();
                    String jsonData = gson.toJson(mDailyWeatherDataModel.get(position));
                    intent.putExtra("detailData", jsonData);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDailyWeatherDataModel.size();
        }
    }

    class DailyReportViewHolder extends RecyclerView.ViewHolder {
        View holder;
        ImageView icon;
        TextView date, weekday, tempHigh, tempLow, summary;
        //TouchListener touchListener = new TouchListener();

        DailyReportViewHolder(View view) {
            super(view);
            holder = view.findViewById(R.id.item_daily_info_holder);

            date = view.findViewById(R.id.tv_daily_info_date);
            weekday = view.findViewById(R.id.tv_daily_info_weekday);
            icon = view.findViewById(R.id.ic_daily_info_icon);
            tempHigh = view.findViewById(R.id.tv_daily_info_temp_high);
            tempLow = view.findViewById(R.id.tv_daily_info_temp_low);
            summary = view.findViewById(R.id.tv_daily_info_summary);
        }

        public void setDate(String date) {
            this.date.setText(date);
        }

        public void setWeekday(String weekday) {
            this.weekday.setText(weekday);
        }

        public void setIcon(String resource) {
            Integer imageRsc = WeatherIcons.getIconResource(resource);
            this.icon.setImageResource(Integer.valueOf(imageRsc));
        }

        public void setTempHigh(String temp) {
            this.tempHigh.setText(temp + "\u00b0F");
        }

        public void setTempLow(String temp) {
            this.tempLow.setText(temp + "\u00b0F");
        }

        public void setSummary(String summary) {
            this.summary.setText(summary);
        }
    }
}

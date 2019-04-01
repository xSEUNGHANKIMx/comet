package io.comet.Utils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.comet.Model.AccessToken;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Singleton {
    private volatile static Singleton instance = null;
    private final String LOGIN_DOMAIN = "https://api.gpac.works/";
    private AccessToken token = new AccessToken("", "");
    private final String WEATHER_DOMAIN = "https://api.darksky.net/forecast";
    private final String WEATHER_API_KEY = "9ddb5bc933606669297dc963fbd3574b";
    public final static String USER_BROADCAST_ACTION = "USER_BROADCAST_ACTION";
    public final static int ACCESS_CAMERA = 100;
    public static boolean isNetworkConnected = true;
    public static final int PERMISSION_CALLBACK_CONSTANT = 101;
    public static final int REQUEST_PERMISSION_SETTING = 102;

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                instance = new Singleton();
            }
        }

        return instance;
    }

    public AccessToken getToken() {
        return token;
    }

    public void setToken(String aToken, String rToken) {
        token = new AccessToken(aToken, rToken);
    }

    public APIService loginRetrofit() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(LOGIN_DOMAIN)
                .addConverterFactory(CustomGsonBuilder.getCustomConverter());

        final AccessToken aToken = token;

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .addHeader("x_access_token", aToken.aToken)
                                .addHeader("xsoc", "")
                                .addHeader("xmob", "Android")
                                .build();
                        return chain.proceed(request);
                    }
                }).build();

        builder.client(client);
        return builder.build().create(APIService.class);
    }

    public Retrofit weatherRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(WEATHER_DOMAIN + File.separator + WEATHER_API_KEY + File.separator)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }
}

package io.comet.Utils;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.comet.Model.AccessToken;
import okhttp3.Connection;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;

public class Singleton {
    private volatile static Singleton instance = null;
    private final String DEVELOPMENT_DOMAIN = "https://api.gpac.works/";
    private AccessToken token = new AccessToken("", "");

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

    public APIService retrofit() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .addConverterFactory(CustomGsonBuilder.getCustomConverter())
                .baseUrl(DEVELOPMENT_DOMAIN);

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

}

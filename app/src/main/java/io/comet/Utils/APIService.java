package io.comet.Utils;

import io.comet.Model.AccessToken;
import io.comet.Model.SimpleRes;
import io.comet.Model.WeatherModel;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

public interface APIService {
    @POST("user/login")
    @FormUrlEncoded
    Call<AccessToken> login(
            @Field("user_email") String email,
            @Field("user_passwd") String password
    );

    @POST("user/logout")
    @FormUrlEncoded
    Call<SimpleRes> logout(
            @Field("device") String device
    );

    @GET("{lat},{lon}")
    Observable<Response<WeatherModel>> getWeather(
            @Path("lat") double lat,
            @Path("lon") double lon
    );
}

package io.comet.Utils;

import io.comet.Model.AccessToken;
import io.comet.Model.SimpleRes;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIService {
    @POST("user/login")
    @FormUrlEncoded
    Call<AccessToken> login(@Field("user_email") String email, @Field("user_passwd") String password);

    @POST("user/logout")
    @FormUrlEncoded
    Call<SimpleRes> logout(@Field("device") String device);
}

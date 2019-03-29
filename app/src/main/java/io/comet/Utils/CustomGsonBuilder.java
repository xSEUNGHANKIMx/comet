package io.comet.Utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import io.comet.Model.AccessToken;
import io.comet.Model.SimpleRes;
import retrofit2.Converter;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomGsonBuilder {
    static Converter.Factory getCustomConverter() {
        return GsonConverterFactory.create(getGsonBuilder());
    }

    static Gson getGsonBuilder() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(SimpleRes.class, new JsonDeserializer<SimpleRes>() {

            @Override
            public SimpleRes deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                JsonObject obj = json.getAsJsonObject();
                try {
                    return new SimpleRes(obj.get("message").toString(), obj.get("code").getAsInt(), obj.has("data") ? obj.get("data").toString() : "", YMDtoDate(obj.get("timestamp").toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });

        builder.registerTypeAdapter(AccessToken.class, new JsonDeserializer<AccessToken>() {
            @Override
            public AccessToken deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                JsonObject data = json.getAsJsonObject().get("data").getAsJsonObject();
                JsonObject token = data.get("token").getAsJsonObject();
                return new AccessToken(token.get("access_token").toString(), token.get("refresh_token").toString());
            }
        });

        return builder.create();
    }

    static Date YMDtoDate(String date) throws ParseException {
        if (date.length() == 0) return null;
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat df = new SimpleDateFormat(pattern);

        return df.parse(date);
    }
}

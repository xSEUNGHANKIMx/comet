package io.comet.Utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

import io.comet.Model.SimpleRes;
import retrofit2.Converter;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomGsonBuilder {
    static Converter.Factory getCustomConverter() {
        return GsonConverterFactory.create(getGsonBuilder());
    }

    Gson getGsonBuilder() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter((Type) SimpleRes.class, null);

        return builder.create();
    }
}

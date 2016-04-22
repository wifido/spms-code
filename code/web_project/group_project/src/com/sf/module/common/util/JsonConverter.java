package com.sf.module.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static com.sf.module.common.util.StringUtil.isBlank;

public class JsonConverter {
    public static <T> String convertObjectToJson(T objects) {
        return getGson().toJson(objects);
    }

    public static <T> T convertJsonToObject(String expected, TypeToken token) {
        return (T) getGson().fromJson(expected, token.getType());
    }

    private static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss");
        return gsonBuilder.create();
    }

    public static <T> List<T> convertObjectListFromJson(String json, TypeToken token) {
        List<T> list = new ArrayList<T>();

        if (isBlank(json)) {
            return list;
        }

        try {
            list = getGson().fromJson(json, token.getType());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return list;
    }

}

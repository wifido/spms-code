package com.sf.module.pushserver.util;

import com.google.gson.Gson;

public class Json {

    public static <T> String toJson(T dto) {
        return new Gson().toJson(dto);
    }
}

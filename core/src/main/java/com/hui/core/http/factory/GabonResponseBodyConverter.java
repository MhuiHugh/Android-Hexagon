package com.hui.core.http.factory;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

import okhttp3.ResponseBody;
import retrofit2.Converter;


/**
 * Created by Petterp
 * on 2019-12-21
 * Function:
 */
public class GabonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Type mType;

    GabonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        mType = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        if (mType == String.class || mType.getClass().isPrimitive()) {
            return (T) response;
        }
        return gson.fromJson(response, mType);
    }


    private static Type getParameterUpperBound(ParameterizedType type) {
        Type[] types = type.getActualTypeArguments();
        if (0 >= types.length) {
            throw new IllegalArgumentException(
                    "Index " + 0 + " not in range [0," + types.length + ") for " + type);
        }
        Type paramType = types[0];
        if (paramType instanceof WildcardType) {
            return ((WildcardType) paramType).getUpperBounds()[0];
        }
        return paramType;
    }
}



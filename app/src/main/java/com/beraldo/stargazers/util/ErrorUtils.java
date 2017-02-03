package com.beraldo.stargazers.util;

import android.util.Log;

import com.beraldo.stargazers.data.source.net.APIError;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ErrorUtils {
    public static APIError parseError(Retrofit retrofit, Response<?> response) {
        Converter<ResponseBody, APIError> converter =
                retrofit.responseBodyConverter(APIError.class, new Annotation[0]);

        APIError error;

        try {
            //Log.d("ERROR", response.errorBody().contentType().toString());
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            Log.d("EXCEPTION", e.getMessage());
            return new APIError("IOException while trying to parse the API error. " + e.getMessage(), "");
        }

        return error;
    }
}

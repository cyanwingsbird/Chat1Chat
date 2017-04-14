package net.cyanwingsbird.chat1chat.networking;

import net.cyanwingsbird.chat1chat.Global;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Andy on 25/8/2016.
 */

public class StatusUtils {

    public static APIStatus parseError(Response<?> response) {


        Converter<ResponseBody, APIStatus> errorConverter = ((Retrofit)new Retrofit.Builder().baseUrl(Global.getServerURL()).addConverterFactory(GsonConverterFactory.create()).build()).responseBodyConverter(APIStatus.class, new Annotation[0]);
        APIStatus error;
        try {
            error = errorConverter.convert(response.errorBody());
        } catch (IOException e) {
            return new APIStatus();
        }
        return error;
    }
}

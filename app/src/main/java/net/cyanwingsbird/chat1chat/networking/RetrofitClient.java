package net.cyanwingsbird.chat1chat.networking;

import android.util.Log;

import net.cyanwingsbird.chat1chat.Global;
import net.cyanwingsbird.chat1chat.userAccount.AccountInfo;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Andy on 6/6/2016.
 */
public class RetrofitClient {

    final static String TAG = "RetrofitClient";

    public Call login(String username, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Global.getServerURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Call<AccountInfo> call = null;
        try {
            RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
            call = retrofitInterface.login(username, password);
        } catch (Exception e) {
            Log.i(TAG, "Error " + e);
        }
        return  call;
    }
}

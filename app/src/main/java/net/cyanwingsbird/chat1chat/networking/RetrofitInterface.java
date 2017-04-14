package net.cyanwingsbird.chat1chat.networking;


import net.cyanwingsbird.chat1chat.userAccount.AccountInfo;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Andy on 6/6/2016.
 */


public interface RetrofitInterface {

    @FormUrlEncoded
    @POST("login.php")
    Call<AccountInfo> login(@Field("username") String username, @Field("password") String password);

}

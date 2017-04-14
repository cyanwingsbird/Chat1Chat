package net.cyanwingsbird.chat1chat.networking;


import net.cyanwingsbird.chat1chat.dataset.Friend;
import net.cyanwingsbird.chat1chat.userAccount.AccountInfo;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Andy on 6/6/2016.
 */


public interface RetrofitInterface {

    @FormUrlEncoded
    @POST("registerAccount.php")
    Call<APIStatus> registration(@Field("username") String username, @Field("password") String password, @Field("display_name") String display_name);

    @FormUrlEncoded
    @POST("login.php")
    Call<AccountInfo> login(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("addFd.php")
    Call<APIStatus> addFd(@Field("username") String username, @Field("password") String password, @Field("fdUserID") String fdUserID);

    @FormUrlEncoded
    @POST("getFdList.php")
    Call<ArrayList<Friend>> getFdList(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("getPendingList.php")
    Call<ArrayList<Friend>> getPendingList(@Field("username") String username, @Field("password") String password);


}

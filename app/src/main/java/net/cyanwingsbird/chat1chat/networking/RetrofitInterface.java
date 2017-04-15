package net.cyanwingsbird.chat1chat.networking;


import net.cyanwingsbird.chat1chat.dataset.Friend;
import net.cyanwingsbird.chat1chat.userAccount.AccountInfo;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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

    @FormUrlEncoded
    @POST("acceptPendingFd.php")
    Call<APIStatus> acceptPendingFd(@Field("username") String username, @Field("password") String password, @Field("fdUserID") String fdUserID);

    @Multipart
    @POST("changeProfile.php")
    Call<APIStatus> changeProfile(@Part("username") RequestBody username, @Part("password") RequestBody password, @Part("display_name") RequestBody display_name, @Part MultipartBody.Part upload_file);





}

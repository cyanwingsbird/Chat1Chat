package net.cyanwingsbird.chat1chat.networking;

import android.util.Log;

import net.cyanwingsbird.chat1chat.Global;
import net.cyanwingsbird.chat1chat.dataset.Friend;
import net.cyanwingsbird.chat1chat.dataset.Message;
import net.cyanwingsbird.chat1chat.userAccount.AccountInfo;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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

    public Call addFd(String username, String password, String fdUserID) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Global.getServerURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Call<APIStatus> call = null;
        try {
            RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
            call = retrofitInterface.addFd(username, password, fdUserID);
        } catch (Exception e) {
            Log.i(TAG, "Error " + e);
        }
        return  call;
    }

    public Call registration(String username, String password, String display_name) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Global.getServerURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Call<APIStatus> call = null;
        try {
            RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
            call = retrofitInterface.registration(username, password, display_name);
        } catch (Exception e) {
            Log.i(TAG, "Error " + e);
        }
        return  call;
    }

    public Call getFdList(String username, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Global.getServerURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Call<ArrayList<Friend>> call = null;
        try {
            RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
            call = retrofitInterface.getFdList(username, password);
        } catch (Exception e) {
            Log.i(TAG, "Error " + e);
        }
        return  call;
    }

    public Call getPendingList(String username, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Global.getServerURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Call<ArrayList<Friend>> call = null;
        try {
            RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
            call = retrofitInterface.getPendingList(username, password);
        } catch (Exception e) {
            Log.i(TAG, "Error " + e);
        }
        return  call;
    }

    public Call acceptPendingFd(String username, String password, String fdUserID) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Global.getServerURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Call<APIStatus> call = null;
        try {
            RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
            call = retrofitInterface.acceptPendingFd(username, password, fdUserID);
        } catch (Exception e) {
            Log.i(TAG, "Error " + e);
        }
        return  call;
    }

    public Call changeProfile(String username, String password, String display_name, File upload_file) {
        RequestBody username_body = RequestBody.create(MediaType.parse("text/plain"), username);
        RequestBody password_body = RequestBody.create(MediaType.parse("text/plain"), password);
        RequestBody display_body = RequestBody.create(MediaType.parse("text/plain"), display_name);

        MultipartBody.Part file_part = null;
        if(upload_file!=null)
        {
            RequestBody file_body = RequestBody.create(MediaType.parse("image/*"), upload_file);
            file_part = MultipartBody.Part.createFormData("upload_file", upload_file.getName(), file_body);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Global.getServerURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Call<APIStatus> call = null;
        try {
            RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
            call = retrofitInterface.changeProfile(username_body, password_body, display_body, file_part);
        } catch (Exception e) {
            Log.i(TAG, "Error " + e);
        }
        return  call;
    }

    public Call sendMsg(String username, String password, String receiver_ID,
                        String message_type, String message_content, File upload_file) {
        RequestBody username_body = RequestBody.create(MediaType.parse("text/plain"), username);
        RequestBody password_body = RequestBody.create(MediaType.parse("text/plain"), password);
        RequestBody receiver_body = RequestBody.create(MediaType.parse("text/plain"), receiver_ID);
        RequestBody message_type_body = RequestBody.create(MediaType.parse("text/plain"), message_type);
        RequestBody message_content_body = null;
        if(message_content!=null) {
            message_content_body = RequestBody.create(MediaType.parse("text/plain"), message_content);
        }

        MultipartBody.Part file_part = null;
        if(upload_file!=null)
        {
            RequestBody file_body = RequestBody.create(MediaType.parse("image/*"), upload_file);
            file_part = MultipartBody.Part.createFormData("upload_file", upload_file.getName(), file_body);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Global.getServerURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Call<APIStatus> call = null;
        try {
            RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
            call = retrofitInterface.sendMsg(username_body, password_body, receiver_body, message_type_body, message_content_body, file_part);
        } catch (Exception e) {
            Log.i(TAG, "Error " + e);
        }
        return  call;
    }

    public Call getMsg(String username, String password, String fdUserID) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Global.getServerURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Call<ArrayList<Message>> call = null;
        try {
            RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
            call = retrofitInterface.getMsg(username, password, fdUserID);
        } catch (Exception e) {
            Log.i(TAG, "Error " + e);
        }
        return  call;
    }

    public Call getFd(String username, String password, String fdUserID) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Global.getServerURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Call<Friend> call = null;
        try {
            RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
            call = retrofitInterface.getFd(username, password, fdUserID);
        } catch (Exception e) {
            Log.i(TAG, "Error " + e);
        }
        return  call;
    }
}

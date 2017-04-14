package net.cyanwingsbird.chat1chat;

import android.os.Environment;
import android.provider.Settings;

import net.cyanwingsbird.chat1chat.userAccount.AccountInfo;
import net.cyanwingsbird.chat1chat.userAccount.LoginInfo;

/**
 * Created by lampinghim on 24/3/2017.
 */

public class Global {
    private final static String serverURL = "http://52.74.120.149/chat1chat/";
    private final static String fileFolderPath = Environment.getExternalStorageDirectory() + "/Chat1Chat";
    private final static String loginFilePath = getFileFolderPath() +"/login_data.json";
    private static LoginInfo loginInfo;
    private static AccountInfo accountInfo;


    public static String getDeviceID(){
        return Settings.Secure.ANDROID_ID;
    }

    public static String getServerURL() {
        return serverURL;
    }

    public static String getFileFolderPath() {
        return fileFolderPath;
    }

    public static String getLoginFilePath() {
        return loginFilePath;
    }

    public static LoginInfo getLoginInfo() {
        return loginInfo;
    }

    public static void setLoginInfo(LoginInfo loginInfo) {
        Global.loginInfo = loginInfo;
    }

    public static AccountInfo getAccountInfo() {
        return accountInfo;
    }

    public static void setAccountInfo(AccountInfo accountInfo) {
        Global.accountInfo = accountInfo;
    }

}
package net.cyanwingsbird.chat1chat.userAccount;

import com.google.gson.Gson;

import net.cyanwingsbird.chat1chat.Global;
import net.cyanwingsbird.chat1chat.utility.MyFileHandler;

/**
 * Created by Andy on 2/6/2016.
 */
public class UserAccountManager {

    private static final String TAG = "UserAccountManager";

    public static LoginInfo getLogin_info() {
        String json = MyFileHandler.readFile(Global.getLoginFilePath());
        if(json!=null)
        {
            LoginInfo loginInfo = new Gson().fromJson(json, LoginInfo.class);
            return loginInfo;
        }
        return null;
    }

    public static void setLogin_info(LoginInfo login_info) {
        String stored_info = new Gson().toJson(login_info);
        MyFileHandler.writeFile(stored_info, Global.getLoginFilePath());
    }

    public static void logout()
    {
        Global.setAccountInfo(null);
        Global.setLoginInfo(null);
        MyFileHandler.removeFolder();
    }

}

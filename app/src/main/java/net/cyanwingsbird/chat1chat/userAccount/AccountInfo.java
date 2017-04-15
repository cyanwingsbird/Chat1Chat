package net.cyanwingsbird.chat1chat.userAccount;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Andy on 26/8/2016.
 */

public class AccountInfo {
    @SerializedName("userID")
    @Expose
    private String userID;
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("profilePic")
    @Expose
    private String profilePic;

    /**
     * @return The userID
     */
    public String getUserID() {
        return userID;
    }

    /**
     * @param userID The userID
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * @return The displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName The display_name
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @return The profilePic
     */
    public String getProfilePic() {
        return profilePic;
    }

    /**
     * @param profilePic The profilePic
     */
    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }


}

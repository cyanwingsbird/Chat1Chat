package net.cyanwingsbird.chat1chat.dataset;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by lampinghim on 4/12/2016.
 */

public class Friend {
    @SerializedName("userID")
    @Expose
    private String userID;
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("profilePic")
    @Expose
    private String profilePic;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }


}


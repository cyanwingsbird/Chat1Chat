package net.cyanwingsbird.chat1chat.dataset;

import java.util.ArrayList;

/**
 * Created by lampinghim on 4/12/2016.
 */

public class FriendList {

    private ArrayList<Friend> friendArrayList = new ArrayList<>();

    public FriendList(ArrayList<Friend> friendArrayList)
    {
        this.setFriendArrayList(friendArrayList);
    }

    public ArrayList<Friend> getFriendArrayList() {
        return friendArrayList;
    }

    public void setFriendArrayList(ArrayList<Friend> friendArrayList) {
        this.friendArrayList = friendArrayList;
    }
}

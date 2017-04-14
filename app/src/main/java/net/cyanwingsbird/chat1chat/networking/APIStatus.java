package net.cyanwingsbird.chat1chat.networking;

/**
 * Created by Andy on 25/8/2016.
 */

public class APIStatus {

    private int statusCode;
    private String message;

    public APIStatus() {
    }

    public int status() {
        return statusCode;
    }

    public String message() {
        return message;
    }

}

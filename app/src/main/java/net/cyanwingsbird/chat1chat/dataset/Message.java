
package net.cyanwingsbird.chat1chat.dataset;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Message {

    @SerializedName("messageID")
    @Expose
    private String messageID;
    @SerializedName("from_userID")
    @Expose
    private String fromUserID;
    @SerializedName("to_userID")
    @Expose
    private String toUserID;
    @SerializedName("send_time")
    @Expose
    private String sendTime;
    @SerializedName("message_type")
    @Expose
    private String messageType;
    @SerializedName("message_content")
    @Expose
    private String messageContent;

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getFromUserID() {
        return fromUserID;
    }

    public void setFromUserID(String fromUserID) {
        this.fromUserID = fromUserID;
    }

    public String getToUserID() {
        return toUserID;
    }

    public void setToUserID(String toUserID) {
        this.toUserID = toUserID;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}

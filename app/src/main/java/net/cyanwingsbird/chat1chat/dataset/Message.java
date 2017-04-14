
package net.cyanwingsbird.chat1chat.dataset;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Message {

    @SerializedName("sender")
    @Expose
    private Integer sender;
    @SerializedName("content")
    @Expose
    private String content;

    /**
     *
     * @return
     * The sender
     */
    public Integer getSender() {
        return sender;
    }

    /**
     *
     * @param sender
     * The sender
     */
    public void setSender(Integer sender) {
        this.sender = sender;
    }

    /**
     *
     * @return
     * The content
     */
    public String getContent() {
        return content;
    }

    /**
     *
     * @param content
     * The content
     */
    public void setContent(String content) {
        this.content = content;
    }

}


package com.dsquare.hibour.pojos.comments;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Datum {

    @SerializedName("comment_message")
    @Expose
    private String commentMessage;
    @SerializedName("comment_time")
    @Expose
    private String commentTime;
    @SerializedName("comment_date")
    @Expose
    private String commentDate;
    @SerializedName("user")
    @Expose
    private User user;

    /**
     * 
     * @return
     *     The commentMessage
     */
    public String getCommentMessage() {
        return commentMessage;
    }

    /**
     * 
     * @param commentMessage
     *     The comment_message
     */
    public void setCommentMessage(String commentMessage) {
        this.commentMessage = commentMessage;
    }

    /**
     * 
     * @return
     *     The commentTime
     */
    public String getCommentTime() {
        return commentTime;
    }

    /**
     * 
     * @param commentTime
     *     The comment_time
     */
    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    /**
     * 
     * @return
     *     The commentDate
     */
    public String getCommentDate() {
        return commentDate;
    }

    /**
     * 
     * @param commentDate
     *     The comment_date
     */
    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    /**
     * 
     * @return
     *     The user
     */
    public User getUser() {
        return user;
    }

    /**
     * 
     * @param user
     *     The user
     */
    public void setUser(User user) {
        this.user = user;
    }

}

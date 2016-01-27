
package com.dsquare.hibour.pojos.posts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Postpojos {

    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("post_id")
    @Expose
    private String postId;
    @SerializedName("post_type")
    @Expose
    private String postType;
    @SerializedName("post_subtype")
    @Expose
    private String postSubtype;
    @SerializedName("post_message")
    @Expose
    private String postMessage;
    @SerializedName("post_image")
    @Expose
    private String postImage;
    @SerializedName("post_date")
    @Expose
    private String postDate;
    @SerializedName("post_time")
    @Expose
    private String postTime;
    @SerializedName("post_status")
    @Expose
    private String postStatus;
    @SerializedName("post_likes_count")
    @Expose
    private Integer postLikesCount;
    @SerializedName("post_liked_users")
    @Expose
    private List<PostLikedUser> postLikedUsers = new ArrayList<PostLikedUser>();
    @SerializedName("post_comments")
    @Expose
    private List<PostComment> postComments = new ArrayList<PostComment>();

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

    /**
     * 
     * @return
     *     The postId
     */
    public String getPostId() {
        return postId;
    }

    /**
     * 
     * @param postId
     *     The post_id
     */
    public void setPostId(String postId) {
        this.postId = postId;
    }

    /**
     * 
     * @return
     *     The postType
     */
    public String getPostType() {
        return postType;
    }

    /**
     * 
     * @param postType
     *     The post_type
     */
    public void setPostType(String postType) {
        this.postType = postType;
    }

    /**
     * 
     * @return
     *     The postSubtype
     */
    public String getPostSubtype() {
        return postSubtype;
    }

    /**
     * 
     * @param postSubtype
     *     The post_subtype
     */
    public void setPostSubtype(String postSubtype) {
        this.postSubtype = postSubtype;
    }

    /**
     * 
     * @return
     *     The postMessage
     */
    public String getPostMessage() {
        return postMessage;
    }

    /**
     * 
     * @param postMessage
     *     The post_message
     */
    public void setPostMessage(String postMessage) {
        this.postMessage = postMessage;
    }

    /**
     * 
     * @return
     *     The postImage
     */
    public String getPostImage() {
        return postImage;
    }

    /**
     * 
     * @param postImage
     *     The post_image
     */
    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    /**
     * 
     * @return
     *     The postDate
     */
    public String getPostDate() {
        return postDate;
    }

    /**
     * 
     * @param postDate
     *     The post_date
     */
    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    /**
     * 
     * @return
     *     The postTime
     */
    public String getPostTime() {
        return postTime;
    }

    /**
     * 
     * @param postTime
     *     The post_time
     */
    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    /**
     * 
     * @return
     *     The postStatus
     */
    public String getPostStatus() {
        return postStatus;
    }

    /**
     * 
     * @param postStatus
     *     The post_status
     */
    public void setPostStatus(String postStatus) {
        this.postStatus = postStatus;
    }

    /**
     * 
     * @return
     *     The postLikesCount
     */
    public Integer getPostLikesCount() {
        return postLikesCount;
    }

    /**
     * 
     * @param postLikesCount
     *     The post_likes_count
     */
    public void setPostLikesCount(Integer postLikesCount) {
        this.postLikesCount = postLikesCount;
    }

    /**
     * 
     * @return
     *     The postLikedUsers
     */
    public List<PostLikedUser> getPostLikedUsers() {
        return postLikedUsers;
    }

    /**
     * 
     * @param postLikedUsers
     *     The post_liked_users
     */
    public void setPostLikedUsers(List<PostLikedUser> postLikedUsers) {
        this.postLikedUsers = postLikedUsers;
    }

    /**
     * 
     * @return
     *     The postComments
     */
    public List<PostComment> getPostComments() {
        return postComments;
    }

    /**
     * 
     * @param postComments
     *     The post_comments
     */
    public void setPostComments(List<PostComment> postComments) {
        this.postComments = postComments;
    }

}

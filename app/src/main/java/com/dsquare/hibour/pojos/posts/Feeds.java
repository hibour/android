package com.dsquare.hibour.pojos.posts;

import com.dsquare.hibour.database.table.FeedsTable;

/**
 * Created by ASHOK on 2/7/2016.
 */
public class Feeds {
  private String postId;
  private String postImage;
  private String postDescription;
  private String postDate;
  private String postTime;
  private String postedUserName;
  private String postedUserImage;
  private String postedUserId;
  private String userLiked;
  private String likesCount;
  private String commentsCount;
  private String postyType;

  public Feeds() {
  }

  public Feeds(String postId, String postImage, String postDescription, String postDate,
               String postTime, String postedUserName, String postedUserImage,
               String postedUserId, String userLiked, String likesCount, String commentsCount,
               String postyType) {
    this.postId = postId;
    this.postImage = postImage;
    this.postDescription = postDescription;
    this.postDate = postDate;
    this.postTime = postTime;
    this.postedUserName = postedUserName;
    this.postedUserImage = postedUserImage;
    this.postedUserId = postedUserId;
    this.userLiked = userLiked;
    this.likesCount = likesCount;
    this.commentsCount = commentsCount;
    this.postyType = postyType;
  }

  public Feeds(FeedsTable post) {
    this(post.postid, post.imgurl, post.description,
        post.date, post.time, post.username, post.userimgurl,
        post.userid, String.valueOf(post.userliked),
        String.valueOf(post.likescount),
        String.valueOf(post.commentscount),
        post.posttype);
  }

  public String getPostId() {
    return postId;
  }

  public void setPostId(String postId) {
    this.postId = postId;
  }

  public String getPostImage() {
    return postImage;
  }

  public void setPostImage(String postImage) {
    this.postImage = postImage;
  }

  public String getPostDescription() {
    return postDescription;
  }

  public void setPostDescription(String postDescription) {
    this.postDescription = postDescription;
  }

  public String getPostDate() {
    return postDate;
  }

  public void setPostDate(String postDate) {
    this.postDate = postDate;
  }

  public String getPostTime() {
    return postTime;
  }

  public void setPostTime(String postTime) {
    this.postTime = postTime;
  }

  public String getPostedUserName() {
    return postedUserName;
  }

  public void setPostedUserName(String postedUserName) {
    this.postedUserName = postedUserName;
  }

  public String getPostedUserImage() {
    return postedUserImage;
  }

  public void setPostedUserImage(String postedUserImage) {
    this.postedUserImage = postedUserImage;
  }

  public String getPostedUserId() {
    return postedUserId;
  }

  public void setPostedUserId(String postedUserId) {
    this.postedUserId = postedUserId;
  }

  public String isUserLiked() {
    return userLiked;
  }

  public void setUserLiked(String userLiked) {
    this.userLiked = userLiked;
  }

  public String getLikesCount() {
    return likesCount;
  }

  public void setLikesCount(String likesCount) {
    this.likesCount = likesCount;
  }

  public String getCommentsCount() {
    return commentsCount;
  }

  public void setCommentsCount(String commentsCount) {
    this.commentsCount = commentsCount;
  }

  public String getPostyType() {
    return postyType;
  }

  public void setPostyType(String postyType) {
    this.postyType = postyType;
  }
}


package com.dsquare.hibour.pojos.posts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Datum {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("Userid")
    @Expose
    private String Userid;
    @SerializedName("posttype")
    @Expose
    private String posttype;
    @SerializedName("posttypename")
    @Expose
    private String posttypename;
    @SerializedName("Postmessage")
    @Expose
    private String Postmessage;
    @SerializedName("Postimage")
    @Expose
    private String Postimage;
    @SerializedName("Likescount")
    @Expose
    private String Likescount;
    @SerializedName("Liked")
    @Expose
    private Boolean Liked;
    @SerializedName("Status")
    @Expose
    private String Status;
    @SerializedName("com")
    @Expose
    private List<Com> com = new ArrayList<Com>();
    @SerializedName("likes")
    @Expose
    private List<Like> likes = new ArrayList<Like>();

    /**
     *
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The Userid
     */
    public String getUserid() {
        return Userid;
    }

    /**
     *
     * @param Userid
     * The Userid
     */
    public void setUserid(String Userid) {
        this.Userid = Userid;
    }

    /**
     *
     * @return
     * The posttype
     */
    public String getPosttype() {
        return posttype;
    }

    /**
     *
     * @param posttype
     * The posttype
     */
    public void setPosttype(String posttype) {
        this.posttype = posttype;
    }

    /**
     *
     * @return
     * The posttypename
     */
    public String getPosttypename() {
        return posttypename;
    }

    /**
     *
     * @param posttypename
     * The posttypename
     */
    public void setPosttypename(String posttypename) {
        this.posttypename = posttypename;
    }

    /**
     *
     * @return
     * The Postmessage
     */
    public String getPostmessage() {
        return Postmessage;
    }

    /**
     *
     * @param Postmessage
     * The Postmessage
     */
    public void setPostmessage(String Postmessage) {
        this.Postmessage = Postmessage;
    }

    /**
     *
     * @return
     * The Postimage
     */
    public String getPostimage() {
        return Postimage;
    }

    /**
     *
     * @param Postimage
     * The Postimage
     */
    public void setPostimage(String Postimage) {
        this.Postimage = Postimage;
    }

    /**
     *
     * @return
     * The Likescount
     */
    public String getLikescount() {
        return Likescount;
    }

    /**
     *
     * @param Likescount
     * The Likescount
     */
    public void setLikescount(String Likescount) {
        this.Likescount = Likescount;
    }

    /**
     *
     * @return
     * The Liked
     */
    public Boolean getLiked() {
        return Liked;
    }

    /**
     *
     * @param Liked
     * The Liked
     */
    public void setLiked(Boolean Liked) {
        this.Liked = Liked;
    }

    /**
     *
     * @return
     * The Status
     */
    public String getStatus() {
        return Status;
    }

    /**
     *
     * @param Status
     * The Status
     */
    public void setStatus(String Status) {
        this.Status = Status;
    }

    /**
     *
     * @return
     * The com
     */
    public List<Com> getCom() {
        return com;
    }

    /**
     *
     * @param com
     * The com
     */
    public void setCom(List<Com> com) {
        this.com = com;
    }

    /**
     *
     * @return
     * The likes
     */
    public List<Like> getLikes() {
        return likes;
    }

    /**
     *
     * @param likes
     * The likes
     */
    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

}

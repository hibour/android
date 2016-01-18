
package com.dsquare.hibour.pojos.posts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Like {

    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("Image")
    @Expose
    private String Image;
    @SerializedName("Profession")
    @Expose
    private String Profession;
    @SerializedName("Age")
    @Expose
    private String Age;
    @SerializedName("postName")
    @Expose
    private String postName;

    /**
     * 
     * @return
     *     The userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 
     * @param userName
     *     The userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 
     * @return
     *     The Image
     */
    public String getImage() {
        return Image;
    }

    /**
     * 
     * @param Image
     *     The Image
     */
    public void setImage(String Image) {
        this.Image = Image;
    }

    /**
     * 
     * @return
     *     The Profession
     */
    public String getProfession() {
        return Profession;
    }

    /**
     * 
     * @param Profession
     *     The Profession
     */
    public void setProfession(String Profession) {
        this.Profession = Profession;
    }

    /**
     * 
     * @return
     *     The Age
     */
    public String getAge() {
        return Age;
    }

    /**
     * 
     * @param Age
     *     The Age
     */
    public void setAge(String Age) {
        this.Age = Age;
    }

    /**
     * 
     * @return
     *     The postName
     */
    public String getPostName() {
        return postName;
    }

    /**
     * 
     * @param postName
     *     The postName
     */
    public void setPostName(String postName) {
        this.postName = postName;
    }

}

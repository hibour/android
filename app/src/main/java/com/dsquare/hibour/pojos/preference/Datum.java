
package com.dsquare.hibour.pojos.preference;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {


    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("preferencesname")
    @Expose
    private String preferencesname;
    @SerializedName("image1")
    @Expose
    private String image1;
    @SerializedName("image2")
    @Expose
    private String image2;

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
     * The preferencesname
     */
    public String getPreferencesname() {
        return preferencesname;
    }

    /**
     *
     * @param preferencesname
     * The preferencesname
     */
    public void setPreferencesname(String preferencesname) {
        this.preferencesname = preferencesname;
    }

    /**
     *
     * @return
     * The image1
     */
    public String getImage1() {
        return image1;
    }

    /**
     *
     * @param image1
     * The image1
     */
    public void setImage1(String image1) {
        this.image1 = image1;
    }

    /**
     *
     * @return
     * The image2
     */
    public String getImage2() {
        return image2;
    }

    /**
     *
     * @param image2
     * The image2
     */
    public void setImage2(String image2) {
        this.image2 = image2;
    }

}

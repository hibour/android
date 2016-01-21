
package com.dsquare.hibour.pojos.posttype;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("posttypename")
    @Expose
    private String posttypename;

    /**
     * 
     * @return
     *     The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The posttypename
     */
    public String getPosttypename() {
        return posttypename;
    }

    /**
     * 
     * @param posttypename
     *     The posttypename
     */
    public void setPosttypename(String posttypename) {
        this.posttypename = posttypename;
    }

}

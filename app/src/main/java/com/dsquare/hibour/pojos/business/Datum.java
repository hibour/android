
package com.dsquare.hibour.pojos.business;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("Business Name")
    @Expose
    private String BusinessName;

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
     *     The BusinessName
     */
    public String getBusinessName() {
        return BusinessName;
    }

    /**
     * 
     * @param BusinessName
     *     The Business Name
     */
    public void setBusinessName(String BusinessName) {
        this.BusinessName = BusinessName;
    }

}

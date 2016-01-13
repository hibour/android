
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
     *     The preferencesname
     */
    public String getPreferencesname() {
        return preferencesname;
    }

    /**
     * 
     * @param preferencesname
     *     The preferencesname
     */
    public void setPreferencesname(String preferencesname) {
        this.preferencesname = preferencesname;
    }

}

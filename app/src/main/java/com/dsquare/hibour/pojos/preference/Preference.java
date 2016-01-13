
package com.dsquare.hibour.pojos.preference;

import java.util.ArrayList;
import java.util.List;


import com.dsquare.hibour.pojos.preference.Datum;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Preference {

    @SerializedName("data")
    @Expose
    private List<Datum> data = new ArrayList<Datum>();

    /**
     * 
     * @return
     *     The data
     */
    public List<Datum> getData() {
        return data;
    }

    /**
     * 
     * @param data
     *     The data
     */
    public void setData(List<Datum> data) {
        this.data = data;
    }

}

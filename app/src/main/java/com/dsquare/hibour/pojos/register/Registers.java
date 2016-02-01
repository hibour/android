
package com.dsquare.hibour.pojos.register;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Registers {

    @SerializedName("data")
    @Expose
    private Data data;

    /**
     *
     * @return
     *     The data
     */
    public Data getData() {
        return data;
    }

    /**
     *
     * @param data
     *     The data
     */
    public void setData(Data data) {
        this.data = data;
    }

}

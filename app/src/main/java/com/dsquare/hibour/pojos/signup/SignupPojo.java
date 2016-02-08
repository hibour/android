
package com.dsquare.hibour.pojos.signup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignupPojo {

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

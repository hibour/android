package com.dsquare.hibour.pojos.posts;

/**
 * Created by Dsquare Android on 1/28/2016.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PostData {

    @SerializedName("data")
    @Expose
    private List<Postpojos> data = new ArrayList<Postpojos>();

    /**
     *
     * @return
     * The data
     */
    public List<Postpojos> getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(List<Postpojos> data) {
        this.data = data;
    }

}

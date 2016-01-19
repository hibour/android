package com.dsquare.hibour.pojos.govtproofs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Dsquare Android on 1/18/2016.
 */
public class Datum {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("ProofName")
    @Expose
    private String ProofName;

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
     * The ProofName
     */
    public String getProofName() {
        return ProofName;
    }

    /**
     *
     * @param ProofName
     * The ProofName
     */
    public void setProofName(String ProofName) {
        this.ProofName = ProofName;
    }
}


package com.dsquare.hibour.pojos.businesssubtype;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("business_id")
    @Expose
    private String businessId;
    @SerializedName("service_name")
    @Expose
    private String serviceName;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("Address")
    @Expose
    private String Address;
    @SerializedName("Sublocality")
    @Expose
    private String Sublocality;
    @SerializedName("Number")
    @Expose
    private String Number;

    /**
     * 
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The businessId
     */
    public String getBusinessId() {
        return businessId;
    }

    /**
     * 
     * @param businessId
     *     The business_id
     */
    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    /**
     * 
     * @return
     *     The serviceName
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * 
     * @param serviceName
     *     The service_name
     */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * 
     * @return
     *     The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * 
     * @param createdAt
     *     The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 
     * @return
     *     The Address
     */
    public String getAddress() {
        return Address;
    }

    /**
     * 
     * @param Address
     *     The Address
     */
    public void setAddress(String Address) {
        this.Address = Address;
    }

    /**
     * 
     * @return
     *     The Sublocality
     */
    public String getSublocality() {
        return Sublocality;
    }

    /**
     * 
     * @param Sublocality
     *     The Sublocality
     */
    public void setSublocality(String Sublocality) {
        this.Sublocality = Sublocality;
    }

    /**
     * 
     * @return
     *     The Number
     */
    public String getNumber() {
        return Number;
    }

    /**
     * 
     * @param Number
     *     The Number
     */
    public void setNumber(String Number) {
        this.Number = Number;
    }

}

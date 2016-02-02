
package com.dsquare.hibour.pojos.neighours;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("Username")
    @Expose
    private String Username;
    @SerializedName("Password")
    @Expose
    private String Password;
    @SerializedName("Email")
    @Expose
    private String Email;
    @SerializedName("Regtype")
    @Expose
    private String Regtype;
    @SerializedName("gcm")
    @Expose
    private String gcm;
    @SerializedName("Address")
    @Expose
    private String Address;
    @SerializedName("Address1")
    @Expose
    private String Address1;

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
     *     The Username
     */
    public String getUsername() {
        return Username;
    }

    /**
     * 
     * @param Username
     *     The Username
     */
    public void setUsername(String Username) {
        this.Username = Username;
    }

    /**
     * 
     * @return
     *     The Password
     */
    public String getPassword() {
        return Password;
    }

    /**
     * 
     * @param Password
     *     The Password
     */
    public void setPassword(String Password) {
        this.Password = Password;
    }

    /**
     * 
     * @return
     *     The Email
     */
    public String getEmail() {
        return Email;
    }

    /**
     * 
     * @param Email
     *     The Email
     */
    public void setEmail(String Email) {
        this.Email = Email;
    }

    /**
     * 
     * @return
     *     The Regtype
     */
    public String getRegtype() {
        return Regtype;
    }

    /**
     * 
     * @param Regtype
     *     The Regtype
     */
    public void setRegtype(String Regtype) {
        this.Regtype = Regtype;
    }

    /**
     * 
     * @return
     *     The gcm
     */
    public String getGcm() {
        return gcm;
    }

    /**
     * 
     * @param gcm
     *     The gcm
     */
    public void setGcm(String gcm) {
        this.gcm = gcm;
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
     *     The Address1
     */
    public String getAddress1() {
        return Address1;
    }

    /**
     * 
     * @param Address1
     *     The Address1
     */
    public void setAddress1(String Address1) {
        this.Address1 = Address1;
    }

}


package com.dsquare.hibour.pojos.register;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("Username")
    @Expose
    private String Username;
    @SerializedName("Email")
    @Expose
    private String Email;
    @SerializedName("Regtype")
    @Expose
    private String Regtype;

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

}

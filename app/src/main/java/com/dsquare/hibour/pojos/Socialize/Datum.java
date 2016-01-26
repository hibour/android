package com.dsquare.hibour.pojos.Socialize;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASHOK on 1/26/2016.
 */
public class Datum {
    @SerializedName("preference_id")
    @Expose
    private String preferenceId;
    @SerializedName("preference_name")
    @Expose
    private String preferenceName;
    @SerializedName("preference_image1")
    @Expose
    private String preferenceImage1;
    @SerializedName("preference_image2")
    @Expose
    private String preferenceImage2;
    @SerializedName("is_user_selected")
    @Expose
    private String isUserSelected;
    @SerializedName("choosed_users")
    @Expose
    private List<ChoosedUser> choosedUsers = new ArrayList<ChoosedUser>();

    /**
     *
     * @return
     * The preferenceId
     */
    public String getPreferenceId() {
        return preferenceId;
    }

    /**
     *
     * @param preferenceId
     * The preference_id
     */
    public void setPreferenceId(String preferenceId) {
        this.preferenceId = preferenceId;
    }

    /**
     *
     * @return
     * The preferenceName
     */
    public String getPreferenceName() {
        return preferenceName;
    }

    /**
     *
     * @param preferenceName
     * The preference_name
     */
    public void setPreferenceName(String preferenceName) {
        this.preferenceName = preferenceName;
    }

    /**
     *
     * @return
     * The preferenceImage1
     */
    public String getPreferenceImage1() {
        return preferenceImage1;
    }

    /**
     *
     * @param preferenceImage1
     * The preference_image1
     */
    public void setPreferenceImage1(String preferenceImage1) {
        this.preferenceImage1 = preferenceImage1;
    }

    /**
     *
     * @return
     * The preferenceImage2
     */
    public String getPreferenceImage2() {
        return preferenceImage2;
    }

    /**
     *
     * @param preferenceImage2
     * The preference_image2
     */
    public void setPreferenceImage2(String preferenceImage2) {
        this.preferenceImage2 = preferenceImage2;
    }

    /**
     *
     * @return
     * The isUserSelected
     */
    public String getIsUserSelected() {
        return isUserSelected;
    }

    /**
     *
     * @param isUserSelected
     * The is_user_selected
     */
    public void setIsUserSelected(String isUserSelected) {
        this.isUserSelected = isUserSelected;
    }

    /**
     *
     * @return
     * The choosedUsers
     */
    public List<ChoosedUser> getChoosedUsers() {
        return choosedUsers;
    }

    /**
     *
     * @param choosedUsers
     * The choosed_users
     */
    public void setChoosedUsers(List<ChoosedUser> choosedUsers) {
        this.choosedUsers = choosedUsers;
    }
}

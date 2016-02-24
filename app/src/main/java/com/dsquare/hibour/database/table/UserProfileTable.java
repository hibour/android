package com.dsquare.hibour.database.table;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.dsquare.hibour.pojos.user.UserDetail;
import com.dsquare.hibour.pojos.user.UserProfile;

/**
 * Created by Aditya Ravikanti on 2/23/2016.
 */
@Table(name = "user_profile")
public class UserProfileTable extends Model{
    @Column(name = "user_id", index = true, unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String user_id;
    @Column(name = "firstname")
    public String firstname;
    @Column(name = "lastname")
    public String lastname;
    @Column(name = "password")
    public String password;
    @Column(name = "image")
    public String Image;
    @Column(name = "gender")
    public String Gender;
    @Column(name = "email")
    public String Email;
    @Column(name = "address")
    public String Address;
    @Column(name = "mobile")
    public String mobile;
    @Column(name = "dob")
    public String dob;
    @Column(name="notifications")
    public String notifications;
    @Column(name = "sublocality")
    public String sublocality;
    @Column (name = "lat")
    public String lat;
    @Column(name = "lng")
    public String lng;
    @Column(name = "placeid")
    public String placeid;

    public UserProfileTable() {
        super();
    }

    public UserProfileTable(UserProfile user) {
        super();
        user_id = user.user_id;
        firstname = user.firstname;
        lastname = user.lastname;
        Image = user.Image;
        Gender = user.Gender;
        Email = user.Email;
        Address = user.Address;
        mobile = user.mobile;
        dob = user.dob;
        notifications = user.notifications;
        sublocality = user.sublocality;
        lat = user.lat;
        lng = user.lng;
        placeid = user.placeid;
    }

}

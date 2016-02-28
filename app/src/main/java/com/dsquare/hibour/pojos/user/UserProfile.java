package com.dsquare.hibour.pojos.user;

import com.dsquare.hibour.database.table.UserProfileTable;

/**
 * Created by Aditya Ravikanti on 2/23/2016.
 */
public class UserProfile {
  public String user_id;
  public String firstname;
  public String lastname;
  public String password;
  public String Image;
  public String Gender;
  public String Email;
  public String Address;
  public String mobile;
  public String dob;
  public String notifications;
  public String sublocality;
  public String lat;
  public String lng;
  public String placeid;

  public UserProfile() {
  }

  public UserProfile(UserProfileTable user) {
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

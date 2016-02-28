package com.dsquare.hibour.pojos.user;

import android.util.Log;

import com.dsquare.hibour.database.table.UserDetailTable;

public class UserDetail {
  public String id;
  public String Username;
  public String Image;
  public String Gender;
  public String Email;
  public String Address;

  public UserDetail() {
  }

  public UserDetail(UserDetailTable user) {
    id = user.user_id;
    Username = user.Username;
    Log.d("UserDetail",Username);
    Image = user.Image;
    Gender = user.Gender;
    Email = user.Email;
    Address = user.Address;
  }
}

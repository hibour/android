package com.dsquare.hibour.database.table;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.dsquare.hibour.pojos.user.UserDetail;

@Table(name = "user_detail")
public class UserDetailTable extends Model {


  @Column(name = "user_id", index = true, unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
  public int user_id;
  @Column(name = "username", index = true, unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
  public String Username;
  @Column(name = "image")
  public String Image;
  @Column(name = "gender")
  public String Gender;
  @Column(name = "email")
  public String Email;
  @Column(name = "address")
  public String Address;

  public UserDetailTable() {
    super();
  }

  public UserDetailTable(UserDetail userDetail) {
    super();
    user_id = userDetail.id;
    Username = userDetail.Username;
    Image = userDetail.Image;
    Gender = userDetail.Gender;
    Email = userDetail.Email;
    Address = userDetail.Address;
  }

}

package com.dsquare.hibour.database.table;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Dsquare Android on 2/6/2016.
 */
@Table(name = "feeds")
public class FeedsTable extends Model {

  @Column(name = "post_id", index = true, unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
  public String postid;
  @Column(name = "post_type")
  public String posttype;
  @Column(name = "date")
  public String date;
  @Column(name = "time")
  public String time;
  @Column(name = "post_message")
  public String description;
  @Column(name = "imgurl")
  public String imgurl;
  @Column(name = "likescount")
  public String likescount;
  @Column(name = "commentscount")
  public String commentscount;
  @Column(name = "userliked")
  public String userliked;
  @Column(name = "posteduser")
  public FeedsUserTable posteduser;
  @Column(name = "username")
  public String username;
  @Column(name = "userid")
  public String userid;
  @Column(name = "userimgurl")
  public String userimgurl;

  public FeedsTable() {
    super();
  }

  public FeedsTable(String postid, String date, String time, String description, String imgurl
      , FeedsUserTable posteduser, String postType) {
    super();
    this.postid = postid;
    this.date = date;
    this.time = time;
    this.description = description;
    this.imgurl = imgurl;
    this.posteduser = posteduser;
    this.userid = posteduser.id;
    this.userimgurl = posteduser.imgurl;
    this.username = posteduser.name;
    this.posttype = postType;
  }
}

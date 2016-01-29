package com.dsquare.hibour.database.table;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.dsquare.hibour.utils.Helper;

@Table(name = "notification")
public class NotificationTable extends Model {

  @Column(name = "message")
  public String message;

  @Column(name = "date")
  public String date;

  @Column(name = "status")
  public String status;

  public NotificationTable() {
    super();
  }

  public NotificationTable(String message, String date, String status) {
    super();
    this.message = message;
    this.date = date;
    this.status = status;
  }

  public NotificationTable(String message) {
    this(message, Helper.getTodayDate(), "unread");
  }

}

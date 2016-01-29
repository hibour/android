package com.dsquare.hibour.pojos.message;

import java.util.Calendar;
import java.util.Date;

public class UserMessage {
  public String fromUserID;
  public String toUserID;
  public Date time;
  public String message;

  public UserMessage(String fromUserID, String toUserID, String message, Date time) {
    this.fromUserID = fromUserID;
    this.toUserID = toUserID;
    this.time = time;
    this.message = message;
  }

  public UserMessage(String fromUserID, String toUserID, String message) {
    this(fromUserID, toUserID, message, Calendar.getInstance().getTime());
  }

}

package com.dsquare.hibour.pojos.message;

import java.util.Calendar;
import java.util.Date;

public class UserMessage {
  public Date local_id;
  public String fromUserID;
  public String toUserID;
  public Date time;
  public String message;
  public int messageState;
  public boolean isRead;

  public UserMessage(Date local_id, String fromUserID, String toUserID, String message, int messageState, Date time) {
    this.local_id = local_id;
    this.fromUserID = fromUserID;
    this.toUserID = toUserID;
    this.time = time;
    this.message = message;
    this.messageState = messageState;
    isRead = false;
  }

  public UserMessage(Date local_id, String fromUserID, String toUserID, String message, int messageState) {
    this(local_id, fromUserID, toUserID, message, messageState, Calendar.getInstance().getTime());
  }

  public void setRead(boolean isRead) {
    this.isRead = isRead;
  }

  public Calendar getTime() {
    Calendar messageTime = Calendar.getInstance();
    messageTime.setTime(time);
    return messageTime;
  }
}

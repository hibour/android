package com.dsquare.hibour.database.table;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.dsquare.hibour.pojos.message.UserMessage;

import java.util.Date;

@Table(name = "user_message")
public class UserMessageTable extends Model {

  @Column(name = "message")
  public String message;

  @Column(name = "to_user")
  public String to;

  @Column(name = "from_user")
  public String from;

  @Column(name = "message_time")
  public Date date;

  public UserMessageTable() {
    super();
  }

  public UserMessageTable(UserMessage userMessage) {
    super();
    message = userMessage.message;
    to = userMessage.toUserID;
    from = userMessage.fromUserID;
    date = userMessage.time;
  }

}

package com.dsquare.hibour.database.table;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.dsquare.hibour.pojos.message.UserMessage;

import java.util.Date;

@Table(name = "user_message")
public class UserMessageTable extends Model {

  @Column(name = "local_id", index = true, unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
  public Date local_id;

  @Column(name = "session_user")
  public String session_user;

  @Column(name = "message")
  public String message;

  @Column(name = "to_user")
  public String to;

  @Column(name = "from_user")
  public String from;

  @Column(name = "message_time")
  public Date date;

  @Column(name = "status")
  public int message_state;

  public UserMessageTable() {
    super();
  }

  public UserMessageTable(UserMessage userMessage, String session_user) {
    this();
    this.local_id = userMessage.local_id;
    this.session_user = session_user;
    message = userMessage.message;
    to = userMessage.toUserID;
    from = userMessage.fromUserID;
    date = userMessage.time;
    message_state = userMessage.messageState;
  }
}
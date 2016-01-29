package com.dsquare.hibour.database;

import android.content.Context;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.dsquare.hibour.database.table.NotificationTable;
import com.dsquare.hibour.database.table.UserDetailTable;
import com.dsquare.hibour.database.table.UserMessageTable;
import com.dsquare.hibour.pojos.message.UserMessage;
import com.dsquare.hibour.pojos.user.UserDetail;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {
  private static final String LOG_TAG = DatabaseHandler.class.getSimpleName();
  private Context context;

  public DatabaseHandler(Context context) {
    this.context = context;
  }

  public void insertUserMessage(UserMessage userMessage) {
    new UserMessageTable(userMessage).save();
  }

  public List<UserMessage> getUserMessage(String user_1, String user_2) {
    List<UserMessageTable> userMessageTableList = new Select().from(UserMessageTable.class)
        .where("(to_user = \"" + user_1 + "\" and from_user = \"" + user_2 + "\") or (to_user = \""
            + user_2 + "\" and from_user = \"" + user_1 + "\") ").orderBy("message_time DESC")
        .execute();
    List<UserMessage> userMessageList = new ArrayList<>();
    for (UserMessageTable message : userMessageTableList) {
      userMessageList.add(new UserMessage(message.from, message.to, message.message, message.date));
    }
    return userMessageList;
  }

  /* insert notifications into database*/
  public void insertNotificationIntoDatabase(String message) {
    new NotificationTable(message).save();
  }

  /*get notifications from database*/
  public List<NotificationTable> getListOfNotifications() {
    return new Select().from(NotificationTable.class).execute();
  }

  public void insertUserDetails(UserDetail userDetail) {
    new Delete().from(UserDetailTable.class).where(" user_id = " + userDetail.id).execute();
    new UserDetailTable(userDetail).save();
  }

  public UserDetail getUserDetail(int user_id) {
    UserDetailTable user = new Select().from(UserDetailTable.class).where("user_id = " + user_id).executeSingle();
    if (user == null)
      return null;
    return new UserDetail(user);
  }
}

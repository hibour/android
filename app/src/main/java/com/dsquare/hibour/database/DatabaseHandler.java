package com.dsquare.hibour.database;

import android.content.Context;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;
import com.dsquare.hibour.database.table.FeedsTable;
import com.dsquare.hibour.database.table.NotificationTable;
import com.dsquare.hibour.database.table.UserDetailTable;
import com.dsquare.hibour.database.table.UserMessageTable;
import com.dsquare.hibour.database.table.UserProfileTable;
import com.dsquare.hibour.pojos.message.UserMessage;
import com.dsquare.hibour.pojos.posts.PostData;
import com.dsquare.hibour.pojos.posts.Postpojos;
import com.dsquare.hibour.pojos.user.UserDetail;
import com.dsquare.hibour.pojos.user.UserProfile;
import com.dsquare.hibour.utils.Hibour;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseHandler {
  private static final String LOG_TAG = DatabaseHandler.class.getSimpleName();
  private Context context;
  private Hibour application;

  public DatabaseHandler(Context context) {
    this.context = context;
    this.application = Hibour.getInstance(context);
  }

  /* insert posts in posts table*/
  public void insertFeeds(PostData data) {
    List<Postpojos> feedsData = data.getData();
    for (Postpojos feed : feedsData) {
      new FeedsTable(feed.getPostId(), feed.getPostDate(), feed.getPostTime(), feed.getPostMessage()
          , feed.getPostImage(), feed.getUser().getId(), feed.getUser().getImage()
          , feed.getPostLikesCount() + "", feed.getUser().getName(), feed.getPostType()
          , feed.getPostComments().size() + "", feed.getPostUserLiked() + "").save();
    }

  }

  /**/
  public void insertUserMessage(UserMessage userMessage) {
    new UserMessageTable(userMessage, application.getUserId()).save();
  }

  public List<FeedsTable> getFeeds() {
    return new Select().from(FeedsTable.class).execute();
  }

  public List<UserMessage> getUserMessage(String user_1, String user_2) {
    List<UserMessageTable> userMessageTableList = new Select().from(UserMessageTable.class)
        .where("((to_user = \"" + user_1 + "\" and from_user = \"" + user_2 + "\") or (to_user = \""
            + user_2 + "\" and from_user = \"" + user_1 + "\") ) and session_user = "
            + application.getUserId()).orderBy("message_time DESC")
        .execute();
    List<UserMessage> userMessageList = new ArrayList<>();
    for (UserMessageTable message : userMessageTableList) {
      userMessageList.add(new UserMessage(message.local_id, message.from, message.to, message.message, message.message_state, message.date));
    }
    return userMessageList;
  }

  public UserMessage getRecentUserMessage(String user_1, String user_2) {
    List<UserMessageTable> userMessageTableList = new Select().from(UserMessageTable.class)
        .where("((to_user = \"" + user_1 + "\" and from_user = \"" + user_2 + "\") or (to_user = \""
            + user_2 + "\" and from_user = \"" + user_1 + "\") ) and session_user = "
            + application.getUserId()).orderBy("message_time DESC")
        .limit(1)
        .execute();
    for (UserMessageTable message : userMessageTableList) {
      return new UserMessage(message.local_id, message.from, message.to, message.message, message.message_state, message.date);
    }
    return null;
  }

  public void deleteUserMessage(String user_1, String user_2) {
    new Delete().from(UserMessageTable.class)
        .where("((to_user = \"" + user_1 + "\" and from_user = \"" + user_2 + "\") or (to_user = \""
            + user_2 + "\" and from_user = \"" + user_1 + "\") ) and session_user = "
            + application.getUserId())
        .execute();
  }

  public List<UserDetail> getChartUserList() {
    List<UserDetail> userDetailList = new ArrayList<>();
    HashMap<String, Integer> map = new HashMap<>();
    List<UserMessageTable> temp = new Select()
        .from(UserMessageTable.class)
        .where("session_user=" + application.getUserId())
        .groupBy("to_user, from_user")
        .having("max(local_id) = local_id")
        .orderBy("message_time DESC")
        .execute();

//    Log.e(LOG_TAG, "size:"+temp.size());
    for (UserMessageTable t : temp) {
      if (t.to.equalsIgnoreCase(application.getUserId())) {
        if (!map.containsKey(t.from)) {
          map.put(t.from, 1);
          addUserToList(userDetailList, t.from);
        }
      } else {
        if (!map.containsKey(t.to)) {
          map.put(t.to, 1);
          addUserToList(userDetailList, t.to);
        }
      }
    }
    return userDetailList;
  }

  private void addUserToList(List<UserDetail> userDetailList, String user_id) {
    UserDetail tempUser = getUserDetail(user_id);
    if (tempUser == null) {
      tempUser = new UserDetail();
      tempUser.id = user_id;
      tempUser.Username = "Hibour User";
    }
    userDetailList.add(tempUser);
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
    new Delete().from(UserDetailTable.class).where(" user_id = " + userDetail.id +
        " and session_user = " + application.getUserId()).execute();
    new UserDetailTable(userDetail, application.getUserId()).save();
  }

  public UserDetail getUserDetail(String user_id) {
    UserDetailTable user = new Select().from(UserDetailTable.class).where(" user_id = " + user_id +
        " and session_user = " + application.getUserId()).executeSingle();
    if (user == null)
      return null;
    return new UserDetail(user);
  }

  public List<UserDetail> getUserListContainKey(String key) {
    List<UserDetailTable> userList = new Select().from(UserDetailTable.class).where("username LIKE \"%" + key +
        "%\" and session_user = " + application.getUserId()).execute();
    List<UserDetail> userDetailList = new ArrayList<>();
    for (UserDetailTable user : userList) {
      userDetailList.add(new UserDetail(user));
    }
    return userDetailList;
  }

  public void insertUserProfile(UserProfile userProfile) {
    new UserProfileTable(userProfile).save();
  }

  public UserProfile getUserProfile() {
    UserProfileTable user = new Select().from(UserProfileTable.class).executeSingle();
    return new UserProfile(user);
  }

    /* update feeds data*/
    public void updateFeedsData(String likesCount,String isUserLiked,String postId){
        new Update(FeedsTable.class)
                .set("likescount ="+likesCount,"userliked = "+isUserLiked)
                .where("post_id = ?", postId)
                .execute();

    }
}

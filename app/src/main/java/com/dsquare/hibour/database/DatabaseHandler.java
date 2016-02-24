package com.dsquare.hibour.database;

import android.content.Context;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.dsquare.hibour.database.table.FeedsTable;
import com.dsquare.hibour.database.table.NotificationTable;
import com.dsquare.hibour.database.table.UserDetailTable;
import com.dsquare.hibour.database.table.UserMessageTable;
import com.dsquare.hibour.database.table.UserProfileTable;
import com.dsquare.hibour.pojos.message.UserMessage;
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
   /* public void insertFeeds(String postid, String date, String time,String description
            , String imgurl,FeedsUserTable posteduser){
        new FeedsTable(postid,date,time,description,imgurl,posteduser);
    }*/
    /**/
    public void insertUserMessage(UserMessage userMessage) {
        new UserMessageTable(userMessage, application.getUserId()).save();
    }
    public  List<FeedsTable> getFeeds(){
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
        new Delete().from(UserDetailTable.class).where(" user_id = " + userDetail.id).execute();
        new UserDetailTable(userDetail).save();
    }

    public UserDetail getUserDetail(String user_id) {
        UserDetailTable user = new Select().from(UserDetailTable.class).where("user_id = " + user_id).executeSingle();
        if (user == null)
            return null;
        return new UserDetail(user);
    }

    public void insertUserProfile(UserProfile userProfile){
        new UserProfileTable(userProfile).save();
    }
    public UserProfile getUserProfile(){
        UserProfileTable user = new Select().from(UserProfileTable.class).executeSingle();
        return new UserProfile(user);
    }
}

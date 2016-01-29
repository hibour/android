package com.dsquare.hibour.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.dsquare.hibour.R;
import com.dsquare.hibour.adapters.NotificationsAdapter;
import com.dsquare.hibour.database.DatabaseHandler;
import com.dsquare.hibour.database.table.NotificationTable;

import java.util.ArrayList;
import java.util.List;

public class Notifications extends AppCompatActivity implements View.OnClickListener {

    private ImageView backIcon;

    private List<NotificationTable> notificationsList = new ArrayList<>();
    private DatabaseHandler handler;
    private ListView notifList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        initializeViews();
        initializeEventListeners();
    }
    /*initialize views*/
    private void initializeViews(){
        backIcon = (ImageView)findViewById(R.id.notif_back_icon);
        handler = new DatabaseHandler(this);
        notifList = (ListView)findViewById(R.id.notifications_list);
        notifList.setAdapter(new NotificationsAdapter(this,getSampleData()));
    }
    /* initialize click listeners*/
    private void initializeEventListeners(){
        backIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.notif_back_icon:
                this.finish();
                break;
        }
    }
    /*get notifications from database*/
    public void getNotifsFrmDatabase() {
        notificationsList = handler.getListOfNotifications();
        notifList.setAdapter(new NotificationsAdapter(this, notificationsList));
    }
    /* get sample data to show*/
    private List<NotificationTable> getSampleData() {
        List<NotificationTable> data = new ArrayList<>();
        for(int i=0;i<10;i++){
            if(i<5){
                data.add(new NotificationTable("Sample notification data. Sample notifications data.", "2016-1-25", "unread"));
            }else{
                data.add(new NotificationTable("Sample notification data. Sample notifications data.", "2016-1-25", "read"));
            }
        }
        return data;
    }
}

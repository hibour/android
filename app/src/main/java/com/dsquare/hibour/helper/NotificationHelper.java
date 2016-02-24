package com.dsquare.hibour.helper;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import com.dsquare.hibour.R;
import com.dsquare.hibour.activities.Splash;
import com.dsquare.hibour.pojos.user.UserDetail;

import java.util.List;

public class NotificationHelper {

  private static final String LOG_TAG = NotificationHelper.class.getSimpleName();
  private Context context;

  public NotificationHelper(Context context) {
    this.context = context;
  }

  public void createNotification(UserDetail userDetail, String message, int notificationID, Intent intent) {
    Intent resultIntent;
    if (intent != null) {
      resultIntent = intent;
    } else {
      resultIntent = new Intent(context, Splash.class);
    }


    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
    stackBuilder.addParentStack(Splash.class);

    stackBuilder.addNextIntent(resultIntent);
    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
        PendingIntent.FLAG_UPDATE_CURRENT);

    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    Notification myNotification = new NotificationCompat.Builder(context)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setAutoCancel(true)
        .setContentIntent(resultPendingIntent)
        .setSound(alarmSound)
        .build();

    RemoteViews contentView = new RemoteViews(context.getPackageName(),
        R.layout.notification_layout);
    contentView.setTextViewText(R.id.user_name, userDetail.Username);
    contentView.setTextViewText(R.id.user_message, message);

    myNotification.contentView = contentView;

    NotificationManager notificationManager =
        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    notificationManager.notify(notificationID, myNotification);
  }

  public boolean isAppOnForeground() {
    ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
    if (appProcesses == null) {
      return false;
    }
    final String packageName = context.getPackageName();
    for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
      if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
        return true;
      }
    }
    return false;
  }

}

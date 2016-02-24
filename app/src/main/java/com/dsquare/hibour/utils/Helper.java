package com.dsquare.hibour.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Settings;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Helper {

  private Context context;

  public Helper(Context context) {
    this.context = context;
  }

  /* get today date*/
  public static String getTodayDate() {
    Calendar currentDate = Calendar.getInstance(); //Get the current date
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); //format it as per your requirement
    return formatter.format(currentDate.getTime());
  }

  public String getDeviceId() {
    return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
  }

  public String getUserEmail() {
    String []projection = new String[]{ContactsContract.CommonDataKinds.Email.ADDRESS};
    String selection = ContactsContract.Contacts.Data.MIMETYPE +"= '" + ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE + "'";

    ContentResolver content = context.getContentResolver();
    Cursor cursor = content.query(Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI, ContactsContract.Contacts.Data.CONTENT_DIRECTORY),
            projection, selection, null,
            null);

    if (cursor != null) {
      if(cursor.moveToNext()) {
        return cursor.getString(0);

      }
    }

    return null;
  }

  public String[] getName() {
    String []projection = new String[]{ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME};
    String selection = ContactsContract.Contacts.Data.MIMETYPE +"= '" + ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME  +"' OR " + ContactsContract.Contacts.Data.MIMETYPE +"= '" + ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME  +"' OR " + ContactsContract.Contacts.Data.MIMETYPE +"= '" + ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME  +"'";
    String []result = new String[3];

    ContentResolver content = context.getContentResolver();
    Cursor cursor = content.query(Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI, ContactsContract.Contacts.Data.CONTENT_DIRECTORY),
            projection, selection, null,
            null);

    if (cursor != null) {
      if(cursor.moveToNext()) {
        result[0] = cursor.getString(0);
        result[1] = cursor.getString(1);
        result[2] = cursor.getString(2);

      }
    }

    return result;
  }

}

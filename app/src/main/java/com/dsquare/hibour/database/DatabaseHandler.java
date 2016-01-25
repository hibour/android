package com.dsquare.hibour.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dsquare.hibour.utils.Constants;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Aditya Ravikanti on 1/25/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private SQLiteDatabase database;
    private Context context;

    public DatabaseHandler(Context context) {
        super(context, context.getFilesDir() + File.separator + Constants.DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String messagesQuery = "CREATE TABLE IF NOT EXISTS notifications(message TEXT, date TEXT)";
        db.execSQL(messagesQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS notifications");
        onCreate(db);
    }

    /* initialize database*/
    private void initializeDatabase(){
        if(database == null){
            database = getWritableDatabase();
        }
    }
    /* insert notifications into database*/
    public void insertNotificationIntoDatabase(String message){
        initializeDatabase();
        ContentValues cv = new ContentValues();
        try{
            cv.put("message",message);
            cv.put("date",getTodayDate());
            database.insert("gcm_notifications",null,cv);
            Log.d("notification", "inserted");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /* get today date*/
    public String getTodayDate(){
        Calendar currentDate = Calendar.getInstance(); //Get the current date
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd"); //format it as per your requirement
        return formatter.format(currentDate.getTime());
    }
    /*get notifications from database*/
    public List<String[]> getListOfNotifications(){
        initializeDatabase();
        List<String[]> gcmList = new ArrayList<String[]>();
        String getQuery = "SELECT * FROM notifications";
        Cursor cursor = database.rawQuery(getQuery, null);
        if(cursor != null && cursor.moveToFirst()){
            do{
                String[] message = new String[2];
                message[0] = cursor.getString(cursor.getColumnIndex("message"));
                message[1] = cursor.getString(cursor.getColumnIndex("date"));
                gcmList.add(message);
            }while (cursor.moveToNext());
        }
        return gcmList;
    }
}

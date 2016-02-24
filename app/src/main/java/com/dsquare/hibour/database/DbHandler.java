package com.dsquare.hibour.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dsquare.hibour.pojos.posts.Postpojos;
import com.dsquare.hibour.utils.Constants;

import java.io.File;
import java.util.List;

/**
 * Created by Aditya Ravikanti on 2/16/2016.
 */
public class DbHandler extends SQLiteOpenHelper {
    private SQLiteDatabase database;
    private Context context;

    public DbHandler(Context context) {
        super(context, context.getFilesDir() + File.separator + Constants.DATABASE_NAME, null, 1);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String dbQuery = "CREATE TABLE IF NOT EXISTS feeds(feedId TEXT, feedMessage TEXT, feedImage TEXT" +
                ", feedDate TEXT, feedTime , commsCount INTEGER, likesCount INTEGER, feedType TEXT, feedUserId TEXT" +
                ", feedUserName TEXT, feedUserImage TEXT)";
        db.execSQL(dbQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS feeds");
        onCreate(db);
    }

    private void initializeDatabase(){
        if(database == null){
            database = getWritableDatabase();
        }
    }
   /* public boolean insertPillsIntoDatabase(List<Postpojos> data){
        ContentValues cv = new ContentValues();
        if(database == null){
            initializeDatabase();
        }
        for(Postpojos feed : data){
            Log.d("indb", "yes");
            String dbQuery = "SELECT * FROM pillsInfo WHERE prescpid = '"+singleTablet.getPrescriptionId()+"'";
            Cursor cursor = database.rawQuery(dbQuery, null);
            Log.d("count",cursor.getCount()+"");
            if(!(cursor.getCount()>0)){
                for(Tablet tablet : singleTablet.getTablets()){
                    try{
                        cv.put("prescpid",singleTablet.getPrescriptionId());
                        cv.put("pillName", tablet.getTabletName());
                        cv.put("startDate", singleTablet.getPrescriptionDate());
                        if(tablet.getNumberOfDays().equals("0")){
                            cv.put("endDate", singleTablet.getPrescriptionDate());
                        }else{
                            cv.put("endDate", getDateFormTodayDate(tablet.getNumberOfDays()));
                        }
                        cv.put("morning", tablet.getBreakfast());
                        cv.put("lunch", tablet.getLunch());
                        cv.put("night", tablet.getDinner());
                        cv.put("mab", tablet.getAfterBeforeBreakfast());
                        cv.put("lab", tablet.getAfterBeforeLunch());
                        cv.put("nab", tablet.getAfterBeforeDinner());
                        database.insert("pillsInfo", null, cv);
                        Log.d("tabletes", "inserted");
                    }catch (Exception e){
                        e.printStackTrace();
                        break;
                    }
                }
                insertRecordIntoPillMonitor(singleTablet.getTablets()
                        ,singleTablet.getPrescriptionDate()
                        ,singleTablet.getPrescriptionId());
            }
            cursor.close();

            //Toast.makeText(context, "Pills updated successfully", Toast.LENGTH_SHORT).show();
        }
        return false;
    }*/
}

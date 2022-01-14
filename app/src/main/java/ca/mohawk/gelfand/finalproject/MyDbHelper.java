package ca.mohawk.gelfand.finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDbHelper extends SQLiteOpenHelper {

    public static final String TAG = "==MyDbHelper==";

    // Database fields
    public static final String DATABASE_FILE_NAME = "MyDatabase.db";
    public static final int DATABASE_VERSION = 1;
    public static final String MYTABLE = "mytable";
    public static final String ID = "_id";
    public static final String COURSECODE = "code";
    public static final String COURSENAME = "title";
    public static final String COURSEPROGRAM = "program";
    public static final String COURSEHOURS = "hours";
    public static final String COURSEDESCRIPTION = "description";

    // Create SQL Table
    private static final String SQL_CREATE = "CREATE TABLE " + MYTABLE + " ( " + ID + " INTEGER PRIMARY KEY, " + COURSECODE + " TEXT, "
            + COURSENAME + " TEXT, " + COURSEPROGRAM + " TEXT, " + COURSEHOURS + " TEXT, " + COURSEDESCRIPTION + " TEXT) ";

    public MyDbHelper(Context context) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "constructor");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate " + SQL_CREATE);
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This is only called if the database changes
    }
}

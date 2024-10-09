package com.example.habittrackerapp;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DefaultHabitsManager extends SQLiteOpenHelper {
    private static DatabaseHelper databaseHelper;

    public static final String DATABASE_NAME = "habits.db";
    public static final String TABLE_NAME = "default_habits";
    private static final int DATABASE_VERSION = 2;

    public DefaultHabitsManager(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " ( " +
                "defaultHabitID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, description TEXT, TEXT, INTEGER DEFAULT 0)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }


}

package com.example.habittrackerapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Information
    private static final String DATABASE_NAME = "habits.db";
    private static final int DATABASE_VERSION = 3;

    // Habits Table
    public static final String TABLE_NAME = "habits";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_TRACKING_TYPE = "trackingType";
    public static final String COLUMN_HABIT_TYPE = "habitType";
    public static final String COLUMN_CURRENT_QUANTITY = "currentQuantity";
    public static final String COLUMN_GOAL_QUANTITY = "goalQuantity";
    public static final String COLUMN_GOAL_PERIOD = "goalPeriod";
    public static final String COLUMN_FREQUENCY = "frequency";
    public static final String COLUMN_REMINDER = "reminder";
    public static final String COLUMN_NOTES = "notes";
    public static final String COLUMN_COMPLETION_STATUS = "completionStatus";

    // ActiveHabitsStats Table
    public static final String TABLE_ACTIVE_HABITS_STATS = "ActiveHabitsStats";
    public static final String COLUMN_HABIT_STATS_DATE = "HabitStatsDate";
    public static final String COLUMN_DAYS_DONE_IN_MONTH = "DaysDoneInMonth";
    public static final String COLUMN_DAYS_TOTAL_DONE = "DaysTotalDone";
    public static final String COLUMN_DAYS_CURRENT_STREAK = "DaysCurrentStreak";
    public static final String COLUMN_DAYS_BEST_STREAK = "DaysBestStreak";
    public static final String COLUMN_GOAL_QUANTITY_TOTAL = "GoalQuantityTotal";
    public static final String COLUMN_GOAL_QUANTITY_DAILY_AVG = "GoalQuantityDailyAvg";
    public static final String COLUMN_OVERALL_RATE = "OverallRate";

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create habits table
        String createHabitsTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_TRACKING_TYPE + " TEXT, " +
                COLUMN_HABIT_TYPE + " TEXT, " +
                COLUMN_CURRENT_QUANTITY + "TEXT DEFAULT 0," +
                COLUMN_GOAL_QUANTITY + " TEXT, " +
                COLUMN_GOAL_PERIOD + " TEXT, " +
                COLUMN_FREQUENCY + " TEXT, " +
                COLUMN_REMINDER + " TEXT, " +
                COLUMN_NOTES + " TEXT, " +
                COLUMN_COMPLETION_STATUS + " INTEGER DEFAULT 0)";
        db.execSQL(createHabitsTable);

        // Create ActiveHabitsStats table
        String createActiveHabitsStatsTable = "CREATE TABLE " + TABLE_ACTIVE_HABITS_STATS + " (" +
                COLUMN_ID + " INTEGER, " +
                COLUMN_HABIT_STATS_DATE + " TEXT, " +
                COLUMN_DAYS_DONE_IN_MONTH + " INTEGER, " +
                COLUMN_DAYS_TOTAL_DONE + " INTEGER, " +
                COLUMN_DAYS_CURRENT_STREAK + " INTEGER, " +
                COLUMN_DAYS_BEST_STREAK + " INTEGER, " +
                COLUMN_GOAL_QUANTITY_TOTAL + " INTEGER, " +
                COLUMN_GOAL_QUANTITY_DAILY_AVG + " REAL, " +
                COLUMN_OVERALL_RATE + " REAL, " +
                COLUMN_NOTES + " TEXT, " +
                "PRIMARY KEY (" + COLUMN_ID + ", " + COLUMN_HABIT_STATS_DATE + "))";
        db.execSQL(createActiveHabitsStatsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVE_HABITS_STATS);
            onCreate(db);
        }
    }

    // Insert habit
    public long insertHabitDB(String name, String description, String trackingType, String habitType,
                            String goalQuantity, String goalPeriod, String frequency, String reminder, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_TRACKING_TYPE, trackingType);
        values.put(COLUMN_HABIT_TYPE, habitType);
        values.put(COLUMN_GOAL_QUANTITY, goalQuantity);
        values.put(COLUMN_GOAL_PERIOD, goalPeriod);
        values.put(COLUMN_FREQUENCY, frequency);
        values.put(COLUMN_REMINDER, reminder);
        values.put(COLUMN_NOTES, notes);
        return db.insert(TABLE_NAME, null, values);
    }

    // Insert habit
    public long insertHabit(String name, String description, String trackingType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_TRACKING_TYPE, trackingType);
        return db.insert(TABLE_NAME, null, values);
    }

    // Update habit
    public int updateHabitQuantity(String name, int goalQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_GOAL_QUANTITY, goalQuantity);
        return db.update(TABLE_NAME, values, COLUMN_NAME + " = ?", new String[]{String.valueOf(name)});
    }

    // Update habit
    public int updateHabit(int id, String name, String description, String trackingType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_TRACKING_TYPE, trackingType);
        return db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // Update habit completion status
    public int updateCompletionStatus(int id, boolean isComplete) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COMPLETION_STATUS, isComplete ? 1 : 0);
        return db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // Delete habit
    public int deleteHabit(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // Fetch all habits
    public Cursor getAllHabits() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    // Fetch all habits
    public Cursor getHabitsByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME + "=?",
                new String[]{String.valueOf(name)});
    }

    // Insert or update habit statistics
    public void updateHabitStats(int habitId, String date, int daysDone, int daysTotal, int streak, int bestStreak, float overallRate, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, habitId);
        values.put(COLUMN_HABIT_STATS_DATE, date);
        values.put(COLUMN_DAYS_DONE_IN_MONTH, daysDone);
        values.put(COLUMN_DAYS_TOTAL_DONE, daysTotal);
        values.put(COLUMN_DAYS_CURRENT_STREAK, streak);
        values.put(COLUMN_DAYS_BEST_STREAK, bestStreak);
        values.put(COLUMN_OVERALL_RATE, overallRate);
        values.put(COLUMN_NOTES, notes);

        db.insertWithOnConflict(TABLE_ACTIVE_HABITS_STATS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    // Fetch habit statistics by habit ID
    public Cursor getHabitStats(int habitId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ACTIVE_HABITS_STATS + " WHERE " + COLUMN_ID + "=?", new String[]{String.valueOf(habitId)});
    }
    // Check if there are any habit statistics
    public boolean hasUserStatistics() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_ACTIVE_HABITS_STATS, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

    // Fetch latest habit statistics for each habit along with the habit name
    public Cursor getAllUserStatistics() {
        SQLiteDatabase db = this.getReadableDatabase();

        // Use a query to fetch the latest entry for each habit, including the habit name
        String query = "SELECT stats.*, habits." + COLUMN_NAME + " FROM " + TABLE_ACTIVE_HABITS_STATS + " AS stats " +
                "JOIN " + TABLE_NAME + " AS habits ON stats." + COLUMN_ID + " = habits." + COLUMN_ID + " " +
                "WHERE stats." + COLUMN_HABIT_STATS_DATE + " = (" +
                "  SELECT MAX(" + COLUMN_HABIT_STATS_DATE + ") " +
                "  FROM " + TABLE_ACTIVE_HABITS_STATS + " AS innerStats " +
                "  WHERE innerStats." + COLUMN_ID + " = stats." + COLUMN_ID + ")";

        return db.rawQuery(query, null);
    }


    // This entire method is for testing purposes to see if statistics display works
    // This can be deleted in the final version but is currently useful for Stats view and initial habits
    public void insertSampleData() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if the sample habits already exist
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME + " = ?", new String[]{"Read a Book"});
        cursor.moveToFirst();
        int habitCount = cursor.getInt(0);
        cursor.close();

        if (habitCount == 0) {  // If the habit does not exist, insert sample habits
            // Insert sample habits
            long habit1Id = insertHabit("Read a Book", "Read 30 pages of a book", "daily");
            long habit2Id = insertHabit("Exercise", "Exercise for 30 minutes", "daily");

            // Insert sample statistics for Habit 1 (Read a Book) if they do not already exist
            Cursor statsCursor1 = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_ACTIVE_HABITS_STATS + " WHERE " + COLUMN_ID + " = ?", new String[]{String.valueOf(habit1Id)});
            statsCursor1.moveToFirst();
            int statsCount1 = statsCursor1.getInt(0);
            statsCursor1.close();

            if (statsCount1 == 0) {
                updateHabitStats((int) habit1Id, "2024-01-01", 5, 50, 5, 10, 0.8f, "Keep it up!");
                updateHabitStats((int) habit1Id, "2024-02-01", 4, 30, 4, 7, 0.75f, "Doing well");
            }

            // Insert sample statistics for Habit 2 (Exercise) if they do not already exist
            Cursor statsCursor2 = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_ACTIVE_HABITS_STATS + " WHERE " + COLUMN_ID + " = ?", new String[]{String.valueOf(habit2Id)});
            statsCursor2.moveToFirst();
            int statsCount2 = statsCursor2.getInt(0);
            statsCursor2.close();

            if (statsCount2 == 0) {
                updateHabitStats((int) habit2Id, "2024-01-01", 10, 100, 3, 5, 0.9f, "Great effort!");
                updateHabitStats((int) habit2Id, "2024-02-01", 6, 70, 2, 4, 0.85f, "Needs improvement");
            }
        }
    }
}
package com.example.habittrackerapp;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class StatsActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        // Initialize the database helper
        dbHelper = new DatabaseHelper(this);

        // Insert sample data for testing (should remove later)
        dbHelper.insertSampleData();

        // Initialize views
        TextView statsView = findViewById(R.id.statsView);
        Button btnBack = findViewById(R.id.btnBack);

        // Check if user statistics exist
        if (dbHelper.hasUserStatistics()) {
            displayUserStatistics(statsView);
        }
        // Set the back button functionality
        btnBack.setOnClickListener(v -> finish());
    }


    private void displayUserStatistics(TextView statsView) {
        Cursor cursor = dbHelper.getAllUserStatistics();  // Now returns the habit name along with statistics
        StringBuilder statsBuilder = new StringBuilder();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Retrieve data from cursor
                String habitName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
                String date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_HABIT_STATS_DATE));
                int daysDone = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_DAYS_DONE_IN_MONTH));
                int totalDays = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_DAYS_TOTAL_DONE));
                int currentStreak = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_DAYS_CURRENT_STREAK));
                int bestStreak = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_DAYS_BEST_STREAK));
                float overallRate = cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.COLUMN_OVERALL_RATE));

                // Append data to statsBuilder
                statsBuilder.append("Habit: ").append(habitName)  // Display habit name
                        .append("\nDate: ").append(date)
                        .append("\nDays Done in Month: ").append(daysDone)
                        .append("\nTotal Days Done: ").append(totalDays)
                        .append("\nCurrent Streak: ").append(currentStreak)
                        .append("\nBest Streak: ").append(bestStreak)
                        .append("\nOverall Rate: ").append(overallRate)
                        .append("\n\n");
            } while (cursor.moveToNext());

            cursor.close();
        } else {
            statsBuilder.append("No statistics found.");
        }
        statsView.setText(statsBuilder.toString());
    }

}



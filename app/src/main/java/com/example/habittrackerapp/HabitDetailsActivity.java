package com.example.habittrackerapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HabitDetailsActivity extends AppCompatActivity {

    private AlertDialog dialog;
    private TextView habitNameTextView;
    private CardView counterCardView;
    private TextView completedTextView;
    private TextView totalTextView;
    private Button notesButton;
    private Button statsButton;
    private ImageView backImageView;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_habit_details);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");

        backImageView = findViewById(R.id.back_button);
        habitNameTextView = findViewById(R.id.habit_name_text_view);
        counterCardView = findViewById(R.id.counter_card_view);
        completedTextView = findViewById(R.id.completed_text_view);
        totalTextView = findViewById(R.id.total_text_view);
        notesButton = findViewById(R.id.habit_notes_bttn);
        statsButton = findViewById(R.id.habit_stats_bttn);

//        if(name != null){
//            dbHelper = new DatabaseHelper(this);
//            Cursor cursor = dbHelper.getHabitsByName(name);
//
//            String completedQuantity = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CURRENT_QUANTITY));
//            String totalQuantity = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_GOAL_QUANTITY_TOTAL));
//
//            completedTextView.setText(completedQuantity);
//            totalTextView.setText(totalQuantity);
//        }


        // Set onClickListener for back_button
        backImageView.setOnClickListener(v -> {
            goBack();
        });

        // Set habit_name_text_view with name
        habitNameTextView.setText(name);

        // Set counter_view on click listener to increment goalQuantity
        counterCardView.setOnClickListener(v -> {
            incrementGoalQuantity();
        });

        // Set onClickListener for Notes button
        notesButton.setOnClickListener(v -> {
            displayNotesDialog();
        });

        // Set onClickListener for stats button
        statsButton.setOnClickListener(v -> {
            displayCurrentStatsActivity();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void incrementGoalQuantity() {
        dbHelper = new DatabaseHelper(this);

        String name = habitNameTextView.getText().toString();

        int completedQuantity = Integer.parseInt(completedTextView.getText().toString());
        int totalQuantity = Integer.parseInt(totalTextView.getText().toString());

        if(completedQuantity < totalQuantity){
            completedQuantity++;
            dbHelper.updateHabitQuantity(name, completedQuantity);
            Log.d("Debug: ", String.valueOf(completedQuantity));
            completedTextView.setText(String.valueOf(completedQuantity));
        }
    }

    private void goBack() {
        finish();
    }

    // Display a StatsActivity for the current habit.
    private void displayCurrentStatsActivity() {
        Intent intent = new Intent(HabitDetailsActivity.this, HabitStatsActivity.class);
        startActivity(intent); // Start StatsActivity to show statistics
    }

    // Display the NotesDialog for the current habit.
    private void displayNotesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set fragment_notes view
        View view = getLayoutInflater().inflate(R.layout.fragment_notes, null);
        builder.setView(view);

        // TO-DO: Create save button to update notes

        // Set cancel button and show the dialog
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> dialog.dismiss());
        dialog = builder.create();
        dialog.show();
    }

}
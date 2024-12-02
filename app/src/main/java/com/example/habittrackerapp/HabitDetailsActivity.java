package com.example.habittrackerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

public class HabitDetailsActivity extends AppCompatActivity {

    private AlertDialog dialog;
    private TextView habitNameTextView;
    private Button notesButton;
    private Button statsButton;
    private ImageView backImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_habit_details);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");

        backImageView = findViewById(R.id.back_button);
        habitNameTextView = findViewById(R.id.habit_name_text_view);
        notesButton = findViewById(R.id.habit_notes_bttn);
        statsButton = findViewById(R.id.habit_stats_bttn);

        // Set onClickListener for back_button
        backImageView.setOnClickListener(v -> {
            goBack();
        });

        // Set habit_name_text_view with name
        habitNameTextView.setText(name);

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
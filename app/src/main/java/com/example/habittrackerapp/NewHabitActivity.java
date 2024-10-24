package com.example.habittrackerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.habittrackerapp.databinding.ActivityNewHabitBinding;

public class NewHabitActivity extends AppCompatActivity {

    private TextView titleTextView;
    private EditText nameEditText;
    private EditText descriptionEditText;
    private EditText habitTypeEditText;
    private EditText goalQuantityEditText;
    private EditText goalPeriodEditText;
    private EditText frequencyEditText;
    private EditText reminderTimeEditText;
    private EditText notesEditText;

    private ActivityNewHabitBinding binding;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_habit);
        binding = ActivityNewHabitBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.completeButton.setOnClickListener(view1 -> {
            newHabitComplete();
        });

        Intent intent = getIntent();
        String habitName = intent.getStringExtra("habit_name");
        String habitType = intent.getStringExtra("habit_type");

        // Loads habit name and type to new habit
        if(habitName != null && habitType != null){
            titleTextView = findViewById(R.id.title_text_view);
            nameEditText = findViewById(R.id.name_edit_text);
            habitTypeEditText = findViewById(R.id.habit_type_edit_text);

            titleTextView.setText(habitName);
            nameEditText.setText(habitName);
            habitTypeEditText.setText(habitType);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // newHabitComplete: Adds new habit to sqlite database
    public void newHabitComplete(){
        dbHelper = new DatabaseHelper(this);

        nameEditText = findViewById(R.id.name_edit_text);
        descriptionEditText = findViewById(R.id.description_edit_text);
        habitTypeEditText = findViewById(R.id.habit_type_edit_text);
        goalQuantityEditText = findViewById(R.id.goal_quantity_edit_text);
        goalPeriodEditText = findViewById(R.id.goal_period_edit_text);
        frequencyEditText = findViewById(R.id.frequency_edit_text);
        reminderTimeEditText = findViewById(R.id.reminder_time_edit_text);
        notesEditText = findViewById(R.id.notes_edit_text);

        String habitName = String.valueOf(nameEditText.getText());
        String habitDescription = String.valueOf(descriptionEditText.getText());
        String habitType = String.valueOf(habitTypeEditText.getText());
        String habitGoalQuantity = String.valueOf(goalQuantityEditText.getText());
        String habitGoalPeriod = String.valueOf(goalPeriodEditText.getText());
        String habitFrequency = String.valueOf(frequencyEditText.getText());
        String habitReminder = String.valueOf(reminderTimeEditText.getText());
        String habitNotes = String.valueOf(notesEditText.getText());

        // Validate input
        if (habitName.isEmpty() ||  habitType.isEmpty()) {
            return; // Prevent saving if input is invalid
        }

        long id = dbHelper.insertHabit(habitName, habitDescription, habitType);
        Habit habit = new Habit((int) id, habitName, habitDescription, habitType, false);

        finish();
    }
}
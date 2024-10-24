package com.example.habittrackerapp;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    // Define main variables
    private Button add;
    private AlertDialog dialog;
    private LinearLayout layout;
    private DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get widgets from view
        add = findViewById(R.id.btnAdd);
        layout = findViewById(R.id.container);
        dbHelper = new DatabaseHelper(this);

        // Call method to create form from dialog.xml when button is clicked
//        buildDialog();
//        add.setOnClickListener(v -> dialog.show());

        // Displays dialog alert with list of habits to build or quit
        add.setOnClickListener(v -> newHabitListBuildDialog());
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        // Load existing habits from the database
        loadHabitsFromDatabase();

        if(dialog != null){
            dialog.dismiss();
        }
    }

    // Load habits from the SQLite database and display them as cards
    private void loadHabitsFromDatabase() {
        layout.removeAllViews();

        Cursor cursor = dbHelper.getAllHabits();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String description = cursor.getString(2);
            String trackingType = cursor.getString(3);
            boolean isComplete = cursor.getInt(4) == 1;  // 1 = Complete, 0 = Incomplete

            Habit habit = new Habit(id, name, description, trackingType, isComplete);
            addCard(habit);
        }
        cursor.close();
    }

    // Create AlertDialog with list of habits to build or quit
    public void newHabitListBuildDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.new_habit_list, null);
        builder.setView(view);

        TabLayout tabLayout = view.findViewById(R.id.newHabitListTabLayout);
        ViewPager2 viewPager2 = view.findViewById(R.id.view_pager);

        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter((FragmentActivity) this);
        viewPager2.setAdapter(myViewPagerAdapter);

        // Set up tab selection listener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Set cancel button and show the dialog
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
            dialog.dismiss();
        });

        dialog = builder.create();
        dialog.show();
    }

    // Create the buildDialog function to show form for dialog.xml when add button clicked
    public void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog, null);

        final EditText name = view.findViewById(R.id.etName);
        final EditText description = view.findViewById(R.id.etDescription);
        final Spinner trackingSpinner = view.findViewById(R.id.spinnerTracking);

        setupTrackingSpinner(trackingSpinner);

        builder.setView(view)
                .setTitle("Enter your habit")
                .setPositiveButton("Save", (dialog, which) -> {
                    String habitName = name.getText().toString();
                    String habitDescription = description.getText().toString();
                    String trackingType = trackingSpinner.getSelectedItem().toString();

                    // Validate input
                    if (habitName.isEmpty() || habitDescription.isEmpty()) {
                        return; // Prevent saving if input is invalid
                    }

                    // Insert into the database
                    long id = dbHelper.insertHabit(habitName, habitDescription, trackingType);
                    Habit habit = new Habit((int) id, habitName, habitDescription, trackingType, false);
                    addCard(habit); // Newly created, not complete
                })
                .setNegativeButton("Cancel", null);

        dialog = builder.create();
    }

    // Define addCard to display a card with habit data
    private void addCard(Habit habit) {
        // Use card.xml to display habits
        final View view = getLayoutInflater().inflate(R.layout.card, null);

        // Set fields in buttons to display in each habit
        TextView nameView = view.findViewById(R.id.txtName);
        TextView descriptionView = view.findViewById(R.id.txtDescription);
        TextView trackingView = view.findViewById(R.id.txtTrackingType);
        CheckBox completeCheckBox = view.findViewById(R.id.checkBoxComplete);
        Button delete = view.findViewById(R.id.btnDelete);
        Button edit = view.findViewById(R.id.btnEdit);

        String tracking = "Tracking: " + habit.getTrackingType();

        nameView.setText(habit.getName());
        descriptionView.setText(habit.getDescription());
        trackingView.setText(tracking);
        completeCheckBox.setChecked(habit.isComplete());

        // Set click listeners for delete, edit, and completion checkbox
        delete.setOnClickListener(v -> {
            dbHelper.deleteHabit(habit.getId());
            layout.removeView(view);
        });

        edit.setOnClickListener(v -> showEditDialog(habit, view));

        completeCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            dbHelper.updateCompletionStatus(habit.getId(), isChecked);
            // Later add this information to another table in the database?
            // Would be good to track it somehow for graphs later on
        });

        layout.addView(view);
    }

    // Show edit dialog for a card (habit)
    private void showEditDialog(Habit habit, View oldView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog, null);

        final EditText editName = dialogView.findViewById(R.id.etName);
        final EditText editDescription = dialogView.findViewById(R.id.etDescription);
        final Spinner editTracking = dialogView.findViewById(R.id.spinnerTracking);

        setupTrackingSpinner(editTracking);

        editName.setText(habit.getName());
        editDescription.setText(habit.getDescription());

        String currentTrackingType = habit.getTrackingType();
        int spinnerPosition = ((ArrayAdapter) editTracking.getAdapter()).getPosition(currentTrackingType);
        editTracking.setSelection(spinnerPosition);

        builder.setView(dialogView)
                .setTitle("Edit your habit")
                .setPositiveButton("Save", (dialog, which) -> {
                    String newName = editName.getText().toString();
                    String newDescription = editDescription.getText().toString();
                    String newTrackingType = editTracking.getSelectedItem().toString();

                    habit.setName(newName);
                    habit.setDescription(newDescription);
                    habit.setTrackingType(newTrackingType);

                    dbHelper.updateHabit(habit.getId(), newName, newDescription, newTrackingType);

                    // Clear the old card view and add the updated one
                    layout.removeView(oldView);
                    addCard(habit);
                })
                .setNegativeButton("Cancel", null);

        builder.create().show();
    }

    // Setup spinner for tracking type (Daily, Weekly, Monthly)
    private void setupTrackingSpinner(Spinner spinner) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.tracking_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close(); // Close the database connection
    }
}

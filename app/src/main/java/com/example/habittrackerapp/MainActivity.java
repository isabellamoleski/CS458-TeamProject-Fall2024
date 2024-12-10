package com.example.habittrackerapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // Define main variables
    private Button add;
    private Button stats; // Added button for statistics
    private AlertDialog dialog;
    private LinearLayout layout;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get widgets from view
        add = findViewById(R.id.btnAdd);
        stats = findViewById(R.id.btnStats);
        layout = findViewById(R.id.container);
        dbHelper = new DatabaseHelper(this);

        // Set onClickListener for Add Habit button
        add.setOnClickListener(v -> newHabitListBuildDialog());

        // Set onClickListener for Stats button
        stats.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StatsActivity.class);
            startActivity(intent); // Start StatsActivity to show statistics
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        // Load existing habits from the database
        loadHabitsFromDatabase();

        // Dismiss any dialogs if needed
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    // Load habits from the SQLite database and display them as cards
    private void loadHabitsFromDatabase() {
        layout.removeAllViews(); // Clear previous views

        Cursor cursor = dbHelper.getAllHabits(); // Get habits from the database
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String description = cursor.getString(2);
            String trackingType = cursor.getString(3);
            boolean isComplete = cursor.getInt(4) == 1; // 1 = Complete, 0 = Incomplete

            Habit habit = new Habit(id, name, description, trackingType, isComplete);
            addCard(habit); // Display the habit as a card
        }
        cursor.close();
    }

    // Create AlertDialog with list of habits to build or quit
    public void newHabitListBuildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.new_habit_list, null);
        builder.setView(view);

        // Set up ViewPager for tabbed habits
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
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> dialog.dismiss());
        dialog = builder.create();
        dialog.show();
    }

    // Define addCard to display a card with habit data
    private void addCard(Habit habit) {
        final View view = getLayoutInflater().inflate(R.layout.card, null);

        // Set habit details in the card
        TextView nameView = view.findViewById(R.id.txtName);
        TextView descriptionView = view.findViewById(R.id.txtDescription);
        TextView trackingView = view.findViewById(R.id.txtTrackingType);
        CheckBox checkBoxComplete = view.findViewById(R.id.checkBoxComplete); // Completion checkbox
        Button btnDelete = view.findViewById(R.id.btnDelete);
        Button btnEdit = view.findViewById(R.id.btnEdit);
        
        nameView.setText(habit.getName());
        descriptionView.setText(habit.getDescription());
        trackingView.setText("Tracking: " + habit.getTrackingType());
        checkBoxComplete.setChecked(habit.isComplete());

        // Handle view navigation to HabitDetailsActivity
        view.setOnClickListener(view1 -> {
            displayHabitDetails(habit.getName());
        });

        // Handle habit deletion
        btnDelete.setOnClickListener(v -> {
            showDeleteConfirmationDialog(habit, view);
            dbHelper.deleteHabit(habit.getId()); // Delete from database
            loadHabitsFromDatabase(); // Reload habits after deletion
        });

        // Handle habit editing
        btnEdit.setOnClickListener(v -> showEditDialog(habit, view));

        // Handle habit completion checkbox
        checkBoxComplete.setOnCheckedChangeListener((buttonView, isChecked) -> {
            dbHelper.updateCompletionStatus(habit.getId(), isChecked);
        });

        layout.addView(view); // Add the card to the layout
    }

    private void showDeleteConfirmationDialog(Habit habit, View cardView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Habit")
                .setMessage("Are you sure you want to delete \"" + habit.getName() + "\"? This action cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    // Delete from database
                    int result = dbHelper.deleteHabit(habit.getId());
                    if (result > 0) {
                        // Successfully deleted, remove the card
                        layout.removeView(cardView);

                        // Optional: Show confirmation toast
                        Toast.makeText(MainActivity.this,
                                "Habit deleted successfully",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // Show error if deletion failed
                        Toast.makeText(MainActivity.this,
                                "Error deleting habit",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setIcon(android.R.drawable.ic_dialog_alert);

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    // Display the HabitDetailsActivity
    private void displayHabitDetails(String name) {
        Intent intent = new Intent(MainActivity.this, HabitDetailsActivity.class);

        // Pass habit's name
        intent.putExtra("name", name);

        startActivity(intent); // Start HabitDetailsActivity
    }

    // Show edit dialog for a habit card
    private void showEditDialog(Habit habit, View oldView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog, null);

        final EditText editName = dialogView.findViewById(R.id.etName);
        final EditText editDescription = dialogView.findViewById(R.id.etDescription);
        final Spinner editTracking = dialogView.findViewById(R.id.spinnerTracking);

        setupTrackingSpinner(editTracking); // Populate spinner with tracking types

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

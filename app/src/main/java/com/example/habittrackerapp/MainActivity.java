package com.example.habittrackerapp;

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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

// Github push test comment - disregard

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

        add = findViewById(R.id.btnAdd);
        layout = findViewById(R.id.container);
        dbHelper = new DatabaseHelper(this);

        // Load existing habits from the database
        loadHabitsFromDatabase();

        // Call method to create form from dialog.xml when button is clicked
        buildDialog();
        add.setOnClickListener(v -> dialog.show());
    }

    // Load habits from the SQLite database and display them as cards
    private void loadHabitsFromDatabase() {
        Cursor cursor = dbHelper.getAllHabits();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String description = cursor.getString(2);
            String trackingType = cursor.getString(3);
            boolean isComplete = cursor.getInt(4) == 1;  // 1 = Complete, 0 = Incomplete

            addCard(id, name, description, trackingType, isComplete);
        }
        cursor.close();
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

                    // Insert into the database
                    long id = dbHelper.insertHabit(habitName, habitDescription, trackingType);
                    addCard((int) id, habitName, habitDescription, trackingType, false); // Newly created, not complete
                })
                .setNegativeButton("Cancel", null);

        dialog = builder.create();
    }

    // Define addCard to display a card with habit data
    private void addCard(int id, String name, String description, String trackingType, boolean isComplete) {
        // Use card.xml to display habits
        final View view = getLayoutInflater().inflate(R.layout.card, null);

        // Set fields in buttons to display in each habit
        TextView nameView = view.findViewById(R.id.txtName);
        TextView descriptionView = view.findViewById(R.id.txtDescription);
        TextView trackingView = view.findViewById(R.id.txtTrackingType);
        CheckBox completeCheckBox = view.findViewById(R.id.checkBoxComplete);
        Button delete = view.findViewById(R.id.btnDelete);
        Button edit = view.findViewById(R.id.btnEdit);

        nameView.setText(name);
        descriptionView.setText(description);
        trackingView.setText("Tracking: " + trackingType);
        completeCheckBox.setChecked(isComplete);

        // Set click listeners for delete, edit, and completion checkbox
        delete.setOnClickListener(v -> {
            dbHelper.deleteHabit(id);
            layout.removeView(view);
        });

        edit.setOnClickListener(v -> showEditDialog(id, nameView, descriptionView, trackingView));

        completeCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            dbHelper.updateCompletionStatus(id, isChecked);
            // Later add this information to another table in the database?
            // Would be good to track it somehow for graphs later on
        });

        layout.addView(view);
    }

    // Show edit dialog for a card (habit)
    private void showEditDialog(int id, final TextView nameView, final TextView descriptionView, final TextView trackingView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog, null);

        final EditText editName = dialogView.findViewById(R.id.etName);
        final EditText editDescription = dialogView.findViewById(R.id.etDescription);
        final Spinner editTracking = dialogView.findViewById(R.id.spinnerTracking);

        setupTrackingSpinner(editTracking);

        editName.setText(nameView.getText().toString());
        editDescription.setText(descriptionView.getText().toString());

        String currentTrackingType = trackingView.getText().toString().replace("Tracking: ", "");
        int spinnerPosition = ((ArrayAdapter) editTracking.getAdapter()).getPosition(currentTrackingType);
        editTracking.setSelection(spinnerPosition);

        builder.setView(dialogView)
                .setTitle("Edit your habit")
                .setPositiveButton("Save", (dialog, which) -> {
                    String newName = editName.getText().toString();
                    String newDescription = editDescription.getText().toString();
                    String newTrackingType = editTracking.getSelectedItem().toString();

                    nameView.setText(newName);
                    descriptionView.setText(newDescription);
                    trackingView.setText("Tracking: " + newTrackingType);

                    dbHelper.updateHabit(id, newName, newDescription, newTrackingType);
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
}

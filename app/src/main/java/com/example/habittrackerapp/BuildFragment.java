package com.example.habittrackerapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.habittrackerapp.HabitsListAdapter;
import com.example.habittrackerapp.R;

import java.util.Arrays;
import java.util.List;

public class BuildFragment extends Fragment {

    public BuildFragment() {
        // Required empty public constructor
    }

    public static BuildFragment newInstance() {
        return new BuildFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_build, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.build_habit_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Array Data
        List<String> data = loadBuildList();

        // Adapter
        HabitsListAdapter adapter = new HabitsListAdapter(data, item -> {
            Intent intent = new Intent(view.getContext(), NewHabitActivity.class);
            intent.putExtra("habit_name", item);
            intent.putExtra("habit_type", "Build");
            view.getContext().startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        return view;
    }

    // Returns a list of Build habits
    public List<String> loadBuildList(){
        return Arrays.asList("Breathe", "Meditate", "Read A Book", "Learning",
                "Review Today", "Mind Clearing", "Drink Water", "Eat Fruits",
                "Eat Vegetables", "No Sugar", "Sleep Early", "Eat Low-Fat",
                "Eat Breakfast", "Call Parents", "Contact A Friend", "Save Money",
                "Keep A Diary", "Eat An Apple", "Walk", "Run", "Stretch",
                "Stand", "Yoga", "Cycling", "Swim", "Burn Calorie",
                "Exercise", "Anaerobic");
    }
}
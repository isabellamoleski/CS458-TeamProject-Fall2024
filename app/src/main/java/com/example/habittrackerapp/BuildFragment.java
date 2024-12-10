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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<String[]> data = loadBuildList();

        // Adapter
        HabitsListAdapter adapter = new HabitsListAdapter(data, pos -> {
            Intent intent = new Intent(view.getContext(), NewHabitActivity.class);
            intent.putExtra("habit_name", data.get(pos)[0]);
            intent.putExtra("habit_description", data.get(pos)[1]);
            intent.putExtra("habit_type", "Build");
            view.getContext().startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        return view;
    }

    // Returns a list of Build habits
    public List<String[]> loadBuildList(){

        List<String[]> buildList = new ArrayList<>();

        buildList.add(new String[]{"Breathe", "Practice deep breathing to relax and reduce stress."});
        buildList.add(new String[]{"Meditate", "Spend a few minutes in mindfulness or meditation."});
        buildList.add(new String[]{"Read A Book", "Enrich your mind with daily reading."});
        buildList.add(new String[]{"Learning", "Dedicate time to learn something new."});
        buildList.add(new String[]{"Review Today", "Reflect on the day’s activities and accomplishments."});
        buildList.add(new String[]{"Mind Clearing", "Take a moment to clear your thoughts."});
        buildList.add(new String[]{"Drink Water", "Stay hydrated with regular water intake."});
        buildList.add(new String[]{"Eat Fruits", "Add fruits to your diet for better nutrition."});
        buildList.add(new String[]{"Eat Vegetables", "Include vegetables in your meals for health benefits."});
        buildList.add(new String[]{"No Sugar", "Avoid added sugars for better health."});
        buildList.add(new String[]{"Sleep Early", "Ensure a good night's rest by sleeping early."});
        buildList.add(new String[]{"Eat Low-Fat", "Opt for low-fat options to support your diet."});
        buildList.add(new String[]{"Eat Breakfast", "Start your day with a nutritious breakfast."});
        buildList.add(new String[]{"Call Parents", "Connect with your parents to strengthen bonds."});
        buildList.add(new String[]{"Contact A Friend", "Reach out to a friend for social connection."});
        buildList.add(new String[]{"Save Money", "Set aside money to build financial stability."});
        buildList.add(new String[]{"Keep A Diary", "Record thoughts or events to reflect later."});
        buildList.add(new String[]{"Eat An Apple", "Enjoy an apple for a healthy snack."});
        buildList.add(new String[]{"Walk", "Go for a walk to stay active and refreshed."});
        buildList.add(new String[]{"Run", "Boost endurance with a daily run."});
        buildList.add(new String[]{"Stretch", "Stretch your body to improve flexibility."});
        buildList.add(new String[]{"Stand", "Spend time standing to counteract sitting too much."});
        buildList.add(new String[]{"Yoga", "Practice yoga for balance and relaxation."});
        buildList.add(new String[]{"Cycling", "Ride a bike to enhance fitness and stamina."});
        buildList.add(new String[]{"Swim", "Dive into swimming for a full-body workout."});
        buildList.add(new String[]{"Burn Calorie", "Engage in activities to burn calories effectively."});
        buildList.add(new String[]{"Exercise", "Build strength and endurance with physical exercise."});
        buildList.add(new String[]{"Anaerobic", "Perform short bursts of high-intensity exercises."});

        return buildList;
    }
}
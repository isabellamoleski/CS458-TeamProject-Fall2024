package com.example.habittrackerapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.habittrackerapp.HabitsListAdapter;
import com.example.habittrackerapp.R;

import java.util.Arrays;
import java.util.List;

public class QuitFragment extends Fragment {


    public QuitFragment() {
        // Required empty public constructor
    }

    public static QuitFragment newInstance() {
        return new QuitFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quit, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.quit_habit_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Array Data
        List<String> data = loadQuitList();

        // Adapter
        HabitsListAdapter adapter = new HabitsListAdapter(data, item -> {
            Intent intent = new Intent(view.getContext(), NewHabitActivity.class);
            intent.putExtra("habit_name", item);
            intent.putExtra("habit_type", "Quit");
            view.getContext().startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        return view;
    }

    // Returns a list of Build habits
    public List<String> loadQuitList(){
        return Arrays.asList("Smoke Less", "Drink Less Alcohol", "Drink Less Beverage",
                "Drink Less Caffeine", "Eat Less Sugar", "Less Carbohydrate",
                "Play Less Game", "Complain Less", "Sit Less", "Watch Less TV",
                "Less Social App", "Spend Less");
    }
}
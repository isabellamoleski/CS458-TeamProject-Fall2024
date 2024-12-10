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

import java.util.ArrayList;
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
        List<String[]> data = loadQuitList();

        // Adapter
        HabitsListAdapter adapter = new HabitsListAdapter(data, pos -> {
            Intent intent = new Intent(view.getContext(), NewHabitActivity.class);
            intent.putExtra("habit_name", data.get(pos)[0]);
            intent.putExtra("habit_description", data.get(pos)[1]);
            intent.putExtra("habit_type", "Quit");
            view.getContext().startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        return view;
    }

    // Returns a list of Build habits
    public List<String[]> loadQuitList(){

        List<String[]> quitList = new ArrayList<>();

        quitList.add(new String[]{"Smoke Less", "Reduce smoking to improve your health."});
        quitList.add(new String[]{"Drink Less Alcohol", "Cut back on alcohol consumption for better well-being."});
        quitList.add(new String[]{"Drink Less Beverage", "Limit sugary or high-calorie beverages."});
        quitList.add(new String[]{"Drink Less Caffeine", "Lower caffeine intake for balanced energy levels."});
        quitList.add(new String[]{"Eat Less Sugar", "Reduce sugar consumption for a healthier diet."});
        quitList.add(new String[]{"Less Carbohydrate", "Limit carbs to support a balanced diet."});
        quitList.add(new String[]{"Play Less Game", "Spend less time gaming to focus on other activities."});
        quitList.add(new String[]{"Complain Less", "Cultivate positivity by reducing complaints."});
        quitList.add(new String[]{"Sit Less", "Minimize sitting time to stay active."});
        quitList.add(new String[]{"Watch Less TV", "Cut back on TV time for a productive day."});
        quitList.add(new String[]{"Less Social App", "Limit social app usage to reclaim time."});
        quitList.add(new String[]{"Spend Less", "Reduce spending to save money."});

        return quitList;
    }
}
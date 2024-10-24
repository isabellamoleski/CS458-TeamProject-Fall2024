package com.example.habittrackerapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HabitsListAdapter extends RecyclerView.Adapter<HabitsListAdapter.ViewHolder> {
    private List<String> habitsList;
    private OnItemClickListener onItemClickListener;

    // Constructor to pass the data
    public HabitsListAdapter(List<String> stringList, OnItemClickListener onItemClickListener) {
        this.habitsList = stringList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.habit_item, parent, false);
        return new ViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Set the data for each item
        String currentItem = habitsList.get(position);
        holder.textView.setText(currentItem);
    }

    @Override
    public int getItemCount() {
        return habitsList.size(); // Number of items in the list
    }

    // ViewHolder class that holds the layout for each item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_text);

            // Set click listener for the whole itemView
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    // Pass the clicked item to the listener
                    listener.onItemClick(textView.getText().toString());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String item);
    }
}

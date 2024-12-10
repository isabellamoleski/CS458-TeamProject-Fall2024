package com.example.habittrackerapp;

public class Habit {
    private int id;
    private String name;
    private String description;
    private String trackingType;
    private String habitType;
    private String goalQuantity;
    private String goalPeriod;
    private String frequency;
    private String reminder;
    private String notes;
    private boolean isComplete;

    public Habit(int id, String name, String description, String trackingType, boolean isComplete) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.trackingType = trackingType;
        this.isComplete = isComplete;
    }

    public Habit(int id, String name, String description, String habitType, String goalQuantity,
                 String goalPeriod, String frequency, String reminder, String notes, boolean isComplete) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.habitType = habitType;
        this.goalQuantity = goalQuantity;
        this.goalPeriod = goalPeriod;
        this.frequency = frequency;
        this.reminder = reminder;
        this.notes = notes;
        this.isComplete = isComplete;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getTrackingType() {
        return trackingType;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTrackingType(String trackingType) {
        this.trackingType = trackingType;
    }

    public void setComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getGoalPeriod() {
        return goalPeriod;
    }

    public void setGoalPeriod(String goalPeriod) {
        this.goalPeriod = goalPeriod;
    }

    public String getGoalQuantity() {
        return goalQuantity;
    }

    public void setGoalQuantity(String goalQuantity) {
        this.goalQuantity = goalQuantity;
    }

    public String getHabitType() {
        return habitType;
    }

    public void setHabitType(String habitType) {
        this.habitType = habitType;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Habit{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", trackingType='" + trackingType + '\'' +
                ", isComplete=" + isComplete +
                '}';
    }
}

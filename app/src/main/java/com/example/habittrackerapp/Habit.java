package com.example.habittrackerapp;

public class Habit {
    private int id;
    private String name;
    private String description;
    private String trackingType;
    private boolean isComplete;

    public Habit(int id, String name, String description, String trackingType, boolean isComplete) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.trackingType = trackingType;
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

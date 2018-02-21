package com.example.max.mentalhealthapp;

/**
 * Created by Max on 2/12/2018.
 */

public class MoodReport {
 String date, time, notes;
    int happy, energy, irritated, anxious;
    public MoodReport(String d,String t, int h, int e, int i, int a, String n) {
        date = d;
        time = t;
        happy = h;
        energy = e;
        irritated = i;
        anxious = a;
        notes = n;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getNotes() {
        return notes;
    }

    public int getHappy() {
        return happy;
    }

    public int getEnergy() {
        return energy;
    }

    public int getIrritated() {
        return irritated;
    }

    public int getAnxious() {
        return anxious;
    }
}
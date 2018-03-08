package com.example.max.mentalhealthapp;

import java.util.Date;

//object stores data from each mood report
public class MoodReport implements Comparable<MoodReport> {
 private String date, time, notes;
    private int happy, energy, irritated, anxious, sad;
    private Date dateObj;

    public MoodReport(String d,String t, int h, int e, int i, int a, int s, String n, Date tempDate) {
        date = d;
        time = t;
        happy = h;
        energy = e;
        irritated = i;
        anxious = a;
        sad = s;
        notes = n;
        dateObj = tempDate;
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

    public int getSad() { return sad; }

    public Date getDateObj() { return dateObj; }

    //so that the array can be sorted based on time
    @Override
    public int compareTo(MoodReport o) {
        return getDateObj().compareTo(o.getDateObj());
    }
}
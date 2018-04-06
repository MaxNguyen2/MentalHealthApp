package com.example.max.mentalhealthapp;

import java.util.Date;

//Object that stores data from each mood report
class MoodReport implements Comparable<MoodReport> {
 private String date, time, notes;
    private int happy, energy, irritated, anxious, sad;
    private Date dateObj;

    MoodReport(String d,String t, int h, int e, int i, int a, int s, String n, Date tempDate) {
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

    String getDate() { return date; }

    public String getTime() { return time; }

    String getNotes() { return notes; }

    int getHappy() { return happy; }

    int getEnergy() { return energy; }

    int getIrritated() { return irritated; }

    int getAnxious() { return anxious; }

    int getSad() { return sad; }

   Date getDateObj() { return dateObj; }

    //so that the array can be sorted based on time
    @Override
    public int compareTo(MoodReport o) {
        return getDateObj().compareTo(o.getDateObj());
    }
}


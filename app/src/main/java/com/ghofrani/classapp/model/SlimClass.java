package com.ghofrani.classapp.model;

import android.content.Context;

import com.ghofrani.classapp.modules.DatabaseHelper;

public class SlimClass {

    private final String name;
    private final String location;
    private final String teacher;
    private final int color;

    public SlimClass(Context context, String name, String location, String teacher) {

        this.name = name;
        this.location = location;
        this.teacher = teacher;

        DatabaseHelper databaseHelper = new DatabaseHelper(context);

        this.color = databaseHelper.getClassColor(name);

        databaseHelper.close();

    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getTeacher() {
        return teacher;
    }

    public int getColor() {
        return color;
    }

}
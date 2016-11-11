package com.yl.rxjava_retrofit.bean;

import java.util.ArrayList;

/**
 *
 */
public class Student {
    private String name;

    private ArrayList<Course> courses;

    public Student(String name, ArrayList<Course> courses) {
        this.name = name;
        this.courses = courses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }
}


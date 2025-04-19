package com.example.todolist;

import java.util.Date;
public class Task {
    public int id;
    public String name;
    public Date deadLine;
    public int duration;
    public String description;
    public boolean completed;

    public Task(String n, Date dl, int d, String des, boolean c){
        name = n;
        deadLine = dl;
        duration = d;
        description = des;
        completed = c;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}

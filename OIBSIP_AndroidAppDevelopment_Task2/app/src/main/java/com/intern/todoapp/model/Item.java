package com.intern.todoapp.model;

public class Item {
    public long id;
    public long userId;
    public String section;   // "tasks" | "events" | "notes"
    public String title;
    public String body;
    public long createdAt;

    public Item() {}
    public Item(long id, long userId, String section, String title, String body, long createdAt) {
        this.id = id; this.userId = userId; this.section = section;
        this.title = title; this.body = body; this.createdAt = createdAt;
    }
}

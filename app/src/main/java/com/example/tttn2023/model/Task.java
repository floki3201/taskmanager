package com.example.tttn2023.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Task implements Serializable, Parcelable {
    private String id;
    private String idProject;
    private String title;
    private String date;
    private String time;
    private String category;
    private String status;
    private String description;
    private String alarmSet;

    public Task() {
    }

    public Task(String title, String date, String time, String status, String category, String description) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.category = category;
        this.status = status;
        this.description = description;
    }

    public Task(String id, String title, String date, String time, String status, String category, String description) {
        this.id = id;
//        this.idProject = idProject;
        this.title = title;
        this.date = date;
        this.time = time;
        this.category = category;
        this.status = status;
        this.description = description;
    }

    protected Task(Parcel in) {
        id = in.readString();
        title = in.readString();
        date = in.readString();
        time = in.readString();
        category = in.readString();
        status = in.readString();
        description = in.readString();
        alarmSet = in.readString();
//        idProject = in.readString();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAlarmSet() {
        return alarmSet;
    }

    public void setAlarmSet(String alarmSet) {
        this.alarmSet = alarmSet;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public String getIdProject() {
//        return idProject;
//    }
//
//    public void setIdProject(String idProject) {
//        this.idProject = idProject;
//    }

    @Exclude
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
//        map.put("idProject", idProject);
        map.put("title", title);
        map.put("date", date);
        map.put("time", time);
        map.put("category", category);
        map.put("status", status);
        map.put("description", description);
        return map;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(id);
//        parcel.writeString(idProject);
        parcel.writeString(title);
        parcel.writeString(date);
        parcel.writeString(time);
        parcel.writeString(category);
        parcel.writeString(status);
        parcel.writeString(description);
        parcel.writeString(alarmSet);
    }
}
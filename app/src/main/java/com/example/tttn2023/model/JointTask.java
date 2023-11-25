package com.example.tttn2023.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class JointTask implements Serializable, Parcelable {
    private String id;
    private String projectId;
    private String title;
    private String date;
    private String time;
    private String status;
    private String description;
//    private String alarmSet;
    private String employeeId;
    private String linkFile;

//    public JointTask(String title, String description, String date, String time, String status, String employeeId) {
//        this.title = title;
//        this.date = date;
//        this.time = time;
//        this.status = status;
//        this.description = description;
//        this.employeeId = employeeId;
//    }

    public JointTask(String title, String description, String date, String time, String status,String projectId, String employeeId, String linkFile) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.status = status;
        this.description = description;
        this.projectId = projectId;
        this.employeeId = employeeId;
        this.linkFile = linkFile;
    }
//    public JointTask(String title, String description, String date, String time, String status, String projectId, String employeeId) {
//        this.projectId = projectId;
//        this.title = title;
//        this.date = date;
//        this.time = time;
//        this.status = status;
//        this.description = description;
//        this.employeeId = employeeId;
//    }

    public JointTask(String id, String title, String description, String date, String time, String status,String projectId, String employeeId, String linkFile) {
        this.id = id;
        this.projectId = projectId;
        this.title = title;
        this.date = date;
        this.time = time;
        this.status = status;
        this.description = description;
        this.employeeId = employeeId;
        this.linkFile = linkFile;
    }
    protected JointTask(Parcel in) {
        id = in.readString();
        title = in.readString();
        description = in.readString();
        date = in.readString();
        time = in.readString();
        status = in.readString();
        projectId = in.readString();
        employeeId = in.readString();
        linkFile = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
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

    public String getLinkFile() {
        return linkFile;
    }

    public void setLinkFile(String linkFile) {
        this.linkFile = linkFile;
    }
    //    public String getAlarmSet() {
//        return alarmSet;
//    }
//
//    public void setAlarmSet(String alarmSet) {
//        this.alarmSet = alarmSet;
//    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public static final Creator<JointTask> CREATOR = new Creator<JointTask>() {
        @Override
        public JointTask createFromParcel(Parcel in) {
            return new JointTask(in);
        }

        @Override
        public JointTask[] newArray(int size) {
            return new JointTask[size];
        }
    };
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("title", title);
        map.put("description", description);
        map.put("date", date);
        map.put("time", time);
        map.put("status", status);
        map.put("projectId", projectId);
        map.put("employeeId", employeeId);
        map.put("linkFile", linkFile);
//        map.put("alarmSet", alarmSet);
        return map;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(date);
        parcel.writeString(time);
        parcel.writeString(status);
        parcel.writeString(projectId);
        parcel.writeString(employeeId);
        parcel.writeString(linkFile);
    }
}

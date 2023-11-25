package com.example.tttn2023.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JointProject implements Serializable, Parcelable {
    private String id;
    private String title;
    private String content;
    private String ownerId;
    private List<Map<String, String>> listMember;
    protected JointProject(Parcel in) {
        id = in.readString();
        title = in.readString();
        content = in.readString();
        ownerId = in.readString();
//        listMember = in.readArrayList();
    }
    public JointProject(){

    }
    public JointProject(String id, String title, String content){
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public JointProject(String id, String title, String content, String ownerId, List<Map<String, String>> listMember) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.ownerId = ownerId;
        this.listMember = listMember;
    }

    public static final Creator<JointProject> CREATOR = new Creator<JointProject>() {
        @Override
        public JointProject createFromParcel(Parcel in) {
            return new JointProject(in);
        }

        @Override
        public JointProject[] newArray(int size) {
            return new JointProject[size];
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public List<Map<String, String>> getListMember() {
        return listMember;
    }

    public void setListMember(List<Map<String, String>> listMember) {
        this.listMember = listMember;
    }

    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("title", title);
        map.put("content", content);
        map.put("ownerId", ownerId);
        map.put("listMember", listMember);
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
        parcel.writeString(content);
    }
}

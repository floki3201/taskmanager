package com.example.tttn2023.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PerPro implements Serializable, Parcelable {
    private String id;
    private String title;
    private String content;

    private String ownerId;

    private List<String> listMember;
    protected PerPro(Parcel in) {
        id = in.readString();
        title = in.readString();
        content = in.readString();
    }

    public PerPro() {
    }

    public PerPro(String id,String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public PerPro(String id, String title, String content, String ownerId, List<String> listMember) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.ownerId = ownerId;
        this.listMember = listMember;
    }

    public static final Creator<PerPro> CREATOR = new Creator<PerPro>() {
        @Override
        public PerPro createFromParcel(Parcel in) {
            return new PerPro(in);
        }

        @Override
        public PerPro[] newArray(int size) {
            return new PerPro[size];
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

    public List<String> getListMember() {
        return listMember;
    }

    public void setListMember(List<String> listMember) {
        this.listMember = listMember;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
//        map.put("idProject", idProject);
        map.put("title", title);
        map.put("content", content);
        map.put("ownerId", ownerId);
        map.put("listMember", listMember);
        return map;
    }
    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(content);
    }
}

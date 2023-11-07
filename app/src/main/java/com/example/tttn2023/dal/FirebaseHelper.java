package com.example.tttn2023.dal;

import com.example.tttn2023.model.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FirebaseHelper {
    private FirebaseDatabase database;
    private DatabaseReference ref;
    public List<Task> userTaskList;

    public FirebaseHelper() {
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        userTaskList = new ArrayList<>();
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }

    public DatabaseReference getRef() {
        return ref;
    }



}

package com.example.tttn2023.dal;

import com.example.tttn2023.model.PersonalTask;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FirebaseHelper {
    private FirebaseDatabase database;
    private DatabaseReference ref;
    public List<PersonalTask> userPersonalTaskList;

    public FirebaseHelper() {
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        userPersonalTaskList = new ArrayList<>();
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }

    public DatabaseReference getRef() {
        return ref;
    }



}

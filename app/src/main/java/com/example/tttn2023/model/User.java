package com.example.tttn2023.model;

import com.google.firebase.auth.FirebaseUser;

public class User {
    public static FirebaseUser current_user;
    public static Boolean isOwner = false;

    public static Boolean getIsOwner() {
        return isOwner;
    }

    public static void setIsOwner(Boolean isOwner) {
        User.isOwner = isOwner;
    }

    public User() {
    }

    public static FirebaseUser getCurrent_user() {
        return current_user;
    }

    public static void setCurrent_user(FirebaseUser current_user) {
        User.current_user = current_user;
    }

    public static void signOut() {
        User.current_user = null;
    }
}

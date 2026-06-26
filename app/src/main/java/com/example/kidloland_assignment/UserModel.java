package com.example.kidloland_assignment;

public class UserModel {

    String name, dob, email;
    boolean isCurrentUser;

    public UserModel(String name, String dob, String email, boolean isCurrentUser) {
        this.name = name;
        this.dob = dob;
        this.email = email;
        this.isCurrentUser = isCurrentUser;
    }
}

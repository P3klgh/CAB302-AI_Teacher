package com.cab302ai_teacher.model;

public class UserFactory {

    public static User create(String firstName, String lastName, String email, String password, String role) {

        return new User(firstName, lastName, email, password, role);
    }

}
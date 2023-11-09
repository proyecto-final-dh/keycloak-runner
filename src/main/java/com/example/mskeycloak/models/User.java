package com.example.mskeycloak.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class User {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private List<String> groups;

//    public User(String username, String firstName, String lastName, String email) {
//        this.username = username;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.email = email;
//    }

//    public User(String id, String username, String firstName, String lastName, String email) {
//        this.id = id;
//        this.username = username;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.email = email;
//    }
//
    public User(String id, String firstName, String lastName) {
        this.id = id;
        this.username = firstName + "_" + lastName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = firstName + lastName + "@hotmail.com";
    }

    public User(String firstName, String lastName) {
        this.username = firstName + "_" + lastName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = firstName + lastName + "@hotmail.com";
    }
}

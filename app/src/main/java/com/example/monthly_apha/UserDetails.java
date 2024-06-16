package com.example.monthly_apha;

public class UserDetails {
    String uid;
    String name;
    String email;
    String password;

    //user constructor to assign name, email and password
    public UserDetails(String name, String email, String password ){
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // constructor includes all attributes including id+ (+ID)
    public UserDetails(String id, String name, String email, String password ){
        uid = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // getters
    public String getId() {
        return uid;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }

    // Setters
    public void setId(String id) {
        uid = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }








}

package com.example.mymicroprojectmad.auth;


public class RegisterUser {
    private String username;
    private String password;
    private String name;

    public RegisterUser(String username, String password, String name) {
        this.username = username;
        this.password = password;
        this.name = name;
    }

    // Getters and setters (or use Lombok library for automatic generation)
    // ...

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Example methods to create User object
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

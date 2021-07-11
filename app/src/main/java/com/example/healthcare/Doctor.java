package com.example.healthcare;

public class Doctor {
    private String dept,username,email;
    
    public Doctor(String dept,String email, String name){
        this.dept = dept;
        this.username = name;
        this.email = email;
    }

    public String getDept() {
        return dept;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }
}

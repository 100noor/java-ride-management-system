package com.mycompany.rideapp;

public class Login {

    String username;
    String password;
    String role;

    public Login(String u, String p, String r) {
        username = u;
        password = p;
        role = r;
    }
    
   
    public boolean check(String u, String p, String r) {
        return username.equals(u)
                && password.equals(p)
                && role.equals(r);
    }
}
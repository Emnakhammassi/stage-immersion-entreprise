package com.FlickFlow.FlickFlow.user.api.Response;


public class JwtResponse {
    private final String jwt;
    private final String username;
    private int userId;

    public JwtResponse(String jwt, String username, int userId) {
        this.jwt = jwt;
        this.username = username;
        this.userId = userId;
    }

    public JwtResponse(String jwt, String username) {
        this.jwt = jwt;
        this.username = username;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getJwt() {
        return jwt;
    }

    public String getUsername() {
        return username;
    }}

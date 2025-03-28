package com.eduhubpro.eduhubpro.Security.Auth.Model;

import com.eduhubpro.eduhubpro.Util.Enum.EntityEnum.UserRole;

public class AuthResponse {
    private String jwt;
    private String email;
    private long expiration;
    private UserRole userRole;

    public AuthResponse(String jwt, String email, long expiration, UserRole userRole) {
        this.jwt = jwt;
        this.email = email;
        this.expiration = expiration;
        this.userRole = userRole;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public UserRole getRole() {
        return userRole;
    }

    public void setRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
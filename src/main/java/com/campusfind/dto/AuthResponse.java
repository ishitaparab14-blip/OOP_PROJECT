package com.campusfind.dto;

public class AuthResponse {
    private boolean success;
    private String message;
    private UserProfileDto user;

    public AuthResponse() {
    }

    public AuthResponse(boolean success, String message, UserProfileDto user) {
        this.success = success;
        this.message = message;
        this.user = user;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserProfileDto getUser() {
        return user;
    }

    public void setUser(UserProfileDto user) {
        this.user = user;
    }
}

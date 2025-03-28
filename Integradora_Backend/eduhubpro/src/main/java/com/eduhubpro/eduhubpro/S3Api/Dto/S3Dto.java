package com.eduhubpro.eduhubpro.S3Api.Dto;

import jakarta.validation.constraints.NotBlank;

public class S3Dto {

    @NotBlank(groups = {GetProfilePhotoUrl.class}, message = " El id del usuario no puede ser nulo")
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public interface GetProfilePhotoUrl {
    }
}
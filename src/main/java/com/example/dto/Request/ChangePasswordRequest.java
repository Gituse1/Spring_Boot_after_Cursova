package com.example.dto.Request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {


    private String currentPassword;

    @Size(min = 6, message = "Новий пароль має бути мінімум 6 символів")
    private String newPassword;
    private String oldPassword;
    private String email;
    private String confirmationPassword; // Опціонально, для перевірки на фронті
}
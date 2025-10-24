package com.kakeibo.kakeibo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RequestUserDto {
    @NotBlank(message = "ユーザー名は必須です")
    private String userName;
    @NotBlank(message = "パスワードは必須です")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "パスワードは英数字のみです")
    private String password;
}

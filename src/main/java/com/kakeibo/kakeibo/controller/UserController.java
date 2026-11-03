package com.kakeibo.kakeibo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kakeibo.kakeibo.dto.RequestUserDto;
import com.kakeibo.kakeibo.model.RequestLogin;
import com.kakeibo.kakeibo.model.ResponseLogin;
import com.kakeibo.kakeibo.model.ResponseUserRegist;
import com.kakeibo.kakeibo.service.UserService;

import jakarta.validation.Valid;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/api/users/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseUserRegist register(@Valid @RequestBody RequestUserDto dto) {
        return userService.registerUser(dto);
    }

    @PostMapping("/api/users/login")
    public ResponseLogin login(@RequestBody RequestLogin dto) {
        return userService.login(dto);
    }
}

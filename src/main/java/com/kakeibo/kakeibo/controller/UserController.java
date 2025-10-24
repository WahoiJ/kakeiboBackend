package com.kakeibo.kakeibo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kakeibo.kakeibo.dto.RequestUserDto;
import com.kakeibo.kakeibo.entity.UserEntity;
import com.kakeibo.kakeibo.model.RequestLogin;
import com.kakeibo.kakeibo.model.ResponseLogin;
import com.kakeibo.kakeibo.service.UserService;

@RestController
public class UserController {
    @Autowired
  private UserService userService;
  @PostMapping("/api/users/register")
  public UserEntity register(@RequestBody RequestUserDto dto) {

    return userService.registerUser(dto);
      // サービスクラスのユーザ登録処理呼び出し

  }

    @PostMapping("/api/users/login")
  public ResponseLogin login(@RequestBody RequestLogin dto) {
    return userService.login(dto);
  }


}

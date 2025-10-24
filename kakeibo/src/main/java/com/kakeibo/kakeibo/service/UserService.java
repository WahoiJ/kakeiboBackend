package com.kakeibo.kakeibo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kakeibo.kakeibo.config.JwtUtil;
import com.kakeibo.kakeibo.dto.RequestUserDto;
import com.kakeibo.kakeibo.entity.UserEntity;
import com.kakeibo.kakeibo.model.RequestLogin;
import com.kakeibo.kakeibo.model.RequestUserRegist;
import com.kakeibo.kakeibo.model.ResponseLogin;
import com.kakeibo.kakeibo.model.ResponseUserRegist;
import com.kakeibo.kakeibo.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public ResponseUserRegist insertUser(RequestUserRegist requestUserRegist) {
        UserEntity user = createUser(requestUserRegist);
        userRepository.save(user);
        ResponseUserRegist responseUserRegist = new ResponseUserRegist();
        responseUserRegist.setId(user.getId());
        responseUserRegist.setUserName(user.getUserName());
        return responseUserRegist;
    }

    public UserEntity createUser(RequestUserRegist requestUserRegist) {
        String hashPw = encoder.encode(requestUserRegist.getPassword());
        UserEntity user = new UserEntity();
        user.setUserName(requestUserRegist.getUserName());
        user.setPassword(hashPw);
        return user;
    }

    public ResponseLogin login(RequestLogin requestLogin) {
        Optional<UserEntity> optionalUser = userRepository.findByUserName(requestLogin.getUserName());
        ResponseLogin responseLogin = new ResponseLogin();

        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();
            if (encoder.matches(requestLogin.getPassword(), user.getPassword())) {
                responseLogin.setStatus("success");
                responseLogin.setId(user.getId());
                responseLogin.setUserName(user.getUserName());
                String token = jwtUtil.generateToken(user.getUserName(), user.getId());
                responseLogin.setToken(token);
            } else {
                responseLogin.setStatus("error: invalid password");
            }
        } else {
            responseLogin.setStatus("error: user not found");
        }
        return responseLogin;
    }

    private UserEntity createUser(RequestLogin requestLogin) {
        String hashPw = encoder.encode(requestLogin.getPassword());
        UserEntity user = new UserEntity();
        user.setUserName(requestLogin.getUserName());
        user.setPassword(hashPw);
        return user;
    }

    public UserEntity registerUser(RequestUserDto dto) {
        UserEntity user = new UserEntity();
        user.setUserName(dto.getUserName());
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
        return userRepository.save(user);
    }

    public UserEntity loginUser(RequestUserDto dto) {
        Optional<UserEntity> optionalUser = userRepository.findByUserName(dto.getUserName());
        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();
            if (encoder.matches(dto.getPassword(), user.getPassword())) {
                return user;
            } else {
                throw new RuntimeException("Invalid password");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }
}
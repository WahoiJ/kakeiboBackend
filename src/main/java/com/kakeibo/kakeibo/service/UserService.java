package com.kakeibo.kakeibo.service;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.kakeibo.kakeibo.config.JwtUtil;
import com.kakeibo.kakeibo.dto.RequestUserDto;
import com.kakeibo.kakeibo.entity.UserEntity;
import com.kakeibo.kakeibo.model.RequestLogin;
import com.kakeibo.kakeibo.model.ResponseLogin;
import com.kakeibo.kakeibo.model.ResponseUserRegist;
import com.kakeibo.kakeibo.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /** ユーザー登録。パスワードハッシュはレスポンスに含めない */
    @Transactional
    public ResponseUserRegist registerUser(RequestUserDto dto) {
        String userName = dto.getUserName().trim();
        if (userRepository.findByUserName(userName).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "このユーザー名は既に使われています");
        }
        UserEntity user = new UserEntity();
        user.setUserName(userName);
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);

        ResponseUserRegist response = new ResponseUserRegist();
        response.setId(user.getId());
        response.setUserName(user.getUserName());
        return response;
    }

    /** ログイン。失敗時は 401 を返す */
    public ResponseLogin login(RequestLogin requestLogin) {
        String userName = requestLogin.getUserName() == null ? "" : requestLogin.getUserName().trim();
        UserEntity user = userRepository.findByUserName(userName)
                .filter(u -> requestLogin.getPassword() != null && encoder.matches(requestLogin.getPassword(), u.getPassword()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "ユーザー名またはパスワードが正しくありません"));

        ResponseLogin responseLogin = new ResponseLogin();
        responseLogin.setStatus("success");
        responseLogin.setId(user.getId());
        responseLogin.setUserName(user.getUserName());
        responseLogin.setToken(jwtUtil.generateToken(user.getUserName(), user.getId()));
        return responseLogin;
    }
}

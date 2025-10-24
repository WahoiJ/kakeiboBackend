package com.kakeibo.kakeibo.model;

import lombok.Data;

@Data
public class ResponseLogin {

    private String status;
    private Integer id;
    private String userName;
    private String token;
}

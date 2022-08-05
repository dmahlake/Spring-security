package com.spring.security.dto;

import lombok.Data;

@Data
public class LoginDto {

    private String usernameOrEmailAddress;
    private String password;
}

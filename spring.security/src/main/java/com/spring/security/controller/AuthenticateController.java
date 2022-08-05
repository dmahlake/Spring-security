package com.spring.security.controller;


import com.spring.security.dto.LoginDto;
import com.spring.security.dto.SignUpDto;
import com.spring.security.persistence.repo.CustomerRepo;
import com.spring.security.service.CustomerUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.CustomAutowireConfigurer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticateController {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    CustomerUserDetails userDetails;

    @Autowired
    CustomerRepo customerRepo;

    @PostMapping("/signin")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginDto loginDto)
    {
        return new ResponseEntity<>(userDetails.login(loginDto), HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto)
    {
        if (customerRepo.existsByUsername(signUpDto.getUsername()))
        {
            return new ResponseEntity<>( "Username is already taken", HttpStatus.BAD_REQUEST);
        }
        if (customerRepo.existsByEmailAddress(signUpDto.getEmailAddress())){
            return new ResponseEntity<>("Email address is already taken", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userDetails.registerUser(signUpDto), HttpStatus.OK);
    }
}

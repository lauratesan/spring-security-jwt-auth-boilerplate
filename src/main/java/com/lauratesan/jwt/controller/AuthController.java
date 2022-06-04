package com.lauratesan.jwt.controller;


import com.lauratesan.jwt.dto.*;
import com.lauratesan.jwt.mapper.UserMapper;
import com.lauratesan.jwt.model.auth.User;
import com.lauratesan.jwt.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return authService.registerUser(signUpRequest);
    }


    @PostMapping("/getJwtInfo")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<JwtResponse> getJwtTokenInfo() {
        return authService.getJwtResponse();
    }

    @GetMapping("/getUserInfo")
    @PreAuthorize("hasRole('USER')")
    public UserDTO getUserInfo() {
        User user = authService.getUserInfo();
        return userMapper.toUserDto(user);
    }
}
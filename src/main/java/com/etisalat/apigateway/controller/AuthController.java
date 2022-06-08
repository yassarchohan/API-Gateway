package com.etisalat.apigateway.controller;

import com.etisalat.apigateway.dto.LoginDto;
import com.etisalat.apigateway.dto.LoginResDto;
import com.etisalat.apigateway.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private LoginService loginService;


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDto loginDto) {
        LoginResDto loginResDto = loginService.authenticateUser(loginDto);
        return ResponseEntity.ok(loginResDto);
    }

    @PostMapping("/test")
    public ResponseEntity<?> test(@RequestHeader(value = "Authorization") String Authorization) {
        if (Authorization == null) {
            return ResponseEntity.badRequest().body("unauthorize");
        }
        String response = loginService.testToken(Authorization);
        return ResponseEntity.ok(response);
    }
}

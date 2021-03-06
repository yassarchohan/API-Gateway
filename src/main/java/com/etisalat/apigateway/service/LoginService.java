package com.etisalat.apigateway.service;

import com.etisalat.apigateway.client.WsoClient;
import com.etisalat.apigateway.dto.LoginDto;
import com.etisalat.apigateway.dto.LoginResDto;
import com.etisalat.apigateway.dto.ValidateTokenDto;
import com.etisalat.apigateway.exception.JwtTokenMalformedException;
import com.etisalat.apigateway.exception.JwtTokenMissingException;
import com.etisalat.apigateway.util.JwtUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import feign.form.spring.SpringFormEncoder;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.slf4j.Slf4jLogger;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoginService {

    private WsoClient wsoClient;

    @Value("${client.client-id}")
    private String clientId;

    @Value("${client.client-secret}")
    private String clientSecret;

    @Value("${provider.host}")
    private String url;

    @Value("${client.grant-type}")
    private String grantType;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    public LoginResDto authenticateUser(LoginDto loginDto) {
        try {
            this.wsoClient = Feign.builder().encoder(new SpringFormEncoder(new SpringEncoder(messageConverters))).decoder(new GsonDecoder()).logger(new Slf4jLogger(LoginService.class)).
                    requestInterceptor(new BasicAuthRequestInterceptor(clientId, clientSecret)).target(WsoClient.class, url);
            LoginResDto loginResDto = wsoClient.authenticate(grantType, loginDto.getUsername(), loginDto.getPassword());
            log.info("login response: " + loginResDto);
            return loginResDto;
        }
        catch (Exception e) {
            log.error("Error in authentication " + e.getMessage());
            return null;
        }
    }

    public String testToken(String token) {
        try {
            log.info("the token is: " + token);
            this.wsoClient = Feign.builder().encoder(new SpringFormEncoder(new SpringEncoder(messageConverters))).decoder(new GsonDecoder()).logger(new Slf4jLogger(LoginService.class)).
                    requestInterceptor(new BasicAuthRequestInterceptor("admin", "admin")).target(WsoClient.class, url);
            ValidateTokenDto response = wsoClient.validateToken(token);
            log.info("login response: " + response);
            if (response.getActive().equalsIgnoreCase("true")) {
                return "token is valid";
            } else {
                return "token not valid";
            }
        }
        catch (Exception e) {
            log.error("Error validing token " + e.getMessage());
            return null;
        }
    }

    public void validateWsoToken(final String token) throws JwtTokenMalformedException, JwtTokenMissingException {
        log.info("the token is: " + token);
        this.wsoClient = Feign.builder().encoder(new SpringFormEncoder(new SpringEncoder(messageConverters))).decoder(new GsonDecoder()).logger(new Slf4jLogger(LoginService.class)).
                requestInterceptor(new BasicAuthRequestInterceptor("admin", "admin")).target(WsoClient.class, url);
        ValidateTokenDto response = wsoClient.validateToken(token);
        log.info("login response: " + response);
        if (response.getActive().equalsIgnoreCase("false")) {
            throw new JwtTokenMalformedException("JWT Expired");
        }
        else {
            return;
        }
    }
}

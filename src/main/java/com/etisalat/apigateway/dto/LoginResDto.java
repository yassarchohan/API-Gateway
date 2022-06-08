package com.etisalat.apigateway.dto;

import lombok.Data;

@Data
public class LoginResDto {
    private String access_token;
    private String token_type;
    private String expires_in;
}

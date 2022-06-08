package com.etisalat.apigateway.dto;

import lombok.Data;

@Data
public class ValidateTokenDto {
    private String aut;
    private String nbf;
    private String active;
    private String token_type;
    private String exp;
    private String iat;
    private String client_id;
    private String username;
}

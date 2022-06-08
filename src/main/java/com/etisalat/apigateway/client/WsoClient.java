package com.etisalat.apigateway.client;

import com.etisalat.apigateway.dto.LoginResDto;
import com.etisalat.apigateway.dto.ValidateTokenDto;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface WsoClient {

    @RequestLine(value ="POST /oauth2/token")
    LoginResDto authenticate(@Param("grant_type") String grant_type, @Param("username") String username, @Param("password") String password);

    @RequestLine(value ="POST /oauth2/introspect")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    ValidateTokenDto validateToken(@Param("token") String token);

}

package com.saksham.Ecommerce.service;

import com.saksham.Ecommerce.request.LoginRequest;
import com.saksham.Ecommerce.response.AuthResponse;
import com.saksham.Ecommerce.response.SignupRequest;

public interface AuthService {

    void sendLoginAndSignupOtp(String email) throws Exception;
    String createUser(SignupRequest request) throws Exception;
    AuthResponse signing(LoginRequest request) throws Exception;

}

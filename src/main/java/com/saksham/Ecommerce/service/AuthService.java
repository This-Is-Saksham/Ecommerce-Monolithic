package com.saksham.Ecommerce.service;

import com.saksham.Ecommerce.response.SignupRequest;

public interface AuthService {

    String createUser(SignupRequest request);

}

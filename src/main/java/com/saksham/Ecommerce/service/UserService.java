package com.saksham.Ecommerce.service;

import com.saksham.Ecommerce.entity.User;

public interface UserService {

    User findUserByJwtToken(String jwt) throws Exception;
    User findUserByEmail(String email) throws Exception;


}

package com.saksham.Ecommerce.service.impl;

import com.saksham.Ecommerce.config.JwtProvider;
import com.saksham.Ecommerce.entity.User;
import com.saksham.Ecommerce.repository.UserRepository;
import com.saksham.Ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    // first we will find the email id from JWT token, then we will find the user from email
    @Override
    public User findUserByJwtToken(String jwt) throws Exception {
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        return this.findUserByEmail(email);
    }

    @Override
    public User findUserByEmail(String email) throws Exception {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new Exception("user not found with email");
        }
        return user;
    }
}

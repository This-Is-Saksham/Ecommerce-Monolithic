package com.saksham.Ecommerce.service.impl;

import com.saksham.Ecommerce.config.JwtProvider;
import com.saksham.Ecommerce.domain.UserRole;
import com.saksham.Ecommerce.entity.Cart;
import com.saksham.Ecommerce.entity.User;
import com.saksham.Ecommerce.repository.CartRepository;
import com.saksham.Ecommerce.repository.UserRepository;
import com.saksham.Ecommerce.response.SignupRequest;
import com.saksham.Ecommerce.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final JwtProvider jwtProvider;

    @Override
    public String createUser(SignupRequest request) {
        // check user is existed with the email
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            User newUser = new User();
            newUser.setEmail(request.getEmail());
            newUser.setFullName(request.getFullName());
            newUser.setRole(UserRole.ROLE_CUSTOMER);
            newUser.setMobileNumber("123456789"); //dummy mobile number for now
            newUser.setPassword(passwordEncoder.encode(request.getOtp()));
            user = userRepository.save(newUser);

            Cart cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(UserRole.ROLE_CUSTOMER.toString()));
        Authentication authentication = new UsernamePasswordAuthenticationToken(request.getEmail(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtProvider.generateToken(authentication);
    }
}

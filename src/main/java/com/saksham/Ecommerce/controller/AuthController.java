package com.saksham.Ecommerce.controller;

import com.saksham.Ecommerce.domain.UserRole;
import com.saksham.Ecommerce.entity.User;
import com.saksham.Ecommerce.repository.UserRepository;
import com.saksham.Ecommerce.response.AuthResponse;
import com.saksham.Ecommerce.response.SignupRequest;
import com.saksham.Ecommerce.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignupRequest req) {

        String jwt = authService.createUser(req);

        AuthResponse res = new  AuthResponse();
        res.setJwt(jwt);
        res.setMessage("success");
        res.setRole(UserRole.ROLE_CUSTOMER);

        
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}

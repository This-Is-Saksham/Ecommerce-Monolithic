package com.saksham.Ecommerce.controller;

import com.saksham.Ecommerce.domain.UserRole;
import com.saksham.Ecommerce.entity.User;
import com.saksham.Ecommerce.response.AuthResponse;
import com.saksham.Ecommerce.response.SignupRequest;
import com.saksham.Ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/users/profile")
    public ResponseEntity<User> createUserHandler(@RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}

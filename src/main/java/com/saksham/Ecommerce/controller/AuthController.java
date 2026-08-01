package com.saksham.Ecommerce.controller;

import com.saksham.Ecommerce.domain.UserRole;
import com.saksham.Ecommerce.entity.VerificationCode;
import com.saksham.Ecommerce.repository.UserRepository;
import com.saksham.Ecommerce.response.ApiResponse;
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
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignupRequest req) throws Exception {

        String jwt = authService.createUser(req);

        AuthResponse res = new  AuthResponse();
        res.setJwt(jwt);
        res.setMessage("success");
        res.setRole(UserRole.ROLE_CUSTOMER);

        
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/send-otp") //change in api
    public ResponseEntity<ApiResponse> sendOtpHandler(@RequestBody VerificationCode req) throws Exception {

        System.out.println("inside Controller ");
        authService.sendLoginAndSignupOtp(req.getEmail());

        ApiResponse res = new ApiResponse();

        res.setMessage("sent otp successfully on -" + req.getEmail());
        System.out.println("exiting Controller ");

        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}

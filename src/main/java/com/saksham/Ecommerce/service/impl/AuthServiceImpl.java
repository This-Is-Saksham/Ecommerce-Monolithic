package com.saksham.Ecommerce.service.impl;

import com.saksham.Ecommerce.config.JwtProvider;
import com.saksham.Ecommerce.domain.UserRole;
import com.saksham.Ecommerce.entity.Cart;
import com.saksham.Ecommerce.entity.Seller;
import com.saksham.Ecommerce.entity.User;
import com.saksham.Ecommerce.entity.VerificationCode;
import com.saksham.Ecommerce.repository.CartRepository;
import com.saksham.Ecommerce.repository.SellerRepository;
import com.saksham.Ecommerce.repository.UserRepository;
import com.saksham.Ecommerce.repository.VerificationCodeRepository;
import com.saksham.Ecommerce.request.LoginRequest;
import com.saksham.Ecommerce.response.AuthResponse;
import com.saksham.Ecommerce.response.SignupRequest;
import com.saksham.Ecommerce.service.AuthService;
import com.saksham.Ecommerce.service.EmailService;
import com.saksham.Ecommerce.utils.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final JwtProvider jwtProvider;
    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;
    private final CustomUserServiceImpl customUserService;
    private final SellerRepository sellerRepository;


    @Override
    public void sendLoginAndSignupOtp(String email, UserRole role) throws Exception {

        String SIGNING_PREFIX = "signing_";
        String SELLER_PREFIX = "seller_";


        if (email.startsWith(SIGNING_PREFIX)) {

            email = email.substring(SIGNING_PREFIX.length());

            if (role.equals(UserRole.ROLE_SELLER)) {
                Seller seller = sellerRepository.findByEmail(email);
                if(seller == null){
                    throw new Exception("seller not exist with Email");
                }


            }else {
                User user = userRepository.findByEmail(email);
                if(user == null){
                    throw new Exception("user not exist with Email");
                }
            }


        }

        VerificationCode isExist = verificationCodeRepository.findByEmail(email);
        if(isExist != null){
            verificationCodeRepository.delete(isExist); // if the one code exist then delete that code and create new one
        }

        //creating new verification code (OPT Util class)
        String otp = OtpUtil.generateOtp();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(email);

        verificationCodeRepository.save(verificationCode);

        //sending email with otp to user
        String subject = "E-commerce login OTP";
        String text = "your login OTP is - " +otp;

        System.out.println("inside Send Login OTP method ");

        emailService.sendVerificationOtpEmail(email, otp, subject, text);

        System.out.println("exiting Sign login OTP method ");
        System.out.println("exiting Sign login OTP method ");
    }

    @Override
    public String createUser(SignupRequest request) throws Exception {

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(request.getEmail());
        // if verification code is present in the DB with the matching mail id then we will move forward if not then we
        // will throw error.

        if(verificationCode == null || !verificationCode.getOtp().equals(request.getOtp())) {
            throw new Exception("wrong otp");
        }


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

    @Override
    public AuthResponse signing(LoginRequest request) throws Exception {
        String username = request.getEmail();
        String otp = request.getOtp();

        Authentication authentication = authentication(username, otp);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roleName = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Login Success");
        authResponse.setRole(UserRole.valueOf(roleName));

        return authResponse;
    }

    private Authentication authentication(String username, String otp) {
        UserDetails userDetails = customUserService.loadUserByUsername(username);

        String SELLER_PREFIX = "seller_";
        if (username.startsWith(SELLER_PREFIX)) {
            username =  username.substring(SELLER_PREFIX.length());
        }


        if(userDetails == null){
            throw new BadCredentialsException("Invalid username");
        }


        // checking verification code present in DB
        VerificationCode verificationCode = verificationCodeRepository.findByEmail(username);

        if (verificationCode == null || !verificationCode.getOtp().equals(otp)){
            throw new BadCredentialsException("Wrong otp");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}

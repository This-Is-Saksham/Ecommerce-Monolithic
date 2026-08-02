package com.saksham.Ecommerce.service.impl;

import com.saksham.Ecommerce.domain.UserRole;
import com.saksham.Ecommerce.entity.Seller;
import com.saksham.Ecommerce.entity.User;
import com.saksham.Ecommerce.repository.SellerRepository;
import com.saksham.Ecommerce.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@RequiredArgsConstructor
@Service
public class CustomUserServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    // seller prefix because if email or username comes with seller then we need to check in seller table else we can check in user table
    private static final String SELLER_PREFIX = "seller_";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.startsWith(SELLER_PREFIX)) {
            String actualUsername =  username.substring(SELLER_PREFIX.length());
            Seller seller = sellerRepository.findByEmail(actualUsername);

            if(seller != null) {
                return buildUserDetails(seller.getEmail(), seller.getPassword(),  seller.getRole());
            }
        }else {
            User user = userRepository.findByEmail(username);
            if(user != null) {
                return buildUserDetails(user.getEmail(), user.getPassword(), user.getRole());
            }
        }
        throw new UsernameNotFoundException("user not found with this email - " +username);
    }

    private UserDetails buildUserDetails(String email, String password, UserRole role) {
        if(role == null) {
            role = UserRole.ROLE_CUSTOMER;
        }
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority(role.toString()));

        return new  org.springframework.security.core.userdetails.User(email, password, authorityList);
    }
}

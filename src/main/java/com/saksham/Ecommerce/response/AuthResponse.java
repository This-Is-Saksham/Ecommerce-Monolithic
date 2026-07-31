package com.saksham.Ecommerce.response;

import com.saksham.Ecommerce.domain.UserRole;
import lombok.Data;

@Data
public class AuthResponse {
    private String jwt;
    private String message;
    private UserRole role;


}

package com.saksham.Ecommerce.request;

import com.saksham.Ecommerce.domain.UserRole;
import lombok.Data;

@Data
public class LoginOtpRequest {

    private String email;
    private String otp;
    private UserRole role;

}

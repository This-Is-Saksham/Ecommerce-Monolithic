package com.saksham.Ecommerce.service;

import com.saksham.Ecommerce.domain.AccountStatus;
import com.saksham.Ecommerce.entity.Seller;
import com.saksham.Ecommerce.exception.SellerException;

import java.util.List;

public interface SellerService {
    Seller getSellerProfile(String jwt) throws Exception;
    Seller createSeller(Seller seller) throws Exception;
    Seller getSellerById(Long id) throws SellerException;
    Seller getSellerByEmail(String email) throws Exception;
    List<Seller> getAllSellersByAccountStatus(AccountStatus accountStatus);
    Seller updateSeller(Long id, Seller seller) throws Exception;
    void deleteSeller(Long id) throws Exception;
    Seller verifySellerEmail(String email, String otp) throws Exception;
    Seller updateSellerAccountStatus(Long id, AccountStatus accountStatus) throws Exception;
}

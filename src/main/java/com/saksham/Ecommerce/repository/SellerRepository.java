package com.saksham.Ecommerce.repository;

import com.saksham.Ecommerce.domain.AccountStatus;
import com.saksham.Ecommerce.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    Seller findByEmail(String email);
    List<Seller> findByAccountStatus(AccountStatus accountStatus);
}

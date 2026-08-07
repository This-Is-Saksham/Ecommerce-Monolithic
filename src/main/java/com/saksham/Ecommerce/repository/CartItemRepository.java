package com.saksham.Ecommerce.repository;

import com.saksham.Ecommerce.entity.Cart;
import com.saksham.Ecommerce.entity.CartItem;
import com.saksham.Ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartAndProductAndSize(Cart cart, Product product, String size);
}

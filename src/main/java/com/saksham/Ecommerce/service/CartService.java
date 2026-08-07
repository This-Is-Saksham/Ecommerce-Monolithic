package com.saksham.Ecommerce.service;

import com.saksham.Ecommerce.entity.Cart;
import com.saksham.Ecommerce.entity.CartItem;
import com.saksham.Ecommerce.entity.Product;
import com.saksham.Ecommerce.entity.User;

public interface CartService {
    public CartItem addItemToCart(User user, Product product, String size, int quantity);
    public Cart findUserCart(User user);


}

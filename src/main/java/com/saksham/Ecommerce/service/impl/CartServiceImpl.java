package com.saksham.Ecommerce.service.impl;

import com.saksham.Ecommerce.entity.Cart;
import com.saksham.Ecommerce.entity.CartItem;
import com.saksham.Ecommerce.entity.Product;
import com.saksham.Ecommerce.entity.User;
import com.saksham.Ecommerce.repository.CartItemRepository;
import com.saksham.Ecommerce.repository.CartRepository;
import com.saksham.Ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;

    @Override
    public CartItem addItemToCart(User user, Product product, String size, int quantity) {

        Cart cart = findUserCart(user);
        CartItem isPresent = cartItemRepository.findByCartAndProductAndSize(cart, product, size);

        if (isPresent == null) {
            CartItem cartItem = new CartItem();

            int totalPrice = quantity*product.getSellingPrice();

            cartItem.setProduct(product);
            cartItem.setSize(size);
            cartItem.setQuantity(quantity);
            cartItem.setUserId(user.getId());
            cartItem.setSellingPrice(totalPrice);
            cart.getCartItems().add(cartItem);
            cartItem.setCart(cart);
            cartItem.setMrpPrice(quantity*product.getMrpPrice());

            return cartItemRepository.save(cartItem);
        }


        return isPresent;

    }

    @Override
    public Cart findUserCart(User user) {
        Cart cart = cartRepository.findByUserId(user.getId());

        int totalPrice = 0;
        int totalDiscountedPrice = 0;
        int totalItem = 0;

        for(CartItem cartItem : cart.getCartItems()) {
            totalPrice += cartItem.getMrpPrice();
            totalDiscountedPrice += cartItem.getSellingPrice();
            totalItem += cartItem.getQuantity();
        }

        cart.setTotalMRPPrice(totalPrice);
        cart.setTotalItem(totalItem);
        cart.setTotalSellingPrice(totalDiscountedPrice);
        cart.setDiscount(calculateDiscountPercentage(totalPrice, totalDiscountedPrice));

        return cart;
    }

    private int calculateDiscountPercentage(int mrpPrice, int sellingPrice) {
        if(mrpPrice <= 0) {
            return 0;
        }
        double discount = mrpPrice - sellingPrice;
        double discountPercentage = (discount/mrpPrice)*100;
        return (int)discountPercentage;
    }
}

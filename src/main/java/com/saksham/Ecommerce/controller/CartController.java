package com.saksham.Ecommerce.controller;

import com.saksham.Ecommerce.entity.Cart;
import com.saksham.Ecommerce.entity.CartItem;
import com.saksham.Ecommerce.entity.Product;
import com.saksham.Ecommerce.entity.User;
import com.saksham.Ecommerce.request.AddItemRequest;
import com.saksham.Ecommerce.response.ApiResponse;
import com.saksham.Ecommerce.service.impl.CartItemServiceImpl;
import com.saksham.Ecommerce.service.impl.CartServiceImpl;
import com.saksham.Ecommerce.service.impl.ProductServiceImpl;
import com.saksham.Ecommerce.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private  final CartServiceImpl cartService;
    private final CartItemServiceImpl cartItemService;
    private final UserServiceImpl userService;
    private final ProductServiceImpl productService;

    @GetMapping
    public ResponseEntity<Cart> findUserCartHandler(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);

        Cart cart = cartService.findUserCart(user);

        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PutMapping("/add")
    public ResponseEntity<CartItem> addItemToCart (@RequestBody AddItemRequest request,
                                                   @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        Product product = productService.findProductById(request.getProductId());

        CartItem item = cartService.addItemToCart(
                user,
                product,
                request.getSize(),
                request.getQuantity()
        );

        ApiResponse response = new ApiResponse();
        response.setMessage("Item added to Cart Successfully");

        return new ResponseEntity<>(item, HttpStatus.OK);

    }

    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteItemFromCart (@PathVariable Long cartItemId,
                                                           @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        cartItemService.deleteCartItem(user.getId(), cartItemId);

        ApiResponse response = new ApiResponse();
        response.setMessage("Item deleted from Cart Successfully");

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @PutMapping("/item/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItemHandler(@PathVariable Long cartItemId,
                                                          @RequestBody CartItem cartItem,
                                                          @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        CartItem updatedCartItem = null;

        if (cartItem.getQuantity() > 0) {
            updatedCartItem = cartItemService.updateCartItem(user.getId(), cartItemId, cartItem);
        }

        return new ResponseEntity<>(updatedCartItem, HttpStatus.ACCEPTED);
    }

}

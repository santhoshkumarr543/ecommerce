package com.ecommerce.project.controller;

import com.ecommerce.project.model.Cart;
import com.ecommerce.project.payload.CartDTO;
import com.ecommerce.project.repository.CartRepository;
import com.ecommerce.project.service.CartServiceImpl;
import com.ecommerce.project.util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private CartServiceImpl cartService;

    @Tag(name = "Cart APIs", description = "APIs for managing carts")
    @Operation(summary = "Create cart", description = "API to create a new cart")
    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId, @PathVariable Integer quantity){
        CartDTO cartDTO = cartService.addProductToCart(productId, quantity);
        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
    }

    // This method used for admin panel
    @Tag(name = "Cart APIs", description = "APIs for managing carts")
    @Operation(summary = "Get cart", description = "API to get all cart")
    @GetMapping("/carts")
    public ResponseEntity<List<CartDTO>> getCarts(){
        List<CartDTO> cartDTOS = cartService.getAllCarts();
        return new ResponseEntity<>(cartDTOS, HttpStatus.FOUND);
    }

    @Tag(name = "Cart APIs", description = "APIs for managing carts")
    @Operation(summary = "Get cart", description = "API to get a cart by user")
    @GetMapping("/carts/user/cart")
    public ResponseEntity<CartDTO> getCartForSpecificUser(){
        String emailId = authUtil.loggedInEmail();
        Cart cart = cartRepository.findCartByEmail(emailId);
        Long cartId = cart.getCartId();
        CartDTO cartDTO = cartService.getCartForSpecificUser(emailId, cartId);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    // This method is used for increasing and decreasing product quantity in carts
    // Eg. - 1 +
    @Tag(name = "Cart APIs", description = "APIs for managing carts")
    @Operation(summary = "update cart", description = "API to update the product quantity in the cart")
    @PutMapping("/carts/products/{productId}/quantity/{operation}")
    public ResponseEntity<CartDTO> updateProductQuantityInCart(@PathVariable Long productId, @PathVariable String operation){
        CartDTO cartDTO = cartService.updateProductQuantityInCart(productId,
                operation.equalsIgnoreCase("delete")? -1 : 1);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @Tag(name = "Cart APIs", description = "APIs for managing carts")
    @Operation(summary = "Delete cart", description = "API to delete a cart by passing cartId and productId")
    @DeleteMapping("/carts/{cartId}/product/{productId}")
    public ResponseEntity<String> deleteProductFromCart(@PathVariable Long cartId, @PathVariable Long productId){
        String status = cartService.deleteProductFromCart(cartId, productId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}

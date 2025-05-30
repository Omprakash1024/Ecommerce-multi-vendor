package com.app.oneplace.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.oneplace.exceptions.ProductException;
import com.app.oneplace.model.AppUser;
import com.app.oneplace.model.Cart;
import com.app.oneplace.model.CartItem;
import com.app.oneplace.model.Product;
import com.app.oneplace.request.AddItemRequest;
import com.app.oneplace.response.ApiResponse;
import com.app.oneplace.services.CartItemService;
import com.app.oneplace.services.CartService;
import com.app.oneplace.services.ProductService;
import com.app.oneplace.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

	private final CartService cartService;
	private final CartItemService cartItemService;
	private final UserService userService;
	private final  ProductService productService;
	
	@GetMapping()
	public ResponseEntity<Cart> findUserCartHandler(@RequestHeader("Authorization") String jwt) throws Exception{
		
		AppUser user = userService.findUserByJWTtoken(jwt);
		
		Cart cart = cartService.findUserCart(user);
		
		 return new ResponseEntity<Cart>(cart, HttpStatus.OK);
		
	}
	
	@PutMapping("/add")
	public ResponseEntity<CartItem> addItemToCart(
			@RequestBody AddItemRequest req,
			@RequestHeader("Authorization") String jwt) throws ProductException, Exception{
		
		AppUser user = userService.findUserByJWTtoken(jwt);
		Product product = productService.findProductById(req.getProductId());
		
		CartItem item = cartService.addCartItem(user, product, req.getSize(), req.getQuantity());
		
		ApiResponse res = new ApiResponse();
		res.setMessage("Item added to cart successfully");
		
		 return new ResponseEntity<CartItem>(item, HttpStatus.ACCEPTED);
		
	}
	
	@DeleteMapping("/item/{cartItemId}")
	public ResponseEntity<ApiResponse> deleteCartItemHandler(
			@PathVariable Long cartItemId,
			@RequestHeader("Authorization") String jwt) throws  Exception{
		
		AppUser user = userService.findUserByJWTtoken(jwt);
		cartItemService.removeCartItem(user.getId(), cartItemId);
		
		ApiResponse res = new ApiResponse();
		res.setMessage("Item removed from cart successfully");
		
		return new ResponseEntity<ApiResponse>(res, HttpStatus.ACCEPTED);
		
	}
	
	
	@PutMapping("/item/{cartItemId}")
	public ResponseEntity<CartItem> updateCartItemHandler(
			@PathVariable Long cartItemId,
			@RequestBody CartItem cartItem,
			@RequestHeader("Authorization") String jwt) throws 
	Exception{
		
		AppUser user = userService.findUserByJWTtoken(jwt);
	
		CartItem updatedCartItem = null;
		
		if(cartItem.getQuantity()>0) {
			updatedCartItem = cartItemService.updateCartItem(user.getId(), cartItemId, cartItem);
		}
		return new ResponseEntity<CartItem>(updatedCartItem, HttpStatus.ACCEPTED);
		
	}
	
}

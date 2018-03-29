package com.cms.controller.front;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cms.Feedback;
import com.cms.entity.Cart;
import com.cms.entity.CartItem;
import com.cms.entity.Product;
import com.cms.routes.RouteMapping;

/**
 * Controller - 购物车项
 * 
 * 
 * 
 */
@RouteMapping(url = "/cart_item")
public class CartItemController extends BaseController{

	/**
	 * 保存
	 */
	public void save(){
		Long productId = getParaToLong("productId");
		Integer quantity = getParaToInt("quantity");
		Cart currentCart = getCurrentCart();
		CartItem cartItem = new CartItem().dao().find(productId,currentCart.getId());
		if(cartItem == null){
		    cartItem = new CartItem();
			cartItem.setCartId(currentCart.getId());
			cartItem.setQuantity(quantity);
			cartItem.setProductId(productId);
			cartItem.setCreateDate(new Date());
			cartItem.setModifyDate(new Date());
			cartItem.save();
		}else{
		    Integer oldQuantity = cartItem.getQuantity();
		    cartItem.setQuantity(oldQuantity+quantity);
		    cartItem.update();
		}
		Map<String,Object> data = new HashMap<>();
		data.put("quantity", currentCart.getQuantity());
		data.put("totalPrice", currentCart.getTotalPrice());
		List<CartItem> cartItems = currentCart.getCartItems();
		List<Map<String,Object>> cartItemDatas = new ArrayList<>();
		for(CartItem item : cartItems){
		    Map<String,Object> cartItemData = new HashMap<>();
		    cartItemData.put("quantity", item.getQuantity());
		    cartItemData.put("id", item.getId());
		    Product product = item.getProduct();
		    Map<String,Object> productData = new HashMap<>();
		    productData.put("path", product.getPath());
		    productData.put("name", product.getName());
		    productData.put("image", product.getImage());
		    productData.put("price", product.getPrice());
		    cartItemData.put("product", productData);
		    cartItemDatas.add(cartItemData);
		}
		data.put("cartItems", cartItemDatas);
		renderJson(data);
	}
	
	
	/**
	 * 更新
	 */
	public void update(){
	    Cart currentCart = getCurrentCart();
		Long productId = getParaToLong("productId");
		Integer quantity = getParaToInt("quantity");
		Integer type = getParaToInt("type");
		CartItem cartItem = new CartItem().dao().find(productId,currentCart.getId());
		if(cartItem == null){
		    cartItem = new CartItem();
		    cartItem.setCartId(currentCart.getId());
	        cartItem.setQuantity(quantity);
	        cartItem.setProductId(productId);
	        cartItem.setCreateDate(new Date());
	        cartItem.setModifyDate(new Date());
	        cartItem.save();
		}else{
		    if(type==1){
		        Integer oldQuantity = cartItem.getQuantity();
	            cartItem.setQuantity(oldQuantity+quantity);
		    }else{
		        cartItem.setQuantity(quantity);
		    }
		    cartItem.update();
		}
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("subtotal", cartItem.getSubtotal());
		result.put("productQuantity", cartItem.getQuantity());
		result.put("quantity", currentCart.getQuantity());
		result.put("totalPrice", currentCart.getTotalPrice());
		renderJson(result);
	}
	
	
	/**
	 * 删除
	 */
	public void delete(){
		Long id = getParaToLong("id");
		new CartItem().dao().deleteById(id);
		renderJson(Feedback.success(new HashMap<>()));
	}
	
	/**
	 * 清除
	 */
	public void deleteAll(){
		Cart currentCart = getCurrentCart();
		List<CartItem> cartItems = currentCart.getCartItems();
		if(cartItems!=null && cartItems.size()>0){
			for(CartItem cartItem:cartItems){
				cartItem.delete();
			}
		}
		redirect("/");
	}
}

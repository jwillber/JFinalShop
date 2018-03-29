package com.cms.controller.front;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cms.entity.Cart;
import com.cms.entity.CartItem;
import com.cms.entity.Product;
import com.cms.routes.RouteMapping;

/**
 * Controller - 购物车
 * 
 * 
 * 
 */
@RouteMapping(url = "/cart")
public class CartController extends BaseController{

	
	/**
	 * 列表
	 */
	public void list(){
		Cart currentCart = getCurrentCart();
		setAttr("currentCart",currentCart);
		render("/templates/"+getTheme()+"/"+getDevice()+"/cartList.html");
	}
	
	
	/**
	 * 查看
	 */
	public void view(){
	    Cart currentCart = getCurrentCart();
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
	
}

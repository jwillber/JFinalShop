package com.cms.entity;

import java.math.BigDecimal;
import java.util.List;

import com.cms.entity.base.BaseCart;

/**
 * Entity - 购物车
 * 
 * 
 * 
 */
@SuppressWarnings("serial")
public class Cart extends BaseCart<Cart> {
    
    /**
     * 购物车项
     */
    private List<CartItem> cartItems;
    
    /**
     * 会员
     */
    private Member member;
    
    /**
     * 购物车cookie 
     */
    public static final String COOKIE_CART="cookie_cart";
    
   /**
     * 根据cartKey查询购物车
     * 
     * @param cartKey
     *          cartKey
     * @return  购物车
     */
    public Cart findByCartKey(String cartKey){
        return findFirst("select * from kf_cart where cartKey=? ",cartKey);
    }
    
    
    /**
     * 获取购物车项
     * 
     * @return 购物车项
     */
    public List<CartItem> getCartItems(){
        if(cartItems == null){
            cartItems = new CartItem().dao().find("select * from kf_cart_item where cartId = ? ", getId());
        }
        return cartItems;
    }
    
    
    /**
     * 获取会员
     * 
     * @return  会员
     */
    public Member getMember(){
        if(member == null){
            member = new Member().dao().findById(getMemberId());
        }
        return member;
    }
    
    
    /**
     * 获取数量
     * 
     * @return 数量
     */
    public int getQuantity() {
        int quantity = 0;
        List<CartItem> cartItems = getCartItems();
        if (cartItems != null) {
            for (CartItem cartItem : cartItems) {
                if (cartItem.getQuantity() != null) {
                    quantity += cartItem.getQuantity();
                }
            }
        }
        return quantity;
    }
    
    
    /**
     * 获取商品总价格
     * 
     * @return 商品总价格
     */
    public BigDecimal getTotalPrice() {
        BigDecimal totalPrice = BigDecimal.ZERO;
        if (getCartItems() != null) {
            for (CartItem cartItem : getCartItems()) {
                totalPrice = totalPrice.add(cartItem.getSubtotal());
            }
        }
        return totalPrice;
    }

}

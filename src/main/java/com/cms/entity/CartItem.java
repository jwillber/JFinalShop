package com.cms.entity;

import java.math.BigDecimal;

import com.cms.entity.base.BaseCartItem;

/**
 * Entity - 购物车项
 * 
 * 
 * 
 */
@SuppressWarnings("serial")
public class CartItem extends BaseCartItem<CartItem> {
    
    /**
     * 商品
     */
    private Product product;
    
   /**
     * 根据商品id获取订单项
     * 
     * @param productId
     *              商品id
     * @param cartId
     *              购物车id
     * @return  订单项
     */
    public CartItem find(Long productId,Long cartId){
        return  findFirst("select * from kf_cart_item where productId=? and cartId=?", productId,cartId);
    }
    
    /**
     * 获取商品
     * 
     * @return 商品
     */
    public Product getProduct(){
        if(product == null){
            product = new Product().dao().findById(getProductId());
        }
        return product;
    }
    
    /**
     * 获取价格
     * 
     * @return 价格
     */
    public BigDecimal getPrice() {
        if (getProduct() != null && getProduct().getPrice() != null) {
            return getProduct().getPrice();
        } else {
            return BigDecimal.ZERO;
        }
    }
    
    /**
     * 获取小计
     * 
     * @return 小计
     */
    public BigDecimal getSubtotal() {
        if (getQuantity() != null) {
            return getPrice().multiply(new BigDecimal(getQuantity()));
        } else {
            return BigDecimal.ZERO;
        }
    }
}

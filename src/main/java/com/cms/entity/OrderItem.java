package com.cms.entity;

import java.math.BigDecimal;

import com.cms.entity.base.BaseOrderItem;

/**
 * Entity -订单项
 * 
 * 
 * 
 */
@SuppressWarnings("serial")
public class OrderItem extends BaseOrderItem<OrderItem> {
    
    /**
     * 商品
     */
    private Product product;
    
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

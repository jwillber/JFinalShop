package com.cms.entity;

import com.cms.CommonAttribute;
import com.cms.entity.base.BasePayment;

/**
 * Entity - 支付
 * 
 * 
 * 
 */
@SuppressWarnings("serial")
public class Payment extends BasePayment<Payment> {
    
    /**
     * 会员
     */
    private Member member;
    
    /**
     * 获取支付方式
     * 
     * @return 支付方式
     */
    public String getPaymentMethod(){
        return (String) CommonAttribute.paymentMethodNames.get(getPaymentMethod());
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
}

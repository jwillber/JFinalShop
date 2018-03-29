/*
 * 
 * 
 * 
 */
package com.cms.controller.front;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.cms.CommonAttribute;
import com.cms.entity.Cart;
import com.cms.entity.Member;
import com.cms.util.DeviceUtils;
import com.cms.util.SystemUtils;
import com.jfinal.core.Controller;

/**
 * Controller - 基类
 * 
 * 
 * 
 */
public class BaseController extends Controller{
	
   /**
     * 获取当前会员
     * 
     * @return 当前会员
     */
    protected Member getCurrentMember() {
        Member currentMember = (Member) getSession().getAttribute(Member.SESSION_MEMBER);
        return currentMember;
    }
    
    /**
     * 获取当前购物车
     * 
     * @return 当前购物车
     */
    protected Cart getCurrentCart() {
        String cookieCart = getCookie(Cart.COOKIE_CART);
        if(cookieCart==null){
            return setCurrentCart();
        }else{
            Cart currentCart = new Cart().dao().findByCartKey(cookieCart);
            if(currentCart==null){
                //清除cookie
                removeCookie(Cart.COOKIE_CART);
                return setCurrentCart();
            }
            return currentCart;
        }
    }
    
    /**
     * 设置当前购物车
     * 
     * @return  当前购物车
     */
    protected Cart setCurrentCart(){
        String cookieCart=UUID.randomUUID().toString();
        Cart currentCart = new Cart();
        currentCart.setCartKey(cookieCart);
        currentCart.setCreateDate(new Date());
        currentCart.setModifyDate(new Date());
        Member currentMember = getCurrentMember();
        if(currentMember!=null){
            currentCart.setMemberId(currentMember.getId());
        }
        currentCart.save();
        setCookie(Cart.COOKIE_CART, cookieCart, 30*24*60*60);
        return currentCart;
    }
	
	/**
	 * 获取主题
	 * 
	 * @return 主题
	 */
	public String getTheme(){
		return SystemUtils.getConfig().getTheme();
	}
	
	/**
	 * 获取设备
	 * @return
	 */
	public String getDevice(){
	    if(DeviceUtils.isMobile(getRequest())){
	        return "mobile";
	    }
	    return "front";
	}
	
	/**
	 * 获取BigDecimal数据
	 * 
	 * @param name
	 * 			名称
	 * @return BigDecimal数据
	 */
	public BigDecimal getParaToBigDecimal(String name){
		String value = getPara(name);
		if(StringUtils.isNotBlank(value)){
			return new BigDecimal(value);
		}
		return null;
	}

   /**
     * 判断是否是GET请求
     * 
     * @return 是否是GET请求
     */
    public Boolean isGet(){
        if(CommonAttribute.GET.equalsIgnoreCase(getRequest().getMethod())){
            return true;
        }
        return false;
    }
}
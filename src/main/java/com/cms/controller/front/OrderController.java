package com.cms.controller.front;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import com.cms.CommonAttribute;
import com.cms.entity.Cart;
import com.cms.entity.CartItem;
import com.cms.entity.Member;
import com.cms.entity.Order;
import com.cms.entity.OrderItem;
import com.cms.entity.Receiver;
import com.cms.routes.RouteMapping;


/**
 * Controller - 订单
 * 
 * 
 * 
 */
@RouteMapping(url = "/order")
public class OrderController extends BaseController{

	
	/**
	 * 添加
	 */
	public void add(){
		Member currentMember = getCurrentMember();
		Receiver defaultReceiver=new Receiver().dao().findDefault(currentMember.getId());
		if(defaultReceiver==null){
			redirect("/receiver/add");
		}else{
			Cart currentCart = getCurrentCart();
			setAttr("currentCart",currentCart);
			setAttr("defaultReceiver",defaultReceiver);
			render("/templates/"+getTheme()+"/"+getDevice()+"/orderAdd.html");
		}
	}
	
	/**
	 * 保存
	 */
	public void save(){
	    Order order = getModel(Order.class,"",true);
		Member currentMember = getCurrentMember();
		Cart currentCart = getCurrentCart();
		order.setCreateDate(new Date());
		order.setModifyDate(new Date());
		order.setAmount(currentCart.getTotalPrice());
		order.setTotalPrice(currentCart.getTotalPrice());
		order.setQuantity(currentCart.getQuantity());
		order.setSn(DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS")+RandomStringUtils.randomNumeric(5));
		order.setStatus(CommonAttribute.ORDER_STATUS_PENDING_PAYMENT);
		order.setMemberId(currentMember.getId());
		order.save();
		List<CartItem>  cartItems = currentCart.getCartItems();
		for(CartItem cartItem:cartItems){
			OrderItem orderItem = new OrderItem();
			orderItem.setCreateDate(new Date());
			orderItem.setModifyDate(new Date());
			orderItem.setName(cartItem.getProduct().getName());
			orderItem.setPrice(cartItem.getPrice());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setSn(cartItem.getProduct().getSn());
			orderItem.setImage(cartItem.getProduct().getImage());
			orderItem.setOrderId(order.getId());
			orderItem.setProductId(cartItem.getProductId());
			orderItem.save();
		}
		for(CartItem cartItem:cartItems){
			new CartItem().dao().deleteById(cartItem.getId());
		}
		redirect("/payment?orderId="+order.getId());
	}

}

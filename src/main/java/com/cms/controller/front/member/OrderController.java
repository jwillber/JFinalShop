package com.cms.controller.front.member;

import com.cms.controller.front.BaseController;
import com.cms.entity.Member;
import com.cms.entity.Order;

import com.cms.routes.RouteMapping;


/**
 * Controller - 订单
 * 
 * 
 * 
 */
@RouteMapping(url = "/member/order")

public class OrderController extends BaseController{

	/**
	 * 列表
	 */
	public void list(){
		Integer pageNumber = getParaToInt("pageNumber");
		String status = getPara("status");
		if(pageNumber==null){
			pageNumber=1;
		}
		int pageSize = 20 ; 
		Member currentMember = getCurrentMember();
		setAttr("page",new Order().dao().findPage(pageNumber,pageSize,currentMember.getId(),status));
		render("/templates/"+getTheme()+"/"+getDevice()+"/memberOrderList.html");
	}
	
	   
    public void detail(){
        Long id = getParaToLong(0);
        Order order = new Order().dao().findById(id);
        setAttr("order", order);
        render("/templates/"+getTheme()+"/"+getDevice()+"/memberOrderDetail.html");
    }
}

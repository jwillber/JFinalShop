package com.cms.controller.admin;

import com.cms.CommonAttribute;
import com.cms.entity.Order;
import com.cms.routes.RouteMapping;


/**
 * Controller - 订单
 * 
 * 
 * 
 */
@RouteMapping(url = "/admin/order")

public class OrderController extends BaseController{

	/**
	 * 查看
	 */
	public void view(){
		Long id = getParaToLong("id");
		setAttr("order", new Order().dao().findById(id));
		render(getView("order/view"));
	}
	
	
	/**
	 * 发货
	 */
	public void shipping(){
		Long id = getParaToLong("id");
		Order order = new Order().dao().findById(id);
		order.setStatus(CommonAttribute.ORDER_STATUS_SHIPPED);
		order.update();
		redirect("/admin/order/view?id="+id);
	}
	
	/**
	 * 完成
	 */
	public void complete(){
		Long id = getParaToLong("id");
		Order order = new Order().dao().findById(id);
		order.setStatus(CommonAttribute.ORDER_STATUS_COMPLETED);
		order.update();
		redirect("/admin/order/view?id="+id);
	}
	
	
	/**
	 * 列表
	 */
	public void list(){
	    String sn = getPara("sn");
	    Integer pageNumber = getParaToInt("pageNumber");
        if(pageNumber==null){
            pageNumber = 1;
        }
		setAttr("page", new Order().dao().findPage(sn,pageNumber,PAGE_SIZE));
		setAttr("sn", sn);
		render(getView("order/list"));
	}
}

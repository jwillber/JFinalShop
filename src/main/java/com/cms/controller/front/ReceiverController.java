package com.cms.controller.front;

import java.util.Date;

import com.cms.entity.Area;
import com.cms.entity.Receiver;

import com.cms.routes.RouteMapping;


/**
 * Controller - 收货地址
 * 
 * 
 * 
 */
@RouteMapping(url = "/receiver")

public class ReceiverController extends BaseController{

	/**
	 * 添加
	 */
	public void add(){
		render("/templates/"+getTheme()+"/"+getDevice()+"/receiverAdd.html");
	}
	
	/**
	 * 保存
	 */
	public void save(){
		Receiver defaultReceiver=new Receiver().dao().findDefault(getCurrentMember().getId());
		if(defaultReceiver!=null && defaultReceiver.getIsDefault()){
		    defaultReceiver.setIsDefault(false);
		    defaultReceiver.update();
		}
		Receiver receiver = getModel(Receiver.class,"",true);
		receiver.setCreateDate(new Date());
		receiver.setModifyDate(new Date());
		receiver.setAreaName(receiver.getArea().getFullName());
		receiver.setMemberId(getCurrentMember().getId());
		receiver.setIsDefault(true);
		receiver.save();
		redirect("/order/add");
	}
	
	/**
	 * 编辑
	 */
	public void list(){
		setAttr("receivers", new Receiver().dao().findList(getCurrentMember().getId()));
		render("/templates/"+getTheme()+"/"+getDevice()+"/receiverList.html");
	}
	
	
	/**
	 * 更新
	 */
	public void update(){
		Receiver defaultReceiver=new Receiver().dao().findDefault(getCurrentMember().getId());
		if(defaultReceiver!=null && defaultReceiver.getIsDefault()){
            defaultReceiver.setIsDefault(false);
            defaultReceiver.update();
        }
		Receiver receiver = getModel(Receiver.class,"",true);
		if(receiver.getAreaId()!=null){
		    receiver.setAreaName(receiver.getArea().getFullName());
		}
		receiver.setModifyDate(new Date());
		receiver.setIsDefault(true);
		receiver.update();
		redirect("/order/add");
	}
	
	
}

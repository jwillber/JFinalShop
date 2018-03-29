package com.cms.controller.front.member;

import java.util.Date;

import com.cms.Feedback;
import com.cms.controller.front.BaseController;
import com.cms.entity.Receiver;

import com.cms.routes.RouteMapping;

/**
 * Controller - 收货地址
 * 
 * 
 * 
 */
@RouteMapping(url = "/member/receiver")

public class ReceiverController extends BaseController{

    
    /**
     * 添加
     */
    public void add() {
        render("/templates/"+getTheme()+"/"+getDevice()+"/memberReceiverAdd.html");
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
		receiver.setAreaName(receiver.getArea().getFullName());
		receiver.setCreateDate(new Date());
		receiver.setModifyDate(new Date());
		receiver.setMemberId(getCurrentMember().getId());
		receiver.setIsDefault(true);
		receiver.save();
		redirect("/member/receiver/list");
	}
	
	   /**
     * 编辑
     */
    public void edit() {
        Long id = getParaToLong("id");
        setAttr("receiver", new Receiver().dao().findById(id));
        render("/templates/"+getTheme()+"/"+getDevice()+"/memberReceiverEdit.html");
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
		redirect("/member/receiver/list");
	}
	
	/**
	 * 列表
	 */
	public void list(){
		setAttr("receivers", new Receiver().dao().findList(getCurrentMember().getId()));
		render("/templates/"+getTheme()+"/"+getDevice()+"/memberReceiverList.html");
	}
	
   /**
     * 删除
     */
    public void delete(){
        Long id = getParaToLong("id");
        new Receiver().dao().deleteById(id);
        renderJson(Feedback.success(""));
    }
}

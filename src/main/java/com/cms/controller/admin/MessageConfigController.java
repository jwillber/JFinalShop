/*
 * 
 * 
 * 
 */
package com.cms.controller.admin;

import java.util.Date;

import com.cms.CommonAttribute;
import com.cms.entity.MessageConfig;
import com.cms.routes.RouteMapping;

/**
 * Controller - 消息配置
 * 
 * 
 * 
 */
@RouteMapping(url = "/admin/message_config")
public class MessageConfigController extends BaseController {

	/**
	 * 编辑
	 */
	public void edit() {
		Long id = getParaToLong("id");
		setAttr("messageConfigTypeNames", CommonAttribute.messageConfigTypeNames);
		setAttr("messageConfig", new MessageConfig().dao().findById(id));
		render(getView("message_config/edit"));
	}

	/**
	 * 更新
	 */
	public void update() {
		MessageConfig messageConfig = getModel(MessageConfig.class,"",true);   
		Boolean isSmsEnabled = messageConfig.getIsSmsEnabled();
		if(isSmsEnabled == null){
			messageConfig.setIsSmsEnabled(false);
		}
		messageConfig.setModifyDate(new Date());
		messageConfig.update();
		redirect("/admin/message_config/list");
	}

	/**
	 * 列表
	 */
	public void list() {
		setAttr("messageConfigs", new MessageConfig().dao().findAll());
		setAttr("messageConfigTypeNames", CommonAttribute.messageConfigTypeNames);
		render(getView("message_config/list"));
	}

}
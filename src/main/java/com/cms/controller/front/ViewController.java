package com.cms.controller.front;

import com.cms.routes.RouteMapping;

/**
 * Controller - 页面
 * 
 * 
 * 
 */
@RouteMapping(url = "/view")
public class ViewController extends BaseController{

	/**
	 * 首页
	 */
	public void index(){
		String viewName = getPara(0);
		render("/templates/"+getTheme()+"/"+getDevice()+"/"+viewName+".html");
	}
}

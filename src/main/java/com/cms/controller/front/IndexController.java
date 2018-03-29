package com.cms.controller.front;

import com.cms.routes.RouteMapping;

/**
 * Controller - 首页
 * 
 * 
 * 
 */
@RouteMapping(url = "/")
public class IndexController extends BaseController{

	/**
	 * 首页
	 */
	public void index(){
	    render("/templates/"+getTheme()+"/"+getDevice()+"/index.html");
	}
}

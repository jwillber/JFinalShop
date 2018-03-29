package com.cms.controller.front.member;

import com.cms.controller.front.BaseController;
import com.cms.routes.RouteMapping;

/**
 * Controller - 首页
 * 
 * 
 * 
 */
@RouteMapping(url = "/member")
public class IndexController extends BaseController{
	
	
	public void index(){
	    render("/templates/"+getTheme()+"/"+getDevice()+"/member.html");
	}

}

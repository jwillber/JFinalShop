/*
 * 
 * 
 * 
 */
package com.cms.controller.admin;


import com.cms.routes.RouteMapping;
import com.jfinal.core.JFinal;

/**
 * Controller - 共用
 * 
 * 
 * 
 */
@RouteMapping(url = "/admin/common")

public class CommonController extends BaseController {



	/**
	 * 主页
	 */
	public void main() {
	    render(getView("common/main"));
	}
	
	/**
	 * 首页
	 */
	public void index() {
		setAttr("javaVersion", System.getProperty("java.version"));
		setAttr("javaHome", System.getProperty("java.home"));
		setAttr("osName", System.getProperty("os.name"));
		setAttr("osArch", System.getProperty("os.arch"));
		setAttr("serverInfo", JFinal.me().getServletContext().getServerInfo());
		setAttr("servletVersion", JFinal.me().getServletContext().getMajorVersion() + "." + JFinal.me().getServletContext().getMinorVersion());
		render(getView("common/index"));
	}
}
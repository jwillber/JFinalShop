package com.cms.controller.front;

import org.apache.commons.codec.digest.DigestUtils;

import com.cms.Feedback;
import com.cms.entity.Member;
import com.cms.routes.RouteMapping;

/**
 * Controller - 密码
 * 
 * 
 * 
 */
@RouteMapping(url = "/password")
public class PasswordController extends BaseController{

	/**
	 * 忘记密码
	 */
	public void forgot(){
	    if(isGet()){
	        render("/templates/"+getTheme()+"/"+getDevice()+"/passwordForgot.html");
	    }else{
	        String username = getPara("username");
	        String password = getPara("password");
	        Member member = new Member().dao().findByUsername(username);
	        if(member==null){
	            setAttr("feedback", Feedback.error("用户不存在"));
	            render("/templates/"+getTheme()+"/"+getDevice()+"/passwordForgot.html");
	        }else{
	            member.setPassword(DigestUtils.md5Hex(password));
	            member.update();
	            redirect("/login");
	        }
	    }
	}
}

package com.cms.controller.front;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import com.cms.Feedback;
import com.cms.entity.Member;
import com.cms.routes.RouteMapping;

/**
 * Controller - 登录
 * 
 * 
 * 
 */
@RouteMapping(url = "/login")
public class LoginController extends BaseController{

	/**
	 * 登录页面
	 */
	public void index(){
	    String username = getPara("username");
        String password = getPara("password");
        if(StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)){
            Member member = new Member().dao().findByUsername(username);
            if(member == null){
                setAttr("feedback", Feedback.error("用户不存在"));
            }else if(!DigestUtils.md5Hex(password).equals(member.getPassword())){
                setAttr("feedback", Feedback.error("用户名密码错误"));
            }else{
                getSession().setAttribute(Member.SESSION_MEMBER, member);
                redirect("/");
                return;
            }
        }
		render("/templates/"+getTheme()+"/"+getDevice()+"/login.html");
	}
}

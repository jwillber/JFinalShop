package com.cms.controller.front;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import com.cms.Feedback;
import com.cms.entity.Member;
import com.cms.routes.RouteMapping;

/**
 * Controller - 注册
 * 
 * 
 * 
 */
@RouteMapping(url = "/register")
public class RegisterController extends BaseController{

	/**
	 * 注册页面
	 */
	public void index(){
	    String username = getPara("username");
        String password = getPara("password");
        if(StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)){
        	Member pMember = new Member().dao().findByUsername(username);
        	if(pMember!=null){
        		setAttr("feedback", Feedback.error("用户已存在"));
        		render("/templates/"+getTheme()+"/"+getDevice()+"/register.html");
        	}else{
        		Member member = new Member();
        		member.setMobile(username);
        		member.setPassword(DigestUtils.md5Hex(password));
        		member.setAmount(BigDecimal.ZERO);
        		member.setBalance(BigDecimal.ZERO);
        		member.setRegisterIp(getRequest().getRemoteAddr());
        		member.setCreateDate(new Date());
        		member.setModifyDate(new Date());
        		member.save();
        		redirect("/login");
        	}
        }else{
            render("/templates/"+getTheme()+"/"+getDevice()+"/register.html");
        }
	}
}

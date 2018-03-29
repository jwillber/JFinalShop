package com.cms.controller.front.member;

import org.apache.commons.codec.digest.DigestUtils;

import com.cms.Feedback;
import com.cms.controller.front.BaseController;
import com.cms.entity.Member;
import com.cms.routes.RouteMapping;

@RouteMapping(url = "/member/password")
public class PasswordController extends BaseController{

    /**
     * 编辑
     */
    public void edit(){
        render("/templates/"+getTheme()+"/"+getDevice()+"/memberPasswordEdit.html");
    }
    
    /**
     * 修改
     */
    public void update(){
        String currentPassword = getPara("currentPassword");
        String password = getPara("password");
        Member currentMember = getCurrentMember();
        if(!currentMember.getPassword().equals(DigestUtils.md5Hex(currentPassword))){
            renderJson(Feedback.error("密码错误!"));
            return;
        }
        currentMember.setPassword(DigestUtils.md5Hex(password));
        currentMember.update();
        renderJson(Feedback.success(""));
    }
}

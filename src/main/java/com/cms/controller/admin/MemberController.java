package com.cms.controller.admin;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import com.cms.Feedback;
import com.cms.entity.Member;
import com.cms.routes.RouteMapping;


/**
 * Controller - 会员
 * 
 * 
 * 
 */
@RouteMapping(url = "/admin/member")

public class MemberController extends BaseController{

	/**
	 * 添加
	 */
	public void add(){
	    render(getView("member/add"));
	}
	
	/**
	 * 保存
	 */
	public void save(){
		Member member = getModel(Member.class,"",true);
		member.setPassword(DigestUtils.md5Hex(member.getPassword()));
		member.setCreateDate(new Date());
		member.setModifyDate(new Date());
		member.setBalance(BigDecimal.ZERO);
		member.save();
		redirect(getListQuery("/admin/member/list"));
	}
	
	
	/**
	 * 编辑
	 */
	public void edit(){
		Long id = getParaToLong("id");
		setAttr("member", new Member().dao().findById(id));
		render(getView("member/edit"));
	}
	
	/**
	 * 更新
	 */
	public void update(){
	    Long id = getParaToLong("id");
	    String password = getPara("password");
	    Member member = new Member().dao().findById(id);
	    if(StringUtils.isNotBlank(password)){
	        member.setPassword(DigestUtils.md5Hex(password));
	        member.update();
	    }
		redirect(getListQuery("/admin/member/list"));
	}
	
	/**
	 * 列表
	 */
	public void list(){
	    String mobile = getPara("mobile");
	    Integer pageNumber = getParaToInt("pageNumber");
        if(pageNumber==null){
            pageNumber = 1;
        }
		setAttr("page", new Member().dao().findPage(mobile,pageNumber,PAGE_SIZE));
		setAttr("mobile", mobile);
		render(getView("member/list"));
	}
	
	/**
	 * 删除
	 */
	public void delete(){
		Long ids[] = getParaValuesToLong("ids");
		for(Long id:ids){
			new Member().dao().deleteById(id);
		}
		renderJson(Feedback.success(new HashMap<>()));
	}
}

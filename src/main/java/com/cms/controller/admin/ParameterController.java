package com.cms.controller.admin;

import java.util.Date;
import java.util.HashMap;

import com.alibaba.fastjson.JSONArray;
import com.cms.Feedback;
import com.cms.entity.Parameter;
import com.cms.entity.ProductCategory;
import com.cms.routes.RouteMapping;


/**
 * Controller - 参数
 * 
 * 
 * 
 */
@RouteMapping(url = "/admin/parameter")

public class ParameterController extends BaseController{


	/**
	 * 添加
	 */
	public void add(){
		Long sampleId = getParaToLong("sampleId");
		setAttr("sample", new Parameter().dao().findById(sampleId));
		setAttr("productCategoryTree", new ProductCategory().dao().findTree());
		render(getView("parameter/add"));
	}
	
	/**
	 * 保存
	 */
	public void save(){
		Parameter parameter = getModel(Parameter.class,"",true);
		parameter.setCreateDate(new Date());
		parameter.setModifyDate(new Date());
		parameter.setName(JSONArray.toJSONString(getParaValues("name")));
		parameter.save();
		redirect(getListQuery("/admin/parameter/list"));
	}
	
	/**
	 * 编辑
	 */
	public void edit(){
		Long id = getParaToLong("id");
		setAttr("parameter", new Parameter().dao().findById(id));
		render(getView("parameter/edit"));
	}
	
	/**
	 * 更新
	 */
	public void update(){
		Parameter parameter = getModel(Parameter.class,"",true);
		parameter.set("names", JSONArray.toJSONString(getParaValues("names")));
		parameter.update();
		redirect("/admin/parameter/list");
	}
	
	/**
	 * 列表
	 */
	public void list(){
	    String subgroup = getPara("subgroup");
	    Integer pageNumber = getParaToInt("pageNumber");
        if(pageNumber==null){
            pageNumber = 1;
        }
		setAttr("page", new Parameter().dao().findPage(subgroup,pageNumber,PAGE_SIZE));
		setAttr("subgroup", subgroup);
		render(getView("parameter/list"));
	}
	
	/**
	 * 删除
	 */
	public void delete(){
		Long ids[] = getParaValuesToLong("ids");
		for(Long id:ids){
			new Parameter().dao().deleteById(id);
		}
		renderJson(Feedback.success(new HashMap<>()));
	}
	
	
}

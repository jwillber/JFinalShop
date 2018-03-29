package com.cms.controller.admin;

import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang.ArrayUtils;

import com.cms.Feedback;
import com.cms.entity.Article;
import com.cms.entity.ArticleCategory;
import com.cms.routes.RouteMapping;


/**
 * Controller - 文章
 * 
 * 
 * 
 */
@RouteMapping(url = "/admin/article")

public class ArticleController extends BaseController{

	/**	
	 * 添加
	 */
	public void add(){
		setAttr("articleCategoryTree", new ArticleCategory().dao().findTree());
		render(getView("article/add"));
	}
	
	/**
	 * 保存
	 */
	public void save(){
		Article article = getModel(Article.class,"",true);
		article.setCreateDate(new Date());
		article.setModifyDate(new Date());
		article.setHits(0l);
		article.save();
		redirect(getListQuery("/admin/article/list"));
	}
	
	/**
	 * 编辑
	 */
	public void edit(){
		setAttr("articleCategoryTree", new ArticleCategory().dao().findTree());
		setAttr("article", new Article().dao().findById(getParaToLong("id")));
		render(getView("article/edit"));
	}
	
	/**
	 * 更新
	 */
	public void update(){
		getModel(Article.class,"",true).update();
		redirect(getListQuery("/admin/article/list"));
	}
	
	/**
	 * 列表
	 */
	public void list(){
	    String title = getPara("title");
	    Integer pageNumber = getParaToInt("pageNumber");
        if(pageNumber==null){
            pageNumber = 1;
        }
        Long articleCategoryId = getParaToLong("articleCategoryId");
		setAttr("page", new Article().dao().findPage(articleCategoryId,title,pageNumber,PAGE_SIZE));
		setAttr("title", title);
		render(getView("article/list"));
	}
	
	/**
	 * 删除
	 */
	public void delete(){
	    Long ids[] = getParaValuesToLong("ids");
        if(ArrayUtils.isNotEmpty(ids)){
            for(Long id:ids){
                new Article().dao().deleteById(id);
            }
        }
        renderJson(Feedback.success(new HashMap<>()));
	}
	
}

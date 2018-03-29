package com.cms.controller.admin;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.cms.Feedback;
import com.cms.entity.Article;
import com.cms.entity.ArticleCategory;
import com.cms.routes.RouteMapping;


/**
 * Controller - 文章分类
 * 
 * 
 * 
 */
@RouteMapping(url = "/admin/article_category")

public class ArticleCategoryController extends BaseController{

	/**
	 * 添加
	 */
	public void add(){
		setAttr("articleCategoryTree", new ArticleCategory().dao().findTree());
		render(getView("article_category/add"));
	}
	
	/**
	 * 保存
	 */
	public void save(){
		ArticleCategory articleCategory = getModel(ArticleCategory.class,"",true);
		articleCategory.setCreateDate(new Date());
		articleCategory.setModifyDate(new Date());
		articleCategory.setValue();
		articleCategory.save();
		redirect(getListQuery("/admin/article_category/list"));
	}
	
	/**
	 * 编辑
	 */
	public void edit(){
		setAttr("articleCategory", new ArticleCategory().dao().findById(getParaToLong("id")));
		render(getView("article_category/edit"));
	}
	
	/**
	 * 更新
	 */
	public void update(){
		getModel(ArticleCategory.class,"",true).update();
		redirect(getListQuery("/admin/article_category/list"));
	}
	
	/**
	 * 列表
	 */
	public void list(){
		setAttr("articleCategoryTree", new ArticleCategory().dao().findTree());
		render(getView("article_category/list"));
	}
	
	/**
	 * 删除
	 */
	public void delete(){
	    Long id = getParaToLong("id");
	    ArticleCategory articleCategory = new ArticleCategory().dao().findById(id);
        if (articleCategory == null) {
            renderJson(Feedback.error("分类不存在"));
            return;
        }
        List<ArticleCategory> children = articleCategory.getChildren();
        if (children != null && !children.isEmpty()) {
            renderJson(Feedback.error("存在下级分类，无法删除"));
            return;
        }
        List<Article> articles = articleCategory.getArticles();
        if (articles != null && !articles.isEmpty()) {
            renderJson(Feedback.error("存在下级内容，无法删除"));
            return;
        }
        new ArticleCategory().dao().deleteById(id);
        renderJson(Feedback.success(new HashMap<>()));
	}
}

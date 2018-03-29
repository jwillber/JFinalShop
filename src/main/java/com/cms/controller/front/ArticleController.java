package com.cms.controller.front;

import com.cms.CommonAttribute;
import com.cms.entity.Article;
import com.cms.entity.ArticleCategory;
import com.cms.routes.RouteMapping;

/**
 * Controller - 文章
 * 
 * 
 * 
 */
@RouteMapping(url = "/article")
public class ArticleController extends BaseController{
	
    /**
     * 每页记录数
     */
    private static final int PAGE_SIZE = 20;
	
	/**
	 * 内容
	 */
	public void detail(){
		Long articleId = getParaToLong(0);
		Integer pageNumber = getParaToInt(1);
		Article article = new Article().dao().findById(articleId);
        if (article == null || pageNumber < 1 || pageNumber > article.getTotalPages()) {
            render(CommonAttribute.FRONT_RESOURCE_NOT_FOUND_VIEW);
            return;
        }
        setAttr("article", article);
        setAttr("pageNumber", pageNumber);
		render("/templates/"+getTheme()+"/"+getDevice()+"/articleDetail.html");
	}

	/**
	 * 列表
	 */
	public void list(){
		Long articleCategoryId = getParaToLong(0);
		Integer pageNumber = getParaToInt("pageNumber");
		String orderBy = getPara("orderBy");
		if(pageNumber==null){
			pageNumber=1;
		}
		setAttr("page",new Article().dao().findPage(articleCategoryId,pageNumber,PAGE_SIZE,orderBy));
		setAttr("articleCategory",new ArticleCategory().dao().findById(articleCategoryId));
		render("/templates/"+getTheme()+"/"+getDevice()+"/articleList.html");
	}
}

/*
 * 
 * 
 * 
 */
package com.cms.template.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.cms.TemplateVariable;
import com.cms.entity.ArticleCategory;
import com.cms.util.FreeMarkerUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 模板指令 - 下级文章分类列表
 * 
 * 
 * 
 */
@TemplateVariable(name="article_category_children_list")
public class ArticleCategoryChildrenListDirective extends BaseDirective {

	/** "文章分类ID"参数名称 */
	private static final String articleCategoryId_PARAMETER_NAME = "articleCategoryId";
	
	/** "是否递归"参数名称 */
	private static final String RECURSIVE_PARAMETER_NAME = "recursive";

	/** 变量名称 */
	private static final String VARIABLE_NAME = "articleCategories";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		Long articleCategoryId = FreeMarkerUtils.getParameter(articleCategoryId_PARAMETER_NAME,Long.class, params);
		Boolean recursive = FreeMarkerUtils.getParameter(RECURSIVE_PARAMETER_NAME,Boolean.class, params);
		Integer count = getCount(params);
		List<ArticleCategory> articleCategories = new ArticleCategory().dao().findChildren(articleCategoryId,recursive != null ? recursive : true, count);
		setLocalVariable(VARIABLE_NAME, articleCategories, env, body);
	}

}
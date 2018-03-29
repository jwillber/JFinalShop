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
import com.cms.entity.Article;
import com.cms.util.FreeMarkerUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 模板指令 - 文章列表
 * 
 * 
 * 
 */
@TemplateVariable(name="article_list")
public class ArticleListDirective extends BaseDirective {

	/** "文章分类ID"参数名称 */
	private static final String articleCategoryId_PARAMETER_NAME = "articleCategoryId";

	/** 变量名称 */
	private static final String VARIABLE_NAME = "articles";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		Long articleCategoryId = FreeMarkerUtils.getParameter(articleCategoryId_PARAMETER_NAME, Long.class, params);
		Integer count = getCount(params);
		String orderBy = getOrderBy(params);
		List<Article> articles = new Article().dao().findList(articleCategoryId, null, count, orderBy);
		setLocalVariable(VARIABLE_NAME, articles, env, body);
	}

}
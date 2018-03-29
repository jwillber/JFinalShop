package com.cms.controller.front;

import org.apache.commons.lang.BooleanUtils;

import com.cms.CommonAttribute;
import com.cms.entity.Product;
import com.cms.entity.ProductCategory;
import com.cms.routes.RouteMapping;

/**
 * Controller - 商品
 * 
 * 
 * 
 */
@RouteMapping(url = "/product")
public class ProductController extends BaseController{
	
	
	/**
	 * 详情页
	 */
	public void detail(){
		Long productId=getParaToLong(0);
		Product product = new Product().dao().findById(productId);
        if (product == null || BooleanUtils.isNotTrue(product.getIsMarketable())) {
            render(CommonAttribute.FRONT_RESOURCE_NOT_FOUND_VIEW);
            return;
        }
        setAttr("product", product);
		render("/templates/"+getTheme()+"/"+getDevice()+"/productDetail.html");
	}
	
	
	/**
	 * 列表
	 */
	public void list(){
		Long productCategoryId=getParaToLong(0);
		Integer pageNumber = getParaToInt("pageNumber");
		String orderBy = getPara("orderBy");
		if(pageNumber==null){
			pageNumber=1;
		}
		int pageSize = 20 ; 
		setAttr("page",new Product().dao().findPage(pageNumber,pageSize,productCategoryId,null,orderBy));
		setAttr("productCategory",new ProductCategory().dao().findById(productCategoryId));
		render("/templates/"+getTheme()+"/"+getDevice()+"/productList.html");
	}
	
	/**
	 * 搜索
	 */
	public void search(){
		Integer pageNumber = getParaToInt("pageNumber");
		String orderBy = getPara("orderBy");
		String keyword = getPara("keyword");
		if(pageNumber==null){
			pageNumber=1;
		}
		int pageSize = 20 ; 
		setAttr("page",new Product().dao().findPage(pageNumber,pageSize,null,keyword,orderBy));
		setAttr("keyword", keyword);
		render("/templates/"+getTheme()+"/"+getDevice()+"/productSearch.html");
	}

}

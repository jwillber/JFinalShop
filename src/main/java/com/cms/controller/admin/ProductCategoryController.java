package com.cms.controller.admin;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.cms.Feedback;
import com.cms.entity.Product;
import com.cms.entity.ProductCategory;
import com.cms.routes.RouteMapping;


/**
 * Controller - 商品分类
 * 
 * 
 * 
 */
@RouteMapping(url = "/admin/product_category")

public class ProductCategoryController extends BaseController{

    /**
     * 添加
     */
    public void add(){
        setAttr("productCategoryTree", new ProductCategory().dao().findTree());
        render(getView("product_category/add"));
    }
    
    /**
     * 保存
     */
    public void save(){
        ProductCategory productCategory = getModel(ProductCategory.class,"",true);
        productCategory.setCreateDate(new Date());
        productCategory.setModifyDate(new Date());
        productCategory.setValue();
        productCategory.save();
        redirect(getListQuery("/admin/product_category/list"));
    }
    
    /**
     * 编辑
     */
    public void edit(){
        setAttr("productCategory", new ProductCategory().dao().findById(getParaToLong("id")));
        render(getView("product_category/edit"));
    }
    
    /**
     * 更新
     */
    public void update(){
        getModel(ProductCategory.class,"",true).update();
        redirect(getListQuery("/admin/product_category/list"));
    }
    
    /**
     * 列表
     */
    public void list(){
        setAttr("productCategoryTree", new ProductCategory().dao().findTree());
        render(getView("product_category/list"));
    }
    
    /**
     * 删除
     */
    public void delete(){
        Long id = getParaToLong("id");
        ProductCategory productCategory = new ProductCategory().dao().findById(id);
        if (productCategory == null) {
            renderJson(Feedback.error("分类不存在"));
            return;
        }
        List<ProductCategory> children = productCategory.getChildren();
        if (children != null && !children.isEmpty()) {
            renderJson(Feedback.error("存在下级分类，无法删除"));
            return;
        }
        List<Product> products = productCategory.getProducts();
        if (products != null && !products.isEmpty()) {
            renderJson(Feedback.error("存在下级内容，无法删除"));
            return;
        }
        new ProductCategory().dao().deleteById(id);
        renderJson(Feedback.success(new HashMap<>()));
    }
}
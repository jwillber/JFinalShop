package com.cms.entity;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.cms.entity.base.BaseParameter;
import com.cms.util.DBUtils;
import com.jfinal.plugin.activerecord.Page;

/**
 * Entity -参数
 * 
 * 
 * 
 */
@SuppressWarnings("serial")
public class Parameter extends BaseParameter<Parameter> {
    
    /**
     * 商品分类
     */
    private ProductCategory productCategory;
    
   /**
     * 查找参数分页
     * 
     * @param pageable
     *            分页信息
     * @return 参数分页
     */
    public Page<Parameter> findPage(String subgroup,Integer pageNumber,Integer pageSize){
        String filterSql = "";
        if(StringUtils.isNotBlank(subgroup)){
            filterSql+= " and subgroup like '%"+subgroup+"%'";
        }
        String orderBySql = DBUtils.getOrderBySql("sort asc");
        return paginate(pageNumber, pageSize, "select *", "from kf_parameter where 1=1 "+filterSql+orderBySql);
    }
    
    /**
     * 查找商品列表
     * 
     * @param productCategoryId
     *            商品分类Id
     * @param siteId
     *            站点id              
     * @return 商品列表
     */
    public List<Parameter> findList(Long productCategoryId){
        String filterSql = "";
        if(productCategoryId!=null){
            filterSql+=" and productCategoryId="+productCategoryId;
        }
        String orderBySql = DBUtils.getOrderBySql("sort asc");
        return find("select * from kf_parameter where 1=1 "+filterSql+orderBySql);
    }
    
    /**
     * 获取参数名称列表
     * @return  名称列表
     */
    public List<String> getNames(){
        return JSONArray.parseArray(getName(), String.class);
    }
    
    /**
     * 获取商品分类
     * @return 商品分类
     */
    public ProductCategory getProductCategory(){
        if(productCategory == null){
            productCategory = new ProductCategory().dao().findById(getProductCategoryId());
        }
        return productCategory;
    }
}

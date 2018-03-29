package com.cms.entity;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.cms.entity.base.BaseProduct;
import com.cms.util.DBUtils;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

/**
 * Entity - 商品
 * 
 * 
 * 
 */
@SuppressWarnings("serial")
public class Product extends BaseProduct<Product> {
    
    /**
     * 路径
     */
    private static final String PATH = "/product/detail/%d";
    
	
    /**
     * 商品分类
     */
    private ProductCategory productCategory;
    
   /**
     * 查找商品分页
     * 
     * @param pageNumber
     *            页码
     * @param pageSize
     *            每页记录数
     * @param productCategoryId
     *            商品分类Id
     * @return 商品分页
     */
    public Page<Product> findPage(Integer pageNumber,Integer pageSize,Long productCategoryId){
        String filterSql = "";
        if(productCategoryId!=null){
            filterSql+=" and productCategoryId="+productCategoryId;
        }
        String orderBySql = DBUtils.getOrderBySql("createDate desc");
        return paginate(pageNumber, pageSize, "select *", "from kf_product where 1=1 "+filterSql+orderBySql);
    }
    
    /**
     * 查找商品分页
     * 
     * @param pageNumber
     *            页码
     * @param pageSize
     *            每页记录数
     * @param productCategoryId
     *            商品分类Id
     * @param keyword
     *            关键词
     * @param orderBy
     *            排序
     * @return 商品分页
     */
    public Page<Product> findPage(Integer pageNumber,Integer pageSize,Long productCategoryId,String keyword,String orderBy){
        String filterSql = "";
        if(productCategoryId!=null){
            filterSql+=" and productCategoryId="+productCategoryId;
        }
        if(StrKit.notBlank(keyword)){
            filterSql+=" and name like '%"+keyword+"%'";
        }
        String orderBySql = "";
        if(StringUtils.isBlank(orderBy)){
            orderBySql = DBUtils.getOrderBySql("createDate desc");
        }else{
            orderBySql = DBUtils.getOrderBySql(orderBy);
        }
        return paginate(pageNumber, pageSize, "select *", "from kf_product where 1=1 "+filterSql+orderBySql);
    }
    
    /**
     * 查找商品列表
     * 
     * @param productCategoryId
     *            商品分类Id
     * @param first
     *            起始记录
     * @param count
     *            数量
     * @param orderBy
     *            排序
     * @return 商品列表
     */
    public List<Product> findList(Long productCategoryId,Integer first,Integer count,String orderBy){
        String filterSql = "";
        if(productCategoryId!=null){
            filterSql+=" and productCategoryId="+productCategoryId;
        }
        String orderBySql = "";
        if(StringUtils.isBlank(orderBy)){
            orderBySql = DBUtils.getOrderBySql("createDate desc");
        }else{
            orderBySql = DBUtils.getOrderBySql(orderBy);
        }
        String countSql=DBUtils.getCountSql(first, count);
        return find("select * from kf_product where 1=1 "+filterSql+orderBySql+countSql);
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
    
    /**
     * 获取参数
     * @return 参数
     */
    public List<ParameterValue> getParameterValues(){
        if(StrKit.notBlank(getParameterValue())){
            return JSONArray.parseArray(getParameterValue(),ParameterValue.class);
        }
        return null;
    }
    
    /**
     * 获取路径
     * 
     * @return 路径
     */
    public String getPath() {
        return String.format(Product.PATH, getId());
    }
}

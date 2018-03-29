package com.cms.entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;

import com.cms.entity.base.BaseProductCategory;
import com.cms.util.DBUtils;

/**
 * Entity - 商品分类
 * 
 * 
 * 
 */
@SuppressWarnings("serial")
public class ProductCategory extends BaseProductCategory<ProductCategory> {
    /**
     * 树路径分隔符
     */
    public static final String TREE_PATH_SEPARATOR = ",";

    /**
     * 路径
     */
    private static final String PATH = "/product/list/%d";
    
    /**
     * 商品
     */
    private List<Product> products;
    
    /**
     * 下级分类
     */
    private List<ProductCategory> children;
    
    /**
     * 上级分类
     */
    private ProductCategory parent;
    
    /**
     * 参数
     */
    private List<Parameter> parameters;
    
    /**
     * 获取商品
     * 
     * @return 商品
     */
    public List<Product> getProducts(){
        if(products == null){
            products = new Product().dao().find("select * from kf_product where productCategoryId = ?",getId());
        }
        return products;
    }
    
    /**
     * 获取下级分类
     * 
     * @return 下级分类
     */
    public List<ProductCategory> getChildren() {
        if(children == null){
            children = new ProductCategory().dao().find("select * from kf_product_category where parentId=?",getParentId());
        }
        return children;
    }
    
    /**
     * 获取上级分类
     * 
     * @return  上级分类
     */
    public ProductCategory getParent(){
        if(parent == null){
            parent = findById(getParentId());
        }
        return parent;
    }
    
    /**
     * 获取参数
     * 
     * @return 参数
     */
    public List<Parameter> getParameters() {
        if(parameters == null){
            parameters = new Parameter().dao().find("select * from kf_parameter where productCategoryId = ?",getId());
        }
        return parameters;
    }
    
    
   /**
     * 查找顶级商品分类
     * 
     * @return 顶级商品分类
     */
    public List<ProductCategory> findRoots() {
        return findRoots(null);
    }
    
    /**
     * 查找顶级商品分类
     * 
     * @param count
     *            数量
     * @return 顶级商品分类
     */
    public List<ProductCategory> findRoots(Integer count){
        String orderBySql = DBUtils.getOrderBySql("sort asc");
        String countSql=DBUtils.getCountSql(null, count);
        return find("select * from kf_product_category where parentId is null "+orderBySql+countSql);
    }
    
    
    /**
     * 查找上级商品分类
     * 
     * @param productCategoryId
     *            商品分类Id
     * @param recursive
     *            是否递归
     * @param count
     *            数量
     * @return 上级商品分类
     */
    public List<ProductCategory> findParents(Long productCategoryId, boolean recursive, Integer count){
        ProductCategory productCategory = findById(productCategoryId);
        if(productCategoryId == null || productCategory.getParentId() == null){
            return Collections.emptyList();
        }
        if(recursive){
            String countSql=DBUtils.getCountSql(null, count);
            String orderBySql = DBUtils.getOrderBySql("grade asc");
            return find("select * from kf_product_category where id in ("+StringUtils.join(productCategory.getParentIds(), ",")+") "+orderBySql+countSql);
        }else{
            return find("select * from kf_product_category where id = ? ",findById(productCategoryId).getParentId());
        }
    }
    
    
    /**
     * 查找下级商品分类
     * 
     * @param productCategoryId
     *            商品分类Id
     * @param recursive
     *            是否递归
     * @param count
     *            数量
     * @return 下级商品分类
     */
    public List<ProductCategory> findChildren(Long productCategoryId,boolean recursive,Integer count){
        if(recursive){
            String countSql=DBUtils.getCountSql(null, count);
            String orderBySql = DBUtils.getOrderBySql("grade asc,sort asc");
            List<ProductCategory> productCategories;
            if(productCategoryId!=null){
                productCategories = find("select * from kf_product_category where treePath like ? "+orderBySql+countSql,"%,"+productCategoryId+",%");
            }else{
                productCategories = find("select * from kf_product_category where 1=1 "+orderBySql+countSql);
            }
            sort(productCategories);
            return productCategories;
        }else{
            String orderBySql = DBUtils.getOrderBySql("sort asc");
            return find("select * from kf_product_category where parentId = ? "+orderBySql,productCategoryId);
        }
    }
    
    /**
     * 查找商品分类树
     * 
     * @param siteId
     *            站点id 
     * @return 商品分类树
     */
    public List<ProductCategory> findTree(){
        return findChildren(null,true,null);
    }
    
    
    /**
     * 获取所有上级分类ID
     * 
     * @return 所有上级分类ID
     */
    public Long[] getParentIds() {
        String[] parentIds = StringUtils.split(getTreePath(), TREE_PATH_SEPARATOR);
        Long[] result = new Long[parentIds.length];
        for (int i = 0; i < parentIds.length; i++) {
            result[i] = Long.valueOf(parentIds[i]);
        }
        return result;
    }
    
    
    /**
      * 排序商品分类
      * 
      * @param productCategories
      *            商品分类
      */
     private void sort(List<ProductCategory> productCategories) {
         if (CollectionUtils.isEmpty(productCategories)) {
             return;
         }
         final Map<Long, Integer> sortMap = new HashMap<>();
         for (ProductCategory productCategory : productCategories) {
             sortMap.put(productCategory.getId(), productCategory.getSort());
         }
         Collections.sort(productCategories, new Comparator<ProductCategory>() {
             @Override
             public int compare(ProductCategory productCategory1, ProductCategory productCategory2) {
                 Long[] ids1 = (Long[]) ArrayUtils.add(productCategory1.getParentIds(), productCategory1.getId());
                 Long[] ids2 = (Long[]) ArrayUtils.add(productCategory2.getParentIds(), productCategory2.getId());
                 Iterator<Long> iterator1 = Arrays.asList(ids1).iterator();
                 Iterator<Long> iterator2 = Arrays.asList(ids2).iterator();
                 CompareToBuilder compareToBuilder = new CompareToBuilder();
                 while (iterator1.hasNext() && iterator2.hasNext()) {
                     Long id1 = iterator1.next();
                     Long id2 = iterator2.next();
                     Integer sort1 = sortMap.get(id1);
                     Integer sort2 = sortMap.get(id2);
                     compareToBuilder.append(sort1, sort2).append(id1, id2);
                     if (!iterator1.hasNext() || !iterator2.hasNext()) {
                         compareToBuilder.append(productCategory1.getGrade(), productCategory2.getGrade());
                     }
                 }
                 return compareToBuilder.toComparison();
             }
         });
     }
    
    
    /**
     * 设置值
     * 
     */
    public void setValue() {
        if (getParentId() != null) {
            ProductCategory parent = findById(getParentId());
            setTreePath(parent.getTreePath() + parent.getId() + ProductCategory.TREE_PATH_SEPARATOR);
        } else {
            setTreePath(ProductCategory.TREE_PATH_SEPARATOR);
        }
        setGrade(getParentIds().length);
    }
    
    /**
     * 获取路径
     * 
     * @return 路径
     */
    public String getPath() {
        return String.format(ProductCategory.PATH, getId());
    }
}

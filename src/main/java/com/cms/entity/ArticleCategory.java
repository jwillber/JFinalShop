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

import com.cms.entity.base.BaseArticleCategory;
import com.cms.util.DBUtils;

/**
 * Entity - 文章分类
 * 
 * 
 * 
 */
@SuppressWarnings("serial")
public class ArticleCategory extends BaseArticleCategory<ArticleCategory> {
    
    /**
     * 树路径分隔符
     */
    public static final String TREE_PATH_SEPARATOR = ",";

    /**
     * 路径
     */
    private static final String PATH = "/article/list/%d";
    
    /**
     * 文章
     */
    private List<Article> articles;
    
    /**
     * 下级分类
     */
    private List<ArticleCategory> children;
    
    /**
     * 上级分类
     */
    private ArticleCategory parent;
    
   /**
     * 获取文章
     * 
     * @return 文章
     */
    public List<Article> getArticles(){
        if(articles == null){
            articles = new Article().dao().find("select * from kf_article where articleCategoryId = ?",getId());
        }
        return articles;
    }
    
    /**
     * 获取下级分类
     * 
     * @return 下级分类
     */
    public List<ArticleCategory> getChildren() {
        if(children == null){
            children = find("select * from kf_article_category where parentId=?",getParentId());
        }
        return children;
    }
    
    /**
     * 获取上级分类
     * @return  上级分类
     */
    public ArticleCategory getParent(){
        if(parent == null){
            parent = findById(getParentId());
        }
        return parent;
    }
	
	
   /**
     * 查找顶级文章分类
     * 
     * @return 顶级文章分类
     */
    public List<ArticleCategory> findRoots() {
        return findRoots(null);
    }
	
	/**
     * 查找顶级文章分类
     * 
     * @param count
     *            数量
     * @return 顶级文章分类
     */
    public List<ArticleCategory> findRoots(Integer count){
        String orderBySql = DBUtils.getOrderBySql("sort asc");
        String countSql=DBUtils.getCountSql(null, count);
        return find("select * from kf_article_category where parentId is null "+orderBySql+countSql);
    }
    
    
    /**
     * 查找上级文章分类
     * 
     * @param articleCategoryId
     *            文章分类Id
     * @param recursive
     *            是否递归
     * @param count
     *            数量
     * @return 上级文章分类
     */
    public List<ArticleCategory> findParents(Long articleCategoryId, boolean recursive, Integer count){
        ArticleCategory articleCategory = findById(articleCategoryId);
        if(articleCategoryId == null || articleCategory.getParentId() == null){
            return Collections.emptyList();
        }
        if(recursive){
            String countSql=DBUtils.getCountSql(null, count);
            String orderBySql = DBUtils.getOrderBySql("grade asc");
            return find("select * from kf_article_category where id in ("+StringUtils.join(articleCategory.getParentIds(), ",")+") "+orderBySql+countSql);
        }else{
            return find("select * from kf_article_category where id = ? ",findById(articleCategoryId).getParentId());
        }
    }
    
    
    /**
     * 查找下级文章分类
     * 
     * @param articleCategoryId
     *            文章分类Id
     * @param recursive
     *            是否递归
     * @param count
     *            数量
     * @return 下级文章分类
     */
    public List<ArticleCategory> findChildren(Long articleCategoryId,boolean recursive,Integer count){
        if(recursive){
            String countSql=DBUtils.getCountSql(null, count);
            String orderBySql = DBUtils.getOrderBySql("grade asc,sort asc");
            List<ArticleCategory> articleCategories;
            if(articleCategoryId!=null){
                articleCategories = find("select * from kf_article_category where treePath like ? "+orderBySql+countSql,"%,"+articleCategoryId+",%");
            }else{
                articleCategories = find("select * from kf_article_category where 1=1 "+orderBySql+countSql);
            }
            sort(articleCategories);
            return articleCategories;
        }else{
            String orderBySql = DBUtils.getOrderBySql("sort asc");
            return find("select * from kf_article_category where parentId = ? "+orderBySql,articleCategoryId);
        }
    }
    
    /**
     * 查找文章分类树
     * 
     * @param siteId
     *            站点id 
     * @return 文章分类树
     */
    public List<ArticleCategory> findTree(){
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
      * 排序文章分类
      * 
      * @param articleCategories
      *            文章分类
      */
     private void sort(List<ArticleCategory> articleCategories) {
         if (CollectionUtils.isEmpty(articleCategories)) {
             return;
         }
         final Map<Long, Integer> sortMap = new HashMap<>();
         for (ArticleCategory articleCategory : articleCategories) {
             sortMap.put(articleCategory.getId(), articleCategory.getSort());
         }
         Collections.sort(articleCategories, new Comparator<ArticleCategory>() {
             @Override
             public int compare(ArticleCategory articleCategory1, ArticleCategory articleCategory2) {
                 Long[] ids1 = (Long[]) ArrayUtils.add(articleCategory1.getParentIds(), articleCategory1.getId());
                 Long[] ids2 = (Long[]) ArrayUtils.add(articleCategory2.getParentIds(), articleCategory2.getId());
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
                         compareToBuilder.append(articleCategory1.getGrade(), articleCategory2.getGrade());
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
            ArticleCategory parent = findById(getParentId());
            setTreePath(parent.getTreePath() + parent.getId() + ArticleCategory.TREE_PATH_SEPARATOR);
        } else {
            setTreePath(ArticleCategory.TREE_PATH_SEPARATOR);
        }
        setGrade(getParentIds().length);
    }
    
    /**
     * 获取路径
     * 
     * @return 路径
     */
    public String getPath() {
        return String.format(ArticleCategory.PATH, getId());
    }
}

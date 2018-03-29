package com.cms.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.cms.entity.base.BaseArticle;
import com.cms.util.DBUtils;
import com.jfinal.plugin.activerecord.Page;

/**
 * Entity - 文章
 * 
 * 
 * 
 */
@SuppressWarnings("serial")
public class Article extends BaseArticle<Article> {
    
    /**
     * 路径
     */
    private static final String PATH = "/article/detail/%d_%d";

    /**
     * 内容分页长度
     */
    private static final int PAGE_CONTENT_LENGTH = 2000;

    /**
     * 内容分页标签
     */
    private static final String PAGE_BREAK_TAG = "page_break_tag";

    /**
     * 段落配比
     */
    private static final Pattern PARAGRAPH_PATTERN = Pattern.compile("[^,;\\.!?，；。！？]*([,;\\.!?，；。！？]+|$)");
	
    /**
     * 文章分类
     */
    private ArticleCategory articleCategory;
    
    
    /**
     * 获取文章分类
     * @return 文章分类
     */
    public ArticleCategory getArticleCategory(){
        if(articleCategory == null){
            articleCategory = new ArticleCategory().dao().findById(getArticleCategoryId());
        }
        return articleCategory;
    }
    
    
   /**
     * 查找文章分页
     * 
     * @param articleCategoryId
     *            文章分类Id
     * @param pageable
     *            分页信息
     * @param siteId
     *            站点id            
     * @return 文章分页
     */
    public Page<Article> findPage(Long articleCategoryId,String title,Integer pageNumber,Integer pageSize){
        String filterSql = "";
        if(articleCategoryId!=null){
            filterSql+=" and articleCategoryId="+articleCategoryId;
        }
        if(StringUtils.isNotBlank(title)){
            filterSql+= " and title like '%"+title+"%'";
        }
        String orderBySql = DBUtils.getOrderBySql("createDate desc");
        return paginate(pageNumber, pageSize, "select *", "from kf_article where 1=1 "+filterSql+orderBySql);
    }
    
    /**
     * 查找文章分页
     * 
     * @param articleCategoryId
     *            文章分类Id
     * @param pageNumber
     *            页码
     * @param pageSize
     *            每页记录数
     * @param order
     *            排序
     * @param direction
     *            方向
     * @param siteId
     *            站点id                 
     * @return 文章分页
     */
    public Page<Article> findPage(Long articleCategoryId,Integer pageNumber,Integer pageSize,String orderBy){
        String filterSql = "";
        if(articleCategoryId!=null){
            filterSql+=" and articleCategoryId="+articleCategoryId;
        }
        String orderBySql = "";
        if(StringUtils.isNotBlank(orderBy)){
            orderBySql = DBUtils.getOrderBySql(orderBy);
        }else{
            orderBySql = DBUtils.getOrderBySql("createDate desc");
        }
        return paginate(pageNumber, pageSize, "select *", "from kf_article where 1=1 "+filterSql+orderBySql);
    }
    
    /**
     * 查找文章列表
     * 
     * @param articleCategoryId
     *            文章分类Id
     * @param first
     *            起始记录
     * @param count
     *            数量
     * @param beginDate
     *            起始日期
     * @param endDate
     *            结束日期
     * @param filters
     *            筛选
     * @param orderBy
     *            排序
     * @param siteId
     *            站点id                 
     * @return 文章列表
     */
    public List<Article> findList(Long articleCategoryId,Integer first,Integer count,String orderBy){
        String filterSql = "";
        if(articleCategoryId!=null){
            filterSql+=" and articleCategoryId="+articleCategoryId;
        }
        String orderBySql = "";
        if(StringUtils.isNotBlank(orderBy)){
            orderBySql = DBUtils.getOrderBySql(orderBy);
        }else{
            orderBySql = DBUtils.getOrderBySql("createDate desc");
        }
        String countSql=DBUtils.getCountSql(first, count);
        return find("select * from kf_article where 1=1 "+filterSql+orderBySql+countSql);
    }
    
    
    /**
     * 获取路径
     * 
     * @return 路径
     */
    public String getPath() {
        return getPath(1);
    }

    /**
     * 获取路径
     * 
     * @param pageNumber
     *            页码
     * @return 路径
     */
    public String getPath(Integer pageNumber) {
        return String.format(Article.PATH, getId(), pageNumber);
    }

    /**
     * 获取文本内容
     * 
     * @return 文本内容
     */
    public String getText() {
        if (StringUtils.isEmpty(getContent())) {
            return StringUtils.EMPTY;
        }
        return StringUtils.remove(Jsoup.parse(getContent()).text(), PAGE_BREAK_TAG);
    }

    /**
     * 获取分页内容
     * 
     * @return 分页内容
     */
    public String[] getPageContents() {
        if (StringUtils.isEmpty(getContent())) {
            return new String[] { StringUtils.EMPTY };
        }
        if (StringUtils.contains(getContent(), PAGE_BREAK_TAG)) {
            return StringUtils.splitByWholeSeparator(getContent(), PAGE_BREAK_TAG);
        }
        List<Node> childNodes = Jsoup.parse(getContent()).body().childNodes();
        if (CollectionUtils.isEmpty(childNodes)) {
            return new String[] { getContent() };
        }
        List<String> pageContents = new ArrayList<>();
        int textLength = 0;
        StringBuilder paragraph = new StringBuilder();
        for (Node node : childNodes) {
            if (node instanceof Element) {
                Element element = (Element) node;
                paragraph.append(element.outerHtml());
                textLength += element.text().length();
                if (textLength >= PAGE_CONTENT_LENGTH) {
                    pageContents.add(paragraph.toString());
                    textLength = 0;
                    paragraph.setLength(0);
                }
            } else if (node instanceof TextNode) {
                TextNode textNode = (TextNode) node;
                Matcher matcher = PARAGRAPH_PATTERN.matcher(textNode.text());
                while (matcher.find()) {
                    String content = matcher.group();
                    paragraph.append(content);
                    textLength += content.length();
                    if (textLength >= PAGE_CONTENT_LENGTH) {
                        pageContents.add(paragraph.toString());
                        textLength = 0;
                        paragraph.setLength(0);
                    }
                }
            }
        }
        String pageContent = paragraph.toString();
        if (StringUtils.isNotEmpty(pageContent)) {
            pageContents.add(pageContent);
        }
        return pageContents.toArray(new String[pageContents.size()]);
    }

    /**
     * 获取分页内容
     * 
     * @param pageNumber
     *            页码
     * @return 分页内容
     */
    public String getPageContent(Integer pageNumber) {
        if (pageNumber == null || pageNumber < 1) {
            return null;
        }
        String[] pageContents = getPageContents();
        if (pageContents.length < pageNumber) {
            return null;
        }
        return pageContents[pageNumber - 1];
    }

    /**
     * 获取总页数
     * 
     * @return 总页数
     */
    public int getTotalPages() {
        return getPageContents().length;
    }
    
    
    /**
     * 获取上一篇文章
     * 
     * @return 上一篇文章
     */
    public Article getLastArticle(){
        return findFirst("select * from kf_article where id < ? limit 1",getId());
    }
    
    /**
     * 获取下一篇文章
     * 
     * @return 下一篇文章
     */
    public Article getNextArticle(){
        return findFirst("select * from kf_article where id > ? limit 1",getId());
    }
}

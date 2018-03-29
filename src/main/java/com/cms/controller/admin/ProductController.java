package com.cms.controller.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.time.DateFormatUtils;

import com.alibaba.fastjson.JSONArray;
import com.cms.Feedback;
import com.cms.entity.Parameter;
import com.cms.entity.ParameterValue;
import com.cms.entity.Product;
import com.cms.entity.ProductCategory;
import com.cms.entity.ProductImage;
import com.cms.routes.RouteMapping;
import com.cms.util.ProductImageUtils;
import com.jfinal.upload.UploadFile;

/**
 * Controller - 商品
 * 
 * 
 * 
 */
@RouteMapping(url = "/admin/product")

public class ProductController extends BaseController {

    /**
     * 添加
     */
    public void add() {
        setAttr("productCategoryTree", new ProductCategory().dao().findTree());
        render(getView("product/add"));
    }

    /**
     * 保存
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void save() {
        List<UploadFile> uploadFiles = getFiles();
        Product product = getModel(Product.class, "", true);
        List<ParameterValue> parameterValues = getBatchBean(ParameterValue.class, "parameterValues");
        List<ProductImage> productImages = getBatchBean(ProductImage.class, "productImages");
        if (uploadFiles != null) {
            Collections.sort(uploadFiles, new Comparator<UploadFile>() {
                @Override
                public int compare(UploadFile uploadFile1, UploadFile uploadFile2) {
                    // TODO Auto-generated method stub
                    CompareToBuilder compareToBuilder = new CompareToBuilder();
                    compareToBuilder.append(uploadFile1.getParameterName(), uploadFile2.getParameterName());
                    return compareToBuilder.toComparison();
                }
            });
            for (int i = 0; i < uploadFiles.size(); i++) {
                ProductImage productImage = productImages.get(i);
                productImage.setFile(uploadFiles.get(i));
            }
        }

        filterProductImage(productImages);
        filterParameterValue(parameterValues);

        if (CollectionUtils.isNotEmpty(parameterValues)) {
            product.setParameterValue(JSONArray.toJSONString(parameterValues));
        } else {
            product.setParameterValue("[]");
        }
        ProductImageUtils.generate(productImages);
        if (CollectionUtils.isNotEmpty(productImages)) {
            product.setProductImage(JSONArray.toJSONString(productImages));
        } else {
            product.setProductImage("[]");
        }
        product.setParameterValue(JSONArray.toJSONString(parameterValues));
        product.setSn(DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS") + RandomStringUtils.randomNumeric(5));
        product.setCreateDate(new Date());
        product.setModifyDate(new Date());
        product.setHits(0l);
        product.setSales(0l);
        product.save();
        redirect(getListQuery("/admin/product/list"));
    }

    /**
     * 编辑
     */
    public void edit() {
        setAttr("productCategoryTree", new ProductCategory().dao().findTree());
        setAttr("product", new Product().dao().findById(getParaToLong("id")));
        render(getView("product/edit"));
    }

    /**
     * 更新
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void update() {
        List<UploadFile> uploadFiles = getFiles();
        Product product = getModel(Product.class, "", true);
        List<ParameterValue> parameterValues = getBatchBean(ParameterValue.class, "parameterValues");
        List<ProductImage> productImages = getBatchBean(ProductImage.class, "productImages");
        if (uploadFiles != null) {
            Collections.sort(uploadFiles, new Comparator<UploadFile>() {
                @Override
                public int compare(UploadFile uploadFile1, UploadFile uploadFile2) {
                    // TODO Auto-generated method stub
                    CompareToBuilder compareToBuilder = new CompareToBuilder();
                    compareToBuilder.append(uploadFile1.getParameterName(), uploadFile2.getParameterName());
                    return compareToBuilder.toComparison();
                }
            });
            for (int i = 0; i < uploadFiles.size(); i++) {
                ProductImage productImage = productImages.get(i);
                productImage.setFile(uploadFiles.get(i));
            }
        }

        filterProductImage(productImages);
        filterParameterValue(parameterValues);
        
        
        if (CollectionUtils.isNotEmpty(parameterValues)) {
            product.setParameterValue(JSONArray.toJSONString(parameterValues));
        } else {
            product.setParameterValue("[]");
        }

        ProductImageUtils.generate(productImages);
        if (CollectionUtils.isNotEmpty(productImages)) {
            product.setProductImage(JSONArray.toJSONString(productImages));
        } else {
            product.setProductImage("[]");
        }
        product.setParameterValue(JSONArray.toJSONString(parameterValues));
        product.setModifyDate(new Date());
        product.update();
        redirect(getListQuery("/admin/product/list"));
    }

    /**
     * 列表
     */
    public void list() {
        Integer pageNumber = getParaToInt("pageNumber");
        if (pageNumber == null) {
            pageNumber = 1;
        }
        Long productCategoryId = getParaToLong("productCategoryId");
        setAttr("page", new Product().dao().findPage(pageNumber, PAGE_SIZE, productCategoryId));
        render(getView("product/list"));
    }

    /**
     * 删除
     */
    public void delete() {
        Long ids[] = getParaValuesToLong("ids");
        for (Long id : ids) {
            new Product().dao().deleteById(id);
        }
        renderJson(Feedback.success(new HashMap<>()));
    }

    /**
     * 获取参数
     */
    public void parameters() {
        Long productCategoryId = getParaToLong("productCategoryId");
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        ProductCategory productCategory = new ProductCategory().dao().findById(productCategoryId);
        List<Parameter> parameters = productCategory.getParameters();
        if (productCategory == null || parameters == null || parameters.size() == 0) {
            renderJson(data);
        }
        for (Parameter parameter : parameters) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("subGroup", parameter.getSubgroup());
            item.put("names", parameter.getNames());
            data.add(item);
        }
        renderJson(data);
    }

    private void filterParameterValue(List<ParameterValue> parameterValues) {
        CollectionUtils.filter(parameterValues, new Predicate() {
            public boolean evaluate(Object object) {
                ParameterValue parameterValue = (ParameterValue) object;
                if (parameterValue == null || StringUtils.isEmpty(parameterValue.getGroup())) {
                    return false;
                }
                CollectionUtils.filter(parameterValue.getEntries(), new Predicate() {
                    private Set<String> set = new HashSet<String>();

                    public boolean evaluate(Object object) {
                        ParameterValue.Entry entry = (ParameterValue.Entry) object;
                        return entry != null && StringUtils.isNotEmpty(entry.getName())
                                && StringUtils.isNotEmpty(entry.getValue()) && set.add(entry.getName());
                    }
                });
                return CollectionUtils.isNotEmpty(parameterValue.getEntries());
            }
        });
    }

    private void filterProductImage(List<ProductImage> productImages) {
        CollectionUtils.filter(productImages, new Predicate() {
            public boolean evaluate(Object object) {
                ProductImage productImage = (ProductImage) object;
                return productImage != null && !productImage.isEmpty();
            }
        });
    }
}

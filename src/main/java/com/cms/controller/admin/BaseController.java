/*
 * 
 * 
 * 
 */
package com.cms.controller.admin;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.cms.CommonAttribute;
import com.cms.entity.Admin;
import com.jfinal.core.Controller;
import com.jfinal.core.Injector;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.activerecord.TableMapping;

/**
 * Controller - 基类
 * 
 * 
 * 
 */
public class BaseController extends Controller {

    /** 每页记录数 */
    protected static final int PAGE_SIZE = 20;

    /** 列表查询Cookie名称 */
    private static final String LIST_QUERY_COOKIE_NAME = "listQuery";

    /**
     * 获取当前管理员
     * 
     * @return 当前管理员
     */
    protected Admin getCurrentAdmin() {
        Admin currentAdmin = (Admin) getSession().getAttribute(Admin.SESSION_ADMIN);
        return currentAdmin;
    }

    /**
     * 获取页面
     * 
     * @return 页面
     */
    public String getView(String view) {
        return CommonAttribute.ADMIN_PATH + view + CommonAttribute.VIEW_EXTENSION;
    }

    /**
     * 获取列表参数
     * 
     * @return 列表参数
     */
    public String getListQuery(String url) {
        String listQuery = getCookie(LIST_QUERY_COOKIE_NAME);
        if (StringUtils.isNotBlank(url) && StringUtils.isNotEmpty(listQuery)) {
            if (StringUtils.startsWith(listQuery, "?")) {
                listQuery = listQuery.substring(1);
            }
            if (StringUtils.contains(url, "?")) {
                url = url + "&" + listQuery;
            } else {
                url = url + "?" + listQuery;
            }
            removeCookie(LIST_QUERY_COOKIE_NAME);
        }
        return url;
    }

    protected <T> List<T> getBatchBean(Class<T> beanClass, String prefix) {
        List<String> keys = getArrayKeys(prefix);
        if (keys.size() < 1) {
            return null;
        }
        List<T> modelList = new ArrayList<T>();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            T t = Injector.injectBean(beanClass, key, getRequest(), true);
            Field[] fields = t.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.getType() == List.class) {
                    field.setAccessible(true);
                    ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                    java.lang.reflect.Type actualType = parameterizedType.getActualTypeArguments()[0];
                    try {
                        Class actualTypeClass = Class.forName(actualType.toString().substring(6));
                        Table table = TableMapping.me().getTable(actualTypeClass);
                        if (table == null) {
                            field.set(t, getBatchBean(actualTypeClass, key + "." + field.getName()));
                        } else {
                            field.set(t, getBatchModel(actualTypeClass, key + "." + field.getName()));
                        }
                    } catch (ClassNotFoundException | IllegalArgumentException | IllegalAccessException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            modelList.add(t);
        }
        return modelList;
    }

    protected <T> List<T> getBatchModel(Class<T> modelClass, String prefix) {
        List<String> keys = getArrayKeys(prefix);
        if (keys.size() < 1) {
            return null;
        }
        List<T> modelList = new ArrayList<T>();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            T t = Injector.injectModel(modelClass, key, getRequest(), true);
            Field[] fields = t.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.getType() == List.class) {
                    field.setAccessible(true);
                    ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                    java.lang.reflect.Type actualType = parameterizedType.getActualTypeArguments()[0];
                    try {
                        Class actualTypeClass = Class.forName(actualType.toString().substring(6));
                        Table table = TableMapping.me().getTable(actualTypeClass);
                        if (table == null) {
                            field.set(t, getBatchBean(actualTypeClass, key + "." + field.getName()));
                        } else {
                            field.set(t, getBatchModel(actualTypeClass, key + "." + field.getName()));
                        }
                    } catch (ClassNotFoundException | IllegalArgumentException | IllegalAccessException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            modelList.add(t);
        }
        return modelList;
    }

    protected <T> T getSingleModel(Class<T> modelClass, String prefix) {
        T t = Injector.injectModel(modelClass, prefix, getRequest(), true);
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getType() == List.class) {
                field.setAccessible(true);
                ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                java.lang.reflect.Type actualType = parameterizedType.getActualTypeArguments()[0];
                try {
                    Class actualTypeClass = Class.forName(actualType.toString().substring(6));
                    Table table = TableMapping.me().getTable(actualTypeClass);
                    if (table == null) {
                        field.set(t, getBatchBean(actualTypeClass, field.getName()));
                    } else {
                        field.set(t, getBatchModel(actualTypeClass, field.getName()));
                    }
                } catch (ClassNotFoundException | IllegalArgumentException | IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return t;
    }

    protected List<String> getArrayKeys(String prefix) {
        Set<String> keys = new HashSet<String>();
        String arrayPrefix = prefix + "[";
        String key = null;
        Enumeration<String> names = getRequest().getParameterNames();
        while (names.hasMoreElements()) {
            key = names.nextElement();
            if (!key.startsWith(arrayPrefix)) {
                continue;
            }
            if (key.indexOf("]") == -1) {
                continue;
            }
            String index = key.substring(arrayPrefix.length(), arrayPrefix.length() + 1);
            keys.add(arrayPrefix + index + "]");
        }
        List<String> list = new ArrayList<String>(keys);
        Collections.sort(list);
        return list;
    }

    /**
     * 获取BigDecimal数据
     * 
     * @param name
     *            名称
     * @return BigDecimal数据
     */
    public BigDecimal getParaToBigDecimal(String name) {
        String value = getPara(name);
        if (StringUtils.isNotBlank(value)) {
            return new BigDecimal(value);
        }
        return null;
    }
}
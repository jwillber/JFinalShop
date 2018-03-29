/*
 * 
 * 
 * 
 */
package com.cms;

import java.util.Map;

import org.apache.commons.collections.map.ListOrderedMap;

/**
 * 公共参数
 * 
 * 
 * 
 */
public final class CommonAttribute {
	
	/** 日期格式配比 */
	public static final String[] DATE_PATTERNS = new String[] { "yyyy", "yyyy-MM", "yyyyMM", "yyyy/MM", "yyyy-MM-dd", "yyyyMMdd", "yyyy/MM/dd", "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss", "yyyy/MM/dd HH:mm:ss" };

	/** UTF-8编码 */
	public static final String UTF_8 = "UTF-8";
	
	/** POST */
	public static final String POST="post";
	
	/** GET */
	public static final String GET="get";
	
	/** 后台页面 */
	public static final String ADMIN_PATH="/WEB-INF/admin/";
	
	/** 后台错误页面 */
	public static final String ADMIN_ERROR_VIEW = ADMIN_PATH+"common/500.html";
	
	/** 后台权限错误页面 */
	public static final String ADMIN_UNAUTHORIZED_VIEW = ADMIN_PATH+"common/403.html";
	
	/** 前台错误页面 */
	public static final String FRONT_ERROR_VIEW = "/500.html";
	
	/** 前台权限错误页面 */
	public static final String FRONT_RESOURCE_NOT_FOUND_VIEW = "/404.html";
	
	/** config.xml文件路径 */
	public static final String CONFIG_XML_PATH = "/config.xml";
	
	/** config.properties */
	public static final String CONFIG_PROPERTIES = "config.properties";
	
	/** job.properties */
	public static final String JOB_PROPERTIES = "job.properties";
	
	/** 页面后缀 */
	public static final String VIEW_EXTENSION = ".html";
	
	/** 参数分隔符 */
	public static final String URL_PARA_SEPARATOR = "-";
	
	/** 上传文件目录 */
	public static final String BASE_UPLOAD_PATH = "upload";
	
	/** JSON时间格式 */
	public static final String JSON_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 不可实例化
	 */
	private CommonAttribute() {
	}
	
	/**
	 *消息类型
	 */
	
	/** 成功 */
	public static final String FEEDBACK_TYPE_SUCCESS="success";
	
	/** 警告 */
	public static final String FEEDBACK_TYPE_WARN="warn";
	
	/** 错误 */
	public static final String FEEDBACK_TYPE_ERROR="error";
	
	/** 消息类型名称 */
	public static final Map feedbackTypeNames = new ListOrderedMap(){{
		put(FEEDBACK_TYPE_SUCCESS,"成功");
		put(FEEDBACK_TYPE_WARN, "警告");
		put(FEEDBACK_TYPE_ERROR, "错误");
	}};
	
	/**
	 * 广告类型
	 */
	
	/** 文本 */
	public static final String AD_TYPE_TEXT="text";
	
	/** 图片 */
	public static final String AD_TYPE_IMAGE="image";
	
	/** 广告类型名称 */
	public static final Map adTypeNames = new ListOrderedMap(){{
		put(AD_TYPE_TEXT, "文本");
		put(AD_TYPE_IMAGE, "图片");
	}};
	
	/**
	 * 友情链接类型
	 */
	
	/** 文本 */
	public static final String FRIENDLINK_TYPE_TEXT="text";
	
	/** 图片 */
	public static final String FRIENDLINK_TYPE_IMAGE="image";
	
	/** 友情链接类型名称 */
	public static final Map friendLinkTypeNames = new ListOrderedMap(){{
		put(FRIENDLINK_TYPE_TEXT, "文本");
		put(FRIENDLINK_TYPE_IMAGE, "图片");
	}};
	
	/**
	 * 水印位置
	 */
	
	/** 无 */
	public static final String CONFIG_WATERMARK_POSITION_NO="no";
	
	/** 左上 */
	public static final String CONFIG_WATERMARK_POSITION_TOP_LEFT="topLeft";
	
	/** 右上 */
	public static final String CONFIG_WATERMARK_POSITION_TOP_RIGHT="topRight";
	
	/** 居中 */
	public static final String CONFIG_WATERMARK_POSITION_CENTER="center";
	
	/** 左下 */
	public static final String CONFIG_WATERMARK_POSITION_BOTTOM_LEFT="bottomLeft";
	
	/** 右下 */
	public static final String CONFIG_WATERMARK_POSITION_BOTTOM_RIGHT="bottomRight";
	
	/** 水印位置名称 */
	public static final Map configWatermarkPositionNames = new ListOrderedMap(){{
		put(CONFIG_WATERMARK_POSITION_NO, "无");
		put(CONFIG_WATERMARK_POSITION_TOP_LEFT, "左上");
		put(CONFIG_WATERMARK_POSITION_TOP_RIGHT, "右上");
		put(CONFIG_WATERMARK_POSITION_CENTER, "居中");
		put(CONFIG_WATERMARK_POSITION_BOTTOM_LEFT, "左下");
		put(CONFIG_WATERMARK_POSITION_BOTTOM_RIGHT, "右下");
	}};
	
	
	/**
	 * 设置类型
	 */
	
	/** input(单行文本) */
	public static final String CONFIG_TYPE_INPUT="input";
	
	/** textarea(多行文本) */
	public static final String CONFIG_TYPE_TEXTAREA="textarea";
	
	/** editor(编辑器) */
	public static final String CONFIG_TYPE_EDITOR="editor";
	
	/** file(单文件/图片上传) */
	public static final String CONFIG_TYPE_FILE="file";
	
	/** 设置类型名称 */
	public static final Map configTypeNames = new ListOrderedMap(){{
		put(CONFIG_TYPE_INPUT, "input(单行文本)");
		put(CONFIG_TYPE_TEXTAREA, "textarea(多行文本)");
		put(CONFIG_TYPE_EDITOR, "editor(编辑器)");
		put(CONFIG_TYPE_FILE, "file(单文件/图片上传)");
	}};
	
	
    /**
     * 消息配置类型
     */
    
    /** 测试消息 */
    public static final String MESSAGE_CONFIG_TYPE_TEST_MESSAGE="testMessage";
    
    /** 找回密码 */
    public static final String MESSAGE_CONFIG_TYPE_FIND_PASSWORD="findPassword";
    
    /** 会员注册 */
    public static final String MESSAGE_CONFIG_TYPE_REGISTER_MEMBER="registerMember";
    
    /** 消息配置类型名称 */
    public static final Map messageConfigTypeNames = new ListOrderedMap(){{
        put(MESSAGE_CONFIG_TYPE_TEST_MESSAGE, "测试消息");
        put(MESSAGE_CONFIG_TYPE_FIND_PASSWORD, "找回密码");
        put(MESSAGE_CONFIG_TYPE_REGISTER_MEMBER, "会员注册");
    }};
    
    /**
     * 订单状态
     */
    
    /** 等待付款 */
    public static final String ORDER_STATUS_PENDING_PAYMENT="pendingPayment";
    
    /** 等待发货 */
    public static final String ORDER_STATUS_PENDING_SHIPMENT="pendingShipment";
    
    /** 已发货 */
    public static final String ORDER_STATUS_SHIPPED="shipped";
    
    /** 已收货 */
    public static final String ORDER_STATUS_RECEIVED="received";
    
    /** 已完成 */
    public static final String ORDER_STATUS_COMPLETED="completed";
    
    /** 已取消 */
    public static final String ORDER_STATUS_CANCELED="canceled";
    
    /** 订单状态名称 */
    public static final Map orderStatusNames = new ListOrderedMap(){{
        put(ORDER_STATUS_PENDING_PAYMENT, "等待付款");
        put(ORDER_STATUS_PENDING_SHIPMENT, "等待发货");
        put(ORDER_STATUS_SHIPPED, "已发货");
        put(ORDER_STATUS_RECEIVED, "已收货");
        put(ORDER_STATUS_COMPLETED, "已完成");
        put(ORDER_STATUS_CANCELED, "已取消");
    }};
    
    /**
     * 支付方式
     */
    
    /** 支付宝 */
    public static final String PAYMENT_METHOD_ALIPAY="alipay";
    
    /** 微信 */
    public static final String PAYMENT_METHOD_WEIXIN="weixin";
    
    /** 支付方式名称 */
    public static final Map paymentMethodNames = new ListOrderedMap(){{
        put(PAYMENT_METHOD_ALIPAY, "支付宝");
        put(PAYMENT_METHOD_WEIXIN, "微信");
    }};
    
    /**
     * 配送方式
     */
    
    /** 门店自提 */
    public static final String SHIPPING_METHOD_SELF="self";
    
    /** 门店配送 */
    public static final String SHIPPING_METHOD_DELIVER="deliver";
    
    /** 支付方式名称 */
    public static final Map shippingMethodNames = new ListOrderedMap(){{
        put(SHIPPING_METHOD_SELF, "门店自提");
        put(SHIPPING_METHOD_DELIVER, "门店配送");
    }};
}
package com.cms;

import java.io.Serializable;

public class Config implements Serializable {

	private static final long serialVersionUID = 1012463140749529805L;

	/** 缓存名称 */
	public static final String CACHE_NAME = "config";

	/** 网站名称 */
	private String siteName;
	
	/** 网站网址 */
	private String siteUrl;
	
	/** logo */
	private String logo;
	
	/** 标题 */
	private String title;
	
	/** 关键词 */
	private String keywords;
	
	/** 描述 */
	private String description;
	
    /** 商品图片(大)宽度 */
    private Integer largeProductImageWidth;

    /** 商品图片(大)高度 */
    private Integer largeProductImageHeight;

    /** 商品图片(中)宽度 */
    private Integer mediumProductImageWidth;

    /** 商品图片(中)高度 */
    private Integer mediumProductImageHeight;

    /** 商品缩略图宽度 */
    private Integer thumbnailProductImageWidth;

    /** 商品缩略图高度 */
    private Integer thumbnailProductImageHeight;

    /** 默认商品图片(大) */
    private String defaultLargeProductImage;

    /** 默认商品图片(小) */
    private String defaultMediumProductImage;

    /** 默认缩略图 */
    private String defaultThumbnailProductImage;
	
	/** 是否水印开启 */
	private Boolean isWatermarkEnabled;
	
	/** 水印透明度 */
	private Integer watermarkAlpha;
	
	/** 水印图片 */
	private String watermarkImage;
	
	/** 水印位置 */
	private String watermarkPosition;
	
	/** 文件上传路径 */
	private String fileUploadPath;

	/** 主题 */
	private String theme;
	
    /** 短信签名 */
    private String smsSignName;
    
    /** 短信accessKey */
    private String smsAccessKey;

    /** 短信accessSecret */
    private String smsAccessSecret;
    
    /** 短信Endpoint */
    private String smsEndpoint;
    
    /** 短信主题名称 */
    private String smsTopicName;
    
    /** 支付宝appId */
    private String alipayAppId;
    
    /** 支付宝app私钥 */
    private String alipayAppPrivateKey;
    
    /** 支付宝公钥 */
    private String alipayPublicKey;
    
    /** 微信支付appId */
    private String weixinpayAppId;
    
    /** 微信支付商户ID */
    private String weixinpayMchId;
    
    /** 微信支付key */
    private String weixinpayKey;
    
	
	
	public String getSmsEndpoint() {
        return smsEndpoint;
    }

    public void setSmsEndpoint(String smsEndpoint) {
        this.smsEndpoint = smsEndpoint;
    }

    public String getSmsTopicName() {
        return smsTopicName;
    }

    public void setSmsTopicName(String smsTopicName) {
        this.smsTopicName = smsTopicName;
    }

    public String getAlipayAppId() {
        return alipayAppId;
    }

    public void setAlipayAppId(String alipayAppId) {
        this.alipayAppId = alipayAppId;
    }

    public String getAlipayAppPrivateKey() {
        return alipayAppPrivateKey;
    }

    public void setAlipayAppPrivateKey(String alipayAppPrivateKey) {
        this.alipayAppPrivateKey = alipayAppPrivateKey;
    }

    public String getAlipayPublicKey() {
        return alipayPublicKey;
    }

    public void setAlipayPublicKey(String alipayPublicKey) {
        this.alipayPublicKey = alipayPublicKey;
    }

    public String getWeixinpayAppId() {
        return weixinpayAppId;
    }

    public void setWeixinpayAppId(String weixinpayAppId) {
        this.weixinpayAppId = weixinpayAppId;
    }

    public String getWeixinpayMchId() {
        return weixinpayMchId;
    }

    public void setWeixinpayMchId(String weixinpayMchId) {
        this.weixinpayMchId = weixinpayMchId;
    }

    public String getWeixinpayKey() {
        return weixinpayKey;
    }

    public void setWeixinpayKey(String weixinpayKey) {
        this.weixinpayKey = weixinpayKey;
    }

    /**
	 * 获取网站名称
	 * 
	 * @return 网站名称
	 */
	public String getSiteName() {
		return siteName;
	}

	/**
	 * 设置网站名称
	 * 
	 * @param siteName
	 *            网站名称
	 */
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	/**
	 * 获取网站网址
	 * 
	 * @return 网站网址
	 */
	public String getSiteUrl() {
		return siteUrl;
	}

	/**
	 * 设置网站网址
	 * 
	 * @param siteUrl
	 *            网站网址
	 */
	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}

	/**
	 * 获取logo
	 * 
	 * @return logo
	 */
	public String getLogo() {
		return logo;
	}

	/**
	 * 设置logo
	 * 
	 * @param logo
	 *            logo
	 */
	public void setLogo(String logo) {
		this.logo = logo;
	}
	
	/**
	 * 获取标题
	 * 
	 * @return 标题
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 设置标题
	 * 
	 * @param title
	 *            标题
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 获取关键词
	 * 
	 * @return 关键词
	 */
	public String getKeywords() {
		return keywords;
	}

	/**
	 * 设置关键词
	 * 
	 * @param keywords
	 *            关键词
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	/**
	 * 获取描述
	 * 
	 * @return 描述
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 设置描述
	 * 
	 * @param description
	 *            描述
	 */
	public void setDescription(String description) {
		this.description = description;
	}

   /**
     * 获取商品图片(大)宽度
     * 
     * @return 商品图片(大)宽度
     */
    public Integer getLargeProductImageWidth() {
        return largeProductImageWidth;
    }

    /**
     * 设置商品图片(大)宽度
     * 
     * @param largeProductImageWidth
     *            商品图片(大)宽度
     */
    public void setLargeProductImageWidth(Integer largeProductImageWidth) {
        this.largeProductImageWidth = largeProductImageWidth;
    }

    /**
     * 获取商品图片(大)高度
     * 
     * @return 商品图片(大)高度
     */
    public Integer getLargeProductImageHeight() {
        return largeProductImageHeight;
    }

    /**
     * 设置商品图片(大)高度
     * 
     * @param largeProductImageHeight
     *            商品图片(大)高度
     */
    public void setLargeProductImageHeight(Integer largeProductImageHeight) {
        this.largeProductImageHeight = largeProductImageHeight;
    }

    /**
     * 获取商品图片(中)宽度
     * 
     * @return 商品图片(中)宽度
     */
    public Integer getMediumProductImageWidth() {
        return mediumProductImageWidth;
    }

    /**
     * 设置商品图片(中)宽度
     * 
     * @param mediumProductImageWidth
     *            商品图片(中)宽度
     */
    public void setMediumProductImageWidth(Integer mediumProductImageWidth) {
        this.mediumProductImageWidth = mediumProductImageWidth;
    }

    /**
     * 获取商品图片(中)高度
     * 
     * @return 商品图片(中)高度
     */
    public Integer getMediumProductImageHeight() {
        return mediumProductImageHeight;
    }

    /**
     * 设置商品图片(中)高度
     * 
     * @param mediumProductImageHeight
     *            商品图片(中)高度
     */
    public void setMediumProductImageHeight(Integer mediumProductImageHeight) {
        this.mediumProductImageHeight = mediumProductImageHeight;
    }

    /**
     * 获取商品缩略图宽度
     * 
     * @return 商品缩略图宽度
     */
    public Integer getThumbnailProductImageWidth() {
        return thumbnailProductImageWidth;
    }

    /**
     * 设置商品缩略图宽度
     * 
     * @param thumbnailProductImageWidth
     *            商品缩略图宽度
     */
    public void setThumbnailProductImageWidth(Integer thumbnailProductImageWidth) {
        this.thumbnailProductImageWidth = thumbnailProductImageWidth;
    }

    /**
     * 获取商品缩略图高度
     * 
     * @return 商品缩略图高度
     */
    public Integer getThumbnailProductImageHeight() {
        return thumbnailProductImageHeight;
    }

    /**
     * 设置商品缩略图高度
     * 
     * @param thumbnailProductImageHeight
     *            商品缩略图高度
     */
    public void setThumbnailProductImageHeight(Integer thumbnailProductImageHeight) {
        this.thumbnailProductImageHeight = thumbnailProductImageHeight;
    }

    /**
     * 获取默认商品图片(大)
     * 
     * @return 默认商品图片(大)
     */
    public String getDefaultLargeProductImage() {
        return defaultLargeProductImage;
    }

    /**
     * 设置默认商品图片(大)
     * 
     * @param defaultLargeProductImage
     *            默认商品图片(大)
     */
    public void setDefaultLargeProductImage(String defaultLargeProductImage) {
        this.defaultLargeProductImage = defaultLargeProductImage;
    }

    /**
     * 获取默认商品图片(小)
     * 
     * @return 默认商品图片(小)
     */
    public String getDefaultMediumProductImage() {
        return defaultMediumProductImage;
    }

    /**
     * 设置默认商品图片(小)
     * 
     * @param defaultMediumProductImage
     *            默认商品图片(小)
     */
    public void setDefaultMediumProductImage(String defaultMediumProductImage) {
        this.defaultMediumProductImage = defaultMediumProductImage;
    }

    /**
     * 获取默认缩略图
     * 
     * @return 默认缩略图
     */
    public String getDefaultThumbnailProductImage() {
        return defaultThumbnailProductImage;
    }

    /**
     * 设置默认缩略图
     * 
     * @param defaultThumbnailProductImage
     *            默认缩略图
     */
    public void setDefaultThumbnailProductImage(String defaultThumbnailProductImage) {
        this.defaultThumbnailProductImage = defaultThumbnailProductImage;
    }
	
	/**
	 * 获取是否水印开启
	 * 
	 * @return 是否水印开启
	 */
	public Boolean getIsWatermarkEnabled() {
		return isWatermarkEnabled;
	}

	/**
	 * 设置是否水印开启
	 * 
	 * @param isWatermarkEnabled
	 *            是否水印开启
	 */
	public void setIsWatermarkEnabled(Boolean isWatermarkEnabled) {
		this.isWatermarkEnabled = isWatermarkEnabled;
	}

	/**
	 * 获取水印透明度
	 * 
	 * @return 水印透明度
	 */
	public Integer getWatermarkAlpha() {
		return watermarkAlpha;
	}
	
	/**
	 * 设置水印透明度
	 * 
	 * @param watermarkAlpha
	 *            水印透明度
	 */
	public void setWatermarkAlpha(Integer watermarkAlpha) {
		this.watermarkAlpha = watermarkAlpha;
	}

	/**
	 * 获取水印图片
	 * 
	 * @return 水印图片
	 */
	public String getWatermarkImage() {
		return watermarkImage;
	}

	/**
	 * 设置水印图片
	 * 
	 * @param watermarkImage
	 *            水印图片
	 */
	public void setWatermarkImage(String watermarkImage) {
		this.watermarkImage = watermarkImage;
	}

	/**
	 * 获取水印位置
	 * 
	 * @return 水印位置
	 */
	public String getWatermarkPosition() {
		return watermarkPosition;
	}

	/**
	 * 设置水印位置
	 * 
	 * @param watermarkPosition
	 *            水印位置
	 */
	public void setWatermarkPosition(String watermarkPosition) {
		this.watermarkPosition = watermarkPosition;
	}

	/**
	 * 获取文件上传路径
	 * 
	 * @return 文件上传路径
	 */
	public String getFileUploadPath() {
		return fileUploadPath;
	}

	/**
	 * 设置文件上传路径
	 * 
	 * @param fileUploadPath
	 *            文件上传路径
	 */
	public void setFileUploadPath(String fileUploadPath) {
		if (fileUploadPath != null) {
			if (!fileUploadPath.startsWith("/")) {
				fileUploadPath = "/" + fileUploadPath;
			}
			if (!fileUploadPath.endsWith("/")) {
				fileUploadPath += "/";
			}
		}
		this.fileUploadPath = fileUploadPath;
	}
	
	/**
	 * 获取主题
	 * 
	 * @return 主题
	 */
	public String getTheme() {
		return theme;
	}

	/**
	 * 设置主题
	 * 
	 * @param theme
	 *            主题
	 */
	public void setTheme(String theme) {
		this.theme = theme;
	}

   /**
     * 获取短信accessKey
     * 
     * @return 短信accessKey
     */
    public String getSmsAccessKey() {
        return smsAccessKey;
    }
    
    /**
     * 设置短信accessKey
     * 
     * @param smsAccessKey
     *            短信accessKey
     */
    public void setSmsAccessKey(String smsAccessKey) {
        this.smsAccessKey = smsAccessKey;
    }

    /**
     * 获取短信accessSecret
     * 
     * @return 短信accessSecret
     */
    public String getSmsAccessSecret() {
        return smsAccessSecret;
    }
    
    /**
     * 设置短信accessSecret
     * 
     * @param smsAccessSecret
     *            短信accessSecret
     */
    public void setSmsAccessSecret(String smsAccessSecret) {
        this.smsAccessSecret = smsAccessSecret;
    }
    
    /**
     * 获取短信签名
     * 
     * @return 短信签名
     */
    public String getSmsSignName() {
        return smsSignName;
    }

    /**
     * 设置短信签名
     * 
     * @param smsSignName
     *            短信签名
     */
    public void setSmsSignName(String smsSignName) {
        this.smsSignName = smsSignName;
    }
}

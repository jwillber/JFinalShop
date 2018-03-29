package com.cms.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public class DeviceUtils {

    // \b 是单词边界(连着的两个(字母字符 与 非字母字符) 之间的逻辑上的间隔),      
    // 字符串在编译时会被转码一次,所以是 "\\b"      
    // \B 是单词内部逻辑间隔(连着的两个字母字符之间的逻辑上的间隔)      
	public static final String PHONE_REG = "\\b(ip(hone|od)|android|opera m(ob|in)i" + "|windows (phone|ce)|blackberry" + "|s(ymbian|eries60|amsung)|p(laybook|alm|rofile/midp"
			+ "|laystation portable)|nokia|fennec|htc[-_]" + "|mobile|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";
	public static final String TABLE_REG = "\\b(ipad|tablet|(Nexus 7)|up.browser" + "|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";   
        
    //移动设备正则匹配：手机端、平板    
	public static final Pattern PHONE_PAT = Pattern.compile(PHONE_REG, Pattern.CASE_INSENSITIVE);      
	public static final Pattern TABLE_PAT = Pattern.compile(TABLE_REG, Pattern.CASE_INSENSITIVE);      
          
    /**  
     * 检测是否是移动设备访问  
     *   
     * @Title: check  
     * @Date : 2014-7-7 下午01:29:07  
     * @param userAgent 浏览器标识  
     * @return true:移动设备接入，false:pc端接入  
     */    
    public static boolean isMobile(HttpServletRequest request){      
    	 String userAgent = request.getHeader("User-Agent").toLowerCase();  
        if(null == userAgent){      
            userAgent = "";      
        }      
        // 匹配      
        Matcher matcherPhone = PHONE_PAT.matcher(userAgent);      
        Matcher matcherTable = TABLE_PAT.matcher(userAgent);      
        if(matcherPhone.find() || matcherTable.find()){      
            return true;      
        } else {      
            return false;      
        }      
    }  
    
    /**
     * 获取设备名称
     * @param userAgent
     * @return
     */
     public static String getName(HttpServletRequest request) {
    	 String userAgent = request.getHeader("User-Agent").toLowerCase();  
    	if (null == userAgent) {
    		userAgent = "";
    	}
    	// 匹配
    	Matcher matcherPhone = PHONE_PAT.matcher(userAgent);
    	Matcher matcherTable = TABLE_PAT.matcher(userAgent);
    	if (matcherPhone.find()) {
    		return matcherPhone.group();
    	} else if (matcherTable.find()) {
    		return matcherTable.group();
    	} else {
    		return "pc";
    	}
    }
	
}

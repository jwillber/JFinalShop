package com.cms.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import com.cms.entity.Admin;
import com.cms.entity.Member;

public class PermissionFilter implements Filter{
    
    /** 不包含 */
    private List<String> adminExcludes = new ArrayList<String>(){{
        add("/admin/login");
        add("/admin/error");
    }};
    
    /** 不包含 */
    private List<String> permissionExcludes = new ArrayList<String>(){{
        add("/admin/logout");
        add("/admin/common");
        add("/admin/file");
    }};
    
    /** 不包含 */
    private List<String> memberExcludes = new ArrayList<String>(){{
        add("/register");
        add("/login");
        add("/password");
        add("/payment/alipayNotify");
        add("/payment/weixinNotify");
    }};
    
    /** 包含 */
    private List<String> memberIncludes = new ArrayList<String>(){{
        add("/member");
        add("/member/");
        add("/order/");
        add("/receiver/");
        add("/payment/");
    }};

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        // TODO Auto-generated method stub
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        String url = request.getRequestURI().toString();
        String contextPath = request.getContextPath();
        url = url.substring(contextPath.length());
        if(StringUtils.isBlank(url) 
                || url.equals("/") 
                || url.startsWith("/upload") 
                || url.startsWith("/static") 
                || url.startsWith("/common")
                || StringUtils.isNotBlank(FilenameUtils.getExtension(url))
            ){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        //匹配member
        for(String key : memberExcludes){
            if(url.startsWith(key)){
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }
        for(String key : memberIncludes){
            if(url.startsWith(key)){
                Member currentMember = (Member) request.getSession().getAttribute(Member.SESSION_MEMBER);
            	if(currentMember!=null){
        	        filterChain.doFilter(servletRequest, servletResponse);
                    return;
            	}
        		response.sendRedirect(contextPath+"/login");
        		return;
               
            }
        }
        if(!url.startsWith("/admin")){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        //匹配admin
        for(String key : adminExcludes){
            if(url.startsWith(key)){
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }
        Admin currentAdmin = (Admin) request.getSession().getAttribute(Admin.SESSION_ADMIN);
        if(currentAdmin!=null){
            for(String key : permissionExcludes){
                if(url.startsWith(key)){
                    filterChain.doFilter(servletRequest, servletResponse);
                    return;
                }
            }
            List<String> permissions = currentAdmin.getPermissions();
            for(String key : permissions){
                if(url.startsWith(key)){
                    filterChain.doFilter(servletRequest, servletResponse);
                    return;
                }
            }
            response.sendRedirect(contextPath+"/admin/error/unauthorized");
            return;
        }
        response.sendRedirect(contextPath+"/admin/login");
        return;
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
        
    }

}

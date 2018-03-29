package com.cms.util;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.cms.Config;
import com.jfinal.kit.HttpKit;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

public class WeixinUtils {
    
    /** 缓存名称 */
    public static final String CACHE_NAME = "weixin";
    
    /** CacheManager */
    private static final CacheManager CACHE_MANAGER = CacheUtils.getCacheManager();

	
    //=====基本 token  和  ticket
    
	private static final String GET_TOKEN_URL="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
	
	public static String getAccessToken(String appId,String appSecret){
	    Ehcache cache = CACHE_MANAGER.getEhcache(CACHE_NAME);
        String cacheKey = "accessToken";
        Element cacheElement = cache.get(cacheKey);
        if (cacheElement == null) {
            //======获取开始
            String url = String.format(GET_TOKEN_URL, appId, appSecret);
            String content= HttpKit.get(url, null);
            JSONObject jsonObject = JSONObject.parseObject(content);
            System.out.println("======"+jsonObject.toJSONString());
            String token = "";
            if (null != jsonObject) {
                try {
                    token = jsonObject.getString("access_token");
                } catch (JSONException e) {
                    System.out.println("获取token失败 errcode:" + jsonObject.getIntValue("errcode") + " errmsg:" + jsonObject.getString("errmsg"));
                }
            }
          //======获取结束
            cache.put(new Element(cacheKey, token));
            cacheElement = cache.get(cacheKey);
        }
        return (String)cacheElement.getObjectValue();
	    
	}
	
	private static final String GET_JSAPI_TICKET_URL="https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";
	
	public static String getJsapiTicket(String accessToken){
	    Ehcache cache = CACHE_MANAGER.getEhcache(CACHE_NAME);
        String cacheKey = "jsapiTicket";
        Element cacheElement = cache.get(cacheKey);
        if (cacheElement == null) {
            //======获取开始
            String url = String.format(GET_JSAPI_TICKET_URL,accessToken);
            System.out.println(url);
            String content= HttpKit.get(url, null);
            JSONObject jsonObject = JSONObject.parseObject(content);
            String ticket = jsonObject.getString("ticket");
            if(StringUtils.isBlank(ticket)){
                return null;
            }
            //======获取结束
            cache.put(new Element(cacheKey, ticket));
            cacheElement = cache.get(cacheKey);
        }
        return (String)cacheElement.getObjectValue();
	}
	
	//获取用户信息
	private static final String GET_USER_INFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN";
	
	//{country=中国, subscribe=1, city=长沙, openid=obknuvpx-L2QwySLwCDBs6VwUuc0, tagid_list=[], sex=1, groupid=0, language=zh_CN, remark=, subscribe_time=1515471683, province=湖南, nickname=樱木, headimgurl=http://wx.qlogo.cn/mmopen/Q3auHgzwzM5N6OYHyYyICmNCHuvT1rBvB81IZ1My8ricRVg3GeHdCCmvXQ1QuGYeXSTEQSjpzXicylicOZibBKIgXw/0}
    public static Map<String,Object> getUserInfo(String accessToken,String openId){
        String content = HttpKit.get(String.format(GET_USER_INFO_URL, accessToken,openId), null);
        System.out.println("GET_USER_INFO_URL=="+content);
        return JSONObject.parseObject(content, Map.class);
	}
	
	//获取用户auth信息
	
	private static final String GET_AUTHORIZE_URL="https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=%s#wechat_redirect";
	
	public static String getAuthorizeUrl(String appId,String redirectUri,String state){
	    String url = String.format(GET_AUTHORIZE_URL, appId,redirectUri,state);
	    System.out.println("url=="+url);
	    return url;
	}
	
	private static final String GET_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
	//access_token   openid
	public static Map<String,Object> getOauth2Token(String appId,String appSecret,String code){
	    String content = HttpKit.get(String.format(GET_ACCESS_TOKEN_URL, appId,appSecret,code), null);
	    System.out.println("GET_ACCESS_TOKEN_URL=="+content);
	    return JSONObject.parseObject(content, Map.class);
	}
	
	
	private static final String GET_OAUTH2_USERINFO_URL= "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";
	
	//nickname headimgurl
	public static Map<String,Object> getOauth2Userinfo(String accessToken,String openId){
	    String content = HttpKit.get(String.format(GET_OAUTH2_USERINFO_URL, accessToken,openId), null);
	    System.out.println("GET_OAUTH2_USERINFO_URL=="+content);
	    return JSONObject.parseObject(content, Map.class);
	}
	
	//客服消息
	private static final String CUSTOM_SEND_URL="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=%s";
	public static void customSend(String accessToken,String message){
	    String content = HttpKit.post(String.format(CUSTOM_SEND_URL, accessToken), message);
	    System.out.println("==CUSTOM_SEND_URL=="+content);
	}
	
    //获取文本消息内容
    public static String getTextMessage(String touser,String content){
        String message="";
        message+="{";
        message+="\"touser\":\""+touser+"\",";   //OPENID
        message+="\"msgtype\":\"text\",";
        message+="\"text\":";
        message+="{";
        message+="\"content\":\""+content+"\"";
        message+="}";
        message+="}";
        return message;
    }
    //获取图片消息内容
    public static String getImageMessage(String touser,String mediaId){
        String message="";
        message+="{";
        message+="\"touser\":\""+touser+"\",";   //OPENID
        message+="\"msgtype\":\"image\",";
        message+="\"image\":";
        message+="{";
        message+="\"media_id\":\""+mediaId+"\"";
        message+="}";
        message+="}";    
        return message;
    }
    //获取语音消息内容
    public static String getVoiceMessage(String touser,String mediaId){
        String message="";
        message+="{";
        message+="\"touser\":\""+touser+"\",";   //OPENID
        message+="\"msgtype\":\"voice\",";
        message+="\"voice\":";
        message+="{";
        message+="\"media_id\":\""+mediaId+"\"";
        message+="}";
        message+="}";   
        return message;
    }
    //获取视频消息内容
    public static String getVideoMessage(String touser,String mediaId,String thumbMediaId,String title,String description){
        String message="";
        message+="{";
        message+="\"touser\":\""+touser+"\",";   //OPENID
        message+="\"msgtype\":\"video\",";
        message+="\"video\":";
        message+="{";
        message+="\"media_id\":\""+mediaId+"\"";
        message+="\"thumb_media_id\":\""+thumbMediaId+"\"";
        message+="\"title\":\""+title+"\"";
        message+="\"description\":\""+description+"\"";
        message+="}";
        message+="}";   
        return message;
    }
    //获取音乐消息内容
    public static String getMusicMessage(String touser,String title,String description,String musicurl,String hqmusicurl,String thumbMediaId){
        String message="";
        message+="{";
        message+="\"touser\":\""+touser+"\",";   //OPENID
        message+="\"msgtype\":\"music\",";
        message+="\"music\":";
        message+="{";
        message+="\"title\":\""+title+"\"";
        message+="\"description\":\""+description+"\"";
        message+="\"musicurl\":\""+musicurl+"\"";
        message+="\"hqmusicurl\":\""+hqmusicurl+"\"";
        message+="\"thumb_media_id\":\""+thumbMediaId+"\"";
        message+="}";
        message+="}";
        return message;
    }
    
    //获取图文消息（点击跳转到外链）图文消息条数限制在8条以内，注意，如果图文数超过8，则将会无响应。
    public static String getNewsMessage(String touser,List<Map<String,String>> data){
        String message="";
        message+="{";
        message+="\"touser\":\""+touser+"\",";
        message+="\"msgtype\":\"news\",";
        message+="\"news\":{";
        message+="\"articles\": [";
        for(int i=0;i<data.size();i++){
            Map<String,String> map = new HashMap<String,String>();
            message+="{";
            message+="\"title\":\""+map.get("map")+"\",";
            message+="\"description\":\""+map.get("description")+"\",";
            message+="\"url\":\""+map.get("url")+"\",";
            message+="\"picurl\":\""+map.get("picurl")+"\"";
            message+="}";
        }    
        message+="]";
        message+="}";
        message+="}";
        return message;
    }
    //获取图文消息（点击跳转到图文消息页面） 图文消息条数限制在8条以内，注意，如果图文数超过8，则将会无响应。
    public static String getMpnewsMessage(String touser,String mediaId){
        String message="";
        message+="{";
        message+="\"touser\":\""+touser+"\",";   //OPENID
        message+="\"msgtype\":\"mpnews\",";
        message+="\"mpnews\":";
        message+="{";
        message+="\"media_id\":\""+mediaId+"\"";
        message+="}";
        message+="}";  
        return message;
    }
	
    /** 临时二维码 url */
    public static final String GET_QRCODE_CREATE_URL="https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=%s";
    /** 临时二维码下载url */
    public static final String GET_SHOWQRCODE_URL="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=%s";
	
    //ticket
    public static String getQrcodeCreate(String accessToken,String qrcodeContent){
        String content = HttpKit.post(String.format(GET_QRCODE_CREATE_URL, accessToken), qrcodeContent);
        System.out.println("GET_QRCODE_CREATE_URL=="+content);
        JSONObject jsonObject = JSONObject.parseObject(content);
        return jsonObject.getString("ticket");
    }
    
    //url
    public static String getShowqrcodeUrl(String ticket){
        return String.format(GET_SHOWQRCODE_URL, ticket);
    }
    
    //获取Qrcode内容
    public static String getQrcodeContent(Long sceneId){
        return "{\"expire_seconds\": 2592000, \"action_name\": \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": "+sceneId+"}}}";
    }
    
    //素材
    /** 新增临时素材 url */
    public static final String UPLOAD_URL="https://api.weixin.qq.com/cgi-bin/media/upload?access_token=%s&type=%s";
    
    //type  media_id   created_at
    public static String uploadImage(String accessToken,File file){
        String content = WebUtils.post(String.format(UPLOAD_URL, accessToken,"image"),file);
        JSONObject jsonObject = JSONObject.parseObject(content);
        return jsonObject.getString("media_id");
    }
    
    
    //菜单
    public static final String MENU_CREATE_URL="https://api.weixin.qq.com/cgi-bin/menu/create?access_token=%s";
    
    public static void menuCreate(String accessToken,String data){
        String content = HttpKit.post(String.format(MENU_CREATE_URL, accessToken), data);
        System.out.println(content);
    }
    
    //公众号支付
    /**
     * 
     * @param amount    订单金额
     * @param subject   商品信息
     * @param openId    用户openId
     * @param httpRequest
     * @param httpResponse
     * @return
     */
    public static Map<String,String> wechatPay(BigDecimal amount,String subject,String openId,HttpServletRequest httpRequest,HttpServletResponse httpResponse){
      Config config = SystemUtils.getConfig();
      Map<String,String> params = wechatUnifiedorder(config.getWeixinpayAppId(), config.getWeixinpayMchId(), openId, subject, amount.multiply(new BigDecimal(100)).intValue(), IPUtils.getIpAddress(httpRequest));
      String prepay_id = params.get("prepay_id");
      Map<String,String> result = new HashMap<String,String>();
      result.put("prepay_id", prepay_id);
      result.put("appId", config.getWeixinpayAppId());
      result.put("timeStamp", getCurrentTimestamp()+"");
      result.put("nonceStr", generateNonceStr());
      result.put("signType", SignType.MD5.toString());
      String sign="";
         try {
             sign = generateSignature(result, config.getWeixinpayKey(),SignType.MD5);
         } catch (Exception e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }
         result.put("paySign",sign);
         return result;
         
    }
    
    
    //h5支付
    public static Map<String,String> h5Pay(BigDecimal amount,String subject,HttpServletRequest httpRequest,HttpServletResponse httpResponse){
        Config config = SystemUtils.getConfig();
      Map<String,String> params = h5Unifiedorder(config.getWeixinpayAppId(), config.getWeixinpayMchId(), subject, amount.multiply(new BigDecimal(100)).intValue(), IPUtils.getIpAddress(httpRequest));
      String mweb_url = params.get("mweb_url");
      Map<String,String> result = new HashMap<String,String>();
      result.put("mweb_url", mweb_url);
      return result;
  }
    
    //企业付款
    /**
     * 
     * @param mch_appid  微信分配的账号ID（企业号corpid即为此appId）
     * @param mchid   微信支付分配的商户号
     * @param openid  商户appid下，某用户的openid 
     * @param amount  企业付款金额，单位为分
     * @param desc    企业付款操作说明信息。必填。
     * @return
     * @throws Exception
     */
    public static Map<String,String> transfers(String mch_appid,String mchid,String openid,int amount,String desc) throws Exception{
        KeyStore keyStore  = KeyStore.getInstance("PKCS12");
        FileInputStream instream = new FileInputStream(new File("D:/10016225.p12"));  //证书
        try {
            keyStore.load(instream, "10016225".toCharArray());  //证书密码
        } finally {
            instream.close();
        }

        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, "10016225".toCharArray()) //证书密码
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        try {
            Map<String,String> params = new HashMap<String,String>();
            params.put("mch_appid", mch_appid);
            params.put("mchid", mchid);
            params.put("nonce_str", generateNonceStr());
            params.put("partner_trade_no", generateUUID());
            params.put("openid", openid);
            params.put("check_name", "NO_CHECK");
            params.put("amount", amount+"");
            params.put("desc", desc);
            params.put("spbill_create_ip", IPUtils.getLocalAddress());
            
            String xml = generateSignedXml(params, "123456");  //API密钥
            
            HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers");
            StringEntity entity = new StringEntity(xml,"UTF-8");
            httpPost.setEntity(entity);  
            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                HttpEntity httpEntity = response.getEntity();
                String result = EntityUtils.toString(httpEntity);
                EntityUtils.consume(httpEntity);
                EntityUtils.consume(entity);
                return xmlToMap(result);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }
    
    //发放普通红包
    /**
     * 
     * @param mch_id   微信支付分配的商户号
     * @param wxappid  微信分配的公众账号ID
     * @param send_name  红包发送者名称
     * @param re_openid  接受红包的用户
     * @param total_amount  付款金额，单位分
     * @param total_num  红包发放总人数
     * @param wishing  红包祝福语
     * @param act_name  活动名称
     * @param remark  备注信息
     * @return
     * @throws Exception
     */
    public static Map<String,String> sendredpack(String mch_id,String wxappid,String send_name,String re_openid,int total_amount,int total_num,String wishing,String act_name,String remark) throws Exception{
        KeyStore keyStore  = KeyStore.getInstance("PKCS12");
        FileInputStream instream = new FileInputStream(new File("D:/10016225.p12"));  //证书
        try {
            keyStore.load(instream, "10016225".toCharArray());  //证书密码
        } finally {
            instream.close();
        }

        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, "10016225".toCharArray()) //证书密码
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        try {
            Map<String,String> params = new HashMap<String,String>();
            params.put("mch_id", mch_id);
            params.put("wxappid", wxappid);
            params.put("nonce_str", generateNonceStr());
            params.put("mch_billno", generateUUID());
            params.put("send_name", send_name);
            params.put("re_openid", re_openid);
            params.put("total_amount", total_amount+"");
            params.put("total_num", total_num+"");
            params.put("wishing", wishing);
            params.put("client_ip", IPUtils.getLocalAddress());
            params.put("act_name",act_name);
            params.put("remark",remark);
            
            String xml = generateSignedXml(params, "123456");  //API密钥
            
            HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack");
            StringEntity entity = new StringEntity(xml,"UTF-8");
            httpPost.setEntity(entity);  
            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                HttpEntity httpEntity = response.getEntity();
                String result = EntityUtils.toString(httpEntity);
                EntityUtils.consume(httpEntity);
                EntityUtils.consume(entity);
                return xmlToMap(result);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }
    
    
    //发放裂变红包
    /**
     * 
     * @param mch_id   微信支付分配的商户号
     * @param wxappid   微信分配的公众账号ID
     * @param send_name  红包发送者名称
     * @param re_openid  接收红包的种子用户（首个用户）
     * @param total_amount  红包发放总金额
     * @param total_num  红包发放总人数
     * @param wishing   红包祝福语
     * @param act_name  活动名称
     * @param remark  备注信息
     * @return
     */
    public static Map<String,String> sendgroupredpack(String mch_id,String wxappid,String send_name,String re_openid,int total_amount,int total_num,String wishing,String act_name,String remark) throws Exception{
        KeyStore keyStore  = KeyStore.getInstance("PKCS12");
        FileInputStream instream = new FileInputStream(new File("D:/10016225.p12"));  //证书
        try {
            keyStore.load(instream, "10016225".toCharArray());  //证书密码
        } finally {
            instream.close();
        }

        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, "10016225".toCharArray()) //证书密码
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        try {
            Map<String,String> params = new HashMap<String,String>();
            params.put("mch_id", mch_id);
            params.put("wxappid", wxappid);
            params.put("nonce_str", generateNonceStr());
            params.put("mch_billno", generateUUID());
            params.put("send_name", send_name);
            params.put("re_openid", re_openid);
            params.put("total_amount", total_amount+"");
            params.put("total_num", total_num+"");
            params.put("amt_type", "ALL_RAND");
            params.put("wishing", wishing);
            params.put("client_ip", IPUtils.getLocalAddress());
            params.put("act_name",act_name);
            params.put("remark",remark);
            
            String xml = generateSignedXml(params, "123456");  //API密钥
            
            HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/mmpaymkttransfers/sendgroupredpack");
            StringEntity entity = new StringEntity(xml,"UTF-8");
            httpPost.setEntity(entity);  
            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                HttpEntity httpEntity = response.getEntity();
                String result = EntityUtils.toString(httpEntity);
                EntityUtils.consume(httpEntity);
                EntityUtils.consume(entity);
                return xmlToMap(result);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }
    
    //公众号支付统一下单
    /**
     * 
     * @param appid 微信支付分配的公众账号ID（企业号corpid即为此appId）
     * @param mch_id  微信支付分配的商户号
     * @param openid  某用户的openid 
     * @param body   商品简单描述
     * @param total_fee  订单总金额，单位为分
     * @param spbill_create_ip APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
     * @return
     */
    public static Map<String,String> wechatUnifiedorder(String appid,String mch_id,String openid,String body,int total_fee,String spbill_create_ip){
        Map<String,String> params = new HashMap<String,String>();
        params.put("appid", appid);
        params.put("mch_id", mch_id);
        params.put("nonce_str", generateNonceStr());
        params.put("sign_type", SignType.MD5.toString());
        params.put("body", body);
        params.put("out_trade_no", generateUUID());
        params.put("total_fee", total_fee+"");
        params.put("spbill_create_ip", spbill_create_ip);
        params.put("notify_url", "https://test.saiwuquan.com/weixinnotify");
        params.put("trade_type", "JSAPI");
        params.put("openid", openid);
        
        try{
            String xml = generateSignedXml(params, "123456");  //API密钥
            
            String content = HttpKit.post("https://api.mch.weixin.qq.com/pay/unifiedorder", xml);
            return xmlToMap(content); 
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }
    }
    
    
    //h5支付统一下单
    public static Map<String,String> h5Unifiedorder(String appid,String mch_id,String body,int total_fee,String spbill_create_ip){
        Map<String,String> params = new HashMap<String,String>();
        params.put("appid", appid);
        params.put("mch_id", mch_id);
        params.put("nonce_str", generateNonceStr());
        params.put("sign_type", SignType.MD5.toString());
        params.put("body", body);
        params.put("out_trade_no", generateUUID());
        params.put("total_fee", total_fee+"");
        params.put("spbill_create_ip", spbill_create_ip);
        params.put("notify_url", "https://test.saiwuquan.com/weixinnotify");
        params.put("trade_type", "MWEB");
        params.put("scene_info", "{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"https://test.saiwuquan.com\",\"wap_name\": \"充值\"}}");
        
        try{
            String xml = generateSignedXml(params, "123456");  //API密钥
            
            String content = HttpKit.post("https://api.mch.weixin.qq.com/pay/unifiedorder", xml);
            return xmlToMap(content); 
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }
    }
    
    
    
    public static final String FIELD_SIGN = "sign";
    
    public enum SignType {
        MD5, HMACSHA256
    }
    
    /**
     * XML格式字符串转换为Map
     *
     * @param strXML XML字符串
     * @return XML数据转换后的Map
     * @throws Exception
     */
    public static Map<String, String> xmlToMap(String strXML) throws Exception {
        try {
            Map<String, String> data = new HashMap<String, String>();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
            org.w3c.dom.Document doc = documentBuilder.parse(stream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int idx = 0; idx < nodeList.getLength(); ++idx) {
                Node node = nodeList.item(idx);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                    data.put(element.getNodeName(), element.getTextContent());
                }
            }
            try {
                stream.close();
            } catch (Exception ex) {
                // do nothing
            }
            return data;
        } catch (Exception ex) {
            throw ex;
        }

    }
    
    /**
     * 将Map转换为XML格式的字符串
     *
     * @param data Map类型数据
     * @return XML格式的字符串
     * @throws Exception
     */
    public static String mapToXml(Map<String, String> data) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder= documentBuilderFactory.newDocumentBuilder();
        org.w3c.dom.Document document = documentBuilder.newDocument();
        org.w3c.dom.Element root = document.createElement("xml");
        document.appendChild(root);
        for (String key: data.keySet()) {
            String value = data.get(key);
            if (value == null) {
                value = "";
            }
            value = value.trim();
            org.w3c.dom.Element filed = document.createElement(key);
            filed.appendChild(document.createTextNode(value));
            root.appendChild(filed);
        }
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource source = new DOMSource(document);
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        String output = writer.getBuffer().toString(); //.replaceAll("\n|\r", "");
        try {
            writer.close();
        }
        catch (Exception ex) {
        }
        return output;
    }
    
    /**
     * 生成带有 sign 的 XML 格式字符串
     *
     * @param data Map类型数据
     * @param key API密钥
     * @return 含有sign字段的XML
     */
    public static String generateSignedXml(final Map<String, String> data, String key) throws Exception {
        return generateSignedXml(data, key, SignType.MD5);
    }

    /**
     * 生成带有 sign 的 XML 格式字符串
     *
     * @param data Map类型数据
     * @param key API密钥
     * @param signType 签名类型
     * @return 含有sign字段的XML
     */
    public static String generateSignedXml(final Map<String, String> data, String key, SignType signType) throws Exception {
        String sign = generateSignature(data, key, signType);
        data.put(FIELD_SIGN, sign);
        return mapToXml(data);
    }


    /**
     * 判断签名是否正确
     *
     * @param xmlStr XML格式数据
     * @param key API密钥
     * @return 签名是否正确
     * @throws Exception
     */
    public static boolean isSignatureValid(String xmlStr, String key) throws Exception {
        Map<String, String> data = xmlToMap(xmlStr);
        if (!data.containsKey(FIELD_SIGN) ) {
            return false;
        }
        String sign = data.get(FIELD_SIGN);
        return generateSignature(data, key).equals(sign);
    }

    /**
     * 判断签名是否正确，必须包含sign字段，否则返回false。使用MD5签名。
     *
     * @param data Map类型数据
     * @param key API密钥
     * @return 签名是否正确
     * @throws Exception
     */
    public static boolean isSignatureValid(Map<String, String> data, String key) throws Exception {
        return isSignatureValid(data, key, SignType.MD5);
    }

    /**
     * 判断签名是否正确，必须包含sign字段，否则返回false。
     *
     * @param data Map类型数据
     * @param key API密钥
     * @param signType 签名方式
     * @return 签名是否正确
     * @throws Exception
     */
    public static boolean isSignatureValid(Map<String, String> data, String key, SignType signType) throws Exception {
        if (!data.containsKey(FIELD_SIGN) ) {
            return false;
        }
        String sign = data.get(FIELD_SIGN);
        return generateSignature(data, key, signType).equals(sign);
    }
    
    /**
     * 生成签名
     *
     * @param data 待签名数据
     * @param key API密钥
     * @return 签名
     */
    public static String generateSignature(final Map<String, String> data, String key) throws Exception {
        return generateSignature(data, key, SignType.MD5);
    }

    /**
     * 生成签名. 注意，若含有sign_type字段，必须和signType参数保持一致。
     *
     * @param data 待签名数据
     * @param key API密钥
     * @param signType 签名方式
     * @return 签名
     */
    public static String generateSignature(final Map<String, String> data, String key, SignType signType) throws Exception {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (k.equals(FIELD_SIGN)) {
                continue;
            }
            if (data.get(k).trim().length() > 0) // 参数值为空，则不参与签名
                sb.append(k).append("=").append(data.get(k).trim()).append("&");
        }
        sb.append("key=").append(key);
        if (SignType.MD5.equals(signType)) {
            return MD5(sb.toString()).toUpperCase();
        }
        else if (SignType.HMACSHA256.equals(signType)) {
            return HMACSHA256(sb.toString(), key);
        }
        else {
            throw new Exception(String.format("Invalid sign_type: %s", signType));
        }
    }
    
    /**
     * 获取随机字符串 Nonce Str
     *
     * @return String 随机字符串
     */
    public static String generateNonceStr() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }
    
    /**
     * 生成 MD5
     *
     * @param data 待处理数据
     * @return MD5结果
     */
    public static String MD5(String data) throws Exception {
        java.security.MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] array = md.digest(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 生成 HMACSHA256
     * @param data 待处理数据
     * @param key 密钥
     * @return 加密结果
     * @throws Exception
     */
    public static String HMACSHA256(String data, String key) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }
    
    /**
     * 获取当前时间戳，单位秒
     * @return
     */
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis()/1000;
    }

    /**
     * 获取当前时间戳，单位毫秒
     * @return
     */
    public static long getCurrentTimestampMs() {
        return System.currentTimeMillis();
    }

    /**
     * 生成 uuid， 即用来标识一笔单，也用做 nonce_str
     * @return
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }
 
}

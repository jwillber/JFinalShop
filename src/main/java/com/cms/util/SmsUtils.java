package com.cms.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudTopic;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.common.ServiceException;
import com.aliyun.mns.model.BatchSmsAttributes;
import com.aliyun.mns.model.MessageAttributes;
import com.aliyun.mns.model.RawTopicMessage;
import com.aliyun.mns.model.TopicMessage;
import com.cms.CommonAttribute;
import com.cms.Config;
import com.cms.entity.MessageConfig;
/**
 * Utils - 短信
 * 
 * 
 * 
 */
public class SmsUtils {

	/**
	 * 添加短信发送任务
	 * 
	 * @param templateCode
	 *            模板编码
	 * @param params
	 *            参数
	 * @param toMobiles
	 *            接收人手机号码
	 */
	private static void addSendTask(final String templateCode,final Map<String,Object> params,final String[] toMobiles) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				send(templateCode,params,toMobiles);
			}
		});
		thread.start();
	}

	/**
	 * 发送短信
	 * 
	 * @param templateCode
	 *            模板编码
	 * @param params
	 *            参数
	 * @param toMobiles
	 *            接收人手机号码
	 */
	private static void send(String templateCode,Map<String,Object> params,String[] toMobiles) {
		Config config = SystemUtils.getConfig();
		String smsAccessKey = config.getSmsAccessKey();
		String smsAccessSecret = config.getSmsAccessSecret();
		String smsSignName = config.getSmsSignName();
		String smsEndpoint = config.getSmsEndpoint();
		String smsTopicName = config.getSmsTopicName();
		if (StringUtils.isEmpty(smsAccessKey) || StringUtils.isEmpty(smsAccessSecret) || StringUtils.isEmpty(smsSignName)) {
			return;
		}
        try {
            /**
             * Step 1. 获取主题引用
             */
            CloudAccount account = new CloudAccount(smsAccessKey, smsAccessSecret, smsEndpoint);
            MNSClient client = account.getMNSClient();
            CloudTopic topic = client.getTopicRef(smsTopicName);
            /**
             * Step 2. 设置SMS消息体（必须）
             *
             * 注：目前暂时不支持消息内容为空，需要指定消息内容，不为空即可。
             */
            RawTopicMessage msg = new RawTopicMessage();
            msg.setMessageBody("sms-message");
            /**
             * Step 3. 生成SMS消息属性
             */
            MessageAttributes messageAttributes = new MessageAttributes();
            BatchSmsAttributes batchSmsAttributes = new BatchSmsAttributes();
            // 3.1 设置发送短信的签名（SMSSignName）
            batchSmsAttributes.setFreeSignName(smsSignName);
            // 3.2 设置发送短信使用的模板（SMSTempateCode）
            batchSmsAttributes.setTemplateCode(templateCode);
            // 3.3 设置发送短信所使用的模板中参数对应的值（在短信模板中定义的，没有可以不用设置）
            BatchSmsAttributes.SmsReceiverParams smsReceiverParams = new BatchSmsAttributes.SmsReceiverParams();
            if(params!=null){
                for(String key : params.keySet()){
                    smsReceiverParams.setParam(key, String.valueOf(params.get(key)));
                }
            }
//            smsReceiverParams.setParam("$YourSMSTemplateParamKey1", "$value1");
//            smsReceiverParams.setParam("$YourSMSTemplateParamKey2", "$value2");
            // 3.4 增加接收短信的号码
//            batchSmsAttributes.addSmsReceiver("$YourReceiverPhoneNumber1", smsReceiverParams);
//            batchSmsAttributes.addSmsReceiver("$YourReceiverPhoneNumber2", smsReceiverParams);
            if(toMobiles!=null){
                for(String mobile : toMobiles){
                    batchSmsAttributes.addSmsReceiver(mobile, smsReceiverParams);
                }
            }
            messageAttributes.setBatchSmsAttributes(batchSmsAttributes);
            try {
                /**
                 * Step 4. 发布SMS消息
                 */
                TopicMessage ret = topic.publishMessage(msg, messageAttributes);
                System.out.println("MessageId: " + ret.getMessageId());
                System.out.println("MessageMD5: " + ret.getMessageBodyMD5());
            } catch (ServiceException se) {
                System.out.println(se.getErrorCode() + se.getRequestId());
                System.out.println(se.getMessage());
                se.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            client.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	/**
	 * 发送短信
	 * 
	 * @param templateCode
	 *            模板编码
	 * @param params
	 *            参数
	 * @param toMobiles
	 *            接收人手机号码
	 * @param async
	 *            是否异步
	 */
	public static void send(String templateCode,Map<String,Object> params,String[] toMobiles, boolean async) {
		if (async) {
			addSendTask(templateCode,params,toMobiles);
		} else {
			send(templateCode,params,toMobiles);
		}
	}


	/**
	 * 发送短信(异步)
	 * 
	 * @param templateCode
	 *            模板编码
	 * @param params
	 *            参数
	 * @param toMobile
	 *            接收人手机号码
	 */
	public static void send(String templateCode,Map<String,Object> params,String toMobile) {
		send(templateCode,params,new String[] { toMobile }, true);
	}

	/**
	 * 发送测试短信(同步)
	 * 
	 * @param toMobile
	 *            接收人手机号码
	 */
	public static void sendTestSms(String toMobile) {
		MessageConfig messageConfig = new MessageConfig().dao().findByType(CommonAttribute.MESSAGE_CONFIG_TYPE_TEST_MESSAGE);
		if (messageConfig == null || !messageConfig.getIsSmsEnabled()) {
			return;
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("customer", "测试");
		send(messageConfig.getSmsCode(),params,new String[] { toMobile },false);
	}
	
	/**
	 * 发送会员注册短信(异步)
	 * 
	 * @param member
	 *            会员
	 */
	public static void sendRegisterMemberSms(String code,String toMobile) {
		MessageConfig messageConfig = new MessageConfig().dao().findByType(CommonAttribute.MESSAGE_CONFIG_TYPE_REGISTER_MEMBER);
		if (messageConfig == null || !messageConfig.getIsSmsEnabled()) {
			return;
		}
		Config config = SystemUtils.getConfig();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("code", code);
		params.put("product", config.getSiteName());
		
		send(messageConfig.getSmsCode(),params,toMobile);
	}

    /**
     * 发送找回密码短信(异步)
     * 
     * @param member
     *            会员
     */
    public static void sendFindPasswordSms(String code,String toMobile) {
        MessageConfig messageConfig = new MessageConfig().dao().findByType(CommonAttribute.MESSAGE_CONFIG_TYPE_FIND_PASSWORD);
        if (messageConfig == null || !messageConfig.getIsSmsEnabled()) {
            return;
        }
        Config config = SystemUtils.getConfig();
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("code", code);
        params.put("product", config.getSiteName());
        
        send(messageConfig.getSmsCode(),params,toMobile);
    }


}

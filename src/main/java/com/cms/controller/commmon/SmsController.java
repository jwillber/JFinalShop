package com.cms.controller.commmon;

import java.util.Date;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.time.DateUtils;

import com.cms.CommonAttribute;
import com.cms.entity.SafeKey;
import com.cms.routes.RouteMapping;
import com.cms.util.SmsUtils;
import com.jfinal.core.Controller;

@RouteMapping(url = "/common/sms")
public class SmsController extends Controller{

    
    public void send(){
        String mobile = getPara("mobile");
        String type = getPara("type");
        String code = RandomStringUtils.randomNumeric(4);
        if(CommonAttribute.messageConfigTypeNames.keySet().contains(type)){
            SafeKey safeKey = new SafeKey();
            safeKey.setExpire(DateUtils.addHours(new Date(), 1));
            safeKey.setMobile(mobile);
            safeKey.setCreateDate(new Date());
            safeKey.setModifyDate(new Date());
            safeKey.setValue(code);
            safeKey.setType(type);
            safeKey.save();
            switch (type) {
                case CommonAttribute.MESSAGE_CONFIG_TYPE_TEST_MESSAGE:
                    //SmsUtils.sendTestSms(mobile);
                    break;
                case CommonAttribute.MESSAGE_CONFIG_TYPE_REGISTER_MEMBER:
                    //SmsUtils.sendRegisterMemberSms(code,mobile);
                    break;
                case CommonAttribute.MESSAGE_CONFIG_TYPE_FIND_PASSWORD:
                    //SmsUtils.sendFindPasswordSms(code,mobile);
                    break;
            }
        }
        renderJson(code);
    }
    
}

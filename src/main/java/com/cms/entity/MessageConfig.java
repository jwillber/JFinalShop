package com.cms.entity;

import java.util.List;

import com.cms.entity.base.BaseMessageConfig;

/**
 * Entity - 消息配置
 * 
 * 
 * 
 */
public class MessageConfig extends BaseMessageConfig<MessageConfig> {
	
	private static final long serialVersionUID = -878152889660463735L;
	
	/**
	 * 查找所有消息配置
	 * 
	 * @return 所有消息配置
	 */
	public List<MessageConfig> findAll(){
		return find("select * from kf_message_config");
	}
	
	/**
	 * 根据类型查找消息配置
	 * 
	 * @param type
	 *           类型
	 * @return 消息配置
	 */
	public MessageConfig findByType(String type) {
		if (type == null) {
			return null;
		}
		String sql = "select * from kf_message_config where type = ?";
		return findFirst(sql,type);
	}
}

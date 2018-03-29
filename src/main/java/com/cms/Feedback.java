/*
 * 
 * 
 * 
 */
package com.cms;

/**
 * 消息
 * 
 * 
 * 
 */
public class Feedback {


	/** 类型 */
	private String type;

	/** 数据 */
	private Object data;
	
	/**消息*/
	private String msg;
	
	public Feedback(String type, Object data,String msg) {
		this.type = type;
		this.data = data;
		this.msg = msg;
	}

	/**
	 * 返回成功
	 * @param data
	 * @return
	 */
	public static Feedback success(Object data) {
		return new Feedback(CommonAttribute.FEEDBACK_TYPE_SUCCESS, data , null);
	}

	/**
	 * 返回错误
	 * @param msg
	 * @return
	 */
	public static Feedback error(String msg) {
		return new Feedback(CommonAttribute.FEEDBACK_TYPE_ERROR, null, msg);
	}
	
	/**
	 * 返回警告
	 * @param msg
	 * @return
	 */
	public static Feedback warn(String msg) {
        return new Feedback(CommonAttribute.FEEDBACK_TYPE_WARN, null, msg);
    }

	
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
	

}
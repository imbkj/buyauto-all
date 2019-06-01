package com.buyauto.util.pojo;

import java.io.Serializable;
import java.util.Map;

import com.google.common.collect.Maps;

/**
 * 
 * @ClassName: MessageParam
 * @Description: 消息基类
 * @author cxz
 * @date 2017年5月17日 下午1:48:14
 *
 */
public class MessageParam implements Serializable {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = -5884421708639286499L;

	/**
	 * 发送地址
	 */
	public String address;

	/**
	 * 主题
	 */
	public String subject;

	/**
	 * 类型 0 邮件 1 短信
	 */
	public int type;

	/**
	 * 正文
	 */
	private String context;

	/**
	 * 发送模版名称
	 */
	private String templateName;

	/**
	 * 发送模版自定义参数
	 */
	private Map<String, String> templateParam;

	public Map<String, String> getTemplateParam() {
		return templateParam;
	}

	public void setTemplateParam(Map<String, String> templateParam) {
		this.templateParam = templateParam;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public MessageParam() {
		super();
	}

	@Override
	public String toString() {
		return "MessageParam [address=" + address + ", subject=" + subject + ", type=" + type + ", context=" + context
				+ ", templateName=" + templateName + ", templateParam=" + templateParam + "]";
	}

	public MessageParam(String address, int type, String templateName) {
		super();
		this.address = address;
		this.subject = "";
		this.type = type;
		this.context = "";
		this.templateName = templateName;
		this.templateParam = Maps.newHashMap();
	}

}

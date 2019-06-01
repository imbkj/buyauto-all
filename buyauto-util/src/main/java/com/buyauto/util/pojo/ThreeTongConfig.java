package com.buyauto.util.pojo;

import org.springframework.beans.factory.annotation.Value;

/**
 * @ClassName: ThreeTongSms
 * @Description: 大汉三通短信实体
 * @author liangbo
 * @date 2015年8月21日 上午11:51:26
 *
 */
public class ThreeTongConfig {

	@Value("${three.tong.account}")
	private String account;// 用户名

	@Value("${three.tong.password}")
	private String password;// 密码(明文)
	private String Phone; // 要发送的手机号码
	private String Content;// 短信内容
	@Value("${three.tong.sign}")
	private String sign; // 短信签名
	private String Subcode; // 子号码，可为空
	private String Msgid; // 短信id，查询短信状态报告时需要，可为空
	private String Sendtime; // 定时发送时间

	public ThreeTongConfig() {
		this.Content = "";
		this.Subcode = "";
		this.Msgid = "";
		this.Sendtime = "";

	}

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String phone) {
		Phone = phone;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getSubcode() {
		return Subcode;
	}

	public void setSubcode(String subcode) {
		Subcode = subcode;
	}

	public String getMsgid() {
		return Msgid;
	}

	public void setMsgid(String msgid) {
		Msgid = msgid;
	}

	public String getSendtime() {
		return Sendtime;
	}

	public void setSendtime(String sendtime) {
		Sendtime = sendtime;
	}

	public String getAccount() {
		return account;
	}

	public String getPassword() {
		return password;
	}

	public String getSign() {
		return sign;
	}
}

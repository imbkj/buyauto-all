package com.msg.service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;

import com.buyauto.util.method.CommonCode;
import com.buyauto.util.pojo.MessageParam;
import com.buyauto.util.pojo.MsgConfig;
import com.buyauto.util.pojo.ThreeTongConfig;
import com.ctc.smscloud.xml.http.RespInfo;
import com.ctc.smscloud.xml.http.utils.XMLHttpClientUtil;
import com.google.common.base.Charsets;
import com.msg.sms.IMessageApi;

/**
 * 
 * @ClassName: MessageImpl
 * @Description: 消息发送
 * @author cxz
 * @date 2017年5月17日 下午1:50:50
 *
 */
@Service
public class MessageImpl implements IMessageApi {

	private static final Logger logger = LoggerFactory.getLogger(MessageImpl.class);
	@Autowired
	protected VelocityConfigurer velocityConfigurer;

	/*-------------------------------------------------------邮件默认配置-----------------------------------------------------------------*/
	private static final String EMAILNAME = "51buyauto@sina.com";
	private static final String EMAILPWD = "!QAZ2wsx";
	private static final String NHJF_CN = "麦卡进口车";

	/*-------------------------------------------------------发送邮件-----------------------------------------------------------------*/

	public <T extends MessageParam> boolean sendEmail(T messageParam) {
		if (StringUtils.isEmpty(messageParam.getAddress())) {
			return false;
		}

		JavaMailSenderImpl senderImpl = setEmailUser();

		if (messageParam.getContext() == null || "".equals(messageParam.getContext())) {
			messageParam.setContext(getExpressContent(messageParam, "email"));
		}

		// 建立邮件讯息
		MimeMessage mailMessage = senderImpl.createMimeMessage();
		MimeMessageHelper messageHelper;
		try {
			messageHelper = new MimeMessageHelper(mailMessage, true, "utf-8");
			// 设定收件人、寄件人、主题与内文
			messageHelper.setTo(messageParam.getAddress());
			messageHelper.setFrom(EMAILNAME, NHJF_CN);
			messageHelper.setSubject(messageParam.getSubject());
			messageHelper.setText(messageParam.getContext(), true);

			// 传送邮件
			senderImpl.send(mailMessage);
			logger.info(messageParam.getAddress() + "邮件发送成功！");
		} catch (MessagingException e) {
			logger.info(messageParam.getAddress() + "邮件发送失败！");
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * 
	 * @Title: sendEmailUser
	 * @Description: 设置邮件用户名密码
	 * @param @return 设定文件
	 * @return JavaMailSenderImpl 返回类型
	 * @throws
	 */
	private JavaMailSenderImpl setEmailUser() {
		JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
		// 设定 Mail Server
		senderImpl.setHost("smtp.sina.com");
		// SMTP验证时，需要用户名和密码
		senderImpl.setUsername(EMAILNAME);
		senderImpl.setPassword(EMAILPWD);
		return senderImpl;
	}

	/*-------------------------------------------------------短信默认配置-----------------------------------------------------------------*/

	@Autowired
	protected MsgConfig msgConfig;

	@Autowired
	protected ThreeTongConfig threeTongSms;

	/*-------------------------------------------------------发送短信-----------------------------------------------------------------*/

	public boolean sendSms(String msg, String phone) throws Exception {
		if (!msgConfig.isRealSend()) {
			logger.info(" send fake msg:" + msg + " to " + phone);
			return true;
		}

		// 服务端地址，默认可不设置
		XMLHttpClientUtil.setServerUrlBase("http://3tong.net");

		RespInfo _respInfo = null;
		// 发送短信

		_respInfo = XMLHttpClientUtil.sendSms(threeTongSms.getAccount(), threeTongSms.getPassword(),
				threeTongSms.getMsgid(), phone, msg, threeTongSms.getSign(), threeTongSms.getSubcode(),
				threeTongSms.getSendtime());

		if (XMLHttpClientUtil.whetherSuccess(_respInfo)) {// 成功无异常
			logger.info("Succ send real msg:" + msg + " to " + phone);
			System.out.print("成功");
			return true;
		} else {// 异常情况
			logger.info(this + " send real msg:" + msg + " to " + phone);
			logger.error("Fail send real msg:" + msg + " to " + phone);
		}

		return false;
	}

	public <T extends MessageParam> boolean sendInformation(T messageParam) {

		if (!msgConfig.isRealSend()) {
			logger.info(" send fake msg:" + messageParam.getContext() + " to " + messageParam.getAddress());
			return true;
		}

		String con = getExpressContent(messageParam, "information");

		messageParam.setContext(con);

		/* 服务端地址，默认可不设置 */
		XMLHttpClientUtil.setServerUrlBase("http://3tong.net");
		/* 发送短信 */
		RespInfo _respInfo = XMLHttpClientUtil.sendSms(threeTongSms.getAccount(), threeTongSms.getPassword(),
				threeTongSms.getMsgid(), messageParam.getAddress(), messageParam.getContext(), threeTongSms.getSign(),
				threeTongSms.getSubcode(), threeTongSms.getSendtime());
		System.out.println(_respInfo.getRespVal());

		if (XMLHttpClientUtil.whetherSuccess(_respInfo)) {
			/* 发送成功 */
			logger.info("【发送成功】-Succ send real msg：" + messageParam.getContext() + " to " + messageParam.getAddress());
			return true;
		} else {
			/* 发送失败 */
			logger.error("【发送失败】-Fail send real msg：" + messageParam.getContext() + " to " + messageParam.getAddress());
			return false;
		}

	}

	/*-------------------------------------------------------通用方法-----------------------------------------------------------------*/

	/**
	 * 
	 * @Title: getExpressContent
	 * @Description: 注入模版内容，获取字符串
	 * @param @param t
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public <T extends MessageParam> String getExpressContent(T messageParam, String foldName) {
		Map<String, Object> model = new HashMap<String, Object>();

		model.put("expr", messageParam);

		String html = VelocityEngineUtils.mergeTemplateIntoString(velocityConfigurer.getVelocityEngine(),
				this.getExpressVmPath(foldName + "/" + messageParam.getTemplateName()), Charsets.UTF_8.displayName(),
				model);

		return html;
	};

	/**
	 * 
	 * @Title: getExpressVmPath
	 * @Description: 设置模版路径
	 * @param @param templateName
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getExpressVmPath(String templateName) {
		String vmPath = "template/" + templateName + ".vm";
		return vmPath;
	}

	@Override
	public <T extends MessageParam> boolean sendMessagel(T messageParam) {
		boolean isNext = false;
		if (messageParam.getType() == CommonCode.CmsType.Infomartion) {
			isNext = this.sendInformation(messageParam);
		} else if (messageParam.getType() == CommonCode.CmsType.Email) {
			isNext = this.sendEmail(messageParam);
		}
		return isNext;
	}
}

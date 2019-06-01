package com.controller.msg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buyauto.util.pojo.MessageParam;
import com.external.msg.api.ISendMessageApi;
import com.msg.sms.IMessageApi;

@Service("sendMessageImpl")
public class SendMessageImpl implements ISendMessageApi {

	@Autowired
	private IMessageApi messageApi;

	@Override
	public void sendMessageApi(MessageParam param) {
		messageApi.sendMessagel(param);
	}



}

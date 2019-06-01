package com.msg.sms;

import com.buyauto.util.pojo.MessageParam;

/**
 * 
 * @ClassName: IEamilApi
 * @Description:发送消息
 * @author cxz
 * @date 2015年10月22日 下午2:26:13
 *
 */
public interface IMessageApi {

	/**
	 * 
	 * @Title: sendMessagel
	 * @Description: 消息发送
	 * @param @param t
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public <T extends MessageParam> boolean sendMessagel(T t);

}

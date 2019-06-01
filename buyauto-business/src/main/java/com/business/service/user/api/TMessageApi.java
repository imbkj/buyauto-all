package com.business.service.user.api;

import com.buyauto.entity.user.TUsersMsg;
import com.buyauto.util.pojo.PageVo;

public interface TMessageApi {

	/**
	* @Title: messageList 
	* @Description: 获取信息中心列表
	* @param id
	* @param page
	* @param rows
	* @return PageVo<TUsersMsg>    返回类型 
	* @throws 
	*/
	PageVo<TUsersMsg> messageList(Long id, int page, int rows);

	/**
	* @Title: doLevelUp 
	* @Description: 申请个人等级提升
	* @param @param id
	* @param @param msgId
	* @param @param name
	* @param @param cardID
	* @param @param cardFile
	* @param @param debitCard
	* @param @param debitCardBank
	* @param @param debitCardFile
	* @param @param creditCard
	* @param @param creditCardBank
	* @param @param creditCardFile    设定文件 
	* @return Integer    返回类型 
	* @throws 
	*/
	Integer doLevelUp(Long id, String msgId, String name, String cardID, String cardFile, String cardBFile, String debitCard,
			String debitCardBank, String debitCardFile, String creditCard, String creditCardBank,
			String creditCardFile);
	/** 
	* @Title: sendInsideMsg 
	* @Description: 发送站内信
	* @param id
	* @param info
	* @return Integer    返回类型 
	* @throws 
	*/
	Integer sendInsideMsg(Long id, String info,Integer msgType);

	Integer messageHasRead(String id);

	Integer messageAllRead(Long id);

	Integer checkUpLevel(Long id);

	/**
	 * 上传卡信息
	 * @param token 
	 * @param facade 姓名\卡号
	 * @param content 身份证\卡行
	 * @param cardFile 身份证正面\银行卡正面
	 * @param cardbFile 身份证反面
	 * @param type 1:身份证 2：借记卡 3：信用卡
	 * @return
	 */
	Integer subInfo(Long userId, String facade, String content, String cardFile, String cardbFile, String type);

	/**
	 * 提交
	 * @param userId 用户ID
	 * @param msgId 消息ID
	 * @return
	 */
	Integer checkInfoCard(Long userId, String msgId);

}

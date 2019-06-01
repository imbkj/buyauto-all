package com.business.service.user.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.business.service.user.api.TMessageApi;
import com.business.service.user.api.TUsersApi;
import com.buyauto.entity.user.TUsers;
import com.buyauto.entity.user.TUsersMsg;
import com.buyauto.entity.user.TUsersSales;
import com.buyauto.mapper.user.TUsersMsgMapper;
import com.buyauto.mapper.user.TUsersSalesMapper;
import com.buyauto.util.method.CommonCode;
import com.buyauto.util.method.KeyUtil;
import com.buyauto.util.method.StringUtil;
import com.buyauto.util.pojo.PageVo;

@Service
public class TMessageImpl implements TMessageApi {

	private static final Logger logger = LoggerFactory.getLogger(TMessageImpl.class);

	@Autowired
	private TUsersMsgMapper tUsersMsgMapper;
	@Autowired
	private TUsersSalesMapper tUsersSalesMapper;

	@Autowired
	private TUsersApi tUsersImpl;
	
	/**
	 * @Title: messageList @Description: 获取信息中心列表 @param id @param page @param
	 *         rows @return PageVo<TUsersMsg>    返回类型 @throws
	 */
	@Override
	public PageVo<TUsersMsg> messageList(Long id, int page, int rows) {
		PageVo<TUsersMsg> pageVo = new PageVo<TUsersMsg>(0, page, rows);
		List<TUsersMsg> list = tUsersMsgMapper.queryMessage(id, pageVo.getPageStartNumber(), pageVo.getPageEndNumber());
		int msgCount = tUsersMsgMapper.queryMessageCount(id);
		pageVo.setTotal(msgCount);
		pageVo.setRows(list);
		return pageVo;
	}

	/**
	 * @Title: doLevelUp @Description: 申请个人等级提升 @param id @param msgId @param
	 *         name @param cardID @param cardFile @param debitCard @param
	 *         debitCardBank @param debitCardFile @param creditCard @param
	 *         creditCardBank @param creditCardFile    设定文件 @return Integer   
	 *         返回类型 @throws
	 */
	@Override
	public Integer doLevelUp(Long id, String msgId, String name, String cardID, String cardFile,String cardBFile, String debitCard,
			String debitCardBank, String debitCardFile, String creditCard, String creditCardBank,
			String creditCardFile) {
		TUsersSales tUsersSales = tUsersSalesMapper.selectByUid(id);
		TUsersMsg tUsersMsg = new TUsersMsg();
		tUsersMsg.setId(Long.valueOf(msgId));
		tUsersMsg.setStatus(CommonCode.MsgStatus.READ);
		tUsersMsgMapper.updateByPrimaryKeySelective(tUsersMsg);
		if (tUsersSales == null) {
			TUsersSales tUsersSalesNew = new TUsersSales(KeyUtil.generateDBKey(), id, name, cardID, cardFile,cardBFile, debitCard,
					debitCardBank, debitCardFile, creditCard, creditCardBank, creditCardFile, new Date(),
					CommonCode.SalesStatus.WAITPASS);
			tUsersSalesMapper.insertSelective(tUsersSalesNew);
		} else if (tUsersSales.getStatus() == CommonCode.SalesStatus.NOPASS || tUsersSales.getStatus() == CommonCode.SalesStatus.PASS) {
			TUsersSales tUsersSalesNew = new TUsersSales(tUsersSales.getId(), id, name, cardID, cardFile,cardBFile, debitCard,
					debitCardBank, debitCardFile, creditCard, creditCardBank, creditCardFile, new Date(),
					CommonCode.SalesStatus.WAITPASS);
			tUsersSalesMapper.updateByPrimaryKeySelective(tUsersSalesNew);
		} else if (tUsersSales.getStatus() == CommonCode.SalesStatus.WAITPASS) {
			return CommonCode.LevelUpResult.INDOING;
		} else {
			return CommonCode.LevelUpResult.HASDONE;
		}
		sendInsideMsg(id, "您的升级邀请已提交，请耐心等待后台审核。",CommonCode.MsgType.NONE);
		return CommonCode.LevelUpResult.SUCCESS;
	}

	/**
	 * @Title: sendInsideMsg @Description: 发送站内信 @param id @param info @return
	 *         Integer    返回类型 @throws
	 */
	@Override
	public Integer sendInsideMsg(Long id, String info,Integer msgType) {
		Integer c = 0;
		TUsersMsg tUsersMsg = new TUsersMsg(KeyUtil.generateDBKey(), id, info, msgType, new Date(),
				CommonCode.MsgStatus.NOREAD);
		c = tUsersMsgMapper.insertSelective(tUsersMsg);
		if (c <= CommonCode.RESULTNUM) {
			return CommonCode.Audit.ERROR;
		}
		return CommonCode.Audit.SUCCESS;
	}

	@Override
	public Integer messageHasRead(String id) {
		TUsersMsg tUsersMsg = new TUsersMsg();
		tUsersMsg.setId(Long.valueOf(id));
		tUsersMsg.setStatus(CommonCode.MsgStatus.READ);
		Integer c = tUsersMsgMapper.updateByPrimaryKeySelective(tUsersMsg);
		if (c <= CommonCode.RESULTNUM) {
			return CommonCode.Audit.ERROR;
		}
		return CommonCode.Audit.SUCCESS;
	}

	@Override
	public Integer messageAllRead(Long id) {
		tUsersMsgMapper.updateAllReadByUid(id);
		return CommonCode.Audit.SUCCESS;
	}

	@Override
	public Integer checkUpLevel(Long id) {
		TUsersSales tUsersSales = tUsersSalesMapper.selectByUid(id);
		if (tUsersSales != null && tUsersSales.getStatus() == CommonCode.SalesStatus.WAITPASS) {
			return CommonCode.Audit.ERROR;
		}else{
			return CommonCode.Audit.SUCCESS;
		}
	}

	/**
	 * 上传卡信息
	 * @param userId 用户ID
	 * @param facade 姓名\卡号
	 * @param content 身份证\卡行
	 * @param cardFile 身份证正面\银行卡正面
	 * @param cardbFile 身份证反面
	 * @param type 1:身份证 2：借记卡 3：信用卡
	 * @return
	 */
	@Override
	public Integer subInfo(Long id, String facade, String content, String cardFile, String cardbFile, String type) {
		Integer code = subInfoCheck( id,  facade,  content,  cardFile,  cardbFile,  type);
		if(code!=null) return code;
		TUsersSales tUsersSalesNew;
		if(CommonCode.CardType.CARDONE.equals(type)){
			tUsersSalesNew =  new TUsersSales(KeyUtil.generateDBKey(), id, facade, content, cardFile,cardbFile, null,null, null, null, null, null, new Date(),CommonCode.SalesStatus.WAITPASS);
		}else if(CommonCode.CardType.CARDTWO.equals(type)){
			tUsersSalesNew = new TUsersSales(KeyUtil.generateDBKey(), id, null, null, null,null, facade,content, cardFile, null, null, null, new Date(),CommonCode.SalesStatus.WAITPASS);
		}else if(CommonCode.CardType.CARDTHREE.equals(type)){
			tUsersSalesNew = new TUsersSales(KeyUtil.generateDBKey(), id, null, null, null,null, null,null, null, facade, content, cardFile, new Date(),CommonCode.SalesStatus.WAITPASS);
		}else{
			return CommonCode.RetAppInfo.ERRORTYPE;//类型错误
		}
		TUsersSales tUsersSales = tUsersSalesMapper.selectByUid(id);
		if (tUsersSales == null) {
			tUsersSalesMapper.insertSelective(tUsersSalesNew);
		}else {
			tUsersSalesNew.setId(tUsersSales.getId());
			tUsersSalesMapper.updateByPrimaryKeySelective(tUsersSalesNew);
//			return CommonCode.LevelUpResult.HASDONE;//已经通过审核
		}
		return CommonCode.LevelUpResult.SUCCESS;//提交成功;
	}
	
	public Integer subInfoCheck(Long userId, String facade, String content, String cardFile, String cardbFile, String type){
		if(StringUtil.isEmpty(facade)){
			return CommonCode.RetAppInfo.NFACADE;//姓名或卡号为空
		}
		if(StringUtil.isEmpty(content)){
			return CommonCode.RetAppInfo.NCONTENT;//身份证或开户行为空
		}
		if(StringUtil.isEmpty(cardFile)){
			return CommonCode.RetAppInfo.NCARDFILE;//身份证正面文件或银行卡照片为空
		}
		if("1".equals(type)){
			if(StringUtil.isEmpty(cardbFile)){
				return CommonCode.RetAppInfo.NCARDBFILE;//身份证反面文件
			}
		}
		return null;
	}

	/**
	 * 提交
	 * @param userId 用户ID
	 * @param msgId 消息ID
	 * @return
	 */
	@Override
	public Integer checkInfoCard(Long userId, String msgId) {
		TUsersSales tUsers = tUsersImpl.getSalerById(userId);
		if(StringUtil.isEmpty(tUsers.getCardid()) || StringUtil.isEmpty(tUsers.getCardFile()) || StringUtil.isEmpty(tUsers.getCardBFile())){
			return CommonCode.RetAppInfo.NOCARD;//身份证未上传
		}
		if(StringUtil.isEmpty(tUsers.getDebitCard()) || StringUtil.isEmpty(tUsers.getDebitCardBank()) || StringUtil.isEmpty(tUsers.getDebitCardFile())){
			return CommonCode.RetAppInfo.NODEBIT;//借记卡未上传
		}
		if(StringUtil.isEmpty(tUsers.getCreditCard()) ||StringUtil.isEmpty(tUsers.getCreditCardBank()) || StringUtil.isEmpty(tUsers.getCreditCardFile())){
			return CommonCode.RetAppInfo.NOCREDIT;//信用卡未上传
		}
//		TUsersMsg tUsersMsg = new TUsersMsg();
//		tUsersMsg.setId(Long.valueOf(msgId));
//		tUsersMsg.setStatus(CommonCode.MsgStatus.READ);
//		tUsersMsgMapper.updateByPrimaryKeySelective(tUsersMsg);
		sendInsideMsg(userId, "您的升级邀请已提交，请耐心等待后台审核。",CommonCode.MsgType.NONE);
		return null;
	}
}

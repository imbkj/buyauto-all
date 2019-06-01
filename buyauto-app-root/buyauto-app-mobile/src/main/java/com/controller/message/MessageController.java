package com.controller.message;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.business.service.user.api.TMessageApi;
import com.business.service.user.api.TUsersApi;
import com.buyauto.entity.user.TUsers;
import com.buyauto.entity.user.TUsersMsg;
import com.buyauto.entity.user.TUsersSales;
import com.buyauto.util.anno.AuthAnno;
import com.buyauto.util.method.AuthBackend;
import com.buyauto.util.method.CommonCode;
import com.buyauto.util.method.RedisUtil;
import com.buyauto.util.method.SessionUtil;
import com.buyauto.util.method.StringUtil;
import com.buyauto.util.pojo.PageVo;
import com.buyauto.util.pojo.UserSessionInfo;
import com.buyauto.util.web.AppWebResultVo;
import com.buyauto.util.web.UrlRegulation;
import com.external.user.api.ITuserApi;

/**
 * 消息中心
 */
@RestController
@RequestMapping(value = UrlRegulation.SecurityPrefix.HTTP + UrlRegulation.RequestPrefix.REQ_LOGIN
		+ UrlRegulation.BizPrefix.MSG)
public class MessageController {

	private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

	@Autowired
	private TMessageApi messageImpl;
	@Autowired
	private TUsersApi tUsersImpl;
	@Qualifier("userService")
	@Autowired
	private ITuserApi userService;

	/**
	 * 获取消息列
	 * @return
	 */
	@RequestMapping(value = "/getMegInfo")
	@ResponseBody
	public AppWebResultVo<PageVo<TUsersMsg>>  getMegInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam("token") String token,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int rows) {
		// 获取当前登录用户
		Long userId = RedisUtil.getUserByKey(token);
		if(null==userId){
			return AppWebResultVo.buildFail(CommonCode.RetAppInfo.NUSEROBJ);
		}
		PageVo<TUsersMsg> tUsersMsgList = messageImpl.messageList(userId, page, rows);
		return AppWebResultVo.buildSucc(tUsersMsgList);
	}

	/**
	 * 消息已读
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/hasRead")
	@ResponseBody
	public AppWebResultVo<String> hasRead(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("token") String token,
			@RequestParam("id") String id) {
		try {
			Long userId = RedisUtil.getUserByKey(token);
			if(null==userId){
				return AppWebResultVo.buildFail(CommonCode.RetAppInfo.NUSEROBJ);
			}
			if(StringUtil.isEmpty(id)){
				return AppWebResultVo.buildWarn(CommonCode.RetAppInfo.NMSG);
			}
			Integer code = messageImpl.messageHasRead(id);
			if(CommonCode.Audit.ERROR.equals(code)){
				return AppWebResultVo.buildWarn(CommonCode.RetAppInfo.ERROR);
			}
			return AppWebResultVo.buildSucc("");
		} catch (Exception e) {
			logger.error("消息已读报错！id:"+id);
			return AppWebResultVo.buildWarn(CommonCode.RetAppInfo.ERROR);
		}
	}
	
	/**
	 * 接受邀请
	 * @param id  消息ID
	 * @param token
	 * @return
	 */
	@RequestMapping("/checkUpLevel")
	public AppWebResultVo<String> checkUpLevel(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam("id") String id,
			@RequestParam("token") String token) {
		// 获取当前登录用户
		Long userId = RedisUtil.getUserByKey(token);
		if(null==userId){
			return AppWebResultVo.buildFail(CommonCode.RetAppInfo.NUSEROBJ);
		}
		TUsers tUsers = tUsersImpl.getPersonalById(userId);
		Integer code = 0;
		if (tUsers.getRealLevel() == null || tUsers.getSysLevel() == null || tUsers.getRealLevel() >= tUsers.getSysLevel()) {
			code = CommonCode.RetAppInfo.WRONGDATA;//错误数据
		}
		/*个人销售升经销商*/
		if(tUsers.getRealLevel() < tUsers.getSysLevel() && tUsers.getRealLevel() == CommonCode.SysLevel.SELLER){
			tUsersImpl.editUserLevelById(userId, null, CommonCode.SysLevel.BOSS);
			code =CommonCode.RetAppInfo.PASS;/*审核通过*/
		}else{
			/*个人升销售*/
			code = messageImpl.checkUpLevel(userId);
		}
		/*消息设置已读*/
		if (code == CommonCode.RetAppInfo.WRONGDATA || code == CommonCode.Audit.ERROR || code == CommonCode.RetAppInfo.PASS) {
			messageImpl.messageHasRead(id);
		}
		if(code == CommonCode.Audit.ERROR){
			code = CommonCode.RetAppInfo.WAITID;//用户证件待审核
		}else if(code ==  CommonCode.Audit.SUCCESS){
			code = CommonCode.RetAppInfo.NOUSERCARD;//用户证件未上传
		}
		return AppWebResultVo.buildSucc(code.toString());
	}
	
	/**
	 * 到立即升级页面
	 * @param session
	 * @param id
	 * @return
	 */
	@RequestMapping("/personalInfo")
	public ModelAndView personalInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam("token") String token,
			@RequestParam("msgId") String msgId) {
		// 获取当前登录用户
		Long userId = RedisUtil.getUserByKey(token);
		if(null==userId){
			logger.error("到立即升级页面,未查到该用户ID。token:"+token);
//			return new ModelAndView("other/500");
		}
		TUsers tUsers = tUsersImpl.getPersonalById(userId);
		/*等级*/
		if (tUsers == null || tUsers.getRealLevel() == tUsers.getSysLevel() || tUsers.getRealLevel() != CommonCode.SysLevel.PERSONAL) {
			logger.error("APP到立即升级页面,等级错误。token:"+token+" 用户ID:"+userId);
//			return new ModelAndView("other/500");
		}
		/*已读*/
		messageImpl.messageHasRead(msgId);
		ModelAndView modelAndView = new ModelAndView("files_cards/upgrade");
		modelAndView.addObject("msgId", msgId);//消息ID
		modelAndView.addObject("token", token);//唯一码
		modelAndView.addObject("user", tUsers);//用户信息
		return modelAndView;
	}
	
	/**
	 * 提交按钮
	 * @param token
	 * @param msgId
	 * @return
	 */
	@RequestMapping("/subButton")
	public String subButton(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam("token") String token,
			@RequestParam("msgId") String msgId) {
		// 获取当前登录用户
		Long userId = RedisUtil.getUserByKey(token);
		if(null==userId){
			logger.info("提交按钮,未查到该用户ID。token:"+token);
			return CommonCode.RetAppInfo.NUSEROBJ;
		}
		Integer code = messageImpl.checkInfoCard(userId,msgId);
		if(code!=null){
			return code.toString();
		}else{
			return CommonCode.RetAppInfo.SUCCESS;
		}
	}
	
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
	@RequestMapping("/subInfo")
	public String subInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam("token") String token,
			@RequestParam("facade") String facade,
			@RequestParam("content") String content,
			@RequestParam("cardFile") String cardFile,
			@RequestParam(value="cardbFile",required=false) String cardbFile,
			@RequestParam(value="type",defaultValue="1") String type) {
		// 获取当前登录用户
		Long userId = RedisUtil.getUserByKey(token);
		if(null==userId){
			logger.info("上传卡信息接口,未查到该用户ID。token:"+token);
			return CommonCode.RetAppInfo.NUSEROBJ;
		}
		Integer code = messageImpl.subInfo(userId,facade,content,cardFile,cardbFile,type);
		return code.toString();
	}
	
	/**
	 * 到填写信用卡信息页面
	 * @param token 用户唯一吗
	 * @param msgId 消息ID
	 * @return
	 */
	@RequestMapping("/creditCards")
	public ModelAndView test1(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam("token") String token,
			@RequestParam("msgId") String msgId) {
		TUsersSales tUsers = getById(token);
		ModelAndView mb = new ModelAndView("files_cards/credit_cards");
		mb.addObject("user", tUsers);
		mb.addObject("msgId", msgId);
		mb.addObject("token", token);
		return mb;
	}
	
	/**
	 * 到填写借记卡信息页面
	 * @param token 用户唯一吗
	 * @param msgId 消息ID
	 * @return
	 */
	@RequestMapping("/debitCards")
	public ModelAndView test2(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam("token") String token,
			@RequestParam("msgId") String msgId) {
		TUsersSales tUsers = getById(token);
		ModelAndView mb = new ModelAndView("files_cards/debit_cards");
		mb.addObject("user", tUsers);
		mb.addObject("msgId", msgId);
		mb.addObject("token", token);
		return mb;
	}
	
	/**
	 * 到填写身份证信息页面
	 * @param token 用户唯一吗
	 * @param msgId 消息ID
	 * @return
	 */
	@RequestMapping("/idcards")
	public ModelAndView test3(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam("token") String token,
			@RequestParam("msgId") String msgId) {
		TUsersSales tUsers = getById(token);
		ModelAndView mb = new ModelAndView("files_cards/idcards");
		mb.addObject("user", tUsers);
		mb.addObject("msgId", msgId);
		mb.addObject("token", token);
		return mb;
	}
	
	@RequestMapping("/test4")
	public ModelAndView test4(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		return new ModelAndView("files_cards/upgrade");
	}
	
	private TUsersSales getById(String token){
		Long userId = RedisUtil.getUserByKey(token);
		if(null==userId){
			logger.info("未查到该用户ID。token:"+token);
		}
		try {
			TUsersSales tUsers = tUsersImpl.getSalerById(userId);
			return tUsers;
		} catch (Exception e) {
			return null;
		}
	}
}

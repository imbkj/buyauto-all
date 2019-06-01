package com.controller.message;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.business.service.user.api.TMessageApi;
import com.business.service.user.api.TUsersApi;
import com.buyauto.entity.user.TUsers;
import com.buyauto.entity.user.TUsersMsg;
import com.buyauto.util.anno.AuthAnno;
import com.buyauto.util.method.AuthBackend;
import com.buyauto.util.method.CommonCode;
import com.buyauto.util.method.SessionUtil;
import com.buyauto.util.pojo.PageVo;
import com.buyauto.util.pojo.UserSessionInfo;
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
	 * 到消息中心页面
	 * 
	 * @return
	 */
	@RequestMapping("/mainPage")
	@AuthAnno(rules = { AuthBackend.PERSONALMESSAGECENTER })
	public ModelAndView mainPage(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("message/main");
		return modelAndView;
	}

	/**
	 * 获取消息列
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getMegInfo", method = RequestMethod.POST)
	public PageVo<TUsersMsg> getMegInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int rows) {
		// 获取当前登录用户
		UserSessionInfo userLogin = SessionUtil.getUserSessionInfoFromPc(session);
		PageVo<TUsersMsg> tUsersMsgList = messageImpl.messageList(userLogin.getId(), page, rows);
		return tUsersMsgList;
	}

	/**
	 * 到填写个人详细资料页面
	 * 
	 * @param session
	 * @param id
	 * @return
	 */
	@RequestMapping("/personalInfo")
	public ModelAndView personalInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			String id) {
		// 获取当前登录用户
		UserSessionInfo userLogin = SessionUtil.getUserSessionInfoFromPc(session);
		TUsers tUsers = tUsersImpl.getPersonalById(userLogin.getId());
		if (tUsers.getRealLevel() == tUsers.getSysLevel() || tUsers.getRealLevel() != CommonCode.SysLevel.PERSONAL) {
			return new ModelAndView("other/500");
		}
		ModelAndView modelAndView = new ModelAndView("message/personalInfo");
		modelAndView.addObject("msgId", id);
		modelAndView.addObject("tUsers", tUsers);
		return modelAndView;
	}

	/**
	 * @Title: levelUp @Description: 申请个人等级提升 @param @param id @param @param
	 *         msgId @param @param name @param @param cardID @param @param
	 *         cardFile @param @param debitCard @param @param
	 *         debitCardBank @param @param debitCardFile @param @param
	 *         creditCard @param @param creditCardBank @param @param
	 *         creditCardFile    设定文件 @return String    返回类型 @throws
	 */
	@RequestMapping(value = "/levelUp", method = RequestMethod.POST)
	public String levelUp(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam("msgId") String msgId, @RequestParam("name") String name,
			@RequestParam("cardID") String cardID, @RequestParam("cardFile") String cardFile,@RequestParam("cardBFile")String cardBFile,
			@RequestParam("debitCard") String debitCard, @RequestParam("debitCardBank") String debitCardBank,
			@RequestParam("debitCardFile") String debitCardFile, @RequestParam("creditCard") String creditCard,
			@RequestParam("creditCardBank") String creditCardBank,
			@RequestParam("creditCardFile") String creditCardFile) {
		// 获取当前登录用户
		UserSessionInfo userLogin = SessionUtil.getUserSessionInfoFromPc(session);
		TUsers tUsers = tUsersImpl.getPersonalById(userLogin.getId());
		if (tUsers.getRealLevel() == tUsers.getSysLevel() || tUsers.getRealLevel() != CommonCode.SysLevel.PERSONAL) {
			return CommonCode.LevelUpResult.ERRORLEVEL.toString();
		}
		Integer code = messageImpl.doLevelUp(userLogin.getId(), msgId, name, cardID, cardFile,cardBFile, debitCard, debitCardBank,
				debitCardFile, creditCard, creditCardBank, creditCardFile);
		return code.toString();
	}

	@RequestMapping(value = "/levelDown", method = RequestMethod.POST)
	public String levelDown(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		// 获取当前登录用户
		UserSessionInfo userLogin = SessionUtil.getUserSessionInfoFromPc(session);
		TUsers tUsers = tUsersImpl.getPersonalById(userLogin.getId());
		if (tUsers.getRealLevel() != CommonCode.SysLevel.BOSS) {
			return CommonCode.LevelUpResult.ERRORLEVEL.toString();
		}
		tUsersImpl.editUserLevelById(userLogin.getId(), null, CommonCode.SysLevel.SELLER);
		UserSessionInfo userSessionInfo = new UserSessionInfo(userLogin.getId(), userLogin.getuName(), userLogin.getCompanyId(),
				userLogin.getCompanyName(), userLogin.getAccount(), userLogin.getPhone(), userLogin.getEmail(), userLogin.getuStatus(),
				userLogin.getPosition(), userLogin.getJobNumber(),CommonCode.SysLevel.SELLER);
		userSessionInfo = userService.validationRoles(userSessionInfo.getId(), userSessionInfo);
		SessionUtil.addUserSessionInfoIntoSessionFromPc(session, userSessionInfo);
		Integer code = CommonCode.Audit.PASS;
		return code.toString();
	}
	
	/**
	 * 消息已读
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/hasRead", method = RequestMethod.POST)
	public String hasRead(HttpServletRequest request, HttpServletResponse response, @RequestParam("id") String id) {
		Integer code = messageImpl.messageHasRead(id);
		return code.toString();
	}

	/**
	 * 全部消息已读
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/allRead", method = RequestMethod.POST)
	public String allRead(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		// 获取当前登录用户
		UserSessionInfo userLogin = SessionUtil.getUserSessionInfoFromPc(session);
		Integer code = messageImpl.messageAllRead(userLogin.getId());
		return code.toString();
	}

	/**
	 * 查询是否可以升级
	 * 
	 * @param session
	 * @param id
	 * @return
	 */
	@RequestMapping("/checkUpLevel")
	@AuthAnno(rules = { AuthBackend.PERSONALINVITATION })
	public String checkUpLevel(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			String id) {
		// 获取当前登录用户
		UserSessionInfo userLogin = SessionUtil.getUserSessionInfoFromPc(session);
		TUsers tUsers = tUsersImpl.getPersonalById(userLogin.getId());
		Integer code = 0;
		if (tUsers.getRealLevel() == null || tUsers.getSysLevel() == null || tUsers.getRealLevel() >= tUsers.getSysLevel()) {
			code = CommonCode.Audit.WRONGDATA;
		}else if(tUsers.getRealLevel() < tUsers.getSysLevel() && tUsers.getRealLevel() == CommonCode.SysLevel.SELLER){
			tUsersImpl.editUserLevelById(userLogin.getId(), null, CommonCode.SysLevel.BOSS);
			UserSessionInfo userSessionInfo = new UserSessionInfo(userLogin.getId(), userLogin.getuName(), userLogin.getCompanyId(),
					userLogin.getCompanyName(), userLogin.getAccount(), userLogin.getPhone(), userLogin.getEmail(), userLogin.getuStatus(),
					userLogin.getPosition(), userLogin.getJobNumber(),CommonCode.SysLevel.BOSS);
			userSessionInfo = userService.validationRoles(userSessionInfo.getId(), userSessionInfo);
			SessionUtil.addUserSessionInfoIntoSessionFromPc(session, userSessionInfo);
			code = CommonCode.Audit.PASS;
		}else {
			code = messageImpl.checkUpLevel(userLogin.getId());
		}
		if (code == CommonCode.Audit.WRONGDATA || code == CommonCode.Audit.ERROR || code == CommonCode.Audit.PASS) {
			messageImpl.messageHasRead(id);
		}
		return code.toString();
	}
}

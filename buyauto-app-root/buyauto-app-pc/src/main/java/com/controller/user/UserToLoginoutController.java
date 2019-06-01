package com.controller.user;

import java.util.HashMap;
import java.util.Map;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.buyauto.util.method.CommonCode;
import com.buyauto.util.method.IpUtil;
import com.buyauto.util.method.SessionUtil;
import com.buyauto.util.method.StringUtil;
import com.buyauto.util.pojo.UserSessionInfo;
import com.buyauto.util.web.UrlRegulation;
import com.controller.other.OtherController;
import com.external.msg.api.ISendMessageApi;
import com.external.user.api.ITuserApi;

/**
 * 用户类(未登录)
 */
@RestController
@RequestMapping(value = UrlRegulation.SecurityPrefix.HTTP + UrlRegulation.RequestPrefix.REQ_NO_LOGIN + UrlRegulation.BizPrefix.USER)
public class UserToLoginoutController {

	private static final Logger logger = LoggerFactory.getLogger(UserToLoginoutController.class);

	@Qualifier("userService")
	@Autowired
	private ITuserApi userService;

	@Qualifier("sendMessage")
	@Autowired
	private ISendMessageApi sendMessage;

	@Autowired
	private OtherController otherController;

	/**
	 * 到登陆页面
	 */
	@RequestMapping(UrlRegulation.BizPrefix.LOGIN)
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("login/login");
		return modelAndView;
	}

	/**
	 * 到找回密码页面
	 */
	@RequestMapping(value = "/findPassword")
	public ModelAndView findPassword(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("login/find_password");
		return modelAndView;
	}

	/**
	 * 登录
	 * 
	 * @param account
	 *            账号
	 * @param pwd
	 *            密码
	 * @param clientDevice
	 *            登录设备 1PC,2手机,3微信,4ios,5andoird,99未知
	 * @return
	 */
	@RequestMapping(value = "/doLogin", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> doLogin(HttpServletRequest request, HttpServletResponse response, HttpSession session, 
			@RequestParam("account") String account,
			@RequestParam("pwd") String pwd,
			@RequestParam(value = "clientDevice", defaultValue = "1") Integer clientDevice) {
		String ip = IpUtil.getIpAddr(request);
		Map<String, Object> userInfo = userService.login(account, pwd, ip, clientDevice);
		Integer code = (Integer) userInfo.get(CommonCode.UserState.CODE);
		if (code == CommonCode.LoginResult.SUCCESS || code == CommonCode.LoginResult.SUCCESS_JUMP || code == CommonCode.LoginResult.SUCCESS_JUMP_EDITPWD ) {
			UserSessionInfo userSessionInfo = (UserSessionInfo) userInfo.get(CommonCode.UserState.USERINFO);
			userSessionInfo = userService.validationRoles(userSessionInfo.getId(), userSessionInfo);
			SessionUtil.addUserSessionInfoIntoSessionFromPc(session, userSessionInfo);
		}
		Map<String, Object> ret = new HashMap<String, Object>();
		if(code==CommonCode.LoginResult.STATUS_NOTAUDIT){
			//审核不通过原因
			ret.put(CommonCode.LoginState.INFO,userInfo.get(CommonCode.LoginState.INFO));
		}
		ret.put(CommonCode.UserState.CODE,code.toString());
		return ret;
	}

	/**
	 * 退出登录
	 * 
	 * @return
	 */
	@RequestMapping("/logout")
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse res, HttpSession session) {
		SessionUtil.delUserSessionInfoFromPc(session);
		return new ModelAndView("login/login");
	}

	/**
	 * 到经销商/供应商注册页面
	 */
	@RequestMapping("/register")
	public ModelAndView register(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("register/register");
		return modelAndView;
	}

	/**
	 * 经销商\供应商 注册
	 * @param account 账号
	 * @param pwd 密码
	 * @param companyName 经销商名称
	 * @param name 姓名
	 * @param phone 手机
	 * @param email 邮箱
	 * @param file 证件
	 * @param codePhone 手机验证码
	 * @param codeImg 图片验证码
	 * @param address 办公场所
	 * @param type 0:经销商,1:供应商
	 * @return
	 */
	@RequestMapping(value = "/doRegister")
	@ResponseBody
	public String doRegister(HttpServletRequest request, HttpServletResponse response, HttpSession session, 
			@RequestParam("name") String account,
			@RequestParam("password") String pwd,
			@RequestParam("companyName") String companyName,
			@RequestParam("add_name") String name,
			@RequestParam("mobile") String phone,
			@RequestParam("email") String email,
			@RequestParam("file") String file, 
			@RequestParam("code") String codePhone,
			@RequestParam("math") String codeImg,
			@RequestParam("address") String address,
			@RequestParam("userType") Integer type) {
		String codeImgService = (String) SessionUtil.getSessionKey(session, CommonCode.UserState.CODE_IMG_REGISTER);// 图片验证码
		String codePhoneService = (String) SessionUtil.getSessionKey(session, CommonCode.UserState.CODE_PHONE_REGISTER);// 手机验证码
		SessionUtil.addSessionKey(session, CommonCode.UserState.CODE_IMG_REGISTER, "");// 清空图片验证码
		Integer code = userService.registerCompanyUser(account, pwd, companyName, name, phone, email, file, codePhone, codeImg, codeImgService, codePhoneService, address,type);
		if (code.equals(CommonCode.RegisterResult.ALEADY_PHONE)) {
			SessionUtil.addSessionKey(session, CommonCode.UserState.CODE_PHONE_REGISTER, "");// 手机已被注册,清空手机验证码
		}
		return code.toString();
	}

	/**
	 * 生成注册图片验证码(注册)
	 */
	@RequestMapping("/codeRegister")
	public void kaptcha(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		otherController.getKaptchaImage(session, request, response, CommonCode.UserState.CODE_IMG_REGISTER);// 生成验证码
	}

	/**
	 * 发送验证码(注册)
	 */
	@RequestMapping(value = "/phoneCodeRegister", method = RequestMethod.POST)
	public String phoneKaptcha(HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam("phone") String phone,
			@RequestParam("codeImg") String codeImg) throws Exception {
		String code_img_register = SessionUtil.getSessionKey(session, CommonCode.UserState.CODE_IMG_REGISTER);
		// 图片验证码为空,不能发送短信
		if (code_img_register == null || StringUtil.isEmpty(code_img_register)) {
			return CommonCode.RegisterResult.INVALID_IMGCODE.toString();
		}
		if (!codeImg.equals(code_img_register)) {
			return CommonCode.RegisterResult.ERROR_PARAM.toString();
		}
		// 发送手机验证码
		otherController.sendMessage(request, response, session, "Register", phone, CommonCode.CmsType.Infomartion, CommonCode.UserState.CODE_PHONE_REGISTER);
		return CommonCode.RegisterResult.SUCCESS.toString();
	}

	/**
	 * 到经销商找回密码页面
	 */
	@RequestMapping("/forget")
	public ModelAndView forget(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("login/find_password");
		return modelAndView;
	}

	/**
	 * 生成图片验证码(经销商找回密码)
	 */
	@RequestMapping("/codeForget")
	public void codeForget(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws Exception {
		otherController.getKaptchaImage(session, request, response, CommonCode.UserState.CODE_IMG_FORGET);// 生成验证码
	}

	/**
	 * 发送手机验证码(经销商找回密码)
	 * @param phone  手机
	 * @param imgCode	图片验证码
	 */
	@RequestMapping(value = "/phoneCodeForget", method = RequestMethod.POST)
	public String phoneCode(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam("phone") String phone,
			@RequestParam("codeImg") String imgCode) throws Exception {
		String code_img_forget = SessionUtil.getSessionKey(session, CommonCode.UserState.CODE_IMG_FORGET);
		// 图片验证码为空,不能发送短信
		if (code_img_forget == null || StringUtil.isEmpty(code_img_forget)) {
			return CommonCode.RegisterResult.INVALID_IMGCODE.toString();
		}
		//验证图片验证码
		if(!code_img_forget.equals(imgCode)){
			return CommonCode.RegisterResult.FIMG_CODE.toString();
		}
		//验证该手机
		Integer code = userService.checkedPhone(phone);
		if(code.equals(CommonCode.ForgetResult.SUCCESS)){
			// 发送手机验证码	
			otherController.sendMessage(request, response, session, "ForgetPwd", phone, CommonCode.CmsType.Infomartion, CommonCode.UserState.CODE_PHONE_FORGET);
		}
		return code.toString();
	}

	/**
	 * 经销商找回密码
	 * 
	 * @param phone
	 *            手机号
	 * @param codePhone
	 *            手机验证码
	 * @param codeImg
	 *            图片验证码
	 * @return
	 */
	@RequestMapping(value = "/doForget", method = RequestMethod.POST)
	@ResponseBody
	public String doForget(HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam("phone") String phone,
			@RequestParam("codePhone") String codePhone, @RequestParam("codeImg") String codeImg) {
		String codeImgService = SessionUtil.getSessionKey(session, CommonCode.UserState.CODE_IMG_FORGET);// 图片验证码
		String codePhoneService = SessionUtil.getSessionKey(session, CommonCode.UserState.CODE_PHONE_FORGET);// 手机验证码
		SessionUtil.addSessionKey(session, CommonCode.UserState.CODE_IMG_FORGET, "");// 清空图片验证码
		Integer code = userService.forgetUser(phone, codePhone, codeImg, codeImgService, codePhoneService);
		return code.toString();
	}

	/**
	 * 经销商修改密码
	 * 
	 * @param phone
	 *            手机号
	 * @param pwd
	 *            密码
	 * @param codePhone
	 *            手机验证码
	 * @return
	 */
	@RequestMapping(value = "/editPwd", method = RequestMethod.POST)
	@ResponseBody
	public String editPwd(HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam("phone") String phone, @RequestParam("pwd") String pwd,
			@RequestParam("codePhone") String codePhone) {
		String codePhoneService = SessionUtil.getSessionKey(session, CommonCode.UserState.CODE_PHONE_FORGET);// 手机验证码
		Integer code = userService.editPwd(phone, pwd, codePhone, codePhoneService);
		return code.toString();
	}
	
	
	
	/**
	 * 到个人注册页面
	 */
	@RequestMapping("/registerPersonal")
	public ModelAndView registerPersonal(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("register/register_personal");
		return modelAndView;
	}
	
	
	/**
	 * 个人注册
	 * 
	 * @param phone
	 *            手机账号
	 * @param pwd
	 *            密码
	 * @param codePhone
	 *            手机验证码
	 * @param codeImg
	 *            图片验证码
	 * @param recommender
	 *            推荐人手机
	 * @return
	 */
	@RequestMapping(value = "/doRegisterPersonal", method = RequestMethod.POST)
	@ResponseBody
	public String doRegisterPersonal(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam("password") String pwd, @RequestParam("mobile") String phone,
			@RequestParam("code") String codePhone, @RequestParam("math") String codeImg,
			@RequestParam("recommender") String recommender) {
		String codeImgService = (String) SessionUtil.getSessionKey(session, CommonCode.UserState.CODE_IMG_REGISTER);// 图片验证码
		String codePhoneService = (String) SessionUtil.getSessionKey(session, CommonCode.UserState.CODE_PHONE_REGISTER);// 手机验证码
		SessionUtil.addSessionKey(session, CommonCode.UserState.CODE_IMG_REGISTER, "");// 清空图片验证码
		Integer code = userService.registerPersonalUser(phone, pwd, codePhone, codeImg, codeImgService, codePhoneService, recommender);
		if (code.equals(CommonCode.RegisterResult.ALEADY_PHONE)) {
			SessionUtil.addSessionKey(session, CommonCode.UserState.CODE_PHONE_REGISTER, "");// 手机已被注册,清空手机验证码
		}
		return code.toString();
	}

	
}

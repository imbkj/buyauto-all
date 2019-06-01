package com.controller.user;

import java.lang.reflect.Method;
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

import com.business.service.user.api.TUsersApi;
import com.buyauto.dao.orders.TUsersCommissionDao;
import com.buyauto.entity.user.TUsers;
import com.buyauto.entity.user.TUsersRecommender;
import com.buyauto.util.method.CommonCode;
import com.buyauto.util.method.IpUtil;
import com.buyauto.util.method.RedisUtil;
import com.buyauto.util.method.RegularUtil;
import com.buyauto.util.method.StringUtil;
import com.buyauto.util.pojo.PageVo;
import com.buyauto.util.pojo.UserSessionInfo;
import com.buyauto.util.web.AppWebResultVo;
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
	
	@Autowired
	private TUsersApi tUsersImpl;


	
	/**
	 * 用户登录
	 * @param account 账号
	 * @param pwd 密码
	 * @param clientDevice 登录设备 1PC,2手机,3微信,4ios,5andoird,99未知
	 * @return
	 */
	@RequestMapping(value = "/doLogin")
	@ResponseBody
	public AppWebResultVo<Map<String, Object>> doLogin(HttpServletRequest request, HttpServletResponse response, HttpSession session, 
			@RequestParam("account") String account,
			@RequestParam("pwd") String pwd,
			@RequestParam(value = "clientDevice", defaultValue = "2") Integer clientDevice) {
		Map<String, Object> ret = new HashMap<String, Object>();
		String ip = IpUtil.getIpAddr(request);
		Map<String, Object> userInfo = userService.login(account, pwd, ip, clientDevice);
		Integer code = (Integer) userInfo.get(CommonCode.UserState.CODE);
		if (code == CommonCode.LoginResult.SUCCESS || code == CommonCode.LoginResult.SUCCESS_JUMP || code == CommonCode.LoginResult.SUCCESS_JUMP_EDITPWD ) {
			UserSessionInfo userSessionInfo = (UserSessionInfo) userInfo.get(CommonCode.UserState.USERINFO);
			/*只能个人用户登录*/
			if(!CommonCode.UserPosition.PERSONAL.equals(userSessionInfo.getPosition())){
				ret.put(CommonCode.UserState.CODE, CommonCode.LoginResult.ERROR);
				logger.info(account+"用户登录,该用户不属于个人用户,禁止登录");
				return AppWebResultVo.buildSucc(ret);
			}
			Long id = userSessionInfo.getId();
			String key = RedisUtil.setRedis(id.toString());//保存用户ID
			ret.put(CommonCode.UserState.TOKEN,key);//向手机端发送唯一标识
			logger.info("登录成功,向手机端发送唯一TOKEN:"+key+" \n\t\t\t\t-用户ID为:"+id);
		}
		if(code==CommonCode.LoginResult.STATUS_NOTAUDIT){
			//审核不通过原因
			ret.put(CommonCode.LoginState.INFO,userInfo.get(CommonCode.LoginState.INFO));
		}
		ret.put(CommonCode.UserState.CODE,code.toString());
		return AppWebResultVo.buildSucc(ret);
	}
	
	/**
	 * 手机用户(个人)注册
	 * @param phone 手机 
	 * @param pwd 密码
	 * @param code 手机验证码
	 * @param recommend 推荐人手机号
	 * @return
	 */
	@RequestMapping(value = "/register")
	@ResponseBody
	public AppWebResultVo<Map<String,Object>> register(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam(value="phone",required=false) String phone,
			@RequestParam(value="pwd",required=false) String pwd,
			@RequestParam(value="code",required=false) String code,
			@RequestParam(value="recommend",required=false) String recommend){
		String Key = CommonCode.UserState.PHONE+phone;
		String serviceCode = RedisUtil.getRedisByKey(Key);
		Map<String,Object> map = userService.register(phone,pwd,code,recommend,serviceCode);
		Integer codeRet = (Integer) map.get(CommonCode.UserState.CODE);
		if(CommonCode.RegisterResult.SUCCESS.equals(codeRet)){
			String key = RedisUtil.setRedis(map.get(CommonCode.UserState.TOKEN).toString());
			map.put(CommonCode.UserState.TOKEN,key);
		}else if(CommonCode.RegisterResult.ERROR.equals(codeRet)){
			/*系统报错,插入数据失败*/
			map.put(CommonCode.UserState.CODE,CommonCode.RetAppInfo.ERROR);
		}
		return AppWebResultVo.buildSucc(map);
	}
	
	
	/**
	 * 发送短信验证码
	 * @param phone 手机号
	 * @param templ 1:注册验证码  2：找回密码  3：修改密码
	 * @return
	 */
	@RequestMapping(value = "/registerCodeByApp", method = RequestMethod.POST)
	@ResponseBody
	public AppWebResultVo<String> registerCode(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam("phone") String phone,
			@RequestParam(value="type") Integer type){
		if(RegularUtil.isTelephone(phone)){
			String template= CommonCode.Message.TYPESONE;
			if(CommonCode.Message.TYPEONE.equals(type)){
				template = CommonCode.Message.TYPESONE;//注册验证码
			}else{
				/*验证手机是否存在*/
				String code = userService.forgotPwdByPhone(phone);
				if(!StringUtil.isEmpty(code)){
					return AppWebResultVo.buildFail(code);
				}
				if(CommonCode.Message.TYPETWO.equals(type)){
					template = CommonCode.Message.TYPESTWO;//找回密码
				}else if(CommonCode.Message.TYPETHREE.equals(type)){
					template = CommonCode.Message.TYPESTHREE;//修改密码
				}
			}
			String Key = CommonCode.UserState.PHONE+phone;
			otherController.sendMessage(request, response, session, template, phone, CommonCode.CmsType.Infomartion, Key);
			return AppWebResultVo.buildSucc("");
		}
		return AppWebResultVo.buildFail(CommonCode.RetAppInfo.REG_PHONE);
	}
	
	/**
	 * 修改密码
	 * @param oldPwd 旧密码
	 * @param newPwdOne 新密码
	 * @param newPwdTwo 确认新密码
	 * @param token token唯一键
	 * @return
	 */
	@RequestMapping(value = "/findPwd")
	@ResponseBody
	public AppWebResultVo<Map<String,Object>> findPwd(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam("oldPwd") String oldPwd,
			@RequestParam("newPwdOne") String newPwdOne,
			@RequestParam("newPwdTwo") String newPwdTwo,
			@RequestParam("code") String code,
			@RequestParam("token") String token){
		Map<String,Object> map = new HashMap<String, Object>();
		Long userId = RedisUtil.getUserByKey(token);
		if(null==userId){
			map.put(CommonCode.UserState.CODE, CommonCode.RetAppInfo.NUSEROBJ);
			return AppWebResultVo.buildSucc(map);
		}
		TUsers user = tUsersImpl.queryUserById(userId);
		if(null==user){
			map.put(CommonCode.UserState.CODE, CommonCode.RetAppInfo.NUSEROBJ);
			return AppWebResultVo.buildSucc(map);
		}
		String key = CommonCode.UserState.PHONE+user.getPhone();
		String serviceCode = RedisUtil.getRedisByKey(key);
		map = userService.findPwd(oldPwd,newPwdOne,newPwdTwo,code,serviceCode,user);
		return AppWebResultVo.buildSucc(map);
	}
	
	/***
	 * 个人中心
	 * @param token 用户唯一键
	 * @param page 当前页码
	 * @param rows 一页显示条数
	 * @return
	 */
	@RequestMapping(value = "/userCenter")
	@ResponseBody
	public AppWebResultVo<Map<String,Object>> userCenter(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam("token") String token,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int rows){
		Long userId = RedisUtil.getUserByKey(token);
		if(userId==null){
			return AppWebResultVo.buildFail(CommonCode.RetAppInfo.NUSEROBJ);
		}
		Map<String,Object> map = new HashMap<String, Object>();
		TUsers user = userService.getById(userId);
		if(user==null){
			return AppWebResultVo.buildFail(CommonCode.RetAppInfo.NUSEROBJ);
		}
		PageVo<TUsersCommissionDao> list = tUsersImpl.getRackBackList(userId, page, rows);
		/*父级*/
		TUsersRecommender tUsersRecommender = tUsersImpl.getRecommenderByUid(userId);
		if (tUsersRecommender != null && tUsersRecommender.getFatherPhone() != null) {
			map.put("recommPhone", tUsersRecommender.getFatherPhone());/*被xxx邀请*/
		}else{
			map.put("recommPhone", null);
		}
		map.put("name", user.getName());/*用户姓名*/
		map.put("phone", user.getPhone());/*手机*/
		map.put("commission", list);/*返佣信息列表*/
		map.put("realLevel", user.getRealLevel());/*职位*/
		return AppWebResultVo.buildSucc(map);
	}
	
	/**
	 * 安全退出
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/loginout")
	@ResponseBody
	public AppWebResultVo<String> userCenter(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam("token") String token){
		RedisUtil.setRedis(token, "");
		return AppWebResultVo.buildSucc("");
	}
	

	
	/**
	 * 找回密码-1-验证手机号
	 * @param phone
	 * @return
	 */
	@RequestMapping(value = "/forgotPwd")
	@ResponseBody
	public AppWebResultVo<String> forgotPwd(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam("phone") String phone){
		String code = userService.forgotPwdByPhone(phone);
		if(!StringUtil.isEmpty(code)){
			return AppWebResultVo.buildFail(code);
		}
		/*发送短信*/
		this.registerCode( request,  response,  session,phone,CommonCode.Message.TYPETWO);
		return AppWebResultVo.buildSucc("");
	}

	/**
	 * 找回密码-2-修改密码
	 * @param phone 手机
	 * @param pwd 新密码
	 * @param code 手机验证码
	 * @return
	 */
	@RequestMapping(value = "/forgotPwdTwo")
	@ResponseBody
	public AppWebResultVo<String> forgotPwd(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam("phone") String phone,
			@RequestParam("pwd") String pwd,
			@RequestParam("code") String code){
		String key = CommonCode.UserState.PHONE+phone;
		String serviceCode = RedisUtil.getRedisByKey(key);
		String codeRet = userService.forgotPwd(phone,pwd,code,serviceCode);
		if(!StringUtil.isEmpty(codeRet)){
			AppWebResultVo.buildFail(codeRet);
		}
		return AppWebResultVo.buildSucc("");
	}
	
	/**
	 * 注册协议
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/regProtocol")
	public ModelAndView regProtocol(HttpServletRequest request, HttpServletResponse response, HttpSession session){
		return new ModelAndView("/files_cards/agreement");
	}
	
}

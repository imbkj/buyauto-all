package com.controller.user;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.business.service.sys.api.ISysEngineApi;
import com.buyauto.dao.user.TUsersPojo;
import com.buyauto.entity.sys.SysConfig;
import com.buyauto.entity.user.TUsers;
import com.buyauto.util.anno.AuthAnno;
import com.buyauto.util.method.AuthBackend;
import com.buyauto.util.method.CommonCode;
import com.buyauto.util.method.CustomDateEditor;
import com.buyauto.util.method.SessionUtil;
import com.buyauto.util.pojo.PageVo;
import com.buyauto.util.pojo.UserSessionInfo;
import com.buyauto.util.web.UrlRegulation;
import com.external.user.api.ITuserApi;

/**
 * 用户(需登录)
 */
@Controller
@RestController
@RequestMapping(value = UrlRegulation.SecurityPrefix.HTTP + UrlRegulation.RequestPrefix.REQ_LOGIN
		+ UrlRegulation.BizPrefix.USER)
public class UserToLoginController {

	private static final Logger logger = LoggerFactory.getLogger(UserToLoginController.class);

	@Qualifier("userService")
	@Autowired
	private ITuserApi userService;
	@Autowired
	private ISysEngineApi sysEngineApi;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(true));
	}

	/**
	 * 到员工管理页面
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping("/employee")
	@AuthAnno(rules = { AuthBackend.PERSONOPERATION })
	public ModelAndView employee(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("user/accountant_child");
		return modelAndView;
	}

	/***
	 * 获取员工数量(可用员工、可新建账号、冻结账号)
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getAccountState", method = RequestMethod.POST)
	@ResponseBody
	// @AuthAnno(rules = { AuthBackend.PERSONOPERATION })
	public Map<String, Object> getAccountState(HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
		UserSessionInfo user = SessionUtil.getUserSessionInfoFromPc(session);
		Map<String, Object> map = userService.findAccount(user);
		return map;
	}

	/**
	 * 员工列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getUserByPage", method = RequestMethod.POST)
	@ResponseBody
	public PageVo<TUsersPojo> getUserByPage(HttpServletRequest request, HttpServletResponse response,
			HttpSession session, @RequestParam(value = "page", defaultValue = "1") Integer pageNumber,
			@RequestParam(value = "rows", defaultValue = "20") Integer pageSize) {
		UserSessionInfo user = SessionUtil.getUserSessionInfoFromPc(session);
		PageVo<TUsersPojo> tProducts = userService.getUserByPage(pageNumber, pageSize, user.getId());
		return tProducts;
	}

	/**
	 * 冻结或启用
	 * 
	 * @param id
	 * @param state
	 *            1可用,其他数字 冻结
	 * @return
	 */
	@RequestMapping(value = "/updateState", method = RequestMethod.POST)
	@ResponseBody
	public String updateState(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam(value = "id") Long id, @RequestParam(value = "state") Integer state) {
		Long adminId = SessionUtil.getUserSessionInfoFromPc(session).getId();
		Integer code = userService.updateState(id, state, adminId);
		return code.toString();
	}

	/**
	 * 到新建员工页面
	 */
	@RequestMapping("/add")
	public ModelAndView add(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("user/new_employee");
		return modelAndView;
	}

	/**
	 * 新建子员工
	 * 
	 * @param companyId
	 *            经销商ID
	 * @param name
	 *            姓名
	 * @param position
	 *            职位 0管理员,1销售，2财务
	 * @param companyName
	 *            经销商名称
	 * @param account
	 *            账号
	 * @param phone
	 *            手机
	 * @param email
	 *            邮箱
	 * @param pwd
	 *            密码
	 * @param hirdeate
	 *            入职时间
	 * @param jobNumber
	 *            工号
	 * @param region
	 *            所属区域
	 * @return
	 */
	@RequestMapping(value = "addUser", method = RequestMethod.POST)
	@ResponseBody
	public String addUser(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam("companyid") Long companyId, @RequestParam("name") String name,
			@RequestParam("position") Integer position, @RequestParam("companyname") String companyName,
			@RequestParam("account") String account, @RequestParam("phone") String phone,
			@RequestParam(value = "email", required = false) String email, @RequestParam("pwd") String pwd,
			@RequestParam(value = "hirdeate", required = false) Date hirdeate,
			@RequestParam(value = "jobNumber", required = false) String jobNumber,
			@RequestParam(value = "region", required = false) String region) {
		UserSessionInfo user = SessionUtil.getUserSessionInfoFromPc(session);
		Integer code = userService.addUser(companyId, name, position, companyName, account, phone, email, pwd, hirdeate,
				jobNumber, region, user);
		return code.toString();
	}

	/**
	 * 到修改员工页面
	 * 
	 * @param id
	 *            员工ID
	 * @return
	 */
	@RequestMapping("/editUser")
	public ModelAndView editUser(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam("id") Long id) {
		ModelAndView md = new ModelAndView("user/register_infomation");
		md.addObject("id", id);
		return md;
	}

	/**
	 * 查询用户信息
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/getUserById", method = RequestMethod.POST)
	@ResponseBody
	public TUsers getUserById(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam("id") Long id) {
		return userService.getById(id);
	}

	/**
	 * 修改子员工
	 * 
	 * @param id
	 *            员工ID
	 * @param name
	 *            姓名
	 * @param position
	 *            职位 0管理员,1销售，2财务
	 * @param pwd
	 *            密码
	 * @param hirdeate
	 *            入职时间
	 * @param jobNumber
	 *            工号
	 * @param region
	 *            所属区域
	 * @return
	 */
	@RequestMapping(value = "editUser", method = RequestMethod.POST)
	@ResponseBody
	public String editUser(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam("id") Long id, @RequestParam("name") String name,
			@RequestParam(value = "power", required = false) Integer position,
			@RequestParam(value = "password", required = false) String pwd,
			@RequestParam(value = "hirdeate", required = false) Date hirdeate,
			@RequestParam(value = "leadername", required = false) String jobNumber,
			@RequestParam(value = "inform", required = false) String region,
			@RequestParam(value = "email", required = false) String email) {
		UserSessionInfo user = SessionUtil.getUserSessionInfoFromPc(session);
		Integer code = userService.editUser(id, name, position, pwd, hirdeate, jobNumber, region, user, email);
		return code.toString();
	}

	/**
	 * 经销商首页
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/leaderIndex", method = RequestMethod.GET)
	@AuthAnno(rules = { AuthBackend.DISTRIBUTOROPERATION })
	public ModelAndView leaderIndex(HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("user/leader_index");
		UserSessionInfo user = SessionUtil.getUserSessionInfoFromPc(session);
		Map<String, Object> indexData = userService.queryLeaderIndexData(user.getId());
		modelAndView.addObject("indexData", indexData);
		return modelAndView;
	}
	
	

	/**
	 * 到强制修改密码页面
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/toResetPwd", method = RequestMethod.GET)
	public ModelAndView toResetPwd(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("login/reset_password");
		return modelAndView;
	}

	/**
	 * 强制修改密码
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/resetPwd", method = RequestMethod.POST)
	@ResponseBody
	public String resetPwd(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam(value = "password") String pwd) {
		UserSessionInfo user = SessionUtil.getUserSessionInfoFromPc(session);
		Integer code = userService.restPwd(user, pwd);
		return code.toString();
	}

	/**
	 * 到我的等级页面
	 * 
	 * @return
	 */
	@RequestMapping("/grade")
	@AuthAnno(rules = { AuthBackend.PERSONALGRADE })
	public ModelAndView grade(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		UserSessionInfo user = SessionUtil.getUserSessionInfoFromPc(session);
		Map<String, String>upgradeCondition= sysEngineApi.queryUpgradeCondition();
		Integer salesVolume= userService.querySalesVolume(user.getId());
//		SysConfig upgradeConfig = sysEngineApi.queryUpgradeConfig(CommonCode.DowngradeOrUpgrade.UPGRADE);
//		SysConfig downgradeSysConfig = sysEngineApi.queryUpgradeConfig(CommonCode.DowngradeOrUpgrade.DOWNGRADE);
		ModelAndView modelAndView = new ModelAndView("personal/grade");
		
		modelAndView.addObject("salesVolume", salesVolume);
		modelAndView.addObject("upgradeCondition", upgradeCondition);
//		modelAndView.addObject("upgradeConfig", upgradeConfig);
//		modelAndView.addObject("downgradeSysConfig", downgradeSysConfig);
		return modelAndView;
	}
}

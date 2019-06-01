package com.controller.sys;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.buyauto.dao.sys.SysRuleUserDao;
import com.buyauto.dao.user.TUsersRecommenderDao;
import com.buyauto.entity.sys.EasyUiTree;
import com.buyauto.entity.sys.SysRuleRole;
import com.buyauto.entity.sys.SysRuleUser;
import com.buyauto.mapper.sys.SysRuleRoleMapper;
import com.buyauto.mapper.sys.SysRuleUserMapper;
import com.buyauto.util.anno.AuthAnno;
import com.buyauto.util.method.AuthBackend;
import com.buyauto.util.method.CommonCode;
import com.buyauto.util.method.CustomDateEditor;
import com.buyauto.util.method.KeyUtil;
import com.buyauto.util.method.MD5Util;
import com.buyauto.util.method.StringUtil;
import com.buyauto.util.pojo.PageVo;
import com.buyauto.util.pojo.UserSessionInfoBackend;
import com.buyauto.util.web.UrlRegulation;
import com.google.common.collect.Lists;

@Controller
@RestController
@RequestMapping(UrlRegulation.SecurityPrefix.HTTP + UrlRegulation.RequestPrefix.REQ_LOGIN
		+ UrlRegulation.PrefixBackendBusiness.OPER)
public class SysOperationController {

	@Autowired
	private ISysEngineApi sysEngineApi;

	@Autowired
	private SysRuleUserMapper sysUserMapper;

	@Autowired
	private SysRuleRoleMapper ruleRoleMapper;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(true));
	}

	/**
	 * 前往用户管理
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "toOperation", method = RequestMethod.GET)
	@AuthAnno(rules = { AuthBackend.USERMANAGEMENT })
	public ModelAndView toOperation(HttpServletRequest request, HttpServletResponse response) {

		return new ModelAndView("user/operation");
	}

	/**
	 * 前往角色管理
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "toRole", method = RequestMethod.GET)
	@AuthAnno(rules = { AuthBackend.ROLEMANAGEMENT })
	public ModelAndView toRole(HttpServletRequest request, HttpServletResponse response) {

		return new ModelAndView("user/role");
	}

	@RequestMapping(value = "/roles", method = RequestMethod.GET)
	@ResponseBody
	public PageVo<SysRuleRole> roleList(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int rows) {
		return sysEngineApi.queryRole(page, rows);
	}

	@RequestMapping(value = "/role/view", method = RequestMethod.GET)
	public ModelAndView roleToView() {
		return new ModelAndView("sys/role.operation");
	}

	@RequestMapping(value = "/change/mypwd", method = RequestMethod.POST)
	@ResponseBody
	public Boolean changePwd(HttpServletRequest request, @RequestParam String pwd) {

		UserSessionInfoBackend userSessionInfo = (UserSessionInfoBackend) request.getSession().getAttribute(
				UserSessionInfoBackend.SessionKey);

		return true;
	}

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	@ResponseBody
	public PageVo<SysRuleUserDao> userList(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int rows) {
		return sysEngineApi.queryRuleUserDao(page, rows);
	}

	@RequestMapping(value = "/user", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> user(@RequestParam Long id) {
		return sysEngineApi.selectUserById(id);
	}

	@RequestMapping(value = "/roles/all/enable", method = RequestMethod.GET)
	@ResponseBody
	public List<SysRuleRole> rolesEnable() {
		return sysEngineApi.selectRolePage(0, 10000, CommonCode.YesOrNo.Yes).getRows();
	}

	@RequestMapping(value = "/edit/user", method = RequestMethod.POST)
	public String editUser(HttpServletRequest request, SysRuleUser user) {

		String msg = "SUCCESS";
		SysRuleUser ruleUser = sysEngineApi.selectUserByname(user.getUserName());
		if (null == ruleUser || ruleUser.getId().equals(user.getId())) {
			user.setUpdateTime(new Date());
			if ("" != user.getUserPwd() && null != user.getUserPwd()) {
				user.setUserPwd(MD5Util.encryption(user.getUserPwd()));
			}
			sysEngineApi.updateRuleUser(user);
		} else {
			msg = "用户名已经存在";
		}
		return msg;
	}

	@RequestMapping(value = "/create/user", method = RequestMethod.POST)
	public String saveUser(HttpServletRequest request, SysRuleUser user) {
		String msg = "SUCCESS";
		SysRuleUser ruleUser = sysEngineApi.selectUserByname(user.getUserName());
		if (null == ruleUser) {
			user.setId(KeyUtil.generateDBKey());
			user.setUserPwd(MD5Util.encryption(user.getUserPwd()));
			sysEngineApi.insertRuleUser(user);
		} else {
			msg = "用户名已经存在";
		}
		return msg;
	}

	@RequestMapping(value = "/edit/role", method = RequestMethod.POST)
	public String editRole(HttpServletRequest request, HttpSession session, SysRuleRole role) {

		String msg = "SUCCESS";
		SysRuleRole ruleRole = sysEngineApi.selectRoleByname(role.getRoleName());
		if (null == ruleRole || ruleRole.getId().equals(role.getId())) {
			UserSessionInfoBackend sessionBackend = (UserSessionInfoBackend) session
					.getAttribute(UserSessionInfoBackend.SessionKey);
			role.setUpdateTime(new Date());
			role.setUpdateUser(sessionBackend.getId());
			sysEngineApi.updateRuleRole(role);
		} else {
			msg = "角色名称已经存在";
		}
		return msg;
	}

	@RequestMapping(value = "/create/role", method = RequestMethod.POST)
	public String saveRole(HttpServletRequest request, HttpSession session, SysRuleRole role) {
		String msg = "SUCCESS";
		SysRuleRole ruleRole = sysEngineApi.selectRoleByname(role.getRoleName());
		if (null == ruleRole) {
			UserSessionInfoBackend sessionBackend = (UserSessionInfoBackend) session
					.getAttribute(UserSessionInfoBackend.SessionKey);
			role.setId(KeyUtil.generateDBKey());
			role.setCreateUser(sessionBackend.getId());
			sysEngineApi.insertRuleRole(role);
		} else {
			msg = "角色名称已经存在";
		}
		return msg;
	}

	@RequestMapping(value = "/user/status/enable", method = RequestMethod.GET)
	public String userStatusDnable(HttpServletRequest request, @RequestParam Long id) {

		String msg = "SUCCESS";
		SysRuleUser ruleUser = sysUserMapper.selectByPrimaryKey(id);
		if (null == ruleUser) {
			msg = "该用户已经被删除!";
		} else {
			ruleUser.setIsTrue(CommonCode.YesOrNo.Yes);
			sysEngineApi.updateRuleUser(ruleUser);
		}

		return msg;
	}

	@RequestMapping(value = "/user/status/disable", method = RequestMethod.GET)
	public String roleStatusDisable(HttpServletRequest request, @RequestParam Long id) {

		String msg = "SUCCESS";
		SysRuleUser ruleUser = sysUserMapper.selectByPrimaryKey(id);
		if (null == ruleUser) {
			msg = "该用户已经被删除!";
		} else {
			ruleUser.setIsTrue(CommonCode.YesOrNo.No);
			sysEngineApi.updateRuleUser(ruleUser);
		}

		return msg;
	}

	@RequestMapping(value = "/role/status/disable", method = RequestMethod.GET)
	public String roleStatusDisable(HttpServletRequest request, HttpSession session, @RequestParam Long id) {

		String msg = "SUCCESS";
		SysRuleRole ruleRole = ruleRoleMapper.selectByPrimaryKey(id);
		if (null == ruleRole) {
			msg = "该角色已经被删除!";
		} else {
			UserSessionInfoBackend sessionBackend = (UserSessionInfoBackend) session
					.getAttribute(UserSessionInfoBackend.SessionKey);
			ruleRole.setIsEnable((short) CommonCode.YesOrNo.No);
			ruleRole.setUpdateTime(new Date());
			ruleRole.setUpdateUser(sessionBackend.getId());
			sysEngineApi.updateRuleRole(ruleRole);
		}

		return msg;
	}

	@RequestMapping(value = "/role/status/enable", method = RequestMethod.GET)
	public String roleStatusDnable(HttpServletRequest request, HttpSession session, @RequestParam Long id) {

		String msg = "SUCCESS";
		SysRuleRole ruleRole = ruleRoleMapper.selectByPrimaryKey(id);
		if (null == ruleRole) {
			msg = "该角色已经被删除!";
		} else {
			UserSessionInfoBackend sessionBackend = (UserSessionInfoBackend) session
					.getAttribute(UserSessionInfoBackend.SessionKey);
			ruleRole.setIsEnable((short) CommonCode.YesOrNo.Yes);
			ruleRole.setUpdateTime(new Date());
			ruleRole.setUpdateUser(sessionBackend.getId());
			sysEngineApi.updateRuleRole(ruleRole);
		}

		return msg;
	}

	@RequestMapping(value = "/role", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> role(@RequestParam Long id) {
		return sysEngineApi.selectRoleById(id);
	}

	@RequestMapping(value = "/roleRules", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> rules(@RequestParam Long id) {
		return sysEngineApi.selectRulesByRoleId(id);
	}

	@RequestMapping(value = "/findRole", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> findRole(@RequestParam Long id) {
		return sysEngineApi.findRole(id);
	}

	@RequestMapping(value = "/modules", method = RequestMethod.GET)
	@ResponseBody
	public List<EasyUiTree> modules() {
		List<EasyUiTree> trees = Lists.newArrayList();
		trees.add(sysEngineApi.selectAllModules());
		return trees;
	}

	@RequestMapping(value = "/set/role", method = RequestMethod.POST)
	public String setRole(HttpServletRequest request, @RequestParam("id") Long id,
			@RequestParam(required = false, defaultValue = "") String rules) {

		String msg = "SUCCESS";
		SysRuleRole ruleRole = ruleRoleMapper.selectByPrimaryKey(id);
		if (null == ruleRole) {
			msg = "该角色已经被删除!";
		} else {
			String[] split = rules.split(",");
			List<Long> ids = new ArrayList<Long>();
			for (int i = 0; i < split.length; i++) {
				if (!StringUtil.isEmpty(split[i]))
					ids.add(Long.valueOf(split[i]));
			}
			sysEngineApi.createRulePrivilege(id, ids);
		}

		return msg;
	}

	@RequestMapping(value = "/roleModules", method = RequestMethod.GET)
	@ResponseBody
	public List<SysRuleRole> roleModules(@RequestParam int isEnable) {
		return sysEngineApi.findAllEnableRole(isEnable);
	}

	@RequestMapping(value = "/set/userRole", method = RequestMethod.POST)
	public String userRole(HttpServletRequest request, HttpSession session, @RequestParam("userId") Long userId,
			@RequestParam("roleId") Long roleId) {
		String msg = "SUCCESS";
		SysRuleRole ruleRole = ruleRoleMapper.selectByPrimaryKey(roleId);
		if (null == ruleRole) {
			msg = "该角色已经被删除!";
		} else {
			UserSessionInfoBackend sessionBackend = (UserSessionInfoBackend) session
					.getAttribute(UserSessionInfoBackend.SessionKey);
			sysEngineApi.insertUserRole(userId, roleId, sessionBackend.getId());
		}

		return msg;
	}
	
	/**
	 * 前往个人销售上下线统计
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/toCountSales", method = RequestMethod.GET)
//	@AuthAnno(rules = { AuthBackend.USERMANAGEMENT })
	public ModelAndView toCountSales(HttpServletRequest request, HttpServletResponse response) {
		
		return new ModelAndView("user/countSales");
	}
	
	/**
	 * 分页条件查询个人审核列表
	 * 
	 * @param page
	 *            页码
	 * @param rows
	 *            一页条数
	 * @param strStartDate
	 *            开始时间
	 * @param strEndDate
	 *            结束时间
	 * @param status
	 *            0待审核,1可用,2审核不通过,3冻结
	 * @return
	 */
	@RequestMapping(value = "/recommderList", method = RequestMethod.GET)
	@ResponseBody
	public PageVo<TUsersRecommenderDao> personalList(HttpServletRequest request, HttpSession session, HttpServletResponse response,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int rows,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "strStartDate", required = false) String strStartDate,
			@RequestParam(value = "strEndDate", required = false) String strEndDate,
			@RequestParam(value = "type", required = false,defaultValue = "0") int type) {
		return sysEngineApi.recommderList(name,phone,page, rows, type, strStartDate, strEndDate);
	}
}

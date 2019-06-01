package com.business.service.sys.impl;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.business.service.sys.api.ISysEngineApi;
import com.buyauto.dao.sys.SysConfigPojo;
import com.buyauto.dao.sys.SysConfigUpgradeDao;
import com.buyauto.dao.sys.SysRuleUserDao;
import com.buyauto.dao.user.TUsersRecommenderDao;
import com.buyauto.entity.sys.EasyUiTree;
import com.buyauto.entity.sys.SysConfig;
import com.buyauto.entity.sys.SysRuleMoudle;
import com.buyauto.entity.sys.SysRuleOperation;
import com.buyauto.entity.sys.SysRulePrivilege;
import com.buyauto.entity.sys.SysRuleRole;
import com.buyauto.entity.sys.SysRuleUser;
import com.buyauto.entity.sys.SysRuleUserrole;
import com.buyauto.entity.user.TUsers;
import com.buyauto.entity.user.TUsersRecommender;
import com.buyauto.mapper.sys.SysConfigMapper;
import com.buyauto.mapper.sys.SysRuleMoudleMapper;
import com.buyauto.mapper.sys.SysRuleOperationMapper;
import com.buyauto.mapper.sys.SysRulePrivilegeMapper;
import com.buyauto.mapper.sys.SysRuleRoleMapper;
import com.buyauto.mapper.sys.SysRuleUserDaoMapper;
import com.buyauto.mapper.sys.SysRuleUserMapper;
import com.buyauto.mapper.sys.SysRuleUserroleMapper;
import com.buyauto.mapper.user.TUsersRecommenderMapper;
import com.buyauto.util.core.tool.web.GsonUtil;
import com.buyauto.util.method.CommonCode;
import com.buyauto.util.method.KeyUtil;
import com.buyauto.util.method.MD5Util;
import com.buyauto.util.method.SessionUtil;
import com.buyauto.util.pojo.PageVo;
import com.buyauto.util.pojo.UserSessionInfoBackend;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

@Service
public class SysEngine implements ISysEngineApi {
	@Autowired
	private SysRuleUserMapper sysUserMapper;

	@Autowired
	private SysRuleUserDaoMapper sysUserDaoMapper;

	@Autowired
	private SysRulePrivilegeMapper rulePrivilegeMapper;

	@Autowired
	private SysRuleRoleMapper ruleRoleMapper;

	@Autowired
	private SysRuleUserroleMapper userroleMapper;
	@Autowired
	private SysRuleMoudleMapper sysRuleMoudleMapper;
	@Autowired
	private SysRuleOperationMapper sysRuleOperationMapper;
	@Autowired
	private SysConfigMapper sysConfigMapper;
	@Autowired
	private TUsersRecommenderMapper tUsersRecommderMapper;

	@Override
	public int backendUserLogin(String userName, String userPwd, HttpSession session) {
		SysRuleUser ruleUser = sysUserMapper.queryUserByName(userName);
		String pwd = MD5Util.encryption(userPwd);
		if (ruleUser == null || !pwd.equals(ruleUser.getUserPwd())) {

			return CommonCode.SysRuleLogin.FAIL_USER_PWD;
		} else if (ruleUser.getIsTrue() != CommonCode.YesOrNo.Yes) {
			return CommonCode.SysRuleLogin.FAIL_IS_FALSE;
		}
		// 查询用户角色ID,是否分配角色
		SysRuleUserrole userrole = userroleMapper.selectByUserId(ruleUser.getId());
		if (userrole == null) {
			return CommonCode.SysRuleLogin.FAIL_NO_ROLE;
		}
		// 查询用户角色信息是否禁用
		SysRuleRole role = ruleRoleMapper.selectByPrimaryKey(userrole.getRoleId());
		if (role.getIsEnable() != CommonCode.YesOrNo.Yes) {
			return CommonCode.SysRuleLogin.FAIL_ROLE_FALSE;
		} else {
			UserSessionInfoBackend sessionBackend = new UserSessionInfoBackend();
			sessionBackend.setId(ruleUser.getId());
			sessionBackend.setName(ruleUser.getRealName());
			sessionBackend.setLastLoginTime(new Date());

			/* 查询角色权限 */
			List<Long> privileges = rulePrivilegeMapper.findRuleByRoleId(role.getId());

			Set<Long> privilegesSet = new HashSet<Long>();
			privilegesSet.addAll(privileges);
			if (privilegesSet.size() <= 0) {
				return CommonCode.SysRuleLogin.FAIL_NO_RULE;
			}
			sessionBackend.setAuthorities(privilegesSet);
			SessionUtil.addUserSessionInfoIntoSessionFromBackend(session, sessionBackend);
			return CommonCode.SysRuleLogin.SUCCESS;
		}

	}

	@Override
	public PageVo<SysRuleRole> queryRole(int pageNumber, int pageSize) {
		int roleCount = ruleRoleMapper.queryRoleCount(CommonCode.YesOrNo.Yes);
		PageVo<SysRuleRole> pageVo = new PageVo<SysRuleRole>(0, pageNumber, pageSize);
		List<SysRuleRole> roleList = ruleRoleMapper.queryRole(0, pageVo.getPageStartNumber(),
				pageVo.getPageEndNumber());
		for (int i = 0; i < roleList.size(); i++) {
			if (roleList.get(i).getRoleName().equals(CommonCode.DistributorRole.DISTRIBUTOR)) {
				roleList.remove(i);
				roleCount--;
				break;
			}
		}
		for (int i = 0; i < roleList.size(); i++) {
			if (roleList.get(i).getRoleName().equals(CommonCode.DistributorRole.TREASURER)) {
				roleList.remove(i);
				roleCount--;
				break;
			}
		}
		for (int i = 0; i < roleList.size(); i++) {
			if (roleList.get(i).getRoleName().equals(CommonCode.DistributorRole.SUPPLIER)) {
				roleList.remove(i);
				roleCount--;
				break;
			}
		}
		for (int i = 0; i < roleList.size(); i++) {
			if (roleList.get(i).getRoleName().equals(CommonCode.DistributorRole.PERSONAL)) {
				roleList.remove(i);
				roleCount--;
				break;
			}
		}
		for (int i = 0; i < roleList.size(); i++) {
			if (roleList.get(i).getRoleName().equals(CommonCode.DistributorRole.SALESPERSON)) {
				roleList.remove(i);
				roleCount--;
				break;
			}
		}

		pageVo.setTotal(roleCount);
		pageVo.setRows(roleList);
		return pageVo;
	}

	@Override
	public PageVo<SysRuleUser> queryRuleUser(int pageNumber, int pageSize) {

		PageVo<SysRuleUser> pageVo = new PageVo<SysRuleUser>(0, pageNumber, pageSize);
		List<SysRuleUser> roleList = sysUserMapper.queryRuleUser(null, pageVo.getPageStartNumber(),
				pageVo.getPageEndNumber());
		int roleCount = ruleRoleMapper.queryRoleCount(CommonCode.YesOrNo.Yes);
		pageVo.setTotal(roleCount);
		pageVo.setRows(roleList);
		return pageVo;
	}

	Gson gson = new Gson();

	@Override
	public Map<String, Object> selectUserById(Long id) {

		SysRuleUser ruleUser = sysUserMapper.selectByPrimaryKey(id);
		List<Long> roles = userroleMapper.selectByUid(id);
		Map<String, Object> map = GsonUtil.fronJson2Map(gson.toJson(ruleUser));
		if (map != null) {
			map.put("roleId", roles);
		}
		map.put("password", "");
		return map;
	}

	@Override
	public PageVo<SysRuleRole> selectRolePage(Integer startIndex, Integer endIndex, Integer isEnable) {
		PageVo<SysRuleRole> roles = new PageVo<SysRuleRole>(0, 0, 0);
		roles.setRows(ruleRoleMapper.queryRole(isEnable, startIndex, endIndex));
		roles.setTotal(ruleRoleMapper.queryRoleCount(isEnable));
		return roles;
	}

	@Override
	public SysRuleUser selectUserByname(String userName) {

		return sysUserMapper.queryUserByName(userName);
	}

	@Override
	public Integer updateRuleUser(SysRuleUser user) {
		return sysUserMapper.updateByPrimaryKeySelective(user);
	}

	@Override
	public Integer insertRuleUser(SysRuleUser user) {
		return sysUserMapper.insert(user);
	}

	@Override
	public Map<String, Object> selectRoleById(Long id) {
		SysRuleUser user = sysUserMapper.selectByPrimaryKey(id);
		List<Long> rules = convertPrivilegesToRuleIdArray(rulePrivilegeMapper.selectPrivilege(1, id, 2));
		Map<String, Object> map = GsonUtil.fronJson2Map(gson.toJson(user));
		if (map != null) {
			map.put("rules", gson.toJson(rules));
		}
		return map;
	}

	private List<Long> convertPrivilegesToRuleIdArray(List<SysRulePrivilege> list) {
		List<Long> ids = new ArrayList<Long>();
		if (list != null) {
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				SysRulePrivilege sysRulePrivilege = (SysRulePrivilege) iterator.next();
				ids.add(sysRulePrivilege.getRuleId());
			}
		}
		return ids;
	}

	@Override
	public EasyUiTree selectAllModules() {
		EasyUiTree root = new EasyUiTree();
		root.setText("全部");
		queryMoudles(root);
		return root;
	}

	public void queryMoudles(EasyUiTree root) {
		List<SysRuleMoudle> moudles = sysRuleMoudleMapper.queryListByFather(root.getId());
		// 删除第一个经销商管理

		for (int i = 0; i < moudles.size(); i++) {
			if (moudles.get(i).getMoudleName().equals(CommonCode.HidderTree.DISTRIBUTOR)) {
				moudles.remove(i);
				break;
			}
		}
		
		
		for (int i = 0; i < moudles.size(); i++) {
			if (moudles.get(i).getMoudleName().equals(CommonCode.HidderTree.PERSONAL)) {
				moudles.remove(i);
				break;
			}
		}
		
		for (int i = 0; i < moudles.size(); i++) {
			if (moudles.get(i).getMoudleName().equals(CommonCode.HidderTree.SUPPLIER)) {
				moudles.remove(i);
				break;
			}
		}

		List<EasyUiTree> childrens = new ArrayList<EasyUiTree>();
		for (Iterator iterator = moudles.iterator(); iterator.hasNext();) {
			SysRuleMoudle sysRuleMoudle = (SysRuleMoudle) iterator.next();
			EasyUiTree tree = new EasyUiTree();
			tree.setId(sysRuleMoudle.getId());
			tree.setText(sysRuleMoudle.getMoudleName());
			childrens.add(tree);
			queryMoudles(tree);
		}
		if (root.getId() != null) {
			List<SysRuleOperation> operationsByModule = sysRuleOperationMapper
					.selectAllOperationsByModule(root.getId());
			for (Iterator iterator = operationsByModule.iterator(); iterator.hasNext();) {
				SysRuleOperation sysRuleOperation = (SysRuleOperation) iterator.next();
				EasyUiTree tree = new EasyUiTree();
				tree.setId(sysRuleOperation.getId());
				tree.setText(sysRuleOperation.getOperName());
				tree.setType("oper");
				childrens.add(tree);
			}
		}
		root.setChildren(childrens);
	}

	@Override
	public boolean createRulePrivilege(Long userId, List<Long> rules) {
		rulePrivilegeMapper.deletePrivilegeWithRuleId(userId);
		for (Long rule : rules) {
			SysRulePrivilege privilege = new SysRulePrivilege();
			privilege.setId(KeyUtil.generateDBKey());
			privilege.setPrivilegeMaster(2); //
			privilege.setPrivilegeId(userId);
			privilege.setCreateTime(new Date());
			privilege.setIsEnable(new Short("1"));
			privilege.setRuleType(2);
			privilege.setRuleId(rule);

			rulePrivilegeMapper.insert(privilege);
		}
		return true;
	}

	@Override
	public SysRuleRole selectRoleByname(String roleName) {
		SysRuleRole role = ruleRoleMapper.queryRoleByName(roleName);
		return role;
	}

	@Override
	public Integer insertRuleRole(SysRuleRole role) {
		return ruleRoleMapper.insertSelective(role);
	}

	@Override
	public Integer updateRuleRole(SysRuleRole role) {
		return ruleRoleMapper.updateByPrimaryKeySelective(role);
	}

	@Override
	public Map<String, Object> findRole(Long id) {
		SysRuleRole role = ruleRoleMapper.selectByPrimaryKey(id);
		Map<String, Object> map = GsonUtil.fronJson2Map(gson.toJson(role));
		return map;
	}

	@Override
	public Map<String, Object> selectRulesByRoleId(Long id) {
		SysRuleRole role = ruleRoleMapper.selectByPrimaryKey(id);
		List<Long> rules = convertPrivilegesToRuleIdArray(rulePrivilegeMapper.selectPrivilege(2, id, 2));
		Map<String, Object> map = GsonUtil.fronJson2Map(gson.toJson(role));
		if (map != null) {
			map.put("rules", gson.toJson(rules));
		}
		return map;
	}

	@Override
	public List<SysRuleRole> findAllEnableRole(int isEnable) {
		List<SysRuleRole> findAllRole = ruleRoleMapper.findAllRole(isEnable);
		for (int i = 0; i < findAllRole.size(); i++) {
			if (findAllRole.get(i).getRoleName().equals(CommonCode.DistributorRole.DISTRIBUTOR)) {
				findAllRole.remove(i);
				break;
			}

		}
		for (int i = 0; i < findAllRole.size(); i++) {
			if (findAllRole.get(i).getRoleName().equals(CommonCode.DistributorRole.TREASURER)) {
				findAllRole.remove(i);
				break;
			}

		}
		for (int i = 0; i < findAllRole.size(); i++) {
			if (findAllRole.get(i).getRoleName().equals(CommonCode.DistributorRole.SALESPERSON)) {
				findAllRole.remove(i);
				break;
			}

		}
		return findAllRole;
	}

	@Override
	public Integer insertUserRole(Long userId, Long roleId, Long createUser) {
		userroleMapper.deleteByUserId(userId);
		SysRuleUserrole userrole = new SysRuleUserrole();
		userrole.setId(KeyUtil.generateDBKey());
		userrole.setCreateTime(new Date());
		userrole.setCreateUser(createUser);
		userrole.setRoleId(roleId);
		userrole.setUserId(userId);
		return userroleMapper.insertSelective(userrole);
	}

	@Override
	public PageVo<SysRuleUserDao> queryRuleUserDao(int pageNumber, int pageSize) {
		PageVo<SysRuleUserDao> pageVo = new PageVo<SysRuleUserDao>(0, pageNumber, pageSize);
		List<SysRuleUserDao> roleList = sysUserDaoMapper.queryRuleUser(null, pageVo.getPageStartNumber(),
				pageVo.getPageEndNumber());
		int roleCount = ruleRoleMapper.queryRoleCount(CommonCode.YesOrNo.Yes);
		pageVo.setTotal(roleCount);
		pageVo.setRows(roleList);
		return pageVo;
	}

	@Override
	public PageVo<SysConfig> queryaddressList(String scvalue, Integer state, Integer pageNumber, Integer pageSize,
			String strStartDate, String strEndDate, String extractAddress) {

		Date startTime = null;
		Date endTime = null;
		if (strStartDate != null && strEndDate != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			try {
				startTime = sdf.parse(strStartDate);
				endTime = sdf.parse(strEndDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		PageVo<SysConfig> pageVo = new PageVo<SysConfig>(0, pageNumber, pageSize);
		int configCount = sysConfigMapper.queryaddressListCount(scvalue, state, startTime, endTime, extractAddress);
		List<SysConfig> configList = sysConfigMapper.queryaddressList(scvalue, state, pageVo.getPageStartNumber(),
				pageVo.getPageEndNumber(), startTime, endTime, extractAddress);
		pageVo.setRows(configList);
		pageVo.setTotal(configCount);
		return pageVo;
	}

	@Override
	public Integer addAddress(SysConfig sysConfig, String extractAddress, int configType) {
		Date now = new Date();
		Boolean isNext = true;
		Integer sameCount = sysConfigMapper.querySame(sysConfig.getScValue());
		if (sysConfig.getId() != null) {
			if (sameCount > 1) {
				return CommonCode.feedback.REPEAT;
			}
			// 修改
			SysConfig updataAdderss = sysConfigMapper.selectByPrimaryKey(sysConfig.getId());
			updataAdderss.setScRemark(sysConfig.getScRemark());
			updataAdderss.setScValue(sysConfig.getScValue());
			isNext = sysConfigMapper.updateByPrimaryKeySelective(updataAdderss) == CommonCode.DBSuccess.Success
					&& isNext;
		} else {
			// 确认是否有重名

			if (sameCount > 0) {
				return CommonCode.feedback.REPEAT;
			}
			SysConfig adderss = new SysConfig();
			adderss.setId(KeyUtil.generateDBKey());
			adderss.setGroupName(extractAddress);
			adderss.setScValue(sysConfig.getScValue());
			adderss.setScRemark(sysConfig.getScRemark());
			if (extractAddress == CommonCode.ConfigGroup.EXTRACT_ADDRESS) {
				adderss.setState(CommonCode.ConfigState.ENABLE);
			} else {
				adderss.setState(CommonCode.ConfigState.DISABLE);
			}
			adderss.setState(CommonCode.ConfigState.DISABLE);
			adderss.setCreateTime(now);
			adderss.setScType(configType);
			isNext = sysConfigMapper.insertSelective(adderss) == CommonCode.DBSuccess.Success && isNext;
		}
		if (isNext) {
			return CommonCode.feedback.SUCCESS;
		}
		return CommonCode.feedback.FAIL;
	}

	@Override
	public SysConfigPojo showAddress(Long id) {
		SysConfigPojo configPojo = new SysConfigPojo();
		SysConfig sysConfig = sysConfigMapper.selectByPrimaryKey(id);
		try {
			BeanUtils.copyProperties(configPojo, sysConfig);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return configPojo;
	}

	@Override
	public Boolean updataDisable(Long id) {
		SysConfig sysConfig = sysConfigMapper.selectByPrimaryKey(id);
		sysConfig.setState(CommonCode.ConfigState.DISABLE);
		return sysConfigMapper.updateByPrimaryKeySelective(sysConfig) == CommonCode.DBSuccess.Success;
	}

	@Override
	public Boolean updataEnable(Long id) {
		SysConfig sysConfig = sysConfigMapper.selectByPrimaryKey(id);
		sysConfig.setState(CommonCode.ConfigState.ENABLE);
		return sysConfigMapper.updateByPrimaryKeySelective(sysConfig) == CommonCode.DBSuccess.Success;
	}

	@Override
	public Boolean updataDelete(Long id) {
		return sysConfigMapper.deleteByPrimaryKey(id) == CommonCode.DBSuccess.Success;
	}

	@Override
	public List<SysConfig> queryPackList() {
		return sysConfigMapper.queryPackList();
	}

	@Override
	public SysConfig querySelectedBrand(Long id) {
		return sysConfigMapper.selectByPrimaryKey(id);
	}

	@Override
	public SysConfig querySysConfigAddress(Long sysConfigId) {
		return sysConfigMapper.selectByPrimaryKey(sysConfigId);
	}

	@Override
	public List<SysConfig> queryAddressList() {
		return sysConfigMapper.querySysConfigtractList(CommonCode.ConfigGroup.EXTRACT_ADDRESS);
	}

	@Override
	public List<SysConfig> queryOptionalList() {
		return sysConfigMapper.queryOptionalList(CommonCode.ConfigGroup.OPTIONAL_CONFIG);
	}

	@Override
	public List<SysRuleUser> getUsersForCheck() {
		// TODO 后台审核人员
		Long roleId = userroleMapper.getRoleIdByRoleName();
		List<Long> usersId = userroleMapper.getCheckerId(roleId);
		List<SysRuleUser> users = new ArrayList<SysRuleUser>();
		if (usersId.size() > CommonCode.operateRes.NO) {
			for (Long userId : usersId) {
				SysRuleUser user = sysUserMapper.getUserInfo(userId);
				users.add(user);
			}
		}
		return users;
	}

	@Override
	public Boolean addUpgradeConfig(SysConfigUpgradeDao sysConfigUpgradeDao) {
		// 转为JSON
		Gson gs = new Gson();
		Date date = new Date();
		SysConfig config = new SysConfig();
		config.setGroupName(CommonCode.ConfigGroup.UPGRADE_DOWNGRADE);
		if (sysConfigUpgradeDao.getCategory().equals(CommonCode.DowngradeOrUpgrade.PERSONAL_UPGRADE)) {
			config.setScRemark(CommonCode.DowngradeOrUpgradeScRemark.PERSONAL_UPGRADE);
		} else if (sysConfigUpgradeDao.getCategory().equals(CommonCode.DowngradeOrUpgrade.SALE_UPGRADE)) {
			config.setScRemark(CommonCode.DowngradeOrUpgradeScRemark.SALE_UPGRADE);
		} else if (sysConfigUpgradeDao.getCategory().equals(CommonCode.DowngradeOrUpgrade.SALE_DOWNGRADE)) {
			config.setScRemark(CommonCode.DowngradeOrUpgradeScRemark.SALE_DOWNGRADE);
		} else if (sysConfigUpgradeDao.getCategory().equals(CommonCode.DowngradeOrUpgrade.DISTRIBUTOR_DOWNGRADE)) {
			config.setScRemark(CommonCode.DowngradeOrUpgradeScRemark.DISTRIBUTOR_DOWNGRADE);
		} else {
			return false;
		}
		// List<Map<String, String>> listItems = Lists.newArrayList();
		Map<String, String> next = Maps.newHashMap();
		next.put("category", sysConfigUpgradeDao.getCategory());
		next.put("month", sysConfigUpgradeDao.getMonth().isEmpty() ? "0" : sysConfigUpgradeDao.getMonth());
		next.put("totalSales",
				sysConfigUpgradeDao.getTotalSales().isEmpty() ? "0" : sysConfigUpgradeDao.getTotalSales());
		next.put("monthlySales",
				sysConfigUpgradeDao.getMonthlySales().isEmpty() ? "0" : sysConfigUpgradeDao.getMonthlySales());
		next.put("totalAmount",
				sysConfigUpgradeDao.getTotalAmount().isEmpty() ? "0" : sysConfigUpgradeDao.getTotalAmount());
		next.put("monthlyAmount",
				sysConfigUpgradeDao.getMonthlyAmount().isEmpty() ? "0" : sysConfigUpgradeDao.getMonthlyAmount());
		String json = gs.toJson(next);
		if (sysConfigUpgradeDao.getUpgradeId() != null) {
			config = sysConfigMapper.selectByPrimaryKey(sysConfigUpgradeDao.getUpgradeId());
			config.setScValue(json);
			return sysConfigMapper.updateByPrimaryKeySelective(config) == CommonCode.DBSuccess.Success;
		} else {
			config.setScType(CommonCode.ConfigType.TEXT);
			config.setId(KeyUtil.generateDBKey());
			config.setState(CommonCode.ConfigState.ENABLE);// 默认启用
			config.setScValue(json);
			config.setCreateTime(date);
			return sysConfigMapper.insertSelective(config) == CommonCode.DBSuccess.Success;
		}

	}

	@Override
	public SysConfig queryUpgradeConfig(Long id) {
		return sysConfigMapper.selectByPrimaryKey(id);
//		SysConfig sysConfig = new SysConfig();
//		
//		if (sysConfigUpgradeDao.equals(CommonCode.DowngradeOrUpgrade.UPGRADE)) {
//			sysConfig = sysConfigMapper.queryByGroupNameAndState(CommonCode.ConfigGroup.UPGRADE_CONFIG,
//					CommonCode.ConfigState.ENABLE);
//
//		} else if (sysConfigUpgradeDao.equals(CommonCode.DowngradeOrUpgrade.DOWNGRADE)) {
//			sysConfig = sysConfigMapper.queryByGroupNameAndState(CommonCode.ConfigGroup.DOWNGRADE_CONFIG,
//					CommonCode.ConfigState.ENABLE);
//		} else {
//			return null;
//		}
//		if (sysConfig != null) {
//			return sysConfig;
//		}
//		return null;
	}

	/**
	 * @Title: queryCommissionConfig @Description: 查询佣金配置 @return
	 *         TOrdersCommission    返回类型 @throws
	 */
	@Override
	public PageVo<SysConfig> queryCommissionConfig(String groupName, Integer pageNumber, Integer pageSize) {
		PageVo<SysConfig> pageVo = new PageVo<SysConfig>(0, pageNumber, pageSize);
		List<SysConfig> commissionList = sysConfigMapper.queryCommissionConfig(groupName, pageVo.getPageStartNumber(),
				pageVo.getPageEndNumber());
		int commissionCount = sysConfigMapper.queryCommissionConfigCount(groupName);
		pageVo.setTotal(commissionCount);
		pageVo.setRows(commissionList);
		return pageVo;
	}

	@Override
	public void editConfigByKey(Long id) {
		SysConfig sysConfig = sysConfigMapper.selectByPrimaryKey(id);
		sysConfigMapper.updateValueByKey(sysConfig.getScValue(), sysConfig.getScKey().replace("_edit", ""));
	}

	@Override
	public Integer editValueById(Long id, String scValue) {
		SysConfig sysConfig = new SysConfig();
		sysConfig.setId(id);
		sysConfig.setScValue(scValue);
		sysConfig.setState(CommonCode.ConfigState.WAITPASS);
		int c = sysConfigMapper.updateByPrimaryKeySelective(sysConfig);
		if (c <= CommonCode.RESULTNUM) {
			return CommonCode.Audit.ERROR;
		}
		return CommonCode.Audit.SUCCESS;
	}

	@Override
	public PageVo<TUsersRecommenderDao> recommderList(String name, String phone, int page, int rows, int type, String strStartDate,
			String strEndDate) {
		Date strStartDates = null;
		Date strEndDates = null;
		if (strStartDate != null && strEndDate != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				strStartDates = sdf.parse(strStartDate);
				strEndDates = sdf.parse(strEndDate);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		PageVo<TUsersRecommenderDao> pageVo = new PageVo<TUsersRecommenderDao>(0, page, rows);
		if (name == null && phone == null) {
			pageVo.setTotal(0);
			pageVo.setRows(new ArrayList<TUsersRecommenderDao>());
		} else {
			TUsersRecommender tUsersRecommender = tUsersRecommderMapper.selectRecommenderByPhone(name, phone);
			if (tUsersRecommender == null) {
				pageVo.setTotal(0);
				pageVo.setRows(new ArrayList<TUsersRecommenderDao>());
			} else {
				String fatherId=null;
				String grandpaId=null;
				if(tUsersRecommender.getFatherId()!=null){
					fatherId=tUsersRecommender.getFatherId().toString();
				}
				if(tUsersRecommender.getGrandpaId()!=null){
					grandpaId=tUsersRecommender.getGrandpaId().toString();
				}
				List<TUsersRecommenderDao> list = tUsersRecommderMapper.selectRecommenderByUid(
						pageVo.getPageStartNumber(), pageVo.getPageEndNumber(),
						tUsersRecommender.getUserId().toString(), type, fatherId,
						grandpaId, strStartDates, strEndDates);
				int auditCount = tUsersRecommderMapper.selectCountRecommenderByUid(
						tUsersRecommender.getUserId().toString(), type, fatherId,
						grandpaId, strStartDates, strEndDates);
				pageVo.setTotal(auditCount);
				pageVo.setRows(list);
			}
		}
		return pageVo;
	}

	@Override
	public Map<String, Object> queryInsuranceConfig() {
		Map<String, Object> Insurance =new HashMap<String, Object>();
		SysConfig type_one = sysConfigMapper.queryByGroupNameAndState(CommonCode.Insurance.TYPE_ONE, CommonCode.ConfigState.ENABLE);
		SysConfig type_two = sysConfigMapper.queryByGroupNameAndState(CommonCode.Insurance.TYPE_TWO, CommonCode.ConfigState.ENABLE);
		if(type_one==null){
			Insurance.put("InsuranceOneName", null);
		}else{
			Insurance.put("InsuranceOneName", type_one.getScRemark());
			Insurance.put("InsuranceOneValue", type_one.getScValue());
		}
		
		if(type_two==null){
			Insurance.put("InsuranceTwoName", null);
		}else{
			Insurance.put("InsuranceTwoName", type_two.getScRemark());
			Insurance.put("InsuranceTwoValue", type_two.getScValue());
		}
		return Insurance;
	}

	@Override
	public SysConfig queryInsuranceType(Integer insuranceType) {
		if(insuranceType == CommonCode.InsuranceTyep.TYPE_ONE){
			return sysConfigMapper.queryByGroupNameAndState(CommonCode.Insurance.TYPE_ONE, CommonCode.ConfigState.ENABLE);
		}else if(insuranceType == CommonCode.InsuranceTyep.TYPE_TWO){
			return sysConfigMapper.queryByGroupNameAndState(CommonCode.Insurance.TYPE_TWO, CommonCode.ConfigState.ENABLE);
		}
		return null;
	}

	@SuppressWarnings("unused")
	@Override
	public Map<String, String> queryUpgradeCondition() {
		Gson gs = new Gson();
		Map<String, String> rule = new HashMap<String, String>();
		//个人升级规则
		SysConfig personal_upgrade = sysConfigMapper.queryByGroupNameAndStateAndRemark(CommonCode.ConfigGroup.UPGRADE_DOWNGRADE,CommonCode.ConfigState.ENABLE,CommonCode.DowngradeOrUpgradeScRemark.PERSONAL_UPGRADE);
		//销售升级规则
		SysConfig sale_upgrade = sysConfigMapper.queryByGroupNameAndStateAndRemark(CommonCode.ConfigGroup.UPGRADE_DOWNGRADE,CommonCode.ConfigState.ENABLE,CommonCode.DowngradeOrUpgradeScRemark.SALE_UPGRADE);
		String personal_upgrade_scValue = personal_upgrade.getScValue();
		String sale_upgrade_scValue = sale_upgrade.getScValue();
		LinkedTreeMap<String, String> personal_upgrade_obj = (LinkedTreeMap<String, String>)gs.fromJson(personal_upgrade_scValue, Object.class);
		LinkedTreeMap<String, String> sale_upgrade_obj = (LinkedTreeMap<String, String>)gs.fromJson(sale_upgrade_scValue, Object.class);
		rule.put("personalMonth", personal_upgrade_obj.get(CommonCode.UpgradeDowngradeRule.MONTH));
		rule.put("personalSales", personal_upgrade_obj.get(CommonCode.UpgradeDowngradeRule.TOTALSALES));
		rule.put("personalMonthSales", personal_upgrade_obj.get(CommonCode.UpgradeDowngradeRule.MONTHLYSALES));
		rule.put("saleMonth", sale_upgrade_obj.get(CommonCode.UpgradeDowngradeRule.MONTH));
		rule.put("saleSales", sale_upgrade_obj.get(CommonCode.UpgradeDowngradeRule.TOTALSALES));
		rule.put("saleMonthSales", sale_upgrade_obj.get(CommonCode.UpgradeDowngradeRule.MONTHLYSALES));
		//经销商降级规则
		SysConfig distributor_downgrade = sysConfigMapper.queryByGroupNameAndStateAndRemark(CommonCode.ConfigGroup.UPGRADE_DOWNGRADE,CommonCode.ConfigState.ENABLE,CommonCode.DowngradeOrUpgradeScRemark.DISTRIBUTOR_DOWNGRADE);
		//销售降级规则
		SysConfig sale_downgrade = sysConfigMapper.queryByGroupNameAndStateAndRemark(CommonCode.ConfigGroup.UPGRADE_DOWNGRADE,CommonCode.ConfigState.ENABLE,CommonCode.DowngradeOrUpgradeScRemark.SALE_DOWNGRADE);
		String sale_downgrade_scValueDownGrade = sale_downgrade.getScValue();
 		String distributor_downgrade_scValueDownGrade = distributor_downgrade.getScValue();
 		
 		LinkedTreeMap<String, String> sale_downgrade_objDownGrade = (LinkedTreeMap<String, String>)gs.fromJson(sale_downgrade_scValueDownGrade, Object.class);
 		LinkedTreeMap<String, String> distributor_downgrade_objDownGrade = (LinkedTreeMap<String, String>)gs.fromJson(distributor_downgrade_scValueDownGrade, Object.class);
 		rule.put("saleDowMonth", sale_downgrade_objDownGrade.get(CommonCode.UpgradeDowngradeRule.MONTH));
		rule.put("saleDowSales", sale_downgrade_objDownGrade.get(CommonCode.UpgradeDowngradeRule.TOTALSALES));
		rule.put("saleDowMonthSales", sale_downgrade_objDownGrade.get(CommonCode.UpgradeDowngradeRule.MONTHLYSALES));
		rule.put("disDowMonth", distributor_downgrade_objDownGrade.get(CommonCode.UpgradeDowngradeRule.MONTH));
		rule.put("disDowSales", distributor_downgrade_objDownGrade.get(CommonCode.UpgradeDowngradeRule.TOTALSALES));
		rule.put("disDowMonthSales", distributor_downgrade_objDownGrade.get(CommonCode.UpgradeDowngradeRule.MONTHLYSALES));
		return rule;
	}

}

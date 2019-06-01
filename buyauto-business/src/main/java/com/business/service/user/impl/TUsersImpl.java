package com.business.service.user.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.business.service.user.api.TUsersApi;
import com.buyauto.dao.orders.TUsersCommissionDao;
import com.buyauto.dao.user.TUsersPojo;
import com.buyauto.entity.orders.TOrderFinance;
import com.buyauto.entity.sys.SysRuleRole;
import com.buyauto.entity.sys.SysRuleUserrole;
import com.buyauto.entity.user.TUserFinance;
import com.buyauto.entity.user.TUsers;
import com.buyauto.entity.user.TUsersRecommender;
import com.buyauto.entity.user.TUsersSales;
import com.buyauto.mapper.orders.TOrderFinanceMapper;
import com.buyauto.mapper.orders.TOrdersCommissionMapper;
import com.buyauto.mapper.orders.TOrdersMapper;
import com.buyauto.mapper.sys.SysRulePrivilegeMapper;
import com.buyauto.mapper.sys.SysRuleRoleMapper;
import com.buyauto.mapper.sys.SysRuleUserroleMapper;
import com.buyauto.mapper.user.TUserFinanceMapper;
import com.buyauto.mapper.user.TUsersMapper;
import com.buyauto.mapper.user.TUsersRecommenderMapper;
import com.buyauto.mapper.user.TUsersSalesMapper;
import com.buyauto.util.method.CommonCode;
import com.buyauto.util.method.KeyUtil;
import com.buyauto.util.method.RegularUtil;
import com.buyauto.util.method.StringUtil;
import com.buyauto.util.pojo.PageVo;
import com.buyauto.util.pojo.UserSessionInfo;

@Service
public class TUsersImpl implements TUsersApi {
	
	private static final Logger logger = LoggerFactory.getLogger(TUsersImpl.class);
	
	@Autowired
	private TUsersMapper tUsersMapper;
	
	@Autowired
	private TOrdersMapper tOrdersMapper;
	
	@Autowired
	private SysRuleRoleMapper ruleRoleMapper;
	
	@Autowired
	private SysRuleUserroleMapper ruleUserroleMapper;
	
	@Autowired
	private SysRulePrivilegeMapper rulePrivilegeMapper;
	
	@Autowired
	private TUsersRecommenderMapper tUsersRecommenderMapper;
	
	@Autowired
	private TUsersSalesMapper tUsersSalesMapper;
	@Autowired
	private TOrdersCommissionMapper tOrdersCommissionMapper;
	@Autowired
	private TUserFinanceMapper tUserFinanceMapper;
	@Autowired
	private TOrderFinanceMapper tOrderFinanceMapper;
	/**
	 * 手机号登录
	 * @param phone	手机
	 * @param pwd	密码
	 * @return
	 */
	@Override
	public List<TUsers> loginByPhone(String phone) {
		return tUsersMapper.loginByPhone(phone);
	}

	/**
	 * 邮箱登录
	 * @param email 邮箱
	 * @param pwd 密码
	 * @return
	 */
	@Override
	public List<TUsers> loginByEmail(String email) {
		return tUsersMapper.loginByEmail(email);
	}

	/**
	 * 根据ID查询经销商名称
	 */
	@Override
	public String getCompanyById(Long id) {
		return tUsersMapper.getCompanyById(id);
	}

	/**
	 * 查找经销商id和名称列表
	 */
	@Override
	public List<TUsers> findCompany() {
		return tUsersMapper.findCompany();
	}

	/**
	 * 员工登录
	 * @param companyName 公司名称
	 * @param name 姓名
	 * @param pwd  密码
	 */
	@Override
	public List<TUsers> loginByAccount(String companyName, String name) {
		return tUsersMapper.loginByAccount(companyName,name);
	}

	/**
	 * 新增用户
	 */
	@Override
	public Integer insert(TUsers user) {
		return tUsersMapper.insertSelective(user);
	}

	/**
	 * 查找邮箱是否被注册
	 */
	@Override
	public Integer getUserCountByEmail(String email) {
		return tUsersMapper.getUserCountByEmail(email);
	}

	/**
	 * 查找账户是否被注册
	 * @param account 账号
	 * @param companyName 经销商名称
	 * @return
	 */
	@Override
	public Integer getUserCountByAccount(String account, String companyName) {
		return tUsersMapper.getUserCountByAccount(account,companyName);
	}

	/**
	 * 查找手机是否被注册
	 */
	@Override
	public Integer getUserCountByPhone(String phone) {
		return tUsersMapper.getUserCountByPhone(phone);
	}

	/**
	 * 查找经销商名称是否被注册
	 */
	@Override
	public Integer getUserCountByCompanyName(String companyName) {
		return  tUsersMapper.getUserCountByCompanyName(companyName);
	}

	/**
	 * 根据手机修改密码
	 * @param phone
	 * @param pwd
	 * @return
	 */
	@Override
	public Integer editPwdByPhone(String phone, String pwd) {
		return  tUsersMapper.editPwdByPhone(phone,pwd);
	}

	/**
	 * 修改员工信息
	 */
	@Override
	public Integer editUser(TUsers user) {
		return tUsersMapper.updateByPrimaryKeySelective(user);
	}

	/**
	 * 根据手机查找用户
	 */
	@Override
	public TUsers getUserByPhone(String phone) {
		return tUsersMapper.getUserByPhone(phone);
	}

	/**
	 * 获取员工数量(可用员工、可新建账号、冻结账号)
	 */
	@Override
	public Map<String, Object> findAccount(UserSessionInfo user) {
		Long id = user.getId();
		Integer use = tUsersMapper.findUseAccount(id,CommonCode.UserState.STATUS_SUCCESS);//可用账号
		Integer freeze = tUsersMapper.findFreezeAccount(id,CommonCode.UserState.STATUS_FROZEN);//冻结账号
		Integer accountCount = getAccountByCount(id);//查找经销商名下员工数量
		Integer newAccount = CommonCode.UserState.ACCOUNT_COUNT - accountCount;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CommonCode.UserState.CODE_STATUS_SUCCESS, use);
		map.put(CommonCode.UserState.CODE_STATUS_FROZEN, freeze);
		map.put(CommonCode.UserState.CODE_ACCOUNT_COUNT, newAccount);
		return map;
	}

	/**
	 * 查找经销商名下员工数量
	 */
	@Override
	public Integer getAccountByCount(Long id) {
		return tUsersMapper.getAccountByCount(id);
	}

	/**
	 * 员工列表
	 */
	@Override
	public List<TUsersPojo> getUserByPage(Long id, int pageStartNumber, int pageEndNumber) {
		return tUsersMapper.getUserByPage(id,pageStartNumber,pageEndNumber);
	}

	/**
	 * 修改状态
	 * @param id
	 * @param statusFrozen 1正常 3冻结
	 * @return
	 */
	@Override
	public Integer editStateById(Long id, int statusFrozen) {
		TUsers user = new TUsers();
		user.setId(id);
		user.setStatus(statusFrozen);
		user.setUpdateDate(new Date());
		return tUsersMapper.updateByPrimaryKeySelective(user);
	}

	/**
	 * 查询用户信息 
	 * @param id
	 * @return
	 */
	@Override
	public TUsers getById(Long id) {
		return tUsersMapper.selectByPrimaryKey(id);
	}
	

	/**
	 * 修改经销商状态
	 * @param id	
	 * @param status
	 */
	@Override
	public Integer editStatus(Long id, Integer status) {
		if(status==null || id ==null){
			return CommonCode.UserResult.ERROR_PAMMER;
		}
		TUsers user = new TUsers();
		user.setId(id);
		user.setStatus(status);
		user.setUpdateDate(new Date());
		Integer c = tUsersMapper.updateByPrimaryKeySelective(user);
		if(c>CommonCode.RESULTNUM){
			//审核成功插入数据
			return CommonCode.UserResult.SUCCESS;
		}else{
			return CommonCode.UserResult.ERROR;
		}
	}
	
	
	@Override
	public Map<String, Object> queryLeaderIndexData(Long id) {
		Map<String , Object> homePage = new HashMap<String , Object>();
		TUsers tUsers = tUsersMapper.selectByPrimaryKey(id);
		//今日订单
		Long conpanyId = tUsers.getCompanyId()==null?tUsers.getId():tUsers.getCompanyId();
		Integer orderCount = tOrdersMapper.queryTodayOrder(conpanyId);
		Integer snpaidOrderCount = tOrdersMapper.queryUnpaidOrder(conpanyId);
		Integer staffCount = tUsersMapper.queryTotalStaff(conpanyId);
		Integer waitCount =tOrdersMapper.queryWaitDeliverOrder(conpanyId);
		BigDecimal deposit =tOrdersMapper.queryDepositOrder(conpanyId) == null ?CommonCode.BigDecimalCommon.ZERO :tOrdersMapper.queryDepositOrder(conpanyId);
		if(deposit==null){
			deposit=CommonCode.BigDecimalCommon.ZERO;
		}
		BigDecimal retainage =tOrdersMapper.queryRetainageOrder(conpanyId)== null ?CommonCode.BigDecimalCommon.ZERO :tOrdersMapper.queryRetainageOrder(conpanyId);
		if(retainage==null){
			retainage=CommonCode.BigDecimalCommon.ZERO;
		}
		BigDecimal amountTotal = deposit.add(retainage);
		homePage.put("orderCount", orderCount);
		homePage.put("snpaidOrderCount", snpaidOrderCount);
		homePage.put("staffCount", staffCount);
		homePage.put("waitCount", waitCount);
		homePage.put("deposit", deposit);
		homePage.put("retainage", retainage);
		homePage.put("amountTotal", amountTotal);
		return homePage;
	}

	/**
	 * 给用户角色赋予角色
	 */
	@Override
	public boolean giveRole(Long id, String distributor,Long userId) {
		SysRuleRole ruleRole = ruleRoleMapper.selectDistributorRole(distributor);
		if(ruleRole!=null){
			SysRuleUserrole userrole = new SysRuleUserrole();
			userrole.setId(KeyUtil.generateDBKey());
			userrole.setCreateTime(new Date());
			userrole.setCreateUser(userId);//经销商自主注册则不存在创建人
			userrole.setRoleId(ruleRole.getId());
			userrole.setUserId(id);
			return ruleUserroleMapper.insertSelective(userrole) == CommonCode.DBSuccess.Success;
		}
		return false;
	}

	@Override
	public UserSessionInfo validationRoles(Long id, UserSessionInfo userSessionInfo) {
		//TODO
		//二期修改session绑定方式
		String role ="";
		TUsers tUsers = tUsersMapper.selectByPrimaryKey(id);
		if(tUsers == null){
			logger.info("用户不存在");
		}else{
			//SysRuleUserrole userrole = ruleUserroleMapper.selectByUserId(id);
			if(tUsers.getPosition() ==null){
				logger.info("用户未绑定角色");
			}else{
				//根据岗位分配权限 old 待更新
				if(tUsers.getPosition()==CommonCode.UserPosition.ADMIN){
					role=CommonCode.DistributorRole.DISTRIBUTOR;
				}else if(tUsers.getPosition()==CommonCode.UserPosition.SALE){
					role=CommonCode.DistributorRole.SALESPERSON;
				}else if(tUsers.getPosition()==CommonCode.UserPosition.FINANCE){
					role=CommonCode.DistributorRole.TREASURER;
				}else if(tUsers.getPosition()==CommonCode.UserPosition.SUPPLIER){
					role=CommonCode.DistributorRole.SUPPLIER;
					//role=CommonCode.DistributorRole.SALESPERSON;
				}else if(tUsers.getPosition()==CommonCode.UserPosition.PERSONAL){
					role=CommonCode.DistributorRole.PERSONAL;
				}
				SysRuleRole roleList = ruleRoleMapper.selectDistributorRole(role);
				//SysRuleRole role = ruleRoleMapper.selectByPrimaryKey(tUsers.getPosition());
				if(roleList==null){
					logger.info("用户角色未设置");
				}else{
					List<Long> privileges = rulePrivilegeMapper.findRuleByRoleId(roleList.getId());
					Set<Long> privilegesSet = new HashSet<Long>();
					privilegesSet.addAll(privileges);
					if (privilegesSet.size() <= 0) {
						logger.info("角色未设置权限");
					}
					userSessionInfo.setAuthorities(privilegesSet);
				}
			}
		}
		
		return userSessionInfo;
	}

	/**
	 * 获取经销商所有财务的手机号
	 */
	@Override
	public List<TUsers> getFinancePhone(Long companyId) {
		return tUsersMapper.getFinancePhone(companyId);
	}

	/**
	 * 查找工号是否重复
	 */
	@Override
	public Integer getUserCountByJobNumber(String jobNumber, Long companyId,Long id) {
		return tUsersMapper.getUserCountByJobNumber(jobNumber,companyId,id);
	}
	
	@Override
	public List<TUsers> queryFinanceList(Long companyId) {
		return tUsersMapper.queryFinanceList(companyId);
	}

	/**
	 * 根据ID修改员工信息
	 */
	@Override
	public Integer editUserById(TUsers user) {
		return tUsersMapper.updateByPrimaryKeySelective(user);
	}

	@Override
	public TUsers queryUserById(Long companyId) {
		return tUsersMapper.selectByPrimaryKey(companyId);
	}

	/**
	 * 根据ID获取手机号码
	 */
	@Override
	public String getPhoneById(Long id) {
		return tUsersMapper.getPhoneById(id);
	}

	
	//校验供应商注册参数
	private Integer checkRegisterSupplier(String name, String pwd, String companyName, String officeSpace, String contactName, 
			String phone, String codeImg, String codePhone,String email, String codeImgService, String codePhoneService) {
		//非空判断
		if(StringUtil.isEmpty(name)){
			return CommonCode.RegisterResult.NACCOUTN;
		}else if(StringUtil.isEmpty(pwd)){
			return CommonCode.RegisterResult.NPWD;
		}else if(StringUtil.isEmpty(companyName)){
			return CommonCode.RegisterResult.NCOMPANY_NAME;
		}else if(StringUtil.isEmpty(officeSpace)){
			return CommonCode.RegisterResult.NADDRESS;
		}else if(StringUtil.isEmpty(contactName)){
			return CommonCode.RegisterResult.NNAME;
		}else if(StringUtil.isEmpty(phone)){
			return CommonCode.RegisterResult.NPHONE;
		}else if(StringUtil.isEmpty(codeImg)){
			return CommonCode.RegisterResult.NIMGCODE;
		}else if(StringUtil.isEmpty(codePhone)){
			return CommonCode.RegisterResult.NPHONECODE;
		}else if(StringUtil.isEmpty(email)){
			return CommonCode.RegisterResult.NEMAIL;
		}else if(StringUtil.isEmpty(codeImgService)){
			return CommonCode.RegisterResult.NCODEIMG_SERVICE;
		}else if(StringUtil.isEmpty(codePhoneService)){
			return CommonCode.RegisterResult.NCODEPHONE_SERVICE;
		}
		//格式判断
		if(RegularUtil.isTelephone(phone)){
			return CommonCode.RegisterResult.REG_PHONE;
		}
		if(RegularUtil.isEmaile(email)){
			return CommonCode.RegisterResult.REG_EMAIL;
		}
		//验证码判断
		if(!codeImgService.equals(codeImg)){
			return CommonCode.RegisterResult.FIMG_CODE;
		}
		if(!codePhoneService.equals(codePhone)){
			return CommonCode.RegisterResult.FPHONE_CODE;
		}
		//验证是否重复
		Integer c = tUsersMapper.getUserCountByCompanyName(companyName);// 查找公司名称是否被注册
		if (c > CommonCode.RESULTNUM) {
			return CommonCode.RegisterResult.ALEADY_COMPANYNAME;
		}
		c = tUsersMapper.getUserCountByPhone(phone);// 查找手机是否被注册
		if (c > CommonCode.RESULTNUM) {
			return CommonCode.RegisterResult.ALEADY_PHONE;
		}
		if(!StringUtil.isEmpty(email)){
			c = tUsersMapper.getUserCountByEmail(email);// 查找邮箱是否被注册
			if (c > CommonCode.RESULTNUM) {
				return CommonCode.RegisterResult.ALEADY_EMAIL;
			}
		}
		return null;
	}
	
	/**
	 * 查找推荐人是否超限额
	 * @param recommender
	 * @return
	 */
	@Override
	public Integer getUserCountByRecommender(String recommender) {
		return tUsersMapper.getUserCountByRecommender(recommender);
	}

	@Override
	public void insertRecommender(TUsersRecommender tUsersRecommender) {
		tUsersRecommenderMapper.insertSelective(tUsersRecommender);
	}

	@Override
	public Integer getRecommenderCountByPhone(String recommender) {
		return tUsersMapper.getRecommenderCountByPhone(recommender);
	}

	/**
	 * 根据ID查询个人用户
	 * @return
	 */
	@Override
	public TUsers getPersonalById(Long id) {
		return tUsersMapper.getPersonalById(id);
	}
	/** 
	* @Title: editUserLevelById 
	* @Description: 修改用户等级
	* @param id
	* @param sysLevel
	* @param realLevel 
	* @return void    返回类型 
	*/
	@Override
	public void editUserLevelById(Long id, Integer sysLevel, Integer realLevel) {
		tUsersMapper.editUserLevelById(id, sysLevel, realLevel);
	}
	/**
	 * 手机查询用户关系
	 * @param recommender
	 * @return
	 */
	@Override
	public TUsersRecommender selectRecommenderByPhone(String recommender) {
		return tUsersRecommenderMapper.selectRecommenderByPhone(null,recommender);
	}

	@Override
	public TUsersSales getSalerById(Long id) {
		return tUsersSalesMapper.selectByUid(id);
	}

	@Override
	public TUsersRecommender getRecommenderByUid(Long uId) {
		return tUsersRecommenderMapper.getRecommenderByUid(uId);
	}

	/** 
	* @Title: getRackBackList 
	* @Description: 查询返佣列表
	* @param uId
	* @param page
	* @param  rows
	* @return List<TUsersCommissionDao>    返回类型 
	*/
	@Override
	public PageVo<TUsersCommissionDao> getRackBackList(Long uId, int page, int rows) {
		PageVo<TUsersCommissionDao> pojo = new PageVo<TUsersCommissionDao>(0, page, rows);
		List<TUsersCommissionDao> listRow = tOrdersCommissionMapper.getUsersCommissionList(uId, pojo.getPageStartNumber(), pojo.getPageEndNumber());
		Integer count = tOrdersCommissionMapper.getUsersCommissionByCount(uId);
		if(listRow!=null){
			for (TUsersCommissionDao c :listRow ) {
				if(c.getRatio()==null){
					c.setRatio("0");
				}
				if(c.getCommission()==null){
					c.setCommission(new BigDecimal(0));
				}
			}
		}
		pojo.setRows(listRow);
		pojo.setTotal(count);
		return pojo;
	}

	@Override
	public TUserFinance getUserFinanceUidById(Long id) {
		return tUserFinanceMapper.selectByPrimaryKey(id);
	}

	@Override
	public TOrderFinance getOrderFinanceUidById(Long id) {
		return tOrderFinanceMapper.selectByPrimaryKey(id);
	}

	@Override
	public Integer querySalesVolume(Long id) {
		return tOrdersMapper.querySalesVolume(id);
	}
}

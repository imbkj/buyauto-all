package com.business.service.user.api;

import java.util.List;
import java.util.Map;

import com.buyauto.dao.orders.TUsersCommissionDao;
import com.buyauto.dao.user.TUsersPojo;
import com.buyauto.entity.orders.TOrderFinance;
import com.buyauto.entity.user.TUserFinance;
import com.buyauto.entity.user.TUsers;
import com.buyauto.entity.user.TUsersRecommender;
import com.buyauto.entity.user.TUsersSales;
import com.buyauto.util.pojo.PageVo;
import com.buyauto.util.pojo.UserSessionInfo;

public interface TUsersApi {

	/**
	 * 手机号登录
	 * @param phone	手机
	 * @return
	 */
	public List<TUsers> loginByPhone(String phone);
	
	/**
	 * 邮箱登录
	 * @param email 邮箱
	 * @return
	 */
	public List<TUsers> loginByEmail(String email);
	
	/**
	 * 根据ID查询经销商名称
	 * @return
	 */
	public String getCompanyById(Long id);
	
	/**
	 * 查找经销商id和名称列表
	 * @return
	 */
	public List<TUsers> findCompany();

	/**
	 * 员工登录
	 * @param companyName  公司名称
	 * @param name		   姓名
	 * @return
	 */
	public List<TUsers> loginByAccount(String companyName, String name);

	/**
	 * 新增用户
	 * @param user
	 * @return
	 */
	public Integer insert(TUsers user);

	/**
	 * 查找邮箱是否被注册
	 * @param email
	 * @return
	 */
	public Integer getUserCountByEmail(String email);

	/**
	 * 查找账户是否被注册
	 * @param account 账号
	 * @param companyName 经销商名称
	 * @return
	 */
	public Integer getUserCountByAccount(String account, String companyName);

	/**
	 * 查找手机是否被注册
	 * @param phone
	 * @return
	 */
	public Integer getUserCountByPhone(String phone);


	/**
	 * 查找经销商名称是否被注册
	 * @param companyName
	 * @return
	 */
	public Integer getUserCountByCompanyName(String companyName);

	/**
	 * 根据手机修改密码
	 * @param phone
	 * @param pwd
	 * @return
	 */
	public Integer editPwdByPhone(String phone, String pwd);

	/**
	 * 修改员工信息
	 * @param user
	 * @return
	 */
	public Integer editUser(TUsers user);

	/**
	 * 根据手机查找
	 * @param phone
	 * @return
	 */
	public TUsers getUserByPhone(String phone);

	/**
	 * 获取经销商 可使用账号、可新建账号、冻结账号
	 * @return
	 */
	public Map<String, Object> findAccount(UserSessionInfo user);
	
	/**
	 * 查找经销商名下员工数量
	 * @param id
	 * @return
	 */
	public Integer getAccountByCount(Long id);
	
	/**
	 * 员工列表
	 * @param id
	 * @param pageStartNumber
	 * @param pageEndNumber
	 * @return
	 */
	public List<TUsersPojo> getUserByPage(Long id, int pageStartNumber, int pageEndNumber);

	/**
	 * 修改状态
	 * @param id
	 * @param statusFrozen 1正常 3冻结
	 * @return
	 */
	public Integer editStateById(Long id, int statusFrozen);

	/**
	 * 查询用户信息 
	 * @param id
	 * @return
	 */
	public TUsers getById(Long id);

	
	/**
	 * 修改经销商状态
	 * @param id	
	 * @param status
	 */
	public Integer editStatus(Long id, Integer status);

	/**
	 * 经销商首页达时报
	 * @param id
	 * @return
	 */
	public Map<String, Object> queryLeaderIndexData(Long id);

	/**
	 * 角色赋予
	 * @param id
	 * @param distributor 
	 * @param userId 
	 * @return
	 */
	public boolean giveRole(Long id, String distributor, Long userId);

	/**
	 * session中丢入角色
	 * @param id
	 * @param userSessionInfo
	 * @return
	 */
	public UserSessionInfo validationRoles(Long id, UserSessionInfo userSessionInfo);
	/**
	 * 获取经销商手机号
	 * @param companyId
	 * @return
	 */
	public List<TUsers> getFinancePhone(Long companyId);

	/**
	 * 查找工号是否重复
	 * @param jobNumber  工号
	 * @param companyId  经销商ID
	 * @return
	 */
	public Integer getUserCountByJobNumber(String jobNumber, Long companyId , Long id);

	
	/**
	 * 查询对应的财务
	 * @param companyId
	 * @return
	 */
	public List<TUsers> queryFinanceList(Long companyId);

	/**
	 * 根据ID修改用户信息
	 * @param id
	 * @param pwd
	 * @return
	 */
	public Integer editUserById(TUsers userNew);

	public TUsers queryUserById(Long companyId);

	//根据ID获取手机号码
	public String getPhoneById(Long id);

	/**
	 * 查找推荐人是否超限额
	 * @param recommender
	 * @return
	 */
	public Integer getUserCountByRecommender(String recommender);

	/**
	 * 新增用户关系
	 * @param tUsersRecommender
	 * @return
	 */
	public void insertRecommender(TUsersRecommender tUsersRecommender);

	/**
	 * 查找推荐人是否存在
	 * @param recommender
	 * @return
	 */
	public Integer getRecommenderCountByPhone(String recommender);

	/**
	 * 根据ID查询个人用户
	 * @return
	 */
	public TUsers getPersonalById(Long id);

	/** 
	* @Title: editUserLevelById 
	* @Description: 修改用户等级
	* @param id
	* @param sysLevel
	* @param realLevel 
	* @return void    返回类型 
	*/
	public void editUserLevelById(Long id, Integer sysLevel, Integer realLevel);
	/**
	 * 手机查询用户关系
	 * @param recommender
	 * @return
	 */
	public TUsersRecommender selectRecommenderByPhone(String recommender);

	public TUsersSales getSalerById(Long id);
	/**
	 * uid查询用户关系
	 * @param recommender
	 * @return
	 */
	public TUsersRecommender getRecommenderByUid(Long uId);

	/** 
	* @Title: getRackBackList 
	* @Description: 查询返佣列表
	* @param uId
	* @param page
	* @param  rows
	* @return PageVo<TUsersCommissionDao>    返回类型 
	*/
	public PageVo<TUsersCommissionDao> getRackBackList(Long uId, int page, int rows);

	public TUserFinance getUserFinanceUidById(Long id);

	public TOrderFinance getOrderFinanceUidById(Long id);

	/**
	 * 查询用户月销售额
	 * @param id
	 * @return
	 */
	public Integer querySalesVolume(Long id);

}

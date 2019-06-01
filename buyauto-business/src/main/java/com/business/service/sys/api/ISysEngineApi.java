package com.business.service.sys.api;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.buyauto.dao.sys.SysConfigPojo;
import com.buyauto.dao.sys.SysConfigUpgradeDao;
import com.buyauto.dao.sys.SysRuleUserDao;
import com.buyauto.dao.user.TUsersRecommenderDao;
import com.buyauto.entity.sys.EasyUiTree;
import com.buyauto.entity.sys.SysConfig;
import com.buyauto.entity.sys.SysRuleRole;
import com.buyauto.entity.sys.SysRuleUser;
import com.buyauto.util.pojo.PageVo;




public interface ISysEngineApi {

	/**
	 * 
	 * @Title: backendUserLogin
	 * @Description: 后台用户登录
	 * @param @param userName
	 * @param @param userPwd 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public int backendUserLogin(String userName, String userPwd, HttpSession session);
	
	
	/**
	 * 
	 * @Title: queryRole
	 * @Description:查询SysRuleRole
	 * @param @return 设定文件
	 * @return SysRuleRole 返回类型
	 * @throws
	 */
	PageVo<SysRuleRole> queryRole(int pageNumber, int pageSize);
	
	/**
	 * 
	 * @Title: queryRuleUser
	 * @Description:查询SysRuleUser
	 * @param @return 设定文件
	 * @return SysRuleUser 返回类型
	 * @throws
	 */
	PageVo<SysRuleUser> queryRuleUser(int pageNumber, int pageSize);
	
	/**
	 * 
	 * @Title: queryRuleUser
	 * @Description:查询SysRuleUser
	 * @param @return 设定文件
	 * @return SysRuleUser 返回类型
	 * @throws
	 */
	PageVo<SysRuleUserDao> queryRuleUserDao(int pageNumber, int pageSize);
	
	Map<String, Object> selectUserById(Long id);
	
	PageVo<SysRuleRole> selectRolePage(Integer startIndex, Integer endIndex,Integer isEnable);
	
	/**
	 * 
	 * @Title: selectUserByname
	 * @Description:验证用户名是否存在
	 * @param @return 设定文件
	 * @return SysRuleUser 返回类型
	 * @throws
	 */
	SysRuleUser selectUserByname(String userName);
	
	SysRuleRole selectRoleByname(String roleName);
	
	Integer updateRuleUser(SysRuleUser user);
	
	Integer updateRuleRole(SysRuleRole role);
	
	Integer insertRuleUser(SysRuleUser user);
	
	Integer insertRuleRole(SysRuleRole role);
	
	Map<String, Object> selectRoleById(Long id);
	/**
	 * 查找全部角色
	 * @param isEnable 1启用 2禁用 0全部
	 * @return
	 */
	List<SysRuleRole> findAllEnableRole(int isEnable);
	
	Map<String, Object> findRole(Long id);
	
	EasyUiTree selectAllModules();
	
	boolean createRulePrivilege(Long userId, List<Long> rules);

	Integer insertUserRole(Long userId, Long roleId ,Long createUser);

    Map<String, Object> selectRulesByRoleId(Long id);





	/**
	 * 新增/修改提车地址
	 * @param sysConfig
	 * @param extractAddress 
	 * @param text 
	 * @return
	 */
	public Integer addAddress(SysConfig sysConfig, String extractAddress, int text);


	public SysConfigPojo showAddress(Long id);

	/**
	 * sysConfig状态禁用
	 * @param id
	 * @return
	 */
	public Boolean updataDisable(Long id);


	/**
	 * sysconfig状态启用
	 * @param id
	 * @return
	 */
	public Boolean updataEnable(Long id);


	/**
	 * 删除提车地址(物理删除)
	 * @param id
	 * @return
	 */
	public Boolean updataDelete(Long id);


	/**
	 * 查询进口车提车地址
	 * @param scvalue
	 * @param state
	 * @param pageNumber
	 * @param pageSize
	 * @param extractAddress 
	 * @return
	 */
	public PageVo<SysConfig> queryaddressList(String scvalue, Integer state,
			Integer pageNumber, Integer pageSize, String strStartDate,
			String strEndDate, String extractAddress);


	/**
	 * 查询车辆品牌
	 * @return
	 */
	public List<SysConfig> queryPackList();


	/**
	 * 查询选中的品牌
	 * @param id
	 * @return
	 */
	public SysConfig querySelectedBrand(Long id);

	
	/***
	 * 查询提车地址
	 * @param sysConfigId
	 * @return
	 */
	public SysConfig querySysConfigAddress(Long sysConfigId);


	/**
	 * 查询提车地址
	 * @return
	 */
	public List<SysConfig> queryAddressList();


	/**
	 * 查询可选配置
	 * @return
	 */
	public List<SysConfig> queryOptionalList();
	
	/**
	 * 查找后台审核人员
	 * @return
	 */
	public List<SysRuleUser> getUsersForCheck();
	

	/**
	 * 新增升降级配置
	 * @param sysConfigUpgradeDao
	 * @return
	 */
	public Boolean addUpgradeConfig(SysConfigUpgradeDao sysConfigUpgradeDao);


	/**
	 * 查询已有升降级规则
	 * @param sysConfigUpgradeDao
	 * @return
	 */
	public SysConfig queryUpgradeConfig(Long id);


	/**
	 * @param pageSize 
	 * @param pageNumber 
	 * @param groupName  
	* @Title: queryCommissionConfig 
	* @Description: 查询佣金配置
	* @return TOrdersCommission    返回类型 
	* @throws 
	*/
	public PageVo<SysConfig> queryCommissionConfig(String groupName, Integer pageNumber, Integer pageSize);

	public void editConfigByKey(Long id);

	public Integer editValueById(Long id, String scValue);

	public PageVo<TUsersRecommenderDao> recommderList(String name, String phone, int page, int rows, int type, String strStartDate,
			String strEndDate);


	/**
	 * 查询保险信息
	 * @return
	 */
	public Map<String, Object> queryInsuranceConfig();


	/**
	 * 查询保险方式
	 * @param insuranceType
	 * @return
	 */
	public SysConfig queryInsuranceType(Integer insuranceType);


	/**
	 * 查询用户升降级规则
	 * @return
	 */
	public Map<String, String> queryUpgradeCondition();


}

package com.buyauto.mapper.user;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.buyauto.dao.user.TUsersPojo;
import com.buyauto.entity.user.TUsers;

public interface TUsersMapper {
	
    int insert(TUsers record);
    int insertSelective(TUsers record);
    int deleteByPrimaryKey(Long id);
    TUsers selectByPrimaryKey(Long id);
    int updateByPrimaryKeySelective(TUsers record);
    int updateByPrimaryKey(TUsers record);

    /**
	 * 手机号登录
	 * @param phone	手机
	 * @param pwd	密码
	 * @return
	 */
    List<TUsers> loginByPhone(@Param("phone") String phone);

	/**
	 * 邮箱登录
	 * @param email 邮箱
	 * @param pwd 密码
	 * @return
	 */
	List<TUsers> loginByEmail(@Param("email")String email);

	/**
	 * 根据ID查询经销商名称
	 * @param id
	 */
	String getCompanyById(@Param("id") Long id);

	/**
	 * 查找经销商id和名称列表
	 * @return
	 */
	List<TUsers> findCompany();
	/**
	 * 根据经销商id查找销售人员
	 * @param companyId
	 * @return
	 */
	List<TUsers> findUsersByCompanyId(@Param("companyId") Long companyId);

	/**
	 * 员工登录
	 * @param companyName 公司名称
	 * @param account 账户名
	 */
	List<TUsers> loginByAccount(@Param("companyName") String companyName,@Param("account") String account);

	/**
	 * 查找邮箱是否被注册
	 * @param email
	 * @return
	 */
	Integer getUserCountByEmail(@Param("email") String email);

	/**
	 * 查找账户是否被注册
	 * @param account 账号
	 * @param companyName 经销商名称
	 * @return
	 */
	Integer getUserCountByAccount(@Param("account") String account,@Param("companyName") String companyName);
	
	/**
	 * 查找手机是否被注册
	 * @param phone
	 * @return
	 */
	Integer getUserCountByPhone(@Param("phone") String phone);
	
	/**
	 * 查找经销商名称是否被注册
	 * @param companyName
	 * @return
	 */
	Integer getUserCountByCompanyName(@Param("companyName") String companyName);
	
	/**
	 * 根据手机修改密码
	 * @param phone
	 * @param pwd
	 * @return
	 */
	int editPwdByPhone(@Param("phone") String phone, @Param("pwd") String pwd);
	
	/**
	 * 根据手机查找用户
	 * @param phone 手机
	 * @return
	 */
	TUsers getUserByPhone(@Param("phone") String phone);
	
	/**
	 * 可使用账号 
	 * @param id
	 * @param statusSuccess
	 * @return
	 */
	Integer findUseAccount(@Param("id") Long id,@Param("statusSuccess") int statusSuccess);
	
	/**
	 * 冻结账号
	 * @param id
	 * @param statusFrozen
	 * @return
	 */
	Integer findFreezeAccount(@Param("id") Long id,@Param("statusFrozen") int statusFrozen);
	/**
	 * 查找经销商名下员工数量
	 * @param id
	 * @return
	 */
	Integer getAccountByCount(@Param("id") Long id);
	
	/**
	 * 员工列表
	 * @param id
	 * @param pageStartNumber
	 * @param pageEndNumber
	 * @return
	 */
	List<TUsersPojo> getUserByPage(@Param("id") Long id,@Param("pageStartNumber")  int pageStartNumber,@Param("pageEndNumber")  int pageEndNumber);
	
	
	
	/**
	 * 分页条件查询经销商审核列表  - 后台
	 * @param page 页码
	 * @param rows 一页条数
	 * @param status 类型  0待审核,1可用,2审核不通过,3冻结
	 * @param strStartDate 开始时间
	 * @param strEndDate   结束时间
	 * @return
	 */
	List<TUsers> queryCommpanyAudit(@Param("pageStartNumber")int pageStartNumber,@Param("pageEndNumber") int pageEndNumber,@Param("status") String status,@Param("startTime") Date strStartDates,@Param("endTime")  Date strEndDates);

	/**
	 * 条件查询经销商审核列表 总数   - 后台
	 * @param status 类型  0待审核,1可用,2审核不通过,3冻结
	 * @param strStartDate 开始时间
	 * @param strEndDate   结束时间
	 * @return
	 */
	int queryCommpanyAuditCount(@Param("startTime") Date strStartDates,@Param("endTime") Date strEndDates,@Param("status") String status);
	
	
	/**
	 * 禁用该经销商名下子所有员工
	 * @param id	经销商ID
	 * @param freezeFail  禁用
	 * @return
	 */
	Integer editSubEmployeeStatus(@Param("id") Long id,@Param("freezeFail") int freezeFail);
	
	
	/**
	 * 查询现有员工总数
	 * @param companyId
	 * @return
	 */
	Integer queryTotalStaff(@Param("companyId")Long companyId);
	
	/**
	 * 查询对应的财务
	 * @param companyId
	 * @return
	 */
	List<TUsers> queryFinanceList(@Param("companyId")Long companyId);
	/**
	 * 
	 * @param companyId
	 * @return
	 */
	List<TUsers> getFinancePhone(@Param("companyId")Long companyId);
	/**
	 *  查找工号是否重复
	 * @param jobNumber
	 * @return
	 */
	Integer getUserCountByJobNumber(@Param("jobNumber")String jobNumber,@Param("companyId")Long companyId,@Param("id")Long id);
	/**
	 * 根据ID获取手机号码
	 * @param id
	 * @return
	 */
	String getPhoneById(@Param("id")Long id);
	
	/**
	 * 查找推荐人是否超限额
	 * @param recommender
	 * @return
	 */
	Integer getUserCountByRecommender(@Param("recommender")String recommender);
	
	/**
	 * 查找推荐人手机是否存在
	 * @param recommender
	 * @return
	 */
	Integer getRecommenderCountByPhone(@Param("recommender")String recommender);
	/**
	  * 分页条件查询供应商审核列表
	  * @param page 页码
	  * @param rows 一页条数
	  * @param strStartDate 开始时间
	  * @param strEndDate   结束时间
	  * @param status 0待审核,1可用,2审核不通过,3冻结
	  * @param selfSupport 是否自营  0否1是
	  * @return
	*/
	List<TUsers> querySupplierAudit(@Param("pageStartNumber")int pageStartNumber,@Param("pageEndNumber") int pageEndNumber,@Param("status") String status,@Param("selfSupport") String selfSupport,@Param("startTime") Date strStartDates,@Param("endTime") Date strEndDates);
	/**
	  * 查询供应商审核列表总数
	  * @param page 页码
	  * @param rows 一页条数
	  * @param status 0待审核,1可用,2审核不通过,3冻结
	  * @param selfSupport 是否自营  0否1是
	  * @return
	*/
	int querySupplierAuditCount(@Param("startTime")Date strStartDates,@Param("endTime") Date strEndDates,@Param("status") String status,@Param("selfSupport") String selfSupport);
	/**
	  * 查询个人审核列表总数
	  * @param page 页码
	  * @param rows 一页条数
	  * @param status 0待审核,1可用,2审核不通过,3冻结
	  * @return
	*/
	int queryPersonalAuditCount(@Param("startTime")Date strStartDates,@Param("endTime") Date strEndDates,@Param("status") String status);
	/**
	  * 分页条件查询个人审核列表
	  * @param page 页码
	  * @param rows 一页条数
	  * @param strStartDate 开始时间
	  * @param strEndDate   结束时间
	  * @param status 0待审核,1可用,2审核不通过,3冻结
	  * @return
	*/
	List<TUsers> queryPersonalAudit(@Param("pageStartNumber")int pageStartNumber,@Param("pageEndNumber") int pageEndNumber,@Param("status") String status,@Param("startTime") Date strStartDates,@Param("endTime") Date strEndDates);
	/**
	 * 根据ID查询个人用户
	 * @return
	 */
	TUsers getPersonalById(@Param("id")Long id);
	/**
	 * 查询所有身份为个人的可升级用户
	 * @return
	 */
	List<TUsers> queryUpgradePersonalList();
	
	/**
	 * 查询所有身份为个人的可降级用户
	 * @return
	 */
	List<TUsers> queryDownGradePersonalList();
	
	/**
	 * 验证该用户是否为今日新升级用户
	 * @param id
	 * @return
	 */
	TUsers queryValidateDowngrade(@Param("id")Long id);
	
	/** 
	* @Title: editUserLevelById 
	* @Description: 修改用户等级
	* @param id
	* @param sysLevel
	* @param realLevel 
	* @return void    返回类型 
	*/
	void editUserLevelById(@Param("id")Long id,@Param("sysLevel") Integer sysLevel,@Param("realLevel") Integer realLevel);
	

}
package com.external.user.api;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.buyauto.dao.user.TUsersPojo;
import com.buyauto.entity.user.TUsers;
import com.buyauto.util.pojo.PageVo;
import com.buyauto.util.pojo.UserSessionInfo;


public interface ITuserApi {

	/**
	 * 登录
	 * @param account 账户
	 * @param pwd	密码
	 * @param clientDevice 登录设备
	 * @return
	 */
	public Map<String,Object> login(String account,String pwd,String ip,Integer clientDevice);

	/**
	 * 经销商注册
	 * @param account 账号
	 * @param pwd密码
	 * @param companyName 经销商名称
	 * @param name 姓名
	 * @param phone 手机
	 * @param email邮箱
	 * @param file 文件凭证
	 * @param codePhone 手机验证码
	 * @param codeImg  图片验证码
	 * @param codeImgService  图片验证码(session中)
	 * @param codePhoneService 手机验证码(session中)
	 * @return
	 */
	public Integer registerCompanyUser(String account, String pwd,
			String companyName, String name, String phone, String email,
			String file, String codePhone, String codeImg,
			String codeImgService, String codePhoneService,String address,Integer type);

	
	/**
	 * 经销商找回密码
	 * @param phone  手机
	 * @param codePhone  手机验证码
	 * @param codeImg    图片验证码
	 * @param codeImgService sessoin中图片验证码
	 * @param codePhoneService sessoin中手机验证码
	 * @return
	 */
	public Integer forgetUser(String phone, String codePhone, String codeImg,
			String codeImgService, String codePhoneService);


	/**
	 * 经销商修改密码
	 * @param phone 手机号
	 * @param pwd	密码
	 * @param codePhoneService 验证码(session)
	 * @param codePhone 验证码
	 * @return
	 */
	public Integer editPwd(String phone, String pwd, String codePhone, String codePhoneService);

	/**
	 * 新建子员工
	 * @param companyId	经销商ID
	 * @param name		姓名
	 * @param position	职位 0管理员,1销售，2财务
	 * @param companyName	经销商名称
	 * @param account	账号
	 * @param phone		手机
	 * @param email		邮箱
	 * @param pwd		密码
	 * @param hirdeate	入职时间
	 * @param jobNumber	工号
	 * @param region	所属区域
	 * @param user 
	 * @return
	 */
	public Integer addUser(Long companyId, String name, Integer position, String companyName, String account, String phone, String email, String pwd, Date hirdeate,
			String jobNumber, String region, UserSessionInfo user);

	/**
	 * 修改子员工
	 * @param id		员工ID
	 * @param name		姓名
	 * @param position	职位 0管理员,1销售，2财务
	 * @param pwd		密码
	 * @param hirdeate	入职时间
	 * @param jobNumber	工号
	 * @param region	所属区域
	 * @param user		经销商对象
	 * @param email		邮箱
	 * @return
	 */
	public Integer editUser(Long id, String name, Integer position, String pwd, Date hirdeate, String jobNumber, String region,UserSessionInfo user,String email);

	/**
	 * 获取员工数量(可用员工、可新建账号、冻结账号)
	 */
	public Map<String,Object> findAccount(UserSessionInfo user);

	/**
	 * 员工列表
	 * @param pageNumber 页码
	 * @param pageSize	一页条数
	 * @param id		经销商ID
	 * @return
	 */
	public PageVo<TUsersPojo> getUserByPage(Integer pageNumber, Integer pageSize, Long id);

	/**
	 * 冻结或启用
	 * @param id
	 * @param state  1可用,其他数字 冻结
	 * @param adminId 
	 * @return
	 */
	public Integer updateState(Long id, Integer state, Long adminId);

	/**
	 * 查询用户信息 
	 * @param id
	 * @return
	 */
	public TUsers getById(Long id);

	/**
	 * 查询首页达时报
	 * @param id
	 * @return
	 */
	public Map<String, Object> queryLeaderIndexData(Long id);

	/**
	 * 在用户登录时session中放入角色
	 * @param id
	 * @param userSessionInfo 
	 * @return
	 */
	public UserSessionInfo validationRoles(Long id, UserSessionInfo userSessionInfo);

	/**
	 * 查询对应公司的财务
	 * @param userId
	 * @return
	 */
	public List<TUsers> queryFinanceList(Long userId);

	/**
	 * 验证该手机是否已注册
	 * @param phone
	 * @return
	 */
	public Integer checkedPhone(String phone);

	/**
	 *  强制修改密码
	 * @param user
	 * @param pwd
	 * @return
	 */
	public Integer restPwd(UserSessionInfo user, String pwd);

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
	 * @param codeImgService
	 *            图片验证码(session中)
	 * @param codePhoneService
	 *            手机验证码(session中)
	 * @param recommender           
	 *            推荐人手机
	 * @return
	 */
	public Integer registerPersonalUser(String phone, String pwd, String codePhone, String codeImg,
			String codeImgService, String codePhoneService, String recommender);

	/**
	 * 手机用户(个人)注册
	 * @param phone 手机 
	 * @param pwd 密码
	 * @param code 手机验证码
	 * @param recommend 推荐人手机号
	 * @return
	 */
	public Map<String, Object> register(String phone, String pwd, String code, String recommend,String serviceCode);

	/**
	 * 找回密码
	 * @param oldPwd 旧密码
	 * @param newPwdOne 新密码
	 * @param newPwdTwo 确认新密码
	 * @param userId 用户ID
	 * @param user 
	 * @param serviceCode 
	 * @return
	 */
	public Map<String, Object> findPwd(String oldPwd, String newPwdOne, String newPwdTwo, String code, String serviceCode, TUsers user);

	/**
	 * 验证手机号是否存在
	 * @param phone
	 * @return
	 */
	public String forgotPwdByPhone(String phone);

	/**
	 * 找回密码-2-修改密码
	 * @param phone 手机
	 * @param pwd 新密码
	 * @param code 手机验证码
	 * @return
	 */
	public String forgotPwd(String phone, String pwd, String code, String serviceCode);

	/**
	 * 查询用户月销售额
	 * @param id
	 * @return
	 */
	public Integer querySalesVolume(Long id);

	
}

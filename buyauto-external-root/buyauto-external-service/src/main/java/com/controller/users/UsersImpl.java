package com.controller.users;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.business.service.audit.api.TAuditingApi;
import com.business.service.user.api.LogLoginApi;
import com.business.service.user.api.TUsersApi;
import com.buyauto.dao.user.TUsersPojo;
import com.buyauto.entity.log.LogLogin;
import com.buyauto.entity.user.TUsers;
import com.buyauto.entity.user.TUsersRecommender;
import com.buyauto.util.method.CommonCode;
import com.buyauto.util.method.KeyUtil;
import com.buyauto.util.method.MD5Util;
import com.buyauto.util.method.RedisUtil;
import com.buyauto.util.method.RegularUtil;
import com.buyauto.util.method.StringUtil;
import com.buyauto.util.pojo.PageVo;
import com.buyauto.util.pojo.UserSessionInfo;
import com.buyauto.util.web.AppWebResultVo;
import com.external.user.api.ITuserApi;

/**
 * 前台用户服务层
 */
@Service("usersImpl")
public class UsersImpl implements ITuserApi {

	private static final Logger logger = LoggerFactory.getLogger(UsersImpl.class);

	@Autowired
	private TUsersApi tUsersImpl;

	@Autowired
	private LogLoginApi logLoginImpl;

	@Autowired
	private TAuditingApi tAuditingImpl;
	
	

	/**
	 * 登录
	 * 
	 * @param account
	 *            账户
	 * @param pwd
	 *            密码
	 * @param clientDevice
	 *            登录设备
	 * @return
	 */
	@Override
	public Map<String, Object> login(String account, String userPwd, String ip, Integer clientDevice) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtil.isEmpty(account)) {
			map.put(CommonCode.UserState.CODE, CommonCode.LoginResult.NACCOUTN);
			return map;
		}
		if (StringUtil.isEmpty(userPwd)) {
			map.put(CommonCode.UserState.CODE, CommonCode.LoginResult.NPWD);
			return map;
		}
		int i = account.indexOf(":");
		List<TUsers> users = new ArrayList<TUsers>();
		// 账号名登录
		if (i > 0) {
			String companyName = account.substring(0, i);// 公司名称
			int len = account.length();
			String name = account.substring(i + 1, len);// 员工账号名
			users = tUsersImpl.loginByAccount(companyName, name);
			if (users == null || users.size() <= 0) {
				map.put(CommonCode.UserState.CODE, CommonCode.LoginResult.ERROR);
				return map;
			}
		} else if (RegularUtil.isEmaile(account)) {
			// 邮箱登录
			users = tUsersImpl.loginByEmail(account);
			if (users == null || users.size() <= 0) {
				map.put(CommonCode.UserState.CODE, CommonCode.LoginResult.ERROR);
				return map;
			}
		} else if (RegularUtil.isTelephone(account)) {
			// 手机登录
			users = tUsersImpl.loginByPhone(account);
			if (users == null || users.size() <= 0) {
				map.put(CommonCode.UserState.CODE, CommonCode.LoginResult.ERROR);
				return map;
			}
		}
		String pwd = MD5Util.encryption(userPwd);// 密码Md5加密
		if (users != null && users.size() > 0) {
			Map<String, Object> stateUser = verifiStatus(users, pwd);// 验证用户状态
			Integer result = (Integer) stateUser.get(CommonCode.LoginState.CODE);
			if (result != null) {
				map.put(CommonCode.UserState.CODE, result);
				// 如果审核不通过返回原因
				if (result.equals(CommonCode.LoginResult.STATUS_NOTAUDIT)) {
					String info = (String) stateUser.get(CommonCode.LoginState.INFO);
					map.put(CommonCode.LoginState.INFO, info);
				}
				return map;
			}
			TUsers user = (TUsers) stateUser.get(CommonCode.LoginState.OBJ);
			// 保存登录信息
			saveUser(user, ip, account, clientDevice);
			String companyName = null;
			if (user.getCompanyId() != null) {
				TUsers tuser = tUsersImpl.queryUserById(user.getCompanyId());
				companyName = tuser.getAccount();
			} else {
				companyName = user.getAccount();
			}
			Integer level = 0;
			if (user.getRealLevel() != null) {
				level=user.getRealLevel();
			}
			UserSessionInfo userInfo = new UserSessionInfo(user.getId(), user.getName(), user.getCompanyId(),
					companyName, user.getAccount(), user.getPhone(), user.getEmail(), user.getStatus(),
					user.getPosition(), user.getJobNumber(),level);
			// 经销商可新建员工数量
			userInfo.setMaxNumberCount(user.getMaxNumberCount());
			Integer isFirstLogin = user.getIsFirstLogin();// 是否首次登录 0首次1已登陆
			Integer position = user.getPosition();// 职位
			// 如果是子员工登录,判断密码是否需要修改
			if (position != null && !position.equals(CommonCode.UserState.POSITION_ADMIN) && isFirstLogin != null
					&& isFirstLogin.equals(CommonCode.UserState.ISFIRSTLOGIN_YES)) {
				map.put(CommonCode.UserState.CODE, CommonCode.LoginResult.SUCCESS_JUMP_EDITPWD);
			} else if (user.getPosition() == CommonCode.UserState.POSITION_ADMIN
					|| user.getPosition() == CommonCode.UserState.POSITION_FINANCE) {
				map.put(CommonCode.UserState.CODE, CommonCode.LoginResult.SUCCESS_JUMP);
			} else {
				map.put(CommonCode.UserState.CODE, CommonCode.LoginResult.SUCCESS);
			}
			// 管理员用户首次登录，修改用户登录状态
			if ((isFirstLogin == null || CommonCode.UserState.ISFIRSTLOGIN_YES == isFirstLogin)
					&& position.equals(CommonCode.UserState.POSITION_ADMIN)) {
				// 首次登录状态修改成否
				TUsers userNew = new TUsers();
				userNew.setId(user.getId());
				userNew.setIsFirstLogin(CommonCode.UserState.ISFIRSTLOGIN_NO);
				Integer ret = tUsersImpl.editUserById(userNew);
			}
			map.put(CommonCode.UserState.USERINFO, userInfo);
			return map;// 登录成功
		}
		map.put(CommonCode.UserState.CODE, CommonCode.LoginResult.ACCOUNT_ERROR);
		return map;
	}

	/**
	 * 保存用户登录对象,记录日志
	 * 
	 * @param user
	 *            用户对象
	 * @param ip
	 *            请求IP地址
	 * @param clientDevice
	 *            登录设备
	 */
	public void saveUser(TUsers user, String ip, String account, Integer clientDevice) {
		// 添加登录日志
		LogLogin logLogin = new LogLogin();
		logLogin.setId(KeyUtil.generateDBKey());
		logLogin.setLoginName(account);
		logLogin.setIpaddress(ip);
		logLogin.setResult(CommonCode.LogLogin.SUCCESS);
		logLogin.setCreateTime(new Date());
		logLogin.setClientDevice(clientDevice);
		logLoginImpl.insert(logLogin);
	}

	/**
	 * 验证用户状态
	 * 
	 * @param user
	 * @return
	 */
	public Map<String, Object> verifiStatus(List<TUsers> users, String pwd) {
		Map<String, Object> map = new HashMap<String, Object>();
		TUsers user = null;// 用户对象
		if (users.size() == 1) {
			user = users.get(0);
		} else {
			for (TUsers u : users) {
				Integer status = u.getStatus();// 用户状态
				// 如果状态不等于审核不通过
				if (status != CommonCode.UserState.STATUS_NOTAUDIT) {
					user = u;
					break;
				}
			}
			if (user == null) {
				// 获取最新用户信息
				for (int i = 0; i < users.size(); i++) {
					if (i == 0) {
						user = users.get(i);
						continue;
					}
					if (user.getCreateDate().before(users.get(i).getCreateDate())) {
						user = users.get(i);
					}
				}

			}
		}
		if (!pwd.equals(user.getPwd())) {
			map.put(CommonCode.LoginState.CODE, CommonCode.LoginResult.ERROR);
			return map;
		}
		Integer position = user.getPosition();// 岗位
		// 不是管理员需要验证is_freeze 经销商冻结状态
		if (position != CommonCode.UserState.POSITION_ADMIN) {
			Integer freeze = user.getIsFreeze();
			// 验证对应的经销商商是否被冻结
			if (null != freeze && CommonCode.UserState.FREEZE_FAIL == freeze) {
				map.put(CommonCode.LoginState.CODE, CommonCode.LoginResult.FREEZE_FAIL);
				return map;
			}
		}
		// 验证该用户状态
		Integer state = user.getStatus();
		if (state == CommonCode.UserState.STATUS_AUDIT_WAIT) {
			// 用户审核中
			map.put(CommonCode.LoginState.CODE, CommonCode.LoginResult.STATUS_AUDIT_WAIT);
		} else if (state == CommonCode.UserState.STATUS_FROZEN) {
			// 该用户已冻结
			map.put(CommonCode.LoginState.CODE, CommonCode.LoginResult.STATUS_FROZEN);
		} else if (state == CommonCode.UserState.STATUS_NOTAUDIT) {
			// 该用户审核不通过
			map.put(CommonCode.LoginState.CODE, CommonCode.LoginResult.STATUS_NOTAUDIT);
			// 查找审核不通过原因
			Long mid = user.getId();
			String infoNoPass = tAuditingImpl.getNotPassInfo(mid);
			map.put(CommonCode.LoginState.INFO, infoNoPass);
		}
		map.put(CommonCode.LoginState.OBJ, user);
		return map;
	}

	/**
	 * 经销商注册
	 * 
	 * @param account
	 *            账号
	 * @param pwd密码
	 * @param companyName
	 *            经销商名称
	 * @param name
	 *            姓名
	 * @param phone
	 *            手机
	 * @param email邮箱
	 * @param file
	 *            文件凭证
	 * @param codePhone
	 *            手机验证码
	 * @param codeImg
	 *            图片验证码
	 * @param codeImgService
	 *            图片验证码(session中)
	 * @param codePhoneService
	 *            手机验证码(session中)
	 * @param type  0:经销商,1:供应商
	 * @return
	 */
	@Override
	public Integer registerCompanyUser(String account, String pwd, String companyName, String name, String phone,
			String email, String file, String codePhone, String codeImg, String codeImgService, String codePhoneService,
			String address,Integer type) {
		Integer checkCode = checkParam(account, pwd, companyName, name, phone, email, file, codePhone, codeImg,
				codeImgService, codePhoneService, address, null, CommonCode.UserState.STATUS_AUDIT_WAIT);
		if (checkCode != null) {
			return checkCode;
		}
		TUsers user = new TUsers(KeyUtil.generateDBKey(), account, MD5Util.encryption(pwd), companyName, name, phone,
				email, new Date(), CommonCode.UserState.STATUS_AUDIT_WAIT,address);
		/*如果是经销商*/
		if(!StringUtil.isEmpty(type)&&type.equals(CommonCode.UserType.SELLER)){
			user.setPosition(CommonCode.UserState.POSITION_ADMIN);//经销商 职位-管理员
			user.setMaxNumberCount(CommonCode.UserState.ACCOUNT_COUNT);// 经销商新建子员工数量
		}else if(!StringUtil.isEmpty(type)&&type.equals(CommonCode.UserType.SUPPLIER)){
			user.setPosition(CommonCode.UserState.POSITION_SUPPLIER);//职位-供应商
			user.setSelfSupport(CommonCode.UserState.SELF_SUPPORT_NO);//供应商注册默认非自营
		}
		user.setType(type);//0:经销商,1:供应商,2:个人
		try {
			JSONArray jsonArray = new JSONArray(file);// 序列化图片数据
			if (jsonArray == null || jsonArray.length() < 1) {
				return CommonCode.RegisterResult.NFILE;// 无图片
			}
			if (jsonArray.length() > 3) {
				return CommonCode.RegisterResult.SIZEOUTFILE;// 证件凭证最大只能上传3张
			}
			// 循环图片获取图片名称
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String key = jsonObject.getString("preId");// 图片唯一键
				String value = jsonObject.getString("name");// 获取图片名称
				if (i == 0) {
					user.setFile1(value);
				} else if (i == 1) {
					user.setFile2(value);
				} else if (i == 2) {
					user.setFile3(value);
				}
			}
		} catch (JSONException e) {
			logger.error(e + "用户上传凭证,多图转json失败");
			e.printStackTrace();
		}
		user.setIsFirstLogin(CommonCode.UserState.ISFIRSTLOGIN_YES);// 标记首次登录
		// 添加用户
		Integer c = tUsersImpl.insert(user);
		if (c > CommonCode.RESULTNUM) {
			
			//经销商注册成功赋予角色
			/*if(!StringUtil.isEmpty(type)&&type.equals(CommonCode.UserType.SELLER)){
				boolean a = tUsersImpl.giveRole(user.getId(), CommonCode.DistributorRole.DISTRIBUTOR, 1L);
			}*/
			/*供应商赋予角色 ..*/
			
			return CommonCode.RegisterResult.SUCCESS;// 成功
		} else {
			return CommonCode.RegisterResult.ERROR;// 插入数据失败
		}
	}

	// 验证经销商注册参数是否正确
	private Integer checkParam(String account, String pwd, String companyName, String name, String phone, String email,
			String file, String codePhone, String codeImg, String codeImgService, String codePhoneService,
			String address, String recommender, int statusSuccess) {
		if (StringUtil.isEmpty(pwd)) {
			return CommonCode.RegisterResult.NPWD;
		} else if (StringUtil.isEmpty(phone)) {
			return CommonCode.RegisterResult.NPHONE;
		} else if (StringUtil.isEmpty(codePhone)) {
			return CommonCode.RegisterResult.NPHONECODE;
		} else if (StringUtil.isEmpty(codeImg)) {
			return CommonCode.RegisterResult.NIMGCODE;
		} else if (StringUtil.isEmpty(codePhoneService)) {
			return CommonCode.RegisterResult.NCODEPHONE_SERVICE;
		}

		// 验证验证码
		if (!codeImg.equals(codeImgService)) {
			return CommonCode.RegisterResult.FIMG_CODE;
		} else if (!codePhone.equals(codePhoneService)) {
			return CommonCode.RegisterResult.FPHONE_CODE;
		}

		// 验证手机格式
		if (!RegularUtil.isTelephone(phone)) {
			return CommonCode.RegisterResult.REG_PHONE;
		}

		// 查找手机是否被注册
		Integer c = tUsersImpl.getUserCountByPhone(phone);
		if (c > CommonCode.RESULTNUM) {
			return CommonCode.RegisterResult.ALEADY_PHONE;
		}

		// 推荐人手机验证
		if (!StringUtil.isEmpty(recommender)) {
			if (!RegularUtil.isTelephone(recommender)) {
				return CommonCode.RegisterResult.REC_PHONE;
			}
			c = tUsersImpl.getRecommenderCountByPhone(recommender);
			if (c <= CommonCode.RESULTNUM) {
				return CommonCode.RegisterResult.NRECOMMENDER;
			}
//			c = tUsersImpl.getUserCountByRecommender(recommender);
//			if (c > CommonCode.LIMITNUM) {
//				return CommonCode.RegisterResult.OUT_RANGE;
//			}
		}
		// 非个人注册附加验证
		if (statusSuccess != CommonCode.UserState.STATUS_SUCCESS) {
			if (StringUtil.isEmpty(account)) {
				return CommonCode.RegisterResult.NACCOUTN;
			} else if (StringUtil.isEmpty(companyName)) {
				return CommonCode.RegisterResult.NCOMPANY_NAME;
			} else if (StringUtil.isEmpty(name)) {
				return CommonCode.RegisterResult.NNAME;
			} else if (StringUtil.isEmpty(file)) {
				return CommonCode.RegisterResult.NFILE;
			} else if (StringUtil.isEmpty(address)) {
				return CommonCode.RegisterResult.NCODEPHONE_SERVICE;
			}

			// 验证邮箱格式
			if (!StringUtil.isEmpty(email) && !RegularUtil.isEmaile(email)) {
				return CommonCode.RegisterResult.REG_EMAIL;
			}

			// 查找公司名称是否被注册
			c = tUsersImpl.getUserCountByCompanyName(companyName);
			if (c > CommonCode.RESULTNUM) {
				return CommonCode.RegisterResult.ALEADY_COMPANYNAME;
			}

			// 查找账户是否被注册
			c = tUsersImpl.getUserCountByAccount(account, companyName);
			if (c > CommonCode.RESULTNUM) {
				return CommonCode.RegisterResult.ALEADY_ACCOUNT;
			}

			// 查找邮箱是否被注册
			if (!StringUtil.isEmpty(email)) {
				c = tUsersImpl.getUserCountByEmail(email);
				if (c > CommonCode.RESULTNUM) {
					return CommonCode.RegisterResult.ALEADY_EMAIL;
				}
			}
		}

		return null;
	}

	/**
	 * 经销商找回密码
	 * 
	 * @param phone
	 *            手机
	 * @param codePhone
	 *            手机验证码
	 * @param codeImg
	 *            图片验证码
	 * @param codeImgService
	 *            sessoin中图片验证码
	 * @param codePhoneService
	 *            sessoin中手机验证码
	 * @return
	 */
	@Override
	public Integer forgetUser(String phone, String codePhone, String codeImg, String codeImgService,
			String codePhoneService) {
		if (StringUtil.isEmpty(phone)) {
			return CommonCode.ForgetResult.NPHONE;
		} else if (StringUtil.isEmpty(codePhone)) {
			return CommonCode.ForgetResult.NPHONECODE;
		} else if (StringUtil.isEmpty(codeImg)) {
			return CommonCode.ForgetResult.NIMGCODE;
		} else if (StringUtil.isEmpty(codeImgService)) {
			return CommonCode.ForgetResult.INVALID_IMGCODE;
		} else if (StringUtil.isEmpty(codePhoneService)) {
			return CommonCode.ForgetResult.INVALID_PHONE;
		} else if (!codeImgService.equals(codeImg)) {
			return CommonCode.ForgetResult.FIMG_CODE;
		} else if (!codePhoneService.equals(codePhone)) {
			return CommonCode.ForgetResult.FPHONE_CODE;
		}
		TUsers user = tUsersImpl.getUserByPhone(phone);
		if (user == null) {
			return CommonCode.ForgetResult.NO_USER;// 未查到该用户
		}
		if (user != null && CommonCode.UserState.POSITION_ADMIN != user.getPosition()) {
			return CommonCode.ForgetResult.CK_USER;// 只有管理员才能修改密码
		}
		return CommonCode.ForgetResult.SUCCESS;
	}

	/**
	 * 经销商修改密码
	 * 
	 * @param phone
	 *            手机号
	 * @param pwd
	 *            密码
	 * @return
	 */
	@Override
	public Integer editPwd(String phone, String pwd, String codePhone, String codePhoneService) {
		if (StringUtil.isEmpty(phone)) {
			return CommonCode.ForgetResult.NPHONE;
		} else if (StringUtil.isEmpty(pwd)) {
			return CommonCode.ForgetResult.NPWD;
		} else if (!RegularUtil.isTelephone(phone)) {
			return CommonCode.ForgetResult.REG_PHONE;
		} else if (codePhoneService == null) {
			return CommonCode.ForgetResult.INVALID_PHONE;
		} else if (!codePhoneService.equals(codePhone)) {
			return CommonCode.ForgetResult.CK_EDITPWD;
		}
		Integer c = tUsersImpl.editPwdByPhone(phone, MD5Util.encryption(pwd));
		if (c > CommonCode.RESULTNUM) {
			return CommonCode.ForgetResult.SUCCESS;
		} else {
			return CommonCode.ForgetResult.ERROR;
		}
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
	@Override
	public Integer addUser(Long companyId, String name, Integer position, String companyName, String account,
			String phone, String email, String pwd, Date hirdeate, String jobNumber, String region,
			UserSessionInfo user_session) {
		String distributorRole = "";
		// 验证参数
		Integer code = checkParamByUser(companyId, name, position, companyName, account, phone, email, pwd, hirdeate,
				jobNumber, region, user_session);
		if (code != null) {
			return code;
		}
		TUsers user = new TUsers(KeyUtil.generateDBKey(), companyId, name, position, companyName, account, phone, email,
				MD5Util.encryption(pwd), hirdeate, jobNumber, region, CommonCode.UserState.STATUS_SUCCESS,
				CommonCode.UserState.FREEZE_SUCESS);
		// 标记首次登录
		user.setIsFirstLogin(CommonCode.UserState.ISFIRSTLOGIN_YES);
		Integer c = tUsersImpl.insert(user);
		if (c > CommonCode.RESULTNUM) {
			if (position == 1) {
				distributorRole = CommonCode.DistributorRole.SALESPERSON;
			} else if (position == 2) {
				distributorRole = CommonCode.DistributorRole.TREASURER;
			} else {
				System.out.println("错误的用户角色");
			}

			// 添加所所建员工的角色
			//tUsersImpl.giveRole(user.getId(), distributorRole, companyId);
			return CommonCode.UserResult.SUCCESS;
		} else {
			return CommonCode.UserResult.ERROR;
		}
	}

	// 添加子员工 参数校验
	private Integer checkParamByUser(Long companyId, String name, Integer position, String companyName, String account,
			String phone, String email, String pwd, Date hirdeate, String jobNumber, String region,
			UserSessionInfo user_session) {
		Integer count = tUsersImpl.getAccountByCount(companyId);// 经销商名下子员工数量
		Integer accountCount = user_session.getMaxNumberCount();// 经销商可新建员工数量
		if (accountCount == null)
			accountCount = 0;
		if (count >= accountCount) {
			return CommonCode.UserResult.COUNT_ACCOUNT;// 经销商名下子员工已到最大数量
		}
		if (StringUtil.isEmpty(name)) {
			return CommonCode.UserResult.NNAME;
		} else if (position == null) {
			return CommonCode.UserResult.NPOSITION;
		} else if (StringUtil.isEmpty(companyName)) {
			return CommonCode.UserResult.NCOMPANY_NAME;
		} else if (StringUtil.isEmpty(account)) {
			return CommonCode.UserResult.NACCOUTN;
		} else if (StringUtil.isEmpty(phone)) {
			return CommonCode.UserResult.NPHONE;
		} else if (StringUtil.isEmpty(pwd)) {
			return CommonCode.UserResult.NPWD;
		}
		if (!RegularUtil.isTelephone(phone)) {
			return CommonCode.UserResult.REG_PHONE;
		} else if (!StringUtil.isEmpty(email) && !RegularUtil.isEmaile(email)) {
			return CommonCode.UserResult.REG_EMAIL;
		}
		Integer c = tUsersImpl.getUserCountByPhone(phone);// 查找手机是否被注册
		if (c > CommonCode.RESULTNUM) {
			return CommonCode.UserResult.ALEADY_PHONE;
		}
		c = tUsersImpl.getUserCountByAccount(account, companyName);// 查找账户是否被注册
		if (c > CommonCode.RESULTNUM) {
			return CommonCode.UserResult.ALEADY_ACCOUNT;
		}
		if (!StringUtil.isEmpty(email)) {
			c = tUsersImpl.getUserCountByEmail(email);// 查找邮箱是否被注册
			if (c > CommonCode.RESULTNUM) {
				return CommonCode.UserResult.ALEADY_EMAIL;
			}
		}
		if (!StringUtil.isEmpty(jobNumber)) {
			c = tUsersImpl.getUserCountByJobNumber(jobNumber, companyId, null);// 查找工号是否重复
			if (c > CommonCode.RESULTNUM) {
				return CommonCode.UserResult.ALEADY_JOBNUMBER;
			}
		}
		return null;
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
	 * @param userInfo
	 *            经销商对象
	 * @param email
	 *            邮箱
	 * @return
	 */
	@Override
	public Integer editUser(Long id, String name, Integer position, String pwd, Date hirdeate, String jobNumber,
			String region, UserSessionInfo userInfo, String email) {
		if (StringUtil.isEmpty(id)) {
			return CommonCode.UserResult.NID;
		}
		if (StringUtil.isEmpty(name)) {
			return CommonCode.UserResult.NNAME;
		}
		// else if(position == null){
		// return CommonCode.UserResult.NPOSITION;
		// }
		TUsers user = new TUsers();
		// 邮箱验证
		if (!StringUtil.isEmpty(email)) {
			// 验证邮箱格式
			if (!RegularUtil.isEmaile(email)) {
				return CommonCode.UserResult.REG_EMAIL;
			}
			// 验证邮箱是否被注册过
			Integer c = tUsersImpl.getUserCountByEmail(email);
			if (c > CommonCode.RESULTNUM) {
				return CommonCode.UserResult.ALEADY_EMAIL;
			}
			user.setEmail(email);
		}
		Long userId = userInfo.getId();// 经销商ID
		if (!StringUtil.isEmpty(jobNumber)) {
			Integer c = tUsersImpl.getUserCountByJobNumber(jobNumber, userId, id);// 查找工号是否重复
			if (c > CommonCode.RESULTNUM) {
				return CommonCode.UserResult.ALEADY_JOBNUMBER;
			}
		}
		TUsers employee = tUsersImpl.getById(id);
		if (employee == null) {
			return CommonCode.UserResult.ERROR_PAMMER;
		}
		Long companyId = employee.getCompanyId();// 对应经销商ID
		if (!userId.equals(companyId)) {
			// 此员工不是当前经销商名下员工
			return CommonCode.UserResult.EMPLOYEE_ACCOUNT;
		}
		user.setId(id);
		user.setName(name);
		// user.setPosition(position);
		if (!StringUtil.isEmpty(pwd)) {
			user.setPwd(MD5Util.encryption(pwd));
			// 标记首次登录,用的登录强制修改密码
			user.setIsFirstLogin(CommonCode.UserState.ISFIRSTLOGIN_YES);
		}
		user.setHiredate(hirdeate);
		user.setJobNumber(jobNumber);
		user.setRegion(region);
		user.setUpdateDate(new Date());
		Integer c = tUsersImpl.editUser(user);
		if (c > CommonCode.RESULTNUM) {
			return CommonCode.UserResult.SUCCESS;
		} else {
			return CommonCode.UserResult.ERROR;
		}
	}

	/**
	 * 获取员工数量(可用员工、可新建账号、冻结账号)
	 */
	@Override
	public Map<String, Object> findAccount(UserSessionInfo user) {
		Map<String, Object> account = tUsersImpl.findAccount(user);
		return account;
	}

	/**
	 * 员工列表
	 */
	@Override
	public PageVo<TUsersPojo> getUserByPage(Integer pageNumber, Integer pageSize, Long id) {
		PageVo<TUsersPojo> pojo = new PageVo<TUsersPojo>(0, pageNumber, pageSize);
		List<TUsersPojo> listRow = tUsersImpl.getUserByPage(id, pojo.getPageStartNumber(), pojo.getPageEndNumber());
		Integer count = tUsersImpl.getAccountByCount(id);
		pojo.setRows(listRow);
		pojo.setTotal(count);
		return pojo;
	}

	/**
	 * 冻结或启用
	 * 
	 * @param id
	 * @param state
	 *            1可用,其他数字 冻结
	 * @param adminId
	 *            经销商ID
	 * @return
	 */
	@Override
	public Integer updateState(Long id, Integer state, Long adminId) {
		TUsers user = tUsersImpl.getById(id);
		if (user == null || user.getCompanyId() == null || !(user.getCompanyId()).equals(adminId)) {
			return CommonCode.UserResult.EMPLOYEE_ACCOUNT;
		}
		Integer c = 0;
		if (state != null && state == CommonCode.UserState.STATUS_SUCCESS) {
			c = tUsersImpl.editStateById(id, CommonCode.UserState.STATUS_SUCCESS);
		} else {
			c = tUsersImpl.editStateById(id, CommonCode.UserState.STATUS_FROZEN);
		}
		if (c > CommonCode.RESULTNUM) {
			return CommonCode.UserResult.SUCCESS;
		}
		return CommonCode.UserResult.ERROR;
	}

	/**
	 * 查询用户信息
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public TUsers getById(Long id) {
		return tUsersImpl.getById(id);
	}

	/**
	 * 验证该手机是否已注册
	 */
	@Override
	public Integer checkedPhone(String phone) {
		if (!RegularUtil.isTelephone(phone)) {
			return CommonCode.ForgetResult.REG_PHONE;
		}
		TUsers user = tUsersImpl.getUserByPhone(phone);
		if (user == null) {
			return CommonCode.ForgetResult.NO_USER;// 未查到该用户
		}
		/* 只有经销商管理员才有找回密码功能 */
		Integer position = user.getPosition();
		if (CommonCode.UserState.POSITION_ADMIN != position) {
			return CommonCode.ForgetResult.CK_USER;// 只有管理员才能修改密码
		}
		Integer status = user.getStatus();// 状态
		if (status != null && status.equals(CommonCode.UserState.STATUS_AUDIT_WAIT)) {
			return CommonCode.ForgetResult.STATUS_AUDIT_WAIT;// 用户审核中 503
		} else if (status != null && status.equals(CommonCode.UserState.STATUS_FROZEN)) {
			return CommonCode.ForgetResult.STATUS_FROZEN;// 该用户已冻结 504
		}
		return CommonCode.ForgetResult.SUCCESS;
	}

	@Override
	public Map<String, Object> queryLeaderIndexData(Long id) {
		return tUsersImpl.queryLeaderIndexData(id);
	}

	@Override
	public UserSessionInfo validationRoles(Long id, UserSessionInfo userSessionInfo) {
		return tUsersImpl.validationRoles(id, userSessionInfo);
	}

	@Override
	public List<TUsers> queryFinanceList(Long companyId) {
		return tUsersImpl.queryFinanceList(companyId);
	}

	/**
	 * 强制修改密码
	 */
	@Override
	public Integer restPwd(UserSessionInfo user, String pwd) {
		if (user == null || user.getId() == null) {
			return CommonCode.RestPwd.ERROR;
		} else if (StringUtil.isEmpty(pwd)) {
			return CommonCode.RestPwd.NPWD;
		}
		Integer position = user.getPosition();
		// 只有子员工才能修改密码
		if (position != null && position.equals(CommonCode.UserState.POSITION_ADMIN)) {
			return CommonCode.RestPwd.FADMIN;
		}
		Long id = user.getId();
		String newPwd = MD5Util.encryption(pwd);
		TUsers userNew = new TUsers();
		userNew.setId(id);
		userNew.setPwd(newPwd);
		// 首次登录状态修改成否
		userNew.setIsFirstLogin(CommonCode.UserState.ISFIRSTLOGIN_NO);
		Integer ret = tUsersImpl.editUserById(userNew);
		// 如果是管理员或者是财务,页面跳转到账房页面
		if (position == CommonCode.UserState.POSITION_ADMIN || position == CommonCode.UserState.POSITION_FINANCE) {
			return CommonCode.RestPwd.SUCCESS_JUMP;
		} else {
			return CommonCode.RestPwd.SUCCESS;
		}

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
	 * @param codeImgService
	 *            图片验证码(session中)
	 * @param codePhoneService
	 *            手机验证码(session中)
	 * @param recommender
	 *            推荐人手机
	 * @return
	 */
	@Override
	public Integer registerPersonalUser(String phone, String pwd, String codePhone, String codeImg,
			String codeImgService, String codePhoneService, String recommender) {
		Integer checkCode = checkParam(null, pwd, null, null, phone, null, null, codePhone, codeImg, codeImgService,
				codePhoneService, null, recommender, CommonCode.UserState.STATUS_SUCCESS);
		if (checkCode != null) {
			return checkCode;
		}
		return saveRecommender(KeyUtil.generateDBKey(),recommender, phone, pwd);
	}

	/**
	 * 新增个人用户
	 * @param recommender 推荐人手机号
	 * @param phone 手机号
	 * @param pwd 密码
	 * @return
	 */
	private Integer saveRecommender(Long userId,String recommender,String phone,String pwd){
		TUsers user = new TUsers(userId, null, MD5Util.encryption(pwd), null, null, phone, null,CommonCode.UserState.POSITION_PERSONAL,
				new Date(), CommonCode.UserState.STATUS_SUCCESS);
		user.setRecommender(recommender);// 推荐人手机
		user.setRealLevel(CommonCode.SysLevel.PERSONAL);
		user.setSysLevel(CommonCode.SysLevel.PERSONAL);
		user.setType(CommonCode.UserType.PERSONAL);
		user.setIsFirstLogin(CommonCode.UserState.ISFIRSTLOGIN_YES);// 标记首次登录
		user.setPosition(CommonCode.UserState.POSITION_PERSONAL);//职位-个人
		// 添加用户
		Integer c = tUsersImpl.insert(user);
		if (c > CommonCode.RESULTNUM) {
			// 个人注册成功插入关系表
			if(!StringUtil.isEmpty(recommender)){
				TUsersRecommender users = tUsersImpl.selectRecommenderByPhone(recommender);
				TUsersRecommender tUsersRecommender = new TUsersRecommender();
				tUsersRecommender.setId(KeyUtil.generateDBKey());
				tUsersRecommender.setUserId(userId);
				tUsersRecommender.setUserPhone(phone);
				tUsersRecommender.setFatherId(users.getId());
				tUsersRecommender.setFatherPhone(recommender);
				if(users.getFatherId() != null){
					tUsersRecommender.setGrandpaId(users.getFatherId());
				}
				if(users.getFatherPhone() != null){
					tUsersRecommender.setGrandpaPhone(users.getFatherPhone());
				}
				tUsersRecommender.setCreateDate(new Date());
				tUsersImpl.insertRecommender(tUsersRecommender);
			}
			return CommonCode.RegisterResult.SUCCESS;// 成功
		} else {
			return CommonCode.RegisterResult.ERROR;// 插入数据失败
		}
	}
	/**
	 * 手机用户(个人)注册
	 * @param phone 手机 
	 * @param pwd 密码
	 * @param code 手机验证码
	 * @param recommend 推荐人手机号
	 * @return
	 */
	@Override
	public Map<String, Object> register(String phone, String pwd, String code, String recommend,String serviceCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer checkCode = checkParam(null, pwd, null, null, phone, null, null, code, CommonCode.UserState.CODE_IMG_REGISTER, CommonCode.UserState.CODE_IMG_REGISTER,
				serviceCode, null, recommend, CommonCode.UserState.STATUS_SUCCESS);
		if(checkCode!=null){
			map.put(CommonCode.UserState.CODE,checkCode);
			return map;
		}
		Long userId = KeyUtil.generateDBKey();
		Integer retCode = saveRecommender(userId,recommend, phone, pwd);
		if(CommonCode.RegisterResult.SUCCESS.equals(retCode)){
			map.put(CommonCode.UserState.TOKEN,userId);
		}
		map.put(CommonCode.UserState.CODE,retCode);
		return map;
	}

	/**
	 * 找回密码（手机端）
	 * @param oldPwd 旧密码
	 * @param newPwdOne 新密码
	 * @param newPwdTwo 确认新密码
	 * @param userId 用户ID
	 * @return
	 */
	@Override
	public Map<String, Object> findPwd(String oldPwd, String newPwdOne, String newPwdTwo,String code, String serviceCode, TUsers user) {
		Map<String, Object> map = new HashMap<String, Object>();
		String md5OldPwd = MD5Util.encryption(oldPwd);
		if(!(user.getPwd()).equals(md5OldPwd)){
			map.put(CommonCode.UserState.CODE,CommonCode.RetAppInfo.NOLDPWD);
			return map;
		}
		if(!StringUtil.isEmpty(newPwdOne) && !newPwdOne.equals(newPwdTwo)){
			map.put(CommonCode.UserState.CODE,CommonCode.RetAppInfo.NOLDPWD); //两次密码输入不一致//CommonCode.RegisterResult.NPWDTWO
			return map;
		}
		if(!StringUtil.isEmpty(code) && !code.equals(serviceCode)){
			map.put(CommonCode.UserState.CODE, CommonCode.RetAppInfo.FPHONE_CODE);
			return map;
		}
		user.setPwd(MD5Util.encryption(newPwdTwo));
		user.setUpdateDate(new Date());
		Integer ret = tUsersImpl.editUserById(user);
		if(ret>CommonCode.RESULTNUM){
			map.put(CommonCode.UserState.CODE, CommonCode.RetAppInfo.SUCCESS);
		}else{
			map.put(CommonCode.UserState.CODE, CommonCode.RetAppInfo.ERROR);
		}
		return map;
	}

	/**
	 * 验证手机号是否存在
	 */
	@Override
	public String forgotPwdByPhone(String phone) {
		if(!RegularUtil.isTelephone(phone)){
			return CommonCode.RetAppInfo.REG_PHONE;
		}
		TUsers user = tUsersImpl.getUserByPhone(phone);
		if(user==null){
			return CommonCode.RetAppInfo.NUSER;
		}
		if(!CommonCode.UserPosition.PERSONAL.equals(user.getPosition())){
			return CommonCode.RetAppInfo.NUSERTYPE;
		}
		return null;
	}

	/**
	 * 找回密码-2-修改密码
	 * @param phone 手机
	 * @param pwd 新密码
	 * @param code 手机验证码
	 * @return
	 */
	@Override
	public String forgotPwd(String phone, String pwd, String code, String serviceCode) {
		TUsers user = tUsersImpl.getUserByPhone(phone);
		if(user==null){
			return CommonCode.RetAppInfo.NUSER;
		}
		if(!code.equals(serviceCode)){
			return CommonCode.RetAppInfo.FPHONE_CODE;
		}
		user.setPwd(MD5Util.encryption(pwd));
		user.setUpdateDate(new Date());
		Integer ret = tUsersImpl.editUser(user);
		if(ret <= CommonCode.DBSuccess.Fail ){
			return CommonCode.RetAppInfo.ERROR;
		}
		return null;
	}
	

	@Override
	public Integer querySalesVolume(Long id) {
		return tUsersImpl.querySalesVolume(id);
	}


	
	

}

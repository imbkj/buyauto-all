package com.business.service.audit.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.business.service.audit.api.TAuditingApi;
import com.buyauto.dao.user.TOrderFinanceDao;
import com.buyauto.dao.user.TUserFinanceDao;
import com.buyauto.entity.audit.TAuditing;
import com.buyauto.entity.orders.TOrderFinance;
import com.buyauto.entity.user.TUserFinance;
import com.buyauto.entity.user.TUsers;
import com.buyauto.entity.user.TUsersMsg;
import com.buyauto.mapper.audit.TAuditingMapper;
import com.buyauto.mapper.orders.TOrderFinanceMapper;
import com.buyauto.mapper.sys.SysConfigMapper;
import com.buyauto.mapper.user.TUserFinanceMapper;
import com.buyauto.mapper.user.TUsersMapper;
import com.buyauto.mapper.user.TUsersMsgMapper;
import com.buyauto.mapper.user.TUsersSalesMapper;
import com.buyauto.util.method.CommonCode;
import com.buyauto.util.method.KeyUtil;
import com.buyauto.util.pojo.PageVo;
import com.buyauto.util.pojo.UserSessionInfoBackend;

@Service
public class TAuditingImpl implements TAuditingApi {

	@Autowired
	private TAuditingMapper tAuditingMapper;
	@Autowired
	private TUsersMapper tUsersMapper;
	@Autowired
	private TUsersMsgMapper tUsersMsgMapper;
	@Autowired
	private TUsersSalesMapper tUsersSalesMapper;
	@Autowired
	private SysConfigMapper sysConfigMapper;
	@Autowired
	private TUserFinanceMapper tUserFinanceMapper;
	@Autowired
	private TOrderFinanceMapper tOrderFinanceMapper;

	/**
	 * 分页条件查询审核列表
	 * 
	 * @param page
	 *            页码
	 * @param rows
	 *            一页条数
	 * @param type
	 *            类型
	 * @param strStartDate
	 *            开始时间
	 * @param strEndDate
	 *            结束时间
	 * @return
	 */
	@Override
	public PageVo<TAuditing> queryAudit(int page, int rows, String type, String strStartDate, String strEndDate,
			String result) {
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
		PageVo<TAuditing> pageVo = new PageVo<TAuditing>(0, page, rows);
		List<TAuditing> list = tAuditingMapper.queryAudit(pageVo.getPageStartNumber(), pageVo.getPageEndNumber(), type,
				strStartDates, strEndDates, result);
		int auditCount = tAuditingMapper.queryAuditCount(strStartDates, strEndDates, type, result);
		pageVo.setTotal(auditCount);
		pageVo.setRows(list);
		return pageVo;
	}

	/**
	 * 修改审核信息
	 */
	@Override
	public int updateResult(TAuditing auditing, UserSessionInfoBackend loginUser) {
		if (auditing != null && null == auditing.getResult()) {
			auditing.setResult(CommonCode.Audit.PASS);// 通过
		}
		auditing.setOperId(loginUser.getId());// 操作人id
		auditing.setOperName(loginUser.getName());// 操作人姓名
		return tAuditingMapper.updateByPrimaryKeySelective(auditing);
	}

	/**
	 * 审核
	 * 
	 * @param result
	 *            审核结果0通过1不通过
	 * @param type
	 *            审核类型 0经销商注册,1订单审核，2订单取消审核
	 * @param id
	 *            对应ID
	 * @param operName
	 *            操作人姓名
	 * @param operId
	 *            操作人ID
	 */
	@Override
	public int insertAudit(int result, int type, Long id, String operName, Long operId, String info) {
		// 经销商,供应商,个人审核 result 0通过1不通过
		if (type == CommonCode.Audit.AUDITTYPEONE || type == CommonCode.Audit.AUDITTYPEFORE
				|| type == CommonCode.Audit.AUDITTYPEFIVE|| type == CommonCode.Audit.AUDITTYPESEVEN) {
			if (result == CommonCode.UserState.STATUS_NOTAUDIT) {
				result = CommonCode.Audit.NOTPASS;// 不通过
			} else {
				result = CommonCode.Audit.PASS;// 通过
			}
		}
		TAuditing audit = new TAuditing(KeyUtil.generateDBKey(), result, type, id, new Date(), operName, operId, info);
		Integer c = tAuditingMapper.insertSelective(audit);
		if (c > CommonCode.RESULTNUM) {
			return CommonCode.Audit.SUCCESS;
		}
		return CommonCode.Audit.ERROR;
	}

	/**
	 * 根据对应ID查询原因
	 */
	public String getNotPassInfo(Long mid) {
		return tAuditingMapper.getNotPassInfoByMId(mid);
	}

	/**
	 * 根据ID查询审核意见
	 * 
	 * @param id
	 *            主键ID
	 */
	@Override
	public String getInfoById(Long id) {
		return tAuditingMapper.getInfoById(id);
	}

	/**
	 * 分页条件查询经销商审核列表
	 * 
	 * @param page
	 *            页码
	 * @param rows
	 *            一页条数
	 * @param status
	 *            类型 0待审核,1可用,2审核不通过,3冻结
	 * @param strStartDate
	 *            开始时间
	 * @param strEndDate
	 *            结束时间
	 * @return
	 */
	@Override
	public PageVo<TUsers> auditCommpanyList(int page, int rows, String status, String strStartDate, String strEndDate) {
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
		PageVo<TUsers> pageVo = new PageVo<TUsers>(0, page, rows);
		List<TUsers> list = tUsersMapper.queryCommpanyAudit(pageVo.getPageStartNumber(), pageVo.getPageEndNumber(),
				status, strStartDates, strEndDates);
		int auditCount = tUsersMapper.queryCommpanyAuditCount(strStartDates, strEndDates, status);
		pageVo.setTotal(auditCount);
		pageVo.setRows(list);
		return pageVo;
	}

	/**
	 * 冻结\解冻经销商
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	@Override
	public Integer frozenCommpany(Long id, int status) {
		TUsers user = new TUsers();
		user.setId(id);
		user.setUpdateDate(new Date());
		int freeze = CommonCode.UserState.FREEZE_FAIL;
		// 冻结
		if (CommonCode.UserState.STATUS_FROZEN == status) {
			user.setStatus(CommonCode.UserState.STATUS_FROZEN);
		} else {
			// 可用
			freeze = CommonCode.UserState.FREEZE_SUCESS;
			user.setStatus(CommonCode.UserState.STATUS_SUCCESS);
		}
		Integer c = tUsersMapper.updateByPrimaryKeySelective(user);// 修改该经销商状态
		if (c <= CommonCode.RESULTNUM) {
			return CommonCode.Audit.ERROR;
		}
		// 该经销商名下子员工全部 禁用\启用
		c = tUsersMapper.editSubEmployeeStatus(id, freeze);
		return CommonCode.Audit.SUCCESS;
	}

	@Override
	public TAuditing getCheckInfo(Long mid) {
		return tAuditingMapper.getCheckInfoByMId(mid);
	}

	/**
	 * 分页条件查询供应商审核列表
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
	 * @param selfSupport
	 *            是否自营 0否1是
	 * @return
	 */
	@Override
	public PageVo<TUsers> supplierList(int page, int rows, String status, String selfSupport, String strStartDate,
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
		PageVo<TUsers> pageVo = new PageVo<TUsers>(0, page, rows);
		List<TUsers> list = tUsersMapper.querySupplierAudit(pageVo.getPageStartNumber(), pageVo.getPageEndNumber(),
				status, selfSupport, strStartDates, strEndDates);
		int auditCount = tUsersMapper.querySupplierAuditCount(strStartDates, strEndDates, status, selfSupport);
		pageVo.setTotal(auditCount);
		pageVo.setRows(list);
		return pageVo;
	}

	/**
	 * 修改供应商自营状态
	 * 
	 * @param id
	 * @param support
	 *            自营供应商:0否1是
	 * @return
	 */
	@Override
	public Integer support(Long id, Integer support) {
		TUsers user = new TUsers();
		user.setId(id);
		user.setUpdateDate(new Date());
		// 自营
		if (CommonCode.UserState.SELF_SUPPORT == support) {
			user.setSelfSupport(support);
		} else {
			// 取消自营
			user.setSelfSupport(CommonCode.UserState.SELF_SUPPORT_NO);
		}
		Integer c = tUsersMapper.updateByPrimaryKeySelective(user);
		if (c <= CommonCode.RESULTNUM) {
			return CommonCode.Audit.ERROR;
		}
		return CommonCode.Audit.SUCCESS;
	}

	/**
	 * 冻结/解冻供应商
	 * 
	 * @param id
	 * @param status
	 *            状态 1可用 3冻结
	 * @return
	 */
	@Override
	public Integer frozenSupplier(Long id, Integer status) {
		TUsers user = new TUsers();
		user.setId(id);
		user.setUpdateDate(new Date());
		// 冻结
		if (CommonCode.UserState.STATUS_FROZEN == status) {
			user.setStatus(CommonCode.UserState.STATUS_FROZEN);
		} else {
			// 可用
			user.setStatus(CommonCode.UserState.STATUS_SUCCESS);
		}
		Integer c = tUsersMapper.updateByPrimaryKeySelective(user);// 修改该经销商状态
		if (c <= CommonCode.RESULTNUM) {
			return CommonCode.Audit.ERROR;
		}
		return CommonCode.Audit.SUCCESS;
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
	@Override
	public PageVo<TUsers> personalList(int page, int rows, String status, String strStartDate, String strEndDate) {
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
		PageVo<TUsers> pageVo = new PageVo<TUsers>(0, page, rows);
		List<TUsers> list = tUsersMapper.queryPersonalAudit(pageVo.getPageStartNumber(), pageVo.getPageEndNumber(),
				status, strStartDates, strEndDates);
		int auditCount = tUsersMapper.queryPersonalAuditCount(strStartDates, strEndDates, status);
		pageVo.setTotal(auditCount);
		pageVo.setRows(list);
		return pageVo;
	}

	/**
	 * 冻结\解冻个人
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	@Override
	public Integer frozenPersonal(Long id, Integer status) {
		TUsers user = new TUsers();
		user.setId(id);
		user.setUpdateDate(new Date());
		// 冻结
		if (CommonCode.UserState.STATUS_FROZEN == status) {
			user.setStatus(CommonCode.UserState.STATUS_FROZEN);
		} else {
			// 可用
			user.setStatus(CommonCode.UserState.STATUS_SUCCESS);
		}
		Integer c = tUsersMapper.updateByPrimaryKeySelective(user);// 修改该个人状态
		if (c <= CommonCode.RESULTNUM) {
			return CommonCode.Audit.ERROR;
		}
		return CommonCode.Audit.SUCCESS;
	}

	/**
	 * 邀请个人升级
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public Integer levelUpPersonal(Long id) {
		TUsers tUsers = tUsersMapper.selectByPrimaryKey(id);
		int level = 0;
		String info = "";
		if (tUsers.getRealLevel() == null || tUsers.getSysLevel() == null || tUsers.getSysLevel() <= tUsers.getRealLevel()){
			return CommonCode.Audit.ERROR;
		}
		if (tUsers.getRealLevel() == CommonCode.SysLevel.PERSONAL && tUsers.getSysLevel() > tUsers.getRealLevel()) {
			level = CommonCode.MsgType.LEVELUP;
			info = "尊贵的客户，您已满足升级为个人销售的条件，特诚意邀请您！";
		}
		if (tUsers.getRealLevel() == CommonCode.SysLevel.SELLER && tUsers.getSysLevel() > tUsers.getRealLevel()) {
			level = CommonCode.MsgType.LEVELTOP;
			info = "尊贵的客户，您已满足升级为个人经销商的条件，特诚意邀请您！";
		}
		TUsersMsg tUsersMsg = new TUsersMsg(KeyUtil.generateDBKey(), id, info, level, new Date(),
				CommonCode.MsgStatus.NOREAD);
		Integer c = tUsersMsgMapper.insertSelective(tUsersMsg);
		if (c <= CommonCode.RESULTNUM) {
			return CommonCode.Audit.ERROR;
		}
		return CommonCode.Audit.SUCCESS;
	}

	/**
	 * 根据对应ID查询个人审核原因
	 * 
	 * @return
	 */
	@Override
	public String getNotPassInfoForPersonal(Long mid) {
		return tUsersSalesMapper.getNotPassInfoByMId(mid);
	}

	/**
	 * 修改状态
	 * 
	 * @param id
	 * @param result
	 *            1通过 2不通过
	 * @return
	 */
	@Override
	public Integer editStatus(Long id, Integer result) {
		tUsersSalesMapper.updateStatusByUid(id, result);
		return CommonCode.Audit.SUCCESS;
	}

	@Override
	public Integer editConfigStatus(Long id, Integer result) {
		sysConfigMapper.updateStatusById(id, result);
		return CommonCode.Audit.SUCCESS;
	}

	@Override
	public PageVo<TUserFinanceDao> supplierFinanceList(int page, int rows, String status, String name, String phone,
			String strStartDate, String strEndDate) {
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
		PageVo<TUserFinanceDao> pageVo = new PageVo<TUserFinanceDao>(0, page, rows);
		List<TUserFinanceDao> list = tUserFinanceMapper.queryFinanceAudit(pageVo.getPageStartNumber(),
				pageVo.getPageEndNumber(), status, name, phone, strStartDates, strEndDates);
		int auditCount = tUserFinanceMapper.queryFinanceAuditCount(strStartDates, strEndDates, status, name, phone);
		pageVo.setTotal(auditCount);
		pageVo.setRows(list);
		return pageVo;
	}

	@Override
	public Integer editUserFinanceStatus(Long id, Integer result) {
		TUserFinance tUserFinance = new TUserFinance();
		tUserFinance.setId(id);
		tUserFinance.setStatus(result);
		tUserFinanceMapper.updateByPrimaryKeySelective(tUserFinance);
		return CommonCode.Audit.SUCCESS;
	}

	@Override
	public PageVo<TOrderFinanceDao> personalFinanceList(int page, int rows, String status, String name, String phone,
			String strStartDate, String strEndDate) {
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
		PageVo<TOrderFinanceDao> pageVo = new PageVo<TOrderFinanceDao>(0, page, rows);
		List<TOrderFinanceDao> list = tOrderFinanceMapper.queryFinanceAudit(pageVo.getPageStartNumber(),
				pageVo.getPageEndNumber(), status, name, phone, strStartDates, strEndDates);
		int auditCount = tOrderFinanceMapper.queryFinanceAuditCount(strStartDates, strEndDates, status, name, phone);
		pageVo.setTotal(auditCount);
		pageVo.setRows(list);
		return pageVo;
	}

	@Override
	public Integer editOrderFinanceStatus(Long id, Integer result) {
		TOrderFinance tOrderFinance = new TOrderFinance();
		tOrderFinance.setId(id);
		tOrderFinance.setStatus(result);
		tOrderFinanceMapper.updateByPrimaryKeySelective(tOrderFinance);
		return CommonCode.Audit.SUCCESS;
	}
}

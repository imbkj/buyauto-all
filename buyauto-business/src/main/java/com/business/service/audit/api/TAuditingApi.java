package com.business.service.audit.api;

import com.buyauto.dao.user.TOrderFinanceDao;
import com.buyauto.dao.user.TUserFinanceDao;
import com.buyauto.entity.audit.TAuditing;
import com.buyauto.entity.user.TUsers;
import com.buyauto.util.pojo.PageVo;
import com.buyauto.util.pojo.UserSessionInfoBackend;

public interface TAuditingApi {

	/**
	 * 分页条件查询审核列表
	 * @param page 页码
	 * @param rows 一页条数
	 * @param type 类型
	 * @param strStartDate 开始时间
	 * @param strEndDate   结束时间
	 * @param result      查看全部或未审核
	 * @return
	 */
	PageVo<TAuditing> queryAudit(int page, int rows, String type,
			String strStartDate, String strEndDate, String result);
	/**
	 * 修改审核信息
	 * @param auditing  审核信息
	 * @param loginUser 登录人对象
	 * @return
	 */
	int updateResult(TAuditing auditing, UserSessionInfoBackend loginUser);
	
	/**
	 * 新增审核数据
	 * @param result 审核结果0通过1不通过
	 * @param type 审核类型    0经销商注册,1订单审核，2订单取消审核
	 * @param id	对应ID
	 * @param operName  操作人姓名
	 * @param operId	操作人ID
	 * @param info		原因
	 * @return
	 */
	int insertAudit(int result,int type,Long id,String operName,Long operId,String info);

	/**
	 * 根据对应ID查询原因
	 * @param mid
	 * @return
	 */
	String getNotPassInfo(Long mid);
	
	/**
	 * 根据ID查询审核意见
	 * @param id 主键ID
	 * @return
	 */
	String getInfoById(Long id);
	
	/**
	 * 分页条件查询审核列表
	 * @param page 页码
	 * @param rows 一页条数
	 * @param status 类型  0待审核,1可用,2审核不通过,3冻结
	 * @param strStartDate 开始时间
	 * @param strEndDate   结束时间
	 * @return
	 */
	PageVo<TUsers> auditCommpanyList(int page, int rows, String status, String strStartDate, String strEndDate);
	
	/**
	 * 冻结经销商
	 * @param id
	 * @return
	 */
	Integer frozenCommpany(Long id,int status);
	/**
	 * 根据对应ID查询原因
	 * @param mid
	 * @return
	 */
	TAuditing getCheckInfo(Long mid);
	
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
	PageVo<TUsers> supplierList(int page, int rows, String status, String selfSupport, String strStartDate, String strEndDate);
	
	/**
	 * 修改供应商自营状态
	 * @param id
	 * @param support 自营供应商:0否1是
	 * @return
	 */
	Integer support(Long id, Integer support);
	

	/**
	 * 冻结/解冻供应商
	 * @param id
	 * @param status 状态 1可用 3冻结
	 * @return
	 */
	Integer frozenSupplier(Long id, Integer status);
	
	/**
	  * 分页条件查询个人审核列表
	  * @param page 页码
	  * @param rows 一页条数
	  * @param strStartDate 开始时间
	  * @param strEndDate   结束时间
	  * @param status 0待审核,1可用,2审核不通过,3冻结
	  * @return
	  */
	PageVo<TUsers> personalList(int page, int rows, String status, String strStartDate, String strEndDate);
	/**
	 * 冻结/解冻个人
	 * @param id
	 * @param status 状态 1可用 3冻结
	 * @return
	 */
	Integer frozenPersonal(Long id, Integer status);
	/**
	 * 邀请个人升级
	 * @param id
	 * @return
	 */
	Integer levelUpPersonal(Long id);
	/**
	 * 根据对应ID查询个人审核原因
	 * @return
	 */
	String getNotPassInfoForPersonal(Long mid);
	/**
	 * 修改状态
	 * @param id
	 * @param result 1通过 2不通过
	 * @return
	 */
	Integer editStatus(Long id, Integer result);
	/**
	 * 修改状态
	 * @param id
	 * @param result 1通过 2不通过
	 * @return
	 */
	Integer editConfigStatus(Long id, Integer result);
	/**
	 * 分页条件查询供应商融资审核列表
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
     * @param name
	 *            姓名
	 * @param phone
	 *            手机
	 * @return
	 */
	PageVo<TUserFinanceDao> supplierFinanceList(int page, int rows, String status, String name, String phone,
			String strStartDate, String strEndDate);
	
	Integer editUserFinanceStatus(Long id, Integer result);
	
	PageVo<TOrderFinanceDao> personalFinanceList(int page, int rows, String status, String name, String phone,
			String strStartDate, String strEndDate);
	
	Integer editOrderFinanceStatus(Long id, Integer result);
	
}

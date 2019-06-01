package com.controller.audit;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.business.service.audit.api.TAuditingApi;
import com.business.service.orders.api.ITOredersApi;
import com.business.service.products.api.IProductsApi;
import com.business.service.sys.api.ISysEngineApi;
import com.business.service.user.api.TMessageApi;
import com.business.service.user.api.TUsersApi;
import com.buyauto.dao.user.TOrderFinanceDao;
import com.buyauto.dao.user.TUserFinanceDao;
import com.buyauto.entity.audit.TAuditing;
import com.buyauto.entity.orders.TOrderFinance;
import com.buyauto.entity.orders.TOrders;
import com.buyauto.entity.products.TProducts;
import com.buyauto.entity.user.TUserFinance;
import com.buyauto.entity.user.TUsers;
import com.buyauto.entity.user.TUsersSales;
import com.buyauto.util.anno.AuthAnno;
import com.buyauto.util.method.AuthBackend;
import com.buyauto.util.method.CommonCode;
import com.buyauto.util.method.CustomDateEditor;
import com.buyauto.util.method.SessionUtil;
import com.buyauto.util.method.StringUtil;
import com.buyauto.util.pojo.FinanceDto;
import com.buyauto.util.pojo.PageVo;
import com.buyauto.util.pojo.UserSessionInfoBackend;
import com.buyauto.util.web.UrlRegulation;
import com.controller.orders.TOrdersController;
import com.controller.other.OtherController;
import com.external.msg.api.ISendMessageApi;
import com.google.common.collect.Maps;

/**
 * 审核
 */
@Controller
@RestController
@RequestMapping(UrlRegulation.SecurityPrefix.HTTP + UrlRegulation.RequestPrefix.REQ_LOGIN
		+ UrlRegulation.PrefixBackendBusiness.AUDIT)
public class TAuditingController {

	@Autowired
	private TAuditingApi tAuditingImpl;

	@Autowired
	private TUsersApi tUsersImpl;

	@Autowired
	private IProductsApi productsApi;

	@Autowired
	private OtherController otherController;

	@Autowired
	private TMessageApi messageImpl;

	@Autowired
	private ISysEngineApi sysEngineApi;

	@Qualifier("sendMessage")
	@Autowired
	private ISendMessageApi sendMessage;

	@Autowired
	private ITOredersApi ordersApi;
	
	@Autowired
	private TOrdersController tOrdersController;

	@Autowired
	private FinanceDto financeDto;

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(TAuditingController.class);

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(true));
	}

	/**
	 * 前往审核页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/auditList", method = RequestMethod.GET)
	@AuthAnno(rules = { AuthBackend.AUDITLIST })
	public ModelAndView auditList(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		return new ModelAndView("audit/auditList");
	}

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
	@RequestMapping(value = "/getAuditList", method = RequestMethod.GET)
	@ResponseBody
	public PageVo<TAuditing> getAuditList(HttpServletRequest request, HttpSession session, HttpServletResponse response,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int rows,
			@RequestParam(value = "strStartDate", required = false) String strStartDate,
			@RequestParam(value = "strEndDate", required = false) String strEndDate,
			@RequestParam(value = "result", required = false) String result,
			@RequestParam(value = "type", required = false) String type) {
		return tAuditingImpl.queryAudit(page, rows, type, strStartDate, strEndDate, result);
	}

	/**
	 * 查询审核原因
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getPassInfo")
	@ResponseBody
	public String getPassInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam(value = "id") Long id) {
		String info = tAuditingImpl.getInfoById(id);
		return info;
	}

	/**
	 * 前往经销商审核页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/commpanyAudit")
	@AuthAnno(rules = { AuthBackend.Distributor })
	public ModelAndView commpanyAudit(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		return new ModelAndView("audit/commpanyAudit");
	}

	/**
	 * 分页条件查询经销商审核列表
	 * 
	 * @param page
	 *            页码
	 * @param rows
	 *            一页条数
	 * @param type
	 *            类型 0待审核,1可用,2审核不通过,3冻结
	 * @param strStartDate
	 *            开始时间
	 * @param strEndDate
	 *            结束时间
	 * @return
	 */
	@RequestMapping(value = "/auditCommpanyList", method = RequestMethod.GET)
	@ResponseBody
	public PageVo<TUsers> auditCommpanyList(HttpServletRequest request, HttpSession session,
			HttpServletResponse response, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int rows,
			@RequestParam(value = "strStartDate", required = false) String strStartDate,
			@RequestParam(value = "strEndDate", required = false) String strEndDate,
			@RequestParam(value = "status", required = false) String status) {
		return tAuditingImpl.auditCommpanyList(page, rows, status, strStartDate, strEndDate);
	}

	/**
	 * 获取员工信息
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/getUserById")
	public TUsers getUserById(HttpServletRequest request, HttpSession session, HttpServletResponse response,
			@RequestParam(value = "id") Long id) {
		return tUsersImpl.getById(id);
	}

	/**
	 * 审核 经销商/供应商
	 * 
	 * @param id
	 * @param info
	 *            信息
	 * @param result
	 *            结果
	 * @return
	 */
	@RequestMapping(value = "/auditCompany", method = RequestMethod.POST)
	@ResponseBody
	public String auditCompany(HttpServletRequest request, HttpSession session, HttpServletResponse response,
			@RequestParam(value = "id") Long id, @RequestParam(value = "result") Integer result,
			@RequestParam(value = "info") String info, @RequestParam(value = "type") Integer type) {
		UserSessionInfoBackend user = SessionUtil.getUserSessionInfoFromBackend(session);
		Integer c = tUsersImpl.editStatus(id, result);
		if (!StringUtil.isEmpty(type) && type.equals(CommonCode.Audit.AUDITTYPEONE)) {
			type = CommonCode.Audit.AUDITTYPEONE;
		} else {
			type = CommonCode.Audit.AUDITTYPEFORE;
		}
		c = tAuditingImpl.insertAudit(result, type, id, user.getName(), user.getId(), info);
		String phone = tUsersImpl.getPhoneById(id);
		Map<String, String> map = Maps.newHashMap();
		// 经销商审核不通过发送通知
		if (result == CommonCode.UserState.STATUS_NOTAUDIT) {
			otherController.sendMsg(phone, "AuditNotCompany", map);
		} else {
			otherController.sendMsg(phone, "AuditCompany", map);
		}
		return c.toString();
	}

	/**
	 * 产品审核
	 * 
	 * @param request
	 * @param session
	 * @param response
	 * @param id
	 * @param result
	 * @param info
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "/productExamine", method = RequestMethod.POST)
	@ResponseBody
	public String productExamine(HttpServletRequest request, HttpSession session, HttpServletResponse response,
			@RequestParam(value = "id") Long id, @RequestParam(value = "result") Integer result,
			@RequestParam(value = "info") String info, @RequestParam(value = "type") Integer type) {
		UserSessionInfoBackend user = SessionUtil.getUserSessionInfoFromBackend(session);
		// 对产品进行修改
		Integer feedback = productsApi.toExamine(id, result);
		if (feedback == CommonCode.UserResult.SUCCESS) {
			// 插入审核表记录
			feedback = tAuditingImpl.insertAudit(result, type, id, user.getName(), user.getId(), info);
		}
		return feedback.toString();
	}
	
	@RequestMapping(value = "/productExamineFare", method = RequestMethod.POST)
	@ResponseBody
	public String productExamineFare(HttpServletRequest request, HttpSession session, HttpServletResponse response,
			@RequestParam(value = "id") Long id, @RequestParam(value = "result") Integer result,
			@RequestParam(value = "info") String info, @RequestParam(value = "type") Integer type,
			@RequestParam(value = "personal") String personal,@RequestParam(value = "sales") String sales,
			@RequestParam(value = "distributor") String distributor) {
		UserSessionInfoBackend user = SessionUtil.getUserSessionInfoFromBackend(session);
		// 对产品进行修改
		Integer feedback = productsApi.toExamineFare(id, result,personal,sales,distributor);
		if (feedback == CommonCode.UserResult.SUCCESS) {
			// 插入审核表记录
			feedback = tAuditingImpl.insertAudit(result, type, id, user.getName(), user.getId(), info);
		}
		return feedback.toString();
	}
	
	

	/**
	 * 根据对应ID查询审核原因
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getInfoByMid")
	@ResponseBody
	public String getInfoByMid(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam(value = "id") Long mid) {
		String info = tAuditingImpl.getNotPassInfo(mid);
		return info;
	}

	/**
	 * 冻结/解冻经销商
	 * 
	 * @param id
	 * @param status
	 *            状态 1可用 3冻结
	 * @return
	 */
	@RequestMapping("/frozenCommpany")
	@ResponseBody
	public String frozenCommpany(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam(value = "id") Long id, @RequestParam(value = "status") Integer status) {
		Integer code = tAuditingImpl.frozenCommpany(id, status);
		return code.toString();
	}

	/**
	 * 前往供应商商管理页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/supplierAudit")
	@AuthAnno(rules = { AuthBackend.SUPPLIER })
	public ModelAndView supplierAudit(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		return new ModelAndView("audit/supplierAudit");
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
	@RequestMapping(value = "/supplierList", method = RequestMethod.GET)
	@ResponseBody
	public PageVo<TUsers> supplierList(HttpServletRequest request, HttpSession session, HttpServletResponse response,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int rows,
			@RequestParam(value = "strStartDate", required = false) String strStartDate,
			@RequestParam(value = "strEndDate", required = false) String strEndDate,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "selfSupport", required = false) String selfSupport) {
		return tAuditingImpl.supplierList(page, rows, status, selfSupport, strStartDate, strEndDate);
	}

	/**
	 * 修改供应商自营状态
	 * 
	 * @param id
	 * @param support
	 *            自营供应商:0否1是
	 * @return
	 */
	@RequestMapping("/support")
	@ResponseBody
	public String support(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam(value = "id") Long id, @RequestParam(value = "support") Integer support) {
		Integer code = tAuditingImpl.support(id, support);
		return code.toString();
	}

	/**
	 * 冻结/解冻供应商
	 * 
	 * @param id
	 * @param status
	 *            状态 1可用 3冻结
	 * @return
	 */
	@RequestMapping("/frozenSupplier")
	@ResponseBody
	public String frozenSupplier(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam(value = "id") Long id, @RequestParam(value = "status") Integer status) {
		Integer code = tAuditingImpl.frozenSupplier(id, status);
		return code.toString();
	}

	/**
	 * 前往个人用户管理页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/personalAudit")
	@AuthAnno(rules = { AuthBackend.PERSONAL })
	public ModelAndView personalAudit(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		return new ModelAndView("audit/personalAudit");
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
	@RequestMapping(value = "/personalList", method = RequestMethod.GET)
	@ResponseBody
	public PageVo<TUsers> personalList(HttpServletRequest request, HttpSession session, HttpServletResponse response,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int rows,
			@RequestParam(value = "strStartDate", required = false) String strStartDate,
			@RequestParam(value = "strEndDate", required = false) String strEndDate,
			@RequestParam(value = "status", required = false) String status) {
		return tAuditingImpl.personalList(page, rows, status, strStartDate, strEndDate);
	}

	/**
	 * 获取个人用户信息
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/getPersonalById")
	public TUsers getPersonalById(HttpServletRequest request, HttpSession session, HttpServletResponse response,
			@RequestParam(value = "id") Long id) {
		return tUsersImpl.getPersonalById(id);
	}

	/**
	 * 冻结/解冻个人
	 * 
	 * @param id
	 * @param status
	 *            状态 1可用 3冻结
	 * @return
	 */
	@RequestMapping("/frozenPersonal")
	@ResponseBody
	public String frozenPersonal(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam(value = "id") Long id, @RequestParam(value = "status") Integer status) {
		Integer code = tAuditingImpl.frozenPersonal(id, status);
		return code.toString();
	}

	/**
	 * 邀请个人升级
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/levelUpPersonal")
	@ResponseBody
	public String levelUpPersonal(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam(value = "id") Long id) {
		Integer code = tAuditingImpl.levelUpPersonal(id);
		return code.toString();
	}

	/**
	 * 根据对应ID查询个人审核原因
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getInfoByMidForPersonal")
	@ResponseBody
	public String getInfoByMidForPersonal(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam(value = "id") Long mid) {
		String info = tAuditingImpl.getNotPassInfoForPersonal(mid);
		return info;
	}

	/**
	 * 审核个人
	 * 
	 * @param id
	 * @param info
	 *            信息
	 * @param result
	 *            结果
	 * @return
	 */
	@RequestMapping(value = "/auditPersonal", method = RequestMethod.POST)
	@ResponseBody
	public String auditPersonal(HttpServletRequest request, HttpSession session, HttpServletResponse response,
			@RequestParam(value = "id") Long id, @RequestParam(value = "result") Integer result,
			@RequestParam(value = "info") String info) {
		UserSessionInfoBackend user = SessionUtil.getUserSessionInfoFromBackend(session);
		Integer c = tAuditingImpl.editStatus(id, result);
		tAuditingImpl.insertAudit(result, CommonCode.Audit.AUDITTYPEFIVE, id, user.getName(), user.getId(), info);
		if (result == CommonCode.SupplierFinanceStatus.PASS) {
			info = "尊贵的客户，恭喜您，已成为个人销售！";
			tUsersImpl.editUserLevelById(id, null, CommonCode.SysLevel.SELLER);
			TUsersSales tUsersSales = tUsersImpl.getSalerById(id);
			TUsers tUsers = new TUsers();
			tUsers.setName(tUsersSales.getName());
			tUsers.setId(id);
			tUsers.setUpdateDate(new Date());
			tUsersImpl.editUserById(tUsers);
		} else if (result == CommonCode.SupplierFinanceStatus.NOTPASS) {
			info = "尊贵的客户，非常抱歉，您未能通过升级个人销售的审核，原因：" + info;
		}
		messageImpl.sendInsideMsg(id, info, CommonCode.MsgType.NONE);
		// 个人升级审核发送通知
		// String phone = tUsersImpl.getPhoneById(id);
		// Map<String, String> map = Maps.newHashMap();
		// if(result == CommonCode.UserState.STATUS_NOTAUDIT){
		// otherController.sendMsg(phone, "AuditNotCompany", map);
		// }else{
		// otherController.sendMsg(phone, "AuditCompany", map);
		// }
		return c.toString();
	}

	/**
	 * 审核佣金配置
	 * 
	 * @param id
	 * @param info
	 *            信息
	 * @param result
	 *            结果
	 * @return
	 */
	@RequestMapping(value = "/auditCommission", method = RequestMethod.POST)
	@ResponseBody
	public String auditCommission(HttpServletRequest request, HttpSession session, HttpServletResponse response,
			@RequestParam(value = "id") Long id, @RequestParam(value = "result") Integer result,
			@RequestParam(value = "info") String info) {
		UserSessionInfoBackend user = SessionUtil.getUserSessionInfoFromBackend(session);
		Integer c = tAuditingImpl.editConfigStatus(id, result);
		if (result == CommonCode.Audit.PASS) {
			// 审核通过修改实际数据
			sysEngineApi.editConfigByKey(id);
		}
		tAuditingImpl.insertAudit(result, CommonCode.Audit.AUDITTYPESIX, id, user.getName(), user.getId(), info);
		return c.toString();
	}

	/**
	 * 前往个人用户管理页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/supplierFinanceAudit")
	@AuthAnno(rules = { AuthBackend.SUPPLIERFINANCE })
	public ModelAndView supplierFinanceAudit(HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
		return new ModelAndView("audit/supplierFinanceAudit");
	}

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
	@RequestMapping(value = "/supplierFinanceList", method = RequestMethod.POST)
	@ResponseBody
	public PageVo<TUserFinanceDao> supplierFinanceList(HttpServletRequest request, HttpSession session,
			HttpServletResponse response, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int rows,
			@RequestParam(value = "strStartDate", required = false) String strStartDate,
			@RequestParam(value = "strEndDate", required = false) String strEndDate,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "phone", required = false) String phone) {
		return tAuditingImpl.supplierFinanceList(page, rows, status, name, phone, strStartDate, strEndDate);
	}

	/**
	 * 审核供应商融资
	 * 
	 * @param id
	 * @param info
	 *            信息
	 * @param result
	 *            结果
	 * @return
	 */
	@RequestMapping(value = "/auditSupplierFinance", method = RequestMethod.POST)
	@ResponseBody
	public String auditSupplierFinance(HttpServletRequest request, HttpSession session, HttpServletResponse response,
			@RequestParam(value = "id") Long id, @RequestParam(value = "result") Integer result,
			@RequestParam(value = "info") String info) {
		UserSessionInfoBackend user = SessionUtil.getUserSessionInfoFromBackend(session);
		Integer c = tAuditingImpl.editUserFinanceStatus(id, result);
		tAuditingImpl.insertAudit(result, CommonCode.Audit.AUDITTYPESEVEN, id, user.getName(), user.getId(), info);
		TUserFinance tUserFinance = tUsersImpl.getUserFinanceUidById(id);
		if (result == CommonCode.SupplierFinanceStatus.PASS) {
			info = "尊贵的客户，您的" + tUserFinance.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP) + "元融资申请已经通过审核！";
		} else if (result == CommonCode.SupplierFinanceStatus.NOTPASS) {
			info = "尊贵的客户，非常抱歉，您的" + tUserFinance.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP) + "元融资申请未能通过审核，原因："
					+ info;
		}

		messageImpl.sendInsideMsg(tUserFinance.getUserId(), info, CommonCode.MsgType.NONE);
		return c.toString();
	}

	/**
	 * 前往个人用户管理页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/personalFinanceAudit")
	@AuthAnno(rules = { AuthBackend.PERSONALFINANCE })
	public ModelAndView personalFinanceAudit(HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
		return new ModelAndView("audit/personalFinanceAudit");
	}

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
	@RequestMapping(value = "/personalFinanceList", method = RequestMethod.POST)
	@ResponseBody
	public PageVo<TOrderFinanceDao> personalFinanceList(HttpServletRequest request, HttpSession session,
			HttpServletResponse response, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int rows,
			@RequestParam(value = "strStartDate", required = false) String strStartDate,
			@RequestParam(value = "strEndDate", required = false) String strEndDate,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "phone", required = false) String phone) {
		return tAuditingImpl.personalFinanceList(page, rows, status, name, phone, strStartDate, strEndDate);
	}

	/**
	 * 审核供应商融资
	 * 
	 * @param id
	 * @param info
	 *            信息
	 * @param result
	 *            结果
	 * @return
	 */
	@RequestMapping(value = "/auditPersonalFinance", method = RequestMethod.POST)
	@ResponseBody
	public String auditPersonalFinance(HttpServletRequest request, HttpSession session, HttpServletResponse response,
			@RequestParam(value = "id") Long id, @RequestParam(value = "result") Integer result,
			@RequestParam(value = "info") String info) {
		UserSessionInfoBackend user = SessionUtil.getUserSessionInfoFromBackend(session);
		Integer c = tAuditingImpl.editOrderFinanceStatus(id, result);
		tAuditingImpl.insertAudit(result, CommonCode.Audit.AUDITTYPESEVEN, id, user.getName(), user.getId(), info);
		TOrderFinance tOrderFinance = tUsersImpl.getOrderFinanceUidById(id);
		if (result == CommonCode.SupplierFinanceStatus.PASS) {
			info = "尊贵的客户，您的" + tOrderFinance.getLoanAmount().setScale(2, BigDecimal.ROUND_HALF_UP) + "元融资申请已经通过审核！";
		} else if (result == CommonCode.SupplierFinanceStatus.NOTPASS) {
			tOrdersController.orderStatusChg(request, response, id, CommonCode.SaveStatus.CANCELORDER, CommonCode.SaveStatus.WAITDEPOSIT, null);
			info = "尊贵的客户，非常抱歉，您的" + tOrderFinance.getLoanAmount().setScale(2, BigDecimal.ROUND_HALF_UP)
					+ "元融资申请未能通过审核，原因：" + info;
		}

		messageImpl.sendInsideMsg(tOrderFinance.getUserId(), info, CommonCode.MsgType.NONE);
		return c.toString();
	}

	/**
	 * 计算贷款方案价格
	 * 
	 * @param amount
	 *            总价
	 * @param term
	 *            年限
	 * @return
	 */
	@RequestMapping(value = "/getCarFinance", method = RequestMethod.GET)
	public Map<String, Object> carFinanceCalculate(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "loanAmount", required = true) BigDecimal amount,
			@RequestParam(value = "id", required = true) Long orderId,
			@RequestParam(value = "term", required = true) Integer term) {
		Map<String, Object> carFinance = ordersApi.carFinanceCalculate(amount, term, financeDto);
		TOrders tOrders = ordersApi.getOrderById(orderId);
		TProducts tProducts = productsApi.getProductById(tOrders.getProductId());
		BigDecimal otherPay = tProducts.getMustConfigurePrice().add(tOrders.getConfigurePrice())
				.add(tOrders.getPremiums());
		carFinance.put("otherPay", otherPay);
//		BigDecimal firstMonPay = null;
//		if (carFinance.get("firstMonPay") != null) {
//			firstMonPay = otherPay.add((BigDecimal) carFinance.get("firstMonPay"));
//		} 
//		carFinance.put("firstMonPay", firstMonPay);
		return carFinance;
	}
}

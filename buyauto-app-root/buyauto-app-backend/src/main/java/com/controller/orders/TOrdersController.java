package com.controller.orders;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.business.service.audit.api.TAuditingApi;
import com.business.service.orders.api.ITCommissionApi;
import com.business.service.orders.api.ITOrdersCSVApi;
import com.business.service.orders.api.ITOredersApi;
import com.business.service.products.api.IProductsApi;
import com.business.service.user.api.TUsersApi;
import com.buyauto.dao.orders.TOrdersDao;
import com.buyauto.entity.orders.TOrdersFiles;
import com.buyauto.entity.user.TUsers;
import com.buyauto.util.anno.AuthAnno;
import com.buyauto.util.method.AuthBackend;
import com.buyauto.util.method.CommonCode;
import com.buyauto.util.method.CustomDateEditor;
import com.buyauto.util.method.SessionUtil;
import com.buyauto.util.pojo.BaseSftpDto;
import com.buyauto.util.pojo.PageVo;
import com.buyauto.util.pojo.UserSessionInfoBackend;
import com.buyauto.util.web.UrlRegulation;
import com.controller.other.OtherController;

@Controller
@RestController
@RequestMapping(UrlRegulation.SecurityPrefix.HTTP
		+ UrlRegulation.RequestPrefix.REQ_LOGIN
		+ UrlRegulation.PrefixBackendBusiness.ORDERS)
public class TOrdersController {
	
	private static final Logger logger = LoggerFactory.getLogger(TOrdersController.class);
	
	@Autowired
	private ITOredersApi ordersApi;
	
	@Autowired
	private ITCommissionApi tCommissionImpl;
	
	@Autowired
	private TAuditingApi auditingApi;
	
	@Autowired
	private BaseSftpDto ftpbean;
	
	@Autowired
	private ITOrdersCSVApi iTOrdersCSVApi;
	
	@Autowired
	private TUsersApi tUsersApi;
	
	@Autowired
	private OtherController otherController;
	
	@Autowired
	private IProductsApi productApi;
	
	@Autowired
	private ITOrdersCSVApi ordersCSVApi;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(true));
	}
	/**
	 * 前往订单管理
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "toOrders", method = RequestMethod.GET)
	@AuthAnno(rules = { AuthBackend.ORDERLIST })
	public ModelAndView toOrdersMgr(HttpServletRequest request,
			HttpServletResponse response) {
		return new ModelAndView("orders/ordersList");
	}
	
	/**
	 * 分页查询订单列表
	 * @param page 当前页
	 * @param rows 显示行数
	 * @param strStartDate 开始时间
	 * @param strEndDate 结束时间
	 * @param state 订单状态
	 * @return
	 */
	@RequestMapping(value = "ordersList", method = RequestMethod.POST)
	@ResponseBody
	public PageVo<TOrdersDao> orderList(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int rows,
			@RequestParam(value = "orderId", required = false) Long orderId,
			@RequestParam(value = "companyId", required = false) Long companyId,
			@RequestParam(value = "strStartDate", required = false) String strStartDate,
			@RequestParam(value = "strEndDate", required = false) String strEndDate,
			@RequestParam(value = "state", required = false) Integer state) {

		return ordersApi.queryOrders(null,companyId,orderId,page, rows, strStartDate, strEndDate, state);
	}
	/**
	 * 订单详情
	 * @param id
	 * @return
	 */
	@RequestMapping(value="orderDetial",method = RequestMethod.GET)
	public TOrdersDao orderDetial(@RequestParam Long id){
		return ordersApi.getOrdersById(id);
	}
	/**
	 * 修改订单状态
	 * @param request
	 * @param response
	 * @param id 订单id
	 * @param status 要修改的订单状态
	 * @return
	 */
	@RequestMapping(value="orderChg",method=RequestMethod.POST)
	public Integer orderStatusChg(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "oldStatus", required = false) Integer oldStatus,
			@RequestParam(value = "orderPayStatus", required = false) Integer orderPayStatus){
		int msg=CommonCode.operateRes.YES;
		try {
			//TODO
			TOrdersDao order = ordersApi.getOrdersById(id);
			int orderChgRes=ordersApi.orderChg(id, status,oldStatus,orderPayStatus);
			if (orderChgRes < CommonCode.operateRes.YES) {
				msg=CommonCode.operateRes.NO;
			}else{
				//尾款审核通过 直接开始计算佣金
				if(status ==CommonCode.SaveStatus.COMPLETE ){
					tCommissionImpl.commCalculate(id.toString());
				}
				if (status == CommonCode.SaveStatus.WAITTHREECARDS
						|| status == CommonCode.SaveStatus.CANCELORDER
						|| status == CommonCode.SaveStatus.SECONDCHK
						|| status == CommonCode.SaveStatus.TAILMOENYUNPASS) {
					// 订单尾款审核通过发短信
					otherController.sendMessage(request, response, order, status);
				}
				if (status == CommonCode.SaveStatus.NEWCREATE || status == CommonCode.SaveStatus.WAITORDERS) {
					Boolean bool = iTOrdersCSVApi.createOrderCsv(id, status, ftpbean);
					if (bool) {
						if (status == CommonCode.SaveStatus.NEWCREATE) {
							Boolean boolOrderChg = ordersApi.orderChg(id, CommonCode.SaveStatus.CONFIRMING ,null,null) == CommonCode.DBSuccess.Success?true:false;
							System.out.println("-------------订单状态修改为确认中结果:" + boolOrderChg);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return msg;
	}
	/**
	 * 审核取消订单
	 * @param id 订单
	 * @param result 审核结果
	 * @param type 审核类型
	 * @param operName 操作员
	 * @param operId 操作员id
	 * @param info 审核意见
	 * @return
	 */
	@RequestMapping(value="cancelOrders",method=RequestMethod.POST)
	public Integer cancelOrder(HttpServletRequest request,
			HttpSession session,
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "result",required = false) Integer result,
			@RequestParam(value = "type",required = false) Integer type,
			@RequestParam(value = "info",required = false) String info ){
		
		int msg=CommonCode.operateRes.YES;
		try {
			//获取当前登录用户
			UserSessionInfoBackend loginUser=SessionUtil.getUserSessionInfoFromBackend(session);
			//插入订单取消理由
			int cancelRes=ordersApi.cancelOrders(result, type, id, loginUser.getName(), loginUser.getId(), info);
			if (cancelRes<CommonCode.operateRes.YES) {
				msg=CommonCode.operateRes.NO;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return msg;
	}
	
	/**
	 * 获得取消原因
	 * @param request
	 * @param id 订单id
	 * @return
	 */
	@RequestMapping(value="getCancelRea",method=RequestMethod.GET)
	public String getCancelR(HttpServletRequest request,
			@RequestParam(value = "id", required = false) Long id){
		//从审核表中获取
		return auditingApi.getNotPassInfo(id);
	}
	/**
	 * 经销商列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="getCompanys" , method=RequestMethod.GET)
	public List<TUsers> getCompanyList(HttpServletRequest request){
		return ordersApi.companyList();
	}
	
	/**
	 * （修改车辆状态为已发货）
	 * @param request
	 * @param carId 车辆编号
	 * @return
	 */
	@RequestMapping(value="changeCarStatus" , method=RequestMethod.POST)
	public Boolean changeCar(HttpServletRequest request,@RequestParam(value="carId",required = false) Long carId){
		return ordersApi.changeCarStatus(carId);
	}
	/**
	 * 保存订单上传的凭证
	 * @param request
	 * @param response
	 * @param session
	 * @param orderId 订单id
	 * @param type 凭证类型
	 * @param depositBackImageH  图片标识
	 * @param depositBackImageOld
	 * @return
	 */
	@RequestMapping(value="saveOrdersImg",method=RequestMethod.POST)
	public Boolean saveOrdersCertificate(HttpServletRequest request, HttpServletResponse response,HttpSession session,
			@RequestParam(value = "orderId", required = false) Long orderId,
			@RequestParam(value = "type", required = false) Integer type,
			@RequestParam(value = "depositBackImageH",required = false) String depositBackImageH,
			@RequestParam(value = "depositBackImageOld",required = false) String depositBackImageOld){
		
		return ordersApi.addOrdersFiles(orderId, type, depositBackImageH, depositBackImageOld);
	}
	/**
	 *获取订单凭证 
	 * @param request
	 * @param response
	 * @param session
	 * @param orderId 订单编号
	 * @param type 订单图片类型
	 * @return
	 */
	@RequestMapping(value="getOrdersImages", method=RequestMethod.POST)
	public List<TOrdersFiles> getOrdersFiles(HttpServletRequest request, HttpServletResponse response,HttpSession session,
			@RequestParam(value = "orderId", required = false) Long orderId,
			@RequestParam(value = "type", required = false) Integer type){
		
		return ordersApi.queryOrdersFiles(orderId, type);
	}
	/**
	 * 订单取消审核不通过短信通知该销售
	 * @param request
	 * @param response
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value = "msgToSeller", method = RequestMethod.GET)
	public void msgToSeller(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@RequestParam(value = "orderId", required = false) Long orderId) {
		TOrdersDao order = ordersApi.getOrdersById(orderId);
		otherController.sendMessage(request, response, order, CommonCode.SaveStatus.CANCELCHECKNOTPASS);
	}
	
}

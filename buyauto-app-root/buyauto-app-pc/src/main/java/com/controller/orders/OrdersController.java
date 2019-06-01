package com.controller.orders;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.business.service.orders.api.ITCommissionApi;
import com.business.service.orders.api.ITOredersApi;
import com.buyauto.dao.orders.TOrdersDao;
import com.buyauto.entity.audit.TAuditing;
import com.buyauto.entity.orders.TOrderFinance;
import com.buyauto.entity.orders.TOrdersFiles;
import com.buyauto.entity.user.TUsers;
import com.buyauto.util.anno.AuthAnno;
import com.buyauto.util.method.AuthBackend;
import com.buyauto.util.method.CommonCode;
import com.buyauto.util.method.SessionUtil;
import com.buyauto.util.pojo.FinanceDto;
import com.buyauto.util.pojo.PageVo;
import com.buyauto.util.pojo.UploadPath;
import com.buyauto.util.pojo.UserSessionInfo;
import com.buyauto.util.web.UrlRegulation;
import com.controller.other.OtherController;
import com.external.orders.api.ITOrdersPcApi;
import com.external.products.api.IProductsPcApi;

@RestController
@RequestMapping(value =UrlRegulation.SecurityPrefix.HTTP + UrlRegulation.RequestPrefix.REQ_LOGIN + UrlRegulation.BizPrefix.ORDERS )
public class OrdersController {
	private static final Logger logger = LoggerFactory.getLogger(OrdersController.class);
	
	@Qualifier("orderService")
	@Autowired
	private ITOrdersPcApi ordersPcApi;
	
	@Autowired
	private UploadPath uploadPath;
	
	@Autowired
	private ITCommissionApi tCommissionImpl;
	
	@Qualifier("productService")
	@Autowired
	private IProductsPcApi productsPcApi;
	
	@Autowired
	private OtherController otherController;
	
	@Autowired
	private ITOredersApi ordersApi;

	@Autowired
	private FinanceDto financeDto;
	/**
	 * 跳转到我的订单页面
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/ordersList")
	@AuthAnno(rules = { AuthBackend.PERSONALCOMMISSION })
	public ModelAndView toOrders(HttpServletRequest request, HttpServletResponse response, HttpSession session){
		return new ModelAndView("car/car_indent");
		//return new ModelAndView("car/my_order");
	}
	
	@RequestMapping(value = "/myOrder", method = RequestMethod.GET)
	@AuthAnno(rules = { AuthBackend.MYORDER })
	public ModelAndView toProductsList(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("car/my_order");
		return modelAndView;
	}
	
	
	/**
	 * 跳转到经销商账房
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/myAccount")
	@AuthAnno(rules = { AuthBackend.ORDEROPERATION })
	public ModelAndView toAccount(HttpServletRequest request, HttpServletResponse response, HttpSession session){
		return new ModelAndView("user/accountant");
	}
	
	/**
	 * 跳转到经销商订单管理
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/myOrdersMgr")
	@AuthAnno(rules = { AuthBackend.ORDERMANAGEMENT })
	public ModelAndView OrdersMgr(HttpServletRequest request, HttpServletResponse response, HttpSession session){
		return new ModelAndView("user/order_mange");
	}
	/**
	 * 经商上订单管理
	 * @param request
	 * @param response
	 * @param pageNumber
	 * @param pageSize
	 * @param userId 销售id
	 * @param state 状态
	 * @return
	 */
	@RequestMapping(value="/getOrders",method=RequestMethod.POST)
	public PageVo<TOrdersDao> getOrders(HttpServletRequest request, HttpServletResponse response,
			HttpSession session,
			@RequestParam("page") Integer pageNumber,
			@RequestParam("rows") Integer pageSize,
			@RequestParam(value="userId", required = false) Long userId,
			@RequestParam(value = "state", required = false) Integer state
			){
		//订单列表
		//获取当前登录用户
		UserSessionInfo userLogin=SessionUtil.getUserSessionInfoFromPc(session);
		PageVo<TOrdersDao> ordersList=ordersPcApi.ordersList(userId, userLogin.getCompanyId(), pageNumber, pageSize, state , null);
		return ordersList;
	}
	
	/**
	 * 我的订单
	 * @param request
	 * @param response
	 * @param pageNumber
	 * @param pageSize
	 * @param state 状态
	 * @param searchTitle 产品标题 
	 * @return
	 */
	@RequestMapping(value="/myOrders",method=RequestMethod.POST)
	public PageVo<TOrdersDao> myOrders(HttpServletRequest request, HttpServletResponse response,
			HttpSession session,
			@RequestParam("page") Integer pageNumber,
			@RequestParam("rows") Integer pageSize,
			@RequestParam(value = "state", required = false) Integer state,
			@RequestParam(value = "searchTitle", required = false) String searchTitle
			){
		//订单列表
		//获取当前登录用户
		UserSessionInfo userLogin=SessionUtil.getUserSessionInfoFromPc(session);
		PageVo<TOrdersDao> ordersList=ordersPcApi.ordersList(userLogin.getId(), userLogin.getCompanyId(), pageNumber, pageSize, state , searchTitle);
		return ordersList;
	}
	
	/**
	 * 获取订单详情(我的订单)
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/orderDetial/{id}",method=RequestMethod.GET)
	@AuthAnno(rules = { AuthBackend.MYORDER })
	public ModelAndView orderDetital(HttpServletRequest request,HttpSession session, HttpServletResponse response,@PathVariable(value="id") Long id){
		ModelAndView mav=new ModelAndView("car/car_indent_deta");
		//根据用户登录信息判断用户需要返回的页面
		//TODO
		UserSessionInfo userLogin=SessionUtil.getUserSessionInfoFromPc(session);
		//取得用户角色
		Integer position = productsPcApi.queryUserPosition(userLogin.getId());
		//判断用户角色进行不同的跳转
		if(position ==null){
			return new ModelAndView("other/404");
		}else if(position == CommonCode.UserPosition.PERSONAL){
			mav=new ModelAndView("car/car_indent_deta");
		}else if(position == CommonCode.UserPosition.SUPPLIER){
			mav=new ModelAndView("car/car_orderinfo_deta");
		}else if(position == CommonCode.UserPosition.ADMIN|| position == CommonCode.UserPosition.SALE || position == CommonCode.UserPosition.FINANCE ){
			mav=new ModelAndView("car/car_order_info");
		}else {
			return new ModelAndView("other/404");
		}
		
		
		//订单详情
		TOrdersDao order=ordersPcApi.getOrderByid(id);
		if (order == null) {
			return new ModelAndView("other/404");
		}
		TOrderFinance tOrderFinance = ordersPcApi.getOrderFinanceByid(id);
		int financeType = CommonCode.IsFinance.FALSE;
		if(tOrderFinance != null){
			Map<String, Object> carFinance = ordersApi.carFinanceCalculate(tOrderFinance.getLoanAmount(), tOrderFinance.getTerm(), financeDto);
			carFinance.put("term", tOrderFinance.getTerm());
			mav.addObject("carFinance", carFinance);
			financeType = CommonCode.IsFinance.TRUE;
		}
		//三证信息
		List<TOrdersFiles> carThree = ordersPcApi.queryOrdersFiles(id, CommonCode.OrdersFilesType.CARCERTIFICATE);
		//订单审核言论
		TAuditing tAuditing = ordersPcApi.getCheckInfo(id);
		mav.addObject("order",order);
		mav.addObject("uploadPath", uploadPath.getShowPath());
		mav.addObject("carThree", carThree);
		mav.addObject("tAuditing",tAuditing);
		mav.addObject("financeType", financeType);
		return mav;
	}
	
	/**
	 * 获取订单详情(经销商订单管理或者账房)
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/orderInfo/{id}",method=RequestMethod.GET)
	public ModelAndView orderInfo(HttpServletRequest request,HttpSession session, HttpServletResponse response,@PathVariable(value="id") Long id){
		ModelAndView mav=new ModelAndView("user/order_info");
		//获取当前登录用户
		UserSessionInfo userLogin=SessionUtil.getUserSessionInfoFromPc(session);
		//订单详情
		TOrdersDao order=ordersPcApi.getOrderByid(id);
		if (order == null) {
			return new ModelAndView("other/404");
		}
		//三证信息
		List<TOrdersFiles> carThree = ordersPcApi.queryOrdersFiles(id, CommonCode.OrdersFilesType.CARCERTIFICATE);
		//订单审核言论
		TAuditing tAuditing = ordersPcApi.getCheckInfo(id);
		mav.addObject("order",order);
		mav.addObject("uploadPath", uploadPath.getShowPath());
		mav.addObject("carThree", carThree);
		mav.addObject("tAuditing",tAuditing);
		mav.addObject("position", userLogin.getPosition());
		return mav;
	}
	/**
	 * 修改订单状态
	 * @param request
	 * @param response
	 * @param id 订单id
	 * @param status 要修改的订单状态
	 * @return
	 */
	@RequestMapping(value="/orderChg",method=RequestMethod.POST)
	public Integer orderStatusChg(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "oldStatus", required = false) Integer oldStatus,
			@RequestParam(value = "orderPayStatus", required = false) Integer orderPayStatus) {
		int msg = CommonCode.operateRes.YES;
		try {
			int orderChgRes = ordersPcApi.orderChg(id, status, oldStatus, orderPayStatus);
			
			if (orderChgRes < CommonCode.operateRes.YES) {
				msg = CommonCode.operateRes.NO;
			}else{
				/*定金待审核和尾款待审核的时候短信通知后台审核人员*/
				//尾款审核通过 直接开始计算佣金
				if(status ==CommonCode.SaveStatus.COMPLETE ){
					tCommissionImpl.commCalculate(id.toString());
				}
				if(status == CommonCode.SaveStatus.WAITCHK || status == CommonCode.SaveStatus.CHECKTAILMONEY){
					//TODO 财务手机号
					otherController.msgToBackend(request, response, id,null, status);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return msg;
	}
	/**
	 * 销售取消订单
	 * @param request
	 * @param response
	 * @param orderId
	 * @param cancelReason
	 * @return
	 */
	@RequestMapping(value="/cancelOrder",method = RequestMethod.POST)
	public Integer cancelOrderBySeller(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "orderId", required = false) Long orderId,
			@RequestParam(value = "cancelReason", required = false) String cancelReason){
		//TODO 发送消息给运营
		int msg = CommonCode.operateRes.YES;
		int cancelOrderRes = ordersPcApi.cancelOrderBySeller(orderId, cancelReason);
		if (cancelOrderRes < CommonCode.operateRes.YES) {
			msg = CommonCode.operateRes.NO;
		}
		return msg;
	}
	
	/**
	 * 获得订单审核结论
	 * @param request
	 * @param id 订单id
	 * @return
	 */
	@RequestMapping(value="/getCheckInfo",method=RequestMethod.GET)
	public String getCheckInfo(HttpServletRequest request,
			@RequestParam(value = "orderId", required = false) Long orderId){
		//从审核表中获取
		TAuditing tAuditing = ordersPcApi.getCheckInfo(orderId);
		return tAuditing.getInfo();
	}
	
	/**
	 * 保存订单上传的凭证（定金、尾款）
	 * @param request
	 * @param response
	 * @param session
	 * @param orderId 订单id
	 * @param type 凭证类型
	 * @param depositBackImageH  图片标识
	 * @param depositBackImageOld
	 * @return
	 */
	@RequestMapping(value="/saveOrdersImg",method=RequestMethod.POST)
	public Boolean saveOrdersCertificate(HttpServletRequest request, HttpServletResponse response,HttpSession session,
			@RequestParam(value = "orderId", required = false) Long orderId,
			@RequestParam(value = "type", required = false) Integer type,
			@RequestParam(value = "depositBackImageH",required = false) String depositBackImageH,
			@RequestParam(value = "depositBackImageOld",required = false) String depositBackImageOld){
		
		return ordersPcApi.addOrdersFiles(orderId, type, depositBackImageH, depositBackImageOld);
	}
	/**
	 *获取订单凭证（定金、尾款、车辆三证）
	 * @param request
	 * @param response
	 * @param session
	 * @param orderId 订单编号
	 * @param type 订单图片类型
	 * @return
	 */
	@RequestMapping(value="/getOrdersImages", method=RequestMethod.POST)
	public List<TOrdersFiles> getOrdersFiles(HttpServletRequest request, HttpServletResponse response,HttpSession session,
			@RequestParam(value = "orderId", required = false) Long orderId,
			@RequestParam(value = "type", required = false) Integer type){
		
		return ordersPcApi.queryOrdersFiles(orderId, type);
	}
	/**
	 * 等待支付定金
	 * @param request
	 * @param response
	 * @param id 订单编号
	 * @return
	 */
	@RequestMapping(value="/orderPayment/{orderId}",method=RequestMethod.GET)
	public ModelAndView orderPay(HttpServletRequest request, HttpServletResponse response,@PathVariable(value="orderId")Long orderId){
		ModelAndView modelAndView=new ModelAndView("car/car_pay");
		//订单信息
		TOrdersDao order=ordersPcApi.getOrderByid(orderId);
		//商品缩略图
		String coverImage =productsPcApi.queryProductsImg(order.getProductId(),CommonCode.ProductsImageType.COVER);
		modelAndView.addObject("uploadPath", uploadPath.getShowPath());
		modelAndView.addObject("order",order);
		modelAndView.addObject("coverImage", coverImage);
		modelAndView.addObject("nowDate", System.currentTimeMillis());
		return modelAndView;
	}
	/**
	 * 销售人员下拉框
	 * @param session
	 * @return
	 */
	@RequestMapping(value="getUsersByCompanyId" ,method= RequestMethod.GET)
	public List<TUsers> getUsersByCompanyId(HttpSession session){
		//获取当前登录用户
		UserSessionInfo userLogin=SessionUtil.getUserSessionInfoFromPc(session);
		return ordersPcApi.getUsersByCompanyId(userLogin.getId());
	}
	/**
	 * 经销商财务查看凭证大图
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/picDetails")
	public ModelAndView picDetails(HttpServletRequest request, HttpServletResponse response, HttpSession session){
		return new ModelAndView("user/check_info");
	}
	
}

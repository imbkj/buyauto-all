package com.controller.order;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.business.service.orders.api.ITOredersApi;
import com.buyauto.dao.orders.TOrdersDao;
import com.buyauto.entity.audit.TAuditing;
import com.buyauto.entity.orders.TOrderFinance;
import com.buyauto.entity.orders.TOrdersFiles;
import com.buyauto.entity.user.TUsers;
import com.buyauto.util.method.CommonCode;
import com.buyauto.util.method.EncodedUtile;
import com.buyauto.util.method.RedisUtil;
import com.buyauto.util.pojo.FinanceDto;
import com.buyauto.util.pojo.PageVo;
import com.buyauto.util.web.AppWebResultVo;
import com.buyauto.util.web.UrlRegulation;
import com.external.orders.api.ITOrdersPcApi;
import com.external.user.api.ITuserApi;

@RestController
@RequestMapping(value =UrlRegulation.SecurityPrefix.HTTP + UrlRegulation.RequestPrefix.REQ_LOGIN + UrlRegulation.BizPrefix.ORDERS )
public class OrdersController {
	private static final Logger logger = LoggerFactory.getLogger(OrdersController.class);
	
	@Qualifier("orderService")
	@Autowired
	private ITOrdersPcApi ordersPcApi;
	
	@Qualifier("userService")
	@Autowired
	private ITuserApi userService;
	
	@Autowired
	private ITOredersApi ordersApi;
	
	@Autowired
	private FinanceDto financeDto;
	
	/**
	 * 我的订单列表
	 * @param token
	 * @param page
	 * @param rows
	 * @param state 状态
	 * @param searchTitle 产品标题 
	 * @return
	 */
	@RequestMapping(value="/myOrders")
	public AppWebResultVo<PageVo<TOrdersDao>> myOrders(HttpServletRequest request, HttpServletResponse response,HttpSession session,
			@RequestParam("token") String token,
			@RequestParam(value="page",defaultValue = "1") Integer pageNumber,
			@RequestParam(value="rows",defaultValue = "20") Integer pageSize,
			@RequestParam(value = "state", required = false) Integer state,
			@RequestParam(value = "searchTitle", required = false) String searchTitle
			){
		Long userId = RedisUtil.getUserByKey(token);
		if(userId==null){
			return AppWebResultVo.buildFail(CommonCode.RetAppInfo.NUSEROBJ);
		}
		TUsers user = userService.getById(userId);
		if(user==null){
			return AppWebResultVo.buildFail(CommonCode.RetAppInfo.NUSEROBJ);
		}
		searchTitle = EncodedUtile.decodeUnicode(searchTitle);
		PageVo<TOrdersDao> ordersList=ordersPcApi.ordersList(userId, user.getCompanyId(), pageNumber, pageSize, state , searchTitle);
		return AppWebResultVo.buildSucc(ordersList);
	}
	
	
	/**
	 * 获取订单详情(我的订单)
	 * @param id 订单ID
 	 * @param token 用户唯一码
	 * @return
	 */
	@RequestMapping(value="/orderDetial")
	public AppWebResultVo<Map<String,Object>> orderDetital(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="id") Long id,
			@RequestParam("token") String token){
		Long userId = RedisUtil.getUserByKey(token);
		if(userId==null){
			return AppWebResultVo.buildFail(CommonCode.RetAppInfo.NUSEROBJ);
		}
		//订单详情
		TOrdersDao order=ordersPcApi.getOrderByid(id);
		if (order == null) {
			return AppWebResultVo.buildFail(CommonCode.RetAppInfo.ORDERN);
		}
		Map<String,Object> map = new HashMap<String, Object>();
		TOrderFinance tOrderFinance = ordersPcApi.getOrderFinanceByid(id);
		int financeType = CommonCode.IsFinance.FALSE;
		if(tOrderFinance != null){
			Map<String, Object> carFinance = ordersApi.carFinanceCalculate(tOrderFinance.getLoanAmount(), tOrderFinance.getTerm(), financeDto);
			carFinance.put("term", tOrderFinance.getTerm());
			map.put("carFinance", carFinance);
			financeType = CommonCode.IsFinance.TRUE;
		}
		//三证信息
		List<TOrdersFiles> carThree = ordersPcApi.queryOrdersFiles(id, CommonCode.OrdersFilesType.CARCERTIFICATE);
		//订单审核言论
		TAuditing tAuditing = ordersPcApi.getCheckInfo(id);
		map.put("order",order);
		map.put("carThree", carThree);
		map.put("tAuditing",tAuditing);
		map.put("financeType", financeType);
		return AppWebResultVo.buildSucc(map);
	}
	
	
}

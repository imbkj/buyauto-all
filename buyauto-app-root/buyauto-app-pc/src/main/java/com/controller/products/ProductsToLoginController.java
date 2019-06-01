package com.controller.products;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.business.service.orders.api.ITOredersApi;
import com.business.service.products.api.IProductsApi;
import com.buyauto.dao.products.TProductsPojo;
import com.buyauto.entity.orders.TOrders;
import com.buyauto.entity.sys.SysConfig;
import com.buyauto.util.anno.AuthAnno;
import com.buyauto.util.method.AuthBackend;
import com.buyauto.util.method.CommonCode;
import com.buyauto.util.method.SessionUtil;
import com.buyauto.util.pojo.FinanceDto;
import com.buyauto.util.pojo.UploadPath;
import com.buyauto.util.pojo.UserSessionInfo;
import com.buyauto.util.web.UrlRegulation;
import com.external.orders.api.ITOrdersPcApi;
import com.external.products.api.IProductsPcApi;
import com.external.user.api.ITuserApi;

/**
 * 产品控制层(需登录)
 */
@RestController
@RequestMapping(value = UrlRegulation.SecurityPrefix.HTTP + UrlRegulation.RequestPrefix.REQ_LOGIN
		+ UrlRegulation.BizPrefix.PRODUCTS)
public class ProductsToLoginController {

	private static final Logger logger = LoggerFactory.getLogger(ProductsToLoginController.class);

	@Qualifier("productService")
	@Autowired
	private IProductsPcApi productsPcApi;

	@Qualifier("orderService")
	@Autowired
	private ITOrdersPcApi ordersPcApi;

	@Qualifier("userService")
	@Autowired
	private ITuserApi userService;

	@Autowired
	private UploadPath uploadPath;
	@Autowired
	private ITOredersApi ordersApi;

	@Autowired
	private FinanceDto financeDto;

	@RequestMapping(value = "/carConfig", method = RequestMethod.GET)
	@AuthAnno(rules = { AuthBackend.PURCHASE })
	public ModelAndView carConfig(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("car/car_config");
		return modelAndView;
	}

	/**
	 * 配置车辆
	 * 
	 * @param productsId
	 * @return
	 */
	@RequestMapping(value = "/detail/{productsId}", method = RequestMethod.GET)
	@AuthAnno(rules = { AuthBackend.PURCHASE })
	public ModelAndView queryProductsDetailed(HttpServletRequest request, HttpServletResponse response,
			HttpSession session, @PathVariable(value = "productsId") Long productsId) {
		UserSessionInfo user = SessionUtil.getUserSessionInfoFromPc(session);
		ModelAndView modelAndView = new ModelAndView("car/car_config");
		TProductsPojo productsPojo = productsPcApi.queryProductsDetailed(productsId, user);
		if (productsPojo == null) {
			return new ModelAndView("other/404");
		}
		SysConfig sysConfigBrand = productsPcApi.querySysConfigAddress(productsPojo.getBrandId());
		List<SysConfig> sysConfigAddress = productsPcApi.querySysConfigAddress();
		modelAndView.addObject("productsPojo", productsPojo);
		modelAndView.addObject("uploadPath", uploadPath.getShowPath());
		modelAndView.addObject("sysConfigBrand", sysConfigBrand);
		modelAndView.addObject("sysConfigAddress", sysConfigAddress);
		// 此时去sysConfig表中读取保险配置1,2
		// TODO
		Map<String, Object> insurance = productsPcApi.queryInsuranceConfig();
		modelAndView.addObject("insurance", insurance);
		return modelAndView;
	}

	/**
	 * 订单-配置
	 * @param productsId 订单ID
	 * @param name 名称
	 * @param mobile 手机
	 * @param deliveryMode 配送方式 0自提 1配送
	 * @param sysConfigId 地址ID(自提不用填)
	 * @param optionalConfigure 可选配置
	 * @param extractionTime 提车时间
	 * @param extractionAddress 配送地址(自提不用填)
	 * @param insuranceType 保险  1,2
	 * @param financeType 贷款方案
	 * @param term 贷款期限
	 * @return
	 */
	@RequestMapping(value = "/confirmOrder", method = RequestMethod.POST)
	@ResponseBody
	@AuthAnno(rules = { AuthBackend.PURCHASE })
	public ModelAndView confirmProductsDetailed(HttpServletRequest request, HttpServletResponse response,
			HttpSession session, Long productsId, String name, Long mobile, String deliveryMode,
			@RequestParam(value = "sysConfigId", required = false) Long sysConfigId,
			@RequestParam(value = "optionalConfigure", required = false) String optionalConfigure,
			@RequestParam(value = "extractionTime", required = false) String extractionTime,
			@RequestParam(value = "extractionAddress", required = false) String extractionAddress,
			@RequestParam(value = "insuranceType", required = false) Integer insuranceType,
			@RequestParam(value = "financeType", required = false) Integer financeType,
			@RequestParam(value = "term", required = false) Integer term) {
		UserSessionInfo user = SessionUtil.getUserSessionInfoFromPc(session);
		ModelAndView modelAndView = new ModelAndView("car/car_affirm");
		try {
			extractionTime = new String(extractionTime.getBytes("iso8859-1"), "UTF-8");
			name = new String(name.getBytes("iso8859-1"), "UTF-8");
			extractionAddress = new String(extractionAddress.getBytes("iso8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		TProductsPojo tProductsPojo = productsPcApi.queryProductsDetailed(productsId, user);
		if (tProductsPojo == null) {
			return new ModelAndView("other/404");
		}
		// 车辆确认信息
		if (optionalConfigure != null && !optionalConfigure.isEmpty()) {
			String validateConfigure = productsPcApi.validateConfigure(tProductsPojo.getOptionalConfigure(),optionalConfigure);
			tProductsPojo.setOptionalConfigure(validateConfigure);
			
		} else {
			tProductsPojo.setOptionalConfigure(null);
		}
		if (deliveryMode.isEmpty()) {
			logger.info("提取方式有误");
			return new ModelAndView("other/404");
			// TODO 跳转错误页面
		}
		tProductsPojo.setDeliveryMode(deliveryMode);// 配送方式
		// 提取
		if (deliveryMode.equals(CommonCode.deliveryMode.EXTRACT)) {
			SysConfig sysConfigAddress = productsPcApi.querySysConfigAddress(sysConfigId);
			tProductsPojo.setSysConfigAddress(sysConfigAddress);
		}
		// 配送
		if (deliveryMode.equals(CommonCode.deliveryMode.DISTRIBUTION)) {
			tProductsPojo.setDistribution(extractionAddress);
		}
		// 确认保险方式
		if (insuranceType != null) {
			SysConfig sysConfigInsuranceType = productsPcApi.queryInsuranceType(insuranceType);
			if (sysConfigInsuranceType != null) {
				tProductsPojo.setInsuranceType(insuranceType);
				tProductsPojo.setInsuranceAmount(new BigDecimal(sysConfigInsuranceType.getScValue()));
				// 必选配置（含保险）
				tProductsPojo.setMustConfigurePrice(
						tProductsPojo.getInsuranceAmount().add(tProductsPojo.getMustConfigurePrice()));
				modelAndView.addObject("insuranceName", sysConfigInsuranceType.getScRemark());
			} else {
				logger.info("错误的保险方式");
				return new ModelAndView("other/404");
			}
		} else {
			logger.info("未选择保险方式");
			return new ModelAndView("other/404");
			// 未选择保险方式
		}
		tProductsPojo.setExtractPerson(name);
		tProductsPojo.setExtractPhone(mobile);
		tProductsPojo.setExtractDate(extractionTime);
		Map<String, Object> carFinance = ordersApi.carFinanceCalculate(tProductsPojo.getCarPrice(), term, financeDto);
		carFinance.put("term", term);
		modelAndView.addObject("carFinance", carFinance);
		modelAndView.addObject("financeType", financeType);
		modelAndView.addObject("tProductsPojo", tProductsPojo);
		modelAndView.addObject("uploadPath", uploadPath.getShowPath());
		return modelAndView;

	}

	/**
	 * 确认提交订单
	 * 
	 * @param productsId
	 * @param extractPerson
	 * @param sysConfigId
	 * @param phone
	 * @param optionalConfigure
	 * @param remarks
	 * @param extractDate
	 * @return
	 */
	@RequestMapping(value = "/orderSubmit", method = RequestMethod.POST)
	@ResponseBody
	@AuthAnno(rules = { AuthBackend.PURCHASE })
	public String orderSubmit(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			Long productsId, String extractPerson, Long phone,
			@RequestParam(value = "optionalConfigure", required = false) String optionalConfigure,
			@RequestParam(value = "remarks", required = false) String remarks,
			@RequestParam(value = "sysConfigId", required = false) Long sysConfigId,
			@RequestParam(value = "extractDate", required = false) String extractDate,
			@RequestParam(value = "delivery", required = false) String deliveryMode,
			@RequestParam(value = "distribution", required = false) String distribution,
			@RequestParam(value = "insuranceType", required = false) Integer insuranceType,
			@RequestParam(value = "financeType", required = false) Integer financeType,
			@RequestParam(value = "term", required = false) Integer term) {

		UserSessionInfo userSession = SessionUtil.getUserSessionInfoFromPc(session);
		TProductsPojo tProductsPojo = productsPcApi.queryProductsDetailed(productsId, userSession);
		if (tProductsPojo == null) {
			return null;
		}
		tProductsPojo.setExtractPhone(phone);
		if (optionalConfigure != null && !optionalConfigure.isEmpty()) {
			String validateConfigure = productsPcApi.validateConfigure(tProductsPojo.getOptionalConfigure(),
					optionalConfigure);
			tProductsPojo.setOptionalConfigure(validateConfigure);
		} else {
			tProductsPojo.setOptionalConfigure(null);
		}
		TOrders tOrders = ordersPcApi.orderSubmit(tProductsPojo, extractPerson, sysConfigId, remarks, extractDate,
				deliveryMode, distribution, userSession.getId(), insuranceType);
		
		if (tOrders == null) {
			return null;
		}
		
		//插入个人金融方案
		if(financeType == CommonCode.IsFinance.TRUE){
			ordersPcApi.saveOrderFinance(tOrders,term,userSession.getPhone(),tProductsPojo.getCarPrice());
		}
		if (!tOrders.getCancelReason().isEmpty()) {
			return tOrders.getCancelReason();
		}

		// 此时不在链接FTP 直接到达待支付定金状态

		/* 调用接口订单状态自动走到待支付定金 */
		// Boolean boolChgOrder = ordersPcApi.createOrderCsv(tOrders.getId(),
		// tOrders.getStatus(),ftpbean);
		// if (boolChgOrder) {
		// Boolean bool = ordersPcApi.modifyOrder(tOrders.getId(),
		// CommonCode.SaveStatus.CONFIRMING);
		// System.out.println("-------------订单状态修改为确认中结果:" + bool);
		// }
		return tOrders.getId().toString();
	}

	/**
	 * 计算贷款方案价格
	 * @param amount 总价
	 * @param term 年限
	 * @return
	 */
	@RequestMapping(value = "/carFinanceCalculate", method = RequestMethod.POST)
	public Map<String, Object> carFinanceCalculate(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "amount", required = true) BigDecimal amount,
			@RequestParam(value = "term", required = true) Integer term) {

		Map<String, Object> carFinance = ordersApi.carFinanceCalculate(amount, term, financeDto);
		return carFinance;
	}

}

package com.controller.products;

import java.math.BigDecimal;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.business.service.orders.api.ITOredersApi;
import com.business.service.products.api.IProductsApi;
import com.buyauto.dao.products.TProductsPojo;
import com.buyauto.entity.orders.TOrders;
import com.buyauto.entity.sys.SysConfig;
import com.buyauto.entity.user.TUsers;
import com.buyauto.util.method.CommonCode;
import com.buyauto.util.method.EncodedUtile;
import com.buyauto.util.method.RedisUtil;
import com.buyauto.util.method.StringUtil;
import com.buyauto.util.pojo.FinanceDto;
import com.buyauto.util.pojo.PageVo;
import com.buyauto.util.pojo.UploadPath;
import com.buyauto.util.pojo.UserSessionInfo;
import com.buyauto.util.web.AppWebResultVo;
import com.buyauto.util.web.UrlRegulation;
import com.external.orders.api.ITOrdersPcApi;
import com.external.products.api.IProductsPcApi;
import com.external.user.api.ITuserApi;

/**
 * 车辆产品控制层
 */
@RestController
@RequestMapping(value = UrlRegulation.SecurityPrefix.HTTP + UrlRegulation.RequestPrefix.REQ_NO_LOGIN + UrlRegulation.BizPrefix.PRODUCTS)
public class ProductsToLogoutController {

	private static final Logger logger = LoggerFactory.getLogger(ProductsToLogoutController.class);
	
	@Qualifier("productService")
	@Autowired
	private IProductsPcApi productsPcApi;
	
	@Qualifier("orderService")
	@Autowired
	private ITOrdersPcApi ordersPcApi;

	@Autowired
	private UploadPath uploadPath;

	@Autowired
	private FinanceDto financeDto;
	@Autowired
	private ITOredersApi ordersApi;
	
	@Qualifier("userService")
	@Autowired
	private ITuserApi userService;
	
	@Autowired
	private IProductsApi productsImpl;
	/**
	 * 查询车辆品牌
	 * @return
	 */
	@RequestMapping(value = "/garageBrand")
	public AppWebResultVo<List<SysConfig>> garageBrand(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		List<SysConfig> brandList = productsPcApi.queryBrandList();
		return AppWebResultVo.buildSucc(brandList);
	}
	
	/**
	 * 车库查询
	 * @param brandId 品牌
	 * @param carType 车身
	 * @param priceRange 价格区间
	 * @param sortOrder 排序方式
	 * @param title 输入
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/garageProducts")
	@ResponseBody
	public AppWebResultVo<PageVo<TProductsPojo>> garageProducts(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam(value = "brandId", required = false,defaultValue="-1") Long brandId, // 品牌
			@RequestParam(value = "carType", required = false,defaultValue="-1") Integer carType, // 车身
			@RequestParam(value = "priceRange", required = false,defaultValue="-1") Integer priceRange,// 价格区间
			@RequestParam(value = "sortOrder", required = false,defaultValue="0") Integer sortOrder,// 排序方式
			@RequestParam(value = "title", required = false) String title,// 输入
			@RequestParam(value = "token",required=false) String token,
			@RequestParam(value = "pageNumber",defaultValue="1") Integer pageNumber, @RequestParam(value="pageSize",defaultValue="15") Integer pageSize) {
		UserSessionInfo  user = null;
		Long userId = RedisUtil.getUserByKey(token);
		if(userId!=null){
			user = new UserSessionInfo(userId);
		}
		System.out.println(title);
		if(!StringUtil.isEmpty(title)){
			title = EncodedUtile.decodeUnicode(title);
			System.out.println("转码后"+title);
		}
		PageVo<TProductsPojo> tProducts = productsPcApi.querygarageProducts(brandId, carType,priceRange,sortOrder, title, pageNumber, pageSize,user);
		return AppWebResultVo.buildSucc(tProducts);
	}

	/**
	 * 根据ID查询车辆信息
	 * @param productsId
	 * @return
	 */
	@RequestMapping(value = "/carInfo")
	public AppWebResultVo<TProductsPojo> carInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			 @RequestParam(value = "productsId") String productsId,
			 @RequestParam(value = "token",required=false) String token) {
		UserSessionInfo  user = null;
		Long userId = RedisUtil.getUserByKey(token);
		if(userId!=null){
			user = new UserSessionInfo(userId);
		}
		if(StringUtil.isEmpty(productsId)){
			return AppWebResultVo.buildFail(CommonCode.RetAppInfo.NPRODUCTID);//"产品ID为空"
		}
		Long id = null;
		try {
			id = Long.valueOf(productsId);
		} catch (Exception e) {
			return AppWebResultVo.buildFail(CommonCode.RetAppInfo.NPRODUCTIDNUM);//产品ID必须为数字
		}
		TProductsPojo tProductsPojo = productsPcApi.queryExhibitionTProducts(id,user);
		if(tProductsPojo==null){
			return AppWebResultVo.buildFail(CommonCode.RetAppInfo.NQUERYPRODUCT);//"未查到该车辆信息"
		}
		SysConfig sysConfigBrand = productsPcApi.querySysConfigAddress(tProductsPojo.getBrandId());/*品牌*/
		tProductsPojo.setScValue(sysConfigBrand.getScValue());
		return AppWebResultVo.buildSucc(tProductsPojo);
	}


	/**
	 * 订购-配置车辆
	 * @param productsId
	 * @return
	 */
	@RequestMapping(value = "/detail")
	@ResponseBody
	public AppWebResultVo<Map<String,Object>> queryProductsDetailed(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam(value = "productsId") Long productsId	,
			@RequestParam(value = "token",required=false) String token) {
		Map<String,Object> map = new HashMap<String, Object>();
		UserSessionInfo  user = null;
		Long userId = RedisUtil.getUserByKey(token);
		if(userId!=null){
			user = new UserSessionInfo(userId);
		}else{
			return AppWebResultVo.buildFail(CommonCode.RetAppInfo.NUSEROBJ);//未登录
		}
		TProductsPojo productsPojo = productsPcApi.queryProductsDetailed(productsId,user);
		if(productsPojo==null){
			return AppWebResultVo.buildFail(CommonCode.RetAppInfo.NQUERYPRODUCT);//未查到该车辆信息
		}
		SysConfig sysConfigBrand = productsPcApi.querySysConfigAddress(productsPojo.getBrandId());
		productsPojo.setScValue(sysConfigBrand.getScValue());
//		List<SysConfig> sysConfigAddress =  productsPcApi.querySysConfigAddress();
		//此时去sysConfig表中读取保险配置1,2
		Map<String,Object> insurance=productsPcApi.queryInsuranceConfig();
		map.put(CommonCode.RetAppInfo.INSURANCE,insurance);
//		map.put(CommonCode.RetAppInfo.ADDRESS, sysConfigAddress);
		map.put(CommonCode.RetAppInfo.PRODUCT, productsPojo);
		return AppWebResultVo.buildSucc(map);
	}
	
	/**
	 * 订购-确认界面
	 * @param productsId 产品ID
	 * @param name  提车人姓名
	 * @param mobile 手机号
	 * @param deliveryMode 配送方式 0配送 1自提
	 * @param sysConfigId (自提必传)提车地址ID
	 * @param optionalConfigure 可选配置id逗号分隔
	 * @param extractionTime 提车时间|期望交付时间
	 * @param extractionAddress 配送地址(配送必填)
	 * @param insuranceType 1:保险配置 1 2:保险配置 2
	 * @param financeType 贷款方案  1:全款 2：贷款
	 * @param term 贷款期限(financeType=2必传) 
	 * @param remarks 留言
	 * @return
	 */
	@RequestMapping(value = "/confirmOrder")
	@ResponseBody
	public AppWebResultVo<Map<String, Object>> confirmProductsDetailed(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam(value = "productsId")Long productsId, 
			@RequestParam(value = "name")String name,
			@RequestParam(value = "mobile")Long mobile,
			@RequestParam(value = "deliveryMode")String deliveryMode,
			@RequestParam(value = "token",required=false) String token,
			@RequestParam(value = "sysConfigId",required=false) Long sysConfigId,
			@RequestParam(value = "optionalConfigure", required = false) String optionalConfigure,
			@RequestParam(value = "extractionTime",required = false)String extractionTime,
			@RequestParam(value = "extractionAddress",required = false)String extractionAddress,
			@RequestParam(value = "insuranceType",required = false)Integer insuranceType,
			@RequestParam(value = "financeType", required = false) Integer financeType,
			@RequestParam(value = "remarks",required = false)String remarks,
			@RequestParam(value = "term", required = false) Integer term){
		UserSessionInfo user = null;
		Long userId = RedisUtil.getUserByKey(token);
		if(userId!=null){
			user = new UserSessionInfo(userId);
		}else{
			return AppWebResultVo.buildFail(CommonCode.RetAppInfo.NUSEROBJ);//未登录
		}
		System.out.println(extractionTime+"  "+name+"  "+extractionAddress+" "+remarks);
		extractionTime = EncodedUtile.decodeUnicode(extractionAddress);
		name = EncodedUtile.decodeUnicode(name);
		extractionAddress = EncodedUtile.decodeUnicode(extractionAddress);
		remarks = EncodedUtile.decodeUnicode(remarks);
		System.out.println(extractionTime+"  "+name+"  "+extractionAddress+" "+remarks);
		TProductsPojo tProductsPojo = productsPcApi.queryProductsDetailed(productsId,user);
		if(tProductsPojo==null){
			return AppWebResultVo.buildFail(CommonCode.RetAppInfo.NQUERYPRODUCT);//未查到该车辆信息
		}
		// 车辆确认信息
		if (optionalConfigure != null && !optionalConfigure.isEmpty()) {
			String validateConfigure=productsPcApi.validateConfigure(tProductsPojo.getOptionalConfigure(), optionalConfigure);
			tProductsPojo.setOptionalConfigure(validateConfigure);
			//可选配置json字符串转换成对象，提供app解析
			tProductsPojo.setOptionalConfigureList(productsPcApi.convertOpt(validateConfigure));
		} else {
			tProductsPojo.setOptionalConfigure(null);
		}
		tProductsPojo.setDeliveryMode(deliveryMode);//配送方式
		//自提
		if(deliveryMode.equals(CommonCode.deliveryMode.EXTRACT)){
			if(StringUtil.isEmpty(sysConfigId)){
				return AppWebResultVo.buildFail(CommonCode.RetAppInfo.NSINCE);//(自提必传)提车地址ID
			}
			SysConfig sysConfigAddress = productsPcApi.querySysConfigAddress(sysConfigId);
			tProductsPojo.setSysConfigAddress(sysConfigAddress);
		}
		//配送
		if(deliveryMode.equals(CommonCode.deliveryMode.DISTRIBUTION)){
			if(StringUtil.isEmpty(extractionAddress)){
				return AppWebResultVo.buildFail(CommonCode.RetAppInfo.NEXTRACTIONADDRESS);//(配送必传)配送地址
			}
			tProductsPojo.setDistribution(extractionAddress);
		}
		//确认保险方式
		if(insuranceType!=null){
			SysConfig sysConfigInsuranceType= productsPcApi.queryInsuranceType(insuranceType);
			if(sysConfigInsuranceType!=null){
				tProductsPojo.setInsuranceType(insuranceType);
				tProductsPojo.setInsuranceAmount(new BigDecimal(sysConfigInsuranceType.getScValue()));
				//必选配置（含保险）
				tProductsPojo.setMustConfigurePrice(tProductsPojo.getInsuranceAmount().add(tProductsPojo.getMustConfigurePrice()));
				tProductsPojo.setInsuranceName(sysConfigInsuranceType.getScRemark());
			}else{
				logger.info("传递保险方式不正确");
				return AppWebResultVo.buildFail(CommonCode.RetAppInfo.EINSURANCE);//传递保险方式不正确
			}
		}else{
			logger.info("未选择保险方式");
			return AppWebResultVo.buildFail(CommonCode.RetAppInfo.NINSURANCE);//未选择保险方式
		}
		tProductsPojo.setExtractPerson(name);
		tProductsPojo.setExtractPhone(mobile);
		tProductsPojo.setExtractDate(extractionTime);
		Map<String, Object> map = new HashMap<String, Object>();
		if(CommonCode.IsFinance.TRUE.equals(financeType)){
			if(term==null) return AppWebResultVo.buildFail(CommonCode.RetAppInfo.NTERM);//未选在年限(贷款必传)
			Map<String, Object> carFinance = ordersApi.carFinanceCalculate(tProductsPojo.getCarPrice(), term,financeDto);
			carFinance.put("term", term);
			map.put("carFinance", carFinance);
		}
		map.put("financeType", financeType);
		map.put("products", tProductsPojo);
		map.put("remarks", remarks);
		return AppWebResultVo.buildSucc(map);
	}
	
	/**
	 * 计算贷款方案价格
	 * @param amount 总价
	 * @param term 年限
	 * @return
	 */
	@RequestMapping(value = "/carFinanceCalculate")
	public AppWebResultVo<Map<String, Object>> carFinanceCalculate(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam(value = "amount", required = true) BigDecimal amount,
			@RequestParam(value = "term", required = true) Integer term) {
		Map<String, Object> carFinance = ordersApi.carFinanceCalculate(amount,term,financeDto);
		return AppWebResultVo.buildSucc(carFinance);
	}
	
	/**
	 * 订购-确认提交订单
	 * @param productsId 产品ID
	 * @param phone 提车人电话
	 * @param optionalConfigure 可选配置(id逗号分隔)
	 * @param extractPerson 提车人(Unicode转码)
	 * @param remarks 留言(选填Unicode转码)
	 * @param extractDate 提车时间(Unicode转码)
	 * @param sysConfigId 提车地址ID(自提必传)
	 * @param distribution 配送地址 (配送必传,Unicode转码)
	 * @param deliveryMode 配送方式 1配送 0自提
	 * @param insuranceType 1:保险配置 1 2:保险配置 2
	 * @param financeType 贷款方案  1:全款 2：贷款
	 * @param term 贷款期限(financeType=2必传) 
	 * @return
	 */
	@RequestMapping(value = "/orderSubmit", method = RequestMethod.POST)
	@ResponseBody
	public AppWebResultVo<String> orderSubmit(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam(value = "productsId")Long productsId,
			@RequestParam(value = "extractPerson")String extractPerson,
			@RequestParam(value = "phone")Long phone,
			@RequestParam(value = "optionalConfigure", required = false) String optionalConfigure,
			@RequestParam(value = "remarks", required = false) String remarks,
			@RequestParam(value = "sysConfigId", required = false) Long sysConfigId,
			@RequestParam(value = "extractDate", required = false) String extractDate,
			@RequestParam(value = "deliveryMode", required = false) String deliveryMode,
			@RequestParam(value = "distribution", required = false) String distribution,
			@RequestParam(value = "insuranceType", required = false) Integer insuranceType,
			@RequestParam(value = "financeType", required = false) Integer financeType,
			@RequestParam(value = "token",required=false) String token,
			@RequestParam(value = "term", required = false) Integer term) {
		UserSessionInfo user = null;
		Long userId = RedisUtil.getUserByKey(token);
		if(userId!=null){
			user = new UserSessionInfo(userId);
		}else{
			return AppWebResultVo.buildFail(CommonCode.RetAppInfo.NUSEROBJ);//未登录
		}
		TProductsPojo tProductsPojo = productsPcApi.queryProductsDetailed(productsId, user);
		if (tProductsPojo == null) {
			return AppWebResultVo.buildFail(CommonCode.RetAppInfo.NQUERYPRODUCT);//未查到该车辆信息
		}
		tProductsPojo.setExtractPhone(phone);
		if (!StringUtil.isEmpty(optionalConfigure)) {
			String validateConfigure = productsPcApi.validateConfigure(tProductsPojo.getOptionalConfigure(),optionalConfigure);
			tProductsPojo.setOptionalConfigure(validateConfigure);
		} else {
			tProductsPojo.setOptionalConfigure(null);
		}
		extractPerson = EncodedUtile.decodeUnicode(extractPerson);
		remarks = EncodedUtile.decodeUnicode(remarks);
		extractDate = EncodedUtile.decodeUnicode(extractDate);
		distribution = EncodedUtile.decodeUnicode(distribution);
		TOrders tOrders = ordersPcApi.orderSubmit(tProductsPojo, extractPerson, sysConfigId, remarks, extractDate,
				deliveryMode, distribution,userId, insuranceType);
		if (tOrders == null) {
			return AppWebResultVo.buildFail(CommonCode.RetAppInfo.EORDER);//下单失败
		}
		if (!tOrders.getCancelReason().isEmpty()) {
			return AppWebResultVo.buildFail(tOrders.getCancelReason());//下单失败
		}
		//插入个人金融方案
		if(financeType == CommonCode.IsFinance.TRUE){
			TUsers userSession = userService.getById(userId);
			ordersPcApi.saveOrderFinance(tOrders,term,userSession.getPhone(),tProductsPojo.getCarPrice());
		}
		return AppWebResultVo.buildSucc(tOrders.getId().toString());//下单成功，返回订单ID	
	}
	

}

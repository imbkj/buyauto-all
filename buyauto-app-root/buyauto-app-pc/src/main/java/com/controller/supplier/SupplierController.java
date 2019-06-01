package com.controller.supplier;

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
import org.springframework.web.servlet.ModelAndView;

import com.business.service.orders.api.ITOredersApi;
import com.buyauto.dao.orders.TOrdersDao;
import com.buyauto.dao.products.TProductsInputPojo;
import com.buyauto.dao.products.TProductsPojo;
import com.buyauto.entity.products.TProducts;
import com.buyauto.entity.sys.SysConfig;
import com.buyauto.util.anno.AuthAnno;
import com.buyauto.util.method.AuthBackend;
import com.buyauto.util.method.CommonCode;
import com.buyauto.util.method.SessionUtil;
import com.buyauto.util.method.StringUtil;
import com.buyauto.util.pojo.PageVo;
import com.buyauto.util.pojo.UploadPath;
import com.buyauto.util.pojo.UserSessionInfo;
import com.buyauto.util.web.UrlRegulation;
import com.external.orders.api.ITOrdersPcApi;
import com.external.products.api.IProductsPcApi;

/**
 * 供应商控制层
 */
@RestController
@RequestMapping(value = UrlRegulation.SecurityPrefix.HTTP + UrlRegulation.RequestPrefix.REQ_LOGIN
		+ UrlRegulation.BizPrefix.SUPPLIER)
public class SupplierController {
	
	private static final Logger logger = LoggerFactory.getLogger(SupplierController.class);
	
	
	@Qualifier("productService")
	@Autowired
	private IProductsPcApi productsPcApi;

	@Autowired
	private UploadPath uploadPath;
	
	@Qualifier("orderService")
	@Autowired
	private ITOrdersPcApi ordersPcApi;


	/**
	 * 到供应商个人中心页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/toProductsList", method = RequestMethod.GET)
	@AuthAnno(rules = { AuthBackend.SUPPLIERPRODUCTLIST })
	public ModelAndView toProductsList(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("user_center/products_list");
		return modelAndView;
	}

	/**
	 * 到供应商个人中心添加产品页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/productsAdd", method = RequestMethod.GET)
	@AuthAnno(rules = { AuthBackend.SUPPLIERNEWMERCHANDISE })
	public ModelAndView productsAdd(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("user_center/products_list_add");
		return modelAndView;
	}

	/**
	 * 到供应商个人中心我的订单页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/myOrder", method = RequestMethod.GET)
	@AuthAnno(rules = { AuthBackend.SUPPLIERMYORDER })
	public ModelAndView myOrder(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("user_center/my_order");
		return modelAndView;
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
	@RequestMapping(value="/getMyOrders",method=RequestMethod.POST)
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
		PageVo<TOrdersDao> ordersList=ordersPcApi.supplierOrdersList(userLogin.getId(), userLogin.getCompanyId(), pageNumber, pageSize, state , searchTitle);
		return ordersList;
	}
	

	/**
	 * 到供应商草稿箱
	 * 
	 * @return
	 */
	@RequestMapping(value = "/recycleBin", method = RequestMethod.GET)
	@AuthAnno(rules = { AuthBackend.SUPPLIERDRAFTS })
	public ModelAndView recycleBin(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("user_center/recycle_bin");
		return modelAndView;
	}

	/**
	 * 到供应商回收站
	 * 
	 * @return
	 */
	@RequestMapping(value = "/draft", method = RequestMethod.GET)
	@AuthAnno(rules = { AuthBackend.SUPPLIERECYCLEBIN })
	public ModelAndView draft(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("user_center/draft_box");
		return modelAndView;
	}

	/**
	 * 到金融方案
	 * 
	 * @return
	 */
	@RequestMapping(value = "/bankingProgram", method = RequestMethod.GET)
	@AuthAnno(rules = { AuthBackend.SUPPLIERPROGRAMME })
	public ModelAndView bankingProgram(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("user_center/banking_program");
		return modelAndView;
	}
	
	/**
	 * 到预览页面
	 * @return
	 */
	@RequestMapping(value = "/carsInfo")
	public ModelAndView carsInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("user_center/cars_info");
		return modelAndView;
	}

	/**
	 * 商品列表
	 * 
	 * @param productStatus
	 *            1：商品列表 0：草稿箱 3：垃圾箱
	 * @param status
	 *            审核状态
	 * @param title
	 *            产品名称
	 * @param productsId
	 *            产品ID
	 * @param strStartDate
	 *            开始时间
	 * @param strEndDate
	 *            结束时间
	 * @param pageNumber
	 *            当前页码
	 * @param pageSize
	 *            一页条数
	 * @return
	 */
	@RequestMapping(value = "/showProductsList")
	@ResponseBody
	public PageVo<TProducts> showProductsList(HttpServletRequest request, HttpServletResponse response,
			HttpSession session, @RequestParam(value = "productStatus", defaultValue = "1") String productStatus,
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "productsId", required = false) Long productsId,
			@RequestParam(value = "strStartDate", required = false) String strStartDate,
			@RequestParam(value = "strEndDate", required = false) String strEndDate,
			@RequestParam(value = "page", defaultValue = "1") Integer pageNumber,
			@RequestParam(value = "rows", defaultValue = "20") Integer pageSize) {
		UserSessionInfo user = SessionUtil.getUserSessionInfoFromPc(session);
		return productsPcApi.queryProductsList(productStatus, title, status, productsId, strStartDate, strEndDate,
				pageNumber, pageSize, user);
	}

	/**
	 * 保存或修改产品车辆信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/saveProductsList")
	@ResponseBody
	public Map<String, Object> saveProductsList(HttpServletRequest request, HttpServletResponse response,
			HttpSession session, TProductsInputPojo tProductsInputPojo) {
		UserSessionInfo user = SessionUtil.getUserSessionInfoFromPc(session);
		Map<String, Object> map = productsPcApi.saveProductsList(tProductsInputPojo, user);
		return map;
	}

	/**
	 * 查询品牌信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/queryBrandList")
	public List<SysConfig> queryBrandList(HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
		List<SysConfig> brandList = productsPcApi.queryBrandList();
		return brandList;
	}

	/**
	 * 到修改产品信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/toEditProducts")
	@AuthAnno(rules = { AuthBackend.SUPPLIERCOMMODITYMODIFICATION })
	public ModelAndView toEditProducts(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam("id") String id) {
		if (StringUtil.isEmpty(id)) {
			return new ModelAndView("other/404");
		}
		ModelAndView modelAndView = new ModelAndView("user_center/products_list_revise");
		modelAndView.addObject("id", id);
		return modelAndView;
	}

	/**
	 * 根据ID查询产品信息
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/getProductsById")
	@ResponseBody
	public Map<String,Object> getProductsById(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam(value = "id") Long id) {
		Map<String,Object> map = new HashMap<String, Object>();
		TProductsPojo tProductsPojo = productsPcApi.getProductsById(id);
		if(tProductsPojo==null){
			map.put(CommonCode.RetAppInfo.CODE, CommonCode.RetAppInfo.NQUERYPRODUCT);
			return map;
		}
		map.put(CommonCode.RetAppInfo.CODE, CommonCode.RetAppInfo.SUCCESS);
		map.put(CommonCode.RetAppInfo.OBJ, tProductsPojo);
		return map;
	}

	/**
	 * 产品删除
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/updatadelete")
	public Boolean updatadelete(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam(value = "id") Long id) {
		return productsPcApi.updataDelete(id);
	}

	/**
	 * 产品下架
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/updataDisable")
	public Boolean updataDisable(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam("id") Long id) {
		return productsPcApi.updataDisable(id);
	}
	

	/**
	 * 更新产品上架
	 * @return
	 */
	@RequestMapping(value = "/updataEnable", method = RequestMethod.GET)
	public Boolean enable(HttpServletRequest request, HttpServletResponse response, HttpSession session, 
			@RequestParam("id") Long id) {
		return productsPcApi.updataEnable(id);
	}
	

	/**
	 * 产品还原
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/updataRestore")
	public Boolean restore(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam("id") Long id) {
		return productsPcApi.updataRestore(id);
	}

	/**
	 * 查看未通过原因
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/getReasonById")
	@ResponseBody
	public String getReasonById(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam("id") Long id) {
		return productsPcApi.getReasonById(id);
	}
	
	/**
	 * 提交审核
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/subAudit")
	@ResponseBody
	public String subAudit(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam("id") Long id) {
		return productsPcApi.subAudit(id);
	}
	

	/**
	 * 预览
	 * @param productsId 产品ID
	 * @return
	 */
	@RequestMapping(value = "/carInfo")
	@AuthAnno(rules = { AuthBackend.SUPPLIERPREVIEW })
	public ModelAndView carInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam(value = "productsId") String id) {
		ModelAndView modelAndView = new ModelAndView("user_center/cars_info");
		UserSessionInfo user = SessionUtil.getUserSessionInfoFromPc(session);
		Long productsId;
		try {
			productsId = Long.valueOf(id);
		} catch (Exception e) {
			logger.info("产品预览ID转Long出错!id:"+id);
			return new ModelAndView("other/404");
		}
		TProductsPojo tProductsPojo = productsPcApi.queryExhibitionTProducts(productsId, user);
		if(tProductsPojo==null){
			logger.info("产品预览未查到当前产品ID:"+id);
			return new ModelAndView("other/404");
		}
		SysConfig sysConfigBrand = productsPcApi.querySysConfigAddress(tProductsPojo.getBrandId());
		modelAndView.addObject("tProductsPojo", tProductsPojo);// 车辆产品信息
		modelAndView.addObject("sysConfigBrand", sysConfigBrand);// 车型
		int carNoCount = tProductsPojo.getStock();//可用库存
		modelAndView.addObject("uploadPath", uploadPath.getShowPath());
		modelAndView.addObject("carNoCount", carNoCount);
		return modelAndView;
	}

	/**
	 * 添加供应商融资申请
	 * @param amount
	 * @param port
	 * @param type
	 * @param repayment
	 * @param term
	 * @return
	 */
	@RequestMapping(value = "/supplierFinance", method = RequestMethod.POST)
	@ResponseBody
	public String supplierFinance(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam(value = "amount", required = true) String amount,
			@RequestParam(value = "port", required = false) String port,
			@RequestParam(value = "type", required = true) Integer type,
			@RequestParam(value = "repayment", required = false) Integer repayment,
			@RequestParam(value = "term", required = false) Integer term) {
		UserSessionInfo user = SessionUtil.getUserSessionInfoFromPc(session);
		Integer c = productsPcApi.addSupplierFinance(user.getId(),amount, type, port, repayment, term);
		return c.toString();
	}

}

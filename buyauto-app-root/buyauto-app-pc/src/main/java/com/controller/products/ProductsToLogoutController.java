package com.controller.products;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.buyauto.dao.products.TProductsPojo;
import com.buyauto.entity.sys.SysConfig;
import com.buyauto.util.method.SessionUtil;
import com.buyauto.util.pojo.PageVo;
import com.buyauto.util.pojo.UploadPath;
import com.buyauto.util.pojo.UserSessionInfo;
import com.buyauto.util.web.UrlRegulation;
import com.external.products.api.IProductsPcApi;

@RestController
@RequestMapping(value = UrlRegulation.SecurityPrefix.HTTP + UrlRegulation.RequestPrefix.REQ_NO_LOGIN + UrlRegulation.BizPrefix.PRODUCTS)
public class ProductsToLogoutController {

	@Qualifier("productService")
	@Autowired
	private IProductsPcApi productsPcApi;

	@Autowired
	private UploadPath uploadPath;

	@RequestMapping(value = "/garageScr", method = RequestMethod.GET)
	public ModelAndView garageScr(HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam(value = "brandId", required = false) String brandId,
			@RequestParam(value = "carType", required = false) String carType, @RequestParam(value = "priceRange", required = false) String priceRange,
			@RequestParam(value = "title", required = false) String title, @RequestParam(value = "sortOrder", required = false) String sortOrder,
			@RequestParam(value = "openMode",required = false)String openMode) {
		for (String key : request.getParameterMap().keySet()) {
			System.out.println(request.getParameterMap().get(key) + "------" + key);
		}
		ModelAndView modelAndView = new ModelAndView("car/garage_scr");
		modelAndView.addObject("brandId", brandId == null || brandId.length() == 0 ? -1 : brandId);
		modelAndView.addObject("carType", carType == null || carType.length() == 0 ? -1 : carType);
		modelAndView.addObject("priceRange", priceRange == null || priceRange.length() == 0 ? -1 : priceRange);
		modelAndView.addObject("openMode", openMode == null || openMode.length() == 0 ? -1 : openMode);
		try {
			modelAndView.addObject("title", title == null || title.length() == 0 ? "" : (new String(title.getBytes("iso8859-1"), "UTF-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		modelAndView.addObject("sortOrder", sortOrder == null || sortOrder.length() == 0 ? 0 : sortOrder);
		return modelAndView;
	}

	// @RequestMapping(value = "/garageScr/{brandId}/{carType}/{priceRange}/{title}", method = RequestMethod.GET)
	// public ModelAndView garageScrParameter(HttpServletRequest request, HttpServletResponse response, HttpSession session, @PathVariable(value = "sort") String sort,
	// @PathVariable(value = "brandId") String brandId, @PathVariable(value = "carType") String carType, @PathVariable(value = "priceRange") String priceRange,
	// @PathVariable(value = "title") String title) {
	// ModelAndView modelAndView = new ModelAndView("car/garage_scr");
	// modelAndView.addObject("brandId", brandId.length() == 0 ? -1 : brandId);
	// modelAndView.addObject("carType", carType.length() == 0 ? -1 : carType);
	// modelAndView.addObject("priceRange", priceRange.length() == 0 ? -1 : priceRange);
	// modelAndView.addObject("title", title.length() == 0 ? "" : title);
	// return modelAndView;
	// }

	/**
	 * 根据ID查询车辆信息
	 * @param productsId
	 * @return
	 */
	@RequestMapping(value = "/carInfo/{productsId}")
	public ModelAndView carInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session, @PathVariable(value = "productsId") Long productsId) {
		ModelAndView modelAndView = new ModelAndView("car/car_info");
		UserSessionInfo user = SessionUtil.getUserSessionInfoFromPc(session);
		TProductsPojo tProductsPojo = productsPcApi.queryExhibitionTProducts(productsId,user);
		if(tProductsPojo==null){
			return new ModelAndView("other/404");
		}
		SysConfig sysConfigBrand = productsPcApi.querySysConfigAddress(tProductsPojo.getBrandId());
		//查询可用车架号数量
		//int carNoCount = productsPcApi.queryCarNoCount(tProductsPojo.getProductsId());
		//查询可用库存
		int carNoCount = tProductsPojo.getStock();
		modelAndView.addObject("tProductsPojo", tProductsPojo);
		modelAndView.addObject("uploadPath", uploadPath.getShowPath());
		modelAndView.addObject("sysConfigBrand", sysConfigBrand);
		modelAndView.addObject("carNoCount", carNoCount);
		return modelAndView;
	}

	/**
	 * 车辆详情
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param productsId
	 * @return
	 */
	@RequestMapping(value = "/exhibitionTProducts/{productsId}")
	public TProductsPojo exhibitionTProducts(HttpServletRequest request, HttpServletResponse response, HttpSession session, @PathVariable(value = "productsId") Long productsId) {
		UserSessionInfo user = SessionUtil.getUserSessionInfoFromPc(session);
		TProductsPojo tProductsPojo = productsPcApi.queryExhibitionTProducts(productsId,user);
		return tProductsPojo;
	}

	/**
	 * 车库查询
	 *
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */

	@RequestMapping(value = "/garageProducts", method = RequestMethod.POST)
	@ResponseBody
	public PageVo<TProductsPojo> garageProducts(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam(value = "brandId", required = false) String brandId, // 品牌
			@RequestParam(value = "carType", required = false) String carType, // 车身
			@RequestParam(value = "priceRange", required = false) String priceRange,// 价格区间
			@RequestParam(value = "sortOrder", required = false) String sortOrder,// 排序方式
			@RequestParam(value = "title", required = false) String title,// 输入
			@RequestParam("pageNumber") Integer pageNumber, @RequestParam("pageSize") Integer pageSize) {
		UserSessionInfo user = SessionUtil.getUserSessionInfoFromPc(session);
		PageVo<TProductsPojo> tProducts = productsPcApi.querygarageProducts(Long.parseLong(brandId), Integer.parseInt(carType), Integer.parseInt(priceRange),
				Integer.parseInt(sortOrder), title, pageNumber, pageSize,user);
		return tProducts;
	}

	// @RequestMapping(value = "/garageProducts", method = RequestMethod.GET)
	// @ResponseBody
	// public PageVo<TProductsPojo> garageProducts(HttpServletRequest request, HttpServletResponse response, HttpSession session, Long brandId, Integer carType, Integer priceRange,
	// Integer sortOrder, String title, Integer pageNumber, Integer pageSize) {
	// PageVo<TProductsPojo> tProducts = productsPcApi.querygarageProducts(brandId, carType, priceRange, sortOrder, title, pageNumber, pageSize);
	// return tProducts;
	// }

	/**
	 * 查询车辆品牌
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/garageBrand", method = RequestMethod.GET)
	public List<SysConfig> garageBrand(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		List<SysConfig> brandList = productsPcApi.queryBrandList();
		return brandList;
	}

}

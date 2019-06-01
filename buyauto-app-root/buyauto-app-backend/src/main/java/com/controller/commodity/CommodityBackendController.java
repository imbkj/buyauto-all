package com.controller.commodity;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.business.service.products.api.IProductsApi;
import com.business.service.sys.api.ISysEngineApi;
import com.buyauto.dao.products.TProductsInputPojo;
import com.buyauto.dao.products.TProductsPojo;
import com.buyauto.entity.products.TProducts;
import com.buyauto.entity.products.TProductsCar;
import com.buyauto.entity.sys.SysConfig;
import com.buyauto.util.anno.AuthAnno;
import com.buyauto.util.method.AuthBackend;
import com.buyauto.util.method.CommonCode;
import com.buyauto.util.pojo.PageVo;
import com.buyauto.util.pojo.UploadPath;
import com.buyauto.util.web.UrlRegulation;

@RestController
@RequestMapping(UrlRegulation.SecurityPrefix.HTTP + UrlRegulation.RequestPrefix.REQ_LOGIN + UrlRegulation.BizPrefix.MALL)
public class CommodityBackendController {

	@Autowired
	private IProductsApi productsApi;

	@Autowired
	private ISysEngineApi sysEngineApi;

	@Autowired
	private UploadPath uploadPath;

	/**
	 * 产品页
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/showInfo", method = RequestMethod.GET)
	@AuthAnno(rules = { AuthBackend.PRODUCTLIST })
	public ModelAndView showInfo(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView modelAndView = new ModelAndView("commodity/productView");
		modelAndView.addObject("productStatus", CommonCode.ProductStatus.PRODUCT);
		return modelAndView;
		// return new ModelAndView("commodity/productView");
	}

	/**
	 * 草稿箱
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/draft", method = RequestMethod.GET)
	@AuthAnno(rules = { AuthBackend.DRAFTS })
	public ModelAndView draft(HttpServletRequest request, HttpServletResponse response,HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("commodity/productView");
		modelAndView.addObject("productStatus", CommonCode.ProductStatus.DRAFT);
		return modelAndView;
	}

	/**
	 * 回收站
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/recycle", method = RequestMethod.GET)
	@AuthAnno(rules = { AuthBackend.RECYCLEBIN })
	public ModelAndView recycle(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("commodity/productView");
		modelAndView.addObject("productStatus", CommonCode.ProductStatus.RECYCLE);
		return modelAndView;
	}

	@RequestMapping(value = "/showProductsList", method = RequestMethod.POST)
	public PageVo<TProducts> showProductsList(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam(value = "title", required = false) String title, @RequestParam(value = "productStatus", required = false) String productStatus,
			@RequestParam(value = "status", required = false) Integer status, @RequestParam(value = "strStartDate", required = false) Date strStartDate,
			@RequestParam(value = "strEndDate", required = false) Date strEndDate, @RequestParam(value = "recommend", required = false) String recommend,
			@RequestParam(value = "productsId", required = false) Long productsId, @RequestParam("page") Integer pageNumber, @RequestParam("rows") Integer pageSize) {
		return productsApi.queryProductsList(title, status, strStartDate, strEndDate, pageNumber, pageSize, productStatus, recommend, productsId,null);
	}
 
	/**
	 * 保存车辆录入
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param title
	 * @param carType
	 * @param priceRange
	 * @param position
	 * @param basicConfigure
	 * @param mustConfigure
	 * @param optionalConfigure
	 * @param carPrice
	 * @param mustConfigurePrice
	 * @param stock
	 * @param content
	 * @return
	 */
	@RequestMapping(value = "/saveProductsList", method = RequestMethod.POST)
	@ResponseBody
	public Boolean saveProductsList(HttpServletRequest request, HttpServletResponse response, HttpSession session,
	TProductsInputPojo tProductsInputPojo) {
		
		int saveState = 0;
		if (tProductsInputPojo.getDraftSave() == null || tProductsInputPojo.getDraftSave().isEmpty()) {
			// 新建
			saveState = CommonCode.SaveState.NEWLY;
		} else {
			// 草稿
			saveState = CommonCode.SaveState.DRAFT;
		}
		TProductsInputPojo p =productsApi.queryProductsList(tProductsInputPojo, saveState,CommonCode.SelfSupporting.No);
		if(p!=null){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 查询品牌信息
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/queryBrandList", method = RequestMethod.GET)
	public List<SysConfig> queryBrandList(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		List<SysConfig> brandList = sysEngineApi.queryPackList();
		return brandList;
	}

	/**
	 * 根据ID查询产品信息
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/showUpdateProducts", method = RequestMethod.GET)
	@ResponseBody
	public TProductsPojo showUpdateProducts(HttpServletRequest request, HttpServletResponse response, HttpSession session, Long id) {
		TProductsPojo tProductsPojo = productsApi.showUpdateProducts(id);
		tProductsPojo.setImgfilePath(uploadPath.getShowPath());
		return tProductsPojo;
	}

	/**
	 * 更新产品上架
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/updataEnable", method = RequestMethod.GET)
	public Boolean enable(HttpServletRequest request, HttpServletResponse response, HttpSession session, Long id) {
		return productsApi.updataEnable(id);
	}

	/**
	 * 产品下架
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/updataDisable", method = RequestMethod.GET)
	public Boolean updataDisable(HttpServletRequest request, HttpServletResponse response, HttpSession session, Long id) {
		return productsApi.updataDisable(id);
	}

	/**
	 * 产品删除
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/updatadelete", method = RequestMethod.GET)
	public Boolean updatadelete(HttpServletRequest request, HttpServletResponse response, HttpSession session, Long id) {
		return productsApi.updataDelete(id);
	}

	/**
	 * 还原到产品
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/updataRestore", method = RequestMethod.GET)
	public Boolean restore(HttpServletRequest request, HttpServletResponse response, HttpSession session, Long id) {
		return productsApi.updataRestore(id,CommonCode.SaveState.NEWLY);
	}

	/**
	 * 产品推荐
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/updataRecommend", method = RequestMethod.GET)
	public Boolean recommend(HttpServletRequest request, HttpServletResponse response, HttpSession session, Long id) {
		return productsApi.updataRecommend(id);
	}

	/**
	 * 产品取消推荐
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/updataCancelRecommend", method = RequestMethod.GET)
	public Boolean cancelRecommend(HttpServletRequest request, HttpServletResponse response, HttpSession session, Long id) {
		return productsApi.updataCancelRecommend(id);
	}

	@RequestMapping(value = "/querySelectedBrand", method = RequestMethod.GET)
	public SysConfig querySelectedBrand(HttpServletRequest request, HttpServletResponse response, HttpSession session, Long id) {
		return sysEngineApi.querySelectedBrand(id);
	}
	
	/**
	 * 查询可选配置
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/queryOptionalList", method = RequestMethod.GET)
	public List<SysConfig> queryOptionalList(HttpServletRequest request, HttpServletResponse response, HttpSession session){
		List<SysConfig> optionalList = sysEngineApi.queryOptionalList();
		return optionalList;
	}
	
	/**
	 *查询对应的车架号
	 * @param request
	 * @param response
	 * @param session
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/queryCarNo", method = RequestMethod.GET)
	public List<TProductsCar> queryCarNo(HttpServletRequest request, HttpServletResponse response, HttpSession session, Long id){
		return productsApi.queryCarNo(id);
	}

}

package com.controller.index;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.buyauto.dao.cms.CmsBannerPojo;
import com.buyauto.dao.cms.CmsNewsPojo;
import com.buyauto.dao.products.TProductsPojo;
import com.buyauto.entity.sys.SysConfig;
import com.buyauto.util.method.SessionUtil;
import com.buyauto.util.pojo.UploadPath;
import com.buyauto.util.pojo.UserSessionInfo;
import com.buyauto.util.web.UrlRegulation;
import com.external.msg.api.ISendMessageApi;
import com.external.products.api.IProductsPcApi;

@RestController
@RequestMapping(value = UrlRegulation.SecurityPrefix.HTTP + UrlRegulation.RequestPrefix.REQ_NO_LOGIN
		+ UrlRegulation.BizPrefix.INDEX)
public class IndexController {

	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

	@Qualifier("sendMessage")
	@Autowired
	private ISendMessageApi sendMessage;

	@Qualifier("productService")
	@Autowired
	private IProductsPcApi productsPcApi;

	@Autowired
	private UploadPath uploadPath;

	@RequestMapping(value = "/getIndexProductsList", method = RequestMethod.GET)
	@ResponseBody
	public List<TProductsPojo> indexProductsList(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		// 展示首页推荐
		UserSessionInfo user = SessionUtil.getUserSessionInfoFromPc(session);
		List<TProductsPojo> indexProductsList = productsPcApi
				.queryIndexRecommend(user);
		System.out.println(indexProductsList);
		return indexProductsList;
	}

	@RequestMapping(value = "/getBrandList", method = RequestMethod.GET)
	@ResponseBody
	public List<SysConfig> brandList(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		// 首页品牌展示
		List<SysConfig> brandList = productsPcApi.queryBrandList();
		return brandList;
	}

	@RequestMapping(value = "/getCommodityList", method = RequestMethod.GET)
	@ResponseBody
	public List<TProductsPojo> commodityList(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		// 展示首页的十条商品信息
		UserSessionInfo user = SessionUtil.getUserSessionInfoFromPc(session);
		List<TProductsPojo> commodityList = productsPcApi
				.queryIndexCommodityList(user);
		System.out.println("展示条数：" + commodityList.size());
		return commodityList;
	}

	@RequestMapping(value = "/getCmsBannerList", method = RequestMethod.GET)
	@ResponseBody
	public List<CmsBannerPojo> cmsBannerList(HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
		// banner
		List<CmsBannerPojo> cmsBannerList = productsPcApi.queryIndexBannerList();
		return cmsBannerList;
	}

	@RequestMapping(value = "/getCmsNewsList", method = RequestMethod.GET)
	@ResponseBody
	public List<CmsNewsPojo> cmsNewsList(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		// 新闻
		List<CmsNewsPojo> cmsNewsList = productsPcApi.queryCmsNewsList();
		return cmsNewsList;
	}

	@RequestMapping(value = "/getCmsNoticeList", method = RequestMethod.GET)
	@ResponseBody
	public List<CmsNewsPojo> cmsNoticeList(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		// 公告
		List<CmsNewsPojo> cmsNoticeList = productsPcApi.queryCmsNoticeList();
		return cmsNoticeList;
	}

	@RequestMapping(value = "/getShowAddress", method = RequestMethod.GET)
	@ResponseBody
	public String showAddress(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		// 图片展示路径
		return uploadPath.getShowPath();
	}

	@RequestMapping(value = "/dingdan")
	public ModelAndView dindan(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

		return new ModelAndView("car/car_indent");
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	public List<CmsNewsPojo> indexDataGet(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

		ModelAndView modelAndView = new ModelAndView("index", "time", new Date());
		// 展示首页推荐
		List<TProductsPojo> indexProductsList = productsPcApi.queryIndexRecommend(null);
		// 首页品牌展示
		List<SysConfig> brandList = productsPcApi.queryBrandList();
		// 展示首页的十条商品信息
		List<TProductsPojo> commodityList = productsPcApi.queryIndexCommodityList(null);
		// banner
		List<CmsBannerPojo> cmsBannerList = productsPcApi.queryIndexBannerList();
		// 新闻
		List<CmsNewsPojo> cmsNewsList = productsPcApi.queryCmsNewsList();
		// 公告
		List<CmsNewsPojo> cmsNoticeList = productsPcApi.queryCmsNoticeList();

		modelAndView.addObject("indexProductsList", indexProductsList);
		modelAndView.addObject("brandList", brandList);
		modelAndView.addObject("commodityList", commodityList);
		modelAndView.addObject("cmsBannerList", cmsBannerList);
		modelAndView.addObject("cmsNewsList", cmsNewsList);
		modelAndView.addObject("cmsNoticeList", cmsNoticeList);

		System.out.println(uploadPath.getShowPath());

		// MessageParam messageParam = new MessageParam("15370925501",
		// CommonCode.CmsType.Infomartion, "Register");
		// Map<String, String> map = Maps.newHashMap();
		// map.put("vCode", "8888");
		// messageParam.setTemplateParam(map);
		// sendMessage.sendMessageApi(messageParam);

		// return modelAndView;
		return null;
	}
}

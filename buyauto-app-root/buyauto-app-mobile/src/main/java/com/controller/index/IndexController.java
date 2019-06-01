package com.controller.index;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.buyauto.dao.cms.CmsBannerPojo;
import com.buyauto.dao.products.TProductsPojo;
import com.buyauto.util.method.CommonCode;
import com.buyauto.util.method.RedisUtil;
import com.buyauto.util.pojo.UserSessionInfo;
import com.buyauto.util.web.AppWebResultVo;
import com.buyauto.util.web.UrlRegulation;
import com.external.products.api.IProductsPcApi;

@RestController
@RequestMapping(value = UrlRegulation.SecurityPrefix.HTTP + UrlRegulation.RequestPrefix.REQ_NO_LOGIN
+ UrlRegulation.BizPrefix.INDEX)
public class IndexController {

	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

	@Qualifier("productService")
	@Autowired
	private IProductsPcApi productsPcApi;
	
	/**
	 * 获取首页数据
	 * @return
	 */
	@RequestMapping(value = "/")
	@ResponseBody
	public AppWebResultVo<Map<String,Object>> indexDate(HttpServletRequest request, HttpServletResponse response,HttpSession session,
			@RequestParam(value="token",required=false) String token) {
		Map<String,Object> map = new HashMap<String, Object>();
		UserSessionInfo  user = null;
		Long userId = RedisUtil.getUserByKey(token);
		if(userId!=null){
			user = new UserSessionInfo(userId);
		}
		// 头部banner轮播
		List<CmsBannerPojo> cmsBannerList = productsPcApi.queryIndexBannerList();
		// 展示首页热门推荐
		List<TProductsPojo> indexProductsList = productsPcApi.queryIndexRecommend(user);
		// 展示首页的十条商品信息(精选好车)
		List<TProductsPojo> commodityList = productsPcApi.queryIndexCommodityList(user);
		logger.info("首页访问,精选好车展示条数："+ commodityList.size());
		map.put(CommonCode.phoneIndex.BANNER, cmsBannerList);//轮播图
		map.put(CommonCode.phoneIndex.PRODUCTSHOT, indexProductsList);//热门推荐
		map.put(CommonCode.phoneIndex.PRODUCTSLIST, commodityList);//精选推荐
		return AppWebResultVo.buildSucc(map);
	}
		
	
}

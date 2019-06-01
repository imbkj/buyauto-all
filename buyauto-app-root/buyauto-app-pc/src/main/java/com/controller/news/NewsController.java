package com.controller.news;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.buyauto.dao.cms.NewsPojo;
import com.buyauto.entity.cms.CmsNews;
import com.buyauto.util.method.CustomDateEditor;
import com.buyauto.util.pojo.PageVo;
import com.buyauto.util.web.UrlRegulation;
import com.external.news.api.NewsApi;

/**
 * 新闻资讯公告
 */
@Controller
@RestController
@RequestMapping(value =UrlRegulation.SecurityPrefix.HTTP + UrlRegulation.RequestPrefix.REQ_NO_LOGIN + UrlRegulation.BizPrefix.NEWS )
public class NewsController {

	private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

	@Autowired
	private NewsApi newsApi;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(true));
	}
	
	/**
	 * 到资讯列表
	 * @param type 1新闻资讯2卖卡公告
	 * @param session
	 * @return
	 */
	@RequestMapping()
	public ModelAndView news(HttpServletRequest request,HttpServletResponse response, HttpSession session,
			@RequestParam(value="type",defaultValue="1") Integer type) {
		ModelAndView modelAndView = new ModelAndView("car/car_news");
		modelAndView.addObject("type", type);
		return modelAndView;
	}
	
	/**
	 * 根据类型分页查询‘新闻资讯公告’列表
	 * @param type 新闻类型： 1进口车公告 2新闻资讯 3新车资讯 4试驾评测
	 * @param pageNumber 页码
	 * @param pageSize  一页条数
	 * @return
	 */
	@RequestMapping("/list")
	public PageVo<NewsPojo> newsList(HttpServletRequest request,HttpServletResponse response, HttpSession session,
			@RequestParam(value = "type", defaultValue = "1") Integer type,
			@RequestParam(value="page",defaultValue="1") Integer pageNumber,
			@RequestParam(value="rows",defaultValue="10") Integer pageSize){
		PageVo<NewsPojo> list = newsApi.newsList(type,pageNumber,pageSize);
		return list;
	}
	
	
	/**
	 * 到资讯详情页
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping("/info")
	public ModelAndView newsDeta(HttpServletRequest request,HttpServletResponse response, HttpSession session,
			@RequestParam("id") Long id) {
		ModelAndView modelAndView = new ModelAndView("car/news_deta");
		modelAndView.addObject("id",id);
		return modelAndView;
	}
	
	/**
	 * 新闻详情 
	 * @param id
	 * @return
	 */
	@RequestMapping("getInfoById")
	public CmsNews getInfoById(HttpServletRequest request,HttpServletResponse response, HttpSession session,
			@RequestParam("id") Long id){
		CmsNews news = newsApi.getInfoById(id);
		return news;
	}
	
	
	
}

package com.controller.cms;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.business.service.cms.api.ICmsNewsApi;
import com.buyauto.dao.cms.CmsNewsPojo;
import com.buyauto.entity.cms.CmsNews;
import com.buyauto.util.anno.AuthAnno;
import com.buyauto.util.method.AuthBackend;
import com.buyauto.util.method.CommonCode;
import com.buyauto.util.method.CustomDateEditor;
import com.buyauto.util.pojo.PageVo;
import com.buyauto.util.pojo.UploadPath;
import com.buyauto.util.pojo.UserSessionInfoBackend;
import com.buyauto.util.web.UrlRegulation;
import com.controller.index.IndexController;

@RestController
@RequestMapping(UrlRegulation.SecurityPrefix.HTTP + UrlRegulation.RequestPrefix.REQ_LOGIN
		+ UrlRegulation.PrefixBackendBusiness.CMS)
public class CmsNewsController {

	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

	@Autowired
	private ICmsNewsApi cmsNewsApi;

	@Autowired
	private UploadPath uploadPath;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(true));
	}

	/**
	 * 前往新闻管理
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "toCmsNews", method = RequestMethod.GET)
	@AuthAnno(rules = { AuthBackend.JOURNALISMMANAGEMENT })
	public ModelAndView toCmsNewsMgr(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("cms/news");
	}

	/**
	 * 根据id 获取新闻信息
	 */
	@RequestMapping(value = "/news/getNews", method = RequestMethod.GET)
	@ResponseBody
	public CmsNews getNewsById(HttpServletRequest request, @RequestParam Long id) {
		// 新闻
		return cmsNewsApi.selectCmsNewsById(id);
	}

	/**
	 * 新闻列表分页展示
	 * 
	 * @param page
	 *            默认页
	 * @param rows
	 *            显示行数
	 * @param strStartDate
	 *            开始时间
	 * @param strEndDate
	 *            结束时间
	 * @param state
	 *            新闻状态
	 * @param title
	 *            新闻标题
	 * @return
	 */
	@RequestMapping(value = "/news", method = RequestMethod.POST)
	@ResponseBody
	public PageVo<CmsNewsPojo> newsList(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int rows,
			@RequestParam(value = "strStartDate", required = false) String strStartDate,
			@RequestParam(value = "strEndDate", required = false) String strEndDate,
			@RequestParam(value = "state", required = false) Integer state,
			@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "newsType", required = false) Integer newsType
			) {

		return cmsNewsApi.queryCmsNews(page, rows, strStartDate, strEndDate, state, title,newsType);
	}

	/**
	 * 删除新闻
	 * 
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/news/del", method = RequestMethod.GET)
	public Integer deleteCmsNews(HttpServletRequest request, @RequestParam Long id) {
		int msg = CommonCode.operateRes.YES;
		try {
			int delRes = cmsNewsApi.deleteCmsNewsById(id);
			if (delRes < CommonCode.operateRes.YES) {
				msg = CommonCode.operateRes.NO;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return msg;
	}
	/**
	 * 修改新闻状态 发布/禁用/启用
	 * @param request
	 * @param id
	 * @param chgWhat 修改什么
 	 * @param what 改成什么
	 * @return
	 */
	@RequestMapping(value = "/news/newsChg", method = RequestMethod.POST)
	public Integer newsStatusChg(HttpServletRequest request,
			@RequestParam Long id, @RequestParam Integer chgWhat,@RequestParam Integer what) {
			// 根据id获取到新闻
		CmsNews cmsNewsFind = cmsNewsApi.selectCmsNewsById(id);
		int updateNewsRes = cmsNewsApi.updateCmsNews(cmsNewsFind,chgWhat,what);
		return updateNewsRes;
	}

	/**
	 * 新增新闻
	 * 
	 * @param request
	 * @param cmsNews
	 * @return
	 */
	@RequestMapping(value = "/news/create", method = RequestMethod.POST)
	public Integer addNews(HttpServletRequest request, HttpSession session, CmsNews cmsNews) {
		int msg = CommonCode.operateRes.YES;
		try {
			// 获取当前操作员
			UserSessionInfoBackend sessionBackend = (UserSessionInfoBackend) session.getAttribute(UserSessionInfoBackend.SessionKey);
			// 检查新闻标题是否存在如果存在提示标题已存在
			CmsNews newsCheck = cmsNewsApi.getNewsByTitle(cmsNews.getTitle());
			if (newsCheck != null) {
				msg = CommonCode.newsStatus.ISEXIST;
			} else {
				int addNewsRes = cmsNewsApi.insertCmsNews(cmsNews, sessionBackend.getId());
				msg = addNewsRes;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return msg;
	}

	/**
	 * 编辑新闻
	 * @param request
	 * @param cmsNews
	 * @return
	 */
	@RequestMapping(value = "/news/edit", method = RequestMethod.POST)
	public Integer editNews(HttpServletRequest request, CmsNews cmsNews) {
		CmsNews newsCheck = cmsNewsApi.getNewsByTitle(cmsNews.getTitle());
		if (newsCheck != null && !newsCheck.getNewsId().equals(cmsNews.getNewsId())) {
			return CommonCode.newsStatus.ISEXIST;
		} else {
			return cmsNewsApi.updateCmsNews(cmsNews,null,null);
		}
	}

}

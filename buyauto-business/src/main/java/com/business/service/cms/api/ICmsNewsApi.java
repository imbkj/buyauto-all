package com.business.service.cms.api;

import java.util.List;

import com.buyauto.dao.cms.CmsNewsPojo;
import com.buyauto.dao.cms.NewsPojo;
import com.buyauto.entity.cms.CmsNews;
import com.buyauto.util.pojo.PageVo;

public interface ICmsNewsApi {

	Integer insertCmsNews(CmsNews cmsNews, Long operId);

	Integer updateCmsNews(CmsNews cmsNews,Integer chgWhat,Integer what);

	Integer deleteCmsNewsById(Long id);

	CmsNews selectCmsNewsById(Long id);

	/**
	 * 分页查询
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	PageVo<CmsNewsPojo> queryCmsNews(int pageNumber, int pageSize,
			String strStartDate, String strEndDate, Integer state, String title,Integer newsType);

	/**
	 * 根据新闻标题获取到新闻
	 * 
	 * @param title
	 * @return
	 */
	CmsNews getNewsByTitle(String title);

	/**
	 * 查询首页展示的新闻
	 * @return
	 */
	List<CmsNewsPojo> queryCmsNewsList();

	/**
	 * 查询首页展示的公告
	 * @return
	 */
	List<CmsNewsPojo> queryCmsNoticeList();

	
	/**
	 * 根据类型分页查询‘新闻资讯公告’列表-前台
	 * @param type 新闻类型： 1进口车公告 2新闻资讯 3新车资讯 4试驾评测
	 * @param pageNumber 页码
	 * @param pageSize  一页条数
	 * @return
	 */
	List<NewsPojo> getNewsByType(Integer type, int pageStartNumber, int pageEndNumber);

	/**
	 * 根据类型查询数量-前台
	 * @param type
	 * @return
	 */
	Integer getNewsCountByType(Integer type);

	/**
	 * 新闻详情 
	 * @param id
	 * @return
	 */
	CmsNews getInfoById(Long id);

}

package com.external.news.api;

import com.buyauto.dao.cms.NewsPojo;
import com.buyauto.entity.cms.CmsNews;
import com.buyauto.util.pojo.PageVo;

public interface NewsApi {

	/**
	 * 根据类型分页查询‘新闻资讯公告’列表
	 * @param type 新闻类型： 1进口车公告 2新闻资讯 3新车资讯 4试驾评测
	 * @param pageNumber 页码
	 * @param pageSize  一页条数
	 * @return
	 */
	PageVo<NewsPojo> newsList(Integer type, Integer pageNumber, Integer pageSize);

	/**
	 * 新闻详情 
	 * @param id
	 * @return
	 */
	CmsNews getInfoById(Long id);

}

package com.controller.news;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.business.service.cms.api.ICmsNewsApi;
import com.buyauto.dao.cms.NewsPojo;
import com.buyauto.entity.cms.CmsNews;
import com.buyauto.util.pojo.PageVo;
import com.external.news.api.NewsApi;

@Service
public class NewsImpl implements NewsApi {

	@Autowired
	private ICmsNewsApi cmsNewsEngine;
	
	/**
	 * 根据类型分页查询‘新闻资讯公告’列表
	 * @param type 新闻类型： 1进口车公告 2新闻资讯 3新车资讯 4试驾评测
	 * @param pageNumber 页码
	 * @param pageSize  一页条数
	 * @return
	 */
	@Override
	public PageVo<NewsPojo> newsList(Integer type, Integer pageNumber, Integer pageSize) {
		PageVo<NewsPojo> page = new PageVo<NewsPojo>(0,pageNumber,pageSize);
		List<NewsPojo> listDate = cmsNewsEngine.getNewsByType(type,page.getPageStartNumber(),page.getPageEndNumber());
		Integer count = cmsNewsEngine.getNewsCountByType(type);
		page.setTotal(count);
		page.setRows(listDate);
		return page;
	}

	/**
	 * 新闻详情 
	 * @param id
	 * @return
	 */
	@Override
	public CmsNews getInfoById(Long id) {
		return cmsNewsEngine.getInfoById(id);
	}

}

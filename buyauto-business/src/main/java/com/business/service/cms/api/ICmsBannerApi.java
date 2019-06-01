package com.business.service.cms.api;

import java.util.List;

import com.buyauto.dao.cms.CmsBannerPojo;
import com.buyauto.entity.cms.CmsBanner;
import com.buyauto.util.pojo.PageVo;

public interface ICmsBannerApi {
	
	
	public CmsBanner getById(Long id);

	public Integer updateBanner(CmsBanner banner, Long l, Integer isonlineNo);
	
	public Integer insertBanner(CmsBanner banner, Long l);
	
	/**
	 * 分页查询banner
	 * @param page 页码
	 * @param rows 一页条数
	 * @param page 页码
	 * @param rows 一页条数
	 * @param strStartDate 起始日期
	 * @param strEndDate   终止日期
	 * @param state 	   状态
	 * @param title		标题
	 */
	PageVo<CmsBannerPojo> queryBanner(int pageNumber, int pageSize, String strStartDate, String strEndDate, Integer state, String title);

	//编辑banner
	public Integer editBanner(CmsBanner banner, Long l);

	public CmsBanner getByTitle(String title);

	public Integer deleteById(Long id);

	/**
	 * 查询首页展示banner
	 * @return
	 */
	public List<CmsBannerPojo> queryIndexBannerList();
}

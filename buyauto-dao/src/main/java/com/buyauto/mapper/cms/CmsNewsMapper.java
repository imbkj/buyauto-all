package com.buyauto.mapper.cms;


import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.buyauto.dao.cms.CmsNewsPojo;
import com.buyauto.dao.cms.NewsPojo;
import com.buyauto.entity.cms.CmsNews;

public interface CmsNewsMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_news
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_news
     *
     * @mbggenerated
     */
    int insert(CmsNews record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_news
     *
     * @mbggenerated
     */
    int insertSelective(CmsNews record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_news
     *
     * @mbggenerated
     */
    CmsNews selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_news
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(CmsNews record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_news
     *
     * @mbggenerated
     */
    int updateByPrimaryKeyWithBLOBs(CmsNews record);

    
    /**
     * 根据新闻标题查询数据
     * @param title
     * @return
     */
    CmsNews selectCmsNewsByTitle(@Param("title") String title);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_news
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(CmsNews record);
    /**
     * 查询新闻总数
     * @return 新闻数量
     */
    int selectNewsCount(@Param("startTime") Date strStartDates,@Param("endTime")  Date strEndDates, @Param("state") Integer state,@Param("title")  String title, @Param("newsType") Integer newsType);
    /**
     * 分页查询新闻
     * @param startNumber
     * @param endNumber
     * @param title
     * @return
     */
    List<CmsNewsPojo> selectCmsNewsList(@Param("startNumber") int startNumber,@Param("endNumber") int endNumber,@Param("startTime") Date strStartDates,@Param("endTime")  Date strEndDates, @Param("state") Integer state,@Param("title")  String title, @Param("newsType") Integer newsType);

    /**
     * 查询首页展示新闻
     * @param news 
     * @return
     */
	List<CmsNewsPojo> queryCmsNewsList(@Param("news")int news);

	/**
	 * 根据类型分页查询‘新闻资讯公告’列表-前台
	 * @param type 新闻类型： 1进口车公告 2新闻资讯 3新车资讯 4试驾评测
	 * @param pageNumber 页码
	 * @param pageSize  一页条数
	 * @return
	 */
	List<NewsPojo> getNewsByType(@Param("type") Integer type,@Param("pageStartNumber") int pageStartNumber,@Param("pageEndNumber") int pageEndNumber);

	/**
	 * 根据类型查询数量-前台
	 * @param type
	 * @return
	 */
	Integer getNewsCountByType(@Param("type") Integer type);

	/**
	 * 查找新闻热点条数
	 * @return
	 */
	Integer findNewsHot(@Param("type") Integer type);
}
package com.business.service.cms.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.business.service.cms.api.ICmsNewsApi;
import com.buyauto.dao.cms.CmsNewsPojo;
import com.buyauto.dao.cms.NewsPojo;
import com.buyauto.entity.cms.CmsNews;
import com.buyauto.mapper.cms.CmsNewsMapper;
import com.buyauto.mapper.sys.SysRuleUserMapper;
import com.buyauto.util.method.CommonCode;
import com.buyauto.util.method.CustomDateEditor;
import com.buyauto.util.method.KeyUtil;
import com.buyauto.util.pojo.PageVo;
@Service
public class CmsNewsEngine implements ICmsNewsApi {

	@Autowired
	private CmsNewsMapper cmsNewsMapper;
	@Autowired
	SysRuleUserMapper sysRuleUserMapper;//用户
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(true));
	}
	
	/**
	 * 新增新闻
	 */
	@Override
	public Integer insertCmsNews(CmsNews cmsNews,Long operId) {
		Integer isHot = cmsNews.getIsHot();
		if(isHot.equals(CommonCode.CmsNewsType.ISHOT_YES)){
			Integer type = cmsNews.getType();
			Integer code = checkIsHot(type);//热点最大只能设置6条
			if(code!=null){
				return code;
			}else{
				cmsNews.setIsHot(isHot);
			}
		}
		Date now = new Date();
		cmsNews.setNewsId(KeyUtil.generateDBKey());
		//设置默认排序为1
		if (cmsNews.getOrderNum()==null) {
			cmsNews.setOrderNum(1);
		}
		//设置默认上线时间
		if (cmsNews.getStatus()==CommonCode.isOnline.isOnline_Yes) {
			cmsNews.setOnlinetime(now);
		}
		//设置修改时间为当前
		cmsNews.setUpdateTime(now);
		//设置默认创建时间
		if (cmsNews.getCreateTime()==null) {
			cmsNews.setCreateTime(now);
		}
		//设置新闻的操作员id
		cmsNews.setOperId(operId);
		Integer addNewsRes = cmsNewsMapper.insert(cmsNews);
		if (addNewsRes < CommonCode.operateRes.YES) {
			return CommonCode.operateRes.NO;
		}
		return CommonCode.operateRes.YES;
	}
	/**
	 * 修改新闻
	 */
	@Override
	public Integer updateCmsNews(CmsNews cmsNews,Integer chgWhat,Integer what) {
		//新闻类型
		Integer type = cmsNews.getType();
		//设置修改时间
		cmsNews.setUpdateTime(new Date());
		
		//快捷修改部分(发布、置顶、热点<以及取消>)
		if (chgWhat!=null && what!=null) {
			if (CommonCode.chgWhat.HOT.equals(chgWhat)) {
				if (what.equals(CommonCode.CmsNewsType.ISHOT_YES)) {
					Integer code = checkIsHot(type);//热点最大只能设置6条
					if(code!=null){
						return code;
					}else{
						cmsNews.setIsHot(what);
					}
				}else{
					cmsNews.setIsHot(what);
				}
			}else if (CommonCode.chgWhat.TOP==chgWhat) {
				cmsNews.setIsTop(what);	
			}else if(CommonCode.chgWhat.STATUS==chgWhat){
				if (what == CommonCode.newsStatus.PUBLISH) {
					//如果要发布新闻设置上线时间
					cmsNews.setStatus(what);
					//设置新闻上线时间
					cmsNews.setOnlinetime(new Date());
				}else if(what == CommonCode.newsStatus.UNPUBLISH){
					//重置上线时间
					cmsNews.setStatus(what);
					cmsNews.setIsTop(CommonCode.CmsNews.UNTOP);
					cmsNews.setIsHot(CommonCode.CmsNews.UNHOT);
				}
			}
		}else{
			//chgWhat & what为空的时候是修改方法(编辑新闻方法)
			if (cmsNews.getStatus()==CommonCode.newsStatus.PUBLISH) {
				//设置新闻上线时间
				cmsNews.setOnlinetime(new Date());
			}
			//判断热点
			Integer isHot = cmsNews.getIsHot();
			if(isHot.equals(CommonCode.CmsNewsType.ISHOT_YES)){
				Integer code = checkIsHot(type);//热点最大只能设置6条
				if(code!=null){
					return code;
				}
			}
		}		
		Integer updateNewsRes = cmsNewsMapper.updateByPrimaryKeySelective(cmsNews);
		if (updateNewsRes < CommonCode.operateRes.YES) {
			return CommonCode.operateRes.NO;
		}
		return CommonCode.operateRes.YES;
	}
	/**
	 * 新闻热点不能超过6条
	 * @return
	 */
	public Integer checkIsHot(Integer type){
		if(type.equals(CommonCode.CmsNewsType.NOTICE)){
			Integer countNotice = cmsNewsMapper.findNewsHot(CommonCode.CmsNewsType.NOTICE);//查找新闻公告热点条数
			if(countNotice>=CommonCode.CmsNews.isHOte){
				return CommonCode.CmsNews.MAX_NOTICE_CODE;//新闻
			}
		}else if(type.equals(CommonCode.CmsNewsType.NEWS)){
			Integer countInformation = cmsNewsMapper.findNewsHot(CommonCode.CmsNewsType.NEWS);//查找新闻资讯热点条数
			if(countInformation >= CommonCode.CmsNews.isHOte){
				return CommonCode.CmsNews.MAX_INFORMATION_CODE;//资讯
			}
		}
		return null;
		
	}
	
	@Override
	public Integer deleteCmsNewsById(Long id) {
		return cmsNewsMapper.deleteByPrimaryKey(id);
	}

	@Override
	public CmsNews selectCmsNewsById(Long id) {
		return cmsNewsMapper.selectByPrimaryKey(id);
	}
	/**
	 * 按条件分页查询
	 */
	@Override
	public PageVo<CmsNewsPojo> queryCmsNews(int pageNumber, int pageSize,
			String strStartDate, String strEndDate, Integer state, String title,Integer newsType) {
		PageVo<CmsNewsPojo> pageVo=new PageVo<CmsNewsPojo>(0, pageNumber, pageSize);
		Date strStartDates = null;
		Date strEndDates = null;
		//格式化时间
		if(strStartDate!=null && strEndDate!=null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				strStartDates = sdf.parse(strStartDate);
				strEndDates = sdf.parse(strEndDate);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(null !=state && -1 == state) state = null;
		if(null !=newsType && -1 == newsType) newsType = null;
		List<CmsNewsPojo> cmsNewsList=cmsNewsMapper.selectCmsNewsList(pageVo.getPageStartNumber(), pageVo.getPageEndNumber(),strStartDates,strEndDates,state,title,newsType);
 		int cmsNewsCount=cmsNewsMapper.selectNewsCount(strStartDates,strEndDates,state,title,newsType);
 		pageVo.setTotal(cmsNewsCount);
 		pageVo.setRows(getOperNameByOperId(cmsNewsList));
		return pageVo;
	}
	
	/**
	 * 根据操作人ID查找操作人姓名
	 * @return
	 */
	public List<CmsNewsPojo> getOperNameByOperId(List<CmsNewsPojo> list){
		for (CmsNewsPojo news : list) {
			Long operId = news.getOperId();//操作人ID
			String operName = sysRuleUserMapper.getNameById(operId);
			news.setOperName(operName);
		}
		return list;
	}

	@Override
	public CmsNews getNewsByTitle(String title) {
		return cmsNewsMapper.selectCmsNewsByTitle(title);
	}


	@Override
	public List<CmsNewsPojo> queryCmsNewsList() {
		return cmsNewsMapper.queryCmsNewsList(CommonCode.CmsNewsType.NEWS);
	}


	@Override
	public List<CmsNewsPojo> queryCmsNoticeList() {
		return cmsNewsMapper.queryCmsNewsList(CommonCode.CmsNewsType.NOTICE);
	}

	
	/**
	 * 根据类型分页查询‘新闻资讯公告’列表-前台
	 * @param type 新闻类型： 1进口车公告 2新闻资讯 3新车资讯 4试驾评测
	 * @param pageNumber 页码
	 * @param pageSize  一页条数
	 * @return
	 */
	@Override
	public List<NewsPojo> getNewsByType(Integer type, int pageStartNumber, int pageEndNumber) {
		return cmsNewsMapper.getNewsByType(type,pageStartNumber,pageEndNumber);
	}

	/**
	 * 根据类型查询数量-前台
	 * @param type
	 * @return
	 */
	@Override
	public Integer getNewsCountByType(Integer type) {
		return cmsNewsMapper.getNewsCountByType(type);
	}

	/**
	 * 新闻详情 -前台
	 * @param id
	 * @return
	 */
	@Override
	public CmsNews getInfoById(Long id) {
		return cmsNewsMapper.selectByPrimaryKey(id);
	}
	
	

}

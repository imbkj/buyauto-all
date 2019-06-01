package com.business.service.cms.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.business.service.cms.api.ICmsBannerApi;
import com.buyauto.dao.cms.CmsBannerPojo;
import com.buyauto.entity.cms.CmsBanner;
import com.buyauto.mapper.cms.CmsBannerMapper;
import com.buyauto.mapper.sys.SysRuleUserMapper;
import com.buyauto.util.method.CommonCode;
import com.buyauto.util.method.StringUtil;
import com.buyauto.util.pojo.PageVo;
import com.google.gson.Gson;

@Service
public class CmsBannerImpl implements ICmsBannerApi{

	@Autowired
	private CmsBannerMapper cmsBannerMapper;//banner
	@Autowired
	SysRuleUserMapper sysRuleUserMapper;//用户
	
	
	@Override
	public CmsBanner getById(Long id) {
		return cmsBannerMapper.selectByPrimaryKey(id);
	}

	@Override
	public Integer updateBanner(CmsBanner banner,Long id,Integer inOnline) {
		//如果执行上线功能
		if(inOnline!=null && inOnline.equals(CommonCode.isOnline.isOnline_Yes)){
			//查询banner上线是否超过10条
			Integer count = cmsBannerMapper.fingBannerByOnline();
			if(count>=CommonCode.CmsBanner.MAXONLINE){
				return CommonCode.CmsBanner.MAXONLINE_CODE;
			}
		}
		banner.setOperId(id);
		banner.setUpdateTime(new Date());
		banner.setStatus(inOnline);
		cmsBannerMapper.updateByPrimaryKeySelective(banner);
		return null; 
	}

	//新增banner
	@Override
	public Integer insertBanner(CmsBanner banner,Long id) {
		banner.setOperId(id);
		Integer status = banner.getStatus();
		//未上线
		if(StringUtil.isEmpty(status) || CommonCode.CmsBanner.isOnline_No.equals(status)){
			banner.setStatus(CommonCode.CmsBanner.isOnline_No);//0 未上线 1上线
		}else{
			Integer count = cmsBannerMapper.fingBannerByOnline();//查询上线banner总条数
			//如果大于10条
			if(count>=CommonCode.CmsBanner.MAXONLINE){
				return CommonCode.CmsBanner.MAXONLINE_CODE;
			}
		}
		if(banner.getOrderNum() == null){
			banner.setOrderNum(1);//排序 默认1
		}
		Date date = new Date();
		if(banner.getCreateTime() == null){
			banner.setCreateTime(date);//创建时间
		}
		banner.setUpdateTime(date);
		banner.setType(0);//默认为0首页滚动
		cmsBannerMapper.insertSelective(banner);
		return null;
	}
	
	/**
	 * 查找banner列表
	 */
	@Override
	public PageVo<CmsBannerPojo> queryBanner(int pageNumber, int pageSize,
			String strStartDate, String strEndDate, Integer state, String title) {
		PageVo<CmsBannerPojo> pageVo = new PageVo<CmsBannerPojo>(0, pageNumber, pageSize);
		Date strStartDates = null;
		Date strEndDates = null;
		if(strStartDate!=null && strEndDate!=null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				strStartDates = sdf.parse(strStartDate);
				strEndDates = sdf.parse(strEndDate);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(null !=state && -1 == state) state = null;//-1代表前台选择全部
		List<CmsBannerPojo> bannerList = cmsBannerMapper.queryBanner(pageVo.getPageStartNumber(), pageVo.getPageEndNumber()
				,strStartDates,strEndDates,state,title);
		int bannerCount = cmsBannerMapper.queryBannerCount(strStartDates,strEndDates,state,title);
		pageVo.setTotal(bannerCount);
		pageVo.setRows(getOperNameByOperId(bannerList));
		return pageVo;
	}
	
	/**
	 * 根据操作人ID查找操作人姓名
	 * @return
	 */
	public List<CmsBannerPojo> getOperNameByOperId(List<CmsBannerPojo> list){
		for (CmsBannerPojo c : list) {
			Long operId = c.getOperId();//操作人ID
			String operName = sysRuleUserMapper.getNameById(operId);
			c.setOperName(operName);
			c = getValueById(c);
		}
		return list;
	}
	
	/**
	 * 根据ID获取相应value
	 */
	public CmsBannerPojo getValueById(CmsBannerPojo c){
		if(c==null || c.getType() == null) return c;
		if(0 == c.getType()){
			c.setTypeName("首页滚动");
		}else if(1 == c.getType()){
			c.setTypeName("新闻页");
		}else if(2 == c.getType()){
			c.setTypeName("车辆详情");
		}
		return c;
	}
	
	Gson gson = new Gson();
	
	/**
	 * 根据ID查询pojo
	 */
	public CmsBannerPojo getPojoById(Long id){
		CmsBannerPojo c = cmsBannerMapper.getByid(id);
		return getValueById(c);
	}

	/**
	 * 编辑
	 */
	@Override
	public Integer editBanner(CmsBanner banner, Long id) {
		Integer status = banner.getStatus();//上线状态
		//未上线
		if(StringUtil.isEmpty(status) || CommonCode.CmsBanner.isOnline_No.equals(status)){
			banner.setStatus(CommonCode.CmsBanner.isOnline_No);//0 未上线 1上线
		}else{
			Integer count = cmsBannerMapper.fingBannerByOnline();//查询上线banner总条数
			//如果大于10条
			if(count>=CommonCode.CmsBanner.MAXONLINE){
				return CommonCode.CmsBanner.MAXONLINE_CODE;
			}
		}
				
		banner.setUpdateTime(new Date());//修改时间
		banner.setOperId(id);
		cmsBannerMapper.updateByPrimaryKeySelective(banner);
		return null;
	}
	
	/**
	 * 查找对应title
	 */
	@Override
	public CmsBanner getByTitle(String title) {
		return cmsBannerMapper.getByTitle(title);
	}

	@Override
	public Integer deleteById(Long id) {
		return cmsBannerMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 查询首页banner
	 */
	@Override
	public List<CmsBannerPojo> queryIndexBannerList() {
		return cmsBannerMapper.queryIndexBannerList();
	}

	
}

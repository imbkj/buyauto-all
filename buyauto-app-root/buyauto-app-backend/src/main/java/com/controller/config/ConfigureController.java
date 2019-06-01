package com.controller.config;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.business.service.sys.api.ISysEngineApi;
import com.buyauto.dao.sys.SysConfigPojo;
import com.buyauto.dao.sys.SysConfigUpgradeDao;
import com.buyauto.entity.sys.SysConfig;
import com.buyauto.util.anno.AuthAnno;
import com.buyauto.util.method.AuthBackend;
import com.buyauto.util.method.CommonCode;
import com.buyauto.util.pojo.PageVo;
import com.buyauto.util.pojo.UploadPath;
import com.buyauto.util.web.UrlRegulation;

@RestController
@RequestMapping(UrlRegulation.SecurityPrefix.HTTP + UrlRegulation.RequestPrefix.REQ_LOGIN
		+ UrlRegulation.BizPrefix.CONFIGURE)
public class ConfigureController {
	
	@Autowired
	private ISysEngineApi sysEngineApi;
	
	@Autowired
	private UploadPath uploadPath;

	/**
	 * 提车地址配置
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/address", method = RequestMethod.GET)
	@AuthAnno(rules = { AuthBackend.ADDRESSLIST })
	public ModelAndView showInfo(HttpServletRequest request,
			HttpServletResponse response) {
		return new ModelAndView("configure/receive_car_address");
	}
	
	/**
	 * 可选配置配置
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/optionalConfig", method = RequestMethod.GET)
	public ModelAndView optionalConfig(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("configure/receive_car_address");
		modelAndView.addObject("configureType", CommonCode.ConfigureType.OPTIONALCONFIG);
		return modelAndView;
	}

	/**
	 * 展示提车地址
	 * @param request
	 * @param response
	 * @param session
	 * @param scvalue
	 * @param state
	 * @param strStartDate
	 * @param strEndDate
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/addressList", method = RequestMethod.POST)
	public PageVo<SysConfig> addressList(HttpServletRequest request, HttpServletResponse response,
			HttpSession session, @RequestParam(value = "scvalue", required = false) String scvalue,
			@RequestParam(value="state", required = false) Integer state,
			@RequestParam(value ="strStartDate",required=false)String strStartDate,
			@RequestParam(value ="strEndDate",required=false)String strEndDate,
			@RequestParam("page") Integer pageNumber,
			@RequestParam("rows") Integer pageSize){
		
		PageVo<SysConfig> searchExamList = sysEngineApi.queryaddressList(scvalue, state, pageNumber,pageSize,strStartDate,strEndDate,CommonCode.ConfigGroup.EXTRACT_ADDRESS);
		return searchExamList;
	}
	
	/**
	 * 展示可选信息
	 * @param request
	 * @param response
	 * @param session
	 * @param scvalue
	 * @param state
	 * @param strStartDate
	 * @param strEndDate
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/optionalList", method = RequestMethod.POST)
	public PageVo<SysConfig> optionalList(HttpServletRequest request, HttpServletResponse response,
			HttpSession session, @RequestParam(value = "scvalue", required = false) String scvalue,
			@RequestParam(value="state", required = false) Integer state,
			@RequestParam(value ="strStartDate",required=false)String strStartDate,
			@RequestParam(value ="strEndDate",required=false)String strEndDate,
			@RequestParam("page") Integer pageNumber,
			@RequestParam("rows") Integer pageSize){
		PageVo<SysConfig> searchExamList = sysEngineApi.queryaddressList(scvalue, state, pageNumber,pageSize,strStartDate,strEndDate,CommonCode.ConfigGroup.OPTIONAL_CONFIG);
		return searchExamList;
	}
	
	/**
	 * 新增提车地址
	 * @param request
	 * @param response
	 * @param session
	 * @param scvalue
	 * @param scremark
	 * @return
	 */
	@RequestMapping(value = "/addOptional", method = RequestMethod.POST)
	public Integer addOptional(HttpServletRequest request, HttpServletResponse response,
			HttpSession session,SysConfig sysConfig){
		return sysEngineApi.addAddress(sysConfig,CommonCode.ConfigGroup.OPTIONAL_CONFIG,CommonCode.ConfigType.TEXT);
	}
	
	
	/**
	 * 新增可选配置
	 * @param request
	 * @param response
	 * @param session
	 * @param scvalue
	 * @param scremark
	 * @return
	 */
	@RequestMapping(value = "/addAddress", method = RequestMethod.POST)
	public Integer addAddress(HttpServletRequest request, HttpServletResponse response,
			HttpSession session,SysConfig sysConfig){
		return sysEngineApi.addAddress(sysConfig,CommonCode.ConfigGroup.EXTRACT_ADDRESS,CommonCode.ConfigType.TEXT);
	}
	
	/**
	 * 修改提车地址
	 * @param request
	 * @param response
	 * @param session
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/updataShowAddress", method = RequestMethod.GET)
	public SysConfigPojo showAddress(HttpServletRequest request, HttpServletResponse response,
			HttpSession session,Long id){
		SysConfigPojo sysConfigPojo = sysEngineApi.showAddress(id);
		sysConfigPojo.setImgfilePath(uploadPath.getShowPath());
		return sysConfigPojo;
	}
	
	
	
	/**
	 * 修改可选配置
	 * @param request
	 * @param response
	 * @param session
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/updataShowOptional", method = RequestMethod.GET)
	public SysConfigPojo showOptional(HttpServletRequest request, HttpServletResponse response,
			HttpSession session,Long id){
		SysConfigPojo sysConfigPojo = sysEngineApi.showAddress(id);
		sysConfigPojo.setImgfilePath(uploadPath.getShowPath());
		return sysConfigPojo;
	}
	
	/**
	 * sysconfig状态禁用
	 * @param request
	 * @param response
	 * @param session
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/updataDisable", method = RequestMethod.GET)
	public Boolean disable(HttpServletRequest request, HttpServletResponse response,
			HttpSession session,Long id){
		return sysEngineApi.updataDisable(id);
	}
	
	/**
	 * sysConfig状态启用
	 * @param request
	 * @param response
	 * @param session
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/updataEnable", method = RequestMethod.GET)
	public Boolean enable(HttpServletRequest request, HttpServletResponse response,
			HttpSession session,Long id){
		return sysEngineApi.updataEnable(id);
	}
	
	/**
	 * 删除提车地址(物理删除)
	 * @param request
	 * @param response
	 * @param session
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public Boolean delete(HttpServletRequest request, HttpServletResponse response,
			HttpSession session,Long id){
		return sysEngineApi.updataDelete(id);
	}
	
	
	
	@RequestMapping(value = "/carBrand", method = RequestMethod.GET)
	@AuthAnno(rules = { AuthBackend.BRANDLIST })
	public ModelAndView showBrandInfo(HttpServletRequest request,
			HttpServletResponse response) {
		return new ModelAndView("configure/receive_car_brand");
	}
	
	
	
	/**
	 * 展示车辆品牌
	 * @param request
	 * @param response
	 * @param session
	 * @param scvalue
	 * @param state
	 * @param strStartDate
	 * @param strEndDate
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/carBrandList", method = RequestMethod.POST)
	public PageVo<SysConfig> carBrandList(HttpServletRequest request, HttpServletResponse response,
			HttpSession session, @RequestParam(value = "scvalue", required = false) String scvalue,
			@RequestParam(value="state", required = false) Integer state,
			@RequestParam(value ="strStartDate",required=false)String strStartDate,
			@RequestParam(value ="strEndDate",required=false)String strEndDate,
			@RequestParam("page") Integer pageNumber,
			@RequestParam("rows") Integer pageSize){
		
		PageVo<SysConfig> searchExamList = sysEngineApi.queryaddressList(scvalue, state, pageNumber,pageSize,strStartDate,strEndDate,CommonCode.ConfigGroup.CAR_BRAND);
		return searchExamList;
	}
	
	
	/**
	 * 新增品牌
	 * @param request
	 * @param response
	 * @param session
	 * @param scvalue
	 * @param scremark
	 * @return
	 */
	@RequestMapping(value = "/addCarBrand", method = RequestMethod.POST)
	public Integer addCarBrand(HttpServletRequest request, HttpServletResponse response,
			HttpSession session,SysConfig sysConfig){
		return sysEngineApi.addAddress(sysConfig,CommonCode.ConfigGroup.CAR_BRAND,CommonCode.ConfigType.PICTURE);
	}
	
	

	/**
	 * 新增用户升降级规则
	 * @param request
	 * @param response
	 * @param session
	 * @param sysConfigUpgradeDao
	 * @return
	 */
	@RequestMapping(value ="/addUpgradeConfig",method =RequestMethod.POST)
	public Boolean addUpgradeConfig(HttpServletRequest request, HttpServletResponse response,
			HttpSession session,SysConfigUpgradeDao  sysConfigUpgradeDao){
		return  sysEngineApi.addUpgradeConfig(sysConfigUpgradeDao);
	}
	
	
	/**
	 * 查看用户升降级规则
	 * @param request
	 * @param response
	 * @param session
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/upgradeDowngradeConfigList", method = RequestMethod.POST)
	public PageVo<SysConfig> upgradeDowngradeConfigList(HttpServletRequest request, HttpServletResponse response,
			HttpSession session,
			@RequestParam("page") Integer pageNumber,
			@RequestParam("rows") Integer pageSize){
		PageVo<SysConfig> tOrdersCommission = sysEngineApi.queryCommissionConfig(CommonCode.ConfigGroup.UPGRADE_DOWNGRADE,pageNumber,pageSize);
		return tOrdersCommission;
	}
	
	/**
	 * 配置升降级规则
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/upgradeOrDowngrade", method = RequestMethod.GET)
	@AuthAnno(rules = { AuthBackend.RELEGATION })
	public ModelAndView upgradeOrDowngrade(HttpServletRequest request,
			HttpServletResponse response){
		return new ModelAndView("configure/upgrade_downgrade");
	}
	

	/**
	 * 查询已有升降级规则
	 * @param request
	 * @param response
	 * @param session
	 * @param sysConfigUpgradeDao
	 * @return
	 */
	@RequestMapping(value="/queryUpgradeConfig",method =RequestMethod.GET)
	public SysConfig queryUpgradeConfig(HttpServletRequest request, HttpServletResponse response,
			HttpSession session,Long  id){
		return sysEngineApi.queryUpgradeConfig(id);
	}
	
	/**
	 * 前往佣金比例配置页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/commission")
	@AuthAnno(rules = { AuthBackend.COMMISSIONCONFIG })
	public ModelAndView commission(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		return new ModelAndView("configure/commission");
	}
	/**
	 * 查询佣金比例列表
	 * @param request
	 * @param response
	 * @param session
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/commissionConfList", method = RequestMethod.POST)
	public PageVo<SysConfig> commissionConfList(HttpServletRequest request, HttpServletResponse response,
			HttpSession session,
			@RequestParam("page") Integer pageNumber,
			@RequestParam("rows") Integer pageSize){
		PageVo<SysConfig> tOrdersCommission = sysEngineApi.queryCommissionConfig(CommonCode.ConfigGroup.COMMISSION_CONFIG,pageNumber,pageSize);
		return tOrdersCommission;
	}
	/**
	 * 展示提车地址
	 * @param request
	 * @param response
	 * @param session
	 * @param scvalue
	 * @param state
	 * @param strStartDate
	 * @param strEndDate
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/editCommission", method = RequestMethod.POST)
	public String editCommission(HttpServletRequest request, HttpServletResponse response,
			HttpSession session,
			@RequestParam(value = "id") Long id, @RequestParam(value = "scValue") String scValue){
		Integer c = sysEngineApi.editValueById(id,scValue);
		return c.toString();
	}
}

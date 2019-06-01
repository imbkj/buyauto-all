package com.controller.cms;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.business.service.cms.api.ICmsBannerApi;
import com.buyauto.dao.cms.CmsBannerPojo;
import com.buyauto.entity.cms.CmsBanner;
import com.buyauto.util.anno.AuthAnno;
import com.buyauto.util.method.AuthBackend;
import com.buyauto.util.method.CommonCode;
import com.buyauto.util.method.CustomDateEditor;
import com.buyauto.util.method.KeyUtil;
import com.buyauto.util.method.SessionUtil;
import com.buyauto.util.pojo.FileModel;
import com.buyauto.util.pojo.PageVo;
import com.buyauto.util.pojo.UploadPath;
import com.buyauto.util.pojo.UserSessionInfoBackend;
import com.buyauto.util.web.UrlRegulation;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@RestController
@RequestMapping(UrlRegulation.SecurityPrefix.HTTP + UrlRegulation.RequestPrefix.REQ_LOGIN
		+ UrlRegulation.PrefixBackendBusiness.CMS)
public class CmsBannerController {

	@Autowired
	private ICmsBannerApi cmsBannerImpl;

	@Autowired
	private UploadPath uploadPath;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(true));
	}

	/**
	 * 到banner页
	 * 
	 * @return
	 */
	@RequestMapping("/tobanner")
	@AuthAnno(rules = { AuthBackend.BANNERMANAGEMENT })
	public ModelAndView tobanner(HttpServletRequest request,HttpSession session, HttpServletResponse response) {
		return new ModelAndView("cms/banner");
	}

	/**
	 * 分页查询banner
	 * 
	 * @param page
	 *            页码
	 * @param rows
	 *            一页条数
	 * @param page
	 *            页码
	 * @param rows
	 *            一页条数
	 * @param strStartDate
	 *            起始日期
	 * @param strEndDate
	 *            终止日期
	 * @param state
	 *            状态
	 * @param title
	 *            标题
	 */
	@RequestMapping(value = "/banner", method = RequestMethod.GET)
	@ResponseBody
	public PageVo<CmsBannerPojo> bannerList(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int rows,
			@RequestParam(value = "strStartDate", required = false) String strStartDate,
			@RequestParam(value = "strEndDate", required = false) String strEndDate,
			@RequestParam(value = "state", required = false) Integer state,
			@RequestParam(value = "title", required = false) String title, HttpSession session,
			HttpServletRequest request) {
		return cmsBannerImpl.queryBanner(page, rows, strStartDate, strEndDate, state, title);
	}

	/**
	 * banner上线
	 * 
	 * @param id
	 *            bannerID
	 * @return
	 */
	@RequestMapping(value = "/bannerUp", method = RequestMethod.GET)
	public String bannerUp(HttpSession session, HttpServletRequest request, @RequestParam Long id) {
		CmsBanner banner = cmsBannerImpl.getById(id);
		if (null == banner) {
			return CommonCode.Fail.DATAISDELETED;
		}
		UserSessionInfoBackend loginUser = SessionUtil.getUserSessionInfoFromBackend(session);
		Integer state = cmsBannerImpl.updateBanner(banner, loginUser.getId(), CommonCode.isOnline.isOnline_Yes);
		if(state==null){
			return CommonCode.SUCCESS;
		}else{
			return state.toString();//banner上线是否超过10条   101
		}
	}

	/**
	 * banner 下线
	 * 
	 * @param id
	 *            banner ID
	 * @return
	 */
	@RequestMapping(value = "/bannerDown", method = RequestMethod.GET)
	public String bannerDown(HttpSession session, HttpServletRequest request, @RequestParam Long id) {
		CmsBanner banner = cmsBannerImpl.getById(id);
		if (null == banner) {
			return CommonCode.Fail.DATAISDELETED;
		}
		UserSessionInfoBackend loginUser = SessionUtil.getUserSessionInfoFromBackend(session);
		cmsBannerImpl.updateBanner(banner, loginUser.getId(), CommonCode.isOnline.isOnline_No);
		return CommonCode.SUCCESS;
	}

	/**
	 * 新增banner
	 * 
	 * @param banner
	 * @return
	 */
	@RequestMapping(value = "/create/banner", method = RequestMethod.POST)
	@ResponseBody
	public String saveUser(HttpSession session, HttpServletRequest request,CmsBanner banner) {
		CmsBanner banner_cms = cmsBannerImpl.getByTitle(banner.getTitle());
		if (banner_cms == null) {
			banner.setId(KeyUtil.generateDBKey());
			UserSessionInfoBackend loginUser = SessionUtil.getUserSessionInfoFromBackend(session);
			Integer state = cmsBannerImpl.insertBanner(banner, loginUser.getId());
			if (state == null) {
				return CommonCode.SUCCESS;
			} else {
				return state.toString();
			}
		} else {
			return CommonCode.CmsResult.TITLEEXISTS;
		}
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/findBannerById")
	@ResponseBody
	public Map<String, Object> getBannerById(@RequestParam(value = "id", required = false) Long id) {
		Map<String, Object> map = Maps.newHashMap();
		CmsBanner banner = cmsBannerImpl.getById(id);
		map.put("banner", banner);
		// 上传图片集合
		List<FileModel> list = Lists.newArrayList();
		// banner.getImage()获得FileModel所需的图片name
		list.add(new FileModel("", "", banner.getImage(), uploadPath.getShowPath(), "", 0l));
		map.put("djson", list);
		return map;
	}

	/**
	 * 编辑提交
	 */
	@RequestMapping(value = "/edit/banner", method = RequestMethod.POST)
	@ResponseBody
	public String editUser(HttpSession session, HttpServletRequest request,CmsBanner banner) {
		CmsBanner banner_cms = cmsBannerImpl.getByTitle(banner.getTitle());
		if (banner_cms != null && !banner_cms.getId().equals(banner.getId())) {
			return CommonCode.CmsResult.TITLEEXISTS;
		} else {
			UserSessionInfoBackend loginUser = SessionUtil.getUserSessionInfoFromBackend(session);
			Integer state = cmsBannerImpl.editBanner(banner, loginUser.getId());
			if (state == null) {
				return CommonCode.SUCCESS;
			} else {
				return state.toString();
			}
		}

	}


}

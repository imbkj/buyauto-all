package com.controller.commission;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.business.service.user.api.TUsersApi;
import com.buyauto.dao.orders.TUsersCommissionDao;
import com.buyauto.entity.user.TUsersRecommender;
import com.buyauto.util.anno.AuthAnno;
import com.buyauto.util.method.AuthBackend;
import com.buyauto.util.method.SessionUtil;
import com.buyauto.util.pojo.PageVo;
import com.buyauto.util.pojo.UserSessionInfo;
import com.buyauto.util.web.UrlRegulation;

/**
 * 返佣信息
 */
@RestController
@RequestMapping(value = UrlRegulation.SecurityPrefix.HTTP + UrlRegulation.RequestPrefix.REQ_LOGIN
		+ UrlRegulation.BizPrefix.COMMISSION)
public class CommissionController {

	private static final Logger logger = LoggerFactory.getLogger(CommissionController.class);

	@Autowired
	private TUsersApi tUsersImpl;

	/**
	 * 到返佣信息页面
	 * 
	 * @return
	 */
	@RequestMapping("/rackBack")
	@AuthAnno(rules = { AuthBackend.PERSONALORDER })
	public ModelAndView rackBack(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		UserSessionInfo userLogin = SessionUtil.getUserSessionInfoFromPc(session);
		TUsersRecommender tUsersRecommender = tUsersImpl.getRecommenderByUid(userLogin.getId());
		ModelAndView modelAndView = new ModelAndView("personal/rackback");
		if (tUsersRecommender != null && tUsersRecommender.getFatherPhone() != null) {
			modelAndView.addObject("recommPhone", tUsersRecommender.getFatherPhone());
		}
		return modelAndView;
	}

	/**
	 * 获取返佣信息列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/rackBackList", method = RequestMethod.POST)
	public PageVo<TUsersCommissionDao> rackBackList(HttpServletRequest request, HttpServletResponse response,
			HttpSession session, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int rows) {
		UserSessionInfo userLogin = SessionUtil.getUserSessionInfoFromPc(session);
		PageVo<TUsersCommissionDao> list = tUsersImpl.getRackBackList(userLogin.getId(), page, rows);
		return list;
	}
}

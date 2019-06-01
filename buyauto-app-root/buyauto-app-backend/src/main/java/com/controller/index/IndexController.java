package com.controller.index;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.business.service.index.api.BackendIndexApi;
import com.business.service.time.api.IBaseTimerApi;
import com.buyauto.util.anno.AuthAnno;
import com.buyauto.util.method.AuthBackend;
import com.buyauto.util.web.UrlRegulation;

@RestController
@RequestMapping(UrlRegulation.SecurityPrefix.HTTP + UrlRegulation.RequestPrefix.REQ_LOGIN
		+ UrlRegulation.PrefixBackendBusiness.HOME_PAGE)
public class IndexController {

	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

	@Autowired
	private BackendIndexApi indexApi;
	
	@Autowired
	private IBaseTimerApi baseApi;
	/*
	 * @RequestMapping(value = "/") public ModelAndView index(HttpServletRequest
	 * request, HttpServletResponse response, HttpSession session) {
	 * ModelAndView modelAndView = new ModelAndView("index", "time", new
	 * Date()); return modelAndView; }
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView showIndex(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		return new ModelAndView("index");
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView showIndex1(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		return new ModelAndView("index");
	}
	/**
	 * 后台达时报
	 * @return
	 */
	@RequestMapping(value="dataReports" , method = RequestMethod.GET)
	@AuthAnno(rules = { AuthBackend.DATAOVERVIEW })
	public ModelAndView toDataReports(){
		ModelAndView modelAndView = new ModelAndView("other/dataReport");
		//达时报
		modelAndView.addObject("reports", indexApi.getIndexReport());
		return modelAndView;
	}
	
	@RequestMapping(value="testt",method= RequestMethod.GET)
	public void aa(){
		baseApi.productDataInput();
	}
}

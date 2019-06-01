package com.buyauto.util.web;

/**
 * 
 * @ClassName: UrlRegulation
 * @Description: URL统一协议
 * @author cxz
 * @date 2016年11月8日 下午6:28:38
 *
 */
public class UrlRegulation {

	/* 请求根路径，这个不做前缀处理 */
	public static final String ROOT = "/";

	/**
	 * 
	 * @ClassName: SecurityPrefix
	 * @Description: Controller第0层,https安全层
	 * @author cxz
	 * @date 2016年11月8日 下午6:31:26
	 *
	 */
	public class SecurityPrefix {

		/* https访问地址前缀 */
		public static final String HTTPS = "/hs";

		/* http访问地址前缀 */
		public static final String HTTP = "/h";
	}

	/**
	 * 
	 * @ClassName: RequestPrefix
	 * @Description: Controller第1层,用户登陆安全层
	 * @author cxz
	 * @date 2016年11月8日 下午6:29:51
	 *
	 */
	public class RequestPrefix {

		public static final String REQ_LOGIN = "/l";
		/* 请求需登录的前缀 */

		/* 请求无需登录的前缀 */
		public static final String REQ_NO_LOGIN = "/n";

		/* 普通请求 */
		public static final String REQ_COMMON = "/c";

	}

	/**
	 * 
	 * @ClassName: BizPrefix
	 * @Description: Controller第2层,业务描述层
	 * @author cxz
	 * @date 2016年11月8日 下午6:29:12
	 *
	 */
	public class BizPrefix {
		/* 进口车商城 */
		public static final String MALL = "/mall";

		/* 配置管理 */
		public static final String CONFIGURE = "/configure";

		/* 首页 */
		public static final String INDEX = "/index";

		/* 订单 */
		public static final String ORDERS = "/orders";

		/* 图片验证码 */
		public static final String CAPTCHA = "/captcha";

		/* 产品 */
		public static final String PRODUCTS = "/products";

		/* 配置 */
		public static final String CONFIG = "/config";

		/* 操作前台用户  */
		public static final String USER = "/user";
		
		/* 登录  */
		public static final String LOGIN = "/login";
		
		/* 新闻  */
		public static final String NEWS = "/news";
		
		/* 消息  */
		public static final String MSG = "/msg";

		/* 供应商  */
		public static final String SUPPLIER = "/supplier";	
		
		/* 供应商  */
		public static final String COMMISSION = "/commission";		
	}

	/**
	 * 
	 * @ClassName: BizPrefix
	 * @Description: Controller第2层,业务描述层
	 * @author cxz
	 * @date 2016年11月8日 下午6:29:12
	 *
	 */
	public class BizPrefixBackend {
		/* 订单支付 */
		public static final String SEARCH_RECHARGE = "/sr";
	}

	/**
	 * 用户管理
	 * 
	 * @author yhy
	 *
	 */
	public class PrefixBackendBusiness {
		/* 首页 */
		public static final String HOME_PAGE = "/i";
		/* 用户登录 */
		public static final String LOGIN = "/login";
		/* 用户管理 */
		public static final String OPER = "/oper";
		/* CMS管理 */
		public static final String CMS = "/cms";
		/* 订单管理 */
		public static final String ORDERS = "/orders";
		/* 审核管理 */
		public static final String AUDIT = "/audit";

	}

}

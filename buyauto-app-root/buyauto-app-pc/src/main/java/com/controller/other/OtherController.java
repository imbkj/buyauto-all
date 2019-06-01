package com.controller.other;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.buyauto.entity.sys.SysRuleUser;
import com.buyauto.entity.user.TUsers;
import com.buyauto.util.method.CommonCode;
import com.buyauto.util.method.SessionUtil;
import com.buyauto.util.method.StringUtil;
import com.buyauto.util.method.UploadifyUtils;
import com.buyauto.util.pojo.BaseSftpDto;
import com.buyauto.util.pojo.FileModel;
import com.buyauto.util.pojo.MessageParam;
import com.buyauto.util.pojo.UploadPath;
import com.external.msg.api.ISendMessageApi;
import com.external.orders.api.ITOrdersPcApi;
import com.external.user.api.ITuserApi;
import com.google.code.kaptcha.Producer;
import com.google.common.collect.Maps;

@RestController
public class OtherController {

	@Autowired
	private UploadPath uploadPath;

	@Autowired
	private Producer captchaProducer;// 图片验证码

	@Autowired
	private BaseSftpDto ftpbean;

	@Qualifier("sendMessage")
	@Autowired
	private ISendMessageApi sendMessage;
	
	@Qualifier("orderService")
	@Autowired
	private ITOrdersPcApi ordersPcApi;
	
	@Qualifier("userService")
	@Autowired
	private ITuserApi userService;

	private static final Logger logger = LoggerFactory.getLogger(OtherController.class);

	private String[] fileType = {"PDF","JPG", "BMP", "PNG"};

	// private String[] fileType = { "JPG", "BMP", "PNG" };

	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("index");
		return modelAndView;
	}

	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public ModelAndView user(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("user/new_employee");
		return modelAndView;
	}

	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	@ResponseBody
	public FileModel uploadFile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 转型为MultipartHttpServletRequest
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		// 获取文件到map容器中
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

		for (String key : fileMap.keySet()) {
			MultipartFile fileList = fileMap.get(key);
			CommonsMultipartFile mFile = (CommonsMultipartFile) fileList;
			String fname = mFile.getFileItem().getName();
			if (StringUtil.isEmpty(fname) || !validateFileType(fname.substring(fname.lastIndexOf(".") + 1))) {
				logger.info("传图格式错误，fname:" + fname);
				return null;
			}
		}
		// 获取页面传递过来的路径参数
		String preId = request.getParameter("pre");

		String filePath = uploadPath.getRootPath();

		// 文件上传并返回map容器，map存储了文件信息
		FileModel fileModel = UploadifyUtils.uploadFiles(preId, filePath, fileMap);

		return fileModel;
	}

	@RequestMapping(value = "/getShowAddress", method = RequestMethod.GET)
	@ResponseBody
	public String showAddress(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		// 图片展示路径
		return uploadPath.getShowPath();
	}

	@RequestMapping(value = "/getShowAddress1", method = RequestMethod.GET)
	@ResponseBody
	public String showAddress1(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		// 图片展示路径
		return ftpbean.getFilePath();
	}

	private boolean validateFileType(String type) {
		for (String ot : fileType) {
			if (ot.equalsIgnoreCase(type)) {
				return true;
			}
		}
		return false;
	}

	public SimpleDateFormat savePathDateFormat = new SimpleDateFormat("yyyyMM");

	private String getDateToday() {
		return savePathDateFormat.format(new Date());
	}

	/**
	 * 图片验证码
	 * 
	 * @param key
	 *            session键
	 */
	public void getKaptchaImage(HttpSession session, HttpServletRequest request, HttpServletResponse response, @RequestParam("key") String key) throws Exception {
		if (StringUtil.isEmpty(key))
			key = "";
		// 清除浏览器的缓存
		response.setDateHeader("Expires", 0);
		// Set standard HTTP/1.1 no-cache headers.
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		// Set standard HTTP/1.0 no-cache header.
		response.setHeader("Pragma", "no-cache");
		// return a jpeg
		response.setContentType("image/jpeg");
		// 浏览器记忆功能-----当前过浏览器和服务器交互成功以后下载的图片和资源会进行缓存一次。下次刷新的时候就不会在到服务器去下载。
		// 获取KAPTCHA验证的随机文本
		String capText = captchaProducer.createText();
		// 将生成好的图片放入会话中
		// session.setAttribute(key, capText);
		SessionUtil.addSessionKey(session, key, capText);

		// create the image with the text
		BufferedImage bi = captchaProducer.createImage(capText);
		ServletOutputStream out = response.getOutputStream();
		// write the data out
		ImageIO.write(bi, "jpg", out);
		try {
			out.flush();
		} finally {
			out.close();// 关闭
		}
	}

	/**
	 * 发送手机或邮箱验证码
	 * 
	 * @param template
	 *            模板
	 * @param contact
	 *            发送地址(手机或邮箱)
	 * @param fomartion
	 *            发送邮箱或手机
	 * @param sessionKey
	 *            验证码存到session中的key
	 */
	public void sendMessage(HttpServletRequest request, HttpServletResponse response, HttpSession session, String template, String contact, int fomartion, String sessionKey) {
		MessageParam messageParam = new MessageParam(contact, fomartion, template);
		Map<String, String> map = Maps.newHashMap();
		int num = (int) ((Math.random() * 9 + 1) * 100000);// 产生6位随机数
		SessionUtil.addSessionKey(session, sessionKey, (num + ""));
		map.put("vCode", num + "");
		messageParam.setTemplateParam(map);
		sendMessage.sendMessageApi(messageParam);
	}

	/**
	 * 404
	 * 
	 * @param request
	 * @param res
	 * @return
	 */
	@RequestMapping(value = "/error/404", method = RequestMethod.GET)
	public ModelAndView to404(HttpServletRequest request, HttpServletResponse res) {
		return new ModelAndView("other/404");
	}

	/**
	 * 500
	 * 
	 * @param request
	 * @param res
	 * @return
	 */
	@RequestMapping(value = "/error/500", method = RequestMethod.GET)
	public ModelAndView to500(HttpServletRequest request, HttpServletResponse res) {
		return new ModelAndView("other/500");
	}
	
	/**
	 * 关于我们
	 * @return
	 */
	@RequestMapping(value = "/about", method = RequestMethod.GET)
	public ModelAndView about(){
		return new ModelAndView("frame/about_us");
	}

	/**
	 * 帮助中心
	 * @return
	 */
	@RequestMapping(value = "/aboutHelp", method = RequestMethod.GET)
	public ModelAndView aboutHelp(){
		return new ModelAndView("frame/about_help");
	}

	/**
	 * 权限不足
	 * 
	 * @param request
	 * @param res
	 * @return
	 */
	@RequestMapping(value = "/error/notPermission", method = RequestMethod.GET)
	public ModelAndView notPermission(HttpServletRequest request, HttpServletResponse res) {
		return new ModelAndView("other/permission_dend");
	}

	@RequestMapping(value = "/deleteFile", method = RequestMethod.POST)
	@ResponseBody
	public Boolean deleteFile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String folderPath = request.getParameter("key");
		System.out.println(folderPath);
		return true;
	}
	/**
	 * 短信推送后台通知有订单取消
	 * @param request
	 * @param response
	 * @param orderId
	 * @param status
	 */
	public void msgToBackend(HttpServletRequest request, HttpServletResponse response,Long orderId,Long companyId, Integer status) {
		List<SysRuleUser> usersForCheck = ordersPcApi.getUsersForCheck();
		
		Map<String, String> map = Maps.newHashMap();
		MessageParam msgParam = new MessageParam();
		msgParam.setType(CommonCode.CmsType.Infomartion);  //短信
		msgParam.setTemplateName("Orders");   //使用模板

		map.put("orderId", orderId.toString());	//设置订单编号
		
		if (usersForCheck.size() > CommonCode.operateRes.NO) {
			for (SysRuleUser user : usersForCheck) {
				map.put("userName", user.getRealName());
				msgParam.setAddress(user.getUserName());
				if (status == CommonCode.SaveStatus.WAITCHK) { // 定金待审核
					map.put("content", CommonCode.MsgToFinanceOrOperation.DEPOSITWAITFORCHECK);
				} else if (status == CommonCode.SaveStatus.CHECKTAILMONEY) { // 尾款待审核
					map.put("content", CommonCode.MsgToFinanceOrOperation.TAILMONEYWAITFORCHECK);
				}
				msgParam.setTemplateParam(map);
				sendMessage.sendMessageApi(msgParam);
			}
		}
	}
	/**
	 * 获取服务器当前时间
	 * @return
	 */
	@RequestMapping(value="/getServerCurrentTime",method = RequestMethod.GET)
	public Date getServerCurrentTime(){
		return new Date();
	}

}

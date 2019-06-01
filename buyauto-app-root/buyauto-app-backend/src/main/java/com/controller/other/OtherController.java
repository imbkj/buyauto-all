package com.controller.other;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.business.service.sys.api.ISysEngineApi;
import com.business.service.time.api.IBaseTimerApi;
import com.business.service.user.api.TUsersApi;
import com.buyauto.dao.orders.TOrdersDao;
import com.buyauto.entity.orders.TOrders;
import com.buyauto.entity.user.TUsers;
import com.buyauto.util.method.CommonCode;
import com.buyauto.util.method.UploadifyUtils;
import com.buyauto.util.pojo.FileModel;
import com.buyauto.util.pojo.MessageParam;
import com.buyauto.util.pojo.UploadPath;
import com.external.msg.api.ISendMessageApi;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@RestController
public class OtherController {

	private static final Logger logger = LoggerFactory.getLogger(OtherController.class);

	@Autowired
	private ISysEngineApi sysEngine;

	@Autowired
	private UploadPath uploadPath;
	
	@Qualifier("sendMessage")
	@Autowired
	private ISendMessageApi sendMessage;
	
	@Autowired
	private TUsersApi tUsersApi;
	
	@Autowired
	private IBaseTimerApi baseApi;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView toLogin(HttpServletRequest request, HttpServletResponse res) {
		return new ModelAndView("redirect:/h/l/i");
	}

	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	@ResponseBody
	public Object uploadFile(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		// 转型为MultipartHttpServletRequest
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		// 获取文件到map容器中
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		// 获取页面传递过来的路径参数
		String preId = request.getParameter("pre");

		String filePath = uploadPath.getRootPath();

		// 文件上传并返回map容器，map存储了文件信息
		FileModel fileModel = UploadifyUtils.uploadFiles(preId, filePath, fileMap);

		return fileModel;
	}

	@RequestMapping(value = "/deleteFile", method = RequestMethod.POST)
	@ResponseBody
	public Boolean deleteFile(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		String folderPath = request.getParameter("key");
		System.out.println(folderPath);
		return true;
	}

	@RequestMapping(value = "/getImagePath", method = RequestMethod.GET)
	@ResponseBody
	public String getImagePath(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {

		return uploadPath.getShowPath();
	}
	@RequestMapping(value = "/aaa", method = RequestMethod.GET)
	public void testUpGrade(HttpServletRequest request, HttpServletResponse response){
		//TODO
		Map<String, List<TUsers>> upGradeUserList = baseApi.queryUpGradeUserList();
		System.out.println(upGradeUserList);
	}

	@RequestMapping(value = "/getFileImage", method = RequestMethod.POST)
	@ResponseBody
	public List<FileModel> getFileImage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<FileModel> list = Lists.newArrayList();

		list.add(new FileModel("pre-ee", "", "", "", "", 0l));

		return list;
	}

	@RequestMapping(value = "/error/404", method = RequestMethod.GET)
	public ModelAndView to404(HttpServletRequest request, HttpServletResponse res) {
		return new ModelAndView("other/404");
	}

	@RequestMapping(value = "/error/noAuth", method = RequestMethod.GET)
	public ModelAndView toNoAuth(HttpServletRequest request, HttpServletResponse res) {
		return new ModelAndView("other/noAuth");
	}

	@RequestMapping(value = "/error/500", method = RequestMethod.GET)
	public ModelAndView to500(HttpServletRequest request, HttpServletResponse res) {
		return new ModelAndView("other/500");
	}
	/**
	 * 发送通知短信给经销商财务
	 * @param order 订单信息
	 * @param status 订单状态
	 */
	public void sendMessage(HttpServletRequest request, HttpServletResponse response,TOrdersDao order,Integer status){
		List<TUsers> finance = tUsersApi.getFinancePhone(order.getCompanyId());  //获取该经销商的所有财务
		
		MessageParam msgParam = new MessageParam();
		msgParam.setType(CommonCode.CmsType.Infomartion);  //短信
		msgParam.setTemplateName("Orders");   //使用模板
		Map<String, String> map = Maps.newHashMap();
		map.put("orderId", order.getId().toString());
		
		if (status == CommonCode.SaveStatus.CANCELCHECKNOTPASS || status == CommonCode.SaveStatus.CANCELORDER) { //订单取消审核不通过通知销售
			TUsers saller = tUsersApi.getById(order.getUserId()); //得到下单销售
			map.put("userName", saller.getName());
			msgParam.setAddress(saller.getPhone());
			if(status == CommonCode.SaveStatus.CANCELORDER){ //取消成功
				map.put("content", CommonCode.MsgToFinanceOrOperation.ORDERCANCELSUCCESS);
			}else{
				map.put("content", CommonCode.MsgToFinanceOrOperation.ORDERCANCELNOTPASS);
			}
			msgParam.setTemplateParam(map);
			sendMessage.sendMessageApi(msgParam);
		}else{
			if (finance.size() > CommonCode.operateRes.NO) {
				for (TUsers tUsers : finance) {
					map.put("userName", tUsers.getName());
					msgParam.setAddress(tUsers.getPhone());
					if(status == CommonCode.SaveStatus.SECONDCHK){ //定金审核不通过
						map.put("content",CommonCode.MsgToFinanceOrOperation.DEPOSITCHECKNOTPASS);
					}else if(status == CommonCode.SaveStatus.TAILMOENYUNPASS){  //尾款审核不通过
						map.put("content",CommonCode.MsgToFinanceOrOperation.TAILMONEYCHECKNOTPASS);
					}else if(status == CommonCode.SaveStatus.WAITTHREECARDS){  //尾款审核通过
						map.put("content",CommonCode.MsgToFinanceOrOperation.TAILMONEYCHECKPASS);
					}
					msgParam.setTemplateParam(map);
					sendMessage.sendMessageApi(msgParam);
				}
			}
		}
	}
	
	/**
	 * 发送短信
	 * @param phone 手机
	 * @param templet 短信模板名称
	 * @param date 替换的内容
	 */
	public void sendMsg(String phone,String templet,Map<String, String> date){
		try {
			MessageParam messageParam = new MessageParam(phone, CommonCode.CmsType.Infomartion, templet);
			Map<String, String> map = Maps.newHashMap();
			map.putAll(date);//变量
			messageParam.setTemplateParam(map);
			sendMessage.sendMessageApi(messageParam);
		} catch (Exception e) {
			logger.info("短信调用失败");
			e.printStackTrace();
		}
	}
	
	

}

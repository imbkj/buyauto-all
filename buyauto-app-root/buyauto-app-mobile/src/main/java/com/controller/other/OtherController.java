package com.controller.other;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.ServletException;
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

import com.buyauto.util.method.CommonCode;
import com.buyauto.util.method.RedisUtil;
import com.buyauto.util.method.StringUtil;
import com.buyauto.util.method.UploadifyUtils;
import com.buyauto.util.pojo.FileModel;
import com.buyauto.util.pojo.MessageParam;
import com.buyauto.util.pojo.UploadPath;
import com.buyauto.util.web.AppWebResultVo;
import com.external.msg.api.ISendMessageApi;
import com.google.common.collect.Maps;

@RestController
public class OtherController {


	@Autowired
	private UploadPath uploadPath;
	
	@Qualifier("sendMessage")
	@Autowired
	private ISendMessageApi sendMessage;

	private static final Logger logger = LoggerFactory.getLogger(OtherController.class);


	private String[] fileType = {"PDF","JPG", "BMP", "PNG"};
	
	@RequestMapping(value = "/")
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws Exception {
		ModelAndView modelAndView = new ModelAndView("index", "time", new Date());
		return modelAndView;
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
	public void sendMessage(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			String template, String contact, int fomartion, String sessionKey) {
		MessageParam messageParam = new MessageParam(contact, fomartion, template);
		Map<String, String> map = Maps.newHashMap();
		int num = (int) ((Math.random() * 9 + 1) * 100000);// 产生6位随机数
		RedisUtil.setRedis(sessionKey,num+"");
		map.put("vCode", num + "");
		messageParam.setTemplateParam(map);
		sendMessage.sendMessageApi(messageParam);
	}
	
	// 图片展示路径
	@RequestMapping(value = "/getShowAddress")
	@ResponseBody
	public AppWebResultVo<String> showAddress(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		return AppWebResultVo.buildSucc(uploadPath.getShowPath());
	}
		
	/**
	 * 文件上传
	 * @return
	 */
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	@ResponseBody
	public FileModel uploadFile(HttpServletRequest request, HttpServletResponse response){
		// 转型为MultipartHttpServletRequest
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		// 获取文件到map容器中
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		for (String key : fileMap.keySet()) {
			MultipartFile fileList = fileMap.get(key);
			CommonsMultipartFile mFile = (CommonsMultipartFile) fileList;
			String fname = mFile.getFileItem().getName();
			if (StringUtil.isEmpty(fname) || !validateFileType(fname.substring(fname.lastIndexOf(".") + 1))) {
				logger.error("传图格式错误，fname:" + fname);
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
		
	private boolean validateFileType(String type) {
		for (String ot : fileType) {
			if (ot.equalsIgnoreCase(type)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * IOS用
	 * @param request
	 * @param response
	 * @param version
	 * @return 1最新版本  2可以更新  3必须更新
	 * @throws FileNotFoundException 
	 */
	@RequestMapping(value = "/version")
	@ResponseBody
	public AppWebResultVo<String> getVersion(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = true) String version){
		String rootPath=this.getClass().getResource("/").getPath();
		String filePath=rootPath+"app-version.conf";
		Scanner scanner = null;
		String currentVersion = "";
		try {
			scanner = new Scanner(new File(filePath));
			currentVersion=scanner.nextLine();/*文件内容*/
		} catch (FileNotFoundException e) {
			logger.error("ios更新接口,读取文件失败!");
			e.printStackTrace();
		}finally{
			if(scanner!=null)scanner.close();
		}
		
		String result=CommonCode.AppVersionResponse.VersionLatest;/*最新*/
		/*服务器IOS版本号*/
		int currentVersion_value = Integer.valueOf(currentVersion.replace('.', '0'));
		/*手机端版本号*/
		int version_value = Integer.valueOf(version.replace('.', '0'));
		/*比较*/
		if(version_value > currentVersion_value){
			result=CommonCode.AppVersionResponse.VersionLatest; 
		}else if(currentVersion_value == version_value){
			result=CommonCode.AppVersionResponse.VersionLatest;
		}else{
			result=CommonCode.AppVersionResponse.VersionBlock;
		}
		
		return AppWebResultVo.buildSucc(result);
	}

	/**
	 * app安卓用
	 * @param version
	 * @return 1最新版本  2可以更新  3必须更新
	 * @throws FileNotFoundException
	 */
	@RequestMapping(value = "/appversion")
	@ResponseBody
	public String getAppVersion(HttpServletRequest request, HttpServletResponse response){
		String rootPath=this.getClass().getResource("/").getPath();
		String filePath=rootPath+"androind-version.json";
		Scanner scanner = null;
		String currentVersion = "";
		try {
			scanner = new Scanner(new File(filePath));
			while (scanner.hasNextLine()) {
				currentVersion +=scanner.nextLine();
			}
		} catch (FileNotFoundException e) {
			logger.error("安卓更新接口,读取文件失败!");
			e.printStackTrace();
		}finally{
			if(scanner!=null)scanner.close();
		}
		return currentVersion;
	}
	
	

}

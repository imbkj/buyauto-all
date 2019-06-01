package com.controller.job;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.business.service.orders.api.ITOrdersCSVApi;
import com.business.service.orders.api.ITOredersApi;
import com.business.service.products.api.IProductsTempApi;
import com.business.service.time.api.IBaseTimerApi;
import com.business.service.user.api.TUsersApi;
import com.buyauto.entity.orders.TOrders;
import com.buyauto.entity.user.TUsers;
import com.buyauto.util.method.CommonCode;
import com.buyauto.util.method.FtpUtil;
import com.buyauto.util.pojo.BaseSftpDto;
import com.buyauto.util.pojo.MessageParam;
import com.controller.base.IJob;
import com.external.msg.api.ISendMessageApi;
import com.google.common.collect.Maps;

@Service("FtpCarJob")
public class ContractDemo implements IJob {
	private final static Logger logger = LoggerFactory.getLogger(ContractDemo.class);

	@Autowired
	private IBaseTimerApi baseApi;
	@Autowired 
	private IProductsTempApi productTempApi;
	@Autowired
	private BaseSftpDto ftpDto;
	@Autowired
	private ITOrdersCSVApi tOrdersCSVApi;
	@Autowired
	private ITOredersApi orderApi;
	@Autowired
	private TUsersApi tUsersApi;
	
	@Qualifier("sendMessage")
	@Autowired
	private ISendMessageApi sendMessage;
	
	/* 随机休眠器 */
	private final static Random randomSleep = new Random();

	@Override
	public void execute() {
		try {
			Thread.sleep(randomSleep.nextInt(10000));
		} catch (InterruptedException e) {
			// 系统原因或者中断原因，直接返回 
			logger.error(e.getMessage(), e);
			return;
		}
		
		
		
		//1.获取车辆信息
		Map<String, File> listFileName = baseApi.queryFTPcarList(ftpDto);
		System.out.println("listFileName:"+listFileName.toString());
		System.out.println("listFileName.size():"+listFileName.size());
		if(listFileName !=null && listFileName.size() >0 ){
			//product表数据转存
			baseApi.productDataInput();
		}
		FtpUtil ftp = new FtpUtil();
		ftp.connectServer(ftpDto);
		for (Map.Entry<String, File> entry : listFileName.entrySet()) {
			String fileName=entry.getKey();
			boolean bb =ftp.removeFile(CommonCode.FtpPath.CARLIST+CommonCode.FtpPath.UPLOAD, fileName);
			System.out.println("删除 ["+fileName+"]=" + bb);
			File resCsv=entry.getValue();
			bb =ftp.fileUpload(CommonCode.FtpPath.CARLIST+CommonCode.FtpPath.TMP+"/", entry.getValue());
			if(!bb){//上传失败
				System.out.println(fileName+"上传失败");
				break;
			}
			bb =ftp.renameFile(CommonCode.FtpPath.CARLIST+CommonCode.FtpPath.TMP+"/"+resCsv.getName(), CommonCode.FtpPath.CARLIST+CommonCode.FtpPath.RESULT+"/"+fileName);
			System.out.println(fileName+"上传成功，重命名到result目录="+bb);
		}
		ftp.closeServer();
		//2.获取返回结果
		try {
			List<Map<String, Object>>  orderMaps =  tOrdersCSVApi.queryFTPResult(ftpDto);
			if (orderMaps.size() > CommonCode.operateRes.NO) {
				for (Map<String, Object> map : orderMaps) {
					//取出车架号和订单状态
					String carNo = (String) map.get("carNo");
					Integer orderStatus = (Integer) map.get("orderStatus");
					
					TOrders orderFind = orderApi.getOrderByCarNo(carNo);
					MessageParam msgParam = new MessageParam();
					Map<String, String> maps = Maps.newHashMap();
					maps.put("orderId", orderFind.getId().toString());
					//待支付定金和待支付尾款的时候短信通知经销商财务
					if (orderStatus == CommonCode.SaveStatus.WAITDEPOSIT || orderStatus == CommonCode.SaveStatus.WAITTAILMONEY) {
						List<TUsers> finance = tUsersApi.getFinancePhone(orderFind.getCompanyId());  //获取该经销商的所有财务
						if (finance.size() > CommonCode.operateRes.NO) {
							msgParam.setType(CommonCode.CmsType.Infomartion);  //短信
							msgParam.setTemplateName("Orders");   //使用模板
							for (TUsers tUsers : finance) {
								maps.put("userName", tUsers.getName());
								msgParam.setAddress(tUsers.getPhone());
								if (orderStatus == CommonCode.SaveStatus.WAITDEPOSIT) {
									maps.put("content", CommonCode.MsgToFinanceOrOperation.ORDERCREATION);
								}else if(orderStatus == CommonCode.SaveStatus.WAITTAILMONEY){
									maps.put("content", CommonCode.MsgToFinanceOrOperation.DEPOSITCHECKPASS);
								}
								msgParam.setTemplateParam(maps);
								sendMessage.sendMessageApi(msgParam);
							}
						}
					//意外情况邮件通知指定处理人
					}else if(orderStatus == CommonCode.SaveStatus.SECONDCHK || orderStatus == CommonCode.SaveStatus.TAILMOENYUNPASS){
						for (String emailTo : CommonCode.emailList) {
							msgParam.setType(CommonCode.CmsType.Email);  //短信
							msgParam.setTemplateName("OrdersEmail");   //使用模板
							if (orderStatus == CommonCode.SaveStatus.SECONDCHK) {
								maps.put("orderStatus", "待发货");
							}else{
								maps.put("orderStatus", "待上传三证");
							}
							msgParam.setAddress(emailTo);
							msgParam.setSubject("订单异常状况处理");
							msgParam.setTemplateParam(maps);
							sendMessage.sendMessageApi(msgParam);
						}
					}
				}
			}
			
		} catch (Exception e) {
			// 系统原因或者中断原因，直接返回 
			logger.error(e.getMessage(), e);
			return;
		}
		
		//3.获取三证信息
	    //baseApi.queryFTPCarLicense(ftpDto);
		
		//4.获取已完成订单
	    
	}

}

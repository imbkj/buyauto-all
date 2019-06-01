package com.business.service.orders.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.business.service.orders.api.ITOrdersCSVApi;
import com.business.service.orders.api.ITOredersApi;
import com.business.service.user.api.TUsersApi;
import com.buyauto.dao.orders.TOrdersDao;
import com.buyauto.util.method.CommonCode;
import com.buyauto.util.method.CvsAnalysisUtil;
import com.buyauto.util.method.DateUtil;
import com.buyauto.util.method.FtpUtil;
import com.buyauto.util.method.StringUtil;
import com.buyauto.util.pojo.BaseSftpDto;
import com.google.common.collect.Maps;

@Service
public class OrdersCSVImpl implements ITOrdersCSVApi {

	private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);

	@Autowired
	private ITOredersApi iorderAPI;
	@Autowired
	private TUsersApi tUsersApi;

	
	@Override
	public boolean createOrderCsv(Long orderID, Integer status,BaseSftpDto ftpDto) {
		switch (status) {
			case CommonCode.SaveStatus.NEWCREATE:
				
				break;
			case CommonCode.SaveStatus.WAITORDERS:
				
				break;
			case CommonCode.SaveStatus.WAITTHREECARDS:
				
				break;
			default:
				return false;
		}
		
		final Long oid = orderID;
		final Integer sts = status;
		
		final BaseSftpDto baseSftpDto = ftpDto;
		fixedThreadPool.execute(new Runnable() {   //TODO 线程执行没有继续
			public void run() {
				FtpUtil ftp = new FtpUtil();
				try {
					System.out.println("进入线程="+oid + "," + sts);
					//新增订单去元初确认库存
					LinkedHashMap map = new LinkedHashMap();
					map.put("1", "车架号");
					map.put("2", "选装件");
					map.put("3", "经销商名称");
					map.put("4", "经销商销售");
					map.put("5", "提车人姓名");
					map.put("6", "提车人手机");
					map.put("7", "提车地点");
					map.put("8", "期望交付日期");
					map.put("9", "订单创建时间");
					map.put("10", "订单金额");
					List exportData = new ArrayList<Map>();
					TOrdersDao orderDao = iorderAPI.getOrdersById(oid);
					Map row1 = new LinkedHashMap<String, String>();
			        row1.put("1", iorderAPI.getCarNo(orderDao.getCarId()));
			        String configure="";
			        if(!StringUtil.isEmpty(orderDao.getConfigure())){
			        	JSONArray jsonArray = new JSONArray(orderDao.getConfigure());
				        for (int i = 0; i < jsonArray.length(); i++) {
				        	JSONObject jsonObject = jsonArray.getJSONObject(i);
				        	configure+=jsonObject.getString("name")+";";
						}
			        }
			        row1.put("2", configure);
			        row1.put("3", tUsersApi.getById(orderDao.getCompanyId()).getCompanyName());
			        row1.put("4", tUsersApi.getById(orderDao.getUserId()).getName());
			        row1.put("5", orderDao.getCustomer());
			        row1.put("6", orderDao.getCustomerTel());
			        row1.put("7", orderDao.getTakeLocation());
			        row1.put("8", orderDao.getDeliverTime());
			        row1.put("9", DateUtil.getPlusTime(orderDao.getCreateDate()));
			        row1.put("10", orderDao.getAmount());
					String outPutPath = "";
					switch (sts) {
						case CommonCode.SaveStatus.NEWCREATE:
							outPutPath = CommonCode.FtpPath.ORDERPRE;
							map.put("11", "状态（0预定1取消）");
							row1.put("11", "0");
							break;
						case CommonCode.SaveStatus.WAITORDERS:
							outPutPath = CommonCode.FtpPath.ORDER;
							map.put("11", "已付定金金额");
							row1.put("11", orderDao.getDeposit());
							break;
						case CommonCode.SaveStatus.WAITTHREECARDS:
							outPutPath = CommonCode.FtpPath.ORDERPAY;
							map.put("11", "已付定金金额");
							map.put("12", "已付尾款金额");
							row1.put("11", orderDao.getDeposit());
							row1.put("12", orderDao.getAmount().subtract(orderDao.getDeposit()));
							break;
					}
					exportData.add(row1);
					String fileName=oid+"";
					CvsAnalysisUtil cvsUtil =new CvsAnalysisUtil();
					File upFile = cvsUtil.createCSVFile(exportData, map, outPutPath+CommonCode.FtpPath.UPLOAD+"/", fileName,baseSftpDto.getFilePath());
					
					ftp.connectServer(baseSftpDto);
					boolean bb =ftp.fileUpload(outPutPath+CommonCode.FtpPath.TMP+"/", upFile);
					if(!bb){//上传失败
						System.out.println("上传失败");
						ftp.closeServer();
						return ;
					}
					System.out.println("上传成功="+outPutPath+CommonCode.FtpPath.TMP+"/"+upFile.getName());
					ftp.renameFile(outPutPath+CommonCode.FtpPath.TMP+"/"+upFile.getName(), outPutPath+CommonCode.FtpPath.UPLOAD+"/"+upFile.getName());
				} catch (Exception e) {
					e.printStackTrace();
				}
				ftp.closeServer();
			}
		});
		
		return true;
	}

	@Override
	public void createOrderCancelCsv(Long orderID, BaseSftpDto ftpDto) {
		final Long oid = orderID;
		final BaseSftpDto baseSftpDto = ftpDto;
		fixedThreadPool.execute(new Runnable() {   //TODO 线程执行没有继续
			public void run() {
				try {
					System.out.println("进入线程="+oid);
					//新增订单去元初确认库存
					LinkedHashMap map = new LinkedHashMap();
					map.put("1", "车架号");
					map.put("2", "选装件");
					map.put("3", "经销商名称");
					map.put("4", "经销商销售");
					map.put("5", "提车人姓名");
					map.put("6", "提车人手机");
					map.put("7", "提车地点");
					map.put("8", "期望交付日期");
					map.put("9", "订单创建时间");
					map.put("10", "订单金额");
					map.put("11", "状态（0预定1取消）");
					List exportData = new ArrayList<Map>();
					TOrdersDao orderDao = iorderAPI.getOrdersById(oid);
					Map row1 = new LinkedHashMap<String, String>();
			        row1.put("1", iorderAPI.getCarNo(orderDao.getCarId()));
			        row1.put("2", orderDao.getConfigure());
			        row1.put("3", tUsersApi.getById(orderDao.getCompanyId()).getCompanyName());
			        row1.put("4", tUsersApi.getById(orderDao.getUserId()).getName());
			        row1.put("5", orderDao.getCustomer());
			        row1.put("6", orderDao.getCustomerTel());
			        row1.put("7", orderDao.getTakeLocation());
			        row1.put("8", orderDao.getDeliverTime());
			        row1.put("9", DateUtil.getPlusTime(orderDao.getCreateDate()));
			        row1.put("10", orderDao.getAmount());
			        row1.put("11", "1");
					String outPutPath = CommonCode.FtpPath.ORDERPRE;
					exportData.add(row1);
					String fileName=oid+"";
					CvsAnalysisUtil cvsUtil =new CvsAnalysisUtil();
					File upFile = cvsUtil.createCSVFile(exportData, map, outPutPath+CommonCode.FtpPath.UPLOAD+"/", fileName,baseSftpDto.getFilePath());
					FtpUtil ftp = new FtpUtil();
					ftp.connectServer(baseSftpDto);
					boolean bb =ftp.fileUpload(outPutPath+CommonCode.FtpPath.TMP+"/", upFile);
					if(!bb){//上传失败
						ftp.closeServer();
						return ;
					}
					ftp.renameFile(outPutPath+CommonCode.FtpPath.TMP+"/"+upFile.getName(), outPutPath+CommonCode.FtpPath.UPLOAD+"/"+upFile.getName());
					ftp.closeServer();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	@Override
	public void createCarResultCsv(List exportData, BaseSftpDto ftpDto) {
		
	}
	
	@Override
	public void createOrderResultCsv(List exportData, BaseSftpDto ftpDto) {
		
	}
	
	@Override
	public List<Map<String, Object>> queryFTPResult(BaseSftpDto ftpDto) throws Exception{
		List<Map<String, Object>> orderMaps = new ArrayList<Map<String,Object>>();
		FtpUtil ftp = new FtpUtil();
		ftp.connectServer(ftpDto);
		for(int i=0;i<3;i++){
			String ftppath = "";
			Integer sts=0;
			Integer errorSts=0;
			switch (i) {
				case 0:
					ftppath = CommonCode.FtpPath.ORDERPRE+CommonCode.FtpPath.RESULT+"/";
					sts= CommonCode.SaveStatus.WAITDEPOSIT;
					errorSts = CommonCode.SaveStatus.FIRSTCHK;
					break;
				case 1:
					ftppath = CommonCode.FtpPath.ORDER+CommonCode.FtpPath.RESULT+"/";
					sts= CommonCode.SaveStatus.WAITTAILMONEY;
					errorSts = CommonCode.SaveStatus.SECONDCHK;
					break;
				case 2:
					ftppath = CommonCode.FtpPath.ORDERPAY+CommonCode.FtpPath.RESULT+"/";
					sts= CommonCode.SaveStatus.WAITTHREECARDS;
					errorSts = CommonCode.SaveStatus.TAILMOENYUNPASS;
					break;
			}
			List fl = ftp.getFileList(ftppath);
			if (fl == null) {
				System.out.println("查询列表：" + ftppath+"为空");
				break;
			}
			System.out.println("查询列表：" + ftppath);
			
			for (int k = 0; k < fl.size(); k++) {
				String filename = fl.get(k).toString();
				System.out.println("获取文件：" + filename);
				if (filename.endsWith(".csv")) {
					String sres = ftp.fileDownloadtoString(ftppath,ftpDto.getFilePath()+CommonCode.FtpPath.CARLIST+"/",filename);
					System.out.println("下载到=" + sres);
					if (sres == null) {
						break;
					}
					List<Map<Integer, String>> list = CvsAnalysisUtil.readCSV(sres);
					if (list == null) {
						break;
					}
					ftp.removeFile(ftppath, filename);
					for (int j = 0; j < list.size(); j++) {
						Map<String, Object> orderMap = Maps.newHashMap();
						
						Map<Integer, String> map = list.get(j);
						String carNO=map.get(0);
						String res=map.get(map.size()-2);
						String reson=map.get(map.size()-1);
						System.out.println("carNO="+carNO+",res="+res+",message="+reson);
						
						orderMap.put("carNo", carNO);
						if(res.equals(String.valueOf(CommonCode.FTPSuccess.Success))){
							iorderAPI.modifyOrder(carNO, sts);
							orderMap.put("orderStatus", sts);
						}else{
							//发送错误报告至指定邮箱
							if (errorSts == CommonCode.SaveStatus.FIRSTCHK) {
								iorderAPI.modifyOrder(carNO, errorSts);
							}
							orderMap.put("orderStatus", errorSts);
							orderMap.put("message", reson);
						}
						orderMaps.add(orderMap);
					}
				}
			}
		}
		ftp.closeServer();
		return orderMaps;
	}
}

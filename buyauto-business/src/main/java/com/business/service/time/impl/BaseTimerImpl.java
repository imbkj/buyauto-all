package com.business.service.time.impl;

import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.org.eclipse.jdt.internal.core.index.Index;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.business.service.orders.api.ITOredersApi;
import com.business.service.time.api.IBaseTimerApi;
import com.buyauto.entity.orders.TOrders;
import com.buyauto.entity.products.TProducts;
import com.buyauto.entity.products.TProductsCar;
import com.buyauto.entity.products.TProductsTemp;
import com.buyauto.entity.sys.SysConfig;
import com.buyauto.entity.user.TUsers;
import com.buyauto.mapper.orders.TOrdersMapper;
import com.buyauto.mapper.products.TProductsCarMapper;
import com.buyauto.mapper.products.TProductsMapper;
import com.buyauto.mapper.products.TProductsTempMapper;
import com.buyauto.mapper.sys.SysConfigMapper;
import com.buyauto.mapper.user.TUsersMapper;
import com.buyauto.util.core.tool.web.GsonUtil;
import com.buyauto.util.json.JSONUtil;
import com.buyauto.util.method.CommonCode;
import com.buyauto.util.method.CustomDateEditor;
import com.buyauto.util.method.CvsAnalysisUtil;
import com.buyauto.util.method.FtpUtil;
import com.buyauto.util.method.KeyUtil;
import com.buyauto.util.method.CommonCode.newsStatus;
import com.buyauto.util.pojo.BaseSftpDto;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

@Service
public class BaseTimerImpl implements IBaseTimerApi {
	private static final Logger logger = LoggerFactory.getLogger(BaseTimerImpl.class);
	@Autowired
	private TOrdersMapper tOrdersMapper;
	@Autowired
	private TProductsTempMapper tProductsTempMapper;
	@Autowired
	private ITOredersApi iTOredersApi;
	@Autowired
	private TProductsMapper tProductsMapper;
	@Autowired
	private TProductsCarMapper tProductsCarMapper;
	@Autowired
	private SysConfigMapper sysConfigMapper;
	@Autowired
	private TUsersMapper  tUsersMapper;
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(true));
	}
	
	@Override
	public List<TOrders> queryOrderOvertimeList() {
		return tOrdersMapper.queryDepositOvertime();
	}

	@Override
	public boolean updateOrderOvertime(Long id) {
		Date now = new Date();
		TOrders tOrders = tOrdersMapper.selectByPrimaryKey(id);
		tOrders.setUpdateDate(new Date());
		tOrders.setPayStatus(CommonCode.SaveStatus.OVERTIME);
		tOrders.setStatus(CommonCode.SaveStatus.CLOSEORDER);
		tOrders.setCarId(null);//滞空车架号对应
		tOrders.setUpdateDate(now);
		//TODO 库存释放？ 待定 目前仅超时订单 余下待定
		Boolean isNext = tOrdersMapper.updateByPrimaryKeySelective(tOrders)==CommonCode.DBSuccess.Success;
		if(isNext){
			//更新数据库车架号状态
			TProductsCar tProductsCar = tProductsCarMapper.selectByPrimaryKey(tOrders.getCarId());
			if(tProductsCar!=null){
				tProductsCar.setStatus(CommonCode.ProductsTempCarStatus.AVAILABLE);
				tProductsCar.setUpdateTime(now);
				isNext = tProductsCarMapper.updateByPrimaryKeySelective(tProductsCar)==CommonCode.DBSuccess.Success;
			}else {
				isNext =false;
			}
		}
		return isNext;
	}
	
	@Override
	public Map<String, File> queryFTPcarList(BaseSftpDto ftpDto){
		Map<String, File> returnMap =new HashMap<String, File>();
		FtpUtil ftp = new FtpUtil();
		Date now =new Date();
		String ftppath = CommonCode.FtpPath.CARLIST+CommonCode.FtpPath.UPLOAD+"/";
		try {
			ftp.connectServer(ftpDto);
			List fl = ftp.getFileList(ftppath);
			if (fl != null) {
				for (int k = 0; k < fl.size(); k++) {
					String filename = fl.get(k).toString();
					System.out.println("获取文件：" + filename);
					if (filename.endsWith(".csv")) {
						String sres = ftp.fileDownloadtoString(ftppath,ftpDto.getFilePath()+CommonCode.FtpPath.CARLIST+"/",filename);
						System.out.println("下载=" + sres);
						if (sres != null) {
							List<Map<Integer, String>> list = CvsAnalysisUtil.readCSV(sres);
							if (list != null) {
								
								for (int i = 0; i < list.size(); i++) {
									//System.out.print(i + "=");
									TProductsTemp ptemp=new TProductsTemp();
									Map<Integer, String> map = list.get(i);
									if(map.size()>21){
										ptemp.setCarNo(map.get(0));
										ptemp.setBrand(map.get(1));
										ptemp.setCargoCode(map.get(2));
										ptemp.setCargoNameCn(map.get(3));
										ptemp.setCargoNameEn(map.get(4));
										ptemp.setCarType(map.get(5));
										ptemp.setOrderModel(map.get(6));
										ptemp.setPosition(map.get(7));
										ptemp.setCarModel(map.get(8));
										ptemp.setOutColor(map.get(9));
										ptemp.setInColor(map.get(10));
										ptemp.setCarStandards(map.get(11));
										ptemp.setCountry(map.get(12));
										ptemp.setPower(map.get(13));
										ptemp.setEngine(map.get(14));
										ptemp.setFactoryDate(map.get(15));
										ptemp.setDoors(map.get(16));
										ptemp.setSeats(map.get(17));
										ptemp.setRemarks(map.get(18));
										ptemp.setLogistics(map.get(19));
										ptemp.setCarPrice(map.get(20));
										ptemp.setMustConfigure(map.get(21));
										ptemp.setMustConfigurePrice(map.get(22));
										ptemp.setCreateDate(now);
										ptemp.setExecuteDate(now);
										
										Map<String, String> jsonMap = new HashMap<String, String>();
										for (int j = 23; j < map.size()-2; ) {
											jsonMap.put(map.get(j++), map.get(j++));
										}
										String optional_configure=JSONUtil.toJSONString(jsonMap);
										ptemp.setOptionalConfigure(optional_configure);
										ptemp.setId(KeyUtil.generateDBKey());
										ptemp.setStatus(CommonCode.ProductTempStatus.NEW);
										int dbRes=0;
										if(!ptemp.getCarNo().isEmpty()){
											dbRes = tProductsTempMapper.insert(ptemp);	
										}
										if(dbRes==1){
											map.put(map.size()-2, "1");
											map.put(map.size()-1, "插入成功");
										}else{
											map.put(map.size()-2, "0");
											map.put(map.size()-1, "数据库失败");
										}
									}
								}
								CvsAnalysisUtil cvsUtil =new CvsAnalysisUtil();
								File upFile = cvsUtil.createCSVFile(list, getCarListLinkedHashMap(), ftppath, filename,ftpDto.getFilePath());
								returnMap.put(filename, upFile);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ftp.closeServer();
		}
		return returnMap;
	}
	
	private LinkedHashMap getCarListLinkedHashMap(){
		LinkedHashMap map = new LinkedHashMap();
		map.put("1", "车架号");
		map.put("2", "品牌");
		map.put("3", "货物代码");
		map.put("4", "中文品名");
		map.put("5", "英文品名");
		map.put("6", "车型（0轿车1SUV2MPV3皮卡4卡车5跑车）");
		map.put("7", "类型");
		map.put("8", "车辆位置（0港口现车1预定车2在途车）");
		map.put("9", "车辆型号");
		map.put("10", "车体外观颜色");
		map.put("11", "车身内部颜色");
		map.put("12", "车辆标准");
		map.put("13", "原产地");
		map.put("14", "排量");
		map.put("15", "发动机类型");
		map.put("16", "出厂日期");
		map.put("17", "门数");
		map.put("18", "座位数");
		map.put("19", "备注");
		map.put("20", "物流状态");
		map.put("21", "裸车价");
		map.put("22", "必选改内容");
		map.put("23", "必选改装价");
		map.put("24", "选装1");
		map.put("25", "选装1价格");
		map.put("26", "选装2");
		map.put("27", "选装2价格");
		map.put("28", "选装3");
		map.put("29", "选装3价格");
		map.put("30", "选装4");
		map.put("31", "选装4价格");
		map.put("32", "选装5");
		map.put("33", "选装5价格");
		map.put("34", "选装6");
		map.put("35", "选装6价格");
		map.put("36", "选装7");
		map.put("37", "选装7价格");
		map.put("38", "选装8");
		map.put("39", "选装8价格");
		map.put("40", "选装9");
		map.put("41", "选装9价格");
		map.put("42", "选装10");
		map.put("43", "选装10价格");
		map.put("44", "request");
		map.put("45", "message");
		return map;
	}
	
	public void queryFTPCarLicense(BaseSftpDto ftpDto){
		FtpUtil ftp = new FtpUtil();
		String ftppath = CommonCode.FtpPath.CARLICENSE+"/";
		try {
			ftp.connectServer(ftpDto);
			
			List fl = ftp.getFileList(ftppath);
			if (fl != null) {
				System.out.print("获取三证开始");
				for (int k = 0; k < fl.size(); k++) {
					String dirname = fl.get(k).toString();
					List fimg = ftp.getFileList(ftppath+dirname);
					if (fimg != null) {
						System.out.println("获取文件夹内容：" + dirname +":");
						for (int i = 0; i < fimg.size(); i++) {
							System.out.print(fimg.get(i));
							String sres = ftp.fileDownloadtoString(ftppath+dirname+"/",ftpDto.getFilePath()+ftppath+dirname+"/", fimg.get(i).toString());
							System.out.println("下载=" + sres);
							ftp.removeFile(ftppath+dirname, fimg.get(i).toString());
						}
					}
					//TODO 如果状态不是待上传三证(106)的话不进行三证上传，
					//不应该那么快就删除ftp上的三证信息
					iTOredersApi.uploadCarTh(dirname, fimg); 
					boolean tep= ftp.removeDirectory(ftppath,dirname, "");
					System.out.println("删除结果="+tep);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ftp.closeServer();
		}
	}

	@Override
	public void productDataInput() {
		Date now = new Date();
		Boolean isNext=true;
		List<TProductsTemp> notEntered= tProductsTempMapper.queryNotEnteredData();
		for (TProductsTemp tProductsTemp : notEntered) {
			//筛除错误数据
			if(tProductsTemp.getCarNo().isEmpty()){
				tProductsTemp.setStatus(CommonCode.ProductTempStatus.FAIL);
				tProductsTempMapper.updateByPrimaryKeySelective(tProductsTemp);
				continue;
			}
			//先查询是否有重复的车架号
			
			Integer tProductsCarCount= tProductsCarMapper.queryByCarNo(tProductsTemp.getCarNo());
			if(tProductsCarCount>0){
				if(tProductsTemp.getLogistics() !=null){
				//更新物流数据
				//查询到对应该车价号的订单
				TProductsCar tProductsCar= tProductsCarMapper.queryCarByCarNo(tProductsTemp.getCarNo());
				if(tProductsCar != null ){
					//根据车架号查询订单
					TOrders tOrders=tOrdersMapper.getOrderByCarId(tProductsCar.getId());
					if(tOrders!=null && tOrders.getStatus() !=CommonCode.SaveStatus.CLOSEORDER){
						Gson gs = new Gson();
						ArrayList<?> obj = (ArrayList<?>) gs.fromJson(tOrders.getLogisticsInfo(), Object.class);
						
						List<Map<String, String>> listItems = Lists.newArrayList();
						for (Object object : obj) {
							Map<String, String> nexl = Maps.newHashMap();
							LinkedTreeMap<String, String> UserNext = (LinkedTreeMap<String, String>) object;
							nexl.put("modifiTime",UserNext.get("modifiTime"));
							nexl.put("logistStatus", UserNext.get("logistStatus"));
							listItems.add(nexl);
						}
						
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String modifiTime=null;
						if(tProductsTemp.getCreateDate()!=null){
							modifiTime = dateFormat.format(tProductsTemp.getCreateDate());
						}else{
							modifiTime = dateFormat.format(now);
						}
						
						Map<String, String> next = Maps.newHashMap();
						next.put("modifiTime", modifiTime);
						next.put("logistStatus", tProductsTemp.getLogistics());
						listItems.add(next);
						tOrders.setLogisticsInfo(gs.toJson(listItems));
						tOrders.setUpdateDate(now);
						isNext=tOrdersMapper.updateByPrimaryKeySelective(tOrders)==CommonCode.DBSuccess.Success;
						if(isNext){
							tProductsTemp.setStatus(CommonCode.ProductTempStatus.INSERTR);
							tProductsTempMapper.updateByPrimaryKeySelective(tProductsTemp);
							continue;
						}
					}
				  }
				}
				//更新temp为失败
				tProductsTemp.setStatus(CommonCode.ProductTempStatus.FAIL);
				tProductsTempMapper.updateByPrimaryKeySelective(tProductsTemp);
				continue;
				
			}
			//查询当前tProduct中是否存在
			TProducts  existence = tProductsMapper.queryExistence(tProductsTemp);
			if(existence!=null){
				    //存在
					TProductsCar tProductsCar = new TProductsCar();
					tProductsCar.setId(KeyUtil.generateDBKey());
					tProductsCar.setCarNo(tProductsTemp.getCarNo());
					tProductsCar.setEngineNo(tProductsTemp.getEngine());
					tProductsCar.setStatus(CommonCode.ProductsTempCarStatus.AVAILABLE);
					tProductsCar.setCreateTime(now);
					tProductsCar.setUpdateTime(now);
					tProductsCar.setVersion(CommonCode.Version.Default);
					tProductsCar.setProductsId(existence.getProductsId());
					isNext=tProductsCarMapper.insertSelective(tProductsCar)==CommonCode.DBSuccess.Success;
					//导入成功/失败后修改temp表数据 缺少更新时间字段
					if(isNext){
						tProductsTemp.setStatus(CommonCode.ProductTempStatus.INSERTR);
						tProductsTempMapper.updateByPrimaryKeySelective(tProductsTemp);
					}else{
						tProductsTemp.setStatus(CommonCode.ProductTempStatus.FAIL);
						tProductsTempMapper.updateByPrimaryKeySelective(tProductsTemp);
					}
			}else{
				//tProduct表中不存在该类类型 新增一条车辆种类
				TProducts tProducts = new TProducts();
				//确定车辆品牌是否存在
				 SysConfig tempBrand = sysConfigMapper.queryTempBrand(tProductsTemp.getBrand(),CommonCode.ConfigGroup.CAR_BRAND);
				 if(tempBrand!=null){
					 //该品牌存在
					 tProducts.setBrandId(tempBrand.getId());
				 }else{
					 //没有则创建该品牌
					 SysConfig sysConfig = new SysConfig();
					 sysConfig.setId(KeyUtil.generateDBKey());
					 sysConfig.setScValue(tProductsTemp.getBrand());
					 sysConfig.setGroupName(CommonCode.ConfigGroup.CAR_BRAND);
					 sysConfig.setState(CommonCode.ConfigState.DISABLE);
					 sysConfig.setScType(CommonCode.ConfigType.PICTURE);
					 sysConfig.setCreateTime(now);
					 isNext =sysConfigMapper.insertSelective(sysConfig)==CommonCode.DBSuccess.Success;
					 if(!isNext){
						 //品牌创建失败 直接将Temp表内设为失败
							tProductsTemp.setStatus(CommonCode.ProductTempStatus.FAIL);
							tProductsTempMapper.updateByPrimaryKeySelective(tProductsTemp);
							continue;
					 }else{
						 tProducts.setBrandId(sysConfig.getId());
					 }
				 }
				tProducts.setProductsId(KeyUtil.generateDBKey());
				tProducts.setCarType(Integer.valueOf(tProductsTemp.getCarType()));
				//格式化金额 区分区间
				Integer carPrice = AmountFormatting(tProductsTemp.getCarPrice());
				if(carPrice< CommonCode.CarPrice.FIVE_HUNDRED_THOUSAND){
					tProducts.setPriceRange(CommonCode.PriceRange.FIVE_HUNDRED_THOUSAND);
				}else if(carPrice<CommonCode.CarPrice.ONE_MILLION){
					tProducts.setPriceRange(CommonCode.PriceRange.ONE_MILLION);
				}else if(carPrice<CommonCode.CarPrice.ONE_MILLION_FIVE_HUNDRED){
					tProducts.setPriceRange(CommonCode.PriceRange.ONE_MILLION_FIVE_HUNDRED);
				}else if(carPrice>= CommonCode.CarPrice.ONE_MILLION_FIVE_HUNDRED){
					tProducts.setPriceRange(CommonCode.PriceRange.EXCEED);
				}else{
					//错误数据？
					System.out.println("当前价格有误"+carPrice);
				}
				tProducts.setPosition(Integer.valueOf(tProductsTemp.getPosition()));
				tProducts.setTitle(tProductsTemp.getCargoNameCn());//车辆的中文名称作为title
				tProducts.setCarModel(tProductsTemp.getCarModel());//车类型
				tProducts.setOutColor(tProductsTemp.getOutColor());
				tProducts.setInColor(tProductsTemp.getInColor());
				tProducts.setCountry(tProductsTemp.getCountry());
				tProducts.setPower(tProductsTemp.getPower());
				tProducts.setEngine(tProductsTemp.getEngine());
				tProducts.setRecommend(CommonCode.RecommendStatus.NOT_RECOMMENDED);//产品默认写死为不推荐
				//tProducts.setFactoryDate(tProductsTemp.getFactoryDate());
				//出厂日期格式化
				Date format =FormatFactoryDate(tProductsTemp.getFactoryDate());
				tProducts.setFactoryDate(format);
				tProducts.setMustConfigure(tProductsTemp.getMustConfigure());
				//格式化必选配置金额
				Integer mustConfigurePrice = AmountFormatting(tProductsTemp.getMustConfigurePrice());
				tProducts.setMustConfigurePrice(new BigDecimal(mustConfigurePrice));
				//格式化json
				String jsonFormat = JSONFormat(tProductsTemp.getOptionalConfigure());
				tProducts.setOptionalConfigure(jsonFormat);
				
				tProducts.setCarPrice(new BigDecimal(carPrice));
				tProducts.setDoors(tProductsTemp.getDoors());
				tProducts.setSeats(tProductsTemp.getSeats());
				tProducts.setCarStandards(tProductsTemp.getCarStandards());
				tProducts.setRemarks(tProductsTemp.getRemarks());
				tProducts.setCreateDate(now);
				tProducts.setUpdateDate(now);
				tProducts.setVersion(CommonCode.Version.Default);
				tProducts.setContent("<p> </p>");
				tProducts.setStatus(CommonCode.SaveState.DRAFT);//存储至草稿箱中 作为不完整数据 需要待手动完善后才能作为正式数据上线
				isNext=tProductsMapper.insertSelective(tProducts)==CommonCode.DBSuccess.Success;
				if(isNext){
					TProductsCar tProductsCar = new TProductsCar();
					tProductsCar.setId(KeyUtil.generateDBKey());
					tProductsCar.setCarNo(tProductsTemp.getCarNo());
					tProductsCar.setEngineNo(tProductsTemp.getEngine());
					tProductsCar.setStatus(CommonCode.ProductsTempCarStatus.AVAILABLE);
					tProductsCar.setCreateTime(now);
					tProductsCar.setUpdateTime(now);
					tProductsCar.setVersion(CommonCode.Version.Default);
					tProductsCar.setProductsId(tProducts.getProductsId());
					isNext=tProductsCarMapper.insertSelective(tProductsCar)==CommonCode.DBSuccess.Success;
					//导入成功/失败后修改temp表数据 缺少更新时间字段
					if(isNext){
						tProductsTemp.setStatus(CommonCode.ProductTempStatus.INSERTR);
						tProductsTempMapper.updateByPrimaryKeySelective(tProductsTemp);
					}else{
						tProductsTemp.setStatus(CommonCode.ProductTempStatus.FAIL);
						tProductsTempMapper.updateByPrimaryKeySelective(tProductsTemp);
					}
				}else{
					//数据新建失败无法关联  将该条数据视为失败
					tProductsTemp.setStatus(CommonCode.ProductTempStatus.FAIL);
					tProductsTempMapper.updateByPrimaryKeySelective(tProductsTemp);
					continue;
				}
			}
		}
	}
	
	/**
	 * 时间格式化
	 * @param factoryDate
	 * @return
	 */
	private Date FormatFactoryDate(String factoryDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date parse=new Date();
		try {
			parse = dateFormat.parse(factoryDate);
		} catch (ParseException e) {
			
			dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			try {
				parse = dateFormat.parse(factoryDate);
			} catch (ParseException e1) {
				logger.error(e + "时间转换失败");
				e1.printStackTrace();
			}
		}
		return parse;
	}

	/**
	 * 金额格式化
	 * @param carPrice
	 * @return
	 */
	private  Integer AmountFormatting(String carPrice) {
		int x=0;  
        //遍历数组的每个元素    
        for(int i=0;i<=carPrice.length()-1;i++) {  
            String getstr=carPrice.substring(i,i+1);  
            if(getstr.equals(",")){  
                x++;  
            }  
        }  
        for (int i = 0; i < x; i++) {
        	 int a=carPrice.indexOf(",");
        	 carPrice=carPrice.substring(0, a)+carPrice.substring(a+1);
		}
        Integer valueOf=0;
        try {
        	 double double1 = new Double(carPrice);
             valueOf=(int) double1;
		} catch (Exception e) {
			logger.error(e + "金额转换失败");
			e.printStackTrace();
		}
        
		return valueOf;
	}
	
	private String JSONFormat(String configurePrice){
		List<Map<String, String>> listItems = Lists.newArrayList();
		Gson gs = new Gson();
		Map<String,String> fronJson2Map = GsonUtil.fronJson2Map(configurePrice);
		for (String  name : fronJson2Map.keySet()) {
			Map<String, String> next = Maps.newHashMap();
			String string = fronJson2Map.get(name);
			if(!name.isEmpty() &&  !string.isEmpty()){
				next.put("id", KeyUtil.generateDBKey() + "");
				next.put("name", name);
				
				next.put("number", (AmountFormatting(fronJson2Map.get(name))).toString());
				listItems.add(next);
			}
		}
		return gs.toJson(listItems);
	}
	/**
	 * 自动确认收车
	 */
	@Override
	public List<TOrders> getAutomaticTakeCarOrders() {
		return tOrdersMapper.queryAutomaticTakeCar();
	}
	/**
	 * 自动收车
	 * @throws ParseException 
	 */
	@Override
	public boolean updateAutomaticTakeCarOrders(Long orderId) throws ParseException {
		TOrders order = tOrdersMapper.selectByPrimaryKey(orderId);
		order.setStatus(CommonCode.SaveStatus.COMPLETE);
		order.setCompleteTime(new Date());
		order.setUpdateDate(new Date());
		Boolean isNext = tOrdersMapper.updateByPrimaryKeySelective(order)==CommonCode.DBSuccess.Success;
		if(isNext)System.out.println("自动收货成功");
		return isNext;
	}
	
	@Override
	public Map<String, List<TUsers>> queryUpGradeUserList() {
		//TODO
		//取得sysConfig中的升级配置
		Gson gs = new Gson();
		List<TUsers> tuser =null;
		List<TUsers> downGradeUser =null;
		//SysConfig sysConfig = new SysConfig();
		//查询个人升级规则
		SysConfig personal_upgrade = sysConfigMapper.queryByGroupNameAndStateAndRemark(CommonCode.ConfigGroup.UPGRADE_DOWNGRADE,CommonCode.ConfigState.ENABLE,CommonCode.DowngradeOrUpgradeScRemark.PERSONAL_UPGRADE);
		SysConfig sale_upgrade = sysConfigMapper.queryByGroupNameAndStateAndRemark(CommonCode.ConfigGroup.UPGRADE_DOWNGRADE,CommonCode.ConfigState.ENABLE,CommonCode.DowngradeOrUpgradeScRemark.SALE_UPGRADE);
		//SysConfig sysConfig = sysConfigMapper.queryByGroupNameAndState(CommonCode.ConfigGroup.UPGRADE_CONFIG,CommonCode.ConfigState.ENABLE);
		if(sale_upgrade!=null &&  personal_upgrade!=null){
		String personal_upgrade_scValue = personal_upgrade.getScValue();
		String sale_upgrade_scValue = sale_upgrade.getScValue();
		LinkedTreeMap<String, String> personal_upgrade_obj = (LinkedTreeMap<String, String>)gs.fromJson(personal_upgrade_scValue, Object.class);
		LinkedTreeMap<String, String> sale_upgrade_obj = (LinkedTreeMap<String, String>)gs.fromJson(sale_upgrade_scValue, Object.class);
 		//查询到为个人的用户  且为可升级用户
		LinkedTreeMap<String, String> obj = new LinkedTreeMap<String, String>();
 		tuser = tUsersMapper.queryUpgradePersonalList();
 		for (int i = 0; i < tuser.size(); i++) {
 			if(tuser.get(i).getSysLevel() == CommonCode.SysLevel.PERSONAL){
 				obj=personal_upgrade_obj;
 			}else{
 				obj=sale_upgrade_obj;
 			}
 			//两次验证 首先验证总月数的对应总额
 			Integer judge = tOrdersMapper.queryTotalExamine(tuser.get(i).getId(),obj.get(CommonCode.UpgradeDowngradeRule.MONTH),obj.get(CommonCode.UpgradeDowngradeRule.TOTALSALES),obj.get(CommonCode.UpgradeDowngradeRule.TOTALAMOUNT));
 			if(judge ==null || judge == CommonCode.DBSuccess.Fail){
 				tuser.remove(i);
 			}else{
 				Integer month = Integer.valueOf(obj.get(CommonCode.UpgradeDowngradeRule.MONTH));
 				//按每月查询 查询出所有月数
 		 		for (int j = 1; j <=month; j++) {
 		 			Integer eachJudge = tOrdersMapper.queryEachExamine(j, j-1, obj.get(CommonCode.UpgradeDowngradeRule.MONTHLYSALES), obj.get(CommonCode.UpgradeDowngradeRule.MONTHLYAMOUNT),tuser.get(i).getId());
 		 			if(eachJudge ==null || eachJudge == CommonCode.DBSuccess.Fail){
 		 				tuser.remove(i);
 		 				break;
 		 			}
 		 		}
 			}
 			}
		}
 		//查询降级规则
 		//SysConfig sysConfigDownGrade = sysConfigMapper.queryByGroupNameAndState(CommonCode.ConfigGroup.DOWNGRADE_CONFIG,CommonCode.ConfigState.ENABLE);
		SysConfig sale_downgrade = sysConfigMapper.queryByGroupNameAndStateAndRemark(CommonCode.ConfigGroup.UPGRADE_DOWNGRADE,CommonCode.ConfigState.ENABLE,CommonCode.DowngradeOrUpgradeScRemark.SALE_DOWNGRADE);
		SysConfig distributor_downgrade = sysConfigMapper.queryByGroupNameAndStateAndRemark(CommonCode.ConfigGroup.UPGRADE_DOWNGRADE,CommonCode.ConfigState.ENABLE,CommonCode.DowngradeOrUpgradeScRemark.DISTRIBUTOR_DOWNGRADE);
		if(sale_downgrade!=null&& distributor_downgrade !=null){
 		
 		String sale_downgrade_scValueDownGrade = sale_downgrade.getScValue();
 		String distributor_downgrade_scValueDownGrade = distributor_downgrade.getScValue();
 		
 		LinkedTreeMap<String, String> sale_downgrade_objDownGrade = (LinkedTreeMap<String, String>)gs.fromJson(sale_downgrade_scValueDownGrade, Object.class);
 		LinkedTreeMap<String, String> distributor_downgrade_objDownGrade = (LinkedTreeMap<String, String>)gs.fromJson(distributor_downgrade_scValueDownGrade, Object.class);
 		
 		LinkedTreeMap<String, String> objDownGrade = new LinkedTreeMap<String, String>();
 		//查询降级用户
 		downGradeUser = tUsersMapper.queryDownGradePersonalList();
 		for (int i = 0; i < downGradeUser.size(); i++) {
 			if(downGradeUser.get(i).getSysLevel() == CommonCode.SysLevel.PERSONAL){
 				objDownGrade=sale_downgrade_objDownGrade;
 			}else{
 				objDownGrade=distributor_downgrade_objDownGrade;
 			}
 			Integer downGradejudge = tOrdersMapper.queryTotalExamineDownGrade(downGradeUser.get(i).getId(),objDownGrade.get(CommonCode.UpgradeDowngradeRule.MONTH),objDownGrade.get(CommonCode.UpgradeDowngradeRule.TOTALSALES),objDownGrade.get(CommonCode.UpgradeDowngradeRule.TOTALAMOUNT));
 			if(downGradejudge !=null && downGradejudge !=CommonCode.DBSuccess.Success){
 				//如果为null则表示该月该用户并未有收益 sum（amount）为null  
 				if(downGradejudge == CommonCode.DBSuccess.Fail){
 					downGradeUser.remove(i);
 				}
 			}else{
 				Integer downGradeMonth = Integer.valueOf(objDownGrade.get(CommonCode.UpgradeDowngradeRule.MONTH));
 				//按每月查询 查询出所有月数
 		 		for (int j = 1; j <=downGradeMonth; j++) {
 		 			Integer eachJudge = tOrdersMapper.queryEachExamineDownGrade(j, j-1, objDownGrade.get(CommonCode.UpgradeDowngradeRule.MONTHLYSALES), objDownGrade.get(CommonCode.UpgradeDowngradeRule.MONTHLYAMOUNT),downGradeUser.get(i).getId());
 		 			if(eachJudge!=null){
 		 				//如果为null则表示该月该用户并未有收益 sum（amount）为null  
 		 				if( eachJudge == CommonCode.DBSuccess.Fail){
 	 		 				downGradeUser.remove(i);
 	 		 				break;
 	 		 			}
 		 			}
 		 		}
 			}
 			}
 		}
 		
 		Map<String, List<TUsers>> userList = new HashMap<String, List<TUsers>>();
 		userList.put("upgrade", tuser);//升级
 		userList.put("downGrade", downGradeUser);//降级
 		
		return userList;
	}

	/**
	 * 用户实际等级升级
	 */
	@Override
	public boolean updateUserUpgrade(Long id) {
		TUsers tUsers = tUsersMapper.selectByPrimaryKey(id);
		if(tUsers == null){
			return false;
		}
		tUsers.setUpdateDate(new Date());
		//判断用户当前实际等级和系统等级  用户为个人则自动升级 为销售则只修改系统实际等级 
		if(tUsers.getRealLevel() == CommonCode.SysLevel.PERSONAL){
			//tUsers.setRealLevel(tUsers.getRealLevel() +1 );
			tUsers.setSysLevel(tUsers.getRealLevel() +1);
		}else if(tUsers.getRealLevel() == CommonCode.SysLevel.SELLER){
			//此时用户为销售  需要通过邀请升级 只修改系统等级不修改实际等级
			tUsers.setSysLevel(tUsers.getRealLevel()+ 1);
		}else{
			//此时用户为经销商 不需要升级  除非手动修改数据库 一般不会出现经销商数据出现在升级list中 只记录日志
			logger.info("错误的用户升级数据：经销商数据出现在升级list中，id:"+tUsers.getId());
		}
		//tUsers.setRealLevel(tUsers.getRealLevel() +1 );//符合规则直接升级
		return tUsersMapper.updateByPrimaryKeySelective(tUsers) ==CommonCode.DBSuccess.Success;
		
	}

	@Override
	public boolean updateUserDowngrade(Long id) {
		//查询降级用户
		TUsers tUsers = tUsersMapper.selectByPrimaryKey(id);
		if(tUsers ==null){
			return false;
		}
		//防止出现升级规则与降级规则冲突的状况 此时验证该用户是否为今日新升级用户
		TUsers tUsersValidate =tUsersMapper.queryValidateDowngrade(id);
		if(tUsersValidate!=null){
			//为NULL证明该用户并非今日新升级用户 
			if(tUsers.getRealLevel() == CommonCode.SysLevel.BOSS){
				//此时用户为经销商  不可自动降低 只处理实际等级 等待手动降级
				tUsers.setSysLevel(tUsers.getRealLevel()-1);
			}else if(tUsers.getRealLevel() == CommonCode.SysLevel.SELLER){
				//此时用户为销售  可自动降级
				tUsers.setSysLevel(tUsers.getRealLevel()- 1);
				tUsers.setRealLevel(tUsers.getRealLevel()-1);
			}else{
				//此时用户为个人 不可降级  除非手动修改数据库 一般不会出现个人用户数据出现在降级list中 只记录日志
				logger.info("错误的用户降级数据：个人数据出现在降级list中，id:"+tUsers.getId());
			}
			tUsers.setUpdateDate(new Date());
			return tUsersMapper.updateByPrimaryKeySelective(tUsers) ==CommonCode.DBSuccess.Success;
		}else{
			//该用户为今日新升级用户 不在做降级处理
			return true;
		}
	}

/*	public static void main(String[] args) {
		String carPrice  = "899,900.00";
		
		
		int x=0;  
        //遍历数组的每个元素    
        for(int i=0;i<=carPrice.length()-1;i++) {  
            String getstr=carPrice.substring(i,i+1);  
            if(getstr.equals(",")){  
                x++;  
            }  
        }  
        System.out.print(x);  
        
        for (int i = 0; i < x; i++) {
        	 int a=carPrice.indexOf(",");
        	 carPrice=carPrice.substring(0, a)+carPrice.substring(a+1);
   		  System.out.println(carPrice);
		}
        carPrice = carPrice.substring(0, carPrice.length() - 3);
		 
		Integer valueOf = Integer.valueOf(carPrice);
		System.out.println(valueOf);
	}*/
	/*public static void main(String[] args) {
		List<Map<String, String>> listItems = Lists.newArrayList();
		Gson gs = new Gson();
		String json = "{'无钥匙进入':'18,800.00','迎宾灯':'4,500.00'}";
		Map<String,String> fronJson2Map = GsonUtil.fronJson2Map(json);
		for (String  name : fronJson2Map.keySet()) {
			System.out.println(fronJson2Map.get(name));
			
			Map<String, String> next = Maps.newHashMap();
			next.put("id", KeyUtil.generateDBKey() + "");
			next.put("name", name);
			
			next.put("number", (AmountFormatting(fronJson2Map.get(name))).toString());
			listItems.add(next);
		}
		String json2 = gs.toJson(listItems);
		System.out.println(json2);
	}*/
	/*public static void main(String[] args) {
		Gson gs = new Gson();
		List<Map<String, String>> listItems = Lists.newArrayList();
		Map<String, String> next = Maps.newHashMap();
		next.put("modifiTime","1111");
		next.put("logistStatus", "2222");
		listItems.add(next);
		String json = gs.toJson(listItems);
		System.out.println(json);
		ArrayList<?> obj = (ArrayList<?>) gs.fromJson(json, Object.class);
		 listItems = Lists.newArrayList();
		for (Object object : obj) {
			Map<String, String> nexl = Maps.newHashMap();
			LinkedTreeMap<String, String> UserNext = (LinkedTreeMap<String, String>) object;
			nexl.put("modifiTime",UserNext.get("modifiTime"));
			nexl.put("logistStatus", UserNext.get("logistStatus"));
			listItems.add(nexl);
		}
		Map<String, String> nexl = Maps.newHashMap();
		nexl.put("modifiTime","333");
		nexl.put("logistStatus", "444");
		listItems.add(nexl);
		String jsons = gs.toJson(listItems);
		System.out.println(jsons);
		//obj.add(next);
		
	}*/
}

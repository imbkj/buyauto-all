package com.business.service.orders.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.business.service.audit.api.TAuditingApi;
import com.business.service.orders.api.ITOredersApi;
import com.buyauto.dao.orders.TOrdersDao;
import com.buyauto.dao.products.TProductsPojo;
import com.buyauto.entity.orders.TOrderFinance;
import com.buyauto.entity.orders.TOrders;
import com.buyauto.entity.orders.TOrdersFiles;
import com.buyauto.entity.products.TProducts;
import com.buyauto.entity.products.TProductsCar;
import com.buyauto.entity.products.TProductsImage;
import com.buyauto.entity.sys.SysConfig;
import com.buyauto.entity.user.TUsers;
import com.buyauto.mapper.orders.TOrderFinanceMapper;
import com.buyauto.mapper.orders.TOrdersDaoMapper;
import com.buyauto.mapper.orders.TOrdersFilesMapper;
import com.buyauto.mapper.orders.TOrdersMapper;
import com.buyauto.mapper.products.TProductsCarMapper;
import com.buyauto.mapper.products.TProductsImageMapper;
import com.buyauto.mapper.products.TProductsMapper;
import com.buyauto.mapper.sys.SysConfigMapper;
import com.buyauto.mapper.user.TUsersMapper;
import com.buyauto.util.method.CommonCode;
import com.buyauto.util.method.CustomDateEditor;
import com.buyauto.util.method.KeyUtil;
import com.buyauto.util.pojo.FinanceDto;
import com.buyauto.util.pojo.PageVo;
import com.buyauto.util.pojo.UserSessionInfo;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
@Service
public class TOrdersImpl implements ITOredersApi {

	private static final Logger logger = LoggerFactory.getLogger(TOrdersImpl.class);
	
	@Autowired
	private TOrdersMapper ordersMapper;
	
	@Autowired
	private TOrdersDaoMapper ordersDaoMapper;
	
	@Autowired
	private TOrdersFilesMapper ordersFilesMapper;
	
	@Autowired
	private TProductsMapper productMapper;
	
	@Autowired
	private TProductsImageMapper productImageMapper;
	
	@Autowired
	private TUsersMapper userMapper;
	
	@Autowired
	private TAuditingApi auditingApi;
	
	@Autowired
	private SysConfigMapper sysConfigMapper;
	
	@Autowired
	private TProductsCarMapper productCarMapper;
	
	@Autowired
	private TOrderFinanceMapper tOrderFinanceMapper;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(true));
	}
	/* (non-Javadoc)
	 * 新增订单
	 * @see com.business.service.orders.api.ITOredersApi#insertOrders(com.buyauto.entity.orders.TOrders)
	 */
	@Override
	public Integer insertOrders(TOrders orders) {
		//生成主键
		orders.setId(KeyUtil.generateDBKey());
		//创建日期
		orders.setCreateDate(new Date());
		//默认修改日期
		orders.setUpdateDate(new Date());
		return ordersMapper.insert(orders);
	}

	@Override
	public Integer deleteOrders(Long id) {
		return ordersMapper.deleteByPrimaryKey(id);
	}

	@Override
	public Integer updateOrders(TOrders orders) {
		//默认修改日期
		orders.setUpdateDate(new Date());
		return ordersMapper.updateByPrimaryKeySelective(orders);
	}
	/*  获取订单详情
	 * @see com.business.service.orders.api.ITOredersApi#getOrdersById(java.lang.Long)
	 */
	@Override
	public TOrdersDao getOrdersById(Long id) {
		TOrdersDao ordersDao=ordersDaoMapper.selectByPrimaryKey(id);
		if(ordersDao == null){
			return null;
		}
		//插入产品
		TProducts product=new TProducts();
		product = productMapper.selectByPrimaryKey(ordersDao.getProductId());
		//product.setCarP
		
		if(product != null && ordersDao.getPremiums()!=null){
			product.setMustConfigurePrice(product.getMustConfigurePrice().add(ordersDao.getPremiums()));
		}
		
		ordersDao.setProduct(product);
		//插入产品图片
		ordersDao.setProductImage(productImageMapper.getTProductsImageByProductId(ordersDao.getProductId()));
		//插入产品品牌
		//ordersDao.setCarBrandName(ordersDaoMapper.getCarBrandName(product.getBrandId()));
		//格式化创建时间
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ordersDao.setCreatTime(format.format(ordersDao.getCreateDate()));
		SysConfig SysConfig = sysConfigMapper.queryByGroupNameAndState(CommonCode.ConfigGroup.ACCOUNT_NUMBER, CommonCode.ConfigState.ENABLE);
		ordersDao.setAccountNumber(SysConfig.getScValue());
		//插入对公账号
		return ordersDao;
	}
	/**
	 * 分页查询订单列表
	 */
	@Override
	public PageVo<TOrdersDao> queryOrders(Long userId,Long companyId,Long orderId,int pageNumber, int pageSize,
			String strStartDate, String strEndDate, Integer state) {
		PageVo<TOrdersDao> pageVo=new PageVo<TOrdersDao>(0, pageNumber, pageSize);
		Date strStartDates = null;
		Date strEndDates = null;
		//格式化时间
		if(strStartDate!=null && strEndDate!=null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				strStartDates = sdf.parse(strStartDate);
				strEndDates = sdf.parse(strEndDate);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(null !=companyId && -1 == companyId) companyId = null;
		if(null !=state && -1 == state) state = null;
		//获得产品列表
		List<TOrdersDao> ordersList=ordersDaoMapper.selectordersList(userId,companyId,orderId,pageVo.getPageStartNumber(), pageVo.getPageEndNumber(),strStartDates,strEndDates,state);
		//包含产品类的集合
		List<TOrdersDao> mapOrdersList=new ArrayList<TOrdersDao>();
		//查询订购单数
 		int ordersCount=ordersDaoMapper.selectOrdersCount(userId,companyId,orderId,strStartDates,strEndDates,state);
 		//循环遍历ordersList将得到的TPoeducts类放入
 		for (TOrdersDao ordersDao : ordersList) {
 			TProducts product=new TProducts();
 			TProductsImage productImage=new TProductsImage();
 			//得到对应的产品
 			product=productMapper.selectByPrimaryKey(ordersDao.getProductId());
			ordersDao.setProduct(product);
			//获得产品图片
			productImage=productImageMapper.getTProductsImageByProductId(ordersDao.getProductId());
			ordersDao.setProductImage(productImage);
			//插入产品品牌
			//ordersDao.setCarBrandName(ordersDaoMapper.getCarBrandName(product.getBrandId()));
			//加入经销商名称
			ordersDao.setCompanyName(userMapper.getCompanyById(ordersDao.getUserId()));
			//格式化金额
			//ordersDao.setDeposit((VCToolbar.formatBigDecimalThousandBack(ordersDao.getDeposit(), true));
			mapOrdersList.add(ordersDao);
		}
 		pageVo.setTotal(ordersCount);
 		pageVo.setRows(mapOrdersList);
		return pageVo;
		
	}
	/**
	 * 修改订单状态
	 */
	@Override
	public Integer orderChg(Long id, Integer status,Integer oldStatus,Integer orderPayStatus) {
		Date now = new Date();
		Integer result = CommonCode.operateRes.YES;
		TOrders orderChg=new TOrders();
		orderChg=ordersMapper.selectByPrimaryKey(id);
		TProductsCar tProductsCar = productCarMapper.selectByPrimaryKey(orderChg.getCarId());
		//oldStatus为空表示不同意取消订单
		if (oldStatus != null) {
			//保留原状态
			orderChg.setOldStatus(oldStatus);
		}
		if(orderPayStatus!=null){
			//如果同意取消订单并且同意退款设置支付状态为  退款
			orderChg.setPayStatus(orderPayStatus);
		}
		if (status != null) {
			//订单完成时设置成交时间
			if(status == CommonCode.SaveStatus.COMPLETE){
				orderChg.setCompleteTime(now);
			}
			if(status == CommonCode.SaveStatus.NEWCREATE){
				orderChg.setCreateDate(now);
				//重新提交次数加1
				orderChg.setResubmit(orderChg.getResubmit() + 1);
			}
			//订单关闭或取消的时候释放车架号
			if (status == CommonCode.SaveStatus.CLOSEORDER || status == CommonCode.SaveStatus.CANCELORDER) {
				orderChg.setCarId(null);
			}
			//定金审核不通过
			if (status == CommonCode.SaveStatus.SECONDCHK) {
				orderChg.setPayStatus(0);
			}
		}
		
		//默认修改日期
		orderChg.setUpdateDate(now);
		//设置现状态
		orderChg.setStatus(status);
		Integer updateRes = ordersMapper.updateByPrimaryKeySelective(orderChg);
		if(updateRes > CommonCode.operateRes.NO){
			//TODO 解锁车架号
			/**订单关闭或取消的时候解锁车架号**/
			//			if (status !=null && status == CommonCode.SaveStatus.CLOSEORDER || status == CommonCode.SaveStatus.CANCELORDER) {
			//				if (tProductsCar != null) {
			//					tProductsCar.setUpdateTime(now);
			//					tProductsCar.setStatus(CommonCode.ProductsTempCarStatus.AVAILABLE);
			//					productCarMapper.updateByPrimaryKeySelective(tProductsCar);
			//				}
			//			}
			/**订单关闭或取消的时候释放库存**/
	    	productMapper.releaseStock(orderChg.getProductId());
			
		}else{
			result = CommonCode.operateRes.NO;
		}
		return result;
	}
	/**
	 * 订单凭证上传
	 */
	@Override
	public Boolean addOrdersFiles(Long orderId,Integer type,String depositBackImageH,String depositBackImageOld) {
		Boolean isNext = true;
		TOrdersFiles ordersFiles=new TOrdersFiles();
		List<TOrdersFiles> ordersFilesList  = ordersFilesMapper.getOrdersFilesByOrderId(orderId,type);
		if(depositBackImageOld=="" || depositBackImageOld == null){
			depositBackImageOld="[]";
		}
		JSONArray jsonArray;
		JSONObject jsonObject;
		try {
			jsonArray= new JSONArray(depositBackImageOld);
			if(ordersFilesList.size() != jsonArray.length()){
				//旧图有改动 找到已经不存在的图 删除
				for (int j = 0; j < ordersFilesList.size(); j++)  {
					for (int i = 0; i < jsonArray.length(); i++) {
						jsonObject = jsonArray.getJSONObject(i);
						System.out.println(jsonObject.getString("name"));
						System.out.println(ordersFilesList.get(j).getFilePath());
						if(ordersFilesList.get(j).getFilePath().equals(jsonObject.getString("name"))){
							ordersFilesList.remove(j);
						}
					}
				}
				for (TOrdersFiles ordersImg : ordersFilesList) {
					ordersFilesMapper.deleteByPrimaryKey(ordersImg.getId());
				}
			}
			jsonArray = new JSONArray(depositBackImageH);
			for (int i = 0; i < jsonArray.length(); i++) {
				jsonObject = jsonArray.getJSONObject(i);
				String key = jsonObject.getString("preId");
				String value = jsonObject.getString("name");
				
				ordersFiles=new TOrdersFiles();
				//生成主键
				ordersFiles.setId(KeyUtil.generateDBKey());
				//默认创建日期
				ordersFiles.setCreateDate(new Date());
				ordersFiles.setOrderId(orderId);
				ordersFiles.setFileName(key);
				ordersFiles.setFilePath(value);
				ordersFiles.setType(type);
				//默认状态启用
				ordersFiles.setStatus(CommonCode.ConfigState.ENABLE);
				ordersFiles.setVersion(CommonCode.Version.Default);
				isNext = ordersFilesMapper.insertSelective(ordersFiles) ==CommonCode.DBSuccess.Success&&isNext;	
			}
		} catch (JSONException e) {
			logger.error(e + "多图转json失败");
			e.printStackTrace();
		}
		if(isNext && type == CommonCode.OrdersFilesType.CARCERTIFICATE){
			//更新订单状态
			TOrders tOrders = ordersMapper.selectByPrimaryKey(orderId);
			tOrders.setUpdateDate(new Date());
			tOrders.setStatus(CommonCode.SaveStatus.WAITSHIP);
			ordersMapper.updateByPrimaryKeySelective(tOrders);
		}
		return isNext;
	}
	
	/**
	 * 经销商列表  筛选条件
	 */
	@Override
	public List<TUsers> companyList() {
		return userMapper.findCompany();
	}
	/**
	 * 获取订单凭证
	 */
	@Override
	public List<TOrdersFiles> queryOrdersFiles(Long orderId, Integer type) {
		if (type == null) {
			//获取定金支付凭证和尾款支付凭证
			return ordersFilesMapper.getOrdersFilesPaied(orderId);
		}else{
			return ordersFilesMapper.getOrdersFilesByOrderId(orderId, type);
		}
	}
	/**
	 * 订单列表
	 */
	@Override
	public PageVo<TOrdersDao> ordersPcList(Long userId,Long companyId,int pageNumber, int pageSize, Integer state,String searchTitle) {
		
		PageVo<TOrdersDao> pageVo=new PageVo<TOrdersDao>(0, pageNumber, pageSize);
		//判断登录人员岗位
		Long user_id=null;
		TUsers user=userMapper.selectByPrimaryKey(userId);
		//财务
		if (user!=null) {
			if (user.getPosition()==CommonCode.UserState.POSITION_FINANCE || user.getPosition()==CommonCode.UserState.POSITION_ADMIN) {
				//管理员和财务可以查看经销商所有订单
				user_id=null;
			}else{
				//销售可以查看自己的订单
				user_id=userId;
			}
		}
		
		if(null !=state && -1 == state) state = null;
	    List<TOrdersDao> ordersList = ordersDaoMapper.searchOrdersPc(user_id, companyId, pageVo.getPageStartNumber(), pageVo.getPageEndNumber(),state,searchTitle);
		int ordersCount = ordersDaoMapper.searchOrdersCountPc(user_id,companyId,state,searchTitle);
		
		List<TOrdersDao> newOrdersList=new ArrayList<TOrdersDao>();
		
		for (TOrdersDao dao : ordersList) {
			TProducts product=new TProducts();
 			//得到对应的产品
 			product=productMapper.selectByPrimaryKey(dao.getProductId());
 			//UserSessionInfo userSessionInfo = new UserSessionInfo();
 			//TProductsPojo tProductsPojo = productMapper.queryProductsDetailed(product.getProductsId());
 			//userSessionInfo.setId(user_id);
 			//BigDecimal actualCarCarPrice = actualCarCarPrice(userSessionInfo,tProductsPojo);
 			//product.setCarPrice(actualCarCarPrice);
 			dao.setProduct(product);
 			TProductsImage productImage = new TProductsImage();
 			
 			
 			//获得产品图片
			productImage=productImageMapper.getTProductsImageByProductId(dao.getProductId());
			dao.setProductImage(productImage);
 			//插入产品品牌
 			//dao.setCarBrandName(ordersDaoMapper.getCarBrandName(product.getBrandId()));
			//加入经销商名称
 			dao.setCompanyName(userMapper.getCompanyById(dao.getUserId()));
 			
// 			BigDecimal pre = dao.getPremiums();//保险金额
// 			if(pre==null)pre = new BigDecimal(0);
// 			BigDecimal MustPrice = product.getMustConfigurePrice();/*必须价格总价*/
// 			product.setMustConfigurePrice(MustPrice.add(pre));/*必须件总价包含保险价格*/
 			
 			newOrdersList.add(dao);
		}
		
		pageVo.setTotal(ordersCount);
		pageVo.setRows(newOrdersList);
		
		return pageVo;
	}
	/**
	 * 审核取消订单
	 */
	@Override
	public int cancelOrders(Integer result, Integer type, Long id,
			String operName, Long operId, String info) {
		TOrders orders=ordersMapper.selectByPrimaryKey(id);
		int updateRes=0;
		try {
			//将取消原因入订单表
			orders.setCancelReason(info);
			updateRes=ordersMapper.updateByPrimaryKeySelective(orders);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return auditingApi.insertAudit(result, type, id, operName, operId, info);
	}
	
	
	/**
	 * 开始录入order表
	 */
	@Override
	public TOrders orderSubmit(TProductsPojo tProductsPojo, String extractPerson, Long sysConfigId, String remarks, String extractDate,String deliveryMode, String distribution,Long userId,Integer insuranceType) {
		Date now = new Date();
		TOrders order = new TOrders();
		TUsers tUsers = userMapper.selectByPrimaryKey(userId);
		boolean isNext =false;
		if(tProductsPojo.getStatus() != CommonCode.SaveState.SHELVES){
			logger.info("产品已下架");
			order.setCancelReason(CommonCode.OrderErrorType.PRODUCT_SHELF);
	    	return order; 
		}
		SysConfig insuranceConfig =null;
		if(insuranceType ==null){
			logger.info("保险信息有误");
			order.setCancelReason(CommonCode.OrderErrorType.INSURANCE_ERROR);
	    	return order; 
		}else{
			if(insuranceType == CommonCode.InsuranceTyep.TYPE_ONE){
				insuranceConfig = sysConfigMapper.queryByGroupNameAndState(CommonCode.Insurance.TYPE_ONE, CommonCode.ConfigState.ENABLE);
			}else if(insuranceType == CommonCode.InsuranceTyep.TYPE_TWO){
				insuranceConfig = sysConfigMapper.queryByGroupNameAndState(CommonCode.Insurance.TYPE_TWO, CommonCode.ConfigState.ENABLE);
			}
			if(insuranceConfig==null){
				logger.info("保险信息有误");
				order.setCancelReason(CommonCode.OrderErrorType.INSURANCE_ERROR);
		    	return order; 
			}
		}
		if(tUsers ==null){
			logger.info("用户不存在");
			order.setCancelReason(CommonCode.OrderErrorType.USER_DOES_NOT_EXIST);
	    	return order; 
		}
		if(tUsers.getStatus()  == CommonCode.UserState.STATUS_FROZEN){
			logger.info("用户已禁用");
			order.setCancelReason(CommonCode.OrderErrorType.ROLE_PROHIBIT);
	    	return order;
		}
		if(deliveryMode.equals(CommonCode.deliveryMode.EXTRACT)){
			order.setTakeWay(CommonCode.ExtractionMethod.DISTRIBUTION);
			SysConfig sysConfig = sysConfigMapper.selectByPrimaryKey(sysConfigId);
			if(sysConfig ==null){
				logger.info("提车地址不存在");
				order.setCancelReason(CommonCode.OrderErrorType.THE_ADDRESS_NOT_PRESENT);
		    	return order; 
			}
			order.setTakeLocation(sysConfig.getScValue());
		}else if(deliveryMode.equals(CommonCode.deliveryMode.DISTRIBUTION)){
			order.setTakeWay(CommonCode.ExtractionMethod.SINCE);
			order.setTakeLocation(distribution);
		}else{
			//配送方式有误
			logger.info("配送方式有误");
			order.setCancelReason(CommonCode.OrderErrorType.DELIVERY_MODE_IS_INCORRECT);
	    	return order; 
		}
		//处理车架号
		//	    List<TProductsCar> tProductsCar = productCarMapper.queryCarNoByProductId(tProductsPojo.getProductsId());
		//	    if(tProductsCar.size() <=0){
		//	    	
		//	    	logger.info("没有车架号");
		//	    	order.setCancelReason(CommonCode.OrderErrorType.NO_FRAME_NUMBER);
		//	    	return order; 
		//	    }
		//	    for (TProductsCar tProductsCar2 : tProductsCar) {
		//	    	tProductsCar2.setStatus(CommonCode.ProductsTempCarStatus.DISABLE);//锁
		//	    	tProductsCar2.setUpdateTime(now);
		//	    	Integer feedback =productCarMapper.updateByLocking(tProductsCar2);
		//	    	if(feedback >0){
		//	    		tProductsCarId=tProductsCar2;
		//	    		break;
		//	    	}
		//		}
		//车架号逻辑取消 改为直接锁库存
		//确认库存
			if(tProductsPojo.getStock()==null||tProductsPojo.getStock()<1){
					logger.info("库存不足");
			    	order.setCancelReason(CommonCode.OrderErrorType.INSUFFICIENT_STOCK);
			    	return order;
			}else{
				//尝试锁定库存
				if(productMapper.reduceStock(tProductsPojo.getProductsId())==CommonCode.DBSuccess.Fail){
					logger.info("库存不足");
			    	order.setCancelReason(CommonCode.OrderErrorType.INSUFFICIENT_STOCK);
			    	return order;
				}
			}
		    try {
//			    if(tProductsCarId == null){
//			    	logger.info("车架号锁定失败");
//			    	order.setCancelReason(CommonCode.OrderErrorType.FRAME_NUMBER_BINDING_FAILED);
//			    	return order;
//			    }
				int configurePrice = 0;
				order.setId(KeyUtil.generateDBKey());
				order.setUserId(userId);
				order.setCompanyId(tUsers.getCompanyId());
				order.setProductId(tProductsPojo.getProductsId());
				//设置供应商ID
				order.setSupplierId(tProductsPojo.getOperId());
				
				//处理json
				String optionalConfigure = tProductsPojo.getOptionalConfigure();
				if(optionalConfigure!=null && !optionalConfigure.isEmpty()){
					Gson gs = new Gson();
					ArrayList<?> userJsonObj = (ArrayList<?>) gs.fromJson(optionalConfigure, Object.class);
					for (Object userItem : userJsonObj) {
						LinkedTreeMap<String, String> userNext = (LinkedTreeMap<String, String>) userItem;
						String number = userNext.get("number");
						configurePrice += Integer.parseInt(number);
					}
				}
				order.setPremiums(new BigDecimal(insuranceConfig.getScValue()));//保险金额
				order.setConfigure(optionalConfigure);//JSON
				order.setConfigurePrice(new BigDecimal(configurePrice));
				order.setCount(1);//购买数量暂时固定为1
				order.setDeliverTime(extractDate);
				order.setCustomer(extractPerson);
				order.setCustomerTel(tProductsPojo.getExtractPhone().toString());//提车人电话
				//计算定金
				//BigDecimal carPrice = tProductsPojo.getCarPrice();//车辆价格
				//根据用户角色加价
				UserSessionInfo info = new UserSessionInfo();
				info.setId(userId);
				TProductsPojo queryProductsDetailed = productMapper.queryProductsDetailed(tProductsPojo.getProductsId());
				BigDecimal carPrice = actualCarCarPrice(info,queryProductsDetailed);
				order.setCarPrice(carPrice);
				BigDecimal mustConfigurePrice = tProductsPojo.getMustConfigurePrice();//必选配置价格
				//总价 = 裸车价+必选配置价格+可选配置价格+保险费
				BigDecimal amount = carPrice.add(mustConfigurePrice).add(order.getConfigurePrice()).add(order.getPremiums());
				amount =  amount.setScale(0, BigDecimal.ROUND_HALF_UP);
				Integer depositRatio = tProductsPojo.getDeposit();//定金比例
				//车价 * 定金比例 \ 100
				BigDecimal deposit = carPrice.multiply(new BigDecimal(depositRatio)).divide(new BigDecimal(100)); 
				deposit =  deposit.setScale(0, BigDecimal.ROUND_HALF_UP);
				order.setDeposit(deposit);
				//order.setStatus(CommonCode.SaveStatus.NEWCREATE);//新建状态
				order.setStatus(CommonCode.SaveStatus.WAITDEPOSIT);//取消FTP后订单创建直接为代付定金状态
				order.setPayStatus(CommonCode.SaveStatus.UNPAIED);
				order.setMessage(remarks);
				order.setCreateDate(now);
				order.setLogisticsInfo("[]");
				order.setPremiums(new BigDecimal(insuranceConfig.getScValue()));//保险金额
				order.setUpdateDate(now);
				order.setCancelReason("");
				//order.setCarId(tProductsCarId.getId());
				order.setAmount(amount);
				order.setVersion(CommonCode.Version.Default);
				isNext =ordersMapper.insertSelective(order) == CommonCode.DBSuccess.Success;
			    } catch (Exception e) {
			    	//订单失败释放库存
			    	productMapper.releaseStock(tProductsPojo.getProductsId());
			    	throw e;
			    }finally{
//			    	if(!isNext && tProductsCarId!=null){
//			    		tProductsCarId.setUpdateTime(now);
//			    		tProductsCarId.setStatus(CommonCode.ProductsTempCarStatus.AVAILABLE);
//			    		productCarMapper.updateByPrimaryKeySelective(tProductsCarId);
//			    	}
			    }
		if(isNext){
			return  ordersMapper.selectByPrimaryKey(order.getId());
		}
		//创建失败 放开车架号的锁
		return null;
		
	}
	
	/**
	 * 销售人员列表
	 */
	@Override
	public List<TUsers> getUsersByCompanyId(Long companyId) {
		return userMapper.findUsersByCompanyId(companyId);
	}
	/**
	 * 修改车辆状态
	 */
	@Override
	public Boolean changeCarStatus(Long carId) {
		Boolean isNext=true;
		TProductsCar car = productCarMapper.selectByPrimaryKey(carId);
		try {
			car.setStatus(CommonCode.ProductCarStatus.SHIPPED);
			int chgRes = productCarMapper.updateByPrimaryKeySelective(car);
			if (chgRes<CommonCode.operateRes.YES) {
				isNext = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isNext;
	}
	/**
	 * 修改订单状态
	 */
	@Override
	public Boolean modifyOrder(String carNo, Integer status) {
		System.out.println("----------------------------------------carNo="+carNo+",status"+status);
		Boolean isNext = true;
		Date now = new Date();
		//根据车架号找到car_id
		Long carId = ordersDaoMapper.getCarIdByCarNo(carNo);
		TOrders orderChg = ordersMapper.getOrderByCarId(carId);
		/** 库存确认不通过  **/
		
		/*TProductsCar tProductsCar = productCarMapper.selectByPrimaryKey(orderChg.getCarId());
		if (status == CommonCode.SaveStatus.FIRSTCHK) {
			//TODO 解锁车架号
			//库存确认不通过，解锁数据库车架号
			tProductsCar.setUpdateTime(now);
			tProductsCar.setStatus(CommonCode.ProductsTempCarStatus.AVAILABLE); //解锁
			productCarMapper.updateByPrimaryKeySelective(tProductsCar);
		}*/
		
		/** 库存确认不通过  **/
		orderChg.setStatus(status);
		orderChg.setUpdateDate(now);
		int chgRes = ordersMapper.updateByPrimaryKeySelective(orderChg);
		if (chgRes < CommonCode.operateRes.YES) {
			isNext = false;
		}
		System.out.println("-----------------------------------------更新订单状态结果"+isNext);
		return isNext;
	}
	/*
	 * 销售取消订单
	 * @see com.business.service.orders.api.ITOredersApi#cancelOrderBySeller(java.lang.Long, java.lang.String)
	 */
	@Override
	public int cancelOrderBySeller(Long orderId, String cancelReason) {
		TOrders orders=ordersMapper.selectByPrimaryKey(orderId);
		//设置修改时间
		orders.setUpdateDate(new Date());
		//设置原状态
		orders.setOldStatus(orders.getStatus());
		//设置新状态为取消待审核
		orders.setStatus(CommonCode.SaveStatus.CANCELWAITCHECK);
		//将取消原因入订单表
		orders.setCancelReason(cancelReason);
		return ordersMapper.updateByPrimaryKeySelective(orders);
	}
	/*
	 * 上传车辆三证
	 * @see com.business.service.orders.api.ITOredersApi#uploadCarTh(java.lang.Long, java.util.List)
	 */
	@Override
	public Boolean uploadCarTh(String carNo, List<String> orderFiles) {
		Boolean isNext = true;
		Long carId=ordersDaoMapper.getCarIdByCarNo(carNo);
		TOrders tOrders = ordersMapper.getOrderByCarId(carId);
		if(tOrders != null && tOrders.getStatus() == CommonCode.SaveStatus.WAITTHREECARDS){
			TOrdersFiles orderFilesTh = new TOrdersFiles();
			try {
				//更改订单状态
				tOrders.setStatus(CommonCode.SaveStatus.WAITSHIP);
				tOrders.setUpdateDate(new Date());
				isNext = ordersMapper.updateByPrimaryKey(tOrders)== CommonCode.DBSuccess.Success && isNext;
				//上传三证图片
				if (orderFiles.size()>0) {
					for (String tOrdersFiles : orderFiles) {
						Long id = KeyUtil.generateDBKey();
						String strPre = StringUtils.substringBeforeLast(tOrdersFiles, ".");
						//文件名称
						String tOrdersFilesAfter = tOrdersFiles.replace(strPre,id+"");
						
						orderFilesTh.setId(KeyUtil.generateDBKey());
						orderFilesTh.setOrderId(tOrders.getId());
						orderFilesTh.setType(CommonCode.OrdersFilesType.CARCERTIFICATE);
						orderFilesTh.setFilePath(tOrdersFilesAfter);
						isNext = ordersFilesMapper.insertSelective(orderFilesTh) == CommonCode.DBSuccess.Success && isNext;
					}
				}
			} catch (Exception e) {
				isNext = false;
				logger.error("车辆三证上传失败" + e.getMessage());
			}
		}
		return isNext;
	}
	
	public String getCarNo(Long carId){
		TProductsCar pcar=productCarMapper.selectByPrimaryKey(carId);
		if(pcar!=null){
			return pcar.getCarNo();
		}
		return "";
	}
	
	@Override
	public TOrders getOrderById(Long orderId) {
		return ordersMapper.selectByPrimaryKey(orderId);
	}

	@Override
	public TOrders getOrderByCarNo(String carNo) {
		Long carId = ordersDaoMapper.getCarIdByCarNo(carNo);
		TOrders orderFind = ordersMapper.getOrderByCarId(carId);
		return orderFind;
	}
	
	@Override
	public Map<String, Object> carFinanceCalculate(BigDecimal amount, Integer term, FinanceDto financeDto) {
		BigDecimal priceLine = new BigDecimal(financeDto.getPriceLine());
		int bzj = financeDto.getBzj();
		int sxf = 0;
		int dq = 0;
		if (term == CommonCode.FinanceTerm.ONE) {
			sxf = financeDto.getSxfOne();
			dq = financeDto.getDqOne();
		} else if (term == CommonCode.FinanceTerm.TWO) {
			sxf = financeDto.getSxfTwo();
			dq = financeDto.getDqTwo();
		} else {
			sxf = financeDto.getSxfThree();
			dq = financeDto.getDqThree();
		}
		BigDecimal firstMonPay = null;
		BigDecimal bzjPay = null;
		BigDecimal sxfPay = null;
		boolean outRange = false;
		BigDecimal MonPay = amount.divide(new BigDecimal(term * 12),2,
				BigDecimal.ROUND_HALF_UP);
		if (amount.compareTo(priceLine) > 0) {
			bzjPay = amount.multiply(new BigDecimal(bzj).multiply(new BigDecimal(0.01))).setScale(2,
					BigDecimal.ROUND_HALF_UP);
			sxfPay = amount.multiply(new BigDecimal(sxf).multiply(new BigDecimal(0.01))).setScale(2,
					BigDecimal.ROUND_HALF_UP);
			outRange = true;
		} else {
			firstMonPay = amount.multiply(new BigDecimal(dq).multiply(new BigDecimal(0.01)).multiply(new BigDecimal(term))).add(MonPay)
					.setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		Map<String, Object> carFinance = new HashMap<String, Object>();
		carFinance.put("MonPay", MonPay);//月供
		carFinance.put("bzjPay", bzjPay);//保证金（ 大于70万有）
		carFinance.put("sxfPay", sxfPay);//手续费（ 大于70万有）
		carFinance.put("firstMonPay", firstMonPay);//首付（ <=70万有）
		carFinance.put("outRange", outRange);//true：大于70万 false不大于
		return carFinance;
	}
	@Override
	public void saveOrderFinance(TOrderFinance tOrderFinance) {
		tOrderFinanceMapper.insertSelective(tOrderFinance);
	}
	
	
	@Override
	public TOrderFinance getOrderFinanceByid(Long orderId) {
		return tOrderFinanceMapper.selectByOrderId(orderId);
	}
	
	public BigDecimal actualCarCarPrice(UserSessionInfo user, TProductsPojo tProductsPojo) {
		BigDecimal actualPrice = new BigDecimal("0");
		if (user == null) {
			// 此时用户未登录 看到的价格为个人用户所见价格
			BigDecimal a = tProductsPojo.getPersonal();
			actualPrice = tProductsPojo.getCarPrice().add(tProductsPojo.getPersonal() == null ? new BigDecimal("0") : tProductsPojo.getPersonal());
		} else {
			TUsers tUsers = userMapper.selectByPrimaryKey(user.getId());
			// 判断用户角色
			if (tUsers == null) {
				logger.info("未查到该用户，用户ID："+user.getId());
				actualPrice = tProductsPojo.getCarPrice().add(tProductsPojo.getPersonal() == null ? new BigDecimal("0") : tProductsPojo.getPersonal());
			} else if (tUsers.getPosition() == CommonCode.UserPosition.ADMIN || tUsers.getPosition() == CommonCode.UserPosition.FINANCE
					|| tUsers.getPosition() == CommonCode.UserPosition.SALE) {
				// 经销商及其下角色所见为个人经销商价格
				actualPrice = tProductsPojo.getCarPrice().add(tProductsPojo.getDistributor() == null ? new BigDecimal("0") : tProductsPojo.getDistributor());
			} else if (tUsers.getPosition() == CommonCode.UserPosition.SUPPLIER) {
				// 供应商所见为个人用户所见价格
				actualPrice = tProductsPojo.getCarPrice().add(tProductsPojo.getPersonal() == null ? new BigDecimal("0") : tProductsPojo.getPersonal());
			} else if (tUsers.getPosition() == CommonCode.UserPosition.PERSONAL) {
				// 用户为个人的时候 根据其当前系统等级决定所见价格
				if (tUsers.getRealLevel() == CommonCode.SysLevel.PERSONAL) {
					// 个人用户
					actualPrice = tProductsPojo.getCarPrice().add(tProductsPojo.getPersonal() == null ? new BigDecimal("0") : tProductsPojo.getPersonal());
				} else if (tUsers.getRealLevel() == CommonCode.SysLevel.SELLER) {
					// 个人销售
					actualPrice = tProductsPojo.getCarPrice().add(tProductsPojo.getSales() == null ? new BigDecimal("0") : tProductsPojo.getSales());
				} else if (tUsers.getRealLevel() == CommonCode.SysLevel.BOSS) {
					// 个人经销商
					actualPrice = tProductsPojo.getCarPrice().add(tProductsPojo.getDistributor() == null ? new BigDecimal("0") : tProductsPojo.getDistributor());
				} else {
					// 此时用户为异常数据 不在个人体系内 日志输出
					actualPrice = tProductsPojo.getCarPrice().add(tProductsPojo.getPersonal() == null ? new BigDecimal("0") : tProductsPojo.getPersonal());
					logger.info("系统等级错误用户" + tUsers.getId());
				}

			} else {
				// 此时用户为异常数据 不在角色体系内 输出日志
				actualPrice = tProductsPojo.getCarPrice().add(tProductsPojo.getPersonal() == null ? new BigDecimal("0") : tProductsPojo.getPersonal());
				logger.info("角色等级错误用户" + tUsers.getId());
			}
		}
		return actualPrice;
	}
	@Override
	public PageVo<TOrdersDao> supplierOrdersList(Long id, Long companyId, Integer pageNumber, Integer pageSize, Integer state, String searchTitle) {
		PageVo<TOrdersDao> pageVo=new PageVo<TOrdersDao>(0, pageNumber, pageSize);
		//判断登录人员岗位
		Long user_id=null;
		TUsers user=userMapper.selectByPrimaryKey(id);
		//供应商
		if (user!=null) {
			//不为供应商直接返回null
			if (user.getPosition()!=CommonCode.UserState.POSITION_SUPPLIER ) {
				return null;
			}
		}
	
		
		List<TOrdersDao> ordersList = ordersDaoMapper.searchSupplierOrdersPc(id, companyId, pageVo.getPageStartNumber(), pageVo.getPageEndNumber(),state,searchTitle);
		
	   // List<TOrdersDao> ordersList = ordersDaoMapper.searchOrdersPc(user_id, companyId, pageVo.getPageStartNumber(), pageVo.getPageEndNumber(),state,searchTitle);
		int ordersCount = ordersDaoMapper.searchSupplierOrdersPcCount(id,companyId,state,searchTitle);
		
		List<TOrdersDao> newOrdersList=new ArrayList<TOrdersDao>();
		
		for (TOrdersDao dao : ordersList) {
			TProducts product=new TProducts();
 			//得到对应的产品
 			product=productMapper.selectByPrimaryKey(dao.getProductId());
 			dao.setProduct(product);
 			TProductsImage productImage = new TProductsImage();
 			
 			
 			//获得产品图片
			productImage=productImageMapper.getTProductsImageByProductId(dao.getProductId());
			dao.setProductImage(productImage);
 			//插入产品品牌
 			//dao.setCarBrandName(ordersDaoMapper.getCarBrandName(product.getBrandId()));
			//加入经销商名称
 			dao.setCompanyName(userMapper.getCompanyById(dao.getUserId()));
 			newOrdersList.add(dao);
		}
		
		pageVo.setTotal(ordersCount);
		pageVo.setRows(newOrdersList);
		
		return pageVo;
	}
	
	
	
}

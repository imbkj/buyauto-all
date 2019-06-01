package com.controller.products;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.list.TreeList;
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.business.service.audit.api.TAuditingApi;
import com.business.service.cms.api.ICmsBannerApi;
import com.business.service.cms.api.ICmsNewsApi;
import com.business.service.products.api.IProductsApi;
import com.business.service.sys.api.ISysEngineApi;
import com.buyauto.dao.cms.CmsBannerPojo;
import com.buyauto.dao.cms.CmsNewsPojo;
import com.buyauto.dao.products.TProductsInputPojo;
import com.buyauto.dao.products.TProductsPojo;
import com.buyauto.entity.products.TProducts;
import com.buyauto.entity.products.TProductsImage;
import com.buyauto.entity.sys.SysConfig;
import com.buyauto.entity.user.TUserFinance;
import com.buyauto.entity.user.TUsers;
import com.buyauto.mapper.user.TUserFinanceMapper;
import com.buyauto.mapper.user.TUsersMapper;
import com.buyauto.util.method.CommonCode;
import com.buyauto.util.method.KeyUtil;
import com.buyauto.util.method.StringUtil;
import com.buyauto.util.pojo.PageVo;
import com.buyauto.util.pojo.UploadPath;
import com.buyauto.util.pojo.UserSessionInfo;
import com.external.products.api.IProductsPcApi;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

@Service("productsPcImpl")
public class ProductsPcImpl implements IProductsPcApi {

	private static final Logger logger = LoggerFactory.getLogger(ProductsPcImpl.class);

	@Autowired
	private IProductsApi productsApi;

	@Autowired
	private ISysEngineApi sysEngineApi;

	@Autowired
	private ICmsBannerApi cmsBannerApi;

	@Autowired
	private ICmsNewsApi cmsNewsApi;

	@Autowired
	private UploadPath uploadPath;

	@Autowired
	private TAuditingApi tAuditingImpl;

	@Autowired
	private TUserFinanceMapper tUserFinanceMapper;
	
	@Autowired
	private TUsersMapper  tUsersMapper;

	/**
	 * 展示首页热门推荐
	 */
	@Override
	public List<TProductsPojo> queryIndexRecommend(UserSessionInfo user) {
		List<TProductsPojo> productsList = productsApi.queryIndexRecommend(user);
		return productsList;
	}

	/**
	 * 查询品牌信息
	 * 
	 * @return
	 */
	@Override
	public List<SysConfig> queryBrandList() {
		return sysEngineApi.queryPackList();
	}

	/**
	 * 展示首页的十条商品信息(精选好车)
	 */
	@Override
	public List<TProductsPojo> queryIndexCommodityList(UserSessionInfo user) {
		return productsApi.queryIndexCommodityList(user);
	}

	/**
	 * 查询首页banner
	 */
	@Override
	public List<CmsBannerPojo> queryIndexBannerList() {
		return cmsBannerApi.queryIndexBannerList();
	}

	@Override
	public List<CmsNewsPojo> queryCmsNewsList() {
		return cmsNewsApi.queryCmsNewsList();
	}

	@Override
	public List<CmsNewsPojo> queryCmsNoticeList() {
		return cmsNewsApi.queryCmsNoticeList();
	}

	/**
	 * 查询车库中展示的车辆信息
	 * 
	 * @param brandId
	 *            品牌
	 * @param carType
	 *            车身
	 * @param priceRange
	 *            价格区间
	 * @param sortOrder
	 *            排序方式
	 * @param title
	 *            输入
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@Override
	public PageVo<TProductsPojo> querygarageProducts(Long brandId, Integer carType, Integer priceRange,
			Integer sortOrder, String title, Integer pageNumber, Integer pageSize, UserSessionInfo user) {
		return productsApi.querygarageProducts(brandId, carType, priceRange, sortOrder, title, pageNumber, pageSize,
				user);
	}

	@Override
	public TProductsPojo queryProductsDetailed(Long productsId, UserSessionInfo user) {
		TProductsPojo products = productsApi.queryProductsDetailed(productsId, user);
		if(products==null) return null;
		String opt = products.getOptionalConfigure();//可选配置(json)
		if(!StringUtil.isEmpty(opt)){
			products.setOptionalConfigureList(convertOpt(opt));//可选配置json转对象(提供app解析)
		}
		return products;
	}
	
	/**
	 * 可选配置json字符串转换成对象，提供app解析
	 * @param opt
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>>  convertOpt(String opt){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		try {
			Gson g =new Gson();
			list = g.fromJson(opt,list.getClass());
			return list;
		} catch (Exception e) {
			logger.error("可选配置json转对象(提供app解析)转换失败。"+opt);
			return list;
		}
	}

	/**
	 * 查询车辆详情
	 */
	@Override
	public TProductsPojo queryExhibitionTProducts(Long productsId, UserSessionInfo user) {
		TProductsPojo tProductsPojo = productsApi.queryExhibitionTProducts(productsId, user);
		if(tProductsPojo!=null){
			tProductsPojo.setFilePath(uploadPath.getShowPath());// 图片IP前缀
		}
		return tProductsPojo;
	}

	/**
	 * 确认用户的可选配置
	 */
	@Override
	public String validateConfigure(String optionalConfigure, String selective) {
		String[] split = selective.split(",");
		Gson gs = new Gson();
		List<Map<String, String>> listItems = Lists.newArrayList();
		try {
			ArrayList<?> dbJsonObj = (ArrayList<?>) gs.fromJson(optionalConfigure, Object.class);
			for (Object dbItem : dbJsonObj) {
				LinkedTreeMap<String, String> dbNext = (LinkedTreeMap<String, String>) dbItem;
				for (String selectId : split) {
					if (dbNext.get("id").equals(selectId)) {
						Map<String, String> resultMap = Maps.newHashMap();
						resultMap.put("id", dbNext.get("id"));
						resultMap.put("name", dbNext.get("name"));
						resultMap.put("number", dbNext.get("number"));
						listItems.add(resultMap);
					}
				}
			}
		} catch (Exception e) {
			logger.info("解析用户的可选配置出错！"+selective);
			e.printStackTrace();
		}
		return gs.toJson(listItems);
	}
	

	@Override
	public SysConfig querySysConfigAddress(Long sysConfigId) {
		return sysEngineApi.querySysConfigAddress(sysConfigId);
	}

	@Override
	public List<SysConfig> querySysConfigAddress() {
		return sysEngineApi.queryAddressList();

	}

	@Override
	public String queryProductsImg(Long productId, int cover) {
		return productsApi.queryProductsImg(productId, cover);
	}

	@Override
	public int queryCarNoCount(Long productsId) {
		return productsApi.queryCarNoCount(productsId);
	}

	/**
	 * 商品列表
	 * 
	 * @param status
	 *            审核状态
	 * @param title
	 *            产新名称
	 * @param productsId
	 *            产品ID
	 * @param strStartDate
	 *            开始时间
	 * @param strEndDate
	 *            结束时间
	 * @param pageNumber
	 *            当前页码
	 * @param pageSize
	 *            一页条数
	 * @return
	 */
	@Override
	public PageVo<TProducts> queryProductsList(String productStatus, String title, Integer status, Long productsId,
			String strStartDate, String strEndDate, Integer pageNumber, Integer pageSize, UserSessionInfo user) {
		Date starDate = null;
		Date endDate = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			if (!StringUtil.isEmpty(strStartDate)) {
				starDate = sdf.parse(strStartDate);
			}
			if (!StringUtil.isEmpty(strEndDate)) {
				starDate = sdf.parse(strEndDate);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("条件时间String转换Date出错");
		}
		return productsApi.queryProductsList(title, status, starDate, endDate, pageNumber, pageSize, productStatus,
				null, productsId, user);
	}

	/**
	 * 保存或修改产品车辆信息
	 * 
	 * @return
	 */
	@Override
	public Map<String, Object> saveProductsList(TProductsInputPojo tProductsInputPojo, UserSessionInfo user) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 如果新建提交，需要验证参数
		if (StringUtil.isEmpty(tProductsInputPojo.getDraftSave())) {
			Integer code = checkParam(tProductsInputPojo);
			if (code != null) {
				map.put(CommonCode.Product.CODE, code);
				return map;
			}
		}
		
		tProductsInputPojo.setOperId(user.getId());
		int saveState = 0;
		if (tProductsInputPojo.getDraftSave() == null || tProductsInputPojo.getDraftSave().isEmpty()) {
			// 新建
			saveState = CommonCode.SaveState.NEWLY;
		} else {
			// 草稿
			saveState = CommonCode.SaveState.DRAFT;
		}
		TProductsInputPojo pojo = null;
		try {
			pojo = productsApi.queryProductsList(tProductsInputPojo, saveState, CommonCode.SelfSupporting.YES);
		} catch (Exception e) {
			logger.info("供应商添加或修改产品失败！系统报错;"+e.getMessage());
			map.put(CommonCode.Product.CODE, CommonCode.Product.ERROR);
			return map;
		}
		if (pojo == null) {
			map.put(CommonCode.Product.CODE, CommonCode.Product.ERROR);
			return map;
		}
		map.put(CommonCode.Product.CODE, CommonCode.Product.SUCCESS);
		map.put(CommonCode.Product.OBJ, pojo);
		return map;
	}

	/**
	 * 提交车辆信息参数验证
	 * @return
	 */
	private Integer checkParam(TProductsInputPojo products) {
			if (StringUtil.isEmpty(products.getTitle())) {// 标题
				return CommonCode.Product.NTITLE;
			}
			if (products.getBrandId() == null || products.getBrandId().equals(null)) {// 品牌ID
				return CommonCode.Product.NBRANDID;
			}
			if (products.getPosition() == null) {// 车辆状态
				return CommonCode.Product.NPOSITION;
			}
			if (products.getPriceRange() == null) {// 价格区间
				return CommonCode.Product.NPRICERANGE;
			}
			if (products.getCarType() == null) {// 车辆类别
				return CommonCode.Product.NCARTYPE;
			}
			if (products.getDeposit() != null && (products.getDeposit() > 100 || products.getDeposit() < 0)) {// 定金支付比例
				return CommonCode.Product.NDEPOSIT;
			}
			if (StringUtil.isEmpty(products.getFactoryDate())) {// 出厂日期
				return CommonCode.Product.NFACTORYDATE;
			}
			if (StringUtil.isEmpty(products.getCarPrice())) {// 裸车价格
				return CommonCode.Product.NCARPRICE;
			}
			if (StringUtil.isEmpty(products.getMustConfigurePrice())) {// 必选件总价
				return CommonCode.Product.NMUSTCONFIGUREPRICE;
			}
			if (StringUtil.isEmpty(products.getChinaPrice())) {// 国内指导价
				return CommonCode.Product.NCHINAPRICE;
			}
			if (StringUtil.isEmpty(products.getStock())) {// 库存
				return CommonCode.Product.NSTOCK;
			}
			if (StringUtil.isEmpty(products.getShrinkImageH())) {// 缩略图
				return CommonCode.Product.NINDEXIMAGEH;
			}
			if (StringUtil.isEmpty(products.getIndexImageH())) {// 封面图
				return CommonCode.Product.NSHRINKIMAGEH;
			}
			if (StringUtil.isEmpty(products.getDetailedImageH())) {// 详情 (多图)
				return CommonCode.Product.NDETAILEDIMAGEH;
			}else{
				try {
					JSONArray jsonArray = new JSONArray(products.getDetailedImageH());/*新图*/
					if(jsonArray.length()<=0){
						if(StringUtil.isEmpty(products.getDetailedImageOld())){
							return CommonCode.Product.NDETAILEDIMAGEH;// 详情 (多图)
						}
						JSONArray jsonArrayOld = new JSONArray(products.getDetailedImageOld());/*老图*/
						if(jsonArrayOld.length()<=0){
							return CommonCode.Product.NDETAILEDIMAGEH;// 详情 (多图)
						}
					}
				} catch (JSONException e) {
					logger.error(e + "(详情)多图转json失败");
					e.printStackTrace();
				}
			}
			if (StringUtil.isEmpty(products.getRecommendImageH())) {// 推荐图
				return CommonCode.Product.NRECOMMENDIMAGEH;
			}
			if (StringUtil.isEmpty(products.getContent())) {// 产品正文
				return CommonCode.Product.NCONTENT;
			}
		return null;
	}

	/**
	 * 根据ID查询产品信息
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public TProductsPojo getProductsById(Long id) {
		// 根据ID查询产品信息
		TProductsPojo tProductsPojo = productsApi.showUpdateProducts(id);
		if(tProductsPojo!=null){
			// 图片的前缀IP地址
			tProductsPojo.setImgfilePath(uploadPath.getShowPath());
		}
		return tProductsPojo;
	}

	/**
	 * 产品删除
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public Boolean updataDelete(Long id) {
		return productsApi.updataDelete(id);
	}

	/**
	 * 产品下架
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public Boolean updataDisable(Long id) {
		return productsApi.updataDisable(id);
	}

	/**
	 * 产品还原
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public Boolean updataRestore(Long id) {
		return productsApi.updataRestore(id,CommonCode.SaveState.DRAFT);
	}

	/**
	 * 查看未通过原因
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public String getReasonById(Long id) {
		String infoNoPass = tAuditingImpl.getNotPassInfo(id);
		if(StringUtil.isEmpty(infoNoPass)){
			infoNoPass = "";
		}
		return infoNoPass;
	}

	/**
	 * 查询保险信息
	 */
	@Override
	public Map<String, Object> queryInsuranceConfig() {
		return sysEngineApi.queryInsuranceConfig();
	}

	@Override
	public SysConfig queryInsuranceType(Integer insuranceType) {
		return sysEngineApi.queryInsuranceType(insuranceType);
	}

	/**
	 * @Title: addSupplierFinance
	 * @Description: 添加供应商融资申请
	 * @param uId
	 * @param amount
	 * @param type
	 * @param port
	 * @param repayment
	 * @param term
	 * @return Integer    返回类型
	 */
	@Override
	public Integer addSupplierFinance(Long uId, String amount, Integer type, String port, Integer repayment,
			Integer term) {
		Float dbPort = null;
		if(port != null && !port.equals("")){
			dbPort = Float.valueOf(port);
		}
		TUserFinance tUserFinance = new TUserFinance(KeyUtil.generateDBKey(), uId, new BigDecimal(amount), repayment,
				dbPort, term, type, new Date(), CommonCode.SupplierFinanceStatus.WAITPASS);
		tUserFinanceMapper.insertSelective(tUserFinance);
		return CommonCode.SaveStatus.COMPLETE;
	}

	/**
	 * 更新产品上架
	 * @return
	 */
	@Override
	public Boolean updataEnable(Long id) {
		return productsApi.updataEnable(id);
	}

	/**
	 * 提交审核
	 * @param id
	 * @return
	 */
	@Override
	public String subAudit(Long id) {
		TProductsPojo products = productsApi.showUpdateProducts(id);
		Integer code =  checkParamSub(products);/*校验参数是否可以提交*/
		if(code!=null){
			return code.toString();
		}
		Integer num = productsApi.subAudit(id);
		return (num>CommonCode.RESULTNUM?CommonCode.Product.SUCCESS:CommonCode.Product.ERROR).toString();
	}
	
	/**
	 * 提交审核时，验证参数是否填写完整
	 * @return
	 */
	private Integer checkParamSub(TProductsPojo products) {
		if(products==null){
			return CommonCode.Product.NQPRODUCT;//未查到该产品
		}
		if (StringUtil.isEmpty(products.getTitle())) {// 标题
			return CommonCode.Product.NTITLE;
		}
		if (products.getBrandId() == null || products.getBrandId().equals(null)) {// 品牌ID
			return CommonCode.Product.NBRANDID;
		}
		if (products.getPosition() == null) {// 车辆状态
			return CommonCode.Product.NPOSITION;
		}
		if (products.getPriceRange() == null) {// 价格区间
			return CommonCode.Product.NPRICERANGE;
		}
		if (products.getCarType() == null) {// 车辆类别
			return CommonCode.Product.NCARTYPE;
		}
		if (products.getDeposit() != null && (products.getDeposit() > 100 || products.getDeposit() < 0)) {// 定金支付比例
			return CommonCode.Product.NDEPOSIT;
		}
		if (StringUtil.isEmpty(products.getFactoryDate())) {// 出厂日期
			return CommonCode.Product.NFACTORYDATE;
		}
		if (StringUtil.isEmpty(products.getCarPrice())) {// 裸车价格
			return CommonCode.Product.NCARPRICE;
		}
		if (StringUtil.isEmpty(products.getMustConfigurePrice())) {// 必选件总价
			return CommonCode.Product.NMUSTCONFIGUREPRICE;
		}
		if (StringUtil.isEmpty(products.getChinaPrice())) {// 国内指导价
			return CommonCode.Product.NCHINAPRICE;
		}
		if (StringUtil.isEmpty(products.getStock())) {// 库存
			return CommonCode.Product.NSTOCK;
		}
		if (StringUtil.isEmpty(products.getContent())) {// 产品正文
			return CommonCode.Product.NCONTENT;
		}
		List<TProductsImage> imgs = products.gettProductsImage();
		if(imgs==null || imgs.size()<=0){
			return CommonCode.Product.NINDEXIMAGEH;
		}
		Boolean fail0 = true;// 缩略
		Boolean fail1 = true;// 封面
		Boolean fail2 = true;// 推荐
		Boolean fail3 = true;// 详情
//		Boolean fail4 = true;// 车辆三证
		for (TProductsImage img : imgs) {
			Integer type = img.getType();
			if(CommonCode.ProductsImageType.THUMBNAIL == type){
				fail0 = false;// 缩略
			}
			if(CommonCode.ProductsImageType.COVER == type){
				fail1 = false;// 推荐	
			}
			if(CommonCode.ProductsImageType.RECOMMEND == type){
				fail2 = false;// 推荐
			}
			if(CommonCode.ProductsImageType.DETAILS == type){
				fail3 = false;// 详情
			}
//			if(CommonCode.ProductsImageType.PRODUCTFILE == type){
//				fail4 = false;// 车辆三证
//			}
		}
		if(fail0){
			return CommonCode.Product.NINDEXIMAGEH;// 缩略图
		}
		if(fail1){
			return CommonCode.Product.NSHRINKIMAGEH;// 封面图
		}
		if(fail2){
			return CommonCode.Product.NDETAILEDIMAGEH;// 详情 (多图)
		}
		if(fail3){
			return CommonCode.Product.NRECOMMENDIMAGEH;// 推荐图
		}
//		if(fail4){
//			return CommonCode.Product.NFILEPRODUCT;// 车辆三证为空
//		}
	    return null;
   }

	@Override
	public Integer queryUserPosition(Long id) {
		TUsers tUsers = tUsersMapper.selectByPrimaryKey(id);
		if(tUsers!=null){
			return tUsers.getPosition();
		}
		return null;
	}

}

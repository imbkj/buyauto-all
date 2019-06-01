package com.business.service.products.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.business.service.products.api.IProductsApi;
import com.buyauto.dao.products.TProductsInputPojo;
import com.buyauto.dao.products.TProductsPojo;
import com.buyauto.entity.products.TProducts;
import com.buyauto.entity.products.TProductsCar;
import com.buyauto.entity.products.TProductsImage;
import com.buyauto.entity.sys.SysConfig;
import com.buyauto.entity.user.TUsers;
import com.buyauto.mapper.products.TProductsCarMapper;
import com.buyauto.mapper.products.TProductsImageMapper;
import com.buyauto.mapper.products.TProductsMapper;
import com.buyauto.mapper.products.TProductsTempMapper;
import com.buyauto.mapper.sys.SysConfigMapper;
import com.buyauto.mapper.user.TUsersMapper;
import com.buyauto.util.method.CommonCode;
import com.buyauto.util.method.KeyUtil;
import com.buyauto.util.method.NHDateUtils;
import com.buyauto.util.method.StringUtil;
import com.buyauto.util.pojo.PageVo;
import com.buyauto.util.pojo.UserSessionInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

@Service
public class ProductsImpl implements IProductsApi {

	private static final Logger logger = LoggerFactory.getLogger(ProductsImpl.class);

	@Autowired
	private TProductsMapper tProductsMapper;

	@Autowired
	private TProductsImageMapper tProductsImageMapper;

	@Autowired
	private SysConfigMapper sysConfigMapper;

	@Autowired
	private TProductsTempMapper tProductsTempMapper;

	@Autowired
	private TProductsCarMapper tProductsCarMapper;

	@Autowired
	private TUsersMapper tUsersMapper;

	/**
	 * 查询车辆信息列表
	 * 
	 * @param title
	 *            产品名称
	 * @param status
	 *            审核状态
	 * @param strStartDate
	 *            开始时间
	 * @param strEndDate
	 *            结束时间
	 * @param pageNumber
	 *            当前页码
	 * @param pageSize
	 *            一页条数
	 * @param productStatus
	 *            1：商品列表 0：草稿箱 3：垃圾箱
	 * @param productsId
	 *            产品ID
	 * @param recommend
	 *            0不推荐，1推荐
	 * @return
	 */
	@Override
	public PageVo<TProducts> queryProductsList(String title, Integer status, Date strStartDate, Date strEndDate, Integer pageNumber, Integer pageSize, String productStatus,
			String recommend, Long productsId, UserSessionInfo user) {
		Long id = null;
		if (user != null && user.getId() != null) {
			id = user.getId();
		}
		PageVo<TProducts> pageVo = new PageVo<TProducts>(0, pageNumber, pageSize);
		List<TProducts> roleList = tProductsMapper.queryProductsList(title, status, strStartDate, strEndDate, pageVo.getPageStartNumber(), pageVo.getPageEndNumber(),
				productStatus, recommend, productsId, id);
		int count = tProductsMapper.queryProductsCount(title, status, strStartDate, strEndDate, productStatus, recommend, productsId, id);
		pageVo.setRows(roleList);
		pageVo.setTotal(count);
		return pageVo;
	}


	/**
	 * 保存或修改产品车辆信息
	 * 
	 * @return
	 */
	@Override
	public TProductsInputPojo queryProductsList(
			TProductsInputPojo tProductsInputPojo, int saveState,
			Integer self_support) {

		Date date = new Date();
		Date factoryDates = date;
		TProductsImage tpImage = new TProductsImage();

		Boolean isNext = true;
		
		// id为null新增否则修改
		if (tProductsInputPojo.getId() != null) {
			/* 修改产品 */
			if (tProductsInputPojo.getFactoryDate() != null) {
				factoryDates = NHDateUtils.parseDateFormat(tProductsInputPojo.getFactoryDate());
			}
			TProducts tProducts = tProductsMapper
					.selectByPrimaryKey(tProductsInputPojo.getId());
			if (tProducts != null) {
				tProducts.setBrandId(tProductsInputPojo.getBrandId());
				tProducts.setTitle(tProductsInputPojo.getTitle());
				tProducts.setCarType(tProductsInputPojo.getCarType());
				tProducts.setPriceRange(tProductsInputPojo.getPriceRange());
				tProducts.setPosition(tProductsInputPojo.getPosition());
				tProducts.setBasicConfigure(tProductsInputPojo.getBasicConfigure());
				tProducts.setMustConfigure(tProductsInputPojo.getMustConfigure());
				/*if(!StringUtil.isEmpty(tProductsInputPojo.getSales())){
					tProducts.setSales(new BigDecimal(tProductsInputPojo.getSales()));
				}
				if(!StringUtil.isEmpty(tProductsInputPojo.getPersonal())){
					tProducts.setPersonal(new BigDecimal(tProductsInputPojo.getPersonal()));
				}
				if(!StringUtil.isEmpty(tProductsInputPojo.getDistributor())){
					tProducts.setDistributor(new BigDecimal(tProductsInputPojo.getDistributor()));
				}*/
				Gson gs = new Gson();
				List<Map<String, String>> listItems = Lists.newArrayList();

				ArrayList<?> obj = (ArrayList<?>) gs.fromJson(tProductsInputPojo.getOptionalConfigure(),Object.class);
				for (Object userItem : obj) {
					LinkedTreeMap<String, String> UserNext = (LinkedTreeMap<String, String>) userItem;
					Map<String, String> next = Maps.newHashMap();
					next.put("id", KeyUtil.generateDBKey() + "");
					next.put("name", UserNext.get("name"));
					next.put("number", UserNext.get("number"));
					listItems.add(next);
				}
				tProducts.setOptionalConfigure(gs.toJson(listItems));
				if(!StringUtil.isEmpty(tProductsInputPojo.getCarPrice())){
					tProducts.setCarPrice(new BigDecimal(tProductsInputPojo.getCarPrice()));
				}
				if(!StringUtil.isEmpty(tProductsInputPojo.getMustConfigurePrice())){
					tProducts.setMustConfigurePrice(new BigDecimal(tProductsInputPojo.getMustConfigurePrice()));
				}
				if (tProductsInputPojo.getStock().isEmpty()) {
					tProductsInputPojo.setStock("0");
				}
				tProducts.setStock(new Integer(tProductsInputPojo.getStock()));
				tProducts.setContent(tProductsInputPojo.getContent());
				tProducts.setStatus(saveState);
//				tProducts.setCreateDate(date);
				tProducts.setUpdateDate(date);
				if (tProductsInputPojo.getChinaPrice().isEmpty()) {
					tProductsInputPojo.setChinaPrice("0");
				}
				if(!StringUtil.isEmpty(tProductsInputPojo.getChinaPrice())){
					tProducts.setChinaPrice(new BigDecimal(tProductsInputPojo.getChinaPrice()));
				}
				tProducts.setCountry(tProductsInputPojo.getCountry());
				tProducts.setDeposit(tProductsInputPojo.getDeposit());
				tProducts.setCarModel(tProductsInputPojo.getCarModel());
				tProducts.setOutColor(tProductsInputPojo.getOutColor());
				tProducts.setInColor(tProductsInputPojo.getInColor());
				tProducts.setEngine(tProductsInputPojo.getEngine());
				tProducts.setGearbox(tProductsInputPojo.getGearbox());
				if (tProductsInputPojo.getFactoryDate() != null) {
					tProducts.setFactoryDate(factoryDates);
				}
				isNext = tProductsMapper.updateByPrimaryKeySelective(tProducts) == CommonCode.DBSuccess.Success;

				if (isNext && !StringUtil.isEmpty(tProductsInputPojo.getShrinkImageH())) {
					// 缩略
					tpImage = tProductsImageMapper.selectByImgEdit(tProducts.getProductsId(),CommonCode.ProductsImageType.THUMBNAIL);
					// 假设没有 则创建一个 应为导入数据
					if (tpImage == null) {
						tpImage = new TProductsImage();
						tpImage.setId(KeyUtil.generateDBKey());
						tpImage.setProductId(tProducts.getProductsId());
						tpImage.setFilePath(tProductsInputPojo.getIndexImageH());
						tpImage.setType(CommonCode.ProductsImageType.THUMBNAIL);
						tpImage.setCreateDate(date);
						tpImage.setStatus(CommonCode.ConfigState.ENABLE);
						tpImage.setVersion(CommonCode.Version.Default);
						isNext = tProductsImageMapper.insertSelective(tpImage) == CommonCode.DBSuccess.Success && isNext;
					} else {
						tpImage.setFilePath(tProductsInputPojo.getShrinkImageH());
						isNext = tProductsImageMapper.updateByPrimaryKeySelective(tpImage) == CommonCode.DBSuccess.Success && isNext;
					}
				}

				if (isNext&&!StringUtil.isEmpty(tProductsInputPojo.getIndexImageH())) {
					// 封面
					tpImage = tProductsImageMapper.selectByImgEdit(tProducts.getProductsId(),CommonCode.ProductsImageType.COVER);
					if (tpImage == null) {
						tpImage = new TProductsImage();
						tpImage = new TProductsImage();
						tpImage.setId(KeyUtil.generateDBKey());
						tpImage.setProductId(tProducts.getProductsId());
						tpImage.setFilePath(tProductsInputPojo.getIndexImageH());
						tpImage.setType(CommonCode.ProductsImageType.COVER);
						tpImage.setCreateDate(date);
						tpImage.setStatus(CommonCode.ConfigState.ENABLE);
						tpImage.setVersion(CommonCode.Version.Default);
						isNext = tProductsImageMapper.insertSelective(tpImage) == CommonCode.DBSuccess.Success && isNext;
					} else {
						tpImage.setFilePath(tProductsInputPojo.getIndexImageH());
						isNext = tProductsImageMapper.updateByPrimaryKeySelective(tpImage) == CommonCode.DBSuccess.Success && isNext;
					}
				}
				if (isNext&&!StringUtil.isEmpty(tProductsInputPojo.getRecommendImageH())) {
					// 推荐
					tpImage = tProductsImageMapper.selectByImgEdit(tProducts.getProductsId(),CommonCode.ProductsImageType.RECOMMEND);
					if (tpImage == null) {
						tpImage = new TProductsImage();
						tpImage.setId(KeyUtil.generateDBKey());
						tpImage.setProductId(tProducts.getProductsId());
						tpImage.setFilePath(tProductsInputPojo.getRecommendImageH());
						tpImage.setType(CommonCode.ProductsImageType.RECOMMEND);
						tpImage.setCreateDate(date);
						tpImage.setStatus(CommonCode.ConfigState.ENABLE);
						tpImage.setVersion(CommonCode.Version.Default);
						isNext = tProductsImageMapper.insertSelective(tpImage) == CommonCode.DBSuccess.Success && isNext;
					} else {
						tpImage.setFilePath(tProductsInputPojo.getRecommendImageH());
						isNext = tProductsImageMapper.updateByPrimaryKeySelective(tpImage) == CommonCode.DBSuccess.Success&& isNext;
					}
				}
				if (isNext) {
					// 详情 多图
					List<TProductsImage> tpImages = tProductsImageMapper.selectByImgEditFigure(tProducts.getProductsId(),CommonCode.ProductsImageType.DETAILS);
					if (tpImages.size() < 1) {
						JSONArray jsonArray;
						JSONObject jsonObject;
						try {
							if (!StringUtil.isEmpty(tProductsInputPojo.getDetailedImageH())) {
								jsonArray = new JSONArray(tProductsInputPojo.getDetailedImageH());
								for (int i = 0; i < jsonArray.length(); i++) {
									jsonObject = jsonArray.getJSONObject(i);
									String key = jsonObject.getString("preId");
									String value = jsonObject.getString("name");

									tpImage = new TProductsImage();
									tpImage.setId(KeyUtil.generateDBKey());
									tpImage.setProductId(tProducts.getProductsId());
									tpImage.setFileName(key);
									tpImage.setFilePath(value);
									tpImage.setType(CommonCode.ProductsImageType.DETAILS);
									tpImage.setCreateDate(date);
									tpImage.setStatus(CommonCode.ConfigState.ENABLE);
									tpImage.setVersion(CommonCode.Version.Default);
									isNext = tProductsImageMapper.insertSelective(tpImage) == CommonCode.DBSuccess.Success && isNext;
								}
							}
						} catch (JSONException e) {
							logger.error(e + "(详情)多图转json失败");
							e.printStackTrace();
						}
					} else {
						JSONArray jsonArray;
						JSONObject jsonObject;
						try {
							if(!StringUtil.isEmpty(tProductsInputPojo.getDetailedImageOld())){
								jsonArray = new JSONArray(tProductsInputPojo.getDetailedImageOld());
								if (tpImages.size() != jsonArray.length()) {
									// 旧图有改动 找到已经不存在的图 删除
									for (int j = 0; j < tpImages.size(); j++) {
										for (int i = 0; i < jsonArray.length(); i++) {
											jsonObject = jsonArray.getJSONObject(i);
											System.out.println(jsonObject.getString("name"));
											System.out.println(tpImages.get(j).getFilePath());
											if (tpImages.get(j).getFilePath().equals(jsonObject.getString("name"))) {
												tpImages.remove(j);
											}
										}
									}
									for (TProductsImage tProductsImage : tpImages) {
										tProductsImageMapper.deleteByPrimaryKey(tProductsImage.getId());
									}
								}
							}
							if(!StringUtil.isEmpty(tProductsInputPojo.getDetailedImageH())){
								jsonArray = new JSONArray(tProductsInputPojo.getDetailedImageH());
								for (int i = 0; i < jsonArray.length(); i++) {
									jsonObject = jsonArray.getJSONObject(i);
									String key = jsonObject.getString("preId");
									String value = jsonObject.getString("name");

									tpImage = new TProductsImage();
									tpImage.setId(KeyUtil.generateDBKey());
									tpImage.setProductId(tProducts.getProductsId());
									tpImage.setFileName(key);
									tpImage.setFilePath(value);
									tpImage.setType(CommonCode.ProductsImageType.DETAILS);
									tpImage.setCreateDate(date);
									tpImage.setStatus(CommonCode.ConfigState.ENABLE);
									tpImage.setVersion(CommonCode.Version.Default);
									isNext = tProductsImageMapper.insertSelective(tpImage) == CommonCode.DBSuccess.Success && isNext;
								}
							}
						} catch (JSONException e) {
							logger.error(e + "多图转json失败");
							e.printStackTrace();
						}
					}

					// 车辆三证 多图
					if (isNext) {
						//查数据库有没有老图
						List<TProductsImage> fileImages = tProductsImageMapper.selectByImgEditFigure(tProducts.getProductsId(),CommonCode.ProductsImageType.PRODUCTFILE);
						//如果没有老图，不用做对比，直接保存数据
						if (fileImages==null ||fileImages.size() < 1) {
							JSONArray jsonArray;
							JSONObject jsonObject;
							try {
								if (!StringUtil.isEmpty(tProductsInputPojo.getProductFileImg())) {
									jsonArray = new JSONArray(tProductsInputPojo.getProductFileImg());
									for (int i = 0; i < jsonArray.length(); i++) {
										jsonObject = jsonArray.getJSONObject(i);
										String key = jsonObject.getString("preId");
										String value = jsonObject.getString("name");

										tpImage = new TProductsImage();
										tpImage.setId(KeyUtil.generateDBKey());
										tpImage.setProductId(tProducts.getProductsId());
										tpImage.setFileName(key);
										tpImage.setFilePath(value);
										tpImage.setType(CommonCode.ProductsImageType.PRODUCTFILE);
										tpImage.setCreateDate(date);
										tpImage.setStatus(CommonCode.ConfigState.ENABLE);
										tpImage.setVersion(CommonCode.Version.Default);
										isNext = tProductsImageMapper.insertSelective(tpImage) == CommonCode.DBSuccess.Success && isNext;
									}
								}
							} catch (JSONException e) {
								logger.error(e + "(详情)多图转json失败");
								e.printStackTrace();
							}
						} else {
							JSONArray jsonArray;
							JSONObject jsonObject;
							try {
								if(!StringUtil.isEmpty(tProductsInputPojo.getProductFileImgOld())){
									jsonArray = new JSONArray(tProductsInputPojo.getProductFileImgOld());
									//老图和上传的数量不一致,说明图片改动过,需要移除掉被删除的图片
									if (fileImages.size() != jsonArray.length()) {
										// 旧图有改动 找到已经不存在的图 删除
										for (int j = 0; j < fileImages.size(); j++) {
											for (int i = 0; i < jsonArray.length(); i++) {
												jsonObject = jsonArray.getJSONObject(i);
												System.out.println(jsonObject.getString("name")+" == "+fileImages.get(j).getFilePath());
												if (fileImages.get(j).getFilePath().equals(jsonObject.getString("name"))) {
													fileImages.remove(j);
												}
											}
										}
										for (TProductsImage tProductsImage : fileImages) {
											tProductsImageMapper.deleteByPrimaryKey(tProductsImage.getId());
										}
									}
								}
								if(!StringUtil.isEmpty(tProductsInputPojo.getProductFileImg())){
									jsonArray = new JSONArray(tProductsInputPojo.getProductFileImg());
									for (int i = 0; i < jsonArray.length(); i++) {
										jsonObject = jsonArray.getJSONObject(i);
										String key = jsonObject.getString("preId");
										String value = jsonObject.getString("name");
										
										tpImage = new TProductsImage();
										tpImage.setId(KeyUtil.generateDBKey());
										tpImage.setProductId(tProducts.getProductsId());
										tpImage.setFileName(key);
										tpImage.setFilePath(value);
										tpImage.setType(CommonCode.ProductsImageType.PRODUCTFILE);
										tpImage.setCreateDate(date);
										tpImage.setStatus(CommonCode.ConfigState.ENABLE);
										tpImage.setVersion(CommonCode.Version.Default);
										isNext = tProductsImageMapper.insertSelective(tpImage) == CommonCode.DBSuccess.Success && isNext;
									}
								}
							} catch (JSONException e) {
								logger.error(e + "多图转json失败");
								e.printStackTrace();
							}

						}
					}
				}
				return isNext == true ? tProductsInputPojo : null;
			} else {
				logger.info("该ID有误");
				return null;
			}
		} else {
			/* 新增产品 */
			if (tProductsInputPojo.getFactoryDate() != null) {
				factoryDates = NHDateUtils.parseDateFormat(tProductsInputPojo.getFactoryDate());
			}
			TProducts products = new TProducts();
			Long id = KeyUtil.generateDBKey();
			tProductsInputPojo.setId(id);
			products.setProductsId(id);
			products.setOperId(tProductsInputPojo.getOperId());//添加这个产品的用户ID
			products.setBrandId(tProductsInputPojo.getBrandId());
			products.setTitle(tProductsInputPojo.getTitle());
			products.setCarType(tProductsInputPojo.getCarType());
			products.setPriceRange(tProductsInputPojo.getPriceRange());
			products.setPosition(tProductsInputPojo.getPosition());
			products.setBasicConfigure(tProductsInputPojo.getBasicConfigure());
			products.setMustConfigure(tProductsInputPojo.getMustConfigure());
			if(!StringUtil.isEmpty(tProductsInputPojo.getChinaPrice())){
				products.setChinaPrice(new BigDecimal(tProductsInputPojo.getChinaPrice()));//国内指导价
			}
			products.setSelfSupport(self_support);// 是否为自营
		/*	if(!StringUtil.isEmpty(tProductsInputPojo.getSales())){
				products.setSales(new BigDecimal(tProductsInputPojo.getSales()));//个人销售加价 
			}
			if(!StringUtil.isEmpty(tProductsInputPojo.getPersonal())){
				products.setPersonal(new BigDecimal(tProductsInputPojo.getPersonal()));//个人客户加价
			}
			if(!StringUtil.isEmpty(tProductsInputPojo.getDistributor())){
				products.setDistributor(new BigDecimal(tProductsInputPojo.getDistributor()));//经销商加价
			}*/
			/*-------*/
			// 追加ID
			Gson gs = new Gson();
			List<Map<String, String>> listItems = Lists.newArrayList();
			// 可选配置json转换
			ArrayList<?> obj = (ArrayList<?>) gs.fromJson(tProductsInputPojo.getOptionalConfigure(), Object.class);
			for (Object userItem : obj) {
				LinkedTreeMap<String, String> UserNext = (LinkedTreeMap<String, String>) userItem;
				Map<String, String> next = Maps.newHashMap();
				next.put("id", KeyUtil.generateDBKey() + "");
				next.put("name", UserNext.get("name"));
				next.put("number", UserNext.get("number"));
				listItems.add(next);
			}
			products.setOptionalConfigure(gs.toJson(listItems));
			/*-------*/
			if(!StringUtil.isEmpty(tProductsInputPojo.getCarPrice())){
				products.setCarPrice(new BigDecimal(tProductsInputPojo.getCarPrice()));
			}
			if(!StringUtil.isEmpty(tProductsInputPojo.getMustConfigurePrice())){
				products.setMustConfigurePrice(new BigDecimal(tProductsInputPojo.getMustConfigurePrice()));
			}
			if(!StringUtil.isEmpty(tProductsInputPojo.getStock())){
				products.setStock(new Integer(tProductsInputPojo.getStock()));
			}
			products.setRecommend(CommonCode.RecommendStatus.NOT_RECOMMENDED);
			products.setContent(tProductsInputPojo.getContent());
			products.setStatus(saveState);
			products.setCreateDate(date);
			products.setUpdateDate(date);
			products.setCountry(tProductsInputPojo.getCountry());
			products.setDeposit(tProductsInputPojo.getDeposit());
			products.setCarModel(tProductsInputPojo.getCarModel());
			products.setOutColor(tProductsInputPojo.getOutColor());
			products.setInColor(tProductsInputPojo.getInColor());
			products.setEngine(tProductsInputPojo.getEngine());
			products.setGearbox(tProductsInputPojo.getGearbox());
			// 出厂日期
			if (tProductsInputPojo.getFactoryDate() != null) {
				products.setFactoryDate(factoryDates);
			}
			products.setVersion(CommonCode.Version.Default);
			isNext = tProductsMapper.insertSelective(products) == CommonCode.DBSuccess.Success;

			if (isNext && !StringUtil.isEmpty(tProductsInputPojo.getShrinkImageH())) {
				/* 保存缩略图 */
				tpImage = new TProductsImage();
				tpImage.setId(KeyUtil.generateDBKey());
				tpImage.setProductId(products.getProductsId());// 关联产品ID
				tpImage.setFilePath(tProductsInputPojo.getShrinkImageH());// 图片路径
				tpImage.setType(CommonCode.ProductsImageType.THUMBNAIL);// 类型
				tpImage.setCreateDate(date);
				tpImage.setStatus(CommonCode.ConfigState.ENABLE);// 状态
				tpImage.setVersion(CommonCode.Version.Default);
				isNext = tProductsImageMapper.insertSelective(tpImage) == CommonCode.DBSuccess.Success && isNext;
			}
			if (isNext && !StringUtil.isEmpty(tProductsInputPojo.getIndexImageH())) {
				/* 保存封面图 */
				tpImage = new TProductsImage();
				tpImage.setId(KeyUtil.generateDBKey());
				tpImage.setProductId(products.getProductsId());
				tpImage.setFilePath(tProductsInputPojo.getIndexImageH());
				tpImage.setType(CommonCode.ProductsImageType.COVER);
				tpImage.setCreateDate(date);
				tpImage.setStatus(CommonCode.ConfigState.ENABLE);
				tpImage.setVersion(CommonCode.Version.Default);
				isNext = tProductsImageMapper.insertSelective(tpImage) == CommonCode.DBSuccess.Success && isNext;
			}
			if (isNext && !StringUtil.isEmpty(tProductsInputPojo.getRecommendImageH())) {
				/* 保存推荐图 */
				tpImage = new TProductsImage();
				tpImage.setId(KeyUtil.generateDBKey());
				tpImage.setProductId(products.getProductsId());
				tpImage.setFilePath(tProductsInputPojo.getRecommendImageH());
				tpImage.setType(CommonCode.ProductsImageType.RECOMMEND);
				tpImage.setCreateDate(date);
				tpImage.setStatus(CommonCode.ConfigState.ENABLE);
				tpImage.setVersion(CommonCode.Version.Default);
				isNext = tProductsImageMapper.insertSelective(tpImage) == CommonCode.DBSuccess.Success && isNext;
			}
			if (isNext && !StringUtil.isEmpty(tProductsInputPojo.getDetailedImageH())) {
				/* 保存详情图(多图) */
				try {
					JSONArray array = new JSONArray(tProductsInputPojo.getDetailedImageH());
					for (int i = 0; i < array.length(); i++) {
						JSONObject jsonObject = array.getJSONObject(i);
						String key = jsonObject.getString("preId");
						String value = jsonObject.getString("name");

						tpImage = new TProductsImage();
						tpImage.setId(KeyUtil.generateDBKey());
						tpImage.setProductId(products.getProductsId());
						tpImage.setFileName(key);
						tpImage.setFilePath(value);
						tpImage.setType(CommonCode.ProductsImageType.DETAILS);
						tpImage.setCreateDate(date);
						tpImage.setStatus(CommonCode.ConfigState.ENABLE);
						tpImage.setVersion(CommonCode.Version.Default);
						isNext = tProductsImageMapper.insertSelective(tpImage) == CommonCode.DBSuccess.Success&& isNext;
					}
				} catch (JSONException e) {
					logger.error(e + "详情图,多图转json失败");
					e.printStackTrace();
				}
			}
			if (isNext && !StringUtil.isEmpty(tProductsInputPojo.getProductFileImg())) {
				/* 车辆三证 */
				try {
					JSONArray array = new JSONArray(tProductsInputPojo.getProductFileImg());
					for (int i = 0; i < array.length(); i++) {
						JSONObject jsonObject = array.getJSONObject(i);
						String key = jsonObject.getString("preId");
						String value = jsonObject.getString("name");
						tpImage = new TProductsImage();
						tpImage.setId(KeyUtil.generateDBKey());
						tpImage.setProductId(products.getProductsId());
						tpImage.setFileName(key);
						tpImage.setFilePath(value);
						tpImage.setType(CommonCode.ProductsImageType.PRODUCTFILE);
						tpImage.setCreateDate(date);
						tpImage.setStatus(CommonCode.ConfigState.ENABLE);
						tpImage.setVersion(CommonCode.Version.Default);
						isNext = tProductsImageMapper.insertSelective(tpImage) == CommonCode.DBSuccess.Success && isNext;
					}
				} catch (JSONException e) {
					logger.error(e + "(车辆三证)多图转json失败");
					e.printStackTrace();
				}
			}
		}
		return isNext == true ? tProductsInputPojo : null;
	}

	/**
	 * 根据ID查询产品信息
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public TProductsPojo showUpdateProducts(Long id) {
		TProductsPojo tProductsPojo = tProductsMapper.showUpdateProducts(id);
		if(tProductsPojo!=null){
			List<TProductsImage> thumbnamage = tProductsImageMapper.getTProductsPojoImage(tProductsPojo.getProductsId());
			tProductsPojo.settProductsImage(thumbnamage);
		}
		return tProductsPojo;
	}

	// 产品上架
	@Override
	public Boolean updataEnable(Long id) {
		TProducts tProducts = tProductsMapper.selectByPrimaryKey(id);
		if(tProducts==null)return false;
		tProducts.setStatus(CommonCode.SaveState.SHELVES);
		tProducts.setUpdateDate(new Date());
		return tProductsMapper.updateByPrimaryKeySelective(tProducts) == CommonCode.DBSuccess.Success;
	}

	// 产品下架
	@Override
	public Boolean updataDisable(Long id) {
		TProducts tProducts = tProductsMapper.selectByPrimaryKey(id);
		if(tProducts==null)return false;
		tProducts.setStatus(CommonCode.SaveState.ADOPT);
		tProducts.setUpdateDate(new Date());
		tProducts.setRecommend(CommonCode.RecommendStatus.NOT_RECOMMENDED);
		return tProductsMapper.updateByPrimaryKeySelective(tProducts) == CommonCode.DBSuccess.Success;
	}

	// 产品删除
	@Override
	public Boolean updataDelete(Long id) {
		TProducts tProducts = tProductsMapper.selectByPrimaryKey(id);
		tProducts.setStatus(CommonCode.SaveState.RECYCLE);
		tProducts.setUpdateDate(new Date());
		return tProductsMapper.updateByPrimaryKeySelective(tProducts) == CommonCode.DBSuccess.Success;
	}

	@Override
	public Boolean updataRestore(Long id,Integer state) {
		TProducts tProducts = tProductsMapper.selectByPrimaryKey(id);
		tProducts.setStatus(state);
		tProducts.setUpdateDate(new Date());
		return tProductsMapper.updateByPrimaryKeySelective(tProducts) == CommonCode.DBSuccess.Success;
	}

	@Override
	public Boolean updataRecommend(Long id) {
		TProducts tProducts = tProductsMapper.selectByPrimaryKey(id);
		tProducts.setRecommend(CommonCode.RecommendStatus.RECOMMENDED);
		tProducts.setUpdateDate(new Date());
		return tProductsMapper.updateByPrimaryKeySelective(tProducts) == CommonCode.DBSuccess.Success;
	}

	@Override
	public Boolean updataCancelRecommend(Long id) {
		TProducts tProducts = tProductsMapper.selectByPrimaryKey(id);
		tProducts.setRecommend(CommonCode.RecommendStatus.NOT_RECOMMENDED);
		tProducts.setUpdateDate(new Date());
		return tProductsMapper.updateByPrimaryKeySelective(tProducts) == CommonCode.DBSuccess.Success;
	}

	/**
	 * 展示首页热门推荐
	 */
	@Override
	public List<TProductsPojo> queryIndexRecommend(UserSessionInfo user) {
		BigDecimal actualPrice = new BigDecimal("0");
		List<TProductsPojo> tProductList = tProductsMapper.queryIndexRecommend();

		for (TProductsPojo tProductsPojo : tProductList) {
			tProductsPojo.setCarNoCount(tProductsPojo.getStock());
			actualPrice = actualCarCarPrice(user, tProductsPojo);
			tProductsPojo.setCarPrice(actualPrice);
		}
		return tProductList;
	}

	/**
	 * 展示首页的十条商品信息(精选好车)
	 */
	@Override
	public List<TProductsPojo> queryIndexCommodityList(UserSessionInfo user) {
		BigDecimal actualPrice = new BigDecimal("0");
		List<TProductsPojo> tProductsPojo = tProductsMapper.queryIndexTProductsPojoList();
		for (TProductsPojo tProductsPojo2 : tProductsPojo) {
			actualPrice = actualCarCarPrice(user, tProductsPojo2);
			tProductsPojo2.setCarPrice(actualPrice);
			TProductsImage tProductsImage = tProductsImageMapper.queryIndexTProductsPojoImageList(tProductsPojo2.getProductsId());
			if (tProductsImage != null) {
				tProductsPojo2.setFilePath(tProductsImage.getFilePath());
			}

			//tProductsPojo2.setCarNoCount(tProductsCarMapper.queryCarNoCount(tProductsPojo2.getProductsId()));
			//原车架号确认库存改为现库存
			tProductsPojo2.setCarNoCount(tProductsPojo2.getStock());
		//	tProductsPojo2.setCarNoCount(tProductsCarMapper.queryCarNoCount(tProductsPojo2.getProductsId()));
		}
		return tProductsPojo;
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
	public PageVo<TProductsPojo> querygarageProducts(Long brandId, Integer carType, Integer priceRange, Integer sortOrder, String title, Integer pageNumber, Integer pageSize,
			UserSessionInfo user) {
		PageVo<TProductsPojo> pageVo = new PageVo<TProductsPojo>(0, pageNumber, pageSize);
		List<TProductsPojo> roleList = tProductsMapper.querygarageProducts(brandId, carType, priceRange, sortOrder, title, pageVo.getPageStartNumber(), pageVo.getPageEndNumber());
		BigDecimal actualPrice = new BigDecimal("0");
		for (TProductsPojo tProductsPojo : roleList) {
			//tProductsPojo.setCarNoCount(tProductsCarMapper.queryCarNoCount(tProductsPojo.getProductsId()));
			tProductsPojo.setCarNoCount(tProductsPojo.getStock());
			actualPrice = actualCarCarPrice(user, tProductsPojo);
			tProductsPojo.setCarPrice(actualPrice);
		}
		int count = tProductsMapper.querygarageProductsCount(brandId, carType, priceRange, title);
		pageVo.setRows(roleList);
		pageVo.setTotal(count);
		return pageVo;
	}

	@Override
	public TProductsPojo queryProductsDetailed(Long productsId, UserSessionInfo user) {
		BigDecimal actualPrice = new BigDecimal("0");
		TProductsPojo tProductsPojo = tProductsMapper.queryProductsDetailed(productsId);
		if (tProductsPojo == null) {
			return null;
		} else {
			actualPrice = actualCarCarPrice(user, tProductsPojo);
			tProductsPojo.setCarPrice(actualPrice);
		}

		// 缩略图片
		TProductsImage tProductsImage = tProductsImageMapper.selectByImgEdit(productsId, CommonCode.ProductsImageType.THUMBNAIL);
		if (tProductsImage != null) {
			tProductsPojo.setImgfilePath(tProductsImage.getFilePath());

		}
		// 提车地址
		List<SysConfig> list = sysConfigMapper.querySysConfigtractList(CommonCode.ConfigGroup.EXTRACT_ADDRESS);
		tProductsPojo.setExtract(list);
		return tProductsPojo;
	}

	@Override
	public TProductsPojo queryExhibitionTProducts(Long productsId, UserSessionInfo user) {
		BigDecimal actualPrice = new BigDecimal("0");
		TProductsPojo tProductsPojo = tProductsMapper.queryExhibitionTProducts(productsId);
		if (tProductsPojo != null) {
			List<TProductsImage> tProductsImageList = tProductsImageMapper.selectByImgEditFigure(productsId, CommonCode.ProductsImageType.DETAILS);
			tProductsPojo.settProductsImage(tProductsImageList);
			actualPrice = actualCarCarPrice(user, tProductsPojo);//计算不同用户登录所看到的价格
			tProductsPojo.setCarPrice(actualPrice);// 麦卡价格
		}
		return tProductsPojo;
	}

	@Override
	public String queryProductsImg(Long productId, int cover) {
		TProductsImage selectByImgEdit = tProductsImageMapper.selectByImgEdit(productId, cover);
		return selectByImgEdit.getFilePath();
	}

	@Override
	public List<TProductsCar> queryCarNo(Long id) {
		List<TProductsCar> tProductsCar = tProductsCarMapper.queryCarNoByProductsId(id);
		return tProductsCar;
	}

	@Override
	public int queryCarNoCount(Long productsId) {
		return tProductsCarMapper.queryCarNoCount(productsId);
	}

	// 计算裸车价格
	@Override
	public BigDecimal actualCarCarPrice(UserSessionInfo user, TProductsPojo tProductsPojo) {
		BigDecimal actualPrice = new BigDecimal("0");
		if (user == null) {
			// 此时用户未登录 看到的价格为个人用户所见价格
			BigDecimal a = tProductsPojo.getPersonal();
			actualPrice = tProductsPojo.getCarPrice().add(tProductsPojo.getPersonal() == null ? new BigDecimal("0") : tProductsPojo.getPersonal());
		} else {
			TUsers tUsers = tUsersMapper.selectByPrimaryKey(user.getId());
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

	// 产品审核
	@Override
	public Integer toExamine(Long id, Integer result) {
		if (result == null || id == null) {
			return CommonCode.UserResult.ERROR_PAMMER;
		}
		TProducts tProducts = tProductsMapper.selectByPrimaryKey(id);
		if (tProducts != null) {
			if (result == CommonCode.ProductExamine.PASS) {
				tProducts.setStatus(CommonCode.SaveState.ADOPT);
			} else if (result == CommonCode.ProductExamine.NOTPASS) {
				tProducts.setStatus(CommonCode.SaveState.NOT_PASS);
			} else {
				return CommonCode.UserResult.ERROR;
			}
			tProducts.setUpdateDate(new Date());
			if (tProductsMapper.updateByPrimaryKeySelective(tProducts) == CommonCode.DBSuccess.Success) {
				return CommonCode.UserResult.SUCCESS;
			}
		}
		return CommonCode.UserResult.ERROR;
	}

	/**
	 * 提交审核
	 * @param id
	 * @return
	 */
	@Override
	public Integer subAudit(Long id) {
		TProducts tProducts = new TProducts();
		tProducts.setProductsId(id);
		tProducts.setUpdateDate(new Date());
		tProducts.setStatus(CommonCode.SaveState.NEWLY);//该产品状态修改成审核中
		return tProductsMapper.updateByPrimaryKeySelective(tProducts);
	}


	@Override
	public TProducts getProductById(Long productId) {
		return tProductsMapper.selectByPrimaryKey(productId);
	}


	@Override
	public Integer toExamineFare(Long id, Integer result, String personal, String sales, String distributor) {
		if (result == null || id == null) {
			return CommonCode.UserResult.ERROR_PAMMER;
		}
		TProducts tProducts = tProductsMapper.selectByPrimaryKey(id);
		if (tProducts != null) {
			if (result == CommonCode.ProductExamine.PASS) {
				tProducts.setStatus(CommonCode.SaveState.ADOPT);
				//加价操作
				if(!StringUtil.isEmpty(personal)){
					tProducts.setPersonal(new BigDecimal(personal));
				}else{
					tProducts.setPersonal(new BigDecimal("0"));
				}
				if(!StringUtil.isEmpty(sales)){
					tProducts.setSales(new BigDecimal(sales));
				}else{
					tProducts.setSales(new BigDecimal("0"));
				}
				if(!StringUtil.isEmpty(distributor)){
					tProducts.setDistributor(new BigDecimal(distributor));
				}else{
					tProducts.setDistributor(new BigDecimal("0"));
				}
			} else if (result == CommonCode.ProductExamine.NOTPASS) {
				tProducts.setStatus(CommonCode.SaveState.NOT_PASS);
			} else {
				return CommonCode.UserResult.ERROR;
			}
			tProducts.setUpdateDate(new Date());
			if (tProductsMapper.updateByPrimaryKeySelective(tProducts) == CommonCode.DBSuccess.Success) {
				return CommonCode.UserResult.SUCCESS;
			}
		}
		return CommonCode.UserResult.ERROR;
	}

}

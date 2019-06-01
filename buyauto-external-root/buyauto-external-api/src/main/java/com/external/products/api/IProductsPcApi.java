package com.external.products.api;

import java.util.List;
import java.util.Map;

import com.buyauto.dao.cms.CmsBannerPojo;
import com.buyauto.dao.cms.CmsNewsPojo;
import com.buyauto.dao.products.TProductsInputPojo;
import com.buyauto.dao.products.TProductsPojo;
import com.buyauto.entity.products.TProducts;
import com.buyauto.entity.sys.SysConfig;
import com.buyauto.util.pojo.PageVo;
import com.buyauto.util.pojo.UserSessionInfo;

public interface IProductsPcApi {
	
	/**
	 * 查询首页推荐的产品
	 * @return
	 */
	List<TProductsPojo> queryIndexRecommend(UserSessionInfo user);

	/**
	 * 查询展示的品牌信息
	 * @return
	 */
	List<SysConfig> queryBrandList();

	/**
	 * 查询首页展示的十条商品信息
	 * @return
	 */
	List<TProductsPojo> queryIndexCommodityList(UserSessionInfo user);

	/**
	 * 查询首页banner
	 * @return
	 */
	List<CmsBannerPojo> queryIndexBannerList();

	/**
	 * 查询首页展示的新闻
	 * @return
	 */
	List<CmsNewsPojo> queryCmsNewsList();

	/**
	 * 查询首页展示公告的信息
	 * @return
	 */
	List<CmsNewsPojo> queryCmsNoticeList();

	/**
	 * 查询车库中展示的车辆信息
	 * @param brandId 品牌
	 * @param carType 车身
	 * @param priceRange 价格区间
	 * @param sortOrder 排序方式
	 * @param title 输入
	 * @param pageNumber
	 * @param pageSize
	 * @param user 
	 * @return
	 */
	PageVo<TProductsPojo> querygarageProducts(Long brandId, Integer carType,
			Integer priceRange, Integer sortOrder, String title, Integer pageNumber, Integer pageSize, UserSessionInfo user);

	/**
	 * 配置车辆详情
	 * @param productsId
	 * @param user 
	 * @return
	 */
	TProductsPojo queryProductsDetailed(Long productsId, UserSessionInfo user);

	/**
	 * 查询车辆详情
	 * @param productsId
	 * @param user 
	 * @return
	 */
	TProductsPojo queryExhibitionTProducts(Long productsId, UserSessionInfo user);

	/**
	 * 确认用户的可选配置
	 * @param optionalConfigure
	 * @param optionalConfigure2
	 * @return
	 */
	String validateConfigure(String optionalConfigure, String optionalConfigure2);

	/**
	 * 查询提车地址
	 * @param sysConfigId
	 * @return
	 */
	SysConfig querySysConfigAddress(Long sysConfigId);

	/**
	 * 查询提车地址
	 * @return
	 */
	List<SysConfig> querySysConfigAddress();

	/**
	 * 查询对应的商品展示图片
	 * @param productId
	 * @param cover
	 * @return
	 */
	String queryProductsImg(Long productId, int cover);

	/**
	 * 查询可用车架号数量
	 * @param productsId
	 * @return
	 */
	int queryCarNoCount(Long productsId);

	/**
	 * 商品列表
	 * @param productStatus 
	 * @param status 审核状态
	 * @param title  产新名称
	 * @param productsId 产品ID
	 * @param strStartDate 开始时间
	 * @param strEndDate   结束时间
	 * @param pageNumber 当前页码
	 * @param pageSize   一页条数
	 * @param user 
	 * @return
	 */
	PageVo<TProducts> queryProductsList(String productStatus, String title, Integer status, Long productsId, String strStartDate, String strEndDate, Integer pageNumber, Integer pageSize, UserSessionInfo user);

	/**
	 * 保存或修改产品车辆信息
	 * @return
	 */
	Map<String,Object> saveProductsList(TProductsInputPojo tProductsInputPojo,UserSessionInfo user);

	/**
	 * 根据ID查询产品信息
	 * @param id
	 * @return
	 */
	TProductsPojo getProductsById(Long id);

	/**
	 * 产品删除
	 * @param id
	 * @return
	 */
	Boolean updataDelete(Long id);

	/**
	 * 产品下架
	 * @param id
	 * @return
	 */
	Boolean updataDisable(Long id);

	/**
	 * 产品还原
	 * @param id
	 * @return
	 */
	Boolean updataRestore(Long id);

	/**
	 * 查看未通过原因
	 * @param id
	 * @return
	 */
	String getReasonById(Long id);

	/**
	 * 查询保险信息
	 * @return
	 */
	Map<String, Object> queryInsuranceConfig();

	/**
	 * 查询保险方式
	 * @param insuranceType
	 * @return
	 */
	SysConfig queryInsuranceType(Integer insuranceType);

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
	Integer addSupplierFinance(Long uId, String amount, Integer type, String port, Integer repayment, Integer term);

	/**
	 * 更新产品上架
	 * @return
	 */
	Boolean updataEnable(Long id);

	/**
	 * 提交审核
	 * @param id
	 * @return
	 */
	String subAudit(Long id);

	/**
	 * 可选配置json字符串转换成对象，提供app解析
	 * @param opt
	 * @return
	 */
	List<Map<String, Object>> convertOpt(String validateConfigure);

	/**
	 * 查询当前用户登录的角色
	 * @param id
	 * @return
	 */
	Integer queryUserPosition(Long id);


	

}

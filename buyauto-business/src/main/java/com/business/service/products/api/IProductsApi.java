package com.business.service.products.api;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.buyauto.dao.products.TProductsInputPojo;
import com.buyauto.dao.products.TProductsPojo;
import com.buyauto.entity.products.TProducts;
import com.buyauto.entity.products.TProductsCar;
import com.buyauto.util.pojo.PageVo;
import com.buyauto.util.pojo.UserSessionInfo;

public interface IProductsApi {

	/**
	 * 查询车辆信息列表
	 * @param title 产品名称
	 * @param status 审核状态
	 * @param strStartDate 开始时间
	 * @param strEndDate   结束时间
	 * @param pageNumber 当前页码
	 * @param pageSize   一页条数
	 * @param productStatus 1：商品列表  0：草稿箱  3：垃圾箱 
	 * @param productsId 产品ID
	 * @param recommend  0不推荐，1推荐
	 * @return
	 */
	PageVo<TProducts> queryProductsList(String title, Integer status,Date strStartDate, Date strEndDate, Integer pageNumber,Integer pageSize, String productStatus, String recommend, Long productsId,UserSessionInfo user);

	/*Boolean queryProductsList(Long id, String title, Integer carType,
			Integer priceRange, Integer position, String basicConfigure,
			String mustConfigure, String optionalConfigure, String carPrice,
			String mustConfigurePrice, String stock, String content, String country, String factoryDate, Long brandId, String indexImageH, String shrinkImageH, String detailedImageH, String draftSave, String recommendImageH, String detailedImageOld, int newly);*/

	/**
	 * 车辆信息展示回显
	 * @param id
	 * @return
	 */
	TProductsPojo showUpdateProducts(Long id);

	/**
	 * 产品上架
	 * @param id
	 * @return
	 */
	Boolean updataEnable(Long id);

	/**
	 * 产品下架
	 * @param id
	 * @return
	 */
	Boolean updataDisable(Long id);
	
	/**
	 * 产品删除
	 * @param id
	 * @return
	 */
	Boolean updataDelete(Long id);

	/**
	 * 还原到产品
	 * @param id
	 * @return
	 */
	Boolean updataRestore(Long id,Integer state);
	
	/**
	 * 产品推荐
	 * @param id
	 * @return
	 */
	Boolean updataRecommend(Long id);

	/**
	 * 产品取消推荐
	 * @param id
	 * @return
	 */
	Boolean updataCancelRecommend(Long id);

	
	/**
	 * 查询首页展示的推荐商品
	 * @return
	 */
	List<TProductsPojo> queryIndexRecommend(UserSessionInfo user);

	/**
	 * 查询首页展示的十条商品信息
	 * @return
	 */
	List<TProductsPojo> queryIndexCommodityList(UserSessionInfo user);

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
	 * 查询商品详情
	 * @param productsId
	 * @return
	 */
	TProductsPojo queryProductsDetailed(Long productsId, UserSessionInfo user);

	TProductsPojo queryExhibitionTProducts(Long productsId,UserSessionInfo user);

	/**
	 * 保存或修改产品车辆信息
	 * @return
	 */
	TProductsInputPojo queryProductsList(TProductsInputPojo tProductsInputPojo, int saveState, Integer self_support);

	/**
	 * 查询商品对应的图片
	 * @param productId
	 * @param cover
	 * @return
	 */
	String queryProductsImg(Long productId, int cover);
	
	/**
	 * 查询对应的车架号
	 * @param id
	 * @return
	 */
	List<TProductsCar> queryCarNo(Long id);

	/**
	 * 查询可用车架号数量
	 * @param productsId
	 * @return
	 */
	int queryCarNoCount(Long productsId);

	/**
	 * 产品审核
	 * @param id
	 * @param result
	 * @return
	 */
	Integer toExamine(Long id, Integer result);

	/**
	 * 提交审核
	 * @param id
	 * @return
	 */
	Integer subAudit(Long id);

	TProducts getProductById(Long productId);

	BigDecimal actualCarCarPrice(UserSessionInfo user, TProductsPojo tProductsPojo);

	/**
	 * 对产品进行审核和加价
	 * @param id
	 * @param result
	 * @param personal
	 * @param sales
	 * @param distributor
	 * @return
	 */
	Integer toExamineFare(Long id, Integer result, String personal, String sales, String distributor);
}

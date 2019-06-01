package com.buyauto.dao.products;

import java.io.Serializable;


public class TProductsInputPojo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1120122506186904050L;
	private Long id;
	private String title;//标题
	private Integer carType;//0轿车1SUV2MPV3皮卡4卡车5跑车
	private Integer priceRange;//0:50以下,1:100以下,2:150以下,3:150以上
	private Integer position;//0港口现车1预定车2在途车
	private String basicConfigure;//基础配置
	private String mustConfigure;//必选配置
	private String optionalConfigure;//可选配置(json)
	private String carPrice;//裸车价格
	private String mustConfigurePrice;//必选件总价
	private String  stock;//库存
	private String content;//富文本
	private String country;//原产地
	private Long brandId;//品牌ID
	private String factoryDate;//出厂日期
	private Integer deposit;//定金支付比例
	private String carModel;//车辆型号
	private String outColor;//车体外观颜色
	private String inColor;//车身内部颜色
	private String engine;//发动机
	private String gearbox;//变速箱
	private String chinaPrice;//国内指导价
	private String indexImageH;//封面图
	private String shrinkImageH;//缩略图    
	private String detailedImageH;//详情 (多图)
	private String detailedImageOld;//详情 (多图) 修改的时候获取原来的json数据,做比较
	private String recommendImageH;//推荐图
	private String draftSave;//0:草稿  1:新建
	private String sales;//个人销售加价
	private String personal;//个人客户加价
	private String distributor;//经销商加价
	private Long operId;//添加这个产品的用户ID
	private String productFileImg;//车辆三证
	private String productFileImgOld;//车辆三证(老图，修改时作对比)
	
	public String getChinaPrice() {
		return chinaPrice;
	}
	public void setChinaPrice(String chinaPrice) {
		this.chinaPrice = chinaPrice;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getCarType() {
		return carType;
	}
	public void setCarType(Integer carType) {
		this.carType = carType;
	}
	public Integer getPriceRange() {
		return priceRange;
	}
	public void setPriceRange(Integer priceRange) {
		this.priceRange = priceRange;
	}
	public Integer getPosition() {
		return position;
	}
	public void setPosition(Integer position) {
		this.position = position;
	}
	public String getBasicConfigure() {
		return basicConfigure;
	}
	public void setBasicConfigure(String basicConfigure) {
		this.basicConfigure = basicConfigure;
	}
	public String getMustConfigure() {
		return mustConfigure;
	}
	public void setMustConfigure(String mustConfigure) {
		this.mustConfigure = mustConfigure;
	}
	public String getOptionalConfigure() {
		return optionalConfigure;
	}
	public void setOptionalConfigure(String optionalConfigure) {
		this.optionalConfigure = optionalConfigure;
	}
	public String getCarPrice() {
		return carPrice;
	}
	public void setCarPrice(String carPrice) {
		this.carPrice = carPrice;
	}
	public String getMustConfigurePrice() {
		return mustConfigurePrice;
	}
	public void setMustConfigurePrice(String mustConfigurePrice) {
		this.mustConfigurePrice = mustConfigurePrice;
	}
	public String getStock() {
		return stock;
	}
	public void setStock(String stock) {
		this.stock = stock;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public Long getBrandId() {
		return brandId;
	}
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
	public String getFactoryDate() {
		return factoryDate;
	}
	public void setFactoryDate(String factoryDate) {
		this.factoryDate = factoryDate;
	}
	public String getIndexImageH() {
		return indexImageH;
	}
	public void setIndexImageH(String indexImageH) {
		this.indexImageH = indexImageH;
	}
	public String getShrinkImageH() {
		return shrinkImageH;
	}
	public void setShrinkImageH(String shrinkImageH) {
		this.shrinkImageH = shrinkImageH;
	}
	public String getDetailedImageH() {
		return detailedImageH;
	}
	public void setDetailedImageH(String detailedImageH) {
		this.detailedImageH = detailedImageH;
	}
	public String getDetailedImageOld() {
		return detailedImageOld;
	}
	public void setDetailedImageOld(String detailedImageOld) {
		this.detailedImageOld = detailedImageOld;
	}
	public String getRecommendImageH() {
		return recommendImageH;
	}
	public void setRecommendImageH(String recommendImageH) {
		this.recommendImageH = recommendImageH;
	}
	public String getDraftSave() {
		return draftSave;
	}
	public void setDraftSave(String draftSave) {
		this.draftSave = draftSave;
	}
	public Integer getDeposit() {
		return deposit;
	}
	public void setDeposit(Integer deposit) {
		this.deposit = deposit;
	}
	public String getCarModel() {
		return carModel;
	}
	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}
	public String getOutColor() {
		return outColor;
	}
	public void setOutColor(String outColor) {
		this.outColor = outColor;
	}
	public String getInColor() {
		return inColor;
	}
	public void setInColor(String inColor) {
		this.inColor = inColor;
	}
	public String getEngine() {
		return engine;
	}
	public void setEngine(String engine) {
		this.engine = engine;
	}
	public String getGearbox() {
		return gearbox;
	}
	public void setGearbox(String gearbox) {
		this.gearbox = gearbox;
	}
	public String getSales() {
		return sales;
	}
	public void setSales(String sales) {
		this.sales = sales;
	}
	public String getPersonal() {
		return personal;
	}
	public void setPersonal(String personal) {
		this.personal = personal;
	}
	public String getDistributor() {
		return distributor;
	}
	public void setDistributor(String distributor) {
		this.distributor = distributor;
	}
	public Long getOperId() {
		return operId;
	}
	public void setOperId(Long operId) {
		this.operId = operId;
	}
	public String getProductFileImg() {
		return productFileImg;
	}
	public void setProductFileImg(String productFileImg) {
		this.productFileImg = productFileImg;
	}
	public String getProductFileImgOld() {
		return productFileImgOld;
	}
	public void setProductFileImgOld(String productFileImgOld) {
		this.productFileImgOld = productFileImgOld;
	}
	
	
	
}

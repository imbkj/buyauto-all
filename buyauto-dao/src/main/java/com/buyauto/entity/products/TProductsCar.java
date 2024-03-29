package com.buyauto.entity.products;

import java.util.Date;

public class TProductsCar {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_products_car.id
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_products_car.car_no
     *
     * @mbggenerated
     */
    private String carNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_products_car.engine_no
     *
     * @mbggenerated
     */
    private String engineNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_products_car.status
     *
     * @mbggenerated
     */
    private Integer status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_products_car.oper_id
     *
     * @mbggenerated
     */
    private Long operId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_products_car.create_time
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_products_car.update_time
     *
     * @mbggenerated
     */
    private Date updateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_products_car.version
     *
     * @mbggenerated
     */
    private Integer version;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_products_car.products_id
     *
     * @mbggenerated
     */
    private Long productsId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_products_car.id
     *
     * @return the value of t_products_car.id
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_products_car.id
     *
     * @param id the value for t_products_car.id
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_products_car.car_no
     *
     * @return the value of t_products_car.car_no
     *
     * @mbggenerated
     */
    public String getCarNo() {
        return carNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_products_car.car_no
     *
     * @param carNo the value for t_products_car.car_no
     *
     * @mbggenerated
     */
    public void setCarNo(String carNo) {
        this.carNo = carNo == null ? null : carNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_products_car.engine_no
     *
     * @return the value of t_products_car.engine_no
     *
     * @mbggenerated
     */
    public String getEngineNo() {
        return engineNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_products_car.engine_no
     *
     * @param engineNo the value for t_products_car.engine_no
     *
     * @mbggenerated
     */
    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo == null ? null : engineNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_products_car.status
     *
     * @return the value of t_products_car.status
     *
     * @mbggenerated
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_products_car.status
     *
     * @param status the value for t_products_car.status
     *
     * @mbggenerated
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_products_car.oper_id
     *
     * @return the value of t_products_car.oper_id
     *
     * @mbggenerated
     */
    public Long getOperId() {
        return operId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_products_car.oper_id
     *
     * @param operId the value for t_products_car.oper_id
     *
     * @mbggenerated
     */
    public void setOperId(Long operId) {
        this.operId = operId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_products_car.create_time
     *
     * @return the value of t_products_car.create_time
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_products_car.create_time
     *
     * @param createTime the value for t_products_car.create_time
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_products_car.update_time
     *
     * @return the value of t_products_car.update_time
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_products_car.update_time
     *
     * @param updateTime the value for t_products_car.update_time
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_products_car.version
     *
     * @return the value of t_products_car.version
     *
     * @mbggenerated
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_products_car.version
     *
     * @param version the value for t_products_car.version
     *
     * @mbggenerated
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_products_car.products_id
     *
     * @return the value of t_products_car.products_id
     *
     * @mbggenerated
     */
    public Long getProductsId() {
        return productsId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_products_car.products_id
     *
     * @param productsId the value for t_products_car.products_id
     *
     * @mbggenerated
     */
    public void setProductsId(Long productsId) {
        this.productsId = productsId;
    }
}
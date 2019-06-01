package com.buyauto.util.web;

import java.io.Serializable;

/**
 * 返回APP格式类
 */
public class AppWebResultVo<T> implements Serializable, Cloneable {
	/* 静态变量 */
	/**
	 * 序列化值
	 */
	private static final long serialVersionUID = 7023549469966757623L;
	/**
	 * 失败
	 */
	private static final int ReqFail = -1;
	/**
	 * 警告
	 */
	private static final int ReqWarn = 0;
	/**
	 * 成功
	 */
	private static final int ReqSucc = 1;

	/* 私有变量 */
	/**
	 * 请求结果
	 */
	private int reqstu;

	private T detail;

	private String warnCode;

	private String errorCode;
	
	private Object extend;

	/**
	 * 私有构造
	 * 
	 * @param reqstu
	 */
	private AppWebResultVo(int reqstu) {
		this.reqstu = reqstu;
	}

	public static final <T> AppWebResultVo<T> buildSucc(T detail) {
		AppWebResultVo<T> result = new AppWebResultVo<T>(ReqSucc);
		result.detail = detail;
		return result;
	}
	
	public static final <T> AppWebResultVo<T> buildSucc(T detail, String warnCode) {
		AppWebResultVo<T> result = new AppWebResultVo<T>(ReqSucc);
		result.detail = detail;
		result.warnCode = warnCode;
		return result;
	}

	public static final <T> AppWebResultVo<T> buildWarn(T detail, String warnCode) {
		AppWebResultVo<T> result = new AppWebResultVo<T>(ReqWarn);
		result.detail = detail;
		result.warnCode = warnCode;
		return result;
	}

	public static final <T> AppWebResultVo<T> buildWarn(String warnCode) {
		AppWebResultVo<T> result = new AppWebResultVo<T>(ReqWarn);
		result.detail = null;
		result.warnCode = warnCode;
		return result;
	}

	public static final <T> AppWebResultVo<T> buildFail(T detail, String errorCode) {
		AppWebResultVo<T> result = new AppWebResultVo<T>(ReqFail);
		result.detail = detail;
		result.errorCode = errorCode;
		return result;
	}

	public static final <T> AppWebResultVo<T> buildFail(String errorCode) {
		AppWebResultVo<T> result = new AppWebResultVo<T>(ReqFail);
		result.detail = null;
		result.errorCode = errorCode;
		return result;
	}

	public int getReqstu() {
		return reqstu;
	}

	public T getDetail() {
		return detail;
	}

	public String getWarnCode() {
		return warnCode;
	}

	public String getErrorCode() {
		return errorCode;
	}
	

	public Object getExtend() {
		return extend;
	}

	public void setExtend(Object extend) {
		this.extend = extend;
	}

	@Override
	public String toString() {
		return "AppWebResultVo [reqstu=" + reqstu + ", detail=" + detail + ", warnCode=" + warnCode + ", errorCode="
				+ errorCode + "]";
	}

}

package com.buyauto.util.pojo;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Value;

public class FinanceDto implements Serializable {

	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/
	private static final long serialVersionUID = 498064163339178745L;
	
	@Value("${user.priceLine}")
	private int priceLine;

	@Value("${user.bzj}")
	private int bzj;
	
	@Value("${user.sxfOne}")
	private int sxfOne;
	
	@Value("${user.sxfTwo}")
	private int sxfTwo;

	@Value("${user.sxfThree}")
	private int sxfThree;
	
	@Value("${user.dqOne}")
	private int dqOne;
	
	@Value("${user.dqTwo}")
	private int dqTwo;

	@Value("${user.dqThree}")
	private int dqThree;
	
	public int getPriceLine() {
		return priceLine;
	}

	public void setPriceLine(int priceLine) {
		this.priceLine = priceLine;
	}

	public int getBzj() {
		return bzj;
	}

	public void setBzj(int bzj) {
		this.bzj = bzj;
	}

	public int getSxfOne() {
		return sxfOne;
	}

	public void setSxfOne(int sxfOne) {
		this.sxfOne = sxfOne;
	}

	public int getSxfTwo() {
		return sxfTwo;
	}

	public void setSxfTwo(int sxfTwo) {
		this.sxfTwo = sxfTwo;
	}

	public int getSxfThree() {
		return sxfThree;
	}

	public void setSxfThree(int sxfThree) {
		this.sxfThree = sxfThree;
	}

	public int getDqOne() {
		return dqOne;
	}

	public void setDqOne(int dqOne) {
		this.dqOne = dqOne;
	}

	public int getDqTwo() {
		return dqTwo;
	}

	public void setDqTwo(int dqTwo) {
		this.dqTwo = dqTwo;
	}

	public int getDqThree() {
		return dqThree;
	}

	public void setDqThree(int dqThree) {
		this.dqThree = dqThree;
	}

}

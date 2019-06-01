package com.buyauto.util.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @ClassName: PageVo
 * @Description:分页通用类
 * @author cxz
 * @date 2016年5月25日 下午6:21:51
 *
 * @param <T>
 */
public class PageVo<T> implements Serializable {

	private static final long serialVersionUID = -8009087606315453287L;

	/* 总页数 */
	protected int total;
	/* 当前页码 */
	protected int pageNumber;
	/* 数据开始范围 */
	protected int pageStartNumber;
	/* 数据结束范围 */
	protected int pageEndNumber;
	/* 当前查询数据 */
	protected List<T> rows = null;

	/* 插件所需总计录数 */
	protected int recordsTotal;
	/* 插件所需过滤后总计录数 */
	protected int recordsFiltered;

	public PageVo(int total, int pageNumber, int pageSize) {
		super();
		this.total = total;
		this.pageNumber = pageNumber;
		this.pageStartNumber = (pageNumber - 1) * pageSize;
		this.pageEndNumber = pageSize;
	}

	public int getRecordsTotal() {
		return total;
	}

	public int getRecordsFiltered() {
		return total;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {

		this.total = total;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageStartNumber() {
		return pageStartNumber;
	}

	public void setPageStartNumber(int pageStartNumber) {
		this.pageStartNumber = pageStartNumber;
	}

	public int getPageEndNumber() {
		return pageEndNumber;
	}

	public void setPageEndNumber(int pageEndNumber) {
		this.pageEndNumber = pageEndNumber;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

}

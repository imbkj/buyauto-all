package com.buyauto.dao.cms;

import java.io.Serializable;
import java.util.Date;


/**
 * 资讯新闻pojo
 */
public class NewsPojo  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5161709660306776495L;
	private Long id;//id
	private String title;//标题
	private String introduction;//简介
	private String pic;//图片名称
	private Date onlinetime;//上线时间
	private String author;//作者
	
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public Date getOnlinetime() {
		return onlinetime;
	}
	public void setOnlinetime(Date onlinetime) {
		this.onlinetime = onlinetime;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
}
package com.buyauto.entity.sys;

import java.util.List;

public class EasyUiTree {
	private String text;
	private Long id;
	private List<EasyUiTree> children;
	private String type;
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<EasyUiTree> getChildren() {
		return children;
	}
	public void setChildren(List<EasyUiTree> children) {
		this.children = children;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}

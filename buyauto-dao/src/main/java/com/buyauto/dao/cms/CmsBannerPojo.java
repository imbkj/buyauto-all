package com.buyauto.dao.cms;

import java.io.Serializable;
import java.util.Date;

/**
 * banner表pojo 
 */
public class CmsBannerPojo implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -1723411201692917422L;
	private Long id;
    private String title;
    private String link;
    private String image;
    private Integer orderNum;
    private Integer type;
    private Integer status;
    private Date createTime;
    private Date updateTime;
    private Long operId;
    private Integer terminal;
    private Integer version;
    
    //操作人姓名
    private String operName;
    private String typeName;//模块名称
    
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
        this.title = title == null ? null : title.trim();
    }
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link == null ? null : link.trim();
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }
    public Integer getOrderNum() {
        return orderNum;
    }
    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }
    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void UpdateTime(Date createTime) {
        this.createTime = createTime;
    }
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Long getOperId() {
		return operId;
	}
	public void setOperId(Long operId) {
		this.operId = operId;
	}
	public Integer getTerminal() {
		return terminal;
	}
	public void setTerminal(Integer terminal) {
		this.terminal = terminal;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getOperName() {
		return operName;
	}
	public void setOperName(String operName) {
		this.operName = operName;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
}
package com.buyauto.entity.audit;

import java.io.Serializable;
import java.util.Date;

/**
 * 审核实体类
 */
public class TAuditing implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
    private Integer result;
    private String info;
    private Integer type;
    private Long matchId;
    private Date createDate;
    private Long operId;
    private String operName;
    private Integer version;

    public TAuditing(Long id,int result,Integer type,Long matchId, Date createDate,String operName,Long operId,String info){
    	this.id = id;
    	this.type = type;
    	this.matchId = matchId;
    	this.createDate = createDate;
    	this.result = result;
    	this.operName = operName;
    	this.operId = operId;
    	this.info = info;
    }
    
    public TAuditing(){
    	
    }
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getResult() {
        return result;
    }
    public void setResult(Integer result) {
        this.result = result;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info == null ? null : info.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getOperId() {
        return operId;
    }

    public void setOperId(Long operId) {
        this.operId = operId;
    }

    public String getOperName() {
        return operName;
    }

    public void setOperName(String operName) {
        this.operName = operName == null ? null : operName.trim();
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
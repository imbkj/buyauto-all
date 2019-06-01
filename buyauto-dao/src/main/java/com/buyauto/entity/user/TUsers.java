package com.buyauto.entity.user;

import java.util.Date;

public class TUsers implements java.io.Serializable {
    
	private static final long serialVersionUID = 1L;
	
	private Long id;
    private Long companyId;//经销商id
    private String companyName;//经销商名称
    private String name;//姓名
    private String phone;//手机
    private String email;//邮箱
    private String pwd;//密码(加密后的)
    private Integer position;//岗位:0管理员,1销售，2财务 ,3供应商,4个人
    private String account;//账号名
    private Date hiredate;//入职时间
    private String jobNumber;//工号
    private String region;//所属区域
    private String file1;
    private String file2;
    private String file3;
    private Integer status;//0待审核,1可用,2审核不通过,3冻结
    private Integer isFreeze;//经销商冻结状态0可用1禁用
    private Integer type;//0:经销商,1:供应商,2:个人
    private Integer fileType;
    private Date createDate;
    private Date updateDate;
    private Integer version;
    private String companyFullName;//公司完整名称
    private String address;//办公场所
    private Integer maxNumberCount;//经销商可新建员工数量
    private Integer isFirstLogin;//是否首次登陆0首次1已登陆
    private String recommender;//推荐人
    private Integer realLevel;//个人实际等级:0个人,1销售,2经销商
    private Integer sysLevel;//个人系统等级:0个人,1销售,2经销商
    private Integer selfSupport;//自营供应商:0否1是
    private String cardID;//身份证
    private String debitCard;//借记卡号
    private String creditCard;//信用卡号
    private Integer salesStatus;//0待审核,1可用,2审核不通过
    private String creditCardBank;
    private String creditCardFile;
    private String debitCardBank;
    private String debitCardFile;
    private String cardFile;
    
    public TUsers(){}
    public TUsers(Long id,String account2, String pwd2,String companyName2, String name2,
			String phone2, String email2, int i, Date date, int j) {
    	this.id = id;
    	this.account = account2;
    	this.pwd = pwd2;
    	this.companyName =companyName2;
    	this.name = name2;
    	this.phone = phone2;
    	this.email = email2;
    	this.position = i;
    	this.status = j;
    	this.createDate = date;
	}
    
    public TUsers(Long id,String account2, String pwd2,String companyName2, String name2,
			String phone2, String email2, Date date, int j,String address) {
    	this.id = id;
    	this.account = account2;
    	this.pwd = pwd2;
    	this.companyName =companyName2;
    	this.name = name2;
    	this.phone = phone2;
    	this.email = email2;
    	this.status = j;
    	this.createDate = date;
    	this.address=address;
	}

	public TUsers(long id, Long companyId2, String name2, Integer position2, String companyName2, String account2, String phone2, String email2,
			String pwd2,Date hirdeate, String jobNumber2, String region2, int statusAuditWait, int freezeSucess) {
		this.id = id;
		this.companyId = companyId2;
		this.name = name2;
		this.position = position2;
		this.companyName = companyName2;
		this.account = account2;
		this.phone = phone2;
		this.email = email2;
		this.pwd = pwd2;
		this.hiredate = hirdeate;
		this.jobNumber = jobNumber2;
		this.region = region2;
		this.status = statusAuditWait;
		this.isFreeze = freezeSucess;
		this.createDate = new Date();
	}
	
	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd == null ? null : pwd.trim();
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public Date getHiredate() {
        return hiredate;
    }

    public void setHiredate(Date hiredate) {
        this.hiredate = hiredate;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber == null ? null : jobNumber.trim();
    }

    public String getRegion() {
        return region;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_users.region
     *
     * @param region the value for t_users.region
     *
     * @mbggenerated
     */
    public void setRegion(String region) {
        this.region = region == null ? null : region.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_users.file1
     *
     * @return the value of t_users.file1
     *
     * @mbggenerated
     */
    public String getFile1() {
        return file1;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_users.file1
     *
     * @param file1 the value for t_users.file1
     *
     * @mbggenerated
     */
    public void setFile1(String file1) {
        this.file1 = file1 == null ? null : file1.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_users.file2
     *
     * @return the value of t_users.file2
     *
     * @mbggenerated
     */
    public String getFile2() {
        return file2;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_users.file2
     *
     * @param file2 the value for t_users.file2
     *
     * @mbggenerated
     */
    public void setFile2(String file2) {
        this.file2 = file2 == null ? null : file2.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_users.file3
     *
     * @return the value of t_users.file3
     *
     * @mbggenerated
     */
    public String getFile3() {
        return file3;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_users.file3
     *
     * @param file3 the value for t_users.file3
     *
     * @mbggenerated
     */
    public void setFile3(String file3) {
        this.file3 = file3 == null ? null : file3.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_users.status
     *
     * @return the value of t_users.status
     *
     * @mbggenerated
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_users.status
     *
     * @param status the value for t_users.status
     *
     * @mbggenerated
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_users.is_freeze
     *
     * @return the value of t_users.is_freeze
     *
     * @mbggenerated
     */
    public Integer getIsFreeze() {
        return isFreeze;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_users.is_freeze
     *
     * @param isFreeze the value for t_users.is_freeze
     *
     * @mbggenerated
     */
    public void setIsFreeze(Integer isFreeze) {
        this.isFreeze = isFreeze;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_users.type
     *
     * @return the value of t_users.type
     *
     * @mbggenerated
     */
    public Integer getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_users.type
     *
     * @param type the value for t_users.type
     *
     * @mbggenerated
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_users.file_type
     *
     * @return the value of t_users.file_type
     *
     * @mbggenerated
     */
    public Integer getFileType() {
        return fileType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_users.file_type
     *
     * @param fileType the value for t_users.file_type
     *
     * @mbggenerated
     */
    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_users.create_date
     *
     * @return the value of t_users.create_date
     *
     * @mbggenerated
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_users.create_date
     *
     * @param createDate the value for t_users.create_date
     *
     * @mbggenerated
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_users.update_date
     *
     * @return the value of t_users.update_date
     *
     * @mbggenerated
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_users.update_date
     *
     * @param updateDate the value for t_users.update_date
     *
     * @mbggenerated
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_users.version
     *
     * @return the value of t_users.version
     *
     * @mbggenerated
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_users.version
     *
     * @param version the value for t_users.version
     *
     * @mbggenerated
     */
    public void setVersion(Integer version) {
        this.version = version;
    }
	public String getCompanyFullName() {
		return companyFullName;
	}
	public void setCompanyFullName(String companyFullName) {
		this.companyFullName = companyFullName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getMaxNumberCount() {
		return maxNumberCount;
	}
	public void setMaxNumberCount(Integer maxNumberCount) {
		this.maxNumberCount = maxNumberCount;
	}
	public Integer getIsFirstLogin() {
		return isFirstLogin;
	}
	public void setIsFirstLogin(Integer isFirstLogin) {
		this.isFirstLogin = isFirstLogin;
	}
	public String getRecommender() {
		return recommender;
	}
	public void setRecommender(String recommender) {
		this.recommender = recommender;
	}
	public Integer getRealLevel() {
		return realLevel;
	}
	public void setRealLevel(Integer realLevel) {
		this.realLevel = realLevel;
	}
	public Integer getSysLevel() {
		return sysLevel;
	}
	public void setSysLevel(Integer sysLevel) {
		this.sysLevel = sysLevel;
	}
	public Integer getSelfSupport() {
		return selfSupport;
	}
	public void setSelfSupport(Integer selfSupport) {
		this.selfSupport = selfSupport;
	}
	public String getCardID() {
		return cardID;
	}
	public void setCardID(String cardID) {
		this.cardID = cardID;
	}
	public String getDebitCard() {
		return debitCard;
	}
	public void setDebitCard(String debitCard) {
		this.debitCard = debitCard;
	}
	public String getCreditCard() {
		return creditCard;
	}
	public void setCreditCard(String creditCard) {
		this.creditCard = creditCard;
	}
	public Integer getSalesStatus() {
		return salesStatus;
	}
	public void setSalesStatus(Integer salesStatus) {
		this.salesStatus = salesStatus;
	}
	public String getCreditCardBank() {
		return creditCardBank;
	}
	public void setCreditCardBank(String creditCardBank) {
		this.creditCardBank = creditCardBank;
	}
	public String getCreditCardFile() {
		return creditCardFile;
	}
	public void setCreditCardFile(String creditCardFile) {
		this.creditCardFile = creditCardFile;
	}
	public String getDebitCardBank() {
		return debitCardBank;
	}
	public void setDebitCardBank(String debitCardBank) {
		this.debitCardBank = debitCardBank;
	}
	public String getDebitCardFile() {
		return debitCardFile;
	}
	public void setDebitCardFile(String debitCardFile) {
		this.debitCardFile = debitCardFile;
	}
	public String getCardFile() {
		return cardFile;
	}
	public void setCardFile(String cardFile) {
		this.cardFile = cardFile;
	}
}
package com.buyauto.mapper.user;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.buyauto.dao.user.TUserFinanceDao;
import com.buyauto.entity.user.TUserFinance;
import com.buyauto.entity.user.TUsers;

public interface TUserFinanceMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user_finance
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user_finance
     *
     * @mbggenerated
     */
    int insert(TUserFinance record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user_finance
     *
     * @mbggenerated
     */
    int insertSelective(TUserFinance record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user_finance
     *
     * @mbggenerated
     */
    TUserFinance selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user_finance
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(TUserFinance record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user_finance
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(TUserFinance record);

	List<TUserFinanceDao> queryFinanceAudit(@Param("pageStartNumber")int pageStartNumber,@Param("pageEndNumber") int pageEndNumber,@Param("status") String status,@Param("name") String name, @Param("phone")String phone,
			@Param("startTime")Date strStartDates,@Param("endTime") Date strEndDates);

	int queryFinanceAuditCount(@Param("startTime")Date strStartDates,@Param("endTime") Date strEndDates,@Param("status") String status, @Param("name")String name,@Param("phone") String phone);


}
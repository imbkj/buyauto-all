package com.buyauto.mapper.audit;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.buyauto.entity.audit.TAuditing;
import com.buyauto.entity.user.TUsers;
import com.buyauto.util.pojo.PageVo;

/**
 * 审核Mapper 
 */
public interface TAuditingMapper {
	
    int deleteByPrimaryKey(Long id);
    
    int insert(TAuditing record);

    int insertSelective(TAuditing record);
    
    TAuditing selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TAuditing record);

    int updateByPrimaryKey(TAuditing record);

    /**
	 * 分页条件查询审核列表
	 * @param page 页码
	 * @param rows 一页条数
	 * @param type 类型
	 * @param strStartDates 开始时间
	 * @param strEndDates   结束时间
	 */
    List<TAuditing> queryAudit(@Param("startNumber") int startNumber,@Param("endNumber") int rows,@Param("type")  String type,
			@Param("startTime") Date strStartDates,@Param("endTime") Date strEndDates,@Param("result")  String result);

    /**
     * 查找总条数
     */
	int queryAuditCount(@Param("startTime") Date strStartDates,@Param("endTime") Date strEndDates,
			@Param("type") String type,@Param("result")  String result);

	/**
	 * 根据对应ID查询原因
	 * @param mid
	 * @return
	 */
	String getNotPassInfoByMId(@Param("mid") Long mid);

	/**
	 * 根据ID查询审核意见
	 * @param id 主键ID
	 */
	String getInfoById(@Param("id")Long id);

	/**
	 * 根据对应ID查询原因
	 * @param mid
	 * @return
	 */
	TAuditing getCheckInfoByMId(@Param("mid") Long mid);
	
}
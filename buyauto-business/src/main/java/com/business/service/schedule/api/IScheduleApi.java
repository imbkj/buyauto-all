package com.business.service.schedule.api;

import java.util.Date;

import com.buyauto.entity.sys.SysScheduled;


public interface IScheduleApi {

	/**
	 * 根据job名称和对账日查询当前job
	 * 
	 * @param jobName
	 * @param reconDay
	 * @return
	 */
	SysScheduled selectJobInfo(String jobBizName, String reconDay);

	/**
	 * 更新job状态
	 * 
	 * @param jobName
	 *            job名称
	 * @param reconDay
	 *            对账日
	 * @param oldStatus
	 *            job执行之前的状态
	 * @param status
	 *            当前新状态
	 * @param version
	 *            版本控制
	 * @return
	 */
	int updateJobStatus(String jobBizName, String reconDay, int oldStatus, int status, int version);

	/**
	 * 更新执行时间
	 * 
	 * @param id
	 * @param endDate
	 * @param interval
	 * @return
	 */
	int updateJobExecuteTime(long id, Date endDate, long interval, int version);

	/**
	 * 更新job开始时间
	 * 
	 * @param id
	 * @param startDate
	 * @return
	 */
	int updateJobStartTime(long id, Date startDate, int version);
	/**
	 * 
	 * @param jobInfo
	 * @return
	 * @Description: 创建Job
	 * @date 2015年3月19日 下午3:52:44
	 * @author xujinyang
	 * @exception  (方法有异常的话加)
	 */
	int createJobInfo(SysScheduled jobInfo);
	/**
	 * 
	 * @param id
	 * @return
	 * @Description: 查询Job按Id
	 * @date 2015年3月19日 下午3:52:52
	 * @author xujinyang
	 * @exception  (方法有异常的话加)
	 */
	SysScheduled selectJobInfoById(Long id);

}


package com.controller.base;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.business.service.schedule.api.IScheduleApi;
import com.buyauto.entity.sys.SysScheduled;
import com.buyauto.util.core.tool.buyautoenum.JobFlagEnum;
import com.buyauto.util.method.KeyUtil;



public abstract class AbstractJob implements IJob, InitializingBean {

	private final static Logger logger = LoggerFactory.getLogger(AbstractJob.class);

	/* 随机休眠器 */
	private final static Random randomSleep = new Random();

	/* 对账日期格式处理 */
	private static final SimpleDateFormat reconBizDayFormat = new SimpleDateFormat("yyyyMMdd");

	@Autowired
	private IScheduleApi iScheduleApi;

	@Override
	public void execute() {
		/* 进行随机休眠，避免锁循环 */
		try {
			Thread.sleep(randomSleep.nextInt(10000));
		} catch (InterruptedException e) {
			/* 系统原因或者中断原因，直接返回 */
			logger.error(e.getMessage(), e);
			return;
		}
		Date bizDate = this.genBizDay();
		/* 查询当前job是否存在且状态是否为失败状态，如果是失败状态，进行重新执行 */
		String reconDate = this.convertBizDay(bizDate);
		String jobBizName = this.getJobName() + reconDate;
		SysScheduled jobInfo = this.iScheduleApi.selectJobInfo(jobBizName, reconDate);
		boolean isRunning = false;
		if (jobInfo == null) {
			// 说明当天运行Job不存在，需要新建job
			jobInfo = this.genJobInfo(bizDate);
			this.iScheduleApi.createJobInfo(jobInfo);
			isRunning = this.iScheduleApi.updateJobStatus(jobInfo.getJobBizName(), jobInfo.getJobBizDay(), SysScheduled.INIT, SysScheduled.RUNNING, jobInfo.getJobVersion()) == 1;
		} else {
			// job已经存在，检查Job是否失败状态，或者初始化状态那么重新执行
			if (jobInfo.getJobStatus() == SysScheduled.INIT || jobInfo.getJobStatus() == SysScheduled.FAIL) {
				isRunning = this.iScheduleApi.updateJobStatus(jobInfo.getJobBizName(), jobInfo.getJobBizDay(), jobInfo.getJobStatus(), SysScheduled.RUNNING, jobInfo.getJobVersion()) == 1;
			}
		}
		if (!isRunning) {
			return;
		}
		logger.info("job name 为 [" + jobInfo.getJobName() + "] 当天任务task name [" + jobInfo.getJobBizName() + "] 开始执行......");

		Date startDate = new Date();
		// 更新job开始时间
		SysScheduled job = this.iScheduleApi.selectJobInfo(jobInfo.getJobBizName(), jobInfo.getJobBizDay());
		this.iScheduleApi.updateJobStartTime(job.getId(), startDate, job.getJobVersion());
		// 返回job执行结果，系统原因到job失败直接返回false
		boolean result = false;
		try {
			result = this.doExecute(bizDate);
		} catch (Exception ex) {
			logger.error("执行job报错", ex);
		}
		Date endDate = new Date();
		long interval = endDate.getTime() - startDate.getTime();
		// 更新项目结束时间
		SysScheduled info = this.iScheduleApi.selectJobInfoById(job.getId());
		this.iScheduleApi.updateJobExecuteTime(info.getId(), endDate, interval, info.getJobVersion());

		SysScheduled resultJobInfo = this.iScheduleApi.selectJobInfo(jobInfo.getJobBizName(), jobInfo.getJobBizDay());
		if (result) {
			logger.info("job name 为 [" + jobInfo.getJobName() + "] 当天任务task name [" + jobInfo.getJobBizName() + "] 执行成功......");
			// 更新job状态
			this.iScheduleApi.updateJobStatus(jobInfo.getJobBizName(), jobInfo.getJobBizDay(), SysScheduled.RUNNING, SysScheduled.SUCC, resultJobInfo.getJobVersion());
		} else {
			// 更新job失败状态
			this.iScheduleApi.updateJobStatus(jobInfo.getJobBizName(), jobInfo.getJobBizDay(), SysScheduled.RUNNING, SysScheduled.FAIL, resultJobInfo.getJobVersion());
		}

	}

	public SysScheduled genJobInfo(Date date) {
		SysScheduled jobInfo = new SysScheduled();
		jobInfo.setId(KeyUtil.generateDBKey());
		String reconDay = this.convertBizDay(date);
		jobInfo.setJobBizDay(reconDay);
		jobInfo.setJobName(this.getJobName().getJobType());
		jobInfo.setJobStart(new Date());
		jobInfo.setJobStatus(SysScheduled.INIT);
		jobInfo.setJobVersion(Integer.valueOf(0));
		jobInfo.setJobBizName(this.getJobName() + reconDay);
		return jobInfo;
	}

	protected String convertBizDay(Date date) {
		String recon = reconBizDayFormat.format(date);
		return recon;
	}

	/**
	 * 执行job具体内容
	 * 
	 * @return 返回job执行结果，系统原因到job失败直接返回false,与对账的结果没有关系
	 */
	public abstract boolean doExecute(Date date);

	/**
	 * 获取job名称
	 * 
	 * @return
	 */
	public abstract JobFlagEnum getJobName();

	/**
	 * 生成业务执行的时间
	 * 
	 * @return
	 * 
	 */

	public Date genBizDay() {
		/* 会计日/或者对账日处理 */
		Calendar calendar = Calendar.getInstance();
		Date now = new Date();
		calendar.setTime(now);
		calendar.add(Calendar.DATE, (-1) * 1);
		return calendar.getTime();
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}

}

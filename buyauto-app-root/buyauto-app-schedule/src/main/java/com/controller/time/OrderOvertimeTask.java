package com.controller.time;

import java.util.Date;

import com.business.service.time.api.IBaseTimerApi;
import com.buyauto.util.core.tool.buyautoenum.JobFlagEnum;
import com.buyauto.util.pojo.TimerTaskPojo;


public class OrderOvertimeTask extends AbstractTimerTask<IBaseTimerApi> {

	public OrderOvertimeTask(IBaseTimerApi taskApi, Date exDate, Long id) {
		super(taskApi, exDate, id);

	}

	@Override
	public boolean doBusiness(TimerTaskPojo taskPojo) {
		RunTimer.taskIdSet.remove(taskPojo.hashCode());
		RunTimer.maps.remove(taskPojo.hashCode() + "");
		boolean isNext = this.getTaskApi().updateOrderOvertime(taskPojo.getId());
		
		return isNext;
	}

	@Override
	public String getTaskType() {
		return JobFlagEnum.ORDER_OVERTIME.getJobType();
	}

}

package com.controller.time;

import java.util.Date;

import com.business.service.time.api.IBaseTimerApi;
import com.buyauto.util.core.tool.buyautoenum.JobFlagEnum;
import com.buyauto.util.pojo.TimerTaskPojo;

public class UserDowngradeTimeTask extends AbstractTimerTask<IBaseTimerApi> {

	public UserDowngradeTimeTask(IBaseTimerApi taskApi, Date exDate, Long id) {
		super(taskApi, exDate, id);
	}

	@Override
	public boolean doBusiness(TimerTaskPojo taskPojo) {
		System.out.println("用户降级执行=" + taskPojo.toString());
		RunTimer.taskIdSet.remove(taskPojo.hashCode());
		RunTimer.maps.remove(taskPojo.hashCode() + "");
		boolean isNext = this.getTaskApi().updateUserDowngrade(taskPojo.getId());
		return isNext;
	}

	@Override
	public String getTaskType() {
		return JobFlagEnum.USER_DOWNGRADE.getJobType();
	}

}

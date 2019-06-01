package com.controller.time;

import java.text.ParseException;
import java.util.Date;

import com.business.service.time.api.IBaseTimerApi;
import com.buyauto.util.core.tool.buyautoenum.JobFlagEnum;
import com.buyauto.util.pojo.TimerTaskPojo;


public class AutomaticTakeCarTask extends AbstractTimerTask<IBaseTimerApi> {

	public AutomaticTakeCarTask(IBaseTimerApi taskApi, Date exDate, Long id) {
		super(taskApi, exDate, id);

	}

	@Override
	public boolean doBusiness(TimerTaskPojo taskPojo) {
		RunTimer.taskIdSet.remove(taskPojo.hashCode());
		RunTimer.maps.remove(taskPojo.hashCode() + "");
		boolean isNext = false;
		try {
			isNext = this.getTaskApi().updateAutomaticTakeCarOrders(taskPojo.getId());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return isNext;
	}

	@Override
	public String getTaskType() {
		return JobFlagEnum.ORDER_AUTOTAKECAR.getJobType();
	}

}

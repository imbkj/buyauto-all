package com.controller.time;

import java.util.Date;
import java.util.TimerTask;

import com.buyauto.util.pojo.TimerTaskPojo;


public abstract class AbstractTimerTask<T> extends TimerTask {

	private T taskApi;

	private TimerTaskPojo timer;

	public AbstractTimerTask(T taskApi, Date exDate, Long id) {
		super();
		this.taskApi = taskApi;
		this.timer = new TimerTaskPojo(id, exDate, getTaskType());
	}

	public abstract String getTaskType();

	public abstract boolean doBusiness(TimerTaskPojo pojo);

	@Override
	public void run() {
		doBusiness(getTimerPojo());

	}

	public TimerTaskPojo getTimerPojo() {
		return timer;
	}

	public T getTaskApi() {
		return taskApi;
	}

	public int hashCode() {
		return timer.hashCode();
	}

	public boolean equals(Object obj) {
		return timer.hashCode() == obj.hashCode();
	}

}

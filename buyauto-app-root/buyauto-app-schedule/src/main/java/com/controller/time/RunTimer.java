package com.controller.time;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.ConcurrentLinkedDeque;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class RunTimer {

	public static Timer timerPool = new Timer();

	public static Queue<AbstractTimerTask<?>> queue = new ConcurrentLinkedDeque<AbstractTimerTask<?>>();

	public static Set<Integer> taskIdSet = new HashSet<Integer>();

	public static Map<String, AbstractTimerTask<?>> maps = Maps.newHashMap();

	public static AbstractTimerTask<?> getTimer() {
		AbstractTimerTask<?> ex = queue.poll();
		return ex;
	}

	public static void setTimer(AbstractTimerTask<?> ex) {
		if (!taskIdSet.contains(ex.getTimerPojo().hashCode())) {
			queue.offer(ex);
			maps.put(ex.hashCode() + "", ex);
			taskIdSet.add(ex.hashCode());
		}
	}

	public static void runTask() {
		AbstractTimerTask<?> ex = getTimer();
		while (ex != null) {
			Date now = new Date();
			if (ex.getTimerPojo().getExDate().getTime() < now.getTime()) {
				ex.run();
			} else {
				timerPool.schedule(ex, ex.getTimerPojo().getExDate());
			}
			ex = getTimer();
		}
	}
}

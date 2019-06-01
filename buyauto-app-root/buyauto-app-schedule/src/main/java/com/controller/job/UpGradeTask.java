package com.controller.job;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.business.service.time.api.IBaseTimerApi;
import com.buyauto.entity.orders.TOrders;
import com.buyauto.entity.user.TUsers;
import com.buyauto.util.method.CommonCode;
import com.buyauto.util.pojo.TimeSetting;
import com.controller.base.IJob;
import com.controller.time.OrderOvertimeTask;
import com.controller.time.RunTimer;
import com.controller.time.UserDowngradeTimeTask;
import com.controller.time.UserUpgradeTimeTask;

@Service("UpGradeTaskJob")
public class UpGradeTask implements IJob {

	private final static Logger logger = LoggerFactory.getLogger(UpGradeTask.class);

	@Autowired
	private IBaseTimerApi baseApi;

	/* 随机休眠器 */
	private final static Random randomSleep = new Random();

	@Override
	public void execute() {
		// 进行随机休眠，避免锁循环
		try {
			Thread.sleep(randomSleep.nextInt(10000));
		} catch (InterruptedException e) {
			// 系统原因或者中断原因，直接返回
			logger.error(e.getMessage(), e);
			return;
		}

		// 取得所有需要升降级的用户
		Map<String, List<TUsers>> upGradeUserList = baseApi.queryUpGradeUserList();
		List<TUsers> list = upGradeUserList.get("upgrade");
		// 升级
		if (list.size() != 0) {
			for (TUsers tUsers : list) {
				baseApi.updateUserUpgrade(tUsers.getId());
				//RunTimer.setTimer(new UserUpgradeTimeTask(baseApi, new Date(), tUsers.getId()));
			}
			// RunTimer.runTask();
		}

		List<TUsers> downGradeList = upGradeUserList.get("downGrade");
		// 降级
		if (downGradeList.size() != 0) {

			for (TUsers tUsers : downGradeList) {
				System.out.println("用户执行降级处理" + tUsers.toString());
				baseApi.updateUserDowngrade(tUsers.getId());
				//RunTimer.setTimer(new UserDowngradeTimeTask(baseApi, new Date(), tUsers.getId()));
			}
			RunTimer.runTask();
		}

		/***********************************/

	}

}

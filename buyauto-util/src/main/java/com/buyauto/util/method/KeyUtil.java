package com.buyauto.util.method;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * @ClassName: KeyUtil
 * @Description:id生成器
 * @author cxz
 * @date 2016年5月17日 上午10:51:44
 *
 */
public class KeyUtil {

	private static final AtomicInteger seqId = new AtomicInteger(10);

	private static final Lock lock = new ReentrantLock();

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

	public static long generateDBKey() {

		try {
			KeyUtil.lock.lock();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			String strDate = KeyUtil.sdf.format(new Date());
			StringBuffer sb = new StringBuffer();
			sb.append(strDate);
			if (KeyUtil.seqId.get() > 99) {
				KeyUtil.seqId.set(10);
			}
			sb.append(KeyUtil.seqId.getAndIncrement());
			return Long.parseLong(sb.toString());
		} finally {
			KeyUtil.lock.unlock();
		}

	}

}

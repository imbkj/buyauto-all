package com.buyauto.util.method;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.buyauto.util.pojo.BaseJymxResp;
import com.buyauto.util.pojo.BaseSftpDto;

/**
 * 
 * @ClassName: CvsAnalysisUtil
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author cxz
 * @date 2017年5月24日 下午1:49:05
 *
 */
public class CvsAnalysisUtil {

	private static final Logger logger = LoggerFactory
			.getLogger(CvsAnalysisUtil.class);

	private InputStreamReader fr = null;
	private BufferedReader br = null;

	public CvsAnalysisUtil(String f) throws IOException {
		fr = new InputStreamReader(new FileInputStream(f), "GBK");
	}

	public CvsAnalysisUtil() {
	}

	public static List<Map<Integer, String>> readCSV(String filepath)
			throws IOException {
		InputStreamReader isr = new InputStreamReader(new FileInputStream(
				filepath), "GBK");
		BufferedReader tbr = new BufferedReader(isr);
		String rec = null;// 一行
		List<Map<Integer, String>> csvlist = new ArrayList<Map<Integer, String>>();
		try {
			tbr.readLine();
			while ((rec = tbr.readLine()) != null) {
				System.out.println("读取原始数据" + rec);
				Pattern pCells = Pattern
						.compile("(\"[^\"]*(\"{2})*[^\"]*\")*[^,]*,");
				Matcher mCells = pCells.matcher(rec + ",");
				Map<Integer, String> cells = new HashMap<Integer, String>();
				String str;// 一个单元格
				// 读取每个单元格
				int i = 0;
				while (mCells.find()) {
					str = mCells.group();
					str = str.replaceAll(
							"(?sm)\"?([^\"]*(\"{2})*[^\"]*)\"?.*,", "$1");
					str = str.replaceAll("(?sm)(\"(\"))", "$2");
					cells.put(i, str);
					i++;
				}
				csvlist.add(cells);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			isr.close();
			tbr.close();
		}
		return csvlist;
	}

	/**
	 * 生成为CVS文件
	 * 
	 * @param exportData
	 *            源数据List
	 * @param map
	 *            csv文件的列表头map
	 * @param outPutPath
	 *            文件路径
	 * @param fileName
	 *            文件名称
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static File createCSVFile(List exportData, LinkedHashMap map,
			String outPutPath, String fileName, String loaclPath) {
		File csvFile = null;
		BufferedWriter csvFileOutputStream = null;
		try {
			File file = new File(loaclPath + outPutPath);

			if (!file.exists()) {
				boolean bb = file.mkdirs();
				System.out.println("创建文件夹：" + bb);
			}
			System.out.println("文件创建目录" + file.getPath());
			// 定义文件名格式并创建
			System.out.println("csvName=" + fileName);
			csvFile = File.createTempFile(fileName, ".csv", file);
			System.out.println("csvFile：" + file.getPath() + "/"
					+ csvFile.getName());
			// UTF-8使正确读取分隔符","
			csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(csvFile), "GBK"), 1024);
			// System.out.println("csvFileOutputStream：" + csvFileOutputStream);
			// 写入文件头部
			for (Iterator propertyIterator = map.entrySet().iterator(); propertyIterator
					.hasNext();) {
				java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator
						.next();

				csvFileOutputStream
						.write((String) propertyEntry.getValue() != null ? new String(
								((String) propertyEntry.getValue())
										.getBytes("GBK"), "GBK") : "");
				if (propertyIterator.hasNext()) {
					csvFileOutputStream.write(",");
				}
				// System.out.println(new String(((String)
				// propertyEntry.getValue()).getBytes("GBK"), "GBK"));
			}
			csvFileOutputStream.write("\r\n");
			// 写入文件内容
			for (Iterator iterator = exportData.iterator(); iterator.hasNext();) {
				Object row = (Object) iterator.next();
				for (Iterator propertyIterator = map.entrySet().iterator(); propertyIterator
						.hasNext();) {
					java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator
							.next();
					csvFileOutputStream
							.write((String) BeanUtils.getProperty(
									row,
									((String) propertyEntry.getKey()) != null ? (String) propertyEntry
											.getKey() : ""));
					if (propertyIterator.hasNext()) {
						csvFileOutputStream.write(",");
					}
				}
				if (iterator.hasNext()) {
					csvFileOutputStream.write("\r\n");
				}
			}
			csvFileOutputStream.flush();
			System.out
					.println("---------------------CSV生成成功-------------------------");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				csvFileOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return csvFile;
	}

}

package com.buyauto.util.method;

import java.io.File;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.buyauto.util.pojo.FileModel;

public class UploadifyUtils {

	public static FileModel uploadFiles(String preId, String savePath, Map<String, MultipartFile> fiLeMap) {
		// 上传文件
		// 附件模型对象
		FileModel fm = new FileModel();
		try {
			File file = new File(savePath);
			// 判断文件夹是否存在,如果不存在则创建文件夹
			makeDir(file);
			if (fiLeMap != null) {
				for (Map.Entry<String, MultipartFile> entity : fiLeMap.entrySet()) {
					MultipartFile f = entity.getValue();
					if (f != null && !f.isEmpty()) {
						Long id = KeyUtil.generateDBKey();
						String ext = UploadifyUtils.getFileExt(f.getOriginalFilename());// 获取文件后缀
						// 保存文件
						File newFile = new File(savePath + "/" + id + "." + ext);
						f.transferTo(newFile);
						fm.setPreid(preId);
						fm.setFileName(f.getOriginalFilename());
						fm.setName(id + "." + ext);
						fm.setFilePath(savePath);// 保存路径
						fm.setExt(ext);
						fm.setSize(f.getSize());

					}
				}
			}
			return fm;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void makeDir(File dir) {
		String destDirName = dir.getPath();
		if (dir.exists()) {// 判断目录是否存在
			System.out.println("创建目录失败，目标目录已存在！");
		}
		if (!destDirName.endsWith(File.separator)) {// 结尾是否以"/"结束
			destDirName = destDirName + File.separator;
		}
		if (dir.mkdirs()) {// 创建目标目录
			System.out.println("创建目录成功！" + destDirName);
		} else {
			System.out.println("创建目录失败！");
		}
	}

	public static String getFileExt(String fileName) {
		String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
		return prefix;
	}
}

package com.buyauto.util.pojo;

public class FileModel {

	String preid;
	String fileName;
	String name;
	String filePath;
	String ext;
	Long size;

	public String getPreid() {
		return preid;
	}

	public void setPreid(String preid) {
		this.preid = preid;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "FileModel [preid=" + preid + ", fileName=" + fileName + ", name=" + name + ", filePath=" + filePath
				+ ", ext=" + ext + ", size=" + size + "]";
	}

	public FileModel(String preid, String fileName, String name, String filePath, String ext, Long size) {
		super();
		this.preid = preid;
		this.fileName = fileName;
		this.name = name;
		this.filePath = filePath;
		this.ext = ext;
		this.size = size;
	}

	public FileModel() {
		super();
	}

}

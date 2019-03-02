package com.yr.entity;

/**
 * 实体类(用于将它们装到一起)
 * 
 * @author zxy
 *
 *  2018年6月14日 下午9:59:47
 *
 */
public class FileCollect {
	private String fileSize;
	private String fileUrl;
	private String fileName;
	private String fileContent;

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String string) {
		this.fileSize = string;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileContent() {
		return fileContent;
	}

	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}

	@Override
	public String toString() {
		return "FileCollect [fileSize=" + fileSize + ", fileUrl=" + fileUrl + ", fileName=" + fileName
				+ ", fileContent=" + fileContent + "]";
	}

}

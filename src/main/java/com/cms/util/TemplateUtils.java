/*
 * 
 * 
 * 
 */
package com.cms.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import com.cms.TemplateFile;
import com.jfinal.kit.PathKit;

/**
 * Utils - 模板
 * 
 * 
 * 
 */
public class TemplateUtils {

	
	/**
	 * 获取模板文件
	 * 
	 * @return 模板文件
	 */
	public static List<TemplateFile> getTemplateFiles(String directory){
		String templatePath = PathKit.getWebRootPath()+"/templates/"+directory;
		List<TemplateFile> templateFiles=new ArrayList<TemplateFile>();
		File file =new File(templatePath);
		if(!file.exists()){
			file.mkdirs();
		}
		File [] files=file.listFiles();
		for(int i=0;i<files.length;i++){
			TemplateFile templateFile=new TemplateFile();
			templateFile.setName(files[i].getName());
			templateFile.setSize(files[i].length()/ 1024 +"KB");
			templateFile.setModifyDate(DateFormatUtils.format(files[i].lastModified(), "yyyy-MM-dd HH:mm:ss"));
			if(files[i].isDirectory()){
				templateFile.setIsDirectory(true);
			}else{
				templateFile.setIsDirectory(false);
				templateFile.setType(FilenameUtils.getExtension(files[i].getName()));
			}
			templateFiles.add(templateFile);
		}
		return templateFiles;
	}
	
	/**
	 * 读取模板文件内容
	 * 
	 * @param templatePath
	 *            模板路径
	 * @return 模板文件内容
	 */
	public static String read(String templatePath) {
		try {
			String path = PathKit.getWebRootPath()+"/templates/"+templatePath;
			File templateFile = new File(path);
			return FileUtils.readFileToString(templateFile, "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} 
	}

	/**
	 * 写入模板文件内容
	 * 
	 * @param templatePath
	 *            模板路径
	 * @param content
	 *            模板文件内容
	 */
	public static void write(String templatePath, String content) {
		try {
			String path = PathKit.getWebRootPath()+"/templates/"+templatePath;
			File file = new File(path);
			FileUtils.writeStringToFile(file, content, "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} 
	}
}
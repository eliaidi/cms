package com.wk.cms.utils;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.wk.cms.service.exception.FileParseException;

public class FileUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

	public static String[] parseTxt2Arr(MultipartFile file) throws FileParseException {
		
		if(!file.getOriginalFilename().toLowerCase().endsWith(".txt")){
			throw new FileParseException("解析文件失败！文件必须是txt文件");
		}
		
		try {
			String fileCon = getFileContent(file);
			
			return fileCon.split("\r\n");
		} catch (IOException e) {
			throw new FileParseException("获取文件内容失败！",e);
		}
	}

	private static String getFileContent(MultipartFile file) throws IOException {
		
		return new String(file.getBytes());
	}

}

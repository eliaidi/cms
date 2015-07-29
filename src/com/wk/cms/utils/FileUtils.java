package com.wk.cms.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.wk.cms.model.TempFile;
import com.wk.cms.service.exception.FileParseException;
import com.wk.cms.service.exception.ServiceException;

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

	public static void witeFile(TempFile tf, String pDir) throws ServiceException  {
		
		File folder = new File(pDir);
		if(!folder.exists()){
			folder.mkdirs();
		}
		try {
			FileOutputStream fos = new FileOutputStream(new File(folder, tf.getFile().getFileName()));
			InputStream is = tf.getFile().getContent().getBinaryStream();
			byte[] buff = new byte[10000];
			int len;
			
			while((len = is.read(buff))!=-1){
				fos.write(buff, 0, len);
			}
			fos.close();
			is.close();
		} catch (Exception e) {
			throw new ServiceException(e.getMessage(),e);
		}
	}

}

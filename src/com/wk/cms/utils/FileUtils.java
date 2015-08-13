package com.wk.cms.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipException;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.wk.cms.cfg.SysCfg;
import com.wk.cms.model.TempFile;
import com.wk.cms.service.exception.FileParseException;
import com.wk.cms.service.exception.ServiceException;

public class FileUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);
	private static final String SYSTEM_TEMP_DIR_KEY = "temp_dir";

	public static String[] parseTxt2Arr(MultipartFile file,String encode) throws FileParseException {
		
		if(!file.getOriginalFilename().toLowerCase().endsWith(".txt")){
			throw new FileParseException("解析文件失败！文件必须是txt文件");
		}
		
		try {
			String fileCon = getFileContent(file,encode);
			
			return fileCon.split("\r\n");
		} catch (IOException e) {
			throw new FileParseException("获取文件内容失败！",e);
		}
	}

	private static String getFileContent(MultipartFile file,String encode) throws IOException {
		
		return new String(file.getBytes(),encode);
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

	public static void writeFile(String content, String fileName, String dir) throws ServiceException {

		File folder = new File(dir);
		if(!folder.exists()){
			folder.mkdirs();
		}
		try {
			FileOutputStream fos = new FileOutputStream(new File(folder,fileName));
			
			fos.write(content.getBytes("UTF-8"));
			fos.close();
		} catch (Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}

	public static File saveMultiFileToTemp(MultipartFile f) throws ServiceException {
		
		String fileExt = getFileExt(f.getOriginalFilename());
		File desFile = new File(getTempDir(), new Date().getTime()+(fileExt==null?"":"."+fileExt));
		
		try {
			FileOutputStream fos = new FileOutputStream(desFile);
			InputStream is = f.getInputStream();
			byte[] buff = new byte[2000];
			int len;
			while((len = is.read(buff))!=-1){
				fos.write(buff, 0, len);
			}
			
			is.close();
			fos.close();
		} catch (Exception e) {
			throw new ServiceException("写入临时文件失败！",e);
		}
		
		return desFile;
	}

	public static File getTempDir() throws ServiceException {
		String tempDir = SysCfg.getProperty(SYSTEM_TEMP_DIR_KEY);
		if(!StringUtils.hasLength(tempDir)){
			tempDir = System.getProperty("java.io.tmpdir");
		}
		File dir = new File(tempDir);
		if(!dir.exists()){
			dir.mkdirs();
		}
		return dir;
	}

	public static String getFileExt(String fileName) {
		return StringUtils.hasLength(fileName)?(fileName.indexOf(".")>=0?fileName.substring(fileName.lastIndexOf(".")+1):null):null;
	}

	public static void unZip(ZipFile zf,File folder) throws ServiceException {
		try {
			if(!folder.exists()||!folder.isDirectory()){
				folder.mkdirs();
			}
			Enumeration<ZipEntry> entrys = zf.getEntries();
			while(entrys.hasMoreElements()){
				ZipEntry ze = entrys.nextElement();
				File f = new File(folder, ze.getName());
				if(ze.isDirectory()){
					if(!f.exists()) f.mkdirs();
				}else{
					writeFile(zf.getInputStream(ze), f);
				}
			}
			
		} catch (Exception e) {
			throw new ServiceException("解压文件失败！", e);
		} finally{
			
			ZipFile.closeQuietly(zf);
		}
	}
	
	public static void writeFile(InputStream inputStream, File f) throws ServiceException {
	
		try {
			FileOutputStream fos = new FileOutputStream(f);
			byte[] buff = new byte[2000];
			int len ;
			
			while((len = inputStream.read(buff))!=-1){
				fos.write(buff, 0, len);
			}
			
			inputStream.close();
			fos.close();
		} catch (Exception e) {
			throw new ServiceException("写文件失败！", e);
		}
		
	}

	public static void delete(java.io.File tempFile) {
		if(tempFile.exists()){
			if(tempFile.isDirectory()){
				File[] children = tempFile.listFiles();
				if(children.length==0){
					tempFile.delete();
				}else{
					for(File f : children){
						delete(f);
					}
				}
			}
			tempFile.delete();
		}
	}

	public static String getFileCon(java.io.File tempfolder) throws ServiceException {
		
		if(tempfolder==null||!tempfolder.exists()){
			throw new FileParseException("文件不存在！"+tempfolder);
		}
		try {
			FileInputStream fis = new FileInputStream(tempfolder);
			StringBuffer sb = new StringBuffer();
			byte[] buff = new byte[2000];
			int len;
			while((len = fis.read(buff))!=-1){
				sb.append(new String(buff,0,len,System.getProperty("file.encoding")));
			}
			fis.close();
			
			return sb.toString();
		} catch (Exception e) {
			throw new FileParseException("获取文件内容失败！", e);
		} 
		
	}
	
	public static String getFileCon(java.io.File tempfolder,String charset) throws ServiceException {
		
		if(tempfolder==null||!tempfolder.exists()){
			throw new FileParseException("文件不存在！"+tempfolder);
		}
		try {
			FileInputStream fis = new FileInputStream(tempfolder);
			StringBuffer sb = new StringBuffer();
			byte[] buff = new byte[2000];
			int len;
			while((len = fis.read(buff))!=-1){
				sb.append(new String(buff,0,len,charset));
			}
			fis.close();
			
			return sb.toString();
		} catch (Exception e) {
			throw new FileParseException("获取文件内容失败！", e);
		} 
		
	}

	public static byte[] getBytes(java.io.File eFile) throws FileParseException {
		
		try {
			FileInputStream fis = new FileInputStream(eFile);
			byte[] buff = new byte[fis.available()];
			fis.read(buff);
			fis.close();
			return buff;
		} catch (Exception e) {
			throw new FileParseException("解析文件失败！"+eFile,e);
		}
	}

	public static String getFileCon(URL url, int timeout,String encode) throws FileParseException {
		
		try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setReadTimeout(timeout);
			
			InputStream is = connection.getInputStream();
			byte[] buff = new byte[2000];
			int len;
			StringBuilder sb = new StringBuilder();
			
			while((len = is.read(buff))!=-1){
				sb.append(new String(buff,0,len,encode));
			}
			is.close();
			return sb.toString();
		} catch (IOException e) {
			throw new FileParseException("获取远程文件失败！", e);
		}
	}

	public static byte[] getBytes(MultipartFile f) throws FileParseException {

		try {
			InputStream is = f.getInputStream();
			byte[] buff = new byte[is.available()];
			
			is.read(buff);
			is.close();
			return buff;
		} catch (IOException e) {
			throw new FileParseException("获取上传文件内容失败！", e);
		}
	}

	public static void writeFile(com.wk.cms.model.File f, java.io.File dir) throws ServiceException {
		File destFile = new File(dir, f.getFileName());
		try {
			writeFile(f.getContent().getBinaryStream(), destFile);
		} catch (SQLException e) {
			throw new ServiceException("写文件失败！", e);
		}
	}

	public static java.io.File zip(java.io.File localFile) throws FileParseException {
		
		if(!localFile.exists()){
			throw new FileParseException("文件不存在！"+localFile);
		}
		File zipFile = new File(localFile.getParentFile(), localFile.getName()+".zip");
		try {
			ZipOutputStream zos = new ZipOutputStream(zipFile);
			zip(zos,localFile,localFile.getName());
			zos.close();
		} catch (IOException e) {
			throw new FileParseException("压缩文件失败！"+localFile, e);
		}
		return zipFile;
	}

	private static void zip(ZipOutputStream zos, File localFile,String base) {
		
		if(localFile.isDirectory()){
			File[] files = localFile.listFiles();
			for(File f : files){
				zip(zos,f,base+File.separator+f.getName());
			}
		}else{
			try {
				zos.putNextEntry(new ZipEntry(base));
				FileInputStream fis = new FileInputStream(localFile);
				byte[] buff = new byte[10000];
				int len;
				while((len = fis.read(buff))!=-1){
					zos.write(buff, 0, len);
				}
				fis.close();
			} catch (IOException e) {
				LOGGER.error("添加压缩文件发生错误！"+localFile,e);
			}
		}
	}
	
}

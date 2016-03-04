package com.wk.cms.model;

import java.lang.reflect.Field;
import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.shiro.SecurityUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wk.cms.model.annotations.Show;
import com.wk.cms.model.annotations.ShowArea;
import com.wk.cms.mvc.json.BlobJsonSerializer;
import com.wk.cms.service.exception.FileParseException;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.CommonUtils;
import com.wk.cms.utils.FileUtils;
import com.wk.cms.utils.MyBlob;

@Entity
@Table(schema="CMS",name="Sys_File")
public class File {

	@Id
	private String id;
	private String fileName;
	private String fileExt;
	private String encode = "UTF-8";
	
	@Lob
	@Show(ShowArea.Detail)
	@JsonSerialize(using=BlobJsonSerializer.class)
	private Blob content;
	private long fileSize;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date crTime;
	
	private String crUser;

	public File(){}
	public File(String fileName, long size, String fileExt, MyBlob content, String encode2) {
		
		this.fileName = fileName;
		this.fileExt = fileExt;
		this.fileSize = size;
		this.content = content;
		this.crTime = new Date();
		this.crUser = SecurityUtils.getSubject().getPrincipal().toString();
		this.encode = encode2;
	}

	public File(Blob content) {
		this.content = content;
	}
	public File(String id ,String remoteUrl) throws ServiceException   {
		
		byte[] bytes = CommonUtils.getBytesFromUrl(remoteUrl);
		this.fileName = remoteUrl.substring(remoteUrl.lastIndexOf("/")+1);
		this.fileExt = FileUtils.getFileExt(this.fileName);
		this.fileSize = bytes.length;
		this.content = new MyBlob(bytes);
		this.crTime = new Date();
		this.crUser = SecurityUtils.getSubject().getPrincipal().toString();
		this.id = id;
	}
	public File(String id, java.io.File eFile, String encode2) throws FileParseException {
		this.id = id;
		this.crTime = new Date();
		this.crUser = SecurityUtils.getSubject().getPrincipal().toString();
		
		byte[] bytes = FileUtils.getBytes(eFile);
		this.fileName = eFile.getName();
		this.fileExt = FileUtils.getFileExt(this.fileName);
		this.fileSize = bytes.length;
		this.content = new MyBlob(bytes);
		this.encode = encode2;
	}
	public File(String id, MultipartFile f,String encode) throws FileParseException {

		this.id = id;
		this.fileName = f.getOriginalFilename();
		this.fileExt = FileUtils.getFileExt(this.fileName);
		byte[] bytes = FileUtils.getBytes(f);
		this.content = new MyBlob(bytes);
		this.fileSize = bytes.length;
		this.crTime = new Date();
		this.crUser = SecurityUtils.getSubject().getPrincipal().toString();
		this.encode = encode;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEncode() {
		return encode;
	}
	public void setEncode(String encode) {
		this.encode = encode;
	}
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileExt() {
		return fileExt;
	}

	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}

	public Blob getContent() {
		return content;
	}

	public void setContent(Blob content) {
		this.content = content;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public Date getCrTime() {
		return crTime;
	}

	public void setCrTime(Date crTime) {
		this.crTime = crTime;
	}

	public Map<String, String> toJsonMap() throws ServiceException {

		try {
			Map<String,String> m = new HashMap<String, String>();
			Class<File> clazz = (Class<File>) this.getClass();
			Field[] fields = this.getClass().getDeclaredFields();
			BlobJsonSerializer blobJsonSerializer = new BlobJsonSerializer(this.encode);
			//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for(Field f: fields){
				Object val = f.get(this);
				String valStr = "";
				if(val!=null){
					if(CommonUtils.contains(val.getClass().getInterfaces(), Blob.class)){
						if(!FileUtils.isPic(this.getFileName())){
							valStr = blobJsonSerializer.format((Blob) val);
						}
						
					}else if(Date.class.isAssignableFrom(val.getClass())){
						valStr = String.valueOf(((Date)val).getTime());
					}else{
						valStr = val.toString();
					}
				}
				m.put(f.getName(), valStr);
			}
			return m;
		} catch (Exception e) {
			throw new ServiceException("生成File的JSON Map失败！", e);
		} 
	}
	public String getCrUser() {
		return crUser;
	}
	public void setCrUser(String crUser) {
		this.crUser = crUser;
	}
	
}

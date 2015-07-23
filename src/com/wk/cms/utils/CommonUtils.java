package com.wk.cms.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.wk.cms.exception.ParseException;
import com.wk.cms.model.Document;
import com.wk.cms.model.File;
import com.wk.cms.model.TempFile;
import com.wk.cms.model.Template;
import com.wk.cms.service.exception.FileParseException;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.parser.NeteaseDocParser;
import com.wk.cms.utils.parser.RemoteDocParser;

public class CommonUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtils.class);

	public static boolean isEmpty(List<?> list) {
		
		if(list==null||list.size()==0){
			return true;
		}
		return false;
	}

	public static <T> Set<T> list2Set(List<T> list) {
		
		if(isEmpty(list)) return null;
		
		Set<T> set = new HashSet<T>();
		for(T t : list){
			set.add(t);
		}
		return set;
	}

	public static boolean isEmpty(Object[] siteNames) {

		if(siteNames==null||siteNames.length==0){
			return true;
		}
		return false;
	}

	public static String getContent(InputStream inputStream) throws IOException {
		
		StringBuffer s = new StringBuffer();
		
		byte[] buff = new byte[10000];
		int len ;
		
		while((len = inputStream.read(buff))!=-1){
			s.append(new String(buff,0,len));
		}
		return s.toString();
	}

	public static Document loadRemoteDoc(String url) throws  ParseException {
		
		RemoteDocParser docParser = getDocParserByUrl(url);
		return docParser.parse();
	}

	private static RemoteDocParser getDocParserByUrl(String url) throws ParseException {
		
		URL u;
		try {
			u = new URL(url);
		} catch (MalformedURLException e) {
			throw new ParseException("实例化URL失败", e);
		}
		String host = u.getHost();
		
		if(host.toLowerCase().indexOf("163.com")>=0){
			return new NeteaseDocParser(url);
		}
		throw new ParseException("暂不支持解析此网站("+host+")文档");
	}

	public static String list2String(List<String> fields, String str) {
		
		if(isEmpty(fields)) return null;
		if(!StringUtils.hasLength(str)) str = ",";
		
		StringBuffer sb = new StringBuffer();
		for(String f : fields){
			sb.append(str+f);
		}
		sb.delete(0, str.length()-1);
		return sb.toString();
	}

	public static boolean contains(Object[] interfaces, Object class1) {
		
		if(isEmpty(interfaces)) return false;
		
		for(Object o : interfaces){
			if(o.equals(class1)){
				return true;
			}
		}
		return false;
	}

	public static String readStringFromIS(InputStream is,String charsetName) throws IOException {
		
		StringBuffer sb = new StringBuffer();
		
		byte[] buff = new byte[10000];
		int len;
		
		while((len = is.read(buff))!=-1){
			sb.append(new String(buff,0,len,charsetName));
		}
		
		is.close();
		return sb.toString();
	}

	public static String getContentFromUrl(String url) throws FileParseException {

		try {
			org.jsoup.nodes.Document document = Jsoup.parse(new URL(url), 5000);
			
			return document.html();
		} catch (Exception e) {
			throw new FileParseException("获取远程文档失败！！");
		} 
	}

	public static String getRemoteAttrNameByTagName(String tagName) throws ServiceException {
		
		if(tagName.equalsIgnoreCase("script")||tagName.equalsIgnoreCase("img")){
			return "src";
		}else if(tagName.equalsIgnoreCase("link")){
			return "href";
		}
		
		throw new ServiceException("不支持此标签【"+tagName+"】");
	}

	public static byte[] getBytesFromUrl(String remoteUrl) throws ServiceException  {
		
		try {
			URL url = new URL(remoteUrl);
			URLConnection connection = url.openConnection();
			InputStream is = connection.getInputStream();
			byte[] buff = new byte[is.available()];
			is.read(buff);
			is.close();
			return buff;
		} catch (Exception e) {
			throw new ServiceException("获取远程文件失败！",e);
		} 
	}

	public static <T> T findFromList(List<T> siteFiles,
			String[] fieldNames, Object[] fieldValues) throws ServiceException {
		
		if(isEmpty(siteFiles)) return null;
		
		for(T t : siteFiles){
			Object value = t;
			boolean isMatch = true;
			
			for(int i = 0;i<fieldNames.length;i++){
				String fieldName = fieldNames[i];
				String[] fieldLeval = fieldName.split("\\.");
				for(String f : fieldLeval){
					value = getFieldValue(value,f);
				}
				
				isMatch = isMatch&&value.equals(fieldValues[i]);
				if(!isMatch){
					break;
				}
			}
			if(isMatch){
				return t;
			}
		}
		
		
		return null;
	}

	private static Object getFieldValue(Object obj, String field) throws ServiceException {
		
		Class<?> clazz = obj.getClass();
		try {
			Method method = clazz.getDeclaredMethod("get"+field.substring(0, 1).toUpperCase()+field.substring(1));
			
			return method.invoke(obj);
		} catch (Exception e) {
			throw new ServiceException("获取属性值失败！！", e);
		} 
	}

	/**
	 * 下载CSS文件里面的模板附件
	 * @param tf	Css文件
	 * @param val	Css文件路径	用于拼接模板附件的远程地址
	 * @param template	所属模板	
	 * @param siteFiles 站点下已存在的模板附件，如果css文件中包含已存在的模板附件则不进行下载，
	 * @return
	 * @throws ServiceException
	 */
	public static List<TempFile> downLoadCssInnerFiles(File tf, String val,Template template, List<TempFile> siteFiles) throws ServiceException {
		
		try {
			List<TempFile> tempFiles = new ArrayList<TempFile>();
			Set<Template > initTpls = new HashSet<Template>();
			initTpls.add(template);
			
			String fileCon = new String(tf.getContent().getBytes(0, (int) tf.getContent().length()),"UTF-8");
			StringBuffer newCon = new StringBuffer();
			Pattern p = Pattern.compile("url\\(['|\"]?(\\S+)['|\"]?\\);");
			Matcher m = p.matcher(fileCon);
			while(m.find()){
				String url = m.group(1);
				String fileName = url.indexOf("/")>0?url.substring(url.lastIndexOf("/")+1):url;
				LOGGER.debug("找到CSS文件内模板附件【"+fileName+"】");
				m.appendReplacement(newCon, "url("+fileName+")");
				
				TempFile tempFile = CommonUtils.findFromList(siteFiles,new String[]{"file.fileName"},new Object[]{fileName});
				if(tempFile==null){
					
					String remoteUrl = val.substring(0, val.lastIndexOf("/")+1);remoteUrl = remoteUrl + url;
					LOGGER.debug("模板附件【"+fileName+"】不存在，开始下载【"+remoteUrl+"】~~");
					tempFiles.add(new TempFile(UUID.randomUUID().toString(), initTpls, new File(UUID.randomUUID().toString(),remoteUrl), template.getSite()));
				}else{
					LOGGER.debug("模板附件【"+fileName+"】已存在，取消下载，直接添加进模板!");
					tempFiles.add(tempFile);
				}
				
			}
			m.appendTail(newCon);
			return tempFiles;
		} catch (Exception e) {
			throw new ServiceException("下载Css文件内关联文件失败！！", e);
		} 
	}

	public static void main(String[] args) {
		
		try {
			downLoadCssInnerFiles(new File(null, "http://localhost:8888/portal/web/themes/default/css/lin.css"), null, null,null);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

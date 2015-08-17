package com.wk.cms.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.pinyin4j.PinyinHelper;

import org.hibernate.mapping.OneToMany;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.wk.cms.exception.ParseException;
import com.wk.cms.model.Document;
import com.wk.cms.model.File;
import com.wk.cms.model.TempFile;
import com.wk.cms.model.Template;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.parser.NeteaseDocParser;
import com.wk.cms.utils.parser.RemoteDocParser;

public class CommonUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtils.class);

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
		
		return getContent(inputStream, "UTF-8");
	}
	
	public static String getContent(InputStream inputStream,String charsetName) throws IOException {
		
		StringBuffer s = new StringBuffer();
		
		byte[] buff = new byte[10000];
		int len ;
		
		while((len = inputStream.read(buff))!=-1){
			s.append(new String(buff,0,len,charsetName));
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
			
			List<Byte> bytes = new ArrayList<Byte>();
			int i;
			while((i = is.read())!=-1){
				bytes.add((byte) i);
			}
			is.close();
			
			byte[] rs = new byte[bytes.size()];
			for(int j=0;j<bytes.size();j++){
				rs[j] = bytes.get(j);
			}
			
			return rs;
			
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

	/**
	 * 获取对象属性值，仅支持对象的直接属性
	 * @param obj
	 * @param field
	 * @return
	 * @throws ServiceException
	 */
	public static Object getFieldValue(Object obj, String field) throws ServiceException {
		
		Class<?> clazz = obj.getClass();
		try {
			Method method = clazz.getDeclaredMethod("get"+field.substring(0, 1).toUpperCase()+field.substring(1));
			
			return method.invoke(obj);
		} catch (Exception e) {
			throw new ServiceException("获取属性值失败！！", e);
		} 
	}
	
	/**
	 * 获取对象属性值，支持a.b.c形式
	 * @param obj
	 * @param field
	 * @return
	 * @throws ServiceException
	 */
	public static Object getDeepFieldValue(Object obj, String field) throws ServiceException {
		
		String[] fields = field.split("\\.");
		for(String f : fields){
			obj = getFieldValue(obj,f);
		}
		return obj;
	}
	
	/**
	 * 下载CSS文件里面的模板附件
	 * @param tf	Css文件
	 * @param val	Css文件路径	用于拼接模板附件的远程地址
	 * @param template	所属模板	
	 * @param siteFiles 站点下已存在的模板附件，如果css文件中包含已存在的模板附件则不进行下载，
	 * @param isRemote 是否远程文件
	 * @return
	 * @throws ServiceException
	 */
	public static List<TempFile> downLoadCssInnerFiles(File tf, String val,Template template, List<TempFile> siteFiles,CallBack cb,boolean isRemote) throws ServiceException {
		try {
			List<TempFile> tempFiles = new ArrayList<TempFile>();
			Set<Template > initTpls = new HashSet<Template>();
			initTpls.add(template);
			
			String fileCon = new String(tf.getContent().getBytes(0, (int) tf.getContent().length()),"UTF-8");
			StringBuffer newCon = new StringBuffer();
			Pattern p = Pattern.compile("url\\(['|\"]?([^\\)|\\s]+)['|\"]?\\)");
			Matcher m = p.matcher(fileCon);
			while(m.find()){
				String url = m.group(1);
				String fileName = url.indexOf("/")>0?url.substring(url.lastIndexOf("/")+1):url;
				LOGGER.debug("找到CSS文件内模板附件【"+fileName+"】");
				try {
					m.appendReplacement(newCon, "url("+fileName+")");
					TempFile tempFile = CommonUtils.findFromList(siteFiles,new String[]{"file.fileName"},new Object[]{fileName});
					if(tempFile==null){
						String remoteUrl = val.substring(0, val.lastIndexOf("/")+1);remoteUrl = remoteUrl + url;
						if(isRemote){
							LOGGER.debug("模板附件【"+fileName+"】不存在，开始下载【"+remoteUrl+"】~~");
							tempFile = new TempFile(UUID.randomUUID().toString(), initTpls, new File(UUID.randomUUID().toString(),remoteUrl), template.getSite());
						}else{
							tempFile = new TempFile(UUID.randomUUID().toString(), initTpls, new File(UUID.randomUUID().toString(),new java.io.File(remoteUrl),null), template.getSite());
						}
					}else{
						LOGGER.debug("模板附件【"+fileName+"】已存在，取消下载，直接添加进模板!");
					}
					tempFiles.add(tempFile);
					tempFile.getTemplates().add(template);
				} catch (Exception e) {
					LOGGER.error("导入css文件内模板附件失败！URL="+url,e);
				}
			}
			m.appendTail(newCon);
			if(cb!=null){
				cb.doCallBack(new Object[]{newCon.toString()});
			}
			return tempFiles;
		} catch (Exception e) {
			throw new ServiceException("下载Css文件内关联文件失败！！", e);
		} 
	}

	/**
	 * @param tf
	 * @param val
	 * @param template
	 * @param siteFiles
	 * @param cb
	 * @return
	 * @throws ServiceException
	 */
	public static List<TempFile> downLoadCssInnerFiles(File tf, String val,Template template, List<TempFile> siteFiles,CallBack cb) throws ServiceException {
		
		return downLoadCssInnerFiles(tf, val, template, siteFiles, cb, true);
	}

	public static void main(String[] args) {
		
		try {
			downLoadCssInnerFiles(new File(null, "http://localhost:8888/portal/web/themes/default/css/lin.css"), null, null,null,null);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getAppPath(String s) {
		String fullPath = CommonUtils.class.getResource("/").getPath();
		return fullPath.substring(0, fullPath.lastIndexOf(s)+s.length());
	}

	public static String join(String[] idsArr, String sp) {
		
		StringBuffer sb = new StringBuffer();
		
		for(String id : idsArr){
			if(id==null) continue;
			sb.append(sp+id);
		}
		if(sb.length()>0){
			sb.delete(0, 1);
		}
		return sb.toString();
	}

	public static boolean in(Object[] os, Object o) {
		
		for(Object obj : os){
			if(obj.equals(o))
				return true;
		}
		return false;
	}

	public static String join(Set<String> keySet, String sp) {
		
		StringBuffer sb = new StringBuffer();
		for(String o : keySet){
			sb.append(sp+o);
		}
		if(sb.length()>0){
			sb.delete(0, 1);
		}
		return sb.toString();
	}

	public static String getFirstWordOf(String chnlName) {
		
		StringBuilder r = new StringBuilder();
		int len = chnlName.length();
		for(int i=0;i<len;i++){
			String cStr = chnlName.substring(i, i+1);
			if(Pattern.matches("[\u4e00-\u9fa5]",cStr)){
				String py = PinyinHelper.toHanyuPinyinStringArray(chnlName.charAt(i))[0];
				r.append(py.subSequence(0, 1));
			}else{
				r.append(cStr);
			}
		}
		return r.toString();
	}

	public static <T> List<T> removeFrom(T[] sArr, T id) {
		
		List<T> sList = new ArrayList<T>();
		for(int i=0;i<sArr.length;i++){
			T s = sArr[i];
			if(s==null) continue;
			if(!s.equals(id)) sList.add(s);
		}
		return sList;
	}

	public static String join(List<String> list, String sp) {
		StringBuilder sb = new StringBuilder();
		
		for(String s : list){
			sb.append(sp+s);
		}
		if(sb.length()>0){
			sb.delete(0, sp.length());
		}
		return sb.toString();
	}

	public static <T> List<T> push(List<T> templates, T[] arr) {
		if(templates == null){
			templates = new ArrayList<T>();
		}
		if(!CommonUtils.isEmpty(arr)){
			for(T t : arr){
				if(t==null||t.toString().equals("")) continue;
				templates.add(t);
			}
		}
		return templates;
	}

	public static <T> boolean isEmpty(Collection<T> value) {
		return value==null||value.size()==0;
	}

}

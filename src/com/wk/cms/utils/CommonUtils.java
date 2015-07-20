package com.wk.cms.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.validator.constraints.NotEmpty;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;

import com.wk.cms.exception.ParseException;
import com.wk.cms.model.Document;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.parser.NeteaseDocParser;
import com.wk.cms.utils.parser.RemoteDocParser;

public class CommonUtils {

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

}

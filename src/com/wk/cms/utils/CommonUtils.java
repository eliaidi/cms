package com.wk.cms.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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

	public static boolean isEmpty(String[] siteNames) {

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

}

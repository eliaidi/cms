package com.wk.cms.publish.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.CommonUtils;
import com.wk.cms.utils.PublishUtils;

public class ParseUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(ParseUtils.class);
	
	public static String parse(Object obj, String content) {
		
		Pattern p = Pattern.compile("\\$\\{\\s*([a-z0-9_]+)\\s*\\}",Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(content);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			String fName = m.group(1);
			String rv = "";
			if("_url".equalsIgnoreCase(fName)){
				//rv = PublishUtils.getUrl(obj);
			}else{
				try {
					rv = CommonUtils.getDeepFieldValue(obj,fName).toString();
				} catch (ServiceException e) {
					LOGGER.error("获取对象属性值失败！",e);
				}
			}
			m.appendReplacement(sb, rv);
		}
		m.appendTail(sb);
		return sb.toString();
	}
}

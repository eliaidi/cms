package com.wk.cms.cfg;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.wk.cms.service.exception.ServiceException;

public class SysCfg {

	private static final String SYS_CFG_FILE = "/system-cfg.properties";
	private static final String SUPPORT_TEMPLATE_EXT_KEY = "support_parse_template_ext";
	private static final String SUPPORT_PAESE_TAG_KEY = "support_parse_template_tag";
	private static final Logger LOGGER = LoggerFactory.getLogger(SysCfg.class);
	private static Properties pro = new Properties();
	
	static{
		try {
			pro.load(SysCfg.class.getResourceAsStream(SYS_CFG_FILE));
		} catch (IOException e) {
			LOGGER.error("加载系统配置文件失败！"+SYS_CFG_FILE,e);
		}
	}

	public static String getProperty(String key) {
		return pro.getProperty(key);
	}

	public static String[] getSupportTemplateExt() {
		
		String s = pro.getProperty(SUPPORT_TEMPLATE_EXT_KEY);
		if(!StringUtils.hasLength(s)){
			return new String[]{"htm","html"};
		}
		return s.split(";");
	}

	public static Map<String, String> getSupportParseTags() throws ServiceException {

		String s = pro.getProperty(SUPPORT_PAESE_TAG_KEY);
		if(!StringUtils.hasLength(s)){
			throw new ServiceException("系统配置错误！必填项为空"+SUPPORT_PAESE_TAG_KEY);
		}
		Map<String, String> m = new HashMap<String, String>();
		String[] sArr = s.split(";");
		for(String str : sArr){
			String[] strArr = str.split(":");
			m.put(strArr[0], strArr[1]);
		}
		return m;
	}

}

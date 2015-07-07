package com.wk.cms.utils.parser;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import com.wk.cms.exception.ParseException;
import com.wk.cms.model.Document;

public class SinaDocParser extends RemoteDocParser {

	public SinaDocParser(String url) {
		super(url);
	}

	@Override
	public Document parse() throws ParseException {
		try {
			org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
			Elements titleEs = doc.select("#artibodyTitle");
			Elements conEs = doc.select("#articleContent #artibody");
			
			return new Document(titleEs.get(0).html(),conEs.get(0).html());
		} catch (Exception e) {
			throw new ParseException("解析文档失败",e);
		}
	}

}

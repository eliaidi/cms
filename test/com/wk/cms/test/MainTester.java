package com.wk.cms.test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.security.auth.Subject;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.util.ThreadContext;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.wk.cms.model.User;
import com.wk.cms.service.IDocumentService;
import com.wk.cms.service.IUserService;
import com.wk.cms.service.exception.ServiceException;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:application-config.xml",
		"classpath:mvc-config.xml" })
public class MainTester {

	public class ParseSinaNewsListTask implements Runnable {

		private final Logger log = LoggerFactory.getLogger(ParseSinaNewsListTask.class);
		private static final int TIMEOUT = 3000;
		private String url;
		
		public ParseSinaNewsListTask(String u){
			this.url = u;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		@Override
		public void run() {
			try {
				log.info("开始解析List地址：【"+this.url+"】，当前线程：【"+Thread.currentThread()+"】");
				parseList();
			} catch (Exception e) {
				log.error("解析List失败！", e);
			}
		}

		private void parseList() throws Exception {
			Document document = Jsoup.parse(new URL(this.url), TIMEOUT);
			Elements elements = document.select("ul.list_009 > li");
			for(int i = elements.size()-1;i>=0;i--){
				Element element = elements.get(i);
				Element aElement = element.child(0);
				String url = aElement.attr("href");
				try {
					log.info("开始解析Detail【"+url+"】");
					parseDetail(url);
				} catch (Exception e) {
					log.error("解析Detail失败!",e);
				}
			}
		}

		private void parseDetail(String url2) throws Exception {
			Document doc = Jsoup.parse(new URL(url2), TIMEOUT);
			Elements tElements = doc.select("h1#artibodyTitle");
			if(tElements.size()==0) return;
			Elements cElements = doc.select("div#artibody");
			if(cElements.size()==0) return;
			
			save(tElements.get(0).text(),cElements.get(0).html());
		}

	}

	@Autowired
	private IUserService userService;
	
	@Autowired
	private IDocumentService documentService;
	
	@Autowired
	private DefaultSecurityManager securityManager;

	@Test
	public void addUser() throws ServiceException {

		User user = new User();
		user.setUsername("admin");
		user.setPassword("admin");
		user.setTruename("administrator");

		userService.save(user);
	}

	public void save(String text, String html) throws Exception {
		com.wk.cms.model.Document doc = new com.wk.cms.model.Document(text, html);
		documentService.save(doc, "6f8aa488-205e-4da4-953f-e9c8bbc33018");
	}

	@Test
	public void loadSinaNews() {

		//ExecutorService executorService = Executors.newFixedThreadPool(10);
		int totalPage = 100;
		for (int i = 1; i <= totalPage; i++) {
			final String url = "http://roll.news.sina.com.cn/news/shxw/zqsk/index_"+i+".shtml";

			new ParseSinaNewsListTask(url).run();
			//executorService.execute(new ParseSinaNewsListTask(url));
		}
	}
	
	@Before
	public void setup(){
		
		SecurityUtils.setSecurityManager(securityManager);
		
		org.apache.shiro.subject.Subject subject = SecurityUtils.getSubject();
		subject.login(new UsernamePasswordToken("admin", "admin"));
	}
}

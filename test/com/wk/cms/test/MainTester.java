package com.wk.cms.test;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.wk.cms.model.User;
import com.wk.cms.service.IUserService;
import com.wk.cms.service.exception.ServiceException;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"classpath:application-config.xml","classpath:mvc-config.xml"})
public class MainTester {

	@Autowired
	private IUserService userService;
	@Test
	public void addUser() throws ServiceException{
		
		User user = new User();
		user.setUsername("admin");
		user.setPassword("admin");
		user.setTruename("administrator");
		
		userService.save(user);
	}
}

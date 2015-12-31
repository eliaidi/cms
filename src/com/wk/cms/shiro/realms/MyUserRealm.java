package com.wk.cms.shiro.realms;


import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.RememberMeAuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wk.cms.model.User;
import com.wk.cms.service.IUserService;

public class MyUserRealm extends AuthorizingRealm {
	private static final Logger log = LoggerFactory.getLogger(MyUserRealm.class);

	private IUserService userService;
	
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	
	public IUserService getUserService() {
		return userService;
	}
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		
		String username = principals.getPrimaryPrincipal().toString();
		
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		authorizationInfo.setRoles(userService.findRoles(username));
		authorizationInfo.setStringPermissions(userService.findPermissions(username));
		return authorizationInfo;
	}
	
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		
		String username = (String) token.getPrincipal();
		
		User user = userService.findByUserName(username);
		if(user==null){
			throw new AuthenticationException("account["+username+"] not found!");
		}
		if(token instanceof RememberMeAuthenticationToken){
			RememberMeAuthenticationToken tk = (RememberMeAuthenticationToken) token;
			log.debug("tk.isRememberMe()::"+tk.isRememberMe());
			log.debug("tk.getPrincipal()::"+tk.getPrincipal());
			log.debug("tk.getCredentials()::"+tk.getCredentials());
		}
		return new SimpleAuthenticationInfo(username, user.getPassword(), ByteSource.Util.bytes(user.getSalt()), getName());
	}

}

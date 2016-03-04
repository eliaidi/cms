package com.wk.cms.shiro.security;

import java.util.Collection;

import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;

public class MySecurityManager extends DefaultWebSecurityManager {

	@Override
	public boolean hasAllRoles(PrincipalCollection principals,
			Collection<String> roleIdentifiers) {
		if(isAdminUser(principals)) return true;
		for(String roleIdentifier : roleIdentifiers){
			if(hasRole(principals, roleIdentifier)){
				return true;
			}
		}
		return false;
	}

	private boolean isAdminUser(PrincipalCollection principals) {
		
		return "admin".equals(principals.getPrimaryPrincipal());
	}
}

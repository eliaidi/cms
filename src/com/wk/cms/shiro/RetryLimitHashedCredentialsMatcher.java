package com.wk.cms.shiro;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;

public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {

	private static final String PASSWORD_RETRY = "passwordRetryCache";
	private CacheManager cacheManager;
	public RetryLimitHashedCredentialsMatcher(CacheManager cacheManager) {
		
		this.cacheManager = cacheManager;
	}
	
	@Override
	public boolean doCredentialsMatch(AuthenticationToken token,
			AuthenticationInfo info) {
		
		UsernamePasswordToken passwordToken = (UsernamePasswordToken) token;
		Cache<String, AtomicInteger> cache = cacheManager.getCache(PASSWORD_RETRY);
		AtomicInteger num = cache.get(passwordToken.getUsername());
		if(num == null){
			num = new AtomicInteger(0);
		}
		if(num.incrementAndGet()>5){
			throw new ExcessiveAttemptsException("number of failure beyond 5!");
		}
		
		boolean result = super.doCredentialsMatch(token, info);
		
		if(result){
			cache.remove(passwordToken.getUsername());
		}else{
			cache.put(passwordToken.getUsername(), num);
		}
		return result;
	}
}

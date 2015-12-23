package com.wk.cms.utils;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;

import com.wk.cms.model.User;

public class PasswordHash {

	public static void hash(User user) {
		
		user.setSalt(new SecureRandomNumberGenerator().nextBytes().toBase64());
		String newPw = new SimpleHash("MD5", user.getPassword(), user.getSalt(), 2).toHex();
		user.setPassword(newPw);
	}

}

package com.wk.cms.utils;

import java.util.List;

public class CommonUtils {

	public static boolean isEmpty(List<?> list) {
		
		if(list==null||list.size()==0){
			return true;
		}
		return false;
	}

}
